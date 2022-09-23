package com.cn.tw.graduate.bakazhou.Practice3.server.handler;

import com.cn.tw.graduate.bakazhou.Practice3.message.GroupQuitRequestMessage;
import com.cn.tw.graduate.bakazhou.Practice3.message.GroupQuitResponseMessage;
import com.cn.tw.graduate.bakazhou.Practice3.server.session.GroupSession;
import com.cn.tw.graduate.bakazhou.Practice3.server.session.GroupSessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class GroupQuitRequestMessageHandler extends SimpleChannelInboundHandler<GroupQuitRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupQuitRequestMessage msg) {

        String username = msg.getUsername();
        String groupName = msg.getGroupName();

        GroupSession groupSession = GroupSessionFactory.getGroupSession();

        if (!groupSession.getMembers(groupName).contains(username)) {
            ctx.writeAndFlush(new GroupQuitResponseMessage(false,"您不在该群聊中"));
            return;
        }
        groupSession.removeMember(groupName,username);
        if (groupSession.getMembers(groupName).isEmpty()) {
            groupSession.removeGroup(groupName);
        }
        ctx.writeAndFlush(new GroupQuitResponseMessage(true,"您已退出群聊"));
    }
}
