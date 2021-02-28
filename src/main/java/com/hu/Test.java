package com.hu;

import com.csvreader.CsvWriter;
import com.hu.bean.MusicInfo;
import com.hu.bean.MusicTypeInfo;
import com.hu.bean.PlayListInfo;
import com.hu.sprider.UrlGetter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Test {

    @org.junit.Test
    public void test() throws InterruptedException, IOException {
        UrlGetter urlGetter = new UrlGetter();

        List<MusicTypeInfo> musicTypeInfos = urlGetter.getSongTypeUrl();
        List<PlayListInfo> playListInfos = urlGetter.getPlayListUrl(musicTypeInfos);
        List<MusicInfo> musicInfos = urlGetter.getSongUrl(playListInfos);

        String filePath = "D:/test.csv";

        try {
            // 创建CSV写对象
            CsvWriter csvWriter = new CsvWriter(filePath,',', Charset.forName("GBK"));
            //CsvWriter csvWriter = new CsvWriter(filePath);

            // 写表头
            String[] headers = {"音乐标题","音乐id","音乐时长","音乐链接(并不是外链)","歌手","专辑","专辑链接","音乐类型","歌单名"};
            csvWriter.writeRecord(headers);
            musicInfos.forEach(musicInfo -> {
                try {
                    csvWriter.writeRecord(new String[]{musicInfo.getMusicName(),musicInfo.getMusicId(),musicInfo.getMusicDuration(),
                    musicInfo.getMusicHref(),musicInfo.getSinger(),musicInfo.getAlbum(),musicInfo.getAlbumHref(),musicInfo.getMusicType(),musicInfo.getPlayListName()});
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            csvWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
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

