package com.hk.admin.impl.service.shippingOrder;

import com.hk.admin.engine.ShipmentPricingEngine;
import com.hk.admin.pact.dao.courier.AwbDao;
import com.hk.admin.pact.dao.courier.CourierServiceInfoDao;
import com.hk.admin.pact.dao.shipment.ShipmentDao;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.courier.CourierGroupService;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.pact.service.courier.thirdParty.ThirdPartyAwbService;
import com.hk.admin.pact.service.shippingOrder.ShipmentService;
import com.hk.constants.courier.EnumAwbStatus;
import com.hk.constants.courier.CourierConstants;
import com.hk.constants.shipment.EnumBoxSize;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.Shipment;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.pact.dao.courier.PincodeDao;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	CourierServiceInfoDao courierServiceInfoDao;


	@Transactional
	public Shipment createShipment(ShippingOrder shippingOrder) {
		Order order = shippingOrder.getBaseOrder();
		Pincode pincode = pincodeDao.getByPincode(order.getAddress().getPin());
		if (pincode == null) {
			shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_ShipmentNotCreated, CourierConstants.PINCODE_INVALID);
			return null;
		}

		// Ground Shipping logic starts -- suggested courier
		boolean isGroundShipped = false;
		Courier suggestedCourier = null;
		isGroundShipped = isShippingOrderHasGroundShippedItem(shippingOrder);
		if (shippingOrder.getAmount() == 0) {
			suggestedCourier = courierService.getDefaultCourier(pincode, false, isGroundShipped, shippingOrder.getWarehouse());
		} else {
			suggestedCourier = courierService.getDefaultCourier(pincode, shippingOrder.isCOD(), isGroundShipped, shippingOrder.getWarehouse());
		}
		// Ground Shipping logic ends -- suggested courier
		if (suggestedCourier == null) {
			shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_ShipmentNotCreated, CourierConstants.SUGGESTED_COURIER_NOT_FOUND);
			return null;
		}

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

		Double weightInKg = estimatedWeight / 1000;
		Long suggestedCourierId = suggestedCourier.getId();

		Awb suggestedAwb;
		if (ThirdPartyAwbService.integratedCouriers.contains(suggestedCourierId)) {
			suggestedAwb = awbService.getAwbForThirdPartyCourier(suggestedCourier, shippingOrder, weightInKg);
			if (suggestedAwb != null) {
				suggestedAwb = (Awb) awbService.save(suggestedAwb, null);
			}
		} else {
			suggestedAwb = attachAwbToShipment(suggestedCourier, shippingOrder);
		}

		// If we dont have AWB , shipment will not be created
		if (suggestedAwb == null) {
			shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_ShipmentNotCreated, CourierConstants.AWB_NOT_ASSIGNED);
			return null;
		}

		Shipment shipment = new Shipment();
//		shipment.setCourier(suggestedCourier);
		shipment.setEmailSent(false);
		shipment.setAwb(suggestedAwb);
		shipment.setShippingOrder(shippingOrder);
		shipment.setBoxWeight(estimatedWeight / 1000);
		shipment.setBoxSize(EnumBoxSize.MIGRATE.asBoxSize());
		shippingOrder.setShipment(shipment);
		if (courierGroupService.getCourierGroup(shipment.getAwb().getCourier()) != null) {
			shipment.setEstmShipmentCharge(shipmentPricingEngine.calculateShipmentCost(shippingOrder));
			shipment.setEstmCollectionCharge(shipmentPricingEngine.calculateReconciliationCost(shippingOrder));
			shipment.setExtraCharge(shipmentPricingEngine.calculatePackagingCost(shippingOrder));
		}
		shippingOrder = shippingOrderService.save(shippingOrder);
		String trackingId = shipment.getAwb().getAwbNumber();
		String comment = "Shipment Details: " + shipment.getAwb().getCourier().getName() + "/" + trackingId;
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

	@Transactional
	private Awb attachAwbToShipment(Courier courier, ShippingOrder shippingOrder) {
		Awb suggestedAwb;
		if (shippingOrder.getAmount() == 0) {
			suggestedAwb = awbService.getAvailableAwbForCourierByWarehouseCodStatus(courier, null, shippingOrder.getWarehouse(), false, EnumAwbStatus.Unused.getAsAwbStatus());
		} else {
			suggestedAwb = awbService.getAvailableAwbForCourierByWarehouseCodStatus(courier, null, shippingOrder.getWarehouse(), shippingOrder.isCOD(), EnumAwbStatus.Unused.getAsAwbStatus());
		}
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
}
