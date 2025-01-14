package com.ssginc.nojam.member.controller;

import com.ssginc.nojam.branch.service.BranchService;
import com.ssginc.nojam.member.service.EmailService;
import com.ssginc.nojam.member.service.MemberService;
import com.ssginc.nojam.member.vo.MemberVO;
import com.ssginc.nojam.branch.vo.BranchVO;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.SecureRandom;

@Slf4j
@Controller
@RequestMapping("member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final EmailService emailService;
    private final BranchService branchService;

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

            if (dbMember == null) {
                model.addAttribute("loginError", "사용자 정보를 불러올 수 없습니다.");
                return "index";
            }

            // 지점 ID로 지점 정보 조회
            String branchId = dbMember.getBranchId();
            String branchName = null;

            if (branchId != null) {
                BranchVO branch = branchService.selectBranchName(branchId);
                if (branch != null) {
                    branchName = branch.getBranchName();
                } else {
                    log.warn("해당 branchId에 대한 지점 정보를 찾을 수 없습니다: " + branchId);
                }
            }

            // 세션 설정
            session.setAttribute("userId", dbMember.getUserId());
            session.setAttribute("userName", dbMember.getUserName());
            session.setAttribute("userRole", dbMember.getUserRole());
            session.setAttribute("branchId", dbMember.getBranchId());
            if (branchName != null) {
                session.setAttribute("branch_name", branchName);
            }

            System.out.println("로그인 성공 - 세션 정보:");
            System.out.println("ID: " + session.getAttribute("userId"));
            System.out.println("Name: " + session.getAttribute("userName"));
            System.out.println("Role: " + session.getAttribute("userRole"));
            System.out.println("Branch ID: " + session.getAttribute("branchId"));
            System.out.println("Branch Name: " + session.getAttribute("branchName"));

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

        return "/member/find-pw";
    }

    /**
     * 비밀번호 찾기 폼 제출을 처리하는 메서드
     * @param userEmail 사용자가 입력한 이메일 주소
     * @param model     뷰에 데이터를 전달하기 위한 모델
     * @return find-pw.html 뷰
     */
    @PostMapping("find-password")
    public String findPassword(@RequestParam String userEmail,
                               @RequestParam(required = false) String emailDomain,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        log.info("비밀번호 찾기 요청 - 이메일: {}", userEmail);

        // 이메일 도메인을 결합
        if (emailDomain != null && !emailDomain.isEmpty()) {
            userEmail += emailDomain;
        }

        log.info("완성된 이메일: {}", userEmail);

        // 이메일로 회원 조회
        MemberVO member = memberService.getMemberByEmail(userEmail);

        if (member == null) {
            // 이메일이 존재하지 않을 경우
            model.addAttribute("error", "입력하신 이메일 주소는 등록되지 않았습니다.");
            return "member/find-pw";
        }

        // 임시 비밀번호 생성 (예: 8자리 영문 대소문자 및 숫자 조합)
        String tempPassword = generateTempPassword(8);
        log.info("생성된 임시 비밀번호: {}", tempPassword);

        // 임시 비밀번호를 데이터베이스에 저장
        boolean updateSuccess = memberService.resetPassword(userEmail, tempPassword);
        if (!updateSuccess) {
            model.addAttribute("sendError", true);
            return "member/find-pw";
        }

        // 이메일로 임시 비밀번호 전송
        try {
            emailService.sendTempPasswordMail(userEmail, tempPassword);
            //model.addAttribute("sendSuccess", true);  // 비밀번호 찾기 화면에 그대로 있는 경우
            log.info("임시 비밀번호가 {}로 전송되었습니다.", userEmail);
            // 성공 메시지를 플래시 어트리뷰트로 전달
            redirectAttributes.addFlashAttribute("sendSuccess", true);
            return "redirect:/"; // index.html로 리다이렉트
        } catch (MessagingException e) {
            log.error("이메일 전송 실패: ", e);
            model.addAttribute("sendError", true);
            return "member/find-pw";
        }
    }

    /**
     * 지정된 길이의 임시 비밀번호를 생성합니다.
     *
     * @param length 생성할 임시 비밀번호의 길이
     * @return 생성된 임시 비밀번호
     */
    private String generateTempPassword(int length) {
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for(int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }

        return sb.toString();
    }
}
