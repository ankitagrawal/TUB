package com.hk.pact.service;

import freemarker.template.Template;

import java.util.Map;
import java.util.List;

import org.apache.commons.mail.HtmlEmail;

public interface EmailService {

  public boolean sendHtmlEmailNoReply(String templatePath, Object templateValues, String toEmail, String toName);

  public boolean sendHtmlEmail(String templatePath, Object templateValues, String toEmail, String toName);

  public boolean sendHtmlEmail(String templatePath, Object templateValues, String toEmail, String toName, String replyToEmail);

  public boolean sendHtmlEmail(String templatePath, Object templateValues, String toEmail, String toName, String replyToEmail, Map<String, String> headerMap);

  /* public boolean sendHtmlEmail(String templatePath, Object templateValues, String fromEmail, String fromName, String toEmail, String toName, String replyToEmail,
String replyToName, Map<String, String> headerMap, Template template);*/

  public Map<String,HtmlEmail> createHtmlEmail(String templatePath, Object templateValues, String fromEmail, String fromName, String toEmail,
                                   Map<String, String> headerMap, Template template);

  public void sendBulkHtmlEmail(List<Map<String, HtmlEmail>> htmlEmails);

}
