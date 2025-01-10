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
}
