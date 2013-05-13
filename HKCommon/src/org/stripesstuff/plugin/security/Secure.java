package org.stripesstuff.plugin.security;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import net.sourceforge.stripes.action.ActionBean;

/**
 * Annotation used to secure ActionBean classes and methods with roles and permissions within the web appliction.
 * If the
 *
 * @Secure annotation is used, the default behavior is to deny access to unauthenticated users even if no roles are
 *         specified. If roles are specified, they are processed in the order of notAllowed,
 *         requiresAll, and then requiresAny. Any combination of these may be set. You must create a
 *         class that implements StripesSecurityManager and set this as your security manager in the
 *         web.xml config as shown below.
 *
 * <servlet> <servlet-name>StripesDispatcher</servlet-name>
 * <servlet-class>net.sourceforge.stripes.security.controller.DispatcherServlet</servlet-class> <init-param>
 * <param-name>SecurityManager</param-name>
 * <param-value>net.sourceforge.stripes.security.MyContainerSecurityManager</param-value>
 * </init-param> <load-on-startup>1</load-on-startup> </servlet>
 *
 * You can then add the
 * @Secure annotation to any of your action class or method declarations like so.
 *
 * @Secure( roles="MANAGE_USERS, MANAGE_CUSTOMERS" )
 *
 * @author Nic Holbrook
 */
@Retention(RetentionPolicy.RUNTIME)
@Target( {ElementType.METHOD, ElementType.TYPE})
@Documented
public @interface Secure {
  String scheme() default "http";

  // All roles must be satisified
  String[] hasAllRoles() default {};

  // Any one role must be satisified
  String[] hasAnyRoles() default {};

  // All permissions must be satisified
  String[] hasAllPermissions() default {};

  // Any one permission must be satisfied
  String[] hasAnyPermissions() default {};

  // Any one permission must be satisfied
  boolean disallowRememberMe() default false;

  // The URL to which the request will be redirected for authc
  String authUrl() default "";

  // The action bean class to which the request will be redirected for authc
  Class<? extends ActionBean> authActionBean() default ActionBean.class;

  // Should the control redirect back to the original request after authc
  // default behavior will be true if GET, false if POST
  SecureRedirect redirect() default SecureRedirect.Default;

  // specify the URL to which the control will be sent back after authc
  String redirectUrl() default "";

  // specify the bean class to which the control will be sent back after authc
  Class<? extends ActionBean> redirectActionBean() default ActionBean.class;
}
