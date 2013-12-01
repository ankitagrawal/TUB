package com.hk.admin.util;

import com.fedex.ship.stub.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sample code to call the FedEx Delete Shipment Web Service
 * <p/>
 * com.fedex.cancelpackage.stub is generated via WSDL2Java, like this:<br>
 * <pre>
 * java org.apache.axis.wsdl.WSDL2Java -w -p com.fedex.ship.stub http://www.fedex.com/...../ShipService?wsdl
 * </pre>
 * <p/>
 * This sample code has been tested with JDK 5 and Apache Axis 1.4
 */

public class FedExShipmentDeleteUtil {

	private static Logger logger = LoggerFactory.getLogger(FedExShipmentDeleteUtil.class);

	private String fedExAuthKey;

	private String fedExAccountNo;

	private String fedExMeterNo;

	private String fedExPassword;

	private String fedExServerUrl;

	private static final String AXIS_FAULT = "FedEx Axis Fault";

	public FedExShipmentDeleteUtil(String fedExAuthKey, String fedExAccountNo, String fedExMeterNo, String fedExPassword, String fedExServerUrl) {
		this.fedExAuthKey = fedExAuthKey;
		this.fedExAccountNo = fedExAccountNo;
		this.fedExMeterNo = fedExMeterNo;
		this.fedExPassword = fedExPassword;
		this.fedExServerUrl = fedExServerUrl;
	}

	//
	public boolean deleteShipment(String trackingNumber) {
		// Build a CancelPackageRequest object
		DeleteShipmentRequest request = new DeleteShipmentRequest();
		request.setClientDetail(createClientDetail());
		request.setWebAuthenticationDetail(createWebAuthenticationDetail());
		request.setDeletionControl(DeletionControlType.DELETE_ONE_PACKAGE);

		//
		TransactionDetail transactionDetail = new TransactionDetail();
		transactionDetail.setCustomerTransactionId("java sample - Delete Shipment Request Transaction");  //This is a reference field for the customer.  Any value can be used and will be provided in the response.
		request.setTransactionDetail(transactionDetail);

		//
		VersionId versionId = new VersionId("ship", 12, 1, 0);
		request.setVersion(versionId);
		//

		//request.setCarrierCode(CarrierCodeType.FDXE); // CarrierCodeTypes are FDXC(Cargo), FDXE(Express), FDXG(Ground), FDCC(Custom Critical), FXFR(Freight)
		TrackingId id = new TrackingId();
		id.setTrackingNumber(trackingNumber);//getTrackingNumber());
		TrackingIdType idType = TrackingIdType.EXPRESS; // Replace with the Tracking Id Type
		id.setTrackingIdType(idType);
		request.setTrackingId(id); // Replace with the tracking number of the package to cancel / delete
		//
		try {
			// Initialize the service
			ShipServiceLocator service;
			ShipPortType port;
			//
			service = new ShipServiceLocator();
			updateEndPoint(service);
			port = service.getShipServicePort();
			// This is the call to the web service passing in a CancelPackageRequest and returning a CancelPackageReply
			ShipmentReply reply = port.deleteShipment(request);
			//
			NotificationSeverityType nst = reply.getHighestSeverity();
			logger.debug("CancelPackageReply HightestSeverity: " + nst.toString());
			if (isResponseOk(reply.getHighestSeverity())) {
				logger.debug("Successful.");
				return true;
			}

			//printNotifications(reply.getNotifications());

		} catch (Exception e) {
			logger.error("Error deleting the FedEx tracking number:" + AXIS_FAULT);
		}
		return false;
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


	private ClientDetail createClientDetail() {
		ClientDetail clientDetail = new ClientDetail();
		//String accountNumber = System.getProperty("accountNumber");
		//String meterNumber = System.getProperty("meterNumber");

		//
		// See if the accountNumber and meterNumber properties are set,
		// if set use those values, otherwise default them to "XXX"
		//
		//if (accountNumber == null) {
		String	accountNumber = fedExAccountNo; //"510087020"; // Replace "XXX" with clients account number
		//}
		//if (meterNumber == null) {
		String	meterNumber = fedExMeterNo; //"100073086"; // Replace "XXX" with clients meter number
		//}
		clientDetail.setAccountNumber(accountNumber);
		clientDetail.setMeterNumber(meterNumber);
		return clientDetail;
	}

	private WebAuthenticationDetail createWebAuthenticationDetail() {
		WebAuthenticationCredential wac = new WebAuthenticationCredential();
		//String key = System.getProperty("key");
		//String password = System.getProperty("password");

		//
		// See if the key and password properties are set,
		// if set use those values, otherwise default them to "XXX"
		//
		//if (key == null) {
		String key = fedExAuthKey; //"j2wHG2Wru7j4cKWJ"; // Replace "XXX" with clients key
		//}
		//if (password == null) {
		String	password = fedExPassword; //"6KGHIwA4iLtnHKXMQNbQ3vOBs"; // Replace "XXX" with clients password
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
		for (int i = 0; i < notifications.length; i++) {
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

	private void updateEndPoint(ShipServiceLocator serviceLocator) {
		String endPoint = fedExServerUrl; //System.getProperty("endPoint");
		if (endPoint != null) {
			serviceLocator.setShipServicePortEndpointAddress(endPoint);
		}
	}

/*
	private static String getTrackingNumber() {
		// See if a tracking number property is set
		// otherwise default it to some number
		String trackingNumber = System.getProperty("TrackingNumber");
		if (trackingNumber == null) {
			trackingNumber = "794802959850"; // Replace "XXX" with your tracking number
		}
		return trackingNumber;
	}
*/

}
