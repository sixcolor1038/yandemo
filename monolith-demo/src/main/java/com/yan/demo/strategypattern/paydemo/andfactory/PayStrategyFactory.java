package com.yan.demo.strategypattern.paydemo.andfactory;

import com.yan.demo.common.constant.PayConstant;
import com.yan.demo.strategypattern.paydemo.service.Payment;
import com.yan.demo.strategypattern.paydemo.service.impl.AliPay;
import com.yan.demo.strategypattern.paydemo.service.impl.UnionPay;
import com.yan.demo.strategypattern.paydemo.service.impl.WeChatPay;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: sixcolor
 * @Date: 2024-03-11 18:03
 * @Description: 支付策略工厂
 */
public class PayStrategyFactory {

    private static Map<String, Payment> PAYMENT_STRATEGY_MAP = new HashMap<>();

    static {
        PAYMENT_STRATEGY_MAP.put(PayConstant.ALI_PAY, new AliPay());
        PAYMENT_STRATEGY_MAP.put(PayConstant.UNION_PAY, new UnionPay());
        PAYMENT_STRATEGY_MAP.put(PayConstant.WECHAT_PAY, new WeChatPay());
    }

    /**
     * 获取支付方式类
     *
     * @param payType 前端传入支付方式
     * @return Payment 统一支付接口
     */
    public static Payment getPayment(String payType) {
        Payment payment = PAYMENT_STRATEGY_MAP.get(payType);
        if (null == payment) {
            throw new NullPointerException("支付方式选择错误");
        }
        return payment;
    }

}
