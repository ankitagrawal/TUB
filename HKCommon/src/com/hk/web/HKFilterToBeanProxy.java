package com.hk.web;


import javax.servlet.FilterConfig;

import org.springframework.context.ApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;

import com.hk.service.ServiceLocatorFactory;



public class HKFilterToBeanProxy extends DelegatingFilterProxy {

    protected ApplicationContext getContext(FilterConfig filterConfig) {
        return ServiceLocatorFactory.getApplicationContext();
    }

}
