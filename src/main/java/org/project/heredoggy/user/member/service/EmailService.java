package org.project.heredoggy.user.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    public void send(String to, String subject, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(body);
        mailSender.send(message);
    }

    public void sendPasswordResetCode(String to, String token) {
        String subject = "[여기보개] 비밀번호 재설정 인증코드 안내 🐶";
        String body = String.format(
                "안녕하세요, 여기보개입니다! 🐾\n\n" +
                        "아래 인증코드를 입력하여 비밀번호를 재설정해주세요:\n\n" +
                        "🔑 인증코드: %s\n\n" +
                        "※ 인증코드는 30분 동안만 유효합니다.\n\n" +
                        "감사합니다.\n여기보개 팀 드림 🐶", token
        );
        send(to, subject, body);
    }
}
