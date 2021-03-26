package com.th.service;

import java.util.Collection;
import java.util.concurrent.TimeUnit;

/**
 * @author TanHaooo
 * @date 2021/3/24 16:53
 */
public interface RedisTemplateService {
    <T> boolean set(String key, T value);

    <T> boolean set(String key, T value, long time, TimeUnit timeUnit);

    <T> T get(String key, Class<T> clazz);

    boolean deleteKey(String key);

    boolean deleteKeys(Collection<String> keys);

    boolean exists(final String key);
}
