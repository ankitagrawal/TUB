package com.hk.web.action.admin.queue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.impl.dao.inventory.AdminSkuItemDao;
import com.hk.admin.impl.dao.warehouse.BinDao;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.dao.catalog.category.CategoryDao;
import com.hk.dao.payment.PaymentModeDao;
import com.hk.dao.sku.SkuItemDao;
import com.hk.dao.user.UserDaoImpl;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.Bin;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.SkuItem;
import com.hk.manager.OrderManager;
import com.hk.manager.ReferrerProgramManager;
import com.hk.search.ShippingOrderSearchCriteria;
import com.hk.service.UserService;
import com.hk.service.shippingOrder.ShippingOrderService;
import com.hk.service.shippingOrder.ShippingOrderStatusService;
import com.hk.util.BarcodeGenerator;

/**
 * Created by IntelliJ IDEA.
 * User: PRATHAM
 * Date: 1/18/12
 * Time: 4:44 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class JobCartAction extends BaseAction {

  String gatewayOrderId;
  ShippingOrder shippingOrder;
  List<ShippingOrder> pickingQueueOrders = new ArrayList<ShippingOrder>();
  private String barcodePath;
  Map<Bin, Map<ProductVariant, Long>> binHasPVs = new HashMap<Bin, Map<ProductVariant, Long>>();
  Page pickingQueueOrdersPage;
  Category category;
  String baseGatewayOrderId;

  
  CategoryDao categoryDao;
  
  ReferrerProgramManager referrerProgramManager;
  
  UserDaoImpl userDao;
  
  BarcodeGenerator barcodeGenerator;
  
  OrderManager orderManager;
  
  PaymentModeDao paymentModeDao;
  
  SkuItemDao skuItemDao;
  AdminSkuItemDao adminSkuItemDao;
  
  BinDao binDao;
  
  ShippingOrderService shippingOrderService;
  
  UserService userService;
  
  ShippingOrderStatusService shippingOrderStatusService;


  @DefaultHandler
  public Resolution pre() {
    Bin defaultBin = new Bin();
    defaultBin.setAisle("D");
    defaultBin.setRack("D");
    defaultBin.setShelf("D");

    defaultBin = binDao.getOrCreateBin(defaultBin, userService.getWarehouseForLoggedInUser());
    binHasPVs = new HashMap<Bin, Map<ProductVariant, Long>>();

    ShippingOrderSearchCriteria shippingOrderSearchCriteria =  getShippingOrderSearchCriteria(EnumShippingOrderStatus.getStatusForPicking());
   /* shippingOrderSearchCriteria.setShippingOrderStatusList(shippingOrderStatusService.getOrderStatuses());
    shippingOrderSearchCriteria.setBasketCategory(category.getName());*/

    pickingQueueOrdersPage = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria, 1, 10);
    //printingPickingQueueOrdersPage = shippingOrderService.getPickingQueueOrders(1, 10);
    pickingQueueOrders = pickingQueueOrdersPage.getList();
    List<SkuItem> skuItems = new ArrayList<SkuItem>();

    for (ShippingOrder shippingOrder : pickingQueueOrders) {
      Set<LineItem> lineItems = shippingOrder.getLineItems();
      for (LineItem productLineItem : lineItems) {
        skuItems.addAll(adminSkuItemDao.getInStockSkuItemsByQty(productLineItem.getSku(), productLineItem.getQty().intValue()));
      }
    }

    for (SkuItem skuItem : skuItems) {
      Bin bin = skuItem.getBins() != null && skuItem.getBins().size() > 0 ? skuItem.getBins().get(0) : defaultBin;
      Map<ProductVariant, Long> pvQtyMap = binHasPVs.get(bin);
      if (pvQtyMap != null) {
        if (pvQtyMap.get(skuItem.getSkuGroup().getSku().getProductVariant()) != null) {
          pvQtyMap.put(skuItem.getSkuGroup().getSku().getProductVariant(), pvQtyMap.get(skuItem.getSkuGroup().getSku().getProductVariant()) + 1L);
        } else {
          pvQtyMap.put(skuItem.getSkuGroup().getSku().getProductVariant(), 1L);
        }
        binHasPVs.put(bin, pvQtyMap);
      } else {
        pvQtyMap = new HashMap<ProductVariant, Long>();
        pvQtyMap.put(skuItem.getSkuGroup().getSku().getProductVariant(), 1L);
        binHasPVs.put(bin, pvQtyMap);
      }
    }

    //barcodePath = barcodeGenerator.getBarcodePath(shippingOrder.getGatewayOrderId());

    return new ForwardResolution("/pages/admin/jobCart.jsp");
  }


  private ShippingOrderSearchCriteria getShippingOrderSearchCriteria(List<EnumShippingOrderStatus> shippingOrderStatuses) {
    ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
    shippingOrderSearchCriteria.setShippingOrderStatusList(shippingOrderStatusService.getOrderStatuses(shippingOrderStatuses));

    if (baseGatewayOrderId != null) {
      shippingOrderSearchCriteria.setBaseGatewayOrderId(baseGatewayOrderId);
    } else if (gatewayOrderId != null) {
      shippingOrderSearchCriteria.setGatewayOrderId(gatewayOrderId);
    } else {
      shippingOrderSearchCriteria.setBasketCategory(category.getName()).setBaseGatewayOrderId(baseGatewayOrderId).setGatewayOrderId(gatewayOrderId);
    }

    return shippingOrderSearchCriteria;
  }

  public String getGatewayOrderId() {
    return gatewayOrderId;
  }


  public String getBaseGatewayOrderId() {
    return baseGatewayOrderId;
  }

  public void setBaseGatewayOrderId(String baseGatewayOrderId) {
    this.baseGatewayOrderId = baseGatewayOrderId;
  }

  public void setGatewayOrderId(String gatewayOrderId) {
    this.gatewayOrderId = gatewayOrderId;
  }

  public String getBarcodePath() {
    return barcodePath;
  }

  public void setBarcodePath(String barcodePath) {
    this.barcodePath = barcodePath;
  }

  public Map<Bin, Map<ProductVariant, Long>> getBinHasPVs() {
    return binHasPVs;
  }

  public void setBinHasPVs(Map<Bin, Map<ProductVariant, Long>> binHasPVs) {
    this.binHasPVs = binHasPVs;

  }

  public ShippingOrder getShippingOrder() {
    return shippingOrder;
  }

  public void setShippingOrder(ShippingOrder shippingOrder) {
    this.shippingOrder = shippingOrder;
  }

  public List<ShippingOrder> getPickingQueueOrders() {
    return pickingQueueOrders;
  }

  public void setPickingQueueOrders(List<ShippingOrder> pickingQueueOrders) {
    this.pickingQueueOrders = pickingQueueOrders;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }
}
