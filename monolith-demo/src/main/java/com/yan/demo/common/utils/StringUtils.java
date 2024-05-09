package com.yan.demo.common.utils;

import java.util.ArrayList;
import java.util.List;

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


    /**
     * 将传入的字符串分割为单个字符的列表。
     *
     * @param input 需要分割的字符串
     * @return 分割后的字符列表
     */
    public static List<String> splitIntoCharacters(String input) {
        List<String> characters = new ArrayList<>();
        if (input != null && !input.isEmpty()) {
            for (int i = 0; i < input.length(); i++) {
                characters.add(String.valueOf(input.charAt(i)));
            }
        }
        return characters;
    }

    /**
     * 使用逗号作为分隔符来分割字符串，并返回一个字符串数组。
     * 如果字符串以逗号结尾，不会包含由尾随逗号产生的空字符串。
     *
     * @param str 要分割的字符串
     * @return 根据逗号分割的字符串数组。如果输入是null，返回一个空数组。
     */
    public static String[] splitStrByComma(String str) {
        if (str == null) {
            return new String[0];
        }
        return str.split(",\\s*");
    }
}
