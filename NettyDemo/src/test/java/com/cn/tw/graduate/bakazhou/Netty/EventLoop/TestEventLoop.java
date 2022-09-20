package com.cn.tw.graduate.bakazhou.Netty.EventLoop;

import io.netty.channel.DefaultEventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TestEventLoop {
    public static void main(String[] args) {
        //1 创建事件循环组

        // io事件，普通任务，定时任务
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(2);
        //普通任务，定时任务
        DefaultEventLoopGroup defaultEventLoopGroup = new DefaultEventLoopGroup();

        //2 获取事件循环对象
        System.out.println(nioEventLoopGroup.next());
        System.out.println(nioEventLoopGroup.next());
        System.out.println(nioEventLoopGroup.next());

        //3 执行普通任务
        nioEventLoopGroup.next().execute(new Runnable() {
            @Override
            public void run() {
                System.out.println("ok");
            }
        });

        //4 定时任务
        nioEventLoopGroup.next().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                System.out.println(new Date());
            }
        },0,2, TimeUnit.SECONDS);

        //5 关闭
        nioEventLoopGroup.shutdownGracefully();
    }
}
