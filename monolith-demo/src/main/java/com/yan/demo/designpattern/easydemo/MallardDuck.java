package com.yan.demo.designpattern.easydemo;

import com.yan.demo.designpattern.easydemo.service.impl.FlyWithWings;
import com.yan.demo.designpattern.easydemo.service.impl.Quack;

/**
 * @Author: sixcolor
 * @Date: 2024-08-03
 * @Description:
 */
public class MallardDuck extends Duck {
    public MallardDuck() {
        quackBehavior = new Quack();
        flyBehavior = new FlyWithWings();
    }

    public void display() {
        System.out.println("绿头鸭");
    }

}
