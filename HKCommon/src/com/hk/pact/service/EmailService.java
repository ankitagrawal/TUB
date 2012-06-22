package com.hk.pact.service;

import java.util.Map;

public interface EmailService {

  public boolean sendHtmlEmailNoReply(String templatePath, Boolean isTemplateAvailable, Object templateValues, String toEmail, String toName);

  public boolean sendHtmlEmail(String templatePath, Boolean isTemplateAvailable, Object templateValues, String toEmail, String toName);

  public boolean sendHtmlEmail(String templatePath, Boolean isTemplateAvailable, Object templateValues, String toEmail, String toName, String replyToEmail);

  public boolean sendHtmlEmail(String templatePath, Boolean isTemplateAvailable, Object templateValues, String toEmail, String toName, String replyToEmail, Map<String, String> headerMap);

}
