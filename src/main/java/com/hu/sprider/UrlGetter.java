package com.hu.sprider;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 * 用来获取各种url
 */
public class UrlGetter {
    private static CloseableHttpClient httpClient= HttpClients.createDefault(); //http客户端

    /**
     * 构造函数设为私有
     */
    private UrlGetter(){

    }


    /**
     * 根据根url，获取各个歌单的url
     * @param url 根网站url
     */
    public static void getPlayListUrl(String url) throws IOException {
        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response = httpClient.execute(httpget); //HttpResponse
        HttpEntity httpEntity= response.getEntity(); //可以看作是目标网页的抽象

        //将目标网页转换成字符串
        String allContent = EntityUtils.toString(httpEntity, "utf-8");


        System.out.println(allContent);
        //提取出包含鸟类url的源码
        String regex1 = "<h1>鸟类图片大全</h1>.*</div>";
        Pattern pattern1 = Pattern.compile(regex1);
        Matcher matcher1 = pattern1.matcher(allContent);
        matcher1.find();
        String birdContent = matcher1.group(0);
    }
}
