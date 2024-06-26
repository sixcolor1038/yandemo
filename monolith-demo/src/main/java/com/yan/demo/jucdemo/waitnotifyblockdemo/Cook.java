package com.yan.demo.jucdemo.waitnotifyblockdemo;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @Author: sixcolor
 * @Date: 2024-03-16 20:08
 * @Description:
 */
public class Cook extends Thread {
    ArrayBlockingQueue<String> queue;

    public Cook(ArrayBlockingQueue<String> queue) {
        this.queue = queue;
    }

    @Override
    public void run() {
        while (true) {
            try {
                queue.put("数据");
                System.out.println("生产者生产了一份数据");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
