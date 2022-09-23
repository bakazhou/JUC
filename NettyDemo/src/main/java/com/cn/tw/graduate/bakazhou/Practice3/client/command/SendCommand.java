package com.cn.tw.graduate.bakazhou.Practice3.client.command;

import com.cn.tw.graduate.bakazhou.Practice3.message.ChatRequestMessage;
import io.netty.channel.ChannelHandlerContext;

public class SendCommand implements Command{

    @Override
    public void execute(ChannelHandlerContext ctx, String userName, String[] input) {
        ctx.writeAndFlush(new ChatRequestMessage(userName, input[1], input[2]));
    }
}
