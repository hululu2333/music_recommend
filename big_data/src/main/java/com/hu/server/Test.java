package com.hu.server;

import com.alibaba.fastjson.JSON;
import com.hu.bean.FlinkState;
import com.hu.bean.RecommendBasis;
import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.functions.RichFlatMapFunction;
import org.apache.flink.api.common.serialization.SimpleStringSchema;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.connectors.kafka.FlinkKafkaConsumer;
import org.apache.flink.util.Collector;
import scala.Int;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;

public class Test {
    public static void main(String[] args) throws Exception {
        StreamExecutionEnvironment env = StreamExecutionEnvironment.getExecutionEnvironment();

        //配置文件，用来创建kafka消费者
        Properties properties = new Properties();
        properties.setProperty("bootstrap.servers", "192.168.220.130:9092");
        properties.setProperty("group.id", "test");

        //创建kafka消费者
        FlinkKafkaConsumer consumer = new FlinkKafkaConsumer<>("test", new SimpleStringSchema(), properties);
        //JsonDeserializationSchema
//        consumer.setStartFromEarliest();     // 尽可能从最早的记录开始

        //添加源，获得一个dataStream
        DataStream<String> stream = env.addSource(consumer);

        stream.map(new LogToBasisMapFunction()) //将传入的日志映射为RecommendBasis类型
                .keyBy((KeySelector<RecommendBasis, Integer>) value -> value.userId) //以userId为key进行分区
                .flatMap(new RecomResultFlatMapFunction()).print();


        env.execute();

    }


    /**
     * 将数据源的日志，转化为RecommendBasis类型
     */
    public static class LogToBasisMapFunction implements MapFunction<String, RecommendBasis>{

        @Override
        public RecommendBasis map(String value) throws Exception {
            RecommendBasis rb = JSON.parseObject(value, RecommendBasis.class);
            return rb;
        }
    }

    /**
     * 实现了状态操作的flatMap
     */
    public static class RecomResultFlatMapFunction extends RichFlatMapFunction<RecommendBasis,RecommendBasis>{
        ValueState<FlinkState> state; //储存在keyedStream中的状态

        //在调用flatMap之前调用，获得keyedStream中的状态
        @Override
        public void open(Configuration parameters) throws Exception {
            ValueStateDescriptor<FlinkState> desc = new ValueStateDescriptor<>("state", FlinkState.class);
            state = getRuntimeContext().getState(desc);
        }

        @Override
        public void flatMap(RecommendBasis value, Collector<RecommendBasis> out) throws Exception {
            //如果之前没有key为1的事件，则state为空
            if(state.value() == null){
                out.collect(value);//输出分析结果

                FlinkState fk = new FlinkState();
                fk.singers.put(value.singer,1);
                fk.playLists.put(value.playListName,1);
                fk.types.put(value.musicType,1);

                state.update(fk);
            }else {
                //判断状态里是不是已经有key相同的键值对，有就将value加1。没有就新建一个
                if(state.value().types.containsKey(value.musicType)){
                    Integer olderValue = state.value().types.get(value.musicType);
                    state.value().types.replace(value.musicType,olderValue,olderValue+1);
                }else{
                    state.value().types.put(value.musicType,1);
                }

                if(state.value().singers.containsKey(value.singer)){
                    Integer olderValue = state.value().singers.get(value.singer);
                    state.value().singers.replace(value.singer,olderValue,olderValue+1);
                }else{
                    state.value().singers.put(value.singer,1);
                }

                if(state.value().playLists.containsKey(value.playListName)){
                    Integer olderValue = state.value().playLists.get(value.playListName);
                    state.value().playLists.replace(value.playListName,olderValue,olderValue+1);
                }else{
                    state.value().singers.put(value.singer,1);
                }

            }

            //状态更新完成，现在从状态分析出结果，并输出
            RecommendBasis rb = new RecommendBasis();

            Collection<Integer> values = state.value().singers.values();
            values.forEach(i -> System.out.println(i));
            System.out.println("-------------");
//            Arrays.sort(obj);
//            System.out.println(obj[0]);
//            System.out.println(obj[obj.length-1]);

        }
    }

}
