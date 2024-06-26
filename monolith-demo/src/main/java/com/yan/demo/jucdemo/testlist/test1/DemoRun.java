package com.yan.demo.jucdemo.testlist.test1;

/**
 * @Author: sixcolor
 * @Date: 2024-03-26 16:29
 * @Description:
 */
public class DemoRun implements Runnable{

    /**
     * 一共有1000张电影票，可以在两个串口领取，假设每次领取的时间为3000毫秒
     * 要求：请用多线程模拟买票过程并打印剩余电影票数量
     */
    public static void main(String[] args) {
        DemoRun run = new DemoRun();
        Thread t1 = new Thread(run);
        Thread t2 = new Thread(run);
        t1.start();
        t2.start();
    }

    static int ticket = 1000;
    @Override
    public void run() {
        //1.循环
        while (true){
            //2.同步代码块
            synchronized (DemoRun.class){
                //3.判断是否卖完了
                if (ticket == 0){
                    break;
                }else {
                    ticket--;
                    System.out.println(Thread.currentThread().getName()+"剩余电影票数量："+ticket);
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            //礼让线程
            Thread.yield();
        }
    }
}
