package com.yan.demo.tools.zipfileextractor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @Author: sixcolor
 * @Date: 2024-08-16
 * @Description:
 */
public class PasswordReader {
    public static List<String> readPasswords(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath));
    }
}

