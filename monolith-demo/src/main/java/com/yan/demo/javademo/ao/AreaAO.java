package com.yan.demo.javademo.ao;

import com.yan.demo.common.entity.BaseEntity;

public class AreaAO extends BaseEntity {

    private Integer id;
    private String parentId;
    private String code;
    private String name;
    private String level;

    public AreaAO() {
    }

    private AreaAO(Builder builder) {
        this.id = builder.id;
        this.parentId = builder.parentId;
        this.code = builder.code;
        this.name = builder.name;
        this.level = builder.level;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    @Override
    public String toString() {
        return "Area{" +
                "id='" + id + '\'' +
                "parentId='" + parentId + '\'' +
                "code='" + code + '\'' +
                "name='" + name + '\'' +
                "level='" + level + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer id;
        private String parentId;
        private String code;
        private String name;
        private String level;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder parentId(String parentId) {
            this.parentId = parentId;
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

        public Builder level(String level) {
            this.level = level;
            return this;
        }

        public AreaAO build() {
            return new AreaAO(this);
        }
    }
}