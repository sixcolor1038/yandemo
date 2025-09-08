package com.yan.demo.tools.keyboardmouse;

import lombok.extern.slf4j.Slf4j;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import javax.swing.JOptionPane;

@Slf4j
public class AltClickSimulator {
    private static final AtomicBoolean isRunning = new AtomicBoolean(true);
    private static final AtomicLong lastActivityTime = new AtomicLong(System.currentTimeMillis());
    private static final long TIMEOUT_MS = 360000; // 6分钟超时

    public static void main(String[] args) {
        // 记录程序开始时间
        long programStartTime = System.currentTimeMillis();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        System.out.println("程序开始时间: " + dateFormat.format(new Date(programStartTime)));

        // 创建超时监控线程
        Thread timeoutThread = new Thread(() -> {
            while (isRunning.get()) {
                if (System.currentTimeMillis() - lastActivityTime.get() > TIMEOUT_MS) {
                    System.err.println("程序执行超时，强制退出");

                    // 计算并显示已运行时间
                    long elapsedTime = System.currentTimeMillis() - programStartTime;
                    String elapsedTimeStr = formatElapsedTime(elapsedTime);
                    System.err.println("程序已运行: " + elapsedTimeStr);

                    // 尝试释放所有按键
                    try {
                        Robot robot = new Robot();
                        robot.keyRelease(KeyEvent.VK_ALT);
                        robot.keyRelease(KeyEvent.VK_CONTROL);
                        robot.keyRelease(KeyEvent.VK_SHIFT);
                        robot.keyRelease(KeyEvent.VK_RIGHT);
                        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                        robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
                        robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
                    } catch (Exception e) {
                        System.err.println("释放资源时出错: " + e.getMessage());
                    }

                    // 播放错误提示音
                    Toolkit.getDefaultToolkit().beep();
                    Toolkit.getDefaultToolkit().beep();

                    System.exit(1);
                }

                try {
                    Thread.sleep(1000); // 每秒检查一次
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        });

        timeoutThread.setDaemon(true);
        timeoutThread.start();

        try {
            Robot robot = new Robot();
            Random random = new Random();

            // 设置延迟时间（毫秒）
            int initialDelay = 3000; // 初始延迟
            int clickDuration = 50;  // 点击持续时间
            int rightKeyDelay = 100; // 右键按下时间

            // 设置鼠标点击位置
            int clickX = 700;
            int clickY = 550;

            // 模拟Alt+Tab切换回上一个应用程序
            robot.keyPress(KeyEvent.VK_ALT);
            robot.delay(50);
            robot.keyPress(KeyEvent.VK_TAB);
            robot.delay(100);
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.delay(50);
            robot.keyRelease(KeyEvent.VK_ALT);

            System.out.println("已切换回目标应用程序");

            // 初始延迟
            robot.delay(initialDelay);

            int count = 50;
            long totalOperationTime = 0; // 记录总操作时间（不包括随机延迟）

            // 循环50次
            for (int i = 1; i <= count; i++) {
                long loopStartTime = System.currentTimeMillis();

                try {
                    System.out.println("开始第 " + i + " 次循环");

                    // 移动鼠标到指定位置
                    robot.mouseMove(clickX, clickY);
                    robot.delay(100); // 添加延迟确保鼠标移动完成

                    // 按下Alt键
                    robot.keyPress(KeyEvent.VK_ALT);
                    robot.delay(100); // 短暂延迟确保Alt键已按下

                    // 按下并释放鼠标左键
                    robot.mousePress(InputEvent.BUTTON1_DOWN_MASK);
                    robot.delay(clickDuration);
                    robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                    robot.delay(50); // 添加释放后的延迟

                    // 释放Alt键
                    robot.keyRelease(KeyEvent.VK_ALT);
                    robot.delay(50); // 添加释放后的延迟

                    // 短暂延迟
                    robot.delay(200);

                    // 触发键盘右键（按下并释放）
                    robot.keyPress(KeyEvent.VK_RIGHT);
                    robot.delay(rightKeyDelay);
                    robot.keyRelease(KeyEvent.VK_RIGHT);
                    robot.delay(50); // 添加释放后的延迟

                    // 计算本次循环的操作时间（不包括随机延迟）
                    long loopOperationTime = System.currentTimeMillis() - loopStartTime;
                    totalOperationTime += loopOperationTime;

                    // 生成3.5秒到5秒之间的随机延迟（3500-5000毫秒）
                    int randomDelay = 3500 + random.nextInt(1501); // 3500 + [0,1500] = [3500,5000]
                    double randomDelayInSeconds = randomDelay / 1000.0;

                    System.out.println("已完成第 " + i + " 次循环，操作耗时: " + loopOperationTime + "ms, 等待" +
                            String.format("%.1f", randomDelayInSeconds) + "秒后继续...");

                    // 更新最后活动时间
                    lastActivityTime.set(System.currentTimeMillis());

                    // 如果不是最后一次循环，则等待随机时间
                    if (i < count) {
                        robot.delay(randomDelay);
                    }
                } catch (Exception e) {
                    System.err.println("第 " + i + " 次循环出错: " + e.getMessage());
                    e.printStackTrace();

                    // 尝试恢复 - 释放所有可能按下的键
                    try {
                        robot.keyRelease(KeyEvent.VK_ALT);
                        robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                        robot.keyRelease(KeyEvent.VK_RIGHT);
                    } catch (Exception ex) {
                        System.err.println("恢复操作失败: " + ex.getMessage());
                    }

                    // 更新最后活动时间
                    lastActivityTime.set(System.currentTimeMillis());

                    // 短暂延迟后继续
                    robot.delay(1000);
                }
            }

            // 计算总耗时
            long programEndTime = System.currentTimeMillis();
            long totalElapsedTime = programEndTime - programStartTime;
            String totalElapsedTimeStr = formatElapsedTime(totalElapsedTime);

            System.out.println("程序执行完毕！");
            System.out.println("总耗时: " + totalElapsedTimeStr);
            System.out.println("平均每次循环操作时间: " + (totalOperationTime / count) + "ms");

            // 提醒方式1：播放系统提示音
            for (int i = 0; i < 3; i++) {
                Toolkit.getDefaultToolkit().beep();
                robot.delay(500); // 提示音之间的延迟
            }

            // 提醒方式2：弹出对话框，显示总耗时
            JOptionPane.showMessageDialog(null,
                    "程序执行完毕！\n总耗时: " + totalElapsedTimeStr +
                            "\n平均每次循环操作时间: " + (totalOperationTime / count) + "ms",
                    "完成提醒", JOptionPane.INFORMATION_MESSAGE);
            System.gc();//偶尔程序会卡住不结束，就直接关闭吧。
        } catch (AWTException e) {
            System.err.println("创建Robot对象失败: " + e.getMessage());
            e.printStackTrace();

            // 计算并显示已运行时间
            long elapsedTime = System.currentTimeMillis() - programStartTime;
            String elapsedTimeStr = formatElapsedTime(elapsedTime);
            System.err.println("程序已运行: " + elapsedTimeStr);
        } finally {
            isRunning.set(false); // 程序正常结束，停止监控

            // 确保所有按键和鼠标按钮都已释放
            try {
                Robot robot = new Robot();
                robot.keyRelease(KeyEvent.VK_ALT);
                robot.keyRelease(KeyEvent.VK_CONTROL);
                robot.keyRelease(KeyEvent.VK_SHIFT);
                robot.keyRelease(KeyEvent.VK_RIGHT);
                robot.mouseRelease(InputEvent.BUTTON1_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON2_DOWN_MASK);
                robot.mouseRelease(InputEvent.BUTTON3_DOWN_MASK);
            } catch (Exception e) {
                System.err.println("释放资源时出错: " + e.getMessage());
            }
        }
    }

    /**
     * 将毫秒时间格式化为易读的字符串
     *
     * @param millis 毫秒数
     * @return 格式化的时间字符串
     */
    private static String formatElapsedTime(long millis) {
        long hours = TimeUnit.MILLISECONDS.toHours(millis);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis) % 60;
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) % 60;
        long milliseconds = millis % 1000;

        if (hours > 0) {
            return String.format("%d小时 %d分钟 %d秒 %d毫秒", hours, minutes, seconds, milliseconds);
        } else if (minutes > 0) {
            return String.format("%d分钟 %d秒 %d毫秒", minutes, seconds, milliseconds);
        } else if (seconds > 0) {
            return String.format("%d秒 %d毫秒", seconds, milliseconds);
        } else {
            return String.format("%d毫秒", milliseconds);
        }
    }
}