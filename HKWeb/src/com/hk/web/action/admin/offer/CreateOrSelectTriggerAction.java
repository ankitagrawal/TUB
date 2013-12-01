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
import com.hk.domain.offer.OfferTrigger;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = { PermissionConstants.CREATE_OFFER }, authActionBean = AdminPermissionAction.class)
@Component
public class CreateOrSelectTriggerAction extends BaseAction {

    @Validate(on = "select", required = true)
    OfferTrigger       offerTriggerSelect;

    List<OfferTrigger> offerTriggerList;
    List<ProductGroup> productGroupList;

    @ValidateNestedProperties( { @Validate(field = "description", required = true, on = "create") })
    OfferTrigger       offerTriggerCreate;


    @After(stages = LifecycleStage.BindingAndValidation)
    public void populateOnError() {
        pre(); // populate the lists in case of validation failure
    }

    @ValidationMethod(on = "create")
    public void validateCreateTrigger() {
        if (offerTriggerCreate.getAmount() == null && offerTriggerCreate.getQty() == null) {
            getContext().getValidationErrors().add("e1", new SimpleError("Either Qty or Amount is required"));
        }
        if (offerTriggerCreate.getQty() != null && offerTriggerCreate.getProductGroup() == null) {
            getContext().getValidationErrors().add("e2", new SimpleError("productGroup is required when qty is specified"));
        }
    }

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        offerTriggerList = getBaseDao().getAll(OfferTrigger.class);
        productGroupList = getBaseDao().getAll(ProductGroup.class);
        return new ForwardResolution("/pages/admin/offer/createOrSelectTrigger.jsp");
    }

    public Resolution create() {
        offerTriggerCreate = (OfferTrigger) getBaseDao().save(offerTriggerCreate);
        return new RedirectResolution(CreateOrSelectOfferActionAction.class).addParameter("offerTrigger", offerTriggerCreate);
    }

    public Resolution select() {
        return new RedirectResolution(CreateOrSelectOfferActionAction.class).addParameter("offerTrigger", offerTriggerSelect);
    }

    public OfferTrigger getOfferTriggerSelect() {
        return offerTriggerSelect;
    }

    public void setOfferTriggerSelect(OfferTrigger offerTriggerSelect) {
        this.offerTriggerSelect = offerTriggerSelect;
    }

    public OfferTrigger getOfferTriggerCreate() {
        return offerTriggerCreate;
    }

    public void setOfferTriggerCreate(OfferTrigger offerTriggerCreate) {
        this.offerTriggerCreate = offerTriggerCreate;
    }

    public List<OfferTrigger> getOfferTriggerList() {
        return offerTriggerList;
    }

    public List<ProductGroup> getProductGroupList() {
        return productGroupList;
    }


}
