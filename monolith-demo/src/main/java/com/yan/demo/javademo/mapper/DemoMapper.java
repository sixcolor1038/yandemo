package com.yan.demo.javademo.mapper;

import com.yan.demo.javademo.ao.AreaAO;
import com.yan.demo.javademo.entity.Area;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Author: sixcolor
 * @Date: 2024-04-18 14:28
 * @Description:
 */
@Mapper
public interface DemoMapper {
    /**
     * 查询指定行数据
     *
     * @param area 查询条件
     * @return 对象列表
     */
    List<Area> queryArea(AreaAO area);
    /**
     * 分页查询指定行数据
     *
     * @param area 查询条件
     * @return 对象列表
     */
    List<Area> queryAreaByLimit(AreaAO area);

    /**
     * 统计总行数
     *
     * @param area 查询条件
     * @return 总行数
     */
    long countArea(AreaAO area);
}
