package com.yan.demo.javademo.controller;

import com.yan.demo.common.utils.RResult;
import com.yan.demo.javademo.entity.Student;
import com.yan.demo.javademo.service.StudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Author: sixcolor
 * @Date: 2024-04-18 15:14
 * @Description:
 */
@RequestMapping("/student")
@RestController
@Api(value = "学生示例")
public class StudentController extends AbstractController {
    @Autowired
    private StudentService studentService;

    @PostMapping("/addStudent")
    @ApiOperation(value = "新增学生")
    public RResult<Integer> addStudent(@ApiParam(name = "RenameFileAO", value = "需修改文件") @RequestBody Student ao) {
        sou();
        return RResult.success(studentService.addStudent(ao));
    }

    @Override
    public void sou() {
        log.info("bbb");
    }

}
