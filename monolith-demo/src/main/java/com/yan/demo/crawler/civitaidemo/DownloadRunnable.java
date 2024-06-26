package com.yan.demo.crawler.civitaidemo;


import org.springframework.util.StringUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

/**
 * @Author: sixcolor
 * @Date: 2024-06-05 11:50
 * @Description:
 */
public class DownloadRunnable implements Runnable{
    private String urlPath;
    private String targetDirectory;
    private String title;

    public DownloadRunnable(String urlPath, String targetDirectory, String title) {
        this.urlPath = urlPath;
        this.targetDirectory = targetDirectory;
        this.title = title;
    }
    @Override
    public void run() {
        try {
            download(urlPath,targetDirectory,title);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void download(String urlPath , String targetDirectory,String title) throws Exception {
        // 解决url中可能有中文情况
        System.out.println("url:"+ urlPath);
        URL url = new URL(urlPath);
        HttpURLConnection http = (HttpURLConnection)url.openConnection();
        http.setConnectTimeout(3000);
        // 设置 User-Agent 避免被拦截
        http.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)");
        String contentType = http.getContentType();
        System.out.println("contentType: "+ contentType);
        // 获取文件大小
        long length = http.getContentLengthLong();
        System.out.println("文件大小："+(length / 1024)+"KB");
        // 获取文件名
        String fileName = getFileName(http , urlPath,title);
        InputStream inputStream = http.getInputStream();
        byte[] buff = new byte[1024*10];
        OutputStream out = new FileOutputStream(new File(targetDirectory,fileName));
        int len ;
        int count = 0; // 计数
        while((len = inputStream.read(buff)) != -1) {
            out.write(buff, 0, len);
            out.flush();
            ++count ;
        }
        System.out.println("count:"+ count);
        // 关闭资源
        out.close();
        inputStream.close();
        http.disconnect();
    }
    private String getFileName(HttpURLConnection http , String urlPath,String title) throws UnsupportedEncodingException {
        String headerField = http.getHeaderField("Content-Disposition");
        String fileName = null ;
        if(null != headerField) {
            String decode = URLDecoder.decode(headerField, "UTF-8");
            fileName = decode.split(";")[1].split("=")[1].replaceAll("\"", "");
            System.out.println("文件名是： "+ fileName);
        }
        if(null == fileName) {
            // 尝试从url中获取文件名
            String[] arr  = urlPath.split("/");
            fileName = arr[arr.length - 1];
            System.out.println("url中获取文件名："+ fileName);
        }
        if (!StringUtils.isEmpty(title)){
            fileName = title+"."+fileName.split("\\.")[1];
        }
        return fileName;
    }
}
