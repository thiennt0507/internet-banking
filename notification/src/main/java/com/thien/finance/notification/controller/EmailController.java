package com.thien.finance.notification.controller;

import org.springframework.web.bind.annotation.RestController;

import com.thien.finance.notification.service.EmailService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequiredArgsConstructor
@Slf4j
public class EmailController {
    private final EmailService  emailService;

    @GetMapping("/send-test-mail")
    public String requestMethodName() {
        // emailService.sendMail("nguyenthanhthien0507@gmail.com", "Test Email", "This is a test email");
        return "Email test end successfully";
    }

    
}
