package com.yan.demo.common.constant;

import java.time.format.DateTimeFormatter;
import java.util.regex.Pattern;

/**
 * @Author: sixcolor
 * @Date: 2024-04-17 15:04
 * @Description:
 */
public class DateConstant {
    /**
     * 时间戳
     */
    public static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    /**
     * 时间戳格式
     */
    public static final Pattern TIMESTAMP_PATTERN = Pattern.compile("^\\d{14}$");

}
