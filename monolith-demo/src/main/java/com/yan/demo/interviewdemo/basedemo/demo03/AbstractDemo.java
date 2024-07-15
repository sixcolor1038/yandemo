package com.yan.demo.interviewdemo.basedemo.demo03;

/**
 * @Author: sixcolor
 * @Date: 2021-05-02 12:34
 * @Description:
 */
public abstract class AbstractDemo {

    public AbstractDemo() {

    }

    void eat() {
        System.out.println("吃");
    }

    abstract void sleep();

    public static String aa() {
        System.out.println(ab);
        return ax;
    }

    public static final String ax = "ax";

    private static final String ac = "ac";

    private static String ab = "ab";
}
