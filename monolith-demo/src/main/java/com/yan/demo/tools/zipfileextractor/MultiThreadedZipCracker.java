package com.yan.demo.tools.zipfileextractor;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.*;

public class MultiThreadedZipCracker {
    private static final Logger log = LoggerFactory.getLogger(MultiThreadedZipCracker.class);
    private static volatile boolean passwordFound = false;

    public static boolean crackZipFileWithThreads(String zipFilePath, String destDir, List<String> passwords, int threadCount) {
        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        int passwordsPerThread = passwords.size() / threadCount;
        CompletionService<Boolean> completionService = new ExecutorCompletionService<>(executor);

        for (int i = 0; i < threadCount; i++) {
            int start = i * passwordsPerThread;
            int end = (i == threadCount - 1) ? passwords.size() : (i + 1) * passwordsPerThread;
            List<String> subList = passwords.subList(start, end);
            completionService.submit(() -> tryPasswords(zipFilePath, destDir, subList));
        }

        executor.shutdown();
        try {
            for (int i = 0; i < threadCount; i++) {
                Future<Boolean> result = completionService.take();
                if (result.get()) {
                    passwordFound = true;
                    break;
                }
            }
        } catch (InterruptedException | ExecutionException e) {
            log.error("线程执行出错", e);
        } finally {
            executor.shutdownNow();
        }

        return passwordFound;
    }

    private static boolean tryPasswords(String zipFilePath, String destDir, List<String> passwords) {
        if (passwordFound) {
            return false;
        }

        try {
            ZipFile zipFile = new ZipFile(zipFilePath);
            zipFile.setCharset(Charset.forName("GBK"));  // 尝试使用 GBK 处理中文文件名

            if (!zipFile.isEncrypted()) {
                log.info("解压路径: {}", destDir);
                zipFile.extractAll(destDir);
                log.info("文件未加密，解压成功！");
                return true;
            }

            for (String password : passwords) {
                if (passwordFound) {
                    return false; // 其他线程已找到密码，提前退出
                }

                try {
                    zipFile.setPassword(password.toCharArray());
                    log.info("尝试解压到路径: {}", destDir);
                    zipFile.extractAll(destDir);  // 解压到指定路径
                    log.info("解压成功，使用的密码是: {}", password);
                    passwordFound = true;
                    return true;
                } catch (ZipException e) {
                    log.info("解压失败，尝试的密码是: {}", password);
                }
            }
        } catch (ZipException e) {
            log.error("文件解压出错: {}", e.getMessage());
        }

        return false;
    }


    public static void main(String[] args) throws IOException {
        // 假设文件路径和密码本路径
        String zipFilePath = "D:\\test\\123.zip";
        String destDir = "D:\\test";
        List<String> passwords = PasswordReader.readPasswords("E:\\workspace\\数据\\解压工具\\passwords.txt");

        // 使用 10 个线程来并行尝试密码
        boolean success = crackZipFileWithThreads(zipFilePath, destDir, passwords, 10);

        if (success) {
            log.info("解压成功");
        } else {
            log.error("未找到有效密码，解压失败");
        }
    }
}
