package com.hirshi001.serializer;


import com.hirshi001.buffer.buffers.ByteBuffer;

public interface ByteBufSerializer<T>{

    void serialize(T object, ByteBuffer buffer);

    T deserialize(ByteBuffer buffer);
}
