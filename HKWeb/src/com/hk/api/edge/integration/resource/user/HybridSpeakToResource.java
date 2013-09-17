package com.hk.api.edge.integration.resource.user;

import com.hk.api.edge.integration.request.variant.HkrUpdateSpeakToUserDetails;
import com.hk.domain.coupon.DiscountCouponMailingList;
import com.hk.pact.dao.coupon.DiscountCouponMailingListDao;
import com.hk.util.json.JSONResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Path("/speakTo/")
@Component
public class HybridSpeakToResource {

  @Autowired
  private DiscountCouponMailingListDao discountCouponMailingListDao;

  @POST
  @Path("/update/")
  @Produces("application/json")
  public String updateSpeakToResponse(HkrUpdateSpeakToUserDetails hkrUpdateSpeakToUserDetails) {
    String couponCode = "n.a.";
    List<String> messages = new ArrayList<String>();
    JSONResponseBuilder jsonResponseBuilder = new JSONResponseBuilder();
    try {
      DiscountCouponMailingList dcml = new DiscountCouponMailingList();
      dcml.setName(hkrUpdateSpeakToUserDetails.getName());
      dcml.setMobile(hkrUpdateSpeakToUserDetails.getContactNumber());
      dcml.setEmail(hkrUpdateSpeakToUserDetails.getEmail());
      dcml.setCouponCode(couponCode);
      dcml.setSubscribeEmail(hkrUpdateSpeakToUserDetails.isSubscribe());
      dcml.setSubscribeMobile(hkrUpdateSpeakToUserDetails.isSubscribe());
      dcml.setRequestDate(new Date());
      getDiscountCouponMailingListDao().save(dcml);
    } catch (Exception e) {
      messages.add("There came an Error, please try again");
      return jsonResponseBuilder.addField("exception", false).addField("msgs", messages).addField("success", false).build();
    }
    messages.add("We have saved your details, we will contact you shortly");
    return jsonResponseBuilder.addField("exception", false).addField("msgs", messages).addField("success", true).build();
  }

  public DiscountCouponMailingListDao getDiscountCouponMailingListDao() {
    return discountCouponMailingListDao;
  }
}
