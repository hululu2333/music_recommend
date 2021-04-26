package com.hu.bean;

/**
 * 将数据源传来的日志转换为这个POJO
 * 这就是用来推荐音乐的依据
 */
public class RecommendBasis {
    public int userId = 1; //目前不支持多用户，所以id都设为1
    public String singer;
    public String musicType;
    public String playListName;

    @Override
    public String toString() {
        return "RecommendBasis{" +
                "userId=" + userId +
                ", singer='" + singer + '\'' +
                ", musicType='" + musicType + '\'' +
                ", playListName='" + playListName + '\'' +
                '}';
    }
}
