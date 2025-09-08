package com.yan.demo.common.utils.timeutils;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * @Author: sixcolor
 * @Date: 2024-04-22 14:46
 * @Description:
 */
public class TimeUtil {


    /**
     * 将秒转为特定时间格式，如6000秒 转为 1h40m
     */
    public static String formatMillToHM(int time) {
        int hours = time / 3600;
        int remainSeconds = time % 3600;
        int minutes = remainSeconds / 60;
        if (remainSeconds % 60 > 0) {
            minutes++;
        }
        return formatTimeHM(hours, minutes);
    }

    private static String formatTimeHM(int hours, int minutes) {
        if (hours > 0 && minutes > 0) {
            return hours + "h" + minutes + "m";
        } else if (hours > 0) {
            return hours + "h";
        } else {
            return minutes + "m";
        }
    }

    /**
     * 将时间戳（毫秒）转换成具体的时间字符串
     *
     * @param timestamp 时间戳（毫秒）
     * @param pattern   时间格式，如 "yyyy-MM-dd HH:mm:ss"
     * @return 格式化后的时间字符串
     */
    public static String timestampToString(long timestamp, String pattern) {
        // 将时间戳转换为Instant对象
        Instant instant = Instant.ofEpochMilli(timestamp);
        // 转换为本地时间
        LocalDateTime localDateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        // 格式化时间
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return localDateTime.format(formatter);
    }
}
