package com.cn.tw.graduate.bakazhou.Practice3.server.handler;

import com.cn.tw.graduate.bakazhou.Practice3.message.GroupJoinRequestMessage;
import com.cn.tw.graduate.bakazhou.Practice3.message.GroupJoinResponseMessage;
import com.cn.tw.graduate.bakazhou.Practice3.server.session.GroupSession;
import com.cn.tw.graduate.bakazhou.Practice3.server.session.GroupSessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class GroupJoinRequestMessageHandler extends SimpleChannelInboundHandler<GroupJoinRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupJoinRequestMessage msg) throws Exception {
        GroupSession groupSession = GroupSessionFactory.getGroupSession();
        String groupName = msg.getGroupName();
        if (groupSession.groupExist(groupName)){
            String username = msg.getUsername();
            groupSession.joinMember(groupName, username);
            ctx.writeAndFlush(new GroupJoinResponseMessage(true,"加入群组成功，目前群组成员有:"+groupSession.getMembers(groupName)));
        }else {
            ctx.writeAndFlush(new GroupJoinResponseMessage(false,"群组不存在，无法加入"));
        }
    }
}
