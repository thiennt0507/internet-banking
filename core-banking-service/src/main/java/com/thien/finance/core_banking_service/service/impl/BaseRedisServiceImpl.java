package com.thien.finance.core_banking_service.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;


import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.thien.finance.core_banking_service.service.BaseRedisService;


@Service
public class BaseRedisServiceImpl implements BaseRedisService{
    private final RedisTemplate<String, Object> redisTemplate;

    private final HashOperations<String, String, Object> hashOperations;

    public BaseRedisServiceImpl(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }

    @Override
    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }


    @Override
    public void setTimeToLive(String key, Long timeoutInDays) {
        redisTemplate.expire(key, timeoutInDays, TimeUnit.DAYS);
    }

    @Override
    public void hashSet(String key, String field, Object value) {
        hashOperations.put(key, field, value);
    }

    @Override
    public boolean hashExists(String key, String field) {
        return hashOperations.hasKey(key, field);
    }

    @Override
    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    @Override
    public Map<String, Object> getFields(String key) {
        return hashOperations.entries(key);
    }

    @Override
    public Object hashGet(String key, String field) {
        return hashOperations.get(key, field);
    }

    @Override
    public List<Object> hashGetByFieldPrefix(String key, String fieldPrefix) {
        List<Object> Strings =  new ArrayList<Object>();

        Map<String, Object> hashEntries = hashOperations.entries(key);

        for (Entry<String, Object> entry : hashEntries.entrySet()) {
            String currentField = entry.getKey();
            if (currentField.startsWith(fieldPrefix)) {
                Strings.add(entry.getValue());
            }
        }
        return Strings;
    }

    @Override
    public Set<String> getFieldPrefixes(String key) {
        return hashOperations.entries(key).keySet();
    }

    @Override
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    @Override
    public void delete(String key, String field) {
        hashOperations.delete(key, field);
    }

    @Override
    public void delete(String key, List<String> fields) {
        for (String field: fields) {
            hashOperations.delete(key, field);
        }
    }

    @Override
    public boolean keyExists(String key) {
        return this.redisTemplate.hasKey(key);
    }

    @Override
    public void clear(String key) {
       this.redisTemplate.getConnectionFactory().getConnection().flushAll();
    }
}
