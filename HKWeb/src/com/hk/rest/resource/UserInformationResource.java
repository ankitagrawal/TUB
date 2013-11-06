package com.hk.rest.resource;

import com.hk.core.search.UsersSearchCriteria;
import com.hk.domain.user.User;
import com.hk.pact.service.UserSearchService;
import org.apache.commons.lang.StringUtils;
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
    private String zone;
    private String city;
    private String state;
    private String productId;
    private String productVariantId;
    private String verified;

    @Autowired
    UserSearchService userSearchService;


    @GET
    @Path("/product/{productId}")
    @Produces("application/json")
    public Response getUserEmailsByProduct(@PathParam("productId") String productId) {
        this.productId = productId;
        return getUsers();
    }

    @GET
    @Path("/productVariant/{productVariantId}")
    @Produces("application/json")
    public Response getUserEmailsByProductVariant(@PathParam("productVariantId") String productVariantId) {
        this.productVariantId = productVariantId;
        return getUsers();
    }

    @GET
    @Path("/city/{city}")
    @Produces("application/json")
    public Response getUserEmailsByCity(@PathParam("city") String city) {
        this.city = city;
        return getUsers();
    }

    @GET
    @Path("/zone/{zone}")
    @Produces("application/json")
    public Response getUserEmailsByZone(@PathParam("zone") String zone) {
        this.zone = zone;
        return getUsers();
    }

    @GET
    @Path("/state/{state}")
    @Produces("application/json")
    public Response getUserEmailsByState(@PathParam("state") String state) {
        this.state = state;
        return getUsers();
    }

    @GET
    @Path("/verified")
    @Produces("application/json")
    public Response getUserEmailsByVerified() {
        this.verified = "true";
        return getUsers();
    }

    private Response getUsers() {
        Response response = null;
        List<User> users = null;
        criteria = new UsersSearchCriteria();
        try {
            if (StringUtils.isNotBlank(verified)) {
                criteria.setVerified(verified);
            } else if (StringUtils.isNotBlank(productId)) {
                criteria.setProductId(productId);
            } else if (StringUtils.isNotBlank(productVariantId)) {
                criteria.setProductVariantId(productVariantId);
            } else if (StringUtils.isNotBlank(state)) {
                criteria.setProductVariantId(productVariantId);
            } else if (StringUtils.isNotBlank(zone)) {
                criteria.setZone(zone);
            } else if (StringUtils.isNotBlank(city)) {
                criteria.setCity(city);
            } else if (StringUtils.isNotBlank(state)) {
                criteria.setState(state);
            }

            users = userSearchService.searchUsers(criteria);
            List<UserDto> userDtos = new LinkedList<UserDto>();
            for (User u : users) {
                UserDto udto = new UserDto();
                udto.name = u.getName();
                udto.email = u.getEmail();
                udto.login = u.getLogin();
                userDtos.add(udto);
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
    }
}
