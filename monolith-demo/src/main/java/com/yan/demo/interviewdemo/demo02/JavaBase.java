package com.yan.demo.interviewdemo.demo02;

import com.yan.demo.common.utils.timeutils.DateUtil;

/**
 * @Author: sixcolor
 * @Date: 2024-05-01 18:14
 * @Description:
 */
public class JavaBase {
    public static void main(String[] args) {
        System.out.println(DateUtil.getDayBetweenDay("2022-09-01"));
    }


    //String理解
    private static void extracted() {
        //String 对象不可变，是线程安全的，每次对其修改都是产生一个新String对象
        String a = "Hello";
        String b = a;
        a = "World";
        //a和b初始都指向了Hello，当a被指向World时，并没有改变Hello字符串的内容，
        // 而是新建了一个World字符串，并将a的引用指向它，b仍然指向原来的Hello字符串
        System.out.println(a);  //World
        System.out.println(b);  //Hello
    }
}
