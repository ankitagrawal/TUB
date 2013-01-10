package com.hk.admin.util.courier.thirdParty;

import clientStub.IOT_APILocator;
import clientStub.IOT_APISoap_PortType;
import clientStub.ResDO;
import clientStub.AWBStatusVO;

/**
 * Created by IntelliJ IDEA.
 * User: Neha
 * Date: Jan 10, 2013
 * Time: 3:04:40 PM
 * To change this template use File | Settings | File Templates.
 */
public class IOTCourierTrackUtil {

	public IOTCourierTrackUtil(){		
	}

	public String trackShipment(String trackingId){
		IOT_APILocator serviceLocator = new IOT_APILocator();

		try{
			serviceLocator.setIOT_APISoapEndpointAddress("http://118.67.249.169/IOT_CUST_API/IOT_API.asmx");
			IOT_APISoap_PortType port = serviceLocator.getIOT_APISoap();
			//IOT_APISoap_BindingStub bindingStub = new IOT_APISoap_BindingStub(, serviceLocator);
			ResDO response = port.TRACK_AWB_STATUS(trackingId);
			Object[] statusArray = response.getArrAWBStatus();
			AWBStatusVO status = (AWBStatusVO)statusArray[0];
			return (status.getStatusDescription());
		}
		catch(Exception e){

    	}
		return null;
	}
}
