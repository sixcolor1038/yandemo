package com.yan.demo.tools.mapper;

import com.yan.demo.tools.entity.WdPointsTally;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @Author: sixcolor
 * @Date: 2024-09-25
 * @Description:
 */
@Mapper
public interface WdMapper {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    WdPointsTally queryById(Integer id);
    /**
     * 新增数据
     *
     * @param wdPointsTally 实例对象
     * @return 影响行数
     */
    int insert(WdPointsTally wdPointsTally);

    /**
     * 批量新增数据
     *
     * @param entities List<WdPointsTally> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<WdPointsTally> entities);

    /**
     * 批量新增或按主键更新数据
     *
     * @param entities List<WdPointsTally> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<WdPointsTally> entities);

    /**
     * 更新数据
     *
     * @param wdPointsTally 实例对象
     * @return 影响行数
     */
    int update(WdPointsTally wdPointsTally);
}
