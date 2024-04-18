package com.yan.demo.common.utils;

import com.yan.demo.common.constant.DateConstant;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * @Author: sixcolor
 * @Date: 2024-04-17 14:55
 * @Description:
 */
public class FileUtils {
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
            System.out.println("Provided path is not a directory.");
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
                        newFileName = newFileName.replaceFirst("^" + prefix, "");
                    }

                    // 如果文件名发生变化，执行重命名操作
                    if (!fileName.equals(newFileName)) {
                        File newFile = new File(directory, newFileName);
                        if (!file.renameTo(newFile)) {
                            System.out.println("Failed to rename: " + file.getPath());
                        } else {
                            System.out.println("Renamed to: " + newFile.getPath());
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
            System.out.println("Provided path is not a directory.");
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
                    if (!DateConstant.TIMESTAMP_PATTERN.matcher(nameWithoutExtension).matches()) {
                        // 生成新的文件名，使用当前时间的时间戳
                        String timestamp = LocalDateTime.now().format(DateConstant.TIMESTAMP_FORMAT);
                        File newFile = new File(directory, timestamp + extension);

                        // 确保文件名唯一，避免命名冲突
                        while (newFile.exists()) {
                            // 微调时间戳以保证唯一性
                            timestamp = LocalDateTime.now().format(DateConstant.TIMESTAMP_FORMAT);
                            newFile = new File(directory, timestamp + extension);
                        }

                        // 重命名文件
                        if (!file.renameTo(newFile)) {
                            System.out.println("Failed to rename: " + file.getPath());
                        } else {
                            System.out.println("Renamed to: " + newFile.getPath());
                        }
                    }
                }
            }
        }
    }

    /**
     * 统计指定目录及其子目录下的所有文件和文件夹的数量，按文件扩展名分类统计文件。
     *
     * @param directoryPath 目录的路径。
     * @return 包含文件统计信息的Map，其中包含嵌套的结构以表示子目录统计。
     */
    public static Map<String, Object> countFilesAndFolders(String directoryPath) {
        directoryPath = normalizePath(directoryPath);
        File directory = new File(directoryPath);
        Map<String, Object> stats = new TreeMap<>();
        if (directory.exists() && directory.isDirectory()) {
            countFilesRecursive(directory, stats, 0);
        }
        return stats;
    }

    /**
     * 递归统计文件和文件夹
     *
     * @param directory 目录文件对象
     * @param stats     统计结果映射表
     * @param level     当前递归的层级
     */
    private static void countFilesRecursive(File directory, Map<String, Object> stats, int level) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    Map<String, Object> subStats = new TreeMap<>();
                    countFilesRecursive(file, subStats, level + 1);
                    stats.put(file.getName(), subStats);
                } else {
                    // 如果是文件，按扩展名统计
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
                System.out.println(indent + entry.getKey() + ":");
                @SuppressWarnings("unchecked")
                Map<String, Object> subMap = (Map<String, Object>) entry.getValue();
                printStatistics(subMap, level + 1);
            } else {
                System.out.println(indent + entry.getKey() + ": " + entry.getValue());
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

}
