package com.hirshi001.serializer;

import com.hirshi001.buffer.buffers.ByteBuffer;

public interface ByteBufSerializable {

    void writeBytes(ByteBuffer buffer);

    void readBytes(ByteBuffer buffer);

}
