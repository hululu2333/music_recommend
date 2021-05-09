package com.hu.sink;

import com.hu.bean.RecommendBasis;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.sink.RichSinkFunction;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

/**
 * 自定义一个mysqlSink，将数据写入到mysql
 */
public class MysqlSinkFunction extends RichSinkFunction<RecommendBasis> {
    Connection connection;
    PreparedStatement pstmt;
    Statement stmt2 ;

    //获取数据库连接信息
    private Connection getConnection(){
        Connection conn = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");//将mysql驱动注册到DriverManager中去

            DriverManager.getConnection("jdbc:mysql://localhost:3306/music_recommend?useUnicode=true&characterEncoding=utf-8",
                    "root","1008");//数据库连接信息

        }catch (Exception e){
            e.printStackTrace();
        }
        return  conn;
    }


    @Override
    public void open(Configuration parameters) throws Exception {
        super.open(parameters);

        connection = getConnection();
        String sql = "insert into student(id,musicType,playListName,singer) value(?,?,?,?)";
        pstmt = connection.prepareStatement(sql);

        stmt2=connection.createStatement();
    }

    @Override
    public void invoke(RecommendBasis value, Context context) throws Exception {
        //删除原有的数据
        String sql2="delete from recommend where id = 1";
        stmt2.execute(sql2);

        //为前面的占位符赋值
        pstmt.setInt(1,value.userId);
        pstmt.setString(2,value.musicType);
        pstmt.setString(3,value.playListName);
        pstmt.setString(4,value.singer);

        pstmt.executeUpdate();

    }

    /**
     * 在close方法中要释放资源
     * @throws Exception
     */
    @Override
    public void close() throws Exception {
        super.close();

        if(pstmt != null){
            pstmt.close();
        }

        if(stmt2 != null){
            stmt2.close();
        }

        if (connection != null){
            connection.close();
        }
    }

}
