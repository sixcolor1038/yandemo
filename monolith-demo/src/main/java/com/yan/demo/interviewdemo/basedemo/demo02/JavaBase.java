package com.yan.demo.interviewdemo.basedemo.demo02;

import com.yan.demo.common.utils.timeutils.DateUtil;

/**
 * @Author: sixcolor
 * @Date: 2024-05-01 18:14
 * @Description:
 */
public class JavaBase {
    public static void main(String[] args) {
        ok:
        for (int i = 0; i < 10; i++) {
            for (int j = 0; j < 10; j++) {
                System.out.println("i=" + i + ",j=" + j);
                if (j == 5) {
                    System.out.println("到此为止了");
                    break ok;
                }
            }
        }
    }

    private static void extracted2() {
        final String aa = "dfs";
        String bb = aa;
        bb = "fas";
        System.out.println(aa);
        System.out.println(bb);
    }

    private static void extracted1() {
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
