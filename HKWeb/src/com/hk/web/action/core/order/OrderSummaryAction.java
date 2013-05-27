package com.hk.web.action.core.order;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.courier.PincodeCourierService;
import com.hk.constants.core.HealthkartConstants;
import com.hk.constants.core.Keys;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.dto.pricing.PricingDto;
import com.hk.manager.OrderManager;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.order.RewardPointService;
import com.hk.pricing.PricingEngine;
import com.hk.web.action.core.payment.PaymentModeAction;
import com.hk.web.action.core.user.SelectAddressAction;
import net.sourceforge.stripes.action.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;
import org.stripesstuff.plugin.session.Session;

import java.util.HashSet;
import java.util.Set;

@Secure
@Component
@HttpCache(allow = false)
@Ssl
public class OrderSummaryAction extends BaseAction {

    @Autowired
    OrderManager orderManager;
    @Autowired
    OrderService orderService;
    @Autowired
    PricingEngine pricingEngine;
    @Autowired
    private RewardPointService rewardPointService;

    @Session(key = HealthkartConstants.Session.useRewardPoints)
    private boolean useRewardPoints;
    private Double redeemableRewardPoints;

    @Autowired
    private PincodeCourierService pincodeCourierService;
    private boolean groundShippingAllowed = true;

    private PricingDto pricingDto;
    private Order order;
    private Set<CartLineItem> trimCartLineItems = new HashSet<CartLineItem>();

    @Autowired
    PaymentManager paymentManager;
    @Value("#{hkEnvProps['" + Keys.Env.codCharges + "']}")
    private Double codCharges;
    @Value("#{hkEnvProps['" + Keys.Env.codFreeAfter + "']}")
    private Double codFreeAfter;

    @DefaultHandler
    public Resolution pre() {
        User user = getUserService().getLoggedInUser();
        order = orderManager.getOrCreateOrder(user);
        if (order.getAddress() == null) return new RedirectResolution(SelectAddressAction.class);
        trimCartLineItems = orderManager.trimEmptyLineItems(order);

        redeemableRewardPoints = rewardPointService.getTotalRedeemablePoints(user);
        Double rewardPointsUsed = useRewardPoints ? redeemableRewardPoints : 0D;
        pricingDto = new PricingDto(pricingEngine.calculatePricing(order.getCartLineItems(), order.getOfferInstance(), order.getAddress(), rewardPointsUsed), order.getAddress());
        order.setRewardPointsUsed(rewardPointsUsed);
        order = orderService.save(order);

        Set<CartLineItem> groundShippedCartLineItemSet = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).hasOnlyGroundShippedItems(true).filter();
        if (groundShippedCartLineItemSet.size() > 0) {
            groundShippingAllowed = pincodeCourierService.isGroundShippingAllowed(order.getAddress().getPincode().getPincode());
        }

        Double netShopping = pricingDto.getGrandTotalPayable() - pricingDto.getShippingTotal();
        if (netShopping >= codFreeAfter) {
            codCharges = 0.0;
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
        orderService.save(order);
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

    public boolean isGroundShippingAllowed() {
        return groundShippingAllowed;
    }

    public void setGroundShippingAllowed(boolean groundShippingAllowed) {
        this.groundShippingAllowed = groundShippingAllowed;
    }

    public Set<CartLineItem> getTrimCartLineItems() {
        return trimCartLineItems;
    }

    public void setTrimCartLineItems(Set<CartLineItem> trimCartLineItems) {
        this.trimCartLineItems = trimCartLineItems;
    }

}
