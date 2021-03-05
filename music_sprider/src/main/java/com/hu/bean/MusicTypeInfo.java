package com.hu.bean;

/**
 * 音乐类型相关信息
 */
public class MusicTypeInfo {
    private String href; //音乐类型链接
    private String type; //音乐类型

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    @Override
    public String toString() {
        return "MusicTypeInfo{" +
                "href='" + href + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
