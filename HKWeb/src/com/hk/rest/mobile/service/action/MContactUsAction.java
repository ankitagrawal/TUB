package com.hk.rest.mobile.service.action;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.servlet.http.HttpServletResponse;

import com.hk.dto.menu.MenuNode;
import com.hk.helper.MenuHelper;
import com.hk.web.HealthkartResponse;
import com.hk.rest.mobile.service.utils.MHKConstants;
import com.akube.framework.gson.JsonUtils;

import java.util.List;
import java.util.ArrayList;

import net.sourceforge.stripes.action.DefaultHandler;

/**
 * Created by IntelliJ IDEA.
 * User: Satish
 * Date: Oct 14, 2012
 * Time: 10:47:46 AM
 * To change this template use File | Settings | File Templates.
 */
@Path("/mContactus")
@Component


public class MContactUsAction extends MBaseAction{


        @DefaultHandler
        @GET
        @Path("/contactus/")
        @Produces("application/json")

        public String shopPrimaryCategory(@Context HttpServletResponse response) {
            HealthkartResponse healthkartResponse;
            String jsonBuilder = "";
            String message = MHKConstants.STATUS_DONE;
            String status = MHKConstants.STATUS_OK;

            healthkartResponse = new HealthkartResponse(status, message, status);
            jsonBuilder = JsonUtils.getGsonDefault().toJson(healthkartResponse);

            addHeaderAttributes(response);

            return jsonBuilder;

        }
}
