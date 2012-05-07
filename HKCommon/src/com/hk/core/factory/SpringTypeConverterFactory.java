package com.hk.core.factory;

import java.util.Locale;

import javax.servlet.ServletContext;

import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.validation.DefaultTypeConverterFactory;
import net.sourceforge.stripes.validation.TypeConverter;

import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
public class SpringTypeConverterFactory extends DefaultTypeConverterFactory {

    public TypeConverter getInstance(Class<? extends TypeConverter> aClass, Locale locale) throws Exception {
        TypeConverter tc = super.getInstance(aClass, locale);
        /*int servicePackageLength = aClass.getPackage().getName().length() + 1;
        StringBuffer serviceName = new StringBuffer(aClass.getName().substring(servicePackageLength));
        serviceName.setCharAt(0, Character.toLowerCase(serviceName.charAt(0)));
        TypeConverter converter = (TypeConverter) ServiceLocatorFactory.getService(serviceName.toString());
        converter.setLocale(locale);*/
        
        ServletContext servletContext = StripesFilter.getConfiguration().getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
        beanFactory.autowireBean(tc);
        //beanFactory.initializeBean(context.getActionBean(), StringUtils.uncapitalize(context.getActionBean().getClass().getSimpleName()));
        
        return tc;
    }

}
