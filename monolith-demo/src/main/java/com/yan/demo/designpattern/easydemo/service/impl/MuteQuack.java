package com.yan.demo.designpattern.easydemo.service.impl;

import com.yan.demo.designpattern.easydemo.service.QuackBehavior;

/**
 * @Author: sixcolor
 * @Date: 2024-08-03
 * @Description:
 */
public class MuteQuack implements QuackBehavior {
    @Override
    public void quack() {
        System.out.println("沉默中");
    }
}
