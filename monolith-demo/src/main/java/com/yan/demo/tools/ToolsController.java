package com.yan.demo.tools;

import com.yan.demo.common.utils.FileUtils;
import com.yan.demo.common.utils.RResult;
import com.yan.demo.tools.entity.WdPointsTally;
import com.yan.demo.tools.entity.req.WdPointsTallyReq;
import com.yan.demo.tools.rmbconvert.RMBUtil;
import com.yan.demo.tools.service.WdService;
import com.yan.demo.tools.textfile.TextFileUtils;
import com.yan.demo.tools.zipfileextractor.ArchiveUtil;
import com.yan.demo.tools.zipfileextractor.PasswordReader;
import com.yan.demo.tools.zipfileextractor.ZipUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@Tag(name = "ToolsController", description = "工具")
public class ToolsController {

    private static final Logger log = LoggerFactory.getLogger(ToolsController.class);

    @Autowired
    private WdService wdService;

    @GetMapping("/rmbConvert")
    @Operation(summary = "数字转换成大写", description = "数字转换成大写")
    public RResult<String> convert(@RequestParam BigDecimal amount) {
        return RResult.success(RMBUtil.numberToCN(amount), 1L);
    }

    @PostMapping("font")
    @Operation(summary = "转换字体格式", description = "转换字体格式")
    public String font(@RequestBody HashMap<String, String> request) {
        String text = request.get("text");
        boolean flag = Boolean.parseBoolean(request.get("flag"));

        String normalize = Normalizer.normalize(text.replaceAll("[\\p{C}\\p{Z}]", ""), Normalizer.Form.NFKC);
        // 创建替换映射
        Map<String, String> replacements = new HashMap<>();
        replacements.put(",", "，");
        replacements.put(":", "：");
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
    @Operation(summary = "破解", description = "破解")
    public String crackZipFile(@RequestBody HashMap<String, String> request) throws IOException {
        //需解压路径文件名
        String zipFileFullName = request.get("zipFileFullName");
        //解压到的文件路径
        String filePath = request.get("filePath");
        //密码路径
        String passwordFilePath = request.get("passwordFilePath");
        //类型
        String type = request.get("type");
        String folderPath = request.get("folderPath");
        //获取密码
        List<String> passwords = PasswordReader.readPasswords(passwordFilePath);
        //解压文件
        boolean flag;
        if (type.equals("1")) {
            flag = ArchiveUtil.batchExtractFiles(folderPath, filePath, passwords);
        } else {
            flag = ZipUtil.crackZipFile(zipFileFullName, filePath, passwords);
        }
        if (flag) {
            log.info("解压成功");
            return "解压成功";
        } else {
            log.info("解压失败");
            return "解压失败";
        }
    }

    @PostMapping("/merge/text")
    @Operation(summary = "合并文件", description = "将两个txt文件合并成一个，移除重复")
    public RResult<Boolean> merge(@RequestBody HashMap<String, String> request) {
        return RResult.success(TextFileUtils.mergeTextFiles(request.get("file1Path"), request.get("file2Path"), request.get("outputFilePath")));
    }

    /**
     * 最近在使用rime小狼毫,经常多了一堆自造词,难以管理,而且有些自造词还污染了词库,影响使用
     * 就写了这个,用来定期处理一些不常用的词汇,能被清理的词汇,清了也不重要
     * 定期使用即可
     *
     * @param request
     * @return
     */
    @PostMapping("/filterFile/rime")
    @Operation(summary = "筛选不常用小狼毫导出用户词典文本码表文件", description = "筛选文件中数字<=1的行，保留数字>1的行和没有数字的行")
    public RResult<Boolean> filterFile(@RequestBody HashMap<String, String> request) {
        String inputFile = request.get("inputFile");
        String outputFile = request.get("outputFile");
        //保留数字大于几
        int num = 4;
        if (inputFile == null || outputFile == null) {
            return RResult.notFound("输入文件路径和输出文件路径不能为空");
        }

        try {
            boolean result = FileUtils.filterFileContent(inputFile, outputFile, num);
            if (result) {
                return RResult.success(true);
            } else {
                return RResult.failed();
            }
        } catch (Exception e) {
            log.error("文件筛选出错: {}", e.getMessage(), e);
            return RResult.failed();
        }
    }

    @PostMapping("/sort/file")
    @Operation(summary = "按数字排序文件", description = "按照第三列数字大小排序文件，没有数字的行放在最后")
    public RResult<Boolean> sortFile(@RequestBody HashMap<String, String> request) {
        String inputFile = request.get("inputFile");
        String outputFile = request.get("outputFile");
        String sortOrder = request.getOrDefault("sortOrder", "desc"); // desc: 降序, asc: 升序

        if (inputFile == null || outputFile == null) {
            return RResult.notFound("输入文件路径和输出文件路径不能为空");
        }

        try {
            boolean result = FileUtils.sortFileByNumber(inputFile, outputFile, sortOrder);
            if (result) {
                return RResult.success(true);
            } else {
                return RResult.failed();
            }
        } catch (Exception e) {
            log.error("文件排序出错: {}", e.getMessage(), e);
            return RResult.failed();
        }
    }


    @PostMapping("/wd/points")
    @Operation(summary = "wd积分统计", description = "")
    public RResult<WdPointsTally> pointsTally(WdPointsTallyReq WdPointsTallyReq) {
        return RResult.success(wdService.insert(WdPointsTallyReq));
    }

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    @Operation(summary = "通过ID查询单条数据")
    @GetMapping("{id}")
    public RResult<WdPointsTally> queryById(Integer id) {
        return RResult.success(wdService.queryById(id));
    }


}