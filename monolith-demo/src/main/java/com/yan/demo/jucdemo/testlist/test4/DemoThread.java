package com.yan.demo.jucdemo.testlist.test4;

import com.yan.demo.common.utils.randomutils.RandomNumberUtil;

/**
 * @Author: sixcolor
 * @Date: 2024-03-26 17:57
 * @Description:
 */
public class DemoThread extends Thread {
    /**
     * 抢红包使用多线程
     * 假设：100块，分成了3个包，现在有5个人抢
     * 其中，红包是共享数据
     * 5个人是5条线程
     * 打印结果如下：
     * xxx抢到了xxx元
     * xxx抢到了xxx元
     * xxx抢到了xxx元
     * xxx没抢到
     * xxx没抢到
     */
    public static void main(String[] args) {
        //创建线程
        DemoThread2 t1 = new DemoThread2();
        DemoThread2 t2 = new DemoThread2();
        DemoThread2 t3 = new DemoThread2();
        DemoThread2 t4 = new DemoThread2();
        DemoThread2 t5 = new DemoThread2();
        //设置线程名称
        t1.setName("第一");
        t2.setName("Hayes");
        t3.setName("大A");
        t4.setName("Jack");
        t5.setName("猫猫");
        //启动线程
        t1.start();
        t2.start();
        t3.start();
        t4.start();
        t5.start();


    }

    //共享数据
    static double money = 100;
    //分成3个红包
    static int count = 3;
    //最低红包0.01
    static final double MIN = 0.01;

    @Override
    public void run() {
        synchronized (DemoThread2.class) {
            if (count == 0) {
                System.out.println(Thread.currentThread().getName() + "没抢到");
            } else {
                double prize = 0;
                if (count == 1) {
                    prize = money;
                } else {
                    double bounds = money - (count - 1) * MIN;
                    prize = RandomNumberUtil.nextDouble(bounds);
                    if (prize < MIN) {
                        prize = MIN;
                    }
                }
                money = money - prize;
                count--;
                System.out.println(getName() + "抢到了" + prize + "元");
            }
        }

    }
}
