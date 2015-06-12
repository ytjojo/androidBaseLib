package com.kerkr.edu.utill;

import java.io.File;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;


import android.util.Log;

public class CompressUtil {
    public static void unZipFile(String zipFileName, String targetBaseDir) {
        if (!targetBaseDir.endsWith(File.separator)) {
            targetBaseDir += File.separator;
        }
        try {
            
            ZipFile zipFile = new ZipFile(zipFileName);
            
            if (zipFile.isEncrypted()) {
                // 在这里输入密码哦，如果写错了，会报异常的说
                zipFile.setPassword("viewalloc@meishidiandian");
            }
            zipFile.extractAll(targetBaseDir);
        }
        catch (ZipException e) {
        }
    }
    
}
