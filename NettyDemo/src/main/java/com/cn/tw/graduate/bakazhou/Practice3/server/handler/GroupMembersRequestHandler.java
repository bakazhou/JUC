package com.cn.tw.graduate.bakazhou.Practice3.server.handler;

import com.cn.tw.graduate.bakazhou.Practice3.message.GroupMembersRequestMessage;
import com.cn.tw.graduate.bakazhou.Practice3.message.GroupMembersResponseMessage;
import com.cn.tw.graduate.bakazhou.Practice3.server.session.GroupSession;
import com.cn.tw.graduate.bakazhou.Practice3.server.session.GroupSessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Collections;

@ChannelHandler.Sharable
public class GroupMembersRequestHandler extends SimpleChannelInboundHandler<GroupMembersRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMembersRequestMessage msg) throws Exception {
        //群组存在
        GroupSession groupSession = GroupSessionFactory.getGroupSession();
        String groupName = msg.getGroupName();
        if (groupSession.groupExist(groupName)) {
            ctx.writeAndFlush(new GroupMembersResponseMessage(groupSession.getMembers(groupName)));
        }else {
            ctx.writeAndFlush(new GroupMembersResponseMessage(Collections.emptySet()));
        }
    }
}
