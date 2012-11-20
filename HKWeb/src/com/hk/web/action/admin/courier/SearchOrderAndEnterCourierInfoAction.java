package com.hk.web.action.admin.courier;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.stripes.controller.JsonHandler;
import com.akube.framework.gson.JsonUtils;
import com.hk.admin.engine.ShipmentPricingEngine;
import com.hk.admin.pact.dao.courier.CourierServiceInfoDao;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.courier.CourierGroupService;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.pact.service.courier.thirdParty.ThirdPartyAwbService;
import com.hk.admin.pact.service.shippingOrder.ShipmentService;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.courier.EnumAwbStatus;
import com.hk.constants.courier.EnumCourier;
import com.hk.constants.shipment.EnumBoxSize;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.Shipment;
import com.hk.domain.courier.AwbStatus;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.pact.dao.courier.PincodeDao;
import com.hk.pact.dao.shippingOrder.ShippingOrderDao;
import com.hk.pact.service.UserService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.web.action.error.AdminPermissionAction;
import com.hk.web.HealthkartResponse;

@Component
public class SearchOrderAndEnterCourierInfoAction extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(SearchOrderAndEnterCourierInfoAction.class);

	List<ShippingOrder> shippingOrderList = new ArrayList<ShippingOrder>(0);
	ShippingOrder shippingOrder;
	@Autowired
	ShippingOrderDao shippingOrderDao;
	@Autowired
	ShipmentService shipmentService;
	@Autowired
	ShippingOrderService shippingOrderService;
	@Autowired
	UserService userService;
	@Autowired
	PincodeDao pincodeDao;
	@Autowired
	CourierGroupService courierGroupService;
	@Autowired
	private ShipmentPricingEngine shipmentPricingEngine;
	@Autowired
	AwbService awbService;

	@Autowired
	CourierServiceInfoDao courierServiceInfoDao;

	private String trackingId;
	private String gatewayOrderId;
	Courier suggestedCourier;
	List<Courier> availableCouriers;
	Double approxWeight = 0D;
	boolean isGroundShipped = false;

	private Shipment shipment;
	private Courier selectedCourier;

	@Autowired
	private CourierService courierService;
	@Autowired
	private ShippingOrderStatusService shippingOrderStatusService;

    //todo neha what if a shipment wasnt auto created, are you putting the amount check here as well for cod orders?? secondly move this method out for fetching awb, tomorrow there will be more business logic constraints to fetch awb

	@ValidationMethod(on = "saveShipmentDetails")
	public void verifyShipmentDetails() {
		if (StringUtils.isBlank(trackingId) || shipment.getBoxWeight() == null || shipment.getBoxSize() == null || selectedCourier == null) {
			getContext().getValidationErrors().add("1", new SimpleError("Tracking Id, Box weight, Box Size, Courier all are mandatory"));
		}
		if ((shipment.getBoxSize() != null && shipment.getBoxSize().getId().equals(EnumBoxSize.MIGRATE.getId())) || (selectedCourier != null && selectedCourier.getId().equals(EnumCourier.MIGRATE.getId()))) {
			getContext().getValidationErrors().add("2", new SimpleError("None of the values can be migrate"));
		}
		Pincode pinCode = pincodeDao.getByPincode(shippingOrder.getBaseOrder().getAddress().getPin());
		if (pinCode == null) {
			getContext().getValidationErrors().add("3", new SimpleError("Pincode is invalid, It cannot be packed"));
		} else {
			boolean isCod = shippingOrder.isCOD();

//  groundShipping logic Starts---
			isGroundShipped = shipmentService.isShippingOrderHasGroundShippedItem(shippingOrder);
			availableCouriers = courierService.getCouriers(pinCode.getPincode(), isGroundShipped, null, null, false);
//  ground shipping logic ends

			if (availableCouriers == null || availableCouriers.isEmpty()) {
				getContext().getValidationErrors().add("4", new SimpleError("No Couriers are applicable on this pincode, Please contact logistics, Order cannot be packed"));
			}
		}
	}

	@DontValidate
	@DefaultHandler
	@Secure(hasAnyPermissions = {PermissionConstants.VIEW_PACKING_QUEUE}, authActionBean = AdminPermissionAction.class)
	public Resolution pre() {

		return new ForwardResolution("/pages/admin/searchOrderAndEnterCouierInfo.jsp");
	}

	@DontValidate
	public Resolution searchOrders() {
		shippingOrder = shippingOrderDao.findByGatewayOrderId(gatewayOrderId);
		if (shippingOrder == null) {
			addRedirectAlertMessage(new SimpleMessage("Shipping Order not found for the corresponding gateway order id"));
			return new RedirectResolution(SearchOrderAndEnterCourierInfoAction.class);
		} else {
			if (EnumShippingOrderStatus.getStatusForSearchOrderAndEnterCourierInfo().contains(shippingOrder.getOrderStatus().getId())) {
				shipment = shippingOrder.getShipment();
				shippingOrderList.add(shippingOrder);
				for (LineItem lineItem : shippingOrder.getLineItems()) {
					if (lineItem.getSku().getProductVariant().getWeight() != null) {
						approxWeight += lineItem.getSku().getProductVariant().getWeight();
					}
				}
			} else {
				addRedirectAlertMessage(new SimpleMessage("Shipping Order is not checked out. It cannot be packed. "));
				return new RedirectResolution(SearchOrderAndEnterCourierInfoAction.class);
			}
		}

		try {
			Pincode pinCode = pincodeDao.getByPincode(shippingOrder.getBaseOrder().getAddress().getPin());
			if (pinCode != null) {
				boolean isCod = shippingOrder.isCOD();
				isGroundShipped = shipmentService.isShippingOrderHasGroundShippedItem(shippingOrder);
				availableCouriers = courierService.getCouriers(pinCode.getPincode(), isGroundShipped, null, null, false);
				if (shippingOrder.getShipment() != null) {
					suggestedCourier = shippingOrder.getShipment().getAwb().getCourier();
					trackingId = shippingOrder.getShipment().getAwb().getAwbNumber();
				} else {
					suggestedCourier = courierService.getDefaultCourierByPincodeForLoggedInWarehouse(pinCode, isCod, isGroundShipped);
				}

			} else {
				addRedirectAlertMessage(new SimpleMessage("Pincode is INVALID, Please contact Customer Care. It cannot be packed."));
			}

		} catch (Exception e) {
			logger.error("Error while getting suggested courier for shippingOrder#" + shippingOrder.getId(), e);
		}
		return new ForwardResolution("/pages/admin/searchOrderAndEnterCouierInfo.jsp");
	}

	@Secure(hasAnyPermissions = {PermissionConstants.UPDATE_PACKING_QUEUE}, authActionBean = AdminPermissionAction.class)
	public Resolution saveShipmentDetails() {
		shipment.setEmailSent(false);
		if (trackingId == null) {
			addRedirectAlertMessage(new SimpleMessage("Pincode is INVALID, Please contact Customer Care. It cannot be packed."));
			return new RedirectResolution(SearchOrderAndEnterCourierInfoAction.class);
		}
		Awb finalAwb = null;
		Awb suggestedAwb = null;
		if (shippingOrder.getShipment() != null) {
			suggestedAwb = shippingOrder.getShipment().getAwb();
		}
		finalAwb = suggestedAwb;
		if ((suggestedAwb == null) || (!(suggestedAwb.getAwbNumber().equalsIgnoreCase(trackingId.trim()))) ||
				(suggestedCourier != null && (!(selectedCourier.equals(suggestedCourier))))) {
			//User has not used suggested one and  has enetered  AWB manually
			if ((suggestedAwb != null) && (suggestedCourier != null) && (ThirdPartyAwbService.integratedCouriers.contains(suggestedCourier.getId()))) {
				// To delete the tracking no. generated previously
				awbService.deleteAwbForThirdPartyCourier(suggestedCourier, suggestedAwb.getAwbNumber());
			}

			if (ThirdPartyAwbService.integratedCouriers.contains(selectedCourier.getId())) {
//				Double weightInKg = shipment.getBoxWeight();
//                //todo neha wth is this?  are you trying to put 100kg as weight?, btw the method above will never return weight as 0
//				if(weightInKg == 0D){
//					weightInKg = 0.1D;
//				}
				Double weightInKg = shipmentService.getEstimatedWeightOfShipment(shippingOrder);
				Awb thirdPartyAwb = awbService.getAwbForThirdPartyCourier(selectedCourier, shippingOrder, weightInKg);
				if (thirdPartyAwb == null) {
					addRedirectAlertMessage(new SimpleMessage(" The tracking number could not be generated"));
					return new RedirectResolution(SearchOrderAndEnterCourierInfoAction.class);
				} else {
					finalAwb = (Awb) awbService.save(thirdPartyAwb, null);
					awbService.save(finalAwb, EnumAwbStatus.Attach.getId().intValue());
				}
			} else {
				// For Non Fedex Couriers
				Awb awbFromDb = awbService.getAvailableAwbForCourierByWarehouseCodStatus(selectedCourier, trackingId.trim(), null, null, null);
				if (awbFromDb != null && awbFromDb.getAwbNumber() != null) {
					//User has eneterd AWB manually which is present in database Already
					boolean error = false;
					AwbStatus awbStatus = awbFromDb.getAwbStatus();
					if (EnumAwbStatus.getAllStatusExceptUnused().contains(awbStatus)) {
						error = true;
					} else if ((!awbFromDb.getWarehouse().getId().equals(shippingOrder.getWarehouse().getId())) || (awbFromDb.getCod() != shippingOrder.isCOD())) {
						error = true;
					}
					if (error) {
						addRedirectAlertMessage(new SimpleMessage(" OPERATION FAILED *********  Tracking Id : " + trackingId + "       is already Used with other  shipping Order  OR  already Present in another warehouse with same courier"));
						return new RedirectResolution(SearchOrderAndEnterCourierInfoAction.class);
					}
					finalAwb = updateAttachStatus(awbFromDb);

				} else {
					//Create New AWb (Authorization_Pending shows it might not a valid  Awb , since person has added it manually.
					Awb awb = awbService.createAwb(selectedCourier, trackingId.trim(), shippingOrder.getWarehouse(), shippingOrder.isCOD());
					awb = (Awb) awbService.save(awb, null);
					awbService.save(awb, EnumAwbStatus.Authorization_Pending.getId().intValue());
					awbService.refresh(awb);
					finalAwb = awb;
				}
			}
		} else {
			//user has used suggested one
			finalAwb = updateAttachStatus(finalAwb);
		}
		shipment.setAwb(finalAwb);
		shipment.setShippingOrder(shippingOrder);
		shippingOrder.setShipment(shipment);
		shipmentService.save(shipment);
		if (courierGroupService.getCourierGroup(shipment.getAwb().getCourier()) != null) {
			shipment.setEstmShipmentCharge(shipmentPricingEngine.calculateShipmentCost(shippingOrder));
			shipment.setEstmCollectionCharge(shipmentPricingEngine.calculateReconciliationCost(shippingOrder));
			shipment.setExtraCharge(shipmentPricingEngine.calculatePackagingCost(shippingOrder));
		}
		shippingOrder.setOrderStatus(shippingOrderStatusService.find(EnumShippingOrderStatus.SO_Packed));
		shippingOrderDao.save(shippingOrder);
		String comment = "";
		if (shipment != null) {
			String trackingId = shipment.getAwb().getAwbNumber();
			comment = "Shipment Details: " + shipment.getAwb().getCourier().getName() + "/" + trackingId;
		}
		shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_Packed, comment);

		return new RedirectResolution(SearchOrderAndEnterCourierInfoAction.class);
	}

	private Awb updateAttachStatus(Awb finalAwb) {
		int rowsUpdate = (Integer) awbService.save(finalAwb, EnumAwbStatus.Attach.getId().intValue());
		if (rowsUpdate == 0) {
			addRedirectAlertMessage(new SimpleMessage(" OPERATION FAILED *********  Tracking Id : " + trackingId + "       is Already Used with Another User Order   ,     Try again With New Tracking ID"));
			pre();
		}
		awbService.refresh(finalAwb);
		return finalAwb;
	}

	@JsonHandler
	public Resolution getCourierList() {
		List<Courier> courierList = courierService.getCouriers(null, null, false);		
		HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "", courierList);
		return new JsonResolution(healthkartResponse);
	}

	public List<ShippingOrder> getShippingOrderList() {
		return shippingOrderList;
	}

	public void setShippingOrderList(List<ShippingOrder> shippingOrderList) {
		this.shippingOrderList = shippingOrderList;
	}

	public ShippingOrder getShippingOrder() {
		return shippingOrder;
	}

	public void setShippingOrder(ShippingOrder shippingOrder) {
		this.shippingOrder = shippingOrder;
	}

	public void setGatewayOrderId(String gatewayOrderId) {
		this.gatewayOrderId = gatewayOrderId;
	}


	public String getGatewayOrderId() {
		return gatewayOrderId;
	}

	public Courier getSuggestedCourier() {
		return suggestedCourier;
	}

	public void setSuggestedCourier(Courier suggestedCourier) {
		this.suggestedCourier = suggestedCourier;
	}

	public Shipment getShipment() {
		return shipment;
	}

	public void setShipment(Shipment shipment) {
		this.shipment = shipment;
	}

	public List<Courier> getAvailableCouriers() {
		return availableCouriers;
	}	

	public void setShippingOrderStatusService(ShippingOrderStatusService shippingOrderStatusService) {
		this.shippingOrderStatusService = shippingOrderStatusService;
	}

	public void setCourierService(CourierService courierService) {
		this.courierService = courierService;
	}

	public Double getApproxWeight() {
		return approxWeight;
	}

	public void setApproxWeight(Double approxWeight) {
		this.approxWeight = approxWeight;
	}

	public String getTrackingId() {
		return trackingId;
	}

	public void setTrackingId(String trackingId) {
		this.trackingId = trackingId;
	}

	public boolean isGroundShipped() {
		return isGroundShipped;
	}

	public void setGroundShipped(boolean groundShipped) {
		isGroundShipped = groundShipped;
	}


	public Courier getSelectedCourier() {
		return selectedCourier;
	}

	public void setSelectedCourier(Courier selectedCourier) {
		this.selectedCourier = selectedCourier;
	}
}