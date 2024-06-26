package com.yan.demo.jucdemo.basedemo;

/**
 * @Author: sixcolor
 * @Date: 2024-03-16 12:42
 * @Description:
 */
public class DemoRun implements Runnable {
    @Override
    public void run() {
        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName() + " :  " + i);
        }
    }
}
