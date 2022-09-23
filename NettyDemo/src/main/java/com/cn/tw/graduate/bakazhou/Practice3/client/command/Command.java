package com.cn.tw.graduate.bakazhou.Practice3.client.command;

import io.netty.channel.ChannelHandlerContext;

public interface Command {
    void execute(ChannelHandlerContext ctx, String userName, String[] input);
}
