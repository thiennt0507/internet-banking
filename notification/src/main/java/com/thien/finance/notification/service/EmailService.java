package com.thien.finance.notification.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.thien.finance.notification.dto.MailStructure;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final JavaMailSender emailSender;

    @Value("${spring.mail.username")
    private String fromMail;

    public void sendMail(String mail, MailStructure structure) {
       SimpleMailMessage message = new SimpleMailMessage();
       message.setFrom(fromMail);
       message.setSubject(structure.getSubject());
       message.setText(structure.getMessage());
       message.setTo(mail);

       emailSender.send(message);
    }
}
