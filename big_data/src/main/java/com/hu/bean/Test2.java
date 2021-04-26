package com.hu.bean;

import com.alibaba.fastjson.JSON;

public class Test2 {
    public static void main(String[] args) {
        RecommendBasis rb;

        String log = "{\"musicId\":\"531786614\",\"musicName\":\"亲爱的\",\"singer\":\"旅行团\",\"album\":\"感+\",\"musicType\":\"摇滚\",\"playListName\":\"众里寻你?我的七十亿分之一\"}";

        rb =JSON.parseObject(log, RecommendBasis.class);

        System.out.println(rb);
    }
}
