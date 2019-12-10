package com.blockchain.platform.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.blockchain.platform.annotation.BusinessVerify;

/**
 * 交易验证器，是否可以交易
 * @author zhangye
 *
 */
public class BusinessValidator implements ConstraintValidator<BusinessVerify, String> {

	
	public boolean isValid(String value, ConstraintValidatorContext context) {
		// TODO Auto-generated method stub
		return false;
	}

	
}
