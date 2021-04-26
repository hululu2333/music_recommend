package com.hu.bean;

import scala.Tuple2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * flink data flows中的状态类型
 * 用于记录flink任务中的历史状态
 */
public class FlinkState {
    //按下面三个维度来推荐音乐
    public Map<String,Integer> singers = new HashMap<>(); //用来存各个歌手出现的次数
    public Map<String,Integer> types = new HashMap<>(); //各种音乐类型出现的次数
    public Map<String,Integer> playLists = new HashMap<>(); //各个歌单出现的次数

}
