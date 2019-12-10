package com.blockchain.platform.exception;

/**
 * 登录异常
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-07-15 3:11 PM
 **/
public class BcLoginException extends RuntimeException {


    public BcLoginException() {
        super();
    }

    public BcLoginException(String message) {
        super(message);
    }

    public BcLoginException(String message, Throwable cause) {
        super(message, cause);
    }

    public BcLoginException(Throwable cause) {
        super(cause);
    }

    protected BcLoginException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
