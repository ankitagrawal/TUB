package com.hk.rest.resource;

import com.hk.admin.pact.service.email.AdminEmailService;
import com.hk.constants.user.EnumEmailSubscriptions;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.user.User;
import com.hk.manager.LinkManager;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 12/8/12
 * Time: 1:13 PM
 * To change this template use File | Settings | File Templates.
 */
@Path("/user/email")
@Component
public class UserEmailResource {

    private static Logger logger = LoggerFactory.getLogger(UserEmailResource.class);

    @Autowired
    AdminEmailService adminEmailService;
    @Autowired
    UserService userService;
    @Autowired
    LinkManager linkManager;

    @Autowired
    CategoryService categoryService;

    @GET
    @Path("/store/{storeId}/category/{category}/role/{role}")
    @Produces("application/json")
    public Response getEmailsByCategory(@PathParam("category")String category, @PathParam("storeId")int storeId,
                                        @PathParam("role")String role){

        Response response = null;
        try{
            List<User> emailRecepients = adminEmailService.getMailingListByCategory(category, storeId,role);
            List<UserDto> categoryUsers = new ArrayList<UserDto>();
            for (User user : emailRecepients){
                boolean subscribePromotionalOffers = EnumEmailSubscriptions.isSubscribed(EnumEmailSubscriptions.PROMOTIONAL_OFFERS, user.getSubscribedMask());
                if (subscribePromotionalOffers){
                    UserDto userDto = new UserDto();
                    userDto.email = user.getEmail();
                    userDto.login = user.getLogin();
                    userDto.name = user.getName();
                    userDto.unsubscribeToken = user.getUnsubscribeToken();
                    categoryUsers.add(userDto);
                }
            }
            final GenericEntity<List<UserDto>> entity = new GenericEntity<List<UserDto>>(categoryUsers){};
            response = Response.status(Response.Status.OK).entity(entity).build();
        }catch (Exception ex){
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            logger.error("Unable to get User Details ", ex);
        }
        return response;
    }

    @GET
    @Path("/categories")
    @Produces("application/json")
    public Response getCategories(){

        Response response = null;
        try{
            List<Category> primaryCategories = categoryService.getPrimaryCategories();
            List<String> categoryList = new ArrayList<String>();
            for (Category category : primaryCategories){
                categoryList.add(category.getName());
            }
            final GenericEntity<List<String>> entity = new GenericEntity<List<String>>(categoryList){};
            response = Response.status(Response.Status.OK).entity(entity).build();
        }catch (Exception ex){
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            logger.error("Unable to get Category Details ", ex);
        }
        return response;
    }

    @GET
    @Path("/store/{storeId}/email/{email}")
    @Produces("application/json")
    public Response getUserInfoByEmail(@PathParam("category")String category, @PathParam("storeId")int storeId, @PathParam("email")String email){

        Response response = null;
        try{
            User user = null;
            if (!userService.findByEmail(email).isEmpty()){
                user = userService.findByEmail(email).get(0);
            }
            if (user != null){
                UserDto userDto= new UserDto();
                userDto.email = user.getEmail();
                userDto.login = user.getLogin();
                userDto.name = user.getName();
                response = Response.status(Response.Status.OK).entity(userDto).build();
            }
        }catch (Exception ex){
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            logger.error("Unable to get User Details ", ex);
        }
        return response;
    }

    class UserDto{
        public String email;
        public String name;
        public String login;
        public String unsubscribeToken;
    }
}
