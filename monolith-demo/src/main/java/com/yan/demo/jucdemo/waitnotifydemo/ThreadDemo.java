package com.yan.demo.jucdemo.waitnotifydemo;

/**
 * @Author: sixcolor
 * @Date: 2024-03-16 20:01
 * @Description:
 */
public class ThreadDemo {
    public static void main(String[] args) {
        Cook cook = new Cook();
        Food food = new Food();
        cook.setName("生产者");
        food.setName("消费者");
        cook.start();
        food.start();
    }
}
