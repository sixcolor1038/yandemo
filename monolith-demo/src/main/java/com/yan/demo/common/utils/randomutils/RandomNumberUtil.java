package com.yan.demo.common.utils.randomutils;

import java.util.Random;

/**
 * @Author: sixcolor
 * @Date: 2024-03-26 18:43
 * @Description:
 */
public class RandomNumberUtil {
    private static final Random RANDOM = new Random();

    /**
     * 生成随机数
     * @param start 起始值
     * @param end 截止值
     * @param isInteger 是否生成整数
     * @param decimalPlaces 小数点位数（仅当isInteger为false时有效）
     * @return 随机生成的数
     */
    public static Number generateRandomNumber(double start, double end, boolean isInteger, int decimalPlaces) {
        if (start > end) {
            throw new IllegalArgumentException("起始值不能大于截止值");
        }

        if (isInteger) {
            return generateRandomInteger((int) start, (int) end);
        } else {
            return generateRandomDouble(start, end, decimalPlaces);
        }
    }

    private static int generateRandomInteger(int start, int end) {
        return start + RANDOM.nextInt(end - start + 1);
    }

    private static double generateRandomDouble(double start, double end, int decimalPlaces) {
        double randomValue = start + (end - start) * RANDOM.nextDouble();
        return round(randomValue, decimalPlaces);
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException("小数点位数不能为负数");

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    /**
     * 生成随机浮点数
     * 生成一个 0 到指定 bounds 值之间的随机浮点数
     *
     * @param bounds 浮点数上限
     * @return 随机生成的浮点数
     */
    public static double nextDouble(double bounds) {
        if (bounds <= 0.0) {
            throw new IllegalArgumentException("边界值必须为正数");
        }
        Random random = new Random();
        return random.nextDouble() * bounds;
    }
}
