package com.thien.finance.core_banking_service.service;

public interface BaseRedisGenericService<K, F, V> {
    void set(K key, V value);

    V get(K key);

    void setTimeToLive(K key, long timeoutInDays);

    Boolean keyExists(K key);
}
