package com.yan.demo.strategypattern.paydemo.entity;

import com.yan.demo.strategypattern.paydemo.service.Payment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * @Author: sixcolor
 * @Date: 2024-03-11 17:16
 * @Description: 订单类
 */
@Data
@Schema(title = "订单实体类", description = "订单实体")
public class Order {

    @Schema(name = "orderId", description = "订单id", type = "string", example = "123")
    private String orderId;

    @Schema(name = "amount", description = "支付金额", type = "long", example = "100")
    private long amount;

    //具体支付类型的引用
    @Schema(name = "payType", description = "具体支付类型的引用", type = "Payment", example = "1")
    private Payment payType;


    public Order(String orderId, long amount, Payment payType) {
        this.orderId = orderId;
        this.amount = amount;
        this.payType = payType;
    }

    /**
     * 订单支付方法
     */
    public boolean pay() {
        boolean pay = payType.pay(orderId, amount);
        if (!pay) {
            System.out.println("支付失败");
        }
        return pay;
    }
}
