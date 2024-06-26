package com.yan.demo.jucdemo.ticketdemo;

import com.yan.demo.jucdemo.basedemo.DemoCallable;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @Author: sixcolor
 * @Date: 2024-03-16 18:52
 * @Description:
 */
public class ThreadDemo {
    public static void main(String[] args) {
        //通过实现Callable接口实现
        ExecutorService executor = Executors.newFixedThreadPool(3);
        DemoCallable demoCallable = new DemoCallable();

        Future<Integer> future1 = executor.submit(demoCallable);
        Future<Integer> future2 = executor.submit(demoCallable);
        Future<Integer> future3 = executor.submit(demoCallable);

        try {
            // 获取结果
            System.out.println("Thread 1 最终卖了 " + future1.get() + " 张票");
            System.out.println("Thread 2 最终卖了 " + future2.get() + " 张票");
            System.out.println("Thread 3 最终卖了 " + future3.get() + " 张票");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }
    }

    //使用锁lock实现
    private static void extracted() {
        LockThread t1 = new LockThread();
        LockThread t2 = new LockThread();
        LockThread t3 = new LockThread();
        t1.start();
        t2.start();
        t3.start();
    }

    //使用实现Runnable接口的方法处理多线程
    private static void extracted2() {
        DemoRun demoRun = new DemoRun();
        Thread t1 = new Thread(demoRun);
        Thread t2 = new Thread(demoRun);
        Thread t3 = new Thread(demoRun);
        t1.start();
        t2.start();
        t3.start();
    }

    //使用继承Thread类处理多线程
    private static void extracted1() {
        TicketDemoThread t1 = new TicketDemoThread();
        TicketDemoThread t2 = new TicketDemoThread();
        TicketDemoThread t3 = new TicketDemoThread();
        t1.start();
        t2.start();
        t3.start();
    }
}
