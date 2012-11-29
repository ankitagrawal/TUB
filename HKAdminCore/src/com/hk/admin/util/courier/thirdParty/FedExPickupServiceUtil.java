package com.hk.admin.util.courier.thirdParty;

import java.util.*;
import java.math.*;
import org.apache.axis.types.Time;
import org.apache.axis.types.PositiveInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fedex.pickup.stub.*;

/**
 * Sample code to call the FedEx Pickup service with Axis
 * <p>
 * com.fedex.Pickup.stub is generated via WSDL2Java, like this:<br>
 * <pre>
 * java org.apache.axis.wsdl.WSDL2Java -w -p com.fedex.Pickup.stub http://www.fedex.com/...../PickupService?wsdl
 * </pre>
 *
 * This sample code has been tested with JDK 5 and Apache Axis 1.4
 */
public class FedExPickupServiceUtil {
	//
	private static Logger logger = LoggerFactory.getLogger(FedExCourierUtil.class);

    private String fedExAuthKey;

    private String fedExAccountNo;

    private String fedExMeterNo;

    private String fedExPassword;

    private String fedExServerUrl;

	public FedExPickupServiceUtil(String fedExAuthKey, String fedExAccountNo, String fedExMeterNo, String fedExPassword, String fedExServerUrl) {
        this.fedExAuthKey = fedExAuthKey;
        this.fedExAccountNo = fedExAccountNo;
        this.fedExMeterNo = fedExMeterNo;
        this.fedExPassword = fedExPassword;
        this.fedExServerUrl = fedExServerUrl;
    }

	public void createPickupRequest(){
		// Build a PickupRequest object

		CreatePickupRequest request = new CreatePickupRequest();
        request.setClientDetail(createClientDetail());
        request.setWebAuthenticationDetail(createWebAuthenticationDetail());
        //
	    TransactionDetail transactionDetail = new TransactionDetail();
	    transactionDetail.setCustomerTransactionId("java sample - Pickup Request"); // This is a reference field for the customer.  Any value can be used and will be provided in the response.
	    request.setTransactionDetail(transactionDetail);

        //
        VersionId versionId = new VersionId("disp", 5, 0, 0);
        request.setVersion(versionId);
		//
	    PickupOriginDetail PickupOriginDetail = new PickupOriginDetail(); // Origin Information
	    //Party party = new Party();
	    ContactAndAddress party = new ContactAndAddress();
	    //
	    // Pick up location contact details
	    Contact contact = new Contact();
	    contact.setPersonName("Contact Name");
	    contact.setCompanyName("Company Name");
	    contact.setPhoneNumber("1234567890");
	    party.setContact(contact);
	    // Pick up location Address
	    Address address = new Address();
	    String[] street = new String[1];
	    street[0] = "Address Line 1";
	    address.setStreetLines(street);
	    address.setCity("Foster City");
	    address.setStateOrProvinceCode("CA");
	    address.setPostalCode("94404");
	    address.setCountryCode("US");
	    party.setAddress(address);
	    PickupOriginDetail.setPickupLocation(party);
	    PickupOriginDetail.setPackageLocation(PickupBuildingLocationType.FRONT); // Package Location can be NONE, FRONT, REAR, SIDE
	    PickupOriginDetail.setBuildingPart(BuildingPartCode.SUITE); // BuildingPartCode values are APARTMENT, BUILDING,DEPARTMENT, SUITE, FLOOR, ROOM
	    PickupOriginDetail.setBuildingPartDescription("3B");
	    Calendar ready = Calendar.getInstance(); //current date and time
	    ready.add(Calendar.DATE,1);
	    ready.set(Calendar.HOUR_OF_DAY,6);
	    ready.set(Calendar.MINUTE,0);
	    ready.set(Calendar.SECOND,0);

        PickupOriginDetail.setReadyTimestamp(ready); // Package Ready Date and Time
	    Calendar close = Calendar.getInstance(); //current date and time
	    close.add(Calendar.DATE,1);
	    close.set(Calendar.HOUR_OF_DAY,13);
	    close.set(Calendar.MINUTE,0);
	    close.set(Calendar.SECOND,0);

        PickupOriginDetail.setCompanyCloseTime(new Time(close)); // Package Location Closing Time
        request.setOriginDetail(PickupOriginDetail);
        request.setPackageCount(new PositiveInteger("1")); //Number of Packages to Pickup
	    // Packages Weight
	    Weight weight = new Weight();
	    weight.setValue(new BigDecimal(1.0));
	    weight.setUnits(WeightUnits.LB);
	    request.setTotalWeight(weight);
	    request.setCarrierCode(CarrierCodeType.FDXE); //CarrierCodeTypes are FDXC(Cargo), FDXE (Express), FDXG (Ground), FDCC (Custom Critical), FXFR (Freight)
	    //request.setOversizePackageCount(new PositiveInteger("1")); // Set this value if package is over sized
	    request.setRemarks("Testing - do not pickup"); // Courier Remarks
	    //
		try {
			// Initialize the service
			PickupServiceLocator service;
			PickupPortType port;
			//
			service = new PickupServiceLocator();
			updateEndPoint(service);
			port = service.getPickupServicePort();
			// This is the call to the web service passing in a PickupRequest and returning a PickupReply
			CreatePickupReply reply = port.createPickup(request);
			//
			if (isResponseOk(reply.getHighestSeverity()))
			{
				System.out.println("PickupConfirmationNumber  : " + reply.getPickupConfirmationNumber()); // Pickup Confirmation Number
				System.out.println("Location :" + reply.getLocation());
				if(reply.getMessageCode()!=null)
				System.out.println("Message Code: " + reply.getMessageCode() + " Message: " + reply.getMessage() );
			}

			printNotifications(reply.getNotifications());

		} catch (Exception e) {
		    e.printStackTrace();
		}

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
        String accountNumber = fedExAccountNo; // Replace "XXX" with clients account number
        //}
        if (meterNumber == null) {
        	meterNumber = "XXX"; // Replace "XXX" with clients meter number
        }
        clientDetail.setAccountNumber(accountNumber);
        clientDetail.setMeterNumber(meterNumber);
        return clientDetail;
	}

	private static WebAuthenticationDetail createWebAuthenticationDetail() {
        WebAuthenticationCredential wac = new WebAuthenticationCredential();
        String key = System.getProperty("key");
        String password = System.getProperty("password");

        //
        // See if the key and password properties are set,
        // if set use those values, otherwise default them to "XXX"
        //
        if (key == null) {
        	key = "XXX"; // Replace "XXX" with clients key
        }
        if (password == null) {
        	password = "XXX"; // Replace "XXX" with clients password
        }
        wac.setKey(key);
        wac.setPassword(password);
		return new WebAuthenticationDetail(wac);
	}

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

	private static boolean isResponseOk(NotificationSeverityType notificationSeverityType) {
		if (notificationSeverityType == null) {
			return false;
		}
		if (notificationSeverityType.equals(NotificationSeverityType.WARNING) ||
			notificationSeverityType.equals(NotificationSeverityType.NOTE)    ||
			notificationSeverityType.equals(NotificationSeverityType.SUCCESS)) {
			return true;
		}
 		return false;
	}

	private static void updateEndPoint(PickupServiceLocator serviceLocator) {
		String endPoint = System.getProperty("endPoint");
		if (endPoint != null) {
			serviceLocator.setPickupServicePortEndpointAddress(endPoint);
		}
	}
}
