package com.ssginc.nojam.member.controller;

import com.ssginc.nojam.member.service.MemberService;
import com.ssginc.nojam.member.vo.MemberVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequestMapping("member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;



    // localhost:8080/member/register
    // 회원가입 화면 띄우기
    @GetMapping("signup")
    public String signUp() {
        log.info("singup 요청됨.");
        return "/member/signup";
    }

    // 회원가입
    @PostMapping("signup2")
    public String signUp2(MemberVO memberVO, Model model) {
        String emailPattern = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";

        // 이메일 형식 검증
        if (!memberVO.getUserEmail().matches(emailPattern)) {
            model.addAttribute("error", "올바른 이메일 형식을 입력해주세요!");
            return "/member/signup"; // 다시 회원가입 페이지로
        }

        memberService.signUpPwEncoder(memberVO);
        System.out.println("========================================");
        System.out.println("GET request to register received...");
        System.out.println(memberVO);
        return "redirect:/";
    }

    // ID 중복 체크
    @GetMapping("checkId")
    @ResponseBody  // 뷰인 template으로 가지 않고 단순한 데이터나 json으로 보냄.
    public boolean checkId(@RequestParam String userId) {  // ID 중복 체크
        boolean result = memberService.checkId(userId);
        return result;
    }


    @GetMapping("login")
    public String login(HttpSession session) {
        System.out.println("========================================");
        System.out.println("GET request to login received...");

//        session.setAttribute("role", "head office");
//        session.setAttribute("role", "branch manager");
        session.setAttribute("role", "branch worker");

        System.out.println("세션값 설정 확인 >> " + session.getAttribute("role"));

        return "redirect:/home/";
    }

    @GetMapping("logout")
    public String logout(HttpSession session) {
        System.out.println("========================================");
        System.out.println("GET request to logout received...");

        session.removeAttribute("role");

        return "redirect:/";
    }

    // localhost:8080/member/forgot-password
    @GetMapping("forgot-password")
    public String forgotPw() {
        System.out.println("========================================");
        System.out.println("GET request to find password received...");

        return "/member/forgot-password";
    }
}
