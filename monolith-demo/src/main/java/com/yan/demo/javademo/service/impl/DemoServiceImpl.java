package com.yan.demo.javademo.service.impl;

import com.yan.demo.common.utils.FileUtils;
import com.yan.demo.common.utils.RResult;
import com.yan.demo.javademo.entity.RenameFileAO;
import com.yan.demo.javademo.service.DemoService;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @Author: sixcolor
 * @Date: 2024-04-17 15:53
 * @Description:
 */
@Service
public class DemoServiceImpl implements DemoService {

    @Override
    public RResult<Boolean> renameFile(RenameFileAO ao) {
        if (Objects.isNull(ao.getType()) || "".equals(ao.getType())) {
            return RResult.fail();
        } else if ("01".equals(ao.getType())) {
            //重命名指定目录下的所有文件，移除特定的前缀
            FileUtils.renameFilesInDirectory(ao.getPath(), ao.getPrefixes());
        } else if ("02".equals(ao.getType())) {
            // 调用工具类方法，使用时间戳重命名文件
            FileUtils.renameFilesWithTimestamp(ao.getPath());
        } else if ("03".equals(ao.getType())) {
            //统计文件数量
            Map<String, Object> stats = FileUtils.countFilesAndFolders(ao.getPath());
            FileUtils.printStatistics(stats, 0);
        }
        return RResult.success(true);
    }
}
