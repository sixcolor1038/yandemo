package com.yan.demo.common.utils.generator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.util.List;

/**
 * @Author: sixcolor
 * @Date: 2024-04-21 15:00
 * @Description:
 */

public class BuilderGenerator {
    private static final Logger log = LoggerFactory.getLogger(BuilderGenerator.class);

    public static void generateByExcel(String className, List<Field> fields) {
        String currentDirectory = System.getProperty("user.dir");
        String outputDirectory = currentDirectory + File.separator + "monolith-demo\\src\\main\\java\\com\\yan\\demo\\" + "generator";
        String filePath = outputDirectory + File.separator + className + ".java";

        generateClassFile(className, fields, filePath);
    }

    public static void generateClassFile(String className, List<Field> fields, String filePath) {
        String classContent = generateClass(className, fields);

        try {
            File outputFile = new File(filePath);

            if (!outputFile.getParentFile().exists()) {
                if (!outputFile.getParentFile().mkdirs()) {
                    log.info("创建文件夹失败.");
                    return;
                }
            }

            try (OutputStream outputStream = Files.newOutputStream(outputFile.toPath())) {
                outputStream.write(classContent.getBytes());
                log.info("生成成功： " + filePath);
            }
            log.info("生成 " + className + ".java 在路径 " + filePath);
        } catch (IOException e) {
            log.info("生成文件失败: " + e.getMessage());
        }
    }

    public static String generateClass(String className, List<Field> fields) {
        StringBuilder sb = new StringBuilder();

        // 生成类定义
        sb.append("public class ").append(className).append(" {\n\n");

        // 生成 fields
        for (Field field : fields) {
            sb.append("    private ").append(field.type).append(" ").append(field.name).append(";\n");
        }
        sb.append("\n");

        // 生成默认构造函数
        sb.append("    public ").append(className).append("() {\n");
        sb.append("    }\n\n");

        // 生成私有构造函数
        sb.append("    private ").append(className).append("(Builder builder) {\n");
        for (Field field : fields) {
            sb.append("        this.").append(field.name).append(" = builder.").append(field.name).append(";\n");
        }
        sb.append("    }\n\n");

        // 生成 getter 和 setter 方法
        for (Field field : fields) {
            sb.append("    public ").append(field.type).append(" get").append(capitalize(field.name)).append("() {\n");
            sb.append("        return ").append(field.name).append(";\n");
            sb.append("    }\n\n");

            sb.append("    public void set").append(capitalize(field.name)).append("(").append(field.type).append(" ").append(field.name).append(") {\n");
            sb.append("        this.").append(field.name).append(" = ").append(field.name).append(";\n");
            sb.append("    }\n\n");
        }

        // 生成 toString 方法
        sb.append("    @Override\n");
        sb.append("    public String toString() {\n");
        sb.append("        return \"").append(className).append("{\" +\n");
        for (Field field : fields) {
            sb.append("                \"").append(field.name).append("='\" + ").append(field.name).append(" + '\\'' +\n");
        }
        sb.append("                '}';\n");
        sb.append("    }\n");

        // 生成 builder method
        sb.append("    public static Builder builder() {\n");
        sb.append("        return new Builder();\n");
        sb.append("    }\n\n");

        // 生成 Builder class
        sb.append("    public static class Builder {\n");
        for (Field field : fields) {
            sb.append("        private ").append(field.type).append(" ").append(field.name).append(";\n");
        }
        sb.append("\n");

        // 生成 builder methods
        for (Field field : fields) {
            sb.append("        public Builder ").append(field.name).append("(").append(field.type).append(" ").append(field.name).append(") {\n");
            sb.append("            this.").append(field.name).append(" = ").append(field.name).append(";\n");
            sb.append("            return this;\n");
            sb.append("        }\n\n");
        }

        // 生成 build method
        sb.append("        public ").append(className).append(" build() {\n");
        sb.append("            return new ").append(className).append("(this);\n");
        sb.append("        }\n");
        sb.append("    }\n");
        sb.append("}");

        return sb.toString();
    }

    private static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static class Field {
        public String type;
        public String name;

        public Field() {
        }

        public void setType(String type) {
            this.type = type;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Field{" +
                    "type='" + type + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
