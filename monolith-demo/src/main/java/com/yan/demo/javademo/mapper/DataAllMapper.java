/*
package com.yan.demo.javademo.mapper;

import com.yan.demo.javademo.entity.DataAll;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.awt.print.Pageable;
import java.util.List;

*/
/**
 * @Author: sixcolor
 * @Date: 2024-10-10
 * @Description:
 *//*

@Mapper
public interface DataAllMapper{
    */
/**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     *//*

    DataAll queryById(Integer id);
    */
/**
     * 分页查询指定行数据
     *
     * @param dataAll 查询条件
     * @param pageable 分页对象
     * @return 对象列表
     *//*

    List<DataAll> queryAllByLimit(DataAll dataAll, @Param("pageable") Pageable pageable);
    */
/**
     * 统计总行数
     *
     * @param dataAll 查询条件
     * @return 总行数
     *//*

    long count(DataAll dataAll);
    */
/**
     * 新增数据
     *
     * @param dataAll 实例对象
     * @return 影响行数
     *//*

    int insert(DataAll dataAll);
    */
/**
     * 批量新增数据
     *
     * @param entities List<DataAll> 实例对象列表
     * @return 影响行数
     *//*

    int insertBatch(@Param("entities") List<DataAll> entities);
    */
/**
     * 批量新增或按主键更新数据
     *
     * @param entities List<DataAll> 实例对象列表
     * @return 影响行数
     *//*

    int insertOrUpdateBatch(@Param("entities") List<DataAll> entities);
    */
/**
     * 更新数据
     *
     * @param dataAll 实例对象
     * @return 影响行数
     *//*

    int update(DataAll dataAll);
    */
/**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     *//*

    int deleteById(Integer id);
}
*/
