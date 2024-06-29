package com.yan.demo.interviewdemo.demo01;

/**
 * @Author: sixcolor
 * @Date: 2024-04-29 17:16
 * @Description:
 */
public class DemoExtend extends Demo{

    @Override
    public Object haha(Object a) throws RuntimeException{
        System.out.println("你也好");
        return "he he";
    }

    @Override
    protected String haha(String a) {

        return "";
    }
}
