package com.hk.impl.service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.hk.domain.user.Address;
import com.hk.service.impl.FreeMarkerService;

import freemarker.template.Template;

@Service
public class SMSService {

  private static Logger logger = LoggerFactory.getLogger(SMSService.class);

  final FreeMarkerService freeMarkerService = null;
  final String hkSMSURL = "";
  final String hkSMSUserName = "";
  final String hkSMSPassword = "";
  final String hkSMSSender = "";

  /*
  * public SMSService(FreeMarkerService freeMarkerService, //@Named(Keys.Env.hkSMSURL) String hkSMSURL,
  * //@Named(Keys.Env.hkSMSUserName) String hkSMSUserName, //@Named(Keys.Env.hkSMSPassword) String hkSMSPassword,
  * //@Named(Keys.Env.hkSMSSender) String hkSMSSender) { this.freeMarkerService = freeMarkerService; this.hkSMSURL =
  * hkSMSURL; this.hkSMSUserName = hkSMSUserName; this.hkSMSPassword = hkSMSPassword; this.hkSMSSender = hkSMSSender; }
  */

  public boolean sendSMS(String message, String mobile) {
    try {
      if (mobile != null && mobile.length() == 10) {

        logger.info("message---->" + message);

        String sender = "";

        mobile = URLEncoder.encode(mobile, "UTF-8");
        logger.info("mobile------>" + mobile);
        message = URLEncoder.encode(message, "UTF-8");

        sender = URLEncoder.encode(hkSMSSender, "UTF-8");
        logger.info("sender---->" + sender);

        String url_str = "";
        if (hkSMSURL.contains("mysmsmantra")) {
          url_str = hkSMSURL + "?username=" + hkSMSUserName + "&password=" + hkSMSPassword + "&mobileno=" + mobile + "&message=" + message + "&sendername=" + hkSMSSender;
        } else {
          url_str = hkSMSURL + "?username=" + hkSMSUserName + "&password=" + hkSMSPassword + "&to=" + mobile + "&text=" + message + "&from=" + hkSMSSender + "&udh=0";
        }
        logger.info("url string->" + url_str);

        URL url2 = new URL(url_str);
        HttpURLConnection connection = (HttpURLConnection) url2.openConnection();
        connection.setDoOutput(false);
        connection.setDoInput(true);
        connection.setConnectTimeout(10000);
        logger.info("Opened Con->" + connection);

        connection.connect();

        int code = connection.getResponseCode();
        logger.info("code ->" + code);
        if (code == HttpURLConnection.HTTP_OK) {
          logger.info("Disconnecting Connection.");
          connection.disconnect();
        }
      }
    } catch (MalformedURLException e) {
      logger.error("MalformedURLException in sendSMS", e);
      return false;
    } catch (IOException e) {
      logger.error("IOException in sendSMS", e);
      return false;
    } catch (Exception e) {
      logger.error("Catching Exception in sendSMS", e);
      return false;
    }
    return true;
  }

  public boolean sendSMSUsingTemplate(String mobile, String templatePath, Object templateValues) {
    try {
      if (mobile != null && mobile.length() == 10) {

        String message = "";
        Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(templatePath);
        FreeMarkerService.RenderOutput renderOutput = freeMarkerService.getRenderOutputForTemplate(freemarkerTemplate, templateValues);
        if (renderOutput == null) {
          logger.error("Error while rendering freemarker template : " + templatePath);
          return false;
        }
        message = renderOutput.getMessage();
        logger.info("message---->" + message);

        String sender = "";

        mobile = URLEncoder.encode(mobile, "UTF-8");
        logger.info("mobile------>" + mobile);
        message = URLEncoder.encode(message, "UTF-8");

        sender = URLEncoder.encode(hkSMSSender, "UTF-8");
        logger.info("sender---->" + sender);

        String url_str = "";
        if (hkSMSURL.contains("mysmsmantra")) {
          url_str = hkSMSURL + "?username=" + hkSMSUserName + "&password=" + hkSMSPassword + "&mobileno=" + mobile + "&message=" + message + "&sendername=" + hkSMSSender;
        } else {
          url_str = hkSMSURL + "?username=" + hkSMSUserName + "&password=" + hkSMSPassword + "&to=" + mobile + "&text=" + message + "&from=" + hkSMSSender + "&udh=0";
        }
        logger.info("url string->" + url_str);

        URL url2 = new URL(url_str);
        HttpURLConnection connection = (HttpURLConnection) url2.openConnection();
        connection.setDoOutput(false);
        connection.setDoInput(true);
        connection.setConnectTimeout(10000);
        logger.info("Opened Con->" + connection);

        connection.connect();

        int code = connection.getResponseCode();
        logger.info("code ->" + code);
        if (code == HttpURLConnection.HTTP_OK) {
          logger.info("Disconnecting Connection.");
          connection.disconnect();
        }
      }
    } catch (MalformedURLException e) {
      e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
      return false;
    } catch (IOException e) {
      e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
      return false;
    } catch (Exception e) {
      e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
      return false;
    }
    return true;
  }

  public boolean sendSMSUsingTemplate(String message, String templatePath, Object templateValues, Address address) {
    try {
      String mobile = address.getPhone();
      if (mobile != null && mobile.length() == 10) {

        Template freemarkerTemplate = freeMarkerService.getCampaignTemplate(templatePath);
        FreeMarkerService.RenderOutput renderOutput = freeMarkerService.getRenderOutputForTemplate(freemarkerTemplate, templateValues);
        if (renderOutput == null) {
          logger.error("Error while rendering freemarker template : " + templatePath);
          return false;
        }

        if (StringUtils.isBlank(message)) {
          message = renderOutput.getMessage();
        }
        logger.info("message---->" + message);

        String sender = "";

        mobile = URLEncoder.encode(mobile, "UTF-8");
        logger.info("mobile------>" + mobile);
        message = URLEncoder.encode(message, "UTF-8");

        sender = URLEncoder.encode(hkSMSSender, "UTF-8");
        logger.info("sender---->" + sender);

        String url_str = "";
        if (hkSMSURL.contains("mysmsmantra")) {
          url_str = hkSMSURL + "?username=" + hkSMSUserName + "&password=" + hkSMSPassword + "&mobileno=" + mobile + "&message=" + message + "&sendername=" + hkSMSSender;
        } else {
          url_str = hkSMSURL + "?username=" + hkSMSUserName + "&password=" + hkSMSPassword + "&to=" + mobile + "&text=" + message + "&from=" + hkSMSSender + "&udh=0";
        }
        logger.info("url string->" + url_str);

        URL url2 = new URL(url_str);
        HttpURLConnection connection = (HttpURLConnection) url2.openConnection();
        connection.setDoOutput(false);
        connection.setDoInput(true);
        connection.setConnectTimeout(10000);
        logger.info("Opened Con->" + connection);

        connection.connect();

        int code = connection.getResponseCode();
        logger.info("code ->" + code);
        if (code == HttpURLConnection.HTTP_OK) {
          logger.info("Disconnecting Connection.");
          connection.disconnect();
        }
      }
    } catch (MalformedURLException e) {
      logger.error("MalformedURLException in sendSMS", e);
      return false;
    } catch (IOException e) {
      logger.error("IOException in sendSMS", e);
      return false;
    } catch (Exception e) {
      logger.error("Catching Exception in sendSMS", e);
      return false;
    }
    return true;
  }
}
