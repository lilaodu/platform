package com.blockchain.platform.exception;

/**
 * 平台授权异常
 *
 * @author DENGLONG
 * @version 1.0
 * @create 2019-07-15 3:29 PM
 **/
public class BcAuthException extends RuntimeException {


    public BcAuthException() {
        super();
    }

    public BcAuthException(String message) {
        super(message);
    }

    public BcAuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public BcAuthException(Throwable cause) {
        super(cause);
    }

    protected BcAuthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
