package com.cn.tw.graduate.bakazhou.Practice3.server.handler;

import com.cn.tw.graduate.bakazhou.Practice3.message.ChatRequestMessage;
import com.cn.tw.graduate.bakazhou.Practice3.message.ChatResponseMessage;
import com.cn.tw.graduate.bakazhou.Practice3.server.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

@ChannelHandler.Sharable
public class ChatRequestMessageHandler extends SimpleChannelInboundHandler<ChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatRequestMessage msg) throws Exception {
        String fromUser = msg.getFrom();
        String toUser = msg.getTo();
        String content = msg.getContent();

        Channel channel = SessionFactory.getSession().getChannel(toUser);

        System.out.println("channel is "+channel);
        //说明对方在线
        if (channel != null){
            System.out.println("nooooo");
            channel.writeAndFlush(new ChatResponseMessage(fromUser, content));
        }else {
            ctx.writeAndFlush(new ChatResponseMessage(false,"对方目前不在线上，请稍后再试"));
        }
    }
}
