package com.hk.manager;

import com.akube.framework.util.BaseUtils;
import com.hk.constants.core.EnumEmailType;
import com.hk.constants.core.Keys;
import com.hk.constants.email.EmailTemplateConstants;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderLifecycleActivity;
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
import com.hk.domain.order.ShippingOrder;
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
import com.hk.util.HtmlUtil;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;

@SuppressWarnings("unchecked")
@Component
public class EmailManager {

    private static Logger       logger                        = LoggerFactory.getLogger(EmailManager.class);

    private Set<String>         hkAdminEmails                 = null;
    private Set<String>         babyAdminEmails               = null;
    private Set<String>         beautyAdminEmails             = null;
    private Set<String>         diabetesAdminEmails           = null;
    private Set<String>         eyeAdminEmails                = null;
    private Set<String>         homeDevicesAdminEmails        = null;
    private Set<String>         nutritionAdminEmails          = null;
    private Set<String>         personalCareAdminEmails       = null;
    private Set<String>         sportsAdminEmails             = null;
    private Set<String>         servicesAdminEmails           = null;

    @Autowired
    private BaseDao             baseDao;
    @Autowired
    private EmailService        emailService;
    @Autowired
    private EmailRecepientDao   emailRecepientDao;
    @Autowired
    private EmailerHistoryDao   emailerHistoryDao;
    @Autowired
    private EmailCampaignDao    emailCampaignDao;
    @Autowired
    private LinkManager         linkManager;
    @Autowired
    private NotifyMeDao         notifyMeDao;
    @Autowired
    private CategoryService     categoryService;
    @Autowired
    private UserService         userService;
    @Autowired
    private OrderLoggingService orderLoggingService;

    @Value("#{hkEnvProps['" + Keys.Env.hkAdminEmails + "']}")
    private String              hkAdminEmailsString;
    /*
     * @Value("#{hkEnvProps['" + Keys.Env.hkReportAdminEmails + "']}") private String hkReportAdminEmailsString = null;
     */
    @Value("#{hkEnvProps['" + Keys.Env.babyAdminEmails + "']}")
    private String              babyAdminEmailsString         = null;
    @Value("#{hkEnvProps['" + Keys.Env.beautyAdminEmails + "']}")
    private String              beautyAdminEmailsString       = null;
    @Value("#{hkEnvProps['" + Keys.Env.diabetesAdminEmails + "']}")
    private String              diabetesAdminEmailsString     = null;
    @Value("#{hkEnvProps['" + Keys.Env.eyeAdminEmails + "']}")
    private String              eyeAdminEmailsString          = null;
    @Value("#{hkEnvProps['" + Keys.Env.homeDevicesAdminEmails + "']}")
    private String              homeDevicesAdminEmailsString  = null;
    @Value("#{hkEnvProps['" + Keys.Env.nutritionAdminEmails + "']}")
    private String              nutritionAdminEmailsString    = null;
    @Value("#{hkEnvProps['" + Keys.Env.personalCareAdminEmails + "']}")
    private String              personalCareAdminEmailsString = null;
    /*
     * @Value("#{hkEnvProps['" + Keys.Env.logisticsAdminEmails + "']}") private String logisticsAdminEmailsString =
     * null;
     */
    @Value("#{hkEnvProps['" + Keys.Env.sportsAdminEmails + "']}")
    private String              sportsAdminEmailsString       = null;
    @Value("#{hkEnvProps['" + Keys.Env.servicesAdminEmails + "']}")
    private String              servicesAdminEmailsString     = null;

    @Value("#{hkEnvProps['" + Keys.Env.marketingAdminEmails + "']}")
    /*
     * private String marketingAdminEmailsString = null; @Value("#{hkEnvProps['" + Keys.Env.categoryHealthkart + "']}")
     * private String categoryHealthkartListString = null;
     */
    @PostConstruct
    public void postConstruction() {
        // this.hkReportAdminEmails = BaseUtils.split(hkReportAdminEmailsString, ",");
        this.hkAdminEmails = BaseUtils.split(hkAdminEmailsString, ",");
        this.babyAdminEmails = BaseUtils.split(babyAdminEmailsString, ",");
        this.beautyAdminEmails = BaseUtils.split(beautyAdminEmailsString, ",");
        this.diabetesAdminEmails = BaseUtils.split(diabetesAdminEmailsString, ",");
        this.eyeAdminEmails = BaseUtils.split(eyeAdminEmailsString, ",");
        this.homeDevicesAdminEmails = BaseUtils.split(homeDevicesAdminEmailsString, ",");
        this.nutritionAdminEmails = BaseUtils.split(nutritionAdminEmailsString, ",");
        this.personalCareAdminEmails = BaseUtils.split(personalCareAdminEmailsString, ",");
        // this.logisticsAdminEmails = BaseUtils.split(logisticsAdminEmailsString, ",");
        this.sportsAdminEmails = BaseUtils.split(sportsAdminEmailsString, ",");
        this.servicesAdminEmails = BaseUtils.split(servicesAdminEmailsString, ",");
        // this.marketingAdminEmails = BaseUtils.split(marketingAdminEmailsString, ",");
        // this.categoryHealthkartList = BaseUtils.split(categoryHealthkartListString, ",");
    }

    /*
     * @Autowired public EmailManager(EmailService emailService, // @Named(Keys.Env.hkAdminEmails) String hkAdminEmails, //
     * @Named(Keys.Env.hkReportAdminEmails) String hkReportAdminEmails, // @Named(Keys.Env.babyAdminEmails) String
     * babyAdminEmails, // @Named(Keys.Env.beautyAdminEmails) String beautyAdminEmails, //
     * @Named(Keys.Env.diabetesAdminEmails) String diabetesAdminEmails, // @Named(Keys.Env.eyeAdminEmails) String
     * eyeAdminEmails, // @Named(Keys.Env.homeDevicesAdminEmails) String homeDevicesAdminEmails, //
     * @Named(Keys.Env.nutritionAdminEmails) String nutritionAdminEmails, // @Named(Keys.Env.personalCareAdminEmails)
     * String personalCareAdminEmails, // @Named(Keys.Env.sportsAdminEmails) String sportsAdminEmails, //
     * @Named(Keys.Env.servicesAdminEmails) String servicesAdminEmails, // @Named(Keys.Env.logisticsAdminEmails) String
     * logisticsAdminEmails, // @Named(Keys.Env.marketingAdminEmails) String marketingAdminEmails, //
     * @Named(Keys.Env.categoryHealthkart) String categoryHealthkartList) { this.emailService = emailService;
     * this.hkReportAdminEmails = BaseUtils.split(hkReportAdminEmails, ","); this.hkAdminEmails =
     * BaseUtils.split(hkAdminEmails, ","); this.babyAdminEmails = BaseUtils.split(babyAdminEmails, ",");
     * this.beautyAdminEmails = BaseUtils.split(beautyAdminEmails, ","); this.diabetesAdminEmails =
     * BaseUtils.split(diabetesAdminEmails, ","); this.eyeAdminEmails = BaseUtils.split(eyeAdminEmails, ",");
     * this.homeDevicesAdminEmails = BaseUtils.split(homeDevicesAdminEmails, ","); this.nutritionAdminEmails =
     * BaseUtils.split(nutritionAdminEmails, ","); this.personalCareAdminEmails =
     * BaseUtils.split(personalCareAdminEmails, ","); this.logisticsAdminEmails = BaseUtils.split(logisticsAdminEmails,
     * ","); this.sportsAdminEmails = BaseUtils.split(sportsAdminEmails, ","); this.servicesAdminEmails =
     * BaseUtils.split(servicesAdminEmails, ","); this.marketingAdminEmails = BaseUtils.split(marketingAdminEmails,
     * ","); this.categoryHealthkartList = BaseUtils.split(categoryHealthkartList, ","); }
     */

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

        /*
         * for (String hkAdminEmail : hkAdminEmails) { boolean sent =
         * emailService.sendHtmlEmailNoReply(EmailTemplateConstants.inventoryRedZoneEmail, valuesMap, hkAdminEmail, "HK
         * Admin"); if (!sent) success = false; }
         */

        // Sending category specific emails to category admins.
        if (basketCategory != null) {
            for (String categoryAdminEmail : this.categoryAdmins(basketCategory)) {
                boolean sent = emailService.sendHtmlEmailNoReply(EmailTemplateConstants.inventoryRedZoneEmail, valuesMap, categoryAdminEmail, basketCategory.getDisplayName()
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
            for (String categoryAdminEmail : this.categoryAdmins(basketCategory)) {
                boolean sent = emailService.sendHtmlEmailNoReply(EmailTemplateConstants.outOfStockEmail, valuesMap, categoryAdminEmail, basketCategory.getDisplayName()
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
        for (String hkAdminEmail : hkAdminEmails) {
            boolean sent = emailService.sendHtmlEmailNoReply(EmailTemplateConstants.orderConfirmAdminEmail, valuesMap, hkAdminEmail, "HK Admin");
            if (!sent)
                success = false;
        }
        // Sending category specific emails to category admins.
        if (basketCategory != null) {
            for (String categoryAdminEmail : this.categoryAdmins(basketCategory)) {
                boolean sent = emailService.sendHtmlEmailNoReply(EmailTemplateConstants.orderConfirmAdminEmail, valuesMap, categoryAdminEmail, basketCategory.getDisplayName()
                        + " Category Admin");
                if (!sent)
                    success = false;
            }
        }
        return success;
    }

    public Set<String> categoryAdmins(Category category) {
        Set<String> categoryAdmins = new HashSet<String>();

        if (category.getName().equals("parenting")) {
            categoryAdmins = babyAdminEmails;
        } else if (category.getName().equals("beauty")) {
            categoryAdmins = beautyAdminEmails;
        } else if (category.getName().equals("diabetes")) {
            categoryAdmins = diabetesAdminEmails;
        } else if (category.getName().equals("eye")) {
            categoryAdmins = eyeAdminEmails;
        } else if (category.getName().equals("home-devices")) {
            categoryAdmins = homeDevicesAdminEmails;
        } else if (category.getName().equals("nutrition")) {
            categoryAdmins = nutritionAdminEmails;
        } else if (category.getName().equals("personal-care")) {
            categoryAdmins = personalCareAdminEmails;
        } else if (category.getName().equals("services")) {
            categoryAdmins = servicesAdminEmails;
        } else if (category.getName().equals("sports")) {
            categoryAdmins = sportsAdminEmails;
        }

        return categoryAdmins;
    }

    public boolean sendOrderConfirmEmailToUser(Order order) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("order", order);
        PricingDto pricingDto = new PricingDto(order.getCartLineItems(), order.getAddress());
        valuesMap.put("pricingDto", pricingDto);

        // for (LineItem lineItem : pricingDto.getProductLineItems()) {
        // ProcessingDatesDto dto =
        // processingDateCalculatorProvider.get().calculateProcessingDate(order.getPayment().getPaymentDate() == null ?
        // order.getPayment().getCreateDate() : order.getPayment().getPaymentDate(), invoiceLine.getProductScaffold());
        // String expectedDates = FormatUtils.getFormattedDateForUserEnd(dto.getMinProcessDate().toDate()) + " - " +
        // FormatUtils.getFormattedDateForUserEnd(dto.getMaxProcessDate().toDate());
        // lineItem.setExpectedShipDates(expectedDates);
        // }

        return emailService.sendHtmlEmail(EmailTemplateConstants.orderConfirmUserEmail, valuesMap, order.getUser().getEmail(), order.getUser().getName());
    }

    public boolean sendOrderPlacedPaymentPendingEmailToUser(Order order) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("order", order);
        valuesMap.put("pricingDto", new PricingDto(order.getCartLineItems(), order.getAddress()));

        return emailService.sendHtmlEmail(EmailTemplateConstants.orderPlacedPaymentPendingEmail, valuesMap, order.getUser().getEmail(), order.getUser().getName());
    }

    public boolean sendOrderPlacedCodEmailToUser(Order order) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("order", order);
        valuesMap.put("pricingDto", new PricingDto(order.getCartLineItems(), order.getAddress()));

        return emailService.sendHtmlEmail(EmailTemplateConstants.orderPlacedCodEmail, valuesMap, order.getUser().getEmail(), order.getUser().getName());
    }

    public boolean sendOrderPlacedOtherPaymentModeEmailToUser(Order order) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("order", order);
        valuesMap.put("pricingDto", new PricingDto(order.getCartLineItems(), order.getAddress()));

        return emailService.sendHtmlEmail(EmailTemplateConstants.orderPlacedOtherModeEmail, valuesMap, order.getUser().getEmail(), order.getUser().getName());
    }

    public boolean sendOrderCancelEmailToUser(Order order) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("order", order);
        valuesMap.put("pricingDto", new PricingDto(order.getCartLineItems(), order.getAddress()));

        return emailService.sendHtmlEmail(EmailTemplateConstants.orderCancelEmailUser, valuesMap, order.getUser().getEmail(), order.getUser().getName());
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
                for (String categoryAdminEmail : this.categoryAdmins(basketCategory)) {
                    success = emailService.sendHtmlEmailNoReply(EmailTemplateConstants.orderCancelEmailAdmin, valuesMap, categoryAdminEmail, basketCategory.getDisplayName()
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

        return emailService.sendHtmlEmail(EmailTemplateConstants.welcomeEmail, valuesMap, user.getEmail(), user.getName());
    }

    public boolean sendAccountActivationEmail(User user, String activationLink) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("user", user);
        valuesMap.put("activationLink", activationLink);

        return emailService.sendHtmlEmail(EmailTemplateConstants.accountActivationEmail, valuesMap, user.getEmail(), user.getName());
    }

    public boolean sendResetPasswordEmail(User user, String passwordResetLink) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("user", user);
        valuesMap.put("link", passwordResetLink);

        return emailService.sendHtmlEmail(EmailTemplateConstants.passwordResetEmail, valuesMap, user.getEmail(), user.getName());
    }

    public boolean sendDiscountCouponEmail(String name, String email, String coupon) {
        HashMap valuesMap = new HashMap();

        if (name == null)
            name = "Guest User";

        valuesMap.put("name", name);
        valuesMap.put("coupon", coupon);

        return emailService.sendHtmlEmail(EmailTemplateConstants.discountCouponEmail, valuesMap, email, name);
    }

    public boolean sendContactUsMail(String name, String email, String phone, String subject, String message) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("name", name);
        valuesMap.put("email", email);
        valuesMap.put("phone", phone);
        valuesMap.put("subject", subject);
        valuesMap.put("message", HtmlUtil.convertNewLineToBr(message));

        boolean success = true;
        for (String hkAdminEmail : hkAdminEmails) {
            boolean sent = emailService.sendHtmlEmail(EmailTemplateConstants.contactUsEmail, valuesMap, hkAdminEmail, "Admin", email);
            if (!sent)
                success = false;
        }
        boolean sent = emailService.sendHtmlEmail(EmailTemplateConstants.contactUsEmail, valuesMap, "info@healthkart.com", "Admin", email);
        if (!sent)
            success = false;
        return success;
    }

    public boolean sendOrderShippedEmail(ShippingOrder shippingOrder, String invoiceLink) {
        Shipment shipment = shippingOrder.getShipment();
        shipment.setTrackLink(getLinkManager().getOrderTrackLink(shipment.getTrackingId(), shipment.getCourier().getId(), shippingOrder));
        HashMap valuesMap = new HashMap();
        valuesMap.put("order", shippingOrder);
        valuesMap.put("invoiceLink", invoiceLink);

        return emailService.sendHtmlEmail(EmailTemplateConstants.orderShippedEmail, valuesMap, shippingOrder.getBaseOrder().getUser().getEmail(),
                shippingOrder.getBaseOrder().getUser().getName());
    }

    public boolean sendOrderShippedInPartsEmail(Order order, String invoiceLink) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("order", order);
        valuesMap.put("invoiceLink", invoiceLink);

        return emailService.sendHtmlEmail(EmailTemplateConstants.orderShippedInPartsEmail, valuesMap, order.getUser().getEmail(), order.getUser().getName());
    }

    public boolean sendPartialShippingEmail(Order order, String invoiceLink) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("order", order);
        valuesMap.put("invoiceLink", invoiceLink);

        return emailService.sendHtmlEmail(EmailTemplateConstants.partialOrderShippedEmailOld, valuesMap, order.getUser().getEmail(), order.getUser().getName());
    }

    public boolean sendReferralEmail(String toEmail, String fromName, String fromEmail, String unsubscribeLink, String signupLink, String couponCode, String customMessage) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("fromName", fromName);
        valuesMap.put("fromEmail", fromEmail);
        valuesMap.put("unsubscribeLink", unsubscribeLink);
        valuesMap.put("signupLink", signupLink);
        valuesMap.put("couponCode", couponCode);
        valuesMap.put("customMessage", HtmlUtil.convertNewLineToBr(customMessage));

        return emailService.sendHtmlEmail(EmailTemplateConstants.referralEmail, valuesMap, toEmail, toEmail, fromEmail);
    }

    public boolean affiliateVerfiedEmail(String toEmail, String toName, String couponCode, String customMessage) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("toName", toName);
        valuesMap.put("couponCode", couponCode);
        valuesMap.put("customMessage", HtmlUtil.convertNewLineToBr(customMessage));
        return emailService.sendHtmlEmail(EmailTemplateConstants.affiliateVerifiedEmail, valuesMap, toEmail, toEmail);
    }

    public boolean affiliateSignupEmail(String toEmail, String toName) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("toName", toName);
        return emailService.sendHtmlEmail(EmailTemplateConstants.affiliateSignupEmail, valuesMap, toEmail, toEmail);
    }

    public boolean sendProtocolViolationEmail(User violator, User against) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("violator", violator);
        valuesMap.put("against", against);
        return emailService.sendHtmlEmail(EmailTemplateConstants.protocolViolationEmail, valuesMap, "ajeet@healthkart.com", "Admin");
    }

    public boolean sendPOSentForApprovalEmail(PurchaseOrder purchaseOrder) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("purchaseOrder", purchaseOrder);
        return emailService.sendHtmlEmail(EmailTemplateConstants.poSentForApprovalEmail, valuesMap, purchaseOrder.getApprovedBy().getEmail(),
                purchaseOrder.getApprovedBy().getName());
    }

    public boolean sendPOPlacedEmail(PurchaseOrder purchaseOrder) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("purchaseOrder", purchaseOrder);
        return emailService.sendHtmlEmail(EmailTemplateConstants.poPlacedEmail, valuesMap, "accounts@healthkart.com", "Accounts Dept.");
    }

    public boolean sendReferralRewardPointEmail(RewardPoint rewardPoint, RewardPointTxn rewardPointTxn) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("rewardPoint", rewardPoint);
        valuesMap.put("rewardPointTxn", rewardPointTxn);
        return emailService.sendHtmlEmail(EmailTemplateConstants.referralRewardPointEmail, valuesMap, rewardPoint.getUser().getEmail(), rewardPoint.getUser().getFirstName());
    }

    public boolean sendCashBackRewardPointEmail(RewardPoint rewardPoint, RewardPointTxn rewardPointTxn) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("rewardPoint", rewardPoint);
        valuesMap.put("rewardPointTxn", rewardPointTxn);
        return emailService.sendHtmlEmail(EmailTemplateConstants.cashBackRewardPointEmail, valuesMap, rewardPoint.getUser().getEmail(), rewardPoint.getUser().getFirstName());
    }

    public boolean sendFBShareRewardPointEmail(RewardPoint rewardPoint, RewardPointTxn rewardPointTxn) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("rewardPoint", rewardPoint);
        valuesMap.put("rewardPointTxn", rewardPointTxn);
        return emailService.sendHtmlEmail(EmailTemplateConstants.fbShareRewardPointEmail, valuesMap, rewardPoint.getUser().getEmail(), rewardPoint.getUser().getFirstName());
    }

    public boolean sendReferralProgramIntro(User user, String referralProgramLink) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("user", user);
        valuesMap.put("referralProgramLink", referralProgramLink);

        return emailService.sendHtmlEmail(EmailTemplateConstants.referralProgramIntroEmail, valuesMap, user.getEmail(), user.getFirstName());
    }

    public boolean sendCreateTicketEmail(Ticket ticket) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("ticket", ticket);
        boolean reporterEmail = emailService.sendHtmlEmail(EmailTemplateConstants.createTicketEmail, valuesMap, ticket.getReporter().getEmail(), ticket.getReporter().getName(),
                ticket.getReporter().getEmail());
        boolean ownerEmail = true;
        if (!ticket.getReporter().equals(ticket.getOwner())) {
            ownerEmail = emailService.sendHtmlEmail(EmailTemplateConstants.createTicketEmail, valuesMap, ticket.getOwner().getEmail(), ticket.getOwner().getName(),
                    ticket.getReporter().getEmail());
        }
        return (ownerEmail && reporterEmail);
    }

    /*
     * public boolean sendServiceVoucherMailToCustomer(LineItem lineItem) { HashMap valuesMap = new HashMap();
     * valuesMap.put("lineItem", lineItem); valuesMap.put("pricingDto", new
     * PricingDto(lineItem.getOrder().getLineItems(), lineItem.getOrder().getAddress())); return
     * emailService.sendHtmlEmail(EmailTemplateConstants.serviceVoucherMailCustomer, valuesMap,
     * lineItem.getOrder().getUser().getEmail(), lineItem.getOrder().getUser().getName()); }
     */

    public boolean sendServiceVoucherMailToServiceProvider(Order order, CartLineItem lineItem) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("lineItem", lineItem);
        valuesMap.put("order", lineItem.getOrder());
        valuesMap.put("pricingDto", new PricingDto(order.getCartLineItems(), order.getAddress()));
        User adminUser = getUserService().getAdminUser();

        Manufacturer manufacturer = lineItem.getProductVariant().getProduct().getManufacturer();
        String comments = "Email Sent to " + manufacturer.getName() + " at " + manufacturer.getEmail();
        getOrderLoggingService().logOrderActivity(order, adminUser, getOrderLoggingService().getOrderLifecycleActivity(EnumOrderLifecycleActivity.EmailSentToServiceProvider), comments);
        return emailService.sendHtmlEmail(EmailTemplateConstants.serviceVoucherMailServiceProvider, valuesMap, manufacturer.getEmail(), manufacturer.getName());
    }

    public void sendPaymentFailMail(User user, String gatewayOrderId) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("user", user);
        valuesMap.put("gatewayOrderId", gatewayOrderId);
        emailService.sendHtmlEmail(EmailTemplateConstants.paymentFailEmail, valuesMap, "jatin.nayyar@healthkart.com", "Outbound Calling Team");
        //emailService.sendHtmlEmail(EmailTemplateConstants.paymentFailEmail, valuesMap, "info@healthkart.com", "Customer Support");
        //emailService.sendHtmlEmail(EmailTemplateConstants.paymentFailEmail, valuesMap, "pratham@healthkart.com", "Pratham");
        emailService.sendHtmlEmail(EmailTemplateConstants.paymentFailEmail, valuesMap, user.getEmail(), user.getName(), "info@healthkart.com");
    }

    public void sendCourierCollectionPostUpdationMessage(String email, String messagePostUpdation, String uploadDate) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("uploadDate", uploadDate);
        valuesMap.put("messagePostUpdation", messagePostUpdation);
        emailService.sendHtmlEmailNoReply(EmailTemplateConstants.courierCollerctionPostUpdationEmail, valuesMap, email, "HK Admin");
        emailService.sendHtmlEmailNoReply(EmailTemplateConstants.courierCollerctionPostUpdationEmail, valuesMap, "rahul.agarwal@healthkart.com", "HK Admin");
    }

    public void sendWeMissYouEmail(List<User> users, Coupon coupon, EmailCampaign emailCampaign, String xsmtpapi) {
        List<EmailRecepient> emailRecepients = filterUnwantedUsers(users, emailCampaign);
        logger.info("Filtered users list " + emailRecepients.size());
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("X-SMTPAPI", xsmtpapi);
        for (EmailRecepient emailRecepient : emailRecepients) {
            HashMap valuesMap = new HashMap();
            valuesMap.put("unsubscribeLink", getLinkManager().getEmailUnsubscribeLink(emailRecepient));
            if (emailRecepient.getUser() != null) {
                valuesMap.put("user", emailRecepient.getUser());
            }
            valuesMap.put("coupon", coupon);
            emailService.sendHtmlEmail(emailCampaign.getTemplate(), valuesMap, emailRecepient.getEmail(), emailRecepient.getEmail(), "info@healthkart.com", headerMap);
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
            if (emailRecepient.isSubscribed() && getEmailerHistoryDao().findEmailRecipientByCampaign(emailRecepient, emailCampaign) == null) {
                // last mail date null or last mail date > campaign min date
                if (emailRecepient.getLastEmailDate() == null
                        || new DateTime().minusDays(emailCampaign.getMinDayGap().intValue()).isAfter(emailRecepient.getLastEmailDate().getTime())) {
                    emailRecepients.add(emailRecepient);
                }
            }
        }
        return emailRecepients;
    }

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
