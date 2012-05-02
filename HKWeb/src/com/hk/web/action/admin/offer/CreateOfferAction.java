package com.hk.web.action.admin.offer;

import java.util.Date;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;


import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.util.DateUtils;
import com.hk.constants.core.PermissionConstants;
import com.hk.dao.offer.OfferDao;
import com.hk.domain.offer.Offer;
import com.hk.domain.offer.OfferAction;
import com.hk.domain.offer.OfferTrigger;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = {PermissionConstants.CREATE_OFFER}, authActionBean = AdminPermissionAction.class)
@Component
public class CreateOfferAction extends BaseAction {

  OfferTrigger offerTrigger;

  @Validate(required = true)
  OfferAction offerAction;

  @Validate(required = true, maxlength = 200)
  String description;

  @Validate(required = true)
  Date startDate;

  Date endDate;

  String offerIdentifier;

  boolean excludeTriggerProducts;

  String terms;


   OfferDao offerDao;

  @DefaultHandler
  @DontValidate
  public Resolution pre() {
    return new ForwardResolution("/pages/admin/offer/createOffer.jsp");
  }

  public Resolution create() {
    Offer offer = new Offer();
    offer.setOfferTrigger(offerTrigger);
    offer.setOfferAction(offerAction);
    offer.setDescription(description);
    offer.setStartDate(DateUtils.getStartOfDay(startDate));
    offer.setEndDate(DateUtils.getEndOfDay(endDate));
    offer.setExcludeTriggerProducts(excludeTriggerProducts);
    offer.setOfferIdentifier(offerIdentifier);
    offer.setTerms(terms);
    offer = (Offer) offerDao.save(offer);

    addRedirectAlertMessage(new SimpleMessage("Offer [{0}] successfully created", description));
    return new RedirectResolution(OfferAdminAction.class);
  }

  public OfferTrigger getOfferTrigger() {
    return offerTrigger;
  }

  public void setOfferTrigger(OfferTrigger offerTrigger) {
    this.offerTrigger = offerTrigger;
  }

  public OfferAction getOfferAction() {
    return offerAction;
  }

  public void setOfferAction(OfferAction offerAction) {
    this.offerAction = offerAction;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public Date getStartDate() {
    return startDate;
  }

  public void setStartDate(Date startDate) {
    this.startDate = startDate;
  }

  public Date getEndDate() {
    return endDate;
  }

  public void setEndDate(Date endDate) {
    this.endDate = endDate;
  }

  public boolean isExcludeTriggerProducts() {
    return excludeTriggerProducts;
  }

  public void setExcludeTriggerProducts(boolean excludeTriggerProducts) {
    this.excludeTriggerProducts = excludeTriggerProducts;
  }

  public String getOfferIdentifier() {
    return offerIdentifier;
  }

  public void setOfferIdentifier(String offerIdentifier) {
    this.offerIdentifier = offerIdentifier;
  }

  public String getTerms() {
    return terms;
  }

  public void setTerms(String terms) {
    this.terms = terms;
  }
}
