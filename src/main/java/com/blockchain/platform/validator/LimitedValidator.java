package com.blockchain.platform.validator;

import cn.hutool.core.collection.CollUtil;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.blockchain.platform.annotation.LimitedVerify;

import java.util.Arrays;
import java.util.List;

/**
 * 交易验证器
 *
 **/
public class LimitedValidator implements ConstraintValidator<LimitedVerify, String> {

    /**
     * 验证值
     */
    private String [] values;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        List<String> list = Arrays.asList( values);
        return CollUtil.contains( list, s);
    }

    @Override
    public void initialize(LimitedVerify constraintAnnotation) {
        this.values = constraintAnnotation.values();
    }
}
