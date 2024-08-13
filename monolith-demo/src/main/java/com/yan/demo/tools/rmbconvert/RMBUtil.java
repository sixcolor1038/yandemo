package com.yan.demo.tools.rmbconvert;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @Author: sixcolor
 * @Date: 2024-08-13
 * @Description:
 */

public class RMBUtil {

    // 汉字数字
    private static final String[] CN_UPPER_NUMBER = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
    // 汉字单位
    private static final String[] CN_UPPER_MONETRAY_UNIT = {"分", "角", "元", "拾", "佰", "仟", "万", "拾", "佰", "仟", "亿", "拾", "佰", "仟", "万"};
    // 特殊字符：整
    private static final String CN_FULL = "整";
    // 特殊字符：负
    private static final String CN_NEGATIVE = "负";
    // 零元整
    private static final String CN_ZERO_FULL = "零元" + CN_FULL;

    /**
     * 将金额数字转换为中文大写金额
     *
     * @param amount 金额
     * @return 中文大写金额
     */
    public static String numberToCN(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) == 0) {
            return CN_ZERO_FULL;
        }

        StringBuilder sb = new StringBuilder();
        int signum = amount.signum();

        // 取绝对值
        amount = amount.abs();

        // 取小数部分
        long number = amount.movePointRight(2).setScale(0, RoundingMode.HALF_UP).longValue();
        int scale = (int) (number % 100);
        int unitIndex = 0;

        // 处理小数部分
        if (scale == 0) {
            unitIndex = 2;
            number /= 100;
            sb.append(CN_FULL);
        } else {
            if (scale % 10 == 0) {
                unitIndex = 1;
                number /= 10;
                sb.append(CN_UPPER_NUMBER[scale / 10]).append(CN_UPPER_MONETRAY_UNIT[1]);
            } else {
                sb.append(CN_UPPER_NUMBER[scale % 10]).append(CN_UPPER_MONETRAY_UNIT[0]);
                sb.insert(0, CN_UPPER_NUMBER[scale / 10] + CN_UPPER_MONETRAY_UNIT[1]);
            }
            unitIndex = 2;
            number /= 100;
        }

        int zeroCount = 0;
        while (number > 0) {
            int numUnit = (int) (number % 10);
            if (numUnit > 0) {
                if (zeroCount > 0) {
                    sb.insert(0, CN_UPPER_NUMBER[0]);
                }
                sb.insert(0, CN_UPPER_NUMBER[numUnit] + CN_UPPER_MONETRAY_UNIT[unitIndex]);
                zeroCount = 0;
            } else {
                zeroCount++;
                if (unitIndex == 2 || unitIndex == 6 || unitIndex == 10) { // 元、万、亿的位置
                    sb.insert(0, CN_UPPER_MONETRAY_UNIT[unitIndex]);
                }
            }
            number /= 10;
            unitIndex++;
        }

        // 处理壹拾的情况
        String result = sb.toString().replaceAll("壹拾", "拾");

        // 如果signum == -1，则说明是负数
        if (signum == -1) {
            result = CN_NEGATIVE + result;
        }

        return result;
    }


}