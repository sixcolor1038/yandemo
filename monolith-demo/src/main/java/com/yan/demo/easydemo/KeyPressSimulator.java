package com.yan.demo.easydemo;


import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;

/**
 * @Author: sixcolor
 * @Date: 2024-06-06 11:05
 * @Description: 每秒触发一次键盘的右箭头键
 */
public class KeyPressSimulator {
    public static void main(String[] args) {
        keyPress();
    }

    private static void keyPress() {

        try {
            // 创建一个Robot实例，用于生成键盘和鼠标的输入事件
            Robot robot = new Robot();
            // 创建一个Timer实例，用于调度定时任务
            Timer timer = new Timer();


            // 创建一个定时任务，每秒按一次右箭头键
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    // 模拟按下右箭头键
                    robot.keyPress(KeyEvent.VK_RIGHT);
                    // 模拟释放右箭头键
                    robot.keyRelease(KeyEvent.VK_RIGHT);
                    System.out.println("右箭头键被按下");
                }
            };
            // 安排任务从现在开始，每隔1000毫秒（1秒）执行一次
            timer.scheduleAtFixedRate(task, 0, 1000);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
}

