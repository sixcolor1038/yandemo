package com.yan.demo.tools.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: sixcolor
 * @Date: 2024-09-25
 * @Description:
 */
@ApiModel(value = "积分统计", description = "")
public class WdPointsTally implements Serializable, Cloneable {
    /**
     * 标识
     */
    @ApiModelProperty(name = "标识", notes = "")
    private Integer id;
    /**
     * 周一
     */
    @ApiModelProperty(name = "周一", notes = "")
    private Integer day1;
    /**
     * 周二
     */
    @ApiModelProperty(name = "周二", notes = "")
    private Integer day2;
    /**
     * 周三
     */
    @ApiModelProperty(name = "周三", notes = "")
    private Integer day3;
    /**
     * 周四
     */
    @ApiModelProperty(name = "周四", notes = "")
    private Integer day4;
    /**
     * 周五
     */
    @ApiModelProperty(name = "周五", notes = "")
    private Integer day5;
    /**
     * 周六
     */
    @ApiModelProperty(name = "周六", notes = "")
    private Integer day6;
    /**
     * 周日
     */
    @ApiModelProperty(name = "周日", notes = "")
    private Integer day7;
    /**
     * 当前年月日
     */
    @ApiModelProperty(name = "当前年月日", notes = "")
    private Date tallyDate;
    /**
     * 周统计
     */
    @ApiModelProperty(name = "周统计", notes = "")
    private Integer weekTally;

    /**
     * 标识
     */
    public Integer getId() {
        return this.id;
    }

    /**
     * 标识
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 周一
     */
    public Integer getDay1() {
        return this.day1;
    }

    /**
     * 周一
     */
    public void setDay1(Integer day1) {
        this.day1 = day1;
    }

    /**
     * 周二
     */
    public Integer getDay2() {
        return this.day2;
    }

    /**
     * 周二
     */
    public void setDay2(Integer day2) {
        this.day2 = day2;
    }

    /**
     * 周三
     */
    public Integer getDay3() {
        return this.day3;
    }

    /**
     * 周三
     */
    public void setDay3(Integer day3) {
        this.day3 = day3;
    }

    /**
     * 周四
     */
    public Integer getDay4() {
        return this.day4;
    }

    /**
     * 周四
     */
    public void setDay4(Integer day4) {
        this.day4 = day4;
    }

    /**
     * 周五
     */
    public Integer getDay5() {
        return this.day5;
    }

    /**
     * 周五
     */
    public void setDay5(Integer day5) {
        this.day5 = day5;
    }

    /**
     * 周六
     */
    public Integer getDay6() {
        return this.day6;
    }

    /**
     * 周六
     */
    public void setDay6(Integer day6) {
        this.day6 = day6;
    }

    /**
     * 周日
     */
    public Integer getDay7() {
        return this.day7;
    }

    /**
     * 周日
     */
    public void setDay7(Integer day7) {
        this.day7 = day7;
    }

    /**
     * 当前年月日
     */
    public Date getTallyDate() {
        return this.tallyDate;
    }

    /**
     * 当前年月日
     */
    public void setTallyDate(Date tallyDate) {
        this.tallyDate = tallyDate;
    }

    /**
     * 周统计
     */
    public Integer getWeekTally() {
        return this.weekTally;
    }

    /**
     * 周统计
     */
    public void setWeekTally(Integer weekTally) {
        this.weekTally = weekTally;
    }
}