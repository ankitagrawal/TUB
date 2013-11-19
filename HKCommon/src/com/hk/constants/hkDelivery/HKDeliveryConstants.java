package com.hk.constants.hkDelivery;

public class HKDeliveryConstants {

    //Constants for exception
    public static final String EXCEPTION = "Some exception occurred while creating Consignment for Awb Number:";


    // Constants for user messages
    public static final String CONSIGNMNT_CREATION_SUCCESS = " Consignments created successfully.";
    public static final String CONSIGNMNT_CREATION_FAILURE = "Consignment cannot be created.Please enter awb number.";
    public static final String CONSIGNMNT_DUPLICATION_MSG = "Consignments already created for following trackingIds: ";
    public static final String OPEN_RUNSHEET_MESSAGE = "Cannot create runsheet.Following awbNumbers are already linked to an OPEN runsheet:";
    public static final String HUB_CREATION_SUCCESS = "New Hub created successfully.";
    public static final String HUB_EDIT_SUCCESS = "Hub edited successfully.";
    public static final String HUB_CREATION_FAILURE = "Some problem occurred while creating a hub.Please try later.";
    public static final String HUB_EDIT_FAILURE = "Some problem occurred while editing a hub.Please try later.";
    public static final String INVALID_PINCODE_MSG ="Entered pincode doesn't exist in database.";
    public static final String CONSIGNMENT_FAILURE = "Consignments cannot be created.Some problem occurred.";




    //Constants for Hubs
    public static final String HEALTHKART_HUB = "Source Hub";
    public static final String DELIVERY_HUB = "Delivered Hub";

    // Constants for url-parameters used in HK Delivery.
    public static final String RUNSHEET_DOWNLOAD ="runsheetDownloadFunctionality";
    public static final String DOWNLOAD_DELIVERY_SHEET ="downloadDeliveryWorkSheet";
    public static final String PREVIEW_RUNSHEET = "previewRunsheet";
    public static final String RUNSHEET_PREVIEW_PARAM ="runsheetPreview";

    //Constants for consignment payment mode
    public static final String COD = "COD";
    public static final String PREPAID = "PrePaid";

    // Constants for COD params
    public static final String TOTAL_COD_AMT = "totalCODAmount";
    public static final String TOTAL_COD_PKTS = "totalCODPackets";

	public static final String USER_STATUS_RECEIVED = "Shipment received at courier hub.";
	public static final String USER_STATUS_DISPATCHED = "Shipment has been dispatched from hub.";
	public static final String USER_STATUS_CUSTOMERHOLD = "You asked us to hold your shipment.";
	public static final String USER_STATUS_HOLD = "Shipment On Hold at courier hub.";
	public static final String USER_STATUS_DELIVERED = "Shipment has been delivered.";
	public static final String USER_STATUS_DAMAGED = "Shipment is damaged.";
	public static final String USER_STATUS_LOST = "Shipment is lost.";
	public static final String USER_STATUS_RTH = "Shipment returned.";
	public static final String USER_STATUS_RTO = "Shipment returned to healthkart.";
	public static final String USER_SOURCE = "Healthkart Warehouse";
	public static final String USER_DESTINATION = "Customer";

    public static final String CUST_HOLD_WRONG_ADDRESS = "Address Incorrect";
	public static final String CUST_HOLD_HOUSE_LOCKED = "House Locked";
	public static final String CUST_HOLD_UNCONTACTABLE = "Customer Not contactable";
	public static final String CUST_HOLD_PAYMENT_NOT_READY = "COD Payment not ready";
    public static final String CUST_HOLD_FUTURE_DELIVERY = "Customer requested future delivery date";
    public static final String CUST_HOLD_WRONG_DELIVERY = "Customer Refuse - Wrong delivery";
	public static final String CUST_HOLD_DELAY_DELIVERY = "Customer Refuse - Delay Delivery";
	public static final String CUST_HOLD_NOT_INTERESTED = "Customer Refuse - Not interested";

    public static final String CUST_HOLD_WRONG_ADDRESS_DISPLAY = "Address Incomplete / Incorrect";
    public static final String CUST_HOLD_HOUSE_LOCKED_DISPLAY = "House Locked";
    public static final String CUST_HOLD_UNCONTACTABLE_DISPLAY = "We were not able to contact you";
    public static final String CUST_HOLD_PAYMENT_NOT_READY_DISPLAY = "Payment not ready";
    public static final String CUST_HOLD_FUTURE_DELIVERY_DISPLAY = "Future Delivery Request";
    public static final String CUST_HOLD_WRONG_DELIVERY_DISPLAY = "Customer Refuse - Wrong delivery";
    public static final String CUST_HOLD_DELAY_DELIVERY_DISPLAY = "Customer Refuse - Delay Delivery";
    public static final String CUST_HOLD_NOT_INTERESTED_DISPLAY = "Customer Refuse - Not interested";
}
