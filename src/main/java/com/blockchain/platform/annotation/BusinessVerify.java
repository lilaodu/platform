package com.blockchain.platform.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import com.blockchain.platform.validator.BusinessValidator;
import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.CONSTRUCTOR, ElementType.PARAMETER})
@Constraint(validatedBy = {BusinessValidator.class})
public @interface BusinessVerify {

    /**
     * 错误信息
     * @return
     */
    String message() default "";

    /**
     * 验证值
     * @return
     */
    String [] values() default {};


    Class<?> [] groups() default {};


    Class<? extends Payload> [] payload() default {};

}
