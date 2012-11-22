package com.hk.util;

import com.hk.constants.HttpRequestAndSessionConstants;
import com.hk.constants.UtmMediumConstants;
import com.hk.constants.UtmSourceConstants;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TrafficSourceFinder {

	public static String TRAFFIC_SRC = "traffic-src";
	public static String TRAFFIC_SRC_DETAILS = "traffic-src-details";
	public static String TRAFFIC_SRC_PAID = "traffic-src-paid";
	public static String CATEGORY = "category";
	public static String PRODUCT = "product";

	public static String GOOGLE = "google";
	public static String FACEBOOK = "facebook";
	public static String EMAIL_NEWSLETTER = "email-newsletter";
	public static String VIZURY = "vizury";
	public static String HEALTHKART = "healthkart";
	public static String RFERRAL = "referral";
	public static String AFFILIATE = "affiliate";
	public static String OTHERS = "others";

	public static Map<String, String> getTrafficDetails(HttpServletRequest httpRequest) {

		Logger logger = LoggerFactory.getLogger(TrafficSourceFinder.class);

		String trafficSrc = "";
		String trafficSrcDetails = "";
		String trafficSrcPaid = "false";

		Map<String, String> orderReferres = new HashMap<String, String>();

		String referrer = httpRequest.getHeader(HttpRequestAndSessionConstants.REFERER);
		String utm_source = httpRequest.getParameter(HttpRequestAndSessionConstants.UTM_SOURCE);
		if (StringUtils.isNotBlank(utm_source)) {
			trafficSrcDetails += "utm_source=" + utm_source + "||";
		}
		String utm_medium = httpRequest.getParameter(HttpRequestAndSessionConstants.UTM_MEDIUM);
		if (StringUtils.isNotBlank(utm_source)) {
			trafficSrcDetails += "utm_medium=" + utm_medium;
		}

		if (utm_source == null) {
			utm_source = "";
		}
		if (utm_medium == null) {
			utm_medium = "";
		}

		if (referrer == null) {
			referrer = "";
		}
		if (referrer.toLowerCase().contains(GOOGLE)
				|| httpRequest.getParameter(HttpRequestAndSessionConstants.GCLID) != null
				|| httpRequest.getParameter(HttpRequestAndSessionConstants.ADWORD) != null) {
			trafficSrc = GOOGLE;
			if (httpRequest.getParameter(HttpRequestAndSessionConstants.ADWORD) != null
					|| !utm_source.equals("")
					|| httpRequest.getParameter(HttpRequestAndSessionConstants.GCLID) != null) {
				trafficSrcPaid = "true";
			}
		} else if (referrer.toLowerCase().contains(FACEBOOK)
				|| utm_source.toLowerCase().equals(UtmSourceConstants.FACEBOOK.toLowerCase())) {
			trafficSrc = FACEBOOK; 			
			if (!utm_source.equals("")) {
				trafficSrcPaid = "true";
			}
		} else if (httpRequest.getParameter("affid") != null || utm_medium.toLowerCase().equals(UtmMediumConstants.AFFILIATES.toLowerCase())) {
			trafficSrc = AFFILIATE;
			if (httpRequest.getParameter("affid") != null) {
				trafficSrcPaid = "true";
			}
		} else if (!utm_source.equals("")
				&& (utm_source.toLowerCase().contains("VZR".toLowerCase())
				|| utm_source == UtmSourceConstants.VIZURY)
				) {
			trafficSrc = VIZURY;
			trafficSrcPaid = "true";
		} else if (utm_source.toLowerCase().equals(UtmSourceConstants.KOMLI.toLowerCase())) {
			trafficSrc = UtmSourceConstants.KOMLI.toLowerCase();
			trafficSrcPaid = "true";
		} else if (utm_source.toLowerCase().equals(UtmSourceConstants.OHANA.toLowerCase())) {
			trafficSrc = UtmSourceConstants.OHANA.toLowerCase();
			trafficSrcPaid = "true";
		}  else if (utm_medium.toLowerCase().equals(UtmMediumConstants.EMAIL.toLowerCase())
				|| utm_medium.toLowerCase().equals(UtmMediumConstants.EMAILER.toLowerCase())
				|| utm_source.toLowerCase().equals(UtmSourceConstants.NOTIFYME.toLowerCase())
				|| utm_source.toLowerCase().equals(UtmSourceConstants.ENEWSLETTER.toLowerCase())) {
			trafficSrc = EMAIL_NEWSLETTER;
		} else {
			trafficSrc = HEALTHKART;
		}


		if (httpRequest != null) {
			String requestPath = httpRequest.getRequestURI();
			logger.debug("requestPath = " + requestPath);
			if (StringUtils.isNotBlank(requestPath)) {
				requestPath = requestPath.replace(httpRequest.getContextPath(), "");
				logger.debug("requestPath = " + requestPath);
				//Now the URL will start from /
				String[] urlFragmentArray = requestPath.split("/");
				if (urlFragmentArray.length > 3 && requestPath.contains("/product/")) {
					String productId = urlFragmentArray[3];
					logger.debug("productId = " + productId);
					orderReferres.put(PRODUCT, productId);
				} else if(urlFragmentArray.length > 1){
					String category = urlFragmentArray[1];
					logger.debug("category = " + category);
					orderReferres.put(CATEGORY, category);
				}
			}
		}

		orderReferres.put(TRAFFIC_SRC, trafficSrc);
		orderReferres.put(TRAFFIC_SRC_DETAILS, trafficSrcDetails);
		orderReferres.put(TRAFFIC_SRC_PAID, trafficSrcPaid);

		return orderReferres;
	}
}