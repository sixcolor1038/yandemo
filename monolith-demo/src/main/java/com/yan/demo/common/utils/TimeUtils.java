package com.yan.demo.common.utils;

import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;

/**
 * @Author: sixcolor
 * @Date: 2024-04-22 14:46
 * @Description:
 */
public class TimeUtils {
    /**
     * 获取指定年月的最后一天
     *
     * @param year  指定的年份
     * @param month 指定的月份
     * @return 指定年月的最后一天
     */
    public static LocalDate getLastDayOfMonth(int year, int month) {
        return LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * 获取当前月份的最后一天
     *
     * @return 当前月份的最后一天
     */
    public static LocalDate getLastDayOfCurrentMonth() {
        return LocalDate.now().with(TemporalAdjusters.lastDayOfMonth());
    }

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
}
