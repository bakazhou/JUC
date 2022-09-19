package com.cn.tw.graduate.bakazhou;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

class TestByteBuffer {
    @Test
    void shouldReturnStringWhenReadData() {
        //File Channel
        //通过输入输出流
        try(FileChannel channel = new FileInputStream("src/data.txt").getChannel()) {
            //准备缓冲区
            ByteBuffer buffer = ByteBuffer.allocate(1);

            StringBuilder stringBuilder = new StringBuilder();


            //从Channel读取，向Buffer写入
            while (channel.read(buffer) != -1){
                //切换到buffer的读模式
                buffer.flip();
                while (buffer.hasRemaining()){
                    byte b = buffer.get();
                    stringBuilder.append((char) b);
                }

                //切换到写模式
                buffer.clear();
            }

            Assertions.assertEquals("1234567890abcd",stringBuilder.toString());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}