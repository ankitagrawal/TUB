package com.hk.manager;

import java.util.Calendar;
import java.util.Date;
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
import com.hk.domain.clm.KarmaProfile;
import com.hk.domain.marketing.ProductReferrer;
import com.hk.domain.matcher.CartLineItemMatcher;
import com.hk.domain.offer.OfferInstance;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.CartLineItemConfig;
import com.hk.domain.order.CartLineItemExtraOption;
import com.hk.domain.order.Order;
import com.hk.domain.order.OrderCategory;
import com.hk.domain.order.PrimaryReferrerForOrder;
import com.hk.domain.order.SecondaryReferrerForOrder;
import com.hk.domain.payment.Payment;
import com.hk.domain.sku.Sku;
import com.hk.domain.user.User;
import com.hk.dto.pricing.PricingDto;
import com.hk.exception.OutOfStockException;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.catalog.combo.ComboInstanceHasProductVariantDao;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.dao.order.cartLineItem.CartLineItemDao;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.service.OrderStatusService;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.clm.KarmaProfileService;
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
import com.hk.pact.service.subscription.SubscriptionService;
import com.hk.pricing.PricingEngine;
import com.hk.util.HKDateUtil;
import com.hk.util.OrderUtil;
import com.hk.web.filter.WebContext;

@Component
public class OrderManager {

	private static Logger logger = LoggerFactory.getLogger(OrderManager.class);

	@Autowired
	ProductVariantService productVariantService;
	@Autowired
	private CartLineItemService cartLineItemService;
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
	private ReferrerProgramManager referrerProgramManager;
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
		Order order = getOrderService().findByUserAndOrderStatus(user, EnumOrderStatus.InCart);
		if (order != null && !order.isSubscriptionOrder())
			return order;

		order = new Order();
		order.setUser(user);
		order.setOrderStatus(getOrderStatusService().find(EnumOrderStatus.InCart));
		order.setAmount(0D);
		order.setSubscriptionOrder(false);

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

	public boolean createLineItems(List<ProductVariant> productVariants, Order order, Combo combo, ComboInstance comboInstance, ProductReferrer productReferrer) throws OutOfStockException {
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
					CartLineItemMatcher cartLineItemMatcher = new CartLineItemMatcher().addProductVariant(productVariant).addComboInstance(comboInstance).addCartLineItemType(EnumCartLineItemType.Product);
					cartLineItem = getCartLineItemService().getMatchingCartLineItemFromOrder(order, cartLineItemMatcher);

					// cartLineItem = getCartLineItemService().getCartLineItemFromOrder(order, productVariant,
					// comboInstance);
				} else {
					CartLineItemMatcher cartLineItemMatcher = new CartLineItemMatcher().addProductVariant(productVariant).addCartLineItemType(EnumCartLineItemType.Product);
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
					if (productReferrer != null) {
						cartLineItem.setProductReferrer(productReferrer);
					}
					cartLineItem = getCartLineItemService().save(cartLineItem);
					isCartLineItemCreated = true;
				}
			}
		}
		return isCartLineItemCreated;
	}

	public boolean createLineItems(ProductVariant productVariant, CartLineItemConfig cartLineItemConfig, Order order, ProductReferrer productReferrer) throws OutOfStockException {
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
				if (productReferrer != null) {
					cartLineItem.setProductReferrer(productReferrer);
				}
				// cartLineItem.setCostPrice(productVariant.getCostPrice() + configPrice);
				cartLineItem = getCartLineItemService().save(cartLineItem);
				isCartLineItemCreated = true;
			}
		}

		return isCartLineItemCreated;
	}

	public boolean createLineItems(ProductVariant productVariant, List<CartLineItemExtraOption> extraOptions, Order order, ProductReferrer productReferrer) throws OutOfStockException {
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
				if (productReferrer != null) {
					cartLineItem.setProductReferrer(productReferrer);
				}
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
		if (variantQty != null && variantQty > 0 && cartLineItem.getQty() != variantQty) {
			Long previousQty = cartLineItem.getQty() == null ? 0 : cartLineItem.getQty();
			cartLineItem.setQty(previousQty + variantQty);
			cartLineItem = getCartLineItemService().save(cartLineItem);
		}
		return cartLineItem;
	}

	@Transactional
	public Order orderPaymentReceieved(Payment payment) {
		Order order = payment.getOrder();
		order.setPayment(payment);
		order.setGatewayOrderId(payment.getGatewayOrderId());

		if(EnumOrderStatus.InCart.getId().equals(order.getOrderStatus().getId())){

		// apply pricing and save cart line items
		   // logger.info("catrLineItems prev size: " + order.getCartLineItems().size() + " for order : " + order.getId());
		Set<CartLineItem> cartLIFromPricingEngine = getPricingEngine().calculateAndApplyPricing(order.getCartLineItems(), order.getOfferInstance(), order.getAddress(), order.getRewardPointsUsed());
		Set<CartLineItem> cartLineItems = getCartLineItemsFromPricingCartLi(order, cartLIFromPricingEngine);

		//logger.info("catrLineItems size after pricign engine: " + cartLineItems.size() + " for order : " + order.getId());
		PricingDto pricingDto = new PricingDto(cartLineItems, order.getAddress());

		// give commissions to affiliates and and award them reward points if order came from them.
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

		//Order lifecycle activity logging - Payment Marked Successful
		if (payment.getPaymentStatus().getId().equals(EnumPaymentStatus.SUCCESS.getId())) {
			getOrderLoggingService().logOrderActivity(order, order.getUser(),
					getOrderLoggingService().getOrderLifecycleActivity(EnumOrderLifecycleActivity.PaymentMarkedSuccessful), null);
		} else if (payment.getPaymentStatus().getId().equals(EnumPaymentStatus.ON_DELIVERY.getId())) {
			getOrderLoggingService().logOrderActivity(order, getUserService().getAdminUser(),
					getOrderLoggingService().getOrderLifecycleActivity(EnumOrderLifecycleActivity.ConfirmedAuthorization), "Auto confirmation as valid user based on history.");
		}

		// order.setAmount(pricingDto.getGrandTotalPayable());
		order.setAmount(pricingDto.getGrandTotalPayable() + codCharges);
		order.setRewardPointsUsed(pricingDto.getRedeemedRewardPoints());

		cartLineItems = addFreeVariantsToCart(cartLineItems); // function made to handle deals and offers which are

		// associated with a variant, this will help in minimizing brutal use of free checkout
		order.setCartLineItems(cartLineItems);

		// award reward points, if using a reward point offer coupon
		rewardPointService.awardRewardPoints(order);

		// save order with placed status since amount has been applied
		order.setOrderStatus(EnumOrderStatus.Placed.asOrderStatus());

		Set<OrderCategory> categories = getOrderService().getCategoriesForBaseOrder(order);
		order.setCategories(categories);

		//update user karma profile for those whose score is not yet set
		KarmaProfile karmaProfile = getKarmaProfileService().updateKarmaAfterOrder(order);
		if (karmaProfile != null) {
			order.setScore(new Long(karmaProfile.getKarmaPoints()));
		}
		
		Long[] dispatchDays = OrderUtil.getDispatchDaysForBO(order);
		Date targetDelDate = HKDateUtil.addToDate(order.getPayment().getPaymentDate(), Calendar.DAY_OF_MONTH, Integer.parseInt(dispatchDays[0].toString()));
		order.setTargetDispatchDate(targetDelDate);

		order = getOrderService().save(order);

		//Order lifecycle activity logging - Order Placed
		getOrderLoggingService().logOrderActivity(order, order.getUser(), getOrderLoggingService().getOrderLifecycleActivity(EnumOrderLifecycleActivity.OrderPlaced), null);

		getUserService().updateIsProductBought(order);

		// if reward points redeemed then add reward point txns
		if (pricingDto.getRedeemedRewardPoints() > 0) {
			getReferrerProgramManager().redeemRewardPoints(order, pricingDto.getRedeemedRewardPoints());
		}

		Set<CartLineItem> subscriptionCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Subscription).filter();
		if (subscriptionCartLineItems != null && subscriptionCartLineItems.size() > 0) {
			subscriptionService.placeSubscriptions(order);
		}
			getEmailManager().sendOrderConfirmEmailToAdmin(order);
		}
		// Check if HK order then only send emails and no order placed email is necessary for subscription orders
		if (order.getStore() != null && order.getStore().getId().equals(StoreService.DEFAULT_STORE_ID) && !order.isSubscriptionOrder()) {
			// Send mail to Customer
			getPaymentService().sendPaymentEmailForOrder(order);
			sendReferralProgramEmail(order.getUser());
			getSmsManager().sendOrderPlacedSMS(order);
		}
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

	@Transactional
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
				freeCartLineItem.setDiscountOnHkPrice(freeVariant.getHkPrice() * freeVariant.getQty());
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
		//orderDao.refresh(order);
		if (order != null && order.getCartLineItems() != null && !(order.getCartLineItems()).isEmpty()) {
			for (Iterator<CartLineItem> iterator = order.getCartLineItems().iterator(); iterator.hasNext(); ) {
        ComboInstance comboInstanceValue = null;
				CartLineItem lineItem = iterator.next();
				if (lineItem.getLineItemType().getId().equals(EnumCartLineItemType.Product.getId()) || lineItem.getLineItemType().getId().equals(EnumCartLineItemType.Subscription.getId())) {
					if (lineItem.getQty() <= 0) {
						iterator.remove();
						getCartLineItemDao().delete(lineItem);
					} else {
            Boolean bool = false;
            if(lineItem.getComboInstance()!=null){
              if(comboInstanceValue !=null && comboInstanceValue.equals(lineItem.getComboInstance())){
                iterator.remove();
                getCartLineItemDao().delete(lineItem);
                bool = true;
              }
              else{
                comboInstanceValue = null;
                for(Iterator<ComboInstanceHasProductVariant> iterator1 = lineItem.getComboInstance().getComboInstanceProductVariants().iterator(); iterator.hasNext();){
                  if(iterator1.next().getProductVariant().isOutOfStock()){
                    comboInstanceValue = lineItem.getComboInstance();
                    iterator.remove();
                    getCartLineItemDao().delete(lineItem);
                    bool = true;
                    break;
                  }
                }
              }
            }
            if(!bool){
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
									//Check in case of negative unbooked inventory
	                                if (unbookedInventory < 0) {
		                                unbookedInventory = 0L;
	                                }
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

	public SubscriptionService getSubscriptionService() {
		return subscriptionService;
	}

	public void setSubscriptionService(SubscriptionService subscriptionService) {
		this.subscriptionService = subscriptionService;
	}

	public SMSManager getSmsManager() {
		return smsManager;
	}

	public void setSmsManager(SMSManager smsManager) {
		this.smsManager = smsManager;
	}
}
