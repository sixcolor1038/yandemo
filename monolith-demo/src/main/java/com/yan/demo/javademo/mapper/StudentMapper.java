package com.yan.demo.javademo.mapper;

import com.yan.demo.javademo.entity.Student;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author: sixcolor
 * @Date: 2024-04-18 15:17
 * @Description:
 */
@Mapper
public interface StudentMapper {

    int addStudent(Student student);

    Student getStudent(Student student);

}
