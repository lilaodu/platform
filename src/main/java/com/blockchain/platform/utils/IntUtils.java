package com.blockchain.platform.utils;

import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;

/**
 * integer类型工具类
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-04-24 9:54 AM
 **/
public class IntUtils {

    public static final Integer NEGATIVE_ONE = -1;

    public static final Integer INT_ZERO = 0;

    public static final Integer INT_ONE = 1;

    public static final Integer INT_TWO = 2;

    public static final Integer INT_THREE = 3;

    /**
     * 判断 integer 是否为0
     * @param integer
     * @return
     */
    public static boolean isZero(Integer integer) {
        return integer == null || integer.intValue() == 0;
    }

    /**
     * 是否为空
     * @param integer
     * @return
     */
    public static boolean isEmpty(Integer integer) {
        return integer == null;
    }

    /**
     * 判断数字是否相等
     * @param int1
     * @param int2
     * @return
     */
    public static boolean equals(Integer int1, Integer int2){
        if( isEmpty( int1) || isEmpty( int2)) {
            return false;
        }
        return int1.intValue() == int2.intValue();
    }

    /**
     * 比较int1, int2 大小
     * @param int1
     * @param int2
     * @return int1 > int2 true | false
     */
    public static boolean compareTo(Integer int1,Integer int2) {
        if( isEmpty( int1) || isEmpty( int2)) {
            return false;
        }
        return int1.intValue() > int2.intValue();
    }

    public static boolean compare(Integer int1,Integer int2){
        if( isEmpty( int1) || isEmpty( int2)) {
            return false;
        }
        return int1.compareTo( int2) >= 0;
    }

    /**
     * 转int
     * @param object
     * @return
     */
    public static int toInt(Object object) {
        if( ObjectUtil.isEmpty( object) ){
            return 0;
        }
        return Integer.parseInt( StrUtil.toString( object));
    }

    /**
     * 转换Long
     * @param object
     * @return
     */
    public static Long toLong(Object object) {
        if( ObjectUtil.isEmpty( object) ){
            return 0l;
        }
        return Long.parseLong( StrUtil.toString( object));
    }

    /**
     * 向下取整
     * @param num1
     * @param num2
     * @return
     */
    public static Integer floor( Integer num1, Integer num2){
        if(isEmpty( num1) || isEmpty( num2)){
            return 0;
        }
        Integer d = (int)Math.floor( num1/num2);
        return d;
    }

    /**
     * 计算百分比
     * @param int1
     * @param int2
     * @return
     */
    public static String percent(Integer int1,Integer int2){
        if( isZero( int1) || isZero( int2)) {
            return StrUtil.concat( Boolean.FALSE, StrUtil.toString( IntUtils.INT_ZERO), "%");
        }
        return StrUtil.concat( Boolean.FALSE, StrUtil.toString( NumberUtil.div( int1, int2,0)), "%");
    }

    /**
     * 是否大于 0
     * @param i
     * @return
     */
    public static Boolean greaterThanZero(Integer i) {
        return i.intValue() > 0;
    }

    /**
     * 计算偏移量
     * @param total
     * @return
     */
    public static Integer offset(Integer total){
         return total/40 * 40;
    }
}
