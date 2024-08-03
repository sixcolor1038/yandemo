package com.yan.demo.designpattern.easydemo;

import com.yan.demo.designpattern.easydemo.service.impl.FlyNoWay;
import com.yan.demo.designpattern.easydemo.service.impl.Quack;

/**
 * @Author: sixcolor
 * @Date: 2024-08-03
 * @Description:
 */
public class ModelDuck extends Duck{
    public ModelDuck() {
        flyBehavior =new FlyNoWay();
        quackBehavior = new Quack();
    }
    public void display(){
        System.out.println("这是一只模型鸭");
    }
}
