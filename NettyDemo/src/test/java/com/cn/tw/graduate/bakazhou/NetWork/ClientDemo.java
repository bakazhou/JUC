package com.cn.tw.graduate.bakazhou.NetWork;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class ClientDemo {
    public static void main(String[] args) throws IOException {
        // 创建客户端服务器
        SocketChannel client = SocketChannel.open();

        // 进行连接
        client.connect(new InetSocketAddress("localhost",8080));

        client.write(Charset.defaultCharset().encode("Hello! Im client"));

    }
}
