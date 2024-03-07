package com.hirshi001.serializer;

import com.google.common.annotations.GwtIncompatible;
import com.hirshi001.buffer.buffers.ByteBuffer;

import java.lang.reflect.Array;

public class ByteBufferArrayUtil {


    public static <T> void writeNDArray(Object array, int dimension, ByteBuffer buffer, ByteBufSerializer<T> serializer) {
        if (array == null || !array.getClass().isArray()) {
            throw new IllegalArgumentException("bytes must be an array");
        }
//        if (!isSerializable(clazz, serializer)) {
//            throw new IllegalArgumentException("The component type of the array is not serializable");
//        }
        writeNDArray0(array, dimension, buffer, serializer);
    }

    @GwtIncompatible
    private static <T> boolean isSerializable(Class<?> clazz, ByteBufSerializer<T> serializer) {
        if (serializer != null)
            return true;
        return clazz.isPrimitive() || clazz.isEnum() || ByteBufSerializable.class.isAssignableFrom(clazz);
    }

    private static <T> void writeNDArray0(Object bytes, int dimension, ByteBuffer buffer, ByteBufSerializer<T> serializer) {
        if (bytes == null) {
            buffer.writeInt(-1);
            return;
        }
        if (dimension != 1) {
            int length = Array.getLength(bytes);
            buffer.writeInt(length);
            for (int i = 0; i < length; i++) {
                writeNDArray0(Array.get(bytes, i), dimension - 1, buffer, serializer);
            }
            return;
        }

        if (serializer != null) {
            writeObjectArray((Object[]) bytes, buffer, serializer);
            return;
        }

        Class<?> componentType = bytes.getClass().getComponentType();
        if (componentType == byte.class) {
            writeByteArray((byte[]) bytes, buffer);
            return;
        }
        if (componentType == short.class) {
            writeShortArray((short[]) bytes, buffer);
            return;
        }
        if (componentType == int.class) {
            writeIntArray((int[]) bytes, buffer);
            return;
        }
        if (componentType == long.class) {
            writeLongArray((long[]) bytes, buffer);
            return;
        }
        if (componentType == float.class) {
            writeFloatArray((float[]) bytes, buffer);
            return;
        }
        if (componentType == double.class) {
            writeDoubleArray((double[]) bytes, buffer);
            return;
        }
        if (componentType == boolean.class) {
            writeBooleanArray((boolean[]) bytes, buffer);
            return;
        }
    }

    public static <T> void writeObjectArray(Object[] objects, ByteBuffer buffer, ByteBufSerializer<T> serializer) {
        if (objects == null) {
            buffer.writeInt(-1);
            return;
        }
        buffer.writeInt(objects.length);
        for (Object o : objects) {
            writeObject(o, buffer, serializer);
        }
    }

    public static <T> void writeObject(Object object, ByteBuffer buffer, ByteBufSerializer<T> serializer) {
        if (object == null) {
            buffer.writeBoolean(false);
            return;
        }
        buffer.writeBoolean(true);
        serializer.serialize((T) object, buffer);
    }


    public static <T> Object readNDArray(ByteBuffer buffer, ByteBufSerializer<T> serializer) {
        return readNDArray0(buffer, serializer);
    }


    private static <T> Object readNDArray0(ByteBuffer buffer, ByteBufSerializer<T> serializer) {
        int length = buffer.readInt();
        if (length == -1) {
            return null;
        }
        buffer.writeBoolean(true);

        /*
        if (bytes.getClass().getComponentType().isArray()) {
            buffer.writeInt(Array.getLength(bytes));
            for (int i = 0; i < Array.getLength(bytes); i++) {
                readNDArray0(Array.get(bytes, i), buffer, serializer);
            }
        }

         */

        if (serializer != null) {
            readObjectArray(buffer, serializer);
        }

        Class<?> componentType = null;
        if (componentType == byte.class) {
            return readByteArray(buffer);
        }
        if (componentType == short.class) {
            return readShortArray(buffer);
        }
        if (componentType == int.class) {
            return readIntArray(buffer);
        }
        if (componentType == long.class) {
            return readLongArray(buffer);
        }
        if (componentType == float.class) {
            return readFloatArray(buffer);
        }
        if (componentType == double.class) {
            return readDoubleArray(buffer);
        }
        if (componentType == boolean.class) {
            return readBooleanArray(buffer);
        }
        return null;
    }

    public static <T> void readObjectArray(ByteBuffer buffer, ByteBufSerializer<T> serializer) {
        /*
        buffer.writeInt(objects.length);
        for (Object o : objects) {
            readObject(o, buffer, serializer);
        }

         */
    }

    public static <T> T readObject(ByteBuffer buffer, ByteBufSerializer<T> serializer) {
        if (!buffer.readBoolean()) {
            return null;
        }
        return serializer.deserialize(buffer);
    }


    public static void writeByteArray(byte[] bytes, ByteBuffer buffer) {
        buffer.writeInt(bytes.length);
        buffer.writeBytes(bytes);
    }

    public static byte[] readByteArray(ByteBuffer buffer) {
        int length = buffer.readInt();
        byte[] bytes = new byte[length];
        buffer.readBytes(buffer);
        return bytes;
    }

    public static void writeShortArray(short[] shorts, ByteBuffer buffer) {
        buffer.writeInt(shorts.length);
        for (short s : shorts) {
            buffer.writeShort(s);
        }
    }

    public static short[] readShortArray(ByteBuffer buffer) {
        int length = buffer.readInt();
        short[] shorts = new short[length];
        for (int i = 0; i < length; i++) {
            shorts[i] = buffer.readShort();
        }
        return shorts;
    }

    public static void writeIntArray(int[] ints, ByteBuffer buffer) {
        buffer.writeInt(ints.length);
        for (int i : ints) {
            buffer.writeInt(i);
        }
    }

    public static int[] readIntArray(ByteBuffer buffer) {
        int length = buffer.readInt();
        int[] ints = new int[length];
        for (int i = 0; i < length; i++) {
            ints[i] = buffer.readInt();
        }
        return ints;
    }

    public static void writeLongArray(long[] longs, ByteBuffer buffer) {
        buffer.writeInt(longs.length);
        for (long l : longs) {
            buffer.writeLong(l);
        }
    }

    public static long[] readLongArray(ByteBuffer buffer) {
        int length = buffer.readInt();
        long[] longs = new long[length];
        for (int i = 0; i < length; i++) {
            longs[i] = buffer.readLong();
        }
        return longs;
    }

    public static void writeFloatArray(float[] floats, ByteBuffer buffer) {
        buffer.writeInt(floats.length);
        for (float f : floats) {
            buffer.writeFloat(f);
        }
    }

    public static float[] readFloatArray(ByteBuffer buffer) {
        int length = buffer.readInt();
        float[] floats = new float[length];
        for (int i = 0; i < length; i++) {
            floats[i] = buffer.readFloat();
        }
        return floats;
    }

    public static void writeDoubleArray(double[] doubles, ByteBuffer buffer) {
        buffer.writeInt(doubles.length);
        for (double d : doubles) {
            buffer.writeDouble(d);
        }
    }

    public static double[] readDoubleArray(ByteBuffer buffer) {
        int length = buffer.readInt();
        double[] doubles = new double[length];
        for (int i = 0; i < length; i++) {
            doubles[i] = buffer.readDouble();
        }
        return doubles;
    }

    public static void writeBooleanArray(boolean[] booleans, ByteBuffer buffer) {
        buffer.writeInt(booleans.length);
        for (boolean b : booleans) {
            buffer.writeBoolean(b);
        }
    }

    public static boolean[] readBooleanArray(ByteBuffer buffer) {
        int length = buffer.readInt();
        boolean[] booleans = new boolean[length];
        for (int i = 0; i < length; i++) {
            booleans[i] = buffer.readBoolean();
        }
        return booleans;
    }

}
