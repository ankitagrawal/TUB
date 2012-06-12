package com.hk.manager;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.hk.constants.HttpRequestAndSessionConstants;
import com.hk.constants.core.EnumRole;
import com.hk.constants.core.Keys;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.constants.referrer.EnumPrimaryReferrerForOrder;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.builder.CartLineItemBuilder;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.combo.Combo;
import com.hk.domain.catalog.product.combo.ComboInstance;
import com.hk.domain.catalog.product.combo.ComboInstanceHasProductVariant;
import com.hk.domain.matcher.CartLineItemMatcher;
import com.hk.domain.offer.OfferInstance;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.CartLineItemConfig;
import com.hk.domain.order.CartLineItemExtraOption;
import com.hk.domain.order.Order;
import com.hk.domain.order.OrderCategory;
import com.hk.domain.order.PrimaryReferrerForOrder;
import com.hk.domain.order.SecondaryReferrerForOrder;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.payment.Payment;
import com.hk.domain.sku.Sku;
import com.hk.domain.user.User;
import com.hk.domain.clm.KarmaProfile;
import com.hk.dto.pricing.PricingDto;
import com.hk.exception.OutOfStockException;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.catalog.combo.ComboInstanceHasProductVariantDao;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.dao.order.cartLineItem.CartLineItemDao;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.service.OrderStatusService;
import com.hk.pact.service.UserService;
import com.hk.pact.service.clm.KarmaProfileService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.core.AffilateService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.order.CartLineItemService;
import com.hk.pact.service.order.OrderLoggingService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.order.RewardPointService;
import com.hk.pact.service.payment.PaymentService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.store.StoreService;
import com.hk.pricing.PricingEngine;
import com.hk.util.OrderUtil;
import com.hk.web.filter.WebContext;

@Component
public class OrderManager {

    private static Logger                     logger = LoggerFactory.getLogger(OrderManager.class);

    @Autowired
    ProductVariantService                     productVariantService;
    @Autowired
    private CartLineItemService               cartLineItemService;
    @Autowired
    private OrderService                      orderService;
    @Autowired
    private PaymentService                    paymentService;
    @Autowired
    private UserService                       userService;
    @Autowired
    private AffilateService                   affilateService;
    @Autowired
    private RewardPointService                rewardPointService;
    @Autowired
    private ShippingOrderService              shippingOrderService;
    @Autowired
    private EmailManager                      emailManager;
    @Autowired
    private OrderDao                          orderDao;
    @Autowired
    private PricingEngine                     pricingEngine;
    @Autowired
    private LineItemDao                       lineItemDao;
    @Autowired
    private CartLineItemDao                   cartLineItemDao;
    @Autowired
    private LinkManager                       linkManager;
    @Autowired
    private ReferrerProgramManager            referrerProgramManager;
    @Autowired
    private SkuService                        skuService;
    @Autowired
    private InventoryService                  inventoryService;
    @Autowired
    private OrderStatusService                orderStatusService;
    @Autowired
    private BaseDao                           baseDao;
    @Autowired
    private OrderLoggingService               orderLoggingService;
    @Autowired
    private KarmaProfileService               karmaProfileService;

    @Autowired
    private ComboInstanceHasProductVariantDao comboInstanceHasProductVariantDao;

    @Value("#{hkEnvProps['" + Keys.Env.codCharges + "']}")
    private Double                            codCharges;

    @Value("#{hkEnvProps['" + Keys.Env.codFreeAfter + "']}")
    private Double                            codFreeAfter;

    @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
    String                                    adminDownloadsPath;

    Affiliate                                 affiliate;

    /*
     * // @Named(Keys.Env.cashBackPercentage) private Double cashBackPercentage; // @Named(Keys.Env.cashBackLimit)
     * private Double cashBackLimit;
     */

    @Transactional
    public Order getOrCreateOrder(User user) {
        Order order = getOrderService().findByUserAndOrderStatus(user, EnumOrderStatus.InCart);
        if (order != null)
            return order;

        order = new Order();
        order.setUser(user);
        order.setOrderStatus(getOrderStatusService().find(EnumOrderStatus.InCart));
        order.setAmount(0D);

        if (user.getRoleStrings().contains(EnumRole.B2B_USER.getRoleName())) {
            order.setB2bOrder(true);
        }
        Order existingOrderNow = getOrderService().findByUserAndOrderStatus(user, EnumOrderStatus.InCart);
        if (existingOrderNow != null) {
            return existingOrderNow;
        }

        if (WebContext.getRequest().getSession().getAttribute(HttpRequestAndSessionConstants.PRIMARY_REFERRER_ID) != null) {
            Long primaryReferrerForOrderId = (Long) WebContext.getRequest().getSession().getAttribute(HttpRequestAndSessionConstants.PRIMARY_REFERRER_ID);
            order.setPrimaryReferrerForOrder(getBaseDao().get(PrimaryReferrerForOrder.class, primaryReferrerForOrderId));
        }
        if (WebContext.getRequest().getSession().getAttribute(HttpRequestAndSessionConstants.SECONDARY_REFERRER_ID) != null) {
            Long secondaryReferrerForOrderId = (Long) WebContext.getRequest().getSession().getAttribute(HttpRequestAndSessionConstants.SECONDARY_REFERRER_ID);
            order.setSecondaryReferrerForOrder(getBaseDao().get(SecondaryReferrerForOrder.class, secondaryReferrerForOrderId));
        }
        if (user.getOrders().size() == 0 && user.getReferredBy() != null) {
            order.setPrimaryReferrerForOrder(getBaseDao().get(PrimaryReferrerForOrder.class, EnumPrimaryReferrerForOrder.RFERRAL.getId()));
        }
        if (WebContext.getRequest().getSession().getAttribute(HttpRequestAndSessionConstants.UTM_CAMPAIGN) != null) {
            order.setUtmCampaign((String) WebContext.getRequest().getSession().getAttribute(HttpRequestAndSessionConstants.UTM_CAMPAIGN));
        }

        order = getOrderService().save(order);
        return order;
    }

    public boolean createLineItems(List<ProductVariant> productVariants, Order order, Combo combo, ComboInstance comboInstance) throws OutOfStockException {
        boolean isCartLineItemCreated = false;
        Double totalActualHkPriceofComboVariants = 0D;
        if (combo != null && combo.getId() != null) {

            for (ComboInstanceHasProductVariant variant : comboInstanceHasProductVariantDao.findByComboInstance(comboInstance)) {
                totalActualHkPriceofComboVariants += variant.getProductVariant().getHkPrice(null) * variant.getQty();
            }

            /*
             * for (ComboProduct comboProduct : combo.getComboProducts()) { for (ProductVariant variant :
             * comboProduct.getAllowedProductVariants()) { if (variant.getProduct().equals(comboProduct.getProduct())) {
             * totalActualHkPriceofComboVariants += variant.getHkPrice(null) * comboProduct.getQty(); break; } } }
             */
        }

        for (ProductVariant productVariant : productVariants) {
            if (productVariant.getQty() == null || productVariant.getQty() == 0 && combo == null) {
                productVariant.setQty(1L);
            }
            if (canAddVariantToCart(productVariant)) {
                CartLineItem cartLineItem = null;
                if (comboInstance != null) {
                    CartLineItemMatcher cartLineItemMatcher = new CartLineItemMatcher().addProductVariant(productVariant).addComboInstance(comboInstance);
                    cartLineItem = getCartLineItemService().getMatchingCartLineItemFromOrder(order, cartLineItemMatcher);

                    // cartLineItem = getCartLineItemService().getCartLineItemFromOrder(order, productVariant,
                    // comboInstance);
                } else {
                    CartLineItemMatcher cartLineItemMatcher = new CartLineItemMatcher().addProductVariant(productVariant);
                    cartLineItem = getCartLineItemService().getMatchingCartLineItemFromOrder(order, cartLineItemMatcher);
                    // cartLineItem = getCartLineItemService().getCartLineItemFromOrder(order, productVariant);
                }
                if (cartLineItem != null) {
                    /*
                     * if (cartLineItem.getQty() == 0) { cartLineItem.setQty(productVariant.getQty());
                     * getCartLineItemService().save(cartLineItem); }
                     */
                    updateCartLineItemWithQty(cartLineItem, productVariant.getQty());
                } else {
                    if (combo != null && combo.getId() != null) {
                        cartLineItem = getCartLineItemService().createCartLineItemWithBasicDetails(productVariant, order);
                        Double totalDiscount = totalActualHkPriceofComboVariants - combo.getHkPrice();
                        Double percentDiscount = totalDiscount / totalActualHkPriceofComboVariants;
                        cartLineItem.setHkPrice(productVariant.getHkPrice(null) * (1 - percentDiscount));
                        // cartLineItem.setHkPrice(productVariant.getHkPrice(null) - (percentDiscount *
                        // productVariant.getHkPrice(null) * productVariant.getQty()));
                        cartLineItem.setComboInstance(comboInstance);
                    } else {
                        cartLineItem = getCartLineItemService().createCartLineItemWithBasicDetails(productVariant, order);
                    }
                    cartLineItem = getCartLineItemService().save(cartLineItem);
                    isCartLineItemCreated = true;
                }
            }
        }
        return isCartLineItemCreated;
    }

    public boolean createLineItems(ProductVariant productVariant, CartLineItemConfig cartLineItemConfig, Order order) throws OutOfStockException {
        boolean isCartLineItemCreated = false;

        if (canAddVariantToCart(productVariant)) {
            CartLineItemMatcher cartLineItemMatcher = new CartLineItemMatcher().addProductVariant(productVariant).addCartLineItemConfig(cartLineItemConfig);
            CartLineItem cartLineItem = getCartLineItemService().getMatchingCartLineItemFromOrder(order, cartLineItemMatcher);
            // CartLineItem cartLineItem = getCartLineItemService().getCartLineItemFromOrder(order, productVariant,
            // cartLineItemConfig);
            if (cartLineItem != null) {
                updateCartLineItemWithQty(cartLineItem, productVariant.getQty());
            } else {
                cartLineItem = getCartLineItemService().createCartLineItemWithBasicDetails(productVariant, order);
                cartLineItemConfig = (CartLineItemConfig) getBaseDao().save(cartLineItemConfig);
                cartLineItem.setLineItemConfig(cartLineItemConfig);
                double configPrice = cartLineItemConfig.getPrice();
                cartLineItem.setMarkedPrice(productVariant.getMarkedPrice() + configPrice);
                cartLineItem.setHkPrice(productVariant.getHkPrice() + configPrice);
                // cartLineItem.setCostPrice(productVariant.getCostPrice() + configPrice);
                cartLineItem = getCartLineItemService().save(cartLineItem);
                isCartLineItemCreated = true;
            }
        }

        return isCartLineItemCreated;
    }

    public boolean createLineItems(ProductVariant productVariant, List<CartLineItemExtraOption> extraOptions, Order order) throws OutOfStockException {
        boolean isCartLineItemCreated = false;
        if (canAddVariantToCart(productVariant)) {
            CartLineItemMatcher cartLineItemMatcher = new CartLineItemMatcher().addProductVariant(productVariant).addExtraOptions(extraOptions);
            CartLineItem cartLineItem = getCartLineItemService().getMatchingCartLineItemFromOrder(order, cartLineItemMatcher);
            // CartLineItem cartLineItem = cartLineItemService.getCartLineItemFromOrder(order, productVariant,
            // extraOptions);

            if (cartLineItem != null) {
                updateCartLineItemWithQty(cartLineItem, productVariant.getQty());
            } else {
                cartLineItem = getCartLineItemService().createCartLineItemWithBasicDetails(productVariant, order);
                cartLineItem = getCartLineItemService().save(cartLineItem);
                for (CartLineItemExtraOption extraOption : extraOptions) {
                    extraOption.setCartLineItem(cartLineItem);
                    getBaseDao().save(extraOption);
                }
                isCartLineItemCreated = true;
            }
        }
        return isCartLineItemCreated;
    }

    private boolean canAddVariantToCart(ProductVariant productVariant) {
        boolean shouldAdd = false;
        if (productVariant.getQty() != null && productVariant.getQty() > 0) {
            if (productVariant.isOutOfStock()) {
                throw new OutOfStockException(productVariant);
            }
            shouldAdd = true;
        }

        return shouldAdd;
    }

    private CartLineItem updateCartLineItemWithQty(CartLineItem cartLineItem, Long variantQty) {
        // TODO: # warehouse should not it be cartLineItem.getQty != variantQty
        if (variantQty != null && variantQty > 0 && cartLineItem.getQty() != variantQty) {
            cartLineItem.setQty(variantQty);
            cartLineItem = getCartLineItemService().save(cartLineItem);
        }
        return cartLineItem;
    }

    @Transactional
    public Order orderPaymentReceieved(Payment payment) {
        Order order = payment.getOrder();
        order.setPayment(payment);
        order.setGatewayOrderId(payment.getGatewayOrderId());

        /*
         * User user = order.getUser(); OfferInstance offerInstance = order.getOfferInstance();
         */

        // apply pricing and save cart line items
        Set<CartLineItem> cartLIFromPricingEngine = getPricingEngine().calculateAndApplyPricing(order.getCartLineItems(), order.getOfferInstance(), order.getAddress(),
                order.getRewardPointsUsed());
        Set<CartLineItem> cartLineItems = getCartLineItemsFromPricingCartLi(order, cartLIFromPricingEngine);

        PricingDto pricingDto = new PricingDto(cartLineItems, order.getAddress());

        // give commisions to affiliates and and award them reward points if order came from them.
        getAffilateService().saveOfferInstanceAndSaveAffiliateCommission(order, pricingDto);

        // apply cod charges if applicable and update payment object
        Double codCharges = 0D;
        if (payment.isCODPayment()) {
            codCharges = this.codCharges;
            if ((pricingDto.getGrandTotalPayable() - pricingDto.getShippingTotal()) >= this.codFreeAfter) {
                codCharges = 0D;
            }
            CartLineItem codLine = createCodLineItem(order, codCharges);
            order.setAmount(order.getAmount() + codCharges);
            cartLineItems.add(codLine);
            payment.setAmount(order.getAmount());
            getPaymentService().save(payment);
        }        
        /**
         * Order lifecycle activity logging - Payement Marked Successful
         */
        if (payment.getPaymentStatus().getId().equals(EnumPaymentStatus.SUCCESS.getId())) {
            getOrderLoggingService().logOrderActivity(order, order.getUser(),
                    getOrderLoggingService().getOrderLifecycleActivity(EnumOrderLifecycleActivity.PaymentMarkedSuccessful), null);
        }else if (payment.getPaymentStatus().getId().equals(EnumPaymentStatus.ON_DELIVERY.getId())) {
            getOrderLoggingService().logOrderActivity(order, getUserService().getAdminUser(),
                    getOrderLoggingService().getOrderLifecycleActivity(EnumOrderLifecycleActivity.ConfirmedAuthorization), "Auto confirmation as valid user based on history.");
        }

        // order.setAmount(pricingDto.getGrandTotalPayable());
        order.setAmount(pricingDto.getGrandTotalPayable() + codCharges);
        order.setRewardPointsUsed(pricingDto.getRedeemedRewardPoints());

        cartLineItems = addFreeVariantsToCart(cartLineItems); // function made to handle deals and offers which are

        // add promotional freebie - for MIH = SPT391-01 VX Weight Lifting Straps
        if (order.getStore() != null && order.getStore().getId().equals(StoreService.MIH_STORE_ID)) {
            cartLineItems = addFreeCartLineItems("SPT391-01", order);
        }

        //order.setCartLineItems(cartLineItems);
        //order = getOrderService().save(order);

        // associated with a variant, this will help in
        // minimizing brutal use of free checkout
        order.setCartLineItems(cartLineItems);

        // award reward points, if using a reward point offer coupon
        rewardPointService.awardRewardPoints(order);

        // save order with placed status since amount has been applied
        order.setOrderStatus(getOrderStatusService().find(EnumOrderStatus.Placed));

        Set<OrderCategory> categories = getOrderService().getCategoriesForBaseOrder(order);
        order.setCategories(categories);

        /*
           * update user karma profile for those whose score is not yet set
           */
        KarmaProfile karmaProfile = getKarmaProfileService().findByUser(order.getUser());
        order.setScore(new Long(karmaProfile.getKarmaPoints()));
      
        order = getOrderService().save(order);

        /**
         * Order lifecycle activity logging - Order Placed
         */
        getOrderLoggingService().logOrderActivity(order, order.getUser(), getOrderLoggingService().getOrderLifecycleActivity(EnumOrderLifecycleActivity.OrderPlaced), null);

        getUserService().updateIsProductBought(order);

        /**
         * Spliting of orders and auto-escaltion done while splitting if possible Earlier auto escalation was being done
         * at the same instant when the shipping orders were created, now first order status is changed and then it is
         * auto escalated
         */

        // if reward points redeemed then add reward point txns
        if (pricingDto.getRedeemedRewardPoints() > 0) {
            getReferrerProgramManager().redeemRewardPoints(order, pricingDto.getRedeemedRewardPoints());
        }

        /*
         * //Auto escalation of order if inventory is positive if (orderService.autoEscalateOrder(order)) {
         * orderService.logOrderActivity(order, getUserService.getAdminUser(),
         * orderLifecycleActivityDaoProvider.get().find(EnumOrderLifecycleActivity.AutoEscalatedToProcessingQueue.getId()),
         * null); }
         */

        // Check if HK order then only send emails
        if (order.getStore() != null && order.getStore().getId().equals(StoreService.DEFAULT_STORE_ID)) {
            // Send mail to Customer
            getPaymentService().sendPaymentEmailForOrder(order);
            // Send referral program intro email
            sendReferralProgramEmail(order.getUser());
        }
        // Send mail to Admin
        getEmailManager().sendOrderConfirmEmailToAdmin(order);

        return order;
    }

    public void sendReferralProgramEmail(User user) {
        Order lastOrder = getOrderService().getLatestOrderForUser(user);
        if (lastOrder == null) {
            getEmailManager().sendReferralProgramIntro(user, getLinkManager().getReferralProgramUrl());
        }
    }

    public Set<CartLineItem> addFreeCartLineItems(String variantId, Order order) {
        Set<CartLineItem> cartLineItems = order.getCartLineItems();
        ProductVariant productVariant = getProductVariantService().getVariantById(variantId);
        if (productVariant != null) {
            productVariant.setQty(1L);
            CartLineItem cartLineItem = getCartLineItemService().createCartLineItemWithBasicDetails(productVariant, order);
            cartLineItem.setDiscountOnHkPrice(cartLineItem.getHkPrice());
            cartLineItem = getCartLineItemService().save(cartLineItem);
            cartLineItems.add(cartLineItem);
        }
        return cartLineItems;
    }

    private Set<CartLineItem> addFreeVariantsToCart(Set<CartLineItem> cartLineItems) {
        Set<CartLineItem> updatedCartLineItems = new HashSet<CartLineItem>();
        updatedCartLineItems.addAll(cartLineItems);
        for (CartLineItem cartLineItem : cartLineItems) {
            if (cartLineItem.getLineItemType().getId().equals(EnumCartLineItemType.Product.getId())) {
                ProductVariant freeVariant = cartLineItem.getProductVariant().getFreeProductVariant();
                if (freeVariant != null) {
                    CartLineItem existingCartLineItem = getCartLineItemDao().getLineItem(freeVariant, cartLineItem.getOrder());
                    if (existingCartLineItem != null) {
                        updatedCartLineItems.remove(existingCartLineItem);
                    }
                    CartLineItem freeLineItem = createFreeLineItem(cartLineItem, freeVariant);
                    if (freeLineItem != null) {
                        updatedCartLineItems.add(freeLineItem);
                    }
                }
            }
        }
        return updatedCartLineItems;
    }

    // If you dont know what this method does, dont use it, strictly made to handle deals and offers/ free goodies
    // associated with a variant, hence they are given for free
    public CartLineItem createFreeLineItem(CartLineItem cartLineItem, ProductVariant freeVariant) throws OutOfStockException {
        Order order = cartLineItem.getOrder();
        freeVariant.setQty(cartLineItem.getQty());
        if (!freeVariant.isOutOfStock()) {
            CartLineItem existingCartLineItem = getCartLineItemDao().getLineItem(freeVariant, order);
            if (existingCartLineItem == null) { // The variant is not added in user account already
                CartLineItem freeCartLineItem = cartLineItemService.createCartLineItemWithBasicDetails(freeVariant, order);
                freeCartLineItem.setDiscountOnHkPrice(freeVariant.getHkPrice());
                return cartLineItemService.save(freeCartLineItem);
            } else {
                existingCartLineItem.setQty(existingCartLineItem.getQty() + freeVariant.getQty());
                existingCartLineItem.setDiscountOnHkPrice(existingCartLineItem.getDiscountOnHkPrice() + (freeVariant.getHkPrice() * freeVariant.getQty()));
                return cartLineItemService.save(existingCartLineItem);
            }
        }
        return null;
    }

    @Transactional
    private Set<CartLineItem> getCartLineItemsFromPricingCartLi(Order order, Set<CartLineItem> cartLineItems) {
        Set<CartLineItem> finalCartLineItems = new HashSet<CartLineItem>();
        for (CartLineItem cartLineItem : cartLineItems) {
            cartLineItem.setOrder(order);
            OrderUtil.roundOffPricesOnCartLineItem(cartLineItem);
            cartLineItem = getCartLineItemService().save(cartLineItem);
            finalCartLineItems.add(cartLineItem);
        }
        return finalCartLineItems;
    }

    @Transactional
    private CartLineItem createCodLineItem(Order order, Double codAmount) {
        Set<CartLineItem> cartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.CodCharges).filter();
        CartLineItem codLine = null;
        if (cartLineItems == null || cartLineItems.size() == 0) {
            CartLineItem cartLineItem = new CartLineItemBuilder().ofType(EnumCartLineItemType.CodCharges)
            // .tax(serviceTaxProvider.get())
            .hkPrice(codAmount).build();
            cartLineItem.setOrder(order);
            codLine = cartLineItem;
        } else {
            for (CartLineItem cartLineItem : cartLineItems) {
                cartLineItem.setHkPrice(codAmount);
                cartLineItem.setOrder(order);
                cartLineItem.setQty(1L);
            }
            codLine = cartLineItems.iterator().next();
        }

        return getCartLineItemService().save(codLine);
        // cartLineItem.setLineItemStatus(lineItemStatusDao.find(EnumLineItemStatus.NA.getId()));
        // return cartLineItemService.save(cartLineItem);
    }

    public Order recalAndUpdateAmount(Order order) {
        OfferInstance offerInstance = order.getOfferInstance();
        PricingDto pricingDto = new PricingDto(getPricingEngine().calculatePricing(order.getCartLineItems(), offerInstance, order.getAddress(), order.getRewardPointsUsed()),
                order.getAddress());
        order.setAmount(pricingDto.getGrandTotalPayable());

        // set order as referred order if this order is using referral coupon and availing discount as well
        // if (order.getUser().getReferredBy() != null && offerInstance != null && offerInstance.getCoupon() != null &&
        // offerInstance.getCoupon().getReferrerUser() != null && pricingDto.getTotalDiscount() > 0) {
        // order.setReferredOrder(true);
        // }

        return getOrderService().save(order);
    }

    public Order orderPaymentAuthPending(Payment payment) {
        return orderPaymentReceieved(payment);
    }

    public Order trimEmptyLineItems(Order order) {
        if (order != null && order.getCartLineItems() != null && !(order.getCartLineItems()).isEmpty()) {
            for (Iterator<CartLineItem> iterator = order.getCartLineItems().iterator(); iterator.hasNext();) {
                CartLineItem lineItem = iterator.next();
                if (lineItem.getLineItemType().getId().equals(EnumCartLineItemType.Product.getId())) {
                    if (lineItem.getQty() <= 0) {
                        iterator.remove();
                        getCartLineItemDao().delete(lineItem);
                    } else {
                        ProductVariant productVariant = lineItem.getProductVariant();
                        Product product = productVariant.getProduct();
                        boolean isService = false;
                        if (product.isService() != null && product.isService())
                            isService = true;
                        boolean isJit = false;
                        if (product.isJit() != null && product.isJit())
                            isJit = true;
                        if (!isJit && !isService) {
                            List<Sku> skuList = skuService.getSKUsForProductVariant(productVariant);
                            if (skuList != null && !skuList.isEmpty()) {
                                Long unbookedInventory = inventoryService.getAvailableUnbookedInventory(skuList);
                                if (unbookedInventory != null && unbookedInventory < lineItem.getQty()) {
                                    lineItem.setQty(unbookedInventory);
                                    cartLineItemService.save(lineItem);
                                    logger.debug("Set LineItem Qty equals to available unbooked Inventory: " + unbookedInventory + " for Variant:" + productVariant.getId());
                                }
                            }
                        }
                        cartLineItemService.save(lineItem);
                    }
                }
            }
            order = getOrderService().save(order);
        }
        return order;
    }

    public boolean isStepUpAllowed(CartLineItem cartLineItem) {
        ProductVariant productVariant = cartLineItem.getProductVariant();
        Product product = productVariant.getProduct();
        boolean isService = false;
        if (product.isService() != null && product.isService())
            isService = true;
        boolean isJit = false;
        if (product.isJit() != null && product.isJit())
            isJit = true;
        if (!isJit && !isService) {
            Long unbookedInventory = inventoryService.getAvailableUnbookedInventory(skuService.getSKUsForProductVariant(productVariant));
            if (unbookedInventory != null && unbookedInventory > 0 && unbookedInventory < cartLineItem.getQty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method is responsible for updating the order status to shipped and sending order shipped emails. Email could
     * be order shiiped email order shipped email in parts or partial order shipped email. <p/> To check what type of
     * email is to be send and whether the order status is to be changed to shipped or not. We are checking the status
     * of each shipping line belonging to the order. If any of the shipping line has status
     * Printer/DoodleQueue/Processing it means that order status will not be set to shipped and order shipped email
     * whether total or in parts will not be send. Whether to send the partial order shipped email will depend on field
     * "shippedButEmailPendingShippingLines" if shippedButEmailPendingShippingLines if greater than zero than this is
     * the case of sending partial order shipped email. <p/> Pseudo Code: if changeOrderStatusToShipped = true: if
     * shippedButEmailPendingShippingLines == totalShippingLinesInorder send Order Shipped Email else send Order Shipped
     * in parts email else if shippedButEmailPendingShippingLines > 0 send partial order shipped email
     * 
     * @param shippingOrderList
     * @param loggedOnUser
     */
    public void updateOrderStatusToShippedAndSendEmails(List<ShippingOrder> shippingOrderList, User loggedOnUser) {
        // TODO: # warehouse fix this.

        /*
         * for (ShippingOrder shippingOrder : shippingOrderList) { boolean sendCompleteOrderShippedEmail = true; int
         * shippedButEmailPendingLineItems = 0; int totalLineItemsInOrder = 0; for (LineItem lineItem :
         * shippingOrder.getLineItems(EnumLineItemType.Product)) { if
         * (lineItem.getLineItemStatus().getId().equals(EnumLineItemStatus.CHECKEDOUT.getId()) ||
         * lineItem.getLineItemStatus().getId().equals(EnumLineItemStatus.ACTION_AWAITING.getId())) {
         * sendCompleteOrderShippedEmail = false; } else if
         * (lineItem.getLineItemStatus().getId().equals(EnumLineItemStatus.HANDED_OVER_TO_COURIER.getId())) { if
         * (lineItem.isShippedEmailSent() == null || !lineItem.isShippedEmailSent()) { logger.info("Shipping line item " +
         * lineItem.getId() + " with courier " + lineItem.getCourier());
         * lineItem.setTrackLink(getLinkManager().getOrderTrackLink(lineItem.getTrackingId(),
         * lineItem.getCourier().getId()));
         * lineItem.setLineItemStatus(lineItemStatusDao.find(EnumLineItemStatus.SHIPPED.getId()));
         * //lineItem.setShipDate(BaseUtils.getCurrentTimestamp()); lineItemDaoProvider.get().save(lineItem);
         * shippedButEmailPendingLineItems++; } } totalLineItemsInOrder++; } if (sendCompleteOrderShippedEmail) { if
         * (shippedButEmailPendingLineItems == totalLineItemsInOrder) { sendOrderShippedEmail(order, true);
         * logger.info("total order shipped ............................................. order id: " + order.getId()); }
         * else { sendOrderShippedEmail(order, false); logger.info("total order shipped in
         * parts.............................................order id: " + order.getId()); } if
         * (!order.getOrderStatus().getId().equals(EnumOrderStatus.Shipped.getId())) {
         * order.setOrderStatus(orderStatusDao.find(EnumOrderStatus.Shipped.getId())); getOrderService().save(order); } }
         * else if (shippedButEmailPendingLineItems > 0) { sendPartialShippingEmail(order); logger.info("partial order
         * shipped .............................................order id: " + order.getId()); }
         *//**
             * Order lifecycle activity logging - Order Shipped
             */
        /*
         * this.logOrderActivity(order, loggedOnUser,
         * orderLifecycleActivityDaoProvider.get().find(EnumOrderLifecycleActivity.OrderShippedEmailFired.getId()),
         * null); }
         */
    }

    @Deprecated
    public Double getNetDiscountOnLineItem(CartLineItem lineItem) {
        return this.getCouponDiscountOnLineItem(lineItem) + this.getProRatedRedeemPointsLineItem(lineItem);
    }

    @Deprecated
    public Double getCouponDiscountOnLineItem(CartLineItem lineItem) {
        /*
         * Order order = lineItem.getShippingOrder(); Double couponDiscount =
         * lineItemDaoProvider.get().getDiscountOnLineItem(lineItem, order); if (couponDiscount == null) {
         * couponDiscount = 0.0; } return couponDiscount;
         */

        // TODO: # warehouse fix this.
        return null;
    }

    @Deprecated
    public Double getProRatedRedeemPointsLineItem(CartLineItem lineItem) {
        Order order = lineItem.getOrder();
        Double rewardDiscount = 0.0;
        if (order.getRewardPointsUsed() != null) {
            Double hkTotal = 0.0;
            Set<CartLineItem> productCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
            for (CartLineItem productLineItem : productCartLineItems) {
                hkTotal += productLineItem.getQty() * productLineItem.getHkPrice();
            }
            rewardDiscount = ((lineItem.getQty() * lineItem.getHkPrice()) / hkTotal) * order.getRewardPointsUsed();
        }
        return rewardDiscount;
    }

    @Deprecated
    public Double getNetDiscountOnLineItemPerUnit(CartLineItem lineItem, Order order) {
        return this.getCouponDiscountOnLineItemPerUnit(lineItem, order) + this.getProRatedRedeemPointsLineItemPerUnit(lineItem, order);
    }

    @Deprecated
    public Double getCouponDiscountOnLineItemPerUnit(CartLineItem lineItem, Order order) {
        // Order order = lineItem.getShippingOrder();
        /*
         * Double couponDiscountPerUnit = 0D; Double couponDiscount =
         * lineItemDaoProvider.get().getDiscountOnLineItem(lineItem, order); if (lineItem.getQty() != null &&
         * lineItem.getQty() > 0 && couponDiscount != null) { couponDiscountPerUnit = couponDiscount /
         * lineItem.getQty(); } return couponDiscountPerUnit;
         */
        // TODO: # warehouse fix this.
        return null;
    }

    @Deprecated
    public Double getProRatedRedeemPointsLineItemPerUnit(CartLineItem lineItem, Order order) {
        // Order order = lineItem.getShippingOrder();
        Double rewardDiscountPerUnit = 0.0;
        if (order.getRewardPointsUsed() != null) {
            Double hkTotal = 0.0;
            Set<CartLineItem> productCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
            for (CartLineItem productLineItem : productCartLineItems) {
                hkTotal += productLineItem.getQty() * productLineItem.getHkPrice();
            }
            if (hkTotal > 0) {
                rewardDiscountPerUnit = (lineItem.getHkPrice() / hkTotal) * order.getRewardPointsUsed();
            }
        }
        return rewardDiscountPerUnit;

    }

    /**
     * This method need to be re-written as multiple wh would be having different tax rates hence different margins
     * 
     * @param order
     * @return
     */
    public Double getNetMarginOnOrder(Order order) {
        Double margin = 0.0;
        try {
            Set<CartLineItem> productCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
            for (CartLineItem lineItem : productCartLineItems) {
                List<Sku> skuList = getSkuService().getSKUsForProductVariant(lineItem.getProductVariant());
                if (skuList != null && !skuList.isEmpty()) {
                    Sku sku = skuList.get(0);
                    Double costPrice = 0.0;
                    if (lineItem.getProductVariant().getCostPrice() != null) {
                        costPrice = lineItem.getProductVariant().getCostPrice();
                    } else {
                        costPrice = lineItem.getHkPrice();
                    }
                    Double taxPaid = 0.0;
                    Double taxRecoverable = 0.0;
                    Supplier supplier = getInventoryService().getSupplierForSKU(sku);
                    if (supplier != null && costPrice != null) {
                        if (supplier.getState().equalsIgnoreCase("haryana")) {
                            Double surcharge = 0.05;
                            taxPaid = costPrice * sku.getTax().getValue() * (1 + surcharge);
                        } else {
                            // Double surcharge = 0.0; // CST Surcharge
                            Double cst = 0.02; // CST
                            taxPaid = costPrice * cst;
                        }
                        if (supplier.getState().equalsIgnoreCase("haryana")) {
                            taxRecoverable = taxPaid;
                        }
                    }
                    margin += ((lineItem.getHkPrice() * (1 - sku.getTax().getValue() * (1 + 0.05)) - (costPrice + taxPaid - taxRecoverable)) * lineItem.getQty() - this.getNetDiscountOnLineItem(lineItem));
                }
            }
        } catch (Exception e) {
            logger.error("Error while calculating margin for order - " + order.getId());
            logger.error(e.getMessage(), e);
        }

        return margin;
    }

    public CartLineItemService getCartLineItemService() {
        return cartLineItemService;
    }

    public void setCartLineItemService(CartLineItemService cartLineItemService) {
        this.cartLineItemService = cartLineItemService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public PaymentService getPaymentService() {
        return paymentService;
    }

    public void setPaymentService(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public KarmaProfileService getKarmaProfileService() {
        return karmaProfileService;
    }

    public void setKarmaProfileService(KarmaProfileService karmaProfileService) {
        this.karmaProfileService = karmaProfileService;
    }

    public AffilateService getAffilateService() {
        return affilateService;
    }

    public void setAffilateService(AffilateService affilateService) {
        this.affilateService = affilateService;
    }

    public RewardPointService getRewardPointService() {
        return rewardPointService;
    }

    public void setRewardPointService(RewardPointService rewardPointService) {
        this.rewardPointService = rewardPointService;
    }

    public ShippingOrderService getShippingOrderService() {
        return shippingOrderService;
    }

    public void setShippingOrderService(ShippingOrderService shippingOrderService) {
        this.shippingOrderService = shippingOrderService;
    }

    public EmailManager getEmailManager() {
        return emailManager;
    }

    public void setEmailManager(EmailManager emailManager) {
        this.emailManager = emailManager;
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }

    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public PricingEngine getPricingEngine() {
        return pricingEngine;
    }

    public void setPricingEngine(PricingEngine pricingEngine) {
        this.pricingEngine = pricingEngine;
    }

    public LineItemDao getLineItemDao() {
        return lineItemDao;
    }

    public void setLineItemDao(LineItemDao lineItemDao) {
        this.lineItemDao = lineItemDao;
    }

    public CartLineItemDao getCartLineItemDao() {
        return cartLineItemDao;
    }

    public void setCartLineItemDao(CartLineItemDao cartLineItemDao) {
        this.cartLineItemDao = cartLineItemDao;
    }

    public LinkManager getLinkManager() {
        return linkManager;
    }

    public void setLinkManager(LinkManager linkManager) {
        this.linkManager = linkManager;
    }

    public ReferrerProgramManager getReferrerProgramManager() {
        return referrerProgramManager;
    }

    public void setReferrerProgramManager(ReferrerProgramManager referrerProgramManager) {
        this.referrerProgramManager = referrerProgramManager;
    }

    public SkuService getSkuService() {
        return skuService;
    }

    public void setSkuService(SkuService skuService) {
        this.skuService = skuService;
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }

    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public OrderStatusService getOrderStatusService() {
        return orderStatusService;
    }

    public void setOrderStatusService(OrderStatusService orderStatusService) {
        this.orderStatusService = orderStatusService;
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

    public ProductVariantService getProductVariantService() {
        return productVariantService;
    }

    public void setProductVariantService(ProductVariantService productVariantService) {
        this.productVariantService = productVariantService;
    }

    public OrderLoggingService getOrderLoggingService() {
        return orderLoggingService;
    }

    public void setOrderLoggingService(OrderLoggingService orderLoggingService) {
        this.orderLoggingService = orderLoggingService;
    }

    /*
     * @Transactional public void escalateFromActionQueue(CartLineItem lineItem, Courier suggestedCourier) { if
     * (lineItem.getLineItemType().getId().equals(EnumLineItemType.Product.getId())) {
     * lineItem.setLineItemStatus(EnumLineItemStatus.READY_FOR_PROCESS.asLineItemStatus());
     * lineItem.setCourier(suggestedCourier); lineItemDaoProvider.get().save(lineItem); } } @Transactional public void
     * escalateFromActionQueue(CartLineItem lineItem) { if
     * (lineItem.getLineItemType().getId().equals(EnumLineItemType.Product.getId())) {
     * lineItem.setLineItemStatus(EnumLineItemStatus.READY_FOR_PROCESS.asLineItemStatus());
     * lineItemDaoProvider.get().save(lineItem); } } /* @Transactional public Order putOrderOnHold(Order order, User
     * loggedOnUser) { order.setOrderStatus(orderStatusDao.find(EnumOrderStatus.OnHold.getId())); order =
     * getOrderService().save(order); List<Long> applicableLineItemStatusId = new ArrayList<Long>();
     * applicableLineItemStatusId.add(EnumLineItemStatus.READY_FOR_PROCESS.getId());
     * applicableLineItemStatusId.add(EnumLineItemStatus.GONE_FOR_PRINTING.getId());
     * applicableLineItemStatusId.add(EnumLineItemStatus.PICKING.getId());
     * applicableLineItemStatusId.add(EnumLineItemStatus.CHECKEDOUT.getId());
     * applicableLineItemStatusId.add(EnumLineItemStatus.PACKED.getId()); for (CartLineItem lineItem :
     * order.getLineItems()) { if (lineItem.getLineItemType().getId().equals(EnumLineItemType.Product.getId()) &&
     * applicableLineItemStatusId.contains(lineItem.getLineItemStatus().getId())) {
     * lineItem.setLineItemStatus(EnumLineItemStatus.ACTION_AWAITING.asLineItemStatus());
     * lineItemDaoProvider.get().save(lineItem); //Recheckin Inventory against checked out qty if
     * (lineItem.getLineItemStatus().getId().equals(EnumLineItemStatus.CHECKEDOUT.getId())) { List<ProductVariantInventory>
     * checkedOutInventories =
     * productVariantInventoryDaoProvider.get().getCheckedOutSkuItems(lineItem.getShippingOrder(),
     * lineItem.getProductVariant()); for (ProductVariantInventory checkedOutInventory : checkedOutInventories) {
     * inventoryService.inventoryCheckinCheckout(checkedOutInventory.getProductVariant(),
     * checkedOutInventory.getSkuItem(), lineItem, lineItem.getShippingOrder(), null, null,
     * invTxnTypeDaoProvider.get().find(EnumInvTxnType.CANCEL_CHECKIN.getId()), 1L, loggedOnUser); } } } }
     *//*
         * LineItemStatus statusActionAwaiting =
         * lineItemStatusDaoProvider.get().find(EnumLineItemStatus.ACTION_AWAITING.getId()); for (LineItem line :
         * order.getCartLineItems(EnumLineItemType.Product)) { line.setLineItemStatus(statusActionAwaiting);
         * lineItemDaoProvider.get().save(line); }
         *//*

     */

    /**
     * Order lifecycle activity logging - Order Put OnHold
     */
    /*
     * orderService.logOrderActivity(order, loggedOnUser,
     * orderLifecycleActivityDaoProvider.get().find(EnumOrderLifecycleActivity.OrderPutOnHold.getId()), null); return
     * order; }
     */

    /*
     * //logic for cashback offer public void awardRewardPoints(Order order) { OfferInstance offerInstance =
     * order.getOfferInstance(); if (offerInstance != null) { Offer offer = order.getOfferInstance().getOffer(); if
     * (offer != null) { OfferAction offerAction = offer.getOfferAction(); OfferTrigger offerTrigger =
     * offer.getOfferTrigger(); Double applicableOrderAmount = 0.0D; Double cashbackLimit =
     * offerAction.getRewardPointCashbackLimit(); Double cashbackPercent = offerAction.getRewardPointDiscountPercent();
     * ProductGroup productGroup = order.getOfferInstance().getOffer().getOfferAction().getProductGroup(); if
     * (offerTrigger != null) { OfferTriggerMatcher triggerMatcher =
     * offerTrigger.easyMatch(order.getProductCartLineItems()); if
     * (triggerMatcher.hasEasyMatch(offer.isExcludeTriggerProducts())) { for (CartLineItem lineItem :
     * order.getProductCartLineItems()) { if (productGroup == null ||
     * productGroup.contains(lineItem.getProductVariant())) { applicableOrderAmount += lineItem.getHkPrice() *
     * lineItem.getQty(); } } } } Double applicableDiscount = applicableOrderAmount * (cashbackPercent != null ?
     * cashbackPercent : 0.0); if (rewardPointDao.findByReferredOrderAndRewardMode(order,
     * rewardPointModeDaoProvider.get().find(EnumRewardPointMode.HK_CASHBACK.getId())) == null) { if
     * (Boolean.TRUE.equals(offerInstance.getOffer().getOfferAction().isCashback()) && applicableDiscount > 0.00) { if
     * (order.getPayment().getPaymentMode().equals(paymentModeDao.find(EnumPaymentMode.TECHPROCESS.getId())) ||
     * order.getPayment().getPaymentMode().equals(paymentModeDao.find(EnumPaymentMode.CITRUS.getId()))) { RewardPoint
     * techProcessRewardPoint = rewardPointDaoProvider.get().addRewardPoints(order.getUser(), null, order, cashbackLimit !=
     * null ? applicableDiscount < cashbackLimit ? applicableDiscount : cashbackLimit : applicableDiscount, "Cashback
     * Offer", EnumRewardPointStatus.APPROVED,
     * rewardPointModeDaoProvider.get().find(EnumRewardPointMode.HK_CASHBACK.getId()));
     * referrerProgramManagerProvider.get().approveRewardPoints(Arrays.asList(techProcessRewardPoint), new
     * DateTime().plusMonths(1).toDate()); } else { RewardPoint notTechProcessRewardPoint =
     * rewardPointDaoProvider.get().addRewardPoints(order.getUser(), null, order, cashbackLimit != null ?
     * applicableDiscount < cashbackLimit ? applicableDiscount : cashbackLimit : applicableDiscount, "Cashback Offer",
     * EnumRewardPointStatus.PENDING, rewardPointModeDaoProvider.get().find(EnumRewardPointMode.HK_CASHBACK.getId()));
     * referrerProgramManagerProvider.get().approveRewardPoints(Arrays.asList(notTechProcessRewardPoint), new
     * DateTime().plusMonths(1).toDate()); } } } order.getOfferInstance().setActive(false);
     * offerInstanceDaoProvider.get().save(order.getOfferInstance()); } } }
     */

    /*
     * public void generateServiceInvoiceForLineItems(Order order, List<CartLineItem> lineItems) { AccountingInvoice
     * accountingInvoice = new AccountingInvoice(); accountingInvoice.setShippingOrder(order); Long serviceInvoiceId =
     * accountingInvoiceDaoProvider.get().getLastServiceInvoiceId(); if (serviceInvoiceId != null) { serviceInvoiceId +=
     * 1L; } else { serviceInvoiceId = 1L; } accountingInvoice.setServiceInvoiceId(serviceInvoiceId);
     * accountingInvoice.setInvoiceDate(new Date()); accountingInvoice =
     * accountingInvoiceDaoProvider.get().save(accountingInvoice); for (CartLineItem lineItem : lineItems) {
     * InvoiceLineItem invoiceLineItem = new InvoiceLineItem(); invoiceLineItem.setLineItem(lineItem);
     * invoiceLineItem.setAccountingInvoice(accountingInvoice); invoiceLineItemDaoProvider.get().save(invoiceLineItem); } }
     */

    /*
     * public boolean invoiceAlreadyGenerated(ShippingOrder shippingOrer) { List<AccountingInvoice> accountingInvoices =
     * order.getAccountingInvoices(); if (accountingInvoices != null && !accountingInvoices.isEmpty()) {
     * AccountingInvoice latestInvoice = accountingInvoices.get(0); List<CartLineItem> lineItemsInInvoice = new
     * ArrayList<CartLineItem>(); for (InvoiceLineItem invoiceLineItem : latestInvoice.getInvoiceLineItems()) {
     * lineItemsInInvoice.add(invoiceLineItem.getLineItem()); } List<CartLineItem> inProcessLineItems =
     * order.getLineItemsOfTypeAndStatus(EnumLineItemType.Product, EnumLineItemStatus.CHECKEDOUT); if
     * (lineItemsInInvoice.containsAll(inProcessLineItems)) { return true; } } return false; }
     */

    /*
     * @Deprecated // cod not available if either courier service not available there or order is exclusively service
     *             order --> changing to if any service is there public boolean isCodAllowed(Order order) { return
     *             (courierServiceInfoDao.isCodAvailable(order.getAddress().getPin())); }
     */

    /*
     * @Deprecated public Page getSuccessfulOrders(int pageNo, int perPage) { return
     *             getOrderService().findOrdersByOrderStatusAndLineItemStatus(Arrays.asList(EnumLineItemStatus.values()),
     *             Arrays.asList(EnumOrderStatus.Pending, EnumOrderStatus.OnHold, EnumOrderStatus.Shipped,
     *             EnumOrderStatus.Delivered), false, pageNo, perPage); }
     */
    /*
     * @Deprecated public Page getActionAwaitingOrders(int pageNo, int perPage) { return
     *             getOrderService().findOrdersByOrderStatusAndLineItemStatus(Arrays.asList(EnumLineItemStatus.ACTION_AWAITING),
     *             Arrays.asList(EnumOrderStatus.Pending, EnumOrderStatus.OnHold), false, pageNo, perPage); }
     * @Deprecated public Page<Order> getProcessingQueueOrders(int pageNo, int perPage) { return
     *             getOrderService().findOrdersByOrderStatusAndLineItemStatus(
     *             Arrays.asList(EnumLineItemStatus.READY_FOR_PROCESS), Arrays.asList(EnumOrderStatus.Pending), false,
     *             pageNo, perPage); }
     */

    /*
     * @Deprecated public Page<Order> getPrintPickQueueOrders(int pageNo, int perPage) { return
     *             getOrderService().findOrdersByOrderStatusAndLineItemStatus(
     *             Arrays.asList(EnumLineItemStatus.GONE_FOR_PRINTING), Arrays.asList(EnumOrderStatus.Pending), false,
     *             pageNo, perPage); }
     */

    /*
     * @Deprecated public Page<Order> getShipmentQueueOrders(int pageNo, int perPage) { return
     *             getOrderService().findOrdersByOrderStatusAndLineItemStatus( Arrays.asList(EnumLineItemStatus.PACKED),
     *             Arrays.asList(EnumOrderStatus.Pending), false, pageNo, perPage); }
     */

    /*
     * @Deprecated public Page<Order> getDelivereyQueueOrders(int pageNo, int perPage) { return
     *             getOrderService().findOrdersByOrderStatusAndLineItemStatus( Arrays.asList(EnumLineItemStatus.SHIPPED,
     *             EnumLineItemStatus.DELIVERED, EnumLineItemStatus.RETURNED, EnumLineItemStatus.LOST),
     *             Arrays.asList(EnumOrderStatus.Shipped), false, pageNo, perPage); }
     */

    /*
     * @Deprecated public Page<Order> getShipmentEmailQueueOrders(int pageNo, int perPage) { return
     *             getOrderService().findOrdersByOrderStatusAndLineItemStatus(Arrays.asList(EnumLineItemStatus.SHIPPED),
     *             Arrays.asList(EnumOrderStatus.Pending), false, pageNo, perPage); }
     */

    /*
     * @Deprecated public List<Order> getOrdersForSendingShippingEmails() { return
     *             getOrderService().findOrdersForSendingShippingEmails(false); }
     */
    /*
     * public List<Courier> getAvailableCouriers(Order order) { boolean isCOD = false; if (order.getPayment() == null) {
     * isCOD = false; } else if
     * (order.getPayment().getPaymentMode().equals(paymentModeDao.find(EnumPaymentMode.COD.getId()))) { isCOD = true; }
     * return courierServiceInfoDaoProvider.get().getCouriersForPincode(order.getAddress().getPin(), isCOD); }
     */
    /*
     * public List<OrderLifecycleStateTransitionDto> getOrderLifecycleStateTransitionDtoList(Date startDate, Date
     * endDate) { List<OrderLifecycleStateTransitionDto> stateTransitionDtos = new ArrayList<OrderLifecycleStateTransitionDto>();
     * Calendar calendar = Calendar.getInstance(); calendar.setTime(startDate); while
     * (calendar.getTime().before(endDate)) { Set<Long> placedOrders = new HashSet<Long>(); Set<Long> tpslOrders =
     * new HashSet<Long>(); Set<Long> codOrders = new HashSet<Long>(); Set<Long> confirmedCODOrders = new HashSet<Long>();
     * Set<Long> escalableOrders = new HashSet<Long>(); Set<Long> ordersEscalatedToPackingQeueue = new HashSet<Long>();
     * Set<Long> ordersPushedBackToActionQueue = new HashSet<Long>(); Set<Long> ordersEscalatedToShipmentQueue = new
     * HashSet<Long>(); Set<Long> shippedOrders = new HashSet<Long>(); if (calendar.get(Calendar.DAY_OF_WEEK) !=
     * Calendar.SUNDAY) { List<Long> orderIds = getOrderService().getActivityPerformedOrderIds(calendar.getTime(),
     * EnumOrderLifecycleActivity.OrderPlaced.getId(), -1, 16, 0, 16);//4PM if (orderIds != null && orderIds.size() > 0) {
     * placedOrders.addAll(orderIds); List<Long> tpslOrderList =
     * getOrderService().getOrdersByPaymentModeAndStatus(orderIds, Arrays.asList(EnumPaymentMode.TECHPROCESS.getId(),
     * EnumPaymentMode.CITRUS.getId()), Arrays.asList(EnumPaymentStatus.SUCCESS.getId()));
     * tpslOrders.addAll(tpslOrderList); List<Long> codOrderList =
     * getOrderService().getOrdersByPaymentModeAndStatus(orderIds, Arrays.asList(EnumPaymentMode.COD.getId()),
     * Arrays.asList(EnumPaymentStatus.AUTHORIZATION_PENDING.getId(), EnumPaymentStatus.ON_DELIVERY.getId()));
     * codOrders.addAll(codOrderList); List<Long> confirmedCODOrderList =
     * getOrderService().getActivityPerformedBOCount(codOrderList, calendar.getTime(),
     * EnumOrderLifecycleActivity.ConfirmedAuthorization.getId(), -1, 16, 15, 0, 16, 15); //4:15PM
     * confirmedCODOrders.addAll(confirmedCODOrderList); List<Long> escalableOrderList = new ArrayList<Long>();
     * escalableOrderList.addAll(tpslOrderList); escalableOrderList.addAll(confirmedCODOrderList);
     * escalableOrders.addAll(escalableOrderList); List<Long> orderEscalatedToPackingQeueueList =
     * getOrderService().getActivityPerformedBOCount(escalableOrderList, calendar.getTime(),
     * EnumOrderLifecycleActivity.EscalatedPartiallyToProcessingQueue.getId(), -1, 16, 30, 0, 16, 30);//4:30PM
     * orderEscalatedToPackingQeueueList.addAll(orderDao.getActivityPerformedBOCount(escalableOrderList,
     * calendar.getTime(), EnumOrderLifecycleActivity.EscalatedToProcessingQueue.getId(), -1, 16, 30, 0, 16,
     * 30));//4:30PM ordersEscalatedToPackingQeueue.addAll(orderEscalatedToPackingQeueueList); List<Long>
     * orderPushedBackToActionQueueList =
     * getOrderService().getActivityPerformedBOCount(orderEscalatedToPackingQeueueList, calendar.getTime(),
     * EnumOrderLifecycleActivity.EscalatedBackToAwaitingQueue.getId(), -1, 16, 30, 0, 24, 0);
     * ordersPushedBackToActionQueue.addAll(orderPushedBackToActionQueueList); List<Long>
     * orderEscalatedToShipmentQueueList =
     * getOrderService().getActivityPerformedBOCount(orderEscalatedToPackingQeueueList, calendar.getTime(),
     * EnumOrderLifecycleActivity.EscalatedToShipmentQueue.getId(), -1, 16, 30, 0, 24, 0);
     * ordersEscalatedToShipmentQueue.addAll(orderEscalatedToShipmentQueueList); List<Long> shippedOrderList =
     * getOrderService().getActivityPerformedBOCount(orderIds, calendar.getTime(),
     * EnumOrderLifecycleActivity.OrderHandedOverToCourier.getId(), -1, 16, 30, 0, 24, 0);
     * shippedOrders.addAll(shippedOrderList); } } else if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) { //
     * We will merge Sunday and Monday data here List<Long> orderIds =
     * getOrderService().getActivityPerformedOrderIds(calendar.getTime(),
     * EnumOrderLifecycleActivity.OrderPlaced.getId(), -1, 16, 1, 16); //4PM if (orderIds != null && orderIds.size() >
     * 0) { placedOrders.addAll(orderIds); List<Long> tpslOrderList =
     * getOrderService().getOrdersByPaymentModeAndStatus(orderIds, Arrays.asList(EnumPaymentMode.TECHPROCESS.getId(),
     * EnumPaymentMode.CITRUS.getId()), Arrays.asList(EnumPaymentStatus.SUCCESS.getId()));
     * tpslOrders.addAll(tpslOrderList); List<Long> codOrderList =
     * getOrderService().getOrdersByPaymentModeAndStatus(orderIds, Arrays.asList(EnumPaymentMode.COD.getId()),
     * Arrays.asList(EnumPaymentStatus.AUTHORIZATION_PENDING.getId(), EnumPaymentStatus.ON_DELIVERY.getId()));
     * codOrders.addAll(codOrderList); List<Long> confirmedCODOrderList =
     * getOrderService().getActivityPerformedBOCount(codOrderList, calendar.getTime(),
     * EnumOrderLifecycleActivity.ConfirmedAuthorization.getId(), -1, 16, 15, 1, 16, 15); //4:15PM
     * confirmedCODOrders.addAll(confirmedCODOrderList); List<Long> escalableOrderList = new ArrayList<Long>();
     * escalableOrderList.addAll(tpslOrderList); escalableOrderList.addAll(confirmedCODOrderList);
     * escalableOrders.addAll(escalableOrderList); List<Long> orderEscalatedToPackingQeueueList =
     * getOrderService().getActivityPerformedBOCount(escalableOrderList, calendar.getTime(),
     * EnumOrderLifecycleActivity.EscalatedPartiallyToProcessingQueue.getId(), -1, 16, 30, 1, 16, 30); //4:30PM
     * orderEscalatedToPackingQeueueList.addAll(orderDao.getActivityPerformedBOCount(escalableOrderList,
     * calendar.getTime(), EnumOrderLifecycleActivity.EscalatedToProcessingQueue.getId(), -1, 16, 30, 1, 16, 30));
     * //4:30PM ordersEscalatedToPackingQeueue.addAll(orderEscalatedToPackingQeueueList); List<Long>
     * orderPushedBackToActionQueueList =
     * getOrderService().getActivityPerformedBOCount(orderEscalatedToPackingQeueueList, calendar.getTime(),
     * EnumOrderLifecycleActivity.EscalatedBackToAwaitingQueue.getId(), -1, 16, 30, 1, 24, 0);
     * ordersPushedBackToActionQueue.addAll(orderPushedBackToActionQueueList); List<Long>
     * orderEscalatedToShipmentQueueList =
     * getOrderService().getActivityPerformedBOCount(orderEscalatedToPackingQeueueList, calendar.getTime(),
     * EnumOrderLifecycleActivity.EscalatedToShipmentQueue.getId(), -1, 16, 30, 1, 24, 0);
     * ordersEscalatedToShipmentQueue.addAll(orderEscalatedToShipmentQueueList); List<Long> shippedOrderList =
     * getOrderService().getActivityPerformedBOCount(orderIds, calendar.getTime(),
     * EnumOrderLifecycleActivity.OrderHandedOverToCourier.getId(), -1, 16, 30, 1, 24, 0);
     * shippedOrders.addAll(shippedOrderList); } calendar.add(Calendar.HOUR, 24); } OrderLifecycleStateTransitionDto
     * orderLifecycleStateTransitionDto = new OrderLifecycleStateTransitionDto();
     * orderLifecycleStateTransitionDto.setActivityDate(calendar.getTime());
     * orderLifecycleStateTransitionDto.setPlacedOrders(placedOrders);
     * orderLifecycleStateTransitionDto.setTpslOrders(tpslOrders);
     * orderLifecycleStateTransitionDto.setCodOrders(codOrders);
     * orderLifecycleStateTransitionDto.setConfirmedCODOrders(confirmedCODOrders);
     * orderLifecycleStateTransitionDto.setEscalableOrders(escalableOrders);
     * orderLifecycleStateTransitionDto.setOrdersEscalatedToPackingQeueue(ordersEscalatedToPackingQeueue);
     * orderLifecycleStateTransitionDto.setOrdersPushedBackToActionQueue(ordersPushedBackToActionQueue);
     * orderLifecycleStateTransitionDto.setOrdersEscalatedToShipmentQueue(ordersEscalatedToShipmentQueue);
     * orderLifecycleStateTransitionDto.setShippedOrders(shippedOrders);
     * stateTransitionDtos.add(orderLifecycleStateTransitionDto); calendar.add(Calendar.HOUR, 24); } return
     * stateTransitionDtos; }
     */

    /*
     * public Courier getSuggestedCourierService(Order order) { boolean isCOD = false; if
     * (order.getPayment().getPaymentMode().equals(paymentModeDao.find(EnumPaymentMode.COD.getId()))) { isCOD = true; }
     * String pincode = order.getAddress().getPin(); Pincode pincodeObj =
     * pincodeDaoProvider.get().getByPincode(pincode); if (pincodeObj != null) { Courier courier = null; if
     * (pincode.startsWith("122")) { // Gurgaon courier = courierDaoProvider.get().find(EnumCourier.Delhivery.getId());
     * return courier; } else if (pincode.startsWith("110")) { // New Delhi Integer random = (new
     * Random()).nextInt(100); if (random % 2 == 0) { courier =
     * courierDaoProvider.get().find(EnumCourier.Chhotu.getId()); } else { courier =
     * courierDaoProvider.get().find(EnumCourier.Delhivery.getId()); } return courier; } else if
     * (pincode.startsWith("2013")) { // Noida courier = courierDaoProvider.get().find(EnumCourier.Delhivery.getId());
     * return courier; } else { Courier stateCourier =
     * stateCourierServiceDaoProvider.get().getPreferredCourierForState(pincodeObj.getState()); if (stateCourier !=
     * null) { CourierServiceInfo courierServiceInfo =
     * courierServiceInfoDaoProvider.get().findByPincodeAndCourier(pincode, stateCourier, isCOD); if (courierServiceInfo !=
     * null) { return stateCourier; } else { return selectCourierViaRoundRobin(order); } } else { return
     * selectCourierViaRoundRobin(order); } } } return null; }
     */
    /*
     * public Courier selectCourierViaRoundRobin(Order order) { Courier dtdcPlus =
     * courierDaoProvider.get().find(EnumCourier.DTDC_Plus.getId()); Courier dtdcLite =
     * courierDaoProvider.get().find(EnumCourier.DTDC_Lite.getId()); Courier dtdcCOD =
     * courierDaoProvider.get().find(EnumCourier.DTDC_COD.getId()); Courier blueDart =
     * courierDaoProvider.get().find(EnumCourier.BlueDart.getId()); Courier blueDartCOD =
     * courierDaoProvider.get().find(EnumCourier.BlueDart_COD.getId()); Courier speedPost =
     * courierDaoProvider.get().find(EnumCourier.Speedpost.getId()); List<Courier> courierList =
     * this.getAvailableCouriers(order); if (courierList != null && !courierList.isEmpty()) { Integer random = (new
     * Random()).nextInt(courierList.size()); Courier serviceProviderCourier = courierList.get(random); boolean isCOD =
     * false; if (order.getPayment().getPaymentMode().equals(paymentModeDao.find(EnumPaymentMode.COD.getId()))) { isCOD =
     * true; } if (isCOD) { if (serviceProviderCourier.equals(dtdcPlus) || serviceProviderCourier.equals(dtdcLite)) {
     * return dtdcCOD; } else if (serviceProviderCourier.equals(blueDart)) { return blueDartCOD; } else { return
     * serviceProviderCourier; //AFLWIz COD } } else { if (serviceProviderCourier.equals(dtdcPlus) ||
     * serviceProviderCourier.equals(dtdcLite)) { if (courierList.contains(dtdcPlus)) { return dtdcPlus; } else { return
     * dtdcLite; } } else { return serviceProviderCourier; } } } return speedPost; }
     */

    /*
     * public void generateRetailInvoice(Order order) { List<CartLineItem> productLineItems = new ArrayList<CartLineItem>();
     * List<CartLineItem> servicesLineItems = new ArrayList<CartLineItem>(); for (CartLineItem lineItem :
     * order.getLineItemsOfTypeAndStatus(EnumLineItemType.Product, EnumLineItemStatus.HANDED_OVER_TO_COURIER)) { if
     * (lineItem.getProductVariant().getProduct().isService()) { servicesLineItems.add(lineItem); } else { List<InvoiceLineItem>
     * ilis = invoiceLineItemDaoProvider.get().getInvoiceLineItem(lineItem); if (ilis == null || ilis.isEmpty()) {
     * productLineItems.add(lineItem); } } } if (productLineItems != null && !productLineItems.isEmpty()) { if
     * (order.getUser().getRoles().contains(getRoleService().getRoleByName(EnumRole.B2B_USER.getRoleName()))) {
     * generateB2BInvoiceForLineItems(order, productLineItems); } else { generateRetailInvoiceForLineItems(order,
     * productLineItems); } } } private void generateRetailInvoiceForLineItems(Order order, List<CartLineItem>
     * lineItems) { AccountingInvoice accountingInvoice = new AccountingInvoice();
     * accountingInvoice.setShippingOrder(order); Long retailInvoiceId =
     * accountingInvoiceDaoProvider.get().getLastRetailInvoiceId(); if (retailInvoiceId != null) { retailInvoiceId +=
     * 1L; } else { retailInvoiceId = 1L; } accountingInvoice.setRetailInvoiceId(retailInvoiceId);
     * accountingInvoice.setInvoiceDate(new Date()); accountingInvoice =
     * accountingInvoiceDaoProvider.get().save(accountingInvoice); for (CartLineItem lineItem : lineItems) {
     * InvoiceLineItem invoiceLineItem = new InvoiceLineItem(); invoiceLineItem.setLineItem(lineItem);
     * invoiceLineItem.setAccountingInvoice(accountingInvoice); invoiceLineItemDaoProvider.get().save(invoiceLineItem); }
     * SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); File directory = new File(adminDownloadsPath +
     * "/invoices/" + sdf.format(new Date()) + "/"); directory.mkdirs(); String inputFileName =
     * directory.getAbsolutePath() + "/R-" + accountingInvoice.getId().toString() + ".html"; String outputFileName =
     * directory.getAbsolutePath() + "/R-" + accountingInvoice.getId().toString() + ".pdf"; logger.debug("Going to
     * generate pdf - " + outputFileName); String webUrl = getLinkManager().getRetailInvoiceLink(accountingInvoice);
     * logger.debug("Going to generate pdf for webUrl - " + webUrl); generateAndSavePdf.generatePdf(webUrl,
     * inputFileName, outputFileName); } private void generateB2BInvoiceForLineItems(Order order, List<CartLineItem>
     * lineItems) { AccountingInvoice accountingInvoice = new AccountingInvoice();
     * accountingInvoice.setShippingOrder(order); Long b2bInvoiceId =
     * accountingInvoiceDaoProvider.get().getLastB2BInvoiceId(); if (b2bInvoiceId != null) { b2bInvoiceId += 1L; } else {
     * b2bInvoiceId = 1L; } accountingInvoice.setB2bInvoiceId(b2bInvoiceId); accountingInvoice.setInvoiceDate(new
     * Date()); accountingInvoice = accountingInvoiceDaoProvider.get().save(accountingInvoice); for (CartLineItem
     * lineItem : lineItems) { InvoiceLineItem invoiceLineItem = new InvoiceLineItem();
     * invoiceLineItem.setLineItem(lineItem); invoiceLineItem.setAccountingInvoice(accountingInvoice);
     * invoiceLineItemDaoProvider.get().save(invoiceLineItem); } }
     */

    /*
     * @Transactional private void initCartLineItemStatus(List<CartLineItem> cartLineItems, EnumLineItemStatus
     * enumLineItemStatus) { LineItemStatus lineItemStatus =
     * lineItemStatusDaoProvider.get().find(enumLineItemStatus.getId()); for (CartLineItem cartLineItem : cartLineItems) {
     * if (cartLineItem.getLineItemType().getId().equals(EnumLineItemType.Product.getId())) {
     * cartLineItem.setLineItemStatus(lineItemStatus); } } }
     */

    /*
     * @Transactional public boolean autoEscalateOrder(ShippingOrder shippingOrder) { return
     * shippingOrderService.autoEscalateOrder(shippingOrder); }
     */

    /*
     * public void updateStatusAndShippingDetail(List<Order> orderList) { for (Order order : orderList) { for
     * (CartLineItem lineItem : order.getCartLineItems(EnumLineItemType.Product)) { if
     * (lineItem.getLineItemStatus().getId().equals(EnumLineItemStatus.SHIPPED.getId()) && lineItem.getShipDate() ==
     * null) { lineItem.setShipDate(BaseUtils.getCurrentTimestamp()); } lineItemDaoProvider.get().save(lineItem); } } }
     * public void updateOrderStatusToShipped(List<Order> orderList) { for (Order order : orderList) { boolean
     * changeOrderStatus = true; for (CartLineItem shippingLine : order.getCartLineItems(EnumLineItemType.Product)) { if
     * (!(shippingLine.getLineItemStatus().getId().equals(EnumLineItemStatus.SHIPPED.getId()) ||
     * shippingLine.getLineItemStatus().getId().equals(EnumLineItemStatus.CANCELLED.getId()))) { changeOrderStatus =
     * false; break; } } if (changeOrderStatus) {
     * order.setOrderStatus(orderStatusDao.find(EnumOrderStatus.Shipped.getId())); order =
     * getOrderService().save(order); } } }
     */

    /*
     * RewardPointModeDao> rewardPointModeDaoProvider;
     */
    /*
     * CourierDao> courierDaoProvider;
     */
    /*
     * GenerateAndSavePdf generateAndSavePdf;
     */
    /*
     * OfferDao> offerDaoProvider;
     */
    /*
     * StateCourierServiceDao> stateCourierServiceDaoProvider;
     */
    /*
     * PincodeDao> pincodeDaoProvider;
     */
    /*
     * LineItemStatusDao> lineItemStatusDaoProvider;
     */

}
