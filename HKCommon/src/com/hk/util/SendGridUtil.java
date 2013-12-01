package com.hk.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.akube.framework.gson.JsonUtils;
import com.google.gson.Gson;
import com.hk.constants.core.EnumEmailType;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.email.EmailCampaign;

public class SendGridUtil {

	@SuppressWarnings("unchecked")
    public static String getSendGridEmailNewsLetterHeaderJson(List<String> finalCategories, EmailCampaign emailCampaign) {
	  List<String> categories = new ArrayList<String>();
	  if (emailCampaign.getEmailType().getId().equals(EnumEmailType.CustomEmail.getId())) {
	    categories.add("mailmerge_" + emailCampaign.getName());
	  } else {
	    categories.add("hk_newsletters");
	    categories.add("campaign_" + emailCampaign.getName());
	  }
	  categories.addAll(finalCategories);

	  Map sendgridHeaderMap = new HashMap();
	  sendgridHeaderMap.put("category", categories);
      sendgridHeaderMap.put("hkCampaignId", emailCampaign.getId().toString());
	  Gson gson = JsonUtils.getGsonDefault();
	  return gson.toJson(sendgridHeaderMap);
	}


	@SuppressWarnings("unchecked")
	public static String getNotifyMeSendGridHeaderJson(Product product, ProductVariant productVariant, EmailCampaign emailCampaign) {
	    List<String> categories = new ArrayList<String>();
	    categories.add("NotifyUsers");
	    categories.add("campaign_" + emailCampaign.getName());
	    categories.add(product != null ? product.getName() : productVariant != null ? productVariant.getProduct().getName() : "");

	    Map sendGridHeaderMap = new HashMap();
	    sendGridHeaderMap.put("category", categories);
	    Gson gson = JsonUtils.getGsonDefault();
	    return gson.toJson(sendGridHeaderMap);
	}

}
