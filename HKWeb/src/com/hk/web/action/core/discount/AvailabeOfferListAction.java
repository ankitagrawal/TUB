package com.hk.web.action.core.discount;

import java.util.List;

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
import com.hk.domain.offer.OfferInstance;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.manager.OfferManager;
import com.hk.manager.OrderManager;
import com.hk.pact.dao.offer.OfferInstanceDao;
import com.hk.pact.dao.order.OrderDao;
import com.hk.web.HealthkartResponse;

@Secure
@Component
public class AvailabeOfferListAction extends BaseAction implements ValidationErrorHandler {

    /*@Autowired
    private UserService         userService;*/
    @Autowired
    OfferInstanceDao            offerInstanceDao;
    @Autowired
    OfferManager                offerManager;
    @Autowired
    OrderManager                orderManager;
    @Autowired
    OrderDao                    orderDao;

    private List<OfferInstance> offerInstanceList;

    @Validate(required = true)
    private OfferInstance       selectedOffer;

    public Resolution handleValidationErrors(ValidationErrors validationErrors) throws Exception {
        return new JsonResolution(validationErrors, getContext().getLocale());
    }

    @DontValidate
    @DefaultHandler
    public Resolution pre() {
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
        return new ForwardResolution("/pages/modal/availableOffers.jsp");
    }

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
