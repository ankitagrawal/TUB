package com.hk.web.action.admin.inventory;

import com.hk.domain.sku.SkuItemStatus;
import com.hk.pact.service.UserService;
import com.hk.pact.service.inventory.SkuGroupService;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.sku.SkuItemOwner;
import com.hk.web.action.error.AdminPermissionAction;

import java.util.List;
import java.util.ArrayList;

@Secure(hasAnyPermissions = {PermissionConstants.INVENTORY_CHECKIN}, authActionBean = AdminPermissionAction.class)
public class SearchHKBatchAction extends BaseAction {

    private static Logger logger = Logger.getLogger(SearchHKBatchAction.class);


    @Autowired
    SkuGroupService skuGroupService;
    @Autowired
    UserService userService;

    @Validate(required = true)
    private String hkBarcode;

    private List<SkuGroup> skuGroupList = new ArrayList<SkuGroup>();
    private SkuItem skuItemBarcode;


    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/searchHKBatch.jsp");
    }

    public Resolution showBatchInfo() {
        logger.debug("upc: " + hkBarcode);
        List<SkuItemStatus> skuItemStatusList = new ArrayList<SkuItemStatus>();
        List<SkuItemOwner> skuItemOwners = new ArrayList<SkuItemOwner>();
        if (StringUtils.isNotBlank(hkBarcode)) {
            skuItemBarcode = skuGroupService.getSkuItemByBarcode(hkBarcode, userService.getWarehouseForLoggedInUser().getId(), skuItemStatusList, skuItemOwners);
            if (skuItemBarcode != null) {
                skuGroupList.add(skuItemBarcode.getSkuGroup());
            } else {
                skuGroupList = skuGroupService.getSkuGroup(hkBarcode, userService.getWarehouseForLoggedInUser().getId());
            }
            return new ForwardResolution("/pages/admin/searchHKBatch.jsp");
        } else {
            addRedirectAlertMessage(new SimpleMessage("Invalid HK Barcode."));
            return new RedirectResolution(SearchHKBatchAction.class);
        }
    }

    public String getHkBarcode() {
        return hkBarcode;
    }

    public void setHkBarcode(String hkBarcode) {
        this.hkBarcode = hkBarcode;
    }

    public List<SkuGroup> getSkuGroupList() {
        return skuGroupList;
    }

    public void setSkuGroupList(List<SkuGroup> skuGroupList) {
        this.skuGroupList = skuGroupList;
    }

    public SkuItem getSkuItemBarcode() {
        return skuItemBarcode;
    }

    public void setSkuItemBarcode(SkuItem skuItemBarcode) {
        this.skuItemBarcode = skuItemBarcode;
    }
}