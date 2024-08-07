package com.thien.finance.notification.controller;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.thien.finance.event.dto.NotificationEvent;
import com.thien.finance.notification.dto.MailStructure;
import com.thien.finance.notification.service.EmailService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class NotificationController {
    private final EmailService emailService;

    @KafkaListener(topics = "notification-delivery")
    public void listenNotificationDelivery(NotificationEvent message) {
        String mail = message.getRecepient();

        log.info("Message: {}", message);
        emailService.sendMail(mail, MailStructure.builder()
                                .subject(message.getSubject())
                                .message(message.getBody())
                                .build());
    }
}
