package com.yan.demo.strategypattern.paydemo.controller;

import com.yan.demo.strategypattern.paydemo.andfactory.Order;
import com.yan.demo.strategypattern.paydemo.andfactory.PayStrategyFactory;
import com.yan.demo.strategypattern.paydemo.service.Payment;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author: sixcolor
 * @Date: 2024-03-11 18:11
 * @Description:
 */
@RestController
@RequestMapping("pay")
@Tag(name = "PayController", description = "支付接口")
public class PayController {

    @PostMapping
    @Operation(summary = "支付", description = "通过不同方式进行支付，使用了策略模式+工厂模式")
    public boolean pay(@Valid @RequestBody Order order) {
        Payment payment = PayStrategyFactory.getPayment(order.getPayType());
        return payment.pay(order.getOrderId(), order.getAmount());
    }
}
