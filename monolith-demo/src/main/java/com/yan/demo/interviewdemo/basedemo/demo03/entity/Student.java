package com.yan.demo.interviewdemo.basedemo.demo03.entity;

/**
 * @Author: sixcolor
 * @Date: 2021-05-02 12:08
 * @Description:
 */
public class Student {

    private Integer sid;
    private String sName;
    private String sAge;
    private String sSex;

    public Student() {
    }

    public Student(Integer sid, String sName, String sAge, String sSex) {
        this.sid = sid;
        this.sName = sName;
        this.sAge = sAge;
        this.sSex = sSex;
    }

    public Student(Integer sid, String sName, String sAge) {
        this.sid = sid;
        this.sName = sName;
        this.sAge = sAge;
    }

    public Integer getSid() {
        return sid;
    }

    public void setSid(Integer sid) {
        this.sid = sid;
    }

    public String getsName() {
        return sName;
    }

    public void setsName(String sName) {
        this.sName = sName;
    }

    public String getsAge() {
        return sAge;
    }

    public void setsAge(String sAge) {
        this.sAge = sAge;
    }

    public String getsSex() {
        return sSex;
    }

    public void setsSex(String sSex) {
        this.sSex = sSex;
    }

    @Override
    public String toString() {
        return "Student{" +
                "sid=" + sid +
                ", sName='" + sName + '\'' +
                ", sAge='" + sAge + '\'' +
                ", sSex='" + sSex + '\'' +
                '}';
    }
}
