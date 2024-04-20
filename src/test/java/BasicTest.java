import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import com.hirshi001.buffer.bufferfactory.BufferFactory;
import com.hirshi001.buffer.bufferfactory.DefaultBufferFactory;
import com.hirshi001.buffer.buffers.ByteBuffer;
import com.hirshi001.betternetworkingutil.ByteBufSerializerAnnotationProcessor;
import com.hirshi001.betternetworkingutil.ByteBufSerializers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;

import java.util.Arrays;

import static com.google.testing.compile.CompilationSubject.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class BasicTest {


    static BufferFactory bufferFactory;

    @BeforeAll
    public static void setup() {
        bufferFactory = new DefaultBufferFactory();
    }


    @Test
    public void test() throws Exception {
        Compilation compilation = Compiler.javac()
                .withProcessors(new ByteBufSerializerAnnotationProcessor())
                .compile(
                        JavaFileObjects.forResource("com/hirshi001/serializertest/ExampleClass.java"),
                        JavaFileObjects.forResource("com/hirshi001/serializertest/DependencyClass.java"));

        assertThat(compilation).succeeded();
        for(JavaFileObject fileObject : compilation.generatedSourceFiles()){
            System.out.println(fileObject.getCharContent(true));
        }
    }

    @Test
    public void carTest() throws ClassNotFoundException {
        Class<?> clazz = this.getClass().getClassLoader().loadClass("Car");
        System.out.println(ByteBufSerializers.getSerializer(clazz));
    }

    @Test
    public void testOutput() {
        ExampleClassSerializer serializer = new ExampleClassSerializer();
        ExampleClass exampleClass = new ExampleClass();
        exampleClass.dotdot = new DependencyClass[3][][];
        exampleClass.dotdot[0] = new DependencyClass[1][];
        exampleClass.dotdot[1] = new DependencyClass[1][];
        exampleClass.dotdot[2] = new DependencyClass[1][];
        exampleClass.dotdot[0][0] = new DependencyClass[1];
        exampleClass.dotdot[1][0] = new DependencyClass[1];
        exampleClass.dotdot[2][0] = new DependencyClass[1];
        exampleClass.dotdot[0][0][0] = new DependencyClass();
        exampleClass.dotdot[1][0][0] = new DependencyClass();
        exampleClass.dotdot[2][0][0] = new DependencyClass();
        ByteBuffer buffer = bufferFactory.circularBuffer();
        serializer.serialize(exampleClass, buffer);

        ExampleClass deserialized = serializer.deserialize(buffer);
        assertNotNull(deserialized);

        assert Arrays.deepEquals(exampleClass.dotdot, deserialized.dotdot);

    }

}
