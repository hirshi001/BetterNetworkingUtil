package com.hirshi001.serializer;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
@SupportedAnnotationTypes("com.hirshi001.serializer.CreateByteBufSerializer")
public class ByteBufSerializerAnnotationProcesser extends AbstractProcessor {
    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement element : annotations) {
            roundEnv.getElementsAnnotatedWith(element).forEach(this::generateSerializer);
        }
        return false;
    }

    private void generateSerializer(Element element) {
        String className = element.getSimpleName().toString();
        String packageName = element.getEnclosingElement().toString();
        String serializerName = className + "Serializer";
        String serializerFullName = packageName + "." + serializerName;
        List<VariableElement> fields = ElementFilter.fieldsIn(element.getEnclosedElements());

        System.out.println("Class: " + className + " is annotated with @CreateByteBufSerializer");
        System.out.println("Package: " + packageName);
        System.out.println("Serializer Name: " + serializerName);
        System.out.println("Serializer Full Name: " + serializerFullName);
        System.out.println("Fields: " + fields);
        /*
        try (PrintWriter writer = new PrintWriter(
                processingEnv.getFiler().createSourceFile(serializerFullName).openWriter())) {
            writer.println("""
                    package %s;
                    import com.hirshi001.buffer.buffers.ByteBuffer;

                    import %s;
                    public class %s implements com.hirshi001.serializer.ByteBufSerializer<%s>{
                        public static void serialize(ByteBuf buf, %s obj) {
                            %s
                        }
                        public static %s deserialize(ByteBuf buf) {
                            %s
                        }
                    }
                    """.formatted(packageName, element, serializerName, className, serializeFields(fields), className, deserializeFields(fields)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        */

    }
}
