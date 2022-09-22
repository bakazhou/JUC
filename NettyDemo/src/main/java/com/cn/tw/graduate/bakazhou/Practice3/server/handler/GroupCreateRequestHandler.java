package com.cn.tw.graduate.bakazhou.Practice3.server.handler;

import com.cn.tw.graduate.bakazhou.Practice3.message.GroupCreateRequestMessage;
import com.cn.tw.graduate.bakazhou.Practice3.message.GroupCreateResponseMessage;
import com.cn.tw.graduate.bakazhou.Practice3.server.session.GroupSessionFactory;
import com.cn.tw.graduate.bakazhou.Practice3.server.session.SessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.Set;
import java.util.stream.Collectors;

@ChannelHandler.Sharable
public class GroupCreateRequestHandler extends SimpleChannelInboundHandler<GroupCreateRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupCreateRequestMessage msg) {

        String groupName = msg.getGroupName();
        Set<String> members = msg.getMembers();
        if (GroupSessionFactory.getGroupSession().groupExist(groupName)){
            ctx.writeAndFlush(new GroupCreateResponseMessage(false,"群组已经存在,请重新输入群名称"));
        }else{
            Set<String> users = SessionFactory.getSession().getUsers();
            //存储当前这个组
            Set<String> groupUsers = members.stream().filter(users::contains).collect(Collectors.toSet());
            GroupSessionFactory.getGroupSession().createGroup(groupName,groupUsers);
            ctx.writeAndFlush(new GroupCreateResponseMessage(true,"群组创建成功,组名为:"+groupName+",目前包含组员:"+GroupSessionFactory.getGroupSession().getMembers(groupName)));
        }
    }
}
