package com.blockchain.platform.annotation;

import java.lang.annotation.*;

/**
 * 分表标签
 */
@Inherited
@Documented
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface Sharding {

    /**
     * 表名
     * @return
     */
    String [] table() default "";

    /**
     * 分表字段
     * 默认coinPair交易对为分表值
     * @return
     */
    String field() default "coinPair";
}
