package com.cn.tw.graduate.bakazhou.Practice3.client.command;

import com.cn.tw.graduate.bakazhou.Practice3.message.GroupChatRequestMessage;
import io.netty.channel.ChannelHandlerContext;

public class GsendCommand implements Command{
    @Override
    public void execute(ChannelHandlerContext ctx, String userName, String[] input) {
        ctx.writeAndFlush(new GroupChatRequestMessage(userName, input[1], input[2]));
    }
}
