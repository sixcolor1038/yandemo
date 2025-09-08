package com.yan.demo.javademo.entity;

import com.yan.demo.common.entity.PageEntity;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author: sixcolor
 * @Date: 2024-08-25
 * @Description: 学生信息实体类
 */
public class StudentInfo extends PageEntity implements Serializable {
    /**
     * 学生信息ID
     * -- GETTER --
     * 学生信息ID
     * -- SETTER --
     * 学生信息ID
     */
    @ApiModelProperty(name = "学生信息ID", notes = "")
    private String studentInfoId;
    /**
     * 学生编号
     * -- GETTER --
     * 学生编号
     * -- SETTER --
     * 学生编号
     */
    @ApiModelProperty(name = "学生编号", notes = "")
    private String studentNo;
    /**
     * 学生名称
     * -- GETTER --
     * 学生名称
     * -- SETTER --
     * 学生名称
     */
    @ApiModelProperty(name = "学生名称", notes = "")
    private String studentName;
    /**
     * 性别
     * -- GETTER --
     * 性别
     * -- SETTER --
     * 性别
     */
    @ApiModelProperty(name = "性别", notes = "")
    private String gender;
    /**
     * 出生日期
     * -- GETTER --
     * 出生日期
     * -- SETTER --
     * 出生日期
     */
    @ApiModelProperty(name = "出生日期", notes = "")
    private Date birthDate;
    /**
     * 手机号
     * -- GETTER --
     * 手机号
     * -- SETTER --
     * 手机号
     */
    @ApiModelProperty(name = "手机号", notes = "")
    private String phoneNumber;
    /**
     * qq号
     * -- GETTER --
     * qq号
     * -- SETTER --
     * qq号
     */
    @ApiModelProperty(name = "qq号", notes = "")
    private String qqNo;
    /**
     * 兴趣爱好
     * -- GETTER --
     * 兴趣爱好
     * -- SETTER --
     * 兴趣爱好
     */
    @ApiModelProperty(name = "兴趣爱好", notes = "")
    private String hobby;
    /**
     * 家庭住址
     * -- GETTER --
     * 家庭住址
     * -- SETTER --
     * 家庭住址
     */
    @ApiModelProperty(name = "家庭住址", notes = "")
    private String familyAddress;
    /**
     * 电子邮件
     * -- GETTER --
     * 电子邮件
     * -- SETTER --
     * 电子邮件
     */
    @ApiModelProperty(name = "电子邮件", notes = "")
    private String email;
    /**
     * 专业编号
     * -- GETTER --
     * 专业编号
     * -- SETTER --
     * 专业编号
     */
    @ApiModelProperty(name = "专业编号", notes = "")
    private String majorNo;
    /**
     * 学校编码
     * -- GETTER --
     * 学校编码
     * -- SETTER --
     * 学校编码
     */
    @ApiModelProperty(name = "学校编码", notes = "")
    private String schoolCode;
    /**
     * 学院编码
     * -- GETTER --
     * 学院编码
     * -- SETTER --
     * 学院编码
     */
    @ApiModelProperty(name = "学院编码", notes = "")
    private String collegeCode;
    /**
     * 班级编码
     * -- GETTER --
     * 班级编码
     * -- SETTER --
     * 班级编码
     */
    @ApiModelProperty(name = "班级编码", notes = "")
    private String classCode;
    /**
     * 报道日期
     * -- GETTER --
     * 报道日期
     * -- SETTER --
     * 报道日期
     */
    @ApiModelProperty(name = "报道日期", notes = "")
    private Date enrollDate;
    /**
     * 身份证号
     * -- GETTER --
     * 身份证号
     * -- SETTER --
     * 身份证号
     */
    @ApiModelProperty(name = "身份证号", notes = "")
    private String sidCard;
    /**
     * 银行卡号
     * -- GETTER --
     * 银行卡号
     * -- SETTER --
     * 银行卡号
     */
    @ApiModelProperty(name = "银行卡号", notes = "")
    private String bankNo;
    /**
     * 描述
     * -- GETTER --
     * 描述
     * -- SETTER --
     * 描述
     */
    @ApiModelProperty(name = "描述", notes = "")
    private String studentDesc;
    /**
     * 学生状态
     * -- GETTER --
     * 学生状态
     * -- SETTER --
     * 学生状态
     */
    @ApiModelProperty(name = "学生状态", notes = "")
    private String enrollStatus;
    /**
     * 毕业时间
     * -- GETTER --
     * 毕业时间
     * -- SETTER --
     * 毕业时间
     */
    @ApiModelProperty(name = "毕业时间", notes = "")
    private Date graduationTime;
    /**
     * 备注
     * -- GETTER --
     * 备注
     * -- SETTER --
     * 备注
     */
    @ApiModelProperty(name = "备注", notes = "")
    private String remark;

    public StudentInfo() {
    }

    public StudentInfo(String studentInfoId, String studentNo, String studentName, String gender, Date birthDate, String phoneNumber, String qqNo, String hobby, String familyAddress, String email, String majorNo, String schoolCode, String collegeCode, String classCode, Date enrollDate, String sidCard, String bankNo, String studentDesc, String enrollStatus, Date graduationTime, String remark) {
        this.studentInfoId = studentInfoId;
        this.studentNo = studentNo;
        this.studentName = studentName;
        this.gender = gender;
        this.birthDate = birthDate;
        this.phoneNumber = phoneNumber;
        this.qqNo = qqNo;
        this.hobby = hobby;
        this.familyAddress = familyAddress;
        this.email = email;
        this.majorNo = majorNo;
        this.schoolCode = schoolCode;
        this.collegeCode = collegeCode;
        this.classCode = classCode;
        this.enrollDate = enrollDate;
        this.sidCard = sidCard;
        this.bankNo = bankNo;
        this.studentDesc = studentDesc;
        this.enrollStatus = enrollStatus;
        this.graduationTime = graduationTime;
        this.remark = remark;
    }

    public String getStudentInfoId() {
        return studentInfoId;
    }

    public void setStudentInfoId(String studentInfoId) {
        this.studentInfoId = studentInfoId;
    }

    public String getStudentNo() {
        return studentNo;
    }

    public void setStudentNo(String studentNo) {
        this.studentNo = studentNo;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getQqNo() {
        return qqNo;
    }

    public void setQqNo(String qqNo) {
        this.qqNo = qqNo;
    }

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public String getFamilyAddress() {
        return familyAddress;
    }

    public void setFamilyAddress(String familyAddress) {
        this.familyAddress = familyAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMajorNo() {
        return majorNo;
    }

    public void setMajorNo(String majorNo) {
        this.majorNo = majorNo;
    }

    public String getSchoolCode() {
        return schoolCode;
    }

    public void setSchoolCode(String schoolCode) {
        this.schoolCode = schoolCode;
    }

    public String getCollegeCode() {
        return collegeCode;
    }

    public void setCollegeCode(String collegeCode) {
        this.collegeCode = collegeCode;
    }

    public String getClassCode() {
        return classCode;
    }

    public void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    public Date getEnrollDate() {
        return enrollDate;
    }

    public void setEnrollDate(Date enrollDate) {
        this.enrollDate = enrollDate;
    }

    public String getSidCard() {
        return sidCard;
    }

    public void setSidCard(String sidCard) {
        this.sidCard = sidCard;
    }

    public String getBankNo() {
        return bankNo;
    }

    public void setBankNo(String bankNo) {
        this.bankNo = bankNo;
    }

    public String getStudentDesc() {
        return studentDesc;
    }

    public void setStudentDesc(String studentDesc) {
        this.studentDesc = studentDesc;
    }

    public String getEnrollStatus() {
        return enrollStatus;
    }

    public void setEnrollStatus(String enrollStatus) {
        this.enrollStatus = enrollStatus;
    }

    public Date getGraduationTime() {
        return graduationTime;
    }

    public void setGraduationTime(Date graduationTime) {
        this.graduationTime = graduationTime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}