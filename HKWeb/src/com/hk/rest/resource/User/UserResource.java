package com.hk.rest.resource.User;

import com.hk.admin.pact.service.order.AdminOrderService;
import com.hk.constants.core.Keys;
import com.hk.domain.clm.KarmaProfile;
import com.hk.domain.user.User;
import com.hk.domain.user.UserDetail;
import com.hk.pact.service.UserService;
import com.hk.pact.service.clm.KarmaProfileService;
import com.hk.pact.service.user.UserDetailService;
import com.hk.rest.models.user.APIUser;
import com.hk.rest.models.user.APIUserDetail;
import net.sourceforge.stripes.util.CryptoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 10/11/12
 * Time: 7:55 PM
 * To change this template use File | Settings | File Templates.
 */
@Path("/user")
@Component
public class UserResource {

    private static Logger logger = LoggerFactory.getLogger(UserResource.class);
    @Value("#{hkEnvProps['" + Keys.Env.hkApiAccessKey + "']}")
    private String API_KEY;

    @Autowired
    UserDetailService userDetailService;

    @Autowired
    UserService userService;

    @Autowired
    KarmaProfileService karmaProfileService;

    @POST
    @Path ("email/{email}/phone/{phone}")
    @Produces("application/json")
    public Response updateUser(@PathParam("email") String email, @PathParam("phone") long phone) {
        email = email.toLowerCase().trim();
        User user = userService.findByLogin(email);
        Response response = null;
        try{
            if (user != null){
                response = Response.status(Response.Status.NOT_FOUND).build();
            }else{
                UserDetail userDetail = new UserDetail();
                userDetail.setPhone(phone);
                userDetail.setUser(user);
                userDetailService.save(userDetail);
                response = Response.status(Response.Status.OK).build();
            }
        }catch (Exception ex){
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            logger.error("Unable to save User Details ", ex);
        }
        return response;
    }

    @GET
    @Path ("/priority/{priority}")
    @Produces("application/json")
    public Response getUserListByPriority(@PathParam ("priority") long priority) {

        Response response = null;
        try{
            List<UserDetail> userDetailList = userDetailService.getByPriority((int)priority);
            final GenericEntity<List<UserDetail>> entity = new GenericEntity<List<UserDetail>>(userDetailList) { };
            response = Response.status(Response.Status.OK).entity(entity).build();
        }catch (Exception ex){
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            logger.error("Unable to get User Details ", ex);
        }
        return response;
    }

    @GET
    @Path ("/priority/phone/{phone}")
    @Produces("application/json")
    @Encoded
    public Response getUserDetails(@PathParam ("phone") long phone, @QueryParam("key")String key) {

        Response response = null;
        String decryptKey = CryptoUtil.decrypt(key);
        if ((decryptKey == null) || !decryptKey.trim().equals(API_KEY)){
             return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        try{
            List<UserDetail> userDetails = userDetailService.findByPhone(phone);
            ArrayList<APIUserDetail> userDetailList = new ArrayList<APIUserDetail>();
            for (UserDetail userDetail : userDetails){
                APIUserDetail apiUserDetail = new APIUserDetail();
                User user = userDetail.getUser();
                apiUserDetail.setId(user.getId());
                apiUserDetail.setPhone(userDetail.getPhone());
                KarmaProfile karmaProfile = karmaProfileService.findByUser(user);
                if (karmaProfile != null){
                    apiUserDetail.setPriority(karmaProfile.getKarmaPoints() >= 300 ? 1 : 0);
                }
                userDetailList.add(apiUserDetail);
            }
            if (userDetails != null && (userDetails.size() > 0)){
                final GenericEntity<List<APIUserDetail>> entity = new GenericEntity<List<APIUserDetail>>(userDetailList) { };
                response = Response.status(Response.Status.OK).entity(entity).build();
            }else{
                response = Response.status(Response.Status.NOT_FOUND).build();
            }
        }catch (Exception ex){
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            logger.error("Unable to get User Details ", ex);
        }
        return response;
    }
}
