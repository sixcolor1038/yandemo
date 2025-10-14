package com.yan.demo.common.utils;

import com.yan.demo.common.constant.DateConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

/**
 * @Author: sixcolor
 * @Date: 2024-04-17 14:55
 * @Description:
 */
public class FileUtils {
    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

    /**
     * 重命名指定目录下的所有文件，移除特定的前缀。
     *
     * @param directoryPath 目录的路径。
     * @param prefixes      需要移除的前缀数组，例如 {"QQ图片", "微信图片"}。
     */
    public static void renameFilesInDirectory(String directoryPath, List<String> prefixes) {
        directoryPath = normalizePath(directoryPath);
        // 获取目录对象
        File directory = new File(directoryPath);

        // 确保这是一个目录
        if (!directory.isDirectory()) {
            log.info("提供的路径不是目录.");
            return;
        }

        // 遍历目录下的所有文件和文件夹
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String fileName = file.getName();
                    // 替换文件名中的指定前缀
                    String newFileName = fileName;
                    for (String prefix : prefixes) {
                        // 去除前缀字符串中的引号
                        String cleanPrefix = prefix.replace("\"", "");
                        // 使用 Pattern.quote 方法转义前缀字符串，确保不受到正则表达式的影响
                        String escapedPrefix = Pattern.quote(cleanPrefix);
                        log.debug("前缀：{}", escapedPrefix);
                        newFileName = newFileName.replaceFirst("^" + escapedPrefix, "");
                    }
                    // 如果文件名发生变化，执行重命名操作
                    if (!fileName.equals(newFileName)) {
                        File newFile = new File(directory, newFileName);
                        if (!file.renameTo(newFile)) {
                            log.info("重命名失败: 文件名:{}", fileName);
                        } else {
                            log.info("重命名成功: 原文件名:{},  新文件名:{}", fileName, newFile.getPath());
                        }
                    }
                }
            }
        }
    }

    /**
     * 重命名指定目录下的所有文件，使用时间戳命名，除非它们已经是时间戳格式。
     *
     * @param directoryPath 目录的路径。
     */
    public static void renameFilesWithTimestamp(String directoryPath) {
        directoryPath = normalizePath(directoryPath);
        // 获取目录对象
        File directory = new File(directoryPath);

        // 确保这是一个目录
        if (!directory.isDirectory()) {
            log.info("提供的路径不是目录.");
            return;
        }

        // 遍历目录下的所有文件和文件夹
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    String fileName = file.getName();
                    String extension = "";
                    int i = fileName.lastIndexOf('.');
                    if (i > 0) {
                        extension = fileName.substring(i);
                    }

                    // 获取无扩展名的文件名部分
                    String nameWithoutExtension = fileName.substring(0, i);

                    // 检查文件名是否已经是时间戳格式
                    if (!DateConstant.TIMESTAMP_PATTERN_MILLIS.matcher(nameWithoutExtension).matches()) {
                        // 生成新的文件名，使用当前时间的时间戳
                        String timestamp = LocalDateTime.now().format(DateConstant.TIMESTAMP_FORMAT_MILLIS);
                        File newFile = new File(directory, timestamp + extension);

                        // 确保文件名唯一，避免命名冲突
                        while (newFile.exists()) {
                            // 微调时间戳以保证唯一性
                            timestamp = LocalDateTime.now().format(DateConstant.TIMESTAMP_FORMAT_MILLIS);
                            newFile = new File(directory, timestamp + extension);
                        }

                        // 重命名文件
                        if (!file.renameTo(newFile)) {
                            log.info("重命名失败: {}", file.getPath());
                        } else {
                            log.info("重命名成功: 原文件名:{},  新文件名:{}", fileName, newFile.getPath());
                        }
                    }
                }
            }
        }
    }

    /**
     * 统计指定目录下的所有文件和文件夹的数量，按文件扩展名分类统计文件，并根据参数决定是否统计子目录。
     *
     * @param directoryPath         目录的路径。
     * @param includeSubdirectories 是否包含子目录的统计。
     * @return 包含文件统计信息的Map，其中包含嵌套的结构以表示子目录统计。
     */
    public static Map<String, Object> countFilesAndFolders(String directoryPath, boolean includeSubdirectories) {
        directoryPath = normalizePath(directoryPath);
        File directory = new File(directoryPath);
        Map<String, Object> stats = new TreeMap<>();
        if (directory.exists() && directory.isDirectory()) {
            countFilesRecursive(directory, stats, 0, includeSubdirectories);
        }
        return stats;
    }

    /**
     * 递归统计文件和文件夹
     *
     * @param directory             文件夹对象
     * @param stats                 统计结果映射表
     * @param level                 当前递归的层级
     * @param includeSubdirectories 是否递归统计子目录
     */
    private static void countFilesRecursive(File directory, Map<String, Object> stats, int level, boolean includeSubdirectories) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    Map<String, Object> subStats = new TreeMap<>();
                    if (includeSubdirectories) {
                        countFilesRecursive(file, subStats, level + 1, includeSubdirectories);
                        stats.put(file.getName(), subStats);
                    }
                } else {
                    String extension = getExtension(file.getName());
                    stats.put(extension, ((Integer) stats.getOrDefault(extension, 0)) + 1);
                }
            }
        }
    }

    /**
     * 获取文件的扩展名
     *
     * @param fileName 文件名
     * @return 文件扩展名，如果没有则返回 "none"
     */
    private static String getExtension(String fileName) {
        int index = fileName.lastIndexOf('.');
        return index > 0 ? fileName.substring(index + 1) : "none";
    }

    /**
     * 格式化并打印统计结果
     *
     * @param stats 统计数据
     * @param level 当前层级（用于格式化）
     */
    public static void printStatistics(Map<String, Object> stats, int level) {
        String indent = StringUtils.repeatString("  ", level);
        for (Map.Entry<String, Object> entry : stats.entrySet()) {
            if (entry.getValue() instanceof Map<?, ?>) {
                log.info(indent + entry.getKey() + ":");
                @SuppressWarnings("unchecked")
                Map<String, Object> subMap = (Map<String, Object>) entry.getValue();
                printStatistics(subMap, level + 1);
            } else {
                log.info(indent + entry.getKey() + ": " + entry.getValue());
            }
        }
    }

    /**
     * 标准化路径，将单反斜杠替换为双反斜杠
     *
     * @param path 原始路径
     * @return 标准化后的路径
     */
    public static String normalizePath(String path) {
        // 首先替换每个 "\\" 为 "\\"，以确保已经转义的反斜杠不被重复转义
        String doubleBackslash = "\\\\";
        while (path.contains(doubleBackslash)) {
            path = path.replace(doubleBackslash, "\\");
        }

        // 然后替换未被转义的单个反斜杠为双反斜杠
        return path.replace("\\", "\\\\");
    }

    public static void copyFile(File sourceFile, File destFile) throws IOException {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        try (FileChannel sourceChannel = new FileInputStream(sourceFile).getChannel();
             FileChannel destChannel = new FileOutputStream(destFile).getChannel()) {
            destChannel.transferFrom(sourceChannel, 0, sourceChannel.size());
        }
    }

    public static void deleteFile(File file) throws IOException {
        if (!file.delete()) {
            throw new IOException("无法删除文件: " + file.getAbsolutePath());
        }
    }

    /**
     * 筛选文件内容，删除数字<=1的行，保留数字>1的行和没有数字的行
     *
     * @param inputFile  输入文件路径
     * @param outputFile 输出文件路径
     * @return 是否成功
     */
    public static boolean filterFileContent(String inputFile, String outputFile,int num) {
        List<String> filteredLines = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(inputFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // 跳过空行
                if (line.trim().isEmpty()) {
                    continue;
                }

                // 按制表符分割
                String[] parts = line.split("\t");
                if (parts.length >= 3) {
                    try {
                        // 获取第三列的数字
                        int number = Integer.parseInt(parts[2].trim());

                        // 只保留数字 > 1 的行
                        if (number > num) {
                            filteredLines.add(line);
                        }
                        // 数字 <= 1 的行不添加到结果中（即删除）
                    } catch (NumberFormatException e) {
                        // 如果数字解析失败，保留该行（没有数字的行）
                        filteredLines.add(line);
                        log.debug("保留无数字的行: {}", line);
                    }
                } else {
                    // 格式不正确的行（列数不足），也保留
                    filteredLines.add(line);
                    log.debug("保留格式不正确的行: {}", line);
                }
            }
        } catch (IOException e) {
            log.error("读取文件失败: {}", e.getMessage(), e);
            return false;
        }

        // 写入筛选后的结果
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFile))) {
            for (String filteredLine : filteredLines) {
                writer.write(filteredLine);
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            log.error("写入文件失败: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 按照第三列数字大小排序文件
     *
     * @param inputFile 输入文件路径
     * @param outputFile 输出文件路径
     * @param sortOrder 排序顺序：desc(降序，默认) 或 asc(升序)
     * @return 是否成功
     */
    public static boolean sortFileByNumber(String inputFile, String outputFile, String sortOrder) {
        // 使用三个列表分别存储不同类型的行，提高排序效率
        List<LineWithNumber> linesWithNumber = new ArrayList<>();
        List<String> linesWithoutNumber = new ArrayList<>();
        AtomicInteger lineCount = new AtomicInteger(0); // 用于记录原始行号

        try {
            // 使用Files.lines()和流处理提高大文件处理性能
            Files.lines(Paths.get(inputFile))
                    .forEach(line -> {
                        lineCount.incrementAndGet();
                        // 跳过空行
                        if (line.trim().isEmpty()) {
                            return;
                        }

                        // 按制表符分割
                        String[] parts = line.split("\t");
                        if (parts.length >= 3) {
                            try {
                                // 获取第三列的数字
                                int number = Integer.parseInt(parts[2].trim());
                                // 有数字的行，存储行内容和数字
                                linesWithNumber.add(new LineWithNumber(line, number, lineCount.get()));
                            } catch (NumberFormatException e) {
                                // 如果数字解析失败，作为无数字行处理
                                linesWithoutNumber.add(line);
                            }
                        } else {
                            // 格式不正确的行，作为无数字行处理
                            linesWithoutNumber.add(line);
                        }
                    });

            log.info("处理完成：有数字行 {}，无数字行 {}", linesWithNumber.size(), linesWithoutNumber.size());

            // 根据排序顺序对数字行进行排序
            if ("asc".equalsIgnoreCase(sortOrder)) {
                // 升序排序：先按数字升序，数字相同的按原始行号升序
                linesWithNumber.sort(Comparator
                        .comparingInt(LineWithNumber::getNumber)
                        .thenComparingInt(LineWithNumber::getOriginalLineNumber));
            } else {
                // 降序排序：先按数字降序，数字相同的按原始行号升序
                linesWithNumber.sort(Comparator
                        .comparingInt(LineWithNumber::getNumber)
                        .reversed()
                        .thenComparingInt(LineWithNumber::getOriginalLineNumber));
            }

            // 写入排序后的结果
            try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFile))) {
                // 先写入有数字的行（已排序）
                for (LineWithNumber lineWithNumber : linesWithNumber) {
                    writer.write(lineWithNumber.getLine());
                    writer.newLine();
                }

                // 再写入无数字的行（保持原始顺序）
                for (String lineWithoutNumber : linesWithoutNumber) {
                    writer.write(lineWithoutNumber);
                    writer.newLine();
                }
            }

            return true;

        } catch (IOException e) {
            log.error("文件排序失败: {}", e.getMessage(), e);
            return false;
        }
    }

    /**
     * 内部类，用于存储带数字的行，便于排序
     */
    private static class LineWithNumber {
        private final String line;
        private final int number;
        private final int originalLineNumber;

        public LineWithNumber(String line, int number, int originalLineNumber) {
            this.line = line;
            this.number = number;
            this.originalLineNumber = originalLineNumber;
        }

        public String getLine() {
            return line;
        }

        public int getNumber() {
            return number;
        }

        public int getOriginalLineNumber() {
            return originalLineNumber;
        }
    }


}
