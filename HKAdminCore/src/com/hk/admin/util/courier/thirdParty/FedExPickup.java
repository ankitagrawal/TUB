package com.hk.admin.util.courier.thirdParty;

import java.util.*;
import java.math.*;
import org.apache.axis.types.Time;
import org.apache.axis.types.PositiveInteger;
import org.apache.axis.AxisFault;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fedex.pickup.stub.*;

import com.hk.domain.order.ShippingOrder;
import com.hk.service.ServiceLocatorFactory;
import com.hk.constants.courier.CourierConstants;
import com.hk.pact.service.shippingOrder.ShipmentService;

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
public class FedExPickup {
	//
	private static Logger logger = LoggerFactory.getLogger(FedExPickup.class);

    private String fedExAuthKey;

    private String fedExAccountNo;

    private String fedExMeterNo;

    private String fedExPassword;

    private String fedExServerUrl;

	private ShipmentService shipmentService = ServiceLocatorFactory.getService(ShipmentService.class);

	public FedExPickup(String fedExAuthKey, String fedExAccountNo, String fedExMeterNo, String fedExPassword, String fedExServerUrl) {
        this.fedExAuthKey = fedExAuthKey;
        this.fedExAccountNo = fedExAccountNo;
        this.fedExMeterNo = fedExMeterNo;
        this.fedExPassword = fedExPassword;
        this.fedExServerUrl = fedExServerUrl;
    }

	public List<String> createPickupRequest(ShippingOrder shippingOrder, Date date){
		// Build a PickupRequest object

		CreatePickupRequest request = new CreatePickupRequest();
        request.setClientDetail(createClientDetail());
        request.setWebAuthenticationDetail(createWebAuthenticationDetail());
        //
	    TransactionDetail transactionDetail = new TransactionDetail();
	    transactionDetail.setCustomerTransactionId("Healthkart Pickup Request"); // This is a reference field for the customer.  Any value can be used and will be provided in the response.
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
		com.hk.domain.user.Address HKAddress = shippingOrder.getBaseOrder().getAddress();
	    Contact contact = new Contact();
	    contact.setPersonName(HKAddress.getName());//"Contact Name");
	    contact.setCompanyName("");//"Company Name");
	    contact.setPhoneNumber(HKAddress.getPhone());//"1234567890");
	    party.setContact(contact);
	    // Pick up location Address
	    Address address = new Address();
	    String[] street = new String[2];
	    street[0] = HKAddress.getLine1();//"Address Line 1";
		if (HKAddress.getLine2()!= null){
			street[1] = HKAddress.getLine2();
		}
		else{
			street[1] = "";
		}
	    address.setStreetLines(street);
	    address.setCity(HKAddress.getCity());//"Foster City");
	    address.setStateOrProvinceCode(HKAddress.getState());//"CA");
	    address.setPostalCode(HKAddress.getPincode().getPincode());//"94404");
	    address.setCountryCode("IN");//"US");
	    party.setAddress(address);
	    PickupOriginDetail.setPickupLocation(party);
	    PickupOriginDetail.setPackageLocation(PickupBuildingLocationType.FRONT); // Package Location can be NONE, FRONT, REAR, SIDE
	    PickupOriginDetail.setBuildingPart(BuildingPartCode.SUITE); // BuildingPartCode values are APARTMENT, BUILDING,DEPARTMENT, SUITE, FLOOR, ROOM
	    PickupOriginDetail.setBuildingPartDescription("3B");
	    Calendar ready = Calendar.getInstance(); //current date and time
		ready.setTime(date);
		int startHour = ready.get(Calendar.HOUR_OF_DAY);

		/*
	    ready.add(Calendar.DATE,1);
	    ready.set(Calendar.HOUR_OF_DAY,startHour); //6);
	    ready.set(Calendar.MINUTE,0);
	    ready.set(Calendar.SECOND,0);
	    */
        PickupOriginDetail.setReadyTimestamp(ready); // Package Ready Date and Time

		int closeHour = startHour + 2;
	    Calendar close = Calendar.getInstance(); //current date and time
	    close.setTime(date);
	    close.set(Calendar.HOUR_OF_DAY, closeHour );//13); 	    

        PickupOriginDetail.setCompanyCloseTime(new Time(close.getTime().toString().split(" ")[3])); // Package Location Closing Time
        request.setOriginDetail(PickupOriginDetail);
        request.setPackageCount(new PositiveInteger("1")); //Number of Packages to Pickup
	    // Packages Weight
	    Weight weight = new Weight();
	    weight.setValue(new BigDecimal(shipmentService.getEstimatedWeightOfShipment(shippingOrder)));//1.0));
	    weight.setUnits(WeightUnits.KG);
	    request.setTotalWeight(weight);
	    request.setCarrierCode(CarrierCodeType.FDXE); //CarrierCodeTypes are FDXC(Cargo), FDXE (Express), FDXG (Ground), FDCC (Custom Critical), FXFR (Freight)
	    //request.setOversizePackageCount(new PositiveInteger("1")); // Set this value if package is over sized
	    request.setRemarks("Healthkart pickup"); // Courier Remarks
	    //
		String exceptionMessage = "Error occured: ";
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
			List<String> pickupReply = new ArrayList<String>();
			if (isResponseOk(reply.getHighestSeverity()))
			{
				//System.out.println("PickupConfirmationNumber  : " + reply.getPickupConfirmationNumber()); // Pickup Confirmation Number
				//System.out.println("Location :" + reply.getLocation());
				if(reply.getMessageCode()!=null)
					logger.debug("Message Code: " + reply.getMessageCode() + " Message: " + reply.getMessage() );
				pickupReply.add(CourierConstants.SUCCESS);
				pickupReply.add(reply.getLocation() + reply.getPickupConfirmationNumber());
				return pickupReply;
			}

			pickupReply.add(CourierConstants.ERROR);
			pickupReply.add(getNotifications(reply.getNotifications()));
			return pickupReply;			

			//printNotifications(reply.getNotifications());

		}catch (AxisFault af){
			logger.error("Axis Fault message", af.getFaultString());
			exceptionMessage = af.getFaultString();
		}

		catch (Exception e) {
		    logger.error("error requesting pickup service for Fedex", e.getMessage());
			exceptionMessage = e.getMessage();
		}
		return Arrays.asList(exceptionMessage);
	}

	public String getNotifications(com.fedex.pickup.stub.Notification[] notifications) {
		   String messages = "";
		   if (notifications != null && notifications.length != 0) {
			   for (int i = 0; i < notifications.length; i++) {
				   Notification notification = notifications[i];
				   logger.debug("  FedEx Notification no. " + i + ": ");
				   if (notification == null) {
					   continue;
				   }
				   NotificationSeverityType notificationSeverityType = notification.getSeverity();

				   logger.debug("    Severity: " + (notificationSeverityType == null ? "null" : notificationSeverityType.getValue()));
				   logger.debug("    Code: " + notification.getCode());
				   logger.debug("    Message: " + notification.getMessage());
				   if (notificationSeverityType != null && notificationSeverityType.equals(NotificationSeverityType.ERROR)) {
					   messages = messages.concat(notification.getMessage() + ". ");
				   }
			   }
		   }
		   return messages;
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
        //if (meterNumber == null) {
        String meterNumber = fedExMeterNo; // Replace "XXX" with clients meter number
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

	private void updateEndPoint(PickupServiceLocator serviceLocator) {
		String endPoint = fedExServerUrl; //System.getProperty("endPoint");
		if (endPoint != null) {
			serviceLocator.setPickupServicePortEndpointAddress(endPoint);
		}
	}
}
