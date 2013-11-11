package com.hk.rest.resource;

import com.hk.core.search.UsersSearchCriteria;
import com.hk.domain.user.User;
import com.hk.pact.service.UserSearchService;
import com.hk.util.HKCollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 11/5/13
 * Time: 11:09 AM
 * To change this template use File | Settings | File Templates.
 */

@Path("/search/users")
@Component
public class UserInformationResource {
    private static Logger logger = LoggerFactory.getLogger(UserEmailResource.class);
    private UsersSearchCriteria criteria;

    // variables that can be set
    private List<Long> zones;
    private List<String> cities;
    private List<String> states;
    private List<String> productIds;
    private List<String> productVariantIds;
    private String verified;

    @Autowired
    UserSearchService userSearchService;


    @POST
    @Path("/all")
    @Produces("application/json")
    public Response getUsersByProduct(@FormParam("productIds") String productIds,
                                      @FormParam("productVariantIds") String productVariantIds,
                                      @FormParam("cities") String cities,
                                      @FormParam("zones") String zones,
                                      @FormParam("states") String states,
                                      @FormParam("verified") String verified) {
        List<String> prodIds = getListFromString(productIds, ",");
        List<String> prodVarIds = getListFromString(productVariantIds, ",");
        List<String> allcities = getListFromString(cities, ",");
        List<String> allStates = getListFromString(states, ",");
        List<String> allZones = getListFromString(zones, ",");


        this.productIds = prodIds;
        this.productVariantIds = prodVarIds;
        this.cities = allcities;
        this.states = allStates;
        this.verified = verified;
        try {
            this.zones = new ArrayList<Long>();
            for (String z : allZones) {
                Long l = Long.parseLong(z);
                this.zones.add(l);
            }
        } catch (Exception ignore) {
        }
        return getUsers();
    }

    /* @POST
     @Path("/product")
     @Produces("application/json")
     public Response getUsersByProduct(@FormParam("productId") String productId) {
         List<String> prodIds = getListFromString(productId, ",");
         this.productIds = prodIds;
         return getUsers();
     }

     @POST
     @Path("/productVariant")
     @Produces("application/json")
     public Response getUsersByProductVariants(@FormParam("productVariantId") String productVariantId) {
         List<String> prodVarIds = getListFromString(productVariantId, ",");
         this.productVariantIds = prodVarIds;
         return getUsers();
     }

     @POST
     @Path("/city")
     @Produces("application/json")
     public Response getUsersByCity(@FormParam("city") String city) {
         List<String> allcities = getListFromString(city, ",");
         this.cities = allcities;
         return getUsers();
     }

     @POST
     @Path("/zone")
     @Produces("application/json")
     public Response getUsersByZone(@FormParam("zone") String zone) {
         try {
             zones = new ArrayList<Long>();
             List<String> allZones = getListFromString(zone, ",");
             for (String z : allZones) {
                 Long l = Long.parseLong(z);
                 zones.add(l);
             }
         } catch (Exception ignore) {
         }
         return getUsers();
     }

     @POST
     @Path("/state")
     @Produces("application/json")
     public Response getUsersByState(@FormParam("state") String state) {
         List<String> allStates = getListFromString(state, ",");
         this.states = allStates;
         return getUsers();
     }

     @GET
     @Path("/verified")
     @Produces("application/json")
     public Response getUsersByVerified() {
         this.verified = "true";
         return getUsers();
     }
 */
    private Response getUsers() {
        Response response = null;
        List<User> users = null;
        criteria = new UsersSearchCriteria();
        try {
            if (StringUtils.isNotBlank(verified)) {
                criteria.setVerified(verified);
            }
            if (HKCollectionUtils.isNotBlank(productIds)) {
                criteria.setProductIds(productIds);
            }
            if (HKCollectionUtils.isNotBlank(productVariantIds)) {
                criteria.setProductVariantIds(productVariantIds);
            }
            if (HKCollectionUtils.isNotBlank(zones)) {
                criteria.setZones(zones);
            }
            if (HKCollectionUtils.isNotBlank(cities)) {
                criteria.setCities(cities);
            }
            if (HKCollectionUtils.isNotBlank(states)) {
                criteria.setStates(states);
            }

//            users = userSearchService.searchUsers(criteria);
            List<String> ems = userSearchService.searchUserEmails(criteria);
            List<UserDto> userDtos = new LinkedList<UserDto>();
            for (String s : ems) {
                UserDto udto = new UserDto();
                udto.name = "";
                udto.email = "";
                udto.login = s;
                udto.subscribedMask = 0;
                userDtos.add(udto);
            }
//            for (User u : users) {
//                UserDto udto = new UserDto();
//                udto.name = u.getName();
//                udto.email = u.getEmail();
//                udto.login = u.getLogin();
//                udto.subscribedMask = u.getSubscribedMask();
//
//                userDtos.add(udto);
//            }

            final GenericEntity<List<UserDto>> genericEntity = new GenericEntity<List<UserDto>>(userDtos) {
            };
            response = Response.status(Response.Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            logger.error("Unable to get User Details ", ex);
        }

        return response;
    }

    private List<String> getListFromString(String s, String delimiter) {
        if (s == null) return null;
        delimiter = (delimiter == null || delimiter.isEmpty()) ? "," : delimiter;
        List<String> retList = new LinkedList<String>();
        String[] ss = s.split(delimiter);
        for (String str : ss) {
            if (str != null && !str.isEmpty()) {
                str = str.trim();
                retList.add(str);
            }
        }
        return retList;
    }

    class UserDto {
        public String email;
        public String name;
        public String login;
        public Integer subscribedMask;
    }
}
