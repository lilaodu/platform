package com.blockchain.platform.validator;

import cn.hutool.core.util.StrUtil;
import com.blockchain.platform.annotation.MobileVerify;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 手机号码验证器
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-04-29 7:40 PM
 **/
public class MobileValidator implements ConstraintValidator<MobileVerify, String> {

    private boolean required = false;

    /**
     * 手机正则表达式
     */
    private static Pattern pattern = Pattern.compile("^(1[3456789])\\d{9}$");

    /**
     * 验证是否为手机号码
     * @param mobile
     * @return
     */
    public static boolean isMobile(String mobile) {
        if( StrUtil.isEmpty( mobile)){
           return false;
        }
        Matcher m = pattern.matcher( mobile);
        return m.matches();
    }

    @Override
    public boolean isValid(String mobile, ConstraintValidatorContext constraintValidatorContext) {
        if( required){
            return isMobile( mobile);
        } else {
            if( StrUtil.isEmpty( mobile)) {
                return false;
            } else {
                return isMobile( mobile);
            }
        }
    }

    @Override
    public void initialize(MobileVerify constraintAnnotation) {
        required = constraintAnnotation.required();
    }
}
