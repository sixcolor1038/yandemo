package com.yan.demo.exceldemo.controller;

import com.yan.demo.common.utils.RResult;
import com.yan.demo.exceldemo.entity.EmployeeDuty;
import com.yan.demo.exceldemo.entity.ProductList;
import com.yan.demo.exceldemo.service.ExcelDemoService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @Author: sixcolor
 * @Date: 2024-03-03 18:54
 * @Description:
 */
@RestController
@RequestMapping("importExcel")
@Tag(name = "ExcelDemo API", description = "excel示例")
public class ExcelDemoController {

    @Autowired
    private ExcelDemoService excelService;


    @Operation(summary = "通过excel导入产品清单")
    @PostMapping("/importProductList")
    public RResult<List<ProductList>> importProductList(MultipartFile file) throws IOException {
        return RResult.success(excelService.importProductList(file).getData());
    }

    @Operation(summary = "通过excel导出产品清单")
    @PostMapping("/exportProductList")
    public RResult<String> exportProductList(@RequestBody List<ProductList> list) throws IOException {
        return excelService.exportProductList(list);
    }

    @Operation(summary = "通过excel导入人员值班数据")
    @PostMapping("/importEmployeeDuty")
    public RResult<List<EmployeeDuty>> importEmployeeDuty(MultipartFile file) throws IOException {
        return RResult.success(excelService.importEmployeeDuty(file).getData());
    }


    /**
     * 通过ID查询单条数据
     *
     * @param employeeDutyId 主键
     * @return 实例对象
     */
    @GetMapping("{getById}")
    public ResponseEntity<EmployeeDuty> queryById(@RequestParam Long employeeDutyId) {
        return ResponseEntity.ok(excelService.queryById(employeeDutyId));
    }

    /**
     * 分页查询
     *
     * @param employeeDuty 筛选条件
     * @param pageRequest  分页对象
     * @return 查询结果
     */
    @GetMapping("/getAll")
    public ResponseEntity<Page<EmployeeDuty>> paginQuery(EmployeeDuty employeeDuty, PageRequest pageRequest) {
        return ResponseEntity.ok(excelService.paginQuery(employeeDuty, pageRequest));
    }

    /**
     * 如果不能通过sql条件进行查询，且条件可能有可能没有，
     * 通过代码实现，可以使用stream流进行动态过滤
     *
     * @param employeeDuty
     * @return
     */
    @Operation(summary = "通过stream流动态过滤查询")
    @GetMapping("/getList")
    public ResponseEntity<List<EmployeeDuty>> getList(EmployeeDuty employeeDuty) {
        return ResponseEntity.ok(excelService.getList(employeeDuty));
    }

    /**
     * 新增数据
     *
     * @param employeeDuty 实例对象
     * @return 实例对象
     */
    @PostMapping("/add")
    public RResult<EmployeeDuty> add(@RequestBody EmployeeDuty employeeDuty) {
        excelService.insertEmployeeDuty(employeeDuty);
        return RResult.create(employeeDuty);
    }

    /**
     * 更新数据
     *
     * @param employeeDuty 实例对象
     * @return 实例对象
     */
    @PutMapping
    public ResponseEntity<EmployeeDuty> edit(EmployeeDuty employeeDuty) {
        return ResponseEntity.ok(excelService.update(employeeDuty));
    }

    /**
     * 通过主键删除数据
     *
     * @param employeeDutyId 主键
     * @return 是否成功
     */
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(String employeeDutyId) {
        return ResponseEntity.ok(excelService.deleteById(employeeDutyId));
    }
}
