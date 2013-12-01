package com.hk.admin.util.courier.thirdParty;

/**
 * Created by IntelliJ IDEA. User: Neha Date: Sep 21, 2012 Time: 2:29:00 PM To change this template use File | Settings |
 * File Templates.
 */

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.hk.admin.pact.service.courier.PincodeCourierService;
import com.hk.util.ShipmentServiceMapper;
import org.apache.axis.types.NonNegativeInteger;
import org.apache.axis.types.PositiveInteger;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fedex.ship.stub.Address;
import com.fedex.ship.stub.AssociatedShipmentDetail;
import com.fedex.ship.stub.ClientDetail;
import com.fedex.ship.stub.CodCollectionType;
import com.fedex.ship.stub.CodDetail;
import com.fedex.ship.stub.CommercialInvoice;
import com.fedex.ship.stub.Commodity;
import com.fedex.ship.stub.CompletedPackageDetail;
import com.fedex.ship.stub.CompletedShipmentDetail;
import com.fedex.ship.stub.Contact;
import com.fedex.ship.stub.ContactAndAddress;
import com.fedex.ship.stub.CustomerReference;
import com.fedex.ship.stub.CustomerReferenceType;
import com.fedex.ship.stub.CustomsClearanceDetail;
import com.fedex.ship.stub.DropoffType;
import com.fedex.ship.stub.InternationalDocumentContentType;
import com.fedex.ship.stub.LabelFormatType;
import com.fedex.ship.stub.LabelSpecification;
import com.fedex.ship.stub.Money;
import com.fedex.ship.stub.Notification;
import com.fedex.ship.stub.NotificationSeverityType;
import com.fedex.ship.stub.PackagingType;
import com.fedex.ship.stub.Party;
import com.fedex.ship.stub.Payment;
import com.fedex.ship.stub.PaymentType;
import com.fedex.ship.stub.Payor;
import com.fedex.ship.stub.ProcessShipmentReply;
import com.fedex.ship.stub.ProcessShipmentRequest;
import com.fedex.ship.stub.PurposeOfShipmentType;
import com.fedex.ship.stub.RateRequestType;
import com.fedex.ship.stub.RequestedPackageLineItem;
import com.fedex.ship.stub.RequestedShipment;
import com.fedex.ship.stub.ServiceType;
import com.fedex.ship.stub.ShipPortType;
import com.fedex.ship.stub.ShipServiceLocator;
import com.fedex.ship.stub.ShipmentOperationalDetail;
import com.fedex.ship.stub.ShipmentSpecialServiceType;
import com.fedex.ship.stub.ShipmentSpecialServicesRequested;
import com.fedex.ship.stub.ShippingDocumentImageType;
import com.fedex.ship.stub.StringBarcode;
import com.fedex.ship.stub.TransactionDetail;
import com.fedex.ship.stub.VersionId;
import com.fedex.ship.stub.WebAuthenticationCredential;
import com.fedex.ship.stub.WebAuthenticationDetail;
import com.fedex.ship.stub.Weight;
import com.fedex.ship.stub.WeightUnits;
import com.hk.admin.dto.courier.thirdParty.ThirdPartyAwbDetails;
import com.hk.pact.service.shippingOrder.ShipmentService;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.service.UserService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.service.ServiceLocatorFactory;

/**
 * com.fedex.ship.stub is generated via WSDL2Java, like this:<br>
 * <p/>
 * 
 * <pre>
 * java org.apache.axis.wsdl.WSDL2Java -w -p com.fedex.ship.stub http://www.fedex.com/...../ShipService?wsdl
 * </pre>
 * 
 * <p/> This sample code has been tested with JDK 5 and Apache Axis 1.4
 */
//
// Code to call the FedEx Ship Service - GDE Express Domestic India Shipment
//
public class FedExCourierUtil {

    private static Logger        logger                   = LoggerFactory.getLogger(FedExCourierUtil.class);

    private String               fedExAuthKey;

    private String               fedExAccountNo;

    private String               fedExMeterNo;

    private String               fedExPassword;

    private String               fedExServerUrl;

    private ShipmentService shipmentService          = ServiceLocatorFactory.getService(ShipmentService.class);
    private PincodeCourierService pincodeCourierService = ServiceLocatorFactory.getService(PincodeCourierService.class);

    private ShippingOrderService shippingOrderService     = ServiceLocatorFactory.getService(ShippingOrderService.class);
    private UserService          userService              = ServiceLocatorFactory.getService(UserService.class);

    private static final String  FED_EX_AWB_NOT_GENERATED = "FedEx awb not generated: ";
    private static final String  FEDEX_RESPONSE           = "FedEx #RSP: ";
    private static final String  AXIS_FAULT               = "FedEx awb not generated: FedEx #AF";

    public FedExCourierUtil(String fedExAuthKey, String fedExAccountNo, String fedExMeterNo, String fedExPassword, String fedExServerUrl) {
        this.fedExAuthKey = fedExAuthKey;
        this.fedExAccountNo = fedExAccountNo;
        this.fedExMeterNo = fedExMeterNo;
        this.fedExPassword = fedExPassword;
        this.fedExServerUrl = fedExServerUrl;
    }

    public ThirdPartyAwbDetails newFedExShipment(ShippingOrder shippingOrder, Double weightInKg) {
        ProcessShipmentRequest request = buildRequest(shippingOrder, weightInKg); // Build a request object

        String noAwbMessage = FED_EX_AWB_NOT_GENERATED;
        //User adminUser = UserCache.getInstance().getAdminUser();
        User adminUser = userService.getAdminUser();
        //
        try {
            // Initialize the service
            logger.debug("inside newFedExShipment WebService call for order: " + shippingOrder.getGatewayOrderId() + " ");

            ShipServiceLocator service;
            ShipPortType port;
            //
            service = new ShipServiceLocator();
            updateEndPoint(service);
            port = service.getShipServicePort();
            //
            ProcessShipmentReply reply = port.processShipment(request);

            // This is the call to the ship web service passing in a request object and returning a reply object
            //

            if (isResponseOk(reply.getHighestSeverity())) // check if the call was successful
            {
                logNotifications(reply.getNotifications());
                // writeServiceOutput(reply);
                CompletedShipmentDetail csd = reply.getCompletedShipmentDetail();
                CompletedPackageDetail cpd[] = csd.getCompletedPackageDetails();
                String trackingNumber = null;
                if (cpd != null) {
                    for (int i = 0; i < cpd.length; i++) { // Package details / Rating information for each package
                        trackingNumber = cpd[i].getTrackingIds()[0].getTrackingNumber();
                    }
                }

                ThirdPartyAwbDetails thirdPartyAwbDetails = null;

                if (StringUtils.isNotBlank(trackingNumber)) {
                    thirdPartyAwbDetails = new ThirdPartyAwbDetails(trackingNumber);
                    thirdPartyAwbDetails.setBarcodeList(setBarCodeList(reply, shippingOrder));
                    thirdPartyAwbDetails.setRoutingCode(setRoutingCode(reply));
                    thirdPartyAwbDetails.setCod(shippingOrder.isCOD());
                    thirdPartyAwbDetails.setPincode(shippingOrder.getBaseOrder().getAddress().getPincode().getPincode());
                } else {
                    logger.debug("FedEx awb number could not be generated");
                }

                return thirdPartyAwbDetails;
            }

            // printNotifications(reply.getNotifications());
            // comes here when there is an ERROR notification returned
            String notificationComment = logNotifications(reply.getNotifications());
            if (StringUtils.isNotBlank(notificationComment)) {
                noAwbMessage = noAwbMessage + FEDEX_RESPONSE + notificationComment;
            }
            logger.info("FedEx awb number could not be generated: error response notification");

            shippingOrderService.logShippingOrderActivity(shippingOrder, adminUser, EnumShippingOrderLifecycleActivity.SO_ShipmentNotCreated.asShippingOrderLifecycleActivity(),
                    null, noAwbMessage);
        } catch (Exception e) {
            logger.error("Exception while getting awb number from FedEx: Axis fault");
            shippingOrderService.logShippingOrderActivity(shippingOrder, adminUser, EnumShippingOrderLifecycleActivity.SO_ShipmentNotCreated.asShippingOrderLifecycleActivity(),
                    null, AXIS_FAULT);
        }
        return null;
    }

    private ProcessShipmentRequest buildRequest(ShippingOrder shippingOrder, Double weightInKg) {
        ProcessShipmentRequest request = new ProcessShipmentRequest(); // Build a request object

        request.setClientDetail(createClientDetail());
        request.setWebAuthenticationDetail(createWebAuthenticationDetail());
        //
        TransactionDetail transactionDetail = new TransactionDetail();
        transactionDetail.setCustomerTransactionId("java sample - India Ground Shipment"); // The client will get the
        // same value back in the
        // response
        request.setTransactionDetail(transactionDetail);

        //
        VersionId versionId = new VersionId("ship", 12, 1, 0);
        request.setVersion(versionId);

        //
        RequestedShipment requestedShipment = new RequestedShipment();
        requestedShipment.setShipTimestamp(Calendar.getInstance()); // Ship date and time
        requestedShipment.setDropoffType(DropoffType.REGULAR_PICKUP);

        if (ShipmentServiceMapper.isGround(pincodeCourierService.getShipmentServiceType(shippingOrder))) {
            requestedShipment.setServiceType(ServiceType.FEDEX_EXPRESS_SAVER);
        } else {
            requestedShipment.setServiceType(ServiceType.STANDARD_OVERNIGHT);
        }
        // Service types are STANDARD_OVERNIGHT, PRIORITY_OVERNIGHT, FEDEX_GROUND ...
        requestedShipment.setPackagingType(PackagingType.YOUR_PACKAGING);
        // Packaging type FEDEX_BOX, FEDEX_PAK, FEDEX_TUBE, YOUR_PACKAGING, ...

        //
        requestedShipment.setShipper(addShipper(shippingOrder)); // Sender information
        //
        requestedShipment.setRecipient(addRecipient(shippingOrder));
        //
        requestedShipment.setShippingChargesPayment(addShippingChargesPayment());
        //

        if (shippingOrder.getAmount() == 0) {
            requestedShipment.setCustomsClearanceDetail(addCustomsClearanceDetail(1.00, weightInKg)); // Amount cannot
                                                                                                        // be passed as
                                                                                                        // Rs. 0
        } else {
            if (shippingOrder.isCOD()) {
                requestedShipment.setSpecialServicesRequested(addShipmentSpecialServicesRequested(shippingOrder)); // requests
                                                                                                                    // COD
                                                                                                                    // shipment
                requestedShipment.setCustomsClearanceDetail(addCustomsClearanceDetail(shippingOrder.getAmount(), weightInKg));
            } else {
                requestedShipment.setCustomsClearanceDetail(addCustomsClearanceDetail(shippingOrder.getAmount(), weightInKg));
            }
        }
        //
        requestedShipment.setLabelSpecification(addLabelSpecification());
        //
        RateRequestType[] rrt = new RateRequestType[] { RateRequestType.ACCOUNT }; // Rate types requested LIST,
        // MULTIWEIGHT, ...
        requestedShipment.setRateRequestTypes(rrt);
        requestedShipment.setPackageCount(new NonNegativeInteger("1"));
        //
        requestedShipment.setRequestedPackageLineItems(new RequestedPackageLineItem[] { addRequestedPackageLineItem(shippingOrder, weightInKg) });
        request.setRequestedShipment(requestedShipment);
        //
        return request;
    }

    public List<String> setBarCodeList(ProcessShipmentReply reply, ShippingOrder shippingOrder) {
        List<String> retrieveBarcodes = new ArrayList<String>();
        // forward going label (outbound label)
        CompletedShipmentDetail csd = reply.getCompletedShipmentDetail();
        CompletedPackageDetail cpd[] = csd.getCompletedPackageDetails();

        if (cpd != null) {
            for (int i = 0; i < cpd.length; i++) { // Package details / Rating information for each package
                StringBarcode sb = cpd[i].getOperationalDetail().getBarcodes().getStringBarcodes(0);
                retrieveBarcodes.add(sb.getValue());
            }
        }

        // return COD label now
        if (shippingOrder.isCOD()) {
            if (shippingOrder.getAmount() != 0) {
                AssociatedShipmentDetail asd[] = csd.getAssociatedShipments();
                if (cpd != null) {
                    for (int i = 0; i < cpd.length; i++) { // Package details / Rating information for each package
                        StringBarcode sb = asd[i].getPackageOperationalDetail().getBarcodes().getStringBarcodes(0);
                        String returnAwb = asd[i].getTrackingId().getTrackingNumber();
                        retrieveBarcodes.add(sb.getValue());
                        retrieveBarcodes.add(returnAwb);
                    }
                }
            }
        }
        return retrieveBarcodes;
    }

    public List<String> setRoutingCode(ProcessShipmentReply reply) {
        List<String> routingFedEx = new ArrayList<String>();
        CompletedShipmentDetail csd = reply.getCompletedShipmentDetail();
        ShipmentOperationalDetail sod = csd.getOperationalDetail();
        routingFedEx.add(sod.getUrsaSuffixCode());
        return routingFedEx;
    }

    public String logNotifications(Notification[] notifications) {
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

    /*
     * @SuppressWarnings("unused") private static void writeServiceOutput(ProcessShipmentReply reply) throws Exception {
     * try { System.out.println(reply.getTransactionDetail().getCustomerTransactionId()); CompletedShipmentDetail csd =
     * reply.getCompletedShipmentDetail(); String masterTrackingNumber = printMasterTrackingNumber(csd);
     * printShipmentOperationalDetails(csd.getOperationalDetail()); printShipmentRating(csd.getShipmentRating());
     * CompletedPackageDetail cpd[] = csd.getCompletedPackageDetails(); printPackageDetails(cpd);
     * saveShipmentDocumentsToFile(csd.getShipmentDocuments(), masterTrackingNumber);
     * getAssociatedShipmentLabels(csd.getAssociatedShipments()); } catch (Exception e) { e.printStackTrace(); } finally { // } }
     */

    private static boolean isResponseOk(NotificationSeverityType notificationSeverityType) {
        if (notificationSeverityType == null) {
            return false;
        }
        if (notificationSeverityType.equals(NotificationSeverityType.WARNING) || notificationSeverityType.equals(NotificationSeverityType.NOTE)
                || notificationSeverityType.equals(NotificationSeverityType.SUCCESS)) {
            return true;
        }
        return false;
    }

    /*
     * @SuppressWarnings("unused") private static void printNotifications(Notification[] notifications) {
     * System.out.println("Notifications:"); if (notifications == null || notifications.length == 0) {
     * System.out.println(" No notifications returned"); } for (int i = 0; i < notifications.length; i++) { Notification
     * n = notifications[i]; System.out.print(" Notification no. " + i + ": "); if (n == null) {
     * System.out.println("null"); continue; } else { System.out.println(""); } NotificationSeverityType nst =
     * n.getSeverity(); System.out.println(" Severity: " + (nst == null ? "null" : nst.getValue()));
     * System.out.println(" Code: " + n.getCode()); System.out.println(" Message: " + n.getMessage());
     * System.out.println(" Source: " + n.getSource()); } }
     */

    /*
     * private static void printMoney(Money money, String description, String space) { if (money != null) {
     * System.out.println(space + description + ": " + money.getAmount() + " " + money.getCurrency()); } }
     */

    /*
     * private static void printWeight(Weight weight, String description, String space) { if (weight != null) {
     * System.out.println(space + description + ": " + weight.getValue() + " " + weight.getUnits()); } }
     */

    /*
     * private static void printString(String value, String description, String space) { if (value != null) {
     * System.out.println(space + description + ": " + value); } }
     */

    private static Money addMoney(String currency, Double value) {
        Money money = new Money();
        money.setCurrency(currency);
        money.setAmount(new BigDecimal(value));
        return money;
    }

    private static Weight addPackageWeight(Double packageWeight, WeightUnits weightUnits) {
        Weight weight = new Weight();
        weight.setUnits(weightUnits);
        weight.setValue(new BigDecimal(packageWeight));
        return weight;
    }

    /*
     * @SuppressWarnings("unused") private static Dimensions addPackageDimensions(Integer length, Integer height,
     * Integer width, LinearUnits linearUnits) { Dimensions dimensions = new Dimensions(); dimensions.setLength(new
     * NonNegativeInteger(length.toString())); dimensions.setHeight(new NonNegativeInteger(height.toString()));
     * dimensions.setWidth(new NonNegativeInteger(width.toString())); dimensions.setUnits(linearUnits); return
     * dimensions; }
     */

    /*
     * // Shipment level reply information private static void printShipmentOperationalDetails(ShipmentOperationalDetail
     * shipmentOperationalDetail) { if (shipmentOperationalDetail != null) { System.out.println("Routing Details");
     * printString(shipmentOperationalDetail.getUrsaPrefixCode(), "URSA Prefix", " "); if
     * (shipmentOperationalDetail.getCommitDay() != null)
     * printString(shipmentOperationalDetail.getCommitDay().getValue(), "Service Commitment", " ");
     * printString(shipmentOperationalDetail.getAirportId(), "Airport Id", " "); if
     * (shipmentOperationalDetail.getDeliveryDay() != null)
     * printString(shipmentOperationalDetail.getDeliveryDay().getValue(), "Delivery Day", " "); System.out.println(); } }
     */

    /*
     * private static void printShipmentRating(ShipmentRating shipmentRating) { if (shipmentRating != null) {
     * System.out.println("Shipment Rate Details"); ShipmentRateDetail[] srd = shipmentRating.getShipmentRateDetails();
     * for (int j = 0; j < srd.length; j++) { System.out.println(" Rate Type: " + srd[j].getRateType().getValue());
     * printWeight(srd[j].getTotalBillingWeight(), "Shipment Billing Weight", " ");
     * printMoney(srd[j].getTotalBaseCharge(), "Shipment Base Charge", " "); printMoney(srd[j].getTotalNetCharge(),
     * "Shipment Net Charge", " "); printMoney(srd[j].getTotalSurcharges(), "Shipment Total Surcharge", " "); if (null !=
     * srd[j].getSurcharges()) { System.out.println(" Surcharge Details"); Surcharge[] s = srd[j].getSurcharges(); for
     * (int k = 0; k < s.length; k++) { printMoney(s[k].getAmount(), s[k].getSurchargeType().getValue(), " "); } }
     * printFreightDetail(srd[j].getFreightRateDetail()); System.out.println(); } } }
     */

    /*
     * // Package level reply information private static void printPackageOperationalDetails(PackageOperationalDetail
     * packageOperationalDetail) { if (packageOperationalDetail != null) { System.out.println(" Routing Details");
     * printString(packageOperationalDetail.getAstraHandlingText(), "Astra", " ");
     * printString(packageOperationalDetail.getGroundServiceCode(), "Ground Service Code", " "); System.out.println(); } }
     */

    /*
     * private static void printPackageDetails(CompletedPackageDetail[] cpd) throws Exception { if (cpd != null) {
     * System.out.println("Package Details"); for (int i = 0; i < cpd.length; i++) { // Package details / Rating
     * information for each package String trackingNumber = cpd[i].getTrackingIds()[0].getTrackingNumber();
     * printTrackingNumbers(cpd[i]); System.out.println(); // printPackageRating(cpd[i].getPackageRating()); // Write
     * label buffer to file ShippingDocument sd = cpd[i].getLabel(); saveLabelToFile(sd, trackingNumber);
     * printPackageOperationalDetails(cpd[i].getOperationalDetail()); System.out.println(); } } }
     */

    /*
     * private static void printPackageRating(PackageRating packageRating) { if (packageRating != null) {
     * System.out.println("Package Rate Details"); PackageRateDetail[] prd = packageRating.getPackageRateDetails(); for
     * (int j = 0; j < prd.length; j++) { System.out.println(" Rate Type: " + prd[j].getRateType().getValue());
     * printWeight(prd[j].getBillingWeight(), "Billing Weight", " "); printMoney(prd[j].getBaseCharge(), "Base Charge", "
     * "); printMoney(prd[j].getNetCharge(), "Net Charge", " "); printMoney(prd[j].getTotalSurcharges(), "Total
     * Surcharge", " "); if (null != prd[j].getSurcharges()) { System.out.println(" Surcharge Details"); Surcharge[] s =
     * prd[j].getSurcharges(); for (int k = 0; k < s.length; k++) { printMoney(s[k].getAmount(),
     * s[k].getSurchargeType().getValue(), " "); } } System.out.println(); } } }
     */

    /*
     * private static void printTrackingNumbers(CompletedPackageDetail completedPackageDetail) { if
     * (completedPackageDetail.getTrackingIds() != null) { TrackingId[] trackingId =
     * completedPackageDetail.getTrackingIds(); for (int i = 0; i < trackingId.length; i++) { String trackNumber =
     * trackingId[i].getTrackingNumber(); String trackType = trackingId[i].getTrackingIdType().getValue(); String formId =
     * trackingId[i].getFormId(); printString(trackNumber, trackType + " tracking number", " "); printString(formId,
     * "Form Id", " "); } } }
     */

    private String getPayorAccountNumber() {
        // See if payor account number is set as system property,
        // if not default it to "XXX"
        // String payorAccountNumber = System.getProperty("Payor.AccountNumber");
        // if (payorAccountNumber == null) {
        String payorAccountNumber = fedExAccountNo; // "510087020"; // Replace "XXX" with the payor account number
        // }
        return payorAccountNumber;
    }

    private static Party addShipper(ShippingOrder shippingOrder) {
        Party shipperParty = new Party(); // Sender information
        Contact shipperContact = new Contact();
        Warehouse HKWarehouse = shippingOrder.getWarehouse();

        shipperContact.setPersonName(HKWarehouse.getName());// "Healthkart");
        shipperContact.setCompanyName("Aquamarine HealthCare Pvt. Ltd.");
        shipperContact.setPhoneNumber(HKWarehouse.getWhPhone());// "0124-4551616");
        Address shipperAddress = new Address();
        shipperAddress.setStreetLines(new String[] { HKWarehouse.getLine1(), HKWarehouse.getLine2() });// {"4th Floor,

        shipperAddress.setCity(HKWarehouse.getCity());// "Gurgaon");
        shipperAddress.setStateOrProvinceCode(HKWarehouse.getState());// "HR");
        shipperAddress.setPostalCode(HKWarehouse.getPincode());// "122001");
        shipperAddress.setCountryCode("IN");
        shipperAddress.setCountryName("INDIA");
        shipperParty.setContact(shipperContact);
        shipperParty.setAddress(shipperAddress);
        return shipperParty;
    }

    private static Party addRecipient(ShippingOrder shippingOrder) {
        Party recipient = new Party(); // Recipient information
        Contact contactRecip = new Contact();

        // healthkart address
        com.hk.domain.user.Address HKAddress = shippingOrder.getBaseOrder().getAddress();

        contactRecip.setPersonName(HKAddress.getName());// ("Recipient Name");
        contactRecip.setCompanyName("");// Recipient Company Name");//("Recipient Company Name");

		String phoneNo = HKAddress.getPhone();
		if (phoneNo != null && phoneNo.length() >= 10) {
			String phoneNoWith10Digits = phoneNo.substring(phoneNo.length() - 10);
			contactRecip.setPhoneNumber(phoneNoWith10Digits);
		} else {
        contactRecip.setPhoneNumber(phoneNo);// ("1234567890");
		}
		
        recipient.setContact(contactRecip);

        Address addressRecip = new Address();
        if (HKAddress.getLine2() == null || HKAddress.getLine2() == "") {
            addressRecip.setStreetLines(new String[] { HKAddress.getLine1() });
        } else {
            addressRecip.setStreetLines(new String[] { HKAddress.getLine1(), HKAddress.getLine2() });
        }
        // (new String[] { "1 RECIPIENT STREET" });

        addressRecip.setCity(HKAddress.getCity());// ("NEWDELHI");
        addressRecip.setStateOrProvinceCode(HKAddress.getState());// ("DL");
        // addressRecip.setStateOrProvinceCode(HKAddress.getState());//("DL");

        addressRecip.setPostalCode(HKAddress.getPincode().getPincode());// ("110010");
        addressRecip.setCountryCode("IN");
        addressRecip.setCountryName("INDIA");
        addressRecip.setResidential(new Boolean(true));
        recipient.setAddress(addressRecip);
        return recipient;
    }

    private static ContactAndAddress addFinancialInstitutionParty(ShippingOrder shippingOrder) {
        ContactAndAddress contactAndAddress = new ContactAndAddress(); // Recipient information
        Contact contactRecip = new Contact();
        // healthkart address
        com.hk.domain.user.Address HKAddress = shippingOrder.getBaseOrder().getAddress();
        contactRecip.setPersonName(HKAddress.getName());// "Recipient Name");
        contactRecip.setCompanyName("");// Recipient Company Name");
        contactRecip.setPhoneNumber(HKAddress.getPhone());// "1234567890");
        contactAndAddress.setContact(contactRecip);
        //
        Address addressRecip = new Address();
        if (HKAddress.getLine2() == null || HKAddress.getLine2() == "") {
            addressRecip.setStreetLines(new String[] { HKAddress.getLine1() });
        } else {
            addressRecip.setStreetLines(new String[] { HKAddress.getLine1(), HKAddress.getLine2() });
        }
        // addressRecip.setStreetLines(new String[] { "1 RECIPIENT STREET" });
        addressRecip.setCity(HKAddress.getCity());// "NEWDELHI");
        addressRecip.setStateOrProvinceCode(HKAddress.getState());// "DL");
        addressRecip.setPostalCode(HKAddress.getPincode().getPincode());// "110010");
        addressRecip.setCountryCode("IN");
        addressRecip.setCountryName("INDIA");
        addressRecip.setResidential(true);
        contactAndAddress.setAddress(addressRecip);
        return contactAndAddress;
    }

    private Payment addShippingChargesPayment() {
        Payment payment = new Payment(); // Payment information
        payment.setPaymentType(PaymentType.SENDER);
        Payor payor = new Payor();
        Party responsibleParty = new Party();
        responsibleParty.setAccountNumber(getPayorAccountNumber());
        Address responsiblePartyAddress = new Address();
        responsiblePartyAddress.setCountryCode("IN");
        responsibleParty.setAddress(responsiblePartyAddress);
        responsibleParty.setContact(new Contact());
        payor.setResponsibleParty(responsibleParty);
        payment.setPayor(payor);
        return payment;
    }

    private static ShipmentSpecialServicesRequested addShipmentSpecialServicesRequested(ShippingOrder shippingOrder) {
        ShipmentSpecialServicesRequested shipmentSpecialServicesRequested = new ShipmentSpecialServicesRequested();
        ShipmentSpecialServiceType shipmentSpecialServiceType[] = new ShipmentSpecialServiceType[1];
        shipmentSpecialServiceType[0] = ShipmentSpecialServiceType.COD;
        shipmentSpecialServicesRequested.setSpecialServiceTypes(shipmentSpecialServiceType);
        CodDetail codDetail = new CodDetail();
        codDetail.setCollectionType(CodCollectionType.CASH);// GUARANTEED_FUNDS);
        Money codMoney = new Money();
        codMoney.setCurrency("INR");
        codMoney.setAmount(new BigDecimal(shippingOrder.getAmount())); // (400));
        codDetail.setCodCollectionAmount(codMoney);
        codDetail.setFinancialInstitutionContactAndAddress(addFinancialInstitutionParty(shippingOrder));
        codDetail.setRemitToName("Remitter");
        shipmentSpecialServicesRequested.setCodDetail(codDetail);
        return shipmentSpecialServicesRequested;
    }

    private Payment addDutiesPayment() {
        Payment payment = new Payment(); // Payment information
        payment.setPaymentType(PaymentType.SENDER);
        Payor payor = new Payor();
        Party responsibleParty = new Party();
        responsibleParty.setAccountNumber(getPayorAccountNumber());
        Address responsiblePartyAddress = new Address();
        responsiblePartyAddress.setCountryCode("IN");
        responsibleParty.setAddress(responsiblePartyAddress);
        responsibleParty.setContact(new Contact());
        payor.setResponsibleParty(responsibleParty);
        payment.setPayor(payor);
        return payment;
    }

    private static RequestedPackageLineItem addRequestedPackageLineItem(ShippingOrder shippingOrder, Double weightInKg) {
        RequestedPackageLineItem requestedPackageLineItem = new RequestedPackageLineItem();
        requestedPackageLineItem.setSequenceNumber(new PositiveInteger("1"));
        requestedPackageLineItem.setGroupPackageCount(new PositiveInteger("1"));
        requestedPackageLineItem.setWeight(addPackageWeight(weightInKg, WeightUnits.KG));// 50.5, WeightUnits.LB));
        // requestedPackageLineItem.setDimensions(addPackageDimensions(108, 5, 5, LinearUnits.IN));
        requestedPackageLineItem.setCustomerReferences(new CustomerReference[] {
                addCustomerReference(CustomerReferenceType.CUSTOMER_REFERENCE.getValue(), shippingOrder.getGatewayOrderId()), // "CR1234"
                addCustomerReference(CustomerReferenceType.INVOICE_NUMBER.getValue(), "IV1234"),// shippingOrder.getAccountingInvoiceNumber().toString()),
                addCustomerReference(CustomerReferenceType.P_O_NUMBER.getValue(), "PO1234"), });
        return requestedPackageLineItem;
    }

    private static CustomerReference addCustomerReference(String customerReferenceType, String customerReferenceValue) {
        CustomerReference customerReference = new CustomerReference();
        customerReference.setCustomerReferenceType(CustomerReferenceType.fromString(customerReferenceType));
        customerReference.setValue(customerReferenceValue);
        return customerReference;
    }

    private static LabelSpecification addLabelSpecification() {
        LabelSpecification labelSpecification = new LabelSpecification(); // Label specification
        labelSpecification.setImageType(ShippingDocumentImageType.PDF);// Image types PDF, PNG, DPL, ...
        labelSpecification.setLabelFormatType(LabelFormatType.LABEL_DATA_ONLY); // LABEL_DATA_ONLY, COMMON2D
        // labelSpecification.setLabelStockType(LabelStockType.value2); // STOCK_4X6.75_LEADING_DOC_TAB
        // labelSpecification.setLabelPrintingOrientation(LabelPrintingOrientationType.TOP_EDGE_OF_TEXT_FIRST);
        return labelSpecification;
    }

    private CustomsClearanceDetail addCustomsClearanceDetail(Double amount, Double weightInKg) {
        CustomsClearanceDetail customs = new CustomsClearanceDetail(); // International details
        customs.setDutiesPayment(addDutiesPayment());
        customs.setCustomsValue(addMoney("INR", amount));// 400.00));
        customs.setDocumentContent(InternationalDocumentContentType.NON_DOCUMENTS);
        customs.setCommercialInvoice(addCommercialInvoice());
        customs.setCommodities(new Commodity[] { addCommodity(amount, weightInKg) });// Commodity details
        return customs;
    }

    private static CommercialInvoice addCommercialInvoice() {
        CommercialInvoice commercialInvoice = new CommercialInvoice();
        commercialInvoice.setPurpose(PurposeOfShipmentType.SOLD);
        commercialInvoice.setCustomerReferences(new CustomerReference[] { addCustomerReference(CustomerReferenceType.CUSTOMER_REFERENCE.getValue(), "1234"), });
        return commercialInvoice;
    }

    private static Commodity addCommodity(Double amount, Double weightInKg) {
        Commodity commodity = new Commodity();
        commodity.setNumberOfPieces(new NonNegativeInteger("1"));
        commodity.setDescription("Books");
        commodity.setCountryOfManufacture("IN");
        commodity.setWeight(new Weight());
        commodity.getWeight().setValue(new BigDecimal(weightInKg)); // 1.0
        commodity.getWeight().setUnits(WeightUnits.KG);
        commodity.setQuantity(new NonNegativeInteger("4"));
        commodity.setQuantityUnits("EA");
        commodity.setUnitPrice(new Money());
        commodity.getUnitPrice().setAmount(new java.math.BigDecimal(100.000000));
        commodity.getUnitPrice().setCurrency("INR");
        commodity.setCustomsValue(new Money());
        commodity.getCustomsValue().setAmount(new java.math.BigDecimal(amount));// (400.000000));
        commodity.getCustomsValue().setCurrency("INR");
        commodity.setCountryOfManufacture("IN");
        commodity.setHarmonizedCode("490199009100");
        return commodity;
    }

    /*
     * private static void printFreightDetail(FreightRateDetail freightRateDetail) { if (freightRateDetail != null) {
     * System.out.println(" Freight Details"); printFreightNotations(freightRateDetail);
     * printFreightBaseCharges(freightRateDetail); } }
     */

    /*
     * private static void printFreightNotations(FreightRateDetail frd) { if (null != frd.getNotations()) {
     * System.out.println(" Notations"); FreightRateNotation notations[] = frd.getNotations(); for (int n = 0; n <
     * notations.length; n++) { printString(notations[n].getCode(), "Code", " ");
     * printString(notations[n].getDescription(), "Notification", " "); } } }
     */

    /*
     * private static void printFreightBaseCharges(FreightRateDetail frd) { if (null != frd.getBaseCharges()) {
     * FreightBaseCharge baseCharges[] = frd.getBaseCharges(); for (int i = 0; i < baseCharges.length; i++) {
     * System.out.println(" Freight Rate Details"); printString(baseCharges[i].getDescription(), "Description", " ");
     * printString(baseCharges[i].getFreightClass().getValue(), "Freight Class", " ");
     * printString(baseCharges[i].getRatedAsClass().getValue(), "Rated Class", " ");
     * printWeight(baseCharges[i].getWeight(), "Weight", " "); printString(baseCharges[i].getChargeBasis().getValue(),
     * "Charge Basis", " "); printMoney(baseCharges[i].getChargeRate(), "Charge Rate", " ");
     * printMoney(baseCharges[i].getExtendedAmount(), "Extended Amount", " "); printString(baseCharges[i].getNmfcCode(),
     * "NMFC Code", " "); } } }
     */
    /*
     * private static String printMasterTrackingNumber(CompletedShipmentDetail csd) { String trackingNumber = ""; if
     * (null != csd.getMasterTrackingId()) { trackingNumber = csd.getMasterTrackingId().getTrackingNumber();
     * System.out.println("Master Tracking Number"); System.out.println(" Type: " +
     * csd.getMasterTrackingId().getTrackingIdType()); System.out.println(" Tracking Number: " + trackingNumber); }
     * return trackingNumber; } // Saving and displaying shipping documents (labels) private static void
     * saveLabelToFile(ShippingDocument shippingDocument, String trackingNumber) throws Exception {
     * ShippingDocumentPart[] sdparts = shippingDocument.getParts(); for (int a = 0; a < sdparts.length; a++) {
     * ShippingDocumentPart sdpart = sdparts[a]; String labelLocation = System.getProperty("file.label.location"); if
     * (labelLocation == null) { labelLocation = "d:\\"; } String shippingDocumentType =
     * shippingDocument.getType().getValue(); String labelFileName = new String(labelLocation + shippingDocumentType +
     * "." + trackingNumber + "_" + a + ".pdf"); File labelFile = new File(labelFileName); FileOutputStream fos = new
     * FileOutputStream(labelFile); fos.write(sdpart.getImage()); fos.close(); System.out.println("\nlabel file name " +
     * labelFile.getAbsolutePath()); Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " +
     * labelFile.getAbsolutePath()); } } private static void saveShipmentDocumentsToFile(ShippingDocument[]
     * shippingDocument, String trackingNumber) throws Exception { if (shippingDocument != null) { for (int i = 0; i <
     * shippingDocument.length; i++) { ShippingDocumentPart[] sdparts = shippingDocument[i].getParts(); for (int a = 0;
     * a < sdparts.length; a++) { ShippingDocumentPart sdpart = sdparts[a]; String labelLocation =
     * System.getProperty("file.label.location"); if (labelLocation == null) { labelLocation = "d:\\"; } String
     * labelName = shippingDocument[i].getType().getValue(); String shippingDocumentLabelFileName = new
     * String(labelLocation + labelName + "." + trackingNumber + "_" + a + ".pdf"); File shippingDocumentLabelFile = new
     * File(shippingDocumentLabelFileName); FileOutputStream fos = new FileOutputStream(shippingDocumentLabelFile);
     * fos.write(sdpart.getImage()); fos.close(); System.out.println("\nAssociated shipment label file name " +
     * shippingDocumentLabelFile.getAbsolutePath()); Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " +
     * shippingDocumentLabelFile.getAbsolutePath()); } } } }
     *//*
         * private static void getAssociatedShipmentLabels(AssociatedShipmentDetail[] associatedShipmentDetail) throws
         * Exception { if (associatedShipmentDetail != null) { for (int j = 0; j < associatedShipmentDetail.length; j++) {
         * if (associatedShipmentDetail[j].getLabel() != null && associatedShipmentDetail[j].getType() != null) { String
         * trackingNumber = associatedShipmentDetail[j].getTrackingId().getTrackingNumber(); String
         * associatedShipmentType = associatedShipmentDetail[j].getType().getValue(); ShippingDocument
         * associatedShipmentLabel = associatedShipmentDetail[j].getLabel();
         * saveAssociatedShipmentLabelToFile(associatedShipmentLabel, trackingNumber, associatedShipmentType); } } } }
         * private static void saveAssociatedShipmentLabelToFile(ShippingDocument shippingDocument, String
         * trackingNumber, String labelName) throws Exception { ShippingDocumentPart[] sdparts =
         * shippingDocument.getParts(); for (int a = 0; a < sdparts.length; a++) { ShippingDocumentPart sdpart =
         * sdparts[a]; String labelLocation = System.getProperty("file.label.location"); if (labelLocation == null) {
         * labelLocation = "d:\\"; } String associatedShipmentLabelFileName = new String(labelLocation + labelName + "." +
         * trackingNumber + "_" + a + ".pdf"); File associatedShipmentLabelFile = new
         * File(associatedShipmentLabelFileName); FileOutputStream fos = new
         * FileOutputStream(associatedShipmentLabelFile); fos.write(sdpart.getImage()); fos.close();
         * System.out.println("\nAssociated shipment label file name " + associatedShipmentLabelFile.getAbsolutePath());
         * Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " +
         * associatedShipmentLabelFile.getAbsolutePath()); } }
         */

    private ClientDetail createClientDetail() {
        ClientDetail clientDetail = new ClientDetail();
        // String accountNumber = System.getProperty("accountNumber");
        // String meterNumber = System.getProperty("meterNumber");

        //
        // See if the accountNumber and meterNumber properties are set,
        // if set use those values, otherwise default them to "XXX"
        //
        // if (accountNumber == null) {
        String accountNumber = fedExAccountNo; // "510087020"; // Replace "XXX" with clients account number
        // }
        // if (meterNumber == null) {
        String meterNumber = fedExMeterNo; // "100073086"; // Replace "XXX" with clients meter number
        // }
        clientDetail.setAccountNumber(accountNumber);
        clientDetail.setMeterNumber(meterNumber);
        return clientDetail;
    }

    private WebAuthenticationDetail createWebAuthenticationDetail() {
        WebAuthenticationCredential wac = new WebAuthenticationCredential();
        // String key = System.getProperty("key");
        // String password = System.getProperty("password");

        //
        // See if the key and password properties are set,
        // if set use those values, otherwise default them to "XXX"
        //
        // if (key == null) {
        String key = fedExAuthKey; // Replace "XXX" with clients key
        // }
        // if (password == null) {
        String password = fedExPassword; // "6KGHIwA4iLtnHKXMQNbQ3vOBs"; // Replace "XXX" with clients password
        // }
        wac.setKey(key);
        wac.setPassword(password);
        return new WebAuthenticationDetail(wac);
    }

    private void updateEndPoint(ShipServiceLocator serviceLocator) {
        String endPoint = fedExServerUrl; // System.getProperty("endPoint");
        if (endPoint != null) {
            serviceLocator.setShipServicePortEndpointAddress(endPoint);
        }
    }
}
