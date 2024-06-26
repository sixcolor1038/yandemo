package com.yan.demo.jucdemo.waitnotifyblockdemo;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @Author: sixcolor
 * @Date: 2024-03-16 20:20
 * @Description:
 */
public class ThreadDemo {
    public static void main(String[] args) {
        //1.创建阻塞队列的对象
        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(1);
        //2.创建线程对象，并把阻塞队列传过去
        Cook cook = new Cook(queue);
        Food food = new Food(queue);
        //3.开启线程
        cook.start();
        food.start();


    }
}
