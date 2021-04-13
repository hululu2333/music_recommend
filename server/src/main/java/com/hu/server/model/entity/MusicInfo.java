package com.hu.server.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MusicInfo {
    private String musicName; //音乐标题
    private String musicId; //音乐id，就是外链中的songid
    private String musicDuration; //音乐时长
    private String musicHref; //音乐链接（这个链接中包含音乐的歌词与评论）
    private String singer; //歌手
    private String album; //专辑
    private String albumHref; //专辑链接

    private String musicType; //音乐类型
    private String playListName; //歌单名

    private String storePath; //音乐存储的路径
}
