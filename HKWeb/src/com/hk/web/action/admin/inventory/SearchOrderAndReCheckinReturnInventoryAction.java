package com.hk.web.action.admin.inventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.dao.inventory.AdminProductVariantInventoryDao;
import com.hk.admin.pact.dao.inventory.ProductVariantDamageInventoryDao;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.pact.service.reverseOrder.ReverseOrderService;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.inventory.EnumInvTxnType;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.ProductVariantInventory;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderStatus;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.user.User;
import com.hk.domain.reverseOrder.ReverseOrder;
import com.hk.domain.sku.SkuItem;
import com.hk.pact.service.UserService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = { PermissionConstants.INVENTORY_CHECKIN }, authActionBean = AdminPermissionAction.class)
@Component
public class SearchOrderAndReCheckinReturnInventoryAction extends BaseAction {

    @Autowired
    private UserService                     userService;
    @Autowired
    private AdminInventoryService           adminInventoryService;
    @Autowired
    private InventoryService                inventoryService;
    @Autowired
    private ShippingOrderService            shippingOrderService;
    @Autowired
    private OrderService                    orderService;
    @Autowired
    private AdminProductVariantInventoryDao adminProductVariantInventoryDao;
    @Autowired
    private ProductVariantDamageInventoryDao productVariantDamageInventoryDao;
	@Autowired
	private ReverseOrderService 				reverseOrderService;
	@Autowired
	private SkuItemDao							skuItemDao;

    Map<LineItem, String> lineItemRecheckinBarcodeMap = new HashMap<LineItem, String>();

    private Long                            orderId;
    private String                          gatewayOrderId;

    private ShippingOrder                   shippingOrder;
	private ReverseOrder					reverseOrder;
    private List<LineItem>                  lineItems               = new ArrayList<LineItem>();
	private String 							conditionOfItem;
	private static String 					GOOD 	= "good";
	private static String 					DAMAGED = "damaged";
	private static String 					EXPIRED = "expired";

	@DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/searchOrderAndReCheckinReturnInventory.jsp");
    }

    public Resolution searchOrder() {
        if (orderId != null) {
            shippingOrder = getShippingOrderService().find(orderId);
        } else if (StringUtils.isNotBlank(gatewayOrderId)) {
            shippingOrder = getShippingOrderService().findByGatewayOrderId(gatewayOrderId);
        }

		if(shippingOrder != null){			
			ShippingOrderStatus status = shippingOrder.getOrderStatus();
			if(status.equals(EnumShippingOrderStatus.SO_Customer_Return_Refunded.asShippingOrderStatus()) ||
					status.equals(EnumShippingOrderStatus.SO_Customer_Return_Replaced.asShippingOrderStatus())){
				reverseOrder = getReverseOrderService().getReverseOrderByShippingOrderId(shippingOrder.getId());
			}
			if(!EnumShippingOrderStatus.getStatusForReCheckinReturnItems().contains(status)){
				addRedirectAlertMessage(new SimpleMessage("The order is not in a correct status to checkin"));
				shippingOrder = null;
			}
		}

        return new ForwardResolution("/pages/admin/searchOrderAndReCheckinReturnInventory.jsp");
    }

    public Resolution checkinReturnedUnits() {
        User loggedOnUser = userService.getLoggedInUser();

        for (Map.Entry<LineItem, String> lineItemRecheckinQtyMapEntry : lineItemRecheckinBarcodeMap.entrySet()) {
            LineItem lineItem = lineItemRecheckinQtyMapEntry.getKey();
            ProductVariant productVariant = lineItem.getSku().getProductVariant();
            ShippingOrder shippingOrder = lineItem.getShippingOrder();
            String recheckinBarcode = lineItemRecheckinQtyMapEntry.getValue();
            Long alreadyCheckedInUnits = getAdminProductVariantInventoryDao().getCheckedInPVIAgainstReturn(lineItem);
            List<ProductVariantInventory> checkedOutInventories = getAdminProductVariantInventoryDao().getCheckedOutSkuItems(shippingOrder, lineItem);

			if (checkedOutInventories != null && !checkedOutInventories.isEmpty() && alreadyCheckedInUnits < checkedOutInventories.size()) {
				int recheckinCounter = 0;
					for (ProductVariantInventory checkedOutInventory : checkedOutInventories) {
						String skuGroupBarcode = checkedOutInventory.getSkuItem().getSkuGroup().getBarcode();
						if (skuGroupBarcode.equalsIgnoreCase(recheckinBarcode) && checkedOutInventory.getSkuItem().getSkuItemStatus().equals(EnumSkuItemStatus.Checked_OUT.getSkuItemStatus())) {
							recheckinCounter++;
							SkuItem skuItem = checkedOutInventory.getSkuItem();
							if (conditionOfItem.equals(GOOD)){
								getAdminInventoryService().inventoryCheckinCheckout(checkedOutInventory.getSku(), skuItem, lineItem, shippingOrder, null, null,
										null, getInventoryService().getInventoryTxnType(EnumInvTxnType.RETURN_CHECKIN_GOOD), 1L, loggedOnUser);
								skuItem.setSkuItemStatus(EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
								skuItemDao.save(skuItem);
								break;
							}

							if (conditionOfItem.equals(DAMAGED)){
								getAdminInventoryService().inventoryCheckinCheckout(checkedOutInventory.getSku(), skuItem, lineItem, shippingOrder, null, null,
										null, getInventoryService().getInventoryTxnType(EnumInvTxnType.RETURN_CHECKIN_DAMAGED), 1L, loggedOnUser);
								skuItem.setSkuItemStatus(EnumSkuItemStatus.Damaged.getSkuItemStatus());
								skuItemDao.save(skuItem);
								break;
							}

							if (conditionOfItem.equals(EXPIRED)){
								getAdminInventoryService().inventoryCheckinCheckout(checkedOutInventory.getSku(), skuItem, lineItem, shippingOrder, null, null,
										null, getInventoryService().getInventoryTxnType(EnumInvTxnType.RETURN_CHECKIN_EXPIRED), 1L, loggedOnUser);
								skuItem.setSkuItemStatus(EnumSkuItemStatus.Expired.getSkuItemStatus());
								skuItemDao.save(skuItem);
								break;
							}
							inventoryService.checkInventoryHealth(productVariant);
						}
					}

				if (recheckinCounter != 0){
					String comments = "Re Checked-in Returned Item : " + conditionOfItem + "--" + recheckinCounter + " x " + productVariant.getProduct().getName() + "<br/>" +
							productVariant.getOptionsCommaSeparated();
					shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_ReCheckedIn, comments);
//				shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_CheckedInDamageItem, damageComments);
					addRedirectAlertMessage(new SimpleMessage("Returned Units checked in accordingly"));
				} else{
					addRedirectAlertMessage(new SimpleMessage("The Barcode entered doesnt match any of the items OR item not in correct status"));
				}
			}
			else {
				addRedirectAlertMessage(new SimpleMessage("Oops!!! Either Returned Units are already checked in OR No batch information was there while checking out items."));
			}
			orderId = shippingOrder.getId();
        }

        return new RedirectResolution(SearchOrderAndReCheckinReturnInventoryAction.class).addParameter("searchOrder").addParameter("orderId", orderId);
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getGatewayOrderId() {
        return gatewayOrderId;
    }

    public void setGatewayOrderId(String gatewayOrderId) {
        this.gatewayOrderId = gatewayOrderId;
    }

    public ShippingOrder getShippingOrder() {
        return shippingOrder;
    }

    public void setShippingOrder(ShippingOrder shippingOrder) {
        this.shippingOrder = shippingOrder;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

	public Map<LineItem, String> getLineItemRecheckinBarcodeMap() {
		return lineItemRecheckinBarcodeMap;
	}

	public void setLineItemRecheckinBarcodeMap(Map<LineItem, String> lineItemRecheckinBarcodeMap) {
		this.lineItemRecheckinBarcodeMap = lineItemRecheckinBarcodeMap;
	}

	public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public AdminInventoryService getAdminInventoryService() {
        return adminInventoryService;
    }

    public void setAdminInventoryService(AdminInventoryService adminInventoryService) {
        this.adminInventoryService = adminInventoryService;
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }

    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public ShippingOrderService getShippingOrderService() {
        return shippingOrderService;
    }

    public void setShippingOrderService(ShippingOrderService shippingOrderService) {
        this.shippingOrderService = shippingOrderService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public AdminProductVariantInventoryDao getAdminProductVariantInventoryDao() {
        return adminProductVariantInventoryDao;
    }

    public void setAdminProductVariantInventoryDao(AdminProductVariantInventoryDao adminProductVariantInventoryDao) {
        this.adminProductVariantInventoryDao = adminProductVariantInventoryDao;
    }

    public ProductVariantDamageInventoryDao getProductVariantDamageInventoryDao() {
        return productVariantDamageInventoryDao;
    }

    public void setProductVariantDamageInventoryDao(ProductVariantDamageInventoryDao productVariantDamageInventoryDao) {
        this.productVariantDamageInventoryDao = productVariantDamageInventoryDao;
    }

	public ReverseOrderService getReverseOrderService() {
		return reverseOrderService;
	}

	public ReverseOrder getReverseOrder() {
		return reverseOrder;
	}

	public void setReverseOrder(ReverseOrder reverseOrder) {
		this.reverseOrder = reverseOrder;
	}

	public String getConditionOfItem() {
		return conditionOfItem;
	}

	public void setConditionOfItem(String conditionOfItem) {
		this.conditionOfItem = conditionOfItem;
	}
}