package com.hirshi001.serializer;

import com.google.common.annotations.GwtIncompatible;
import com.hirshi001.buffer.buffers.ByteBuffer;

import java.lang.reflect.Array;

public class ByteBufferArrayUtil {

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
