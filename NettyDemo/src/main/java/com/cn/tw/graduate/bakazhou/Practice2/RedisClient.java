package com.cn.tw.graduate.bakazhou.Practice2;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class RedisClient {
    /*
        *3
        $3
        set
        $4
        name
        $11
        TianLe Zhou
     */
    public static void main(String[] args) {
        final byte[] FORMAT = "\r\n".getBytes();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(worker);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(worker,new LoggingHandler(LogLevel.INFO));
                    ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            ByteBuf buffer = ctx.alloc().buffer();
                            buffer.writeBytes("*3".getBytes());
                            buffer.writeBytes(FORMAT);

                            buffer.writeBytes("$3".getBytes());
                            buffer.writeBytes(FORMAT);
                            buffer.writeBytes("set".getBytes());
                            buffer.writeBytes(FORMAT);

                            buffer.writeBytes("$4".getBytes());
                            buffer.writeBytes(FORMAT);
                            buffer.writeBytes("name".getBytes());
                            buffer.writeBytes(FORMAT);

                            buffer.writeBytes("$11".getBytes());
                            buffer.writeBytes(FORMAT);
                            buffer.writeBytes("TianLe Zhou".getBytes());
                            buffer.writeBytes(FORMAT);

                            // 发送命令给Redis执行
                            ctx.channel().writeAndFlush(buffer);
                        }

                        //获取Redis返回的结果
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            ByteBuf buf = (ByteBuf) msg;
                            System.out.println(buf.toString(Charset.defaultCharset()));
                        }
                    });
                }
            });
            // 连接到redis
            ChannelFuture channelFuture = bootstrap.connect(new InetSocketAddress("localhost", 6379)).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            System.out.println("client error");
        } finally {
            worker.shutdownGracefully();
        }
    }
}
