package com.yan.demo.javademo.service;

import com.yan.demo.common.utils.RResult;
import com.yan.demo.javademo.ao.AreaAO;
import com.yan.demo.javademo.ao.BandwidthAO;
import com.yan.demo.javademo.ao.RenameFileAO;
import com.yan.demo.javademo.entity.Area;
import org.springframework.web.multipart.MultipartFile;

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

    /**
     * 分页查询
     *
     * @param area 筛选条件
     * @return 查询结果
     */
    RResult<List<Area>> getAreaToTree(AreaAO area);

    RResult<List<Area>> bandwidthConversion(BandwidthAO ao);
}
