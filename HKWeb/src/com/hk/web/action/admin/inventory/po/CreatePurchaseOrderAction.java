package com.hk.web.action.admin.inventory.po;

import java.util.Date;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;


import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.impl.dao.inventory.PurchaseOrderDao;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.inventory.EnumPurchaseOrderStatus;
import com.hk.dao.BaseDao;
import com.hk.dao.user.UserDao;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.core.PurchaseOrderStatus;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.user.User;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = { PermissionConstants.PO_MANAGEMENT }, authActionBean = AdminPermissionAction.class)
@Component
public class CreatePurchaseOrderAction extends BaseAction {

    private static Logger logger = Logger.getLogger(CreatePurchaseOrderAction.class);

    @Autowired
    PurchaseOrderDao      purchaseOrderDao;
    @Autowired
    BaseDao               baseDao;
    @Autowired
    UserDao               userDao;

    private Supplier      supplier;
    private PurchaseOrder purchaseOrder;

    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/createPurchaseOrder.jsp");
    }

    public Resolution save() {

        User user = null;
        if (getPrincipal() != null) {
            user = getUserDao().getUserById(getPrincipal().getId());
        }
        purchaseOrder.setCreateDate(new Date());
        purchaseOrder.setCreatedBy(user);
        purchaseOrder.setCreatedBy(user);
        purchaseOrder.setUpdateDate(new Date());
        purchaseOrder.setPurchaseOrderStatus(getBaseDao().get(PurchaseOrderStatus.class, EnumPurchaseOrderStatus.Generated.getId()));
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

    public UserDao getUserDao() {
        return userDao;
    }

    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }
    
    
}