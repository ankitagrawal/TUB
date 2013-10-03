package com.hk.impl.service.inventory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hk.constants.core.Keys;
import com.hk.constants.order.EnumUnitProcessedStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.sku.EnumSkuItemOwner;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.domain.api.HKAPIBookingInfo;
import com.hk.domain.api.HKAPIForeignBookingResponseInfo;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.ReplacementOrder;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.*;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.dao.sku.SkuItemDao;
import com.hk.pact.dao.sku.SkuItemLineItemDao;
import com.hk.pact.service.core.WarehouseService;
import com.hk.pact.service.inventory.InventoryHealthService;
import com.hk.pact.service.inventory.SkuItemLineItemService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.service.ServiceLocatorFactory;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Nihal
 * Date: 7/24/13
 * Time: 12:17 AM
 * To change this template use File | Settings | File Templates.
 */

@Service
public class SkuItemLineItemServiceImpl implements SkuItemLineItemService {

  @Autowired
  SkuItemLineItemDao skuItemLineItemDao;
  @Autowired
  SkuItemDao skuItemDao;
  @Autowired
  LineItemDao lineItemDao;
  @Autowired
  SkuService skuService;
  @Autowired
  BaseDao baseDao;
  @Autowired
  WarehouseService warehouseService;
  
  ShippingOrderService shippingOrderService;

  private InventoryHealthService inventoryHealthService;

  private Logger logger = LoggerFactory.getLogger(SkuItemLineItemServiceImpl.class);

  @Value("#{hkEnvProps['" + Keys.Env.brightlifecareRestUrl + "']}")
  private String brightlifecareRestUrl;

  @Override
  public List<SkuItemLineItem> getSkuItemLineItem(LineItem lineItem, Long skuItemStatusId) {
    return getSkuItemLineItemDao().getSkuItemLineItem(lineItem, skuItemStatusId);
  }

  @Override
  public SkuItemLineItem getById(Long skuItemLineItemId) {
    return getSkuItemDao().get(SkuItemLineItem.class, skuItemLineItemId);
  }


  public Boolean createNewSkuItemLineItemForFlipping(CartLineItem cartLineItem){
   LineItem lineItem =  lineItemDao.getLineItem(cartLineItem);
    List<SkuItemCLI> skuItemCLIs = cartLineItem.getSkuItemCLIs();
    for (SkuItemCLI skuItemCLI : skuItemCLIs){
      SkuItemLineItem skuItemLineItem = new SkuItemLineItem();
      SkuItem skuItem = skuItemCLI.getSkuItem();
      if(skuItem.getSkuItemStatus().getId().equals(EnumSkuItemStatus.EXPECTED_CHECKED_IN.getId())){
        skuItem.setSkuItemStatus(skuItem.getSkuItemStatus());
      }else{
        skuItem.setSkuItemStatus(EnumSkuItemStatus.BOOKED.getSkuItemStatus());
      }
      baseDao.save(skuItem);
      skuItemLineItem.setSkuItem(skuItemCLI.getSkuItem());
      skuItemLineItem.setLineItem(lineItem);
      skuItemLineItem.setProductVariant(skuItemCLI.getProductVariant());
      skuItemLineItem.setUnitNum(skuItemCLI.getUnitNum());
      skuItemLineItem.setCreateDate(new Date());
      skuItemLineItem.setSkuItemCLI(skuItemCLI);
      baseDao.save(skuItemLineItem);

    }

    return true;
  }


  @Override
  public Boolean createNewSkuItemLineItem(LineItem lineItem) {
    logger.debug("entering createNewSkuItemLineItem");
    Long unitNum = 0L;
    CartLineItem cartLineItem = lineItem.getCartLineItem();
    unitNum = 0L;
    if (!(lineItem.getShippingOrder() instanceof ReplacementOrder) && (cartLineItem.getSkuItemCLIs() == null || cartLineItem.getSkuItemCLIs().size() <= 0)) {
      return false;
    }

    if (lineItem.getShippingOrder() instanceof ReplacementOrder) {

      freeBookingItem(lineItem.getCartLineItem().getId());
      boolean roStatus = inventoryHealthService.bookInventoryForReplacementOrder(lineItem);
      return roStatus;
      /*
      boolean createSkuCLIFlag = false;
      logger.debug("instance of ro true");
      List<Sku> skuList = new ArrayList<Sku>();
      List<Long> skuStatusIdList = new ArrayList<Long>();
      List<Long> skuItemOwnerList = new ArrayList<Long>();

      skuStatusIdList.add(EnumSkuItemStatus.Checked_IN.getId());
      skuList.add(lineItem.getSku());
      skuItemOwnerList.add(EnumSkuItemOwner.SELF.getId());

      //get available sku items of the given warehouse at given mrp
      List<SkuItem> availableUnbookedSkuItems = getSkuItemDao().getSkuItems(skuList, skuStatusIdList, skuItemOwnerList, lineItem.getMarkedPrice());
      if (availableUnbookedSkuItems == null || availableUnbookedSkuItems.isEmpty() || availableUnbookedSkuItems.size() == 0
          || availableUnbookedSkuItems.size() < lineItem.getQty()) {
        logger.debug("about to return false from createNewSkuItemLineItem");
        return false;
      }

      if (lineItem.getCartLineItem().getSkuItemCLIs() == null || lineItem.getCartLineItem().getSkuItemCLIs().size() == 0) {
        createSkuCLIFlag = true;
      }

      for (int i = 1; i <= lineItem.getQty(); i++) {
        unitNum++;
        SkuItemLineItem skuItemLineItem = new SkuItemLineItem();
        SkuItem skuItem = availableUnbookedSkuItems.get(i - 1);
        //Book the sku item first
        skuItem.setSkuItemStatus(EnumSkuItemStatus.BOOKED.getSkuItemStatus());
        logger.debug("saving si to booked in replacement order ->  " + skuItem.getId());
        skuItem = (SkuItem) getSkuItemDao().save(skuItem);

        if (createSkuCLIFlag) {
          SkuItemCLI skuItemCLI = new SkuItemCLI();
          skuItemCLI.setSkuItem(skuItem);
          skuItemCLI.setCartLineItem(lineItem.getCartLineItem());
          skuItemCLI.setUnitNum(unitNum);
          skuItemCLI.setCreateDate(new Date());
          skuItemCLI.setProductVariant(lineItem.getSku().getProductVariant());
          skuItemCLI = (SkuItemCLI) baseDao.save(skuItemCLI);
          skuItemLineItem.setSkuItemCLI(skuItemCLI);

        } else {
          skuItemLineItem.setSkuItemCLI(cartLineItem.getSkuItemCLIs().get(i - 1));
        }

        //create skuItemLineItem entry
        skuItemLineItem.setSkuItem(skuItem);
        skuItemLineItem.setLineItem(lineItem);
        skuItemLineItem.setUnitNum(unitNum);

        skuItemLineItem.setProductVariant(skuItem.getSkuGroup().getSku().getProductVariant());
        logger.debug("saving sili in replacement order ");
        skuItemLineItem = save(skuItemLineItem);

      }
      return true;
      */

    } else {
      logger.debug("entering normal enter -> ");
      List<SkuItemLineItem> skuItemLineItems = new ArrayList<SkuItemLineItem>();
      for (SkuItemCLI skuItemCLI : cartLineItem.getSkuItemCLIs()) {
        unitNum++;
        SkuItemLineItem skuItemLineItem = new SkuItemLineItem();
        if (lineItem.getShippingOrder().getWarehouse().equals(skuItemCLI.getSkuItem().getSkuGroup().getSku().getWarehouse())) {
          logger.debug("Creating sku_item_line_item without swapping warehouse (same wh before and after split)");

          //Make skuItemLine item as copy of skuItemCLI
          skuItemLineItem.setSkuItem(skuItemCLI.getSkuItem());
          skuItemLineItem.setLineItem(lineItem);
          skuItemLineItem.setUnitNum(unitNum);
          skuItemLineItem.setSkuItemCLI(skuItemCLI);
          skuItemLineItem.setProductVariant(skuItemCLI.getSkuItem().getSkuGroup().getSku().getProductVariant());

          if(skuItemCLI.getSkuItem().getSkuItemStatus().getId().equals(EnumSkuItemStatus.EXPECTED_CHECKED_IN.getId())) {
            skuItemLineItem.getSkuItem().setSkuItemStatus(skuItemCLI.getSkuItem().getSkuItemStatus());
          }else {
            skuItemLineItem.getSkuItem().setSkuItemStatus(EnumSkuItemStatus.BOOKED.getSkuItemStatus());
          }
          //Book the sku item


          getSkuItemDao().save(skuItemLineItem.getSkuItem());

        } else {
          logger.debug("Creating sku_item_line_item. Wh after split was diff than before split booking.");
          List<Sku> skuList = new ArrayList<Sku>();
          List<Long> skuStatusIdList = new ArrayList<Long>();
          List<Long> skuItemOwnerList = new ArrayList<Long>();

          skuStatusIdList.add(EnumSkuItemStatus.Checked_IN.getId());
          skuList.add(lineItem.getSku());
          skuItemOwnerList.add(EnumSkuItemOwner.SELF.getId());

          //get available sku items of the given warehouse at given mrp
          List<SkuItem> availableUnbookedSkuItems = getSkuItemDao().getSkuItems(skuList, skuStatusIdList, skuItemOwnerList, lineItem.getMarkedPrice());
          if (availableUnbookedSkuItems == null || availableUnbookedSkuItems.isEmpty() || availableUnbookedSkuItems.size() == 0) {
            logger.debug("about to return false from createNewSkuItemLineItem for normal case");
            return false;
          }

          //Free existing skuitem on skuItemCLI
          SkuItem oldItem = skuItemCLI.getSkuItem();
          boolean foreignBooking = false;
          if(skuItemCLI.getSkuItem().getSkuItemStatus().getId().equals(EnumSkuItemStatus.EXPECTED_CHECKED_IN.getId())) {
            foreignBooking = true;
          }
          oldItem.setSkuItemStatus(EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
          getSkuItemDao().save(oldItem);

          SkuItem skuItem = availableUnbookedSkuItems.get(0);
          //Book the sku item first
          if (foreignBooking) {
            skuItem.setSkuItemStatus(EnumSkuItemStatus.EXPECTED_CHECKED_IN.getSkuItemStatus());
          } else {
            skuItem.setSkuItemStatus(EnumSkuItemStatus.BOOKED.getSkuItemStatus());
          }

          logger.debug("savingg skuItem for normal orders ");
          skuItem = (SkuItem) getSkuItemDao().save(skuItem);
          //create skuItemLineItem entry
          skuItemLineItem.setSkuItem(skuItem);
          skuItemLineItem.setLineItem(lineItem);
          skuItemLineItem.setUnitNum(unitNum);
          skuItemLineItem.setSkuItemCLI(skuItemCLI);
          skuItemLineItem.setProductVariant(skuItem.getSkuGroup().getSku().getProductVariant());

          //save the state
          logger.debug("savingg sili for normal orders ");


          //set new sku item on skuItemCLI as well
          skuItemCLI.setSkuItem(skuItem);
          skuItemCLI = (SkuItemCLI) getSkuItemDao().save(skuItemCLI);
          skuItemLineItem.setSkuItemCLI(skuItemCLI);
        }
        skuItemLineItems.add(skuItemLineItem);

      }
      lineItem.setSkuItemLineItems(skuItemLineItems);
      lineItem = (LineItem) getLineItemDao().save(lineItem);

    }
    return true;
  }



// This method not in use for now ,, will delete once testing done
  public boolean isWarehouseBeFlippableAB(ShippingOrder shippingOrder, Warehouse targetWarehouse) {
    boolean itemsWasBookedAtAqua = true;
    for (LineItem lineItem : shippingOrder.getLineItems()) {
      Sku sku = getSkuService().getSKU(lineItem.getSku().getProductVariant(), targetWarehouse);

      List<ForeignSkuItemCLI> fsclis = lineItem.getCartLineItem().getForeignSkuItemCLIs();
      if (fsclis != null && fsclis.size() > 0) {
        itemsWasBookedAtAqua = false;
      }
      List<SkuItem> itemsToBeFreed = getSkuItemsTobeFreed(lineItem, Arrays.asList(sku));
      if (!lineItem.getCartLineItem().getProductVariant().getProduct().isJit()) {
        if (itemsToBeFreed == null || itemsToBeFreed.size() <= 0) {
          return false;
        }
      }
      if (itemsWasBookedAtAqua) {
        for (SkuItem skuItem : itemsToBeFreed) {
          skuItem.setSkuItemStatus(EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
          baseDao.save(skuItem);
        }
      } else {
        List<HKAPIForeignBookingResponseInfo> infos = freeBrightInventoryForSOCancellation(lineItem);
        if (infos == null || infos.size() <= 0) {
          return false;
        }
        itemsToBeFreed = updateForeignSICLIForCancelledOrder(infos);
        baseDao.deleteAll(itemsToBeFreed);

      }
    }
    return true;
  }

  @Override
  public boolean isWarehouseBeFlippable(ShippingOrder shippingOrder, Warehouse targetWarehouse) {
    SkuItemLineItemService skuItemLineItemService = ServiceLocatorFactory.getService(SkuItemLineItemService.class);

    List<Warehouse> warehouses = warehouseService.findWarehousesByPrefix(targetWarehouse.getTinPrefix());
    warehouses.remove(targetWarehouse);
    Long warehouseIdForBright = warehouses.get(0).getId();
    for (LineItem lineItem : shippingOrder.getLineItems()) {

      CartLineItem cartLineItem = lineItem.getCartLineItem();
      Product product = cartLineItem.getProductVariant().getProduct();
      Sku sku = skuService.getSKU(cartLineItem.getProductVariant(),targetWarehouse);
      lineItem.setSku(sku);
      lineItem = (LineItem)baseDao.save(lineItem);

      boolean productType = (product.isJit() || product.isDropShipping() || product.isService());
      skuItemLineItemService.freeBookingItem(cartLineItem.getId());
      Map<String, Long> invMap = getInventoryHealthService().getInventoryCountOfAB(lineItem.getCartLineItem(), targetWarehouse);
      if (invMap.get("aquaInventory") >= lineItem.getQty()) {
        cartLineItem = (CartLineItem)getInventoryHealthService().tempBookAquaInventory(cartLineItem, targetWarehouse.getId());
//        createNewSkuItemLineItem(lineItem);
         createNewSkuItemLineItemForFlipping(cartLineItem);
      } else if (invMap.get("brtInventory") >= lineItem.getQty()) {
        getInventoryHealthService().createSicliAndSiliAndTempBookingForBright(lineItem.getCartLineItem(), warehouseIdForBright);

      } else if (!productType) {
        return false;
      }
    }

    return true;
  }


  private List<SkuItem> getSkuItemsTobeFreed(LineItem lineItem, List<Sku> skuList) {

    List<SkuItem> availableUnbookedSkuItems = null;
    List<SkuItem> toBeFreedSkuItemList = new ArrayList<SkuItem>();

    List<SkuItemLineItem> skuItemLineItems = lineItem.getSkuItemLineItems();
    if (skuItemLineItems != null && skuItemLineItems.size() > 0) {
      availableUnbookedSkuItems = getSkuItemDao().getSkuItems(skuList, Arrays.asList(EnumSkuItemStatus.Checked_IN.getId()), Arrays.asList(EnumSkuItemOwner.SELF.getId()), lineItem.getMarkedPrice());
      if (availableUnbookedSkuItems != null && availableUnbookedSkuItems.size() >= lineItem.getQty()) {
        for (SkuItemLineItem skuItemLineItem : skuItemLineItems) {
          SkuItem toBeFreedSkuItem = skuItemLineItem.getSkuItem();
          SkuItem skuItem = availableUnbookedSkuItems.get(skuItemLineItem.getUnitNum().intValue() - 1);
          skuItem.setSkuItemStatus(EnumSkuItemStatus.BOOKED.getSkuItemStatus());
          skuItemLineItem.setSkuItem(skuItem);
          skuItemLineItem.getSkuItemCLI().setSkuItem(skuItem);
          toBeFreedSkuItemList.add(toBeFreedSkuItem);
        }
        getSkuItemLineItemDao().saveOrUpdate(skuItemLineItems);
      }
    }
    return toBeFreedSkuItemList;
  }


  public Boolean freeInventoryForSOCancellation(ShippingOrder shippingOrder) {
    List<SkuItem> skuItemsToBeFreed = new ArrayList<SkuItem>();
    List<SkuItemLineItem> skuItemLineItemsToBeDeleted = new ArrayList<SkuItemLineItem>();
    List<SkuItemCLI> skuItemCLIsToBeDeleted = new ArrayList<SkuItemCLI>();
    List<SkuItem> skuItemsToBeDeleted = new ArrayList<SkuItem>();
    Set<LineItem> lineItems = shippingOrder.getLineItems();
    for (LineItem lineItem : lineItems) {

      CartLineItem cartLineItem = lineItem.getCartLineItem();
      if (bookedOnBright(cartLineItem)) {
        logger.debug("Update booking on Bright");
        List<HKAPIForeignBookingResponseInfo> infos = freeBrightInventoryForSOCancellation(lineItem);

        skuItemsToBeDeleted.addAll(updateForeignSICLIForCancelledOrder(infos));
      }

      List<SkuItemLineItem> skuItemLineItems = lineItem.getSkuItemLineItems();
      if (skuItemLineItems != null && skuItemLineItems.size() > 0) {
        for (SkuItemLineItem skuItemLineItem : skuItemLineItems) {
          SkuItem skuItem = skuItemLineItem.getSkuItem();
          skuItem.setSkuItemStatus(EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
          skuItem = (SkuItem) getSkuItemDao().save(skuItem);
          skuItemsToBeFreed.add(skuItem);
        }
        for (SkuItemCLI skuItemCLI : lineItem.getCartLineItem().getSkuItemCLIs()) {
          skuItemCLI.setSkuItemLineItems(null);
          skuItemCLI = (SkuItemCLI) getSkuItemDao().save(skuItemCLI);
          skuItemCLIsToBeDeleted.add(skuItemCLI);
        }

        skuItemLineItemsToBeDeleted.addAll(lineItem.getSkuItemLineItems());
      }
    }
    for (LineItem lineItem : shippingOrder.getLineItems()) {
      CartLineItem cartLineItem = lineItem.getCartLineItem();
      cartLineItem.setSkuItemCLIs(null);
      cartLineItem = (CartLineItem) baseDao.save(cartLineItem);
      lineItem.setSkuItemLineItems(null);
      lineItem.setCartLineItem(cartLineItem);
      lineItem = (LineItem) baseDao.save(lineItem);
    }
    for (Iterator<SkuItemLineItem> iterator = skuItemLineItemsToBeDeleted.iterator(); iterator.hasNext(); ) {
      SkuItemLineItem skuItemLineItem = (SkuItemLineItem) iterator.next();
      baseDao.delete(skuItemLineItem);
      iterator.remove();
    }
    for (Iterator<SkuItemCLI> iterator = skuItemCLIsToBeDeleted.iterator(); iterator.hasNext(); ) {
      SkuItemCLI skuItemCLI = (SkuItemCLI) iterator.next();
      baseDao.delete(skuItemCLI);
      iterator.remove();
    }
    baseDao.deleteAll(skuItemsToBeDeleted);
    return true;
  }

  private boolean bookedOnBright(CartLineItem cartLineItem) {
    try {

      String url = brightlifecareRestUrl + "product/variant/getBookingForCartLineItemId/" + cartLineItem.getId();
      ClientRequest request = new ClientRequest(url);
      ClientResponse response = request.get();
      int status = response.getStatus();
      if (status == 200) {
        String data = (String) response.getEntity(String.class);
        Boolean bookedAtBright = new Gson().fromJson(data, Boolean.class);
        return bookedAtBright;
      }
    } catch (Exception e) {
      logger.error("Exception while checking booking status on Bright", e.getMessage());
    }
    return false;
  }

  private List<HKAPIForeignBookingResponseInfo> freeBrightInventoryForSOCancellation(LineItem lineItem) {
    List<HKAPIForeignBookingResponseInfo> infos = null;
    try {
      CartLineItem cartLineItem = lineItem.getCartLineItem();
      List<ForeignSkuItemCLI> foreignSkuItemCLIs = cartLineItem.getForeignSkuItemCLIs();
      List<Long> fsicliIds = new ArrayList<Long>();

      for (ForeignSkuItemCLI foreignSkuItemCLI : foreignSkuItemCLIs) {
        fsicliIds.add(foreignSkuItemCLI.getId());
      }

      Gson gson = new Gson();
      String json = gson.toJson(fsicliIds);

      String url = brightlifecareRestUrl + "product/variant/" + "freeBrightInventoryForSOCancellation/";
      ClientRequest request = new ClientRequest(url);
      request.body("application/json", json);
      ClientResponse response = request.post();
      int status = response.getStatus();
      if (status == 200) {
        String data = (String) response.getEntity(String.class);
        Type listType = new TypeToken<List<HKAPIForeignBookingResponseInfo>>() {
        }.getType();
        infos = new Gson().fromJson(data, listType);
        logger.debug("Successfully freed Bright Inventory against SO# " + lineItem.getShippingOrder().getId() + " cancellation");
        return infos;
      }
      logger.debug("Could not free Bright Inventory against SO# " + lineItem.getShippingOrder().getId() + " cancellation");
      return infos;
    } catch (Exception e) {
      logger.error("Exception while freeing bright inventory against cancelling SO# " + lineItem.getShippingOrder().getId(), e.getMessage());
      return infos;
    }
  }

  @Override
  public SkuItemLineItem save(SkuItemLineItem skuItemLineItem) {
    return (SkuItemLineItem) getSkuItemDao().save(skuItemLineItem);
  }

  @Override
  public List<SkuItemLineItem> getSkuItemLineItemForLineItem(LineItem lineItem) {
    return null;  //To change body of implemented methods use File | Settings | File Templates.
  }

  public Boolean freeBookingTable(ShippingOrder shippingOrder) {
    logger.debug("Going to free bookings against Shipping Order:- " + shippingOrder.getId());
    List<SkuItem> skuItemsToBeFreed = new ArrayList<SkuItem>();
    List<SkuItemLineItem> skuItemLineItemsToBeDeleted = new ArrayList<SkuItemLineItem>();
    List<SkuItemCLI> skuItemCLIsToBeDeleted = new ArrayList<SkuItemCLI>();
    List<SkuItemLineItem> skuItemLineItemsToBeRetained = new ArrayList<SkuItemLineItem>();
    List<SkuItemCLI> skuItemCLIsToBeRetained = new ArrayList<SkuItemCLI>();

		Set<LineItem> lineItems = shippingOrder.getLineItems();
		for (LineItem lineItem : lineItems) {
			for (SkuItemLineItem skuItemLineItem : lineItem.getSkuItemLineItems()) {
				SkuItem skuItem = skuItemLineItem.getSkuItem();
				if (skuItem.getSkuItemStatus().getId().equals(EnumSkuItemStatus.BOOKED.getId())
						|| skuItem.getSkuItemStatus().getId().equals(EnumSkuItemStatus.TEMP_BOOKED.getId())
            || skuItem.getSkuItemStatus().getId().equals(EnumSkuItemStatus.Checked_IN.getId())) {
					skuItem.setSkuItemStatus(EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
					skuItem = (SkuItem) getSkuItemDao().save(skuItem);
					skuItemsToBeFreed.add(skuItem);
					SkuItemCLI skuItemCLI = skuItemLineItem.getSkuItemCLI();
					skuItemCLI.setSkuItemLineItems(null);
					skuItemCLI = (SkuItemCLI) getSkuItemDao().save(skuItemCLI);
					skuItemCLIsToBeDeleted.add(skuItemCLI);
					skuItemLineItemsToBeDeleted.add(skuItemLineItem);
				} else {
					skuItemLineItemsToBeRetained.add(skuItemLineItem);
					skuItemCLIsToBeRetained.add(skuItemLineItem.getSkuItemCLI());
				}
			}
		}
		for (LineItem lineItem : shippingOrder.getLineItems()) {
			CartLineItem cartLineItem = lineItem.getCartLineItem();
			if (skuItemCLIsToBeRetained != null && skuItemCLIsToBeRetained.size() > 0) {
				cartLineItem.setSkuItemCLIs(skuItemCLIsToBeRetained);
			} else {
				cartLineItem.setSkuItemCLIs(null);
			}
			cartLineItem = (CartLineItem) baseDao.save(cartLineItem);
			if (skuItemLineItemsToBeRetained != null && skuItemLineItemsToBeRetained.size() > 0) {
				lineItem.setSkuItemLineItems(skuItemLineItemsToBeRetained);
			} else {
				lineItem.setSkuItemLineItems(null);
			}
			lineItem.setCartLineItem(cartLineItem);
			lineItem = (LineItem) baseDao.save(lineItem);
		}
		for (Iterator<SkuItemLineItem> iterator = skuItemLineItemsToBeDeleted.iterator(); iterator.hasNext(); ) {
			SkuItemLineItem skuItemLineItem = (SkuItemLineItem) iterator.next();
			baseDao.delete(skuItemLineItem);
			iterator.remove();
		}
		for (Iterator<SkuItemCLI> iterator = skuItemCLIsToBeDeleted.iterator(); iterator.hasNext(); ) {
			SkuItemCLI skuItemCLI = (SkuItemCLI) iterator.next();
			baseDao.delete(skuItemCLI);
			iterator.remove();
		}
    getShippingOrderService().logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_LoggedComment, null, "Inventory freed for SO.");
		return true;
	}


  public boolean swappingForBrightBooking(CartLineItem cartLineItem) {

    LineItem item = lineItemDao.getLineItem(cartLineItem);
    List<Sku> skuList = Arrays.asList(item.getSku());
    Warehouse aquaWarehouse = item.getSku().getWarehouse();
    String tinPrefix = aquaWarehouse.getTinPrefix();
    List<Warehouse> warehouses = warehouseService.findWarehousesByPrefix(tinPrefix);
    warehouses.remove(aquaWarehouse);
    Long warehousIdForAqua = aquaWarehouse.getId();
    Long wareHouseIdForBright = warehouses.get(0).getId();
    List<Long> skuStatusIdList = Arrays.asList(EnumSkuItemStatus.Checked_IN.getId());
    List<Long> skuItemOwnerList = Arrays.asList(EnumSkuItemOwner.SELF.getId());
    List<SkuItemLineItem> skuItemLineItems = item.getSkuItemLineItems();
    List<SkuItemStatus> skuItemStatus = new ArrayList<SkuItemStatus>();
    skuItemStatus.add(EnumSkuItemStatus.BOOKED.getSkuItemStatus());
    skuItemStatus.add(EnumSkuItemStatus.Checked_OUT.getSkuItemStatus());
    skuItemStatus.add(EnumSkuItemStatus.EXPECTED_CHECKED_IN.getSkuItemStatus());

    Map<String, Long> invMap = getInventoryHealthService().getInventoryCountOfAB(cartLineItem,null);
    Long aquaInventoryCount = invMap.get("aquaInventory");
    if (aquaInventoryCount >= cartLineItem.getQty()) {
      logger.debug("Aqua inventory count in swapping For Bright Booking for cartlineItem Id  : " + cartLineItem.getId() + " for variant :" + cartLineItem.getProductVariant().getId() + " is " + aquaInventoryCount + "for Marked price : " + item.getMarkedPrice());
      List<SkuItem> availableUnbookedSkuItems = skuItemDao.getSkuItems(skuList, skuStatusIdList, skuItemOwnerList, item.getMarkedPrice());
      if (availableUnbookedSkuItems != null && availableUnbookedSkuItems.size() > 0) {

        boolean incorrectMrpItem = false;
        for (SkuItemLineItem skuItemLineItem : skuItemLineItems) {
          if (!skuItemLineItem.getSkuItem().getSkuGroup().getMrp().equals(item.getMarkedPrice()) || !skuItemStatus.contains(skuItemLineItem.getSkuItem().getSkuItemStatus())) {
            incorrectMrpItem = true;
          }
        }
        if (incorrectMrpItem) {
         freeBookingInventoryAtBright(cartLineItem);
        }
        if (cartLineItem.getSkuItemCLIs() == null || cartLineItem.getSkuItemCLIs().size() < 1) {
          cartLineItem = getInventoryHealthService().tempBookAquaInventory(cartLineItem, warehousIdForAqua);
        }
        if (item.getSkuItemLineItems() == null || item.getSkuItemLineItems().size() < 1) {
          createNewSkuItemLineItem(item);
        }
      } else {
        return false;
      }
    } else {
      Long brtInventoryCount = invMap.get("brtInventory");
      List<SkuItem> skuItemsToBeRemoved = new ArrayList<SkuItem>();
      if (brtInventoryCount >= cartLineItem.getQty()) {
        logger.debug("bright  inventory count in swapping For bright  Booking for cartlineItem Id  : " + cartLineItem.getId() + " for variant :" + cartLineItem.getProductVariant().getId() + " is :" + brtInventoryCount + "for Marked price : " + item.getMarkedPrice());
        boolean incorrectMrpItem = false;
        for (SkuItemLineItem skuItemLineItem : skuItemLineItems) {
          if (!skuItemLineItem.getSkuItem().getSkuGroup().getMrp().equals(item.getMarkedPrice()) || !skuItemStatus.contains(skuItemLineItem.getSkuItem().getSkuItemStatus())) {
            incorrectMrpItem = true;
            break;
          }
        }
        if (incorrectMrpItem) {
          freeBookingInventoryAtBright(cartLineItem);
          getInventoryHealthService().createSicliAndSiliAndTempBookingForBright(cartLineItem, wareHouseIdForBright);
        }
      } else {
        return false;
      }

    }
    return true;
  }


  public boolean freeBookingInventoryAtBright(CartLineItem cartLineItem) {
    // Assumption is deleting of all entries but  in future partial deletion
   // List<HKAPIForeignBookingResponseInfo> infos = freeBrightInventoryAgainstBOCancellation(cartLineItem);
    List<HKAPIForeignBookingResponseInfo> infos = freeBrightInventoryAgainstSoValidation(cartLineItem);
    if (infos != null) {
      Boolean status = isBrightItemCheckInAtAqua(cartLineItem);
      deleteSicliAndSili(cartLineItem);
      if (status) {
        freeFsiclis(infos, false);
      } else {
        freeFsiclis(infos, true);
      }
      logger.debug("Inventory freed for  booking at Bright for cartLineItem" + cartLineItem.getId());
      return true;
    }

    logger.debug("Failed to -Inventory freed for booking at Bright for cartLineItem" + cartLineItem.getId());
    return false;
  }

  public List<SkuItem> freeFsiclis(List<HKAPIForeignBookingResponseInfo> infos , Boolean isSkuItemsToBeDeleted) {
    List<SkuItem> skuItems = new ArrayList<SkuItem>();
    List<ForeignSkuItemCLI> fsiclis = new ArrayList<ForeignSkuItemCLI>();

    for (Iterator<HKAPIForeignBookingResponseInfo> iterator = infos.iterator(); iterator.hasNext(); ) {
      HKAPIForeignBookingResponseInfo info = (HKAPIForeignBookingResponseInfo) iterator.next();
      ForeignSkuItemCLI foreignSkuItemCLI = getForeignSkuItemCLI(info.getFsiCLIId());
      foreignSkuItemCLI.setProcessedStatus(info.getProcessed());
      SkuItem skuItem = getSkuItem(foreignSkuItemCLI.getId());
      if (skuItem != null) {
        skuItem.setForeignSkuItemCLI(null);
        skuItems.add(skuItem);
      }
      fsiclis.add(foreignSkuItemCLI);
      CartLineItem cartLineItem = foreignSkuItemCLI.getCartLineItem();
      cartLineItem.getForeignSkuItemCLIs().remove(foreignSkuItemCLI);
      baseDao.save(cartLineItem);
    }
    if (isSkuItemsToBeDeleted) {
      baseDao.deleteAll(skuItems);
    }
    baseDao.deleteAll(fsiclis);
    return skuItems;
  }


  public boolean deleteSicliAndSili(CartLineItem cartLineItem) {
    List<SkuItemCLI> skuItemCLIs = cartLineItem.getSkuItemCLIs();
    if (skuItemCLIs != null && skuItemCLIs.size() > 0) {
      List<SkuItem> skuItemsToBeFreed = new ArrayList<SkuItem>();
      List<SkuItemCLI> skuItemCLIsToBeDeleted = new ArrayList<SkuItemCLI>();
      List<SkuItemLineItem> skuItemLineItemsToBeDeleted = new ArrayList<SkuItemLineItem>();
      for (SkuItemCLI skuItemCLI : cartLineItem.getSkuItemCLIs()) {
        SkuItem skuItem = skuItemCLI.getSkuItem();
        skuItem.setSkuItemStatus(EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
        skuItemsToBeFreed.add(skuItem);
        if (skuItemCLI.getSkuItemLineItems() != null) {
          skuItemLineItemsToBeDeleted.addAll(skuItemCLI.getSkuItemLineItems());
        }
        skuItemCLI.setSkuItemLineItems(null);
      }
      skuItemCLIsToBeDeleted.addAll(cartLineItem.getSkuItemCLIs());
      lineItemDao.saveOrUpdate(skuItemsToBeFreed);
      cartLineItem.setSkuItemCLIs(null);
//      cartLineItem = (CartLineItem) lineItemDao.save(cartLineItem);
      LineItem lineItem = lineItemDao.getLineItem(cartLineItem);
      lineItem.setSkuItemLineItems(null);
      lineItemDao.save(lineItem);

      lineItemDao.deleteAll(skuItemLineItemsToBeDeleted);
      lineItemDao.deleteAll(skuItemCLIsToBeDeleted);
    }

    return true;

  }


  @Override
  public boolean sicliAlreadyExists(CartLineItem cartLineItem) {
    return getSkuItemLineItemDao().sicliAlreadyExists(cartLineItem);
  }

  @Override
  public ForeignSkuItemCLI getForeignSkuItemCLI(Long id) {
    return getSkuItemLineItemDao().getForeignSkuItemCLI(id);
  }

  public List<ForeignSkuItemCLI> getForeignSkuItemCli(CartLineItem cartLineItem) {
    return getSkuItemLineItemDao().getForeignSkuItemCli(cartLineItem);
  }

  public SkuItem getSkuItem(Long fsicliId) {
    return getSkuItemDao().getSkuItem(fsicliId);
  }


  public List<ForeignSkuItemCLI> updateSkuItemForABJit(List<HKAPIForeignBookingResponseInfo> infos) {
    List<ForeignSkuItemCLI> foreignSkuItemCLIs = new ArrayList<ForeignSkuItemCLI>();
    for (HKAPIForeignBookingResponseInfo info : infos) {
      long fsiliId = info.getFsiCLIId();
      ForeignSkuItemCLI foreignSkuItemCLI = getForeignSkuItemCLI(fsiliId);
      if (foreignSkuItemCLI != null) {
        foreignSkuItemCLI.setSkuItemId(info.getFsiId());
        foreignSkuItemCLI.setForeignBarcode(info.getBarcode());
        foreignSkuItemCLI = (ForeignSkuItemCLI) baseDao.save(foreignSkuItemCLI);

        SkuItem skuItem = getSkuItem(foreignSkuItemCLI.getId());
        skuItem.setBarcode(info.getBarcode());
        baseDao.save(skuItem);
        foreignSkuItemCLIs.add(foreignSkuItemCLI);
      }

    }
    return foreignSkuItemCLIs;
  }


  public void populateSILIForABJit(List<ForeignSkuItemCLI> foreignSkuItemCLIs, LineItem lineItem) {
   List <SkuItemLineItem> silis = new ArrayList<SkuItemLineItem>();
    for (ForeignSkuItemCLI foreignSkuItemCLI : foreignSkuItemCLIs) {
      SkuItemLineItem skuItemLineItem = new SkuItemLineItem();
      skuItemLineItem.setProductVariant(foreignSkuItemCLI.getProductVariant());
      skuItemLineItem.setUnitNum(foreignSkuItemCLI.getUnitNum());
      SkuItem skuItem = getSkuItem(foreignSkuItemCLI.getId());
      skuItemLineItem.setSkuItem(skuItem);
      skuItemLineItem.setLineItem(lineItem);
      SkuItemCLI skuItemCli = skuItemLineItemDao.getSkuItemCLI(foreignSkuItemCLI.getCartLineItem(), foreignSkuItemCLI.getUnitNum());
      skuItemLineItem.setSkuItemCLI(skuItemCli);
      skuItemLineItem.setCreateDate(new Date());
      skuItemLineItem =(SkuItemLineItem) baseDao.save(skuItemLineItem);
      silis.add(skuItemLineItem);
    }
     lineItem.setSkuItemLineItems(silis);
     baseDao.save(lineItem);
  }


  public List<SkuItem> updateForeignSICLIForCancelledOrder(List<HKAPIForeignBookingResponseInfo> infos) {
    List<SkuItem> skuItems = new ArrayList<SkuItem>();
    for (HKAPIForeignBookingResponseInfo info : infos) {
      ForeignSkuItemCLI foreignSkuItemCLI = getForeignSkuItemCLI(info.getFsiCLIId());
      foreignSkuItemCLI.setProcessedStatus(info.getProcessed());
      foreignSkuItemCLI = (ForeignSkuItemCLI) baseDao.save(foreignSkuItemCLI);
      SkuItem skuItem = getSkuItem(foreignSkuItemCLI.getId());
      skuItems.add(skuItem);
    }
    return skuItems;
  }


  public ForeignSkuItemCLI getFSICI(Long foreignSkuItemId) {
    return getSkuItemLineItemDao().getFSICI(foreignSkuItemId);
  }


  public List<HKAPIForeignBookingResponseInfo> freeBrightInventoryAgainstBOCancellation(CartLineItem cartLineItem) {
    List<HKAPIForeignBookingResponseInfo> infos = null;
    try {

      List<ForeignSkuItemCLI> foreignSkuItemCLIs = cartLineItem.getForeignSkuItemCLIs();
      List<Long> fsicliIds = new ArrayList<Long>();

      for (ForeignSkuItemCLI foreignSkuItemCLI : foreignSkuItemCLIs) {
        fsicliIds.add(foreignSkuItemCLI.getId());
      }
      Gson gson = new Gson();
      String json = gson.toJson(fsicliIds);

      String url = brightlifecareRestUrl + "product/variant/" + "freeBrightInventoryForSOCancellation/";
      ClientRequest request = new ClientRequest(url);
      request.body("application/json", json);
      ClientResponse response = request.post();
      int status = response.getStatus();
      if (status == 200) {
        String data = (String) response.getEntity(String.class);
        Type listType = new TypeToken<List<HKAPIForeignBookingResponseInfo>>() {
        }.getType();
        infos = new Gson().fromJson(data, listType);
        logger.debug("Successfully freed Bright Inventory against BO# " + cartLineItem.getOrder().getId() + " cancellation");
        return infos;
      }
      logger.debug("Could not free Bright Inventory against BO# " + cartLineItem.getOrder().getId() + " cancellation");
      return infos;
    } catch (Exception e) {
      logger.error("Exception while freeing bright inventory against cancelling SO# " + cartLineItem.getOrder().getId(), e.getMessage());
      return infos;
    }
  }



  public List<HKAPIForeignBookingResponseInfo> freeBrightInventoryAgainstSoValidation(CartLineItem cartLineItem) {
    List<HKAPIForeignBookingResponseInfo> infos = null;
    try {

      List<ForeignSkuItemCLI> foreignSkuItemCLIs = cartLineItem.getForeignSkuItemCLIs();
      List<Long> fsicliIds = new ArrayList<Long>();

      for (ForeignSkuItemCLI foreignSkuItemCLI : foreignSkuItemCLIs) {
        fsicliIds.add(foreignSkuItemCLI.getId());
      }
      Gson gson = new Gson();
      String json = gson.toJson(fsicliIds);

      String url = brightlifecareRestUrl + "product/variant/" + "freeBrightInventoryForSOValidation/";
      ClientRequest request = new ClientRequest(url);
      request.body("application/json", json);
      ClientResponse response = request.post();
      int status = response.getStatus();
      if (status == 200) {
        String data = (String) response.getEntity(String.class);
        Type listType = new TypeToken<List<HKAPIForeignBookingResponseInfo>>() {
        }.getType();
        infos = new Gson().fromJson(data, listType);
        logger.debug("Successfully freed Bright Inventory against So Validation# " + cartLineItem.getOrder().getId() + " cancellation");
        return infos;
      }
      logger.debug("Could not free Bright Inventory So Validation# " + cartLineItem.getOrder().getId() + " cancellation");
      return infos;
    } catch (Exception e) {
      logger.error("Exception while freeing bright inventory against cancelling SO# " + cartLineItem.getOrder().getId(), e.getMessage());
      return infos;
    }
  }



  public List<LineItem> freeBooking(ShippingOrder shippingOrder){

    List<LineItem> failedBookingItems = new ArrayList<LineItem>();
    for (LineItem lineItem : shippingOrder.getLineItems()){
     boolean lineItemFreed =   freeBookingItem(lineItem.getCartLineItem().getId());
      if (!lineItemFreed){
        failedBookingItems.add(lineItem);
      }

    }
   return  failedBookingItems;
  }



  public void   removeRefusedFsicli (List<HKAPIForeignBookingResponseInfo> infos ){

    for (HKAPIForeignBookingResponseInfo info : infos ){
      if (info.getProcessed().equals(EnumUnitProcessedStatus.REFUSED.getId())){
        long fsiliId = info.getFsiCLIId();
        ForeignSkuItemCLI foreignSkuItemCLI = getForeignSkuItemCLI(fsiliId);
        SkuItem skuItem = getSkuItem(fsiliId);
        SkuItemLineItem skuItemLineItem =  skuItemLineItemDao.getSkuItemLineItem(skuItem);
        SkuItemCLI skuItemCLI =   skuItemLineItemDao.getSkuItemCLI(skuItem);

        LineItem lineItem = skuItemLineItem.getLineItem();
        lineItem.getSkuItemLineItems().remove(skuItemLineItem);

        CartLineItem cartLineItem = skuItemCLI.getCartLineItem();
        cartLineItem.getSkuItemCLIs().remove(skuItemCLI);

        skuItemLineItem.setSkuItemCLI(null);
        skuItem.setForeignSkuItemCLI(null);

        cartLineItem.getForeignSkuItemCLIs().remove(foreignSkuItemCLI);

        baseDao.delete(skuItemLineItem);
        baseDao.delete(skuItemCLI);
        baseDao.delete(skuItem);
        foreignSkuItemCLI.setSkuItemId(null);
        baseDao.delete(foreignSkuItemCLI);
      }
    }

  }

  public SkuItemLineItem getBySkuItemId(Long skuItemId) {
    return getSkuItemDao().get(SkuItemLineItem.class, skuItemId);
  }

  public SkuItemLineItemDao getSkuItemLineItemDao() {
    return skuItemLineItemDao;
  }

  public SkuItemDao getSkuItemDao() {
    return skuItemDao;
  }

  public SkuService getSkuService() {
    return skuService;
  }

  public LineItemDao getLineItemDao() {
    return lineItemDao;
  }


  public InventoryHealthService getInventoryHealthService() {
    if (inventoryHealthService == null) {
      inventoryHealthService = ServiceLocatorFactory.getService(InventoryHealthService.class);
    }
    return inventoryHealthService;
  }


  public Boolean isBrightItemCheckInAtAqua(CartLineItem cartLineItem  ) {
    // need to check whether booked inventory has already been checked in at bright
    List<ForeignSkuItemCLI> fsiclis = cartLineItem.getForeignSkuItemCLIs();
    for (ForeignSkuItemCLI foreignSkuItemCLI  : fsiclis){
      SkuItem skuItem = getSkuItem(foreignSkuItemCLI.getId());
      if (skuItem.getSkuItemStatus().getId().equals(EnumSkuItemStatus.BOOKED.getId())){
        return  true;
      }
    }
    return  false;
  }
  
  public Boolean freeInventoryForRTOCheckIn(ShippingOrder shippingOrder) {
		List<SkuItemLineItem> skuItemLineItemsToBeDeleted = new ArrayList<SkuItemLineItem>();
		List<SkuItemCLI> skuItemCLIsToBeDeleted = new ArrayList<SkuItemCLI>();
    Boolean hasReplacementOrder = getShippingOrderService().shippingOrderHasReplacementOrder(shippingOrder);

		Set<LineItem> lineItems = shippingOrder.getLineItems();
		for (LineItem lineItem : lineItems) {
			for (SkuItemCLI skuItemCLI : lineItem.getCartLineItem().getSkuItemCLIs()) {
				skuItemCLI.setSkuItemLineItems(null);
				skuItemCLI = (SkuItemCLI) getSkuItemDao().save(skuItemCLI);
				skuItemCLIsToBeDeleted.add(skuItemCLI);
			}

			skuItemLineItemsToBeDeleted.addAll(lineItem.getSkuItemLineItems());
		}
		for (LineItem lineItem : shippingOrder.getLineItems()) {
			CartLineItem cartLineItem = lineItem.getCartLineItem();
			cartLineItem.setSkuItemCLIs(null);
			cartLineItem = (CartLineItem) baseDao.save(cartLineItem);
			lineItem.setSkuItemLineItems(null);
			lineItem.setCartLineItem(cartLineItem);
			lineItem = (LineItem) baseDao.save(lineItem);
		}
		for (Iterator<SkuItemLineItem> iterator = skuItemLineItemsToBeDeleted.iterator(); iterator.hasNext(); ) {
			SkuItemLineItem skuItemLineItem = (SkuItemLineItem) iterator.next();
			baseDao.delete(skuItemLineItem);
			iterator.remove();
		}

    if(!hasReplacementOrder){
      for (Iterator<SkuItemCLI> iterator = skuItemCLIsToBeDeleted.iterator(); iterator.hasNext(); ) {
        SkuItemCLI skuItemCLI = (SkuItemCLI) iterator.next();
        baseDao.delete(skuItemCLI);
        iterator.remove();
      }
    }
		return true;
	}
	
  public ShippingOrderService getShippingOrderService() {
    return ServiceLocatorFactory.getService(ShippingOrderService.class);
  }

  public Boolean freeBookingItem(Long cartlineItemId) {
    CartLineItem cartLineItem = null;
    Boolean bookingRemoved = false;

    cartLineItem = baseDao.get(CartLineItem.class, cartlineItemId);

    if (cartLineItem != null) {
      if (cartLineItem.getForeignSkuItemCLIs() == null || cartLineItem.getForeignSkuItemCLIs().size() <= 0) {
        bookingRemoved = freeBookingInventoryAtAqua(cartLineItem);
      } else {
        if (bookedOnBright(cartLineItem)) {
          bookingRemoved = freeBookingInventoryAtBright(cartLineItem);
        }

      }
    }

    return bookingRemoved;
  }


  public boolean freeBookingInventoryAtAqua(CartLineItem cartLineItem) {
    List<SkuItemCLI> siclis = cartLineItem.getSkuItemCLIs();
    deleteSicliAndSili(cartLineItem);

    return true;
  }


  public Boolean validateBooking(CartLineItem cartLineItem) {

    if (cartLineItem.getForeignSkuItemCLIs() == null) {
      logger.debug("Call to validate Booked inventory by  aqua for cartlineItem  :" + cartLineItem.getId());
      if (!swappingForAquaBooking(cartLineItem)) return false;
    } else {
      if (bookedOnBright(cartLineItem)) {
        logger.debug("Call to validate Booked inventory by bright for cartlineItem  :" + cartLineItem.getId());
        if (!swappingForBrightBooking(cartLineItem)) return false;
      } else {
        return false;
      }
    }
    return true;
  }
  
  public boolean swappingForAquaBooking(CartLineItem cartLineItem) {

    LineItem item = lineItemDao.getLineItem(cartLineItem);
    List<SkuItemLineItem> skuItemLineItems = item.getSkuItemLineItems();
    List<Sku> skuList = Arrays.asList(item.getSku());
    List<Long> skuStatusIdList = Arrays.asList(EnumSkuItemStatus.Checked_IN.getId());
    List<Long> skuItemOwnerList = Arrays.asList(EnumSkuItemOwner.SELF.getId());
    List<SkuItemStatus> skuItemStatus = new ArrayList<SkuItemStatus>();
    skuItemStatus.add(EnumSkuItemStatus.BOOKED.getSkuItemStatus());
    skuItemStatus.add(EnumSkuItemStatus.Checked_OUT.getSkuItemStatus());

    Warehouse aquaWarehouse = item.getSku().getWarehouse();
    String tinPrefix = aquaWarehouse.getTinPrefix();
    List<Warehouse> warehouses = warehouseService.findWarehousesByPrefix(tinPrefix);
    warehouses.remove(aquaWarehouse);
    Long wareHouseIdForBright = warehouses.get(0).getId();

    Map<String, Long> invMap = getInventoryHealthService().getInventoryCountOfAB(cartLineItem, null);
    Long aquaInventoryCount = invMap.get("aquaInventory");
    Long brtInventoryCount = invMap.get("brtInventory");

    if (aquaInventoryCount >= cartLineItem.getQty()) {
      logger.debug("Aqua inventory count in swapping For Aqua Booking for cartlineItem Id  : " + cartLineItem.getId() + " for variant :" + cartLineItem.getProductVariant().getId() + " is :" + aquaInventoryCount + "for Marked price : " + item.getMarkedPrice());
      List<SkuItem> availableUnbookedSkuItems = skuItemDao.getSkuItems(skuList, skuStatusIdList, skuItemOwnerList, item.getMarkedPrice());
      if (availableUnbookedSkuItems != null && availableUnbookedSkuItems.size() > 0) {

        for (SkuItemLineItem skuItemLineItem : skuItemLineItems) {
          if (!skuItemLineItem.getSkuItem().getSkuGroup().getMrp().equals(item.getMarkedPrice()) || !skuItemStatus.contains(skuItemLineItem.getSkuItem().getSkuItemStatus())) {
            SkuItem skuItem = skuItemLineItem.getSkuItem();
            skuItem.setSkuItemStatus(EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
            skuItem = (SkuItem) baseDao.save(skuItem);

            SkuItem skuItemToBeSet = availableUnbookedSkuItems.get(0);
            skuItemToBeSet.setSkuItemStatus(EnumSkuItemStatus.BOOKED.getSkuItemStatus());
            skuItemToBeSet = (SkuItem) baseDao.save(skuItemToBeSet);

            skuItemLineItem.getSkuItemCLI().setSkuItem(skuItemToBeSet);
            baseDao.save(skuItemLineItem.getSkuItemCLI());
            skuItemLineItem.setSkuItem(skuItemToBeSet);
            skuItemLineItem = (SkuItemLineItem) baseDao.save(skuItemLineItem);
          }
        }
      } else {
        return false;
      }
    } else {

      if (brtInventoryCount >= cartLineItem.getQty()) {
        logger.debug("Bright inventory count  for Aqua swapping for cartlineItem Id  : " + cartLineItem.getId() + " for variant :" + cartLineItem.getProductVariant().getId() + " is : " + brtInventoryCount + "for Marked price : " + item.getMarkedPrice());
        boolean incorrectMrp = false;
        for (SkuItemLineItem skuItemLineItem : skuItemLineItems) {
          if (!skuItemLineItem.getSkuItem().getSkuGroup().getMrp().equals(item.getMarkedPrice()) || !skuItemStatus.contains(skuItemLineItem.getSkuItem().getSkuItemStatus())) {
            incorrectMrp = true;
            break;
          }
        }
        for (SkuItemLineItem skuItemLineItem : skuItemLineItems) {
          SkuItem skuItem = skuItemLineItem.getSkuItem();
          skuItem.setSkuItemStatus(EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
          skuItem = (SkuItem) baseDao.save(skuItem);
        }
        if (incorrectMrp) {
          freeBookingInventoryAtAqua(cartLineItem);
          getInventoryHealthService().createSicliAndSiliAndTempBookingForBright(cartLineItem, wareHouseIdForBright);
        }
      } else {
        return false;
      }

    }
    return true;
  }

}
