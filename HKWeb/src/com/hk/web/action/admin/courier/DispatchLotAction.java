package com.hk.web.action.admin.courier;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.pact.service.courier.DispatchLotService;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.DispatchLot;
import com.restfb.util.StringUtils;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 12/3/12
 * Time: 7:16 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class DispatchLotAction extends BasePaginatedAction {

	@Autowired
	private DispatchLotService dispatchLotService;

	private List<DispatchLot> dispatchLotList = new ArrayList<DispatchLot>();
	private Page dispatchLotPage;
	private DispatchLot dispatchLot;
	private Integer defaultPerPage = 20;
	private String docketNumber;
	private Courier courier;
	private String zone;
	private String source;
	private String destination;
	private Date deliveryStartDate;
	private Date deliveryEndDate;

	@DefaultHandler
	public Resolution pre() {
		return new ForwardResolution("/pages/admin/courier/dispatchLot.jsp");
	}

	public Resolution showDispatchLotList() {
		dispatchLotPage = getDispatchLotService().searchDispatchLot(dispatchLot, docketNumber, courier, zone, source,
				destination, deliveryStartDate, deliveryEndDate, getPageNo(), getPerPage());
		dispatchLotList = dispatchLotPage.getList();
		return new ForwardResolution("/pages/admin/courier/dispatchLotList.jsp");
	}

	public Resolution save() {
		if (dispatchLot == null) {
			addRedirectAlertMessage(new SimpleMessage("Please fill the required values"));
			return new ForwardResolution("/pages/admin/courier/dispatchLot.jsp");
		}
		if (!doValidations()) {
			return new ForwardResolution("/pages/admin/courier/dispatchLot.jsp");
		}
		dispatchLot.setUpdateDate(new Date());
		getDispatchLotService().save(dispatchLot);
		addRedirectAlertMessage(new SimpleMessage("Changes saved"));
		return showDispatchLotList();
	}

	private boolean doValidations() {
		boolean valid = true;
		if (StringUtils.isBlank(dispatchLot.getDocketNumber())) {
			addRedirectAlertMessage(new SimpleMessage("Please fill the Docket Number"));
			valid = false;
		}

		if (dispatchLot.getCourier() == null) {
			addRedirectAlertMessage(new SimpleMessage("Please Select a Courier"));
			valid = false;
		}

		if (StringUtils.isBlank(dispatchLot.getZone())) {
			addRedirectAlertMessage(new SimpleMessage("Please Enter the Zone"));
			valid = false;
		}

		if (StringUtils.isBlank(dispatchLot.getSource())) {
			addRedirectAlertMessage(new SimpleMessage("Please Enter the Source"));
			valid = false;
		}

		if (StringUtils.isBlank(dispatchLot.getDestination())) {
			addRedirectAlertMessage(new SimpleMessage("Please Enter the Destination"));
			valid = false;
		}

		if (dispatchLot.getNoOfShipmentsSent() == null) {
			addRedirectAlertMessage(new SimpleMessage("Please specify the No. of Shipments Sent"));
			valid = false;
		}

		if (dispatchLot.getTotalWeight() == null) {
			addRedirectAlertMessage(new SimpleMessage("Please specify the Total Weight"));
			valid = false;
		}

		return valid;
	}

	public int getPerPageDefault() {
		return defaultPerPage;
	}

	public int getPageCount() {
		return dispatchLotPage == null ? 0 : dispatchLotPage.getTotalPages();
	}

	public int getResultCount() {
		return dispatchLotPage == null ? 0 : dispatchLotPage.getTotalResults();
	}

	public Set<String> getParamSet() {
		HashSet<String> params = new HashSet<String>();
		params.add("dispatchLot");
		params.add("docketNumber");
		params.add("courier");
		params.add("zone");
		params.add("source");
		params.add("destination");
		params.add("deliveryStartDate");
		params.add("deliveryEndDate");
		return params;
	}

	/*public Resolution createNewDispatchLot() {
		if(!StringUtils.isBlank(docketNumber) && courier!= null && !StringUtils.isBlank(zone) && !StringUtils.isBlank(source)
				&& !StringUtils.isBlank(destination) && noOfShipmentsSent != null && noOfShipmentsReceived != null
				&& noOfMotherBags != null && deliveryDate != null && totalWeight != null) {

			dispatchLot = new DispatchLot();
			getDispatchLotService().saveDispatchLot(dispatchLot)

		}



	}*/


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

	public Page getDispatchLotPage() {
		return dispatchLotPage;
	}

	public void setDispatchLotPage(Page dispatchLotPage) {
		this.dispatchLotPage = dispatchLotPage;
	}

	public Integer getDefaultPerPage() {
		return defaultPerPage;
	}

	public void setDefaultPerPage(Integer defaultPerPage) {
		this.defaultPerPage = defaultPerPage;
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

	public List<DispatchLot> getDispatchLotList() {
		return dispatchLotList;
	}

	public void setDispatchLotList(List<DispatchLot> dispatchLotList) {
		this.dispatchLotList = dispatchLotList;
	}

	public Date getDeliveryStartDate() {
		return deliveryStartDate;
	}

	public void setDeliveryStartDate(Date deliveryStartDate) {
		this.deliveryStartDate = deliveryStartDate;
	}

	public Date getDeliveryEndDate() {
		return deliveryEndDate;
	}

	public void setDeliveryEndDate(Date deliveryEndDate) {
		this.deliveryEndDate = deliveryEndDate;
	}
}
