package com.hk.web.action.admin.offer;

import java.util.List;

import net.sourceforge.stripes.action.After;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.controller.LifecycleStage;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidateNestedProperties;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.catalog.product.ProductGroup;
import com.hk.domain.offer.OfferAction;
import com.hk.domain.offer.OfferTrigger;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = { PermissionConstants.CREATE_OFFER }, authActionBean = AdminPermissionAction.class)
@Component
public class CreateOrSelectOfferActionAction extends BaseAction {

    OfferTrigger       offerTrigger;

    @Validate(required = true, on = "select")
    OfferAction        offerActionSelect;

    @ValidateNestedProperties( { @Validate(field = "description", required = true, on = "create") })
    OfferAction        offerActionCreate;

    List<OfferAction>  offerActionList;
    List<ProductGroup> productGroupList;


    @After(stages = LifecycleStage.BindingAndValidation)
    public void popoulateOnError() {
        pre();
    }

    @ValidationMethod(on = "create")
    public void validateCreate() {
        if (offerActionCreate.getDiscountPercentOnHkPrice() == null && offerActionCreate.getOrderLevelDiscountAmount() == null
                && offerActionCreate.getRewardPointDiscountPercent() == null) {
            getContext().getValidationErrors().add("e1", new SimpleError("one of 'dicount on hkPrice' or 'order level discount' or 'reward point discount percent' is required"));
        }
    }

    @DontValidate
    @DefaultHandler
    public Resolution pre() {
        offerActionList = getBaseDao().getAll(OfferAction.class);
        productGroupList = getBaseDao().getAll(ProductGroup.class);
        return new ForwardResolution("/pages/admin/offer/createOrSelectOffer.jsp");
    }

    public Resolution select() {
        return new RedirectResolution(CreateOfferAction.class).addParameter("offerTrigger", offerTrigger).addParameter("offerAction", offerActionSelect);
    }

    public Resolution create() {
        offerActionCreate = (OfferAction) getBaseDao().save(offerActionCreate);
        return new RedirectResolution(CreateOfferAction.class).addParameter("offerTrigger", offerTrigger).addParameter("offerAction", offerActionCreate);
    }

    public OfferTrigger getOfferTrigger() {
        return offerTrigger;
    }

    public void setOfferTrigger(OfferTrigger offerTrigger) {
        this.offerTrigger = offerTrigger;
    }

    public OfferAction getOfferActionSelect() {
        return offerActionSelect;
    }

    public void setOfferActionSelect(OfferAction offerActionSelect) {
        this.offerActionSelect = offerActionSelect;
    }

    public OfferAction getOfferActionCreate() {
        return offerActionCreate;
    }

    public void setOfferActionCreate(OfferAction offerActionCreate) {
        this.offerActionCreate = offerActionCreate;
    }

    public List<OfferAction> getOfferActionList() {
        return offerActionList;
    }

    public List<ProductGroup> getProductGroupList() {
        return productGroupList;
    }


}
