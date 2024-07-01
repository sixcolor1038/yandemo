package com.yan.demo.easydemo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * @Author: sixcolor
 * @Date: 2024-06-19 0:00
 * @Description: 统计一个文件夹下文件中汉字的数量
 */

public class ChineseCharacterCountInFolder {

    public static void main(String[] args) {
        String folderPath = "E:\\";
        File folder = new File(folderPath);

        try {
            int totalChineseCharCount = traverseAndCount(folder);
            System.out.println("------当前文件夹下总汉字数 " + folderPath + ": " + totalChineseCharCount);
        } catch (IOException e) {
            System.err.println("Error reading files: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static int traverseAndCount(File dir) throws IOException {
        int totalChineseChars = 0;
        File[] files = dir.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isFile()) {
                    int charCount = countChineseCharactersInFile(file);
                    System.out.println(file.getName() + " 字数: " + charCount);
                    totalChineseChars += charCount;
                } else if (file.isDirectory()) {
                    totalChineseChars += traverseAndCount(file);
                }
            }
        }
        return totalChineseChars;
    }

    private static int countChineseCharactersInFile(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            int chineseCharCount = 0;
            String line;
            while ((line = reader.readLine()) != null) {

                line = line.replaceAll("[^一-龥]", "");
                chineseCharCount += line.length();
            }
            return chineseCharCount;
        }
    }


}

