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
        // 이메일 중복 체크
        if (memberService.isEmailDuplicate(userEmail)) {
            return "duplicate"; // 중복된 이메일
        }

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
            return "member/signup";
        }

        // 이메일 중복 체크 (서버 측)
        // 클라이언트 측에서의 중복 체크가 완료되었더라도,
        // 두 요청 간에 다른 사용자가 동일한 이메일로 가입했을 가능성을 방지
        if (memberService.isEmailDuplicate(memberVO.getUserEmail())) {
            model.addAttribute("error", "입력하신 이메일 주소는 이미 사용 중입니다.");
            return "member/signup";
        }

        // 이메일 형식 검증
        String emailPattern = "^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$";
        if (!memberVO.getUserEmail().matches(emailPattern)) {
            model.addAttribute("error", "올바른 이메일 형식을 입력해주세요!");
            return "member/signup";
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

    // 로그인
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
            session.setAttribute("userEmail", dbMember.getUserEmail());
            session.setAttribute("branchId", dbMember.getBranchId());
            if (branchName != null) {
                session.setAttribute("branchName", branchName);
            } else {
                session.setAttribute("branchName", "N/A");
            }
            session.setAttribute("userRole", dbMember.getUserRole());

            System.out.println("로그인 성공 - 세션 정보:");
            System.out.println("ID: " + session.getAttribute("userId"));
            System.out.println("Name: " + session.getAttribute("userName"));
            System.out.println("Email: " + session.getAttribute("userEmail"));
            System.out.println("Branch ID: " + session.getAttribute("branchId"));
            System.out.println("Branch Name: " + session.getAttribute("branchName"));
            System.out.println("Role: " + session.getAttribute("userRole"));

            // user_role에 따라 리다이렉트 경로 설정
            String userRole = dbMember.getUserRole();
            if ("GUEST".equals(userRole)) {
                return "redirect:/";  // 수정 필요
            } else if ("BWKR".equals(userRole)) {
                return "redirect:/chart/";
            } else if ("BMNG".equals(userRole)) {
                return "redirect:/chart/";
            } else if ("HEAD".equals(userRole)) {
                return "redirect:/chart/";
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

    // 로그아웃
    @GetMapping("logout")
    public String logout(HttpSession session) {
        System.out.println("========================================");
        System.out.println("GET request to logout received...");

        session.removeAttribute("userId");
        session.removeAttribute("userName");
        session.removeAttribute("userEmail");
        session.removeAttribute("branchId");
        session.removeAttribute("branchName");
        session.removeAttribute("userRole");

        return "redirect:/";
    }

    // 비밀번호 찾기 화면
    @GetMapping("forgot-password")
    public String forgotPw() {
        System.out.println("========================================");
        System.out.println("GET request to find password received...");

        return "/member/find-pw";
    }

    // 임시 비밀번호 발급 SMTP
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

    // 임시 비밀번호 생성
    private String generateTempPassword(int length) {
        final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }

        return sb.toString();
    }

    // 마이페이지로 이동
    @GetMapping("mypage")
    public String myPage(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            // 로그인되지 않은 사용자는 로그인 페이지로 리다이렉트
            return "redirect:/";
        }

        MemberVO member = memberService.getMemberById(userId);
        if (member == null) {
            model.addAttribute("error", "회원 정보를 불러올 수 없습니다.");
            return "error"; // 에러 페이지로 이동 (적절히 설정 필요)
        }

        BranchVO branch = null;
        String branchName = "N/A"; // 기본값 설정
        if (member.getBranchId() != null) {
            branch = branchService.selectBranchName(member.getBranchId());
            if (branch != null) {
                branchName = branch.getBranchName();
            }
        }

        model.addAttribute("member", member);
        model.addAttribute("branchName", branchName);

        return "member/mypage";
    }

    // 정보 수정 페이지로 이동
    @GetMapping("update-mypage")
    public String updateMyPage(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            return "redirect:/";
        }

        MemberVO member = memberService.getMemberById(userId);
        if (member == null) {
            model.addAttribute("error", "회원 정보를 불러올 수 없습니다.");
            return "error";
        }

        model.addAttribute("member", member);
        return "member/update-mypage";
    }

    // 마이페이지 정보 업데이트 처리
    @PostMapping("updateInfo")
    public String updateInfo(
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String currentPassword,
            @RequestParam(required = false) String newPassword,
            @RequestParam(required = false) String confirmNewPassword,
            @RequestParam String updateType,
            HttpSession session,
            Model model,
            RedirectAttributes redirectAttributes
    ) {
        String userId = (String) session.getAttribute("userId");
        if (userId == null) {
            // 로그인되지 않은 사용자는 로그인 페이지로 리다이렉트
            return "redirect:/";
        }

        // 회원 정보 업데이트
        boolean updateSuccess = false;

        if ("name".equals(updateType)) {
            // 사용자명 업데이트
            updateSuccess = memberService.updateUserName(userId, userName);
            if (updateSuccess) {
                // 세션 정보 갱신
                session.setAttribute("userName", userName);
                redirectAttributes.addFlashAttribute("successMessage", "사용자명이 성공적으로 업데이트되었습니다.");
            } else {
                model.addAttribute("error", "사용자명 업데이트에 실패했습니다.");
            }
        } else if ("password".equals(updateType)) {
            // 비밀번호 업데이트
            updateSuccess = memberService.updatePassword(userId, currentPassword, newPassword, confirmNewPassword);
            if (updateSuccess) {
                redirectAttributes.addFlashAttribute("successMessage", "비밀번호가 성공적으로 업데이트되었습니다.");
            } else {
                model.addAttribute("error", "비밀번호 업데이트에 실패했습니다. 현재 비밀번호를 확인해주세요.");
            }
        }

        if (updateSuccess) {
            return "redirect:/member/update-mypage";
        } else {
            return "member/update-mypage";
        }
    }
}