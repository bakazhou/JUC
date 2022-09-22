package com.cn.tw.graduate.bakazhou.Practice3.client;


import com.cn.tw.graduate.bakazhou.Practice3.message.LoginRequestMessage;
import com.cn.tw.graduate.bakazhou.Practice3.message.LoginResponseMessage;
import com.cn.tw.graduate.bakazhou.Practice3.protocol.MessageCodec;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Scanner;

@Slf4j
public class ChatClient {
    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodec MESSAGE_CODEC = new MessageCodec();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(group);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) {
                    ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 24, 4, 4, 0));
                    ch.pipeline().addLast(LOGGING_HANDLER);
                    ch.pipeline().addLast(MESSAGE_CODEC);
                    ch.pipeline().addLast("client handler",new ChannelInboundHandlerAdapter(){

                        // 获取登录操作的返回信息
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) {
                            LoginResponseMessage message = (LoginResponseMessage) msg;

                        }

                        @Override
                        public void channelActive(ChannelHandlerContext ctx) {
                            //连接建立后触发该事件,接收用户在控制台输入的账号密码
                            new Thread(() -> {
                                Scanner scanner = new Scanner(System.in);
                                System.out.println("请输入用户名:");
                                String userName = scanner.nextLine();
                                System.out.println("请输入用户密码:");
                                String password = scanner.nextLine();

                                LoginRequestMessage login = new LoginRequestMessage(userName, password);

                                //此处是ChannelInboundHandlerAdapter入栈处理器，所以处理完之后就会进行出栈，向上走到MessageCodec和LoginHandler
                                ctx.writeAndFlush(login);

                            },"login").start();
                        }
                    });
                }

            });
            Channel channel = bootstrap.connect("localhost", 8080).sync().channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            System.out.println("client error");
        } finally {
            group.shutdownGracefully();
        }
    }
}
