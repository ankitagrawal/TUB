package com.hk.rest.mobile.service.action;

import com.akube.framework.stripes.controller.JsonHandler;
import com.akube.framework.gson.JsonUtils;
import com.hk.constants.discount.OfferConstants;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.subscription.EnumSubscriptionStatus;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.core.fliter.SubscriptionFilter;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.combo.ComboInstance;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.offer.OfferInstance;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.user.Address;
import com.hk.domain.user.User;
import com.hk.dto.pricing.PricingDto;
import com.hk.manager.OfferManager;
import com.hk.manager.OrderManager;
import com.hk.manager.UserManager;
import com.hk.pact.dao.affiliate.AffiliateDao;
import com.hk.pact.dao.coupon.CouponDao;
import com.hk.pact.dao.offer.OfferInstanceDao;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.dao.core.AddressDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.order.CartFreebieService;
import com.hk.pricing.PricingEngine;
import com.hk.report.dto.pricing.PricingSubDto;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.core.user.SelectAddressAction;
import com.hk.rest.mobile.service.model.MCartLineItemsJSONResponse;
import com.hk.rest.mobile.service.utils.MHKConstants;
import com.shiro.PrincipalImpl;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: Satish
 * Date: Oct 2, 2012
 * Time: 9:22:39 PM
 * To change this template use File | Settings | File Templates.
 */
@Path("/mCart")
@Component
public class MCartAction extends MBaseAction{

    @SuppressWarnings("unused")
    private static Logger logger = LoggerFactory.getLogger(MCartAction.class);

    @Validate(encrypted = true)
    private Order order;
    // private List<CartLineItem> cartLineItems;
    private List<ComboInstance> comboInstances;
    private PricingDto pricingDto;
    private Long itemsInCart = 0L;
    private String freebieBanner;
    private Set<Subscription> subscriptions;

    @Autowired
    private UserService userService;
    @Autowired
    AffiliateDao affiliateDao;
    @Autowired
    UserManager userManager;
    @Autowired
    PricingEngine pricingEngine;
    @Autowired
    OrderManager orderManager;
    @Autowired
    LineItemDao lineItemDao;
    @Autowired
    CouponDao couponDao;
    @Autowired
    OfferInstanceDao offerInstanceDao;
    @Autowired
    OfferManager offerManager;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private AddressDao addressDao;
    @Autowired
    private CartFreebieService cartFreebieService;
    @Autowired
    private org.apache.shiro.mgt.SecurityManager securityManager;

    boolean verifyMessage = false;

    @DefaultHandler

    @GET
    @Path("/viewCart/")
    @Produces("application/json")
    public String viewCart(@Context HttpServletResponse response) {
        HealthkartResponse healthkartResponse;
        String jsonBuilder = "";
        String message = MHKConstants.STATUS_DONE;
        String status = MHKConstants.STATUS_OK;

        List<MCartLineItemsJSONResponse> cartItemsList = new ArrayList<MCartLineItemsJSONResponse>();
        MCartLineItemsJSONResponse cartItemResponse;
        User user = null;
        if (securityManager.getSubject().getPrincipal() != null) {
            user = getUserService().getUserById(((PrincipalImpl) securityManager.getSubject().getPrincipal()).getId());
            if (user == null) {
                user = userManager.createAndLoginAsGuestUser(null, null);
            }
        } else {
            user = userManager.createAndLoginAsGuestUser(null, null);
        }
        if (user != null) {
            order = orderManager.getOrCreateOrder(user);

            Set<CartLineItem> cartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
            for (CartLineItem lineItem : cartLineItems) {
                if (lineItem != null && lineItem.getProductVariant() != null) {
                    ProductVariant productVariant = lineItem.getProductVariant();
                    cartItemResponse = new MCartLineItemsJSONResponse();
                    cartItemResponse.setDiscountOnHkPrice(lineItem.getDiscountOnHkPrice());
                    cartItemResponse.setHkPrice(lineItem.getHkPrice());
                    cartItemResponse.setId(lineItem.getId());
                    cartItemResponse.setName(productVariant.getProduct().getName());
                    if(null!=productVariant.getProduct())
                    cartItemResponse.setProductId(productVariant.getProduct().getId());
                    if(null!=lineItem.getLineItemType())
                    cartItemResponse.setLineItemType(lineItem.getLineItemType().getName());
                    cartItemResponse.setMarkedPrice(lineItem.getMarkedPrice());
                    cartItemResponse.setOrder(lineItem.getOrder().getId());
                    if ((productVariant.getProduct().isDeleted() != null && productVariant.getProduct().isDeleted()) || productVariant.isDeleted() || productVariant.isOutOfStock()) {
                        lineItem.setQty(0L);
                    }
                    cartItemResponse.setQty(lineItem.getQty());
                    cartItemResponse.setCartLineItemId(lineItem.getId().toString());
                    cartItemsList.add(cartItemResponse);
                }
            }

            // Trimming cart line items in case of zero qty ie deleted/outofstock/removed
            order = orderManager.trimEmptyLineItems(order);

            if (order != null && cartLineItems != null) {
                itemsInCart = Long.valueOf(order.getExclusivelyProductCartLineItems().size() + order.getExclusivelyComboCartLineItems().size());
            }

            /* Check if user is referred and has referral coupon to apply. If yes, apply automatically */
            if (user.getReferredBy() != null) {
                Coupon coupon = (user.getReferredBy()).getReferrerCoupon();
                if (coupon != null && coupon.isValid()) {
                    List<OfferInstance> offerInstances = offerInstanceDao.findByUserAndCoupon(user, coupon);
                    if (offerInstances == null || offerInstances.isEmpty()) {
                        if (offerManager.isOfferValidForUser(coupon.getOffer(), user)) {
                            Date offerInstanceEndDate = new DateTime().plusDays(OfferConstants.MAX_ALLOWED_DAYS_FOR_15_PERCENT_REFERREL_DISCOUNT).toDate();
                            OfferInstance offerInstance = offerInstanceDao.createOfferInstance(coupon.getOffer(), coupon, user, offerInstanceEndDate);
                            order.setOfferInstance(offerInstance);
                            coupon.setAlreadyUsed(coupon.getAlreadyUsed() + 1);
                            couponDao.save(coupon);
                        } else {
                            verifyMessage = true;
                        }
                    }
                }
            }

            if (order.getOfferInstance() != null && !order.getOfferInstance().isValid()) {
                offerInstanceDao.save(order.getOfferInstance());
                order.setOfferInstance(null);
                order = orderDao.save(order);
            }

            Address address = order.getAddress() != null ? order.getAddress() : new Address();
            /*
             * Set<CartLineItem> cartLineItemsSet = new HashSet<CartLineItem>();
             * cartLineItemsSet.addAll(cartLineItems);
             */
            pricingDto = new PricingDto(pricingEngine.calculatePricing(order.getCartLineItems(), order.getOfferInstance(), address, 0D), address);

            Set<CartLineItem> subscriptionCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Subscription).filter();
            if (subscriptionCartLineItems != null && subscriptionCartLineItems.size() > 0) {
                subscriptions = new SubscriptionFilter(order.getSubscriptions()).addSubscriptionStatus(EnumSubscriptionStatus.InCart).filter();
                itemsInCart += subscriptions.size();
            }
        }

        freebieBanner = cartFreebieService.getFreebieBanner(order);

        healthkartResponse = new HealthkartResponse(status, message, cartItemsList);
        jsonBuilder = JsonUtils.getGsonDefault().toJson(healthkartResponse);

        addHeaderAttributes(response);

        return jsonBuilder;
    }

    @DontValidate
    public Resolution getCartItems() {
        User user = null;
        if (securityManager.getSubject().getPrincipal() != null) {
            user = getUserService().getUserById(((PrincipalImpl) securityManager.getSubject().getPrincipal()).getId());
        }
        if (user != null) {
            order = orderDao.findByUserAndOrderStatus(user, EnumOrderStatus.InCart);
            if (order != null) {
                Set<CartLineItem> cartLineItems = order.getCartLineItems();
                if (cartLineItems != null && !cartLineItems.isEmpty()) {
                    Set<CartLineItem> productCartLineItems = new CartLineItemFilter(cartLineItems).addCartLineItemType(EnumCartLineItemType.Product).filter();
                    if (order != null && productCartLineItems != null) {
                        itemsInCart = Long.valueOf(order.getExclusivelyProductCartLineItems().size() + order.getExclusivelyComboCartLineItems().size());
                    }
                }
                int inCartSubscriptions = new CartLineItemFilter(cartLineItems).addCartLineItemType(EnumCartLineItemType.Subscription).filter().size();
                itemsInCart += inCartSubscriptions;
            }
        }
        return new ForwardResolution("/pages/cart.jsp");
    }

    /**
     * method used to update the latest pricing, for eg when an offer is applied/changed
     *
     * @return
     */
    @JsonHandler
    @DontValidate
    public Resolution pricing(@Context HttpServletResponse response) {
        viewCart(response);
        PricingSubDto pricingSubDto = new PricingSubDto(pricingDto, null);
        HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "", pricingSubDto);

        response.addHeader("Pragma", "no-cache");
        response.addHeader("Cache-Control", "no-cache");
        response.addHeader("Cache-Control", "private");
        response.addHeader("Cache-Control", "no-store");
        response.addHeader("Cache-Control", "max-age=0");
        response.addHeader("Cache-Control", "s-maxage=0");
        response.addHeader("Cache-Control", "must-revalidate");
        response.addHeader("Cache-Control", "proxy-revalidate");
        return new JsonResolution(healthkartResponse);
    }

    @GET
    @Path("/checkOut/")
    @Produces("application/json")
    public String checkout(@QueryParam("orderId") Long orderId,@Context HttpServletResponse response) {
        HealthkartResponse healthkartResponse;
        String jsonBuilder = "";
        String message = MHKConstants.STATUS_DONE;
        String status = MHKConstants.STATUS_OK;

        order = orderManager.getOrderService().find(orderId);
        orderManager.trimEmptyLineItems(order);
        /*
         * for (Iterator<LineItem> lineItemIterator = order.getProductCartLineItems().iterator();
         * lineItemIterator.hasNext();) { LineItem lineItem = lineItemIterator.next(); lineItemDao.save(lineItem); }
         */

        User user = getUserService().getUserById(getPrincipal().getId());
        String email = user.getEmail();
        List<Address> addresses = addressDao.getVisibleAddresses(user);
        Order order = orderManager.getOrCreateOrder(user);
        Address selectedAddress = order.getAddress();
        if (selectedAddress == null) {
            // get the last order address? for not selecting just first non deleted one.
            if (addresses != null && addresses.size() > 0) {
                selectedAddress = addresses.get(0);
                healthkartResponse = new HealthkartResponse(status, message, selectedAddress);
            }else{
                healthkartResponse = new HealthkartResponse(status, message, "No Address");
            }
        }else{

            Map addressMap = new HashMap();
            addressMap.put("name",selectedAddress.getName());
            addressMap.put("city",selectedAddress.getCity());
            addressMap.put("line1",selectedAddress.getLine1());
            addressMap.put("line2",selectedAddress.getLine2());
            addressMap.put("state",selectedAddress.getState());
            addressMap.put("pin",selectedAddress.getPin());
            addressMap.put("phone",selectedAddress.getPhone());

            healthkartResponse = new HealthkartResponse(status, message, addressMap);
        }
        jsonBuilder = JsonUtils.getGsonDefault().toJson(healthkartResponse);

        addHeaderAttributes(response);

        return jsonBuilder;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public String getFreebieBanner() {
        return freebieBanner;
    }

    /*
     * public List<LineItem> getCartLineItems() { return cartLineItems; } public void setLineItems(List<LineItem>
     * cartLineItems) { this.cartLineItems = cartLineItems; }
     */

    public PricingDto getPricingDto() {
        return pricingDto;
    }

    public boolean isVerifyMessage() {
        return verifyMessage;
    }

    public Long getItemsInCart() {
        return itemsInCart;
    }

    public void setItemsInCart(Long itemsInCart) {
        this.itemsInCart = itemsInCart;
    }

    public List<ComboInstance> getComboInstances() {
        return comboInstances;
    }

    public void setComboInstances(List<ComboInstance> comboInstances) {
        this.comboInstances = comboInstances;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public Set<Subscription> getSubscriptions() {
        return subscriptions;
    }

    public void setSubscriptions(Set<Subscription> subscriptions) {
        this.subscriptions = subscriptions;
    }
}
