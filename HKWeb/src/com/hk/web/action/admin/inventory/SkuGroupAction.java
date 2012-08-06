package com.hk.web.action.admin.inventory;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.constants.core.RoleConstants;
import com.hk.domain.sku.SkuGroup;
import com.hk.pact.dao.sku.SkuGroupDao;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import org.apache.log4j.Logger;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.stripesstuff.plugin.security.Secure;

@Secure (hasAnyRoles = {RoleConstants.WH_MANAGER_L1}, authActionBean = AdminPermissionAction.class)
public class SkuGroupAction extends BaseAction {

	private static Logger logger = Logger.getLogger(SkuGroupAction.class);
	private SkuGroup skuGroup;
	private String gatewayOrderId;

	@Autowired
	SkuGroupDao skuGroupDao;

	@DefaultHandler
	public Resolution pre() {
		return new ForwardResolution("/pages/admin/skuGroup.jsp");
	}

	public Resolution save() {
		try {
			if (StringUtils.isBlank(skuGroup.getBatchNumber()) || StringUtils.isEmpty(skuGroup.getBatchNumber())) {
				addRedirectAlertMessage(new SimpleMessage("Batch is required. Plz check."));
				return new RedirectResolution(SkuGroupAction.class).addParameter("skuGroup", skuGroup.getId()).addParameter("gatewayOrderId", gatewayOrderId);
			}
			if (skuGroup.getMrp() != null && skuGroup.getMrp().equals(0.0D)) {
				addRedirectAlertMessage(new SimpleMessage("MRP is required. Plz check."));
				return new RedirectResolution(SkuGroupAction.class).addParameter("skuGroup", skuGroup.getId()).addParameter("gatewayOrderId", gatewayOrderId);
			}
			skuGroupDao.save(skuGroup);
			addRedirectAlertMessage(new SimpleMessage("SkuGroupUpdated."));
			return new RedirectResolution(InventoryCheckoutAction.class).addParameter("gatewayOrderId", gatewayOrderId);
		} catch (Exception e) {

		}
		addRedirectAlertMessage(new SimpleMessage("Some Error. Plz check."));
		return new RedirectResolution(SkuGroupAction.class).addParameter("skuGroup", skuGroup.getId()).addParameter("gatewayOrderId", gatewayOrderId);
	}

	public SkuGroup getSkuGroup() {
		return skuGroup;
	}

	public void setSkuGroup(SkuGroup skuGroup) {
		this.skuGroup = skuGroup;
	}

	public String getGatewayOrderId() {
		return gatewayOrderId;
	}

	public void setGatewayOrderId(String gatewayOrderId) {
		this.gatewayOrderId = gatewayOrderId;
	}
}