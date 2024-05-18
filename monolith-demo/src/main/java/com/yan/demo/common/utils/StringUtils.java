package com.yan.demo.common.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;

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

    /**
     * 动态选择处理方法。
     *
     * @param input 要检查的输入字符串
     * @return 一个数组，第一个元素是大写字母的数量，第二个元素是小写字母的数量
     */
    public static int[] countLetterCasesDynamic(String input) {
        // 动态选择方法的阈值，可以根据实际情况调整
        int threshold = 50000000;
        if (input.length() < threshold) {
            return countLetterCases(input);
        } else {
            return countLetterCasesByStream(input);
        }
    }

    /**
     * 统计给定字符串中的大写和小写字母数量。
     *
     * @param input 要检查的输入字符串
     * @return 一个数组，第一个元素是大写字母的数量，第二个元素是小写字母的数量
     */
    public static int[] countLetterCases(String input) {
        int upperCaseCount = 0;
        int lowerCaseCount = 0;

        // 将字符串转换为字符数组并遍历
        for (char c : input.toCharArray()) {
            if (Character.isUpperCase(c)) {
                upperCaseCount++;
            } else if (Character.isLowerCase(c)) {
                lowerCaseCount++;
            }
        }

        // 返回包含大写和小写字母数量的数组
        return new int[]{upperCaseCount, lowerCaseCount};
    }

    /**
     * 统计给定字符串中的大写和小写字母数量。
     *
     * @param input 要检查的输入字符串
     * @return 一个数组，第一个元素是大写字母的数量，第二个元素是小写字母的数量
     */
    public static int[] countLetterCasesByStream(String input) {
        // 自定义并行流线程池
        ForkJoinPool customThreadPool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
        try {
            return customThreadPool.submit(() -> {
                long upperCaseCount = input.chars()
                        .parallel()
                        .filter(Character::isUpperCase)
                        .count();
                long lowerCaseCount = input.chars()
                        .parallel()
                        .filter(Character::isLowerCase)
                        .count();
                return new int[]{(int) upperCaseCount, (int) lowerCaseCount};
            }).get();
        } catch (Exception e) {
            e.printStackTrace();
            return new int[]{0, 0};
        }
    }

}
