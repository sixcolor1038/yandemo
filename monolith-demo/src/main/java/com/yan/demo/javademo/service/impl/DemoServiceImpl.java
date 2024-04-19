package com.yan.demo.javademo.service.impl;

import com.yan.demo.common.utils.FileUtils;
import com.yan.demo.common.utils.ObjectUtils;
import com.yan.demo.common.utils.RResult;
import com.yan.demo.javademo.ao.RenameFileAO;
import com.yan.demo.javademo.mapper.DemoMapper;
import com.yan.demo.javademo.service.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;

/**
 * @Author: sixcolor
 * @Date: 2024-04-17 15:53
 * @Description:
 */
@Service
public class DemoServiceImpl implements DemoService {

    private static final Logger log = LoggerFactory.getLogger(DemoServiceImpl.class);

    @Autowired
    private DemoMapper demoMapper;

    @Override
    public RResult<Boolean> renameFile(RenameFileAO ao) {
        if (Objects.isNull(ao.getType()) || "".equals(ao.getType())) {
            return RResult.failed();
        } else if ("01".equals(ao.getType())) {
            //重命名指定目录下的所有文件，移除特定的前缀
            FileUtils.renameFilesInDirectory(ao.getPath(), ao.getPrefixes());
        } else if ("02".equals(ao.getType())) {
            // 调用工具类方法，使用时间戳重命名文件
            FileUtils.renameFilesWithTimestamp(ao.getPath());
        } else if ("03".equals(ao.getType())) {
            //统计文件数量
            Map<String, Object> stats = FileUtils.countFilesAndFolders(ao.getPath(), ao.isIncludeSubdirectories());
            FileUtils.printStatistics(stats, 0);
            int count = 0;
            count += ObjectUtils.objectToInt(stats.get("jpg"));
            count += ObjectUtils.objectToInt(stats.get("png"));
            log.info("图片数量：{}",count);
        }
        return RResult.success(true);
    }
}
