package com.hk.web.action.admin.offer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.offer.Offer;
import com.hk.pact.dao.offer.OfferDao;
import com.hk.web.action.error.AdminPermissionAction;

@Component
public class SelectOfferAction extends BasePaginatedAction {

    private Page        offerPage;

    @Validate(required = true)
    private Offer       offer;

    private List<Offer> offers = new ArrayList<Offer>();
    @Autowired
    OfferDao            offerDao;

    @DefaultHandler
    @DontValidate
    @Secure(hasAnyPermissions = { PermissionConstants.VIEW_OFFER }, authActionBean = AdminPermissionAction.class)
    public Resolution pre() {
        offerPage = offerDao.listAll(getPageNo(), getPerPage());
        offers = offerPage.getList();
        return new ForwardResolution("/pages/admin/offer/selectOffer.jsp");
    }

    @Secure(hasAnyPermissions = { PermissionConstants.UPDATE_OFFER }, authActionBean = AdminPermissionAction.class)
    public Resolution selectOffer() {
        if (offer.getEndDate() == null || offer.getEndDate().after(new Date())) {
            return new RedirectResolution(CreateCouponAction.class).addParameter("offer", offer.getId());
        }
        addRedirectAlertMessage(new SimpleMessage("offer has expired"));
        return new RedirectResolution(SelectOfferAction.class);
    }

    @Secure(hasAnyPermissions = { PermissionConstants.UPDATE_OFFER }, authActionBean = AdminPermissionAction.class)
    public Resolution editOffer() {
        return new RedirectResolution(EditOfferAction.class).addParameter("offer", offer.getId());
    }

    public int getPerPageDefault() {
        return 30;
    }

    public int getPageCount() {
        return offerPage != null ? offerPage.getTotalPages() : 0;
    }

    public int getResultCount() {
        return offerPage != null ? offerPage.getTotalResults() : 0;
    }

    public Set<String> getParamSet() {
        return null;
    }

    public List<Offer> getOffers() {
        return offers;
    }

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }
}
