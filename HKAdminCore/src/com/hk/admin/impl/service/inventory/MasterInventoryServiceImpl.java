package com.hk.admin.impl.service.inventory;

import com.hk.admin.pact.service.inventory.MasterInventoryService;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.constants.sku.EnumSkuItemOwner;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.sku.Sku;
import com.hk.domain.sku.SkuItem;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.dao.shippingOrder.ShippingOrderDao;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.pact.dao.sku.SkuItemLineItemDao;
import com.hk.pact.service.inventory.SkuService;
import com.hk.service.ServiceLocatorFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: Oct 5, 2013
 * Time: 3:51:29 PM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class MasterInventoryServiceImpl implements MasterInventoryService {

  private SkuService skuService;
  private SkuItemDao skuItemDao;
  private ShippingOrderDao shippingOrderDao;
  private OrderDao orderDao;
  private SkuItemLineItemDao skuItemLineItemDao;

  @Override
  public List<SKUDetails> getSkuDetails(ProductVariant productVariant) {
    List<SKUDetails> skuDetailsList = new ArrayList<SKUDetails>();
    List<Sku> skuList = getSkuService().getSKUsForProductVariantAtServiceableWarehouses(productVariant);
    for (Sku sku : skuList) {
      SKUDetails skuDetails = new SKUDetails();
      skuDetails.setSku(sku);
      skuDetails.setPhyQty(getUncheckedOutUnits(Arrays.asList(sku)));
      skuDetails.setCiQty(getCheckedInUnits(Arrays.asList(sku)));
      skuDetails.setBookedQty(getBookedUnits(Arrays.asList(sku)));
      skuDetails.setUnbookedLIQty(getUnbookedLIUnits(Arrays.asList(sku)));

      skuDetailsList.add(skuDetails);
    }
    return skuDetailsList;
  }

  @Override
  public Long getCheckedInUnits(List<Sku> skuList) {
    List<SkuItem> skuItems = getSkuItemDao().getSkuItems(skuList,
        Arrays.asList(EnumSkuItemStatus.Checked_IN.getId()),
        Arrays.asList(EnumSkuItemOwner.SELF.getId()), null);
    return skuItems != null && !skuItems.isEmpty() ? Long.valueOf(skuItems.size()) : 0L;
  }

  @Override
  public Long getBookedUnits(List<Sku> skuList) {
    List<SkuItem> skuItems = getSkuItemDao().getSkuItems(skuList,
        Arrays.asList(EnumSkuItemStatus.TEMP_BOOKED.getId(), EnumSkuItemStatus.BOOKED.getId()),
        Arrays.asList(EnumSkuItemOwner.SELF.getId()), null);
    return skuItems != null && !skuItems.isEmpty() ? Long.valueOf(skuItems.size()) : 0L;
  }

  @Override
  public Long getUncheckedOutUnits(List<Sku> skuList) {
    List<SkuItem> skuItems = getSkuItemDao().getSkuItems(skuList,
        Arrays.asList(EnumSkuItemStatus.Checked_IN.getId(), EnumSkuItemStatus.TEMP_BOOKED.getId(), EnumSkuItemStatus.BOOKED.getId()),
        Arrays.asList(EnumSkuItemOwner.SELF.getId()), null);
    return skuItems != null && !skuItems.isEmpty() ? Long.valueOf(skuItems.size()) : 0L;
  }

  @Override
  public Long getUnbookedCLIUnits(ProductVariant productVariant) {
    return getSkuItemLineItemDao().getUnbookedCLICount(productVariant);
  }

  @Override
  public Long getUnbookedLIUnits(List<Sku> skuList) {
    return getSkuItemLineItemDao().getUnbookedLICount(skuList);
  }

  @Override
  public Long getLIUnits(List<Sku> skuList, List<Long> shippingOrderStatusIds) {
    return getShippingOrderDao().getBookedQtyOfSkuInQueue(skuList,
        EnumShippingOrderStatus.getShippingOrderStatusIDs(EnumShippingOrderStatus.getStatusForBookedInventory()));
  }

  @Override
  public Long getUnsplitBOCLIUnits(ProductVariant productVariant) {
    return getOrderDao().getBookedQtyOfProductVariantInQueue(productVariant);
  }

  @Override
  public Long getAvailableUnits(ProductVariant productVariant) {
    List<Sku> skuList = getSkuService().getSKUsForProductVariantAtServiceableWarehouses(productVariant);
    Long inUnits = getCheckedInUnits(skuList);
    Long unbookedCLIUnits = getUnbookedCLIUnits(productVariant);
    Long unbookedLIUnits = getUnbookedLIUnits(skuList);
    return inUnits - unbookedCLIUnits - unbookedLIUnits;
  }

  public SkuService getSkuService() {
    if (skuService == null) {
      skuService = ServiceLocatorFactory.getService(SkuService.class);
    }
    return skuService;
  }

  public SkuItemDao getSkuItemDao() {
    if (skuItemDao == null) {
      skuItemDao = ServiceLocatorFactory.getService(SkuItemDao.class);
    }
    return skuItemDao;
  }

  public ShippingOrderDao getShippingOrderDao() {
    if (shippingOrderDao == null) {
      shippingOrderDao = ServiceLocatorFactory.getService(ShippingOrderDao.class);
    }
    return shippingOrderDao;
  }

  public OrderDao getOrderDao() {
    if (orderDao == null) {
      orderDao = ServiceLocatorFactory.getService(OrderDao.class);
    }
    return orderDao;
  }

  public SkuItemLineItemDao getSkuItemLineItemDao() {
    if (skuItemLineItemDao == null) {
      skuItemLineItemDao = ServiceLocatorFactory.getService(SkuItemLineItemDao.class);
    }
    return skuItemLineItemDao;
  }
}
