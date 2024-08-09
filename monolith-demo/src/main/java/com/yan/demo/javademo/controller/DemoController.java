package com.yan.demo.javademo.controller;

import com.yan.demo.common.enums.HttpStatus;
import com.yan.demo.common.utils.RResult;
import com.yan.demo.common.utils.WeatherUtils;
import com.yan.demo.javademo.ao.AppAO;
import com.yan.demo.javademo.ao.AreaAO;
import com.yan.demo.javademo.ao.BandwidthAO;
import com.yan.demo.javademo.ao.RenameFileAO;
import com.yan.demo.javademo.entity.Area;
import com.yan.demo.javademo.entity.CommonRec;
import com.yan.demo.javademo.service.DemoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.net.URLEncoder;
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

    @Value("${yandemo.hefeng.loc}")
    private String HEFENG_WEATHER_LOC;
    @Value("${yandemo.hefeng.api}")
    private String HEFENG_WEATHER_API;
    @Autowired
    private DemoService demoService;

    @PostMapping("/weather")
    @ApiOperation(value = "根据和风天气获取天气 -测试")
    public RResult<String> weather() {
        return RResult.success(WeatherUtils.getWeatherByHFTQ(HEFENG_WEATHER_LOC, HEFENG_WEATHER_API));
    }

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

    @ApiOperation(value = "redisDemo")
    @GetMapping("/redisDemo")
    public RResult<Boolean> redisDemo() {
        return demoService.redisDemo();
    }

    @ApiOperation(value = "根据id获取通用记录")
    @GetMapping("commonRec/{id}")
    public RResult<CommonRec> getCommonRec(@PathVariable long id) {
        return demoService.queryCommonRec(id);
    }

    @ApiOperation(value = "创建通用记录")
    @PostMapping("commonRec")
    public RResult<CommonRec> createCommonRec(@RequestBody CommonRec commonRec) {
        return demoService.createCommonRec(commonRec);
    }

    @ApiOperation(value = "更新通用记录")
    @PutMapping("commonRec/{id}")
    public RResult<CommonRec> updateCommonRec(@PathVariable long id, @RequestBody CommonRec commonRec) {
        return demoService.updateCommonRec(id, commonRec);
    }

    @ApiOperation(value = "删除通用记录")
    @DeleteMapping("commonRec/{id}")
    public RResult<Boolean> deleteCommonRec(@PathVariable long id) {
        return demoService.deleteCommonRec(id);
    }

    @ApiOperation(value = "pdf导出")
    @GetMapping("/downloadPDF")
    @SneakyThrows(Exception.class)
    public RResult<String> downloadPDF(@RequestBody @Valid CommonRec rec, HttpServletResponse response, HttpServletRequest request) {
        try {
            // 防止日志记录获取session异常
            request.getSession();
            // 设置编码格式
            response.setContentType("application/pdf;charset=UTF-8");
            response.setCharacterEncoding("utf-8");
            String fileName = URLEncoder.encode("下载的PDF名称", "UTF-8");
            response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".pdf");

            demoService.downloadPDF(rec, response);

            // 在成功时返回一个成功消息
            return RResult.success("文件下载成功");
        } catch (Exception e) {
            // 当发生异常时，返回失败的RResult
            return RResult.fail(HttpStatus.FAILED.getCode(), "文件下载失败: " + e.getMessage());
        }

    }

    @ApiOperation(value = "简单调用")
    @PostMapping("easyCall")
    public RResult<String> easyCall(@RequestBody AppAO ao) {

        return RResult.success(demoService.easyCall(ao));
    }
}
