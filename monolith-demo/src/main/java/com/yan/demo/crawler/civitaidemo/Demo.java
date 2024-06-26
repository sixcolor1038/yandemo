package com.yan.demo.crawler.civitaidemo;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @Author: sixcolor
 * @Date: 2024-06-09 15:35
 * @Description:
 */
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Demo {
    public static void main(String[] args) {
        try {
            String url = "https://civitai.com/user/AleksPls/images";
            // 设置连接和读取超时时间为10秒
            Document doc = Jsoup.connect(url).timeout(10000).get();

            Elements imgElements = doc.select("img");

            for (Element imgElement : imgElements) {
                String imgUrl = imgElement.attr("src");
                // 检查 imgUrl 是否以 "http" 开头，避免相对路径问题
                if (!imgUrl.startsWith("http")) {
                    imgUrl = "https://civitai.com" + imgUrl;
                }
                // 确保目标目录存在
                Files.createDirectories(Paths.get("E:/image/ss"));
                downloadImage(imgUrl, "E:/image/ss/" + imgUrl.substring(imgUrl.lastIndexOf('/') + 1));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void downloadImage(String imgUrl, String destFile) {
        try (InputStream in = new URL(imgUrl).openStream();
             FileOutputStream out = new FileOutputStream(destFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

