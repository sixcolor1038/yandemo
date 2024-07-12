package com.yan.demo.interviewdemo.basedemo.demo01;

/**
 * @Author: sixcolor
 * @Date: 2024-04-29 20:46
 * @Description:
 */
public class MathDemo {
    public static void main(String[] args) {


    }

    /**
     * 四舍五入，向上取整
     */
    private static void mathRound() {
        System.out.println(Math.round(11.2));//11
        System.out.println(Math.round(11.5));//12
        System.out.println(Math.round(-11.4));//-11
        System.out.println(Math.round(-11.5));//-11
        System.out.println(Math.round(-11.6));//-12
    }
}
