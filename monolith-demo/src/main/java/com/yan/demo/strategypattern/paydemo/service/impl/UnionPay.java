package com.yan.demo.strategypattern.paydemo.service.impl;

import com.yan.demo.strategypattern.paydemo.service.Payment;

/**
 * @Author: sixcolor
 * @Date: 2024-03-11 17:36
 * @Description:
 */
public class UnionPay implements Payment {
    @Override
    public boolean pay(String orderId, Long amount) {
        System.out.println("银联 支付");
        return true;
    }
}
