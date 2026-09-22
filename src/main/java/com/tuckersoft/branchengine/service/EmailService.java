package com.tuckersoft.branchengine.service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
@Slf4j @Service
public class EmailService {
    private final JavaMailSender mailSender;
    @Value("${app.mail.from:noreply@tuckersoft.com}") private String from;
    @Value("${spring.mail.username:}") private String user;
    public EmailService(JavaMailSender s) { this.mailSender = s; }
    public void send(String to, String subject, String body) {
        if (user == null || user.isBlank()) {
            log.info("[MAIL-MOCK] to={} subject={}", to, subject);
            log.info("[MAIL-MOCK-BODY]\n{}", body);
            return;
        }
        SimpleMailMessage m = new SimpleMailMessage();
        m.setFrom(from); m.setTo(to); m.setSubject(subject); m.setText(body);
        mailSender.send(m);
    }
}
