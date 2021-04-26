package com.hu.sprider;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.hu.Entrance;
import com.hu.bean.MusicInfo;
import com.hu.bean.MusicTypeInfo;
import com.hu.bean.PlayListInfo;
import com.hu.sprider.UrlGetter;
import com.hu.util.MysqlUtil;
import org.apache.http.HttpEntity;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.log4j.Logger;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 工具类，提供两个方法
 * 一个从网上爬取音乐信息并存进csv
 * 另一个方法则读取csv里的信息，将音乐下载到指定路径
*/
public class Sprider {
    private static Logger log = Logger.getLogger(Entrance.class.getClass());
    private static final String CONTEXTPATH = "/musicServer/";

    private Sprider(){}


    /**
     * 调用UrlGetter里的一些方法，获得一些音乐信息
     * 并将这些信息存进csv里
     * @param filePath csv文件存放的路径，如D:/test.csv
     */
    public static void getUrlsToCsv(String filePath) throws InterruptedException, IOException {
        UrlGetter urlGetter = new UrlGetter();

        //经过几层调用，拿到音乐信息列表
        List<MusicTypeInfo> musicTypeInfos = urlGetter.getSongTypeUrl();
        List<PlayListInfo> playListInfos = urlGetter.getPlayListUrl(musicTypeInfos);
        List<MusicInfo> musicInfos = urlGetter.getSongUrl(playListInfos);

        try {
            // 创建CSV写对象
            CsvWriter csvWriter = new CsvWriter(filePath,',', Charset.forName("GBK"));

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
     * 读取csv文件中的音乐信息
     * 根据这些信息将音乐下载下来
     * 用四个线程来下载
     * @param filePath csv文件存放的路径
     * @param targetDir 下载的图片存放的路径，路径要以/结尾
     */
    public static void downMusicFromCsvToFs(String filePath, String targetDir){

        try {
            // 创建CSV读对象
            CsvReader csvReader = new CsvReader(filePath, ',',Charset.forName("GBK"));

            csvReader.readHeaders(); //读表头
            List<MusicInfo> musicInfos = new ArrayList<>(); //将一个csv文件重新转化为一个MusicInfo列表
            while (csvReader.readRecord()){
//                System.out.println(csvReader.getRawRecord()); //读一整行

                MusicInfo musicInfo = new MusicInfo();
                musicInfo.setMusicName(csvReader.get("音乐标题"));
                musicInfo.setMusicId(csvReader.get("音乐id"));
                musicInfo.setMusicDuration(csvReader.get("音乐时长"));
                musicInfo.setMusicHref(csvReader.get("音乐链接(并不是外链)"));
                musicInfo.setSinger(csvReader.get("歌手"));
                musicInfo.setAlbum(csvReader.get("专辑"));
                musicInfo.setAlbumHref(csvReader.get("专辑链接"));
                musicInfo.setMusicType(csvReader.get("音乐类型"));
                musicInfo.setPlayListName(csvReader.get("歌单名"));

                musicInfos.add(musicInfo);
            }

            //将csv分为四段，启用四个线程下载
            int size = musicInfos.size();
            int mark1 = size/4; //437
            int mark2 = size-mark1-mark1;
            int mark3 = size-mark1;

            //下次在MultipleDownload中，下载完成一首歌，就往mysql中插入一条记录
            new Thread(new MultipleDownload(musicInfos.subList(0,mark1),targetDir,"线程0")).start();
            new Thread(new MultipleDownload(musicInfos.subList(mark1,mark2),targetDir,"线程1")).start();
            new Thread(new MultipleDownload(musicInfos.subList(mark2,mark3),targetDir,"线程2")).start();
            new Thread(new MultipleDownload(musicInfos.subList(mark3,size-1),targetDir,"线程3")).start();

            System.out.println("所有进程都启动了");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 进程类
     * 实现多进程下载音乐
     * 输入的targetDir必须为这种类型，以/结尾  D:/music/
     */
    static class MultipleDownload implements Runnable{
        private List<MusicInfo> musicInfos; //四分之一音乐信息
        private String targetDir; //音乐的储存路径
        private String threadName; //当前的线程名
        private CloseableHttpClient httpClient= HttpClients.createDefault(); //客户端实例

        public MultipleDownload(List<MusicInfo> musicInfos, String targetDir,String threadName){
            this.musicInfos=musicInfos;
            this.targetDir=targetDir;
            this.threadName=threadName;
        }


        @Override
        public void run() {
            musicInfos.forEach(musicInfo -> {
                String href = "http://music.163.com/song/media/outer/url?id="+musicInfo.getMusicId()+".mp3";

                HttpGet httpget = new HttpGet(href);

                //为get请求配置cookie设置策略，并设置agent，并设置超时时间
                RequestConfig defaultConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).setConnectionRequestTimeout(1000)
                        .setSocketTimeout(1000).setConnectTimeout(1000).build();
                httpget.setConfig(defaultConfig);
                httpget.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:39.0) Gecko/20100101 Firefox/39.0");
                CloseableHttpResponse response = null; //HttpResponse
                try {
                    response = httpClient.execute(httpget);

//                    log.info("response："+response.toString());
//                    log.info("此次请求的状态码为："+response.getStatusLine().getStatusCode());

                    HttpEntity httpEntity= response.getEntity(); //可以看作是目标网页的抽象

                    if(httpEntity.getContentLength()==-1){
                        System.out.println(threadName+"->  "+musicInfo.getMusicName()+"：资源找不到\n");
                    }else if(httpEntity.getContentLength()<600000){
                        log.info("这首音乐太小，判定为下载失败\n");
                    }else{
                        httpEntity.writeTo(new FileOutputStream(targetDir+musicInfo.getMusicName()+".m4a"));
                        System.out.println(threadName+"->  "+musicInfo.getMusicName()+"：下载完成"+"    文件大小："+httpEntity.getContentLength()+"Byte");

                        //下载成功，将音乐信息存入mysql
                        //储存路径为/musicServer/musicInfo.getMusicName()+".m4a"
                        //---------------------------------------------------
                        MysqlUtil.storeMusicInfo(musicInfo,CONTEXTPATH+musicInfo.getMusicName()+".m4a",threadName);

                    }

                } catch (IOException | ClassNotFoundException | SQLException e) {
                    e.printStackTrace();
                }
            });

        }
    }

}
