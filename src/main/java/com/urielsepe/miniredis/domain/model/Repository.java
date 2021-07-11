package com.urielsepe.miniredis.domain.model;

import java.util.List;

public interface Repository {
    void set(String key, String value, Integer expirationSeconds);
    String get(String key);
    int del(String[] keys);
    int keyCount();
    int incr(String key);
    int zAdd(String key, Double score, String member);
    int zCard(String key);
    Integer zRank(String key, String member);
    List<String> zRange(String key, Integer start, Integer stop);
}
