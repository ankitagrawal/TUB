package com.hk.impl.service;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.springframework.stereotype.Service;

import com.hk.listener.AnnotationDesc;
import com.hk.pact.service.AnnotationsAPI;

/**
 * @author adlakha.vaibhav
 */
@Service
public class AnnotationsAPIImpl implements AnnotationsAPI {

  private ConcurrentHashMap<Class<?>, List<AnnotationDesc>> classAnnotationCache = new ConcurrentHashMap<Class<?>, List<AnnotationDesc>>();

    @Override
    public Set<AnnotationDesc> findAnnotationsOnClass(Class<?> clazz, final Class<?> targetAnnotationClass) {
        List<AnnotationDesc> annotations = checkAndAddToCache(clazz);
        Set<AnnotationDesc> filtered = new HashSet<AnnotationDesc>(annotations);
        CollectionUtils.filter(filtered, new Predicate() {

            public boolean evaluate(Object object) {
                AnnotationDesc annotationDesc = (AnnotationDesc) object;
                return annotationDesc.getAnnotation().annotationType().isAssignableFrom(targetAnnotationClass);
            }

        });
        return filtered;
    }

    private List<AnnotationDesc> checkAndAddToCache(Class<?> clazz) {
        if (classAnnotationCache.containsKey(clazz)) {
            return classAnnotationCache.get(clazz);
        } else {
            List<AnnotationDesc> annotations = new ArrayList<AnnotationDesc>();
            addAnnotationDesc(annotations, clazz.getAnnotations(), null, null);
            for (Field field : clazz.getDeclaredFields()) {
                addAnnotationDesc(annotations, field.getAnnotations(), field, null);
            }
            for (Method method : clazz.getDeclaredMethods()) {
                addAnnotationDesc(annotations, method.getAnnotations(), null, method);
            }
            classAnnotationCache.putIfAbsent(clazz, annotations);
            return annotations;
        }
    }

    private void addAnnotationDesc(List<AnnotationDesc> annotationList, Annotation[] annotations, Field field, Method method) {
        for (Annotation annotation : annotations) {
            annotationList.add(new AnnotationDesc(annotation, field, method));
        }
    }
}

