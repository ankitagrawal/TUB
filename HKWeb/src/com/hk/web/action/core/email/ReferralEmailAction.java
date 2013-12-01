package com.hk.web.action.core.email;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.EmailTypeConverter;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.util.BaseUtils;
import com.hk.constants.core.EnumEmailType;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.user.User;
import com.hk.manager.ReferrerProgramManager;
import com.hk.pact.service.UserService;
import com.hk.web.WOMMEmailManager;

@Secure
@Component
public class ReferralEmailAction extends BaseAction {
    @Autowired
    private WOMMEmailManager       wommEmailManager;
    @Autowired
    private ReferrerProgramManager referrerProgramManager;
    @Autowired
    private UserService            userService;

    @Validate(required = true)
    private String                 senderName;

    @Validate(required = true, converter = EmailTypeConverter.class)
    private String                 senderEmail;

    @Validate(required = true)
    private String                 recipientEmails;

    private String                 customText;

    private List<String>           recepientEmailList = new ArrayList<String>();

    @ValidationMethod
    public void validateRecepientEmails() {
        for (String email : recipientEmails.split(",")) {
            email = email.trim();
            if (BaseUtils.isValidEmail(email)) {
                recepientEmailList.add(email);
            } else {
                addValidationError("emailInvalid", new LocalizableError("/ReferralEmail.action.email.invalid", email));
            }
        }

        if (recepientEmailList.size() > 50) {
            addValidationError("max50allowed", new LocalizableError("/ReferralEmail.action.email.more.than.50"));
        }
    }

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        User user = getUserService().getUserById(getPrincipal().getId());
        // User user = UserCache.getInstance().getUserById(getPrincipal().getId()).getUser();
        if (user != null) {
            senderName = user.getFirstName();
            senderEmail = user.getEmail();
        }
        return new ForwardResolution("/pages/referralEmail.jsp");
    }

    public Resolution send() {
        User user = getUserService().getUserById(getPrincipal().getId());
        // User user = UserCache.getInstance().getUserById(getPrincipal().getId()).getUser();
        Coupon coupon = referrerProgramManager.getOrCreateRefferrerCoupon(user);

        wommEmailManager.sendEmails(user, senderName, senderEmail, coupon, recepientEmailList, EnumEmailType.ReferralEmail, customText, null);
        addRedirectAlertMessage(new SimpleMessage("Referral Email sent successfully."));
        return new RedirectResolution(ReferralEmailAction.class);
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setRecipientEmails(String recipientEmails) {
        this.recipientEmails = recipientEmails;
    }

    public void setCustomText(String customText) {
        this.customText = customText;
    }

    public WOMMEmailManager getWommEmailManager() {
        return wommEmailManager;
    }

    public void setWommEmailManager(WOMMEmailManager wommEmailManager) {
        this.wommEmailManager = wommEmailManager;
    }

    public ReferrerProgramManager getReferrerProgramManager() {
        return referrerProgramManager;
    }

    public void setReferrerProgramManager(ReferrerProgramManager referrerProgramManager) {
        this.referrerProgramManager = referrerProgramManager;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}
