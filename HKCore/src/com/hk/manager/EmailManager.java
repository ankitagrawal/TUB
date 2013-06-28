package com.hk.manager;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.annotation.PostConstruct;

import com.akube.framework.util.DateUtils;
import com.akube.framework.util.FormatUtils;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.util.ssl.SslUtil;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.akube.framework.util.BaseUtils;
import com.hk.cache.CategoryCache;
import com.hk.constants.catalog.category.CategoryConstants;
import com.hk.constants.catalog.image.EnumImageSize;
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
import com.hk.domain.catalog.product.ProductOption;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.EmailType;
import com.hk.domain.coupon.Coupon;
import com.hk.domain.coupon.DiscountCouponMailingList;
import com.hk.domain.courier.Shipment;
import com.hk.domain.email.EmailCampaign;
import com.hk.domain.email.EmailRecepient;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.inventory.rtv.ExtraInventory;
import com.hk.domain.offer.rewardPoint.RewardPoint;
import com.hk.domain.offer.rewardPoint.RewardPointTxn;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.OrderCategory;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.review.Mail;
import com.hk.domain.shippingOrder.FixedShippingOrder;
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
import com.hk.pact.service.store.StoreService;
import com.hk.service.impl.FreeMarkerService;
import com.hk.util.HKImageUtils;
import com.hk.util.HtmlUtil;
import com.hk.util.ProductUtil;
import com.hk.web.filter.WebContext;

import freemarker.template.Template;

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
    private Set<String>         homeLivingAdminEmails           = null;

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
    @Autowired
    private FreeMarkerService   freeMarkerService;

    @Value("#{hkEnvProps['" + Keys.Env.hkAdminEmails + "']}")
    private String              hkAdminEmailsString;
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
    @Value("#{hkEnvProps['" + Keys.Env.sportsAdminEmails + "']}")
    private String              sportsAdminEmailsString       = null;
    @Value("#{hkEnvProps['" + Keys.Env.servicesAdminEmails + "']}")
    private String              servicesAdminEmailsString     = null;
    @Value("#{hkEnvProps['" + Keys.Env.homeLivingAdminEmails + "']}")
    private String              homeLivingAdminEmailsString     = null;

    @Value("#{hkEnvProps['" + Keys.Env.hkNoReplyEmail + "']}")
    private String              hkNoReplyEmail;
    @Value("#{hkEnvProps['" + Keys.Env.hkNoReplyName + "']}")
    private String              hkNoReplyName;
    @Value("#{hkEnvProps['" + Keys.Env.hkContactEmail + "']}")
    private String              hkContactEmail;
    @Value("#{hkEnvProps['" + Keys.Env.logisticsOpsEmails + "']}")
	  private String              logisticsOpsEmails;
    /*
     * @Value("#{hkEnvProps['" + Keys.Env.hkContactName + "']}") private String hkContactName;
     */

    @PostConstruct
    public void postConstruction() {
        this.hkAdminEmails = BaseUtils.split(this.hkAdminEmailsString, ",");
        this.babyAdminEmails = BaseUtils.split(this.babyAdminEmailsString, ",");
        this.beautyAdminEmails = BaseUtils.split(this.beautyAdminEmailsString, ",");
        this.diabetesAdminEmails = BaseUtils.split(this.diabetesAdminEmailsString, ",");
        this.eyeAdminEmails = BaseUtils.split(this.eyeAdminEmailsString, ",");
        this.homeDevicesAdminEmails = BaseUtils.split(this.homeDevicesAdminEmailsString, ",");
        this.nutritionAdminEmails = BaseUtils.split(this.nutritionAdminEmailsString, ",");
        this.personalCareAdminEmails = BaseUtils.split(this.personalCareAdminEmailsString, ",");
        this.sportsAdminEmails = BaseUtils.split(this.sportsAdminEmailsString, ",");
        this.servicesAdminEmails = BaseUtils.split(this.servicesAdminEmailsString, ",");
        this.homeLivingAdminEmails = BaseUtils.split(this.homeLivingAdminEmailsString, ",");
    }

    // TODO:rewrite

    public boolean sendInventoryRedZoneMail(ProductVariant productVariant) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("productVariant", productVariant);

        Category basketCategory = null;
        try {
            basketCategory = this.getCategoryService().getTopLevelCategory(productVariant.getProduct());
            String category = basketCategory.getDisplayName();
            valuesMap.put("category", category);
        } catch (Exception e) {
            logger.error("Exception thrown while getting category", e);
        }

        boolean success = true;

        // Sending category specific emails to category admins.
        if (basketCategory != null) {
            Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.inventoryRedZoneEmail);
            String basketCategoryName = basketCategory.getDisplayName();
            for (String categoryAdminEmail : this.categoryAdmins(basketCategory)) {
                boolean sent = this.emailService.sendHtmlEmailNoReply(freemarkerTemplate, valuesMap, categoryAdminEmail, basketCategoryName + " Category Admin");
                if (!sent) {
					success = false;
				}
            }
        }

        return success;
    }

    public boolean sendOutOfStockMail(ProductVariant productVariant) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("productVariant", productVariant);

        Category basketCategory = null;
        try {
            basketCategory = this.getCategoryService().getTopLevelCategory(productVariant.getProduct());
            String category = basketCategory.getDisplayName();
            valuesMap.put("category", category);
        } catch (Exception e) {
            logger.error("Exception thrown while getting category", e);
        }

        boolean success = true;

        // Sending category specific emails to category admins.
        if (basketCategory != null) {
            Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.outOfStockEmail);
            String basketCategoryName = basketCategory.getDisplayName();
            for (String categoryAdminEmail : this.categoryAdmins(basketCategory)) {
                boolean sent = this.emailService.sendHtmlEmailNoReply(freemarkerTemplate, valuesMap, categoryAdminEmail, basketCategoryName + " Category Admin");
                if (!sent) {
					success = false;
				}
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
            // basketCategory = getCategoryService().getCategoryByName(basketCat);
            basketCategory = CategoryCache.getInstance().getCategoryByName(basketCat).getCategory();
        } catch (Exception e) {
            logger.error("Exception thrown while getting basket category", e);
        }
        valuesMap.put("basketCategory", basketCategory);

        boolean success = true;
        Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.orderConfirmAdminEmail);
        for (String hkAdminEmail : this.hkAdminEmails) {
            boolean sent = this.emailService.sendHtmlEmailNoReply(freemarkerTemplate, valuesMap, hkAdminEmail, "HK Admin");
            if (!sent) {
				success = false;
			}
        }
        // Sending category specific emails to category admins.
        Set<OrderCategory> orderCategories = order.getCategories();
        for (OrderCategory orderCategory : orderCategories) {
            String categoryName = orderCategory.getCategory().getDisplayName();
            for (String categoryAdminEmail : this.categoryAdmins(orderCategory.getCategory())) {
                boolean sent = this.emailService.sendHtmlEmailNoReply(freemarkerTemplate, valuesMap, categoryAdminEmail, categoryName + " Category Admin");
                if (!sent) {
					success = false;
				}
            }
        }
        return success;
    }

    public Set<String> categoryAdmins(Category category) {
        Set<String> categoryAdmins = new HashSet<String>();

        if (category.getName().equals(CategoryConstants.BABY)) {
            categoryAdmins = this.babyAdminEmails;
        } else if (category.getName().equals(CategoryConstants.BEAUTY)) {
            categoryAdmins = this.beautyAdminEmails;
        } else if (category.getName().equals(CategoryConstants.DIABETES)) {
            categoryAdmins = this.diabetesAdminEmails;
        } else if (category.getName().equals(CategoryConstants.EYE)) {
            categoryAdmins = this.eyeAdminEmails;
        } else if (category.getName().equals(CategoryConstants.HEALTH_DEVICES)) {
            categoryAdmins = this.homeDevicesAdminEmails;
        } else if (category.getName().equals(CategoryConstants.NUTRITION) || category.getName().equals(CategoryConstants.SPORTS_NUTRITION) || category.getName().equals(CategoryConstants.HEALTH_NUTRITION)) {
            categoryAdmins = this.nutritionAdminEmails;
        } else if (category.getName().equals(CategoryConstants.PERSONAL_CARE)) {
            categoryAdmins = this.personalCareAdminEmails;
        } else if (category.getName().equals(CategoryConstants.SERVICES)) {
            categoryAdmins = this.servicesAdminEmails;
        } else if (category.getName().equals(CategoryConstants.SPORTS)) {
            categoryAdmins = this.sportsAdminEmails;
        } else if (category.getName().equals(CategoryConstants.HOME_LIVING)) {
            categoryAdmins = this.homeLivingAdminEmails;
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
        // Set<CartLineItem> serviceCartLineItems = new HashSet<CartLineItem>();

        for (OrderCategory orderCategory : orderCategories) {
            if (orderCategory.getCategory().getName().equals(CategoryConstants.SERVICES)) {
                isServiceOrder = Boolean.TRUE;
                break;
            }
        }

        // if (isServiceOrder) {
        // serviceCartLineItems = new CartLineItemFilter(orderCartLineItems).setCategoryName("services").filter();
        // }

        valuesMap.put("order", order);
        valuesMap.put("pricingDto", pricingDto);
        valuesMap.put("categoryCountInOrder", categoryCountInOrder);
        valuesMap.put("isServiceOrder", isServiceOrder);
        // valuesMap.put("serviceCartLineItems", serviceCartLineItems);
        
        Template freemarkerTemplate;
        if (order.getStore().getId().equals(StoreService.LOYALTYPG_ID)) {
        	freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.orderConfirmUserEmailLoyalty);
        } else {
        	freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.orderConfirmUserEmail);
        }
        return this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, order.getUser().getEmail(), order.getUser().getName());
    }

    public boolean sendOrderPlacedPaymentPendingEmailToUser(Order order) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("order", order);
        valuesMap.put("pricingDto", new PricingDto(order.getCartLineItems(), order.getAddress()));

        Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.orderPlacedPaymentPendingEmail);
        return this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, order.getUser().getEmail(), order.getUser().getName());
    }

    public boolean sendOrderPlacedCodEmailToUser(Order order) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("order", order);
        valuesMap.put("pricingDto", new PricingDto(order.getCartLineItems(), order.getAddress()));

        Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.orderPlacedCodEmail);
        return this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, order.getUser().getEmail(), order.getUser().getName());
    }

    public boolean sendOrderPlacedAuthorizedCodEmailToUser(Order order) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("order", order);
        valuesMap.put("pricingDto", new PricingDto(order.getCartLineItems(), order.getAddress()));

        Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.orderPlacedAuthorizedCodEmail);
        return this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, order.getUser().getEmail(), order.getUser().getName());
    }

    public boolean sendOrderPlacedOtherPaymentModeEmailToUser(Order order) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("order", order);
        valuesMap.put("pricingDto", new PricingDto(order.getCartLineItems(), order.getAddress()));

        Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.orderPlacedOtherModeEmail);
        return this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, order.getUser().getEmail(), order.getUser().getName());
    }

    public boolean sendOrderCancelEmailToUser(Order order) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("order", order);
        valuesMap.put("pricingDto", new PricingDto(order.getCartLineItems(), order.getAddress()));
        
        Template freemarkerTemplate;
        if (order.getStore().getId().equals(StoreService.LOYALTYPG_ID)) {
        	freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.orderCancelEmailUserLoyalty);
        } else {
        	freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.orderCancelEmailUser);
        }
        
        return this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, order.getUser().getEmail(), order.getUser().getName());
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
            Category basketCategory = this.getCategoryService().getTopLevelCategory(firstCartLineItem.getProductVariant().getProduct());
            String basketCatName = basketCategory.getDisplayName();
            valuesMap.put("basketCategory", basketCatName);

            // Sending category specific emails to category admins.
            if (basketCategory != null) {
                Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.orderCancelEmailAdmin);
                String basketCategoryName = basketCategory.getDisplayName();
                for (String categoryAdminEmail : this.categoryAdmins(basketCategory)) {
                    success = this.emailService.sendHtmlEmailNoReply(freemarkerTemplate, valuesMap, categoryAdminEmail, basketCategoryName + " Category Admin");
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

        Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.welcomeEmail);
        return this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, user.getEmail(), user.getName());
    }

    public boolean sendAccountActivationEmail(User user, String activationLink) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("user", user);
        valuesMap.put("activationLink", activationLink);

        Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.accountActivationEmail);
        return this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, user.getEmail(), user.getName());
    }

    public boolean sendResetPasswordEmail(User user, String passwordResetLink) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("user", user);
        valuesMap.put("link", passwordResetLink);

        Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.passwordResetEmail);
        return this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, user.getEmail(), user.getName());
    }

    public boolean sendProductReviewEmail(User user, ProductVariant productVariant, Mail mail, String testEmailId, long userReviewMailId){
        HashMap valuesMap = new HashMap();
        valuesMap.put("user", user);
        String productVariantName = productVariant.getProduct().getName() + (productVariant.getVariantName() == null ? "" : productVariant.getVariantName()) ;
        /*if(productVariant.getVariantName() != null){
            valuesMap.put("product", productVariantName+" "+ productVariant.getVariantName());
        }else
            valuesMap.put("product", productVariantName);
        */
        String productImage = "";
        if(productVariant.getMainImageId() != null) {
            productImage = HKImageUtils.getS3ImageUrl(EnumImageSize.TinySize,productVariant.getMainImageId());
        }
        else if(productVariant.getProduct().getMainImageId() != null) {
            productImage = HKImageUtils.getS3ImageUrl(EnumImageSize.TinySize,productVariant.getProduct().getMainImageId());
        }
        else{
            String url = "/images/ProductImages/ProductImagesThumb/"+productVariant.getProduct().getId()+".jpg";
            RedirectResolution redirectResolution = new RedirectResolution(url);
            url = redirectResolution.getUrl(Locale.getDefault());
            productImage = SslUtil.encodeUrlFullForced(WebContext.getRequest(), WebContext.getResponse(), url, null);
        }

        StringBuilder productDiv = new StringBuilder("<div>\n<div style=\"width: 48px; height: 64px; display: inline-block; text-align: center; vertical-align: top\">\n");
        productDiv.append("<img style=\"max-height: 64px; max-width: 48px; font-size: 12px;\" src=\""+productImage+"\" alt=\""+productVariantName+"\"/>\n</div>\n");
        productDiv.append("<div class=\"name\" style=\"font-size: 14px; line-height: 21px; display: inline-block; width: 310px;\">\n" +productVariantName+"<br/>\n");
        if(productVariant.getProductOptions() != null){
            productDiv.append("<table style=\"display: inline-block; font-size: 12px;\">\n");
            for(ProductOption productOption : productVariant.getProductOptions()){
                if(ProductUtil.getVariantValidOptions().contains(productOption.getName().toUpperCase())){
                    productDiv.append("<tr>\n<td style=\"text-align: left;  padding: 0.3em 2em;border: 1px solid #f0f0f0; background: #fafafa;\">"+productOption.getName()+"</td>\n" +
                            "<td style=\"text-align: left; padding: 0.3em 2em;border: 1px solid #f0f0f0; background: #fff;\">");
//                  if(Functions.startsWith(productOption.getValue(),"-"))
                    productDiv.append(productOption.getValue()+"</td>\n</tr>");
                }
            }
            productDiv.append("</table>\n");
        }
        productDiv.append("</div>\n</div>");
        valuesMap.put("productName", productVariantName);
        valuesMap.put("productOptionDiv", productDiv);

        HashMap params = new HashMap();
        params.put("writeNewReviewByMail","");
        params.put("productVariant",productVariant.getId());
        params.put("uid",user.getLogin());
        params.put("urm",userReviewMailId);
        String review_link = this.getLinkManager().getReviewPageLink(params);
        valuesMap.put("review_Link", review_link);

        String unsubscribeLink = this.getLinkManager().getUnsubscribeLink(user);
        valuesMap.put("unsubscribeLink", unsubscribeLink);
        String source = "src";
        valuesMap.put("source", source);
        /*String contextPath = AppConstants.contextPath;
        valuesMap.put("contextPath", contextPath);*/

        //template contents from db
        String mailTemplateContents = mail.getContent();
        if (StringUtils.isNotEmpty(mailTemplateContents)) {
            StringBuilder finalContents = new StringBuilder(mail.getSubject());
            finalContents.append(BaseUtils.newline + mailTemplateContents);
            Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplateFromString(finalContents.toString());
            if(StringUtils.isNotEmpty(testEmailId)) {
				return this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, testEmailId, user.getName());
			} else {
				return this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, user.getEmail(), user.getName());
			}
        } else {
            logger.info(mail.getName()+" Template Content is not present");
            return false;
        }
    }

    public boolean sendDiscountCouponEmail(String name, String email, String coupon) {
        HashMap valuesMap = new HashMap();

        if (name == null) {
			name = "Guest User";
		}

        valuesMap.put("name", name);
        valuesMap.put("coupon", coupon);

        Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.discountCouponEmail);
        return this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, email, name);
    }

    public boolean sendContactUsMail(String name, String email, String phone, String subject, String message) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("name", name);
        valuesMap.put("email", email);
        valuesMap.put("phone", phone);
        valuesMap.put("subject", subject);
        valuesMap.put("message", HtmlUtil.convertNewLineToBr(message));

        boolean success = true;

        Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.contactUsEmail);
        for (String hkAdminEmail : this.hkAdminEmails) {
            boolean sent = this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, hkAdminEmail, "Admin", email);
            if (!sent) {
				success = false;
			}
        }
        // boolean sent = emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, EmailConstants.getHkContactEmail(),
        // "Admin", email);
        boolean sent = this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, this.hkContactEmail, "HK Info", email);
        if (!sent) {
			success = false;
		}
        return success;
    }

    public boolean sendOrderShippedEmail(ShippingOrder shippingOrder, String invoiceLink) {
        Shipment shipment = shippingOrder.getShipment();
        shipment.setTrackLink(this.getLinkManager().getOrderTrackLink(shipment.getAwb().getAwbNumber(), shipment.getAwb().getCourier().getId(), shippingOrder));
        HashMap valuesMap = new HashMap();

        String templatePath = EmailTemplateConstants.orderShippedEmail;

        List<ShippingOrder> shippingOrderYetToBeSentList = new ArrayList<ShippingOrder>();
        List<ShippingOrder> shippingOrderAlreadySentList = new ArrayList<ShippingOrder>();

        Order order = shippingOrder.getBaseOrder();
        valuesMap.put("order", shippingOrder);
        valuesMap.put("invoiceLink", invoiceLink);
        valuesMap.put("targetDeliverDate", DateUtils.getDateString(shippingOrder.getTargetDelDate()));

        for (ShippingOrder shippingOrderFromAllSO : order.getShippingOrders()) {
            if (shippingOrderFromAllSO.isServiceOrder() || shippingOrderFromAllSO.getId().equals(shippingOrder.getId())
                    || shippingOrderFromAllSO.getOrderStatus().equals(EnumShippingOrderStatus.SO_Cancelled.asShippingOrderStatus())) {
                continue;
            } else if (shippingOrderFromAllSO.getOrderStatus().equals(EnumShippingOrderStatus.SO_Shipped.asShippingOrderStatus())
                    || shippingOrderFromAllSO.getOrderStatus().equals(EnumShippingOrderStatus.SO_Delivered.asShippingOrderStatus())) {
                shippingOrderAlreadySentList.add(shippingOrderFromAllSO);
                valuesMap.put("shippingOrderAlreadySentList", shippingOrderAlreadySentList);
            } else if (shippingOrderFromAllSO.getOrderStatus().equals(EnumShippingOrderStatus.SO_ActionAwaiting.asShippingOrderStatus())
                    || shippingOrderFromAllSO.getOrderStatus().equals(EnumShippingOrderStatus.SO_CheckedOut.asShippingOrderStatus())
                    || shippingOrderFromAllSO.getOrderStatus().equals(EnumShippingOrderStatus.SO_ReadyForProcess.asShippingOrderStatus())
                    || shippingOrderFromAllSO.getOrderStatus().equals(EnumShippingOrderStatus.SO_EscalatedBack.asShippingOrderStatus())
                    || shippingOrderFromAllSO.getOrderStatus().equals(EnumShippingOrderStatus.SO_OnHold.asShippingOrderStatus())
                    || shippingOrderFromAllSO.getOrderStatus().equals(EnumShippingOrderStatus.SO_Packed.asShippingOrderStatus())
                    || shippingOrderFromAllSO.getOrderStatus().equals(EnumShippingOrderStatus.SO_Picking.asShippingOrderStatus())
                    || shippingOrderFromAllSO.getOrderStatus().equals(EnumShippingOrderStatus.SO_MarkedForPrinting.asShippingOrderStatus())) {
                shippingOrderYetToBeSentList.add(shippingOrderFromAllSO);
                valuesMap.put("shippingOrderYetToBeSentList", shippingOrderYetToBeSentList);
                templatePath = EmailTemplateConstants.partialOrderShippedEmail;
            }
        }

        Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(templatePath);
        return this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, shippingOrder.getBaseOrder().getUser().getEmail(), shippingOrder.getBaseOrder().getUser().getName());
    }

    public boolean sendSubscriptionOrderShippedEmail(ShippingOrder shippingOrder, Subscription subscription, String invoiceLink) {
        Shipment shipment = shippingOrder.getShipment();
        shipment.setTrackLink(this.getLinkManager().getOrderTrackLink(shipment.getAwb().getAwbNumber(), shipment.getAwb().getCourier().getId(), shippingOrder));
        HashMap valuesMap = new HashMap();
        valuesMap.put("subscription", subscription);
        valuesMap.put("order", shippingOrder);
        valuesMap.put("invoiceLink", invoiceLink);

        Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.subscriptionOrderShippedEmail);
        return this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, shippingOrder.getBaseOrder().getUser().getEmail(), shippingOrder.getBaseOrder().getUser().getName());
    }

    public boolean sendSubscriptionCancellationEmail(Subscription subscription) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("subscription", subscription);

        Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.subscriptionCancelEmailUser);
        return this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, subscription.getBaseOrder().getUser().getEmail(), subscription.getBaseOrder().getUser().getName());
    }

    public boolean sendSubscriptionCancellationEmailToAdmin(Subscription subscription) {
        boolean success = false;
        HashMap valuesMap = new HashMap();
        valuesMap.put("subscription", subscription);
        Category basketCategory = this.getCategoryService().getTopLevelCategory(subscription.getProductVariant().getProduct());
        if (basketCategory != null) {
            Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.subscriptionCancelEmailAdmin);
            String basketCategoryName = basketCategory.getDisplayName();
            for (String categoryAdminEmail : this.categoryAdmins(basketCategory)) {
                success = this.emailService.sendHtmlEmailNoReply(freemarkerTemplate, valuesMap, categoryAdminEmail, basketCategoryName + " Category Admin");
                /* if (!sent) success = false; */
            }
        }
        return success;
    }

    public boolean sendSubscriptionPlacedEmailToUser(Subscription subscription) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("subscription", subscription);

        Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.subscriptionPlacedEmailUser);
        return this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, subscription.getBaseOrder().getUser().getEmail(), subscription.getBaseOrder().getUser().getName());
    }

    public boolean sendSubscriptionPlacedEmailToAdmin(Subscription subscription) {
        boolean success = false;
        HashMap valuesMap = new HashMap();
        valuesMap.put("subscription", subscription);
        Category basketCategory = this.getCategoryService().getTopLevelCategory(subscription.getProductVariant().getProduct());
        if (basketCategory != null) {
            Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.subscriptionPlacedEmailAdmin);
            String basketCategoryName = basketCategory.getDisplayName();
            for (String categoryAdminEmail : this.categoryAdmins(basketCategory)) {
                success = this.emailService.sendHtmlEmailNoReply(freemarkerTemplate, valuesMap, categoryAdminEmail, basketCategoryName + " Category Admin");
                /* if (!sent) success = false; */
            }
        }
        return success;
    }

    public boolean sendSubscriptionVariantOutOfStockEmailAdmin(Subscription subscription) {
        boolean success = false;
        HashMap valuesMap = new HashMap();
        valuesMap.put("subscription", subscription);
        Category basketCategory = this.getCategoryService().getTopLevelCategory(subscription.getProductVariant().getProduct());
        if (basketCategory != null) {
            Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.subscriptionVariantOutOfStockEmailAdmin);
            String basketCategoryName = basketCategory.getDisplayName();
            for (String categoryAdminEmail : this.categoryAdmins(basketCategory)) {
                success = this.emailService.sendHtmlEmailNoReply(freemarkerTemplate, valuesMap, categoryAdminEmail, basketCategoryName + " Category Admin");
                /* if (!sent) success = false; */
            }
        }
        return success;
    }

    public boolean sendOrderShippedInPartsEmail(Order order, String invoiceLink) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("order", order);
        valuesMap.put("invoiceLink", invoiceLink);

        Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.orderShippedInPartsEmail);
        return this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, order.getUser().getEmail(), order.getUser().getName());
    }

    public boolean sendPartialShippingEmail(Order order, String invoiceLink) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("order", order);
        valuesMap.put("invoiceLink", invoiceLink);

        Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.partialOrderShippedEmailOld);
        return this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, order.getUser().getEmail(), order.getUser().getName());
    }

    public boolean sendReferralEmail(String toEmail, String fromName, String fromEmail, String unsubscribeLink, String signupLink, String couponCode, String customMessage) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("fromName", fromName);
        valuesMap.put("fromEmail", fromEmail);
        valuesMap.put("unsubscribeLink", unsubscribeLink);
        valuesMap.put("signupLink", signupLink);
        valuesMap.put("couponCode", couponCode);
        valuesMap.put("customMessage", HtmlUtil.convertNewLineToBr(customMessage));

        Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.referralEmail);
        return this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, toEmail, toEmail, fromEmail);
    }

    public boolean affiliateVerfiedEmail(String toEmail, String toName, String couponCode, String customMessage) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("toName", toName);
        valuesMap.put("couponCode", couponCode);
        valuesMap.put("customMessage", HtmlUtil.convertNewLineToBr(customMessage));

        Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.affiliateVerifiedEmail);
        return this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, toEmail, toEmail);
    }

    public boolean affiliateSignupEmail(String toEmail, String toName) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("toName", toName);

        Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.affiliateSignupEmail);
        return this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, toEmail, toEmail);
    }

    public boolean sendProtocolViolationEmail(User violator, User against) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("violator", violator);
        valuesMap.put("against", against);

        Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.protocolViolationEmail);
        return this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, "ajeet@healthkart.com", "Admin");
    }

    public boolean sendPOSentForApprovalEmail(PurchaseOrder purchaseOrder) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("purchaseOrder", purchaseOrder);

        Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.poSentForApprovalEmail);
        return this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, purchaseOrder.getApprovedBy().getEmail(), purchaseOrder.getApprovedBy().getName());
    }

    public boolean sendPOApprovedEmail(PurchaseOrder purchaseOrder) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("purchaseOrder", purchaseOrder);

        Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.poApprovedEmail);
        return this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, purchaseOrder.getCreatedBy().getEmail(), purchaseOrder.getCreatedBy().getName());
    }

    public boolean sendPOPlacedEmail(PurchaseOrder purchaseOrder) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("purchaseOrder", purchaseOrder);

        Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.poPlacedEmail);
        return this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, "accounts@healthkart.com", "Accounts Dept.");
    }

    public boolean sendReferralRewardPointEmail(RewardPoint rewardPoint, RewardPointTxn rewardPointTxn) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("rewardPoint", rewardPoint);
        valuesMap.put("rewardPointTxn", rewardPointTxn);

        Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.referralRewardPointEmail);
        return this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, rewardPoint.getUser().getEmail(), rewardPoint.getUser().getFirstName());
    }

    public boolean sendCashBackRewardPointEmail(RewardPoint rewardPoint, RewardPointTxn rewardPointTxn) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("rewardPoint", rewardPoint);
        valuesMap.put("rewardPointTxn", rewardPointTxn);

        Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.cashBackRewardPointEmail);
        return this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, rewardPoint.getUser().getEmail(), rewardPoint.getUser().getFirstName());
    }

    public boolean sendFBShareRewardPointEmail(RewardPoint rewardPoint, RewardPointTxn rewardPointTxn) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("rewardPoint", rewardPoint);
        valuesMap.put("rewardPointTxn", rewardPointTxn);

        Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.fbShareRewardPointEmail);
        return this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, rewardPoint.getUser().getEmail(), rewardPoint.getUser().getFirstName());
    }

    public boolean sendReferralProgramIntro(User user, String referralProgramLink) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("user", user);
        valuesMap.put("referralProgramLink", referralProgramLink);

        Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.referralProgramIntroEmail);
        return this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, user.getEmail(), user.getFirstName());
    }

    public boolean sendCreateTicketEmail(Ticket ticket) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("ticket", ticket);

        Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.createTicketEmail);
        boolean reporterEmail = this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, ticket.getReporter().getEmail(), ticket.getReporter().getName(),
                ticket.getReporter().getEmail());
        boolean ownerEmail = true;
        if (!ticket.getReporter().equals(ticket.getOwner())) {
            ownerEmail = this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, ticket.getOwner().getEmail(), ticket.getOwner().getName(), ticket.getReporter().getEmail());
        }
        return (ownerEmail && reporterEmail);
    }

    public boolean sendServiceVoucherMailToServiceProvider(Order order, CartLineItem lineItem) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("lineItem", lineItem);
        valuesMap.put("order", lineItem.getOrder());
        valuesMap.put("pricingDto", new PricingDto(order.getCartLineItems(), order.getAddress()));
         User adminUser = this.getUserService().getAdminUser();

        //User adminUser = UserCache.getInstance().getAdminUser();

        Manufacturer manufacturer = lineItem.getProductVariant().getProduct().getManufacturer();
        String comments = "Emails Sent to " + manufacturer.getName() + " at " + manufacturer.getEmail();
        this.getOrderLoggingService().logOrderActivity(order, adminUser, this.getOrderLoggingService().getOrderLifecycleActivity(EnumOrderLifecycleActivity.EmailSentToServiceProvider),comments);
        Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.serviceVoucherMailServiceProvider);
        boolean bool = false, boolFinal = false;
        String[] emails = manufacturer.getEmail().split(",");
        if(emails.length>=1){
          for(String email : emails){
           bool = this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, email , manufacturer.getName());
            if(bool) {
				boolFinal = true;
			}
          }
        }
      return boolFinal;
    }

    public void sendPaymentFailMail(User user, String gatewayOrderId) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("user", user);
        valuesMap.put("gatewayOrderId", gatewayOrderId);

        Template adminFreemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.adminPaymentFailEmail);
        this.emailService.sendHtmlEmail(adminFreemarkerTemplate, valuesMap, "jatin.nayyar@healthkart.com", "Outbound Calling Team");

        Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.paymentFailEmail);

        this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, user.getEmail(), user.getName(), this.hkContactEmail);

    }

    public void sendCallbackRequestEmail(User user, DiscountCouponMailingList dcml) {
      HashMap valuesMap = new HashMap();
      valuesMap.put("dcml", dcml);
      if (user != null) {
		valuesMap.put("userId", user.getId());
	} else {
		valuesMap.put("userId", "Guest User");
	}

      Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.callbackRequestEmail);
      List<String> emailIds = new ArrayList<String>();
      if (dcml.getCategory() != null && dcml.getCategory().equals("nutrition")) {
        emailIds.add("umang.mehta@healthkart.com");
        emailIds.add("jatin.nayyar@healthkart.com");
      } else if (dcml.getCategory() != null && dcml.getCategory().equals("eye")) {
        emailIds.add("category.eye@healthkart.com");
      } else if (dcml.getCategory() != null && dcml.getCategory().equals("sports")) {
        emailIds.add("category.sports@healthkart.com");
      }
      for (String emailId : emailIds) {
        this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, emailId, "Callback Request - " + dcml.getCategory());
      }

    }

    public void sendCodConverterMail(Order order) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("order", order);
        valuesMap.put("codConverterLink", this.linkManager.getCodConverterLink(order));

        Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.codConverterEmail);
        this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, order.getUser().getEmail(), order.getUser().getName(), "info@healthkart.com");
    }

    public void sendCourierCollectionPostUpdationMessage(String email, String messagePostUpdation, String uploadDate) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("uploadDate", uploadDate);
        valuesMap.put("messagePostUpdation", messagePostUpdation);

        Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.courierCollerctionPostUpdationEmail);
        this.emailService.sendHtmlEmailNoReply(freemarkerTemplate, valuesMap, email, "HK Admin");
        this.emailService.sendHtmlEmailNoReply(freemarkerTemplate, valuesMap, "rahul.agarwal@healthkart.com", "HK Admin");
    }

    public void sendWeMissYouEmail(List<User> users, Coupon coupon, EmailCampaign emailCampaign, String xsmtpapi) {
        List<EmailRecepient> emailRecepients = this.filterUnwantedUsers(users, emailCampaign);
        logger.info("Filtered users list " + emailRecepients.size());
        Map<String, String> headerMap = new HashMap<String, String>();
        headerMap.put("X-SMTPAPI", xsmtpapi);

        Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(emailCampaign.getTemplate());
        for (EmailRecepient emailRecepient : emailRecepients) {
            HashMap valuesMap = new HashMap();
            valuesMap.put("unsubscribeLink", this.getLinkManager().getEmailUnsubscribeLink(emailRecepient));
            if (emailRecepient.getUser() != null) {
                valuesMap.put("user", emailRecepient.getUser());
            }
            valuesMap.put("coupon", coupon);
            this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, emailRecepient.getEmail(), emailRecepient.getEmail(), this.hkContactEmail, headerMap);
            // keep a record in history
            emailRecepient.setEmailCount(emailRecepient.getEmailCount() + 1);
            emailRecepient.setLastEmailDate(new Date());
            this.getEmailRecepientDao().save(emailRecepient);
            this.getEmailerHistoryDao().createEmailerHistory(this.hkNoReplyEmail, this.hkNoReplyName, this.getBaseDao().get(EmailType.class, EnumEmailType.MissYouEmail.getId()), emailRecepient,
                    emailCampaign, "");
        }
    }

    public List<EmailRecepient> filterUnwantedUsers(List<User> users, EmailCampaign emailCampaign) {
        List<EmailRecepient> emailRecepients = new ArrayList<EmailRecepient>();
        for (User user : users) {
            EmailRecepient emailRecepient = this.getEmailRecepientDao().getOrCreateEmailRecepient(user.getEmail());
            if (emailRecepient.isEmailAllowed() && this.getEmailerHistoryDao().findEmailRecipientByCampaign(emailRecepient, emailCampaign) == null) {
                // last mail date null or last mail date > campaign min date
                if (emailRecepient.getLastEmailDate() == null
                        || new DateTime().minusDays(emailCampaign.getMinDayGap().intValue()).isAfter(emailRecepient.getLastEmailDate().getTime())) {
                    emailRecepients.add(emailRecepient);
                }
            }
        }
        return emailRecepients;
    }



    public boolean sendEscalationToDropShipEmail(ShippingOrder shippingOrder) {
        HashMap valuesMap = new HashMap();
        valuesMap.put("shippingOrder", shippingOrder);
        Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.dropShipEscalationEmail);
        return this.emailService.sendHtmlEmail(freemarkerTemplate, valuesMap, this.logisticsOpsEmails,
                EmailTemplateConstants.operationsTeam);
    }

  public boolean sendExtraInventoryMail(ExtraInventory extraInventory){
        HashMap valuesMap = new HashMap();
        valuesMap.put("extraInventory", extraInventory);
        Template freemarkerTemplate = this.freeMarkerService.getCampaignTemplate(EmailTemplateConstants.extraInventoryCreatedEmailToCategory);
        Category category = extraInventory.getPurchaseOrder().getPoLineItems().get(0).getSku().getProductVariant().getProduct().getPrimaryCategory();
        for(String categoryAdminEmail : this.categoryAdmins(category)){
            this.emailService.sendHtmlEmailNoReply(freemarkerTemplate,valuesMap,categoryAdminEmail,category.getDisplayName());
        }
    return true;
  }
    public boolean sendAdminPaymentStatusChangeEmail(String actualStatus, String changedStatus, String gatewayOrderId){
        HashMap valueMap = new HashMap();
        valueMap.put("username","Admin");
        valueMap.put("gatewayOrderId", gatewayOrderId);
        valueMap.put("oldStatus", actualStatus);
        valueMap.put("newStatus", changedStatus);
        Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.adminPaymentStatusChangeEmail);
        return emailService.sendHtmlEmail(freemarkerTemplate, valueMap, "pratham@healthkart.com", "Admin");
    }

    public boolean sendPaymentMisMatchMailToAdmin(Double actualAmt, Double gatewayAmount, String gatewayOrderId){
        HashMap valueMap = new HashMap();
        valueMap.put("username","Admin");
        valueMap.put("gatewayOrderId", gatewayOrderId);
        valueMap.put("RequestAmount", actualAmt);
        valueMap.put("RequestAmount", gatewayAmount);
        Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.adminPaymentMisMatchMail);
        return emailService.sendHtmlEmail(freemarkerTemplate, valueMap, "pratham@healthkart.com", "Admin");
    }

    public boolean sendSoFixedMail(HashMap<String, String> map){
        Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.soFixedMail);
        return emailService.sendHtmlEmail(freemarkerTemplate, map, hkContactEmail, "Admin");
    }
    
    public boolean sendSoFixFailedMail(HashMap<String, String> map){
        Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.soFixFailedMail);
        return emailService.sendHtmlEmail(freemarkerTemplate, map, hkContactEmail, "Admin");
    }
    
    /*
     * public boolean sendProductStatusMail(Product product, String stockStatus) { HashMap valuesMap = new HashMap();
     * valuesMap.put("product", product); valuesMap.put("stockStatus", stockStatus); boolean success = true; Template
     * freemarkerTemplate = freeMarkerService.getCampaignTemplate(EmailTemplateConstants.productStockStatusEmail); for
     * (String marketingAdsMonitorEmail : marketingAdsMonitorEmails) { boolean sent =
     * emailService.sendHtmlEmailNoReply(freemarkerTemplate, valuesMap, marketingAdsMonitorEmail, " Adv Admin"); if
     * (!sent) success = false; } return success; }
     */

    public EmailService getEmailService() {
        return this.emailService;
    }

    public void setEmailService(EmailService emailService) {
        this.emailService = emailService;
    }

    public EmailRecepientDao getEmailRecepientDao() {
        return this.emailRecepientDao;
    }

    public void setEmailRecepientDao(EmailRecepientDao emailRecepientDao) {
        this.emailRecepientDao = emailRecepientDao;
    }

    public EmailerHistoryDao getEmailerHistoryDao() {
        return this.emailerHistoryDao;
    }

    public void setEmailerHistoryDao(EmailerHistoryDao emailerHistoryDao) {
        this.emailerHistoryDao = emailerHistoryDao;
    }

    public EmailCampaignDao getEmailCampaignDao() {
        return this.emailCampaignDao;
    }

    public void setEmailCampaignDao(EmailCampaignDao emailCampaignDao) {
        this.emailCampaignDao = emailCampaignDao;
    }

    public LinkManager getLinkManager() {
        return this.linkManager;
    }

    public void setLinkManager(LinkManager linkManager) {
        this.linkManager = linkManager;
    }

    public NotifyMeDao getNotifyMeDao() {
        return this.notifyMeDao;
    }

    public void setNotifyMeDao(NotifyMeDao notifyMeDao) {
        this.notifyMeDao = notifyMeDao;
    }

    public UserService getUserService() {
        return this.userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public CategoryService getCategoryService() {
        return this.categoryService;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public BaseDao getBaseDao() {
        return this.baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

    public OrderLoggingService getOrderLoggingService() {
        return this.orderLoggingService;
    }

    public void setOrderLoggingService(OrderLoggingService orderLoggingService) {
        this.orderLoggingService = orderLoggingService;
    }
}
