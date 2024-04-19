package com.yan.demo.common.utils;

/**
 * @Author: sixcolor
 * @Date: 2024-04-18 14:45
 * @Description:
 */
public class ObjectUtils {
    /**
     * 将 Object 转换为 int 类型
     *
     * @param obj 要转换的 Object
     * @return 转换后的 int 值，如果转换失败则返回 0
     */
    public static int objectToInt(Object obj) {
        if (obj instanceof Integer) {
            return (int) obj;
        } else if (obj instanceof String) {
            try {
                return Integer.parseInt((String) obj);
            } catch (NumberFormatException e) {
                System.out.println("String类型不能被转换为int类型: " + obj);
            }
        }
        // 其他类型的处理
        return 0;
    }
}
