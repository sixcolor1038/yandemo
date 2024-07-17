package com.yan.demo.common.exception.other;

import com.yan.demo.common.enums.CommonErrorEnum;
import lombok.Data;

/**
 * @Author: sixcolor
 * @Date: 2024-02-16 21:21
 * @Description: 业务异常
 */
@Data
public class BusinessException extends RuntimeException {

    protected Integer errorCode;

    protected String errorMsg;

    public BusinessException(String errorMsg) {
        super(errorMsg);
        this.errorCode = CommonErrorEnum.BUSINESS_ERROR.getErrorCode();
        this.errorMsg = errorMsg;
    }

    public BusinessException(Integer errorCode, String errorMsg) {
        super(errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}
