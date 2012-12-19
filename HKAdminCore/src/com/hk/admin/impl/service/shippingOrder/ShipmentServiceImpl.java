package com.hk.admin.impl.service.shippingOrder;

import java.util.*;

import com.hk.domain.courier.Zone;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hk.admin.engine.ShipmentPricingEngine;
import com.hk.admin.manager.AdminEmailManager;
import com.hk.admin.pact.dao.courier.AwbDao;
import com.hk.admin.pact.dao.courier.CourierServiceInfoDao;
import com.hk.admin.pact.dao.shipment.ShipmentDao;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.courier.CourierGroupService;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.pact.service.courier.thirdParty.ThirdPartyAwbService;
import com.hk.admin.pact.service.shippingOrder.ShipmentService;
import com.hk.cache.UserCache;
import com.hk.constants.courier.CourierConstants;
import com.hk.constants.courier.EnumAwbStatus;
import com.hk.constants.shipment.EnumBoxSize;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.Supplier;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.Shipment;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.user.User;
import com.hk.pact.dao.courier.PincodeDao;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.dao.shippingOrder.ShippingOrderDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.helper.ShippingOrderHelper;

@Service
public class ShipmentServiceImpl implements ShipmentService {

    @Autowired
    CourierService        courierService;
    @Autowired
    PincodeDao            pincodeDao;
    @Autowired
    AwbDao                awbDao;
    @Autowired
    CourierGroupService   courierGroupService;
    @Autowired
    ShipmentPricingEngine shipmentPricingEngine;
    @Autowired
    AwbService            awbService;
    @Autowired
    ShippingOrderService  shippingOrderService;
    @Autowired
    ShipmentDao           shipmentDao;
    @Autowired
    CourierServiceInfoDao courierServiceInfoDao;
    @Autowired
    UserService           userService;
    @Autowired
    AdminEmailManager     adminEmailManager;
    @Autowired
    ShippingOrderStatusService shippingOrderStatusService ;

     @Autowired
    private LineItemDao lineItemDao;

    @Autowired
    private ShippingOrderDao shippingOrderDao;


    @Transactional
    public Shipment createShipment(ShippingOrder shippingOrder) {
        Order order = shippingOrder.getBaseOrder();
	    Zone zone=null;
        Pincode pincode = pincodeDao.getByPincode(order.getAddress().getPin());
        if (pincode == null) {
            //User adminUser = UserCache.getInstance().getAdminUser();
            User adminUser = getUserService().getAdminUser();
            shippingOrderService.logShippingOrderActivity(shippingOrder, adminUser, EnumShippingOrderLifecycleActivity.SO_ShipmentNotCreated.asShippingOrderLifecycleActivity(),
                    CourierConstants.PINCODE_INVALID);
            return null;
        }

	    zone=pincode.getZone();
        boolean isGroundShipped = false;
        Courier suggestedCourier = null;
        String shipmentType;
        isGroundShipped = isShippingOrderHasGroundShippedItem(shippingOrder);
        suggestedCourier = courierService.getDefaultCourier(pincode, shippingOrder.isCOD(), isGroundShipped, shippingOrder.getWarehouse());
        if (isGroundShipped) {
            shipmentType = CourierConstants.GROUND_SHIPPING;
        } else {
            shipmentType = CourierConstants.AIR_SHIPPING;
        }
        //User adminUser = UserCache.getInstance().getAdminUser();
        User adminUser = getUserService().getAdminUser();

        if (suggestedCourier == null) {
            shippingOrderService.logShippingOrderActivity(shippingOrder, adminUser, EnumShippingOrderLifecycleActivity.SO_ShipmentNotCreated.asShippingOrderLifecycleActivity(),
                    CourierConstants.SUGGESTED_COURIER_NOT_FOUND);
            return null;
        } else {
            String pin = pincode.getPincode();
            Boolean isCodAllowedOnGroundShipping = courierService.isCodAllowedOnGroundShipping(pin);

            if (!courierServiceInfoDao.isCourierServiceInfoAvailable(suggestedCourier.getId(), pin, shippingOrder.isCOD(), isGroundShipped, isCodAllowedOnGroundShipping)) {

                shippingOrderService.logShippingOrderActivity(shippingOrder, adminUser, EnumShippingOrderLifecycleActivity.SO_LoggedComment.asShippingOrderLifecycleActivity(),
                        CourierConstants.COURIER_SERVICE_INFO_NOT_FOUND);
            }
        }


        for (LineItem lineItem : shippingOrder.getLineItems()) {
            if (lineItem.getSku().getProductVariant().getProduct().isDropShipping()) {
                shippingOrderService.logShippingOrderActivity(shippingOrder, adminUser,
                        EnumShippingOrderLifecycleActivity.SO_ShipmentNotCreated.asShippingOrderLifecycleActivity(), CourierConstants.DROP_SHIPPED_ORDER);
                return null;
            }
        }

        Double weightInKg = getEstimatedWeightOfShipment(shippingOrder);
        Long suggestedCourierId = suggestedCourier.getId();

        Awb suggestedAwb;
        if (ThirdPartyAwbService.integratedCouriers.contains(suggestedCourierId)) {
            suggestedAwb = awbService.getAwbForThirdPartyCourier(suggestedCourier, shippingOrder, weightInKg);
            if (suggestedAwb != null) {
                suggestedAwb = (Awb) awbService.save(suggestedAwb, null);
                awbService.save(suggestedAwb, EnumAwbStatus.Attach.getId().intValue());
            }
        } else {
            suggestedAwb = attachAwbToShipment(suggestedCourier, shippingOrder);
        }

        // If we dont have AWB , shipment will not be created
        if (suggestedAwb == null) {
            String msg = CourierConstants.AWB_NOT_ASSIGNED + suggestedCourier.getName();

            shippingOrderService.logShippingOrderActivity(shippingOrder, adminUser, EnumShippingOrderLifecycleActivity.SO_ShipmentNotCreated.asShippingOrderLifecycleActivity(),
                    msg);
            if (!(ThirdPartyAwbService.integratedCouriers.contains(suggestedCourierId))) {
                adminEmailManager.sendAwbStatusEmail(suggestedCourier, shippingOrder);
            }
            return null;
        }


        Shipment shipment = new Shipment();
        shipment.setEmailSent(false);
        shipment.setAwb(suggestedAwb);
        shipment.setShippingOrder(shippingOrder);
        shipment.setBoxWeight(weightInKg);
        shipment.setBoxSize(EnumBoxSize.MIGRATE.asBoxSize());
        shippingOrder.setShipment(shipment);
	    shipment.setZone(zone);
        if (courierGroupService.getCourierGroup(shipment.getAwb().getCourier()) != null) {
            shipment.setEstmShipmentCharge(shipmentPricingEngine.calculateShipmentCost(shippingOrder));
            shipment.setEstmCollectionCharge(shipmentPricingEngine.calculateReconciliationCost(shippingOrder));
            shipment.setExtraCharge(shipmentPricingEngine.calculatePackagingCost(shippingOrder));
        }
        shippingOrder = shippingOrderService.save(shippingOrder);
        String trackingId = shipment.getAwb().getAwbNumber();
        String comment = shipmentType + CourierConstants.SHIPMENT_DETAILS + shipment.getAwb().getCourier().getName() + "/" + trackingId;

        shippingOrderService.logShippingOrderActivity(shippingOrder, adminUser, EnumShippingOrderLifecycleActivity.SO_Shipment_Auto_Created.asShippingOrderLifecycleActivity(),
                comment);
        return shippingOrder.getShipment();
    }

    public Shipment saveShipmentDate(Shipment shipment) {
        shipment.setShipDate(new Date());
        return save(shipment);
    }

    public Shipment save(Shipment shipment) {
        return (Shipment) shipmentDao.save(shipment);
    }

    @Transactional
    private Awb attachAwbToShipment(Courier courier, ShippingOrder shippingOrder) {

        Awb suggestedAwb = awbService.getAvailableAwbForCourierByWarehouseCodStatus(courier, null, shippingOrder.getWarehouse(), shippingOrder.isCOD(),
                EnumAwbStatus.Unused.getAsAwbStatus());
        if (suggestedAwb == null) {
            return null;
        }
        int rowsUpdate = (Integer) awbService.save(suggestedAwb, EnumAwbStatus.Attach.getId().intValue());
        awbService.refresh(suggestedAwb);
        if (rowsUpdate == 1) {
            return suggestedAwb;
        } else {
            return attachAwbToShipment(courier, shippingOrder);
        }
    }

    public Shipment findByAwb(Awb awb) {
        return shipmentDao.findByAwb(awb);
    }

    public void delete(Shipment shipment) {
        shipmentDao.delete(shipment);
    }

    @Override
    public Shipment recreateShipment(ShippingOrder shippingOrder) {
        Shipment newShipment = null;
        if (shippingOrder.getShipment() != null) {
            Shipment oldShipment = shippingOrder.getShipment();
            awbService.removeAwbForShipment(oldShipment.getAwb().getCourier(), oldShipment.getAwb());
            newShipment = createShipment(shippingOrder);
            shippingOrder.setShipment(newShipment);
            delete(oldShipment);
        }
        return newShipment;
    }

    @Override
    public boolean isShippingOrderHasGroundShippedItem(ShippingOrder shippingOrder) {
        for (LineItem lineItem : shippingOrder.getLineItems()) {
            if (lineItem.getSku().getProductVariant().getProduct().isGroundShipping()) {
                return true;
            }
        }
        return false;
    }

    public Double getEstimatedWeightOfShipment(ShippingOrder shippingOrder) {
        Double estimatedWeight = 100D;
        for (LineItem lineItem : shippingOrder.getLineItems()) {
            ProductVariant productVariant = lineItem.getSku().getProductVariant();
            Double variantWeight = productVariant.getWeight();
            if (variantWeight == null || variantWeight == 0D) {
                estimatedWeight += 0D;
            } else {
                estimatedWeight += variantWeight;
            }
        }
        return estimatedWeight / 1000;
    }

    public UserService getUserService() {
        return userService;
    }


    public boolean isShippingOrderHasInstallableItem(ShippingOrder shippingOrder) {
        if (shippingOrder.isDropShipping()) {
            for (LineItem lineItem : shippingOrder.getLineItems()) {
                if (lineItem.getSku().getProductVariant().getProduct().getInstallable()) {
                    return true;
                }
            }
        }
        return false;
    }





    public boolean splitDropShippingOrder(ShippingOrder shippingOrder) {
          if (shippingOrder.getLineItems().size() > 1) {
              Set<LineItem> dropShippedLineItems = new HashSet<LineItem>();
              Set<LineItem> currentLineItems = shippingOrder.getLineItems();
              dropShippedLineItems.addAll(currentLineItems);
              for (LineItem lineItem : currentLineItems) {
                  if (lineItem != null) {
                      ProductVariant productVariant = lineItem.getSku().getProductVariant();
                      if (productVariant != null) {
                          Product product = productVariant.getProduct();
                          if (product != null && !product.isDropShipping()) {
                              dropShippedLineItems.remove(lineItem);
                          }
                      }
                  }
              }

//           Splitting on supplier level--
              Map<Long, Set<LineItem>> supplierDropShipMap = new HashMap<Long, Set<LineItem>>();
              for (LineItem lineItem1 : dropShippedLineItems) {
                  if (lineItem1 != null) {
                      ProductVariant productVariant = lineItem1.getSku().getProductVariant();
                      if (productVariant != null) {
                          Product product = productVariant.getProduct();
                          if (product != null && product.getSupplier() != null) {
                              Long supplierid = product.getSupplier().getId();
                              if (supplierDropShipMap.containsKey(supplierid)) {
                                  supplierDropShipMap.get(supplierid).add(lineItem1) ;
                              }else{
                                  Set<LineItem> itemSet = new HashSet<LineItem>();
                                  itemSet.add(lineItem1);
                                  supplierDropShipMap.put(supplierid,itemSet);
                              }
                          }
                      }
                  }
              }

              Set <Long> keys = supplierDropShipMap.keySet();
              for (Long key1 : keys){
                  ShippingOrder newShippingOrder = shippingOrderService.createSOWithBasicDetails(shippingOrder.getBaseOrder(), shippingOrder.getWarehouse());
                  newShippingOrder.setBaseOrder(shippingOrder.getBaseOrder());
                  newShippingOrder.setServiceOrder(false);
                  newShippingOrder.setOrderStatus(shippingOrderStatusService.find(EnumShippingOrderStatus.SO_ActionAwaiting));
                  newShippingOrder.setBasketCategory(shippingOrder.getBasketCategory());
                  newShippingOrder = shippingOrderService.save(newShippingOrder);
                  for (LineItem selectedLineItem : supplierDropShipMap.get(key1)) {
                      selectedLineItem.setShippingOrder(newShippingOrder);
                      lineItemDao.save(selectedLineItem);
                  }
                  shippingOrderDao.refresh(newShippingOrder);
                  ShippingOrderHelper.updateAccountingOnSOLineItems(newShippingOrder, newShippingOrder.getBaseOrder());
                  newShippingOrder.setAmount(ShippingOrderHelper.getAmountForSO(newShippingOrder));
                  newShippingOrder = shippingOrderService.setGatewayIdAndTargetDateOnShippingOrder(newShippingOrder);
                  newShippingOrder.setDropShipping(true);
                  newShippingOrder = shippingOrderService.save(newShippingOrder);

              }
                  ShippingOrderHelper.updateAccountingOnSOLineItems(shippingOrder, shippingOrder.getBaseOrder());
                  shippingOrder.setAmount(ShippingOrderHelper.getAmountForSO(shippingOrder));
                  shippingOrder.setDropShipping(false);
                  shippingOrder = shippingOrderService.save(shippingOrder);
//

              //
       
        /*
              if (currentLineItems.size() != dropShippedLineItems.size()) {
                  currentLineItems.removeAll(dropShippedLineItems);

                  ShippingOrder newShippingOrder = shippingOrderService.createSOWithBasicDetails(shippingOrder.getBaseOrder(), shippingOrder.getWarehouse());
                  newShippingOrder.setBaseOrder(shippingOrder.getBaseOrder());
                  newShippingOrder.setServiceOrder(false);
                  newShippingOrder.setOrderStatus(shippingOrderStatusService.find(EnumShippingOrderStatus.SO_ActionAwaiting));
                  newShippingOrder.setBasketCategory(shippingOrder.getBasketCategory());
                  newShippingOrder = shippingOrderService.save(newShippingOrder);
                  for (LineItem selectedLineItem : dropShippedLineItems) {
                      selectedLineItem.setShippingOrder(newShippingOrder);
                      lineItemDao.save(selectedLineItem);
                  }
                  shippingOrderDao.refresh(newShippingOrder);
                  ShippingOrderHelper.updateAccountingOnSOLineItems(newShippingOrder, newShippingOrder.getBaseOrder());
                  newShippingOrder.setAmount(ShippingOrderHelper.getAmountForSO(newShippingOrder));
                  newShippingOrder = shippingOrderService.setGatewayIdAndTargetDateOnShippingOrder(newShippingOrder);
                  newShippingOrder.setDropShipping(true);
                  newShippingOrder = shippingOrderService.save(newShippingOrder);

            //  No need to create shipment for drop ship order
            //    createShipment(newShippingOrder);
                  shippingOrderDao.refresh(shippingOrder);
                  //shippingOrder = shippingOrderService.find(shippingOrder.getId());
                  ShippingOrderHelper.updateAccountingOnSOLineItems(shippingOrder, shippingOrder.getBaseOrder());
                  shippingOrder.setAmount(ShippingOrderHelper.getAmountForSO(shippingOrder));
                  shippingOrder.setDropShipping(false);
                  shippingOrder = shippingOrderService.save(shippingOrder);
//                  logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Split);
              }


          */
              return true;
          }
         return false;
      }




        public Supplier getFirstElementSupplier( Set <LineItem> dropShippedLineItems){
              for (LineItem lineItem : dropShippedLineItems) {
                  if (lineItem != null) {
                      ProductVariant productVariant = lineItem.getSku().getProductVariant();
                      if (productVariant != null) {
                          Product product = productVariant.getProduct();
                          if (product != null ) {
                             return product.getSupplier();
                          }
                      }
                  }
              }
            return null;
        }


}