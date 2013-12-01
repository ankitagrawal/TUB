package com.hk.listener;


import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
public class AnnotationDesc {

    private Annotation        annotation;
    private Field             field;
    private Method            method;

    public AnnotationDesc(Annotation annotation, Field field) {
        this.annotation = annotation;
        this.field = field;
    }

    public AnnotationDesc(Annotation annotation, Method method) {
        this.annotation = annotation;
        this.method = method;
    }

    public AnnotationDesc(Annotation annotation) {
        this.annotation = annotation;
    }

    public AnnotationDesc(Annotation annotation, Field field, Method method) {
        this.annotation = annotation;
        this.field = field;
        this.method = method;
    }

    /**
     * @return the annotation
     */
    public Annotation getAnnotation() {
        return annotation;
    }

    /**
     * @param annotation the annotation to set
     */
    public void setAnnotation(Annotation annotation) {
        this.annotation = annotation;
    }

    /**
     * @return the field
     */
    public Field getField() {
        return field;
    }

    /**
     * @param field the field to set
     */
    public void setField(Field field) {
        this.field = field;
    }

    /**
     * @return the method
     */
    public Method getMethod() {
        return method;
    }

    /**
     * @param method the method to set
     */
    public void setMethod(Method method) {
        this.method = method;
    }
}
