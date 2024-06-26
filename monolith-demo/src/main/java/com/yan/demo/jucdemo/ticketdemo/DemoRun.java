package com.yan.demo.jucdemo.ticketdemo;

/**
 * @Author: sixcolor
 * @Date: 2024-03-16 18:55
 * @Description:
 */
public class DemoRun implements Runnable {

    static int ticket = 0;

    @Override
    public void run() {
        //1.循环
        while (true) {
            //2.同步代码块(同步方法)
            synchronized (DemoRun.class) {
                //3.判断共享数据是否到了末尾
                if (ticket == 100) {
                    break;
                } else {
                    //4.判断如果没到末尾就继续执行
                    ticket++;
                    System.out.println(Thread.currentThread().getName() + "在卖第" + ticket + "张票");
                }
            }
            Thread.yield();
        }
    }
}
