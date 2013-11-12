package com.hk.web.action.admin.warehouse;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.RoleConstants;
import com.hk.core.search.UsersSearchCriteria;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.service.UserSearchService;
import com.hk.pact.service.UserService;
import com.hk.web.action.admin.AdminHomeAction;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.util.*;

@Component
public class SelectWHAction extends BaseAction {


    @Autowired
    UserDao userDao;
    @Autowired
    private UserService userService;
    private Warehouse setWarehouse;

    private String pvs;
    private String params;
    private int result;
    private Long time;
    private String minimum;
    private String cities;
    private String states;
    private String categories;
    private String verified;

    public String getPvs() {
        return pvs;
    }

    public void setPvs(String pvs) {
        this.pvs = pvs;
    }

    public String getZones() {
        return zones;
    }

    public String getVerified() {
        return verified;
    }

    public void setVerified(String verified) {
        this.verified = verified;
    }

    public void setZones(String zones) {
        this.zones = zones;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public String getStates() {
        return states;
    }

    public void setStates(String states) {
        this.states = states;
    }

    public String getCities() {
        return cities;
    }

    public void setCities(String cities) {
        this.cities = cities;
    }

    private String zones;

    public String getMinimum() {
        return minimum;
    }

    public void setMinimum(String minimum) {
        this.minimum = minimum;
    }

    @Autowired
    UserSearchService userSearchService;

    public Resolution pre() {
        //TODO #introducing gc as a hit n try solution for server performance
//    System.gc();
        return new ForwardResolution("/pages/admin/selectWH.jsp");
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    @DefaultHandler
    public Resolution testSQL() {
        Long startTime = System.currentTimeMillis();
        UsersSearchCriteria criteria = new UsersSearchCriteria();
        String prodnames = params;
        if (prodnames != null) {
            List<String> ids = Arrays.asList(prodnames.split(","));
            criteria.setProductIds(ids);
        }
        if (verified != null) {
            criteria.setVerified(verified);
        }
        if (pvs != null) {
            List<String> pvids = Arrays.asList(pvs.split(","));
            criteria.setProductVariantIds(pvids);
        }
        if (categories != null) {
            List<String> category = Arrays.asList(categories.split(","));
            criteria.setCategories(category);
        }
        if (cities != null) {
            List<String> city = Arrays.asList(cities.split(","));
            criteria.setCities(city);
        }
        if (states != null) {
            List<String> state = Arrays.asList(states.split(","));
            criteria.setStates(state);
        }
        if (zones != null) {
            List<String> zone = Arrays.asList(zones.split(","));
            List<Long> zonel = new ArrayList<Long>(zone.size());
            for (String s : zone) {
                try {
                    Long l = Long.parseLong(s);
                    zonel.add(l);
                } catch (Exception e) {
                }
            }
            criteria.setZones(zonel);
        }


        List<Object[]> ems = null;
        List<User> users = null;
        try {
            if ("1".equals(minimum)) {
                ems = userSearchService.searchUserInfo(criteria);
            } else {
                users = userSearchService.searchUsers(criteria);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ("1".equals(minimum)) {
            result = ems.size();
        } else {

            result = users.size();
        }
        Long endTime = System.currentTimeMillis();
        time = endTime - startTime;
        return new ForwardResolution("/pages/admin/adminHome.jsp");
    }


    @Secure(hasAnyRoles = {RoleConstants.WH_MANAGER_L1, RoleConstants.CATEGORY_MANAGER, RoleConstants.ADMIN}, authActionBean = AdminPermissionAction.class)
    public Resolution bindUserWithWarehouse() {
        User loggedOnUser = getPrincipalUser();
        Set<Warehouse> warehouses = new HashSet<Warehouse>();
        if (setWarehouse != null) {
            warehouses.add(setWarehouse);
        }
        loggedOnUser.setWarehouses(warehouses);
        userService.save(loggedOnUser);

        return new RedirectResolution(AdminHomeAction.class);
    }

    public Resolution getUserWarehouse() {

        setWarehouse = userService.getWarehouseForLoggedInUser();
        return new ForwardResolution("/pages/admin/selectWH.jsp");
    }

    public Warehouse getSetWarehouse() {
        return setWarehouse;
    }

    public void setSetWarehouse(Warehouse setWarehouse) {
        this.setWarehouse = setWarehouse;
    }


    public void setUserService(UserService userService) {
        this.userService = userService;
    }

}