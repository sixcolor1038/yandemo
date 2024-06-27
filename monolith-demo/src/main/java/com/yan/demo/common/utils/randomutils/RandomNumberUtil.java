package com.yan.demo.common.utils.randomutils;

import java.util.Random;

/**
 * @Author: sixcolor
 * @Date: 2024-03-26 18:43
 * @Description:
 */
public class RandomNumberUtil {

    public static double nextDouble(Random random, double bounds) {
        if (bounds <= 0.0) {
            throw new IllegalArgumentException("边界值必须为正数");
        }
        return random.nextDouble() * bounds;
    }
}
