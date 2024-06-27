package com.yan.demo.jucdemo.testlist.test5;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: sixcolor
 * @Date: 2024-03-26 20:09
 * @Description: 多线程模拟抽奖程序，每个线程从共享的奖池中抽取一个随机奖项
 */
public class DemoRun implements Runnable {

    /**
     * 有一个抽奖池，该抽奖池存放了奖励的金额，
     * 该抽奖池中的奖项为10, 5, 20, 50, 100, 200, 500, 800, 2, 80, 300, 700
     * 创建两个抽奖箱(线程)设置线程名称分别为"抽奖箱1","抽奖箱2"
     * 随机从抽奖池中获取奖项元素并打印在控制台上，格式如下：
     * 抽奖箱1又产生了一个10元大奖
     * 抽奖箱1又产生了一个200元大奖
     * 抽奖箱2又产生了一个800元大奖
     * ......
     */
    public static void main(String[] args) {
        // 创建奖池，并添加奖项
        ArrayList<Integer> arrayList = new ArrayList<>();
        Collections.addAll(arrayList, 10, 5, 20, 50, 100, 200, 500, 800, 2, 80, 300, 700);

        // 创建一个固定大小为2的线程池
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        // 提交两个线程任务，使用同一个奖池列表
        executorService.execute(new DemoThread(arrayList));
        executorService.execute(new DemoThread(arrayList));

        // 关闭线程池
        executorService.shutdown();
    }


    // 共享锁对象，用于同步
    private static final Object lock = new Object();

    // 共享的奖池列表
    private final ArrayList<Integer> list;

    /**
     * 构造函数，接受一个奖池列表
     *
     * @param list 奖池列表
     */
    public DemoRun(ArrayList<Integer> list) {
        this.list = list;
    }


    @Override
    public void run() {
        while (true) {
            int prize;
            // 同步块，确保线程安全
            synchronized (lock) {
                // 检查奖池是否为空
                if (list.isEmpty()) {
                    break; // 如果奖池为空，退出循环
                }
                // 随机打乱奖池顺序
                Collections.shuffle(list);
                // 从奖池中移除第一个奖项
                prize = list.remove(0);
            }
            // 打印抽取的奖项
            System.out.println(Thread.currentThread().getName() + " 又产生了一个 " + prize + " 元大奖");

            try {
                // 模拟抽奖时间
                Thread.sleep(5);
            } catch (InterruptedException e) {
                // 如果线程被中断，恢复中断状态
                Thread.currentThread().interrupt();
                System.out.println(Thread.currentThread().getName() + " 被中断");
            }
        }
    }
}

