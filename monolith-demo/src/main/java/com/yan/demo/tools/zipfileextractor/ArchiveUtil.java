package com.yan.demo.tools.zipfileextractor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ArchiveUtil {
    private static final Logger log = LoggerFactory.getLogger(ArchiveUtil.class);


    public static boolean batchExtractFiles(String folderPath, String destFolder, List<String> passwords) {
        File folder = new File(folderPath);
        if (!folder.isDirectory()) {
            log.error("提供的路径不是一个文件夹：{}", folderPath);
            return false;
        }

        File[] files = folder.listFiles((dir, name) -> name.endsWith(".zip") || name.endsWith(".rar") || name.endsWith(".7z"));
        log.info(Arrays.toString(files));
        if (files == null || files.length == 0) {
            log.info("文件夹中没有找到压缩文件：{}", folderPath);
            return false;
        }

        for (File file : files) {
            String fileFullName = file.getAbsolutePath();
            String fileNameWithoutExtension = file.getName().replaceFirst("[.][^.]+$", ""); // Remove extension
            String filePath = new File(destFolder, fileNameWithoutExtension).getAbsolutePath();

            boolean success = false;
            if (file.getName().endsWith(".zip")) {
                success = ZipUtil.crackZipFile(fileFullName, filePath, passwords);
            } else if (file.getName().endsWith(".rar")) {
                success = RarUtil.extractRarFile(fileFullName, filePath);
            } else if (file.getName().endsWith(".7z")) {
                //success = SevenZipUtil.extract7zFile(fileFullName, filePath);
            }

            if (success) {
                log.info("成功解压文件 {}", fileFullName);
            } else {
                log.error("无法解压文件 {}，没有找到有效的密码", fileFullName);
                return false;
            }
        }
        return true;
    }
}

