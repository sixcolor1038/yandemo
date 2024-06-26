package com.yan.demo.jucdemo.ticketdemo;

import java.util.concurrent.Callable;

/**
 * @Author: sixcolor
 * @Date: 2024-03-16 19:06
 * @Description:
 */
public class DemoCall implements Callable<Integer> {
    private static final Object lock = new Object();
    static int ticket = 0;

    @Override
    public Integer call() {
        while (true) {
            synchronized (lock) {
                if (ticket == 100) {
                    break;
                } else {
                    ticket++;
                    System.out.println(Thread.currentThread().getName() + "在卖第" + ticket + "张票");
                }
            }
            Thread.yield();
        }
        return ticket;
    }
}
