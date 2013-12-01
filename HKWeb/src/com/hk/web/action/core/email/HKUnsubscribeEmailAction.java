package com.hk.web.action.core.email;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.email.EmailRecepient;
import com.hk.domain.user.User;
import com.hk.pact.dao.email.EmailRecepientDao;
import com.hk.pact.service.UserService;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Created with IntelliJ IDEA.
 * User: marut
 * Date: 13/01/13
 * Time: 8:34 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class HKUnsubscribeEmailAction extends BaseAction {
    @Autowired
    UserService userService;

    public Resolution pre() {
        String unsubscribeToken = getContext().getRequest().getParameterValues("unsubscribeToken")[0];
        boolean success = userService.unsubscribeUser(unsubscribeToken);
        //EmailRecepient emailRecepient = emailRecepientDao.findByUnsubscribeToken(unsubscribeToken);
        if(!success) {
            addRedirectAlertMessage(new SimpleMessage("Invalid email."));
        }else{
            addRedirectAlertMessage(new SimpleMessage("We have received your request. " +
                    "We will unsubscribe you from all future emails within next 30 minutes."));
        }
        if (isHybridRelease()) {
            return new ForwardResolution("/pages/unsubscribeEmailBeta.jsp");

        }
        return new ForwardResolution("/pages/unsubscribeEmail.jsp");

    }

}
