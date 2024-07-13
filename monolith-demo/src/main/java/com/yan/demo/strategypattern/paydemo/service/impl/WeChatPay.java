package com.yan.demo.strategypattern.paydemo.service.impl;

import com.yan.demo.strategypattern.paydemo.service.Payment;

/**
 * @Author: sixcolor
 * @Date: 2024-03-11 17:35
 * @Description: 微信支付 WeChatPay
 */
public class WeChatPay implements Payment {
    @Override
    public boolean pay(String orderId, Long amount) {
        System.out.println("微信 支付");
        return true;
    }
}
