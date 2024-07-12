package com.yan.demo.common.constant;

import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

/**
 * @Author: sixcolor
 * @Date: 2024-04-17 15:04
 * @Description: 日期时间常量
 */
public class DateConstant {
    /**
     * 默认日期时间格式
     */
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    /**
     * 格式化日期
     */
    public static final DateTimeFormatter DATE_TIME_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * 默认日期格式
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
    /**
     * 格式化日期时间
     */
    public static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    /**
     * 默认时间格式
     */
    public static final String DEFAULT_TIME_FORMAT = "HH:mm:ss";
    /**
     * 时间戳
     */
    public static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    /**
     * 时间戳，精确到毫秒
     */
    public static final DateTimeFormatter TIMESTAMP_FORMAT_MILLIS = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS");

    /**
     * 时间戳，精确到纳秒
     */
    public static final DateTimeFormatter TIMESTAMP_FORMAT_NANOS = DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSSSSSSSS");

    /**
     * 时间戳格式
     */
    public static final Pattern TIMESTAMP_PATTERN = Pattern.compile("^\\d{14}$");
    /**
     * 时间戳格式，精确到毫秒
     */
    public static final Pattern TIMESTAMP_PATTERN_MILLIS = Pattern.compile("^\\d{17}$");

    /**
     * 时间戳格式，精确到毫秒
     */
    public static final Pattern TIMESTAMP_PATTERN_NANOS = Pattern.compile("^\\d{23}$");

}
