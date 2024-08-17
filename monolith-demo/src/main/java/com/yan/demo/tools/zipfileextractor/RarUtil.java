package com.yan.demo.tools.zipfileextractor;

import com.github.junrar.Archive;
import com.github.junrar.rarfile.FileHeader;
import com.github.junrar.exception.RarException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class RarUtil {

    public static boolean extractRarFile(String rarFilePath, String destDir) {
        try (Archive archive = new Archive(new File(rarFilePath))) {
            FileHeader fileHeader;
            while ((fileHeader = archive.nextFileHeader()) != null) {
                File outFile = new File(destDir, fileHeader.getFileNameString().trim());
                if (fileHeader.isDirectory()) {
                    outFile.mkdirs();
                } else {
                    new File(outFile.getParent()).mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(outFile)) {
                        InputStream is = archive.getInputStream(fileHeader);
                        byte[] buffer = new byte[1024];
                        int read;
                        while ((read = is.read(buffer)) != -1) {
                            fos.write(buffer, 0, read);
                        }
                        fos.flush();
                    }
                }
            }
            return true;
        } catch (RarException | IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
