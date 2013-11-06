package com.hk.web.action.core.email;

import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.email.EmailRecepient;
import com.hk.pact.dao.email.EmailRecepientDao;

@Component
public class UnsubscribeEmailAction extends BaseAction {
    @Autowired
    EmailRecepientDao emailRecepientDao;

    @Validate(required = true)
    private String unsubscribeToken;

    public Resolution pre() {
        EmailRecepient emailRecepient = emailRecepientDao.findByUnsubscribeToken(unsubscribeToken);
        if (emailRecepient == null) {
            addRedirectAlertMessage(new SimpleMessage("Invalid email."));
        } else {
            emailRecepient.setSubscribed(false);
            emailRecepientDao.save(emailRecepient);
            addRedirectAlertMessage(new SimpleMessage("You have been unsubscribed successfully."));
        }
        if (isHybridRelease()) {
            return new ForwardResolution("/pages/unsubscribeEmailBeta.jsp");

        }
        return new ForwardResolution("/pages/unsubscribeEmail.jsp");

    }

    public void setUnsubscribeToken(String unsubscribeToken) {
        this.unsubscribeToken = unsubscribeToken;
    }
}

