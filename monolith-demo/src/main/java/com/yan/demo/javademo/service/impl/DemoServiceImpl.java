package com.yan.demo.javademo.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.yan.demo.common.constant.RedisConstant;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
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
            Map<String, Integer> counted = MapUtils.countMapByKey(stats, Arrays.asList("jpg", "png", "jpeg"));
            int count = 0;
            count += ObjectUtils.objectToInt(counted.get("jpg"));
            count += ObjectUtils.objectToInt(counted.get("png"));
            count += ObjectUtils.objectToInt(counted.get("jpeg"));
            log.info("图片数量：{}", count);
            commonMapper.updateCommonRec(CommonRec
                    .builder().id(1L).name("视图数量").value(String.valueOf(count)).build());

        }
        addStudent();
        return RResult.success(true);
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
    public RResult<CommonRec> queryCommonRec(long id) {
        return RResult.success(commonMapper.queryCommonRec(CommonRec.builder().id(id).build()));
    }

    @Override
    public RResult<CommonRec> createCommonRec(CommonRec commonRec) {
        commonRec.setId(redisUtil.getNextId(RedisConstant.COMMON_ID_NEXT));
        int i = commonMapper.addCommonRec(commonRec);
        return RResult.handleResult(i, commonRec);
    }

    @Override
    public RResult<CommonRec> updateCommonRec(long id, CommonRec commonRec) {
        commonRec.setId(id);
        checkCommRec(id);
        int i = commonMapper.updateCommonRec(commonRec);
        return RResult.handleResult(i, commonRec);
    }

    @Override
    public RResult<Boolean> deleteCommonRec(long id) {
        checkCommRec(id);
        return RResult.success(commonMapper.deleteCommonRecById(id));
    }

    public void checkCommRec(long id) {
        CommonRec commonRec = commonMapper.queryCommonRec(CommonRec.builder().id(id).build());
        CheckVerifyUtil.checkIfExists(commonRec, "对象不存在,可能已被删除");
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

    @Override
    public void downloadPDF(CommonRec rec, HttpServletResponse response) throws IOException {
        CommonRec commonRec = new CommonRec();
        commonRec.setCode("000");
        commonRec.setName("vds");
        commonRec.setId(1223L);
        commonRec.setValue("22");
        Font titlefont;
        Font headfont;
        Font keyfont = null;
        Font textfont = null;
        Font content = null;

        try {
            BaseFont bfChinese = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            titlefont = new Font(bfChinese, 16, Font.BOLD);
            headfont = new Font(bfChinese, 14, Font.BOLD);
            keyfont = new Font(bfChinese, 10, Font.BOLD);
            textfont = new Font(bfChinese, 15, Font.NORMAL);
            content = new Font(bfChinese, 10, Font.NORMAL);

        } catch (Exception e) {
            e.printStackTrace();
        }

        BaseFont bf;
        Font font = null;
        try {
            bf = BaseFont.createFont("STSong-Light", "UniGB-UCS2-H", BaseFont.NOT_EMBEDDED);
            font = new Font(bf, 20, Font.BOLD, BaseColor.BLACK);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Document document = new Document(new RectangleReadOnly(842F, 595F));
        document.setMargins(60, 60, 72, 72);
        try {
            PdfWriter writer = PdfWriter.getInstance(document, response.getOutputStream());
            writer.setPageEvent(new PdfPageUtil());
            document.open();

            Paragraph paragraph = new Paragraph("备案回执", font);
            paragraph.setAlignment(1);
            document.add(paragraph);

            float[] widths = {25f, 25f, 25f, 25f, 25f, 25f};
            PdfPTable table = new PdfPTable(widths);
            table.setSpacingBefore(20f);
            table.setWidthPercentage(100.0F);
            table.setHeaderRows(1);
            table.getDefaultCell().setHorizontalAlignment(1);
            PdfPCell cell = null;

            cell = new PdfPCell(new Paragraph("备案编码", content));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setFixedHeight(30);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(commonRec.getCode()));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("备案时间", content));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

//            cell = new PdfPCell(new Paragraph(CheckVerifyUtil.dateToString4(commonRec.getId())));
//            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("申请备案单位", content));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(commonRec.getName(), content));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("作业库点", content));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setFixedHeight(30);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(commonRec.getType(), content));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("负责人", content));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(commonRec.getValue(), content));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("联系电话", content));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(commonRec.getType(), content));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("单据状态", content));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setFixedHeight(30);
            table.addCell(cell);

//            cell = new PdfPCell(new Paragraph(shzt(commonRec.getId()), content));
//            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(cell);

            cell = new PdfPCell(new Paragraph("审核时间", content));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

//            cell = new PdfPCell(new Paragraph(CheckVerifyUtil.dateToString5(commonRec.getType()), content));
//            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
//            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
//            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(" ", content));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            cell = new PdfPCell(new Paragraph(" ", content));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);

            float[] widths2 = {25f, 25f, 25f, 25f, 25f, 25f};
            PdfPTable table2 = new PdfPTable(widths2);
            table2.setSpacingBefore(20f);
            table2.setWidthPercentage(100.0F);
            table2.setHeaderRows(1);
            table2.getDefaultCell().setHorizontalAlignment(1);

            cell = new PdfPCell(new Paragraph("姓名", content));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setFixedHeight(20);
            table2.addCell(cell);

            cell = new PdfPCell(new Paragraph("职务", content));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);

            cell = new PdfPCell(new Paragraph("职业资格", content));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);

            cell = new PdfPCell(new Paragraph("身体状况", content));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);

            cell = new PdfPCell(new Paragraph("熏蒸任务分工", content));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);

            cell = new PdfPCell(new Paragraph("是否外包", content));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table2.addCell(cell);

//            if (commonRec.getPeoples().size() > 0) {
//                for (RecordFumigationPeople prople : commonRec.getPeoples()) {
//                    PdfPCell cell1 = new PdfPCell(new Paragraph(prople.getXm(), content));
//                    PdfPCell cell2 = new PdfPCell(new Paragraph(prople.getZw(), content));
//                    PdfPCell cell3 = new PdfPCell(new Paragraph(prople.getZyzg(), content));
//                    PdfPCell cell4 = new PdfPCell(new Paragraph(prople.getStzk(), content));
//                    PdfPCell cell5 = new PdfPCell(new Paragraph(prople.getXzrwfg(), content));
//                    PdfPCell cell6 = new PdfPCell(new Paragraph(prople.getSfwb(), content));
//
//                    cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
//                    cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                    cell1.setFixedHeight(20);
//                    cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
//                    cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                    cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
//                    cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                    cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
//                    cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                    cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
//                    cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                    cell6.setHorizontalAlignment(Element.ALIGN_CENTER);
//                    cell6.setVerticalAlignment(Element.ALIGN_MIDDLE);
//
//                    table2.addCell(cell1);
//                    table2.addCell(cell2);
//                    table2.addCell(cell3);
//                    table2.addCell(cell4);
//                    table2.addCell(cell5);
//                    table2.addCell(cell6);
//                }
//            }

            float[] widths3 = {25f, 25f, 25f, 25f, 25f};
            PdfPTable table3 = new PdfPTable(widths3);
            table3.setSpacingBefore(20f);
            table3.setWidthPercentage(100.0F);
            table3.setHeaderRows(1);
            table3.getDefaultCell().setHorizontalAlignment(1);

            cell = new PdfPCell(new Paragraph("仓房", content));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setFixedHeight(20);
            table3.addCell(cell);

            cell = new PdfPCell(new Paragraph("货位", content));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(cell);

            cell = new PdfPCell(new Paragraph("粮食品种", content));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(cell);

            cell = new PdfPCell(new Paragraph("计划开始时间", content));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(cell);

            cell = new PdfPCell(new Paragraph("计划结束时间", content));
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table3.addCell(cell);

//            if (commonRec.getType().size() > 0) {
//                for (CommonRec dtl : commonRec.g()) {
//                    PdfPCell cell1 = new PdfPCell(new Paragraph(dtl.getCfmc(), content));
//                    PdfPCell cell2 = new PdfPCell(new Paragraph(dtl.getHwmc(), content));
//                    PdfPCell cell3 = new PdfPCell(new Paragraph(dtl.getLspzmc(), content));
//                    PdfPCell cell4 = new PdfPCell(new Paragraph(CheckVerifyUtil.dateToString4(dtl.getJhxzksrq()), content));
//                    PdfPCell cell5 = new PdfPCell(new Paragraph(CheckVerifyUtil.dateToString4(dtl.getJhxzjsrq()), content));
//                    cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
//                    cell1.setVerticalAlignment(Element.ALIGN_MIDDLE);
//                    cell1.setFixedHeight(20);
//
//                    cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
//                    cell2.setVerticalAlignment(Element.ALIGN_MIDDLE);
//
//                    cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
//                    cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
//
//                    cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
//                    cell4.setVerticalAlignment(Element.ALIGN_MIDDLE);
//
//                    cell5.setHorizontalAlignment(Element.ALIGN_CENTER);
//                    cell5.setVerticalAlignment(Element.ALIGN_MIDDLE);
//
//                    table3.addCell(cell1);
//                    table3.addCell(cell2);
//                    table3.addCell(cell3);
//                    table3.addCell(cell4);
//                    table3.addCell(cell5);
//                }
//            }

            document.add(new Paragraph("\n"));
            document.add(new Paragraph("▋ 基本信息", content));
            document.add(new Paragraph("\n"));

            document.add(table);

            document.add(new Paragraph("\n"));
            document.add(new Paragraph("▋ 人员信息", content));
            document.add(new Paragraph("\n"));

            document.add(table2);

            document.add(new Paragraph("\n"));
            document.add(new Paragraph("▋ 作业储粮粮情", content));
            document.add(new Paragraph("\n"));

            document.add(table3);
            document.close();
        } catch (DocumentException e) {
            log.info("导出pdf失败:{}", e);
            e.printStackTrace();
        }
    }

}
