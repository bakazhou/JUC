package com.cn.tw.graduate.bakazhou.Practice3.protocol;

import com.cn.tw.graduate.bakazhou.Practice3.message.LoginRequestMessage;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestMessageCodec {
    public static void main(String[] args) {
        EmbeddedChannel channel = new EmbeddedChannel(
                new LoggingHandler(LogLevel.INFO),
                new MessageCodec());

        LoginRequestMessage loginUser = new LoginRequestMessage("bakazhou", "123456");
        channel.writeOutbound(loginUser);

    }
}
