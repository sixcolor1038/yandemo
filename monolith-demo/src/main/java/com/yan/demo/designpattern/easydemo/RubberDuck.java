package com.yan.demo.designpattern.easydemo;

import com.yan.demo.designpattern.easydemo.service.impl.FlyNoWay;
import com.yan.demo.designpattern.easydemo.service.impl.Squeak;

/**
 * @Author: sixcolor
 * @Date: 2024-08-03
 * @Description:
 */
public class RubberDuck extends Duck {

    public RubberDuck() {
        quackBehavior = new Squeak();
        flyBehavior = new FlyNoWay();
    }

    public void display() {
        System.out.println("外观是橡皮鸭");
    }
}
