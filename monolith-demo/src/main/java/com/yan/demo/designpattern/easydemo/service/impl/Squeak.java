package com.yan.demo.designpattern.easydemo.service.impl;

import com.yan.demo.designpattern.easydemo.service.QuackBehavior;

/**
 * @Author: sixcolor
 * @Date: 2024-08-03
 * @Description:
 */
public class Squeak implements QuackBehavior {
    @Override
    public void quack() {
        System.out.println("吱吱叫");
    }
}
