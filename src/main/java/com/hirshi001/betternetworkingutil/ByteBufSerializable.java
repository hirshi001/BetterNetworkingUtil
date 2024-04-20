package com.hirshi001.betternetworkingutil;

import com.hirshi001.buffer.buffers.ByteBuffer;

public interface ByteBufSerializable {

    void writeBytes(ByteBuffer buffer);

    void readBytes(ByteBuffer buffer);

}
