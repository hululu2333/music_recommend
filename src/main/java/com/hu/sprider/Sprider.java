package com.hu.sprider;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.hu.bean.MusicInfo;
import com.hu.bean.MusicTypeInfo;
import com.hu.bean.PlayListInfo;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 工具类，提供两个方法
 * 一个从网上爬取音乐信息并存进csv
 * 另一个方法则读取csv里的信息，将音乐下载到指定路径
*/
public class Sprider {

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
            CsvWriter csvWriter = new CsvWriter(filePath,',', Charset.forName("UTF-8"));

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
     * @param filePath csv文件存放的路径
     * @param targetDir 下载的图片存放的路径，路径要以/结尾
     */
    public static void downMusicFromCsvToFs(String filePath, String targetDir){

        try {
            // 创建CSV读对象
            CsvReader csvReader = new CsvReader(filePath, ',', StandardCharsets.UTF_8);

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

            musicInfos.forEach(musicInfo -> {
                new Thread(new MultipleDownload(musicInfo,targetDir)).run();
                try {
                    Thread.sleep(500); //每过0.5秒开启一个线程
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });

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
        private MusicInfo musicInfo;
        private String targetDir; //音乐的储存路径

        public MultipleDownload(MusicInfo musicInfo, String targetDir){
            this.musicInfo=musicInfo;
            this.targetDir=targetDir;
        }


        @Override
        public void run() {
            String href = "http://music.163.com/song/media/outer/url?id="+musicInfo.getMusicId()+".mp3";

            URL url = null;
            URLConnection connection = null;
            InputStream fis=null;
            OutputStream fos=null;
            try {
                url = new URL(href);
                connection = url.openConnection();
                fis = connection.getInputStream();
                fos = new FileOutputStream(targetDir+musicInfo.getMusicName()+".m4a");

                byte[] temp = new byte[1024 * 10];
                int length1 = -1;

                //开始下载
                while ((length1 = fis.read(temp)) != -1) {
                    fos.write(temp,0,length1);
                }
                fos.flush();

                System.out.println(musicInfo.getMusicName()+"：下载完成");

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
