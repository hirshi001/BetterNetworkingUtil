package com.hirshi001.serializer;

import java.util.HashMap;
import java.util.Map;

public class Serializers {

    private static final Map<Class<?>, ByteBufSerializer<?>> serializers = new HashMap<>();


    public static <T> void addSerializer(Class<T> clazz, ByteBufSerializer<T> serializer){
        serializers.put(clazz, serializer);
    }

    public static <T> ByteBufSerializer<T> getSerializer(Class<T> clazz){
        return (ByteBufSerializer<T>) serializers.get(clazz);
    }



}
