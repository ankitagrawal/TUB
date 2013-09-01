package com.hk.impl.service.inventory;

import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.inventory.GoodsReceivedNote;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.inventory.ProductVariantInventory;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.*;
import com.hk.pact.dao.sku.SkuGroupDao;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.pact.dao.sku.SkuItemLineItemDao;
import com.hk.pact.service.inventory.SkuGroupService;
import com.hk.pact.service.inventory.SkuItemLineItemService;
import com.hk.pact.service.splitter.ShippingOrderProcessor;
import com.hk.service.ServiceLocatorFactory;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.constants.sku.EnumSkuItemOwner;

import org.hibernate.mapping.Array;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Arrays;

/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Dec 27, 2012
 * Time: 11:43:25 AM
 * To change this template use File | Settings | File Templates.
 */
@Service
public class SkuGroupServiceImpl implements SkuGroupService {

  @Autowired
  SkuGroupDao skuGroupDao;
  @Autowired
  SkuItemDao skuItemDao;
  @Autowired
  SkuItemLineItemService skuItemLineItemService;
  @Autowired
  SkuItemLineItemDao skuItemLineItemDao;
  
  private ShippingOrderProcessor shippingOrderProcessor;
  


  /*** SkuGroupDao Methods **/
  public List<SkuGroup> getAllCheckedInBatches(ProductVariant productVariant) {
    return skuGroupDao.getAllCheckedInBatches(productVariant);
  }

  public List<SkuGroup> getAllCheckedInBatches(Sku sku) {
    return skuGroupDao.getAllCheckedInBatches(sku);
  }

  public SkuGroup getInStockSkuGroup(String barcode, Long warehouseId, List<SkuItemStatus> skuItemStatusIds) {
    return skuGroupDao.getInStockSkuGroup(barcode, warehouseId, skuItemStatusIds);
  }

    public SkuGroup getForeignSkuGroup(Long warehouseId, Long foreignSkuGroupId) {
      return skuGroupDao.getForeignSkuGroup(warehouseId, foreignSkuGroupId);
    }

  public List<SkuGroup> getCurrentCheckedInBatchGrn(GoodsReceivedNote grn, Sku sku) {
    return skuGroupDao.getCurrentCheckedInBatchGrn(grn, sku);
  }

  public List<SkuGroup> getCurrentCheckedInBatchNotInGrn(GoodsReceivedNote grn, Sku sku) {
    return skuGroupDao.getCurrentCheckedInBatchNotInGrn(grn, sku);
  }

  public List<SkuGroup> getSkuGroup(String barcode, Long warehouseId) {
    return skuGroupDao.getSkuGroup(barcode, warehouseId);
  }

  public List<SkuGroup> getSkuGroupByGrnLineItem(GrnLineItem grnLineItem) {
    return skuGroupDao.getSkuGroupByGrnLineItem(grnLineItem);
  }

  @Transactional
  public void deleteSkuGroup(SkuGroup skuGroup) {
    skuGroupDao.delete(skuGroup);
  }

  public List<SkuGroup> getAllInStockSkuGroups(Sku sku) {
    return skuGroupDao.getAllInStockSkuGroups(sku);
  }


  /**
   * SkuItemDao Methods *
   */
  public List<SkuItem> getInStockSkuItems(SkuGroup skuGroup) {
    return skuItemDao.getInStockSkuItems(skuGroup);
  }

  public List<SkuItem> getInStockSkuItems(SkuGroup skuGroup, List<SkuItemStatus> skuItemStatus) {
    return (List<SkuItem>) skuItemDao.getInStockSkuItems(skuGroup, skuItemStatus);
  }

  public SkuItem getSkuItem(SkuGroup skuGroup, List<SkuItemStatus> skuItemStatusList) {
    return skuItemDao.getSkuItem(skuGroup, skuItemStatusList);
  }

  public List<SkuGroup> getSkuGroupsByBarcodeForStockTransfer(String barcode, Long warehouseId) {
    return skuGroupDao.getSkuGroupsByBarcodeForStockTransfer(barcode, warehouseId);
  }


  public SkuItem getSkuItemByBarcode(String barcode, Long warehouseId, Long statusId) {
    return skuItemDao.getSkuItemByBarcode(barcode, warehouseId, statusId);
  }

  public SkuItem getSkuItemByBarcode(String barcode, Long warehouseId, List<SkuItemStatus> skuItemStatusList, List<SkuItemOwner> skuItemOwners) {
    return skuItemDao.getSkuItemByBarcode(barcode, warehouseId, skuItemStatusList, skuItemOwners);
  }


  public SkuItem saveSkuItem(SkuItem skuItem) {
    return (SkuItem) skuItemDao.save(skuItem);
  }

  @Transactional
  public void deleteAllSkuItemsOfSkuGroup(SkuGroup skuGroup) {
    Set<SkuItem> skuItemList = skuGroup.getSkuItems();
    skuGroup.setSkuItems(null);
    skuGroup = (SkuGroup) skuGroupDao.save(skuGroup);
    for (SkuItem skuItem : skuItemList) {
      skuItemDao.delete(skuItem);
    }
  }

  public List<SkuItem> getCheckedInSkuItems(Sku sku) {
    return skuItemDao.getSkuItems(Arrays.asList(sku),
        EnumSkuItemStatus.getCheckedInPlusBookedStatus(),
        Arrays.asList(EnumSkuItemOwner.SELF.getId()), null);
  }

  public List<SkuGroup> getAllCheckedInBatchForGrn(GoodsReceivedNote grn) {
    return skuGroupDao.getAllCheckedInBatchForGrn(grn);
  }

  public List<SkuItem> getSkuItems(List<Sku> skuList, List<Long> statusIds, List<Long> skuItemOwners, Double mrp) {
    return skuItemDao.getSkuItems(skuList, statusIds, skuItemOwners, mrp);
  }
  
  public SkuItem getSkuItemByBarcode(String barcode){
  	return skuItemDao.getSkuItemByBarcode(barcode);
  }
  
  @Override
	public void updateBookingAfterCheckin(SkuItem skuItem, SkuGroup skuGroup) {

		ForeignSkuItemCLI foreignSkuItemCLI = skuItem.getForeignSkuItemCLI();
		if (foreignSkuItemCLI.getProcessedStatus().equalsIgnoreCase("REFUSED")) {
			// just Checkin the SI
			skuItem.setSkuItemStatus(EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
			skuItem = (SkuItem) skuItemDao.save(skuItem);
			//TODO: Put Shipping Order in some Queue.
		} else {
			skuItem.setSkuItemStatus(EnumSkuItemStatus.BOOKED.getSkuItemStatus());
			skuItem = (SkuItem) skuItemDao.save(skuItem);

			// check if SO is escalable.
			SkuItemLineItem skuItemLineItem = skuItemLineItemDao.getSkuItemLineItem(skuItem);
			ShippingOrder shippingOrder = skuItemLineItem.getLineItem().getShippingOrder();
			Set<LineItem> lineItems = shippingOrder.getLineItems();
			boolean escalable = true;
			List<SkuItemLineItem> skuItemLineItems = new ArrayList<SkuItemLineItem>();
			for (LineItem lineItem : lineItems) {
				if (lineItem.getSkuItemLineItems() != null && lineItem.getSkuItemLineItems().size() > 0) {
					skuItemLineItems.addAll(lineItem.getSkuItemLineItems());
				} else {
					escalable = false;
					break;
				}
			}
			if (escalable) {
				for (SkuItemLineItem item : skuItemLineItems) {
					if (!item.getSkuItem().getSkuItemStatus().getId().equals(EnumSkuItemStatus.BOOKED.getId())) {
						escalable = false;
						break;
					}
				}
			}
			if (escalable) {
				getShippingOrderProcessor().manualEscalateShippingOrder(shippingOrder);
			}
		}
	}
  
  public ShippingOrderProcessor getShippingOrderProcessor() {
    if (shippingOrderProcessor == null) {
    	shippingOrderProcessor = ServiceLocatorFactory.getService(ShippingOrderProcessor.class);
    }
    return shippingOrderProcessor;
}
}
