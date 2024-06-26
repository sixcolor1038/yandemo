package com.yan.demo.common.utils;

import com.yan.demo.common.exception.BusinessException;
import com.yan.demo.javademo.entity.CommonRec;

/**
 * @Author: sixcolor
 * @Date: 2024-05-22 16:24
 * @Description:
 */
public class CheckVerifyUtil {
    public static void checkCommonRec(CommonRec commonRec) {
        if (null == commonRec.getValue() || null == commonRec.getRemark()) {
            RResult.failed();
            return;
        }
        RResult.ok();
    }

    public static <T> void checkIfExists(T object, String errorMessage) {
        if (object == null) {
            throw new BusinessException(errorMessage);
        }
    }
}
