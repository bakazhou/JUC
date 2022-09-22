package com.cn.tw.graduate.bakazhou.Practice3.server;

import com.cn.tw.graduate.bakazhou.Practice3.message.LoginRequestMessage;
import com.cn.tw.graduate.bakazhou.Practice3.message.LoginResponseMessage;
import com.cn.tw.graduate.bakazhou.Practice3.protocol.MessageCodec;
import com.cn.tw.graduate.bakazhou.Practice3.server.service.UserServiceFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.SimpleChannelInboundHandler;
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
        LoggingHandler LOGIN_HANDLER = new LoggingHandler(LogLevel.INFO);
        MessageCodec MESSAGE_CODEC = new MessageCodec();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.group(boss, worker);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 24, 4, 4, 0));
                    ch.pipeline().addLast(LOGIN_HANDLER);
                    ch.pipeline().addLast(MESSAGE_CODEC);


                    //只处理LoginRequestMessage
                    ch.pipeline().addLast(new SimpleChannelInboundHandler<LoginRequestMessage>() {
                        @Override
                        protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) {
                            boolean login = UserServiceFactory.getUserService().login(msg.getUsername(), msg.getPassword());
                            LoginResponseMessage loginResponseMessage;
                            if (login){
                                loginResponseMessage = new LoginResponseMessage(true,"登录成功");
                            }else {
                                loginResponseMessage = new LoginResponseMessage(false,"用户名或密码错误");
                            }
                            ctx.writeAndFlush(loginResponseMessage);
                        }
                    });
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
