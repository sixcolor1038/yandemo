package com.yan.demo.jucdemo.basedemo;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

/**
 * @Author: sixcolor
 * @Date: 2024-03-16 12:33
 * @Description:
 */
public class ThreadBaseDemo {
    public static void main(String[] args) throws InterruptedException {
        DemoThread demoThread = new DemoThread("好线程");
        demoThread.start();

        //插入线程，将线程插入到当前线程之前
        demoThread.join();

        for (int i = 1; i <= 10; i++) {
            System.out.println("main线程：" + i );
        }


    }

    /**
     * 线程测试
     */
    private static void extracted4() {
        //当jvm虚拟机启动后，会自行启动多条线程
        //其中一条线程是main线程，作用是调用main方法，并执行方法种的代码
        Thread thread = Thread.currentThread();
        System.out.println(thread.getName());

        //使用构造方法给线程命名
        DemoThread t1 = new DemoThread("猫");
        DemoThread t2 = new DemoThread("狗");
        System.out.println(t1.getPriority());
        System.out.println(t2.getPriority());
        t1.setPriority(1);
        t1.setPriority(10);
        System.out.println(t1.getPriority());
        System.out.println(t2.getPriority());

        //将线程设置为守护线程
        t2.setDaemon(true);

        t1.start();
        t2.start();
    }

    /**
     * 多线程第三种实现方式
     * 前两种方式没有返回值，不能获取线程返回结果，所以有了第三种Callable
     * 1.自定义个一个Callable类实现Callable接口
     * 2.重写call方法
     * 3.创建一个自定义类对象，是要执行的任务
     * 4.创建FutureTask对象，用于管理多线程运行的结果
     * 5.创建一个Thread类对象，并启动线程
     */
    private static void extracted3() throws InterruptedException, ExecutionException {
        DemoCallable demoCallable = new DemoCallable();
        FutureTask<Integer> integerFutureTask = new FutureTask<>(demoCallable);
        Thread thread = new Thread(integerFutureTask);
        thread.start();
        Integer integer = integerFutureTask.get();
        System.out.println(integer);
    }

    /**
     * 多线程第二种启动方式
     * 1.自定义一个类实现Runnable接口
     * 2.重写run方法
     * 3.创建一个自己类的对象
     * 4.创建Thread类对象，并开启线程
     */
    private static void extracted2() {
        DemoRun demoRun = new DemoRun();
        Thread t1 = new Thread(demoRun);
        Thread t2 = new Thread(demoRun);
        t1.setName("线程1");
        t2.setName("线程2");
        t1.start();
        t2.start();
    }

    /**
     * 多线程第一种启动方式
     * 1.继承Thread类，重写run方法
     * 2.创建子类对象，并启动线程，使用start
     */
    private static void extracted1() {
        DemoThread t1 = new DemoThread();
        DemoThread t2 = new DemoThread();
        t1.setName("线程1");
        t2.setName("线程2");
        t1.start();
        t2.start();
    }
}
