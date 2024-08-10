package com.thien.finance.core_banking_service.model.listener;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.thien.finance.core_banking_service.model.entity.TransactionEntity;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.PostPersist;
import lombok.RequiredArgsConstructor;

@EntityListeners(TransactionEntity.class)
@RequiredArgsConstructor
public class TransactionEntityListener {
    private final RedisTemplate<String, Object> redisTemplate;


    @PostPersist
    public void PostPersist(TransactionEntity transaction) {
       System.out.println("Create transaction");
    }
}
