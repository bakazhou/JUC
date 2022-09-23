package com.cn.tw.graduate.bakazhou.Practice3.client.command;

import com.cn.tw.graduate.bakazhou.Practice3.message.GroupJoinRequestMessage;
import io.netty.channel.ChannelHandlerContext;

public class GjoinCommand implements Command{
    @Override
    public void execute(ChannelHandlerContext ctx, String userName, String[] input) {
        ctx.writeAndFlush(new GroupJoinRequestMessage(userName, input[1]));
    }
}
