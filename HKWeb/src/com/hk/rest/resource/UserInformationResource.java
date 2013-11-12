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

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
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
    private List<String> categories;
    private String verified;
    private int minimum = 0;

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
                                      @FormParam("verified") String verified,
                                      @FormParam("categories") String categories,
                                      @FormParam("minimum") String minimum) {
        List<String> prodIds = getListFromString(productIds, ",");
        List<String> prodVarIds = getListFromString(productVariantIds, ",");
        List<String> allcities = getListFromString(cities, ",");
        List<String> allStates = getListFromString(states, ",");
        List<String> allZones = getListFromString(zones, ",");
        List<String> allCats = getListFromString(categories, ",");


        this.productIds = prodIds;
        this.productVariantIds = prodVarIds;
        this.cities = allcities;
        this.states = allStates;
        this.categories = allCats;
        this.verified = verified;
        try {
            this.zones = new ArrayList<Long>();
            for (String z : allZones) {
                Long l = Long.parseLong(z);
                this.zones.add(l);
            }
        } catch (Exception ignore) {
        }
        try {
            this.minimum = Integer.parseInt(minimum);
        } catch (Exception ignore) {
        }

        return getUsers();
    }

    private Response getUsers() {
        Response response = null;
        criteria = new UsersSearchCriteria();
        try {
            if (StringUtils.isNotBlank(verified)) {
                criteria.setVerified(verified);
            }
            if (HKCollectionUtils.isNotBlank(productIds)) {
                criteria.setProductIds(productIds);
            }
            if (HKCollectionUtils.isNotBlank(categories)) {
                criteria.setCategories(categories);
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

            List<User> users = null;
            List<Object[]> userInfo = null;

            if (minimum == 1) {
                userInfo = userSearchService.searchUserInfo(criteria);
            } else {
                users = userSearchService.searchUsers(criteria);
            }

            List<UserDto> userDtos = new ArrayList<UserDto>();
            if (userInfo != null) {
                for (int i = 0; i < userInfo.size(); i++) {
                    Object[] user = (Object[]) userInfo.get(i);
                    UserDto udto = new UserDto();
                    udto.login = (String) user[0];
                    udto.email = (String) user[1];
                    udto.name = (String) user[2];
                    udto.subscribedMask = (Integer) user[3];
                    udto.unsubscribeToken = (String) user[4];
                    userDtos.add(udto);
                }
            } else if (users != null) {
                for (int i = 0; i < users.size(); i++) {
                    User user = (User) users.get(i);
                    UserDto udto = new UserDto();
                    udto.name = user.getName();
                    udto.email = user.getEmail();
                    udto.login = user.getLogin();
                    udto.subscribedMask = user.getSubscribedMask();
                    udto.unsubscribeToken = user.getUnsubscribeToken();
                    userDtos.add(udto);
                }
            } else {
                throw new Exception("Null Data");
            }

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
        public String unsubscribeToken;
    }
}
