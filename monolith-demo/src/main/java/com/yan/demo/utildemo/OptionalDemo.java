package com.yan.demo.utildemo;

import com.yan.demo.javademo.entity.Student;

import java.util.Optional;
import java.util.function.Consumer;

/**
 * @Author: sixcolor
 * @Date: 2022-04-19 20:24
 * @Description:
 */
public class OptionalDemo {
    public static void main(String[] args) {
        Student student = new Student("1","汪汪","1997-08-23","男");
        Optional<Student> optional = Optional.ofNullable(student);
        optional.ifPresent(student1 -> {});

    }

    private static void extracted() {
        //Student student = new Student("1","汪汪","1997-08-23","男");
        Student student = null;

        // 一般方法
        if (null != student) {
            String sName = student.getSName();
            System.out.println("一般方法：" + sName);
        }

        Optional<Student> optional = Optional.ofNullable(student);

        // “错误”用法
        if (optional.isPresent()) {
            Student student1 = optional.get();
            System.out.println("错误用法：" + student1.getSName());
        }

        //正确用法
        optional.map(Student::getSName).ifPresent(System.out::println);
    }
}
