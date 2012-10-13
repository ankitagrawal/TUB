package com.hk.rest.mobile.service.action;

import org.stripesstuff.plugin.security.Secure;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.pact.service.UserService;
import com.hk.pact.dao.offer.OfferInstanceDao;
import com.hk.pact.dao.order.OrderDao;
import com.hk.manager.OfferManager;
import com.hk.manager.OrderManager;
import com.hk.domain.offer.OfferInstance;
import com.hk.domain.user.User;
import com.hk.domain.order.Order;
import com.hk.web.HealthkartResponse;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.action.*;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Satish
 * Date: Oct 12, 2012
 * Time: 7:31:33 AM
 * To change this template use File | Settings | File Templates.
 */

import java.util.List;
import java.util.Locale;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.validation.ValidationErrors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.akube.framework.gson.JsonUtils;
import com.hk.domain.offer.OfferInstance;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.manager.OfferManager;
import com.hk.manager.OrderManager;
import com.hk.pact.dao.offer.OfferInstanceDao;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.service.UserService;
import com.hk.web.HealthkartResponse;
import com.hk.rest.mobile.service.utils.MHKConstants;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Context;

@Secure
@Component
public class MOfferAction extends MBaseAction{

    @Autowired
    private UserService userService;
    @Autowired
    OfferInstanceDao offerInstanceDao;
    @Autowired
    OfferManager offerManager;
    @Autowired
    OrderManager orderManager;
    @Autowired
    OrderDao orderDao;

    private List<OfferInstance> offerInstanceList;

    @Validate(required = true)
    private OfferInstance       selectedOffer;


    @DontValidate
    @DefaultHandler
    public String availableOffers(@Context HttpServletResponse response) {
        HealthkartResponse healthkartResponse;
        String jsonBuilder = "";
        String message = MHKConstants.STATUS_DONE;
        String status = MHKConstants.STATUS_OK;

        User user = getUserService().getUserById(getPrincipal().getId());
        Order order = orderManager.getOrCreateOrder(user);

        selectedOffer = order.getOfferInstance();
        offerInstanceList = offerInstanceDao.getActiveOffers(user);
        for (OfferInstance offerInstance : offerInstanceList) {
            if (offerInstance.equals(selectedOffer)) {
                order.setOfferInstance(offerInstance);
                orderDao.save(order);
            }
        }
    healthkartResponse = new HealthkartResponse(status, message, offerInstanceList);
    jsonBuilder = JsonUtils.getGsonDefault().toJson(healthkartResponse);

    addHeaderAttributes(response);

    return jsonBuilder;
    }

/*
    @JsonHandler
    public Resolution applyOffer() {
        User user = getUserService().getUserById(getPrincipal().getId());
        Order order = orderManager.getOrCreateOrder(user);

        if (!selectedOffer.getUser().equals(user)) {
            HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, new LocalizableMessage("/AvailabeOfferList.action.invalid.offer"),
                    getContext().getLocale());
            return new JsonResolution(healthkartResponse);
        }

        order.setOfferInstance(selectedOffer);
        orderDao.save(order);

        return new JsonResolution(new HealthkartResponse(HealthkartResponse.STATUS_OK, "Offer applied"));
    }

    @JsonHandler
    public Resolution removeAppliedOffer() {
        User user = getUserService().getUserById(getPrincipal().getId());
        Order order = orderManager.getOrCreateOrder(user);
        order.setOfferInstance(null);
        orderDao.save(order);

        return new JsonResolution(new HealthkartResponse(HealthkartResponse.STATUS_OK, "Offer removed"));
    }
*/

    public List<OfferInstance> getOfferInstanceList() {
        return offerInstanceList;
    }

    public void setOfferInstanceList(List<OfferInstance> offerInstanceList) {
        this.offerInstanceList = offerInstanceList;
    }

    public OfferInstance getSelectedOffer() {
        return selectedOffer;
    }

    public void setSelectedOffer(OfferInstance selectedOffer) {
        this.selectedOffer = selectedOffer;
    }
}
