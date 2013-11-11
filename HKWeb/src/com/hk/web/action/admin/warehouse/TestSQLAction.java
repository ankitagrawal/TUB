package com.hk.web.action.admin.warehouse;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.core.search.UsersSearchCriteria;
import com.hk.domain.user.User;
import com.hk.impl.service.core.UserSearchServiceImpl;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import java.util.Arrays;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: user
 * Date: 11/11/13
 * Time: 3:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestSQLAction extends BaseAction {
    private int result;
    String params;

    @DefaultHandler
    public Resolution handle() {
        UsersSearchCriteria criteria = new UsersSearchCriteria();
        String prodnames = params;
        List<String> ids = Arrays.asList(prodnames.split(","));


        criteria.setProductIds(ids);
        List<User> users = new UserSearchServiceImpl().searchUsers(criteria);
        result = users.size();
        return new ForwardResolution("/pages/admin/adminHome.jsp");
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
