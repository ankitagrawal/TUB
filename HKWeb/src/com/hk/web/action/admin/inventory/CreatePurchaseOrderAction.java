package com.hk.web.action.admin.inventory;

import java.util.Date;
import java.util.Calendar;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.dao.inventory.PurchaseOrderDao;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.inventory.EnumPurchaseOrderStatus;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.core.PurchaseOrderStatus;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.user.User;
import com.hk.pact.dao.BaseDao;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = { PermissionConstants.PO_MANAGEMENT }, authActionBean = AdminPermissionAction.class)
@Component
public class CreatePurchaseOrderAction extends BaseAction {

    //private static Logger logger = Logger.getLogger(CreatePurchaseOrderAction.class);

    @Autowired
    PurchaseOrderDao      purchaseOrderDao;
    @Autowired
    BaseDao               baseDao;

    private Supplier      supplier;
    private PurchaseOrder purchaseOrder;

    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/createPurchaseOrder.jsp");
    }

    public Resolution save() {

        User user = null;
        if (getPrincipal() != null) {
            user = getUserService().getUserById(getPrincipal().getId());
        }
        purchaseOrder.setCreateDate(new Date());
        purchaseOrder.setCreatedBy(user);
        purchaseOrder.setUpdateDate(new Date());
        purchaseOrder.setPurchaseOrderStatus(getBaseDao().get(PurchaseOrderStatus.class, EnumPurchaseOrderStatus.Generated.getId()));

	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(new Date());
	    calendar.add(Calendar.DATE, purchaseOrder.getSupplier().getLeadTime());
	    purchaseOrder.setEstDelDate(calendar.getTime());
	    if (purchaseOrder.getSupplier().getCreditDays() != null && purchaseOrder.getSupplier().getCreditDays() >= 0) {
		    calendar.add(Calendar.DATE, purchaseOrder.getSupplier().getCreditDays());
		    purchaseOrder.setEstPaymentDate(calendar.getTime());
	    } else {
		    purchaseOrder.setEstPaymentDate(new Date());
	    }

        purchaseOrder = (PurchaseOrder) getBaseDao().save(purchaseOrder);

        addRedirectAlertMessage(new SimpleMessage("Changes saved."));
        return new RedirectResolution(EditPurchaseOrderAction.class).addParameter("purchaseOrder", purchaseOrder.getId());
    }

    public PurchaseOrder getPurchaseOrder() {
        return purchaseOrder;
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
    }

    public void setPurchaseOrder(PurchaseOrder purchaseOrder) {
        this.purchaseOrder = purchaseOrder;
    }

    public PurchaseOrderDao getPurchaseOrderDao() {
        return purchaseOrderDao;
    }

    public void setPurchaseOrderDao(PurchaseOrderDao purchaseOrderDao) {
        this.purchaseOrderDao = purchaseOrderDao;
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

    
}