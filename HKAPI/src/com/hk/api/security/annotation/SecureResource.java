package com.hk.api.security.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 *
 * @author vaibhav.adlakha
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( {ElementType.METHOD, ElementType.TYPE})
public @interface SecureResource {

    // All tokens must be present
    String[] hasAllTokens() default {};

}
