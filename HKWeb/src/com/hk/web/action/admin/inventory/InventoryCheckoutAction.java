package com.hk.web.action.admin.inventory;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.hk.admin.manager.BinManager;
import com.hk.admin.pact.dao.inventory.AdminProductVariantInventoryDao;
import com.hk.admin.pact.dao.inventory.AdminSkuItemDao;
import com.hk.admin.pact.dao.inventory.BrandsToAuditDao;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.inventory.EnumInvTxnType;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.constants.catalog.category.CategoryConstants;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.SkuGroup;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.user.User;
import com.hk.manager.OrderManager;
import com.hk.pact.dao.catalog.product.ProductVariantDao;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.dao.sku.SkuGroupDao;
import com.hk.pact.service.OrderStatusService;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.ProductVariantService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.web.HealthkartResponse;
import com.hk.web.action.error.AdminPermissionAction;
import com.hk.taglibs.Functions;
import net.sourceforge.stripes.action.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Secure (hasAnyPermissions = {PermissionConstants.INVENTORY_CHECKOUT}, authActionBean = AdminPermissionAction.class)
@Component
public class InventoryCheckoutAction extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(InventoryCheckoutAction.class);

	// UserDao userDao;
	// ProductVariantDao productVariantDao;

	// OrderLifecycleActivityDao orderLifecycleActivityDao;
	// InvTxnTypeDao invTxnTypeDao;
	// OrderStatusDao orderStatusDao;
	// ShippingOrderDao shippingOrderDao;
	@Autowired
	private AdminSkuItemDao skuItemDao;
	@Autowired
	private LineItemDao lineItemDao;
	@Autowired
	private SkuGroupDao skuGroupDao;
	@Autowired
	private ProductVariantDao productVariantDao;
	@Autowired
	private AdminProductVariantInventoryDao adminProductVariantInventoryDao;
	@Autowired
	private ShippingOrderService shippingOrderService;
	@Autowired
	private UserService userService;
	@Autowired
	private ShippingOrderStatusService shippingOrderStatusService;
	@Autowired
	private OrderStatusService orderStatusService;
	@Autowired
	private OrderService orderService;
	@Autowired
	private AdminInventoryService adminInventoryService;
	@Autowired
	private InventoryService inventoryService;
	@Autowired
	private SkuService skuService;
	@Autowired
	private ProductVariantService productVariantService;
	@Autowired
	private OrderManager orderManager;
	@Autowired
	BinManager binManager;
	@Autowired
	BrandsToAuditDao brandsToAuditDao;

	private ShippingOrder shippingOrder;

	private LineItem lineItem;
	private String upc;
	private ProductVariant productVariant;
	private Long qty;
	private SkuGroup skuGroup;
	private String invoiceNumber;
	private String gatewayOrderId;
	//private boolean wronglyPickedBox = false;
	//private SkuGroup earlierSkuGroup;
	List<SkuGroup> skuGroups;

	@DefaultHandler
	public Resolution pre() {
		return new ForwardResolution("/pages/admin/searchOrderAndCheckoutInventory.jsp");
	}

	public Resolution checkout() {
		shippingOrder = getShippingOrderService().findByGatewayOrderId(gatewayOrderId);
		if (shippingOrder == null) {
			addRedirectAlertMessage(new SimpleMessage("No Such Order"));
		} else if (!EnumShippingOrderStatus.SO_Picking.getId().equals(shippingOrder.getOrderStatus().getId())) {
			addRedirectAlertMessage(new SimpleMessage("Order is not in picking cannot proceed to checkout"));
		} else {
			logger.debug("gatewayId: " + shippingOrder.getGatewayOrderId());
			Set<LineItem> pickingLIs = shippingOrder.getLineItems();
			if (pickingLIs != null && !shippingOrder.getLineItems().isEmpty()) {
				Boolean checkedOut = getAdminInventoryService().areAllUnitsOfOrderCheckedOut(shippingOrder);
				if (checkedOut) {
					shippingOrder.setOrderStatus(shippingOrderStatusService.find(EnumShippingOrderStatus.SO_CheckedOut));
					getShippingOrderService().save(shippingOrder);
				}
				return new ForwardResolution("/pages/admin/inventoryCheckout.jsp");
			}
		}
		addRedirectAlertMessage(new SimpleMessage("No Such Order OR Invalid line item status OR All items are checkedout"));
		return new RedirectResolution(InventoryCheckoutAction.class);
	}

	public Resolution findSkuGroups() {
		SkuGroup skuGroupBarcode;
		//earlierSkuGroup = null;
		List<SkuGroup> inStockSkuGroupList;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		logger.debug("gatewayId: " + shippingOrder.getGatewayOrderId());
		logger.debug("upc: " + upc);

		if (StringUtils.isNotBlank(upc)) {
			skuGroupBarcode = skuGroupDao.getSkuGroup(upc);
			if (skuGroupBarcode != null && skuGroupBarcode.getSku() != null) {
				productVariant = skuGroupBarcode.getSku().getProductVariant();
				skuGroups = new ArrayList<SkuGroup>();
				skuGroups.add(skuGroupBarcode);

				/*inStockSkuGroupList = skuItemDao.getInStockSkuGroups(skuGroupBarcode.getSku());
				if (inStockSkuGroupList != null && inStockSkuGroupList.size() > 0) {
					if (inStockSkuGroupList.size() == 1) {
						if (upc.equalsIgnoreCase(skuGroupBarcode.getBarcode())) {
							skuGroups = inStockSkuGroupList.subList(0, 1);
						} else {
							addRedirectAlertMessage(new SimpleMessage("Selected item not found in inventory list, please contact Admin. "));
							upc = null;
						}

					} else {
						if (upc.equalsIgnoreCase(inStockSkuGroupList.get(0).getBarcode())) {
							skuGroups = inStockSkuGroupList.subList(0, 1);
						} else {
							earlierSkuGroup = inStockSkuGroupList.get(0);
							wronglyPickedBox = true;
							upc = null;
						}
					}
				}*/
			} else {
				List<ProductVariant> pvList = productVariantDao.findVariantListFromUPC(upc);
				if (pvList != null && !pvList.isEmpty()) {
					productVariant = pvList.get(0);
				} else {
					productVariant = productVariantDao.getVariantById(upc);// UPC not available must have entered
					// Variant Id
				}
				logger.debug("productVariant: " + productVariant);
				if (productVariant != null) {
					boolean isBrandAudited = brandsToAuditDao.isBrandAudited(productVariant.getProduct().getBrand(), userService.getWarehouseForLoggedInUser());
					if (isBrandAudited) {
						addRedirectAlertMessage(new SimpleMessage("HKBarcoded Variant. Please scan HK Barcode to Checkout."));
						upc = null;
					} else {
						skuGroups = adminInventoryService.getInStockSkuGroups(upc);
						logger.debug("skuGroups: " + skuGroups.size());
					}
				} else {
					addRedirectAlertMessage(new SimpleMessage("Invalid UPC or VariantID"));
					upc = null;
				}
			}

		} else {
			addRedirectAlertMessage(new SimpleMessage("Invalid UPC or VariantID"));
			upc = null;
		}
		return new ForwardResolution("/pages/admin/inventoryCheckout.jsp");
	}

	public Resolution selectItemFromSkuGroup() {
		/*
				 * User loggedOnUser = null; if (getPrincipal() != null) { loggedOnUser =
				 * userDao.getUserById(getPrincipal().getId()); }
				 */
		User loggedOnUser = userService.getLoggedInUser();
		logger.debug("lineItem: " + lineItem);
		logger.debug("shippingOrder: " + shippingOrder);
		logger.debug("skuGroup: " + skuGroup);
		if (shippingOrder != null) {

			if (skuGroup != null) {
				if (lineItem == null) {
					lineItem = lineItemDao.getLineItem(skuGroup.getSku(), shippingOrder);
				}
				if (lineItem != null) {
					ProductVariant variant = skuGroup.getSku().getProductVariant();
					if (checkMrpPreCheckOut(variant) && skuGroup.getMrp() != null && skuGroup.getMrp() < lineItem.getMarkedPrice()) {
						addRedirectAlertMessage(new SimpleMessage("Oops!! You are trying to checkout lower MRP variant."));
					} else {
						Long checkedOutItemCount = adminProductVariantInventoryDao.getCheckedoutItemCount(lineItem);
						if (checkedOutItemCount == null) {
							checkedOutItemCount = 0L;
						}

						if (Math.abs(checkedOutItemCount) < lineItem.getQty() && shippingOrder.getOrderStatus().getId().equals(EnumShippingOrderStatus.SO_Picking.getId())) {

							List<SkuItem> inStockSkuItems = skuItemDao.getInStockSkuItems(skuGroup);
							if (inStockSkuItems != null && inStockSkuItems.size() > 0) {
								getAdminInventoryService().inventoryCheckinCheckout(skuGroup.getSku(), inStockSkuItems.get(0), lineItem, shippingOrder, null, null, null, getInventoryService().getInventoryTxnType(EnumInvTxnType.INV_CHECKOUT), -1L, loggedOnUser);
								binManager.removeBinAllocated(inStockSkuItems.get(0));
								addRedirectAlertMessage(new SimpleMessage("SkuItem from selected Batch is checked out."));

								String comments = "Checked-out One Unit of Item: " + variant.getProduct().getName() + "<br/>" + variant.getOptionsCommaSeparated();
								shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_CheckedOut, comments);
							} else {
								addRedirectAlertMessage(new SimpleMessage("Some error to get skuitem for skugroup"));
							}

						} else if (shippingOrder.getOrderStatus().getId().equals(EnumShippingOrderStatus.SO_Shipped.getId())) {
							List<SkuItem> inStockSkuItems = skuItemDao.getInStockSkuItems(skuGroup);
							if (inStockSkuItems != null && inStockSkuItems.size() > 0) {
								getAdminInventoryService().inventoryCheckinCheckout(skuGroup.getSku(), inStockSkuItems.get(0), lineItem, shippingOrder, null, null, null, getInventoryService().getInventoryTxnType(EnumInvTxnType.INV_REPEAT_CHECKOUT), -1L, loggedOnUser);
								binManager.removeBinAllocated(inStockSkuItems.get(0));
								addRedirectAlertMessage(new SimpleMessage("SkuItem from selected Batch is checked out."));

								String comments = "Checked-out One Unit of Item: " + variant.getProduct().getName() + "<br/>" + variant.getOptionsCommaSeparated();
								shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_ReCheckedout, comments);
							} else {
								addRedirectAlertMessage(new SimpleMessage("Some error to get skuitem for skugroup"));
							}
						} else {
							addRedirectAlertMessage(new SimpleMessage("Oops!! All SkuItem Units for the In-process LineItem are already checked out."));
						}
					}
				} else {
					addRedirectAlertMessage(new SimpleMessage("Oops!! You are trying to checkout wrong variant. Plz check."));
				}
			}
		} else {
			addRedirectAlertMessage(new SimpleMessage("Oops!! Item can only be checkout against a gatewayId."));
			return new RedirectResolution(InventoryCheckoutAction.class).addParameter("findSkuGroups").addParameter("shippingOrder", shippingOrder.getId()).addParameter("upc", skuGroup.getSku().getProductVariant().getUpc());
		}
		return new RedirectResolution(InventoryCheckoutAction.class).addParameter("checkout").addParameter("gatewayOrderId", shippingOrder.getGatewayOrderId());
	}

	//Excluding eyeglasses category from mrp check.
	private boolean checkMrpPreCheckOut(ProductVariant variant){
		if(Functions.collectionContains(variant.getProduct().getCategories(), CategoryConstants.eyeGlasses)){
			return false;
		}
		return true;
	}

	public Resolution inventoryCheckoutOfItemThatIsNotInOrderLineItem() { // Written for Combo may be used to adjust
		// invs too.
		/*
				 * User user = null; if (getPrincipal() != null) { user = userDao.getUserById(getPrincipal().getId()); }
				 */
		User user = userService.getLoggedInUser();
		logger.debug("Free Checkout of involved or replacing items for order: " + shippingOrder);
		logger.debug("skuGroup: " + skuGroup);
		if (shippingOrder != null) {
			if (skuGroup != null) {
				productVariant = skuGroup.getSku().getProductVariant();
				logger.debug("productVariant: " + productVariant);
				List<SkuItem> inStockSkuItems = skuItemDao.getInStockSkuItems(skuGroup);
				if (inStockSkuItems != null && inStockSkuItems.size() > 0) {
					getAdminInventoryService().inventoryCheckinCheckout(skuGroup.getSku(), inStockSkuItems.get(0), null, shippingOrder, null, null, null, getInventoryService().getInventoryTxnType(EnumInvTxnType.INV_CHECKOUT), -1L, user);
					addRedirectAlertMessage(new SimpleMessage("SkuItem from selected Batch is checked out."));

					String comments = "Free Checked-out One Unit of Item: " + productVariant.getProduct().getName() + "<br/>" + productVariant.getOptionsCommaSeparated();
					shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_CheckedOut, comments);
				} else {
					addRedirectAlertMessage(new SimpleMessage("Some error to get skuitem for skugroup"));
				}

			} else {
				addRedirectAlertMessage(new SimpleMessage("Oops!! You are trying to checkout wrong variant. Plz check."));
			}
		}
		return new RedirectResolution(InventoryCheckoutAction.class).addParameter("checkout").addParameter("gatewayOrderId", shippingOrder.getGatewayOrderId());
	}

	@JsonHandler
	public Resolution updateUPC() {
		logger.debug("productVariant: " + productVariant.getId());
		logger.debug("upc: " + upc);
		productVariant.setUpc(upc);
		getProductVariantService().save(productVariant);

		HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "success");

		return new JsonResolution(healthkartResponse);
	}

	public ShippingOrder getShippingOrder() {
		return shippingOrder;
	}

	public void setShippingOrder(ShippingOrder shippingOrder) {
		this.shippingOrder = shippingOrder;
	}

	public LineItem getLineItem() {
		return lineItem;
	}

	public void setLineItem(LineItem lineItem) {
		this.lineItem = lineItem;
	}

	public String getUpc() {
		return upc;
	}

	public void setUpc(String upc) {
		this.upc = upc;
	}

	public ProductVariant getProductVariant() {
		return productVariant;
	}

	public void setProductVariant(ProductVariant productVariant) {
		this.productVariant = productVariant;
	}

	public Long getQty() {
		return qty;
	}

	public void setQty(Long qty) {
		this.qty = qty;
	}

	public SkuGroup getSkuGroup() {
		return skuGroup;
	}

	public void setSkuGroup(SkuGroup skuGroup) {
		this.skuGroup = skuGroup;
	}

	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	public List<SkuGroup> getSkuGroups() {
		return skuGroups;
	}

	public void setSkuGroups(List<SkuGroup> skuGroups) {
		this.skuGroups = skuGroups;
	}

	public void setGatewayOrderId(String gatewayOrderId) {
		this.gatewayOrderId = gatewayOrderId;
	}

	public String getGatewayOrderId() {
		return gatewayOrderId;
	}

	public void setShippingOrderService(ShippingOrderService shippingOrderService) {
		this.shippingOrderService = shippingOrderService;
	}

	public void setUserService(UserService userService) {
		this.userService = userService;
	}

	public void setShippingOrderStatusService(ShippingOrderStatusService shippingOrderStatusService) {
		this.shippingOrderStatusService = shippingOrderStatusService;
	}

	public InventoryService getInventoryService() {
		return inventoryService;
	}

	public void setInventoryService(InventoryService inventoryService) {
		this.inventoryService = inventoryService;
	}

	public OrderStatusService getOrderStatusService() {
		return orderStatusService;
	}

	public void setOrderStatusService(OrderStatusService orderStatusService) {
		this.orderStatusService = orderStatusService;
	}

	public OrderService getOrderService() {
		return orderService;
	}

	public void setOrderService(OrderService orderService) {
		this.orderService = orderService;
	}

	public AdminInventoryService getAdminInventoryService() {
		return adminInventoryService;
	}

	public void setAdminInventoryService(AdminInventoryService adminInventoryService) {
		this.adminInventoryService = adminInventoryService;
	}

	public SkuService getSkuService() {
		return skuService;
	}

	public void setSkuService(SkuService skuService) {
		this.skuService = skuService;
	}

	public OrderManager getOrderManager() {
		return orderManager;
	}

	public void setOrderManager(OrderManager orderManager) {
		this.orderManager = orderManager;
	}

	public ShippingOrderService getShippingOrderService() {
		return shippingOrderService;
	}

	public UserService getUserService() {
		return userService;
	}

	public ShippingOrderStatusService getShippingOrderStatusService() {
		return shippingOrderStatusService;
	}

	public ProductVariantService getProductVariantService() {
		return productVariantService;
	}

	public void setProductVariantService(ProductVariantService productVariantService) {
		this.productVariantService = productVariantService;
	}

	/*public boolean isWronglyPickedBox() {
		return wronglyPickedBox;
	}*/

	/*public void setWronglyPickedBox(boolean wronglyPickedBox) {
		this.wronglyPickedBox = wronglyPickedBox;
	}*/

	/*public SkuGroup getEarlierSkuGroup() {
		return earlierSkuGroup;
	}

	public void setEarlierSkuGroup(SkuGroup earlierSkuGroup) {
		this.earlierSkuGroup = earlierSkuGroup;
	}*/
}