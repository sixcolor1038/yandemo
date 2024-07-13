package com.yan.demo.javademo.entity;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @Author: sixcolor
 * @Date: 2024-04-18 15:09
 * @Description:
 */
@Data
public class Student {
    private String sId;
    @NotBlank(message = "学生姓名不能为空")
    private String sName;
    private String sBirth;
    private String sSex;

    public Student() {
    }

    public Student(String sId, String sName, String sBirth, String sSex) {
        this.sId = sId;
        this.sName = sName;
        this.sBirth = sBirth;
        this.sSex = sSex;
    }

    private Student(Builder builder) {
        this.sId = builder.sId;
        this.sName = builder.sName;
        this.sBirth = builder.sBirth;
        this.sSex = builder.sSex;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String sId;
        private String sName;
        private String sBirth;
        private String sSex;

        public Builder SID(String SID) {
            this.sId = SID;
            return this;
        }

        public Builder SName(String SName) {
            this.sName = SName;
            return this;
        }

        public Builder SBirth(String SBirth) {
            this.sBirth = SBirth;
            return this;
        }

        public Builder SSex(String SSex) {
            this.sSex = SSex;
            return this;
        }

        public Student build() {
            return new Student(this);
        }
    }

}