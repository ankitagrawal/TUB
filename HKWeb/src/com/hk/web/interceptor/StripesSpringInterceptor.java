package com.hk.web.interceptor;

import javax.servlet.ServletContext;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.controller.StripesFilter;

import org.apache.commons.lang.StringUtils;
import org.hibernate.FlushMode;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.hk.service.ServiceLocatorFactory;

/**
 * 
 * @author vaibhav.adlakha
 *
 */
@Intercepts(LifecycleStage.ActionBeanResolution)
//@Intercepts(LifecycleStage.ResolutionExecution)
public class StripesSpringInterceptor implements Interceptor
{
 
    public Resolution intercept(ExecutionContext context) throws Exception
    {
        Resolution resolution = context.proceed();
        //System.out.println("Running Spring dependency injection for instance of " + context.getActionBean().getClass().getSimpleName());
        ServletContext servletContext = StripesFilter.getConfiguration().getServletContext();
        ApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);
        AutowireCapableBeanFactory beanFactory = applicationContext.getAutowireCapableBeanFactory();
        beanFactory.autowireBeanProperties(context.getActionBean(), AutowireCapableBeanFactory.AUTOWIRE_NO, false);
        beanFactory.initializeBean(context.getActionBean(), StringUtils.uncapitalize(context.getActionBean().getClass().getSimpleName()));
        
        ((SessionFactory) ServiceLocatorFactory.getService("SessionFactory")).getCurrentSession().setFlushMode(FlushMode.MANUAL);
        
        
        //System.out.println("completed Spring dependency injection for instance of " + context.getActionBean().getClass().getSimpleName());
        return resolution;
    }
}