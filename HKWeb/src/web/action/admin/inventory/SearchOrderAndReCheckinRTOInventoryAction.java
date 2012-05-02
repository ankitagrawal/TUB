package web.action.admin.inventory;

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
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import web.action.error.AdminPermissionAction;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.impl.dao.inventory.AdminProductVariantInventoryDao;
import com.hk.admin.impl.dao.inventory.PVDamageInventoryDao;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.inventory.EnumInvTxnType;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.ProductVariantInventory;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.user.User;
import com.hk.service.InventoryService;
import com.hk.service.UserService;
import com.hk.service.order.OrderService;
import com.hk.service.shippingOrder.ShippingOrderService;

@Secure(hasAnyPermissions = { PermissionConstants.INVENTORY_CHECKIN }, authActionBean = AdminPermissionAction.class)
@Component
public class SearchOrderAndReCheckinRTOInventoryAction extends BaseAction {

    private UserService                     userService;
    private AdminInventoryService           adminInventoryService;
    private InventoryService                inventoryService;
    private ShippingOrderService            shippingOrderService;
    private OrderService                    orderService;

    private AdminProductVariantInventoryDao adminProductVariantInventoryDao;
    private PVDamageInventoryDao            productVariantDamageInventoryDao;

    Map<LineItem, Long>                     lineItemRecheckinQtyMap = new HashMap<LineItem, Long>();

    private Long                            orderId;
    private String                          gatewayOrderId;

    private ShippingOrder                   shippingOrder;
    private List<LineItem>                  lineItems               = new ArrayList<LineItem>();

    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/searchOrderAndReCheckinRTOInventory.jsp");
    }

    public Resolution searchOrder() {
        if (orderId != null) {
            shippingOrder = getShippingOrderService().find(orderId);
        } else if (StringUtils.isNotBlank(gatewayOrderId)) {
            shippingOrder = getShippingOrderService().findByGatewayOrderId(gatewayOrderId);
        }
        return new ForwardResolution("/pages/admin/searchOrderAndReCheckinRTOInventory.jsp");
    }

    public Resolution checkinRTOUnits() {
        User loggedOnUser = userService.getLoggedInUser();

        for (Map.Entry<LineItem, Long> lineItemRecheckinQtyMapEntry : lineItemRecheckinQtyMap.entrySet()) {
            LineItem lineItem = lineItemRecheckinQtyMapEntry.getKey();
            ProductVariant productVariant = lineItem.getSku().getProductVariant();
            ShippingOrder shippingOrder = lineItem.getShippingOrder();
            Long recheckinQty = lineItemRecheckinQtyMapEntry.getValue();
            Long checkedInUnits = getAdminProductVariantInventoryDao().getCheckedInPVIAgainstRTO(lineItem) + getProductVariantDamageInventoryDao().getCheckedInPVDIAgainstRTO(lineItem);
            List<ProductVariantInventory> checkedOutInventories = getAdminProductVariantInventoryDao().getCheckedOutSkuItems(shippingOrder, lineItem);
            if (checkedInUnits == 0 && checkedOutInventories != null && !checkedOutInventories.isEmpty()) {
                int recheckinCounter = 0;
                for (ProductVariantInventory checkedOutInventory : checkedOutInventories) {
                    if (recheckinQty > 0 && recheckinCounter < recheckinQty) {
                        recheckinCounter++;
                        getAdminInventoryService().inventoryCheckinCheckout(checkedOutInventory.getSku(), checkedOutInventory.getSkuItem(), lineItem, shippingOrder, null, null,
                                getInventoryService().getInventoryTxnType(EnumInvTxnType.RTO_CHECKIN), 1L, loggedOnUser);
                        // Bug Fix deleting checked out pvi to maintain sanity instead of re-checkin.
                        // productVariantInventoryDao.remove(checkedOutInventory.getId());
                        inventoryService.checkInventoryHealth(productVariant);
                    } else {
                        getAdminInventoryService().damageInventoryCheckin(checkedOutInventory.getSkuItem(), lineItem);
                    }
                }
                String comments = "Re Checked-in RTO Items: " + recheckinQty + " x " + productVariant.getProduct().getName() + "<br/>" + productVariant.getOptionsCommaSeparated();
                shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_ReCheckedIn, comments);
                String damageComments = "Checked-in Damage RTO Items: " + (lineItem.getQty() - recheckinQty) + " x " + productVariant.getProduct().getName() + "<br/>"
                        + productVariant.getOptionsCommaSeparated();
                shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_CheckedInDamageItem, damageComments);
                addRedirectAlertMessage(new SimpleMessage("RTO Units checked in accordingly"));
            } else {
                addRedirectAlertMessage(new SimpleMessage("Oops!!! Eitehr RTO Units are already checked in OR No batch information was there while checking out items."));
            }
        }

        return new RedirectResolution(SearchOrderAndReCheckinRTOInventoryAction.class).addParameter("searchOrder").addParameter("orderId", orderId);
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

    public Map<LineItem, Long> getLineItemRecheckinQtyMap() {
        return lineItemRecheckinQtyMap;
    }

    public void setLineItemRecheckinQtyMap(Map<LineItem, Long> lineItemRecheckinQtyMap) {
        this.lineItemRecheckinQtyMap = lineItemRecheckinQtyMap;
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

    public PVDamageInventoryDao getProductVariantDamageInventoryDao() {
        return productVariantDamageInventoryDao;
    }

    public void setProductVariantDamageInventoryDao(PVDamageInventoryDao productVariantDamageInventoryDao) {
        this.productVariantDamageInventoryDao = productVariantDamageInventoryDao;
    }
    
    

}