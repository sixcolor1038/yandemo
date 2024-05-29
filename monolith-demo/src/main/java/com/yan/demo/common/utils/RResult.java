package com.yan.demo.common.utils;


import com.yan.demo.common.constant.HttpStatus;

import java.io.Serializable;

public class RResult<T> implements Serializable {
    private static final long serialVersionUID = 1L;

    private String code;
    private String message;
    private T data;
    private long total;

    public RResult() {
    }

    public RResult(HttpStatus status) {
        this(HttpStatus.SUCCESS.getCode(), HttpStatus.SUCCESS.getMessage(), null, 0);
    }

    public RResult(String code, T data) {
        this(code, null, data, 0);
    }

    public RResult(String code, String message) {
        this(code, message, null, 0);
    }

    public RResult(String code, String message, T data, long total) {
        this.code = code;
        this.message = message;
        this.data = data;
        this.total = total;
    }

    public static <T> RResult<T> success(T data) {
        return new RResult<>(HttpStatus.SUCCESS.getCode(), HttpStatus.SUCCESS.getMessage(), data, 0);
    }

    public static <T> RResult<T> success(T data, long total) {
        return new RResult<>(HttpStatus.SUCCESS.getCode(), HttpStatus.SUCCESS.getMessage(), data, total);
    }

    public static <T> RResult<T> create(T data) {
        return new RResult<>(HttpStatus.CREATED.getCode(), HttpStatus.CREATED.getMessage(), data, 0);
    }

    public RResult<T> success(String code, T data) {
        this.code = code;
        this.data = data;
        return this;
    }

    public static RResult<String> ok() {
        return new RResult<>(HttpStatus.SUCCESS.getCode(), HttpStatus.SUCCESS.getMessage());
    }

    public RResult<T> success(String code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
        return this;
    }

    public static <T> RResult<T> failed() {
        return new RResult<>(HttpStatus.FAILED.getCode(), HttpStatus.FAILED.getMessage(), null, 0);
    }

    public static <T> RResult<T> fail(String code, String message) {

        return new RResult<>(code, message);
    }

    public static <T> RResult<T> fail(T message) {
        if (message == null) {
            return new RResult<>(HttpStatus.FAILED);
        } else {
            return new RResult<>(HttpStatus.FAILED.getCode(), message);
        }
    }

    public static <T> RResult<T> notFound() {
        return new RResult<>(HttpStatus.NOT_FOUND.getCode(), HttpStatus.NOT_FOUND.getMessage(), null, 0);
    }

    public static <T> RResult<T> notFound(String message) {
        return new RResult<>(HttpStatus.NOT_FOUND.getCode(), message, null, 0);
    }

    public static <T> RResult<T> handleResult(int result, T data) {
        if (result > 0) {
            return RResult.success(data);
        } else {
            return RResult.failed();
        }
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "RResult{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", total=" + total +
                '}';
    }
}
