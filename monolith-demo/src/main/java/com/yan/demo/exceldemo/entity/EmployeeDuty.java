package com.yan.demo.exceldemo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: sixcolor
 * @Date: 2024-03-02 11:22
 * @Description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(value = "员工值班信息")
public class EmployeeDuty {
    /**
     * 主键ID
     */
    @ApiModelProperty(value = "主键ID")
    @TableId(value = "EMPLOYEE_DUTY_ID", type = IdType.AUTO)
    private Long employeeDutyID;
    /**
     * 姓名
     */
    private String name;
    /**
     * 组别
     */
    private String employeeGroup;
    /**
     * 二级组
     */
    private String subGroup1;
    /**
     * 三级组
     */
    private String subGroup2;
    /**
     * 单位
     */
    private String unit;
    /**
     * 办公地点
     */
    private String officeLocation;
    /**
     * 联系方式
     */
    private String contact;
    /**
     * 本周值班
     */
    private String thisWeekOnDuty;
    /**
     * 下周值班
     */
    private String nextWeekOnDuty;
    /**
     * 备注
     */
    private String remarks;

}
