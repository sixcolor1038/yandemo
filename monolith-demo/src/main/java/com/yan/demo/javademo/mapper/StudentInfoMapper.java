package com.yan.demo.javademo.mapper;

import com.yan.demo.common.entity.PageEntity;
import com.yan.demo.javademo.entity.StudentInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;


/**
 * ;(STUDENT_INFO)表数据库访问层
 *
 * @author : http://www.chiner.pro
 * @date : 2024-8-25
 */
@Mapper
public interface StudentInfoMapper {
    /**
     * 通过ID查询单条数据
     *
     * @param studentInfoId 主键
     * @return 实例对象
     */
    StudentInfo queryById(String studentInfoId);

    /**
     * 分页查询指定行数据
     *
     * @param studentInfo 查询条件
     * @return 对象列表
     */
    List<StudentInfo> queryAllByLimit(StudentInfo studentInfo);

    /**
     * 统计总行数
     *
     * @param studentInfo 查询条件
     * @return 总行数
     */
    long count(StudentInfo studentInfo);

    /**
     * 新增数据
     *
     * @param studentInfo 实例对象
     * @return 影响行数
     */
    int insert(StudentInfo studentInfo);

    /**
     * 批量新增数据
     *
     * @param entities List<StudentInfo> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<StudentInfo> entities);

    /**
     * 批量新增或按主键更新数据
     *
     * @param entities List<StudentInfo> 实例对象列表
     * @return 影响行数
     */
    int insertOrUpdateBatch(@Param("entities") List<StudentInfo> entities);

    /**
     * 更新数据
     *
     * @param studentInfo 实例对象
     * @return 影响行数
     */
    int update(StudentInfo studentInfo);

    /**
     * 通过主键删除数据
     *
     * @param studentInfoId 主键
     * @return 影响行数
     */
    int deleteById(String studentInfoId);
}