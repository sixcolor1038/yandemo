package com.yan.demo.tools.zipfileextractor;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.util.*;

public class CheckDuplicates {
    public static void main(String[] args) {
        String filePath = "密码缓存位置";
        Charset charset = Charset.forName("ISO-8859-1");

        try {
            // 读取文件内容
            List<String> lines = Files.readAllLines(Paths.get(filePath), charset);

            // 使用集合检查重复行
            Set<String> uniqueLines = new HashSet<>();
            boolean hasDuplicates = false;

            for (String line : lines) {
                if (!uniqueLines.add(line)) {
                    System.out.println("找到重复行: " + line);
                    hasDuplicates = true;
                }
            }

            if (!hasDuplicates) {
                System.out.println("文件中没有重复行。");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
