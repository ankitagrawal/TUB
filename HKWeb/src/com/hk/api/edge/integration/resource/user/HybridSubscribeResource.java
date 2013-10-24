package com.hk.api.edge.integration.resource.user;

import com.hk.constants.user.EnumEmailSubscriptions;
import com.hk.domain.user.User;
import com.hk.edge.constants.DtoJsonConstants;
import com.hk.pact.service.UserService;
import com.hk.util.json.JSONResponseBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import java.util.ArrayList;
import java.util.List;

@Component
@Path("/subscribe/")
public class HybridSubscribeResource {

  @Autowired
  private UserService userService;

  @POST
  @Path("/{email}/")
  @Produces("application/json")
  public String subscribeUserByEmail(@PathParam(DtoJsonConstants.EMAIL) String email){
    List<String> messages = new ArrayList<String>();
     List<User> users = getUserService().findByEmail(email);
     if(users!=null && users.size()>0){
       for(User user : users){
          if(!user.getSubscribedMask().equals(EnumEmailSubscriptions.SUBSCRIBE_ALL)){
            user.setSubscribedMask(EnumEmailSubscriptions.SUBSCRIBE_ALL);
            getUserService().save(user);
          }
       }
       messages.add("You have successfully subscribed");
       return new JSONResponseBuilder().addField("exception",false).addField("msgs",messages).addField("success",true).build();
     }else{
       messages.add("We are Sorry, you don't have account with us");
       return new JSONResponseBuilder().addField("exception",false).addField("msgs",messages).addField("success",false).build();
     }
  }

  public UserService getUserService() {
    return userService;
  }
}
