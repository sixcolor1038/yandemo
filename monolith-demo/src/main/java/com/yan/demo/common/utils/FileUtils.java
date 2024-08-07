package com.yan.demo.common.utils;

import com.yan.demo.common.constant.DateConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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

}
