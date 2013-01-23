package com.hk.web.action.admin.pos;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.dto.pos.POSLineItemDto;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.pact.service.pos.POSService;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.user.User;
import com.hk.manager.OrderManager;
import com.hk.pact.service.UserService;
import com.hk.pact.service.order.CartLineItemService;
import com.hk.web.HealthkartResponse;
import net.sourceforge.stripes.action.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 1/21/13
 * Time: 2:02 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class POSAction extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(POSAction.class);
	private String phone;
	private String email;
	private String name;
	private String address;
	private String productVariantBarcode;
	private List<POSLineItemDto> posLineItems = new ArrayList<POSLineItemDto>(0);
	private User customer;

	@Autowired
	UserService userService;
	@Autowired
	AdminInventoryService adminInventoryService;
	@Autowired
	OrderManager orderManager;
	@Autowired
	POSService posService;
	@Autowired
	CartLineItemService cartLineItemService;

	@DefaultHandler
	public Resolution pre() {
		return new ForwardResolution("/pages/pos/pos.jsp");
	}

	@SuppressWarnings("unchecked")
	public Resolution getProductDetailsByBarcode() {
		Map dataMap = new HashMap();
		if (productVariantBarcode != null) {
			List<SkuItem> inStockSkuItemList = adminInventoryService.getInStockSkuItems(productVariantBarcode, userService.getWarehouseForLoggedInUser());
			if(inStockSkuItemList == null || inStockSkuItemList.size() == 0) {
				HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "No item found for this Barcode", dataMap);
				noCache();
				return new JsonResolution(healthkartResponse);
			}
			SkuGroup skuGroup = inStockSkuItemList.get(0).getSkuGroup();
			ProductVariant productVariant = skuGroup.getSku().getProductVariant();
			dataMap.put("skuGroupId", skuGroup.getId());
			dataMap.put("product", productVariant.getProduct().getName());
			dataMap.put("options", productVariant.getOptionsCommaSeparated());
			dataMap.put("mrp", skuGroup.getMrp());
			dataMap.put("offerPrice", productVariant.getHkPrice());
			HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Valid Barcode", dataMap);
			noCache();
			return new JsonResolution(healthkartResponse);

		} else {
			logger.error("null or empty barcode or warehouse Id passed to load pv details in getProductDetailsByBarcode method of POSAction");
		}
		HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Invalid Product VariantID", dataMap);
		noCache();
		return new JsonResolution(healthkartResponse);
	}

	@SuppressWarnings("unchecked")
	public Resolution getCustomerDetailsByLogin() {
		Map dataMap = new HashMap();
		if(!StringUtils.isBlank(email)) {
			User customer = userService.findByLogin(email);
			if(customer != null) {
				dataMap.put("customerName", customer.getName());
				dataMap.put("customer", customer);
				HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Valid Barcode", dataMap);
				noCache();
				return new JsonResolution(healthkartResponse);
			}
		} else {
			logger.error("Blank email is passed in POSAction");
		}
		HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Customer Not found", dataMap);
		noCache();
		return new JsonResolution(healthkartResponse);

	}

	public Resolution confirmOrder() {
		if(posLineItems.size() == 0) {
			addRedirectAlertMessage(new SimpleMessage("Please place order for atleast one item"));
			return new RedirectResolution(POSAction.class).addParameter("posLineItems", posLineItems);
		}
		if(customer == null) {
			customer = posService.createUserForStore(email, name, null, "HK_USER");
		}
		if(customer != null) {
			Order order = posService.createOrderForStore(customer);
			for(POSLineItemDto posLineItemDto : posLineItems) {
				ProductVariant productVariant = posLineItemDto.getSkuGroup().getSku().getProductVariant();
				productVariant.setQty(posLineItemDto.getQty());
				CartLineItem cartLineItem = cartLineItemService.createCartLineItemWithBasicDetails(productVariant, order);
				cartLineItemService.save(cartLineItem);
			}

		}
		return new ForwardResolution("/pages/pos/pos.jsp");
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getProductVariantBarcode() {
		return productVariantBarcode;
	}

	public void setProductVariantBarcode(String productVariantBarcode) {
		this.productVariantBarcode = productVariantBarcode;
	}

	public List<POSLineItemDto> getPosLineItems() {
		return posLineItems;
	}

	public void setPosLineItems(List<POSLineItemDto> posLineItems) {
		this.posLineItems = posLineItems;
	}

	public User getCustomer() {
		return customer;
	}

	public void setCustomer(User customer) {
		this.customer = customer;
	}
}
