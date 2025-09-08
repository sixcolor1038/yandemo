package com.yan.demo.javademo.service;

import com.yan.demo.common.entity.PageEntity;
import com.yan.demo.common.utils.RResult;
import com.yan.demo.javademo.entity.StudentInfo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

/**
 * @Author: sixcolor
 * @Date: 2024-08-25
 * @Description: 学生信息表服务接口
 */
public interface StudentInfoService {
    /**
     * 通过ID查询单条数据
     *
     * @param studentInfoId 主键
     * @return 实例对象
     */
    StudentInfo queryById(String studentInfoId);

    /**
     * 分页查询
     *
     * @param studentInfo 筛选条件
     * @return 查询结果
     */
    RResult<List<StudentInfo>> paginQuery(StudentInfo studentInfo);

    /**
     * 新增数据
     *
     * @param studentInfo 实例对象
     * @return 实例对象
     */
    StudentInfo insert(StudentInfo studentInfo);

    /**
     * 更新数据
     *
     * @param studentInfo 实例对象
     * @return 实例对象
     */
    StudentInfo update(StudentInfo studentInfo);

    /**
     * 通过主键删除数据
     *
     * @param studentInfoId 主键
     * @return 是否成功
     */
    boolean deleteById(String studentInfoId);
}