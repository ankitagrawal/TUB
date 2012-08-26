package com.hk.web.action.core.order;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.constants.core.HealthkartConstants;
import com.hk.constants.core.Keys;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.courier.Courier;
import com.hk.domain.offer.OfferInstance;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.dto.pricing.PricingDto;
import com.hk.manager.OrderManager;
import com.hk.manager.ReferrerProgramManager;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.dao.payment.PaymentModeDao;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.service.order.OrderService;
import com.hk.pricing.PricingEngine;
import com.hk.web.action.core.cart.CartAction;
import com.hk.web.action.core.payment.PaymentModeAction;
import com.hk.web.action.core.user.SelectAddressAction;
import net.sourceforge.stripes.action.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;
import org.stripesstuff.plugin.session.Session;

import java.util.List;
import java.util.Set;

@Secure
@Component
public class OrderSummaryAction extends BaseAction {

    private static Logger logger = LoggerFactory.getLogger(OrderSummaryAction.class);

    @Autowired
    private CourierService courierService;
    @Autowired
    UserDao userDao;
    @Autowired
    OrderManager orderManager;
    @Autowired
    private OrderService orderService;
    @Autowired
    PricingEngine pricingEngine;
    @Autowired
    ReferrerProgramManager referrerProgramManager;     

    @Session(key = HealthkartConstants.Session.useRewardPoints)
    private boolean useRewardPoints;

    private PricingDto pricingDto;
    private Order order;
    private Address billingAddress;
    private boolean codAllowed;
    private Double redeemableRewardPoints;
    private List<Courier> availableCourierList;
    private boolean  groundShippingAllowed;
    private boolean  hideCod;
    private Double  cashbackOnGroundshipped;
    private Double  groundshipItemweight;
    private Double  groundshipItemAmount;

    // COD related changes
    @Autowired
    PaymentManager paymentManager;
    @Autowired
    PaymentModeDao paymentModeDao;

    @Value("#{hkEnvProps['" + Keys.Env.codCharges + "']}")
    private Double codCharges;

    @Value("#{hkEnvProps['" + Keys.Env.codFreeAfter + "']}")
    private Double codFreeAfter;

    @Value("#{hkEnvProps['" + Keys.Env.codMinAmount + "']}")
    private Double codMinAmount;

    @Value("#{hkEnvProps['" + Keys.Env.cashBackPercentageOnGroundShipped + "']}")
    private Double cashBackPercentageOnGroundShipped;
    
    // @Named(Keys.Env.codMaxAmount)
    @Value("#{hkEnvProps['codMaxAmount']}")
    private Double codMaxAmount;
    


    @DefaultHandler
    public Resolution pre() {          
        User user = getUserService().getUserById(getPrincipal().getId());
        order = orderManager.getOrCreateOrder(user);
        // Trimming empty line items once again.
        orderManager.trimEmptyLineItems(order);
        OfferInstance offerInstance = order.getOfferInstance();

//        Set<CartLineItem> cartLineItems = order.getCartLineItems();
//        for (CartLineItem lineItem : cartLineItems) {
//            if (lineItem != null && lineItem.getProductVariant() != null) {
//                ProductVariant productVariant = lineItem.getProductVariant();
//                if (productVariant.getProduct().isGroundShipping()) {
//                    groundshipItemAmount =  productVariant.getHkPrice() ;
//                     if (groundshipItemAmount == null ) {
//                        groundshipItemAmount = 0.0;
//                    }
//                    groundshipItemweight = productVariant.getWeight();
//                    if (groundshipItemweight == null || groundshipItemweight == 0.0) {
//                        groundshipItemweight = 125D;
//                    }
//                    hideCod = true;
//                    break;
//                }
//            }
//        }


        Double rewardPointsUsed = 0D;
        redeemableRewardPoints = referrerProgramManager.getTotalRedeemablePoints(user);
        if (useRewardPoints)
            rewardPointsUsed = redeemableRewardPoints;
        if (order.getAddress() == null) {
            return new RedirectResolution(SelectAddressAction.class);
        }

        pricingDto = new PricingDto(pricingEngine.calculatePricing(order.getCartLineItems(), order.getOfferInstance(), order.getAddress(), rewardPointsUsed), order.getAddress());
        Set<CartLineItem> subscriptionCartLineItems=new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Subscription).filter();

        order.setRewardPointsUsed(rewardPointsUsed);
        order = (Order) getBaseDao().save(order);

        // billingAddress = userPreferenceDao.getOrCreateUserPreference(user).getBillingAddress();

        // doing this after populating the pricingDto as this actionBean is also used to display pricing elsewhere
        // using the useActionBean tag
        if (order.getAddress() == null) {
            // addRedirectAlertMessage(new LocalizableMessage("/CheckoutAction.action.address.not.selected"));
            return new RedirectResolution(SelectAddressAction.class);
        } else if (pricingDto.getProductLineCount() == 0) {
            addRedirectAlertMessage(new LocalizableMessage("/CheckoutAction.action.checkout.not.allowed.on.empty.cart"));
            return new RedirectResolution(CartAction.class);
        }
        Address address = order.getAddress();
        String pin = address != null ? address.getPin() : null;
        codAllowed = courierService.isCodAllowed(pin);
        if (codAllowed) {
            Double payable = pricingDto.getGrandTotalPayable();
            if (payable < codMinAmount || payable > codMaxAmount) {
                codAllowed = false;
            }
        }
        if(codAllowed){
            if(subscriptionCartLineItems!=null && subscriptionCartLineItems.size()>0){
                codAllowed=false;
            }
        }

//        if (hideCod) {
//            groundShippingAllowed= courierService.isGroundShippingAllowed(pin);
//            cashbackOnGroundshipped = courierService.getCashbackOnGroundShippedItem(groundshipItemAmount, order, groundshipItemweight);
//            if (cashbackOnGroundshipped == null || cashbackOnGroundshipped == -0.0) {
//                cashbackOnGroundshipped = 0.0;
//            } else {
//                cashbackOnGroundshipped = cashbackOnGroundshipped * cashBackPercentageOnGroundShipped;
//            }
//        }

        hideCod = orderService.isOrderHasGroundShippedItem(order);
        if (hideCod){
           groundShippingAllowed= courierService.isGroundShippingAllowed(pin);
         if (!groundShippingAllowed ){
//                               getProductVariantOnGroundShippingNOTAllowed(order) ;
           }
        }

        Double netShopping = pricingDto.getGrandTotalPayable() - pricingDto.getShippingTotal();
        if (netShopping > codFreeAfter) {
            codCharges = 0.0;
        }
        availableCourierList = courierService.getAvailableCouriers(order);
        if (availableCourierList.size() == 0) {
            availableCourierList = null;
        }
        return new ForwardResolution("/pages/orderSummary.jsp");
    }

    public Resolution withoutRewardPoints() {
        boolean originalUserRewardPoints = useRewardPoints;
        useRewardPoints = false;
        Resolution resolution = pre();
        useRewardPoints = originalUserRewardPoints;
        return resolution;
    }

    public Resolution orderReviewed() {
        getBaseDao().save(order);
        return new RedirectResolution(PaymentModeAction.class);
    }

    public PricingDto getPricingDto() {
        return pricingDto;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public boolean isCodAllowed() {
        return codAllowed;
    }

    public void setCodAllowed(boolean codAllowed) {
        this.codAllowed = codAllowed;
    }

    public boolean isUseRewardPoints() {
        return useRewardPoints;
    }

    public void setUseRewardPoints(boolean useRewardPoints) {
        this.useRewardPoints = useRewardPoints;
    }

    public Double getRedeemableRewardPoints() {
        return redeemableRewardPoints;
    }

    public void setRedeemableRewardPoints(Double redeemableRewardPoints) {
        this.redeemableRewardPoints = redeemableRewardPoints;
    }

    public Double getCodCharges() {
        return codCharges;
    }

    public void setCodCharges(Double codCharges) {
        this.codCharges = codCharges;
    }

    public List<Courier> getAvailableCourierList() {
        return availableCourierList;
    }

    public void setAvailableCourierList(List<Courier> availableCourierList) {
        this.availableCourierList = availableCourierList;
    }

    public boolean isHideCod() {
        return hideCod;
    }

    public void setHideCod(boolean hideCod) {
        this.hideCod = hideCod;
    }

    public Double getCashbackOnGroundshipped() {
        return cashbackOnGroundshipped;
    }

    public void setCashbackOnGroundshipped(Double cashbackOnGroundshipped) {
        this.cashbackOnGroundshipped = cashbackOnGroundshipped;
    }

    public boolean isGroundShippingAllowed() {
        return groundShippingAllowed;
    }

    public void setGroundShippingAllowed(boolean groundShippingAllowed) {
        this.groundShippingAllowed = groundShippingAllowed;
    }
}
