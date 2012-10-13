package com.hk.rest.mobile.service.action;

import com.hk.constants.core.HealthkartConstants;
import com.hk.dto.user.UserLoginDto;
import com.hk.exception.HealthkartLoginException;
import com.hk.manager.UserManager;
import com.hk.pact.dao.RoleDao;
import com.hk.pact.service.RoleService;
import com.hk.rest.mobile.service.model.MUserLoginJSONResponse;
import com.hk.web.HealthkartResponse;
import com.hk.domain.user.Address;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.session.Session;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Satish
 * Date: Sep 21, 2012
 * Time: 5:48:22 PM
 * To change this template use File | Settings | File Templates.
 */
@Path("/mLogin")
@Component

public class MLoginAction {
    private static Logger logger = LoggerFactory.getLogger(MLoginAction.class);


    private String redirectUrl;
    private boolean rememberMe;
    private String source;

    @Session(key = HealthkartConstants.Session.userId)
    private String userId;

    public static final String SOURCE_CHECKOUT = "checkout";

    @Autowired
    UserManager userManager;
    @Autowired
    RoleDao roleDao;

    @Autowired
    private RoleService roleService;

    @DefaultHandler

    @GET
    @Path("/login/")
    //@Produces("application/json")
    public Resolution login(@QueryParam("email") String email, @QueryParam("password") String password) throws Exception {
        HealthkartResponse healthkartResponse;
        String jsonBuilder = "";
        String message = "Done";
        String status = HealthkartResponse.STATUS_OK;
        UserLoginDto userLoginDto = null;
        try {
            userLoginDto = userManager.login(email, password, true);
        } catch (HealthkartLoginException e) {
            message = "Invalid login credentials.";
            status = HealthkartResponse.STATUS_ERROR;
        }
        //      healthkartresponse = new HealthkartResponse(status,message,userLoginDto);
        List<MUserLoginJSONResponse> userItemList = new ArrayList<MUserLoginJSONResponse>();

        MUserLoginJSONResponse mUserLoginJSONResponse = new MUserLoginJSONResponse();
        if (null != userLoginDto.getLoggedUser()) {
            if (null != userLoginDto.getLoggedUser().getEmail())
                mUserLoginJSONResponse.setEmail(userLoginDto.getLoggedUser().getEmail());
            if (null != userLoginDto.getLoggedUser().getId())
                mUserLoginJSONResponse.setId(userLoginDto.getLoggedUser().getId());
            if (null != userLoginDto.getLoggedUser().getLogin())
                mUserLoginJSONResponse.setLogin(userLoginDto.getLoggedUser().getLogin());
            if (null != userLoginDto.getLoggedUser().getName())
                mUserLoginJSONResponse.setName(userLoginDto.getLoggedUser().getName());
            if (null != userLoginDto.getLoggedUser().getPassword())
                mUserLoginJSONResponse.setPassword(userLoginDto.getLoggedUser().getPassword());
            if (null != userLoginDto.getLoggedUser().getAddresses()&&!userLoginDto.getLoggedUser().getAddresses().isEmpty()){
                Address address = (Address)userLoginDto.getLoggedUser().getAddresses().get(0);
                mUserLoginJSONResponse.setCity(address.getCity());
                mUserLoginJSONResponse.setLine1(address.getLine1());
                mUserLoginJSONResponse.setLine2(address.getLine2());
                mUserLoginJSONResponse.setPhone(address.getPhone());
                mUserLoginJSONResponse.setPin(address.getPin());
                mUserLoginJSONResponse.setState(address.getState());
            }
            mUserLoginJSONResponse.setType("Logged");
            userItemList.add(mUserLoginJSONResponse);
        }
        MUserLoginJSONResponse mTempUserLoginJSONResponse = new MUserLoginJSONResponse();
        if (null != userLoginDto.getTempUser()) {
            if (null != userLoginDto.getTempUser().getEmail())
                mTempUserLoginJSONResponse.setEmail(userLoginDto.getTempUser().getEmail());
            if (null != userLoginDto.getTempUser().getId())
                mTempUserLoginJSONResponse.setId(userLoginDto.getTempUser().getId());
            if (null != userLoginDto.getTempUser().getLogin())
                mTempUserLoginJSONResponse.setLogin(userLoginDto.getTempUser().getLogin());
            if (null != userLoginDto.getTempUser().getName())
                mTempUserLoginJSONResponse.setName(userLoginDto.getTempUser().getName());
            if (null != userLoginDto.getTempUser().getPassword())
                mTempUserLoginJSONResponse.setPassword(userLoginDto.getTempUser().getPassword());
            if (null != userLoginDto.getTempUser().getAddresses()&&!userLoginDto.getTempUser().getAddresses().isEmpty()){
                Address address = (Address)userLoginDto.getTempUser().getAddresses().get(0);
                mTempUserLoginJSONResponse.setCity(address.getCity());
                mTempUserLoginJSONResponse.setLine1(address.getLine1());
                mTempUserLoginJSONResponse.setLine2(address.getLine2());
                mTempUserLoginJSONResponse.setPhone(address.getPhone());
                mTempUserLoginJSONResponse.setPin(address.getPin());
                mTempUserLoginJSONResponse.setState(address.getState());

            }
            mUserLoginJSONResponse.setType("Temp");
            userItemList.add(mTempUserLoginJSONResponse);
        }


        healthkartResponse = new HealthkartResponse(status, message, userItemList);
        jsonBuilder = com.akube.framework.gson.JsonUtils.getGsonDefault().toJson(healthkartResponse);
      //  return jsonBuilder;
        return new ForwardResolution("/pages/home.jsp");

    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public void setRedirectUrl(String redirectUrl) {
        this.redirectUrl = redirectUrl;
    }

    public boolean isRememberMe() {
        return rememberMe;
    }

    public void setRememberMe(boolean rememberMe) {
        this.rememberMe = rememberMe;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public RoleService getRoleService() {
        return roleService;
    }

    public void setRoleService(RoleService roleService) {
        this.roleService = roleService;
    }

}

