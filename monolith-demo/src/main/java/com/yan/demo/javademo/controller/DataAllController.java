/*
package com.yan.demo.javademo.controller;

import com.yan.demo.javademo.entity.DataAll;
import com.yan.demo.javademo.service.DataAllService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

*/
/**
 * @Author: sixcolor
 * @Date: 2024-10-10
 * @Description:
 *//*

@RestController
@RequestMapping("/dataAll")
public class DataAllController {
    @Autowired
    private DataAllService dataAllService;

    */
/**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     *//*

    @ApiOperation("通过ID查询单条数据")
    @GetMapping("{id}")
    public ResponseEntity<DataAll> queryById(Integer id){
        return ResponseEntity.ok(dataAllService.queryById(id));
    }

    */
/**
     * 分页查询
     *
     * @param dataAll 筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     *//*

    @ApiOperation("分页查询")
    @GetMapping
    public ResponseEntity<Page<DataAll>> paginQuery(DataAll dataAll, PageRequest pageRequest){
        return ResponseEntity.ok(dataAllService.paginQuery(dataAll, pageRequest));
    }

    */
/**
     * 新增数据
     *
     * @param dataAll 实例对象
     * @return 实例对象
     *//*

    @ApiOperation("新增数据")
    @PostMapping
    public ResponseEntity<DataAll> add(DataAll dataAll){
        return ResponseEntity.ok(dataAllService.insert(dataAll));
    }

    */
/**
     * 更新数据
     *
     * @param dataAll 实例对象
     * @return 实例对象
     *//*

    @ApiOperation("更新数据")
    @PutMapping
    public ResponseEntity<DataAll> edit(DataAll dataAll){
        return ResponseEntity.ok(dataAllService.update(dataAll));
    }

    */
/**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     *//*

    @ApiOperation("通过主键删除数据")
    @DeleteMapping
    public ResponseEntity<Boolean> deleteById(Integer id){
        return ResponseEntity.ok(dataAllService.deleteById(id));
    }
}
*/
