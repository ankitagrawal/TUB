package com.hk.web.action.core.user;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.user.User;
import com.hk.pact.service.UserService;

@Secure
@Component
public class WishlistAction extends BaseAction {

    User                user;
    @Autowired
    private UserService userService;

    public Resolution pre() {
        if (getPrincipal() != null) {
            user = getUserService().getUserById(getPrincipal().getId());
        }

        return new ForwardResolution("/pages/wishlist.jsp");
    }

    public User getUser() {
        return user;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }
    
    
}