package com.hk.web.action.core.subscription;

import com.akube.framework.util.BaseUtils;
import com.hk.constants.subscription.EnumSubscriptionStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.user.User;
import com.hk.domain.order.Order;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.dao.user.UserCartDao;
import com.hk.manager.UserManager;
import com.hk.manager.OrderManager;
import com.hk.pact.service.subscription.SubscriptionService;
import com.hk.web.action.core.user.SignupAction;
import com.hk.web.HealthkartResponse;
import com.hk.exception.OutOfStockException;
import com.akube.framework.stripes.controller.JsonHandler;
import com.akube.framework.stripes.action.BaseAction;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationErrorHandler;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: Jun 19, 2012
 * Time: 6:00:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddSubscriptionAction extends BaseAction implements ValidationErrorHandler {
   //private static Logger logger = Logger.getLogger(AddToCartAction.class);

    Subscription subscription;

    @Autowired
    UserDao userDao;
    @Autowired
    UserManager userManager;
    @Autowired
    OrderManager orderManager;
    @Autowired
    UserCartDao userCartDao;

    @Autowired
    SignupAction signupAction;

    @Autowired
    SubscriptionService subscriptionService;

    @SuppressWarnings({"unchecked", "deprecation"})
    @DefaultHandler
    @JsonHandler
    public Resolution addSubsription() {
        User user = null;
        if (getPrincipal() != null) {
            user = userDao.getUserById(getPrincipal().getId());
            if (user == null) {
                user = userManager.createAndLoginAsGuestUser(null, null);
            }
        } else {
            user = userManager.createAndLoginAsGuestUser(null, null);
        }

        Order order = orderManager.getOrCreateOrder(user);
        List<ProductVariant> selectedProductVariants = new ArrayList<ProductVariant>();
        try {
            if (subscription != null && subscription.getQty()>0 ) {
                subscription.setOrder(order);
                subscription.setUser(user);
                subscription.setSubscriptionStatus(EnumSubscriptionStatus.InCart.asSubscriptionStatus());
                subscription.setCreateDate(BaseUtils.getCurrentTimestamp());
                subscription.setSubscriptionPrice(subscription.getProductVariant().getHkPrice());
                subscription.setCostPriceAtSubscription(subscription.getProductVariant().getCostPrice());
                subscription.setMarkedPriceAtSubscription(subscription.getProductVariant().getMarkedPrice());
                subscription.setHkDiscountAtSubscription(subscription.getProductVariant().getDiscountPercent());
                subscription.setSubscriptionDiscountPercent(1.5);
                subscription.setHkPriceAtSubscription(subscription.getProductVariant().getHkPrice());
                subscriptionService.save(subscription);
            }
        } catch (OutOfStockException e) {
            getContext().getValidationErrors().add("e2", new SimpleError(e.getMessage()));
            return new JsonResolution(getContext().getValidationErrors(), getContext().getLocale());
        }

        Map dataMap = new HashMap();
        //null pointer here --> putting a null check
        if (subscription != null && subscription.getQty()>0) {
            dataMap.put("name", subscription.getProductVariant().getProduct().getName()+" subscription");
            dataMap.put("options", subscription.getProductVariant().getOptionsCommaSeparated());
            dataMap.put("qty", subscription.getQty());
            dataMap.put("itemsInCart", Long.valueOf(order.getExclusivelyProductCartLineItems().size() + order.getExclusivelyComboCartLineItems().size()) + 1L);
            HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Subscription has been added to cart", dataMap);
            noCache();
            return new JsonResolution(healthkartResponse);
        }
        HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Subscription has not been added to cart", dataMap);
        noCache();
        return new JsonResolution(healthkartResponse);
    }

    public Subscription getSubscription() {
        return subscription;
    }

    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public Resolution handleValidationErrors(ValidationErrors validationErrors) throws Exception {
        return new JsonResolution(validationErrors, getContext().getLocale());
    }

}
