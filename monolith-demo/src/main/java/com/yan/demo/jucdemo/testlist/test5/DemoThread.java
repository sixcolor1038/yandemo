package com.yan.demo.jucdemo.testlist.test5;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @Author: sixcolor
 * @Date: 2024-03-26 20:09
 * @Description:
 */
public class DemoThread extends Thread {

    public static void main(String[] args) {
        //创建奖池
        ArrayList<Integer> arrayList = new ArrayList<>();
        Collections.addAll(arrayList, 10, 5, 20, 50, 100, 200, 500, 800, 2, 80, 300, 700);
        //因为list是在前面实例化的时候传入进来的，传同一个list那么不同的实例里的list也指向同一个list地址
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

    @Override
    public void run() {
        while (true) {
            synchronized (DemoThread.class) {
                if (list.size() == 0) {
                    break;
                } else {
                    Collections.shuffle(list);
                    //移除索引为0的值并返回
                    int prize = list.remove(0);
                    System.out.println(getName() + "又产生了一个" + prize + "元大奖");
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
