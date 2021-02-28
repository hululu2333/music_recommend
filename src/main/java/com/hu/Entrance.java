package com.hu;

import com.hu.sprider.Sprider;
import com.hu.sprider.UrlGetter;
import org.apache.log4j.Logger;

import java.io.IOException;

/**
 * 爬虫模块的入口
 */
public class Entrance {
    private static Logger log = Logger.getLogger(Entrance.class.getClass());

    public static void main(String[] args) throws IOException, InterruptedException {
        Sprider.getUrlsToCsv("D:/songInfos.csv");
        log.info("包含音乐信息的csv已生成");

        Sprider.downMusicFromCsv("D:/songInfos.csv");

    }
}
