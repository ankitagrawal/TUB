package com.hk.web.action.admin.crm;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.domain.user.User;
import com.hk.domain.user.UserDetail;
import com.hk.pact.service.user.UserDetailService;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Marut
 * Date: 10/19/12
 * Time: 4:58 PM
 * To change this template use File | Settings | File Templates.
 */
public class UserListByPhoneAction extends BaseAction {

    @Autowired
    UserDetailService userDetailService;

    List<UserDetail> userDetailList = new ArrayList<UserDetail>();

    String remoteAddress;
    String phone;
    String email;

    @DefaultHandler
    public Resolution pre() {

        remoteAddress = getContext().getRequest().getRemoteHost();
        User customer = null;
        Long userPhone = 0L;
        if (getContext().getRequest().getParameterMap().containsKey("phone")){
            phone = getContext().getRequest().getParameter("phone");
            userPhone = Long.parseLong(phone);
        }
        userDetailList = userDetailService.findByPhone(userPhone);
        return new ForwardResolution("/pages/admin/userDetail.jsp");
    }

    public Resolution searchByEmail(){
        return new ForwardResolution("/pages/admin/agentSearchOrder.jsp").addParameter("key",UserDetailService.AUTH_KEY)
                .addParameter("phone", phone);
    }

    public String getRemoteAddress() {
        return remoteAddress;
    }

    public void setRemoteAddress(String remoteAddress) {
        this.remoteAddress = remoteAddress;
    }

    public List<UserDetail> getUserDetailList() {
        return userDetailList;
    }

    public void setUserDetailList(List<UserDetail> userDetailList) {
        this.userDetailList = userDetailList;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
