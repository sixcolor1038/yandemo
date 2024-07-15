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
        return ax;
    }

    public static final String ax = "ax";

    public final String ac = "ac";
}
