package com.cn.tw.graduate.bakazhou.Practice2;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class LengthFieldDecoder {
    public static void main(String[] args) {
        // 模拟服务器
        // 使用EmbeddedChannel测试handler
        EmbeddedChannel channel = new EmbeddedChannel(
                /*
                   数据最大长度为1KB，长度标识前后各有1个字节的附加信息，长度标识长度为4个字节（int）
                   只获取其中的message信息 其他不需要
                   数据实际为
                            +-------------------------------------------------+
                                     |  0  1  2  3  4  5  6  7  8  9  a  b  c  d  e  f |
                            +--------+-------------------------------------------------+----------------+
                            |00000000| ca 00 00 00 05 fe 57 6f 72 6c 64                |......World     |
                            +--------+-------------------------------------------------+----------------+
                            0的位置是长度前的信息，占位一个字节，所以lengthFieldOffset需要设置为1，从1的位置开始读取信息长度
                            1-4是长度信息，int类型占位四个字节，所以lengthFieldLength需要设置为4
                            5的位置为实际信息前的额外数据，所以lengthAdjustment需要设置为1，表明其后1位开始才是实际信息
                            6-a的位置是实际信息，如果想解码取出实际信息，initialBytesToStrip设置为6，因为前面的多余信息所占字节数为1+4+1=6
                 */
                new LengthFieldBasedFrameDecoder(1024, 1, 4, 1, 6),
                new LoggingHandler(LogLevel.INFO)
        );

        // 模拟客户端，写入数据
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer();
        send(buffer, "Helloooooooo");
        channel.writeInbound(buffer);
        send(buffer, "World");
        channel.writeInbound(buffer);
    }

    private static void send(ByteBuf buf, String msg) {
        // 得到数据的长度
        int length = msg.length();
        byte[] bytes = msg.getBytes(StandardCharsets.UTF_8);
        // 将数据信息写入buf
        // 写入长度标识前的其他信息 占一个字节
        buf.writeByte(0xCA);
        // 写入数据长度标识 一个int占四个字节
        buf.writeInt(length);
        // 写入长度标识后的其他信息 占一个字节
        buf.writeByte(0xFE);
        // 写入具体的数据
        buf.writeBytes(bytes);
    }
}
