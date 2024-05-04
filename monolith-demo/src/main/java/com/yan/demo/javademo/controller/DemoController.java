package com.yan.demo.javademo.controller;


import com.yan.demo.common.utils.RResult;
import com.yan.demo.javademo.ao.AreaAO;
import com.yan.demo.javademo.ao.BandwidthAO;
import com.yan.demo.javademo.ao.RenameFileAO;
import com.yan.demo.javademo.entity.Area;
import com.yan.demo.javademo.service.DemoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @Author: sixcolor
 * @Date: 2024-04-12 17:30
 * @Description:
 */
@RequestMapping("/demo")
@RestController
@Api(value = "示例")
public class DemoController extends AbstractController {

    @Autowired
    private DemoService demoService;

    @PostMapping("/rename")
    @ApiOperation(value = "重命名文件")
    public RResult<Boolean> renameFile(@ApiParam(name = "RenameFileAO", value = "需修改文件") @RequestBody RenameFileAO ao) {
        return demoService.renameFile(ao);
    }

    @ApiOperation(value = "根据excel生成Builder代码")
    @PostMapping("/generateBuilderByExcel")
    public RResult<Boolean> generateBuilderByExcel(
            @ApiParam(value = "选择文件", required = true)
            @RequestParam("file") MultipartFile file) throws IOException {
        return RResult.success(demoService.generateBuilderByExcel(file).getData());
    }

    @ApiOperation(value = "获取区域")
    @GetMapping("/getArea")
    public RResult<List<Area>> getArea(AreaAO area) {
        return demoService.getAreaToTree(area);
    }

    @ApiOperation(value = "带宽转换")
    @PostMapping("/bandwidth")
    public RResult<List<Area>> bandwidthConversion(BandwidthAO ao) {
        return demoService.bandwidthConversion(ao);
    }
}
