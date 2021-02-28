package com.hu.sprider;

import com.csvreader.CsvReader;
import com.csvreader.CsvWriter;
import com.hu.bean.MusicInfo;
import com.hu.bean.MusicTypeInfo;
import com.hu.bean.PlayListInfo;

import java.io.IOException;
import java.nio.charset.Charset;
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
     */
    public static void downMusicFromCsv(String filePath){


        String refHref = "http://music.163.com/song/media/outer/url?id=317151.mp3";

        try {
            // 创建CSV读对象
            CsvReader csvReader = new CsvReader(filePath);

            // 读表头
            csvReader.readHeaders();
            while (csvReader.readRecord()){
                // 读一整行
                System.out.println(csvReader.getRawRecord());
                // 读这行的某一列
//                System.out.println(csvReader.get("Link"));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
