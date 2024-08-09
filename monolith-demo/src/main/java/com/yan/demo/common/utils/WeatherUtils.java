package com.yan.demo.common.utils;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.zip.GZIPInputStream;

/**
 * @Author: sixcolor
 * @Date: 2024-08-09
 * @Description:
 */
@Slf4j
public class WeatherUtils {

    /**
     * 从和风天气获取天气信息
     *
     * @param location 城市编码
     * @param key      API Key
     * @return String
     */
    public static String getWeatherByHFTQ(String location, String key) {

        StringBuilder sb = new StringBuilder();
        try {
            String weather_url =
                    "https://devapi.qweather.com/v7/weather/now?location=" + location + "&key=" + key;
            URL url = new URL(weather_url);
            URLConnection conn = url.openConnection();
            InputStream is = conn.getInputStream();
            GZIPInputStream gzin = new GZIPInputStream(is);
            InputStreamReader isr = new InputStreamReader(gzin, StandardCharsets.UTF_8); // 设置读取流的编码格式，自定义编码
            BufferedReader reader = new BufferedReader(isr);
            String line;
            while ((line = reader.readLine()) != null)
                sb.append(line).append(" ");
            reader.close();

            // 解析JSON格式的天气信息数据
            JSONObject json = JSONObject.parseObject(sb.toString());
            JSONObject now = json.getJSONObject("now");
            String obsTime = now.getString("obsTime");//数据观测时间
            String temp = now.getString("temp"); // 获取温度
            String feelsLike = now.getString("feelsLike"); //体感温度
            String feelsLikeLevel = getFeelsLikeLevel(new Double(feelsLike));
            String text = now.getString("text"); //天气状况的文字描述，包括阴晴雨雪等天气状态的描述
            String windDir = now.getString("windDir"); // 获取风向
            String windSpeed = now.getString("windSpeed"); // 获取风速，公里/小时
            String windScale = now.getString("windScale"); // 获取风力
            String humidity = now.getString("humidity"); // 获取湿度
            String pressure = now.getString("pressure"); // 大气压强，默认单位：百帕
            // 输出获取到的天气信息
            System.out.println("城市编号：" + location);
            System.out.println("数据观测时间：" + obsTime);
            System.out.println("天气状况的文字描述：" + text);
            System.out.println("温度：" + temp + "℃");
            System.out.println("体感温度：" + feelsLike + "℃");
            System.out.println("体感等级：" + feelsLikeLevel);
            System.out.println("湿度：" + humidity + "%");
            System.out.println("风向：" + windDir);
            System.out.println("风速：" + windSpeed + "公里/小时");
            System.out.println("风力：" + windScale + "级");
            System.out.println("气压：" + pressure + "百帕");

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("获取天气信息失败：" + e);
        }

        return sb.toString();
    }

    /**
     * 根据体感温度获取体感等级
     *
     * @param temp 体感温度
     * @return 体感等级
     */
    public static String getFeelsLikeLevel(double temp) {
        String level;
        if (temp > 40) {
            level = "极热";
        } else if (temp > 35) {
            level = "酷热";
        } else if (temp > 30) {
            level = "炎热";
        } else if (temp > 27) {
            level = "温暖";
        } else if (temp > 20) {
            level = "适宜";
        } else if (temp > 15) {
            level = "有点凉";
        } else if (temp > 10) {
            level = "凉爽";
        } else if (temp > 5) {
            level = "微冷";
        } else {
            level = "寒冷";
        }
        return level;
    }

}
