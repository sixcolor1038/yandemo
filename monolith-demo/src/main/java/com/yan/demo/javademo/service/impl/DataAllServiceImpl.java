/*
package com.yan.demo.javademo.service.impl;

import com.yan.demo.javademo.entity.DataAll;
import com.yan.demo.javademo.mapper.DataAllMapper;
import com.yan.demo.javademo.service.DataAllService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

*/
/**
 * @Author: sixcolor
 * @Date: 2024-10-10
 * @Description:
 *//*

@Service
public class DataAllServiceImpl implements DataAllService {
    @Autowired
    private DataAllMapper dataAllMapper;

    */
/**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     *//*

    public DataAll queryById(Integer id){
        return dataAllMapper.queryById(id);
    }

    */
/**
     * 分页查询
     *
     * @param dataAll 筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     *//*

    public Page<DataAll> paginQuery(DataAll dataAll, PageRequest pageRequest){
        long total = dataAllMapper.count(dataAll);
        return new PageImpl<>(dataAllMapper.queryAllByLimit(dataAll, pageRequest), pageRequest, total);
    }

    */
/**
     * 新增数据
     *
     * @param dataAll 实例对象
     * @return 实例对象
     *//*

    public DataAll insert(DataAll dataAll){
        dataAllMapper.insert(dataAll);
        return dataAll;
    }

    */
/**
     * 更新数据
     *
     * @param dataAll 实例对象
     * @return 实例对象
     *//*

    public DataAll update(DataAll dataAll){
        dataAllMapper.update(dataAll);
        return queryById(dataAll.getId());
    }

    */
/**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     *//*

    public boolean deleteById(Integer id){
        int total = dataAllMapper.deleteById(id);
        return total > 0;
    }
}
*/
