package com.blockchain.platform.utils;

import com.blockchain.platform.exception.BcAuthException;
import com.blockchain.platform.exception.BcException;
import com.blockchain.platform.exception.BcLoginException;
import lombok.extern.log4j.Log4j2;
import org.springframework.validation.BindingResult;

/**
 * 异常处理工具类
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-07-14 11:21 AM
 **/
@Log4j2
public class ExUtils {

    /**
     * 是否业务异常
     * @param ex
     * @return
     */
    public static final boolean isBizEx(Exception ex) {
        if(ex instanceof BcException){
            return true;
        }
        return false;
    }

    /**
     * 是否为登录异常
     * @param ex
     * @return
     */
    public static final boolean isLoginEx(Exception ex){
        if(ex instanceof BcLoginException){
            return true;
        }
        return false;
    }

    /**
     * 用户登录失败
     * @param localeKey
     * @return
     */
    public static final BcLoginException notLogin(String localeKey) {
        return new BcLoginException( localeKey);
    }

    /**
     * 抛出异常
     * @param ex
     * @param key
     * @return
     */
    public static final BcException error(Exception ex, String key) {
        if( isBizEx( ex)){
            throw (BcException) ex;
        }
        if( isLoginEx( ex)) {
            throw (BcLoginException) ex;
        }
        throw new BcException( key);
    }


    /**
     * 用户未授权
     * @param localeKey
     * @return
     */
    public static final BcAuthException unAuth(String localeKey){
        return new BcAuthException( localeKey);
    }

    /**
     * 普通消息异常返回
     * @param key
     * @return
     */
    public static BcException error(String key){

        log.error(key);

        return new BcException( key);
    }

    /**
     * 验证异常信息
     * @param valid
     * @return
     */
    public static BcException error(BindingResult valid) {
        return new BcException( valid.getFieldError().getDefaultMessage());
    }


    /**
     * 业务异常返回
     * @param key
     * @param values
     * @return
     */
    public static BcException error(String key, Object [] values){
        return new BcException(key, values);
    }

}
