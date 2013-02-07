package com.hk.admin.util.courier.thirdParty;

import clientStub.IOT_APILocator;
import clientStub.IOT_APISoap_PortType;
import clientStub.ResDO;
import clientStub.AWBStatusVO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.hk.admin.dto.courier.thirdParty.ThirdPartyTrackDetails;

/**
 * Created by IntelliJ IDEA. * User: Neha * Date: Jan 10, 2013 * Time: 3:04:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class IndiaOntimeCourierTrack {

	private Logger logger = LoggerFactory.getLogger(IndiaOntimeCourierTrack.class);

	private String statusDescription;

	private String trackingNo;

	private String referenceNo;

	private String deliveryDate;

	private static final String SERVER_URL = "http://118.67.249.169/IOT_CUST_API/IOT_API.asmx";

	public IndiaOntimeCourierTrack(){
	}

	public ThirdPartyTrackDetails trackShipment(String trackingId){
		IOT_APILocator serviceLocator = new IOT_APILocator();

		try{
			serviceLocator.setIOT_APISoapEndpointAddress(SERVER_URL);
			IOT_APISoap_PortType port = serviceLocator.getIOT_APISoap();
			//IOT_APISoap_BindingStub bindingStub = new IOT_APISoap_BindingStub(, serviceLocator);
			ResDO response = port.TRACK_AWB_STATUS(trackingId);
			Object[] statusArray = response.getArrAWBStatus();
			AWBStatusVO status = (AWBStatusVO)statusArray[0];
			if (status != null) {
				ThirdPartyTrackDetails thirdPartyTrackDetails = new ThirdPartyTrackDetails();
				thirdPartyTrackDetails.setAwbStatus(status.getStatusDescription());
				thirdPartyTrackDetails.setReferenceNo(status.getREF_NO());
				thirdPartyTrackDetails.setTrackingNo(status.getAWBNo());
				thirdPartyTrackDetails.setDeliveryDateString(status.getStatusDate());
				return thirdPartyTrackDetails;
			} 			
		}
		catch(Exception e){
		  logger.debug("Exception while tracking IndiaOnTime shipment for tracking id:" + trackingId);
    	}
		return null;
	}

	public String getStatusDescription() {
		return statusDescription;
	}

	public void setStatusDescription(String statusDescription) {
		this.statusDescription = statusDescription;
	}

	public String getTrackingNo() {
		return trackingNo;
	}

	public void setTrackingNo(String trackingNo) {
		this.trackingNo = trackingNo;
	}

	public String getReferenceNo() {
		return referenceNo;
	}

	public void setReferenceNo(String referenceNo) {
		this.referenceNo = referenceNo;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}
}
