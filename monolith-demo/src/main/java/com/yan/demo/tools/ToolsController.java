package com.yan.demo.tools;

import com.yan.demo.common.utils.RResult;
import com.yan.demo.tools.rmbconvert.RMBUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.text.Normalizer;

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

    @PostMapping("font")
    @ApiOperation(value = "转换字体格式")
    public RResult<String> font (@RequestParam String text){
        return RResult.success(Normalizer.normalize(text, Normalizer.Form.NFKC));
    }
}
