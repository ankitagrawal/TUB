package com.hk.web.action.core.user;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.dao.core.TempTokenDao;
import com.hk.dao.email.NotifyMeDao;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.marketing.NotifyMe;
import com.hk.domain.user.User;
import com.hk.manager.EmailManager;
import com.hk.manager.LinkManager;
import com.hk.web.HealthkartResponse;

@Component
public class NotifyMeAction extends BaseAction {

    private static Logger   logger      = LoggerFactory.getLogger(NotifyMeAction.class);

    private ProductVariant  productVariant;
    @Autowired
    TempTokenDao            tempTokenDao;
    @Autowired
    LinkManager             linkManager;
    @Autowired
    EmailManager            emailManager;
    @Autowired
    NotifyMeDao             notifyMeDao;

    
    NotifyMe                notifyMe;

    public static final int EXPIRY_DAYS = 10;

    @DontValidate
    @DefaultHandler
    public Resolution pre() {
        notifyMe = new NotifyMe();
        if (getPrincipal() != null) {
            User user = getUserService().getUserById(getPrincipal().getId());
            if (user != null && user.getEmail() != null) {
                if (user.getEmail() != null) {
                    notifyMe.setEmail(user.getEmail());
                }
                if (user.getName() != null && !user.getName().equals("Guest")) {
                    notifyMe.setName(user.getName());
                }
                if (user.getAddresses() != null && !user.getAddresses().isEmpty() && user.getAddresses().get(0).getPhone() != null) {
                    notifyMe.setPhone(user.getAddresses().get(0).getPhone());
                }
            }
        }
        notifyMe.setProductVariant(productVariant);
        return new ForwardResolution("/pages/modal/notifyMe.jsp");
    }

    @JsonHandler
    public Resolution notifyMe() {
        logger.debug("notifyMe: " + notifyMe);
        Map dataMap = new HashMap();
        HealthkartResponse healthkartResponse = null;
        if (StringUtils.isBlank(notifyMe.getPhone()) || StringUtils.isBlank(notifyMe.getEmail()) || StringUtils.isBlank(notifyMe.getName())) {
            healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Please enter values in all the mandatory fields.", dataMap);
            noCache();
            return new JsonResolution(healthkartResponse);
        }
        List<NotifyMe> notifyMeList = notifyMeDao.getPendingNotifyMeList();
        if (notifyMeList != null) {
            for (NotifyMe notifyMeObject : notifyMeList) {
                if (notifyMe.getProductVariant().equals(notifyMeObject.getProductVariant()) && notifyMe.getEmail().equals(notifyMeObject.getEmail())) {
                    addRedirectAlertMessage(new SimpleMessage("We have already received your request for this product. We will get back to you very soon. Thanks for your visit."));
                    healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK,
                            "Your request for this product has already been received. We will get back to you very soon. Thanks for your visit.", dataMap);
                    return new JsonResolution(healthkartResponse);
                }
            }
        }
        notifyMeDao.save(notifyMe);
        healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Your request for Notification has been registered.", dataMap);
        noCache();
        return new JsonResolution(healthkartResponse);
        // return new RedirectResolution(HomeAction.class);
    }

    public ProductVariant getProductVariant() {
        return productVariant;
    }

    public void setProductVariant(ProductVariant productVariant) {
        this.productVariant = productVariant;
    }

    public NotifyMe getNotifyMe() {
        return notifyMe;
    }

    public void setNotifyMe(NotifyMe notifyMe) {
        this.notifyMe = notifyMe;
    }
}