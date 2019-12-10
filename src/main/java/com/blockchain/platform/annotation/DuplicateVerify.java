package com.blockchain.platform.annotation;

import cn.hutool.core.util.StrUtil;
import com.blockchain.platform.enums.Method;
import com.blockchain.platform.enums.Timestamp;

import java.lang.annotation.*;

/**
 * 重复提交验证
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface DuplicateVerify {

    /**
     * 用户token
     */
    String token() default StrUtil.EMPTY;

    /**
     * 方法名称
     */
    Method method() default Method.OTC_BUY;

    /**
     * 有效时间
     */
    Timestamp timestamp() default Timestamp.ONE_SECONDS;

    /**
     * 效验字段
     * @return
     */
    String [] filed() default {};

    /**
     * 是否强制验证字段
     */
    boolean isForce() default false;
}
