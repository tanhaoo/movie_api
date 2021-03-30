package com.th.service.impl;

import com.th.service.RedisTemplateService;
import com.th.utils.StringToBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author TanHaooo
 * @date 2021/3/24 16:53
 */
@Service
public class RedisTemplateServiceImpl implements RedisTemplateService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 将值放入缓存
     *
     * @param key   键
     * @param value 值
     * @return true成功 false 失败
     */
    public <T> boolean set(String key, T value) {
        try {
            //任意类型转换成String
            String val = StringToBean.beanToString(value);
            if (val == null || val.length() <= 0) {
                return false;
            }
            stringRedisTemplate.opsForValue().set(key, val);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 将值放入缓存并设置时间
     *
     * @param key      键
     * @param value    值
     * @param time     时长
     * @param timeUnit 时间单位
     * @return true成功 false 失败
     */
    public <T> boolean set(String key, T value, long time, TimeUnit timeUnit) {
        if (time > 0) {
            try {
                //任意类型转换成String
                String val = StringToBean.beanToString(value);
                if (val == null || val.length() <= 0) {
                    return false;
                }
                stringRedisTemplate.opsForValue().set(key, val);
                stringRedisTemplate.expire(key, time, timeUnit);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else
            set(key, value);
        return false;
    }

    public <T> T get(String key, Class<T> clazz) {
        try {
            String value = stringRedisTemplate.opsForValue().get(key);
            return StringToBean.stringToBean(value, clazz);
        } catch (Exception e) {
            return null;
        }
    }

    public <T> List<T> getList(String key, Class<T> clazz) {
        try {
            String value = stringRedisTemplate.opsForValue().get(key);
            return StringToBean.stringToList(value, clazz);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key
     * @return
     */
    public boolean exists(final String key) {
        return stringRedisTemplate.hasKey(key);
    }

    /**
     * 删除key
     *
     * @param key
     * @return
     */
    public boolean deleteKey(String key) {
        return stringRedisTemplate.delete(key);
    }

    /**
     * 删除keys
     *
     * @param keys
     * @return
     */
    public boolean deleteKeys(Collection<String> keys) {
        return stringRedisTemplate.delete(keys) == keys.size();
    }


}
