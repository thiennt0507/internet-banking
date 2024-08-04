package com.thien.finance.fun_transfer_service.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.thien.finance.fun_transfer_service.model.entity.FundTransferEntity;
import com.thien.finance.fun_transfer_service.service.RabbitMQJsonProducer;
import com.thien.finance.fun_transfer_service.service.RabbitMQProducer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/message")
public class MessageControlller {
    private final RabbitMQProducer rabbitmqProducer;
    private final RabbitMQJsonProducer rabbitMQJsonProducer;

    @GetMapping("/public")
    public ResponseEntity<String> sendMessage(@RequestParam("message") String message) {
        log.info("Sending message");
        rabbitmqProducer.sendMessage(message);
        return ResponseEntity.ok("Message sent to RabbitMQ");
    }

    @PostMapping("/publish")
    public ResponseEntity<String> sendJsonMessage(@RequestBody FundTransferEntity fund){
        rabbitMQJsonProducer.sendJsonMessage(fund);
        return ResponseEntity.ok("Json message sent to RabbitMQ.");
    }
}
