package com.yan.demo.javademo.job;

import com.yan.demo.common.constant.DateConstant;
import com.yan.demo.common.constant.NumberConstant;
import com.yan.demo.common.utils.CheckVerifyUtil;
import com.yan.demo.common.utils.FileUtils;
import com.yan.demo.common.utils.MapUtils;
import com.yan.demo.common.utils.ObjectUtils;
import com.yan.demo.javademo.ao.RenameFileAO;
import com.yan.demo.javademo.entity.CommonRec;
import com.yan.demo.javademo.mapper.CommonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Map;

/**
 * @Author: sixcolor
 * @Date: 2024-05-28 17:26
 * @Description:
 */
@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    private CommonMapper commonMapper;

    @Scheduled(cron = "0 0/60 * * * ? ")
    @Transactional(rollbackFor = Exception.class)
    public void updateImageCount() {
        CommonRec commonRec = commonMapper.queryCommonRec(CommonRec.builder().id(2L).build());
        CheckVerifyUtil.checkCommonRec(commonRec);
        RenameFileAO ao = new RenameFileAO();
        ao.setPath(commonRec.getValue());
        ao.setIncludeSubdirectories(true);
        ao.setType(NumberConstant.STR_THREE);
        ao.setPrefixes(Arrays.asList(commonRec.getRemark().split(",")));
        //统计文件数量
        Map<String, Object> stats = FileUtils.countFilesAndFolders(ao.getPath(), ao.isIncludeSubdirectories());
        FileUtils.printStatistics(stats, 0);
        Map<String, Integer> counted = MapUtils.countMapByKey(stats, Arrays.asList("jpg", "png", "gif", "mp4", "jpeg"));
        int count = 0;
        count += ObjectUtils.objectToInt(counted.get("jpg"));
        count += ObjectUtils.objectToInt(counted.get("png"));
        count += ObjectUtils.objectToInt(counted.get("gif"));
        count += ObjectUtils.objectToInt(counted.get("mp4"));
        count += ObjectUtils.objectToInt(counted.get("jpeg"));
        log.info("数量：{}", count);
        commonMapper.updateCommonRec(CommonRec
                .builder().id(1L).name("视图数量").value(String.valueOf(count)).build());
        log.info("入参:{}", ao);
        FileUtils.renameFilesInDirectory(ao.getPath(), ao.getPrefixes());
        FileUtils.renameFilesWithTimestamp(ao.getPath());
        CommonRec commonRecEdit = commonMapper.queryCommonRec(CommonRec.builder().id(3L).build());
        CheckVerifyUtil.checkCommonRec(commonRecEdit);
        FileUtils.renameFilesInDirectory(commonRecEdit.getValue(), Arrays.asList(commonRecEdit.getRemark().split(",")));
        FileUtils.renameFilesWithTimestamp(commonRecEdit.getValue());
        log.info("文件重命名结束,结束时间:{}", LocalDateTime.now().format(DateConstant.DATE_TIME_FORMAT));
    }

}
