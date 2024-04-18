package com.yan.demo.common.utils;

/**
 * @Author: sixcolor
 * @Date: 2024-04-17 16:15
 * @Description:
 */
public class StringUtils {
    /**
     * 生成重复的字符串
     *
     * @param str   要重复的字符串
     * @param count 重复的次数
     * @return 生成的重复字符串
     */
    public static String repeatString(String str, int count) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < count; i++) {
            sb.append(str);
        }
        return sb.toString();
    }
}
