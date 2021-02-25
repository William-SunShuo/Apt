package com.ss.aptlib;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)  //作用在类上
@Retention(RetentionPolicy.CLASS)//存活时间
public @interface AutoCreate {
    String value() default "";
}