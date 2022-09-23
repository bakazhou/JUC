package com.cn.tw.graduate.bakazhou.Practice3.client.command;

import com.cn.tw.graduate.bakazhou.Practice3.message.GroupQuitRequestMessage;
import io.netty.channel.ChannelHandlerContext;

public class GquitCommand implements Command{
    @Override
    public void execute(ChannelHandlerContext ctx, String userName, String[] input) {
        ctx.writeAndFlush(new GroupQuitRequestMessage(userName, input[1]));
    }
}
