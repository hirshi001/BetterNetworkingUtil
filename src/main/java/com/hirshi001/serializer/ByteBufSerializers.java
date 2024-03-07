package com.hirshi001.serializer;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class ByteBufSerializers {
    public static <T> ByteBufSerializer<T> getSerializer(Class<T> clazz) {
        List<ClassLoader> classLoaders = getClassLoaders(clazz);
        for (ClassLoader classLoader : classLoaders) {
            Class<ByteBufSerializer<T>> serializerClass = getSerializerClass(clazz, classLoader);
            if(serializerClass == null)
                continue;
            ByteBufSerializer<T> serializer = createInstance(serializerClass);
            if(serializer != null)
                return serializer;
        }
        return null;
    }


    @SuppressWarnings("unchecked")
    private static <T> Class<ByteBufSerializer<T>> getSerializerClass(Class<T> clazz, ClassLoader classLoader) {
        try {
            return (Class<ByteBufSerializer<T>>) classLoader.loadClass("com.hirshi001.serializer.serializers." + clazz.getSimpleName() + "Serializer");
        } catch (ClassNotFoundException e) {
            return null;
        }
    }



    private static <T> ByteBufSerializer<T> createInstance(Class<ByteBufSerializer<T>> clazz) {
        try {
            return clazz.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            return null;
        }
    }


    private static List<ClassLoader> getClassLoaders(Class<?> clazz) {
        List<ClassLoader> classLoaders = new ArrayList<>();
        classLoaders.add(clazz.getClassLoader());
        if (Thread.currentThread().getContextClassLoader() != null)
            classLoaders.add(Thread.currentThread().getContextClassLoader());
        classLoaders.add(ClassLoader.getSystemClassLoader());
        return classLoaders;
    }

}
