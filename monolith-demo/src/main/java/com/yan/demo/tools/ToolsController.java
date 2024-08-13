package com.yan.demo.tools;

import com.yan.demo.common.utils.RResult;
import com.yan.demo.tools.rmbconvert.RMBUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

/**
 * @Author: sixcolor
 * @Date: 2024-08-13
 * @Description:
 */
@RequestMapping("/tools")
@RestController
@Api(value = "工具")
public class ToolsController {


    @GetMapping("/rmbConvert")
    @ApiOperation(value = "数字转换成大写")
    public RResult<String> convert(@RequestParam BigDecimal amount) {
        return RResult.success(RMBUtil.numberToCN(amount), 1L);
    }
}
