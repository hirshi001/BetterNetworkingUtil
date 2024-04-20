package com.hirshi001.betternetworkingutil;

import com.google.auto.service.AutoService;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.ArrayType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_15)
@SupportedAnnotationTypes("com.hirshi001.betternetworkingutil.CreateByteBufSerializer")
public class ByteBufSerializerAnnotationProcessor extends AbstractProcessor {

    public static final String BYTE_BUF_UTIL = "com.hirshi001.buffer.util.ByteBufUtil";

    public static final String ARR_UTIL = "com.hirshi001.betternetworkingutil.ByteBufferArrayUtil";
    public static final String BYTE_BUF_SERIALIZER = "com.hirshi001.betternetworkingutil.ByteBufSerializer";
    public static final String BYTE_BUFFER = "com.hirshi001.buffer.buffers.ByteBuffer";

    public static final String PACKAGE = "com.hirshi001.betternetworkingutil.serializers";

    Types typeUtils;
    TypeMirror byteBufSerializable;

    private final int MODE_SERIALIZE = 0;
    private final int MODE_DESERIALIZE = 1;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        typeUtils = processingEnv.getTypeUtils();
        byteBufSerializable = processingEnv.getElementUtils().getTypeElement("com.hirshi001.betternetworkingutil.ByteBufSerializable").asType();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        List<Element> addedSerializers = new ArrayList<>();
        for (TypeElement element : annotations) {
            addedSerializers.addAll(roundEnv.getElementsAnnotatedWith(element));
        };

        addedSerializers.forEach(this::generateSerializer);
        return false;
    }

    private void generateSerializer(Element element) {
        String className = element.getSimpleName().toString(); // example "String"
        String packageName = element.getEnclosingElement().toString(); // example "java.lang"
        String fullClassName = packageName + "." + className; // example "java.lang.String"
        String serializerName = getSerializerName(className); // example "StringSerializer"
        String serializerFullName = PACKAGE + "." + serializerName; // example "com.hirshi001.betternetworkingutil.serializers.StringSerializer"
        List<VariableElement> fields = ElementFilter.fieldsIn(element.getEnclosedElements());

        System.out.println("Class: " + className + " is annotated with @CreateByteBufSerializer");
        System.out.println("Package: " + packageName);
        System.out.println("Serializer Name: " + serializerName);
        System.out.println("Serializer Full Name: " + serializerFullName);
        System.out.println("Fields: " + fields);

        try (PrintWriter writer = new PrintWriter(
                processingEnv.getFiler().createSourceFile(serializerFullName).openWriter())) {
            String serializeCode = getSerializeCode(fields, 2, MODE_SERIALIZE);
            String deserializeCode = getSerializeCode(fields, 2, MODE_DESERIALIZE);
            String serializerFieldCode = getSerializerFieldCode(fields, 1);
            writer.println("""
                    package %package;
                                       
                                        
                    public class %SerialName implements %Serializer<%ClassName>{
                                        
                    %SerializeFieldCode
                        
                        public void serialize(%ClassName object, %Buffer buffer) {
                    %SerializeCode
                        }
                                        
                        public %ClassName deserialize(%Buffer buffer) {
                            %ClassName object = new %ClassName();
                    %DeserializeCode
                            return object;
                        }
                    }
                    """
                    .replace("%package", PACKAGE)
                    .replace("%SerialName", serializerName)
                    .replace("%Serializer", BYTE_BUF_SERIALIZER)
                    .replace("%ClassName", fullClassName)
                    .replace("%Buffer", BYTE_BUFFER)
                    .replace("%SerializeFieldCode", serializerFieldCode)
                    .replace("%SerializeCode", serializeCode)
                    .replace("%DeserializeCode", deserializeCode));
            //.formatted(serializerName, fullClassName, serializerFieldCode, fullClassName, serializeCode, fullClassName, fullClassName, deserializeCode));

            processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE, "Created Serializer: " + serializerFullName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String getSerializerFieldCode(List<VariableElement> elements, int indent) {
        Set<TypeMirror> serializerType = new HashSet<>();
        String indentString = getIndentString(indent);
        StringBuilder builder = new StringBuilder();
        for (VariableElement element : elements) {
            if (!shouldSerialize(element))
                continue;

            TypeMirror type = getTrueType(element.asType());
            if (type.getKind().isPrimitive() || type.toString().equals("java.lang.String") || isByteBufSerializable(type))
                continue;

            serializerType.add(type);
        }

        for (TypeMirror type : serializerType) {
            String serializerName = getSerializerName(extractSimpleName(type.toString()));
            builder.append(indentString);
            builder.append("private final %s %s = new %s();".formatted(serializerName, serializerName.toLowerCase(), serializerName));
            builder.append("\n");
        }
        return builder.toString();
    }


    private String getSerializeCode(List<VariableElement> elements, int indent, int mode) {
        String indentString = getIndentString(indent);
        StringBuilder builder = new StringBuilder();
        for (VariableElement element : elements) {
            if (!shouldSerialize(element))
                continue;
            serializeElement(element, builder, indentString, mode);
            builder.append("\n");
        }
        builder.deleteCharAt(builder.length() - 1); // remove last newline
        return builder.toString();
    }

    private void serializeElement(VariableElement element, StringBuilder builder, String indentString, int mode) {
        TypeKind kind = element.asType().getKind();
        String elementName = element.getSimpleName().toString();
        // check if element is a primitive
        if (kind.isPrimitive()) {
            builder.append(indentString);
            builder.append(writePrimitive(kind, elementName, mode));
        } else {
            builder.append(indentString);
            builder.append(writeUnknownObject(element, mode).replaceAll("\n", "\n" + indentString));
        }
    }

    private String writeUnknownObject(VariableElement element, int mode) {
        String name = element.getSimpleName().toString();
        TypeMirror type = element.asType();

        if (type.getKind() == TypeKind.ARRAY) {
            TypeMirror trueType = getTrueType(type); // get the true type (not the array type)
            int dimension = count(type.toString(), '[');
            return "\n" + writeArray(type, name, name, trueType, dimension, getIndentString(1), mode) + "\n";
        } else {
            return writeObject(element.asType(), element.getSimpleName().toString(), mode);
        }
    }

    private String writeArray(TypeMirror type, String name, String ogName, TypeMirror trueType, int dimension, String indent, int mode) {
        TypeMirror componentType = ((ArrayType) type).getComponentType();
        if (dimension == 1) { // base case
            if (componentType.getKind().isPrimitive()) {
                // capitalize first letter
                String arrType = componentType.toString();
                arrType = arrType.substring(0, 1).toUpperCase() + arrType.substring(1);
                if (mode == MODE_SERIALIZE)
                    return ARR_UTIL + ".write" + arrType + "Array(object." + name + ", buffer);";
                else
                    return "object." + name + " = " + ARR_UTIL + ".read" + arrType + "Array(buffer);";
            } else {
                String serializingCode = writeObject(componentType, name + "[i]", mode);
                if (mode == MODE_SERIALIZE)
                    return """
                            if(object.%s == null) {
                                buffer.writeInt(-1);
                            }
                            else{
                                buffer.writeInt(object.%s.length);
                                for(int i = 0; i < object.%s.length; i++) {
                                    %s
                                }
                            }""".formatted(name, name, name, serializingCode.replace("\n", "\n" + indent));
                else {
                    String varName = ogName + "length" + dimension;
                    return """
                            int %varName = buffer.readInt();
                            if(%varName == -1) {
                                object.%s = null;
                            }
                            else{
                                object.%s = new %s[%varName];
                                for(int i = 0; i < %varName; i++) {
                                    %s
                                }
                            }"""
                            .replace("%varName", varName)
                            .formatted(name, name, componentType.toString(), serializingCode.replace("\n", "\n" + indent + indent));
                }
            }
        } else { // recursive case
            if (mode == MODE_SERIALIZE) {
                String varName = "i" + dimension;
                return """
                        if(object.%name == null) {
                            buffer.writeInt(-1);
                        }
                        else{
                            buffer.writeInt(object.%name.length);
                            for(int %var = 0; %var < object.%name.length; %var++) {
                                %innerloop
                            }
                        }"""
                        .replace("%var", varName)
                        .replace("%name", name)
                        .replace(
                                "%innerloop",
                                writeArray(componentType, name + "[" + varName + "]", ogName, trueType, dimension - 1, indent, mode)
                                        .replace("\n", "\n" + indent + indent));
            } else {
                String lengthName = ogName + "length" + dimension;
                String varName = ogName + "i" + dimension;
                return """
                        int %lengthName = buffer.readInt();
                        if(%lengthName == -1) {
                            object.%name = null;
                        }
                        else{
                            object.%name = new %truename[%lengthName]%arrDim;
                            for(int %var = 0; %var < %lengthName; %var++) {
                                %innerloop
                            }
                        }"""
                        .replace("%var", varName)
                        .replace("%lengthName", lengthName)
                        .replace("%name", name)
                        .replace("%truename", trueType.toString())
                        .replace("%arrDim", "[]".repeat(dimension - 1))
                        .replace(
                                "%innerloop",
                                writeArray(componentType, name + "[" + varName + "]", ogName, trueType, dimension - 1, indent, mode)
                                        .replace("\n", "\n" + indent + indent));
            }
        }
    }

    private String writeObject(TypeMirror type, String name, int mode) {
        if (isByteBufSerializable(type)) {
            if (mode == MODE_SERIALIZE)
                return "object." + name + ".writeBytes(buffer);";
            else
                return "object." + name + " = new " + type.toString() + "();\n" +
                        "object." + name + ".readBytes(buffer);";
        } else if (type.toString().equals("java.lang.String")) {
            if (mode == MODE_SERIALIZE)
                return BYTE_BUF_UTIL + ".writeStringToBuf(object." + name + ", buffer);";
            else
                return "object." + name + " = " + BYTE_BUF_UTIL + ".readStringFromBuf(buffer);";
        } else {
            if (mode == MODE_SERIALIZE)
                return "this." + getSerializerName(extractSimpleName(type.toString())).toLowerCase() + ".serialize(object." + name + ", buffer);";
            else
                return "object." + name + " = this." + getSerializerName(extractSimpleName(type.toString())).toLowerCase() + ".deserialize(buffer);";
        }
    }


    private String writePrimitive(TypeKind kind, String elementName, int mode) {
        if (mode == MODE_SERIALIZE)
            return "buffer.write" + primitiveToString(kind) + "(object." + elementName + ");";
        else
            return "object." + elementName + " = buffer.read" + primitiveToString(kind) + "();";
    }


    private String getIndentString(int indent) {
        StringBuilder builder = new StringBuilder();
        builder.append("    ".repeat(Math.max(0, indent)));
        return builder.toString();
    }

    private String primitiveToString(TypeKind kind) {
        if (kind == TypeKind.INT) {
            return "Int";
        } else if (kind == TypeKind.LONG) {
            return "Long";
        } else if (kind == TypeKind.SHORT) {
            return "Short";
        } else if (kind == TypeKind.BYTE) {
            return "Byte";
        } else if (kind == TypeKind.FLOAT) {
            return "Float";
        } else if (kind == TypeKind.DOUBLE) {
            return "Double";
        } else if (kind == TypeKind.BOOLEAN) {
            return "Boolean";
        } else if (kind == TypeKind.CHAR) {
            return "Char";
        }
        return "";
    }

    private String extractSimpleName(String fullyQualifiedName) {
        // Use the Elements utility to get the simple name
        return processingEnv.getElementUtils().getTypeElement(fullyQualifiedName).getSimpleName().toString();
    }

    private boolean isByteBufSerializable(TypeMirror typeMirror) {
        return typeUtils.isAssignable(typeMirror, byteBufSerializable);
    }

    private int count(String s, char c) {
        int count = 0;
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == c) {
                count++;
            }
        }
        return count;
    }

    private String getSerializerName(String objectClassName) {
        return objectClassName + "Serializer";
    }


    private boolean shouldSerialize(VariableElement element) {
        if(element.getAnnotation(Serialize.class) != null)
            return true;
        return element.getAnnotation(NoSerialize.class) == null
                && element.getModifiers().contains(Modifier.PUBLIC)
                && !element.getModifiers().contains(Modifier.STATIC)
                && !element.getModifiers().contains(Modifier.FINAL)
                && !element.getModifiers().contains(Modifier.TRANSIENT);
    }

    private TypeMirror getTrueType(TypeMirror type) {
        while (type.getKind() == TypeKind.ARRAY) {
            type = ((ArrayType) type).getComponentType();
        }
        return type;
    }


}
