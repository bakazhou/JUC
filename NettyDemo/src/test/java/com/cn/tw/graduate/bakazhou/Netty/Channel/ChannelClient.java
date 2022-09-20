package com.cn.tw.graduate.bakazhou.Netty.Channel;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;
import java.util.Scanner;

public class ChannelClient {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        ChannelFuture channelFuture = new Bootstrap()
                .group(nioEventLoopGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        ch.pipeline().addLast(new StringEncoder());
                    }
                })
                //connect是异步非阻塞方法，在main函数中只是发起了调用，真正执行的是另一个nio线程，建立连接往往是需要消耗时间的，而如果不执行sync方法，就可能产生连接还没有建立成功，而主线程直接获取了channel
                //，并进行了消息的发送
                .connect(new InetSocketAddress("localhost", 8080));

        channelFuture.sync();
        Channel channel = channelFuture.channel();

        new Thread(() -> {
            Scanner scanner = new Scanner(System.in);
            while (true){
                String input = scanner.nextLine();
                if (input.equals("quit")){
                    channel.close();
                    break;
                }
                channel.writeAndFlush(input);
            }
        }).start();
        //异步等待直到线程关闭
        ChannelFuture closeFuture = channel.closeFuture();
        closeFuture.sync();

        //线程已经关闭
        System.out.println("thread is closed");
        nioEventLoopGroup.shutdownGracefully();
    }
}