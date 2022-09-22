package com.cn.tw.graduate.bakazhou.Practice3.server.handler;

import com.cn.tw.graduate.bakazhou.Practice3.message.LoginRequestMessage;
import com.cn.tw.graduate.bakazhou.Practice3.message.LoginResponseMessage;
import com.cn.tw.graduate.bakazhou.Practice3.server.service.UserServiceFactory;
import com.cn.tw.graduate.bakazhou.Practice3.server.session.SessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;


@ChannelHandler.Sharable
public class LoginRequestMessageSimpleChannelInboundHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) {
        boolean login = UserServiceFactory.getUserService().login(msg.getUsername(), msg.getPassword());
        LoginResponseMessage loginResponseMessage;
        if (login) {
            //保存channel与user的关系，用于后期的通讯
            SessionFactory.getSession().bind(ctx.channel(), msg.getUsername());
            loginResponseMessage = new LoginResponseMessage(true, "登录成功");

        } else {
            loginResponseMessage = new LoginResponseMessage(false, "用户名或密码错误");
        }
        ctx.writeAndFlush(loginResponseMessage);
    }
}
