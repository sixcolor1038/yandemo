package com.yan.demo.javademo.service;

import com.yan.demo.common.utils.RResult;
import com.yan.demo.javademo.ao.AppAO;
import com.yan.demo.javademo.ao.AreaAO;
import com.yan.demo.javademo.ao.BandwidthAO;
import com.yan.demo.javademo.ao.RenameFileAO;
import com.yan.demo.javademo.entity.Area;
import com.yan.demo.javademo.entity.CommonRec;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @Author: sixcolor
 * @Date: 2024-04-17 15:52
 * @Description:
 */
public interface DemoService {

    RResult<Boolean> renameFile(RenameFileAO ao);

    RResult<Boolean> generateBuilderByExcel(MultipartFile file) throws IOException;

    RResult<CommonRec> queryCommonRec(long id);

    RResult<CommonRec> createCommonRec(@RequestBody CommonRec commonRec);

    RResult<CommonRec> updateCommonRec(@PathVariable long id, @RequestBody CommonRec commonRec);

    RResult<Boolean> deleteCommonRec(@PathVariable long id);

    /**
     * 分页查询
     *
     * @param area 筛选条件
     * @return 查询结果
     */
    RResult<List<Area>> getAreaToTree(AreaAO area);

    RResult<List<Area>> bandwidthConversion(BandwidthAO ao);

    RResult<Boolean> redisDemo();

    void downloadPDF(CommonRec rec, HttpServletResponse response) throws IOException;

    String easyCall(AppAO ao);
}
