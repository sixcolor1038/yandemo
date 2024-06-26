package com.yan.demo.jucdemo.waitnotifydemo;

/**
 * @Author: sixcolor
 * @Date: 2024-03-16 20:01
 * @Description: 用于控制生产者和消费者的执行
 */
public class Desk {
    //判断是否有可消费的数据，0：无可消费数据；1：有
    public static int foodFlag = 0;

    //最大消费个数
    public static int count = 10;

    //锁对象
    public static final Object lock = new Object();
}
