package com.hirshi001.betternetworkingutil;


import com.hirshi001.buffer.buffers.ByteBuffer;

public interface ByteBufSerializer<T>{

    void serialize(T object, ByteBuffer buffer);

    T deserialize(ByteBuffer buffer);
}
