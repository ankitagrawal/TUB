package com.hk.impl.service;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
//import com.hk.constants.core.EnumEmailType;
import com.hk.pact.service.EmailService;
/*
import com.hk.pact.dao.email.EmailerHistoryDao;
import com.hk.pact.dao.email.EmailRecepientDao;
import com.hk.pact.dao.BaseDao;
*/
import com.hk.service.impl.FreeMarkerService;
import com.hk.domain.email.EmailCampaign;
import freemarker.template.Template;

/**
 * Author: Kani Date: Dec 17, 2008
 */
@Service
public class EmailServiceImpl implements EmailService {
  private static Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);
  private final int EMAIL_THREAD_POOL_COUNT = 3;
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

  private ExecutorService emailExecutorService = Executors.newFixedThreadPool(EMAIL_THREAD_POOL_COUNT );
  /*@Autowired
  private EmailerHistoryDao emailerHistoryDao;
  @Autowired
  private BaseDao baseDao;
  @Autowired
  private EmailRecepientDao emailRecepientDao;
*/
  // TODO: rewrite
  /*
  * @Autowired public EmailServiceImpl(FreeMarkerService freeMarkerService,// @Named(Keys.Env.hkNoReplyEmail) String
  * noReplyEmail, //@Named(Keys.Env.hkNoReplyName) String noReplyName, //@Named(Keys.Env.hkContactEmail) String
  * contactEmail, //@Named(Keys.Env.hkContactName) String contactName) { this.freeMarkerService = freeMarkerService;
  * this.noReplyEmail = noReplyEmail; this.noReplyName = noReplyName; this.contactEmail = contactEmail;
  * this.contactName = contactName; }
  */

  public boolean sendHtmlEmailNoReply(String templatePath, Object templateValues, String toEmail, String toName) {
    return sendHtmlEmail(templatePath, templateValues, noReplyEmail, noReplyName, toEmail, toName, null, null, null);
  }
  public boolean sendHtmlEmail(String templatePath, Object templateValues, String toEmail, String toName) {
    return sendHtmlEmail(templatePath, templateValues, noReplyEmail, noReplyName, toEmail, toName, contactEmail, contactName, null);
  }
  public boolean sendHtmlEmail(String templatePath, Object templateValues, String toEmail, String toName, String replyToEmail) {
    return sendHtmlEmail(templatePath, templateValues, noReplyEmail, noReplyName, toEmail, toName, replyToEmail, null, null);
  }
  public boolean sendHtmlEmail(String templatePath, Object templateValues, String toEmail, String toName, String replyToEmail, Map<String, String> headerMap) {
    return sendHtmlEmail(templatePath, templateValues, noReplyEmail, noReplyName, toEmail, toName, replyToEmail, null, headerMap);
  }

  public Map<String,HtmlEmail> createHtmlEmail( String templatePath, Object templateValues, String toEmail, String toName, String replyToEmail,Map<String, String> headerMap, Template template){
    return  createHtmlEmail(templatePath, templateValues, noReplyEmail, noReplyName, toEmail, toName, null, null,headerMap, template);
  }

  public Map<String,HtmlEmail> createHtmlEmail(String templatePath, Object templateValues, String fromEmail, String fromName, String toEmail, String toName, String replyToEmail,
                                               String replyToName, Map<String, String> headerMap, Template template){
    Map<String, HtmlEmail> returnMap = new HashMap<String, HtmlEmail>();
    FreeMarkerService.RenderOutput renderOutput = freeMarkerService.processCampaignTemplate(template, templatePath, templateValues);
    HtmlEmail htmlEmail = null;
    try{
      if (renderOutput == null) {
        logger.error("Error while rendering freemarker template : " + templatePath + " in sendHtmlEmail");
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
    }catch(EmailException ex){
      logger.error("EmailException in sendHtmlEmail for template " + templatePath, ex);
      return null;
    }
    return returnMap;
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
  private boolean sendHtmlEmail(String templatePath, Object templateValues, String fromEmail, String fromName, String toEmail, String toName, String replyToEmail, String replyToName, Map<String, String> headerMap)
  {
    Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(templatePath);

    Map<String, HtmlEmail> htmlEmailMap = createHtmlEmail(templatePath, templateValues, fromEmail, fromName, toEmail, toName, replyToEmail,replyToName,headerMap,freemarkerTemplate);
    if (htmlEmailMap == null){
      return false;
    }

    for(Map.Entry<String, HtmlEmail> mapEntry : htmlEmailMap.entrySet()) {
      HtmlEmail htmlEmail = mapEntry.getValue();
      // send this email asynchrounously, we do not want to wait for this process
      sendEmail(htmlEmail);
    }
    return true;
  }

  public void sendBulkHtmlEmail(List<Map<String, HtmlEmail>> htmlEmails, EmailCampaign emailCampaign) {
    // send this email asynchrounously, we do not want to wait for this process
    emailExecutorService.execute(new SendBulkEmailAsyncThread(htmlEmails, emailCampaign));
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

  private class SendBulkEmailAsyncThread implements Runnable {
    private final List<Map<String, HtmlEmail>> emails;
    private final EmailCampaign emailCampaign;
    SendBulkEmailAsyncThread(List<Map<String, HtmlEmail>> email, EmailCampaign emailCampaign) {
      this.emails = email;
      this.emailCampaign = emailCampaign;
    }

    public void run() {
      
      for (Map<String, HtmlEmail> emailMap : emails){

        for(String emailId : emailMap.keySet()) {
          Email email = emailMap.get(emailId);
          logger.debug("sending mail : " + emailId);
          try {
            email.send();
          } catch (EmailException e) {
            logger.error("EmailException in SendEmailAsyncThread.run", e);
          }
        }
      }

    }
  }

}
