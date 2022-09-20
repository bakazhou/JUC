package com.cn.tw.graduate.bakazhou.Netty.Practice;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) throws InterruptedException {
        NioEventLoopGroup worker = new NioEventLoopGroup();
        ChannelFuture channelFuture = new Bootstrap().
                group(worker).
                channel(NioSocketChannel.class).
                handler(new ChannelInitializer<>() {
                    @Override
                    protected void initChannel(Channel ch) throws Exception {
                        //发送消息的处理器，对信息编码
                        ch.pipeline().addLast(new StringEncoder());

                        //处理服务端返回的数据
                        ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf buf = (ByteBuf) msg;
                                System.out.println(buf.toString(Charset.defaultCharset()));
                                super.channelRead(ctx, msg);
                            }
                        });
                    }
                }).connect(new InetSocketAddress("localhost", 8080));


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
        worker.shutdownGracefully();
    }
}
