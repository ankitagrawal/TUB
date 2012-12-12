package com.hk.rest.mobile.service.action;

import com.akube.framework.gson.JsonUtils;
import com.hk.dto.menu.MenuNode;
import com.hk.helper.MenuHelper;
import com.hk.rest.mobile.service.utils.MHKConstants;
import com.hk.web.HealthkartResponse;
import net.sourceforge.stripes.action.DefaultHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Satish
 * Date: Oct 1, 2012
 * Time: 11:57:20 AM
 * To change this template use File | Settings | File Templates.
 */

@Path("/mShop")
@Component

public class MShopAction extends MBaseAction{

    List<MenuNode> menuNodes;
    @Autowired
    MenuHelper menuHelper;

    @DefaultHandler
    @GET
    @Path("/primaryCategory/")
    @Produces("application/json")

    public String shopPrimaryCategory(@Context HttpServletResponse response) {
        HealthkartResponse healthkartResponse;
        String jsonBuilder = "";
        String message = MHKConstants.STATUS_DONE;
        String status = MHKConstants.STATUS_OK;
        List<MMenuNode> primaryMenuList = new ArrayList<MMenuNode>();
        try {
            menuNodes = menuHelper.getMenuNodes();
            MMenuNode primaryMenuNode;
            for (MenuNode menu : menuNodes) {
                primaryMenuNode = new MMenuNode();
                primaryMenuNode.setLevel(menu.getLevel());
                primaryMenuNode.setName(menu.getName());
                //primaryMenuNode.setUrl(menu.getUrl());
                primaryMenuNode.setUrl(menu.getUrl().substring(menu.getUrl().lastIndexOf('/')+1, menu.getUrl().length()));
                primaryMenuList.add(primaryMenuNode);
            }
        } catch (Exception e) {
            message = MHKConstants.NO_RESULTS;
            status = MHKConstants.STATUS_ERROR;
        }
        healthkartResponse = new HealthkartResponse(status, message, primaryMenuList);
        jsonBuilder = JsonUtils.getGsonDefault().toJson(healthkartResponse);

        addHeaderAttributes(response);

        return jsonBuilder;

    }

    @GET
    @Path("/secondaryCategory/")
    @Produces("application/json")

    public String shopSecondaryCategory(@QueryParam("primaryCategory") String primaryNode,
                                        @Context HttpServletResponse response) {
        HealthkartResponse healthkartResponse;
        String jsonBuilder = "";
        String message = MHKConstants.STATUS_DONE;
        String status = MHKConstants.STATUS_OK;
        List<MMenuNode> secondaryMenuList = new ArrayList<MMenuNode>();
        try {
            menuNodes = menuHelper.getMenuNodes();
            MMenuNode secondaryMenuNode;
            for (MenuNode menu : menuNodes) {
                if (menu.getUrl().equals('/'+primaryNode)) {
					for (MenuNode secondaryMenu : menu.getChildNodes()) {
						String url =secondaryMenu.getUrl();
						if (null!=url&& !(url.contains(MHKConstants.EYEGLASSES)
								||url.contains(MHKConstants.LENSES))) {
							secondaryMenuNode = new MMenuNode();
							secondaryMenuNode
									.setLevel(secondaryMenu.getLevel());
							secondaryMenuNode.setName(secondaryMenu.getName());
							// secondaryMenuNode.setUrl(secondaryMenu.getUrl());
							secondaryMenuNode.setUrl(secondaryMenu.getUrl()
									.substring(
											secondaryMenu.getUrl().lastIndexOf(
													'/') + 1,
											secondaryMenu.getUrl().length()));
							secondaryMenuNode.setCurrentCategory(primaryNode);
							secondaryMenuList.add(secondaryMenuNode);
						}
					}
                }
            }
        } catch (Exception e) {
            message = MHKConstants.NO_RESULTS;
            status = MHKConstants.STATUS_ERROR;
        }
        healthkartResponse = new HealthkartResponse(status, message, secondaryMenuList);
        jsonBuilder = JsonUtils.getGsonDefault().toJson(healthkartResponse);
        addHeaderAttributes(response);

        return jsonBuilder;

    }

    @GET
    @Path("/tertiaryCategory/")
    @Produces("application/json")

    public String shopTertiaryCategory(@QueryParam("secondaryCategory") String secondaryNode,
                                       @Context HttpServletResponse response) {
        HealthkartResponse healthkartResponse;
        String jsonBuilder = "";
        String message = MHKConstants.STATUS_DONE;
        String status = MHKConstants.STATUS_OK;
        List<MMenuNode> tertiaryMenuNodeList = new ArrayList<MMenuNode>();
        try {
            menuNodes = menuHelper.getMenuNodes();
            MMenuNode tertiaryMenuNode;
            for (MenuNode menu : menuNodes) {
                for (MenuNode secondaryMenu : menu.getChildNodes()) {
                    if (secondaryMenu.getName().equals(secondaryNode))
                        for (MenuNode tertiaryMenu : secondaryMenu.getChildNodes()) {
                            tertiaryMenuNode = new MMenuNode();
                            tertiaryMenuNode.setLevel(tertiaryMenu.getLevel());
                            tertiaryMenuNode.setName(tertiaryMenu.getName());
                            tertiaryMenuNode.setCurrentCategory(secondaryNode);
                            tertiaryMenuNode.setUrl(tertiaryMenu.getUrl());
                            tertiaryMenuNodeList.add(tertiaryMenuNode);
                        }
                }
            }
        } catch (Exception e) {
            message = MHKConstants.NO_RESULTS;
            status = MHKConstants.STATUS_ERROR;
        }
        healthkartResponse = new HealthkartResponse(status, message, tertiaryMenuNodeList);
        jsonBuilder = JsonUtils.getGsonDefault().toJson(healthkartResponse);

        addHeaderAttributes(response);

        return jsonBuilder;
    }

    public List<MenuNode> getMenuNodes() {
        return menuNodes;
    }
}

class MMenuNode {

    private String name;
    private String url;
    private int level;
    private String currentCategory;

    public String getCurrentCategory() {
        return currentCategory;
    }

    public void setCurrentCategory(String currentCategory) {
        this.currentCategory = currentCategory;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}