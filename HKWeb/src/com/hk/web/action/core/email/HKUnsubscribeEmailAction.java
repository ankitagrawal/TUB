package com.hk.web.action.core.email;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 12/17/12
 * Time: 6:48 PM
 * To change this template use File | Settings | File Templates.
 */

import com.akube.framework.stripes.action.BaseAction;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import org.springframework.stereotype.Component;

@Component
public class HKUnsubscribeEmailAction extends BaseAction {

    public Resolution pre() {

        Boolean success = Boolean.parseBoolean(getContext().getRequest().getParameter("success"));
        if (success){
            addRedirectAlertMessage(new SimpleMessage("You have been unsubscribed successfully."));
        }else{
            addRedirectAlertMessage(new SimpleMessage("Error unsubscribing from HealthKart. Please try again"));
        }
        return new ForwardResolution("/pages/unsubscribeEmail.jsp");
    }
}
