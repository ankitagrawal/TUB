package com.hk.constants.hkDelivery;

public class HKDeliveryConstants {

    //Constants for exception
    public static final String EXCEPTION = "Some exception occurred while creating Consignment for Awb Number:";


    // Constants for user messages
    public static final String CONSIGNMNT_CREATION_SUCCESS = "Consignments created successfully.";
    public static final String CONSIGNMNT_CREATION_FAILURE = "Consignment cannot be created.Please enter awb number.";
    public static final String CONSIGNMNT_DUPLICATION_MSG = "Consignments already created for following trackingIds:";
    public static final String OPEN_RUNSHEET_MESSAGE = "Cannot create runsheet.Following awbNumbers are already linked to an OPEN runsheet:";
    public static final String HUB_CREATION_SUCCESS = "New Hub created successfully.";
    public static final String HUB_EDIT_SUCCESS = "Hub edited successfully.";
    public static final String HUB_CREATION_FAILURE = "Some problem occurred while creating a hub.Please try later.";
    public static final String HUB_EDIT_FAILURE = "Some problem occurred while editing a hub.Please try later.";
    public static final String INVALID_PINCODE_MSG ="Entered pincode doesn't exist in database.";




    //Constants for Hubs
    public static final Long HEALTHKART_HUB_ID = 100L;
    public static final Long DELIVERY_HUB_ID = 200L;
    public static final String HEALTHKART_HUB = "Healthkart Hub";
    public static final String DELIVERY_HUB = "Delivery";
    public static final String GURGAON_HUB = "Gurgaon Hub";
    public static final String SOUTH_DELHI_HUB = "South Delhi Hub";

    // Constants for url-parameters used in HK Delivery.
    public static final String RUNSHEET_DOWNLOAD ="runsheetDownloadFunctionality";

    //Constants for consignment payment mode
    public static final String COD = "COD";
    public static final String PREPAID = "PrePaid";


}
