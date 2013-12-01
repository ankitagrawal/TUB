package com.hk.web.action.admin.user;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;
import org.stripesstuff.plugin.session.Session;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.constants.core.HealthkartConstants;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.user.User;
import com.hk.dto.user.UserFilterDto;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.service.clm.KarmaProfileService;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = {PermissionConstants.SEARCH_USERS}, authActionBean = AdminPermissionAction.class)
@Component
public class SearchUserAction extends BasePaginatedAction {
   
    @Autowired
    private UserDao userDao;

    @Autowired
    private KarmaProfileService karmaProfileService;

  @Session(key = HealthkartConstants.Session.userSearchFilterKey)
  private UserFilterDto userFilterDto;
  private Page userPage;
  private List<User> userList;

  @DefaultHandler
  @DontValidate
  public Resolution pre() {
    userFilterDto = null;
    return new ForwardResolution("/pages/admin/searchUser.jsp");
  }

  @ValidationMethod
  public void validate() {
    if(userFilterDto == null)
      userFilterDto = new UserFilterDto();
  }

  public Resolution search() {
    userPage = userDao.search(userFilterDto, getPageNo(), getPerPage());
    userList = userPage.getList();
    return new ForwardResolution("/pages/admin/searchUser.jsp");
  }
    
  public UserFilterDto getUserFilterDto() {
    return userFilterDto;
  }

  public void setUserFilterDto(UserFilterDto userFilterDto) {
    this.userFilterDto = userFilterDto;
  }


  public int getPerPageDefault() {
    return 20;
  }

  public int getPageCount() {
    return userPage == null ? 0 : userPage.getTotalPages();
  }

  public int getResultCount() {
    return userPage == null ? 0 : userPage.getTotalResults();
  }

  public Set<String> getParamSet() {
    Set<String> params = new HashSet<String>();
    params.add("userFilterDto.login");
    params.add("userFilterDto.email");
    params.add("userFilterDto.name");
    return params;
  }

  public List<User> getUserList() {
    return userList;
  }

    public KarmaProfileService getKarmaProfileService() {
        return karmaProfileService;
    }

    public void setKarmaProfileService(KarmaProfileService karmaProfileService) {
        this.karmaProfileService = karmaProfileService;
    }
}
