package com.hk.web.action.admin.pos;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.pact.service.pos.POSReportService;
import com.hk.admin.pact.service.pos.POSService;
import com.hk.constants.core.Keys;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.dto.pos.PosProductSearchDto;
import com.hk.dto.pos.PosSkuGroupDto;
import com.hk.dto.pos.PosSkuGroupSearchDto;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductService;
import com.hk.util.io.HkXlsWriter;
import com.hk.web.HealthkartResponse;
import net.sourceforge.stripes.action.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 7/18/13
 * Time: 6:39 PM
 * To change this template use File | Settings | File Templates.
 */
public class PosProductSearchAction extends BaseAction {

	private static Logger logger = Logger.getLogger(PosProductSearchAction.class);
	private String brand;
	private String flavor;
	private String size;
	private String color;
	private String form;
	private String productName;
	private List<PosProductSearchDto> posProductSearchDtoList;
	private Sku searchSku;
	private List<SkuGroup> skuGroupList;
	private String primaryCategory;
	private String productVariantId;

	@Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
	String adminDownloads;
	File xlsFile;

	@Autowired
	private UserService userService;
	@Autowired
	private POSService posService;
	@Autowired
	private ProductService productService;
	@Autowired
	private AdminInventoryService adminInventoryService;
	@Autowired
	private POSReportService posReportService;

	@DefaultHandler
	public Resolution pre() {
		return new ForwardResolution("/pages/pos/posProductSearch.jsp");
	}

	public Resolution search() {
		if (userService.getWarehouseForLoggedInUser() == null) {
			addRedirectAlertMessage(new SimpleMessage("Please select a warehouse"));
			return new ForwardResolution("/pages/pos/posProductSearch.jsp");
		}
		posProductSearchDtoList = posService.searchProductInStore(productVariantId, primaryCategory, productName, brand, flavor, size, color, form, userService.getWarehouseForLoggedInUser().getId());
		return new ForwardResolution("/pages/pos/posProductSearch.jsp");
	}

	@JsonHandler
	public Resolution showBatches() {
		DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
		Map dataMap = new HashMap();
		if (searchSku == null) {
			addRedirectAlertMessage(new SimpleMessage("Please select an item"));
			return new ForwardResolution("/pages/pos/posProductSearch.jsp");
		}
		skuGroupList = adminInventoryService.getInStockSkuGroup(searchSku);
		if (skuGroupList == null || skuGroupList.size() == 0) {
			HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "No batch found", dataMap);
			noCache();
			return new JsonResolution(healthkartResponse);
		}
		List<PosSkuGroupDto> posSkuGroupDtoList = new ArrayList<PosSkuGroupDto>(0);
		for (SkuGroup skuGroup : skuGroupList) {
			PosSkuGroupDto posSkuGroupDto = new PosSkuGroupDto();
			if (skuGroup.getGoodsReceivedNote() == null) {
				posSkuGroupDto.setGrnId(null);
			} else {
				posSkuGroupDto.setGrnId(skuGroup.getGoodsReceivedNote().getId());
			}

			posSkuGroupDto.setBatchNumber(skuGroup.getBatchNumber());
			posSkuGroupDto.setCostPrice(skuGroup.getCostPrice());
			posSkuGroupDto.setMrp(skuGroup.getMrp());
			if (skuGroup.getMfgDate() == null) {
				posSkuGroupDto.setMfgDate("");
			} else {
				posSkuGroupDto.setMfgDate(dateFormat.format(skuGroup.getMfgDate()));
			}

			if (skuGroup.getExpiryDate() == null) {
				posSkuGroupDto.setExpiryDate("");
			} else {
				posSkuGroupDto.setExpiryDate(dateFormat.format(skuGroup.getExpiryDate()));
			}

			posSkuGroupDto.setCheckInDate(dateFormat.format(skuGroup.getCreateDate()));
			posSkuGroupDto.setInStockQty(adminInventoryService.getInStockSkuItems(skuGroup).size());
			posSkuGroupDto.setCheckedInQty(skuGroup.getSkuItems().size());
			posSkuGroupDtoList.add(posSkuGroupDto);
		}
		dataMap.put("skuGroupList", posSkuGroupDtoList);
		HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Valid Sku Groups", dataMap);
		noCache();
		return new JsonResolution(healthkartResponse);
	}

	public Resolution downloadBatches() {
		List<PosSkuGroupSearchDto> posSkuGroupSearchDtoList = posService.searchBatchesInStore(productVariantId, primaryCategory, productName, brand, flavor, size, color, form, userService.getWarehouseForLoggedInUser().getId());

		xlsFile = new File(adminDownloads + "/reports/PosStockReport.xls");
		xlsFile = posReportService.generatePosStockReport(xlsFile, posSkuGroupSearchDtoList);

		addRedirectAlertMessage(new SimpleMessage("Download complete"));
		return new HTTPResponseResolution();
	}

	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public String getFlavor() {
		return flavor;
	}

	public void setFlavor(String flavor) {
		this.flavor = flavor;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public List<PosProductSearchDto> getPosProductSearchDtoList() {
		return posProductSearchDtoList;
	}

	public void setPosProductSearchDtoList(List<PosProductSearchDto> posProductSearchDtoList) {
		this.posProductSearchDtoList = posProductSearchDtoList;
	}

	public Sku getSearchSku() {
		return searchSku;
	}

	public void setSearchSku(Sku searchSku) {
		this.searchSku = searchSku;
	}

	public List<SkuGroup> getSkuGroupList() {
		return skuGroupList;
	}

	public void setSkuGroupList(List<SkuGroup> skuGroupList) {
		this.skuGroupList = skuGroupList;
	}

	public String getPrimaryCategory() {
		return primaryCategory;
	}

	public void setPrimaryCategory(String primaryCategory) {
		this.primaryCategory = primaryCategory;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getForm() {
		return form;
	}

	public void setForm(String form) {
		this.form = form;
	}

	public String getProductVariantId() {
		return productVariantId;
	}

	public void setProductVariantId(String productVariantId) {
		this.productVariantId = productVariantId;
	}

	public class HTTPResponseResolution implements Resolution {
		public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
			OutputStream out = null;
			InputStream in = new BufferedInputStream(new FileInputStream(xlsFile));
			res.setContentLength((int) xlsFile.length());
			res.setHeader("Content-Disposition", "attachment; filename=\"" + xlsFile.getName() + "\";");
			out = res.getOutputStream();

			// Copy the contents of the file to the output stream
			byte[] buf = new byte[4096];
			int count = 0;
			while ((count = in.read(buf)) >= 0) {
				out.write(buf, 0, count);
			}
		}

	}

}
