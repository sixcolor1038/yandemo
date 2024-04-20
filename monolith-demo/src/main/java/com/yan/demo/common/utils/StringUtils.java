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
}
