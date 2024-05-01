package com.yan.demo.common.entity;

/**
 * @Author: sixcolor
 * @Date: 2024-05-01 8:51
 * @Description:
 */
public class BaseEntity {
    private int pageNo = 1;

    private int pageSize = 10;

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    @Override
    public String toString() {
        return "BaseEntity{" +
                "pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                '}';
    }
}
