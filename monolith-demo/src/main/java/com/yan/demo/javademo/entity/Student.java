package com.yan.demo.javademo.entity;

import lombok.Data;

/**
 * @Author: sixcolor
 * @Date: 2024-04-18 15:09
 * @Description:
 */
@Data
public class Student {
    private String sId;
    private String sName;
    private String sBirth;
    private String sSex;

    public Student() {
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