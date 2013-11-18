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
    //    private List<String> categories;
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
    public Response getUsersByProduct(@FormParam("productIds") String productIds,
                                      @FormParam("productVariantIds") String productVariantIds,
                                      @FormParam("cities") String cities,
                                      @FormParam("zones") String zones,
                                      @FormParam("states") String states,
                                      @FormParam("verified") String verified,
                                      @FormParam("categories") String categories,
                                      @FormParam("userOrderCount") String userOrderCount,
                                      @FormParam("equality") String equality,
                                      @FormParam("minimum") String minimum,
                                      @FormParam("storeIds") String storeIds,
                                      @FormParam("emails") String emails) {
        List<String> allProdIds = StringUtils.getListFromString(productIds, ",");
        List<String> allProdVarIds = StringUtils.getListFromString(productVariantIds, ",");
        List<String> allCities = StringUtils.getListFromString(cities, ",");
        List<String> allStates = StringUtils.getListFromString(states, ",");
        List<String> allZones = StringUtils.getListFromString(zones, ",");
        List<String> allStores = StringUtils.getListFromString(storeIds, ",");
        List<String> allEmails = StringUtils.getListFromString(emails, ",");

        this.emails = allEmails;
        this.productIds = setProductIds(allProdIds, categories);
        this.productVariantIds = allProdVarIds;
        this.cities = allCities;
        this.states = allStates;
        this.verified = setVerified(verified);
        this.userOrderCount = setUserOrderCount(userOrderCount);
        this.equality = equality;
        this.zones = setLongVals(allZones);
        this.storeIds = setLongVals(allStores);
        this.minimum = setMinimum(minimum);

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

    private int setMinimum(String minimum) {
        int min = 1;
        try {
            min = Integer.parseInt(minimum);
        } catch (Exception ignore) {
        }
        return min;
    }

    private List<Long> setLongVals(List<String> allVals) {
        if (allVals == null) {
            return null;
        }
        List<Long> vals = new ArrayList<Long>();
        for (String z : allVals) {
            try {
                Long id = Long.parseLong(z);
                vals.add(id);
            } catch (Exception ignore) {
            }
        }

        return vals;
    }

    private Integer setUserOrderCount(String userOrderCount) {
        Integer uoc = null;
        try {
            uoc = Integer.parseInt(userOrderCount);
        } catch (Exception e) {
            uoc = null;
        }
        return uoc;
    }

    private Boolean setVerified(String verified) {
        Boolean ver = null;
        try {
            ver = "true".equalsIgnoreCase(verified) ? true : "false".equalsIgnoreCase(verified) ? false : null;
        } catch (Exception e) {
            this.verified = null;
        }
        return ver;
    }

    private List<String> setProductIds(List<String> allProdIds, String categories) {
        Set<Category> cats = categoryService.getCategoriesFromCategoryNames(categories);
        List<Product> prods = new ArrayList<Product>();
        for (Category c : cats) {
            List<Product> p = c.getProducts();
            prods.addAll(p);
        }
        List<String> prodIds = new ArrayList<String>();
        for (Product pr : prods) {
            prodIds.add(pr.getId());
        }
        prodIds.addAll(allProdIds);
        return prodIds;
    }

    class UserDto {
        public String email;
        public String name;
        public String login;
        public Integer subscribedMask;
        public String unsubscribeToken;
    }
}
