package com.cn.tw.graduate.bakazhou.Netty.Practice;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.Charset;

//实现一个双向通信，客户端发送ping，服务端回复pong
public class Server {
    public static void main(String[] args) {
        // acceptWorker用于处理accept事件
        NioEventLoopGroup acceptWorker = new NioEventLoopGroup();
        // readWriteWorker用于处理read和write事件
        NioEventLoopGroup readWriteWorker = new NioEventLoopGroup();
        ChannelFuture channelFuture = new ServerBootstrap().
                //设置为NioEventLoopGroup
                group(acceptWorker, readWriteWorker).
                channel(NioServerSocketChannel.class).
                childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel socketChannel) throws Exception {
                        //readHandler 处理来自客户端的信息
                        socketChannel.pipeline().addLast(acceptWorker, "readHandler", new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                ByteBuf byteBuf = (ByteBuf) msg;
                                String message = byteBuf.toString(Charset.defaultCharset());
                                System.out.println(message);
                                //如果客户端信息为ping
                                if ("ping".equals(message)){
                                    //触发writeHandler
                                    socketChannel.writeAndFlush(ctx.alloc().buffer().writeBytes("pong".getBytes()));
                                }
                            }
                        });

                        //writeHandler 向客户端返回信息
                        socketChannel.pipeline().addLast(readWriteWorker, "writeHandler", new ChannelOutboundHandlerAdapter() {
                            @Override
                            public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
                                ByteBuf byteBuf = (ByteBuf) msg;
                                super.write(ctx, msg, promise);
                            }
                        });
                    }
                }).
                bind(8080);
    }
}
