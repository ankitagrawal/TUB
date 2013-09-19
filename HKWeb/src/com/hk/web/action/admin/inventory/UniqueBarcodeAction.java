package com.hk.web.action.admin.inventory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.util.BarcodeUtil;
import com.hk.constants.core.Keys;
import com.hk.constants.core.RoleConstants;
import com.hk.constants.courier.StateList;
import com.hk.constants.sku.EnumSkuItemOwner;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.sku.SkuItemOwner;
import com.hk.domain.sku.SkuItemStatus;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.sku.SkuGroupDao;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.inventory.SkuGroupService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.web.action.admin.inventory.InventoryCheckinAction.HTTPResponseResolution;
import com.hk.web.action.error.AdminPermissionAction;

@Component
@Secure(hasAnyRoles = { RoleConstants.WH_MANAGER_L1 }, authActionBean = AdminPermissionAction.class)
public class UniqueBarcodeAction extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(UniqueBarcodeAction.class);

	@Autowired
	ProductVariantService productVariantService;
	@Autowired
	SkuGroupService skuGroupService;
	@Autowired
	SkuItemDao skuItemDao;
	@Autowired
	BaseDao baseDao;
	@Autowired
	AdminInventoryService adminInventoryService;
	@Autowired
	UserService userService;
	@Autowired
	SkuService skuService;
	@Autowired
	SkuGroupDao skuGroupDao;

	String productVariantId;
	File printBarcode;
	List<SkuGroup> skuGroups;
	List<SkuItem> skuItems;
	HashMap<SkuGroup, Long> skuGroupQty;
	HashMap<SkuGroup, Long> uniquelyBarcodedskuGroupQty;
	List<Long> skuGroupIds;
	Long skuGroupId;
	Warehouse userWarehouse;

	@Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
	String adminUploadsPath;

	@Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
	String adminDownloadsPath;

	@Value("#{hkEnvProps['" + Keys.Env.barcodeGurgaon + "']}")
	String barcodeGurgaon;

	@Value("#{hkEnvProps['" + Keys.Env.barcodeMumbai + "']}")
	String barcodeMumbai;

	@Validate(required = true, on = "parse")
	private FileBean fileBean;

	@DefaultHandler
	public Resolution pre() {
		if (getUserService().getWarehouseForLoggedInUser() != null) {
			userWarehouse = userService.getWarehouseForLoggedInUser();
		} else {
			addRedirectAlertMessage(new SimpleMessage("There is no warehouse attached with the logged in user. Please select one."));
			return new ForwardResolution("/pages/admin/uniqueBarcode.jsp");
		}
		return new ForwardResolution("/pages/admin/uniqueBarcode.jsp");
	}

	public Resolution searchBatchesForPV() {
		ProductVariant productVariant = productVariantService.getVariantById(productVariantId);
		if(productVariant==null){
			addRedirectAlertMessage(new SimpleMessage("Please enter a valid variant id"));
			return new RedirectResolution(UniqueBarcodeAction.class);
		}
		Sku sku = skuService.findSKU(productVariant, userWarehouse);
		if(sku==null){
			addRedirectAlertMessage(new SimpleMessage("No sku found for the given variant"));
			return new RedirectResolution(UniqueBarcodeAction.class);
		}
		skuGroupQty = new HashMap<SkuGroup, Long>();
		uniquelyBarcodedskuGroupQty = new HashMap<SkuGroup, Long>();
		List<SkuItemOwner> skuItemOwnerList = new ArrayList<SkuItemOwner>();
		List<SkuItemStatus> skuItemStatuses = new ArrayList<SkuItemStatus>();
		skuItemStatuses.add(EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
		skuItemStatuses.add(EnumSkuItemStatus.TEMP_BOOKED.getSkuItemStatus());
		skuItemStatuses.add(EnumSkuItemStatus.Checked_OUT.getSkuItemStatus());
		skuItemOwnerList.add(EnumSkuItemOwner.SELF.getSkuItemOwnerStatus());

		skuGroups = skuGroupDao.getAllCheckedInBatchesWithBarcode(sku);
		if (skuGroups != null && skuGroups.size() > 0) {
			for (SkuGroup skuGroup : skuGroups) {
				skuItems = skuItemDao.getInStockSkuItems(skuGroup, skuItemStatuses, skuItemOwnerList);
				if (skuItems != null && skuItems.size() > 0) {
					if (!skuItems.get(0).getBarcode().startsWith(BarcodeUtil.BARCODE_SKU_ITEM_PREFIX)) {
						skuGroupQty.put(skuGroup, (long) skuItems.size());
					}
					else{
						uniquelyBarcodedskuGroupQty.put(skuGroup, (long) skuItems.size());
					}
				}
			}
		}
		return new ForwardResolution("/pages/admin/uniqueBarcode.jsp");
	}

	public Resolution makeUniqueBarcode() {
		ProductVariant productVariant = productVariantService.getVariantById(productVariantId);
		Sku sku = skuService.findSKU(productVariant, userWarehouse);
		skuGroups = skuGroupDao.getAllCheckedInBatchesWithBarcode(sku);
		if (skuGroups != null && skuGroups.size() > 0) {
			for (SkuGroup skuGroup : skuGroups) {
				List<SkuItemOwner> skuItemOwnerList = new ArrayList<SkuItemOwner>();
				List<SkuItemStatus> skuItemStatuses = new ArrayList<SkuItemStatus>();
				skuItemStatuses.add(EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
				skuItemStatuses.add(EnumSkuItemStatus.TEMP_BOOKED.getSkuItemStatus());
				skuItemStatuses.add(EnumSkuItemStatus.Checked_OUT.getSkuItemStatus());
				skuItemOwnerList.add(EnumSkuItemOwner.SELF.getSkuItemOwnerStatus());

				skuItems = skuItemDao.getInStockSkuItems(skuGroup, skuItemStatuses, skuItemOwnerList);
				if (skuItems != null && skuItems.size() > 0) {
					int i = 1;
					if (!skuItems.get(0).getBarcode().startsWith(BarcodeUtil.BARCODE_SKU_ITEM_PREFIX)) {
						for (SkuItem skuItem : skuItems) {
							String barcode = BarcodeUtil.generateBarCodeForSKuItem(skuItem.getSkuGroup().getId(), i);
							skuItem.setBarcode(barcode);
							skuItem = (SkuItem) baseDao.save(skuItem);
							++i;
						}
					}
				}
			}
		}
		return new RedirectResolution(UniqueBarcodeAction.class).addParameter("searchBatchesForPV").addParameter("productVariantId", productVariantId)
				.addParameter("userWarehouse", sku.getWarehouse().getId());
	}

	public Resolution downloadBarcode() {
		ProductVariant productVariant = productVariantService.getVariantById(productVariantId);
		SkuGroup skuGroup = baseDao.get(SkuGroup.class, skuGroupId);
		List<SkuItemOwner> skuItemOwnerList = new ArrayList<SkuItemOwner>();
		List<SkuItemStatus> skuItemStatuses = new ArrayList<SkuItemStatus>();
		skuItemStatuses.add(EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
		skuItemStatuses.add(EnumSkuItemStatus.TEMP_BOOKED.getSkuItemStatus());
		skuItemStatuses.add(EnumSkuItemStatus.Checked_OUT.getSkuItemStatus());
		skuItemOwnerList.add(EnumSkuItemOwner.SELF.getSkuItemOwnerStatus());

		skuItems = skuItemDao.getInStockSkuItems(skuGroup, skuItemStatuses, skuItemOwnerList);
		if (skuItems == null || skuItems.size() < 1) {
			addRedirectAlertMessage(new SimpleMessage(" No Checked In SkuItem Found "));
			return new RedirectResolution(UniqueBarcodeAction.class).addParameter("searchBatchesForPV").addParameter("productVariantId", productVariantId);
		}
		Map<Long, String> skuItemDataMap = adminInventoryService.skuItemBarcodeMap(skuItems);

		String barcodeFilePath = null;
		Warehouse userWarehouse = null;
		if (getUserService().getWarehouseForLoggedInUser() != null) {
			userWarehouse = userService.getWarehouseForLoggedInUser();
		} else {
			addRedirectAlertMessage(new SimpleMessage("There is no warehouse attached with the logged in user. Please check with the admin."));
			return new RedirectResolution(UniqueBarcodeAction.class).addParameter("searchBatchesForPV").addParameter("productVariantId", productVariantId);
		}
		if (userWarehouse.getState().equalsIgnoreCase(StateList.HARYANA)) {
			barcodeFilePath = barcodeGurgaon;
		} else {
			barcodeFilePath = barcodeMumbai;
		}
		barcodeFilePath = barcodeFilePath + "/" + "printBarcode_" + "_" + productVariant.getId() + "_" + StringUtils.substring(userWarehouse.getCity(), 0, 3)
				+ ".txt";

		try {
			printBarcode = BarcodeUtil.createBarcodeFileForSkuItem(barcodeFilePath, skuItemDataMap);
		} catch (IOException e) {
			logger.error("Exception while appending on barcode file", e);
		}
		addRedirectAlertMessage(new SimpleMessage("Print Barcodes downloaded Successfully."));
		return new HTTPResponseResolution();

	}

	public class HTTPResponseResolution implements Resolution {
		public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
			InputStream in = new BufferedInputStream(new FileInputStream(printBarcode));
			res.setContentType("text/plain");
			res.setCharacterEncoding("UTF-8");
			res.setContentLength((int) printBarcode.length());
			res.setHeader("Content-Disposition", "attachment; filename=\"" + printBarcode.getName() + "\";");
			OutputStream out = res.getOutputStream();

			// Copy the contents of the file to the output stream
			byte[] buf = new byte[4096];
			int count = 0;
			while ((count = in.read(buf)) >= 0) {
				out.write(buf, 0, count);
			}
			in.close();
			out.flush();
			out.close();
		}

	}
	
	public Resolution back(){
		return new RedirectResolution(UniqueBarcodeAction.class);
	}

	public String getProductVariantId() {
		return productVariantId;
	}

	public void setProductVariantId(String productVariantId) {
		this.productVariantId = productVariantId;
	}

	public List<SkuGroup> getSkuGroups() {
		return skuGroups;
	}

	public void setSkuGroups(List<SkuGroup> skuGroups) {
		this.skuGroups = skuGroups;
	}

	public List<SkuItem> getSkuItems() {
		return skuItems;
	}

	public void setSkuItems(List<SkuItem> skuItems) {
		this.skuItems = skuItems;
	}

	public HashMap<SkuGroup, Long> getSkuGroupQty() {
		return skuGroupQty;
	}

	public void setSkuGroupQty(HashMap<SkuGroup, Long> skuGroupQty) {
		this.skuGroupQty = skuGroupQty;
	}

	public List<Long> getSkuGroupIds() {
		return skuGroupIds;
	}

	public void setSkuGroupIds(List<Long> skuGroupIds) {
		this.skuGroupIds = skuGroupIds;
	}

	public Long getSkuGroupId() {
		return skuGroupId;
	}

	public void setSkuGroupId(Long skuGroupId) {
		this.skuGroupId = skuGroupId;
	}

	public Warehouse getUserWarehouse() {
		return userWarehouse;
	}

	public void setUserWarehouse(Warehouse userWarehouse) {
		this.userWarehouse = userWarehouse;
	}

	public HashMap<SkuGroup, Long> getUniquelyBarcodedskuGroupQty() {
		return uniquelyBarcodedskuGroupQty;
	}

	public void setUniquelyBarcodedskuGroupQty(HashMap<SkuGroup, Long> uniquelyBarcodedskuGroupQty) {
		this.uniquelyBarcodedskuGroupQty = uniquelyBarcodedskuGroupQty;
	}
}
