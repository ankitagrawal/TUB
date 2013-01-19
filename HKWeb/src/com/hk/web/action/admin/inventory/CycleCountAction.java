package com.hk.web.action.admin.inventory;


import com.akube.framework.stripes.action.BasePaginatedAction;

import com.akube.framework.dao.Page;
import com.akube.framework.util.FormatUtils;
import com.hk.domain.cycleCount.CycleCountItem;
import com.hk.domain.cycleCount.CycleCount;
import com.hk.domain.warehouse.Warehouse;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.core.JSONObject;

import com.hk.domain.user.User;
import com.hk.admin.pact.service.inventory.CycleCountService;
import com.hk.admin.util.helper.CycleCountHelper;
import com.hk.pact.service.inventory.SkuGroupService;
import com.hk.pact.service.UserService;
import com.hk.constants.inventory.EnumCycleCountStatus;
import com.hk.constants.inventory.EnumAuditStatus;
import com.hk.constants.core.Keys;
import com.hk.util.FtlUtils;
import com.google.gson.reflect.TypeToken;
import com.google.gson.Gson;


import java.util.*;

import java.lang.reflect.Type;
import java.io.*;
import java.text.SimpleDateFormat;

import net.sourceforge.stripes.action.*;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Jan 10, 2013
 * Time: 10:09:56 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class CycleCountAction extends BasePaginatedAction {

	private static Logger logger = LoggerFactory.getLogger(CycleCountAction.class);

	@Autowired
	CycleCountService cycleCountService;
	@Autowired
	SkuGroupService skuGroupService;
	@Autowired
	UserService userService;
	@Autowired
	CycleCountHelper cycleCountHelper;

	@Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
	String adminDownloadsPath;

	private List<CycleCountItem> cycleCountItems;
	private List<CycleCount> cycleCountList;
	private CycleCount cycleCount;
	private String hkBarcode;
	private String message;
	private Map<String, String> hkBarcodeErrorsMap = new HashMap<String, String>();
	private Map<Long, Integer> cycleCountPVImap = new HashMap<Long, Integer>();
//	private Map<Long, Integer> scannedPviVariance = new HashMap<Long, Integer>();
	private String cycleCountPVImapString;
	private boolean error = false;
	private Page cyceCountPage;
	private String brand;
	private Warehouse warehouse;
	private String auditorLogin;
	private Date startDate;
	private Date endDate;
	private Integer defaultPerPage = 20;
	FileBean fileBean;

	public Resolution directToCycleCount() {

		if (cycleCount.getId() == null) {
			cycleCount.setCreateDate(new Date());
			cycleCount.setUser(userService.getLoggedInUser());
			cycleCount.setCycleStatus(EnumCycleCountStatus.InProgress.getId());
			cycleCount = cycleCountService.save(cycleCount);
		}

		return view();

	}

	@DefaultHandler
	public Resolution pre() {
		User loggedOnUser = getPrincipalUser();
		if (warehouse == null) warehouse = loggedOnUser.getSelectedWarehouse();
		User auditor = null;
		if (StringUtils.isNotBlank(auditorLogin)) {
			auditor = getUserService().findByLogin(auditorLogin);
		}
		cyceCountPage = cycleCountService.searchCycleList(brand, warehouse.getId(), auditor, startDate, endDate, getPageNo(), getPerPage());
		if (cyceCountPage != null) {
			cycleCountList = cyceCountPage.getList();
		}
		return new ForwardResolution("/pages/admin/cycleCountList.jsp");

	}


	public Resolution view() {
		//Make null ,to clear field in case of forward request 
		setHkBarcode(null);
		cycleCountItems = cycleCount.getCycleCountItems();
		if (cycleCountPVImapString != null) {
			cycleCountPVImap = getMapFromJsonString(cycleCountPVImapString);
		}
		cycleCountItems = cycleCount.getCycleCountItems();
		int newEntryInMap  = 0;
		for (CycleCountItem cycleCountItem : cycleCountItems) {
			if (!(cycleCountPVImap.containsKey(cycleCountItem.getId()))) {
				//new entry added by another auditor
				newEntryInMap++;
				List<SkuItem> skuItemList = skuGroupService.getInStockSkuItems(cycleCountItem.getSkuGroup());
				int pvi = 0;
				if (skuItemList != null) {
					pvi = skuItemList.size();
				}
				cycleCountPVImap.put(cycleCountItem.getId(), pvi);
			}
		}
		if (cycleCountPVImap != null && cycleCountPVImap.size() > 0) {
			if (newEntryInMap > 0) {
				cycleCountPVImapString = getStringFromMap(cycleCountPVImap);
			}
		}

		return new ForwardResolution("/pages/admin/cycleCount.jsp");
	}

	public Resolution saveScanned() {
		message = null;
		error = false;
		if ((hkBarcode != null) && (!(StringUtils.isEmpty(hkBarcode.trim())))) {
			String brand = cycleCount.getBrandsToAudit().getBrand();
			List<SkuGroup> validSkuGroupList = findSkuGroup(brand, hkBarcode);
			CycleCountItem validCycleCountItem = null;
			SkuGroup validSkuGroup = null;
			if (validSkuGroupList.size() > 0) {
				if (cycleCountPVImapString != null) {
					cycleCountPVImap = getMapFromJsonString(cycleCountPVImapString);
				}
				for (SkuGroup skuGroup : validSkuGroupList) {
					CycleCountItem cycleCountItemFromDb = cycleCountService.getCycleCountItem(cycleCount, skuGroup);
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
					cycleCountPVImapString = getStringFromMap(cycleCountPVImap);
				} else {
					cycleCountService.save(validCycleCountItem);
				}


			} else {
				hkBarcodeErrorsMap.put(hkBarcode, message);
			}
		}
		if (message == null) {
			message = "Sucessfully Scanned for " + hkBarcode;
		}
		return new RedirectResolution(CycleCountAction.class, "view").addParameter("message", message).addParameter("cycleCount", cycleCount.getId())
				.addParameter("cycleCountPVImapString", cycleCountPVImapString).addParameter("error", error);
	}

	private List<SkuGroup> findSkuGroup(String brand, String hkBarcode) {
		Warehouse warehouse = userService.getWarehouseForLoggedInUser();
		List<SkuGroup> skuGroupFromDb = skuGroupService.getSkuGroup(hkBarcode.trim(), warehouse.getId());
		List<SkuGroup> skuGroupListResult = new ArrayList<SkuGroup>();
		if (skuGroupFromDb != null && skuGroupFromDb.size() > 0) {
			for (SkuGroup skuGroupCheckBrand : skuGroupFromDb) {
				if (skuGroupCheckBrand.getSku().getProductVariant().getProduct().getBrand().equals(brand)) {
					skuGroupListResult.add(skuGroupCheckBrand);
				} else {
					error = true;
					message =  hkBarcode + "  does not belong to brand ::" + brand;
					return skuGroupListResult;
				}

			}
		} else {
			error = true;
			message = "Invalid Hk Barcode ::" + hkBarcode;
		}
		return skuGroupListResult;
	}


	public Resolution save() {
		if (cycleCount.getCycleStatus().equals(EnumCycleCountStatus.InProgress.getId())) {
			cycleCount.setCycleStatus(EnumCycleCountStatus.RequestForApproval.getId());
			cycleCount = cycleCountService.save(cycleCount);
		}
		cycleCount = getCycleCount();
		cycleCountItems = cycleCount.getCycleCountItems();
		populateScannedPviVarianceMap(cycleCountItems);
		return new ForwardResolution("/pages/admin/CycleCountVariance.jsp");
	}

	private void populateScannedPviVarianceMap(List<CycleCountItem> cycleCountItems) {
		cycleCountPVImap = new HashMap<Long,Integer>();
		for (CycleCountItem cycleCountItem : cycleCountItems) {
			List<SkuItem> skuItemList = skuGroupService.getInStockSkuItems(cycleCountItem.getSkuGroup());
			int pvi = 0;
			if (skuItemList != null) {
				pvi = skuItemList.size();
			}
			cycleCountPVImap.put(cycleCountItem.getId(), pvi);
		}

	}

	public Resolution saveVariance() {
		for (CycleCountItem cycleCountItem : cycleCountItems) {
			cycleCountService.save(cycleCountItem);
		}
		cycleCount.setCycleStatus(EnumCycleCountStatus.Approved.getId());
		cycleCount = cycleCountService.save(cycleCount);
		return new RedirectResolution(CycleCountAction.class, "save").addParameter("cycleCount", cycleCount.getId());
	}

	public Resolution closeCycleCount() {
		cycleCount.setCycleStatus(EnumCycleCountStatus.Closed.getId());
		cycleCount.getBrandsToAudit().setAuditStatus(EnumAuditStatus.Done.getId());
		cycleCount = cycleCountService.save(cycleCount);
		return new RedirectResolution(CycleCountAction.class, "pre");
	}


	public Resolution generateReconAddExcel() {
		List<CycleCountItem> cycleCountItems = cycleCount.getCycleCountItems();
		List<CycleCountItem> cycleCountItemsForAddRecon = new ArrayList<CycleCountItem>();
		populateScannedPviVarianceMap(cycleCountItems);
		for (CycleCountItem cycleCountItem : cycleCountItems) {
			int pvi = cycleCountPVImap.get(cycleCountItem.getId());
			int scannedQty = cycleCountItem.getScannedQty();
			if ((pvi-scannedQty) <  0) {
				cycleCountItemsForAddRecon.add(cycleCountItem);
			}
		}
		Date todayDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String excelFilePath = adminDownloadsPath + "/cycleCountExcelFiles/" + "CycleCount_" + cycleCount.getId() + "_RvAdd" + sdf.format(todayDate) + ".xls";
		final File excelFile = new File(excelFilePath);
		cycleCountHelper.generateReconVoucherAddExcel(cycleCountItemsForAddRecon, excelFile, cycleCountPVImap);
		return cycleCountHelper.download();

	}


	public Resolution generateCompleteCycleExcel() {
		List<CycleCountItem> cycleCountItems = cycleCount.getCycleCountItems();
		populateScannedPviVarianceMap(cycleCountItems);
		Date todayDate = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String excelFilePath = adminDownloadsPath + "/cycleCountExcelFiles/" + "CompleteCycleCount_" + cycleCount.getId() + "_Variance" + sdf.format(todayDate) + ".xls";
		final File excelFile = new File(excelFilePath);
		cycleCountHelper.generateCompleteCycleCountExcel(cycleCountItems, excelFile, cycleCountPVImap);
		return cycleCountHelper.download();
	}



	//cycle count by uploading notepad.
	public Resolution uploadCycleCountNotepad() {
		if (fileBean == null) {
			addRedirectAlertMessage(new SimpleMessage("Choose File"));
			return new ForwardResolution("/pages/admin/cycleCount.jsp");
		}
		try {
			hkBarcodeErrorsMap = new HashMap<String,String>();
			String brand = cycleCount.getBrandsToAudit().getBrand();
			String excelFilePath = adminDownloadsPath + "/cycleCountExcelFiles/" + System.currentTimeMillis() + ".txt";
			File excelFile = new File(excelFilePath);
			excelFile.getParentFile().mkdirs();
			fileBean.save(excelFile);
			Map<String, Integer> hkBarcodeQtyMap = cycleCountHelper.readCycleCountNotepad(excelFile);
			cycleCountPVImap = new HashMap<Long,Integer>();
			for (String hkbarcodeFromNotepad : hkBarcodeQtyMap.keySet()) {
				List<SkuGroup> validSkuGroupList = findSkuGroup(brand, hkbarcodeFromNotepad);
				if (validSkuGroupList != null && validSkuGroupList.size() > 0) {
					int notepadScannedQty = hkBarcodeQtyMap.get(hkbarcodeFromNotepad);
					for (SkuGroup skuGroup : validSkuGroupList) {
						List<SkuItem> skuItemList = skuGroupService.getInStockSkuItems(skuGroup);
						CycleCountItem cycleCountItemFromDb = cycleCountService.getCycleCountItem(cycleCount, skuGroup);
						int pviQty = 0;
						if (skuItemList != null && skuItemList.size() > 0) {
							pviQty = skuItemList.size();
						}
						if (cycleCountItemFromDb == null) {
							CycleCountItem cycleCountItemNew = new CycleCountItem();
							cycleCountItemNew.setSkuGroup(skuGroup);
							cycleCountItemNew.setCycleCount(cycleCount);
							if (pviQty > 0) {
								if (notepadScannedQty >= pviQty) {
									cycleCountItemNew.setScannedQty(pviQty);
									notepadScannedQty = notepadScannedQty - pviQty;
								} else {
									if ((validSkuGroupList.indexOf(skuGroup)) == (validSkuGroupList.size() - 1)) {
										cycleCountItemNew.setScannedQty(notepadScannedQty);
									}
								}


							} else {
								if ((validSkuGroupList.indexOf(skuGroup)) == (validSkuGroupList.size() - 1)) {
									cycleCountItemNew.setScannedQty(notepadScannedQty);
								}
							}
							cycleCountItemNew = cycleCountService.save(cycleCountItemNew);
							cycleCountPVImap.put(cycleCountItemNew.getId(), pviQty);
						} else {
							int alreadySavedScannedQty = cycleCountItemFromDb.getScannedQty();
							int fillPviQty = pviQty - alreadySavedScannedQty;
							if (fillPviQty > 0) {
								if (notepadScannedQty >= fillPviQty) {
									cycleCountItemFromDb.setScannedQty(pviQty);
									notepadScannedQty = notepadScannedQty - fillPviQty;
								} else {
									if ((validSkuGroupList.indexOf(skuGroup)) == (validSkuGroupList.size() - 1)) {
										cycleCountItemFromDb.setScannedQty(notepadScannedQty + alreadySavedScannedQty);
									}
								}


							} else {
								if ((validSkuGroupList.indexOf(skuGroup)) == (validSkuGroupList.size() - 1)) {
									cycleCountItemFromDb.setScannedQty(notepadScannedQty + alreadySavedScannedQty);
								}
							}

							cycleCountService.save(cycleCountItemFromDb);
						}


					}

				} else {
					hkBarcodeErrorsMap.put(hkbarcodeFromNotepad, message);
				}

			}
			if (hkBarcodeErrorsMap.size() > 0) {
				error = true;
				message = "";
				String newLine = FormatUtils.lineSeperator;
				for (String hkBarcode : hkBarcodeErrorsMap.keySet()) {

					message = message + hkBarcodeErrorsMap.get(hkBarcode) + newLine;
				}
			}
		} catch (IOException e) {
			addRedirectAlertMessage(new SimpleMessage(e.getMessage()));
			return new ForwardResolution("/pages/admin/cycleCount.jsp");
		}
		return new RedirectResolution(CycleCountAction.class, "view").addParameter("cycleCount", cycleCount.getId()).addParameter("message", message).addParameter("error",error);
	}



	private String getStringFromMap(Map<Long, Integer> cycleCountPVImap) {
		StringBuilder stringBuilder = new StringBuilder();
		JSONObject.appendJSONMap(cycleCountPVImap, stringBuilder);
		return stringBuilder.toString();
	}

	private Map<Long, Integer> getMapFromJsonString(String jsonString) {
		Type type = new TypeToken<Map<Long, Integer>>() {
		}.getType();
		Gson gson = new Gson();
		Map<Long, Integer> mapFromString = gson.fromJson(jsonString, type);
		return mapFromString;

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

	public boolean getError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}



	public void setFileBean(FileBean fileBean) {
		this.fileBean = fileBean;
	}

	public int getPerPageDefault() {
		return defaultPerPage;
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public String getAuditorLogin() {
		return auditorLogin;
	}

	public void setAuditorLogin(String auditorLogin) {
		this.auditorLogin = auditorLogin;
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


	public int getPageCount() {
		return cyceCountPage == null ? 0 : cyceCountPage.getTotalPages();
	}

	public int getResultCount() {
		return cyceCountPage == null ? 0 : cyceCountPage.getTotalResults();
	}

	public Set<String> getParamSet() {
		HashSet<String> params = new HashSet<String>();
		params.add("brand");
		params.add("warehouse");
		params.add("auditorLogin");
		params.add("startDate");
		params.add("endDate");
		return params;
	}


	public List<CycleCount> getCycleCountList() {
		return cycleCountList;
	}

	public void setCycleCountList(List<CycleCount> cycleCountList) {
		this.cycleCountList = cycleCountList;
	}
}
