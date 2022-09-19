package com.cn.tw.graduate.bakazhou;

import org.junit.jupiter.api.Test;

import java.nio.ByteBuffer;

public class TestByteAllocate {
    @Test
    void test() {
        //allocate方法开辟的是堆内存空间，读写效率低，同时收到垃圾回收的影响
        ByteBuffer allocate = ByteBuffer.allocate(16);
        //allocateDirect方法开辟的是直接内存，读写效率高，分配效率低，可能会造成内存泄漏
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(16);
    }
}
