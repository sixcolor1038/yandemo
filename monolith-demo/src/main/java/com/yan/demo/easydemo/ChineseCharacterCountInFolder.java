package com.yan.demo.easydemo;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @Author: sixcolor
 * @Date: 2024-06-19 0:00
 * @Description: 统计一个文件夹下文件中汉字的数量
 */
public class ChineseCharacterCountInFolder {

    public static void main(String[] args) {
        try {
            String sqlQuery = "select * from COMMON_REC where ID = 11";
            List<Map<String, Object>> maps = jdbcTemplate.queryForList(sqlQuery);
            Object remark = maps.get(0).get("REMARK");
            String folderPath = remark.toString();
            System.out.println(folderPath);
            File folder = new File(folderPath);
            int totalChineseCharCount = traverseAndCount(folder);
            System.out.println("------当前文件夹下总汉字数 " + folderPath + ": " + totalChineseCharCount);
            String sqlUpdate = "UPDATE COMMON_REC SET VALUE = ? where ID = 11";
            int update = jdbcTemplate.update(sqlUpdate, totalChineseCharCount);
            System.out.println(update);
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

    private static final JdbcTemplate jdbcTemplate;

    static {
        String driver = "com.mysql.cj.jdbc.Driver";//mysql驱动
        String url = "jdbc:mysql://192.168.100.12:13306/yan_demo";//连接地址
        String user = "root";//用户
        String password = "rootroot";//密码

        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setUrl(url);
        dataSource.setDriverClassName(driver);
        dataSource.setUsername(user);
        dataSource.setPassword(password);

        jdbcTemplate = new JdbcTemplate(dataSource);
    }


}

