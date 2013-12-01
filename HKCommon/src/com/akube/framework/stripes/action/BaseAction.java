package com.akube.framework.stripes.action;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.ActionBean;
import net.sourceforge.stripes.action.ActionBeanContext;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationError;
import net.sourceforge.stripes.validation.ValidationErrors;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.controller.BreadcrumbInterceptor;
import com.hk.constants.core.Keys;
import com.hk.domain.user.User;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.service.RoleService;
import com.hk.pact.service.UserService;
import com.shiro.PrincipalImpl;

/**
 * Author: Kani Date: Sep 1, 2008
 */
@Component
public class BaseAction implements ActionBean {
    private ActionBeanContext context;

    @Autowired
    private UserService       userService;

    @Autowired
    private RoleService       roleService;

    @Autowired
    private BaseDao           baseDao;

    @Value("#{hkEnvProps['" + Keys.Env.hybridRelease + "']}")
    private boolean           hybridRelease;

    public ActionBeanContext getContext() {
        return context;
    }

    public void setContext(ActionBeanContext context) {
        this.context = context;
    }

    protected void addValidationError(String key, SimpleError localizableError) {
        getContext().getValidationErrors().add(key, localizableError);
    }

    public List<ValidationError> getMessageList(ValidationErrors validationErrors) {
        List<ValidationError> messageList = new ArrayList<ValidationError>();
        Collection<List<ValidationError>> errorCol = validationErrors.values();
        for (List<ValidationError> errors : errorCol) {
            messageList.addAll(errors);
        }
        return messageList;
    }

    public void addRedirectAlertMessage(SimpleMessage message) {
        List<net.sourceforge.stripes.action.Message> messages = getContext().getMessages("generalMessages");
        messages.add(message);
    }

    public PrincipalImpl getPrincipal() {
        return (PrincipalImpl) SecurityUtils.getSubject().getPrincipal();
    }

    public User getPrincipalUser() {
        if (getPrincipal() == null)
            return null;
        return getUserService().getUserById(getPrincipal().getId());
    }

    public Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public BreadcrumbInterceptor.Crumb getPreviousBreadcrumb() {
        return BreadcrumbInterceptor.getPreviousBreadcrumb(getContext().getRequest().getSession());
    }

    public BreadcrumbInterceptor.Crumb getCurrentBreadcrumb() {
        return BreadcrumbInterceptor.getCurrentBreadcrumb(getContext().getRequest().getSession());
    }

    public void noCache() {
        HttpServletResponse response = getContext().getResponse();
        response.addHeader("Pragma", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        response.addHeader("Cache-Control", "private");
        response.addHeader("Cache-Control", "no-store");
        response.addHeader("Cache-Control", "max-age=0");
        response.addHeader("Cache-Control", "s-maxage=0");
        response.addHeader("Cache-Control", "must-revalidate");
        response.addHeader("Cache-Control", "proxy-revalidate");
    }

    protected boolean isSecureRequest() {
        return getContext().getRequest().isSecure();
    }

    protected String getRemoteHostAddr() {
        return getContext().getRequest().getRemoteHost();
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

    public RoleService getRoleService() {
        return roleService;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

    public boolean isHybridRelease() {
        return hybridRelease;
    }

}
