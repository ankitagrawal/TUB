package com.hk.impl.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

import javax.annotation.PostConstruct;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.hk.constants.core.Keys;
import com.hk.service.impl.FreeMarkerService;

import freemarker.template.Template;

@Service
public class SMSService {

	private static Logger logger = LoggerFactory.getLogger(SMSService.class);

	@Autowired
	FreeMarkerService freeMarkerService;

	public static String smsUrl;
	public static String userName;
	public static String password;
	public static String senderId;
	String mtype = "N";
	String DR = "Y";

	public static boolean useSmsService;

	@Value ("#{hkEnvProps['" + Keys.Env.useSmsService + "']}")
	private String useSmsServiceString;

	@Value ("#{hkEnvProps['" + Keys.Env.hkSMSURL + "']}")
	private String hkSMSURL;

	@Value ("#{hkEnvProps['" + Keys.Env.hkSMSSender + "']}")
	private String hkSMSSender;

	@Value ("#{hkEnvProps['" + Keys.Env.hkSMSUserName + "']}")
	private String hkSMSUserName;

	@Value ("#{hkEnvProps['" + Keys.Env.hkSMSPassword + "']}")
	private String hkSMSPassword;

	@PostConstruct
	public void postConstruction() {
		useSmsService = StringUtils.isNotBlank(useSmsServiceString) && Boolean.parseBoolean(useSmsServiceString);
		smsUrl = hkSMSURL;
		userName = hkSMSUserName;
		password = hkSMSPassword;
		senderId = hkSMSSender;
	}

	public boolean sendSMS(String message, String mobile) {
		String postData = "";
		String retval = "";

		if (useSmsService) {
			try {
				if (mobile != null && mobile.length() >= 10) {
					String mobileWith10Digits = mobile.substring(mobile.length() - 10);
					if (StringUtils.isNumeric(mobileWith10Digits)) {
						mobileWith10Digits = URLEncoder.encode(mobileWith10Digits, "UTF-8");
						message = URLEncoder.encode(message, "UTF-8");

						postData += "User=" + URLEncoder.encode(userName, "UTF-8") + "&passwd=" + password + "&mobilenumber=" + mobileWith10Digits + "&message=" + message + "&sid=" + senderId + "&mtype=" + mtype + "&DR=" + DR;
						URL newUrl = new URL(smsUrl);
						logger.debug("post data to sms gateway: " + postData);

						HttpURLConnection urlconnection = (HttpURLConnection) newUrl.openConnection();
						urlconnection.setDoInput(true);
						urlconnection.setConnectTimeout(10000);
						urlconnection.setRequestMethod("POST");
						urlconnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
						urlconnection.setDoOutput(true);
						OutputStreamWriter out = new OutputStreamWriter(urlconnection.getOutputStream());
						out.write(postData);
						out.close();
						BufferedReader in = new BufferedReader(new InputStreamReader(urlconnection.getInputStream()));
						String decodedString;
						while ((decodedString = in.readLine()) != null) {
							retval += decodedString;
						}
						in.close();
						logger.debug("return value from sms: " + retval);
					} else {
						logger.debug("invalid mobileWith10Digits: " + mobileWith10Digits);
						return false;
					}
				} else {
					logger.debug("invalid mobile: " + mobile);
					return false;
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
		} else {
			return false;
		}
		return true;
	}

	public boolean sendSMSUsingTemplate(String mobile, String templatePath, Object templateValues) {
		String message = "";
		if (useSmsService) {
			Template smsTemplate = freeMarkerService.getCampaignTemplate(templatePath);
			if (smsTemplate != null) {
				FreeMarkerService.RenderOutput renderOutput = freeMarkerService.processSmsTemplate(smsTemplate, templateValues);

				if (renderOutput == null) {
					logger.error("Error while rendering freemarker template : " + templatePath);
					return false;
				}
				message = renderOutput.getMessage();
				return sendSMS(message, mobile);
			}
		}
		return false;
	}

}
