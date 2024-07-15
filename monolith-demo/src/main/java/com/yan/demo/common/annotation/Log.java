package com.yan.demo.common.annotation;

import java.lang.annotation.*;

/**
 * @Author: sixcolor
 * @Date: 2024-04-24 15:14
 * @Description:
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Log {
    String value() default "";
}
