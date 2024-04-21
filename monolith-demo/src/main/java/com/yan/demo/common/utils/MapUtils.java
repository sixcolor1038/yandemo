package com.yan.demo.common.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * @Author: sixcolor
 * @Date: 2024-04-21 10:15
 * @Description:
 */
public class MapUtils {

    /**
     * 统计Map中多种文件类型的数量
     *
     * @param stats     包含文件统计信息的Map
     * @param fileTypes 要统计的文件类型列表，例如 ["jpg", "png"]
     * @return 包含每种文件类型数量的Map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Integer> countMapByKey(Map<String, Object> stats, List<String> fileTypes) {
        Map<String, Integer> counts = new HashMap<>();
        fileTypes.forEach(fileType -> counts.put(fileType, 0));

        Stack<Map<String, Object>> stack = new Stack<>();
        stack.push(stats);

        while (!stack.isEmpty()) {
            Map<String, Object> currentMap = stack.pop();
            for (Map.Entry<String, Object> entry : currentMap.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();

                if (value instanceof Map) {
                    stack.push((Map<String, Object>) value);
                } else if (value instanceof Integer && counts.containsKey(key)) {
                    counts.put(key, counts.get(key) + (int) value);
                }
            }
        }
        return counts;
    }
}
