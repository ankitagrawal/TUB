package com.hk.web.action.admin.inventory;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.cycleCount.CycleCountItem;
import com.hk.domain.cycleCount.CycleCount;
import com.hk.domain.warehouse.Warehouse;
import com.hk.domain.sku.SkuGroup;
import com.hk.admin.pact.service.inventory.CycleCountService;
import com.hk.pact.service.inventory.SkuGroupService;
import com.hk.pact.service.UserService;
import com.hk.constants.inventory.EnumCycleCountStatus;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;

import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.RedirectResolution;
import bsh.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Jan 10, 2013
 * Time: 10:09:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class CycleCountAction extends BaseAction {

	@Autowired
	CycleCountService cycleCountService;
	@Autowired
	SkuGroupService skuGroupService;
	@Autowired
	UserService userService;


	private List<CycleCountItem> cycleCountItems;
	private CycleCount cycleCount;
	private String hkBarcode;
	private String message;
	private boolean error;


	public Resolution pre() {
		cycleCount.setCreateDate(new Date());
		cycleCount.setAuditStatus(EnumCycleCountStatus.Approved.getId());
		cycleCount.setUser(userService.getLoggedInUser());
		cycleCount = cycleCountService.save(cycleCount);
		return new RedirectResolution("/pages/admin/cycleCount.jsp");
	}

	public Resolution saveScanned() {
		if ((hkBarcode != null) && (!(StringUtils.isEmpty(hkBarcode.trim())))) {
			String brand = cycleCount.getBrandsToAudit().getBrand();
			List<SkuGroup> validSkuGroupList = findSkuGroup(brand, hkBarcode);
			if (validSkuGroupList.size() > 0) {
				if (validSkuGroupList.size() == 1) {
					SkuGroup validSkuGroup = validSkuGroupList.get(0);
					CycleCountItem cycleCountItem = new CycleCountItem();
					cycleCountItem.setSkuGroup(validSkuGroup);
					if(cycleCountItems.contains(cycleCountItem)){
						
					}
				} else {

				}

			}

		}
		return new RedirectResolution("/pages/admin/cycleCount.jsp");
	}

	private List<SkuGroup> findSkuGroup(String brand, String hkBarcode) {
		Warehouse warehouse = userService.getWarehouseForLoggedInUser();
		SkuGroup skuGroup = skuGroupService.getSkuGroup(hkBarcode.trim(), warehouse);
		List<SkuGroup> skuGroupListResult = new ArrayList<SkuGroup>();
		if (skuGroup != null) {
			if (skuGroup.getSku().getProductVariant().getProduct().getBrand().equals(brand)) {
				skuGroupListResult.add(skuGroup);
				return skuGroupListResult;
			} else {
				message = "Scanned Product does not belong to brand ::" + brand;
				return skuGroupListResult;
			}
		} else {
			List<SkuGroup> skuGroupListFromDb = skuGroupService.getSkuGroupsByBatch(hkBarcode, warehouse);

			if (skuGroupListFromDb != null && skuGroupListFromDb.size() > 0) {
				for (SkuGroup groupCheckForBrand : skuGroupListFromDb) {
					if (!(groupCheckForBrand.getSku().getProductVariant().getProduct().getBrand().equals(brand))) {
						message = "Scanned Product does not belong to brand ::" + brand;
						continue;
					}
					skuGroupListResult.add(groupCheckForBrand);
				}
				return skuGroupListResult;
			} else {
				message = "Invalid Hk Bracode";
			}
		}

		return skuGroupListResult;
	}

	public Resolution save() {


		return null;
	}


	public List<CycleCountItem> getCycleCountItems() {
		return cycleCountItems;
	}

	public void setCycleCountItems(List<CycleCountItem> cycleCountItems) {
		this.cycleCountItems = cycleCountItems;
	}

	public String getHkBarcode() {
		return hkBarcode;
	}

	public void setHkBarcode(String hkBarcode) {
		this.hkBarcode = hkBarcode;
	}

	public CycleCount getCycleCount() {
		return cycleCount;
	}

	public void setCycleCount(CycleCount cycleCount) {
		this.cycleCount = cycleCount;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}
}
