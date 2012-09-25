package com.hk.web.action.core.discount;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.EmailTypeConverter;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationErrorHandler;
import net.sourceforge.stripes.validation.ValidationErrors;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.manager.SMSManager;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.coupon.DiscountCouponMailingList;
import com.hk.domain.user.User;
import com.hk.manager.EmailManager;
import com.hk.pact.dao.coupon.DiscountCouponMailingListDao;
import com.hk.web.HealthkartResponse;

@Component
public class SendDiscountCouponAction extends BaseAction implements ValidationErrorHandler {

    private static Logger        logger               = LoggerFactory.getLogger(SendDiscountCouponAction.class);

    @Validate(required = true)
    private String               name;

    private String               mobile;
    @Validate(converter = EmailTypeConverter.class)
    private String               email;
    boolean                      subscribe;

    private String               srcUrl;
    private Category             topLevelCategory;
    @Autowired
    SMSManager                   smsManager;
    @Autowired
    EmailManager                 emailManager;
    @Autowired
    DiscountCouponMailingListDao discountCouponMailingListDao;


    private static String        couponCode_beauty    = "BEAUTYTEN";
    private static String        couponCode_nutrition = "NATNUTWK";

    public Resolution handleValidationErrors(ValidationErrors validationErrors) throws Exception {
        return new JsonResolution(validationErrors, getContext().getLocale());
    }

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        User user = null;
        if (getPrincipal() != null) {
            user = getUserService().getUserById(getPrincipal().getId());
            name = user.getName();
            email = user.getEmail();
        }

        return new ForwardResolution("/pages/modal/getDiscountCoupon.jsp");
    }

    @ValidationMethod(on = "getDiscountCoupon")
    public void getDiscountCouponValidation() {
        logger.debug("mobile: " + mobile);
        if (StringUtils.isBlank(mobile) && StringUtils.isBlank(email)) {
            getContext().getValidationErrors().add("e1", new SimpleError("Please enter either mobile number or email id."));
        } else if (StringUtils.isNotBlank(mobile)) {
            if (mobile.length() < 10) {
                getContext().getValidationErrors().add("e1", new SimpleError("Invalid mobile number."));
            } else {
                try {
                    @SuppressWarnings("unused")
                    Long mobInt = Long.valueOf(mobile);
                } catch (NumberFormatException e) {
                    getContext().getValidationErrors().add("e1", new SimpleError("Please enter mobile number without any special characters or space."));
                }
            }
        }
    }

    @JsonHandler
    public Resolution getDiscountCoupon() {
        logger.debug("srcUrl: " + srcUrl);

        String couponCode = couponCode_beauty;
        if (topLevelCategory != null && "nutrition".equals(topLevelCategory.getName())) {
            couponCode = couponCode_nutrition;
        }

        // first check if the discount coupon code has already been sent. if yes, then do not send again.

        if (StringUtils.isNotBlank(mobile)) {
            DiscountCouponMailingList discountCouponMailingList = discountCouponMailingListDao.findByMobile(mobile);
            if (discountCouponMailingList != null) {
                // already sent
                return new JsonResolution(new HealthkartResponse(HealthkartResponse.STATUS_OK,
                        "Your discount coupon has already been sent. Please email our customer support on info@healthkart.com if you are not receiving the SMS."));
            } else {
                smsManager.sendDiscountCoupon(name, mobile, couponCode);
            }
        }
        if (StringUtils.isNotBlank(email)) {
            DiscountCouponMailingList discountCouponMailingList = discountCouponMailingListDao.findByEmail(email);
            if (discountCouponMailingList != null) {
                // already sent
                return new JsonResolution(
                        new HealthkartResponse(
                                HealthkartResponse.STATUS_OK,
                                "We have resent your discount coupon. If you are not receiving the email, please check your junk email folder. Please email our customer support on info@healthkart.com if you are still not able to get the coupon code."));
            } else {
                emailManager.sendDiscountCouponEmail(name, email, couponCode);
            }
        }

        DiscountCouponMailingList dcml = new DiscountCouponMailingList();
        dcml.setName(name);
        dcml.setMobile(mobile);
        dcml.setEmail(email);
        dcml.setSrcUrl(srcUrl);
        dcml.setCategory(topLevelCategory.getName());
        dcml.setCouponCode(couponCode);
        dcml.setSubscribeEmail(subscribe);
        dcml.setSubscribeMobile(subscribe);
        discountCouponMailingListDao.save(dcml);

        return new JsonResolution(new HealthkartResponse(HealthkartResponse.STATUS_OK, "Your discount coupon has been sent."));
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSrcUrl() {
        return srcUrl;
    }

    public void setSrcUrl(String srcUrl) {
        this.srcUrl = srcUrl;
    }

    public Category getTopLevelCategory() {
        return topLevelCategory;
    }

    public void setTopLevelCategory(Category topLevelCategory) {
        this.topLevelCategory = topLevelCategory;
    }

    public boolean isSubscribe() {
        return subscribe;
    }

    public void setSubscribe(boolean subscribe) {
        this.subscribe = subscribe;
    }
}