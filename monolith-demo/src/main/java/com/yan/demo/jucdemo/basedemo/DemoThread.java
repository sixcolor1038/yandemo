package com.yan.demo.jucdemo.basedemo;

/**
 * @Author: sixcolor
 * @Date: 2024-03-16 12:40
 * @Description:
 */
public class DemoThread extends Thread {

    public DemoThread() {
    }

    public DemoThread(String name) {
        super(name);
    }

    @Override
    public void run() {
        for (int i = 1; i <= 100; i++) {
            System.out.println(getName() + " :  " + i);

            //出让线程，表示出让当前线程的cpu执行权
            //只是尽可能使执行均匀，但并不绝对
            //Thread.yield();
        }
    }
}
