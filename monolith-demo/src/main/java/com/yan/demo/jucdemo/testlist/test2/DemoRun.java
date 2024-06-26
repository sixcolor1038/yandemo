package com.yan.demo.jucdemo.testlist.test2;

/**
 * @Author: sixcolor
 * @Date: 2024-03-26 16:42
 * @Description:
 */
public class DemoRun implements Runnable {

    /**
     * 有100份礼品，两人同时发送，当剩下的礼品小于10份的时候则不再送出
     * 利用多线程模拟该过程并将线程的名字和礼物的剩下数量打印出来
     */
    public static void main(String[] args) {

        DemoRun run = new DemoRun();
        Thread t1 = new Thread(run);
        Thread t2 = new Thread(run);
        t1.start();
        t2.start();
    }

    static int ticket = 100;

    @Override
    public void run() {
        while (true) {
            synchronized (DemoRun.class) {
                if (ticket < 10) {
                    System.out.println("礼品小于10份，不要再送了");
                    break;
                } else {
                    ticket--;
                    System.out.println(Thread.currentThread().getName() + "当前礼品剩余 " + ticket + "份");
                }
            }
        }
    }
}
