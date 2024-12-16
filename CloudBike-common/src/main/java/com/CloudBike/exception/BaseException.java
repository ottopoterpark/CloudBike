package com.CloudBike.exception;

/**
 * 业务异常
 * @author unique
 */
public class BaseException extends RuntimeException {

    public BaseException() {
    }

    public BaseException(String msg) {
        super(msg);
    }

}
