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
  public List<SKUDetails> getSkuDetails(ProductVariant productVariant, Double mrp) {
    List<SKUDetails> skuDetailsList = new ArrayList<SKUDetails>();
    List<Sku> skuList = getSkuService().getSKUsForProductVariantAtServiceableWarehouses(productVariant);
    for (Sku sku : skuList) {
      SKUDetails skuDetails = new SKUDetails();
      skuDetails.setSku(sku);
      skuDetails.setPhyQty(getUncheckedOutUnits(Arrays.asList(sku), mrp));
      skuDetails.setCiQty(getCheckedInUnits(Arrays.asList(sku), mrp));
      skuDetails.setCiQtyUnderReview(getUnderReviewCheckedInUnits(Arrays.asList(sku), mrp));
      skuDetails.setBookedQty(getBookedUnits(Arrays.asList(sku), mrp));
      skuDetails.setBookedQtyUnderReview(getUnderReviewBookedUnits(Arrays.asList(sku), mrp));
      skuDetails.setUnbookedLIQty(getUnbookedLIUnits(Arrays.asList(sku), mrp));

      skuDetailsList.add(skuDetails);
    }
    return skuDetailsList;
  }

  @Override
  public Long getCheckedInUnits(List<Sku> skuList, Double mrp) {
    List<SkuItem> skuItems = getSkuItemDao().getSkuItems(skuList,
        Arrays.asList(EnumSkuItemStatus.Checked_IN.getId()),
        Arrays.asList(EnumSkuItemOwner.SELF.getId()), mrp, false);
    return skuItems != null && !skuItems.isEmpty() ? Long.valueOf(skuItems.size()) : 0L;
  }

  @Override
  public Long getUnderReviewCheckedInUnits(List<Sku> skuList, Double mrp) {
    List<SkuItem> skuItems = getSkuItemDao().getSkuItems(skuList,
        Arrays.asList(EnumSkuItemStatus.Checked_IN.getId()),
        Arrays.asList(EnumSkuItemOwner.SELF.getId()), mrp, true);
    return skuItems != null && !skuItems.isEmpty() ? Long.valueOf(skuItems.size()) : 0L;
  }

  @Override
  public Long getBookedUnits(List<Sku> skuList, Double mrp) {
    List<SkuItem> skuItems = getSkuItemDao().getSkuItems(skuList,
        Arrays.asList(EnumSkuItemStatus.TEMP_BOOKED.getId(), EnumSkuItemStatus.BOOKED.getId()),
        Arrays.asList(EnumSkuItemOwner.SELF.getId()), mrp, false);
    return skuItems != null && !skuItems.isEmpty() ? Long.valueOf(skuItems.size()) : 0L;
  }

  @Override
  public Long getUnderReviewBookedUnits(List<Sku> skuList, Double mrp) {
    List<SkuItem> skuItems = getSkuItemDao().getSkuItems(skuList,
        Arrays.asList(EnumSkuItemStatus.TEMP_BOOKED.getId(), EnumSkuItemStatus.BOOKED.getId()),
        Arrays.asList(EnumSkuItemOwner.SELF.getId()), mrp, true);
    return skuItems != null && !skuItems.isEmpty() ? Long.valueOf(skuItems.size()) : 0L;
  }

  @Override
  public Long getUncheckedOutUnits(List<Sku> skuList, Double mrp) {
    List<SkuItem> skuItems = getSkuItemDao().getSkuItems(skuList,
        Arrays.asList(EnumSkuItemStatus.Checked_IN.getId(), EnumSkuItemStatus.TEMP_BOOKED.getId(), EnumSkuItemStatus.BOOKED.getId()),
        Arrays.asList(EnumSkuItemOwner.SELF.getId()), mrp, false);
    return skuItems != null && !skuItems.isEmpty() ? Long.valueOf(skuItems.size()) : 0L;
  }

  @Override
  public Long getUnbookedCLIUnits(ProductVariant productVariant, Double mrp) {
    return getSkuItemLineItemDao().getUnbookedCLICount(productVariant);
  }

  @Override
  public Long getUnbookedLIUnits(List<Sku> skuList, Double mrp) {
    return getSkuItemLineItemDao().getUnbookedLICount(skuList, mrp);
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
  public Long getAvailableUnits(ProductVariant productVariant, Double mrp) {
    List<Sku> skuList = getSkuService().getSKUsForProductVariantAtServiceableWarehouses(productVariant);
    Long inUnits = getCheckedInUnits(skuList, mrp);
    Long unbookedCLIUnits = getUnbookedCLIUnits(productVariant, mrp);
    Long unbookedLIUnits = getUnbookedLIUnits(skuList, mrp);
    return inUnits - unbookedCLIUnits - unbookedLIUnits;
  }

  @Override
  public Long getUnderReviewUnits(ProductVariant productVariant, Double mrp) {
    List<Sku> skuList = getSkuService().getSKUsForProductVariantAtServiceableWarehouses(productVariant);
    List<SkuItem> skuItems = getSkuItemDao().getSkuItems(skuList,
        Arrays.asList(EnumSkuItemStatus.Checked_IN.getId(), EnumSkuItemStatus.TEMP_BOOKED.getId(), EnumSkuItemStatus.BOOKED.getId()),
        Arrays.asList(EnumSkuItemOwner.SELF.getId()), mrp, true);
    return skuItems != null && !skuItems.isEmpty() ? Long.valueOf(skuItems.size()) : 0L;
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
