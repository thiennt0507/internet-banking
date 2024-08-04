package com.thien.finance.fun_transfer_service.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.thien.finance.fun_transfer_service.model.entity.FundTransferEntity;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RabbitMQJsonProducer {
    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.json.key}")
    private String routingJsonKey;

    private RabbitTemplate rabbitTemplate;

    public RabbitMQJsonProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendJsonMessage(FundTransferEntity fund){
        log.info(String.format("Json message sent -> %s",fund.toString()));
        rabbitTemplate.convertAndSend(exchange,routingJsonKey,fund);
    }
}
