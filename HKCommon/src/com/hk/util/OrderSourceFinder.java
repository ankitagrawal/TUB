package com.hk.util;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hk.constants.HttpRequestAndSessionConstants;
import com.hk.constants.UtmMediumConstants;
import com.hk.constants.UtmSourceConstants;
import com.hk.constants.referrer.EnumPrimaryReferrerForOrder;
import com.hk.constants.referrer.EnumSecondaryReferrerForOrder;


public class OrderSourceFinder { 

  public static Map<String, Long> getOrderReferrer(HttpServletRequest httpRequest) {
    Long primaryReferrerId = null;
    Long secondaryReferrerId = null;

    Map<String, Long> orderReferres = new HashMap<String, Long>();

    String referrer = httpRequest.getHeader(HttpRequestAndSessionConstants.REFERER);
    String utm_source = httpRequest.getParameter(HttpRequestAndSessionConstants.UTM_SOURCE);
    String utm_medium = httpRequest.getParameter(HttpRequestAndSessionConstants.UTM_MEDIUM);

    if(utm_source == null){
      utm_source = "";
    }
    if(utm_medium == null){
      utm_medium = "";
    }

    if(referrer == null){
      referrer = "";
    }
    if(referrer.toLowerCase().contains(EnumPrimaryReferrerForOrder.GOOGLE.getName().toLowerCase())
        || httpRequest.getParameter(HttpRequestAndSessionConstants.GCLID) != null
        || httpRequest.getParameter(HttpRequestAndSessionConstants.ADWORD) != null){
      primaryReferrerId = EnumPrimaryReferrerForOrder.GOOGLE.getId();
      if(httpRequest.getParameter(HttpRequestAndSessionConstants.ADWORD) != null
      || !utm_source.equals("")
      || httpRequest.getParameter(HttpRequestAndSessionConstants.GCLID) != null){
        secondaryReferrerId = EnumSecondaryReferrerForOrder.GOOGLE_PAID.getId();
      }
    }

    else if(referrer.toLowerCase().contains(EnumPrimaryReferrerForOrder.FACEBOOK.getName().toLowerCase())
        || utm_source.toLowerCase().equals(UtmSourceConstants.FACEBOOK.toLowerCase()) ){
      primaryReferrerId = EnumPrimaryReferrerForOrder.FACEBOOK.getId();
      if(!utm_source.equals("")){
        secondaryReferrerId = EnumSecondaryReferrerForOrder.FACEBOOK_PAID.getId();
      }
      else if(!utm_medium.equals("")
        && utm_medium == UtmMediumConstants.APP){
        secondaryReferrerId = EnumSecondaryReferrerForOrder.FACEBOOK_APP.getId();
      }
      else{
        secondaryReferrerId = EnumSecondaryReferrerForOrder.FACEBOOK_UNPAID.getId();
      }
    }

    else if(httpRequest.getParameter("affid") != null || utm_medium.toLowerCase().equals(UtmMediumConstants.MICROSITES.toLowerCase())){
      primaryReferrerId = EnumPrimaryReferrerForOrder.AFFILIATE.getId();
      if(httpRequest.getParameter("affid") != null){
        secondaryReferrerId = EnumSecondaryReferrerForOrder.AFFILIATE_PAID.getId();
      }
      else{
        secondaryReferrerId = EnumSecondaryReferrerForOrder.AFFILIATE_UNPAID.getId();
      }
    }

    else if(!utm_source.equals("")
        && (utm_source.toLowerCase().contains("VZR".toLowerCase())
        || utm_source == UtmSourceConstants.VIZURY)
        ){
      primaryReferrerId = EnumPrimaryReferrerForOrder.VIZURY.getId();
    }

    else if(!utm_medium.equals("")
        && (utm_medium == UtmMediumConstants.HOMEBANNER
        || utm_medium == UtmMediumConstants.SITE
        || referrer == EnumPrimaryReferrerForOrder.HEALTHKART.getName())
        ){
      primaryReferrerId = EnumPrimaryReferrerForOrder.HEALTHKART.getId();
    }

    else if(utm_medium.toLowerCase().equals(UtmMediumConstants.EMAIL.toLowerCase())
        || utm_medium.toLowerCase().equals(UtmMediumConstants.EMAILER.toLowerCase())
        || utm_source.toLowerCase().equals(UtmSourceConstants.NOTIFYME.toLowerCase())
        || utm_source.toLowerCase().equals(UtmSourceConstants.ENEWSLETTER.toLowerCase())){
      primaryReferrerId = EnumPrimaryReferrerForOrder.EMAIL.getId();
    }

    else if(utm_source.toLowerCase().equals(UtmSourceConstants.KOMLI.toLowerCase())
        || utm_source.toLowerCase().equals(UtmSourceConstants.OHANA.toLowerCase())){
      primaryReferrerId = EnumPrimaryReferrerForOrder.OTHERS.getId();
    }

    orderReferres.put(HttpRequestAndSessionConstants.PRIMARY_REFERRER_ID, primaryReferrerId);
    orderReferres.put(HttpRequestAndSessionConstants.SECONDARY_REFERRER_ID, secondaryReferrerId);

    return orderReferres;
  }
}