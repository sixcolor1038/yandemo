package com.yan.demo.interviewdemo.basedemo.demo03.entity;

/**
 * @Author: sixcolor
 * @Date: 2021-05-02 12:10
 * @Description:
 */
public class Teacher {

    private Integer tid;
    private String tName;
    private String tAge;
    private String tSex;

    public Teacher() {
    }

    public Teacher(Integer tid, String tName, String tAge, String tSex) {
        this.tid = tid;
        this.tName = tName;
        this.tAge = tAge;
        this.tSex = tSex;
    }

    public Integer getTid() {
        return tid;
    }

    public void setTid(Integer tid) {
        this.tid = tid;
    }

    public String gettName() {
        return tName;
    }

    public void settName(String tName) {
        this.tName = tName;
    }

    public String gettAge() {
        return tAge;
    }

    public void settAge(String tAge) {
        this.tAge = tAge;
    }

    public String gettSex() {
        return tSex;
    }

    public void settSex(String tSex) {
        this.tSex = tSex;
    }

    @Override
    public String toString() {
        return "Teacher{" +
                "tid=" + tid +
                ", tName='" + tName + '\'' +
                ", tAge='" + tAge + '\'' +
                ", tSex='" + tSex + '\'' +
                '}';
    }
}
