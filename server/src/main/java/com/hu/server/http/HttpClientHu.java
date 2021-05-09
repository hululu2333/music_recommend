package com.hu.server.http;

import com.hu.server.exception.SocketNotConnectedcException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * http客户端，对socket进行封装
 * 用于向linux服务器发送消息
 */
public class HttpClientHu {
    private Socket client;

    public HttpClientHu() throws IOException {
        this("localhost",8888);
    }


    public HttpClientHu(String host, int port) throws IOException {
        client = new Socket(host,port);
    }


    //关闭client
    public void close() throws IOException {
        client.close();
    }


    public void sentLog(String log) throws IOException, SocketNotConnectedcException {
        if(!client.isConnected()){
            throw new SocketNotConnectedcException();
        }

        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(client.getOutputStream()));
        bw.write(log);
        bw.flush();

        bw.close();
    }

}
