package com.yan.demo.tools.zipfileextractor;

import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.exception.ZipException;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.AesKeyStrength;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @desc zip解压缩工具
 * @author: wcy
 * @date: 2024/1/12
 * @version: 1.0
 */
public class ZipUtil {
    private static final Logger log = LoggerFactory.getLogger(ZipUtil.class);

    public static boolean crackZipFile(String zipFilePath, String destDir, List<String> passwords) {
        try {
            ZipFile zipFile = new ZipFile(zipFilePath);

            if (!zipFile.isEncrypted()) {
                zipFile.extractAll(destDir);
                System.out.println("文件未加密，解压成功！");
                return true;
            }

            for (String password : passwords) {
                try {
                    zipFile.setPassword(password.toCharArray());
                    zipFile.extractAll(destDir);
                    System.out.println("解压成功，使用的密码是: " + password);
                    return true;  // 解压成功
                } catch (ZipException e) {
                    System.out.println("解压失败，尝试的密码是: " + password);
                }
            }
        } catch (ZipException e) {
            e.printStackTrace();
        }
        return false;  // 所有密码均失败
    }

    /**
     * 压缩指定路径的文件
     * @param srcFilePath 待压缩文件路径
     * @param zipPathFileName zip文件全路径名
     * @param password 加密密码
     * @return
     */
    public static boolean zipFile(String srcFilePath, String zipPathFileName, String password){

        try {
            // 生成的压缩文件
            ZipFile zipFile = new ZipFile(zipPathFileName);
            if (StringUtils.isNotEmpty(password)) {
                zipFile.setPassword(password.toCharArray());
            }
            ZipParameters parameters = new ZipParameters();
            // 压缩级别
            parameters.setCompressionMethod(CompressionMethod.DEFLATE);
            parameters.setCompressionLevel(CompressionLevel.NORMAL);

            if(StringUtils.isNotEmpty(password)){
                parameters.setEncryptFiles(true);
                parameters.setEncryptionMethod(EncryptionMethod.AES);
                parameters.setAesKeyStrength(AesKeyStrength.KEY_STRENGTH_256);
            }

            // 要打包的文件夹
            File file = new File(srcFilePath);
            if (file.isDirectory()) {
                zipFile.addFolder(file, parameters);
            } else {
                zipFile.addFile(file, parameters);
            }
            return true;
        } catch (ZipException e) {
            e.printStackTrace();
            log.error("压缩文件【"+srcFilePath+"】到路径【"+zipPathFileName+"】失败：\n"+e.getMessage());
            return false;
        }
    }

    /**
     *  @param zipFileFullName zip文件所在的路径名
     * @param filePath 解压到的路径
     * @param password 需要解压的密码
     * @return
     */
    public static boolean unZipFile(String zipFileFullName, String filePath, String password) {
        try {
            ZipFile zipFile = new ZipFile(zipFileFullName);
            // 如果解压需要密码
            if(StringUtils.isNotEmpty(password) && zipFile.isEncrypted()) {
                zipFile.setPassword(password.toCharArray());
            }
            zipFile.extractAll(filePath);
            return true;
        } catch (ZipException e) {
            e.printStackTrace();
            log.error("解压文件【"+zipFileFullName+"】到路径【"+filePath+"】失败：\n"+e.getMessage());
            return false;
        }
    }

    /**
     * 添加文件到压缩文件中
     * @param zipFullFileName zip文件所在路径及全名
     * @param fullFileNameList 待添加的文件全路径集合
     * @param rootFolderInZip 在压缩文件里的文件夹名
     * @return
     */
    public static boolean addFilesToZip(String zipFullFileName, List<String> fullFileNameList, String rootFolderInZip) {
        try {
            ZipFile zipFile = new ZipFile(zipFullFileName);
            ArrayList<File> addFiles = new ArrayList<>();
            for (String fileName : fullFileNameList) {
                addFiles.add(new File(fileName));
            }

            ZipParameters parameters = new ZipParameters();
            parameters.setCompressionMethod(CompressionMethod.DEFLATE);
            parameters.setCompressionLevel(CompressionLevel.NORMAL);
            if(StringUtils.isNotEmpty(rootFolderInZip)){
                if(!rootFolderInZip.endsWith("/")){
                    rootFolderInZip = rootFolderInZip+"/";
                }
                parameters.setRootFolderNameInZip(rootFolderInZip);
            }
            zipFile.addFiles(addFiles, parameters);
            return true;
        } catch (ZipException e) {
            e.printStackTrace();
            log.error("添加文件失败：\n"+e.getMessage());
            return false;
        }
    }

    /**
     * 从压缩文件中删除路径
     * @param zipFullFileName
     * @param fileName
     * @return
     */
    public static boolean deleteFileInZip(String zipFullFileName, String fileName) {
        try {
            ZipFile zipFile = new ZipFile(zipFullFileName);
            zipFile.removeFile(fileName);
            return true;
        } catch (ZipException e) {
            e.printStackTrace();
            log.error("删除文件失败：\n"+e.getMessage());
            return false;
        }
    }

}