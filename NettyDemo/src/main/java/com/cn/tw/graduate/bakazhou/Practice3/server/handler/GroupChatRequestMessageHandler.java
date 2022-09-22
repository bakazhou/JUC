package com.cn.tw.graduate.bakazhou.Practice3.server.handler;

import com.cn.tw.graduate.bakazhou.Practice3.message.GroupChatRequestMessage;
import com.cn.tw.graduate.bakazhou.Practice3.message.GroupChatResponseMessage;
import com.cn.tw.graduate.bakazhou.Practice3.server.session.GroupSession;
import com.cn.tw.graduate.bakazhou.Practice3.server.session.GroupSessionFactory;
import com.cn.tw.graduate.bakazhou.Practice3.server.session.Session;
import com.cn.tw.graduate.bakazhou.Practice3.server.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Set;
import java.util.stream.Collectors;

@ChannelHandler.Sharable
public class GroupChatRequestMessageHandler extends SimpleChannelInboundHandler<GroupChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupChatRequestMessage msg) {
        GroupSession groupSession = GroupSessionFactory.getGroupSession();
        String groupName = msg.getGroupName();
        if (!groupSession.groupExist(groupName)){
            ctx.writeAndFlush(new GroupChatResponseMessage(false,"目标群组不存在"));
            return;
        }
        String fromUser = msg.getFrom();
        if (!groupSession.getMembers(groupName).contains(fromUser)){
            ctx.writeAndFlush(new GroupChatResponseMessage(false,"您不是群组成员，请加入群组后再发送消息"));
            return;
        }
        Set<String> toMembers = groupSession.getMembers(groupName).stream().filter(member -> !fromUser.equals(member)).collect(Collectors.toSet());
        Session session = SessionFactory.getSession();
        for (String user : toMembers) {
            Channel channel = session.getChannel(user);
            channel.writeAndFlush(new GroupChatResponseMessage(fromUser,msg.getContent()));
        }

    }
}
