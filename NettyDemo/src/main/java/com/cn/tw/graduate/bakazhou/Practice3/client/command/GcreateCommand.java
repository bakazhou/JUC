package com.cn.tw.graduate.bakazhou.Practice3.client.command;

import com.cn.tw.graduate.bakazhou.Practice3.message.GroupCreateRequestMessage;
import io.netty.channel.ChannelHandlerContext;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class GcreateCommand implements Command{
    @Override
    public void execute(ChannelHandlerContext ctx, String userName, String[] input) {
        Set<String> set = new HashSet<>(Arrays.asList(input[2].split(",")));
        set.add(userName);
        ctx.writeAndFlush(new GroupCreateRequestMessage(input[1], set));
    }

}
