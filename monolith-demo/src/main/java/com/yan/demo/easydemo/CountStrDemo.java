package com.yan.demo.easydemo;

import com.yan.demo.common.utils.StringUtils;

import java.util.concurrent.ForkJoinPool;

/**
 * @Author: sixcolor
 * @Date: 2024-05-18 16:58
 * @Description:
 */
public class CountStrDemo {

    public static void main(String[] args) {
        for (int i = 0; i < 10; i++) {
            extracted();
        }

    }

    private static void extracted() {
        String inputN = "7h* QHelloWorld123";
        String input = StringUtils.repeatString(inputN, 10000000);
        System.out.println("input字符串的长度：" + input.length());

        // 运行多次测试以获得平均时间
        int iterations = 10;

        // 测试传统for循环方法的平均性能
        long totalTimeFor = 0;
        for (int i = 0; i < iterations; i++) {
            long timeMillis = System.currentTimeMillis();
            int[] counts = countLetterCases(input);
            long timeMillis1 = System.currentTimeMillis();
            totalTimeFor += (timeMillis1 - timeMillis);
        }
        System.out.println("传统for循环方法的平均耗时: " + (totalTimeFor / iterations) + " 毫秒");

        // 测试并行流方法的平均性能
        long totalTimeStream = 0;
        for (int i = 0; i < iterations; i++) {
            long timeMillis3 = System.currentTimeMillis();
            int[] countsStream = countLetterCasesByStream(input);
            long timeMillis2 = System.currentTimeMillis();
            totalTimeStream += (timeMillis2 - timeMillis3);
        }
        System.out.println("并行流方法的平均耗时: " + (totalTimeStream / iterations) + " 毫秒");

        // 动态选择方法测试
        long dynamicTime = System.currentTimeMillis();
        int[] dynamicCounts = countLetterCasesDynamic(input);
        long dynamicTimeEnd = System.currentTimeMillis();
        System.out.println("动态选择方法的耗时: " + (dynamicTimeEnd - dynamicTime) + " 毫秒");
        System.out.println("大写字母的数量: " + dynamicCounts[0]);
        System.out.println("小写字母的数量: " + dynamicCounts[1]);
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

    /**
     * 动态选择处理方法。
     *
     * @param input 要检查的输入字符串
     * @return 一个数组，第一个元素是大写字母的数量，第二个元素是小写字母的数量
     */
    public static int[] countLetterCasesDynamic(String input) {
        // 动态选择方法的阈值，可以根据实际情况调整
        int threshold = 40000000;
        if (input.length() < threshold) {
            return countLetterCases(input);
        } else {
            return countLetterCasesByStream(input);
        }
    }
}
