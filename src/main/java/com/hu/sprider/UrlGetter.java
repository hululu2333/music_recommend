package com.hu.sprider;

import com.hu.bean.MusicInfo;
import com.hu.bean.MusicTypeInfo;
import com.hu.bean.PlayListInfo;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * 用来获取各种url
 */

public class UrlGetter {

    private WebDriver driver; //浏览器驱动
    private Logger log = Logger.getLogger(UrlGetter.class.getClass());

    /**
     * 构造函数设为私有
     */
    public UrlGetter(){
        //配置谷歌浏览器驱动的路径
        System.setProperty("webdriver.chrome.driver", "F:\\chromedriver.exe");
        driver = new ChromeDriver();
    }


    /**
     * 根据网易云音乐的根网站
     * 拿到各种类型音乐的网页url
     */
    public List<MusicTypeInfo> getSongTypeUrl() throws InterruptedException {
        String url="https://music.163.com/";

        driver.get(url); //根据url创建一个浏览器界面
        driver.switchTo().frame("g_iframe"); //一个源码中包含多个框架，定位到包含所需信息的那个框架
        Thread.sleep(200);

        //定位到包含音乐类型信息的那个Element
        WebElement types = driver.findElement(By.xpath("//*[@id=\"discover-module\"]/div[1]/div/div/div[1]/div/div"));

        //拿到所有a标签
        List<WebElement> as=types.findElements(By.className("s-fc3"));

        //从a标签中提取出音乐类型的相关信息
        List<MusicTypeInfo> musicTypes = new ArrayList<>();
        for(WebElement we : as){
            MusicTypeInfo mti = new MusicTypeInfo();
            mti.setHref(we.getAttribute("href"));
            mti.setType(we.getText());

            log.info("音乐类型信息："+mti.toString());

            musicTypes.add(mti);
        }

        //driver.quit();
        return musicTypes;
    }


    /**
     * 根据音乐类型url，获取各个歌单的url
     * @param musicTypes 音乐类型信息
     */
    public List<PlayListInfo> getPlayListUrl(List<MusicTypeInfo> musicTypes) throws IOException, InterruptedException {
        List<PlayListInfo> playLists = new ArrayList<>(); //歌单信息

        //遍历每个音乐类型界面，将歌单信息全部提取出来
        for(MusicTypeInfo musicTypeInfo : musicTypes){
            driver.get(musicTypeInfo.getHref());
            driver.switchTo().frame("g_iframe");
            Thread.sleep(200);

            //定位到包含歌单信息的那个Element
            WebElement ele = driver.findElement(By.xpath("//*[@id=\"m-pl-container\"]"));

            //拿到包含歌单信息的a标签们
            List<WebElement> as = ele.findElements(By.className("msk"));

            //将这个音乐类型界面的所有歌单信息提取出来
            for(WebElement we : as){
                PlayListInfo pli = new PlayListInfo();
                pli.setHref(we.getAttribute("href"));
                pli.setPlayListName(we.getAttribute("title"));
                pli.setMusicType(musicTypeInfo.getType());

                log.info("歌单信息："+pli.toString());

                playLists.add(pli);
            }

            //driver.quit();
        }
        return playLists;
    }


    /**
     * 传入所有的播放列表信息
     * 遍历他们，把所有音乐信息提取出来
     * @param playListInfos 播放列表信息
     * @return 所有爬下来的音乐信息
     * @throws InterruptedException
     */
    public List<MusicInfo> getSongUrl(List<PlayListInfo> playListInfos) throws InterruptedException {
        List<MusicInfo> musicInfos = new ArrayList<>(); //音乐信息

        //遍历每个歌单界面，将音乐信息提取出来
        for(PlayListInfo playListInfo : playListInfos){
            driver.get(playListInfo.getHref());
            driver.switchTo().frame("g_iframe");
            Thread.sleep(200);

            //拿到包含所有音乐信息的element
            WebElement we = driver.findElement(By.tagName("tbody"));

            //一个tr标签包含一个完整的音乐信息
            List<WebElement> trs = we.findElements(By.tagName("tr"));

            //遍历每个tr标签，将音乐信息提取出来
            for (WebElement tr : trs){
                MusicInfo musicInfo = new MusicInfo();

                //获得音乐链接
                String musicHref = tr.findElement(By.tagName("a")).getAttribute("href");
                musicInfo.setMusicHref(musicHref);

                //这个td标签包含音乐名 音乐id 音乐时长和歌手名
                WebElement td = tr.findElements(By.tagName("td")).get(2);
                musicInfo.setMusicDuration(td.findElement(By.tagName("span")).getText());
                musicInfo.setMusicId(tr.findElement(By.className("icn-share")).getAttribute("data-res-id"));
                musicInfo.setMusicName(tr.findElement(By.className("icn-share")).getAttribute("data-res-name"));
                musicInfo.setSinger(tr.findElement(By.className("icn-share")).getAttribute("data-res-author"));

                //获得音乐专辑和该专辑的链接
                List<WebElement> tds = tr.findElements(By.tagName("td"));
                musicInfo.setAlbumHref(tds.get(4).findElement(By.tagName("a")).getAttribute("href"));
                musicInfo.setAlbum(tds.get(4).findElement(By.tagName("a")).getAttribute("title"));

                //音乐类型和歌单名
                musicInfo.setMusicType(playListInfo.getMusicType());
                musicInfo.setPlayListName(playListInfo.getPlayListName());

                musicInfos.add(musicInfo);
                log.info("音乐信息："+musicInfo.toString());
            }

        }

        return musicInfos;
    }
}
