package com.yan.demo.tools.textfile;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @Author: sixcolor
 * @Date: 2024-08-22
 * @Description:
 */
public class TextFileUtils {

    /**
     * 将两个文件合并，并移除重复的
     *
     * @param file1Path      源文件1路径
     * @param file2Path      源文件2路径
     * @param outputFilePath 输出文件路径
     * @return 成功true 失败false
     */
    public static boolean mergeTextFiles(String file1Path, String file2Path, String outputFilePath) {
        // 尝试不同的字符编码
        List<Charset> charsets = Arrays.asList(StandardCharsets.UTF_8, StandardCharsets.ISO_8859_1, Charset.forName("GBK"));
        for (Charset charset : charsets) {
            try {
                // 读取第一个文件并将内容存入集合中
                Set<String> lines1 = new HashSet<>(Files.readAllLines(Paths.get(file1Path), charset));

                // 读取第二个文件并将内容存入集合中
                Set<String> lines2 = new HashSet<>(Files.readAllLines(Paths.get(file2Path), charset));

                // 合并两个集合
                lines1.addAll(lines2);

                // 创建一个新集合以去除尾部空行
                Set<String> finalLines = new HashSet<>();
                for (String line : lines1) {
                    finalLines.add(line.trim()); // 去除行首和行尾的空白字符
                }

                // 将合并后的内容写入新文件，确保没有多余的空行
                try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(outputFilePath), charset)) {
                    boolean firstLine = true;
                    for (String line : finalLines) {
                        if (!line.isEmpty()) {
                            if (!firstLine) {
                                // 在写入第一行之前不加换行符
                                writer.newLine();
                            }
                            writer.write(line);
                            firstLine = false;
                        }
                    }
                }
                System.out.println("合并成功！使用字符编码：" + charset.displayName());
                System.out.println("新的文件路径为：" + outputFilePath);
                return true;
            } catch (IOException e) {
                System.out.println("使用字符编码 " + charset.displayName() + " 时出错。");
                e.printStackTrace();
            }
        }
        return false;
    }
}
