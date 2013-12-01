package com.hk.web.action.core.referral;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.*;
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
import com.hk.constants.core.RoleConstants;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.user.User;
import com.hk.manager.ReferrerProgramManager;
import com.hk.pact.dao.RoleDao;
import com.hk.pact.dao.user.UserDao;
import com.hk.web.WOMMEmailManager;
import com.hk.web.action.core.user.ActivateAccountForReferralAction;

@Secure(hasAllRoles = RoleConstants.HK_USER, authActionBean = ActivateAccountForReferralAction.class)
@Component
@HttpCache(allow = false)
public class ReferralProgramAction extends BaseAction {

    @Autowired
    WOMMEmailManager       wommEmailManager;
    @Autowired
    UserDao                userDao;
    @Autowired
    RoleDao                roleDao;
    @Autowired
    ReferrerProgramManager referrerProgramManager;

    @Validate(required = true)
    private String         senderName;

    @Validate(required = true, converter = EmailTypeConverter.class)
    private String         senderEmail;

    @Validate(required = true)
    private String         recipientEmails;

    private String         customText;

    private List<String>   recepientEmailList = new ArrayList<String>();

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
        senderName = user.getFirstName();
        senderEmail = user.getEmail();
        // Role hkUnverifiedUser = getRoleService().getRoleByName(RoleConstants.HK_UNVERIFIED);
        // if (user.getRoles().contains(hkUnverifiedUser)) {
        // return new ForwardResolution(ActivateAccountForReferralAction.class);
        // }
        return new ForwardResolution("/pages/referralProgram.jsp");
    }

    public Resolution send() {
        User user = getUserService().getUserById(getPrincipal().getId());
        Coupon coupon = referrerProgramManager.getOrCreateRefferrerCoupon(user);

        wommEmailManager.sendEmails(user, senderName, senderEmail, coupon, recepientEmailList, EnumEmailType.ReferralEmail, customText, null);
        addRedirectAlertMessage(new SimpleMessage("Referral Email sent successfully."));
        return new RedirectResolution(ReferralProgramAction.class);
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

    public String getRecipientEmails() {
        return recipientEmails;
    }

}