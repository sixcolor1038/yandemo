package com.yan.demo.javademo.service.impl;

import com.yan.demo.common.constant.RedisConstant;
import com.yan.demo.common.utils.RedisUtils;
import com.yan.demo.javademo.entity.Student;
import com.yan.demo.javademo.mapper.StudentMapper;
import com.yan.demo.javademo.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Author: sixcolor
 * @Date: 2024-04-18 15:15
 * @Description:
 */
@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private RedisUtils redisUtil;
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    @Transactional
    public int addStudent(Student student) {
        Long nextId = redisUtil.getNextId(RedisConstant.STUDENT_ID_NEXT);
        return studentMapper.addStudent(Student.builder()
                .SID(String.valueOf(nextId))
                .SBirth(student.getSBirth())
                .SName(student.getSName())
                .SSex(student.getSSex())
                .build());
    }
}
