package com.hu.server.api;

import com.hu.server.model.dto.ResponseDto;
import com.hu.server.model.entity.MusicInfo;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;

@RestController
@Log4j2
public class MusicHandler {

    /**
     * hello world
     */
//    @RequestMapping("/confirm")
//    public ResponseDto<ArrayList<MusicInfo>> confirm(){
//
//        ArrayList<MusicInfo> list = new ArrayList<>();
//        list.add(new MusicInfo());
//        list.add(new MusicInfo());
//        list.add(new MusicInfo());
//
//        return new ResponseDto<>(list);
//    }


    /**
     * 调用这个接口
     * 随机返回11首音乐的信息
     * @return
     */
    @RequestMapping("/hotMusic")
    public ResponseDto<ArrayList<MusicInfo>> hotMusic(){

        ArrayList<MusicInfo> list = new ArrayList<>();
        MusicInfo mi = new MusicInfo();
        mi.setMusicId("1911");
        mi.setMusicName("hotmusic");
        mi.setStorePath("/musicServer/等人.m4a");
        mi.setSinger("hot");

        for (int i=0;i<11;i++){
            list.add(mi);
        }


        return new ResponseDto<>(list);
    }


    /**
     * 调用这个接口
     * 返回用户可能感兴趣的十一首歌的信息
     * @return
     */
    @RequestMapping("/recommendMusic")
    public ResponseDto<ArrayList<MusicInfo>> recommendMusic(){

        ArrayList<MusicInfo> list = new ArrayList<>();
        MusicInfo mi = new MusicInfo();
        mi.setMusicId("1912");
        mi.setMusicName("recommendmusic");
        mi.setStorePath("/musicServer/等人.m4a");
        mi.setSinger("recommend");

        for (int i=0;i<11;i++){
            list.add(mi);
        }


        return new ResponseDto<>(list);

    }


    /**
     * 用于收集用户习惯
     * 传入歌曲id
     * 在java中创建埋点日志
     * 如果前端需要返回该id对应的音乐信息，我也可以返回
     */
    @RequestMapping("/collectInfo")
    public ResponseDto<String> collectInfo(@RequestParam String songId){


        return new ResponseDto<>(songId);
    }

}
