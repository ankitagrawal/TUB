package com.akube.framework.stripes.controller;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Author: Kani
 * Date: Jan 16, 2009
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Breadcrumb {
  int level();
  String name();
  String context() default "";
}
