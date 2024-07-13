package com.yan.demo.strategypattern.paydemo.andfactory;

import io.swagger.v3.oas.annotations.media.Schema;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @Author: sixcolor
 * @Date: 2024-03-11 18:10
 * @Description:
 */
@Schema(title = "订单实体类", description = "订单实体")
public class Order {
    // 订单id
    @Schema(name = "orderId", description = "订单id", type = "string", example = "123")
    @NotBlank(message = "订单id不能为空")
    private String orderId;
    // 金额
    @Schema(name = "amount", description = "支付金额", type = "long", example = "100")
    private long amount;
    // 具体支付类型的引用
    @Schema(name = "payType", description = "具体支付类型的引用", type = "string", example = "1")
    @NotEmpty(message = "支付类型不能为空")
    private String payType;

    public Order(String orderId, long amount) {
        this.orderId = orderId;
        this.amount = amount;
    }

    public String getOrderId() {
        return orderId;
    }
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public long getAmount() {
        return amount;
    }
    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getPayType() {
        return payType;
    }
    public void setPayType(String payType) {
        this.payType = payType;
    }
}

