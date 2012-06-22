package com.hk.impl.service;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hk.constants.core.Keys;
import com.hk.pact.service.EmailService;
import com.hk.service.impl.FreeMarkerService;

/**
 * Author: Kani Date: Dec 17, 2008
 */
@Service
public class EmailServiceImpl implements EmailService {

    private static Logger logger = LoggerFactory.getLogger(EmailService.class);

    @Autowired
    FreeMarkerService     freeMarkerService;
    @Value("#{hkEnvProps['" + Keys.Env.hkNoReplyEmail + "']}")
    private String        noReplyEmail;
    @Value("#{hkEnvProps['" + Keys.Env.hkNoReplyName + "']}")
    private String        noReplyName;
    @Value("#{hkEnvProps['" + Keys.Env.hkContactEmail + "']}")
    private String        contactEmail;
    @Value("#{hkEnvProps['" + Keys.Env.hkContactName + "']}")
    private String        contactName;

    // TODO: rewrite
    /*
     * @Autowired public EmailServiceImpl(FreeMarkerService freeMarkerService,// @Named(Keys.Env.hkNoReplyEmail) String
     * noReplyEmail, //@Named(Keys.Env.hkNoReplyName) String noReplyName, //@Named(Keys.Env.hkContactEmail) String
     * contactEmail, //@Named(Keys.Env.hkContactName) String contactName) { this.freeMarkerService = freeMarkerService;
     * this.noReplyEmail = noReplyEmail; this.noReplyName = noReplyName; this.contactEmail = contactEmail;
     * this.contactName = contactName; }
     */

    public boolean sendHtmlEmailNoReply(String templatePath,Boolean isTemplateAvailable, Object templateValues, String toEmail, String toName) {
        return sendHtmlEmail(templatePath,isTemplateAvailable, templateValues, noReplyEmail, noReplyName, toEmail, toName, null, null, null);
    }

    public boolean sendHtmlEmail(String templatePath,Boolean isTemplateAvailable, Object templateValues, String toEmail, String toName) {
        return sendHtmlEmail(templatePath,isTemplateAvailable, templateValues, noReplyEmail, noReplyName, toEmail, toName, contactEmail, contactName, null);
    }

    public boolean sendHtmlEmail(String templatePath,Boolean isTemplateAvailable, Object templateValues, String toEmail, String toName, String replyToEmail) {
        return sendHtmlEmail(templatePath,isTemplateAvailable, templateValues, noReplyEmail, noReplyName, toEmail, toName, replyToEmail, null, null);
    }

    public boolean sendHtmlEmail(String templatePath,Boolean isTemplateAvailable, Object templateValues, String toEmail, String toName, String replyToEmail, Map<String, String> headerMap) {
        return sendHtmlEmail(templatePath,isTemplateAvailable, templateValues, noReplyEmail, noReplyName, toEmail, toName, replyToEmail, null, headerMap);
    }

    /**
     * This method builds the html email using the template and object values passed. It then calls
     * {@link #sendEmail(org.apache.commons.mail.Email)} to actually send the email
     * 
     * @param templatePath The path of the freemarker template, relative to the template directory configured
     * @param templateValues Usually a HashMap of values to be passed to the template. Can also be any arbitrary object
     * @param toEmail
     * @param toName
     * @return false means email sending failed
     */
    private boolean sendHtmlEmail(String templatePath,Boolean isTemplateAvailable, Object templateValues, String fromEmail, String fromName, String toEmail, String toName, String replyToEmail,
            String replyToName, Map<String, String> headerMap) {
        try {
            FreeMarkerService.RenderOutput renderOutput = freeMarkerService.getRenderOutputForTemplate(templatePath, templateValues);
            if (renderOutput == null) {
                logger.error("Error while rendering freemarker template : " + templatePath + " in sendHtmlEmail");
                return false;
            }
            HtmlEmail htmlEmail = new HtmlEmail();
            htmlEmail.addTo(toEmail, toName).setFrom(fromEmail, fromName).setSubject(renderOutput.getSubject()).setHostName("localhost");
            if (headerMap != null && !headerMap.isEmpty())
                htmlEmail.setHeaders(headerMap);
            if (!StringUtils.isBlank(replyToEmail))
                htmlEmail.addReplyTo(replyToEmail, replyToName);
            htmlEmail.setHtmlMsg(renderOutput.getMessage());

            logger.debug("Trying to send email with Subject: ");
            logger.debug(renderOutput.getSubject());
            logger.debug("Body:");
            logger.debug(renderOutput.getMessage());

            // send this email asynchrounously, we do not want to wait for this process
            sendEmail(htmlEmail);
        } catch (EmailException e) {
            logger.error("EmailException in sendHtmlEmail for template " + templatePath, e);
            return false;
        }
        return true;
    }

    /**
     * This method sends an email asynchrounously
     * 
     * @param email
     */
    protected void sendEmail(Email email) {
        SendEmailAsyncThread emailAsyncThread = new SendEmailAsyncThread(email);
        emailAsyncThread.start();
    }

    /**
     * This is an inner class which extends Thread and send an email in a different thread
     */
    private class SendEmailAsyncThread extends Thread {
        private final Email email;

        SendEmailAsyncThread(Email email) {
            this.email = email;
        }

        public void run() {
            try {
                email.send();
            } catch (EmailException e) {
                logger.error("EmailException in SendEmailAsyncThread.run", e);
            }
        }
    }

}
