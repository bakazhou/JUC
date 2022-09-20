package com.cn.tw.graduate.bakazhou.Netty.Channel;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.nio.charset.Charset;

public class ChannelServer {
    public static void main(String[] args) {

        // 只负责accept事件
        NioEventLoopGroup boss = new NioEventLoopGroup();

        //负责read,write事件
        NioEventLoopGroup worker = new NioEventLoopGroup(2);

        // 对eventloop进行指责划分 分为boss和worker
        // 此处划分为accept事件和read事件
        new ServerBootstrap().
                group(boss,worker).
                //NioServerSocketChannel只会和NioEventLoopGroup中的一个EventLoop绑定
                channel(NioServerSocketChannel.class).
                childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        // 为handler设置指定的NioEventGroup
                        ch.pipeline().addLast(worker,"handleRead",new ChannelInboundHandlerAdapter() {
                            @Override
                            public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                                System.out.println(worker.next());
                                ByteBuf buffer = (ByteBuf) msg;
                                System.out.println(buffer.toString(Charset.defaultCharset()));
                            }
                        });
                    }
                }).
                bind(8080);
    }
}
