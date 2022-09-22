package com.cn.tw.graduate.bakazhou.Practice3.server.handler;

import com.cn.tw.graduate.bakazhou.Practice3.message.GroupCreateRequestMessage;
import com.cn.tw.graduate.bakazhou.Practice3.message.GroupCreateResponseMessage;
import com.cn.tw.graduate.bakazhou.Practice3.server.session.Group;
import com.cn.tw.graduate.bakazhou.Practice3.server.session.GroupSessionFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class GroupCreateRequestHandle extends SimpleChannelInboundHandler<GroupCreateRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupCreateRequestMessage msg) throws Exception {

        String groupName = msg.getGroupName();
        if (GroupSessionFactory.getGroupSession().groupExist(groupName)){
            ctx.writeAndFlush(new GroupCreateResponseMessage(false,"群组已经存在,请重新输入群名称"));
        }else{
            //存储当前这个组
            Group group = GroupSessionFactory.getGroupSession().createGroup(groupName, msg.getMembers());
            ctx.writeAndFlush(new GroupCreateResponseMessage(true,"群组创建成功,组名为:"+group.getName()+"目前包含组员:"+group.getMembers()));
        }
    }
}
