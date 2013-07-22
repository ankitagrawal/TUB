package com.hk.web.action.admin.pos;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.dao.inventory.AdminSkuItemDao;
import com.hk.admin.pact.service.pos.POSService;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuGroup;
import com.hk.dto.pos.PosProductSearchDto;
import com.hk.pact.service.UserService;
import com.hk.web.action.admin.inventory.ListBatchesAndCheckinInventory;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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

	@Autowired
	private UserService userService;
	@Autowired
	private POSService posService;
	@Autowired
	private AdminSkuItemDao adminSkuItemDao;

	@DefaultHandler
	public Resolution pre() {
		return new ForwardResolution("/pages/pos/posProductSearch.jsp");
	}

	public Resolution search() {
		if (userService.getWarehouseForLoggedInUser() == null) {
			addRedirectAlertMessage(new SimpleMessage("Please select a warehouse"));
			return new ForwardResolution("/pages/pos/posProductSearch.jsp");
		}
		posProductSearchDtoList = posService.searchProductInStore(primaryCategory, productName, brand, flavor, size, color, form, userService.getWarehouseForLoggedInUser().getId());
		return new ForwardResolution("/pages/pos/posProductSearch.jsp");
	}

	public Resolution showBatches() {
		if (searchSku == null) {
			addRedirectAlertMessage(new SimpleMessage("Please select an item"));
			return new ForwardResolution("/pages/pos/posProductSearch.jsp");
		}
		skuGroupList = adminSkuItemDao.getInStockSkuGroups(searchSku);
		return new ForwardResolution("/pages/pos/posSkuBatchDetail.jsp");
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
}
