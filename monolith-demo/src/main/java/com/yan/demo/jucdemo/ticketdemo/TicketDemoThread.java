package com.yan.demo.jucdemo.ticketdemo;

/**
 * @Author: sixcolor
 * @Date: 2024-03-16 18:49
 * @Description:
 */
public class TicketDemoThread extends Thread {
    //加上static表示共享，没有的话开启多线程比如100张票，它会导致卖出300张，一张票卖三次
    static int ticket = 0;

    //锁对象一定要唯一
    //static Object obj = new Object();

    @Override
    public void run() {
        while (true) {
            //字节码文件对象是唯一的
            synchronized (TicketDemoThread.class) {
                if (ticket < 100) {
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    ticket++;
                    System.out.println(getName() + "正在卖第 " + ticket + " 张票");
                } else {
                    break;
                }
            }
        }
    }
}
