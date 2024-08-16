package com.yan.demo.tools;

import com.yan.demo.common.utils.RResult;
import com.yan.demo.tools.zipfileextractor.PasswordReader;
import com.yan.demo.tools.rmbconvert.RMBUtil;
import com.yan.demo.tools.zipfileextractor.ZipUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: sixcolor
 * @Date: 2024-08-13
 * @Description:
 */
@RequestMapping("/tools")
@RestController
@Api(value = "工具")
public class ToolsController {

    private static final Logger log = LoggerFactory.getLogger(ToolsController.class);

    @GetMapping("/rmbConvert")
    @ApiOperation(value = "数字转换成大写")
    public RResult<String> convert(@RequestParam BigDecimal amount) {
        return RResult.success(RMBUtil.numberToCN(amount), 1L);
    }

    @PostMapping("font")
    @ApiOperation(value = "转换字体格式")
    public String font(@RequestBody HashMap<String, String> request) {
        String text = request.get("text");
        boolean flag = Boolean.parseBoolean(request.get("flag"));

        String normalize = Normalizer.normalize(text.replaceAll("[\\p{C}\\p{Z}]", ""), Normalizer.Form.NFKC);
        // 创建替换映射
        Map<String, String> replacements = new HashMap<>();
        replacements.put("接又", "接口");
        replacements.put("窗又", "窗口");
        // 可以在这里添加更多的替换项
        if (flag) {
            for (Map.Entry<String, String> entry : replacements.entrySet()) {
                normalize = normalize.replace(entry.getKey(), entry.getValue());
            }
        }
        return normalize;
    }

    /**
     * 使用密码列表尝试解压文件
     *
     * @return 返回成功解压的密码或没有有效密码的信息
     */
    @PostMapping("/crack")
    public String crackZipFile(@RequestBody HashMap<String, String> request) throws IOException {
        String zipFileFullName = request.get("zipFileFullName");
        String filePath = request.get("filePath");
        String passwordFilePath = request.get("passwordFilePath");
        List<String> passwords = PasswordReader.readPasswords(passwordFilePath);
        boolean b = ZipUtil.crackZipFile(zipFileFullName, filePath, passwords);
        if (b) {
            log.info("解压成功");
            return "解压成功";
        } else {
            log.info("解压失败");
            return "解压失败";
        }
    }


}
