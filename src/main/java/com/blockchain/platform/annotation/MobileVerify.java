package com.blockchain.platform.annotation;

import com.blockchain.platform.validator.MobileValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

/**
 * 手机号码验证
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {MobileValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
public @interface MobileVerify {

    /**
     * 错误信息
     * @return
     */
    String message() default "";

    /**
     * 是否强制验证
     * @return
     */
    boolean required() default false;


    Class<?> [] groups() default {};


    Class<? extends Payload> [] payload() default {};
}
