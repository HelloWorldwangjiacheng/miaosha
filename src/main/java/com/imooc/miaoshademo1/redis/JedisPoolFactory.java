package com.imooc.miaoshademo1.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Author w1586
 * @Date 2020/3/3 14:34
 * @Cersion 1.0
 */
@Service
public class JedisPoolFactory {

    @Autowired
    RedisConfig redisConfig;


    @Bean
    public JedisPool JedisPoolFactory(){
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(redisConfig.getPoolMaxIdle());
        jedisPoolConfig.setMaxTotal(redisConfig.getPoolMaxTotal());
        jedisPoolConfig.setMaxWaitMillis(redisConfig.getPoolMaxWait());

        JedisPool jp = new JedisPool(jedisPoolConfig,
                redisConfig.getHost(),
                redisConfig.getPort(),
                redisConfig.getTimeout()*1000,
                redisConfig.getPassword(),
                0);

        return jp;
    }
}
