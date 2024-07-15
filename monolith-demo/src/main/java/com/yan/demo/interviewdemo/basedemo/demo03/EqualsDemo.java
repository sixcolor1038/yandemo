package com.yan.demo.interviewdemo.basedemo.demo03;

import com.yan.demo.interviewdemo.basedemo.demo03.entity.Student;
import com.yan.demo.interviewdemo.basedemo.demo03.entity.Teacher;

/**
 * @Author: sixcolor
 * @Date: 2021-05-02 11:54
 * @Description:
 */
public class EqualsDemo {

    public static void main(String[] args) {
        String a = new String("ab");
        String b = new String("ab");
        String aa = "ab";
        String bb = "ab";
        System.out.println(a == b); //false
        System.out.println(a.equals(b));//true
        System.out.println(aa == bb);//true
        System.out.println(aa.equals(bb));//true
        System.out.println(a == aa);//false
        System.out.println(a.equals(aa));//true
    }

    private static void extracted1() {
        Student student = new Student();
        student.setSid(1);
        student.setsName("1");
        student.setsAge("1");
        student.setsSex("1");
        Student student1 = new Student();
        student1.setSid(1);
        student1.setsName("1");
        student1.setsAge("1");
        student1.setsSex("1");
        Teacher teacher = new Teacher();
        System.out.println(student.equals(teacher));
        System.out.println(student1.equals(student));
        System.out.println(student == student1);
    }

    /**
     * ==和equals
     * ==在比较基本数据类型时，比较的是具体的值，在比较引用数据类型时，比较的是两者的内存地址
     * equals比较对象的内容是否相等
     */
    private static void extracted() {
        int a = 128;
        int b = 128;
        Integer c = 128;
        Integer d = 128;
        System.out.println(a == b);//true
        System.out.println(c.equals(a));//true
        System.out.println(c == d);//false
        System.out.println(c.equals(d));//true
    }

}
