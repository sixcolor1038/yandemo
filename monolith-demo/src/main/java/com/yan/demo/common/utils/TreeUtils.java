package com.yan.demo.common.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @Author: sixcolor
 * @Date: 2024-05-01 9:05
 * @Description: 将扁平列表转换成树形结构的工具类
 */
public class TreeUtils {

    /**
     * 将扁平列表转换成树形结构
     *
     * @param items       待转换的对象列表
     * @param getId       获取对象ID的函数
     * @param getParentId 获取对象父ID的函数
     * @param setChildren 设置对象子节点列表的函数
     * @param <T>         对象类型
     * @param <ID>        ID类型
     * @return 根节点列表
     * @throws IllegalArgumentException 如果存在相同的ID
     */
    public static <T, ID> List<T> convertToTree(List<T> items, Function<T, ID> getId,
                                                Function<T, ID> getParentId, BiConsumer<T, T> setChildren) {
        if (items == null || getId == null || getParentId == null || setChildren == null) {
            throw new IllegalArgumentException("参数不能为空");
        }

        Map<ID, T> itemMap = new HashMap<>();
        List<T> roots = new ArrayList<>();

        for (T item : items) {
            ID id = getId.apply(item);
            if (itemMap.containsKey(id)) {
                throw new IllegalArgumentException("存在相同的ID: " + id);
            }
            itemMap.put(id, item);
        }

        for (T item : items) {
            ID parentId = getParentId.apply(item);
            if (parentId != null) {
                T parent = itemMap.get(parentId);
                if (parent != null) {
                    setChildren.accept(parent, item);
                } else {
                    // 父节点不存在，将其视为根节点
                    roots.add(item);
                }
            } else {
                // 没有父节点，将其视为根节点
                roots.add(item);
            }
        }

        return roots;
    }

}
