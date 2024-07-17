package com.yan.demo.common.utils;


import com.yan.demo.common.enums.CommonErrorEnum;
import com.yan.demo.common.enums.ErrorEnum;
import com.yan.demo.common.exception.other.BusinessException;
import org.hibernate.validator.HibernateValidator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.*;

/**
 * @Author: sixcolor
 * @Date: 2024-02-16 22:48
 * @Description: 校验工具类
 */
public class AssertUtil {

    /**
     * 校验到失败就结束
     */
    private static Validator failFastValidator = Validation.byProvider(HibernateValidator.class)
            .configure()
            .failFast(true)
            .buildValidatorFactory().getValidator();

    /**
     * 全部校验
     */
    private static Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    /**
     * 注解验证参数(校验到失败就结束)
     *
     * @param obj
     */
    public static <T> void fastFailValidate(T obj) {
        Set<ConstraintViolation<T>> constraintViolations = failFastValidator.validate(obj);
        if (constraintViolations.size() > 0) {
            throwException(CommonErrorEnum.PARAM_INVALID, constraintViolations.iterator().next().getMessage());
        }
    }

    /**
     * 注解验证参数(全部校验,抛出异常)
     *
     * @param obj
     */
    public static <T> void allCheckValidateThrow(T obj) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
        if (constraintViolations.size() > 0) {
            StringBuilder errorMsg = new StringBuilder();
            Iterator<ConstraintViolation<T>> iterator = constraintViolations.iterator();
            while (iterator.hasNext()) {
                ConstraintViolation<T> violation = iterator.next();
                //拼接异常信息
                errorMsg.append(violation.getPropertyPath().toString()).append(":").append(violation.getMessage()).append(",");
            }
            //去掉最后一个逗号
            throwException(CommonErrorEnum.PARAM_INVALID, errorMsg.toString().substring(0, errorMsg.length() - 1));
        }
    }


    /**
     * 注解验证参数(全部校验,返回异常信息集合)
     *
     * @param obj
     */
    public static <T> Map<String, String> allCheckValidate(T obj) {
        Set<ConstraintViolation<T>> constraintViolations = validator.validate(obj);
        if (constraintViolations.size() > 0) {
            Map<String, String> errorMessages = new HashMap<>();
            Iterator<ConstraintViolation<T>> iterator = constraintViolations.iterator();
            while (iterator.hasNext()) {
                ConstraintViolation<T> violation = iterator.next();
                errorMessages.put(violation.getPropertyPath().toString(), violation.getMessage());
            }
            return errorMessages;
        }
        return new HashMap<>();
    }

    //如果不是true，则抛异常
    public static void isTrue(boolean expression, String msg) {
        if (!expression) {
            throwException(msg);
        }
    }

    public static void isTrue(boolean expression, ErrorEnum errorEnum, Object... args) {
        if (!expression) {
            throwException(errorEnum, args);
        }
    }

    //如果是true，则抛异常
    public static void isFalse(boolean expression, String msg) {
        if (expression) {
            throwException(msg);
        }
    }

    //如果是true，则抛异常
    public static void isFalse(boolean expression, ErrorEnum errorEnum, Object... args) {
        if (expression) {
            throwException(errorEnum, args);
        }
    }

    //如果不是非空对象，则抛异常
    public static void isNotEmpty(Object obj, String msg) {
        if (isEmpty(obj)) {
            throwException(msg);
        }
    }

    //如果不是非空对象，则抛异常
    public static void isNotEmpty(Object obj, ErrorEnum errorEnum, Object... args) {
        if (isEmpty(obj)) {
            throwException(errorEnum, args);
        }
    }

    //如果不是非空对象，则抛异常
    public static void isEmpty(Object obj, String msg) {
        if (!isEmpty(obj)) {
            throwException(msg);
        }
    }

    public static void equal(Object o1, Object o2, String msg) {
        if (!equal(o1, o2)) {
            throwException(msg);
        }
    }

    public static void notEqual(Object o1, Object o2, String msg) {
        if (equal(o1, o2)) {
            throwException(msg);
        }
    }

    public static boolean equal(Object obj1, Object obj2) {
        return obj1 instanceof Number && obj2 instanceof Number ? equals((Number) obj1, (Number) obj2) : Objects.equals(obj1, obj2);
    }

    public static boolean equals(Number number1, Number number2) {
        return number1 instanceof BigDecimal && number2 instanceof BigDecimal ? equals((BigDecimal) number1, (BigDecimal) number2) : Objects.equals(number1, number2);
    }

    private static boolean isEmpty(Object obj) {
        return isObjEmpty(obj);
    }

    private static boolean isObjEmpty(Object obj) {
        if (null == obj) {
            return true;
        } else if (obj instanceof CharSequence) {
            return isStrEmpty((CharSequence) obj);
        } else if (obj instanceof Map) {
            return isMapEmpty((Map) obj);
        } else if (obj instanceof Iterable) {
            return isIterableEmpty((Iterable) obj);
        } else if (obj instanceof Iterator) {
            return isIteratorEmpty((Iterator) obj);
        } else {
            return isObjArray(obj) ? isArrayEmpty(obj) : false;
        }
    }

    private static boolean isStrEmpty(CharSequence str) {
        return str == null || str.length() == 0;
    }

    private static boolean isMapEmpty(Map<?, ?> map) {
        return null == map || map.isEmpty();
    }

    private static boolean isIterableEmpty(Iterable<?> iterable) {
        return null == iterable || isEmpty(iterable.iterator());
    }

    private static boolean isIteratorEmpty(Iterator<?> Iterator) {
        return null == Iterator || !Iterator.hasNext();
    }

    private static boolean isObjArray(Object obj) {
        return null != obj && obj.getClass().isArray();
    }

    private static boolean isArrayEmpty(Object array) {
        if (array != null) {
            if (isArray(array)) {
                return 0 == Array.getLength(array);
            } else {
                return false;
            }
        } else {
            return true;
        }
    }

    private static boolean isArray(Object obj) {
        return null != obj && obj.getClass().isArray();
    }

    private static void throwException(String msg) {
        throwException(null, msg);
    }

    private static void throwException(ErrorEnum errorEnum, Object... arg) {
        if (Objects.isNull(errorEnum)) {
            errorEnum = CommonErrorEnum.BUSINESS_ERROR;
        }
        throw new BusinessException(errorEnum.getErrorCode(), MessageFormat.format(errorEnum.getErrorMsg(), arg));
    }


}
