package com.yan.demo.interviewdemo.demo02;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: sixcolor
 * @Date: 2024-05-03 18:07
 * @Description:
 */
public class ListDemo {
    public static void main(String[] args) {
    
    }

    //根据list中某个字段排序
    private static void extracted1() {
        ListData list1 = new ListData(1, "李浩", 12);
        ListData list2 = new ListData(2, "李白", 8);
        ListData list3 = new ListData(3, "李发", 25);
        ArrayList<ListData> list = new ArrayList<>();
        list.add(list1);
        list.add(list2);
        list.add(list3);
        Optional<ListData> first = list.stream().findFirst();
        System.out.println(first);
        List<ListData> collect = list.stream()
                .sorted(Comparator.comparingInt(ListData::getAge))
                .collect(Collectors.toList());
        System.out.println(collect);
    }

    @Data
    @AllArgsConstructor
    static class ListData {
        int id;
        String name;
        int age;
    }

    //list基础排序
    private static void extracted() {
        List<Integer> list = Arrays.asList(12, 5, 8, 1, 6, 3, 0);
        Collections.sort(list);
        System.out.println(list);
    }
}
