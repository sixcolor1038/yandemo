package com.yan.demo.interviewdemo.basedemo.demo01;

import java.util.Arrays;

/**
 * @Author: sixcolor
 * @Date: 2024-04-29 17:50
 * @Description: 数组反转
 */
public class ArrayReverse {
    public static void main(String[] args) {
        Integer[] a = new Integer[]{1, 2, 3, 4, 5,};
        int left = 0;
        int right = a.length - 1;
        while (left < right) {
            int temp = a[left];
            a[left] = a[right];
            a[right] = temp;
            left++;
            right--;
        }
        System.out.println(Arrays.toString(a));
    }
}
