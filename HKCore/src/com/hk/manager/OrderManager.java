package com.hk.manager;

import com.hk.constants.HttpRequestAndSessionConstants;
import com.hk.constants.core.EnumRole;
import com.hk.constants.core.EnumUserCodCalling;
import com.hk.constants.core.Keys;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.constants.referrer.EnumPrimaryReferrerForOrder;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.analytics.TrafficTracking;
import com.hk.domain.builder.CartLineItemBuilder;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.combo.Combo;
import com.hk.domain.catalog.product.combo.ComboInstance;
import com.hk.domain.catalog.product.combo.ComboInstanceHasProductVariant;
import com.hk.domain.clm.KarmaProfile;
import com.hk.domain.marketing.ProductReferrer;
import com.hk.domain.matcher.CartLineItemMatcher;
import com.hk.domain.offer.OfferInstance;
import com.hk.domain.order.*;
import com.hk.domain.payment.Payment;
import com.hk.domain.sku.Sku;
import com.hk.domain.store.EnumStore;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.user.User;
import com.hk.domain.user.UserCodCall;
import com.hk.dto.pricing.PricingDto;
import com.hk.exception.OutOfStockException;
import com.hk.impl.service.codbridge.OrderEventPublisher;
import com.hk.loyaltypg.service.LoyaltyProgramService;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.catalog.combo.ComboInstanceHasProductVariantDao;
import com.hk.pact.dao.catalog.combo.ComboInstanceDao;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.dao.order.cartLineItem.CartLineItemDao;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.service.OrderStatusService;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.clm.KarmaProfileService;
import com.hk.pact.service.combo.ComboService;
import com.hk.pact.service.core.AffilateService;
import com.hk.pact.service.inventory.InventoryHealthService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.order.CartLineItemService;
import com.hk.pact.service.order.OrderLoggingService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.order.RewardPointService;
import com.hk.pact.service.payment.PaymentService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.store.StoreService;
import com.hk.pact.service.subscription.SubscriptionService;
import com.hk.pricing.PricingEngine;
import com.hk.util.OrderUtil;
import com.hk.web.filter.WebContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.*;

@Component
public class OrderManager {

  private static Logger logger = LoggerFactory.getLogger(OrderManager.class);

  @Autowired
  ProductVariantService productVariantService;
  @Autowired
  private CartLineItemService cartLineItemService;
  @Autowired
  private ComboService comboService;
  @Autowired
  private OrderService orderService;
  @Autowired
  private PaymentService paymentService;
  @Autowired
  private UserService userService;
  @Autowired
  private AffilateService affilateService;
  @Autowired
  private RewardPointService rewardPointService;
  @Autowired
  private ShippingOrderService shippingOrderService;
  @Autowired
  private EmailManager emailManager;
  @Autowired
  private OrderDao orderDao;
  @Autowired
  private PricingEngine pricingEngine;
  @Autowired
  private LineItemDao lineItemDao;
  @Autowired
  private CartLineItemDao cartLineItemDao;
  @Autowired
  private LinkManager linkManager;
  @Autowired
  private SkuService skuService;
  @Autowired
  private InventoryService inventoryService;
  @Autowired
  private OrderStatusService orderStatusService;
  @Autowired
  private BaseDao baseDao;
  @Autowired
  private OrderLoggingService orderLoggingService;
  @Autowired
  private KarmaProfileService karmaProfileService;
  @Autowired
  private SubscriptionService subscriptionService;
  @Autowired
  private SMSManager smsManager;
  @Autowired
  private ComboInstanceHasProductVariantDao comboInstanceHasProductVariantDao;
  @Autowired
  OrderEventPublisher orderEventPublisher;
  @Autowired
  InventoryHealthService inventoryHealthService;

  @Autowired
  private LoyaltyProgramService loyaltyProgramService;
  @Autowired
  private ComboInstanceDao comboInstanceDao;

  @Value("#{hkEnvProps['" + Keys.Env.codCharges + "']}")
  private Double codCharges;

  @Value("#{hkEnvProps['" + Keys.Env.codFreeAfter + "']}")
  private Double codFreeAfter;

  @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
  String adminDownloadsPath;

  Affiliate affiliate;

  /*
  * // @Named(Keys.Env.cashBackPercentage) private Double cashBackPercentage; // @Named(Keys.Env.cashBackLimit)
  * private Double cashBackLimit;
  */

  @Transactional
  public Order getOrCreateOrder(User user) {
    Order order = this.getOrderService().findCart(user, EnumStore.HEALTHKART.asStore());
    if (order != null && !order.isSubscriptionOrder()) {
      return order;
    }

    order = new Order();
    order.setUser(user);
    order.setOrderStatus(this.getOrderStatusService().find(EnumOrderStatus.InCart));
    order.setAmount(0D);
    order.setSubscriptionOrder(false);

    if (user.getRoleStrings().contains(EnumRole.B2B_USER.getRoleName())) {
      order.setB2bOrder(true);
    }
    Order existingOrderNow = this.getOrderService().findCart(user, EnumStore.HEALTHKART.asStore());
    if (existingOrderNow != null) {
      return existingOrderNow;
    }

    HttpSession session = WebContext.getRequest().getSession();
    if (session.getAttribute(HttpRequestAndSessionConstants.PRIMARY_REFERRER_ID) != null) {
      Long primaryReferrerForOrderId = (Long) session.getAttribute(HttpRequestAndSessionConstants.PRIMARY_REFERRER_ID);
      order.setPrimaryReferrerForOrder(this.getBaseDao().get(PrimaryReferrerForOrder.class, primaryReferrerForOrderId));
    }
    if (session.getAttribute(HttpRequestAndSessionConstants.SECONDARY_REFERRER_ID) != null) {
      Long secondaryReferrerForOrderId = (Long) session.getAttribute(HttpRequestAndSessionConstants.SECONDARY_REFERRER_ID);
      order.setSecondaryReferrerForOrder(this.getBaseDao().get(SecondaryReferrerForOrder.class, secondaryReferrerForOrderId));
    }
    if (user.getOrders().size() == 0 && user.getReferredBy() != null) {
      order.setPrimaryReferrerForOrder(this.getBaseDao().get(PrimaryReferrerForOrder.class, EnumPrimaryReferrerForOrder.RFERRAL.getId()));
    }
    if (session.getAttribute(HttpRequestAndSessionConstants.UTM_CAMPAIGN) != null) {
      order.setUtmCampaign((String) session.getAttribute(HttpRequestAndSessionConstants.UTM_CAMPAIGN));
    }

    order = this.getOrderService().save(order);

    return order;
  }

  public boolean createLineItems(List<ProductVariant> productVariants, Order order, Combo combo, ComboInstance comboInstance, ProductReferrer productReferrer)
      throws OutOfStockException {
    boolean isCartLineItemCreated = false;
    Double totalActualHkPriceofComboVariants = 0D;
    if (combo != null && combo.getId() != null) {

      for (ComboInstanceHasProductVariant variant : this.comboInstanceHasProductVariantDao.findByComboInstance(comboInstance)) {
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
      if (this.canAddVariantToCart(productVariant)) {
        CartLineItem cartLineItem = null;
        if (comboInstance != null) {
          CartLineItemMatcher cartLineItemMatcher = new CartLineItemMatcher().addProductVariant(productVariant).addComboInstance(comboInstance).addCartLineItemType(
              EnumCartLineItemType.Product);
          cartLineItem = this.getCartLineItemService().getMatchingCartLineItemFromOrder(order, cartLineItemMatcher);

          // cartLineItem = getCartLineItemService().getCartLineItemFromOrder(order, productVariant,
          // comboInstance);
        } else {
          CartLineItemMatcher cartLineItemMatcher = new CartLineItemMatcher().addProductVariant(productVariant).addCartLineItemType(EnumCartLineItemType.Product);
          cartLineItem = this.getCartLineItemService().getMatchingCartLineItemFromOrder(order, cartLineItemMatcher);
          // cartLineItem = getCartLineItemService().getCartLineItemFromOrder(order, productVariant);
        }
        if (cartLineItem != null) {
          /*
          * if (cartLineItem.getQty() == 0) { cartLineItem.setQty(productVariant.getQty());
          * getCartLineItemService().save(cartLineItem); }
          */
          this.updateCartLineItemWithQty(cartLineItem, productVariant.getQty());
        } else {
          if (combo != null && combo.getId() != null) {
            cartLineItem = this.getCartLineItemService().createCartLineItemWithBasicDetails(productVariant, order);
            Double totalDiscount = totalActualHkPriceofComboVariants - combo.getHkPrice();
            Double percentDiscount = totalDiscount / totalActualHkPriceofComboVariants;
            cartLineItem.setHkPrice(productVariant.getHkPrice(null) * (1 - percentDiscount));
            // cartLineItem.setHkPrice(productVariant.getHkPrice(null) - (percentDiscount *
            // productVariant.getHkPrice(null) * productVariant.getQty()));
            cartLineItem.setComboInstance(comboInstance);
          } else {
            cartLineItem = this.getCartLineItemService().createCartLineItemWithBasicDetails(productVariant, order);
          }
          if (productReferrer != null) {
            cartLineItem.setProductReferrer(productReferrer);
          }
          cartLineItem = this.getCartLineItemService().save(cartLineItem);
          isCartLineItemCreated = true;
        }
      }
    }
    return isCartLineItemCreated;
  }

  public boolean createLineItems(ProductVariant productVariant, CartLineItemConfig cartLineItemConfig, Order order, ProductReferrer productReferrer) throws OutOfStockException {
    boolean isCartLineItemCreated = false;

    if (this.canAddVariantToCart(productVariant)) {
      CartLineItemMatcher cartLineItemMatcher = new CartLineItemMatcher().addProductVariant(productVariant).addCartLineItemConfig(cartLineItemConfig);
      CartLineItem cartLineItem = this.getCartLineItemService().getMatchingCartLineItemFromOrder(order, cartLineItemMatcher);
      // CartLineItem cartLineItem = getCartLineItemService().getCartLineItemFromOrder(order, productVariant,
      // cartLineItemConfig);
      if (cartLineItem != null) {
        this.updateCartLineItemWithQty(cartLineItem, productVariant.getQty());
      } else {
        cartLineItem = this.getCartLineItemService().createCartLineItemWithBasicDetails(productVariant, order);
        cartLineItemConfig = (CartLineItemConfig) this.getBaseDao().save(cartLineItemConfig);
        cartLineItem.setLineItemConfig(cartLineItemConfig);
        double configPrice = cartLineItemConfig.getPrice();
        cartLineItem.setMarkedPrice(productVariant.getMarkedPrice() + configPrice);
        cartLineItem.setHkPrice(productVariant.getHkPrice() + configPrice);
        if (productReferrer != null) {
          cartLineItem.setProductReferrer(productReferrer);
        }
        // cartLineItem.setCostPrice(productVariant.getCostPrice() + configPrice);
        cartLineItem = this.getCartLineItemService().save(cartLineItem);
        isCartLineItemCreated = true;
      }
    }

    return isCartLineItemCreated;
  }

  public boolean createLineItems(ProductVariant productVariant, List<CartLineItemExtraOption> extraOptions, Order order, ProductReferrer productReferrer)
      throws OutOfStockException {
    boolean isCartLineItemCreated = false;
    if (this.canAddVariantToCart(productVariant)) {
      CartLineItemMatcher cartLineItemMatcher = new CartLineItemMatcher().addProductVariant(productVariant).addExtraOptions(extraOptions);
      CartLineItem cartLineItem = this.getCartLineItemService().getMatchingCartLineItemFromOrder(order, cartLineItemMatcher);
      // CartLineItem cartLineItem = cartLineItemService.getCartLineItemFromOrder(order, productVariant,
      // extraOptions);

      if (cartLineItem != null) {
        this.updateCartLineItemWithQty(cartLineItem, productVariant.getQty());
      } else {
        cartLineItem = this.getCartLineItemService().createCartLineItemWithBasicDetails(productVariant, order);
        if (productReferrer != null) {
          cartLineItem.setProductReferrer(productReferrer);
        }
        cartLineItem = this.getCartLineItemService().save(cartLineItem);
        for (CartLineItemExtraOption extraOption : extraOptions) {
          extraOption.setCartLineItem(cartLineItem);
          this.getBaseDao().save(extraOption);
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
    if (variantQty != null && variantQty > 0 && cartLineItem.getQty() != variantQty) {
      Long previousQty = cartLineItem.getQty() == null ? 0 : cartLineItem.getQty();
      cartLineItem.setQty(previousQty + variantQty);
      cartLineItem = this.getCartLineItemService().save(cartLineItem);
    }
    return cartLineItem;
  }

  @Transactional
  public Order orderPaymentReceieved(Payment payment) {
    Order order = payment.getOrder();
    order.setPayment(payment);
    order.setGatewayOrderId(payment.getGatewayOrderId());

    if (EnumOrderStatus.InCart.getId().equals(order.getOrderStatus().getId())) {

      // apply pricing and save cart line items
      // logger.info("catrLineItems prev size: " + order.getCartLineItems().size() + " for order : " +
      // order.getId());
      Set<CartLineItem> cartLIFromPricingEngine = this.getPricingEngine().calculateAndApplyPricing(order.getCartLineItems(), order.getOfferInstance(), order.getAddress(),
          order.getRewardPointsUsed());
      Set<CartLineItem> cartLineItems = this.getCartLineItemsFromPricingCartLi(order, cartLIFromPricingEngine);

      // logger.info("catrLineItems size after pricign engine: " + cartLineItems.size() + " for order : " +
      // order.getId());
      PricingDto pricingDto = new PricingDto(cartLineItems, order.getAddress());

      // give commissions to affiliates and and award them reward points if order came from them.
      this.getAffilateService().saveOfferInstanceAndSaveAffiliateCommission(order, pricingDto);

      // apply cod charges if applicable and update payment object
      Double codCharges = 0D;
      if (payment.isCODPayment()) {
        codCharges = this.codCharges;
        if ((pricingDto.getGrandTotalPayable() - pricingDto.getShippingTotal()) >= this.codFreeAfter) {
          codCharges = 0D;
        }
        CartLineItem codLine = this.createCodLineItem(order, codCharges);
        order.setAmount(order.getAmount() + codCharges);
        cartLineItems.add(codLine);
        payment.setAmount(order.getAmount());
        this.getPaymentService().save(payment);
      }

      // Order lifecycle activity logging - Payment Marked Successful
      if (payment.getPaymentStatus().getId().equals(EnumPaymentStatus.SUCCESS.getId())) {
        this.getOrderLoggingService().logOrderActivity(order, order.getUser(),
            this.getOrderLoggingService().getOrderLifecycleActivity(EnumOrderLifecycleActivity.PaymentMarkedSuccessful), null);
      } else if (payment.getPaymentStatus().getId().equals(EnumPaymentStatus.ON_DELIVERY.getId())) {
        //User adminUser = UserCache.getInstance().getAdminUser();
        User adminUser = this.getUserService().getAdminUser();
        this.getOrderLoggingService().logOrderActivity(order, adminUser,
            this.getOrderLoggingService().getOrderLifecycleActivity(EnumOrderLifecycleActivity.ConfirmedAuthorization), "Auto confirmation as valid user based on history.");
      }

      if (EnumPaymentStatus.getEscalablePaymentStatuses().contains(payment.getPaymentStatus())) {
        order.setConfirmationDate(new Date());
      }

      // order.setAmount(pricingDto.getGrandTotalPayable());
      order.setAmount(pricingDto.getGrandTotalPayable() + codCharges);
      order.setRewardPointsUsed(pricingDto.getRedeemedRewardPoints());

      // function made to handle deals and offers which are associated with a variant, this will help in minimizing brutal use of free checkout
      cartLineItems = this.addFreeVariantsToCart(cartLineItems);
      order.setCartLineItems(cartLineItems);

      // award reward points, if using a reward point offer coupon
      this.rewardPointService.awardRewardPoints(order);

      // credit loyalty points
      // to do move this API out
      loyaltyProgramService.creditKarmaPoints(order);

      // save order with placed status since amount has been applied
      order.setOrderStatus(EnumOrderStatus.Placed.asOrderStatus());

      Set<OrderCategory> categories = this.getOrderService().getCategoriesForBaseOrder(order);
      order.setCategories(categories);

      // update user karma profile for those whose score is not yet set
      KarmaProfile karmaProfile = this.getKarmaProfileService().updateKarmaAfterOrder(order);
      if (karmaProfile != null) {
        order.setScore((long) karmaProfile.getKarmaPoints());
      }

      order = this.getOrderService().save(order);
      orderDao.refresh(order);

      // Order lifecycle activity logging - Order Placed
      this.getOrderLoggingService().logOrderActivity(order, order.getUser(), this.getOrderLoggingService().getOrderLifecycleActivity(EnumOrderLifecycleActivity.OrderPlaced), null);

      // calling health check
      if (!order.isSubscriptionOrder()) {
        try {
          inventoryHealthService.tempBookSkuLineItemForOrder(order);
        } catch (Exception e) {
          logger.error("Exception while TEMP booking for order#"+order.getId() +" - "+ e.getMessage());
        }
      }

      //Check Inventory health of order lineItems
      for (CartLineItem cartLineItem : order.getCartLineItems()) {
        if (cartLineItem.isType(EnumCartLineItemType.Product)) {
          try {
            logger.info("Inventory Health being called for " + cartLineItem.getProductVariant().getId());
            this.inventoryService.checkInventoryHealth(cartLineItem.getProductVariant());
          } catch (Exception e) {
            logger.error("Exception while Inventory Health for order#" + order.getId() + "; PV#" + cartLineItem.getProductVariant().getId() + " - " + e.getMessage());
          }
        }
      }

      this.getUserService().updateIsProductBought(order);

      // if reward points redeemed then add reward point txns
      if (pricingDto.getRedeemedRewardPoints() > 0) {
        this.getRewardPointService().redeemRewardPoints(order, pricingDto.getRedeemedRewardPoints());
      }

      if (!order.getPayment().getPaymentStatus().getId().equals(EnumPaymentStatus.AUTHORIZATION_PENDING.getId())) {
        Set<CartLineItem> subscriptionCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Subscription).filter();
        if (subscriptionCartLineItems != null && subscriptionCartLineItems.size() > 0) {
          this.subscriptionService.placeSubscriptions(order);
        }
      }
      this.getEmailManager().sendOrderConfirmEmailToAdmin(order);
    }

    // Check if HK order then only send emails and no order placed email is necessary for subscription orders
    if (order.getStore() != null && (order.getStore().getId().equals(StoreService.DEFAULT_STORE_ID) ||
        order.getStore().getId().equals(StoreService.LOYALTYPG_ID)) && !order.isSubscriptionOrder()) {
      // Send mail to Customer
      this.getPaymentService().sendPaymentEmailForOrder(order);
      this.sendReferralProgramEmail(order.getUser());
      this.getSmsManager().sendOrderPlacedSMS(order);
    }

//        //this is the most important method, so it is very important as to from where it is called
//        orderService.splitBOCreateShipmentEscalateSOAndRelatedTasks(order);
    //we are now trying to replace the above method by pushing orderId in queue
//        orderEventPublisher.publishOrderPlacedEvent(order);

    //Set Order in Traffic Tracking
    TrafficTracking trafficTracking = (TrafficTracking) WebContext.getRequest().getSession().getAttribute(HttpRequestAndSessionConstants.TRAFFIC_TRACKING);
    if (trafficTracking != null) {
      trafficTracking.setOrderId(order.getId());
      if (order.getUser().getOrders().size() > 1) {
        trafficTracking.setFirstOrder(0L);
      } else {
        trafficTracking.setFirstOrder(1L);
      }
      this.getBaseDao().save(trafficTracking);
    }

    return order;
  }

  public void sendReferralProgramEmail(User user) {
    Order lastOrder = this.getOrderService().getLatestOrderForUser(user);
    if (lastOrder == null) {
      this.getEmailManager().sendReferralProgramIntro(user, this.getLinkManager().getReferralProgramUrl());
    }
  }

  public Set<CartLineItem> addFreeCartLineItems(String variantId, Order order) {
    Set<CartLineItem> cartLineItems = order.getCartLineItems();
    ProductVariant productVariant = this.getProductVariantService().getVariantById(variantId);
    if (productVariant != null) {
      productVariant.setQty(1L);
      CartLineItem cartLineItem = this.getCartLineItemService().createCartLineItemWithBasicDetails(productVariant, order);
      cartLineItem.setDiscountOnHkPrice(cartLineItem.getHkPrice());
      cartLineItem = this.getCartLineItemService().save(cartLineItem);
      cartLineItems.add(cartLineItem);
    }
    return cartLineItems;
  }

  @Transactional
  private Set<CartLineItem> addFreeVariantsToCart(Set<CartLineItem> cartLineItems) {
    Set<CartLineItem> updatedCartLineItems = new HashSet<CartLineItem>();
    updatedCartLineItems.addAll(cartLineItems);
    for (CartLineItem cartLineItem : cartLineItems) {
      if (cartLineItem.getLineItemType().getId().equals(EnumCartLineItemType.Product.getId())) {

        ProductVariant freeVariant = cartLineItem.getProductVariant().getFreeProductVariant();
        if (freeVariant != null) {
          CartLineItem existingCartLineItem = this.getCartLineItemDao().getLineItem(freeVariant, cartLineItem.getOrder());
          if (existingCartLineItem != null) {
            updatedCartLineItems.remove(existingCartLineItem);
          }
          CartLineItem freeLineItem = this.createFreeLineItem(cartLineItem, freeVariant);
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
    //as on 28-08-13, free variants which are out of stock, will not be added to an order
      if (!(freeVariant.isOutOfStock() || freeVariant.isDeleted() || freeVariant.getProduct().isDeleted())) {
          CartLineItem existingCartLineItem = this.getCartLineItemDao().getLineItem(freeVariant, order);
          if (existingCartLineItem == null) { // The variant is not added in user account already
              CartLineItem freeCartLineItem = this.cartLineItemService.createCartLineItemWithBasicDetails(freeVariant, order);
              freeCartLineItem.setDiscountOnHkPrice(freeVariant.getHkPrice() * freeVariant.getQty());
              return this.cartLineItemService.save(freeCartLineItem);
          } else {
              existingCartLineItem.setQty(existingCartLineItem.getQty() + freeVariant.getQty());
              existingCartLineItem.setDiscountOnHkPrice(existingCartLineItem.getDiscountOnHkPrice() + (freeVariant.getHkPrice() * freeVariant.getQty()));
              return this.cartLineItemService.save(existingCartLineItem);
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
      cartLineItem = this.getCartLineItemService().save(cartLineItem);
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

    return this.getCartLineItemService().save(codLine);
  }

  public Order recalAndUpdateAmount(Order order) {
    OfferInstance offerInstance = order.getOfferInstance();

    PricingDto pricingDto = new PricingDto(this.getPricingEngine().calculatePricing(order.getCartLineItems(), offerInstance, order.getAddress(), order.getRewardPointsUsed()),
        order.getAddress());

    order.setAmount(pricingDto.getGrandTotalPayable());

    return this.getOrderService().save(order);
  }

  public Order orderPaymentAuthPending(Payment payment) {
    return this.orderPaymentReceieved(payment);
  }


  public Set<CartLineItem> trimEmptyLineItems(Order order) {
    Set<CartLineItem> cartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).addCartLineItemType(EnumCartLineItemType.Subscription).filter();
    Set<CartLineItem> trimmedCartLineItems = new HashSet<CartLineItem>();
    Set<ComboInstance> toBeRemovedComboInstanceSet = new HashSet<ComboInstance>();
    for (Iterator<CartLineItem> iterator = cartLineItems.iterator(); iterator.hasNext();) {
      CartLineItem lineItem = iterator.next();
      ProductVariant productVariant = lineItem.getProductVariant();
      Product product = productVariant.getProduct();
      ComboInstance comboInstance = lineItem.getComboInstance();
      List<Sku> skuList = this.skuService.getSKUsForMarkingProductOOS(productVariant);

      if (lineItem.getQty() <= 0) {
        iterator.remove();
        order.getCartLineItems().remove(lineItem);
        this.getBaseDao().delete(lineItem);
      } else {

        if (skuList == null || skuList.isEmpty()
            || product.isDeleted() || product.isOutOfStock()
            || productVariant.isDeleted() || productVariant.isOutOfStock()) {
          if (comboInstance != null) {
            toBeRemovedComboInstanceSet.add(comboInstance);
          }
          if(!product.isJit()){
              lineItem.setQty(0L);
          }
          continue;
        }

        if (!(product.isJit() || product.isService() || product.isDropShipping() || lineItem.getLineItemType().getId().equals(EnumCartLineItemType.Subscription.getId()))) {
          Long liQty = lineItem.getQty();
          Long allowedQty = this.inventoryService.getAllowedStepUpInventory(lineItem);
          if(lineItem.getComboInstance() != null){
            for (CartLineItem li : comboInstanceDao.getSiblingLineItems(lineItem)) {
              Long comboPVQty = li.getComboInstance().getComboInstanceProductVariant(li.getProductVariant()).getQty();
              liQty = liQty / comboPVQty;
              break;
            }
          }
          if (allowedQty != null && allowedQty < liQty) {
            // Check in case of negative unbooked inventory
            if (comboInstance != null) {
              toBeRemovedComboInstanceSet.add(comboInstance);
              continue;
            }
            if (allowedQty <= 0) {
              allowedQty = 0L;
            }
            lineItem.setQty(allowedQty);
          }
        }
      }
    }
    for (Iterator<CartLineItem> iterator = cartLineItems.iterator(); iterator.hasNext();) {
      CartLineItem lineItem = iterator.next();
      ProductVariant productVariant = lineItem.getProductVariant();
      if (toBeRemovedComboInstanceSet.contains(lineItem.getComboInstance()) || lineItem.getQty() <= 0) {
        trimmedCartLineItems.add(lineItem);
        Long qty = lineItem.getQty();
        iterator.remove();
        //check for subscription
        if (lineItem.getLineItemType().getId().equals(EnumCartLineItemType.Subscription.getId())) {
          Subscription subscription = this.subscriptionService.getSubscriptionFromCartLineItem(lineItem);
          this.subscriptionService.abandonSubscription(subscription);
        }
        order.getCartLineItems().remove(lineItem);
        this.getBaseDao().delete(lineItem);
        if (qty <= 0) {
          productVariant.setOutOfStock(true);
          this.getProductVariantService().save(productVariant);
          this.getComboService().markProductOutOfStock(productVariant);
        }
      }
    }
    order = this.getOrderService().save(order);
    return trimmedCartLineItems;
  }

  public boolean isStepUpAllowed(CartLineItem cartLineItem) {
    ProductVariant productVariant = cartLineItem.getProductVariant();
    Product product = productVariant.getProduct();
    boolean isService = false;
    if (product.isService() != null && product.isService()) {
      isService = true;
    }
    boolean isJit = false;
    if (product.isJit() != null && product.isJit()) {
      isJit = true;
    }
    if (!isJit && !isService) {
//            Long unbookedInventory = inventoryService.getAllowedStepUpInventory(skuService.getSKUsForProductVariant(productVariant));
      Long unbookedInventory = this.inventoryService.getAllowedStepUpInventory(productVariant);
      if (unbookedInventory != null && unbookedInventory > 0 && unbookedInventory < cartLineItem.getQty()) {
        return false;
      }
    }
    return true;
  }

    public boolean isStepUpAllowedForCombo(CartLineItem cartLineItem, Long qty) {
      Long stepUpQty = 0L;
      int count = 0;
      for (CartLineItem li : comboInstanceDao.getSiblingLineItems(cartLineItem)) {
        count++;
        ComboInstanceHasProductVariant comboVariant = li.getComboInstance().getComboInstanceProductVariant(li.getProductVariant());
        ProductVariant productVariant = comboVariant.getProductVariant();
        Long allowedQty = inventoryService.getAllowedStepUpInventory(productVariant);
        if (count > 1) {
          stepUpQty = Math.min(stepUpQty, Math.abs(allowedQty / comboVariant.getQty()));
        } else {
          stepUpQty = Math.abs(allowedQty / comboVariant.getQty());
        }
      }
      if (stepUpQty.intValue() < qty.intValue()) {
        return false;
      }
    return true;
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

  public void setGroundShippedItemQuantity(Order order) {
    Set<CartLineItem> cartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
    for (CartLineItem cartLineItem : cartLineItems) {
      if (cartLineItem.getProductVariant().getProduct().getGroundShipping()) {
        cartLineItem.setQty(0L);
      }
    }
  }

  public CartLineItemService getCartLineItemService() {
    return this.cartLineItemService;
  }

  public void setCartLineItemService(CartLineItemService cartLineItemService) {
    this.cartLineItemService = cartLineItemService;
  }

  public OrderService getOrderService() {
    return this.orderService;
  }

  public void setOrderService(OrderService orderService) {
    this.orderService = orderService;
  }

  public PaymentService getPaymentService() {
    return this.paymentService;
  }

  public void setPaymentService(PaymentService paymentService) {
    this.paymentService = paymentService;
  }

  public UserService getUserService() {
    return this.userService;
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  public KarmaProfileService getKarmaProfileService() {
    return this.karmaProfileService;
  }

  public void setKarmaProfileService(KarmaProfileService karmaProfileService) {
    this.karmaProfileService = karmaProfileService;
  }

  public AffilateService getAffilateService() {
    return this.affilateService;
  }

  public void setAffilateService(AffilateService affilateService) {
    this.affilateService = affilateService;
  }

  public RewardPointService getRewardPointService() {
    return this.rewardPointService;
  }

  public void setRewardPointService(RewardPointService rewardPointService) {
    this.rewardPointService = rewardPointService;
  }

  public ShippingOrderService getShippingOrderService() {
    return this.shippingOrderService;
  }

  public void setShippingOrderService(ShippingOrderService shippingOrderService) {
    this.shippingOrderService = shippingOrderService;
  }

  public EmailManager getEmailManager() {
    return this.emailManager;
  }

  public void setEmailManager(EmailManager emailManager) {
    this.emailManager = emailManager;
  }

  public OrderDao getOrderDao() {
    return this.orderDao;
  }

  public void setOrderDao(OrderDao orderDao) {
    this.orderDao = orderDao;
  }

  public PricingEngine getPricingEngine() {
    return this.pricingEngine;
  }

  public void setPricingEngine(PricingEngine pricingEngine) {
    this.pricingEngine = pricingEngine;
  }

  public LineItemDao getLineItemDao() {
    return this.lineItemDao;
  }

  public void setLineItemDao(LineItemDao lineItemDao) {
    this.lineItemDao = lineItemDao;
  }

  public CartLineItemDao getCartLineItemDao() {
    return this.cartLineItemDao;
  }

  public void setCartLineItemDao(CartLineItemDao cartLineItemDao) {
    this.cartLineItemDao = cartLineItemDao;
  }

  public LinkManager getLinkManager() {
    return this.linkManager;
  }

  public void setLinkManager(LinkManager linkManager) {
    this.linkManager = linkManager;
  }

  public SkuService getSkuService() {
    return this.skuService;
  }

  public void setSkuService(SkuService skuService) {
    this.skuService = skuService;
  }

  public InventoryService getInventoryService() {
    return this.inventoryService;
  }

  public void setInventoryService(InventoryService inventoryService) {
    this.inventoryService = inventoryService;
  }

  public OrderStatusService getOrderStatusService() {
    return this.orderStatusService;
  }

  public void setOrderStatusService(OrderStatusService orderStatusService) {
    this.orderStatusService = orderStatusService;
  }

  public BaseDao getBaseDao() {
    return this.baseDao;
  }

  public void setBaseDao(BaseDao baseDao) {
    this.baseDao = baseDao;
  }

  public ProductVariantService getProductVariantService() {
    return this.productVariantService;
  }

  public void setProductVariantService(ProductVariantService productVariantService) {
    this.productVariantService = productVariantService;
  }

  public OrderLoggingService getOrderLoggingService() {
    return this.orderLoggingService;
  }

  public void setOrderLoggingService(OrderLoggingService orderLoggingService) {
    this.orderLoggingService = orderLoggingService;
  }

  public SubscriptionService getSubscriptionService() {
    return this.subscriptionService;
  }

  public void setSubscriptionService(SubscriptionService subscriptionService) {
    this.subscriptionService = subscriptionService;
  }

  public SMSManager getSmsManager() {
    return this.smsManager;
  }

  public void setSmsManager(SMSManager smsManager) {
    this.smsManager = smsManager;
  }

  public ComboService getComboService() {
    return this.comboService;
  }
}
