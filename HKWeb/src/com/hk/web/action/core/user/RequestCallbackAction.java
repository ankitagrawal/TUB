package com.hk.web.action.core.user;

import java.util.Date;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.action.RedirectResolution;
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
import com.hk.domain.catalog.category.Category;
import com.hk.domain.coupon.DiscountCouponMailingList;
import com.hk.domain.user.User;
import com.hk.pact.dao.coupon.DiscountCouponMailingListDao;
import com.hk.web.HealthkartResponse;
import com.hk.manager.EmailManager;

@Component
public class RequestCallbackAction extends BaseAction implements ValidationErrorHandler {

    private static Logger        logger = LoggerFactory.getLogger(RequestCallbackAction.class);

    @Validate(required = true)
    private String               name;

    private String               mobile;
    @Validate(converter = EmailTypeConverter.class)
    private String               email;
    boolean                      subscribe;

    private String               srcUrl;
    private Category             topLevelCategory;
    @Autowired
    DiscountCouponMailingListDao discountCouponMailingListDao;

  @Autowired
  EmailManager emailManager;

    public Resolution handleValidationErrors(ValidationErrors validationErrors) throws Exception {
        return new JsonResolution(validationErrors, getContext().getLocale());
    }

    @DontValidate
    @DefaultHandler
    public Resolution pre() {

        User user = null;
        if (getPrincipal() != null) {
            user = getUserService().getUserById(getPrincipal().getId());
            if (user != null) {
                name = user.getName();
                email = user.getEmail();
            }
        }

        return new ForwardResolution("/pages/modal/requestCallback.jsp");
    }

    @ValidationMethod(on = "getContact")
    public void getContactValidation() {
        if(StringUtils.isBlank(name) || StringUtils.equals(name, "Guest")){
            getContext().getValidationErrors().add("e1", new SimpleError("Please enter a proper name."));  
        }
        if (StringUtils.isBlank(mobile) && StringUtils.isBlank(email)) {
            getContext().getValidationErrors().add("e1", new SimpleError("Please enter either mobile number or email id."));
        } else if (StringUtils.isNotBlank(mobile)) {
            if (mobile.length() < 10) {
                getContext().getValidationErrors().add("e1", new SimpleError("Invalid mobile number."));
            } else {
                try {
                    Long mobInt = Long.valueOf(mobile);
                } catch (NumberFormatException e) {
                    getContext().getValidationErrors().add("e1", new SimpleError("Please enter mobile number without any special characters or space."));
                }
            }
        }
    }

    @JsonHandler
    public Resolution getContact() {

        logger.debug("srcUrl: " + srcUrl);

        String couponCode = "n.a.";

        DiscountCouponMailingList dcml = new DiscountCouponMailingList();
        dcml.setName(name);
        dcml.setMobile(mobile);
        dcml.setEmail(email);
        dcml.setSrcUrl(srcUrl);
        dcml.setCategory(topLevelCategory.getName());
        dcml.setCouponCode(couponCode);
        dcml.setSubscribeEmail(subscribe);
        dcml.setSubscribeMobile(subscribe);
        dcml.setRequestDate(new Date());
        dcml = (DiscountCouponMailingList)discountCouponMailingListDao.save(dcml);
        User loggedOnUser = getUserService().getLoggedInUser();
      
        emailManager.sendCallbackRequestEmail(loggedOnUser, dcml);

        return new JsonResolution(new HealthkartResponse(HealthkartResponse.STATUS_OK, "Your information has been received, we will get in touch with you shortly."));
    }

    public Resolution requestConsultation() {

        logger.debug("srcUrl: " + srcUrl);

        String couponCode = "n.a.";
        DiscountCouponMailingList dcml = new DiscountCouponMailingList();
        dcml.setName(name);
        dcml.setMobile(mobile);
        dcml.setEmail(email);
        dcml.setSrcUrl(srcUrl);
        dcml.setCategory(topLevelCategory.getName());
        dcml.setCouponCode(couponCode);
        dcml.setSubscribeEmail(true);
        dcml.setSubscribeMobile(true);
        dcml.setRequestDate(new Date());
        discountCouponMailingListDao.save(dcml);

        return new RedirectResolution("/" + topLevelCategory);
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
