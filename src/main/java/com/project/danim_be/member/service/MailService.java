package com.project.danim_be.member.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class MailService {

    @Autowired
    private JavaMailSender mailSender;

    public String mailCheck(String email) {

        Random random = new Random();
        int authNumber = random.nextInt(888888) + 111111;

        String sendMail = "teamdanim@gmail.com";
        String toMail = email;
        String title = "[다님] 회원가입 인증 이메일입니다.";
        String content =
                "<div style='margin:30px;'>" +
                "<h2>안녕하세요.</h2>" +
                "<h2>다님에 오신 것을 환영합니다.</h2>" +
                "<br>" +
                "<p>아래 인증번호를 복사해 인증번호 확인란에 입력해주세요.<p>" +
                "<br>" +
                "<p>감사합니다!<p>" +
                "<br>" +
                "<div align='center' style='border:1px solid black; font-family:verdana';>" +
                "<h3 style='color:blue;'>회원가입 인증번호입니다.</h3>" +
                "<div style='font-size:130%'>" +
                "인증 번호 : <strong>" + authNumber + "</strong></div><br/>" +
                "</div>";

        mailSend(sendMail, toMail, title, content);
        return Integer.toString(authNumber);

    }

    public void mailSend(String sendMail, String toMAil, String title, String content) {

        MimeMessage message = mailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "utf-8");
            helper.setFrom(sendMail);
            helper.setTo(toMAil);
            helper.setSubject(title);
            // true로 전달해야 html형식으로 전송. 그렇지 않으면 단순 텍스트로 전송
            helper.setText(content, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }
}
