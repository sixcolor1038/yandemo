package com.yan.demo.strategypattern.paydemo.service;

/**
 * @Author: sixcolor
 * @Date: 2024-03-11 17:32
 * @Description: 统一支付接口 Payment
 */
public interface Payment {
    boolean pay(String orderId, Long amount);
}
