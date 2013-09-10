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

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
  @Path("/details/")
  @Produces("application/json")
  public String getNotifyUserDetails(){
    NotifyUserDetails notifyUserDetails = new NotifyUserDetails();
    User user = getUserService().getLoggedInUser();
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
          notifyUserDetails.setException(true);
          notifyUserDetails.addMessage("You Have Unsubscribed for all emails, Please go to 'My Account' to Subscribe again");
        }
      }else{
        notifyUserDetails.addMessage("No Logged-in User");
      }
    return new JSONResponseBuilder().addField("results",notifyUserDetails).build();
  }

  @POST
  @Path("/update")
  @Produces("application/json")
  public String updateNotifyForVariant(UpdateNotifyMeDetails updateNotifyMeDetails){
    User user = getUserService().findByLogin(updateNotifyMeDetails.getEmail());
    if (user != null) {
      if (!(user.isSubscribedForNotify())) {
        return new JSONResponseBuilder().addField("exception",true).addField("msgs","You have unsubscribed for all emails, Please go to 'My Account' to Subscribe again").build();
      }
    } else {
      EmailRecepient emailRecepient = getEmailRecepientDao().findByRecepient(updateNotifyMeDetails.getEmail());
      if (emailRecepient != null) {
        if (!(emailRecepient.isSubscribed())) {
          return new JSONResponseBuilder().addField("exception",true).addField("msgs","You Have Unsubscribed for all emails , Contact Customer Care") .build();
        }
      }
    }

    ProductVariant productVariant = getProductVariantService().getVariantById(updateNotifyMeDetails.getVariantId());
    if(productVariant!=null){
      List<NotifyMe> notifyMeList = getNotifyMeDao().getPendingNotifyMeList(updateNotifyMeDetails.getEmail(), productVariant);
      if (notifyMeList != null && notifyMeList.size() > 0) {
        return new JSONResponseBuilder().addField("exception",true).addField("msgs","We have received your request for this variant. We will get back to you very soon. Thanks for your visit.").build();
      }
    }else{
       return new JSONResponseBuilder().addField("exception",true).addField("msgs","Something went wrong").build();
    }
    try{
      NotifyMe notifyMe = new NotifyMe();
      notifyMe.setProductVariant(productVariant);
      notifyMe.setEmail(updateNotifyMeDetails.getEmail());
      notifyMe.setName(updateNotifyMeDetails.getName());
      notifyMe.setPhone(updateNotifyMeDetails.getContactNumber());
      getNotifyMeDao().save(notifyMe);
    } catch (Exception e){
      return new JSONResponseBuilder().addField("exception",true).addField("msgs","There came an Error, please try again").build();
    }
    return new JSONResponseBuilder().addField("exception",false).addField("msgs","Your request for this variant has already been received. We will get back to you very soon. Thanks for your visit.").build();
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
