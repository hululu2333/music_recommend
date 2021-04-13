package com.hu;

import com.csvreader.CsvWriter;
import com.hu.bean.MusicInfo;
import com.hu.bean.MusicTypeInfo;
import com.hu.bean.PlayListInfo;
import com.hu.sprider.UrlGetter;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Test {


    @org.junit.Test
    public void test() throws InterruptedException, IOException {
//        String href = "http://music.163.com/song/media/outer/url?id=1421454397.mp3";
//        //String href ="https://gimg2.baidu.com/image_search/src=http%3A%2F%2Fa0.att.hudong.com%2F30%2F29%2F01300000201438121627296084016.jpg&refer=http%3A%2F%2Fa0.att.hudong.com&app=2002&size=f9999,10000&q=a80&n=0&g=0n&fmt=jpeg?sec=1617338141&t=11fdbc2d515a7f9db444a3a771a307eb";
//
//
//        //创建http客户端并拿到目标网页的源码
//        CloseableHttpClient httpClient= HttpClients.createDefault();
//        HttpGet httpget = new HttpGet(href);
//        httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:39.0) Gecko/20100101 Firefox/39.0");
//
////        httpget.setHeader();
//        CloseableHttpResponse response = httpClient.execute(httpget); //HttpResponse
//        HttpEntity httpEntity= response.getEntity(); //可以看作是目标网页的抽象
//
//        httpEntity.writeTo(new FileOutputStream("D:/text.m4a"));
//
//        System.out.println(httpEntity.getContentType());

        String a = "上海市上海城区松江区漕河泾开发区松江高科技园莘砖公路518号5幢705室";

        System.out.println(a.split("市")[0]);

    }


    /**
     * 根据音乐类型url获得歌单url
     * @throws InterruptedException
     */
    @org.junit.Test
    public void test2() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "F:\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        List<String> urls = new ArrayList<>();

        driver.get("https://music.163.com/#/discover/playlist/?cat=华语");
        driver.switchTo().frame("g_iframe");

        Thread.sleep(2000);



        //将范围定位到播放列表
        WebElement playList = driver.findElement(By.xpath("//*[@id=\"m-pl-container\"]"));

        System.out.println(playList.getText());

        //拿到所有a标签
        List<WebElement> as=playList.findElements(By.className("msk"));


        int count=0;
        System.out.println(as.size());
        for(WebElement we : as){
            System.out.println(count++);
            //System.out.println(we.getText());
            String a = we.getAttribute("href");
            System.out.println(a);
        }




//        //执行浏览器后退
//        driver.navigate().back();
//        //System.out.printf("back to %s \n", driver.getCurrentUrl());
//
//        //执行浏览器前面
//        driver.navigate().forward();
//        //System.out.printf("forward to %s \n", driver.getCurrentUrl());

        //driver.quit();
    }
}

