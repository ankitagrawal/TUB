/**
 * 
 */
package com.hk.rest.mobile.service.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;

import net.sourceforge.stripes.action.JsonResolution;
import net.sourceforge.stripes.validation.LocalizableError;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.gson.JsonUtils;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.domain.TempToken;
import com.hk.domain.user.User;
import com.hk.manager.EmailManager;
import com.hk.manager.LinkManager;
import com.hk.pact.dao.core.TempTokenDao;
import com.hk.pact.dao.user.UserDao;
import com.hk.rest.mobile.service.utils.MHKConstants;
import com.hk.web.HealthkartResponse;

/**
 * @author Satish
 *
 */
@Path ("/mForgotPassword")
@Component
public class MForgotPasswordAction extends MBaseAction{


    @Autowired
    UserDao                 userDao;
    @Autowired
    TempTokenDao            tempTokenDao;
    @Autowired
    LinkManager             linkManager;
    @Autowired
    EmailManager            emailManager;
    public static final int EXPIRY_DAYS = 10;

 
    @SuppressWarnings("unchecked")
    @JsonHandler
	@GET
	@Path ("/forgotPassword/")
	@Produces ("application/json")

    public String forgotPassword(@QueryParam("email") String email, @Context HttpServletResponse response) {

		HealthkartResponse healthkartResponse;
		String jsonBuilder = "";
		String message = MHKConstants.STATUS_DONE;
		String status = MHKConstants.STATUS_OK;
		try{
		User user = userDao.findByLogin(email);
        if (user == null) {
            message = MHKConstants.NO_RESULTS;
            status = MHKConstants.STATUS_ERROR;
        }

        TempToken tempToken = tempTokenDao.createNew(user, EXPIRY_DAYS);
        String resetPasswordLink = linkManager.getResetPasswordLink(tempToken);
        // System.out.println("Reset Password Link: " + resetPasswordLink);
        emailManager.sendResetPasswordEmail(user, resetPasswordLink);
        } catch (Exception e) {
            message = MHKConstants.USER_DOES_NOT_EXIST;
            status = MHKConstants.STATUS_ERROR;
            return JsonUtils.getGsonDefault().toJson(new HealthkartResponse(status,message,message));
        }

        healthkartResponse = new HealthkartResponse(MHKConstants.STATUS_OK,
                "Password reset mail has been sent to your email id. Please check your Inbox/Spam/Junk folders.", MHKConstants.STATUS_DONE);

        noCache(response);

        jsonBuilder = JsonUtils.getGsonDefault().toJson(healthkartResponse);

        addHeaderAttributes(response);

        return jsonBuilder;
    }
}