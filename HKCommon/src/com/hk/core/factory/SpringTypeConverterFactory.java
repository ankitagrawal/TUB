package com.hk.core.factory;

import java.util.Locale;

import net.sourceforge.stripes.validation.DefaultTypeConverterFactory;
import net.sourceforge.stripes.validation.TypeConverter;

import com.hk.service.ServiceLocatorFactory;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
public class SpringTypeConverterFactory extends DefaultTypeConverterFactory {

    public TypeConverter getInstance(Class<? extends TypeConverter> aClass, Locale locale) throws Exception {
        TypeConverter converter = ServiceLocatorFactory.getService(aClass);
        converter.setLocale(locale);
        return converter;
    }

}
