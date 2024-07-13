package com.yan.demo.strategypattern.traditionpay;

import lombok.Data;

/**
 * @Author: sixcolor
 * @Date: 2024-03-11 17:16
 * @Description: 订单类
 */
@Data
public class Order {
    //订单id
    private String orderId;

    //支付方式
    private String payType;

    //支付金额
    private long amount;


    public Order(String orderId, String payType, long amount) {
        this.orderId = orderId;
        this.payType = payType;
        this.amount = amount;
    }


    /**
     * 订单支付方法
     */
    public boolean pay() {
        boolean payment = false;
        if ("aliPay".equals(payType)) {
            System.out.println("支付宝支付,订单号为:" + orderId + ",支付金额:" + amount);
            payment = true;
        } else if ("wechatPay".equals(payType)) {
            System.out.println("微信支付,订单号为:" + orderId + ",支付金额:" + amount);
            payment = true;
        } else if ("unionPay".equals(payType)) {
            System.out.println("银联支付,订单号为:" + orderId + ",支付金额:" + amount);
            payment = true;
        }

        return payment;
    }
}
