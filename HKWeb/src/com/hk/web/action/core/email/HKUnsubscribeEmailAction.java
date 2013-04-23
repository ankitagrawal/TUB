package com.hk.web.action.core.email;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.user.EnumEmailSubscriptions;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.email.EmailRecepient;
import com.hk.domain.marketing.NotifyMe;
import com.hk.domain.user.User;
import com.hk.pact.dao.email.EmailRecepientDao;
import com.hk.pact.dao.email.NotifyMeDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.core.user.NotifyMeAction;
import net.sourceforge.stripes.action.*;
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
    @Autowired
    EmailRecepientDao emailRecepientDao;
    @Autowired
    NotifyMeDao notifyMeDao;
    @Autowired
    ProductVariantService productVariantService;

    public Resolution pre() {
        String unsubscribeToken = getContext().getRequest().getParameterValues("unsubscribeToken")[0];
        boolean success = userService.unsubscribeUser(unsubscribeToken);
        //EmailRecepient emailRecepient = emailRecepientDao.findByUnsubscribeToken(unsubscribeToken);
        if (!success) {
            addRedirectAlertMessage(new SimpleMessage("Invalid email."));
        } else {
            addRedirectAlertMessage(new SimpleMessage("We have received your request. " +
                    "We will unsubscribe you from all future emails within next 30 minutes."));
        }
        return new ForwardResolution("/pages/unsubscribeEmail.jsp");
    }

//    public Resolution subscribeUser() {
//        String emailId = getContext().getRequest().getParameterValues("email")[0];
//        boolean userSubscribed = userService.subscribeUser(emailId);
//        NotifyMe notifyMe = new NotifyMe();
//        if (!userSubscribed) {
//            emailRecepientDao.subscribeForNotify(emailId);
//            String variantId = getContext().getRequest().getParameterValues("variant")[1];
//            String name = getContext().getRequest().getParameterValues("name")[2];
//            String phone = getContext().getRequest().getParameterValues("phone")[3];
//            notifyMe.setEmail(emailId);
//            notifyMe.setName(name);
//            ProductVariant productVariant = productVariantService.getVariantById(variantId);
//            notifyMe.setProductVariant(productVariant);
//        }
//        notifyMeDao.save(notifyMe);
//        notifyMeDao.save(notifyMe);
//        HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Your request for Notification has been registered.");
//        noCache();
//        return new JsonResolution(healthkartResponse);
//    }

}
