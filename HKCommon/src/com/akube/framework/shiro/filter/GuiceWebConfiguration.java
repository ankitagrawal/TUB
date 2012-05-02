/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package com.akube.framework.shiro.filter;

import org.apache.shiro.web.config.IniWebConfiguration;

/**
 * <p>Shiro configuration that relies on Guice to define and initialize the Shiro SecurityManager
 * instance (and all of its dependencies) and makes it avaialble to this filter by performing a Guice injection.
 * The URL/filter behavior is still loaded according to the behavior of the parent class
 * {@link IniWebConfiguration}
 * <p/>
 *
 * <p>
 * The web.xml will need an entry like the following
 *
 *
  <filter>
    <filter-name>ShiroFilter</filter-name>
    <filter-class>com.akube.framework.shiro.filter.GuiceShiroFilter</filter-class>
    <init-param>
      <param-name>config</param-name>
      <param-value>
      </param-value>
    </init-param>
    <init-param>
      <param-name>InjectorFactoryClass</param-name>
      <param-value>app.bootstrap.guice.InjectorFactory</param-value>
    </init-param>
    <init-param>
      <param-name>InjectorFactoryMethod</param-name>
      <param-value>getInjector</param-value>
    </init-param>
  </filter>

 the injector factory class - > injector factory method is called to obtain a guice injector

 * </p>
 *
 * @author Animesh Jain
 * @see IniWebConfiguration
 * @since 0.9
 */
@SuppressWarnings("serial")
public class GuiceWebConfiguration extends IniWebConfiguration {

 /* public static final String INJECTOR_FACTORY_CLASS = "InjectorFactoryClass";
  public static final String INJECTOR_FACTORY_METHOD = "InjectorFactoryMethod";

  private static final Log log = LogFactory.getLog(GuiceWebConfiguration.class);

  protected Injector injector;

  public Injector getInjector() {
    return injector;
  }

  public void setInjector(Injector injector) {
    this.injector = injector;
  }

  public GuiceWebConfiguration() {
  }

  @Override
  public void init() throws ShiroException {
    String className = getFilterConfig().getInitParameter(INJECTOR_FACTORY_CLASS);
    String methodName = getFilterConfig().getInitParameter(INJECTOR_FACTORY_METHOD);
    System.out.println("***************  GuiceWebConfiguration init() ***************");
    System.out.println("injector class = "+className);
    System.out.println("injector method = "+methodName);
    
    Get injector from a class which holds an instance for this application. I had a static method in a class that returns the injector.
    I've put the class name and method name in filter init params.
    
    try {
      Class clazz = Class.forName(className);
      Method method = clazz.getMethod(methodName);
      Injector injector = (Injector) method.invoke(null);
      System.out.println("Injector instantiated = "+injector);
      setInjector(injector);
    } catch (ClassNotFoundException e) {
      log.error("Injector factory class not found - "+className, e);
      throw new ShiroException("Injector factory class not found - "+className, e);
    } catch (NoSuchMethodException e) {
      log.error("Injector factory method not found - "+methodName+" in class "+className, e);
      throw new ShiroException("Injector factory method not found - "+methodName+" in class "+className, e);
    } catch (InvocationTargetException e) {
      log.error("InvocationTargetException when trying to invoke - "+methodName+" in class "+className, e);
      throw new ShiroException("InvocationTargetException when trying to invoke - "+methodName+" in class "+className, e);
    } catch (IllegalAccessException e) {
      log.error("IllegalAccessException when trying to invoke - "+methodName+" in class "+className, e);
      throw new ShiroException("IllegalAccessException when trying to invoke - "+methodName+" in class "+className, e);
    }
    super.init();
  }

  @Override
  protected SecurityManager createDefaultSecurityManager() {
    return createSecurityManager(null);
  }

  @Override
  protected SecurityManager createSecurityManager(Map<String, Map<String, String>> sections) {
    return getOrCreateSecurityManager(injector, sections);
  }

  protected SecurityManager getOrCreateSecurityManager(Injector injector, Map<String, Map<String, String>> sections) {
    System.out.println("Trying to create Security Manager");
    SecurityManager securityManager = null;
    if (injector != null) {
      
      The security manager is obtained using the Guice injector.
      Typically one will have to use a custom provider and bind it to the DefaultWebSecurityManager class
      This is the way Guice handles external configuration
      
      securityManager = injector.getInstance(SecurityManager.class);
      SecurityUtils.setSecurityManager(securityManager);
    } else {
      throw new ShiroException("Injector is null. Cannot instantiate security manager");
    }

    return securityManager;
  }
*/
}
