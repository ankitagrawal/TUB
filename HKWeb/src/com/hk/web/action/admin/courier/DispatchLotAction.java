package com.hk.web.action.admin.courier;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.courier.DispatchLotService;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.DispatchLot;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 12/3/12
 * Time: 7:16 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class DispatchLotAction extends BaseAction {

	@Autowired
	private DispatchLotService dispatchLotService;

	private DispatchLot dispatchLot;
	private String docketNumber;
	private Courier courier;
	private String zone;
	private String source;
	private String destination;
	private Long noOfShipmentsSent;
	private Long noOfShipmentsReceived;
	private Long noOfMotherBags;
	private Date deliveryDate;
	private Double totalWeight;

	@DefaultHandler
	public Resolution pre() {
		return new ForwardResolution("/pages/admin/courier/dispatchLot.jsp");
	}

	/*public Resolution createNewDispatchLot() {
		if(!StringUtils.isBlank(docketNumber) && courier!= null && !StringUtils.isBlank(zone) && !StringUtils.isBlank(source)
				&& !StringUtils.isBlank(destination) && noOfShipmentsSent != null && noOfShipmentsReceived != null
				&& noOfMotherBags != null && deliveryDate != null && totalWeight != null) {

			dispatchLot = new DispatchLot();
			getDispatchLotService().saveDispatchLot(dispatchLot)

		}



	}*/

	public Resolution save() {
		getDispatchLotService().save(dispatchLot);
		addRedirectAlertMessage(new SimpleMessage("Changes saved"));
		return new ForwardResolution("/pages/admin/courier/dispatchLot.jsp");
	}

	public DispatchLotService getDispatchLotService() {
		return dispatchLotService;
	}

	public void setDispatchLotService(DispatchLotService dispatchLotService) {
		this.dispatchLotService = dispatchLotService;
	}

	public DispatchLot getDispatchLot() {
		return dispatchLot;
	}

	public void setDispatchLot(DispatchLot dispatchLot) {
		this.dispatchLot = dispatchLot;
	}

	public String getDocketNumber() {
		return docketNumber;
	}

	public void setDocketNumber(String docketNumber) {
		this.docketNumber = docketNumber;
	}

	public Courier getCourier() {
		return courier;
	}

	public void setCourier(Courier courier) {
		this.courier = courier;
	}

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public Long getNoOfShipmentsSent() {
		return noOfShipmentsSent;
	}

	public void setNoOfShipmentsSent(Long noOfShipmentsSent) {
		this.noOfShipmentsSent = noOfShipmentsSent;
	}

	public Long getNoOfShipmentsReceived() {
		return noOfShipmentsReceived;
	}

	public void setNoOfShipmentsReceived(Long noOfShipmentsReceived) {
		this.noOfShipmentsReceived = noOfShipmentsReceived;
	}

	public Long getNoOfMotherBags() {
		return noOfMotherBags;
	}

	public void setNoOfMotherBags(Long noOfMotherBags) {
		this.noOfMotherBags = noOfMotherBags;
	}

	public Date getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(Date deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public Double getTotalWeight() {
		return totalWeight;
	}

	public void setTotalWeight(Double totalWeight) {
		this.totalWeight = totalWeight;
	}
}
