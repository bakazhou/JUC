package com.cn.tw.graduate.bakazhou.ByteBuffer;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

public class TestVisioPacket {
    @Test
    void testVisioPacket() {
        ByteBuffer buffer = ByteBuffer.allocate(32);
        // 模拟粘包+半包
        buffer.put("Hello,world\nI'm Nyima\nHo".getBytes());
        // 调用split函数处理
        split(buffer);
        buffer.put("w are you?\n".getBytes());
        split(buffer);
    }

    private void split(ByteBuffer buffer) {
        buffer.flip();
        for (int i = 0; i < buffer.limit(); i++) {
            //找到换行符的下标
            //调用get(index)方法并不会改变position的位置
            if (buffer.get(i) == '\n'){
                StringBuilder builder = new StringBuilder();
                //当前换行符前的字符长度，即需要取出的缓冲区长度
                int length = i + 1 - buffer.position();
                for (int j = 0; j < length; j++) {
                    //取出buffer中的字符，get方法会使position+1
                    builder.append((char)buffer.get());
                }
                System.out.println("string:"+ builder);
            }
        }
        //调用compact，使缓冲区中未读取完的字符不会被清空
        /*
        例如Hello,world\nI'm Nyima\nHo
        因为Ho后没有\n所以Ho不会被取出，compact会将Ho向前压缩，再第二次读取时就会组成How are you?\n
         */
        buffer.compact();
    }
}
