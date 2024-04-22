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
}
