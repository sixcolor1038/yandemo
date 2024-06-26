package com.yan.demo.jucdemo.testlist.test3;

/**
 * @Author: sixcolor
 * @Date: 2024-03-26 17:14
 * @Description:
 */
public class DemoRun implements Runnable {

    /**
     * 同时开启两个线程，共同获取1-100之间的所有数字
     * 要求：将输出所有奇数
     */
    public static void main(String[] args) {
        DemoRun run = new DemoRun();
        Thread t1 = new Thread(run);
        Thread t2 = new Thread(run);
        t1.start();
        t2.start();
    }

    static int num = 1;

    @Override
    public void run() {
        while (true) {
            synchronized (DemoRun.class) {
                if (num > 100) {
                    break;
                } else {
                    if (num % 2 != 0) {
                        System.out.println(Thread.currentThread().getName() + "当前数字： " + num);
                    }
                    num++;
                }
            }
        }
    }
}
