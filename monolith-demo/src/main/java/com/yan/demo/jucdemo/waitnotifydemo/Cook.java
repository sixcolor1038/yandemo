package com.yan.demo.jucdemo.waitnotifydemo;

/**
 * @Author: sixcolor
 * @Date: 2024-03-16 20:00
 * @Description:
 */
public class Cook extends Thread {

    @Override
    public void run() {
        /*
        1.循环
        2.同步代码块
        3.判断共享数据是否到了末尾(到了末尾)
        4.判断共享数据是否到了末尾(没有到末尾，执行核心逻辑)
         */
        while (true) {
            synchronized (Desk.lock) {
                if (Desk.count == 0) {
                    break;
                } else {
                    //判断是否有可消费数据
                    if (Desk.foodFlag == 1) {
                        try {
                            Desk.lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        System.out.println("生产者生产了一份数据，等待消费者消费");
                        Desk.foodFlag = 1;
                        Desk.lock.notifyAll();
                    }
                }
            }
        }

    }
}
