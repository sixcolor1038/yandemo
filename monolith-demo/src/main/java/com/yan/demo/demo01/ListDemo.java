package com.yan.demo.demo01;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Author: sixcolor
 * @Date: 2024-04-09 15:59
 * @Description:
 */
public class ListDemo {
    public static void main(String[] args) {


        List<Data> list = new ArrayList<>();
        list.add(new Data("1", "2"));
        list.add(new Data("2", "4"));
        list.add(new Data("3", "6"));

        // 使用Stream API计算值的总和
        int sum = list.stream()
                .mapToInt(data -> Integer.parseInt(data.getProgress()))
                .sum();

        // 打印结果
        System.out.println("总计: " + sum);
    }

    private static void extracted() {
        int seconds = 12; // 12秒

        // 计算小时数
        int hours = seconds / 3600;

        // 计算剩余分钟数
        int remainingSeconds = seconds % 3600;
        int minutes = remainingSeconds / 60;
        if (remainingSeconds % 60 > 0) {
            minutes++; // 如果剩余秒数大于0，则分钟数加1
        }

        // 格式化输出
        String formattedTime = formatTime(hours, minutes);
        System.out.println(formattedTime);
    }

    public static String formatTime(int hours, int minutes) {
        if (hours > 0 && minutes > 0) {
            return hours + "h" + minutes + "m";
        } else if (hours > 0) {
            return hours + "h";
        } else {
            return minutes + "m";
        }
    }

    private static void calculatePercent() {
        List<Data> dataList = Arrays.asList(
                new Data("1", "0.00"),
                new Data("2", "0.25"),
                new Data("3", "1"),
                new Data("4", "1")
        );

        double totalProgress = 0.0;
        for (Data data : dataList) {
            totalProgress += Double.parseDouble(data.getProgress());
        }

        double averageProgress = totalProgress / dataList.size();
        double percentage = averageProgress * 100;

        System.out.println("百分比值为：" + percentage + "%");
    }

    static class Data {
        private String id;
        private String progress;

        public Data(String id, String progress) {
            this.id = id;
            this.progress = progress;
        }

        public String getId() {
            return id;
        }

        public String getProgress() {
            return progress;
        }
    }

    private static void unmodifiableList() {
        List<String> list = new ArrayList<>(Arrays.asList("111", "222", "333"));
        System.out.println(list);
        list.add("aaa");
        List<String> unmodifiableList = Collections.unmodifiableList(list);
        System.out.println(unmodifiableList);
        list.add("bbb");
        System.out.println(list);
        List<String> copy = new ArrayList<>();
        copy.add("ccc");
        System.out.println(copy);
        System.out.println("ddd");
    }
}
