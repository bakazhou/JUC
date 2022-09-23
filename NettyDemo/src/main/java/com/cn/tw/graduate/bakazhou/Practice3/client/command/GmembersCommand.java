package com.cn.tw.graduate.bakazhou.Practice3.client.command;

import com.cn.tw.graduate.bakazhou.Practice3.message.GroupMembersRequestMessage;
import io.netty.channel.ChannelHandlerContext;

public class GmembersCommand implements Command{
    @Override
    public void execute(ChannelHandlerContext ctx, String groupName, String[] input) {
        ctx.writeAndFlush(new GroupMembersRequestMessage(input[1]));
    }
}
