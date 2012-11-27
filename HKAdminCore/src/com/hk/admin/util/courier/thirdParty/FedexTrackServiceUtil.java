package com.hk.admin.util.courier.thirdParty;

import com.fedex.track.stub.*;
import org.springframework.stereotype.Component;

/**
 * Demo of using the Track service with Axis
 * to track a shipment.
 * <p>
 * com.fedex.track.stub is generated via WSDL2Java, like this:<br>
 * <pre>
 * java org.apache.axis.wsdl.WSDL2Java -w -p com.fedex.track.stub http://www.fedex.com/...../TrackService?wsdl
 * </pre>
 *
 * This sample code has been tested with JDK 5 and Apache Axis 1.4
 */

public class FedExTrackServiceUtil {

	private String fedExAuthKey;

    private String fedExAccountNo;

    private String fedExMeterNo;

    private String fedExPassword;

    private String fedExServerUrl;

	public FedExTrackServiceUtil(String fedExAuthKey, String fedExAccountNo, String fedExMeterNo, String fedExPassword, String fedExServerUrl){
		this.fedExAuthKey = fedExAuthKey;
        this.fedExAccountNo = fedExAccountNo;
        this.fedExMeterNo = fedExMeterNo;
        this.fedExPassword = fedExPassword;
        this.fedExServerUrl = fedExServerUrl;
	}

	//
	public String trackFedExShipment(String trackingId){

		//
	    com.fedex.track.stub.TrackRequest request = new com.fedex.track.stub.TrackRequest();

        request.setClientDetail(createClientDetail());
        request.setWebAuthenticationDetail(createWebAuthenticationDetail());
        //
        com.fedex.track.stub.TransactionDetail transactionDetail = new com.fedex.track.stub.TransactionDetail();
        transactionDetail.setCustomerTransactionId("java sample - Tracking Request"); //This is a reference field for the customer.  Any value can be used and will be provided in the response.
        request.setTransactionDetail(transactionDetail);

        //
        com.fedex.track.stub.VersionId versionId = new com.fedex.track.stub.VersionId("trck", 6, 0, 0);
        request.setVersion(versionId);
        //
        com.fedex.track.stub.TrackPackageIdentifier packageIdentifier = new com.fedex.track.stub.TrackPackageIdentifier();
        packageIdentifier.setValue(trackingId);//getSystemProperty("Tracking Number")); // tracking number
        packageIdentifier.setType(com.fedex.track.stub.TrackIdentifierType.TRACKING_NUMBER_OR_DOORTAG); // Track identifier types are TRACKING_NUMBER_OR_DOORTAG, TRACKING_CONTROL_NUMBER, ....
        request.setPackageIdentifier(packageIdentifier);
        request.setIncludeDetailedScans(new Boolean(true));
		String status = null;
	    //
		try {
			// Initializing the service
			com.fedex.track.stub.TrackServiceLocator service;
			com.fedex.track.stub.TrackPortType port;
			//
			service = new com.fedex.track.stub.TrackServiceLocator();
			updateEndPoint(service);
			port = service.getTrackServicePort();
		    //
			com.fedex.track.stub.TrackReply reply = port.track(request); // This is the call to the web service passing in a request object and returning a reply object
			//
			if (isResponseOk(reply.getHighestSeverity())) // check if the call was successful
			{
				/*
				System.out.println("Tracking detail\n");
				*/
				com.fedex.track.stub.TrackDetail td[] = reply.getTrackDetails();
				for (int i=0; i< td.length; i++) { // package detail information
				    /*
						System.out.println("Package # : " + td[i].getPackageSequenceNumber()
								+ " and Package count: " + td[i].getPackageCount());
					System.out.println("Tracking number: " + td[i].getTrackingNumber()
								+ " and Tracking number unique identifier: " + td[i].getTrackingNumberUniqueIdentifier());
					System.out.println("Status code: " + td[i].getStatusCode());
					System.out.println("Status description: " + td[i].getStatusDescription());
					*/
					status = td[i].getStatusDescription();
					/*
					if(td[i].getOtherIdentifiers() != null)
					{
						com.fedex.track.stub.TrackPackageIdentifier[] tpi = td[i].getOtherIdentifiers();
						for (int j=0; j< tpi.length; j++) {
							System.out.println(tpi[j].getType() + " " + tpi[j].getValue());
						}
					}
					print("Packaging", td[i].getPackaging());
					printWeight("Package weight", td[i].getPackageWeight());
					printWeight("Shipment weight", td[i].getShipmentWeight());

					print("Ship date & time", td[i].getShipTimestamp().getTime());

					//System.out.println("Destination: " + td[i].getDestinationAddress().getCity()
					//		+ " " + td[i].getDestinationAddress().getPostalCode()
					//		+ " " + td[i].getDestinationAddress().getCountryCode());


					com.fedex.track.stub.TrackEvent[] trackEvents = td[i].getEvents();
					if (trackEvents != null) {
						System.out.println("Events:");
						for (int k = 0; k < trackEvents.length; k++) {
							System.out.println("  Event no.: " + (k+1));
							com.fedex.track.stub.TrackEvent trackEvent = trackEvents[k];
							if (trackEvent != null) {
								print("    Timestamp", trackEvent.getTimestamp().getTime());
								print("    Description", trackEvent.getEventDescription());
								com.fedex.track.stub.Address address = trackEvent.getAddress();
								if (address != null) {
									System.out.println(address.getCity());
									print("    City", address.getCity());
									print("    State", address.getStateOrProvinceCode());
								}
							}
						}
					}
					*/
				}

			}
			return status;


			//printNotifications(reply.getNotifications());


		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static boolean isResponseOk(com.fedex.track.stub.NotificationSeverityType notificationSeverityType) {
		if (notificationSeverityType == null) {
			return false;
		}
		if (notificationSeverityType.equals(com.fedex.track.stub.NotificationSeverityType.WARNING) ||
			notificationSeverityType.equals(com.fedex.track.stub.NotificationSeverityType.NOTE)    ||
			notificationSeverityType.equals(com.fedex.track.stub.NotificationSeverityType.SUCCESS)) {
			return true;
		}
 		return false;
	}


	private static com.fedex.track.stub.ClientDetail createClientDetail() {
        com.fedex.track.stub.ClientDetail clientDetail = new com.fedex.track.stub.ClientDetail();
        //String accountNumber = System.getProperty("accountNumber");
        //String meterNumber = System.getProperty("meterNumber");

        //
        // See if the accountNumber and meterNumber properties are set,
        // if set use those values, otherwise default them to "XXX"
        //
        //if (accountNumber == null) {
        String accountNumber = "340279735"; // Replace "XXX" with clients account number
        //}
        //if (meterNumber == null) {
        String meterNumber = "104430746"; // Replace "XXX" with clients meter number
        //}
        clientDetail.setAccountNumber(accountNumber);
        clientDetail.setMeterNumber(meterNumber);
        return clientDetail;
	}

	private static com.fedex.track.stub.WebAuthenticationDetail createWebAuthenticationDetail() {
        com.fedex.track.stub.WebAuthenticationCredential wac = new com.fedex.track.stub.WebAuthenticationCredential();
        //String key = System.getProperty("key");
        //String password = System.getProperty("password");

        //
        // See if the key and password properties are set,
        // if set use those values, otherwise default them to "XXX"
        //
        //if (key == null) {
        String key = "VCcNQkG9SDeL3KWz"; // Replace "XXX" with clients key
        //}
        //if (password == null) {
        String password = "4Cl8pjvtQOlkY6nkSlHsK3Tco"; // Replace "XXX" with clients password
        //}
        wac.setKey(key);
        wac.setPassword(password);
		return new com.fedex.track.stub.WebAuthenticationDetail(wac);
	}
	/*
	private static void printNotifications(com.fedex.track.stub.Notification[] notifications) {
		System.out.println("Notifications:");
		if (notifications == null || notifications.length == 0) {
			System.out.println("  No notifications returned");
		}
		for (int i=0; i < notifications.length; i++){
			com.fedex.track.stub.Notification n = notifications[i];
			System.out.print("  Notification no. " + i + ": ");
			if (n == null) {
				System.out.println("null");
				continue;
			} else {
				System.out.println("");
			}
			com.fedex.track.stub.NotificationSeverityType nst = n.getSeverity();

			System.out.println("    Severity: " + (nst == null ? "null" : nst.getValue()));
			System.out.println("    Code: " + n.getCode());
			System.out.println("    Message: " + n.getMessage());
			System.out.println("    Source: " + n.getSource());
		}
	}
    */
	private static void updateEndPoint(com.fedex.track.stub.TrackServiceLocator serviceLocator) {
		String endPoint = "https://ws.fedex.com:443/web-services"; //System.getProperty("endPoint");
		if (endPoint != null) {
			serviceLocator.setTrackServicePortEndpointAddress(endPoint);
		}
	}
   /*
	private static void print(String msg, Object obj) {
		if (msg == null || obj == null) {
			return;
		}
		System.out.println(msg + ": " + obj.toString());
	}


	private static void printWeight(String msg, com.fedex.track.stub.Weight weight) {
		if (msg == null || weight == null) {
			return;
		}
		System.out.println(msg + ": " + weight.getValue() + " " + weight.getUnits());
	}

	private static String getSystemProperty(String property){
		String returnProperty = System.getProperty(property);
		if (returnProperty == null){
			return "793927329667";
		}
		return returnProperty;
	}
    */
}
