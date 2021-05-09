package com.hu.server.api;

import com.hu.server.exception.SocketNotConnectedcException;
import com.hu.server.http.HttpClientHu;
import com.hu.server.model.dto.ResponseDto;
import com.hu.server.model.entity.MusicInfo;
import com.hu.server.util.ServerUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.ConnectException;
import java.sql.*;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@CrossOrigin
@RestController
@Log4j2
public class MusicHandler {
    private static Connection conn;
    private static Statement stat;

    //静态初始化代码块，用来创建连接和会话
    static {
        try {
            Class.forName("com.mysql.jdbc.Driver");

            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/music_recommend?useUnicode=true&characterEncoding=utf-8&serverTimezone=GMT%2B8",
                    "root", "1008");

            stat = conn.createStatement();

        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }


    /**
     * 调用这个接口
     * 随机返回11首音乐的信息
     *
     * @return
     */
    @RequestMapping("/hotMusic")
    public ResponseDto<ArrayList<MusicInfo>> hotMusic() throws SQLException {
        //获得数据库中记录的条数
        String sql1 = "select count(musicId) from music";
        ResultSet rs = stat.executeQuery(sql1);
        rs.next();
        int count = rs.getInt("count(musicId)");

        //获得一个随机的起始位置，和往后加11的结束位置
        Random random = new Random();
        int start = random.nextInt(count-11);
        int total = 200;


        //拿到这十一首音乐的信息
        String sql2 = String.format("select * from music limit %s,%s",start,total);
        ResultSet rs2 = stat.executeQuery(sql2);

        ArrayList<MusicInfo> list = new ArrayList<>();
        while(rs2.next()){
            MusicInfo mi = new MusicInfo();
            mi.setMusicId(rs2.getString("musicId"));
            mi.setMusicName(rs2.getString("musicName"));
            mi.setStorePath(rs2.getString("storePath"));
            mi.setSinger(rs2.getString("singer"));

            list.add(mi);
        }


        return new ResponseDto<>(list);
    }


    /**
     * 调用这个接口
     * 返回用户可能感兴趣的部分音乐的信息
     *
     * @return
     */
    @RequestMapping("/recommendMusic")
    public ResponseDto<ArrayList<MusicInfo>> recommendMusic() throws SQLException {
        Statement stat = conn.createStatement();
        ArrayList<MusicInfo> list = new ArrayList<>();

        //拿到推荐依据
        String sql1="select * from recommend ";
        ResultSet rs = stat.executeQuery(sql1);
        rs.next();
        String singer = rs.getString("singer");
        String album = rs.getString("album");
        String musicType = rs.getString("musicType");
        String playListName = rs.getString("playListName");

        String sql2="select * from music where singer = '"+singer+"' limit 6";
        String sql3="select * from music where album = '"+album+"' limit 6";
        String sql4="select * from music where playListName = '"+playListName+"' limit 6";

        list.addAll(ServerUtil.rsToArray(stat.executeQuery(sql2)));
        list.addAll(ServerUtil.rsToArray(stat.executeQuery(sql3)));
        list.addAll(ServerUtil.rsToArray(stat.executeQuery(sql4)));

        return new ResponseDto<>(list);

    }


    /**
     * 用于收集用户习惯
     * 传入歌曲id
     * 在java中创建埋点日志
     * 如果前端需要返回该id对应的音乐信息，我也可以返回
     */
    @RequestMapping("/collectInfo")
    public ResponseDto<String> collectInfo(@RequestParam String songId) throws SQLException, IOException {
        //根据songId查到这首歌的所有信息，并将信息输出到linux中充当用户埋点日志
        String sql = String.format("select musicId,musicName,singer,album,musicType,playListName from music where musicId = '%s'",songId);

        ResultSet rs = stat.executeQuery(sql);
        rs.next();

        //生成日志
        String log = String.format("{\"musicId\":\"%s\",\"musicName\":\"%s\",\"singer\":\"%s\",\"album\":\"%s\",\"musicType\":\"%s\",\"playListName\":\"%s\"}",
                rs.getString("musicId"),rs.getString("musicName"),rs.getString("singer"),rs.getString("album"),
                rs.getString("musicType"),rs.getString("playListName"));

        System.out.println("日志："+log);

        //将日志写入linux服务器
        HttpClientHu httpClient;
        try {
            httpClient = new HttpClientHu("192.168.220.131",8888);
            httpClient.sentLog(log);
        } catch (Exception e) {
            System.out.println("未连接成功");
            return new ResponseDto<>(songId);
        }
        System.out.println("用户日志写入成功");

        return new ResponseDto<>(songId);
    }

}
