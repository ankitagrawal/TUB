package com.hk.web.action.admin.inventory;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.cycleCount.CycleCountItem;
import com.hk.domain.cycleCount.CycleCount;
import com.hk.domain.warehouse.Warehouse;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.core.JSONObject;
import com.hk.admin.pact.service.inventory.CycleCountService;
import com.hk.pact.service.inventory.SkuGroupService;
import com.hk.pact.service.UserService;
import com.hk.constants.inventory.EnumCycleCountStatus;
import com.hk.constants.inventory.EnumAuditStatus;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;


import java.util.*;
import java.lang.reflect.Type;

import net.sourceforge.stripes.action.Resolution;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Jan 10, 2013
 * Time: 10:09:56 PM
 * To change this template use File | Settings | File Templates.
 */

public class CycleCountAction extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(CycleCountAction.class);

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
	private Map<String, String> hkBarcodeErrorsMap = new HashMap<String, String>();
	private Map<Long, Integer> cycleCountPVImap = new HashMap<Long, Integer>();
	private String cycleCountPVImapString;
	private Long cycleCountStatus;

	public Resolution directToCycleCount() {
		if (cycleCount != null && cycleCount.getId() != null) {
			cycleCount.setAuditStatus(EnumCycleCountStatus.InProgress.getId());
		} else {
			cycleCount.setCreateDate(new Date());
			cycleCount.setAuditStatus(EnumCycleCountStatus.Start.getId());
			cycleCount.setUser(userService.getLoggedInUser());
		}
		cycleCount = cycleCountService.save(cycleCount);
		return pre();

	}


	@DefaultHandler
	public Resolution pre() {
		setHkBarcode(null);
		int newEntry = 0; // if map size changes , then only change Map json
		cycleCountItems = cycleCountService.getAllCycleCountItem();
		//convert
		if (cycleCountPVImapString != null) {
			Type type = new TypeToken<Map<Long, Integer>>() {
			}.getType();
			Gson gson = new Gson();
			cycleCountPVImap = gson.fromJson(cycleCountPVImapString, type);
		}
		for (CycleCountItem cycleCountItem : cycleCountItems) {
			if (!(cycleCountPVImap.containsKey(cycleCountItem.getId()))) {
				newEntry++;
				List<SkuItem> skuItemList = skuGroupService.getInStockSkuItems(cycleCountItem.getSkuGroup());
				int pvi = 0;
				if (skuItemList != null) {
					pvi = skuItemList.size();
				}
				cycleCountPVImap.put(cycleCountItem.getId(), pvi);
			}
		}
		if (cycleCountPVImap != null && cycleCountPVImap.size() > 0) {
			if (newEntry > 0) {
				StringBuilder stringBuilder = new StringBuilder();
				JSONObject.appendJSONMap(cycleCountPVImap, stringBuilder);
				cycleCountPVImapString = stringBuilder.toString();
			}
		}
		return new ForwardResolution("/pages/admin/cycleCount.jsp");
	}

	public Resolution saveScanned() {
		message = null;
		if ((hkBarcode != null) && (!(StringUtils.isEmpty(hkBarcode.trim())))) {
			String brand = cycleCount.getBrandsToAudit().getBrand();
			List<SkuGroup> validSkuGroupList = findSkuGroup(brand, hkBarcode);
			CycleCountItem validCycleCountItem = null;
			SkuGroup validSkuGroup = null;
			if (validSkuGroupList.size() > 0) {
				if (cycleCountPVImapString != null) {
					Type type = new TypeToken<Map<Long, Integer>>() {
					}.getType();
					Gson gson = new Gson();
					cycleCountPVImap = gson.fromJson(cycleCountPVImapString, type);
				}

				for (SkuGroup skuGroup : validSkuGroupList) {
					CycleCountItem cycleCountItemFromDb = cycleCountService.getCycleCountItem(skuGroup);
					if (cycleCountItemFromDb == null) {
						validSkuGroup = skuGroup;
						break;
					} else {

						int pvi = cycleCountPVImap.get(cycleCountItemFromDb.getId());
						if ((cycleCountItemFromDb.getScannedQty().intValue()) < pvi) {
							cycleCountItemFromDb.setScannedQty(cycleCountItemFromDb.getScannedQty().intValue() + 1);
							validCycleCountItem = cycleCountItemFromDb;
							break;
						} else {
							if ((validSkuGroupList.indexOf(skuGroup)) == (validSkuGroupList.size() - 1)) {
								cycleCountItemFromDb.setScannedQty(cycleCountItemFromDb.getScannedQty().intValue() + 1);
								validCycleCountItem = cycleCountItemFromDb;
							}

						}

					}
				}

				if (validCycleCountItem == null) {
					validCycleCountItem = new CycleCountItem();
					validCycleCountItem.setSkuGroup(validSkuGroup);
					validCycleCountItem.setCycleCount(cycleCount);
					validCycleCountItem.setScannedQty(1);
					validCycleCountItem = cycleCountService.save(validCycleCountItem);
					List<SkuItem> skuItemList = skuGroupService.getInStockSkuItems(validSkuGroup);
					int pvi = 0;
					if (skuItemList != null) {
						pvi = skuItemList.size();
					}
					cycleCountPVImap.put(validCycleCountItem.getId(), pvi);
				} else {
					cycleCountService.save(validCycleCountItem);
				}


			} else {
				hkBarcodeErrorsMap.put(hkBarcode, message);
			}
		}
		return new RedirectResolution(CycleCountAction.class).addParameter("message", message).addParameter("cycleCount", cycleCount.getId()).addParameter("cycleCountPVImapString", cycleCountPVImapString);
	}

	private List<SkuGroup> findSkuGroup(String brand, String hkBarcode) {
		Warehouse warehouse = userService.getWarehouseForLoggedInUser();
		List<SkuGroup> skuGroupFromDb = skuGroupService.getSkuGroup(hkBarcode.trim(), warehouse);
		List<SkuGroup> skuGroupListResult = new ArrayList<SkuGroup>();
		if (skuGroupFromDb != null && skuGroupFromDb.size() > 0) {
			for (SkuGroup skuGroupCheckBrand : skuGroupFromDb) {
				if (skuGroupCheckBrand.getSku().getProductVariant().getProduct().getBrand().equals(brand)) {
					skuGroupListResult.add(skuGroupCheckBrand);
				} else {
					message = "Scanned Product does not belong to brand ::" + brand;
					return skuGroupListResult;
				}

			}
		} else {
			message = "Invalid Hk Barcode";
		}
		return skuGroupListResult;
	}


	public Resolution save() {
		if (cycleCount.getAuditStatus().equals(EnumCycleCountStatus.InProgress.getId())) {
			cycleCount.setAuditStatus(EnumCycleCountStatus.PendingForApproval.getId());
			cycleCount = cycleCountService.save(cycleCount);
		}
		cycleCount = getCycleCount();
		cycleCountItems = cycleCountService.getAllCycleCountItem();
		for (CycleCountItem cycleCountItem : cycleCountItems) {
			List<SkuItem> skuItemList = skuGroupService.getInStockSkuItems(cycleCountItem.getSkuGroup());
			int pvi = 0;
			if (skuItemList != null) {
				pvi = skuItemList.size();
			}
			Integer variance = pvi - cycleCountItem.getScannedQty().intValue();
			cycleCountPVImap.put(cycleCountItem.getId(), variance);
		}
		return new ForwardResolution("/pages/admin/CycleCountVariance.jsp");
	}

	public Resolution saveVariance() {
		for (CycleCountItem cycleCountItem : cycleCountItems) {
			cycleCountService.save(cycleCountItem);
		}
		cycleCount.setAuditStatus(EnumCycleCountStatus.Approved.getId());
		cycleCount.getBrandsToAudit().setAuditStatus(EnumAuditStatus.Done.getId());
		cycleCount = cycleCountService.save(cycleCount);
		return new RedirectResolution(CycleCountAction.class, "save").addParameter("cycleCount", cycleCount.getId());
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

	public Map<Long, Integer> getCycleCountPVImap() {
		return cycleCountPVImap;
	}

	public void setCycleCountPVImap(Map<Long, Integer> cycleCountPVImap) {
		this.cycleCountPVImap = cycleCountPVImap;
	}

	public Map<String, String> getHkBarcodeErrorsMap() {
		return hkBarcodeErrorsMap;
	}

	public void setHkBarcodeErrorsMap(Map<String, String> hkBarcodeErrorsMap) {
		this.hkBarcodeErrorsMap = hkBarcodeErrorsMap;
	}

	public String getCycleCountPVImapString() {
		return cycleCountPVImapString;
	}

	public void setCycleCountPVImapString(String cycleCountPVImapString) {
		this.cycleCountPVImapString = cycleCountPVImapString;
	}

	public Long getCycleCountStatus() {
		return cycleCountStatus;
	}

	public void setCycleCountStatus(Long cycleCountStatus) {
		this.cycleCountStatus = cycleCountStatus;
	}
}
