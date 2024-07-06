package com.yan.demo.jucdemo.testlist.test4;

import com.yan.demo.common.utils.randomutils.RandomNumberUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * @Author: sixcolor
 * @Date: 2024-03-26 17:57
 * @Description:
 */
public class DemoThread2 extends Thread {
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
    static BigDecimal money = BigDecimal.valueOf(100);
    //分成3个红包
    static int count = 3;
    //最低红包0.01
    static final BigDecimal MIN = BigDecimal.valueOf(0.01);

    @Override
    public void run() {
        synchronized (DemoThread2.class) {
            if (count == 0) {
                System.out.println(Thread.currentThread().getName() + "没抢到");
            } else {
                //中奖金额
                BigDecimal prize;
                if (count == 1) {
                    prize = money;
                } else {
                    //获取抽奖范围
                    double bounds = money.subtract(BigDecimal.valueOf(count - 1).multiply(MIN)).doubleValue();
                    //抽奖金额
                    prize = BigDecimal.valueOf(RandomNumberUtil.nextDouble(bounds));

                }
                //设置抽中红包保留两位小数点，四舍五入
                prize = prize.setScale(2, RoundingMode.UP);
                //从总金额中去掉对应的钱
                money = money.subtract(prize);
                //红包个数减少
                count--;
                //输出信息
                System.out.println(getName() + "抢到了" + prize + "元");
            }
        }

    }
}

