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
import com.hk.dto.user.B2BUserFilterDto;
import com.hk.pact.dao.user.B2BUserDao;
import com.hk.pact.service.clm.KarmaProfileService;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = {PermissionConstants.SEARCH_USERS}, authActionBean = AdminPermissionAction.class)
@Component
public class SearchB2BUserAction extends BasePaginatedAction {
   
    @Autowired
    private B2BUserDao userDao;

    @Autowired
    private KarmaProfileService karmaProfileService;

  @Session(key = HealthkartConstants.Session.b2bUserSearchFilterKey)
  private B2BUserFilterDto userFilterDto;
  private Page userPage;
  private List<User> userList;

  @DefaultHandler
  @DontValidate
  public Resolution pre() {
    userFilterDto = null;
    return new ForwardResolution("/pages/admin/searchB2BUser.jsp");
  }

  @ValidationMethod
  public void validate() {
    if(userFilterDto == null)
      userFilterDto = new B2BUserFilterDto();
  }

  public Resolution search() {
    userPage = userDao.search(userFilterDto, getPageNo(), getPerPage());
    userList = userPage.getList();
    return new ForwardResolution("/pages/admin/searchB2BUser.jsp");
  }
    
  public B2BUserFilterDto getUserFilterDto() {
    return userFilterDto;
  }

  public void setUserFilterDto(B2BUserFilterDto userFilterDto) {
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
