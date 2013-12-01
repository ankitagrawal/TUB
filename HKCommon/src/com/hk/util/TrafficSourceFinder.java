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
	public static String MICROSITES = "microsites";
	public static String OTHERS = "others";

	public static Map<String, String> getTrafficDetails(HttpServletRequest httpRequest) {

		Logger logger = LoggerFactory.getLogger(TrafficSourceFinder.class);

		String trafficSrc = "";
		String trafficSrcDetails = "";
		String trafficSrcPaid = "false";

		Map<String, String> orderReferres = new HashMap<String, String>();

		String referrer = httpRequest.getHeader(HttpRequestAndSessionConstants.REFERER);
		if (referrer == null) {
			referrer = "";
		}
		referrer = referrer.toLowerCase();

		String utm_source = httpRequest.getParameter(HttpRequestAndSessionConstants.UTM_SOURCE);
		if (StringUtils.isNotBlank(utm_source)) {
			utm_source = utm_source.toLowerCase();
			trafficSrcDetails += "utm_source=" + utm_source + "||";
		} else {
			utm_source = "";
		}
		String utm_medium = httpRequest.getParameter(HttpRequestAndSessionConstants.UTM_MEDIUM);
		if (StringUtils.isNotBlank(utm_medium)) {
			utm_medium = utm_medium.toLowerCase();
			trafficSrcDetails += "utm_medium=" + utm_medium + "||";
		} else {
			utm_medium = "";
		}
		String utm_campaign = httpRequest.getParameter(HttpRequestAndSessionConstants.UTM_CAMPAIGN);
		if (StringUtils.isNotBlank(utm_campaign)) {
			trafficSrcDetails += "utm_campaign=" + utm_campaign + "||";
		}
		String aff_id = httpRequest.getParameter(HttpRequestAndSessionConstants.AFF_ID);
		if (StringUtils.isNotBlank(aff_id)) {
			trafficSrcDetails += "aff_id=" + aff_id;
		} else {
			aff_id = "";
		}


		/** Sample URLs **/
		// PAID
		//http://www.healthkart.com/?utm_source=adwords&utm_medium=ad&utm_campaign=hk_brandname&gclid=CNWR4-Pm77MCFYh66wodhhYA1w
		//http://pediasure.in/pediasure_goo/?utm_source=Google&utm_medium=CPC&utm_campaign=Pediasure
		//http://www.youtube.com/watch?v=onBUw-LcVt4&feature=relmfu
		//http://googleads.g.doubleclick.net/mads/gma?u_audio=1&hl=en&preqs=1&app_name=1.0.iphone.com.mfeg.ntv5free&prl=3807&u_h=768&cap_bs=1&u_so=l&u_w=1024&ptime=0&js=afma-sdk-i-v4.1.1&ses
		//http://googleads.g.doubleclick.net/pagead/ads?client=ca-pub-0098238538928727&output=html&h=250&slotname=6694769909&w=300&lmt=1354143520&flash=11.5.31&url=http%3A%2F%2Fwww.160by2.co
		//http://economictimes.indiatimes.com/google_articleshow_top1.cms
		//http://www.googleadservices.com/pagead/aclk?sa=L&ai=CcRq5UWK2UMCEK9GZrAfpgIGIB43py9ACjaisgjTI_LrqAQgAEAFQtpbLpvr_____AWDl0uaDvA6gAeOE-eADyAEBqQJYFGYYakdTPqoEI0_QAMUcfrl0aq5ZPljzPWe
		//http://www.healthkart.com/product/vx-weight-lifting-straps-pair/SPT391?utm_source=facebook&utm_medium=cpc&utm_campaign=hk_fb_sports_b_vx&utm_content=Weight+Lifting+Straps+%282012-1

		//utm_source=adwords||utm_medium=ad||utm_campaign=hk_nutrition
		//utm_source="facebook"||utm_medium="newsfeed_ads"||
		//utm_source=facebook||utm_medium=buy_online_tab||utm_campaign=nicorette||

		// UNPAID
		//http://indiapulse.sulekha.com/forums/personal_baby-diapers-in-india-hyderabad-275571
		//http://www.healthkart.com/product/musclepharm-assault/NUT420?utm_source=enewsletter&utm_medium=email&utm_campaign=nov23_2012_weekend_offer-2012-11-23
		//http://www.google.co.in/url?sa=t&rct=j&q=ovulation%20kit%20in%20india&source=web&cd=1&cad=rja&sqi=2&ved=0CD0QFjAA&url=http%3A%2F%2Fwww.healthkart.com%2Fpersonal-care%2Fwomen%2Fovul

    //If referrer URL is secure and we land on insecure HK url - then the referral URL is blank.
    //Usecase if we search google in secure mode and click on HK unpaid link we would not get the referral and treat it as Direct.


		if (!utm_source.equals("")) {
			if (utm_source.equals(UtmSourceConstants.ADWORDS) || utm_source.equals(UtmSourceConstants.GOOGLE)) {
				trafficSrc = GOOGLE;
			} else if (utm_source.equals(UtmSourceConstants.FACEBOOK) || utm_source.equals(UtmSourceConstants.FB) || utm_source.contains(UtmSourceConstants.FACEBOOK)) {
				trafficSrc = FACEBOOK;
			} else if (utm_source.equals(UtmSourceConstants.ENEWSLETTER) || utm_source.equals(UtmSourceConstants.NOTIFYME)) {
				trafficSrc = EMAIL_NEWSLETTER;
			} else if (utm_source.contains(UtmSourceConstants.VIZURY)) {
				trafficSrc = VIZURY;
				trafficSrcPaid = "true";
			} else if (utm_source.toLowerCase().equals(UtmSourceConstants.KOMLI.toLowerCase())) {
				trafficSrc = UtmSourceConstants.KOMLI.toLowerCase();
				trafficSrcPaid = "true";
			} else if (utm_source.toLowerCase().equals(UtmSourceConstants.OHANA.toLowerCase())) {
				trafficSrc = UtmSourceConstants.OHANA.toLowerCase();
				trafficSrcPaid = "true";
			} else if (utm_medium.toLowerCase().equals(UtmMediumConstants.MICROSITES.toLowerCase())) {
				trafficSrc = MICROSITES;
			} else if (!aff_id.equals("")) {
				trafficSrc = AFFILIATE;
				trafficSrcPaid = "true";
			} else {
				trafficSrc = RFERRAL;
			}
			if (utm_medium.equals(UtmMediumConstants.AD) || utm_medium.equals(UtmMediumConstants.CPC)
					|| utm_medium.equals(UtmMediumConstants.BUY_ONLINE_TAB) || utm_medium.contains(UtmMediumConstants.NEWSFEED_ADS)
					|| utm_medium.equals(UtmMediumConstants.BANNER)) {
				trafficSrcPaid = "true";
			}
		} else if (!referrer.equals("")) {
			if (referrer.contains(GOOGLE)) {
				trafficSrc = GOOGLE;
				if(referrer.contains("googleads.g.doubleclick.net") || referrer.contains("googleadservices.com/pagead/aclk")){
					trafficSrcPaid = "true";
				}
			} else if (referrer.contains(FACEBOOK)) {
				trafficSrc = FACEBOOK;
			} else if (!aff_id.equals("")) {
				trafficSrc = AFFILIATE;
				trafficSrcPaid = "true";
			} else {
				trafficSrc = RFERRAL;
			}
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
				} else if (urlFragmentArray.length > 1) {
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