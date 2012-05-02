package web.action.core.user;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.EmailTypeConverter;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.manager.EmailManager;
import com.hk.manager.UserManager;
import com.hk.service.RoleService;
import com.hk.service.UserService;
import com.hk.web.HealthkartResponse;

@Component
public class ResendAccountActivationLinkAction extends BaseAction {

    private UserManager  userManager;
    private EmailManager emailManager;
    private UserService  userService;
    private RoleService  roleService;

    @Validate(required = true, on = "activateNonLoggedInUser", converter = EmailTypeConverter.class)
    private String       email;

    @JsonHandler
    @Secure
    public Resolution pre() {
        User user = getUserService().getUserById(getPrincipal().getId());
        Role roleDeactivated = getRoleService().getRoleByName(RoleConstants.HK_DEACTIVATED);
        Role roleUnverified = getRoleService().getRoleByName(RoleConstants.HK_UNVERIFIED);
        String message = "";
        if (user.getRoles().contains(roleUnverified) || user.getRoles().contains(roleDeactivated)) {
            String activationLink = userManager.getUserActivationLink(user);
            emailManager.sendAccountActivationEmail(user, activationLink);
            message = "Email has been sent to you at " + user.getEmail();
        } else {
            message = "You have already activated your account.";
        }
        Map<String, Object> data = new HashMap<String, Object>(1);
        data.put("message", message);
        HealthkartResponse hkResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "link send", data);
        return new JsonResolution(hkResponse);
    }

    @Secure
    public Resolution resendActivationEmail() {
        User user = getUserService().getUserById(getPrincipal().getId());
        Role roleDeactivated = getRoleService().getRoleByName(RoleConstants.HK_DEACTIVATED);
        Role roleUnverified = getRoleService().getRoleByName(RoleConstants.HK_UNVERIFIED);
        String message = "";
        if (user.getRoles().contains(roleUnverified) || user.getRoles().contains(roleDeactivated)) {
            String activationLink = userManager.getUserActivationLink(user);
            emailManager.sendAccountActivationEmail(user, activationLink);
            message = "email has been sent to you at " + user.getEmail();
        } else {
            message = "you have already activated your account.";
        }
        Map<String, Object> data = new HashMap<String, Object>(1);
        data.put("message", message);
        HealthkartResponse hkResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "link send", data);
        return new JsonResolution(hkResponse);
    }

    @JsonHandler
    public Resolution activateNonLoggedInUser() {
        User user = getUserService().findByLogin(email);
        HealthkartResponse healthkartResponse = null;
        String message = "";
        Role roleItvUser = getRoleService().getRoleByName(RoleConstants.HK_USER);
        if (user == null) {
            message = "email invalid";
        } else if (user.getRoles().contains(roleItvUser)) {
            message = "you have already activated your account.";
        } else {
            String activationLink = userManager.getUserActivationLink(user);
            emailManager.sendAccountActivationEmail(user, activationLink);
            message = "email has been sent to you at " + user.getEmail();
        }
        Map<String, Object> data = new HashMap<String, Object>(1);
        data.put("message", message);
        healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "link send", data);
        return new JsonResolution(healthkartResponse);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }

    public RoleService getRoleService() {
        return roleService;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

}