package com.cn.tw.graduate.bakazhou.Practice3.protocol;

import com.cn.tw.graduate.bakazhou.Practice3.message.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageCodec;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.charset.Charset;
import java.util.List;

//通过泛型制定编解码的对象
public class MessageCodec extends ByteToMessageCodec<Message> {

    //出栈时进行编码
    @Override
    protected void encode(ChannelHandlerContext ctx, Message msg, ByteBuf out) throws Exception {
        //魔数 BAKAZHOU 占八个字节
        out.writeBytes("BAKAZHOU".getBytes());

        //版本 1 占四个字节
        out.writeInt(1);
        //序列化算法  0代表Json 1代表jdk 占四个字节
        out.writeInt(0);

        //指令类型 int类型占四个字节
        out.writeInt(msg.getMessageType());

        //请求序号 占四个字节
        out.writeInt(msg.getSequenceId());

        //将消息正文站位bytes
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(bos);
        oos.writeObject(msg);
        byte[] msgBytes = bos.toByteArray();

        //正文长度 占四个字节
        out.writeInt(msgBytes.length);

        //正文前一共有28个字节
        //写入正文
        out.writeBytes(msgBytes);
    }

    //入栈时进行解码
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        //魔数 BAKAZHOU 8字节
        String magicNum = in.readBytes(8).toString(Charset.defaultCharset());

        //版本号 4字节
        int version = in.readInt();

        //序列化算法 
        int serializationAlgorithm = in.readInt();

        int messageType = in.readInt();

        //请求序号
        int sequenceId = in.readInt();

        //正文长度
        int length = in.readInt();

        //正文内容
        byte[] msg = new byte[length];
        in.readBytes(msg,0,length);
        //判断序列化方式
        switch (serializationAlgorithm){
            case 0:
                ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(msg));
                Message message = (Message) ois.readObject();

                //传给下一个处理器使用
                out.add(message);
                break;
            case 1:
                break;
            default:
                break;
        }

    }
}
