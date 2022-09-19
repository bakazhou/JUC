package com.cn.tw.graduate.bakazhou;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

public class TestBufferReadWrite {
    @Test
    void test() {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        // 向buffer中写入1个字节的数据
        buffer.put((byte)97);
        // 使用工具类，查看buffer状态
        System.out.println("put one element");
        ByteBufferUtil.debugAll(buffer);

        System.out.println();
        // 向buffer中写入4个字节的数据
        buffer.put(new byte[]{98, 99, 100, 101});
        System.out.println("put four element");
        ByteBufferUtil.debugAll(buffer);
        System.out.println();

        //进入读状态
        buffer.flip();
        System.out.println("after flip");
        ByteBufferUtil.debugAll(buffer);
        System.out.println();

        System.out.println(buffer.get());
        System.out.println(buffer.get());
        System.out.println("after get twice");
        ByteBufferUtil.debugAll(buffer);
        System.out.println();
        // 进入写状态
        // 使用compact切换模式
        buffer.compact();
        System.out.println("after compact");
        ByteBufferUtil.debugAll(buffer);
        System.out.println();
        // 再次写入
        buffer.put((byte)102);
        buffer.put((byte)103);
        System.out.println("after put double element again");
        ByteBufferUtil.debugAll(buffer);
    }
}
