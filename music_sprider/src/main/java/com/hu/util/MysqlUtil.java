package com.hu.util;

import com.hu.bean.MusicInfo;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * 一个用来执行mysql相关操作的工具类
 */
public class MysqlUtil {
    private static List<Connection> conns=new ArrayList<>(); //自定义连接池

    //静态初始化代码块，用来创建连接池和会话
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            for(int i=0;i<4;i++){
                conns.add(DriverManager.getConnection("jdbc:mysql://localhost:3306/music_recommend?useUnicode=true&characterEncoding=utf-8",
                        "root","1008"));
            }

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }


    //下载一首歌就调用一次这个方法，将音乐相关信息存入mysql
    public static void storeMusicInfo(MusicInfo musicInfo,String storePath,String threadName) throws ClassNotFoundException, SQLException {

        String sql = String.format("insert into music values(%s,'%s','%s','%s','%s','%s','%s','%s','%s','%s')",musicInfo.getMusicId(),
                musicInfo.getMusicName(),musicInfo.getMusicDuration(),musicInfo.getMusicHref(),musicInfo.getSinger(),musicInfo.getAlbum(),
                musicInfo.getAlbumHref(),musicInfo.getMusicType(),musicInfo.getPlayListName(),storePath);

        int connNum = Integer.parseInt(threadName.charAt(2)+"");
        Statement stmt = conns.get(connNum).createStatement();

        try {
            stmt.executeUpdate(sql);
        } catch (Exception e) {
            if(e instanceof MySQLIntegrityConstraintViolationException){
                System.out.println("这首歌数据库中已存在\n");
                return;
            }else{
                System.out.println("向mysql查数据时出现一些其他错误");
                System.out.println("sql："+sql);
                e.printStackTrace();
                return;
            }

        }finally {
            stmt.close();
        }

        System.out.println(musicInfo.getMusicName()+"：已存入数据库\n");
    }
}
