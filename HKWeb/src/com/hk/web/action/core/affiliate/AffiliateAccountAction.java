package com.hk.web.action.core.affiliate;

import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.LocalizableError;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;


import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.RoleConstants;
import com.hk.dao.CheckDetailsDao;
import com.hk.dao.affiliate.AffiliateDao;
import com.hk.dao.user.UserDao;
import com.hk.domain.CheckDetails;
import com.hk.domain.affiliate.Affiliate;
import com.hk.domain.user.User;
import com.hk.manager.AffiliateManager;
import com.hk.web.action.core.auth.LogoutAction;

@Secure(hasAnyRoles = {RoleConstants.HK_AFFILIATE, RoleConstants.ADMIN})
@Component
public class AffiliateAccountAction extends BaseAction {

  Affiliate affiliate;
  Double affiliateAccountAmount;
  List<CheckDetails> checkDetailsList;

  private static Logger logger = Logger.getLogger(AffiliateAccountAction.class);
   AffiliateDao affiliateDao;
   AffiliateManager affiliateManager;
   CheckDetailsDao checkDetailsDao;

  @DefaultHandler
  @DontValidate
  public Resolution affiliateAccount() {
    if (getPrincipal() != null) {
      logger.debug("principal" + getPrincipal().getId());
      User user = getUserService().getUserById(getPrincipal().getId());
      affiliate = affiliateDao.getAffilateByUser(user);
      if (user != null)
        logger.debug("user name : " + user.getName());
      if (affiliate != null) {
        logger.debug("affiliate id : " + affiliate.getId());
        affiliateAccountAmount = affiliateManager.getAmountInAccount(affiliate);
      } else {
        logger.debug("affiliate is null");
        addValidationError("e1", new LocalizableError("/Login.action.user.notFound"));
        return new ForwardResolution(LogoutAction.class);
      }
    } else {
      logger.debug("no principal.....");
    }
    return new ForwardResolution("/pages/affiliate/affiliateProfile.jsp");
  }

  public Resolution checksToAffiliate() {
    if (getPrincipal() != null) {
      User user = getUserService().getUserById(getPrincipal().getId());
      affiliate = affiliateDao.getAffilateByUser(user);
      checkDetailsList = checkDetailsDao.getCheckListByAffiliate(affiliate);
    }
    return new ForwardResolution("/pages/affiliate/checksToAffiliate.jsp");
  }

  public Resolution saveAffiliatePreferences() {
    if (affiliate != null) {
      affiliateDao.save(affiliate);
      addRedirectAlertMessage(new SimpleMessage("your preferences have been saved."));
    }
    return new RedirectResolution(AffiliateAccountAction.class);
  }

  public Affiliate getAffiliate() {
    return affiliate;
  }

  public void setAffiliate(Affiliate affiliate) {
    this.affiliate = affiliate;
  }

  public Double getAffiliateAccountAmount() {
    return affiliateAccountAmount;
  }

  public void setAffiliateAccountAmount(Double affiliateAccountAmount) {
    this.affiliateAccountAmount = affiliateAccountAmount;
  }

  public List<CheckDetails> getCheckDetailsList() {
    return checkDetailsList;
  }

  public void setCheckDetailsList(List<CheckDetails> checkDetailsList) {
    this.checkDetailsList = checkDetailsList;
  }
}
