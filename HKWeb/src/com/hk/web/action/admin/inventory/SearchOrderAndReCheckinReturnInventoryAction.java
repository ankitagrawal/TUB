package com.hk.web.action.admin.inventory;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.dao.inventory.AdminProductVariantInventoryDao;
import com.hk.admin.pact.dao.inventory.ProductVariantDamageInventoryDao;
import com.hk.admin.pact.service.inventory.AdminInventoryService;
import com.hk.admin.pact.service.reverseOrder.ReverseOrderService;
import com.hk.admin.util.BarcodeUtil;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.courier.StateList;
import com.hk.constants.inventory.EnumInvTxnType;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.constants.sku.EnumSkuItemOwner;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.ProductVariantInventory;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderStatus;
import com.hk.domain.reverseOrder.ReverseLineItem;
import com.hk.domain.reverseOrder.ReverseOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.SkuItem;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuGroupService;
import com.hk.pact.service.inventory.SkuItemLineItemService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Secure(hasAnyPermissions = {PermissionConstants.INVENTORY_CHECKIN}, authActionBean = AdminPermissionAction.class)
@Component
public class SearchOrderAndReCheckinReturnInventoryAction extends BaseAction {

  private static Logger logger = LoggerFactory.getLogger(SearchOrderAndReCheckinReturnInventoryAction.class);
  @Autowired
  private UserService userService;
  @Autowired
  private AdminInventoryService adminInventoryService;
  @Autowired
  private InventoryService inventoryService;
  @Autowired
  private ShippingOrderService shippingOrderService;
  @Autowired
  private OrderService orderService;
  @Autowired
  private AdminProductVariantInventoryDao adminProductVariantInventoryDao;
  @Autowired
  private ProductVariantDamageInventoryDao productVariantDamageInventoryDao;
  @Autowired
  private ReverseOrderService reverseOrderService;
  @Autowired
  private SkuItemDao skuItemDao;
  @Autowired
  private SkuGroupService skuGroupService;
  @Autowired
  private SkuItemLineItemService skuItemLineItemService;

  Map<LineItem, String> lineItemRecheckinBarcodeMap = new HashMap<LineItem, String>();

  private Long orderId;
  private String gatewayOrderId;

  private ShippingOrder shippingOrder;
  private ReverseOrder reverseOrder;
  private List<LineItem> lineItems = new ArrayList<LineItem>();
  private String conditionOfItem;
  private static String GOOD = "good";
  private static String DAMAGED = "damaged";
  private static String EXPIRED = "expired";
  private LineItem lineItem;
  private ReverseLineItem reverseLineItem;
  File printBarcode;

  @Value("#{hkEnvProps['" + Keys.Env.barcodeGurgaon + "']}")
  String barcodeGurgaon;

  @Value("#{hkEnvProps['" + Keys.Env.barcodeMumbai + "']}")
  String barcodeMumbai;

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

    if (shippingOrder != null) {
      ShippingOrderStatus status = shippingOrder.getOrderStatus();
      if (status.equals(EnumShippingOrderStatus.SO_Customer_Return_Refunded.asShippingOrderStatus()) ||
          status.equals(EnumShippingOrderStatus.SO_Customer_Return_Replaced.asShippingOrderStatus()) ||
          status.equals(EnumShippingOrderStatus.SO_ReversePickup_Initiated.asShippingOrderStatus())) {
        reverseOrder = getReverseOrderService().getReverseOrderByShippingOrderId(shippingOrder.getId());
      }
      if (!EnumShippingOrderStatus.getStatusForReCheckinReturnItems().contains(status)) {
        addRedirectAlertMessage(new SimpleMessage("The order is not in a correct status to checkin"));
        shippingOrder = null;
      }
    }

    return new ForwardResolution("/pages/admin/searchOrderAndReCheckinReturnInventory.jsp");
  }

  public Resolution checkinReturnedUnits() {
    User loggedOnUser = userService.getLoggedInUser();

    if (lineItemRecheckinBarcodeMap != null && lineItemRecheckinBarcodeMap.size() > 1) {
      addRedirectAlertMessage(new SimpleMessage("only one barcode can be checked in at one time"));
    } else {
      for (Map.Entry<LineItem, String> lineItemRecheckinBarcodeMapEntry : lineItemRecheckinBarcodeMap.entrySet()) {
        LineItem lineItem = lineItemRecheckinBarcodeMapEntry.getKey();
        ProductVariant productVariant = lineItem.getSku().getProductVariant();
        ShippingOrder shippingOrder = lineItem.getShippingOrder();
        String recheckinBarcode = lineItemRecheckinBarcodeMapEntry.getValue();
        Long alreadyCheckedInUnits = getAdminProductVariantInventoryDao().getCheckedInPVIAgainstReturn(lineItem);
        List<ProductVariantInventory> checkedOutInventories = getAdminProductVariantInventoryDao().getCheckedOutSkuItems(shippingOrder, lineItem);
        SkuItem findSkuItemByBarcode;
        String skuGroupBarcode, skuItemBarcode;
        if (checkedOutInventories != null && !checkedOutInventories.isEmpty() && alreadyCheckedInUnits < checkedOutInventories.size()) {
          int recheckinCounter = 0;
          findSkuItemByBarcode = skuGroupService.getSkuItemByBarcode(recheckinBarcode, userService.getWarehouseForLoggedInUser().getId(), EnumSkuItemStatus.Checked_OUT.getId());
          for (ProductVariantInventory checkedOutInventory : checkedOutInventories) {
            SkuItem skuItem;
            if (findSkuItemByBarcode != null) {
              skuItem = findSkuItemByBarcode;
              skuItemBarcode = findSkuItemByBarcode.getBarcode();
            } else {
              skuItem = checkedOutInventory.getSkuItem();
              skuGroupBarcode = checkedOutInventory.getSkuItem().getSkuGroup().getBarcode();
              skuItemBarcode = skuGroupBarcode;
            }

            if (skuItemBarcode != null && skuItemBarcode.equalsIgnoreCase(recheckinBarcode) && skuItem.getId().equals(checkedOutInventory.getSkuItem().getId()) &&
                checkedOutInventory.getSkuItem().getSkuItemStatus().equals(EnumSkuItemStatus.Checked_OUT.getSkuItemStatus())) {
              recheckinCounter++;

              if (conditionOfItem.equals(GOOD)) {
                getAdminInventoryService().inventoryCheckinCheckout(checkedOutInventory.getSku(), skuItem, lineItem, shippingOrder, null, null,
                    null, EnumSkuItemStatus.Checked_IN_Non_Saleable, EnumSkuItemOwner.SELF, getInventoryService().getInventoryTxnType(EnumInvTxnType.RETURN_CHECKIN_GOOD_NON_SALEABLE), 0L, loggedOnUser);
                skuItem.setSkuItemStatus(EnumSkuItemStatus.Checked_IN_Non_Saleable.getSkuItemStatus());
                skuItem.setSkuItemOwner(EnumSkuItemOwner.SELF.getSkuItemOwnerStatus());
                skuItemDao.save(skuItem);
                inventoryService.checkInventoryHealth(productVariant);
                break;
              }

              if (conditionOfItem.equals(DAMAGED)) {
                getAdminInventoryService().inventoryCheckinCheckout(checkedOutInventory.getSku(), skuItem, lineItem, shippingOrder, null, null,
                    null, EnumSkuItemStatus.Damaged, EnumSkuItemOwner.SELF, getInventoryService().getInventoryTxnType(EnumInvTxnType.RETURN_CHECKIN_DAMAGED), 0L, loggedOnUser);
                skuItem.setSkuItemStatus(EnumSkuItemStatus.Damaged.getSkuItemStatus());
                skuItem.setSkuItemOwner(EnumSkuItemOwner.SELF.getSkuItemOwnerStatus());
                skuItemDao.save(skuItem);
                inventoryService.checkInventoryHealth(productVariant);
                break;
              }

              if (conditionOfItem.equals(EXPIRED)) {
                getAdminInventoryService().inventoryCheckinCheckout(checkedOutInventory.getSku(), skuItem, lineItem, shippingOrder, null, null,
                    null, EnumSkuItemStatus.Expired, EnumSkuItemOwner.SELF, getInventoryService().getInventoryTxnType(EnumInvTxnType.RETURN_CHECKIN_EXPIRED), 0L, loggedOnUser);
//								skuItem.setSkuItemStatus(EnumSkuItemStatus.Expired.getSkuItemStatus());
//                                skuItem.setSkuItemOwner(EnumSkuItemOwner.SELF.getSkuItemOwnerStatus());
//								skuItemDao.save(skuItem);
                inventoryService.checkInventoryHealth(productVariant);
                break;
              }

            }
          }

          if (recheckinCounter != 0) {
            String comments = "Re Checked-in (Non Saleable) Returned Item : " + conditionOfItem + "--" + recheckinCounter + " x " + productVariant.getProduct().getName() + "<br/>" +
                productVariant.getOptionsCommaSeparated();
            shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_ReCheckedIn, null, comments);
            skuItemLineItemService.freeInventoryForRTOCheckIn(shippingOrder);
            addRedirectAlertMessage(new SimpleMessage("Returned Units checked in accordingly"));
          } else {
            addRedirectAlertMessage(new SimpleMessage("The Barcode entered doesn't match any of the items OR item not in correct status"));
          }
        } else {
          addRedirectAlertMessage(new SimpleMessage("Oops!!! Either Returned Units are already checked in OR No batch information was there while checking out items."));
        }
        orderId = shippingOrder.getId();
      }
    }

    return new RedirectResolution(SearchOrderAndReCheckinReturnInventoryAction.class).addParameter("searchOrder").addParameter("orderId", orderId);
  }

  public Resolution downloadBarcode() {
    List<SkuItem> checkedOutSkuItems = adminInventoryService.getCheckedInOrOutSkuItems(null, null, null, lineItem, -1L);
    List<SkuItem> checkedInSkuItems = adminInventoryService.getCheckedInOrOutSkuItems(null, null, null, lineItem, 1L);

    checkedOutSkuItems.removeAll(checkedInSkuItems);

    ProductVariant productVariant = lineItem.getSku().getProductVariant();
    Map<Long, String> skuItemDataMap = adminInventoryService.skuItemBarcodeMap(checkedOutSkuItems);

    String barcodeFilePath = null;
    Warehouse userWarehouse = null;
    if (getUserService().getWarehouseForLoggedInUser() != null) {
      userWarehouse = userService.getWarehouseForLoggedInUser();
    } else {
      addRedirectAlertMessage(new SimpleMessage("There is no warehouse attached with the logged in user. Please check with the admin."));
      return new RedirectResolution(SearchOrderAndReCheckinReturnInventoryAction.class).addParameter("searchOrder").addParameter("orderId", orderId);
    }
    if (userWarehouse.getState().equalsIgnoreCase(StateList.HARYANA)) {
      barcodeFilePath = barcodeGurgaon;
    } else {
      barcodeFilePath = barcodeMumbai;
    }
    barcodeFilePath = barcodeFilePath + "/" + "printBarcode_" + "reCheckin_" + productVariant + "_" + StringUtils.substring(userWarehouse.getCity(), 0, 3) + ".txt";

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

  public File getPrintBarcode() {
    return printBarcode;
  }

  public void setPrintBarcode(File printBarcode) {
    this.printBarcode = printBarcode;
  }

  public LineItem getLineItem() {
    return lineItem;
  }

  public void setLineItem(LineItem lineItem) {
    this.lineItem = lineItem;
  }

  public ReverseLineItem getReverseLineItem() {
    return reverseLineItem;
  }

  public void setReverseLineItem(ReverseLineItem reverseLineItem) {
    this.reverseLineItem = reverseLineItem;
  }
}