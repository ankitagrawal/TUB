package org.stripesstuff.plugin.security;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ErrorResolution;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.config.BootstrapPropertyResolver;
import net.sourceforge.stripes.config.ConfigurableComponent;
import net.sourceforge.stripes.config.Configuration;
import net.sourceforge.stripes.controller.ExecutionContext;
import net.sourceforge.stripes.controller.Interceptor;
import net.sourceforge.stripes.controller.Intercepts;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.controller.StripesFilter;
import net.sourceforge.stripes.exception.StripesRuntimeException;
import net.sourceforge.stripes.util.Log;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.controller.JsonHandler;
import com.akube.framework.stripes.controller.Modal;
import com.hk.manager.LinkManager;
import com.hk.web.HealthkartResponse;

/**
 * Security interceptor for the Stripes framework. Determines if handling the event for the current execution context is
 * allowed. Execution is allowed if there is no security manager, or if the security manager allows it. See the
 * documentation of the SecurityManager interface for more information. <p/> The security manager is invoked between
 * binding&amp;validation and event handling, in a way to ensure annotations like
 * 
 * @DontValidate and
 * @DontBind work as intended.
 * @author <a href="mailto:kindop@xs4all.nl">Oscar Westra van Holthe - Kind</a>
 * @author <a href="mailto:xf2697@fastmail.fm">Fred Daoud</a>
 * @version $Id: SecurityInterceptor.java,v 1.2 2009-06-10 10:56:50 Kani Exp $
 * @see SecurityManager
 * @see SecurityHandler
 */
@Intercepts( { LifecycleStage.BindingAndValidation, LifecycleStage.CustomValidation, LifecycleStage.EventHandling, LifecycleStage.ResolutionExecution })
@Component
public class SecurityInterceptor implements Interceptor, ConfigurableComponent {
    /**
     * Key used to lookup the name of the SecurityManager class in the Stripes configuration. This class must have a
     * no-arg constructor and implement StipesSecurityManager.
     */
    public static final String          SECURITY_MANAGER_CLASS                 = "SecurityManager.Class";
    public static final String          SECURITY_MANAGER_AUTH_URL              = "SecurityManager.AuthUrl";
    public static final String          SECURITY_MANAGER_AUTH_BEAN_CLASS       = "SecurityManager.AuthBeanClass";
    public static final String          SECURITY_MANAGER_MODAL_AUTH_BEAN_CLASS = "SecurityManager.modal.AuthBeanClass";
    /**
     * Key used to store the security manager in the request before processing resolutions.
     */
    public static final String          SECURITY_MANAGER                       = java.lang.SecurityManager.class.getName();
    /**
     * Logger for this class.
     */
    private static final Log            LOG                                    = Log.getInstance(SecurityInterceptor.class);
    /**
     * The configured security manager.
     */
    private SecurityManager             securityManager;
    private String                      securityAuthUrl;
    private Class<? extends ActionBean> authActionBeanClazz;
    private Class<? extends ActionBean> modalAuthActionBeanClazz;

    /**
     * Initialize the interceptor.
     * 
     * @param configuration the configuration being used by Stripes
     * @throws StripesRuntimeException if the security manager cannot be created
     */
    public void init(Configuration configuration) throws StripesRuntimeException {
        BootstrapPropertyResolver resolver = configuration.getBootstrapPropertyResolver();

        // Instantiate the security manager.

        try {
            // Ask the BootstrapPropertyResolver for a subclass of SecurityManager.
            // BootstrapPropertyResolver will look in web.xml first then scan the
            // classpath if the class wasn't specified in web.xml
            securityAuthUrl = resolver.getFilterConfig().getInitParameter(SECURITY_MANAGER_AUTH_URL);
            authActionBeanClazz = resolver.getClassProperty(SECURITY_MANAGER_AUTH_BEAN_CLASS, ActionBean.class);
            modalAuthActionBeanClazz = resolver.getClassProperty(SECURITY_MANAGER_MODAL_AUTH_BEAN_CLASS, ActionBean.class);

            Class<? extends SecurityManager> clazz = resolver.getClassProperty(SECURITY_MANAGER_CLASS, SecurityManager.class);

            if (clazz != null)
                securityManager = (SecurityManager) clazz.newInstance();
        } catch (Exception e) {
            String msg = "Failed to configure the SecurityManager: instantiation failed.";
            throw new StripesRuntimeException(msg, e);
        }

        if (securityManager != null) {
            LOG.info("Initialized the SecurityInterceptor with the SecurityManager: " + securityManager.toString());
        } else {
            LOG.info("Initialized the SecurityInterceptor without a SecurityManager (all access will be allowed).");
        }
    }

    /**
     * Intercept execution.
     * 
     * @param executionContext the context of the execution being intercepted
     * @return the resulting {@link Resolution}; returns {@link ExecutionContext#proceed()} if all is well
     * @throws Exception on error
     */
    public Resolution intercept(ExecutionContext executionContext) throws Exception {
        Resolution resolution;
        

        if (securityManager != null) {
            switch (executionContext.getLifecycleStage()) {
                case BindingAndValidation:
                    resolution = interceptBindingAndValidation(executionContext);
                    break;
                case CustomValidation:
                    resolution = interceptCustomValidation(executionContext);
                    break;
                case EventHandling:
                    resolution = interceptEventHandling(executionContext);
                    break;
                case ResolutionExecution:
                    resolution = interceptResolutionExecution(executionContext);
                    break;
                default: // Should not happen (see @Intercepts annotation on class)
                    resolution = executionContext.proceed();
                    break;
            }
        } else {
            // There is no security manager, so everything is allowed.

            resolution = executionContext.proceed();
        }

        return resolution;
    }

    /**
     * Intercept execution for binding and/or (custom) validations. Checks that the security doesn't deny access before
     * any error messages are shown.
     * 
     * @param executionContext the context of the execution being intercepted
     * @return the resulting {@link net.sourceforge.stripes.action.Resolution}; returns
     *         {@link ExecutionContext#proceed()} if all is well
     * @throws Exception on error
     */
    protected Resolution interceptBindingAndValidation(ExecutionContext executionContext) throws Exception {
        Resolution resolution = executionContext.proceed();

        // We're handling binding and/or validation. If an error occured, check if access is allowed.
        // If explicitly denied, access is denied (as showing errors would both be pointless and an information leak).

        if (resolution != null && Boolean.FALSE.equals(getAccessAllowed(executionContext))) {
            // If the security manager denies access, deny access.

            LOG.debug("Binding and/or validation failed, and the security manager has denied access.");
            resolution = handleAccessDenied(executionContext.getActionBean(), executionContext.getHandler(), executionContext);
        }

        // Return the result.

        return resolution;
    }

    /**
     * Intercept execution for custom validations. We have made this separate because custom validations may also depend
     * on the existing 'subject' for busines specific validations. So security logic must be invoked before custom
     * validator is called <p/> This will probably create problems if we have method level Secure annotations as well.
     * <p/> As of now the logic was such that method level annotations could override class level annotations. Now
     * method level annotations have been overrided by class level annotations. <p/> Basically custom validator will
     * only pass if the class level Secure annotations pass. Otherwise the user will be taken back to the login screen
     * <p/> Not sure if this is the right thing to do or not.
     * 
     * @param executionContext the context of the execution being intercepted
     * @return the resulting {@link net.sourceforge.stripes.action.Resolution}; returns
     *         {@link ExecutionContext#proceed()} if all is well
     * @throws Exception on error
     */
    protected Resolution interceptCustomValidation(ExecutionContext executionContext) throws Exception {
        Resolution resolution = null;

        // We're handling binding and/or validation. If an error occured, check if access is allowed.
        // If explicitly denied, access is denied (as showing errors would both be pointless and an information leak).

        if (Boolean.TRUE.equals(getAccessAllowed(executionContext))) {
            resolution = executionContext.proceed();
        } else {
            // If the security manager denies access, deny access.
            LOG.debug("Binding and/or validation failed, and the security manager has denied access.");
            resolution = handleAccessDenied(executionContext.getActionBean(), executionContext.getHandler(), executionContext);
        }

        // Return the result.

        return resolution;
    }

    /**
     * Intercept execution for event handling. Checks if the security manager allows access before allowing the event.
     * 
     * @param executionContext the context of the execution being intercepted
     * @return the resulting {@link net.sourceforge.stripes.action.Resolution}; returns
     *         {@link ExecutionContext#proceed()} if all is well
     * @throws Exception on error
     */
    protected Resolution interceptEventHandling(ExecutionContext executionContext) throws Exception {
        // Before handling the event, check if access is allowed.
        // If not explicitly allowed, access is denied.

        Resolution resolution;

        if (Boolean.TRUE.equals(getAccessAllowed(executionContext))) {
            resolution = executionContext.proceed();
        } else {
            LOG.debug("The security manager has denied access.");
            resolution = handleAccessDenied(executionContext.getActionBean(), executionContext.getHandler(), executionContext);
        }

        return resolution;
    }

    /**
     * Intercept execution for resolution execution. Adds the security manager to the request attributes, which is used
     * to give security tags access to the security manager.
     * 
     * @param executionContext the context of the execution being intercepted
     * @return the resulting {@link net.sourceforge.stripes.action.Resolution}; returns
     *         {@link ExecutionContext#proceed()} if all is well
     * @throws Exception on error
     */
    protected Resolution interceptResolutionExecution(ExecutionContext executionContext) throws Exception {
        // Before processing the resolution, add the security manager to the request.
        // This is used (for example) by the security tag.

        executionContext.getActionBeanContext().getRequest().setAttribute(SecurityInterceptor.SECURITY_MANAGER, securityManager);

        return executionContext.proceed();
    }

    /**
     * Determine if the security manager allows access. The return value of this method is the same as the result of
     * {@link SecurityManager#getAccessAllowed(ActionBean,Method) getAccessAllowed(ActionBean, Method)} of the current
     * security manager, unless there is nu security manager (in which case the event is allowed).
     * 
     * @param executionContext the current execution context
     * @return whether or not the security manager allows access, if a decision can be made
     */
    protected Boolean getAccessAllowed(ExecutionContext executionContext) {
        LOG.debug("Checking access for " + executionContext + " at " + executionContext.getLifecycleStage());

        Boolean accessAllowed;
        if (securityManager == null) {
            LOG.debug("There is no security manager, so access is allowed by default.");
            accessAllowed = true;
        } else {
            ActionBean actionBean = executionContext.getActionBean();
            Method handler = executionContext.getHandler();
            accessAllowed = securityManager.getAccessAllowed(actionBean, handler);
            LOG.debug("Security manager returned access allowed: " + accessAllowed);
        }

        return accessAllowed;
    }

    /**
     * Determine what to do when access has been denied. If the SecurityManager implements the optional interface [@Link
     * SecurityHandler}, ask the SecurityManager. Otherwise, return the HTTP error "forbidden". <p/> If any method is
     * annotated with
     * 
     * @link JsonHandler then instead of returning the normal http resolution, will return JavaScriptResolution.
     * @param bean the action bean to which access was denied
     * @param handler the event handler to which access was denied
     * @param executionContext the execution context
     * @return the Resolution to be executed when access has been denied
     */
    protected Resolution handleAccessDenied(ActionBean bean, Method handler, ExecutionContext executionContext) {
        Secure secure = null;
        if (handler.isAnnotationPresent(Secure.class)) {
            secure = handler.getAnnotation(Secure.class);
        }
        if (secure == null && bean.getClass().isAnnotationPresent(Secure.class)) {
            secure = bean.getClass().getAnnotation(Secure.class);
        }

        if (handler.isAnnotationPresent(JsonHandler.class)) {
            HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ACCESS_DENIED, "loggedOut", new JsonAuthRedirect(
                    linkManager.getAuthRedirectUrl()));
            return new JsonResolution(healthkartResponse);
        }

        Resolution resolution = null;
        String authUrl = null;

        // first check if an authUrl has been specified
        if (!StringUtils.isBlank(secure.authUrl())) {
            authUrl = secure.authUrl();
        }
        // check is authActionBean is specified
        if (authUrl == null && ActionBean.class != secure.authActionBean()) {
            authUrl = StripesFilter.getConfiguration().getActionResolver().getUrlBinding(secure.authActionBean());
        }
        // fall back on defaults from web.xml
        if (authUrl == null && !StringUtils.isBlank(securityAuthUrl)) {
            authUrl = securityAuthUrl;
        }
        if (authUrl == null && authActionBeanClazz != null) {
            authUrl = StripesFilter.getConfiguration().getActionResolver().getUrlBinding(authActionBeanClazz);
        }

        if ((bean.getClass().isAnnotationPresent(Modal.class) || handler.isAnnotationPresent(Modal.class)) && authUrl == null && modalAuthActionBeanClazz != null) {
            authUrl = StripesFilter.getConfiguration().getActionResolver().getUrlBinding(modalAuthActionBeanClazz);
        }

        if (authUrl != null) {
            String redirectUrl = null;

            if (!StringUtils.isBlank(secure.redirectUrl())) {
                redirectUrl = secure.redirectUrl();
            }

            if (redirectUrl == null && ActionBean.class != secure.redirectActionBean()) {
                redirectUrl = StripesFilter.getConfiguration().getActionResolver().getUrlBinding(secure.redirectActionBean());
            }

            if (redirectUrl == null) {
                // see if the request was a GET. is so then redirect back to this page.
                // if the request was a POST, then do not redirect
                HttpServletRequest request = executionContext.getActionBeanContext().getRequest();
                String method = request.getMethod();
                if ("GET".equalsIgnoreCase(method)) {
                    redirectUrl = request.getRequestURL().toString();
                    if (!StringUtils.isBlank(request.getQueryString())) {
                        redirectUrl = redirectUrl + "?" + request.getQueryString();
                    }
                }
              if (redirectUrl != null && !redirectUrl.startsWith(secure.scheme()+"://")) {
                if (redirectUrl.startsWith("https"))
                  redirectUrl = redirectUrl.replaceAll("https", "http");
                else if (redirectUrl.startsWith("http"))
                  redirectUrl = redirectUrl.replaceAll("http", "https");
              }
            }

            if (redirectUrl != null && secure.redirect() != SecureRedirect.False) {
                if (authUrl.contains("?")) {
                    authUrl = authUrl + "&" + getRedirectQueryString(redirectUrl);
                } else {
                    authUrl = authUrl + "?" + getRedirectQueryString(redirectUrl);
                }
            }
            resolution = new RedirectResolution(authUrl);
        }

        if (resolution == null) {
            resolution = new ErrorResolution(HttpServletResponse.SC_UNAUTHORIZED);
        }
        return resolution;
    }

    private String getRedirectQueryString(String redirectUrl) {
        String qs = "";
        try {
            qs = J2EESecurityManager.redirectAfterLoginParam + "=" + URLEncoder.encode(redirectUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        return qs;
    }

    @Autowired
    LinkManager linkManager;

    private class JsonAuthRedirect {
        private String authUrl;

        private JsonAuthRedirect(String authUrl) {
            this.authUrl = authUrl;
        }

        public String getAuthUrl() {
            return authUrl;
        }

        public void setAuthUrl(String authUrl) {
            this.authUrl = authUrl;
        }
    }

    public LinkManager getLinkManager() {
        return linkManager;
    }

    public void setLinkManager(LinkManager linkManager) {
        this.linkManager = linkManager;
    }

}
