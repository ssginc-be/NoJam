package com.ssginc.nojam.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import jakarta.mail.MessagingException;
import java.util.Random;


@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    // 인증 코드 생성 로직
    public String createAuthCode() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        // 6자리 숫자 코드 예시
        for (int i = 0; i < 6; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    // 실제로 이메일을 보내는 메서드
    public void sendVerificationMail(String toEmail, String authCode) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        // true: 멀티파트 메세지
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("ssginc55@gmail.com");   // 보내는 사람
        helper.setTo(toEmail);                  // 받는 사람
        helper.setSubject("[NoJam] 회원가입 이메일 인증 코드"); // 메일 제목
        // 메일 내용 (HTML 가능)
        helper.setText(
                "<h1>이메일 인증</h1>" +
                        "<p>아래 인증 코드를 회원가입 창에 입력하세요.</p>" +
                        "<h3>" + authCode + "</h3>",
                true
        );

        mailSender.send(message);
    }

    /**
     * 임시 비밀번호를 사용자에게 전송하는 메서드입니다.
     * @param toEmail     받는 사람의 이메일 주소
     * @param tempPassword 생성된 임시 비밀번호
     * @throws MessagingException 이메일 전송 중 예외 발생 시
     */
    public void sendTempPasswordMail(String toEmail, String tempPassword) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        // true: 멀티파트 메세지
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setFrom("ssginc55@gmail.com");   // 보내는 사람
        helper.setTo(toEmail);                  // 받는 사람
        helper.setSubject("[NoJam] 임시 비밀번호 안내"); // 메일 제목

        // 메일 내용 (HTML 가능)
        helper.setText(
                "<h1>비밀번호 재설정</h1>" +
                        "<p>아래 임시 비밀번호를 사용하여 로그인한 후, 반드시 비밀번호를 변경해주세요.</p>" +
                        "<h3>" + tempPassword + "</h3>",
                true
        );

        mailSender.send(message);
    }
}
