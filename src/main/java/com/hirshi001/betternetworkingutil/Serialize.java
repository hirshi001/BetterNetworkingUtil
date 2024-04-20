package com.hirshi001.betternetworkingutil;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Serialize {

    /*
    String serializer() default "";
    */

}
