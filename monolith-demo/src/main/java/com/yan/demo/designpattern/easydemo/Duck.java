package com.yan.demo.designpattern.easydemo;

import com.yan.demo.designpattern.easydemo.service.FlyBehavior;
import com.yan.demo.designpattern.easydemo.service.QuackBehavior;

/**
 * @Author: sixcolor
 * @Date: 2024-08-03
 * @Description: 鸭子父类
 */
public abstract class Duck {

    FlyBehavior flyBehavior;
    QuackBehavior quackBehavior;

    public void performFly(){
        flyBehavior.fly();
    }
    public void performQuack(){
        quackBehavior.quack();
    }
    public void swim(){
        System.out.println("所有鸭子都会游泳");
    }

    public void setFlyBehavior(FlyBehavior flyBehavior) {
        this.flyBehavior = flyBehavior;
    }

    public void setQuackBehavior(QuackBehavior quackBehavior) {
        this.quackBehavior = quackBehavior;
    }
}
