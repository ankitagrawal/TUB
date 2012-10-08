package com.hk.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.akube.framework.util.BaseUtils;
import com.hk.constants.catalog.category.CategoryConstants;
import com.hk.constants.core.EnumEmailType;
import com.hk.constants.core.Keys;
import com.hk.constants.email.EmailTemplateConstants;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.domain.Ticket;
import com.hk.domain.catalog.Manufacturer;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.EmailType;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.courier.Shipment;
import com.hk.domain.email.EmailCampaign;
import com.hk.domain.email.EmailRecepient;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.offer.rewardPoint.RewardPoint;
import com.hk.domain.offer.rewardPoint.RewardPointTxn;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.OrderCategory;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.subscription.Subscription;
import com.hk.domain.user.User;
import com.hk.dto.pricing.PricingDto;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.email.EmailRecepientDao;
import com.hk.pact.dao.email.EmailerHistoryDao;
import com.hk.pact.dao.email.NotifyMeDao;
import com.hk.pact.dao.marketing.EmailCampaignDao;
import com.hk.pact.service.EmailService;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.CategoryService;
import com.hk.pact.service.order.OrderLoggingService;
import com.hk.service.impl.FreeMarkerService;
import com.hk.util.HtmlUtil;

import freemarker.template.Template;

@SuppressWarnings("unchecked")
@Component
public class EmailManager {

	private static Logger logger = LoggerFactory.getLogger(EmailManager.class);

	private Set<String> hkAdminEmails = null;
	private Set<String> babyAdminEmails = null;
	private Set<String> beautyAdminEmails = null;
	private Set<String> diabetesAdminEmails = null;
	private Set<String> eyeAdminEmails = null;
	private Set<String> homeDevicesAdminEmails = null;
	private Set<String> nutritionAdminEmails = null;
	private Set<String> personalCareAdminEmails = null;
	private Set<String> sportsAdminEmails = null;
	private Set<String> servicesAdminEmails = null;

	@Autowired
	private BaseDao baseDao;
	@Autowired
	private EmailService emailService;
	@Autowired
	private EmailRecepientDao emailRecepientDao;
	@Autowired
	private EmailerHistoryDao emailerHistoryDao;
	@Autowired
	private EmailCampaignDao emailCampaignDao;
	@Autowired
	private LinkManager linkManager;
	@Autowired
	private NotifyMeDao notifyMeDao;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private UserService userService;
	@Autowired
	private OrderLoggingService orderLoggingService;
	@Autowired
	private FreeMarkerService freeMarkerService;

	@Value("#{hkEnvProps['" + Keys.Env.hkAdminEmails + "']}")
	private String hkAdminEmailsString;
	@Value("#{hkEnvProps['" + Keys.Env.babyAdminEmails + "']}")
	private String babyAdminEmailsString = null;
	@Value("#{hkEnvProps['" + Keys.Env.beautyAdminEmails + "']}")
	private String beautyAdminEmailsString = null;
	@Value("#{hkEnvProps['" + Keys.Env.diabetesAdminEmails + "']}")
	private String diabetesAdminEmailsString = null;
	@Value("#{hkEnvProps['" + Keys.Env.eyeAdminEmails + "']}")
	private String eyeAdminEmailsString = null;
	@Value("#{hkEnvProps['" + Keys.Env.homeDevicesAdminEmails + "']}")
	private String homeDevicesAdminEmailsString = null;
	@Value("#{hkEnvProps['" + Keys.Env.nutritionAdminEmails + "']}")
	private String nutritionAdminEmailsString = null;
	@Value("#{hkEnvProps['" + Keys.Env.personalCareAdminEmails + "']}")
	private String personalCareAdminEmailsString = null;
	@Value("#{hkEnvProps['" + Keys.Env.sportsAdminEmails + "']}")
	private String sportsAdminEmailsString = null;
	@Value("#{hkEnvProps['" + Keys.Env.servicesAdminEmails + "']}")
	private String servicesAdminEmailsString = null;

	@Value("#{hkEnvProps['" + Keys.Env.marketingAdminEmails + "']}")
	@PostConstruct
	public void postConstruction() {
		this.hkAdminEmails = BaseUtils.split(hkAdminEmailsString, ",");
		this.babyAdminEmails = BaseUtils.split(babyAdminEmailsString, ",");
		this.beautyAdminEmails = BaseUtils.split(beautyAdminEmailsString, ",");
		this.diabetesAdminEmails = BaseUtils.split(diabetesAdminEmailsString, ",");
		this.eyeAdminEmails = BaseUtils.split(eyeAdminEmailsString, ",");
		this.homeDevicesAdminEmails = BaseUtils.split(homeDevicesAdminEmailsString, ",");
		this.nutritionAdminEmails = BaseUtils.split(nutritionAdminEmailsString, ",");
		this.personalCareAdminEmails = BaseUtils.split(personalCareAdminEmailsString, ",");
		this.sportsAdminEmails = BaseUtils.split(sportsAdminEmailsString, ",");
		this.servicesAdminEmails = BaseUtils.split(servicesAdminEmailsString, ",");
	}

	// TODO:rewrite

	public boolean sendInventoryRedZoneMail(ProductVariant productVariant) {
		HashMap valuesMap = new HashMap();
		valuesMap.put("productVariant", productVariant);

		Category basketCategory = null;
		try {
			basketCategory = getCategoryService().getTopLevelCategory(productVariant.getProduct());
			String category = basketCategory.getDisplayName();
			valuesMap.put("category", category);
		} catch (Exception e) {
			logger.error("Exception thrown while getting category", e);
		}

		boolean success = true;

		// Sending category specific emails to category admins.
		if (basketCategory != null) {
			Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.inventoryRedZoneEmail);
			String basketCategoryName = basketCategory.getDisplayName();
			for (String categoryAdminEmail : this.categoryAdmins(basketCategory)) {
				boolean sent = emailService.sendHtmlEmailNoReply(freemarkerTemplate, valuesMap, categoryAdminEmail, basketCategoryName
						+ " Category Admin");
				if (!sent)
					success = false;
			}
		}

		return success;
	}

	public boolean sendOutOfStockMail(ProductVariant productVariant) {
		HashMap valuesMap = new HashMap();
		valuesMap.put("productVariant", productVariant);

		Category basketCategory = null;
		try {
			basketCategory = getCategoryService().getTopLevelCategory(productVariant.getProduct());
			String category = basketCategory.getDisplayName();
			valuesMap.put("category", category);
		} catch (Exception e) {
			logger.error("Exception thrown while getting category", e);
		}

		boolean success = true;

		// Sending category specific emails to category admins.
		if (basketCategory != null) {
			Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.outOfStockEmail);
			String basketCategoryName = basketCategory.getDisplayName();
			for (String categoryAdminEmail : this.categoryAdmins(basketCategory)) {
				boolean sent = emailService.sendHtmlEmailNoReply(freemarkerTemplate, valuesMap, categoryAdminEmail, basketCategoryName
						+ " Category Admin");
				if (!sent)
					success = false;
			}
		}

		return success;
	}

	public boolean sendOrderConfirmEmailToAdmin(Order order) {
		HashMap valuesMap = new HashMap();
		PricingDto pricingDto = new PricingDto(order.getCartLineItems(), order.getAddress());
		valuesMap.put("order", order);
		valuesMap.put("netMargin", 0D);
		valuesMap.put("pricingDto", pricingDto);

		Category basketCategory = null;

		try {
			String basketCat = order.getBasketCategory();
			// basketCategory = getCategoryDao().find(basketCat);
			basketCategory = getCategoryService().getCategoryByName(basketCat);
		} catch (Exception e) {
			logger.error("Exception thrown while getting basket category", e);
		}
		valuesMap.put("basketCategory", basketCategory);

		boolean success = true;
		Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.orderConfirmAdminEmail);
		for (String hkAdminEmail : hkAdminEmails) {
			boolean sent = emailService.sendHtmlEmailNoReply(freemarkerTemplate, valuesMap, hkAdminEmail, "HK Admin");
			if (!sent)
				success = false;
		}
		// Sending category specific emails to category admins.
		Set<OrderCategory> orderCategories = order.getCategories();
		for (OrderCategory orderCategory : orderCategories) {
			String categoryName = orderCategory.getCategory().getDisplayName();
			for (String categoryAdminEmail : this.categoryAdmins(orderCategory.getCategory())) {
				boolean sent = emailService.sendHtmlEmailNoReply(freemarkerTemplate, valuesMap, categoryAdminEmail, categoryName
						+ " Category Admin");
				if (!sent)
					success = false;
			}
		}
		return success;
	}

	public Set<String> categoryAdmins(Category category) {
		Set<String> categoryAdmins = new HashSet<String>();

		if (category.getName().equals(CategoryConstants.BABY)) {
			categoryAdmins = babyAdminEmails;
		} else if (category.getName().equals(CategoryConstants.BEAUTY)) {
			categoryAdmins = beautyAdminEmails;
		} else if (category.getName().equals(CategoryConstants.DIABETES)) {
			categoryAdmins = diabetesAdminEmails;
		} else if (category.getName().equals(CategoryConstants.EYE)) {
			categoryAdmins = eyeAdminEmails;
		} else if (category.getName().equals(CategoryConstants.HOME_DEVICES)) {
			categoryAdmins = homeDevicesAdminEmails;
		} else if (category.getName().equals(CategoryConstants.NUTRITION)) {
			categoryAdmins = nutritionAdminEmails;
		} else if (category.getName().equals(CategoryConstants.PERSONAL_CARE)) {
			categoryAdmins = personalCareAdminEmails;
		} else if (category.getName().equals(CategoryConstants.SERVICES)) {
			categoryAdmins = servicesAdminEmails;
		} else if (category.getName().equals(CategoryConstants.SPORTS)) {
			categoryAdmins = sportsAdminEmails;
		}

		return categoryAdmins;
	}

	public boolean sendOrderConfirmEmailToUser(Order order) {
		HashMap valuesMap = new HashMap();
		Set<CartLineItem> orderCartLineItems = order.getCartLineItems();
		PricingDto pricingDto = new PricingDto(orderCartLineItems, order.getAddress());

		Set<OrderCategory> orderCategories = order.getCategories();
		int categoryCountInOrder = orderCategories.size();
		Boolean isServiceOrder = Boolean.FALSE;
//        Set<CartLineItem> serviceCartLineItems = new HashSet<CartLineItem>();

		for (OrderCategory orderCategory : orderCategories) {
			if (orderCategory.getCategory().getName().equals(CategoryConstants.SERVICES)) {
				isServiceOrder = Boolean.TRUE;
				break;
			}
		}

//        if (isServiceOrder) {
//            serviceCartLineItems = new CartLineItemFilter(orderCartLineItems).setCategoryName("services").filter();
//        }

		valuesMap.put("order", order);
		valuesMap.put("pricingDto", pricingDto);
		valuesMap.put("categoryCountInOrder", categoryCountInOrder);
		valuesMap.put("isServiceOrder", isServiceOrder);
//        valuesMap.put("serviceCartLineItems", serviceCartLineItems);

		Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.orderConfirmUserEmail);
		return emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, order.getUser().getEmail(), order.getUser().getName());
	}

	public boolean sendOrderPlacedPaymentPendingEmailToUser(Order order) {
		HashMap valuesMap = new HashMap();
		valuesMap.put("order", order);
		valuesMap.put("pricingDto", new PricingDto(order.getCartLineItems(), order.getAddress()));

		Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.orderPlacedPaymentPendingEmail);
		return emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, order.getUser().getEmail(), order.getUser().getName());
	}

	public boolean sendOrderPlacedCodEmailToUser(Order order) {
		HashMap valuesMap = new HashMap();
		valuesMap.put("order", order);
		valuesMap.put("pricingDto", new PricingDto(order.getCartLineItems(), order.getAddress()));

		Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.orderPlacedCodEmail);
		return emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, order.getUser().getEmail(), order.getUser().getName());
	}

	public boolean sendOrderPlacedAuthorizedCodEmailToUser(Order order) {
		HashMap valuesMap = new HashMap();
		valuesMap.put("order", order);
		valuesMap.put("pricingDto", new PricingDto(order.getCartLineItems(), order.getAddress()));

		Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.orderPlacedAuthorizedCodEmail);
		return emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, order.getUser().getEmail(), order.getUser().getName());
	}

	public boolean sendOrderPlacedOtherPaymentModeEmailToUser(Order order) {
		HashMap valuesMap = new HashMap();
		valuesMap.put("order", order);
		valuesMap.put("pricingDto", new PricingDto(order.getCartLineItems(), order.getAddress()));

		Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.orderPlacedOtherModeEmail);
		return emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, order.getUser().getEmail(), order.getUser().getName());
	}

	public boolean sendOrderCancelEmailToUser(Order order) {
		HashMap valuesMap = new HashMap();
		valuesMap.put("order", order);
		valuesMap.put("pricingDto", new PricingDto(order.getCartLineItems(), order.getAddress()));

		Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.orderCancelEmailUser);
		return emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, order.getUser().getEmail(), order.getUser().getName());
	}

	public boolean sendOrderCancelEmailToAdmin(Order order) {
		boolean success = false;
		HashMap valuesMap = new HashMap();
		valuesMap.put("order", order);
		valuesMap.put("pricingDto", new PricingDto(order.getCartLineItems(), order.getAddress()));

		Set<CartLineItem> productCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
		CartLineItem firstCartLineItem = null;
		if (productCartLineItems != null && productCartLineItems.size() > 0) {
			firstCartLineItem = productCartLineItems.iterator().next();
		}

		if (firstCartLineItem != null) {
			Category basketCategory = getCategoryService().getTopLevelCategory(firstCartLineItem.getProductVariant().getProduct());
			String basketCatName = basketCategory.getDisplayName();
			valuesMap.put("basketCategory", basketCatName);

			// Sending category specific emails to category admins.
			if (basketCategory != null) {
				Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.orderCancelEmailAdmin);
				String basketCategoryName = basketCategory.getDisplayName();
				for (String categoryAdminEmail : this.categoryAdmins(basketCategory)) {
					success = emailService.sendHtmlEmailNoReply(freemarkerTemplate, valuesMap, categoryAdminEmail, basketCategoryName
							+ " Category Admin");
					/* if (!sent) success = false; */
				}
			}
		} else {
			logger.error("Attempt to cancel a order with no product cart line items order id is : ", order.getId());
		}

		return success;
	}

	public boolean sendWelcomeEmail(User user, String activationLink) {
		HashMap valuesMap = new HashMap();
		valuesMap.put("user", user);
		valuesMap.put("activationLink", activationLink);

		Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.welcomeEmail);
		return emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, user.getEmail(), user.getName());
	}

	public boolean sendAccountActivationEmail(User user, String activationLink) {
		HashMap valuesMap = new HashMap();
		valuesMap.put("user", user);
		valuesMap.put("activationLink", activationLink);

		Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.accountActivationEmail);
		return emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, user.getEmail(), user.getName());
	}

	public boolean sendResetPasswordEmail(User user, String passwordResetLink) {
		HashMap valuesMap = new HashMap();
		valuesMap.put("user", user);
		valuesMap.put("link", passwordResetLink);

		Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.passwordResetEmail);
		return emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, user.getEmail(), user.getName());
	}

	public boolean sendDiscountCouponEmail(String name, String email, String coupon) {
		HashMap valuesMap = new HashMap();

		if (name == null)
			name = "Guest User";

		valuesMap.put("name", name);
		valuesMap.put("coupon", coupon);

		Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.discountCouponEmail);
		return emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, email, name);
	}

	public boolean sendContactUsMail(String name, String email, String phone, String subject, String message) {
		HashMap valuesMap = new HashMap();
		valuesMap.put("name", name);
		valuesMap.put("email", email);
		valuesMap.put("phone", phone);
		valuesMap.put("subject", subject);
		valuesMap.put("message", HtmlUtil.convertNewLineToBr(message));

		boolean success = true;

		Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.contactUsEmail);
		for (String hkAdminEmail : hkAdminEmails) {
			boolean sent = emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, hkAdminEmail, "Admin", email);
			if (!sent)
				success = false;
		}
		boolean sent = emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, "info@healthkart.com", "Admin", email);
		if (!sent)
			success = false;
		return success;
	}

	public boolean sendOrderShippedEmail(ShippingOrder shippingOrder, String invoiceLink) {
		Shipment shipment = shippingOrder.getShipment();
		shipment.setTrackLink(getLinkManager().getOrderTrackLink(shipment.getAwb().getAwbNumber(), shipment.getCourier().getId(), shippingOrder));
		HashMap valuesMap = new HashMap();

		String templatePath = EmailTemplateConstants.orderShippedEmail;

		List<ShippingOrder> shippingOrderYetToBeSentList = new ArrayList<ShippingOrder>();
		List<ShippingOrder> shippingOrderAlreadySentList = new ArrayList<ShippingOrder>();

		Order order = shippingOrder.getBaseOrder();
		valuesMap.put("order", shippingOrder);
		valuesMap.put("invoiceLink", invoiceLink);

		for (ShippingOrder shippingOrderFromAllSO : order.getShippingOrders()) {
			if (shippingOrderFromAllSO.isServiceOrder()
					|| shippingOrderFromAllSO.getId().equals(shippingOrder.getId())
					|| shippingOrderFromAllSO.getOrderStatus().equals(EnumShippingOrderStatus.SO_Cancelled.asShippingOrderStatus())) {
				continue;
			} else if ( shippingOrderFromAllSO.getOrderStatus().equals(EnumShippingOrderStatus.SO_Shipped.asShippingOrderStatus())
					|| shippingOrderFromAllSO.getOrderStatus().equals(EnumShippingOrderStatus.SO_Delivered.asShippingOrderStatus()) ) {
				shippingOrderAlreadySentList.add(shippingOrderFromAllSO);
				valuesMap.put("shippingOrderAlreadySentList", shippingOrderAlreadySentList);
			} else if (shippingOrderFromAllSO.getOrderStatus().equals(EnumShippingOrderStatus.SO_ActionAwaiting.asShippingOrderStatus())
					|| shippingOrderFromAllSO.getOrderStatus().equals(EnumShippingOrderStatus.SO_CheckedOut.asShippingOrderStatus())
					|| shippingOrderFromAllSO.getOrderStatus().equals(EnumShippingOrderStatus.SO_ReadyForProcess.asShippingOrderStatus())
					|| shippingOrderFromAllSO.getOrderStatus().equals(EnumShippingOrderStatus.SO_EscalatedBack.asShippingOrderStatus())
					|| shippingOrderFromAllSO.getOrderStatus().equals(EnumShippingOrderStatus.SO_OnHold.asShippingOrderStatus())
					|| shippingOrderFromAllSO.getOrderStatus().equals(EnumShippingOrderStatus.SO_Packed.asShippingOrderStatus())
					|| shippingOrderFromAllSO.getOrderStatus().equals(EnumShippingOrderStatus.SO_Picking.asShippingOrderStatus())
					|| shippingOrderFromAllSO.getOrderStatus().equals(EnumShippingOrderStatus.SO_MarkedForPrinting.asShippingOrderStatus())
					) {
				shippingOrderYetToBeSentList.add(shippingOrderFromAllSO);
				valuesMap.put("shippingOrderYetToBeSentList", shippingOrderYetToBeSentList);
				templatePath = EmailTemplateConstants.partialOrderShippedEmail;
			}
		}

		Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(templatePath);
		return emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, shippingOrder.getBaseOrder().getUser().getEmail(),
				shippingOrder.getBaseOrder().getUser().getName());
	}

	public boolean sendSubscriptionOrderShippedEmail(ShippingOrder shippingOrder, Subscription subscription, String invoiceLink) {
		Shipment shipment = shippingOrder.getShipment();
		shipment.setTrackLink(getLinkManager().getOrderTrackLink(shipment.getAwb().getAwbNumber(), shipment.getCourier().getId(), shippingOrder));
		HashMap valuesMap = new HashMap();
		valuesMap.put("subscription", subscription);
		valuesMap.put("order", shippingOrder);
		valuesMap.put("invoiceLink", invoiceLink);

		Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.subscriptionOrderShippedEmail);
		return emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, shippingOrder.getBaseOrder().getUser().getEmail(),
				shippingOrder.getBaseOrder().getUser().getName());
	}


	public boolean sendSubscriptionCancellationEmail(Subscription subscription) {
		HashMap valuesMap = new HashMap();
		valuesMap.put("subscription", subscription);

		Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.subscriptionCancelEmailUser);
		return emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, subscription.getBaseOrder().getUser().getEmail(),
				subscription.getBaseOrder().getUser().getName());
	}

	public boolean sendSubscriptionCancellationEmailToAdmin(Subscription subscription) {
		boolean success = false;
		HashMap valuesMap = new HashMap();
		valuesMap.put("subscription", subscription);
		Category basketCategory = getCategoryService().getTopLevelCategory(subscription.getProductVariant().getProduct());
		if (basketCategory != null) {
			Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.subscriptionCancelEmailAdmin);
			String basketCategoryName = basketCategory.getDisplayName();
			for (String categoryAdminEmail : this.categoryAdmins(basketCategory)) {
				success = emailService.sendHtmlEmailNoReply(freemarkerTemplate, valuesMap, categoryAdminEmail, basketCategoryName
						+ " Category Admin");
				/* if (!sent) success = false; */
			}
		}
		return success;
	}

	public boolean sendSubscriptionPlacedEmailToUser(Subscription subscription) {
		HashMap valuesMap = new HashMap();
		valuesMap.put("subscription", subscription);

		Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.subscriptionPlacedEmailUser);
		return emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, subscription.getBaseOrder().getUser().getEmail(),
				subscription.getBaseOrder().getUser().getName());
	}

	public boolean sendSubscriptionPlacedEmailToAdmin(Subscription subscription) {
		boolean success = false;
		HashMap valuesMap = new HashMap();
		valuesMap.put("subscription", subscription);
		Category basketCategory = getCategoryService().getTopLevelCategory(subscription.getProductVariant().getProduct());
		if (basketCategory != null) {
			Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.subscriptionPlacedEmailAdmin);
			String basketCategoryName = basketCategory.getDisplayName();
			for (String categoryAdminEmail : this.categoryAdmins(basketCategory)) {
				success = emailService.sendHtmlEmailNoReply(freemarkerTemplate, valuesMap, categoryAdminEmail, basketCategoryName
						+ " Category Admin");
				/* if (!sent) success = false; */
			}
		}
		return success;
	}

	public boolean sendSubscriptionVariantOutOfStockEmailAdmin(Subscription subscription) {
		boolean success = false;
		HashMap valuesMap = new HashMap();
		valuesMap.put("subscription", subscription);
		Category basketCategory = getCategoryService().getTopLevelCategory(subscription.getProductVariant().getProduct());
		if (basketCategory != null) {
			Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.subscriptionVariantOutOfStockEmailAdmin);
			String basketCategoryName = basketCategory.getDisplayName();
			for (String categoryAdminEmail : this.categoryAdmins(basketCategory)) {
				success = emailService.sendHtmlEmailNoReply(freemarkerTemplate, valuesMap, categoryAdminEmail, basketCategoryName
						+ " Category Admin");
				/* if (!sent) success = false; */
			}
		}
		return success;
	}

	public boolean sendOrderShippedInPartsEmail(Order order, String invoiceLink) {
		HashMap valuesMap = new HashMap();
		valuesMap.put("order", order);
		valuesMap.put("invoiceLink", invoiceLink);

		Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.orderShippedInPartsEmail);
		return emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, order.getUser().getEmail(), order.getUser().getName());
	}

	/*public boolean sendPartialShippingEmail(Order order, String invoiceLink) {
		 HashMap valuesMap = new HashMap();
		 valuesMap.put("order", order);
		 valuesMap.put("invoiceLink", invoiceLink);

		 Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.partialOrderShippedEmailOld);
		 return emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, order.getUser().getEmail(), order.getUser().getName());
	 }
 */
	public boolean sendReferralEmail(String toEmail, String fromName, String fromEmail, String unsubscribeLink, String signupLink, String couponCode, String customMessage) {
		HashMap valuesMap = new HashMap();
		valuesMap.put("fromName", fromName);
		valuesMap.put("fromEmail", fromEmail);
		valuesMap.put("unsubscribeLink", unsubscribeLink);
		valuesMap.put("signupLink", signupLink);
		valuesMap.put("couponCode", couponCode);
		valuesMap.put("customMessage", HtmlUtil.convertNewLineToBr(customMessage));

		Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.referralEmail);
		return emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, toEmail, toEmail, fromEmail);
	}

	public boolean affiliateVerfiedEmail(String toEmail, String toName, String couponCode, String customMessage) {
		HashMap valuesMap = new HashMap();
		valuesMap.put("toName", toName);
		valuesMap.put("couponCode", couponCode);
		valuesMap.put("customMessage", HtmlUtil.convertNewLineToBr(customMessage));

		Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.affiliateVerifiedEmail);
		return emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, toEmail, toEmail);
	}

	public boolean affiliateSignupEmail(String toEmail, String toName) {
		HashMap valuesMap = new HashMap();
		valuesMap.put("toName", toName);

		Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.affiliateSignupEmail);
		return emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, toEmail, toEmail);
	}

	public boolean sendProtocolViolationEmail(User violator, User against) {
		HashMap valuesMap = new HashMap();
		valuesMap.put("violator", violator);
		valuesMap.put("against", against);

		Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.protocolViolationEmail);
		return emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, "ajeet@healthkart.com", "Admin");
	}

	public boolean sendPOSentForApprovalEmail(PurchaseOrder purchaseOrder) {
		HashMap valuesMap = new HashMap();
		valuesMap.put("purchaseOrder", purchaseOrder);

		Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.poSentForApprovalEmail);
		return emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, purchaseOrder.getApprovedBy().getEmail(),
				purchaseOrder.getApprovedBy().getName());
	}

	public boolean sendPOPlacedEmail(PurchaseOrder purchaseOrder) {
		HashMap valuesMap = new HashMap();
		valuesMap.put("purchaseOrder", purchaseOrder);

		Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.poPlacedEmail);
		return emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, "accounts@healthkart.com", "Accounts Dept.");
	}

	public boolean sendReferralRewardPointEmail(RewardPoint rewardPoint, RewardPointTxn rewardPointTxn) {
		HashMap valuesMap = new HashMap();
		valuesMap.put("rewardPoint", rewardPoint);
		valuesMap.put("rewardPointTxn", rewardPointTxn);

		Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.referralRewardPointEmail);
		return emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, rewardPoint.getUser().getEmail(), rewardPoint.getUser().getFirstName());
	}

	public boolean sendCashBackRewardPointEmail(RewardPoint rewardPoint, RewardPointTxn rewardPointTxn) {
		HashMap valuesMap = new HashMap();
		valuesMap.put("rewardPoint", rewardPoint);
		valuesMap.put("rewardPointTxn", rewardPointTxn);

		Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.cashBackRewardPointEmail);
		return emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, rewardPoint.getUser().getEmail(), rewardPoint.getUser().getFirstName());
	}

	public boolean sendFBShareRewardPointEmail(RewardPoint rewardPoint, RewardPointTxn rewardPointTxn) {
		HashMap valuesMap = new HashMap();
		valuesMap.put("rewardPoint", rewardPoint);
		valuesMap.put("rewardPointTxn", rewardPointTxn);

		Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.fbShareRewardPointEmail);
		return emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, rewardPoint.getUser().getEmail(), rewardPoint.getUser().getFirstName());
	}

	public boolean sendReferralProgramIntro(User user, String referralProgramLink) {
		HashMap valuesMap = new HashMap();
		valuesMap.put("user", user);
		valuesMap.put("referralProgramLink", referralProgramLink);

		Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.referralProgramIntroEmail);
		return emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, user.getEmail(), user.getFirstName());
	}

	public boolean sendCreateTicketEmail(Ticket ticket) {
		HashMap valuesMap = new HashMap();
		valuesMap.put("ticket", ticket);

		Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.createTicketEmail);
		boolean reporterEmail = emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, ticket.getReporter().getEmail(), ticket.getReporter().getName(),
				ticket.getReporter().getEmail());
		boolean ownerEmail = true;
		if (!ticket.getReporter().equals(ticket.getOwner())) {
			ownerEmail = emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, ticket.getOwner().getEmail(), ticket.getOwner().getName(),
					ticket.getReporter().getEmail());
		}
		return (ownerEmail && reporterEmail);
	}

	public boolean sendServiceVoucherMailToServiceProvider(Order order, CartLineItem lineItem) {
		HashMap valuesMap = new HashMap();
		valuesMap.put("lineItem", lineItem);
		valuesMap.put("order", lineItem.getOrder());
		valuesMap.put("pricingDto", new PricingDto(order.getCartLineItems(), order.getAddress()));
		User adminUser = getUserService().getAdminUser();

		Manufacturer manufacturer = lineItem.getProductVariant().getProduct().getManufacturer();
		String comments = "Email Sent to " + manufacturer.getName() + " at " + manufacturer.getEmail();
		getOrderLoggingService().logOrderActivity(order, adminUser, getOrderLoggingService().getOrderLifecycleActivity(EnumOrderLifecycleActivity.EmailSentToServiceProvider), comments);

		Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.serviceVoucherMailServiceProvider);
		return emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, manufacturer.getEmail(), manufacturer.getName());
	}

	public void sendPaymentFailMail(User user, String gatewayOrderId) {
		HashMap valuesMap = new HashMap();
		valuesMap.put("user", user);
		valuesMap.put("gatewayOrderId", gatewayOrderId);

		Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.paymentFailEmail);
		emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, "jatin.nayyar@healthkart.com", "Outbound Calling Team");
		emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, user.getEmail(), user.getName(), "info@healthkart.com");
	}

	public void sendCodConverterMail(Order order) {
		HashMap valuesMap = new HashMap();
		valuesMap.put("order", order);
		valuesMap.put("codConverterLink", linkManager.getCodConverterLink(order));

		Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.codConverterEmail);
		emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, order.getUser().getEmail(), order.getUser().getName(), "info@healthkart.com");
	}

	public void sendCourierCollectionPostUpdationMessage(String email, String messagePostUpdation, String uploadDate) {
		HashMap valuesMap = new HashMap();
		valuesMap.put("uploadDate", uploadDate);
		valuesMap.put("messagePostUpdation", messagePostUpdation);

		Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.courierCollerctionPostUpdationEmail);
		emailService.sendHtmlEmailNoReply(freemarkerTemplate, valuesMap, email, "HK Admin");
		emailService.sendHtmlEmailNoReply(freemarkerTemplate, valuesMap, "rahul.agarwal@healthkart.com", "HK Admin");
	}

	public void sendWeMissYouEmail(List<User> users, Coupon coupon, EmailCampaign emailCampaign, String xsmtpapi) {
		List<EmailRecepient> emailRecepients = filterUnwantedUsers(users, emailCampaign);
		logger.info("Filtered users list " + emailRecepients.size());
		Map<String, String> headerMap = new HashMap<String, String>();
		headerMap.put("X-SMTPAPI", xsmtpapi);

		Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(emailCampaign.getTemplate());
		for (EmailRecepient emailRecepient : emailRecepients) {
			HashMap valuesMap = new HashMap();
			valuesMap.put("unsubscribeLink", getLinkManager().getEmailUnsubscribeLink(emailRecepient));
			if (emailRecepient.getUser() != null) {
				valuesMap.put("user", emailRecepient.getUser());
			}
			valuesMap.put("coupon", coupon);
			emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, emailRecepient.getEmail(), emailRecepient.getEmail(), "info@healthkart.com", headerMap);
			// keep a record in history
			emailRecepient.setEmailCount(emailRecepient.getEmailCount() + 1);
			emailRecepient.setLastEmailDate(new Date());
			getEmailRecepientDao().save(emailRecepient);
			getEmailerHistoryDao().createEmailerHistory("no-reply@healthkart.com", "HealthKart", getBaseDao().get(EmailType.class, EnumEmailType.MissYouEmail.getId()),
					emailRecepient, emailCampaign, "");
		}
	}

	public List<EmailRecepient> filterUnwantedUsers(List<User> users, EmailCampaign emailCampaign) {
		List<EmailRecepient> emailRecepients = new ArrayList<EmailRecepient>();
		for (User user : users) {
			EmailRecepient emailRecepient = getEmailRecepientDao().getOrCreateEmailRecepient(user.getEmail());
			if (emailRecepient.isEmailAllowed() && getEmailerHistoryDao().findEmailRecipientByCampaign(emailRecepient, emailCampaign) == null) {
				// last mail date null or last mail date > campaign min date
				if (emailRecepient.getLastEmailDate() == null
						|| new DateTime().minusDays(emailCampaign.getMinDayGap().intValue()).isAfter(emailRecepient.getLastEmailDate().getTime())) {
					emailRecepients.add(emailRecepient);
				}
			}
		}
		return emailRecepients;
	}

/*
	public boolean sendProductStatusMail(Product product, String stockStatus) {
		HashMap valuesMap = new HashMap();
		valuesMap.put("product", product);
		valuesMap.put("stockStatus", stockStatus);

		boolean success = true;

		Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.productStockStatusEmail);
		for (String marketingAdsMonitorEmail : marketingAdsMonitorEmails) {
			boolean sent = emailService.sendHtmlEmailNoReply(freemarkerTemplate, valuesMap, marketingAdsMonitorEmail, " Adv Admin");
			if (!sent)
				success = false;
		}

		return success;

	}
*/


	public EmailService getEmailService() {
		return emailService;
	}

	public void setEmailService(EmailService emailService) {
		this.emailService = emailService;
	}

	public EmailRecepientDao getEmailRecepientDao() {
		return emailRecepientDao;
	}

	public void setEmailRecepientDao(EmailRecepientDao emailRecepientDao) {
		this.emailRecepientDao = emailRecepientDao;
	}

	public EmailerHistoryDao getEmailerHistoryDao() {
		return emailerHistoryDao;
	}

	public void setEmailerHistoryDao(EmailerHistoryDao emailerHistoryDao) {
		this.emailerHistoryDao = emailerHistoryDao;
	}

	public EmailCampaignDao getEmailCampaignDao() {
		return emailCampaignDao;
	}

	public void setEmailCampaignDao(EmailCampaignDao emailCampaignDao) {
		this.emailCampaignDao = emailCampaignDao;
	}

	public LinkManager getLinkManager() {
		return linkManager;
	}

	public void setLinkManager(LinkManager linkManager) {
		this.linkManager = linkManager;
	}

	public NotifyMeDao getNotifyMeDao() {
		return notifyMeDao;
	}

	public void setNotifyMeDao(NotifyMeDao notifyMeDao) {
		this.notifyMeDao = notifyMeDao;
	}

	public UserService getUserService() {
		return userService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public CategoryService getCategoryService() {
		return categoryService;
	}

	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	public BaseDao getBaseDao() {
		return baseDao;
	}

	public void setBaseDao(BaseDao baseDao) {
		this.baseDao = baseDao;
	}

	public OrderLoggingService getOrderLoggingService() {
		return orderLoggingService;
	}

	public void setOrderLoggingService(OrderLoggingService orderLoggingService) {
		this.orderLoggingService = orderLoggingService;
	}
}
