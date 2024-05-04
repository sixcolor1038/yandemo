package com.yan.demo.javademo.ao;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: sixcolor
 * @Date: 2024-05-02 8:56
 * @Description:
 */
@ApiModel("带宽入参")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BandwidthAO {
    @ApiModelProperty("入参1")
    private String num1;
    @ApiModelProperty("入参类型1")
    private String type1;
    @ApiModelProperty("入参2")
    private String num2;
    @ApiModelProperty("入参类型2")
    private String type2;
}
