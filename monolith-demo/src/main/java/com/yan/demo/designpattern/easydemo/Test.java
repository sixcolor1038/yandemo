package com.yan.demo.designpattern.easydemo;

import com.yan.demo.designpattern.easydemo.service.impl.FlyRocketPowered;

/**
 * @Author: sixcolor
 * @Date: 2024-08-03
 * @Description:
 */
public class Test {
    public static void main(String[] args) {
        Duck mallardDuck = new MallardDuck();
        mallardDuck.performQuack();
        mallardDuck.performFly();

        Duck modelDuck = new ModelDuck();
        modelDuck.performFly();
        modelDuck.setFlyBehavior(new FlyRocketPowered());
        modelDuck.performFly();


    }
}
