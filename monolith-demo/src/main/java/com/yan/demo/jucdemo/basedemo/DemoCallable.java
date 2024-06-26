package com.yan.demo.jucdemo.basedemo;

import java.util.concurrent.Callable;

/**
 * @Author: sixcolor
 * @Date: 2024-03-16 12:50
 * @Description:
 */
public class DemoCallable implements Callable<Integer> {
    @Override
    public Integer call() throws Exception {
        int sum = 0;
        for (int i = 0; i < 100; i++) {
            sum = sum + i;
        }
        return sum;
    }
}
