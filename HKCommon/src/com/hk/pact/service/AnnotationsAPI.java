package com.hk.pact.service;

import java.util.Set;

import com.hk.listener.AnnotationDesc;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
public interface AnnotationsAPI {

    public Set<AnnotationDesc> findAnnotationsOnClass(Class<?> clazz, final Class<?> targetAnnotationClass);
}
