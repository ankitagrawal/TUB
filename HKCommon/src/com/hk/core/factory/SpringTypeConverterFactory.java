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
        int servicePackageLength = aClass.getPackage().getName().length() + 1;
        StringBuffer serviceName = new StringBuffer(aClass.getName().substring(servicePackageLength));
        serviceName.setCharAt(0, Character.toLowerCase(serviceName.charAt(0)));
        TypeConverter converter = (TypeConverter) ServiceLocatorFactory.getService(serviceName.toString());
        converter.setLocale(locale);
        return converter;
    }

}
