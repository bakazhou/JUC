package com.cn.tw.graduate.bakazhou.Practice3.client;


import com.cn.tw.graduate.bakazhou.Practice3.message.*;
import com.cn.tw.graduate.bakazhou.Practice3.protocol.MessageCodec;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class ChatClient {
    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodec MESSAGE_CODEC = new MessageCodec();

        //用于线程间通信，在登录完成后，通知用户线程登录完成，计数器减到0的时候就继续向下运行
        CountDownLatch WAIT_FOR_LOGIN = new CountDownLatch(1);

        //是否登录成功的标识位
        AtomicBoolean LOGIN_SUCCESS = new AtomicBoolean(false);
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
                            //如果msg是登录的返回响应消息
                            if (msg instanceof LoginResponseMessage){
                                LoginResponseMessage loginResponseMessage = (LoginResponseMessage) msg;
                                LOGIN_SUCCESS.set(loginResponseMessage.isSuccess());
                                WAIT_FOR_LOGIN.countDown();
                            }

                            //如果消息是别人发送的消息
                            if (msg instanceof ChatResponseMessage){
                                ChatResponseMessage chatResponseMessage = (ChatResponseMessage) msg;
                                if (chatResponseMessage.getContent() != null){
                                    System.out.println(chatResponseMessage.getFrom()+" say: "+chatResponseMessage.getContent());
                                }else {
                                    System.out.println(chatResponseMessage.getReason());
                                }
                            }

                            //接收群组创建消息
                            if (msg instanceof GroupCreateResponseMessage){
                                GroupCreateResponseMessage groupCreateResponseMessage = (GroupCreateResponseMessage) msg;
                                System.out.println(groupCreateResponseMessage.getReason());
                            }

                            //接收群组创建成员消息
                            if (msg instanceof GroupMembersResponseMessage){
                                GroupMembersResponseMessage groupMembersResponseMessage = (GroupMembersResponseMessage) msg;
                                System.out.println("该群组包含成员:"+groupMembersResponseMessage.getMembers());
                            }

                            //接收加入群组的消息
                            if (msg instanceof GroupJoinResponseMessage){
                                GroupJoinResponseMessage joinResponseMessage = (GroupJoinResponseMessage) msg;
                                System.out.println(joinResponseMessage.getReason());
                            }

                            //处理群发消息
                            if (msg instanceof GroupChatResponseMessage){
                                GroupChatResponseMessage groupChatResponseMessage = (GroupChatResponseMessage) msg;
                                if (groupChatResponseMessage.getContent() == null){
                                    System.out.println(groupChatResponseMessage.getReason());
                                }else {
                                    System.out.println(groupChatResponseMessage.getFrom()+" say: "+groupChatResponseMessage.getContent());
                                }
                            }
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

                                //阻塞等待登录返回的消息
                                try {
                                    WAIT_FOR_LOGIN.await();
                                } catch (InterruptedException e) {
                                    throw new RuntimeException(e);
                                }
                                //登录失败
                                if (!LOGIN_SUCCESS.get()){
                                    System.out.println("登录失败，请重启程序");
                                    ctx.close();
                                    return;
                                }
                                while (true){
                                    System.out.println("==================================");
                                    System.out.println("send [username] [content]");
                                    System.out.println("gsend [group name] [content]");
                                    System.out.println("gcreate [group name] [m1,m2,m3...]");
                                    System.out.println("gmembers [group name]");
                                    System.out.println("gjoin [group name]");
                                    System.out.println("gquit [group name]");
                                    System.out.println("quit");
                                    System.out.println("==================================");
                                    System.out.println("请输入操作指令:");
                                    Scanner operation = new Scanner(System.in);
                                    String[] input = operation.nextLine().split(" ");
                                    switch (input[0]){
                                        case "send":
                                            sendChatMessage(ctx, userName, input);
                                            break;
                                        case "gsend":
                                            sendGroupMessage(ctx, userName, input);
                                            break;
                                        case "gcreate":
                                            createGroup(ctx, userName, input);
                                            break;
                                        case "gmembers":
                                            getGroupMembers(ctx, input);
                                            break;
                                        case "gjoin":
                                            joinGroup(ctx, userName, input);
                                            break;
                                        case "gquit":
                                            quitGroup(ctx, userName, input);
                                            break;
                                        case "quit":
                                            quit(ctx);
                                            return;
                                    }
                                }

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

    private static void quit(ChannelHandlerContext ctx) {
        ctx.channel().close();
    }

    private static void quitGroup(ChannelHandlerContext ctx, String userName, String[] input) {
        ctx.writeAndFlush(new GroupQuitRequestMessage(userName, input[1]));
    }

    private static void joinGroup(ChannelHandlerContext ctx, String userName, String[] input) {
        ctx.writeAndFlush(new GroupJoinRequestMessage(userName, input[1]));
    }

    private static void getGroupMembers(ChannelHandlerContext ctx, String[] input) {
        ctx.writeAndFlush(new GroupMembersRequestMessage(input[1]));
    }

    private static void createGroup(ChannelHandlerContext ctx, String userName, String[] input) {
        Set<String> set = new HashSet<>(Arrays.asList(input[2].split(",")));
        set.add(userName);
        ctx.writeAndFlush(new GroupCreateRequestMessage(input[1], set));
    }

    private static void sendGroupMessage(ChannelHandlerContext ctx, String userName, String[] input) {
        ctx.writeAndFlush(new GroupChatRequestMessage(userName, input[1], input[2]));
    }

    private static void sendChatMessage(ChannelHandlerContext ctx, String userName, String[] input) {
        ctx.writeAndFlush(new ChatRequestMessage(userName, input[1], input[2]));
    }
}
