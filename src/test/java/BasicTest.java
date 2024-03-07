import com.google.testing.compile.Compilation;
import com.google.testing.compile.Compiler;
import com.google.testing.compile.JavaFileObjects;
import com.hirshi001.buffer.bufferfactory.BufferFactory;
import com.hirshi001.buffer.bufferfactory.DefaultBufferFactory;
import com.hirshi001.serializer.ByteBufSerializerAnnotationProcesser;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.tools.JavaFileObject;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Collection;

import static com.google.testing.compile.CompilationSubject.assertThat;


public class BasicTest {


    static BufferFactory bufferFactory;

    @BeforeAll
    public static void setup() {
        bufferFactory = new DefaultBufferFactory();
    }


    @Test
    public void test() throws Exception {
        Compilation compilation = Compiler.javac()
                .withProcessors(new ByteBufSerializerAnnotationProcesser())
                .compile(
                        JavaFileObjects.forResource("com/hirshi001/serializertest/ExampleClass.java"),
                        JavaFileObjects.forResource("com/hirshi001/serializertest/DependencyClass.java"));

        assertThat(compilation).succeeded();
        for(JavaFileObject fileObject : compilation.generatedSourceFiles()){
            System.out.println(fileObject.getCharContent(true));
        }
    }

}
