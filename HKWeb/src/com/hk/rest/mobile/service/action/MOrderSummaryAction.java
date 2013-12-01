package com.hk.rest.mobile.service.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.hk.admin.pact.service.courier.PincodeCourierService;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;
import org.stripesstuff.plugin.session.Session;

import com.akube.framework.gson.JsonUtils;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.pact.service.order.AdminOrderService;
import com.hk.constants.core.HealthkartConstants;
import com.hk.constants.core.Keys;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.courier.Courier;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.dto.pricing.PricingDto;
import com.hk.manager.OrderManager;
import com.hk.manager.payment.PaymentManager;
import com.hk.pact.service.order.RewardPointService;
import com.hk.pricing.PricingEngine;
import com.hk.rest.mobile.service.model.MCartLineItemsJSONResponse;
import com.hk.rest.mobile.service.model.MUserLoginJSONResponse;
import com.hk.rest.mobile.service.utils.MHKConstants;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.core.payment.PaymentModeAction;

@Secure
@Path("/mOrderSummary")
@Component
public class MOrderSummaryAction extends MBaseAction {

    // private static Logger logger = LoggerFactory.getLogger(OrderSummaryAction.class);

    @Autowired
    private CourierService     courierService;
    @Autowired
    private PincodeCourierService pincodeCourierService;
    @Autowired
    OrderManager               orderManager;
    @Autowired
    PricingEngine              pricingEngine;
    /*
     * @Autowired ReferrerProgramManager referrerProgramManager;
     */

    @Autowired
    private RewardPointService rewardPointService;
    @Autowired
    private AdminOrderService  adminOrderService;

    @Session(key = HealthkartConstants.Session.useRewardPoints)
    private boolean            useRewardPoints;

    private PricingDto         pricingDto;
    private Order              order;
    private Address            billingAddress;
    private boolean            codAllowed;
    private Double             redeemableRewardPoints;
    private List<Courier>      availableCourierList;
    private boolean            groundShippingAllowed;
    private boolean            groundShippedItemPresent;
    private boolean            codAllowedOnGroundShipping;
    private Double             cashbackOnGroundshipped;
    Map<String, String>        codFailureMap = new HashMap<String, String>();

    // COD related changes
    @Autowired
    PaymentManager             paymentManager;

    @Value("#{hkEnvProps['" + Keys.Env.codCharges + "']}")
    private Double             codCharges;

    @Value("#{hkEnvProps['" + Keys.Env.codFreeAfter + "']}")
    private Double             codFreeAfter;

    /*
     * @Value("#{hkEnvProps['" + Keys.Env.codMinAmount + "']}") private Double codMinAmount; //
     * @Named(Keys.Env.codMaxAmount) @Value("#{hkEnvProps['codMaxAmount']}") private Double codMaxAmount;
     */

    @DefaultHandler
    @GET
    @Path("/orderSummary/")
    @Produces("application/json")
    public String orderSummary() {
        HealthkartResponse healthkartResponse;
        String jsonBuilder = "";
        String message = "Done";
        String status = HealthkartResponse.STATUS_OK;
        Map<String, Object> orderMap = new HashMap<String, Object>();
        try {
            User user = getUserService().getUserById(getPrincipal().getId());
            order = orderManager.getOrCreateOrder(user);
            // Trimming empty line items once again.
            orderManager.trimEmptyLineItems(order);
            // OfferInstance offerInstance = order.getOfferInstance();
            Double rewardPointsUsed = 0D;
            redeemableRewardPoints = rewardPointService.getTotalRedeemablePoints(user);
            orderMap.put("redeemableRewardPoints", redeemableRewardPoints);
            if (useRewardPoints)
                rewardPointsUsed = redeemableRewardPoints;
            if (order.getAddress() == null) {
                message = MHKConstants.NO_ADDRESS;
                status = MHKConstants.STATUS_ERROR;
                return com.akube.framework.gson.JsonUtils.getGsonDefault().toJson(new HealthkartResponse(status, message, message));
            }

            pricingDto = new PricingDto(pricingEngine.calculatePricing(order.getCartLineItems(), order.getOfferInstance(), order.getAddress(), rewardPointsUsed),
                    order.getAddress());
            orderMap.put("pricingDto", pricingDto);
            order.setRewardPointsUsed(rewardPointsUsed);

            order = (Order) getBaseDao().save(order);
            if (order.getAddress() == null) {
                message = MHKConstants.NO_ADDRESS;
                status = MHKConstants.STATUS_ERROR;
                return com.akube.framework.gson.JsonUtils.getGsonDefault().toJson(new HealthkartResponse(status, message, message));
            } else if (pricingDto.getProductLineCount() == 0) {
                // addRedirectAlertMessage(new
                // LocalizableMessage("/CheckoutAction.action.checkout.not.allowed.on.empty.cart"));
                message = MHKConstants.EMPTY_CART;
                status = MHKConstants.STATUS_ERROR;
                return com.akube.framework.gson.JsonUtils.getGsonDefault().toJson(new HealthkartResponse(status, message, message));
            }

            Address address = order.getAddress();
            MUserLoginJSONResponse add = new MUserLoginJSONResponse();
            add.setCity(address.getCity());
            add.setId(address.getId());
            add.setLine1(address.getLine1());
            add.setLine2(address.getLine2());
            add.setName(address.getName());
            add.setPhone(address.getPhone());
            add.setPin(address.getPincode().getPincode());
            add.setState(address.getState());
            orderMap.put("addressSelected", add);

            String pin = address != null ? address.getPincode().getPincode() : null;

            codFailureMap = adminOrderService.isCODAllowed(order, pricingDto.getGrandTotalPayable());

            List<MCartLineItemsJSONResponse> cartItemsList = new ArrayList<MCartLineItemsJSONResponse>();
            MCartLineItemsJSONResponse cartItemResponse;
            for (CartLineItem lineItem : order.getCartLineItems()) {
                if (lineItem != null && lineItem.getProductVariant() != null) {
                    ProductVariant productVariant = lineItem.getProductVariant();
                    cartItemResponse = new MCartLineItemsJSONResponse();
                    if (null != lineItem.getDiscountOnHkPrice())
                        cartItemResponse.setDiscountOnHkPrice(priceFormat.format(lineItem.getDiscountOnHkPrice()));
                    if (null != lineItem.getHkPrice())
                        cartItemResponse.setHkPrice(priceFormat.format(lineItem.getHkPrice()));
                    cartItemResponse.setId(lineItem.getId());
                    cartItemResponse.setName(productVariant.getProduct().getName());
                    if (null != productVariant.getProduct())
                        cartItemResponse.setProductId(productVariant.getProduct().getId());
                    if (null != lineItem.getLineItemType())
                        cartItemResponse.setLineItemType(lineItem.getLineItemType().getName());
                    if (null != lineItem.getMarkedPrice())
                        cartItemResponse.setMarkedPrice(priceFormat.format(lineItem.getMarkedPrice()));
                    cartItemResponse.setOrder(lineItem.getOrder().getId());
                    cartItemResponse.setQty(lineItem.getQty());
                    cartItemResponse.setCartLineItemId(lineItem.getId().toString());
                    cartItemsList.add(cartItemResponse);
                }
            }
            orderMap.put("cartItems", cartItemsList);

            // Ground Shipping logic starts ---
            CartLineItemFilter cartLineItemFilter = new CartLineItemFilter(order.getCartLineItems());
            Set<CartLineItem> groundShippedCartLineItemSet = cartLineItemFilter.addCartLineItemType(EnumCartLineItemType.Product).hasOnlyGroundShippedItems(true).filter();
            if (groundShippedCartLineItemSet != null && groundShippedCartLineItemSet.size() > 0) {
                groundShippedItemPresent = true;
                groundShippingAllowed = pincodeCourierService.isGroundShippingAllowed(pin);
            }
            orderMap.put("groundShippedItemPresent", groundShippedItemPresent);
            orderMap.put("groundShippingAllowed", groundShippingAllowed);
            // Ground Shipping logic ends --

            Double netShopping = pricingDto.getGrandTotalPayable() - pricingDto.getShippingTotal();
            if (netShopping > codFreeAfter) {
                codCharges = 0.0;
            }
            orderMap.put("codCharges", codCharges);
            Double saved = 0.0;
            if (null != pricingDto.getProductsMrpSubTotal() && null != pricingDto.getProductsHkSubTotal())
                saved = pricingDto.getProductsMrpSubTotal() - pricingDto.getProductsHkSubTotal();

            String saveRs = priceFormat.format(saved);
            orderMap.put("saved", saveRs);

            String total = "0.0";
            if (null != pricingDto.getProductsHkSubTotal())
                total = priceFormat.format(pricingDto.getProductsHkSubTotal());
            orderMap.put("total", total);

            boolean courierAvailable = true;
            availableCourierList = pincodeCourierService.getAvailableCouriers(order);
            if (availableCourierList != null && availableCourierList.size() == 0) {
                courierAvailable = false;
            }
            orderMap.put("courierAvailable", courierAvailable);

        } catch (Exception e) {
            status = MHKConstants.STATUS_ERROR;
            message = MHKConstants.STATUS_ERROR;
            return JsonUtils.getGsonDefault().toJson(new HealthkartResponse(status, message, orderMap));
        }
        healthkartResponse = new HealthkartResponse(status, message, orderMap);
        jsonBuilder = JsonUtils.getGsonDefault().toJson(healthkartResponse);
        return jsonBuilder;
    }

    public String withoutRewardPoints() {
        boolean originalUserRewardPoints = useRewardPoints;
        useRewardPoints = false;
        String resolution = orderSummary();
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

    public Map<String, String> getCodFailureMap() {
        return codFailureMap;
    }

    public void setCodFailureMap(Map<String, String> codFailureMap) {
        this.codFailureMap = codFailureMap;
    }
}
