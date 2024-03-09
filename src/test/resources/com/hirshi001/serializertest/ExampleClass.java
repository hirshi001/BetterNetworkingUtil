package com.hirshi001.serializertest;

import com.hirshi001.buffer.buffers.ByteBuffer;
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

    public void writeBytes(ByteBuffer buffer) {
        if (dotdot == null) {
            buffer.writeInt(-1);
        }
        else {
            buffer.writeInt(dotdot.length);
            for (int i3 = 0; i3 < dotdot.length; i3++) {
                if (dotdot[i3] == null) {
                    buffer.writeInt(-1);
                }
                else {
                    buffer.writeInt(dotdot[i3].length);
                    for(int i2 = 0; i2 < dotdot[i3].length; i2++) {
                        if(dotdot[i3][i2] == null) {
                            buffer.writeInt(-1);
                        }
                        else {
                            buffer.writeInt(dotdot[i3][i2].length);
                            for(int i = 0; i < dotdot[i3][i2].length; i++) {
                                if(dotdot[i3][i2][i] == null) {
                                    buffer.writeInt(-1);
                                }
                                else {
                                    buffer.writeInt(dotdot[i3][i2][i].dependency);
                                }
                            }
                        }
                    }

                }
            }
        }
    }


}
