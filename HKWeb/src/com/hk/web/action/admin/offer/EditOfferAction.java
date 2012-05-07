package com.hk.web.action.admin.offer;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.offer.Offer;
import com.hk.pact.dao.offer.OfferDao;
import com.hk.web.action.error.AdminPermissionAction;

/**
 * Created by IntelliJ IDEA.
 * User: Pratham
 * Date: Sep 3, 2011
 * Time: 4:13:42 PM
 * To change this template use File | Settings | File Templates.
 */

@Secure(hasAnyPermissions = {PermissionConstants.VIEW_OFFER}, authActionBean = AdminPermissionAction.class)
@Component
public class EditOfferAction extends BaseAction {
  @Validate(required = true)
  private Offer offer;
  @Autowired
   OfferDao offerDao;

  @DontValidate
  @DefaultHandler
  public Resolution pre() {
    return new ForwardResolution("/pages/admin/offer/editOffer.jsp");
  }

  @Secure(hasAnyPermissions = {PermissionConstants.UPDATE_OFFER}, authActionBean = AdminPermissionAction.class)
  public Resolution save() {
    offerDao.save(offer);
    addRedirectAlertMessage(new SimpleMessage("saved sucessfully"));
    return new ForwardResolution("/pages/admin/offer/editOffer.jsp");
  }

  public Offer getOffer() {
    return offer;
  }

  public void setOffer(Offer offer) {
    this.offer = offer;
  }
}
