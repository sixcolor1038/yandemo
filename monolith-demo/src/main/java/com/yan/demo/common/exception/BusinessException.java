package com.yan.demo.common.exception;

/**
 * @Author: sixcolor
 * @Date: 2024-05-28 19:12
 * @Description:
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
