package com.yan.demo.jucdemo.waitnotifyblockdemo;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @Author: sixcolor
 * @Date: 2024-03-16 20:10
 * @Description:
 */
public class Food extends Thread {
    ArrayBlockingQueue<String> queue;

    public Food(ArrayBlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String food = queue.take();
                System.out.println(food);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
