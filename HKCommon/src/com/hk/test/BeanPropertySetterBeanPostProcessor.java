package com.hk.test;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * test class only not to be used as of now.
 * 
 * @author vaibhav.adlakha
 */
@SuppressWarnings("unchecked")
public class BeanPropertySetterBeanPostProcessor
// implements BeanFactoryPostProcessor, BeanPostProcessor
{
    private ConfigurableListableBeanFactory factory = null;

    private Map                             config  = null;

    public void setConfig(Map config) {
        this.config = config;
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        // log.debug("before: "+bean+", "+beanName);
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        // log.debug("after: "+bean+", "+beanName);
        // Now set the dependency
        if (config == null)
            return bean;
        Map beanConfig = (Map) config.get(beanName);
        if (beanConfig != null) {
            BeanInfo beanInfo = null;
            try {
                beanInfo = Introspector.getBeanInfo(bean.getClass());
                PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
                if (propertyDescriptors != null) {
                    for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
                        String propertyName = propertyDescriptor.getName();
                        if (beanConfig.get(propertyName) != null) {
                            String beanId = (String) beanConfig.get(propertyName);
                            /*
                             * if (log.isDebugEnabled()) { log.debug("Found propertyDescriptor for " + beanName + ": " +
                             * propertyName); }
                             */
                            try {
                                Object targetBean = factory.getBean(beanId);
                                Method setter = propertyDescriptor.getWriteMethod();
                                setter.invoke(bean, targetBean);
                            } catch (NoSuchBeanDefinitionException nsbde) {
                                // log.error("Bean not found", nsbde);
                                nsbde.printStackTrace();
                            } catch (IllegalAccessException e) {
                                // /log.error("IllegalAccessException", e);
                                e.printStackTrace();
                            } catch (InvocationTargetException e) {
                                // log.error("InvocationTargetException", e);
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } catch (java.beans.IntrospectionException e) {
                e.printStackTrace();
            }
        }
        return bean;
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException {
        this.factory = factory;
    }

}
