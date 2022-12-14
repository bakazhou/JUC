package com.cn.tw.graduate.bakazhou.Practice3.server;

import com.cn.tw.graduate.bakazhou.Practice3.server.handler.*;
import com.cn.tw.graduate.bakazhou.Practice3.protocol.MessageCodec;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class ChatServer {
    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        LoggingHandler LOG = new LoggingHandler(LogLevel.INFO);
        ChatRequestMessageHandler CHAT_REQUEST_HANDLER = new ChatRequestMessageHandler();
        LoginRequestMessageSimpleChannelInboundHandler LOGIN_REQUEST_HANDLER = new LoginRequestMessageSimpleChannelInboundHandler();
        GroupCreateRequestHandler GROUP_CREATE_REQUEST_HANDLER = new GroupCreateRequestHandler();
        GroupMembersRequestHandler GROUP_MEMBERS_REQUEST_HANDLER = new GroupMembersRequestHandler();
        GroupJoinRequestMessageHandler GROUP_JOIN_REQUEST_HANDLER = new GroupJoinRequestMessageHandler();
        GroupChatRequestMessageHandler GROUP_CHAT_REQUEST_HANDLER = new GroupChatRequestMessageHandler();
        GroupQuitRequestMessageHandler GROUP_QUIT_REQUEST_HANDLER = new GroupQuitRequestMessageHandler();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.group(boss, worker);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 24, 4, 4, 0));
                    ch.pipeline().addLast(LOG);
                    ch.pipeline().addLast(new MessageCodec());
                    //??????LoginRequestMessage
                    ch.pipeline().addLast(LOGIN_REQUEST_HANDLER);
                    //??????ChatRequestMessage
                    ch.pipeline().addLast(CHAT_REQUEST_HANDLER);
                    //??????GroupCreateMessage
                    ch.pipeline().addLast(GROUP_CREATE_REQUEST_HANDLER);
                    //??????GroupMembersMessage
                    ch.pipeline().addLast(GROUP_MEMBERS_REQUEST_HANDLER);
                    //??????GroupJoinRequestMessage
                    ch.pipeline().addLast(GROUP_JOIN_REQUEST_HANDLER);
                    //??????GroupChatRequestMessage
                    ch.pipeline().addLast(GROUP_CHAT_REQUEST_HANDLER);
                    //??????GroupQuitRequestMessage
                    ch.pipeline().addLast(GROUP_QUIT_REQUEST_HANDLER);

                }
            });
            Channel channel = serverBootstrap.bind(8080).sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            System.out.println("server error");
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

}
