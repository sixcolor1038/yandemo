package com.yan.demo.javademo.job;

import com.yan.demo.common.constant.DateConstant;
import com.yan.demo.common.constant.JDBCConstant;
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

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
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

    //将某个文件夹下的图片都统计出来，每小时统计一次，将总数更新到数据库
    //@Scheduled(cron = "0 0/60 * * * ? ")
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

    //每小时获取一次edge浏览器的历史记录数量，并更新进数据库
    @Scheduled(cron = "0 0/60 * * * ? ")
    @Transactional(rollbackFor = Exception.class)
    public void updateEdgeHistoryCount() {
        try {
            CommonRec commonRec = commonMapper.queryCommonRec(CommonRec.builder().id(7L).build());
            // 确保临时文件目录存在
            File tempDir = new File(commonRec.getValue());
            if (!tempDir.exists()) {
                tempDir.mkdirs();
            }

            // 获取Edge历史记录条数
            int count = getEdgeHistoryCount(commonRec);
            log.info("Edge历史记录条数: {}", count);
            // 更新数据库中的记录
            String time = LocalDateTime.now().format(DateConstant.DATE_TIME_FORMAT);
            commonMapper.updateCommonRec(CommonRec
                    .builder()
                    .id(6L)
                    .name("Edge历史记录条数")
                    .value(String.valueOf(count))
                    .remark("当前更新时间:" + time)
                    .build());

            log.info("记录已更新,更新时间: {}", time);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private int getEdgeHistoryCount(CommonRec commonRec) throws Exception {
        String path = commonRec.getValue() + "\\EdgeHistoryCopy";
        // 复制数据库文件到临时位置
        FileUtils.copyFile(new File(commonRec.getRemark()), new File(path));

        // 加载SQLite JDBC驱动
        Class.forName(JDBCConstant.JDBC_SQLITE);

        // 连接到复制的Edge历史记录数据库
        int count = 0;
        try (Connection connection = DriverManager
                .getConnection(JDBCConstant.SQLITE_CONNECT + path)) {
            String query = "SELECT count(*) FROM urls";
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(query)) {
                // 获取历史记录条数
                if (resultSet.next()) {
                    count = resultSet.getInt(1);
                } else {
                    throw new Exception("无法获取Edge历史记录条数");
                }
            }
        }

        // 删除临时文件
        FileUtils.deleteFile(new File(path));

        return count;
    }


}
