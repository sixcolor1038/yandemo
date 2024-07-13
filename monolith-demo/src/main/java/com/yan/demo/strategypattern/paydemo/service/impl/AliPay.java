package com.yan.demo.strategypattern.paydemo.service.impl;

import com.yan.demo.strategypattern.paydemo.service.Payment;

/**
 * @Author: sixcolor
 * @Date: 2024-03-11 17:34
 * @Description: 支付宝支付 AliPay
 */
public class AliPay implements Payment {
    @Override
    public boolean pay(String orderId, Long amount) {
        System.out.println("支付宝支付");
        return true;
    }
}
