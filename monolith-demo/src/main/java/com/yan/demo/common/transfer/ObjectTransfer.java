package com.yan.demo.common.transfer;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: sixcolor
 * @Date: 2024-05-04 18:21
 * @Description:
 */
public class ObjectTransfer {

    /**
     * 将PageInfo类型的Area转为List
     */
    public static <T> List<T> convertListToList(List<T> list) {
        if (list == null) {
            return new ArrayList<>();
        }
        return new ArrayList<>(list);
    }


}
