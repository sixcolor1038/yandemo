package com.yan.demo.javademo.service.impl;

import com.yan.demo.common.constant.BaseConstant;
import com.yan.demo.common.constant.DateConstant;
import com.yan.demo.common.utils.*;
import com.yan.demo.common.utils.generator.BuilderGenerator;
import com.yan.demo.javademo.ao.RenameFileAO;
import com.yan.demo.javademo.entity.CommonRec;
import com.yan.demo.javademo.entity.Student;
import com.yan.demo.javademo.mapper.CommonMapper;
import com.yan.demo.javademo.mapper.DemoMapper;
import com.yan.demo.javademo.service.DemoService;
import com.yan.demo.javademo.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private StudentService studentService;
    @Autowired
    private CommonMapper commonMapper;
    @Autowired
    private RedisUtils redisUtil;

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
            Map<String, Integer> counted = MapUtils.countMapByKey(stats, Arrays.asList("jpg", "png"));
            int count = 0;
            count += ObjectUtils.objectToInt(counted.get("jpg"));
            count += ObjectUtils.objectToInt(counted.get("png"));
            log.info("图片数量：{}", count);
            updateCommonRec(count);
        }
        addStudent();
        return RResult.success(true);
    }

    public void updateCommonRec(int count) {
        CommonRec build = CommonRec.builder()
                .id(1L)
                .name("视图数量")
                .value(String.valueOf(count))
                .build();
        commonMapper.updateCommonRec(build);
    }


    public void addStudent() {
        Student student = Student.builder()
                .SID(null)
                .SName(RandomGeneratorUtils.generateRandomName())
                .SSex(System.currentTimeMillis() % 2 == 0 ? "男" : "女")
                .SBirth(RandomGeneratorUtils.generateRandomAge())
                .build();
        studentService.addStudent(student);
    }

    @Override
    public RResult<Boolean> generateBuilderByExcel(MultipartFile file) throws IOException {
        // 从第二行开始遍历
        int num = 1;
        List<List<String>> excelData = ExcelUtils.readExcelFile(num, file.getInputStream());
        log.info("读取excel数据:{}", excelData);
        Map<String, List<List<String>>> groupedByClassName = excelData.stream()
                .collect(Collectors.groupingBy(list -> list.get(0)));
        groupedByClassName.forEach((className, fields) -> {
            List<BuilderGenerator.Field> list = new ArrayList<>();
            fields.forEach(x -> {
                BuilderGenerator.Field field = new BuilderGenerator.Field();
                field.setName(x.get(1));
                field.setType(x.get(2));
                list.add(field);
            });
            BuilderGenerator.generateByExcel(className, list);
        });
        return RResult.success(true);
    }

    @Scheduled(cron = "0 0/60 * * * ? ")
    @Transactional(rollbackFor = Exception.class)
    public void updateImageCount() {
        CommonRec commonRec = commonMapper.queryCommonRec(CommonRec.builder().id(2L).build());
        checkCommonRec(commonRec);
        RenameFileAO ao = new RenameFileAO();
        ao.setPath(commonRec.getValue());
        ao.setIncludeSubdirectories(true);
        ao.setType(BaseConstant.STR_THREE);
        ao.setPrefixes(Arrays.asList(commonRec.getRemark().split(",")));
        //统计文件数量
        Map<String, Object> stats = FileUtils.countFilesAndFolders(ao.getPath(), ao.isIncludeSubdirectories());
        FileUtils.printStatistics(stats, 0);
        Map<String, Integer> counted = MapUtils.countMapByKey(stats, Arrays.asList("jpg", "png", "gif", "mp4"));
        int count = 0;
        count += ObjectUtils.objectToInt(counted.get("jpg"));
        count += ObjectUtils.objectToInt(counted.get("png"));
        count += ObjectUtils.objectToInt(counted.get("gif"));
        count += ObjectUtils.objectToInt(counted.get("mp4"));
        log.info("数量：{}", count);
        updateCommonRec(count);
        log.info("入参:{}", ao);
        FileUtils.renameFilesInDirectory(ao.getPath(), ao.getPrefixes());
        autoRename();
        log.info("文件重命名结束,结束时间:{}", LocalDateTime.now().format(DateConstant.DATE_TIME_FORMAT));
    }

    public void autoRename() {
        CommonRec commonRec = commonMapper.queryCommonRec(CommonRec.builder().id(3L).build());
        checkCommonRec(commonRec);
        FileUtils.renameFilesInDirectory(commonRec.getValue(), Arrays.asList(commonRec.getRemark().split(",")));
    }

    private void checkCommonRec(CommonRec commonRec) {
        if (null == commonRec.getValue() || null == commonRec.getRemark()) {
            RResult.failed();
            return;
        }
        RResult.ok();
    }

}
