package com.hk.web.action.core.order;

import java.util.*;

import com.hk.admin.pact.service.courier.PincodeCourierService;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.LocalizableMessage;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;
import org.stripesstuff.plugin.session.Session;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.pact.service.order.AdminOrderService;
import com.hk.constants.core.HealthkartConstants;
import org.apache.commons.collections.CollectionUtils;
import com.hk.constants.core.Keys;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.courier.Courier;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.dto.pricing.PricingDto;
import com.hk.manager.OrderManager;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.dao.payment.PaymentModeDao;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.service.order.RewardPointService;
import com.hk.pricing.PricingEngine;
import com.hk.web.action.core.cart.CartAction;
import com.hk.web.action.core.payment.PaymentModeAction;
import com.hk.web.action.core.user.SelectAddressAction;

@Secure
@Component
@HttpCache(allow = false)
public class OrderSummaryAction extends BaseAction {

    // private static Logger logger = LoggerFactory.getLogger(OrderSummaryAction.class);

    @Autowired
    private CourierService courierService;
    @Autowired
    private PincodeCourierService pincodeCourierService;
    @Autowired
    UserDao userDao;
    @Autowired
    OrderManager orderManager;
    @Autowired
    PricingEngine pricingEngine;
    @Autowired
    private RewardPointService rewardPointService;
    @Autowired
    private AdminOrderService adminOrderService;

    @Session(key = HealthkartConstants.Session.useRewardPoints)
    private boolean useRewardPoints;

    private PricingDto pricingDto;
    private Order order;
    private Address billingAddress;
    private boolean codAllowed;
    private Double redeemableRewardPoints;
    private List<Courier> availableCourierList;
    private boolean groundShippingAllowed;
    private boolean groundShippedItemPresent;
    private boolean codAllowedOnGroundShipping;
    private Double cashbackOnGroundshipped;
    //    Map<String, String>        codFailureMap = new HashMap<String, String>();
    private Set<CartLineItem> trimCartLineItems;// = new HashSet<CartLineItem>();
    private Integer sizeOfCLI;

    // COD related changes
    @Autowired
    PaymentManager paymentManager;
    @Autowired
    PaymentModeDao paymentModeDao;

    @Value("#{hkEnvProps['" + Keys.Env.codCharges + "']}")
    private Double codCharges;

    @Value("#{hkEnvProps['" + Keys.Env.codFreeAfter + "']}")
    private Double codFreeAfter;

    /*
     * @Value("#{hkEnvProps['" + Keys.Env.codMinAmount + "']}") private Double codMinAmount; //
     * @Named(Keys.Env.codMaxAmount) @Value("#{hkEnvProps['codMaxAmount']}") private Double codMaxAmount;
     */

    @DefaultHandler
    public Resolution pre() {
        User user = getUserService().getUserById(getPrincipal().getId());
        // User user = UserCache.getInstance().getUserById(getPrincipal().getId()).getUser();
        order = orderManager.getOrCreateOrder(user);
        trimCartLineItems = orderManager.trimEmptyLineItems(order);
        sizeOfCLI = order.getCartLineItems().size();

        // OfferInstance offerInstance = order.getOfferInstance();
        Double rewardPointsUsed = 0D;
        redeemableRewardPoints = rewardPointService.getTotalRedeemablePoints(user);
        if (useRewardPoints)
            rewardPointsUsed = redeemableRewardPoints;

        pricingDto = new PricingDto(pricingEngine.calculatePricing(order.getCartLineItems(), order.getOfferInstance(), order.getAddress(), rewardPointsUsed), order.getAddress());

        order.setRewardPointsUsed(rewardPointsUsed);
        order = (Order) getBaseDao().save(order);
        if (order.getAddress() == null) {
            return new RedirectResolution(SelectAddressAction.class);
        }

        Address address = order.getAddress();
        String pin = address != null ? address.getPincode().getPincode() : null;

        CartLineItemFilter cartLineItemFilter = new CartLineItemFilter(order.getCartLineItems());
        Set<CartLineItem> groundShippedCartLineItemSet = cartLineItemFilter.addCartLineItemType(EnumCartLineItemType.Product).hasOnlyGroundShippedItems(true).filter();
        if (groundShippedCartLineItemSet != null && groundShippedCartLineItemSet.size() > 0) {
            groundShippedItemPresent = true;
            groundShippingAllowed = pincodeCourierService.isGroundShippingAllowed(pin);
        }

        Double netShopping = pricingDto.getGrandTotalPayable() - pricingDto.getShippingTotal();
        if (netShopping >= codFreeAfter) {
            codCharges = 0.0;
        }

//        availableCourierList = pincodeCourierService.getAvailableCouriers(order);
//        if (availableCourierList != null && availableCourierList.size() == 0) {
//            availableCourierList = null;
//        }
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

    public boolean isGroundShippedItemPresent() {
        return groundShippedItemPresent;
    }

    public void setGroundShippedItemPresent(boolean groundShippedItemPresent) {
        this.groundShippedItemPresent = groundShippedItemPresent;
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

    public boolean isCodAllowedOnGroundShipping() {
        return codAllowedOnGroundShipping;
    }

    public void setCodAllowedOnGroundShipping(boolean codAllowedOnGroundShipping) {
        this.codAllowedOnGroundShipping = codAllowedOnGroundShipping;
    }

    public Set<CartLineItem> getTrimCartLineItems() {
        return trimCartLineItems;
    }

    public void setTrimCartLineItems(Set<CartLineItem> trimCartLineItems) {
        this.trimCartLineItems = trimCartLineItems;
    }

    public Integer getSizeOfCLI() {
        return sizeOfCLI;
    }

    public void setSizeOfCLI(Integer sizeOfCLI) {
        this.sizeOfCLI = sizeOfCLI;
    }
}
