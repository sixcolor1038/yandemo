package com.yan.demo.javademo.controller;

import com.yan.demo.common.exception.BusinessException;
import com.yan.demo.common.utils.RResult;
import com.yan.demo.javademo.entity.Student;
import com.yan.demo.javademo.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Author: sixcolor
 * @Date: 2024-04-18 15:14
 * @Description:
 */
@RequestMapping("/student")
@RestController
@Tag(name = "StudentController", description = "学生示例")
public class StudentController extends AbstractController {
    @Autowired
    private StudentService studentService;

    @PutMapping()
    @Operation(summary = "新增学生", description = "新增学生")
    public RResult<Student> addStudent(@Valid @RequestBody Student ao) {
        sou();
        int i = studentService.addStudent(ao);
        return RResult.handleResult(i, ao);
    }

    @Override
    public void sou() {
        log.info("bbb");
    }

    @PostMapping()
    @Operation(summary = "查询学生")
    public RResult<Student> getStudent(@RequestBody Student student) {
        return RResult.success(studentService.getStudent(student));
    }

    @GetMapping("{sId}")
    @Operation(summary = "查询学生", description = "根据id查询学生信息")
    public RResult<Student> getStudentById(@PathVariable String sId) {
        return RResult.success(studentService.getStudent(sId));
    }

}
