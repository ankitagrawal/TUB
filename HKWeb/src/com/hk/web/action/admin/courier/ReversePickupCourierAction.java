package com.hk.web.action.admin.courier;

import com.akube.framework.stripes.action.BaseAction;

import com.hk.admin.pact.service.courier.thirdParty.ThirdPartyPickupService;
import com.hk.admin.pact.service.courier.thirdParty.ThirdPartyAwbService;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.pact.service.courier.PincodeCourierService;
import com.hk.admin.pact.service.courier.CourierPickupService;
import com.hk.admin.pact.service.reverseOrder.ReverseOrderService;

import com.hk.admin.factory.courier.thirdParty.ThirdPartyCourierServiceFactory;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierPickupDetail;
import com.hk.domain.reverseOrder.ReverseOrder;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShipmentService;
import com.hk.util.CustomDateTypeConvertor;
import com.hk.constants.courier.CourierConstants;
import com.hk.constants.courier.EnumCourier;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;

import java.util.Date;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Nov 29, 2012
 * Time: 12:58:29 PM
 * To change this template use File | Settings | File Templates.
 */
        
@Component
public class ReversePickupCourierAction extends BaseAction {

	private static Logger logger = LoggerFactory.getLogger(ReversePickupCourierAction.class);

	private Date pickupDate;
	private Long courierId;
	private String shippingOrderId;

	private Long reverseOrderId;
	private List<Courier> availableCouriers;
	private Courier selectedCourier;
	private String manualTrackingNo;
	private String manualConfNo;

	@Autowired
	ShippingOrderService shippingOrderService;
	@Autowired
	CourierService courierService;
	@Autowired
	PincodeCourierService pincodeCourierService;
	@Autowired
	ShipmentService shipmentService;
	@Autowired
	CourierPickupService courierPickupService;
	@Autowired
	ReverseOrderService reverseOrderService;


	@DefaultHandler
	public Resolution pre() {
		if(reverseOrderId != null){
			ReverseOrder reverseOrder = reverseOrderService.getReverseOrderById(reverseOrderId);
			ShippingOrder shippingOrder = reverseOrder.getShippingOrder();
			shippingOrderId = shippingOrder.getGatewayOrderId();
			availableCouriers = pincodeCourierService.getApplicableCouriers(shippingOrder);
		}
		return new ForwardResolution("/pages/admin/reversePickup.jsp");
	}

	public Resolution submit() {
		if (shippingOrderId != null && pickupDate != null) {
			ShippingOrder shippingOrder = shippingOrderService.findByGatewayOrderId(shippingOrderId);
			CourierPickupDetail courierPickupDetail = null;

			if (ThirdPartyAwbService.integratedCouriers.contains(selectedCourier.getId())) {
				List<String> pickupReply = courierPickupService.getPickupDetailsForThirdParty(selectedCourier.getId(), shippingOrder, pickupDate);
				if (pickupReply != null) {
					if (pickupReply.get(0).equals(CourierConstants.SUCCESS)) {
						String confirmationNo = pickupReply.get(1);
						addRedirectAlertMessage(new SimpleMessage("Pickup Request saved. Pickup confirmation number: " + confirmationNo));						
						courierPickupDetail = courierPickupService.requestCourierPickup(selectedCourier, pickupDate, confirmationNo, null);
					} else {
						//message = "Could not generate a pickup request. " + pickupReply.get(1);
						addRedirectAlertMessage(new SimpleMessage("Could not generate a pickup request. " + pickupReply.get(1)));
						return new RedirectResolution(ReversePickupCourierAction.class).addParameter("reverseOrderId", reverseOrderId);
					}
				} else {
					//message = "Could not generate a pickup request (#AF).Please choose some other courier";
					addRedirectAlertMessage(new SimpleMessage("Could not generate a pickup request (#AF).Please choose some other courier"));
					return new RedirectResolution(ReversePickupCourierAction.class).addParameter("reverseOrderId", reverseOrderId);
				}
			} else{
				courierPickupDetail = setDetailsForManualRequests(courierPickupDetail, selectedCourier, pickupDate, manualTrackingNo, manualConfNo);
			}

			courierPickupDetail = courierPickupService.save(courierPickupDetail);
			ReverseOrder reverseOrder = reverseOrderService.getReverseOrderById(reverseOrderId);
			reverseOrderService.setCourierDetails(reverseOrder, courierPickupDetail);
		}
		return new RedirectResolution(ReversePickupCourierAction.class).addParameter("reverseOrderId", reverseOrderId);
	}

	public CourierPickupDetail setDetailsForManualRequests(CourierPickupDetail courierPickupDetail, Courier selectedCourier, Date pickupDate, String manualTrackingNo, String manualConfNo){
		if (manualTrackingNo != null) {
			courierPickupDetail = courierPickupService.requestCourierPickup(selectedCourier, pickupDate, null, manualTrackingNo);
		} else {
			courierPickupDetail = courierPickupService.requestCourierPickup(selectedCourier, pickupDate, manualConfNo, null);
		}
		addRedirectAlertMessage(new SimpleMessage("Pickup Request saved successfully"));
		return courierPickupDetail;
	}

	public Date getPickupDate() {
		return pickupDate;
	}

	@Validate(converter = CustomDateTypeConvertor.class)
	public void setPickupDate(Date pickupDate) {
		this.pickupDate = pickupDate;
	}

	public Long getCourierId() {
		return courierId;
	}

	public void setCourierId(Long courierId) {
		this.courierId = courierId;
	}

	public String getShippingOrderId() {
		return shippingOrderId;
	}

	public void setShippingOrderId(String shippingOrderId) {
		this.shippingOrderId = shippingOrderId;
	}

	public Long getReverseOrderId() {
		return reverseOrderId;
	}

	public void setReverseOrderId(Long reverseOrderId) {
		this.reverseOrderId = reverseOrderId;
	}

	public List<Courier> getAvailableCouriers() {
		return availableCouriers;
	}

	public void setAvailableCouriers(List<Courier> availableCouriers) {
		this.availableCouriers = availableCouriers;
	}

	public Courier getSelectedCourier() {
		return selectedCourier;
	}

	public void setSelectedCourier(Courier selectedCourier) {
		this.selectedCourier = selectedCourier;
	}

	public String getManualTrackingNo() {
		return manualTrackingNo;
	}

	public void setManualTrackingNo(String manualTrackingNo) {
		this.manualTrackingNo = manualTrackingNo;
	}

	public String getManualConfNo() {
		return manualConfNo;
	}

	public void setManualConfNo(String manualConfNo) {
		this.manualConfNo = manualConfNo;
	}
}
