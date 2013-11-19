package com.hk.rest.resource;

import com.akube.framework.util.StringUtils;
import com.hk.core.search.UsersSearchCriteria;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.user.User;
import com.hk.pact.service.UserSearchService;
import com.hk.pact.service.catalog.CategoryService;
import com.hk.util.HKCollectionUtils;
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
import java.util.Collection;
import java.util.List;
import java.util.Set;

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
    private List<String> emails;
    private List<String> states;
    private List<String> productIds;
    private List<String> productVariantIds;
    private List<Long> storeIds;
    private Boolean verified;
    private Integer userOrderCount;
    private String equality;
    private int minimum = 1;

    @Autowired
    UserSearchService userSearchService;
    @Autowired
    CategoryService categoryService;

    @POST
    @Path("/all")
    @Produces("application/json")
    public Response getUsersByProduct(@FormParam("productIds") List<String> productIds,
                                      @FormParam("productVariantIds") List<String> productVariantIds,
                                      @FormParam("cities") List<String> cities,
                                      @FormParam("zones") List<Long> zones,
                                      @FormParam("states") List<String> states,
                                      @FormParam("verified") Boolean verified,
                                      @FormParam("categories") List<String> categories,
                                      @FormParam("userOrderCount") Integer userOrderCount,
                                      @FormParam("equality") String equality,
                                      @FormParam("minimum") int minimum,
                                      @FormParam("storeIds") List<Long> storeIds,
                                      @FormParam("emails") List<String> emails) {
        this.emails = emails;
        this.productIds = productIds;
        this.productVariantIds = productVariantIds;
        this.cities = cities;
        this.states = states;
        this.verified = verified;
        this.userOrderCount = userOrderCount;
        this.equality = equality;
        this.zones = zones;
        this.storeIds = storeIds;
        this.minimum = minimum;
        return getUsers();
    }

    private Response getUsers() {
        Response response = null;
        criteria = new UsersSearchCriteria();
        try {
            if (verified != null) {
                criteria.setVerified(verified);
            }
            if (userOrderCount != null) {
                criteria.setUserOrderCount(userOrderCount);
            }
            if (org.apache.commons.lang.StringUtils.isNotBlank(equality)) {
                criteria.setEquality(equality);
            }
            if (HKCollectionUtils.isNotBlank(storeIds)) {
                criteria.setStoreIds(storeIds);
            }
            if (HKCollectionUtils.isNotBlank(productIds)) {
                criteria.setProductIds(productIds);
            }
            if (HKCollectionUtils.isNotBlank(emails)) {
                criteria.setEmails(emails);
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

    class UserDto {
        public String email;
        public String name;
        public String login;
        public Integer subscribedMask;
        public String unsubscribeToken;
    }
}
