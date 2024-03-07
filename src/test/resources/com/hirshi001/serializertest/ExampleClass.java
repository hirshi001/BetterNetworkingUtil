package com.hirshi001.serializertest;
import com.hirshi001.serializer.CreateByteBufSerializer;
import com.hirshi001.serializer.NoSerialize;

@CreateByteBufSerializer
public class ExampleClass {

    public static int doNotSerialize;

    public DependencyClass[][][] dotdot;
    public ExampleClass(int id, String name) {
    }

    public ExampleClass() {

    }
}
