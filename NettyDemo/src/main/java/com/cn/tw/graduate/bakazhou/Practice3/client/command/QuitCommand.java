package com.cn.tw.graduate.bakazhou.Practice3.client.command;

import io.netty.channel.ChannelHandlerContext;

public class QuitCommand implements Command{
    @Override
    public void execute(ChannelHandlerContext ctx, String userName, String[] input) {
        ctx.close();
    }
}
