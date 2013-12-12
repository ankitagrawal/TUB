package com.hk.rest.resource;

import com.hk.core.search.UsersSearchCriteria;
import com.hk.dto.user.UserDTO;
import com.hk.pact.service.UserSearchService;
import com.hk.pact.service.catalog.CategoryService;
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
    private List<String> zones;
    private List<String> cities;
    private List<String> emails;
    private List<String> states;
    private List<String> productIds;
    private List<String> productVariantIds;
    private List<Long> storeIds;
    private String verified;
    private Integer userOrderCount;
    private String equality;
    private List<String> categories;
    private List<String> badgeNames;
    private String gender;

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
                                      @FormParam("zones") List<String> zones,
                                      @FormParam("states") List<String> states,
                                      @FormParam("verified") String verified,
                                      @FormParam("badgeNames") List<String> badgeNames,
                                      @FormParam("categories") List<String> categories,
                                      @FormParam("userOrderCount") Integer userOrderCount,
                                      @FormParam("equality") String equality,
                                      @FormParam("storeIds") List<Long> storeIds,
                                      @FormParam("emails") List<String> emails,
                                      @FormParam("gender") String gender) {
        this.badgeNames = badgeNames;
        this.categories = categories;
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
        this.gender = gender;
        return getUsers();
    }

    private Response getUsers() {
        Response response = null;
        criteria = new UsersSearchCriteria();
        try {
            if (HKCollectionUtils.isNotBlank(badgeNames)) {
                criteria.setBadgeNames(badgeNames);
            }
            if (HKCollectionUtils.isNotBlank(categories)) {
                criteria.setCategories(categories);
            }
            if (verified != null) {
                criteria.setVerified(verified);
            }
            if (userOrderCount != null) {
                criteria.setUserOrderCount(userOrderCount);
            }
            if (StringUtils.isNotBlank(equality)) {
                criteria.setEquality(equality);
            }
            if (StringUtils.isNotBlank(gender)) {
                criteria.setGender(gender);
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

            List<UserDTO> userInfo = null;

            userInfo = userSearchService.searchUserInfo(criteria);

            if (userInfo == null) {
                throw new Exception("Null Data");
            }

            final GenericEntity<List<UserDTO>> genericEntity = new GenericEntity<List<UserDTO>>(userInfo) {
            };
            response = Response.status(Response.Status.OK).entity(genericEntity).build();
        } catch (Exception ex) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            logger.error("Unable to get User Details ", ex);
        }
        return response;
    }
}
