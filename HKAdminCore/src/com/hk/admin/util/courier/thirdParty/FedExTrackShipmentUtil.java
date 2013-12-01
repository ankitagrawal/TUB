package com.hk.admin.util.courier.thirdParty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.fedex.track.stub.*;
import com.hk.admin.dto.courier.thirdParty.ThirdPartyTrackDetails;
import org.apache.commons.lang.StringUtils;
import java.util.Date;

/**
 * Demo of using the Track service with Axis
 * to track a shipment.
 * <p/>
 * com.fedex.track.stub is generated via WSDL2Java, like this:<br>
 * <pre>
 * java org.apache.axis.wsdl.WSDL2Java -w -p com.fedex.track.stub http://www.fedex.com/...../TrackService?wsdl
 * </pre>
 * <p/>
 * This sample code has been tested with JDK 5 and Apache Axis 1.4
 */

public class FedExTrackShipmentUtil {

	private static Logger logger = LoggerFactory.getLogger(FedExTrackShipmentUtil.class);

	private static final String fedExAuthKey = "VCcNQkG9SDeL3KWz";

	private static final String fedExAccountNo = "340279735";

	private static final String fedExMeterNo = "104430746";

	private static final String fedExPassword = "4Cl8pjvtQOlkY6nkSlHsK3Tco";

	private static final String fedExServerUrl = "https://ws.fedex.com:443/web-services";

	/*
	Fedex Tracking credentials point to the production server of Fedex.
	No tracking is possible using Fedex testing server credentials.
    */
	public FedExTrackShipmentUtil() {
	}


	public ThirdPartyTrackDetails trackFedExShipment(String trackingId) {

		TrackRequest request = new TrackRequest();

		request.setClientDetail(createClientDetail());
		request.setWebAuthenticationDetail(createWebAuthenticationDetail());

		TransactionDetail transactionDetail = new TransactionDetail();
		transactionDetail.setCustomerTransactionId("Healthkart - tracking request"); //This is a reference field for the customer.  Any value can be used and will be provided in the response.
		request.setTransactionDetail(transactionDetail);

		//
		VersionId versionId = new VersionId("trck", 6, 0, 0);
		request.setVersion(versionId);
		//
		TrackPackageIdentifier packageIdentifier = new TrackPackageIdentifier();
		packageIdentifier.setValue(trackingId);//getSystemProperty("Tracking Number")); // tracking number
		packageIdentifier.setType(TrackIdentifierType.TRACKING_NUMBER_OR_DOORTAG); // Track identifier types are TRACKING_NUMBER_OR_DOORTAG, TRACKING_CONTROL_NUMBER, ....
		request.setPackageIdentifier(packageIdentifier);
		request.setIncludeDetailedScans(new Boolean(true));

		//
		try {
			// Initializing the service
			TrackServiceLocator service;
			TrackPortType port;
			//                                                           
			service = new TrackServiceLocator();
			updateEndPoint(service);
			port = service.getTrackServicePort();
			//
			TrackReply reply = port.track(request); // This is the call to the web service passing in a request object and returning a reply object
			//
			if (isResponseOk(reply.getHighestSeverity())) // check if the call was successful
			{
				TrackDetail td[] = reply.getTrackDetails();
				if(td != null) {
					ThirdPartyTrackDetails thirdPartyTrackDetails = new ThirdPartyTrackDetails();
					for (int i = 0; i < td.length; i++) { // package detail information
						/*
					 System.out.println("Tracking number: " + td[i].getTrackingNumber()
								 + " and Tracking number unique identifier: " + td[i].getTrackingNumberUniqueIdentifier()); 					
					 */
						thirdPartyTrackDetails.setAwbStatus(td[i].getStatusDescription());
						thirdPartyTrackDetails.setTrackingNo(td[i].getTrackingNumber());
						if (td[i].getActualDeliveryTimestamp()!= null){
							Date deliveryDate = td[i].getActualDeliveryTimestamp().getTime();
							thirdPartyTrackDetails.setDeliveryDateString(deliveryDate.toString());
						}

						if (td[i].getOtherIdentifiers() != null) {
							TrackPackageIdentifier[] tpi = td[i].getOtherIdentifiers();
							for (int j = 0; j < tpi.length; j++) {
								if (tpi[j].getType().equals(TrackIdentifierType.SHIPPER_REFERENCE)) {
									thirdPartyTrackDetails.setReferenceNo(tpi[j].getValue());
								}
							}
						}

						/*
					 printWeight("Package weight", td[i].getPackageWeight());
					 printWeight("Shipment weight", td[i].getShipmentWeight());
					 print("Ship date & time", td[i].getShipTimestamp().getTime());
					 //System.out.println("Destination: " + td[i].getDestinationAddress().getCity()
					 //		+ " " + td[i].getDestinationAddress().getPostalCode()
					 //		+ " " + td[i].getDestinationAddress().getCountryCode());
					 TrackEvent[] trackEvents = td[i].getEvents();
					 if (trackEvents != null) {
						 System.out.println("Events:");
						 for (int k = 0; k < trackEvents.length; k++) {
							 System.out.println("  Event no.: " + (k+1));
							 TrackEvent trackEvent = trackEvents[k];
							 if (trackEvent != null) {
								 print("    Timestamp", trackEvent.getTimestamp().getTime());
								 print("    Description", trackEvent.getEventDescription());
								 Address address = trackEvent.getAddress();
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
					return thirdPartyTrackDetails;
				}
			}

			//printNotifications(reply.getNotifications());

		} catch (Exception e) {
			logger.debug("Error while making tracking call to Fedex");
		}
		return null;
	}

	private static boolean isResponseOk(NotificationSeverityType notificationSeverityType) {
		if (notificationSeverityType == null) {
			return false;
		}
		if (notificationSeverityType.equals(NotificationSeverityType.WARNING) ||
				notificationSeverityType.equals(NotificationSeverityType.NOTE) ||
				notificationSeverityType.equals(NotificationSeverityType.SUCCESS)) {
			return true;
		}
		return false;
	}


	private static ClientDetail createClientDetail() {
		ClientDetail clientDetail = new ClientDetail();
		//String accountNumber = System.getProperty("accountNumber");
		//String meterNumber = System.getProperty("meterNumber");

		//
		// See if the accountNumber and meterNumber properties are set,
		// if set use those values, otherwise default them to "XXX"
		//
		//if (accountNumber == null) {
		String accountNumber = fedExAccountNo; // Replace "XXX" with clients account number
		//}
		//if (meterNumber == null) {
		String meterNumber = fedExMeterNo; // Replace "XXX" with clients meter number
		//}
		clientDetail.setAccountNumber(accountNumber);
		clientDetail.setMeterNumber(meterNumber);
		return clientDetail;
	}

	private static WebAuthenticationDetail createWebAuthenticationDetail() {
		WebAuthenticationCredential wac = new WebAuthenticationCredential();
		//String key = System.getProperty("key");
		//String password = System.getProperty("password");

		//
		// See if the key and password properties are set,
		// if set use those values, otherwise default them to "XXX"
		//
		//if (key == null) {
		String key = fedExAuthKey; // Replace "XXX" with clients key
		//}
		//if (password == null) {
		String password = fedExPassword; // Replace "XXX" with clients password
		//}
		wac.setKey(key);
		wac.setPassword(password);
		return new WebAuthenticationDetail(wac);
	}

	/*
		private static void printNotifications(Notification[] notifications) {
			System.out.println("Notifications:");
			if (notifications == null || notifications.length == 0) {
				System.out.println("  No notifications returned");
			}
			for (int i=0; i < notifications.length; i++){
				Notification n = notifications[i];
				System.out.print("  Notification no. " + i + ": ");
				if (n == null) {
					System.out.println("null");
					continue;
				} else {
					System.out.println("");
				}
				NotificationSeverityType nst = n.getSeverity();

				System.out.println("    Severity: " + (nst == null ? "null" : nst.getValue()));
				System.out.println("    Code: " + n.getCode());
				System.out.println("    Message: " + n.getMessage());
				System.out.println("    Source: " + n.getSource());
			}
		}
		*/
	private static void updateEndPoint(TrackServiceLocator serviceLocator) {
		String endPoint = fedExServerUrl; //System.getProperty("endPoint");
		serviceLocator.setTrackServicePortEndpointAddress(endPoint);
	}
	/*
		private static void print(String msg, Object obj) {
			if (msg == null || obj == null) {
				return;
			}
			System.out.println(msg + ": " + obj.toString());
		}


		private static void printWeight(String msg, Weight weight) {
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
