package com.yan.demo.javademo.entity;

import lombok.Data;

/**
 * @Author: sixcolor
 * @Date: 2024-04-21 10:09
 * @Description:
 */

@Data
public class CommonRec {

    private Long id;
    private String code;
    private String name;
    private String type;
    private String value;
    private String remark;

    public CommonRec() {
    }

    private CommonRec(Builder builder) {
        this.id = builder.id;
        this.code = builder.code;
        this.name = builder.name;
        this.type = builder.type;
        this.value = builder.value;
        this.remark = builder.remark;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Long id;
        private String code;
        private String name;
        private String type;
        private String value;
        private String remark;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder code(String code) {
            this.code = code;
            return this;
        }

        public Builder name(String name) {
            this.name = name;
            return this;
        }

        public Builder type(String type) {
            this.type = type;
            return this;
        }

        public Builder value(String value) {
            this.value = value;
            return this;
        }

        public Builder remark(String remark) {
            this.remark = remark;
            return this;
        }

        public CommonRec build() {
            return new CommonRec(this);
        }
    }
}