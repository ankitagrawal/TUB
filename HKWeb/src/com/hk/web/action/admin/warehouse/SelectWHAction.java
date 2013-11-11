package com.hk.web.action.admin.warehouse;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.RoleConstants;
import com.hk.core.search.UsersSearchCriteria;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.service.core.UserSearchServiceImpl;
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
import org.springframework.ui.Model;
import org.stripesstuff.plugin.security.Secure;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class SelectWHAction extends BaseAction {


    @Autowired
    UserDao userDao;
    @Autowired
    private UserService userService;
    private Warehouse setWarehouse;

    private String params;
    private int result;
    private String emails;

    public String getEmails() {
        return emails;
    }

    public void setEmails(String emails) {
        this.emails = emails;
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

    @DefaultHandler
    public Resolution testSQL() {
        Long startTime = System.currentTimeMillis();
        UsersSearchCriteria criteria = new UsersSearchCriteria();
        String prodnames = params;
        List<String> ids = Arrays.asList(prodnames.split(","));


        criteria.setProductIds(ids);
        List<String> ems = null;
        List<User> users = null;
        try {
            if ("1".equals(emails)) {
                ems = userSearchService.searchUserEmails(criteria);
            } else {
                users = userSearchService.searchUsers(criteria);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if ("1".equals(emails)) {
            result = ems.size();
        } else {
            result = users.size();
        }
        Long endTime = System.currentTimeMillis();
        // write to file
        String ans = "result size: " + result + " time taken in millis: " + (endTime - startTime);
        try {
            String path = new File(".").getCanonicalPath();
//            File file = new File("D://temp/queryresults.txt");
            File file = new File("/usr/local/projects/rejuvenate/HealthKartWork/logs/queryresults.txt");

            if (!file.exists()) {
                file.createNewFile();
            }
            BufferedWriter br = new BufferedWriter(new FileWriter(file));
            br.write(ans);
            br.close();
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }

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