package com.hk.web.action.core.email;

import java.util.HashMap;

import net.sourceforge.stripes.action.FreeMarkerResolution;
import net.sourceforge.stripes.action.Resolution;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.email.EmailTemplateConstants;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.user.User;
import com.hk.manager.LinkManager;
import com.hk.manager.ReferrerProgramManager;
import com.hk.service.impl.FreeMarkerService;

@Secure
@Component
public class ReferralEmailPreviewAction extends BaseAction {

    private String            senderName;
    @Autowired
    private FreeMarkerService freeMarkerService;
    @Autowired
    LinkManager               linkManager;
    @Autowired
    ReferrerProgramManager    referrerProgramManager;

    @SuppressWarnings("unchecked")
    public Resolution pre() {
        User user = getUserService().getUserById(getPrincipal().getId());
        // User user = UserCache.getInstance().getUserById(getPrincipal().getId()).getUser();
        Coupon coupon = referrerProgramManager.getOrCreateRefferrerCoupon(user);

        String signupLink = linkManager.getReferralSignupLink(user);

        senderName = StringUtils.isBlank(senderName) ? "[your name here]" : senderName;

        HashMap valuesMap = new HashMap();
        valuesMap.put("fromName", senderName);
        valuesMap.put("fromEmail", user.getEmail());
        valuesMap.put("unsubscribeLink", "[Unsubscribe Link comes here]");
        valuesMap.put("signupLink", signupLink);
        valuesMap.put("couponCode", coupon.getCode());
        valuesMap.put("customMessage", "[Your custom message will go here]");

        return new FreeMarkerResolution(EmailTemplateConstants.referralEmail, valuesMap, freeMarkerService);
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }
}
