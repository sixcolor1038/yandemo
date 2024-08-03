package com.yan.demo.designpattern.easydemo;

/**
 * @Author: sixcolor
 * @Date: 2024-08-03
 * @Description:
 */
public class RubberDuck extends Duck {
    @Override
    public void quack() {
        System.out.println("吱吱叫");
    }

    @Override
    public void display() {
        System.out.println("外观是橡皮鸭");
    }
}
