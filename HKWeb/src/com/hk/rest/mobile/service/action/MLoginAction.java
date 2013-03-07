package com.hk.rest.mobile.service.action;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;

import net.sourceforge.stripes.action.DefaultHandler;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.session.Session;

import com.hk.constants.core.HealthkartConstants;
import com.hk.domain.user.Address;
import com.hk.dto.user.UserLoginDto;
import com.hk.exception.HealthkartLoginException;
import com.hk.manager.UserManager;
import com.hk.pact.service.RoleService;
import com.hk.rest.mobile.service.model.MUserLoginJSONResponse;
import com.hk.rest.mobile.service.utils.MHKConstants;
import com.hk.web.HealthkartResponse;

/**
 * Created by IntelliJ IDEA. User: Satish Date: Sep 21, 2012 Time: 5:48:22 PM To
 * change this template use File | Settings | File Templates.
 */
@Path("/mLogin")
@Component
public class MLoginAction extends MBaseAction {
	private static Logger logger = LoggerFactory.getLogger(MLoginAction.class);

	private String redirectUrl;
	private boolean rememberMe = true;
	private String source;

	@Session(key = HealthkartConstants.Session.userId)
	private String userId;

	@Session(key = "userName")
	private String userName;

	public static final String SOURCE_CHECKOUT = "checkout";

	@Autowired
	UserManager userManager;

	@Autowired
	private RoleService roleService;

	@DefaultHandler
	@POST
	@Path("/login/")
	@Produces("application/json")
	public String login(@FormParam("email") String email,
			@FormParam("password") String password,@Context HttpServletRequest request) throws Exception {
		HealthkartResponse healthkartResponse;
		String jsonBuilder = "";
		String message = MHKConstants.STATUS_DONE;
		String status = MHKConstants.STATUS_OK;
		UserLoginDto userLoginDto = null;
		List<MUserLoginJSONResponse> userItemList = null;
		try {
			userLoginDto = userManager.login(email, password, true);
		} catch (HealthkartLoginException e) {
			message = MHKConstants.INVALID_LOGIN_CRDNTLS;
			status = MHKConstants.STATUS_ERROR;
		}
		userName = null;

		if (status == MHKConstants.STATUS_OK) {
			// healthkartresponse = new
			// HealthkartResponse(status,message,userLoginDto);
			userItemList = new ArrayList<MUserLoginJSONResponse>();

			MUserLoginJSONResponse mUserLoginJSONResponse = new MUserLoginJSONResponse();
			if (null != userLoginDto.getLoggedUser()) {
				if (null != userLoginDto.getLoggedUser().getEmail())
					mUserLoginJSONResponse.setEmail(userLoginDto
							.getLoggedUser().getEmail());
				if (null != userLoginDto.getLoggedUser().getId())
					mUserLoginJSONResponse.setId(userLoginDto.getLoggedUser()
							.getId());
				if (null != userLoginDto.getLoggedUser().getLogin())
					mUserLoginJSONResponse.setLogin(userLoginDto
							.getLoggedUser().getLogin());
				if (null != userLoginDto.getLoggedUser().getName()){
					mUserLoginJSONResponse.setName(userLoginDto.getLoggedUser()
							.getName());
					userName = userLoginDto.getLoggedUser()
							.getName();
					//bugus..need to change the logic
					request.getSession().setAttribute("userName", userName);
				}
				if (null != userLoginDto.getLoggedUser().getPassword())
					mUserLoginJSONResponse.setPassword(userLoginDto
							.getLoggedUser().getPassword());
				if (null != userLoginDto.getLoggedUser().getAddresses()
						&& !userLoginDto.getLoggedUser().getAddresses()
								.isEmpty()) {
					Address address = (Address) userLoginDto.getLoggedUser()
							.getAddresses().get(0);
					mUserLoginJSONResponse.setCity(address.getCity());
					mUserLoginJSONResponse.setLine1(address.getLine1());
					mUserLoginJSONResponse.setLine2(address.getLine2());
					mUserLoginJSONResponse.setPhone(address.getPhone());
					mUserLoginJSONResponse.setPin(address.getPincode().getPincode());
					mUserLoginJSONResponse.setState(address.getState());
				}
				mUserLoginJSONResponse.setType("Logged");
				userItemList.add(mUserLoginJSONResponse);
			}
			MUserLoginJSONResponse mTempUserLoginJSONResponse = new MUserLoginJSONResponse();
			if (null != userLoginDto.getTempUser()) {
				if (null != userLoginDto.getTempUser().getEmail())
					mTempUserLoginJSONResponse.setEmail(userLoginDto
							.getTempUser().getEmail());
				if (null != userLoginDto.getTempUser().getId())
					mTempUserLoginJSONResponse.setId(userLoginDto.getTempUser()
							.getId());
				if (null != userLoginDto.getTempUser().getLogin())
					mTempUserLoginJSONResponse.setLogin(userLoginDto
							.getTempUser().getLogin());
				if (null != userLoginDto.getTempUser().getName())
					mTempUserLoginJSONResponse.setName(userLoginDto
							.getTempUser().getName());
				if (null != userLoginDto.getTempUser().getPassword())
					mTempUserLoginJSONResponse.setPassword(userLoginDto
							.getTempUser().getPassword());
				if (null != userLoginDto.getTempUser().getAddresses()
						&& !userLoginDto.getTempUser().getAddresses().isEmpty()) {
					Address address = (Address) userLoginDto.getTempUser()
							.getAddresses().get(0);
					mTempUserLoginJSONResponse.setCity(address.getCity());
					mTempUserLoginJSONResponse.setLine1(address.getLine1());
					mTempUserLoginJSONResponse.setLine2(address.getLine2());
					mTempUserLoginJSONResponse.setPhone(address.getPhone());
					mTempUserLoginJSONResponse.setPin(address.getPincode().getPincode());
					mTempUserLoginJSONResponse.setState(address.getState());

				}
				mUserLoginJSONResponse.setType("Temp");
				userItemList.add(mTempUserLoginJSONResponse);
			}

			userId = userLoginDto.getLoggedUser().getId().toString();
		}
		healthkartResponse = new HealthkartResponse(status, message,
				userItemList);
		jsonBuilder = com.akube.framework.gson.JsonUtils.getGsonDefault()
				.toJson(healthkartResponse);
		return jsonBuilder;

	}

	@GET
	@Path("/logout/")
	@Produces("application/json")
	public String logout(@Context HttpServletRequest request,@Context HttpServletResponse response) {
		HealthkartResponse healthkartResponse;
		String jsonBuilder = "";
		String message = MHKConstants.STATUS_DONE;
		String status = MHKConstants.STATUS_OK;
		try {
			request.getSession().removeAttribute("userName");
			SecurityUtils.getSubject().logout();
		} catch (Exception e) {
			message = MHKConstants.STATUS_ERROR;
			status = MHKConstants.STATUS_ERROR;
		}
		healthkartResponse = new HealthkartResponse(status, message, status);
		jsonBuilder = com.akube.framework.gson.JsonUtils.getGsonDefault()
				.toJson(healthkartResponse);
		return jsonBuilder;
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
