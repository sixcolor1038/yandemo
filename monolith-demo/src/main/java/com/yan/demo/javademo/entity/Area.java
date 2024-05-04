package com.yan.demo.javademo.entity;

import java.util.ArrayList;
import java.util.List;

public class Area {

    private Integer id;
    private Integer parentId;
    private String code;
    private String name;
    private String level;
    private List<Area> areas;

    public Area() {
    }

    private Area(Builder builder) {
        this.id = builder.id;
        this.parentId = builder.parentId;
        this.code = builder.code;
        this.name = builder.name;
        this.level = builder.level;
        this.areas = builder.areas;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
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

    public List<Area> getAreas() {
        return areas;
    }

    public void setAreas(List<Area> areas) {
        this.areas = areas;
    }

    @Override
    public String toString() {
        return "Area{" +
                "id='" + id + '\'' +
                "parentId='" + parentId + '\'' +
                "code='" + code + '\'' +
                "name='" + name + '\'' +
                "level='" + level + '\'' +
                "areas='" + areas + '\'' +
                '}';
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private Integer id;
        private Integer parentId;
        private String code;
        private String name;
        private String level;
        private List<Area> areas;

        public Builder id(Integer id) {
            this.id = id;
            return this;
        }

        public Builder parentId(Integer parentId) {
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

        public Builder areas(List<Area> areas) {
            this.areas = areas;
            return this;
        }

        public Area build() {
            return new Area(this);
        }

    }


    public void addChild(Area child) {
        if (areas == null) {
            areas = new ArrayList<>();
        }
        areas.add(child);
    }
}