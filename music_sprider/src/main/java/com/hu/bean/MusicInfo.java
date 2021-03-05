package com.hu.bean;

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

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getMusicId() {
        return musicId;
    }

    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }

    public String getMusicDuration() {
        return musicDuration;
    }

    public void setMusicDuration(String musicDuration) {
        this.musicDuration = musicDuration;
    }

    public String getMusicHref() {
        return musicHref;
    }

    public void setMusicHref(String musicHref) {
        this.musicHref = musicHref;
    }

    public String getSinger() {
        return singer;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbumHref() {
        return albumHref;
    }

    public void setAlbumHref(String albumHref) {
        this.albumHref = albumHref;
    }

    public String getMusicType() {
        return musicType;
    }

    public void setMusicType(String musicType) {
        this.musicType = musicType;
    }

    public String getPlayListName() {
        return playListName;
    }

    @Override
    public String toString() {
        return "MusicInfo{" +
                "musicName='" + musicName + '\'' +
                ", musicId='" + musicId + '\'' +
                ", musicDuration='" + musicDuration + '\'' +
                ", musicHref='" + musicHref + '\'' +
                ", singer='" + singer + '\'' +
                ", album='" + album + '\'' +
                ", albumHref='" + albumHref + '\'' +
                ", musicType='" + musicType + '\'' +
                ", playListName='" + playListName + '\'' +
                '}';
    }

    public void setPlayListName(String playListName) {
        this.playListName = playListName;
    }
}
