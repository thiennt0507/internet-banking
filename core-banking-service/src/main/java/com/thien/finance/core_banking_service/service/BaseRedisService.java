package com.thien.finance.core_banking_service.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.thien.finance.core_banking_service.model.dto.User;

public interface BaseRedisService {
    void set(String key, Object value);

    void setTimeToLive(String key, Long timeoutInDays);

    void hashSet(String key, String field, Object value);

    boolean keyExists(String key);

    boolean hashExists(String key, String field);

    Object get(String key);

    public Map<String, Object> getFields(String key);

    Object hashGet(String key, String field);

    List<Object> hashGetByFieldPrefix(String key, String fieldPrefix);

    Set<String> getFieldPrefixes(String key);
    
    void delete(String key);

    void delete(String key, String field);

    void delete(String key, List<String> fields);

    void clear(String key);
}
