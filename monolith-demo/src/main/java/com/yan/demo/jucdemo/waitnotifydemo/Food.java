package com.yan.demo.jucdemo.waitnotifydemo;

/**
 * @Author: sixcolor
 * @Date: 2024-03-16 20:00
 * @Description:
 */
public class Food extends Thread {
    @Override
    public void run() {
        /*
        1.循环
        2.同步代码块
        3.判断共享数据是否到了末尾(到了末尾)
        4.判断共享数据是否到了末尾(没有到末尾，执行核心逻辑)
         */
        while (true){
            synchronized (Desk.lock){
                if (Desk.count == 0){
                    break;
                }else {
                    //先判断是否有可消费数据
                    if (Desk.foodFlag==0){
                        //如果没有可消费数据,就等待
                        try {
                            Desk.lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }else {
                        //进入这里表示已经消费过一次了，所以要先减一次
                        Desk.count--;
                        //如果有，就唤醒消费者消费
                        System.out.println("消费者开始消费，还能消费"+Desk.count+"次");
                        //消费完成后，修改消费状态为无可消费数据
                        Desk.foodFlag = 0;
                        //唤醒生产者生产数据
                        Desk.lock.notifyAll();
                    }
                }
            }
        }
    }
}
