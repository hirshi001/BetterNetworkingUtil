package com.hirshi001.serializer;

import java.util.HashMap;
import java.util.Map;

public class ByteBufSerializers {

    private final Map<Class<?>, ByteBufSerializer<?>> serializers = new HashMap<>();

    public void addSerializer(Class<?> clazz, ByteBufSerializer<?> serializer){
        serializers.put(clazz, serializer);
    }

    public ByteBufSerializer<?> getSerializer(Class<?> clazz){
        return serializers.get(clazz);
    }


}
