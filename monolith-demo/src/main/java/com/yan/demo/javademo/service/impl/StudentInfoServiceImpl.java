package com.yan.demo.javademo.service.impl;

import com.yan.demo.common.utils.RResult;
import com.yan.demo.javademo.entity.CommonRec;
import com.yan.demo.javademo.entity.StudentInfo;
import com.yan.demo.javademo.mapper.CommonMapper;
import com.yan.demo.javademo.mapper.StudentInfoMapper;
import com.yan.demo.javademo.service.StudentInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * ;(STUDENT_INFO)表服务实现类
 *
 * @author : http://www.chiner.pro
 * @date : 2024-8-25
 */
@Service
public class StudentInfoServiceImpl implements StudentInfoService {
    @Autowired
    private StudentInfoMapper studentInfoMapper;
    @Autowired
    private CommonMapper commonMapper;

    /**
     * 通过ID查询单条数据
     *
     * @param studentInfoId 主键
     * @return 实例对象
     */
    public StudentInfo queryById(String studentInfoId) {
        return studentInfoMapper.queryById(studentInfoId);
    }

    /**
     * 分页查询
     *
     * @param studentInfo 筛选条件
     * @return 查询结果
     */
    public RResult<List<StudentInfo>> paginQuery(StudentInfo studentInfo) {

        String value = commonMapper.queryCommonRec(CommonRec.builder().id(8L).build()).getValue();
        return RResult.success(studentInfoMapper.queryAllByLimit(studentInfo), Long.parseLong(value));
    }

    /**
     * 新增数据
     *
     * @param studentInfo 实例对象
     * @return 实例对象
     */
    public StudentInfo insert(StudentInfo studentInfo) {
        studentInfoMapper.insert(studentInfo);
        Long query = Long.parseLong(commonMapper.queryCommonRec(CommonRec.builder().id(8L).build()).getValue()) + 1L;
        commonMapper.updateCommonRec(CommonRec.builder().id(8L).value(String.valueOf(query)).build());
        return studentInfo;
    }

    /**
     * 更新数据
     *
     * @param studentInfo 实例对象
     * @return 实例对象
     */
    public StudentInfo update(StudentInfo studentInfo) {
        studentInfoMapper.update(studentInfo);
        return queryById(studentInfo.getStudentInfoId());
    }

    /**
     * 通过主键删除数据
     *
     * @param studentInfoId 主键
     * @return 是否成功
     */
    public boolean deleteById(String studentInfoId) {
        int total = studentInfoMapper.deleteById(studentInfoId);
        Long query = Long.parseLong(commonMapper.queryCommonRec(CommonRec.builder().id(8L).build()).getValue()) - 1L;
        commonMapper.updateCommonRec(CommonRec.builder().id(8L).value(String.valueOf(query)).build());
        return total > 0;
    }
}