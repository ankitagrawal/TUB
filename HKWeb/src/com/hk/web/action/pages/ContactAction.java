package com.hk.web.action.pages;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.action.UrlBinding;
import net.sourceforge.stripes.validation.EmailTypeConverter;
import net.sourceforge.stripes.validation.LocalizableError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationMethod;
import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaFactory;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.HealthkartConstants;
import com.hk.manager.EmailManager;

@UrlBinding("/contact")
@Component
public class ContactAction extends BaseAction {

    @Validate(required = true, maxlength = 60)
    private String       name;

    @Validate(required = true, maxlength = 60, converter = EmailTypeConverter.class)
    private String       email;

    @Validate(maxlength = 20)
    private String       phone;

    @Validate(required = true)
    private String       msgSubject;

    @Validate(required = true)
    private String       msgText;

    @Autowired
    private EmailManager emailManager;

    @DontValidate
    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/static/contactUs.jsp");
    }

    @ValidationMethod
    public void captchaValidation() {
        String challengeField = getContext().getRequest().getParameter("recaptcha_challenge_field");
        String responseField = getContext().getRequest().getParameter("recaptcha_response_field");
        if (StringUtils.isBlank(responseField))
            responseField = "null";

        ReCaptcha captcha = ReCaptchaFactory.newReCaptcha(HealthkartConstants.recaptchaPublicKey, HealthkartConstants.recaptchaPrivateKey, false);
        ReCaptchaResponse response = captcha.checkAnswer(getContext().getRequest().getRemoteAddr(), challengeField, responseField);
        if (!response.isValid()) {
            getContext().getValidationErrors().add("Captcha", new LocalizableError("/Signup.action.captcha.invalid"));
        }
    }

    public Resolution sendMessage() {
        emailManager.sendContactUsMail(name, email, phone, msgSubject, msgText);

        addRedirectAlertMessage(new SimpleMessage("Your message has been sent. We'll get back to you within one business day."));
        return new RedirectResolution(ContactAction.class);
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMsgSubject() {
        return msgSubject;
    }

    public void setMsgSubject(String msgSubject) {
        this.msgSubject = msgSubject;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }
    
    
}
