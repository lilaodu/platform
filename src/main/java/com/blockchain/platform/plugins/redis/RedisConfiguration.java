package com.blockchain.platform.plugins.redis;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * redis配置类
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-04-22 6:49 PM
 **/
@Configuration
public class RedisConfiguration {

    /**
     * redis 连接对象
     * @param factory
     * @return
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisTemplate template = new RedisTemplate( );
        // 连接工厂
        template.setConnectionFactory( factory);

        // 序列化 字符串
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer();
        template.setKeySerializer( stringRedisSerializer);
        template.setHashKeySerializer( stringRedisSerializer);

        // 序列化规则
        GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer = new GenericJackson2JsonRedisSerializer();
        template.setValueSerializer( genericJackson2JsonRedisSerializer);
        template.setHashValueSerializer( genericJackson2JsonRedisSerializer);

        // 关闭事务
        template.setEnableTransactionSupport( false);
        return template;
    }

}
