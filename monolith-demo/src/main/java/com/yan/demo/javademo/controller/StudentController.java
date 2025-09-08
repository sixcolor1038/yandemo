package com.yan.demo.javademo.controller;

import com.yan.demo.common.utils.RResult;
import com.yan.demo.javademo.entity.Student;
import com.yan.demo.javademo.entity.StudentInfo;
import com.yan.demo.javademo.service.StudentInfoService;
import com.yan.demo.javademo.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author: sixcolor
 * @Date: 2024-04-18 15:14
 * @Description:
 */
@RequestMapping("/api/student")
@RestController
@Tag(name = "StudentController", description = "学生示例")
public class StudentController extends AbstractController {
    @Autowired
    private StudentService studentService;
    @Autowired
    private StudentInfoService studentInfoService;

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

    /**
     * 通过ID查询单条数据
     *
     * @param studentInfoId 主键
     * @return 实例对象
     */
    @Operation(summary = "通过ID查询单条数据")
    @GetMapping("info/{studentinfoid}")
    public RResult<StudentInfo> queryById(String studentInfoId) {
        return RResult.success(studentInfoService.queryById(studentInfoId));
    }

    /**
     * 分页查询
     *
     * @param studentInfo 筛选条件
     * @return 查询结果
     */
    @Operation(summary = "分页查询")
    @GetMapping("/getInfo")
    public RResult<List<StudentInfo>> paginQuery(StudentInfo studentInfo) {
        return studentInfoService.paginQuery(studentInfo);
    }

    /**
     * 新增数据
     *
     * @param studentInfo 实例对象
     * @return 实例对象
     */
    @Operation(summary = "新增数据")
    @PostMapping("info")
    public RResult<StudentInfo> add(@RequestBody StudentInfo studentInfo) {
        return RResult.success(studentInfoService.insert(studentInfo));
    }

    /**
     * 更新数据
     *
     * @param studentInfo 实例对象
     * @return 实例对象
     */
    @Operation(summary = "更新数据")
    @PutMapping("info")
    public RResult<StudentInfo> edit(StudentInfo studentInfo) {
        return RResult.success(studentInfoService.update(studentInfo));
    }

    /**
     * 通过主键删除数据
     *
     * @param studentInfoId 主键
     * @return 是否成功
     */
    @Operation(summary = "通过主键删除数据")
    @DeleteMapping("info")
    public RResult<Boolean> deleteById(String studentInfoId) {
        return RResult.success(studentInfoService.deleteById(studentInfoId));
    }
}
