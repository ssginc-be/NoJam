package com.ssginc.nojam.member.controller;

import com.ssginc.nojam.member.service.EmailService;
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
    private final EmailService emailService;

    // 이메일 인증코드 발송
    @PostMapping("sendCode")
    @ResponseBody
    public String sendEmailVerificationCode(@RequestParam String userEmail, HttpSession session) {
        // 1. 인증 코드 생성
        String authCode = emailService.createAuthCode();
        // 2. 인증 코드 메일로 보내기
        try {
            emailService.sendVerificationMail(userEmail, authCode);
        } catch (Exception e) {
            log.error("이메일 발송 실패:", e);
            return "fail";
        }
        // 3. 세션에 인증코드 저장
        session.setAttribute("emailAuthCode", authCode);
        // AJAX 통신에서 사용자가 확인할 문자열 리턴 (JSON으로 해도 무관)
        return "success";
    }

    // 이메일 인증코드 검증
    @PostMapping("verifyCode")
    @ResponseBody
    public String verifyEmailCode(@RequestParam String userEmail,
                                  @RequestParam String authCode,
                                  HttpSession session) {
        // 1. 세션에서 인증코드 가져오기
        String sessionAuthCode = (String) session.getAttribute("emailAuthCode");

        // 2. 인증코드 검증
        if (sessionAuthCode != null && sessionAuthCode.equals(authCode)) {
            // 인증 성공
            return "verified";
        } else {
            // 인증 실패
            return "invalid";
        }
    }

    // 회원가입 화면
    @GetMapping("/signup")
    public String signUp() {
        log.info("singup 요청됨.");
        return "/member/signup";
    }

    // bootstrap 사용할 때 코드
    @GetMapping("signup0")
    public String signUp0() {
        log.info("singup 요청됨.");
        return "signup0";
    }
    // bootstrap 사용할 때 코드
    @GetMapping("index0")
    public String login0() {
        log.info("login 요청됨.");
        return "index0";
    }

    // 회원가입 기능
    @PostMapping("signup2")
    public String signUp2(MemberVO memberVO,
                          @RequestParam("emailAuthCodeInput") String emailAuthCodeInput,
                          Model model,
                          HttpSession session) {
        // 1) 세션에 저장된 인증코드를 가져온다
        String emailAuthCode = (String) session.getAttribute("emailAuthCode");

        // 2) 인증코드 검증
        if (emailAuthCode == null || !emailAuthCode.equals(emailAuthCodeInput)) {
            // 인증 실패
            model.addAttribute("error", "이메일 인증에 실패했습니다. 인증 코드를 다시 확인해주세요!");
            return "signup";
        }

        // 이메일 형식 검증
        String emailPattern = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
        if (!memberVO.getUserEmail().matches(emailPattern)) {
            model.addAttribute("error", "올바른 이메일 형식을 입력해주세요!");
            return "signup";
        }

        // 3) (인증 성공) 비밀번호 암호화 & DB에 회원 정보 저장
        memberService.signUpPwEncoder(memberVO);
        System.out.println("========================================");
        System.out.println("GET request to register received...");
        System.out.println(memberVO);

        // 4) 세션에서 인증코드 제거(보안상)
        session.removeAttribute("emailAuthCode");

        return "redirect:/";
    }

    // ID 중복 체크
    @GetMapping("checkId")
    @ResponseBody  // 뷰인 template으로 가지 않고 단순한 데이터나 json으로 보냄.
    public boolean checkId(@RequestParam String userId) {
        boolean result = memberService.checkId(userId);
        return result;
    }


    @PostMapping("login")
    public String login(MemberVO memberVO, HttpSession session, Model model) {
        System.out.println("========================================");
        System.out.println("memberVO >>>>>>>>>>>>>>>>>>> " + memberVO);
        System.out.println("GET request to login received...");

        // 로그인 서비스 호출
        boolean loginSuccess = memberService.login(memberVO);

        if (loginSuccess) {
            // DB에서 사용자 정보 조회
            MemberVO dbMember = memberService.getMemberById(memberVO.getUserId());

            // 세션 설정
            session.setAttribute("userId", dbMember.getUserId());
            session.setAttribute("userName", dbMember.getUserName());
            session.setAttribute("userRole", dbMember.getUserRole());
            session.setAttribute("branchId", dbMember.getBranchId());

            System.out.println("로그인 성공 - 세션 정보:");
            System.out.println("ID: " + session.getAttribute("userId"));
            System.out.println("Name: " + session.getAttribute("userName"));
            System.out.println("Role: " + session.getAttribute("userRole"));
            System.out.println("Branch ID: " + session.getAttribute("branchId"));

            // user_role에 따라 리다이렉트 경로 설정
            String userRole = dbMember.getUserRole();
            if ("GUEST".equals(userRole)) {
                return "redirect:/home/";  // 수정 필요
            } else if ("BWKR".equals(userRole)) {
                return "redirect:/home/";  // 수정 필요
            } else if ("BMNG".equals(userRole)) {
                return "redirect:/home/";  // 수정 필요
            } else if ("HEAD".equals(userRole)) {
                return "redirect:/home/";  // 수정 필요
            } else {
                model.addAttribute("loginError", "Invalid Role");
                return "index";
            }
        } else {
            // 로그인 실패
            model.addAttribute("loginError", "Invalid ID or Password");
            System.out.println("로그인 실패 - ID: " + memberVO.getUserId());
            return "index";
        }

    }

    @GetMapping("logout")
    public String logout(HttpSession session) {
        System.out.println("========================================");
        System.out.println("GET request to logout received...");

        session.removeAttribute("userId");
        session.removeAttribute("userName");
        session.removeAttribute("userRole");
        session.removeAttribute("branchId");

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
