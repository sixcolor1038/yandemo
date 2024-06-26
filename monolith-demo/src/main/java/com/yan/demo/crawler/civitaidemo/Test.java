package com.yan.demo.crawler.civitaidemo;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * @Author: sixcolor
 * @Date: 2024-06-05 11:50
 * @Description:
 */
public class Test {
    public static void main(String[] args) throws Exception {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(20, 20, 1, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        //翻页使用
        int pageNum = 0;
        //电影排名计数
        int count = 1;

        for (int i = 0; i < 10; i++) {
            String url = "https://movie.douban.com/top250?start=" + pageNum + "&filter=";
            //解析网页（Jsoup返回Document就是浏览器Document对象）
            Document document = Jsoup.connect(url).header("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:33.0) Gecko/20100101 Firefox/33.0").get();
            //获取所有li标签
            Elements name = document.getElementsByClass("article");
            for (Element element : name) {
                Elements li = element.getElementsByTag("li");

                for (Element element1 : li) {
                    //获取标题
                    String title = count + "-" + element1.getElementsByTag("img").attr("alt");
                    //获取图片地址
                    String attr = element1.getElementsByTag("img").attr("src");
                    //下载图片
                    executor.submit(new DownloadRunnable(attr, "/Users/XXX/Desktop/img", title));
                    count++;
                }

            }
            pageNum += 25;
        }
        executor.shutdown();
    }

}