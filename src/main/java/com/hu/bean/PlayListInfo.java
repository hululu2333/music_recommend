package com.hu.bean;

/**
 * 歌单相关信息
 */
public class PlayListInfo {
    private String playListName; //歌单名
    private String href; //歌单链接
    private String musicType; //音乐类型

    public String getPlayListName() {
        return playListName;
    }

    public void setPlayListName(String playListName) {
        this.playListName = playListName;
    }

    @Override
    public String toString() {
        return "PlayListInfo{" +
                "playListName='" + playListName + '\'' +
                ", href='" + href + '\'' +
                ", musicType='" + musicType + '\'' +
                '}';
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getMusicType() {
        return musicType;
    }

    public void setMusicType(String musicType) {
        this.musicType = musicType;
    }

}
