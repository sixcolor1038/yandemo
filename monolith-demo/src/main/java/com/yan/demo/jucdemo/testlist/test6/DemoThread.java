package com.yan.demo.jucdemo.testlist.test6;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @Author: sixcolor
 * @Date: 2024-03-26 20:48
 * @Description:
 */
public class DemoThread extends Thread {

    /**
     * 在上一题的基础上继续完成如下需求：
     * 每次抽奖的过程中，不打印，抽完时一次性随机打印
     * 在此次抽奖过程中，抽奖箱1总共诞生了6个奖项
     * 分别是：10,20,100,500,2,300最高奖项为300元，总额为932元
     * 在此次抽奖过程中，抽奖箱2共产生了6个奖项
     * 分别是：5,50,200,800,80,700最高奖项为800元，总额为1835元
     */
    public static void main(String[] args) {
        //创建奖池
        ArrayList<Integer> arrayList = new ArrayList<>();
        Collections.addAll(arrayList, 10, 5, 20, 50, 100, 200, 500, 800, 2, 80, 300, 700);
        //创建线程
        DemoThread t1 = new DemoThread(arrayList);
        DemoThread t2 = new DemoThread(arrayList);
        t1.setName("抽奖箱1");
        t2.setName("抽奖箱2");
        t1.start();
        t2.start();
    }

    ArrayList<Integer> list;

    public DemoThread(ArrayList<Integer> list) {
        this.list = list;
    }

    static ArrayList<Integer> list1 = new ArrayList<>();
    static ArrayList<Integer> list2 = new ArrayList<>();

    @Override
    public void run() {
        while (true) {
            synchronized (DemoThread.class) {
                if (list.size() == 0) {
                    if ("抽奖箱1".equals(getName())) {
                        System.out.println("抽奖箱1" + list1);
                    } else {
                        System.out.println("抽奖箱2" + list2);
                    }
                    break;
                } else {
                    Collections.shuffle(list);
                    //移除索引为0的值并返回
                    int prize = list.remove(0);
                    if ("抽奖箱1".equals(getName())) {
                        list1.add(prize);
                    } else {
                        list2.add(prize);
                    }
                }
            }
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

}
