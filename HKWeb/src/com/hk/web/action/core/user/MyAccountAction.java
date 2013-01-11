package com.hk.web.action.core.user;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.SimpleError;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.util.BaseUtils;
import com.hk.cache.RoleCache;
import com.hk.constants.core.EnumRole;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.user.Address;
import com.hk.domain.user.B2bUserDetails;
import com.hk.domain.user.Role;
import com.hk.domain.user.User;
import com.hk.manager.UserManager;
import com.hk.pact.dao.RoleDao;
import com.hk.pact.dao.affiliate.AffiliateDao;
import com.hk.pact.dao.core.AddressDao;
import com.hk.pact.dao.user.B2bUserDetailsDao;
import com.hk.pact.dao.user.UserDao;
import com.hk.pact.service.RoleService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Secure(hasAnyRoles = {RoleConstants.HK_USER, RoleConstants.HK_UNVERIFIED}, disallowRememberMe = true)
public class MyAccountAction extends BaseAction {
  private static Logger logger = LoggerFactory.getLogger(MyAccountAction.class);

  User user;
  Affiliate affiliate;
  Address affiliateDefaultAddress;
  B2bUserDetails b2bUserDetails;

  String oldPassword;
  String newPassword;
  String confirmPassword;

  @Autowired
  AddressDao addressDao;
  @Autowired
  UserDao userDao;
  @Autowired
  AffiliateDao affiliateDao;
  @Autowired
  B2bUserDetailsDao b2bUserDetailsDao;
  @Autowired
  UserManager userManager;
  @Autowired
  RoleDao roleDao;
  
  /*@Autowired
  private RoleService roleService;*/

  @DefaultHandler
  public Resolution pre() {
    if (getPrincipal() != null) {
      user = getUserService().getUserById(getPrincipal().getId());
      affiliate = affiliateDao.getAffilateByUser(user);
      if (affiliate != null && affiliate.getMainAddressId() != null) {
        affiliateDefaultAddress = addressDao.get(Address.class,affiliate.getMainAddressId());
      }
      b2bUserDetails = b2bUserDetailsDao.getB2bUserDetails(user);
    }
    noCache();
    return new ForwardResolution("/pages/userProfile.jsp");
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
  }

  public String getOldPassword() {
    return oldPassword;
  }

  public void setOldPassword(String oldPassword) {
    this.oldPassword = oldPassword;
  }

  public String getNewPassword() {
    return newPassword;
  }

  public void setNewPassword(String newPassword) {
    this.newPassword = newPassword;
  }

  public String getConfirmPassword() {
    return confirmPassword;
  }

  public void setConfirmPassword(String confirmPassword) {
    this.confirmPassword = confirmPassword;
  }

  public Resolution editBasicInformation() {
    if(!getPrincipalUser().getId().equals(user.getId())){
        logger.debug("user with id"+getPrincipal().getId()+" just tried to access information of user with id" + user.getId());
        addRedirectAlertMessage(new SimpleMessage("please don't mess up with other users in our system"));
        noCache();
        return new RedirectResolution(getContext().getSourcePage());
    }
    logger.debug("Editing basic information for " + user.getName());
    noCache();
    return new ForwardResolution("/pages/editBasicInformation.jsp");
  }

  public Resolution saveBasicInformation() {
    if(!user.getId().equals(getPrincipalUser().getId())){
        user=getPrincipalUser();
        addRedirectAlertMessage(new SimpleMessage("Please don't mess up with our system"));
        return new RedirectResolution(getContext().getSourcePage());
    }
    if (user.getName().length() > 80) {
      logger.debug("new user name entered exceeded the allowed limit");
      addRedirectAlertMessage(new SimpleMessage("Please enter a valid name!"));
      return new ForwardResolution("/pages/editBasicInformation.jsp");
    }
    if (!BaseUtils.isValidEmail(user.getEmail())) {
      logger.info("email id  " + user.getEmail() + " invalid!");
      addRedirectAlertMessage(new SimpleMessage("PLEASE ENTER A VALID EMAIL ID!"));
      return new ForwardResolution("/pages/editBasicInformation.jsp");
    }
    user = userDao.save(user);
    Role b2bRole = RoleCache.getInstance().getRoleByName(EnumRole.B2B_USER).getRole();
    if (user.getRoles().contains(b2bRole)) {
      b2bUserDetailsDao.save(b2bUserDetails);
    }

    addRedirectAlertMessage(new SimpleMessage("Your basic information has been updated"));
    return new RedirectResolution(MyAccountAction.class);
  }

  public Resolution editPassword() {
     user=getPrincipalUser();
    logger.debug("Editing password for " + user.getName());
      noCache();
    return new ForwardResolution("/pages/editPassword.jsp");
  }

  public Resolution changePassword() {
    user = getUserService().getUserById(getPrincipal().getId());

    if (StringUtils.equals(user.getPasswordChecksum(), BaseUtils.passwordEncrypt(oldPassword))) {
      user.setPassword(newPassword);
      user.setPasswordChecksum(BaseUtils.passwordEncrypt(newPassword));
      userDao.save(user);
      addRedirectAlertMessage(new SimpleMessage("Password has been updated successfully."));
      return new RedirectResolution(MyAccountAction.class);
    } else
      addValidationError("Error message", new SimpleError("Invalid old password"));
    return new ForwardResolution(MyAccountAction.class);
  }

  public B2bUserDetails getB2bUserDetails() {
    return b2bUserDetails;
  }

  public void setB2bUserDetails(B2bUserDetails b2bUserDetails) {
    this.b2bUserDetails = b2bUserDetails;
  }

  public Affiliate getAffiliate() {
    return affiliate;
  }

  public void setAffiliate(Affiliate affiliate) {
    this.affiliate = affiliate;
  }

  public Address getAffiliateDefaultAddress() {
    return affiliateDefaultAddress;
  }

  public void setAffiliateDefaultAddress(Address affiliateDefaultAddress) {
    this.affiliateDefaultAddress = affiliateDefaultAddress;
  }
}