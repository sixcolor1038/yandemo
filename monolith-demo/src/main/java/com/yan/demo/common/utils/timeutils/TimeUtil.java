package com.yan.demo.common.utils.timeutils;

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
}
