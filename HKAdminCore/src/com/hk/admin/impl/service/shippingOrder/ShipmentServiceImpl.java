package com.hk.admin.impl.service.shippingOrder;

import com.hk.admin.engine.ShipmentPricingEngine;
import com.hk.admin.pact.dao.courier.AwbDao;
import com.hk.admin.pact.dao.courier.CourierServiceInfoDao;
import com.hk.admin.pact.dao.shipment.ShipmentDao;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.courier.CourierGroupService;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.pact.service.shippingOrder.ShipmentService;
import com.hk.admin.util.FedExCourier;
import com.hk.admin.util.DeleteFedExShipment;
import com.hk.admin.util.BarcodeGenerator;
import com.hk.constants.courier.EnumAwbStatus;
import com.hk.constants.courier.EnumCourier;
import com.hk.constants.shipment.EnumBoxSize;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.*;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.pact.dao.courier.PincodeDao;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class ShipmentServiceImpl implements ShipmentService {

    @Autowired
    CourierService courierService;
    @Autowired
    PincodeDao pincodeDao;
    @Autowired
    AwbDao awbDao;
    @Autowired
    CourierGroupService courierGroupService;
    @Autowired
    ShipmentPricingEngine shipmentPricingEngine;
    @Autowired
    AwbService awbService;
    @Autowired
    ShippingOrderService shippingOrderService;
    @Autowired
    ShipmentDao shipmentDao;

     @Autowired
    BarcodeGenerator barcodeGenerator;
    @Autowired
    CourierServiceInfoDao courierServiceInfoDao;


    public Shipment createShipment(ShippingOrder shippingOrder) {
        Order order = shippingOrder.getBaseOrder();
        Pincode pincode = pincodeDao.getByPincode(order.getAddress().getPin());
        if (pincode == null) {
            return null;
        }
        Courier suggestedCourier = courierService.getDefaultCourier(pincode, shippingOrder.isCOD(), shippingOrder.getWarehouse());
        if (suggestedCourier == null) {
            return null;
        }
        Awb suggestedAwb = null;

        Double estimatedWeight = 100D;
            for (LineItem lineItem : shippingOrder.getLineItems()) {
                ProductVariant productVariant = lineItem.getSku().getProductVariant();
                if (lineItem.getSku().getProductVariant().getProduct().isDropShipping()) {
                    return null;
                }
                Double variantWeight = productVariant.getWeight();
                if (variantWeight == null || variantWeight == 0D) {
                    estimatedWeight += 0D;
                } else {
                    estimatedWeight += variantWeight;
                }
            }

        Double weightInKg = estimatedWeight/1000;

        if (suggestedCourier.getId().equals(EnumCourier.FedEx.getId())){
            FedExCourier fedExCourier = new FedExCourier();
            String trackingNumber = fedExCourier.newFedExShipment(shippingOrder,weightInKg);
            Awb fedExNumber;
            if (trackingNumber != null){
                fedExNumber = awbService.createAwb(suggestedCourier,trackingNumber, shippingOrder.getWarehouse(), shippingOrder.isCOD());
                suggestedAwb = fedExNumber;
            }
            else{
                return null;
            }
             String routingCode = fedExCourier.getRoutingCode();
             CourierServiceInfo courierServiceInfo = courierServiceInfoDao.getCourierServiceByPincodeAndCourierWithoutCOD(EnumCourier.FedEx.getId(),order.getAddress().getPin());//, shippingOrder.isCOD());//remove COD
             if (courierServiceInfo != null){
                 courierServiceInfo.setRoutingCode(routingCode);
                 courierServiceInfoDao.save(courierServiceInfo);
             }
            
             String forwardBarCode = fedExCourier.getBarCodeList().get(0);
             fedExNumber.setAwbBarCode(forwardBarCode);
             //String forwardBarcodePath = barcodeGenerator.getBarcodePath(forwardBarCode, 2.0f, 200, true);

             if (shippingOrder.isCOD()){
                 String CODReturnBarCode = fedExCourier.getBarCodeList().get(1);
                 fedExNumber.setReturnAwbBarCode(CODReturnBarCode);
                 String returnAwb =  fedExCourier.getBarCodeList().get(2);
                 fedExNumber.setReturnAwbNumber(returnAwb);
                 //String CODBarCodePath = barcodeGenerator.getBarcodePath(CODReturnBarCode, 2.0f, 200, true);
             }

        }
        else{
            suggestedAwb = awbService.getAvailableAwbForCourierByWarehouseCodStatus(suggestedCourier, null, shippingOrder.getWarehouse(), shippingOrder.isCOD(), EnumAwbStatus.Unused.getAsAwbStatus());
            if (suggestedAwb == null) {
                return null;
            }
        }

        //}
        Shipment shipment = new Shipment();
        shipment.setCourier(suggestedCourier);
        shipment.setEmailSent(false);
        suggestedAwb.setUsed(true);
        suggestedAwb.setAwbStatus(EnumAwbStatus.Attach.getAsAwbStatus());
        suggestedAwb = awbService.save(suggestedAwb);
        shipment.setAwb(suggestedAwb);
        shipment.setShippingOrder(shippingOrder);
        shipment.setBoxWeight(estimatedWeight/1000);
        shipment.setBoxSize(EnumBoxSize.MIGRATE.asBoxSize());
        shippingOrder.setShipment(shipment);
        if (courierGroupService.getCourierGroup(shipment.getCourier()) != null) {
            shipment.setEstmShipmentCharge(shipmentPricingEngine.calculateShipmentCost(shippingOrder));
            shipment.setEstmCollectionCharge(shipmentPricingEngine.calculateReconciliationCost(shippingOrder));
            shipment.setExtraCharge(shipmentPricingEngine.calculatePackagingCost(shippingOrder));
        }
        shippingOrder = shippingOrderService.save(shippingOrder);
	    String trackingId = shipment.getAwb().getAwbNumber();
	    String comment = "Shipment Details: " + shipment.getCourier().getName() + "/" + trackingId;
	    shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Shipment_Auto_Created, comment);
        return shippingOrder.getShipment();
    }

    public Shipment saveShipmentDate(Shipment shipment) {
        shipment.setShipDate(new Date());
        return save(shipment);
    }

    public Shipment save(Shipment shipment) {
        return (Shipment) shipmentDao.save(shipment);
    }

    public Awb attachAwbToShipment(Courier courier, ShippingOrder shippingOrder) {
        Shipment shipment = shippingOrder.getShipment();
        Awb suggestedAwb = awbService.getAvailableAwbForCourierByWarehouseCodStatus(courier, null, shippingOrder.getWarehouse(), shippingOrder.isCOD(), EnumAwbStatus.Unused.getAsAwbStatus());
        if (suggestedAwb != null) {
            AwbStatus awbStatus = EnumAwbStatus.Attach.getAsAwbStatus();
            suggestedAwb.setAwbStatus(awbStatus);
            shipment.setAwb(suggestedAwb);
            return suggestedAwb;
        }
        return null;
    }

    public Shipment findByAwb(Awb awb) {
        return shipmentDao.findByAwb(awb);
    }

    public void delete(Shipment shipment){
         if(shipment.getCourier().getId().equals(EnumCourier.FedEx.getId())){
               //delete FedEx tracking no. generated previously
               Boolean result =  new DeleteFedExShipment().deleteShipment(shipment.getAwb().getAwbNumber());
         }
         shipmentDao.delete(shipment);
    }

	@Override
	public Shipment recreateShipment(ShippingOrder shippingOrder) {
		Shipment newShipment = null;
		if (shippingOrder.getShipment() != null) {
			Shipment oldShipment=shippingOrder.getShipment();
			Awb awb = oldShipment.getAwb();
			awb.setAwbStatus(EnumAwbStatus.Unused.getAsAwbStatus());
			awbService.save(awb);
			newShipment = createShipment(shippingOrder);
			shippingOrder.setShipment(newShipment);
			delete(oldShipment);
		}
		return newShipment;
	}
}
