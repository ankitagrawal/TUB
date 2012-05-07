package com.hk.web.action.core.referral;

import javax.servlet.http.Cookie;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.util.CryptoUtil;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.HealthkartConstants;
import com.hk.domain.user.User;
import com.hk.pact.dao.user.UserDao;

@UrlBinding("/action/invite/{userHash}")
@Component
public class ReferralSignupAction extends BaseAction {

    @Validate(required = true)
    private String userHash;

    private User   user;
    @Autowired
    UserDao        userDao;

    @ValidationMethod
    public void validate() {
        user = userDao.findByUserHash(userHash);
        if (user == null) {
            getContext().getValidationErrors().add("invalidHash", new SimpleError("Invalid user."));
        }
    }

    public Resolution pre() {
        Cookie referredByCookie = new Cookie(HealthkartConstants.Cookie.referred_by, CryptoUtil.encrypt(user.getUserHash()));
        referredByCookie.setMaxAge(15 * 24 * 3600); // 15 days
        referredByCookie.setPath("/");
        getContext().getResponse().addCookie(referredByCookie);

        return new ForwardResolution("/pages/referralSignup.jsp");
    }

    public void setUserHash(String userHash) {
        this.userHash = userHash;
    }

    public User getUser() {
        return user;
    }
}
