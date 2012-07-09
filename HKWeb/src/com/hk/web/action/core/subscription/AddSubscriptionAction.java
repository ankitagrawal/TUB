package com.hk.web.action.core.subscription;

import com.akube.framework.util.BaseUtils;
import com.hk.constants.subscription.EnumSubscriptionStatus;
import com.hk.core.fliter.SubscriptionFilter;
import com.hk.domain.builder.SubscriptionBuilder;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.matcher.SubscriptionMatcher;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.subscription.SubscriptionProduct;
import com.hk.domain.user.User;
import com.hk.domain.order.Order;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.dao.user.UserCartDao;
import com.hk.manager.UserManager;
import com.hk.manager.OrderManager;
import com.hk.pact.service.subscription.SubscriptionProductService;
import com.hk.pact.service.subscription.SubscriptionService;
import com.hk.web.action.core.user.SignupAction;
import com.hk.web.HealthkartResponse;
import com.hk.exception.OutOfStockException;
import com.akube.framework.stripes.controller.JsonHandler;
import com.akube.framework.stripes.action.BaseAction;

import java.util.*;

import net.sourceforge.stripes.validation.*;
import org.springframework.beans.factory.annotation.Autowired;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.JsonResolution;

/**
 * Created by IntelliJ IDEA.
 * User: Pradeep
 * Date: Jun 19, 2012
 * Time: 6:00:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class AddSubscriptionAction extends BaseAction implements ValidationErrorHandler {
  //private static Logger logger = Logger.getLogger(AddToCartAction.class);
  @ValidateNestedProperties({
      @Validate(field="subscriptionPeriodDays",required = true, minvalue = 180, maxvalue = 450),
      @Validate(field = "qty",required = true, minvalue = 1),
      @Validate(field = "frequencyDays",required = true,minvalue = 1),
      @Validate(field = "qtyPerDelivery",required = true, minvalue = 1),
      @Validate(field = "startDate",required = true),
      @Validate(field = "productVariant",required = true)
  })
  private Subscription subscription;

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
  @Autowired
  SubscriptionProductService subscriptionProductService;

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
    Subscription priorSubscription=null;
    try {
      if (subscription != null && subscription.getQty()>0 ) {

        priorSubscription= new SubscriptionMatcher().addProduct(subscription.getProductVariant()).match(new SubscriptionFilter(order.getSubscriptions()).addSubscriptionStatus(EnumSubscriptionStatus.InCart).filter());

        if(priorSubscription!=null) {
          SubscriptionProduct subscriptionProduct=subscriptionProductService.findByProductVariant(subscription.getProductVariant());
          SubscriptionBuilder subscriptionBuilder=new SubscriptionBuilder();
          subscriptionBuilder.forUser(user).forOrder(order).forSubscriptionProduct(subscriptionProduct);
          subscriptionBuilder.forProductVariant(subscription.getProductVariant()).forQuantity(subscription.getQty(),subscription.getQtyPerDelivery()).frequency(subscription.getFrequencyDays()).startDate(subscription.getStartDate()).subscriptionPeriod(subscription.getSubscriptionPeriodDays());

          subscriptionService.save(subscriptionBuilder.withStatus(EnumSubscriptionStatus.InCart).build());
        }
      }
    } catch (OutOfStockException e) {
      getContext().getValidationErrors().add("e2", new SimpleError(e.getMessage()));
      return new JsonResolution(getContext().getValidationErrors(), getContext().getLocale());
    }

    Map dataMap = new HashMap();
    Set<Subscription> inCartSubscriptions=new SubscriptionFilter(order.getSubscriptions()).addSubscriptionStatus(EnumSubscriptionStatus.InCart).filter();
    //null pointer here --> putting a null check
    if (subscription != null && subscription.getQty()>0) {
      if(priorSubscription!=null){
        dataMap.put("name", subscription.getProductVariant().getProduct().getName()+" subscription");
        dataMap.put("options", subscription.getProductVariant().getOptionsCommaSeparated());
        dataMap.put("qty", subscription.getQty());
        dataMap.put("itemsInCart", Long.valueOf(order.getExclusivelyProductCartLineItems().size() + order.getExclusivelyComboCartLineItems().size()) + inCartSubscriptions.size());
        HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "variant subscription was already added to your cart", dataMap);
        noCache();
        return new JsonResolution(healthkartResponse);
      }else {
        dataMap.put("name", subscription.getProductVariant().getProduct().getName()+" subscription");
        dataMap.put("options", subscription.getProductVariant().getOptionsCommaSeparated());
        dataMap.put("qty", subscription.getQty());
        dataMap.put("itemsInCart", Long.valueOf(order.getExclusivelyProductCartLineItems().size() + order.getExclusivelyComboCartLineItems().size()) + inCartSubscriptions.size());
        HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Subscription has been added to cart", dataMap);
        noCache();
        return new JsonResolution(healthkartResponse);
      }
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
