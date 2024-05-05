package com.yan.demo.javademo.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.yan.demo.common.constant.DateConstant;
import com.yan.demo.common.constant.NumberConstant;
import com.yan.demo.common.transfer.ObjectTransfer;
import com.yan.demo.common.utils.*;
import com.yan.demo.common.utils.generator.BuilderGenerator;
import com.yan.demo.javademo.ao.AreaAO;
import com.yan.demo.javademo.ao.BandwidthAO;
import com.yan.demo.javademo.ao.RenameFileAO;
import com.yan.demo.javademo.entity.Area;
import com.yan.demo.javademo.entity.CommonRec;
import com.yan.demo.javademo.entity.Student;
import com.yan.demo.javademo.mapper.CommonMapper;
import com.yan.demo.javademo.mapper.DemoMapper;
import com.yan.demo.javademo.service.DemoService;
import com.yan.demo.javademo.service.StudentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
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
    private RedisTemplate<String, Object> redisTemplate;

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
        CommonRec build = CommonRec.builder().id(1L).name("视图数量").value(String.valueOf(count)).build();
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

    @Override
    public RResult<List<Area>> getAreaToTree(AreaAO area) {
        // 首先查询总数用于分页
        long total = demoMapper.countArea(area);
        // 查询当前页的地区列表
        List<Area> areas = demoMapper.queryArea(area);
        areaPageByPageHelper(area);
        areaPageByStream(areas, area);
        areaPageBySql(area);
        // 将结果转为tree
        List<Area> roots = TreeUtils.convertToTree(areas, Area::getId, Area::getParentId, Area::addChild);
        return RResult.success(roots, total);
    }

    private void areaPageByStream(List<Area> list, AreaAO area) {
        List<Area> collect = list.stream()
                .skip((long) (area.getPageNo() - 1) * area.getPageSize())
                .limit(area.getPageSize())
                .collect(Collectors.toList());
        log.info("通过stream流进行分页:{}", collect);
    }

    private void areaPageBySql(AreaAO area) {
        area.setPageNo((area.getPageNo() - 1) * area.getPageSize());
        // 查询当前页的地区列表
        List<Area> areas = demoMapper.queryAreaByLimit(area);
        log.info("通过sql进行分页:{}", areas);
    }

    private void areaPageByPageHelper(AreaAO area) {
        // 使用入参中的页码和页面大小设置分页信息
        PageHelper.startPage(area.getPageNo(), area.getPageSize());
        // 查询当前页的地区列表
        List<Area> areas = demoMapper.queryArea(area);

        // 将查询结果封装成PageInfo对象
        PageInfo<Area> pageInfo = new PageInfo<>(areas);
        // 获取分页信息
        List<Area> list = pageInfo.getList(); // 当前页的数据列表
        int pageSize = pageInfo.getPageSize(); // 每页大小
        int pageNum = pageInfo.getPageNum(); // 当前页码
        long total = pageInfo.getTotal(); // 总记录数
        List<Area> areaList = ObjectTransfer.convertListToList(areas);
        // 输出日志
        log.info("获取当前页:{}", list);
        log.info("根据PageHelper进行分页: 当前页码={}, 每页大小={}, 总记录数={}, 当前页数据={}", pageNum, pageSize, total, areaList);
    }

    @Scheduled(cron = "0 0/60 * * * ? ")
    @Transactional(rollbackFor = Exception.class)
    public void updateImageCount() {
        CommonRec commonRec = commonMapper.queryCommonRec(CommonRec.builder().id(2L).build());
        checkCommonRec(commonRec);
        RenameFileAO ao = new RenameFileAO();
        ao.setPath(commonRec.getValue());
        ao.setIncludeSubdirectories(true);
        ao.setType(NumberConstant.STR_THREE);
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
        FileUtils.renameFilesWithTimestamp(ao.getPath());
        autoRename();
        log.info("文件重命名结束,结束时间:{}", LocalDateTime.now().format(DateConstant.DATE_TIME_FORMAT));
    }

    public void autoRename() {
        CommonRec commonRec = commonMapper.queryCommonRec(CommonRec.builder().id(3L).build());
        checkCommonRec(commonRec);
        FileUtils.renameFilesInDirectory(commonRec.getValue(), Arrays.asList(commonRec.getRemark().split(",")));
        FileUtils.renameFilesWithTimestamp(commonRec.getValue());
    }

    private void checkCommonRec(CommonRec commonRec) {
        if (null == commonRec.getValue() || null == commonRec.getRemark()) {
            RResult.failed();
            return;
        }
        RResult.ok();
    }


    @Override
    public RResult<List<Area>> bandwidthConversion(BandwidthAO ao) {

        return null;
    }

    @Override
    public RResult<Boolean> redisDemo() {
        // 设置值
        redisTemplate.opsForValue().set("key_2024_05_05", "valueA");

        // 获取值
        Object value = redisTemplate.opsForValue().get("key_2024_05_05");
        log.info("键的值为:{}", value);

        // 设置带过期时间的值
        redisTemplate.opsForValue().set("keyWithExpiration20240505", "valueWithExpiration", 60, TimeUnit.SECONDS);

        // 获取剩余过期时间
        Long ttl = redisTemplate.getExpire("keyWithExpiration20240505");
        log.info("键keyWithExpiration20240505的剩余过期时间:{}秒", ttl);

        // 删除键
        redisTemplate.delete("key_2024_05_05");

        // 检查键是否存在
        boolean exists = Boolean.TRUE.equals(redisTemplate.hasKey("key_2024_05_05"));
        log.info("键是否存在:{}", exists);
        return null;
    }

}
