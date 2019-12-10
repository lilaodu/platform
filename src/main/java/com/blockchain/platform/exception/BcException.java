package com.blockchain.platform.exception;

import lombok.Data;

/**
 * 项目级异常
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-07-15 1:45 PM
 **/
@Data
public class BcException extends RuntimeException {

    /**
     * 参数
     */
    private String localeKey;

    /**
     * 替换数据
     */
    private Object [] value;

    public BcException(String localeKey, Object [] value){
        this.localeKey = localeKey;
        this.value = value;
    }

    public BcException() {
        super();
    }

    public BcException(String message) {
        super(message);
    }

    public BcException(String message, Throwable cause) {
        super(message, cause);
    }

    public BcException(Throwable cause) {
        super(cause);
    }

    protected BcException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
