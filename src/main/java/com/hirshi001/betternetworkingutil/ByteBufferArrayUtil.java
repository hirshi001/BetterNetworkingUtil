package com.hirshi001.betternetworkingutil;

import com.hirshi001.buffer.buffers.ByteBuffer;

public class ByteBufferArrayUtil {

    private static boolean nullWriteCheck(Object object, ByteBuffer buffer) {
        if (object == null) {
            buffer.writeInt(-1);
            return true;
        }
        return false;
    }

    public static void writeByteArray(byte[] bytes, ByteBuffer buffer) {
        if (nullWriteCheck(bytes, buffer)) return;
        buffer.writeInt(bytes.length);
        buffer.writeBytes(bytes);
    }

    public static byte[] readByteArray(ByteBuffer buffer) {
        int length = buffer.readInt();
        if(length == -1) return null;
        byte[] bytes = new byte[length];
        buffer.readBytes(buffer);
        return bytes;
    }

    public static void writeShortArray(short[] shorts, ByteBuffer buffer) {
        if (nullWriteCheck(shorts, buffer)) return;
        buffer.writeInt(shorts.length);
        for (short s : shorts) {
            buffer.writeShort(s);
        }
    }

    public static short[] readShortArray(ByteBuffer buffer) {
        int length = buffer.readInt();
        if(length == -1) return null;
        short[] shorts = new short[length];
        for (int i = 0; i < length; i++) {
            shorts[i] = buffer.readShort();
        }
        return shorts;
    }

    public static void writeIntArray(int[] ints, ByteBuffer buffer) {
        if (nullWriteCheck(ints, buffer)) return;
        buffer.writeInt(ints.length);
        for (int i : ints) {
            buffer.writeInt(i);
        }
    }

    public static int[] readIntArray(ByteBuffer buffer) {
        int length = buffer.readInt();
        if(length == -1) return null;
        int[] ints = new int[length];
        for (int i = 0; i < length; i++) {
            ints[i] = buffer.readInt();
        }
        return ints;
    }

    public static void writeLongArray(long[] longs, ByteBuffer buffer) {
        if (nullWriteCheck(longs, buffer)) return;
        buffer.writeInt(longs.length);
        for (long l : longs) {
            buffer.writeLong(l);
        }
    }

    public static long[] readLongArray(ByteBuffer buffer) {
        int length = buffer.readInt();
        if(length == -1) return null;
        long[] longs = new long[length];
        for (int i = 0; i < length; i++) {
            longs[i] = buffer.readLong();
        }
        return longs;
    }

    public static void writeFloatArray(float[] floats, ByteBuffer buffer) {
        if (nullWriteCheck(floats, buffer)) return;
        buffer.writeInt(floats.length);
        for (float f : floats) {
            buffer.writeFloat(f);
        }
    }

    public static float[] readFloatArray(ByteBuffer buffer) {
        int length = buffer.readInt();
        if(length == -1) return null;
        float[] floats = new float[length];
        for (int i = 0; i < length; i++) {
            floats[i] = buffer.readFloat();
        }
        return floats;
    }

    public static void writeDoubleArray(double[] doubles, ByteBuffer buffer) {
        if (nullWriteCheck(doubles, buffer)) return;
        buffer.writeInt(doubles.length);
        for (double d : doubles) {
            buffer.writeDouble(d);
        }
    }

    public static double[] readDoubleArray(ByteBuffer buffer) {
        int length = buffer.readInt();
        if(length == -1) return null;
        double[] doubles = new double[length];
        for (int i = 0; i < length; i++) {
            doubles[i] = buffer.readDouble();
        }
        return doubles;
    }

    public static void writeBooleanArray(boolean[] booleans, ByteBuffer buffer) {
        if (nullWriteCheck(booleans, buffer)) return;
        buffer.writeInt(booleans.length);
        for (boolean b : booleans) {
            buffer.writeBoolean(b);
        }
    }

    public static boolean[] readBooleanArray(ByteBuffer buffer) {
        int length = buffer.readInt();
        if(length == -1) return null;
        boolean[] booleans = new boolean[length];
        for (int i = 0; i < length; i++) {
            booleans[i] = buffer.readBoolean();
        }
        return booleans;
    }

}
