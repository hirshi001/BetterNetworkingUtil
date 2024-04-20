package com.hirshi001.serializertest;
import com.hirshi001.buffer.buffers.ByteBuffer;
import com.hirshi001.betternetworkingutil.ByteBufSerializer;

public class MyDependencyClassSerializer implements ByteBufSerializer<DependencyClass> {

    @Override
    public void serialize(com.hirshi001.serializertest.DependencyClass object, ByteBuffer buffer) {
        buffer.writeInt(object.dependency);
    }

    @Override
    public com.hirshi001.serializertest.DependencyClass deserialize(ByteBuffer buffer) {
        DependencyClass obj = new DependencyClass();
        obj.dependency = buffer.readInt();
        return obj;
    }
}
