package com.hk.api.edge.integration.resource.user;

import com.hk.api.edge.integration.request.variant.UpdateNotifyMeDetails;
import com.hk.api.edge.integration.response.user.NotifyUserDetails;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.email.EmailRecepient;
import com.hk.domain.marketing.NotifyMe;
import com.hk.domain.user.User;
import com.hk.pact.dao.email.EmailRecepientDao;
import com.hk.pact.dao.email.NotifyMeDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.util.json.JSONResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import java.util.ArrayList;
import java.util.List;

@Component
@Path("/notify/")
public class HybridNotifyMeResource {

  @Autowired
  private UserService userService;
  @Autowired
  private EmailRecepientDao emailRecepientDao;
  @Autowired
  private NotifyMeDao notifyMeDao;
  @Autowired
  private ProductVariantService productVariantService;

  @GET
  @Path("/details/{userId}")
  @Produces("application/json")
  public String getNotifyUserDetails(@PathParam("userId") Long userId){
    NotifyUserDetails notifyUserDetails = new NotifyUserDetails();
    User user = getUserService().getUserById(userId);
      if (user != null) {
        if(user.getEmail() != null){
          notifyUserDetails.setEmail(user.getEmail());
        }
        if (user.getName() != null && !user.getName().equals("Guest")) {
          notifyUserDetails.setName(user.getName());
        }
        if (user.getAddresses() != null && !user.getAddresses().isEmpty() && user.getAddresses().get(0).getPhone() != null) {
          notifyUserDetails.setContactNumber(user.getAddresses().get(0).getPhone());
        }
        notifyUserDetails.setSubscribedForNotify(user.isSubscribedForNotify());
        if(!user.isSubscribedForNotify()){
          notifyUserDetails.setException(false);
          notifyUserDetails.setAccountLink("http://healthkart.com/core/user/MyAccount.action?subscribeForEmails=");
          notifyUserDetails.addMessage("You Have Unsubscribed for all emails");
          notifyUserDetails.setSubscribedForNotify(false);
        }
      }else{
        notifyUserDetails.addMessage("User Doesn't Exist");
      }
    return new JSONResponseBuilder().addField("results",notifyUserDetails).build();
  }

  @POST
  @Path("/update")
  @Produces("application/json")
  public String updateNotifyForVariant(UpdateNotifyMeDetails updateNotifyMeDetails){
    List<String> messages = new ArrayList<String>();
    User user = getUserService().findByLogin(updateNotifyMeDetails.getEmail());
    if (user != null) {
      if (!(user.isSubscribedForNotify())) {
        messages.add("You have Unsubscribed for all emails, Please go to 'My Account' to Subscribe again");
        return new JSONResponseBuilder().addField("exception",false).addField("msgs",messages).addField("success",false).addField("accntLnk","http://healthkart.com/core/user/MyAccount.action?subscribeForEmails=").build();
      }
    } else {
      EmailRecepient emailRecepient = getEmailRecepientDao().findByRecepient(updateNotifyMeDetails.getEmail());
      if (emailRecepient != null) {
        if (!(emailRecepient.isSubscribed())) {
          messages.add("You Have Unsubscribed for all Emails , Contact Customer Care");
          return new JSONResponseBuilder().addField("exception",false).addField("success",false).addField("msgs",messages).build();
        }
      }
    }

    ProductVariant productVariant = getProductVariantService().getVariantById(updateNotifyMeDetails.getVariantId());
    if(productVariant!=null){
      if(!productVariant.isOutOfStock()){
        messages.add("This Product is In Stock, please refresh the page");
        return new JSONResponseBuilder().addField("exception",false).addField("success",false).addField("msgs",messages).build();
      } else{
        List<NotifyMe> notifyMeList = getNotifyMeDao().getPendingNotifyMeList(updateNotifyMeDetails.getEmail(), productVariant);
        if (notifyMeList != null && notifyMeList.size() > 0) {
          messages.add("We have already received your request for this variant. We will get back to you very soon. Thanks for your visit.");
          return new JSONResponseBuilder().addField("exception",false).addField("success",false).addField("msgs",messages).build();
        }
      }
    }else{
      messages.add("Something went wrong");
       return new JSONResponseBuilder().addField("exception",false).addField("success",false).addField("msgs",messages).build();
    }
    try{
      NotifyMe notifyMe = new NotifyMe();
      notifyMe.setProductVariant(productVariant);
      notifyMe.setEmail(updateNotifyMeDetails.getEmail());
      notifyMe.setName(updateNotifyMeDetails.getName());
      notifyMe.setPhone(updateNotifyMeDetails.getContactNumber());
      getNotifyMeDao().save(notifyMe);
    } catch (Exception e){
      messages.add("There came an Error, please try again");
      return new JSONResponseBuilder().addField("exception",true).addField("msgs",messages).build();
    }
    messages.add("Your request for this variant has already been received. We will get back to you very soon. Thanks for your visit.");
    return new JSONResponseBuilder().addField("exception",false).addField("success",true).addField("msgs",messages).build();
  }

  public UserService getUserService() {
    return userService;
  }

  public EmailRecepientDao getEmailRecepientDao() {
    return emailRecepientDao;
  }

  public NotifyMeDao getNotifyMeDao() {
    return notifyMeDao;
  }

  public ProductVariantService getProductVariantService() {
    return productVariantService;
  }
}
