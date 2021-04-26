package com.hu.server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * 负责在linux端开启socket服务
 * 接收web server传来的日志，并将其保存在本地
 */
public class LogServer {
    public static void main(String[] args) throws IOException {
        System.out.println("----服务器启动----");

        //1.创建serversocket并指定端口
        ServerSocket server = new ServerSocket(8888);

        while(true){
            //2.阻塞式等待连接，返回一个socket对象，这个socket对象就是客户端socket的抽象
            Socket client = server.accept();
            System.out.println("一个客户建立了连接");

            //3.获取客户端发来的信息
            BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
            String log = br.readLine();

            //4.将获取的信息存至本地文件系统
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("/home/hadoop/music_log",true)));
            bw.write(log);
            bw.write("\n");

            bw.flush();

            System.out.println("写入日志："+log);

            //4.释放资源
            br.close();
            bw.close();
        }

    }
}
