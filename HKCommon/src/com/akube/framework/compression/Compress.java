package com.akube.framework.compression;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * writes a custom response header to the response to signal the compressionFilter to
 * compress this response.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( {ElementType.METHOD, ElementType.TYPE})
public @interface Compress {
}
