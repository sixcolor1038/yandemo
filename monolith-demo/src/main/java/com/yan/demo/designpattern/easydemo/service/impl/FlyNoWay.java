package com.yan.demo.designpattern.easydemo.service.impl;

import com.yan.demo.designpattern.easydemo.service.FlyBehavior;

/**
 * @Author: sixcolor
 * @Date: 2024-08-03
 * @Description:
 */
public class FlyNoWay implements FlyBehavior {
    @Override
    public void fly() {
        System.out.println("不会飞");
    }
}
