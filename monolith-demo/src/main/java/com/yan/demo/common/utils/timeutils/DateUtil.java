package com.yan.demo.common.utils.timeutils;

import com.yan.demo.common.constant.DateConstant;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;

/**
 * @Author: sixcolor
 * @Date: 2024-06-04 11:13
 * @Description: 日期相关的工具类
 */
public class DateUtil {


    /**
     * 计算输入日期和当前日期之间相差的天数
     * 日期格式为yyyy-MM-dd
     * 计算出的数据都是正数
     */
    public static long getDayBetweenDay(String str) {
        // 解析用户输入的日期
        LocalDate date = LocalDate.parse(str, DateConstant.DATE_FORMAT);

        // 获取当前日期
        LocalDate currentDate = LocalDate.now();
        // 计算两个日期之间的天数差
        long daysBetween;
        if (date.isBefore(currentDate)) {
            daysBetween = ChronoUnit.DAYS.between(date, currentDate);
        } else {
            daysBetween = ChronoUnit.DAYS.between(currentDate, date);
        }
        return daysBetween;
    }

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
