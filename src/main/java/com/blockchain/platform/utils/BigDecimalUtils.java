package com.blockchain.platform.utils;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.blockchain.platform.constant.AppConst;
import com.blockchain.platform.constant.BizConst;

/**
 * @Title: BigDecimalUtils.java
 * @Package: com.novel.balbit.util
 * @Description TODO 数学类计算共用
 */
public class BigDecimalUtils {


	/**
	 * 價格 位數
	 * @param price
	 * @return
	 */
	public static int getPriceScale(BigDecimal price){
		int length = 0;
		if(!ObjectUtil.isEmpty( price)){
			String temp = price.toString();
			String suffix = temp.substring( temp.indexOf(".") + 1, temp.length());
			if(!StrUtil.isBlank(suffix)){
				char zero = '0';
				for(int idx = 0;idx < suffix.length();idx ++){
					if( zero != suffix.charAt(idx)){
						length = suffix.length() - idx;
						break;
					}
				}
			}
		}
		return length;
	}

	/**
	 * 数学共用 加法 
	 * @param b1
	 * @param b2
	 * @return b1 + b2
	 */
	public static BigDecimal add(BigDecimal b1, BigDecimal b2) {
		if( ObjectUtil.isEmpty( b1)){
			return b2.setScale(BizConst.TradeConst.BIGDECIMAL_MAX_LENGTH, BigDecimal.ROUND_DOWN);
		}
		if( ObjectUtil.isEmpty( b2)){
			return b1.setScale(BizConst.TradeConst.BIGDECIMAL_MAX_LENGTH, BigDecimal.ROUND_DOWN);
		}
		return b1.add( b2).setScale(BizConst.TradeConst.BIGDECIMAL_MAX_LENGTH, BigDecimal.ROUND_DOWN);
	}
	
	/**
	 * 数学共用 减法
	 * @param b1
	 * @param b2
	 * @return b1 - b2
	 */
	public static BigDecimal subtr(BigDecimal b1, BigDecimal b2){
		if( ObjectUtil.isEmpty( b1) || ObjectUtil.isEmpty( b2)){
			return BigDecimal.ZERO;
		}
		return b1.subtract( b2).setScale(BizConst.TradeConst.BIGDECIMAL_MAX_LENGTH, BigDecimal.ROUND_DOWN);
	}
	
	/**
	 * 数学共用 乘法
	 * @param b1
	 * @param b2
	 * @return b1 * b2
	 */
	public static BigDecimal multi(BigDecimal b1, BigDecimal b2){
		if( ObjectUtil.isEmpty( b1) || ObjectUtil.isEmpty( b2)){
			return BigDecimal.ZERO;
		}
		return b1.multiply( b2).setScale(BizConst.TradeConst.BIGDECIMAL_MAX_LENGTH, BigDecimal.ROUND_DOWN);
	}
	
	/**
	 * 数学共用 除法
	 * @param b1
	 * @param b2
	 * @return b1/b2
	 */
	public static BigDecimal divi(BigDecimal b1, BigDecimal b2){
		if(ObjectUtil.isEmpty( b1) || ObjectUtil.isEmpty( b2) || b2.compareTo( BigDecimal.ZERO) == 0){
			return BigDecimal.ZERO;
		}
		return b1.divide( b2, BizConst.TradeConst.BIGDECIMAL_MAX_LENGTH, BigDecimal.ROUND_DOWN);
	}

	/**
	 * 数学共用类 是否为0
	 * @param b
	 * @return
	 */
	public static boolean isZero(BigDecimal b){
		if(ObjectUtil.isEmpty( b) || b.compareTo( BigDecimal.ZERO) == 0) {
			return true;
		}
		return false;
	}
	
	/**
	 * 转换为double
	 * @param b
	 * @return
	 */
	public static Double toDouble(BigDecimal b) {
		if(ObjectUtil.isEmpty( b) || b.compareTo( BigDecimal.ZERO) == 0) {
			return 0d;
		}
		return b.doubleValue();
	}
	
	/**
	 * 判断b1 是否在 b2 - b3的区间内
	 * @param b1 比较对象
	 * @param b2 from 对象
	 * @param b3 to 对象
	 * @return true 在此区间 ， false 不在此区间
	 */
	public static boolean isBetween(BigDecimal b1, BigDecimal b2, BigDecimal b3) {
		if(ObjectUtil.isEmpty( b2) && ObjectUtil.isEmpty( b3) ) {
			return true;
		}
		// 如果下限为空，则小于等于上限
		if( ObjectUtil.isEmpty( b2) && b1.compareTo( b3) <= 0) {
			return true;
		}
		// 如果上限为空，则大于下限
		if( ObjectUtil.isEmpty( b3) && b1.compareTo( b2) > 0) {
			return true;
		}
		if( b1.compareTo( b2) > 0 &&  b1.compareTo( b3) <= 0) {
			return true;
		}
		return false;
	}

	/**
	 * 比较 b1 b2
	 * @param b1
	 * @param b2
	 * @return
	 */
	public static boolean compare(BigDecimal b1, BigDecimal b2) {
		if ( ObjectUtil.isEmpty(b2) || ObjectUtil.isEmpty( b2)) {
			return false;
		}
		if ( b1.compareTo( b2) >= 0) {
			return true;
		}
		return false;
	}

	/**
	 * 比较 b1 b2
	 * @param b1
	 * @param b2
	 * @return
	 */
	public static boolean compareTo(BigDecimal b1, BigDecimal b2) {
		if ( ObjectUtil.isEmpty(b2) || ObjectUtil.isEmpty( b2)) {
			return false;
		}
		if ( b1.compareTo( b2) > 0) {
			return true;
		}
		return false;
	}


	/**
	 *  四舍五人 保留指定小数位
	 * @param decimal
	 * @param num 指定位数
	 * @return
	 */
	public static BigDecimal sCale(BigDecimal decimal,int num){
		if(ObjectUtil.isEmpty( decimal) && ObjectUtil.isEmpty( decimal) ) {
			return BigDecimal.ZERO;
		}
		BigDecimal setScale = decimal.setScale(num,BigDecimal.ROUND_HALF_UP);

		return setScale;
	}

	/**
	 * Object转BigDecimal类型
	 *
	 * @param value 要转的object类型
	 * @return 转成的BigDecimal类型数据
	 */
	public static BigDecimal toBigDecimal(Object value) {
		BigDecimal ret = null;
		if (value != null) {
			if (value instanceof BigDecimal) {
				ret = (BigDecimal) value;
			} else if (value instanceof String) {
				ret = new BigDecimal((String) value);
			} else if (value instanceof BigInteger) {
				ret = new BigDecimal((BigInteger) value);
			} else if (value instanceof Number) {
				ret = new BigDecimal(((Number) value).doubleValue());
			} else {
				throw new ClassCastException("Not possible to coerce [" + value + "] from class " + value.getClass() + " into a BigDecimal.");
			}
		}
		return sCale(ret,  BizConst.TradeConst.BIGDECIMAL_MAX_LENGTH);
	}
}
