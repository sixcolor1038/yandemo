package com.yan.demo.javademo.controller;


import com.yan.demo.common.utils.RResult;
import com.yan.demo.javademo.ao.RenameFileAO;
import com.yan.demo.javademo.service.DemoService;
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

}
