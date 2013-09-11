package com.hk.impl.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hk.constants.core.Keys;
import com.hk.domain.email.EmailCampaign;
import com.hk.pact.service.EmailService;
import com.hk.service.impl.FreeMarkerService;

import freemarker.template.Template;

/**
 * Author: Kani Date: Dec 17, 2008
 */
@Service
public class EmailServiceImpl implements EmailService {
    private static Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    // private final int EMAIL_THREAD_POOL_COUNT = 3;

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

    /* private ExecutorService emailExecutorService = Executors.newFixedThreadPool(EMAIL_THREAD_POOL_COUNT); */

    /*
     * @Autowired private EmailerHistoryDao emailerHistoryDao; @Autowired private BaseDao baseDao; @Autowired private
     * EmailRecepientDao emailRecepientDao;
     */
    // TODO: rewrite
    /*
     * @Autowired public EmailServiceImpl(FreeMarkerService freeMarkerService,// @Named(Keys.Env.hkNoReplyEmail) String
     * noReplyEmail, //@Named(Keys.Env.hkNoReplyName) String noReplyName, //@Named(Keys.Env.hkContactEmail) String
     * contactEmail, //@Named(Keys.Env.hkContactName) String contactName) { this.freeMarkerService = freeMarkerService;
     * this.noReplyEmail = noReplyEmail; this.noReplyName = noReplyName; this.contactEmail = contactEmail;
     * this.contactName = contactName; }
     */

    public boolean sendHtmlEmailNoReply(Template template, Object templateValues, String toEmail, String toName) {
        return sendHtmlEmail(template, templateValues, noReplyEmail, noReplyName, toEmail, toName, null, null, null);
    }

    public boolean sendHtmlEmail(Template template, Object templateValues, String toEmail, String toName) {
      boolean sent = false;
      try {
        sent = sendHtmlEmail(template, templateValues, noReplyEmail, noReplyName, toEmail, toName, contactEmail, contactName, null);

      } catch (Exception e) {
        logger.error("Exception while sending Email", e.getStackTrace());
      }
      return sent;
    }

    public boolean sendHtmlEmail(Template template, Object templateValues, String toEmail, String toName, String replyToEmail) {
        return sendHtmlEmail(template, templateValues, noReplyEmail, noReplyName, toEmail, toName, replyToEmail, null, null);
    }

    public boolean sendHtmlEmail(Template template, Object templateValues, String toEmail, String toName, String replyToEmail, Map<String, String> headerMap) {
        return sendHtmlEmail(template, templateValues, noReplyEmail, noReplyName, toEmail, toName, replyToEmail, null, headerMap);
    }

    public Map<String, HtmlEmail> createHtmlEmail(Template template, Object templateValues, String toEmail, String toName, String replyToEmail, Map<String, String> headerMap) {
        return createHtmlEmail(template, templateValues, noReplyEmail, noReplyName, toEmail, toName, null, null, headerMap);
    }

    public Map<String, HtmlEmail> createHtmlEmail(Template template, Object templateValues, String fromEmail, String fromName, String toEmail, String toName, String replyToEmail,
            String replyToName, Map<String, String> headerMap) {
        Map<String, HtmlEmail> returnMap = new HashMap<String, HtmlEmail>();
        FreeMarkerService.RenderOutput renderOutput = freeMarkerService.processCampaignTemplate(template, templateValues);
        HtmlEmail htmlEmail = null;
        try {
            if (renderOutput == null) {
                logger.error("Error while rendering freemarker template in sendHtmlEmail");
                return null;
            }
            htmlEmail = new HtmlEmail();
            htmlEmail.addTo(toEmail, toName).setFrom(fromEmail, fromName).setSubject(renderOutput.getSubject()).setHostName("localhost");
            if (headerMap != null && !headerMap.isEmpty())
                htmlEmail.setHeaders(headerMap);
            if (!StringUtils.isBlank(replyToEmail))
                htmlEmail.addReplyTo(replyToEmail, replyToName);
            htmlEmail.setHtmlMsg(renderOutput.getMessage());
            returnMap.put(toEmail, htmlEmail);

            logger.debug("Trying to send email with Subject: ");
            logger.debug(renderOutput.getSubject());
            logger.debug("Body:");
            logger.debug(renderOutput.getMessage());
        } catch (EmailException ex) {
            logger.error("EmailException in sendHtmlEmail for template "+ ex.getCause() + " message : " + ex.getMessage());
            return null;
        }
        return returnMap;
    }

    /**
     * This method builds the html email using the template and object values passed. It then calls
     * {@link #sendEmail(org.apache.commons.mail.Email)} to actually send the email
     * 
     * @param template The path of the freemarker template, relative to the template directory configured
     * @param templateValues Usually a HashMap of values to be passed to the template. Can also be any arbitrary object
     * @param toEmail
     * @param toName
     * @return false means email sending failed
     */
    private boolean sendHtmlEmail(Template template, Object templateValues, String fromEmail, String fromName, String toEmail, String toName, String replyToEmail,
            String replyToName, Map<String, String> headerMap) {
        // Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(template);

        Map<String, HtmlEmail> htmlEmailMap = createHtmlEmail(template, templateValues, fromEmail, fromName, toEmail, toName, replyToEmail, replyToName, headerMap);
        if (htmlEmailMap == null) {
            return false;
        }

        for (Map.Entry<String, HtmlEmail> mapEntry : htmlEmailMap.entrySet()) {
            HtmlEmail htmlEmail = mapEntry.getValue();
            // send this email asynchrounously, we do not want to wait for this process
            sendEmail(htmlEmail);
            logger.debug("-----ftl entry-------"+htmlEmail);
        }
        return true;
    }

    public void sendBulkHtmlEmail(List<Map<String, HtmlEmail>> htmlEmails, EmailCampaign emailCampaign) {
        // send this email asynchrounously, we do not want to wait for this process
        // Future<?> f = new FutureTask<Object>(new SendBulkEmailAsyncThread(htmlEmails, emailCampaign), null);
        sendBulkEmails(htmlEmails);
        // Future<?> f = emailExecutorService.submit(new SendBulkEmailAsyncThread(htmlEmails, emailCampaign));
    }

    public boolean sendHtmlEmail(String subject, String message, String toEmail, String toName, String attachementPath) {
        HtmlEmail htmlEmail = null;
        boolean isSent = true;
        try {
            htmlEmail = new HtmlEmail();
            htmlEmail.addTo(toEmail, toName).setFrom(noReplyEmail, noReplyName).setSubject(subject).setHostName("localhost");
            if (StringUtils.isNotBlank(attachementPath)) {
                EmailAttachment attachment = new EmailAttachment();
                attachment.setPath(attachementPath);
                attachment.setDisposition(EmailAttachment.ATTACHMENT);
                attachment.setDescription(subject);
                htmlEmail.attach(attachment);
            }
            htmlEmail.setHtmlMsg(message);
            htmlEmail.send();

        } catch (EmailException ex) {
            logger.error("EmailException in sendHtmlEmail for template ", ex);
            isSent = false;
        }
        return isSent;
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

    @SuppressWarnings("unused")
    private class SendBulkEmailAsyncThread implements Runnable {
        private final List<Map<String, HtmlEmail>> emails;
        @SuppressWarnings("unused")
        private final EmailCampaign                emailCampaign;

        SendBulkEmailAsyncThread(List<Map<String, HtmlEmail>> email, EmailCampaign emailCampaign) {
            this.emails = email;
            this.emailCampaign = emailCampaign;
        }

        public void run() {
            sendBulkEmails(emails);
        }
    }

    private void sendBulkEmails(List<Map<String, HtmlEmail>> emails) {
        try {
            for (Map<String, HtmlEmail> emailMap : emails) {
                for (String emailId : emailMap.keySet()) {
                    try {
                        Email email = emailMap.get(emailId);
                        logger.debug("sending mail : " + emailId);
                        email.send();
                    } catch (Exception e) {
                        String errorMsg = "Unable to send email to user";
                        if (StringUtils.isNotBlank(emailId)) {
                            errorMsg = errorMsg + " " + emailId;
                        }
                        logger.error(errorMsg, e);
                    }
                }
            }
        } catch (Exception ex) {
            logger.error("Error while sending bulk emails", ex);
        }
    }
    
    public boolean sendEmail(Template template, Object templateValues, String fromEmail, String fromName, String toEmail, String toName, String replyToEmail,
            String replyToName, Set<String> emailSet, Map<String, String> headerMap, String attachPdf, String attachXl){
        // Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(template);
    	boolean isSent = true;
        Map<String, MultiPartEmail> htmlEmailMap = createMultiPartHtmlEmail(template, templateValues, fromEmail, fromName, toEmail, toName, replyToEmail, replyToName, headerMap);
        if (htmlEmailMap == null) {
            return false;
        }

        for (Map.Entry<String, MultiPartEmail> mapEntry : htmlEmailMap.entrySet()) {
        	MultiPartEmail htmlEmail = mapEntry.getValue();
            try {
            	if(emailSet!=null && emailSet.size()>0){
	            	for(String email:emailSet){
	            		htmlEmail.addCc(email);
	            	}
            	}
				
				if (StringUtils.isNotBlank(attachPdf)) {
	                EmailAttachment attachment1 = new EmailAttachment();
	                attachment1.setPath(attachPdf);
	                attachment1.setDisposition(EmailAttachment.ATTACHMENT);
	                htmlEmail.attach(attachment1);
	            }
				
				if (StringUtils.isNotBlank(attachXl)) {
	                EmailAttachment attachment2 = new EmailAttachment();
	                attachment2.setPath(attachXl);
	                attachment2.setDisposition(EmailAttachment.ATTACHMENT);
	                htmlEmail.attach(attachment2);
	            }
				
			} catch (EmailException e) {
				logger.error("EmailException in adding CC/attachments ");
	            isSent = false;
			}
            
            sendEmail(htmlEmail);
        }
        return isSent;
    }
    
    public Map<String, MultiPartEmail> createMultiPartHtmlEmail(Template template, Object templateValues, String fromEmail, String fromName, String toEmail, String toName, String replyToEmail,
            String replyToName, Map<String, String> headerMap) {
        Map<String, MultiPartEmail> returnMap = new HashMap<String, MultiPartEmail>();
        FreeMarkerService.RenderOutput renderOutput = freeMarkerService.processCampaignTemplate(template, templateValues);
        MultiPartEmail htmlEmail = null;
        try {
            if (renderOutput == null) {
                logger.error("Error while rendering freemarker template in sendHtmlEmail");
                return null;
            }
            htmlEmail = new MultiPartEmail();
            htmlEmail.addTo(toEmail, toName).setFrom(fromEmail, fromName).setSubject(renderOutput.getSubject()).setHostName("localhost");
            if (headerMap != null && !headerMap.isEmpty())
                htmlEmail.setHeaders(headerMap);
            if (!StringUtils.isBlank(replyToEmail))
                htmlEmail.addReplyTo(replyToEmail, replyToName);
            
            final MimeBodyPart htmlPart = new MimeBodyPart();
            htmlPart.setContent(renderOutput.getMessage(), "text/html");
            // Create the Multipart.  Add BodyParts to it.
            final Multipart mp = new MimeMultipart("alternative");
            mp.addBodyPart(htmlPart);
            // Set Multipart as the message's content
            final MimeMultipart mmp = new MimeMultipart();
            mmp.addBodyPart(htmlPart);
            
            htmlEmail.addPart(mmp);
            returnMap.put(toEmail, htmlEmail);

            logger.debug("Trying to send email with Subject: ");
            logger.debug(renderOutput.getSubject());
            logger.debug("Body:");
            logger.debug(renderOutput.getMessage());
        } catch (EmailException ex) {
            logger.error("EmailException in sendHtmlEmail for template "+ ex.getCause() + " message : " + ex.getMessage());
            return null;
        } catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return returnMap;
    }
}
