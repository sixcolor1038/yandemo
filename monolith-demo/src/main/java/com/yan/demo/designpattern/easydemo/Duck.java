package com.yan.demo.designpattern.easydemo;

/**
 * @Author: sixcolor
 * @Date: 2024-08-03
 * @Description: 鸭子父类
 */
public class Duck {
    public void quack(){
        System.out.println("呱呱叫");
    }

    public void swim(){
        System.out.println("在游泳");
    }

    public void display(){
        System.out.println("外观");
    }
}
