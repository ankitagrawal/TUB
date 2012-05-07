package com.hk.pact.service;

import java.util.Map;

public interface EmailService {

    public boolean sendHtmlEmailNoReply(String templatePath, Object templateValues, String toEmail, String toName);

    public boolean sendHtmlEmail(String templatePath, Object templateValues, String toEmail, String toName);

    public boolean sendHtmlEmail(String templatePath, Object templateValues, String toEmail, String toName, String replyToEmail);

    public boolean sendHtmlEmail(String templatePath, Object templateValues, String toEmail, String toName, String replyToEmail, Map<String, String> headerMap);

}
