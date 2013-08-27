package com.hk.impl.service.inventory;

import com.google.gson.Gson;
import com.hk.constants.core.Keys;
import com.hk.constants.sku.EnumSkuItemOwner;
import com.hk.constants.sku.EnumSkuItemStatus;
import com.hk.domain.api.HKAPIBookingInfo;
import com.hk.domain.api.HKAPIForeignBookingResponseInfo;
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
import com.hk.pact.service.inventory.SkuItemLineItemService;
import com.hk.pact.service.inventory.SkuService;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

          //Book the sku item
          skuItemLineItem.getSkuItem().setSkuItemStatus(EnumSkuItemStatus.BOOKED.getSkuItemStatus());

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
          oldItem.setSkuItemStatus(EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
          getSkuItemDao().save(oldItem);

          SkuItem skuItem = availableUnbookedSkuItems.get(0);
          //Book the sku item first
          skuItem.setSkuItemStatus(EnumSkuItemStatus.BOOKED.getSkuItemStatus());
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

  @Override
  public boolean isWarehouseBeFlippable(ShippingOrder shippingOrder, Warehouse targetWarehouse) {
    Sku sku = null;
    List<SkuItem> availableUnbookedSkuItems = null;
    List<Sku> skuList = new ArrayList<Sku>();
    List<Long> skuStatusIdList = new ArrayList<Long>();
    List<Long> skuItemOwnerList = new ArrayList<Long>();

    skuStatusIdList.add(EnumSkuItemStatus.Checked_IN.getId());
    skuItemOwnerList.add(EnumSkuItemOwner.SELF.getId());

    List<SkuItem> toBeFreedSkuItemList = new ArrayList<SkuItem>();

    for (LineItem lineItem : shippingOrder.getLineItems()) {
      sku = getSkuService().getSKU(lineItem.getSku().getProductVariant(), targetWarehouse);
      skuList.add(sku);
      availableUnbookedSkuItems = getSkuItemDao().getSkuItems(skuList, skuStatusIdList, skuItemOwnerList, lineItem.getMarkedPrice());

      if (availableUnbookedSkuItems != null && availableUnbookedSkuItems.size() >= lineItem.getQty()) {
        List<SkuItemLineItem> skuItemLineItemList = lineItem.getSkuItemLineItems();
        // Incase of no skuItem line Item List  Just return true  and  rest taken care by Valiadate Method
        if (skuItemLineItemList == null || skuItemLineItemList.size() == 0) {
          return true;
        }
        for (SkuItemLineItem skuItemLineItem : skuItemLineItemList) {
          SkuItem toBeFreedSkuItem = skuItemLineItem.getSkuItem();
          SkuItem skuItem = availableUnbookedSkuItems.get(skuItemLineItem.getUnitNum().intValue() - 1);
          skuItem.setSkuItemStatus(EnumSkuItemStatus.BOOKED.getSkuItemStatus());

          skuItemLineItem.setSkuItem(skuItem);
          skuItemLineItem.getSkuItemCLI().setSkuItem(skuItem);

          toBeFreedSkuItem.setSkuItemStatus(EnumSkuItemStatus.Checked_IN.getSkuItemStatus());
          toBeFreedSkuItemList.add(toBeFreedSkuItem);
        }
        getSkuItemLineItemDao().saveOrUpdate(skuItemLineItemList);
      } else {
        //todo ankit: need to change the sili and sicli for the flipped line items when the condition fails for line item number 2 or more.
        return false;
      }
    }
    if (toBeFreedSkuItemList != null && toBeFreedSkuItemList.size() > 0) {
      getSkuItemDao().saveOrUpdate(toBeFreedSkuItemList);
      return true;
    }
    return false;
  }

  public Boolean freeInventoryForSOCancellation(ShippingOrder shippingOrder) {
    List<SkuItem> skuItemsToBeFreed = new ArrayList<SkuItem>();
    List<SkuItemLineItem> skuItemLineItemsToBeDeleted = new ArrayList<SkuItemLineItem>();
    List<SkuItemCLI> skuItemCLIsToBeDeleted = new ArrayList<SkuItemCLI>();

    Set<LineItem> lineItems = shippingOrder.getLineItems();
    for (LineItem lineItem : lineItems) {
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
      } else {
        //call free inventory on bright side.
        return freeBrightInventoryForSOCancellation(lineItem);
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
    return true;
  }

  private boolean freeBrightInventoryForSOCancellation(LineItem lineItem) {
    try {
      HKAPIBookingInfo hkapiBookingInfo = new HKAPIBookingInfo();
      hkapiBookingInfo.setCliId(lineItem.getCartLineItem().getId());
      hkapiBookingInfo.setMrp(lineItem.getMarkedPrice());
      hkapiBookingInfo.setPvId(lineItem.getSku().getProductVariant().getId());
      hkapiBookingInfo.setQty(lineItem.getQty());

      Gson gson = new Gson();
      String json = gson.toJson(hkapiBookingInfo);

      String url = brightlifecareRestUrl + "product/variant/" + "freeBrightInventoryForSOCancellation/";
      ClientRequest request = new ClientRequest(url);
      request.body("application/json", json);
      ClientResponse response = request.post();
      int status = response.getStatus();
      if (status == 200) {
        String data = (String) response.getEntity(String.class);
        Boolean deleted = new Gson().fromJson(data, Boolean.class);
        logger.debug("Successfully freed Bright Inventory against SO# " + lineItem.getShippingOrder().getId() + " cancellation");
        return deleted;
      }
      logger.debug("Could not free Bright Inventory against SO# " + lineItem.getShippingOrder().getId() + " cancellation");
      return false;
    } catch (Exception e) {
      logger.error("Exception while freeing bright inventory against cancelling SO# " + lineItem.getShippingOrder().getId(), e.getMessage());
      return false;
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
            || skuItem.getSkuItemStatus().getId().equals(EnumSkuItemStatus.TEMP_BOOKED.getId())) {
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
    int i = 0;
    for (ForeignSkuItemCLI foreignSkuItemCLI : foreignSkuItemCLIs) {
      SkuItemLineItem skuItemLineItem = new SkuItemLineItem();
      skuItemLineItem.setProductVariant(foreignSkuItemCLI.getProductVariant());
      skuItemLineItem.setUnitNum(foreignSkuItemCLI.getUnitNum());
      skuItemLineItem.setSkuItem(getSkuItem(foreignSkuItemCLI.getId()));
      skuItemLineItem.setLineItem(lineItem);
      skuItemLineItem.setSkuItemCLI(foreignSkuItemCLI.getCartLineItem().getSkuItemCLIs().get(i));
      skuItemLineItem.setCreateDate(new Date());
      baseDao.save(skuItemLineItem);
      i++;
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
}
