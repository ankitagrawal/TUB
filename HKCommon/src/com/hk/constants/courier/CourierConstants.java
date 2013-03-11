package com.hk.constants.courier;

/**
 * Created by IntelliJ IDEA.
 * User: Rajni
 * Date: Jun 12, 2012
 * Time: 3:18:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class CourierConstants {

    //Constants for exception
    public static final String MALFORMED_URL_EXCEPTION = "Sorry.Database updation failed.Malformed URL for Tracking Id :";
    public static final String IO_EXCEPTION = "Sorry.Database updation failed.IOException encountered for Tracking Id :";
    public static final String NULL_POINTER_EXCEPTION = "Sorry.Database updation failed.NullPointerExpection encountered for Tracking :";
    public static final String EXCEPTION = "Sorry.Database updation failed.Exception encountered for Tracking Id :";
    public static final String PARSE_EXCEPTION = "Sorry.Database updation failed.ParseException occurred for Tracking Id :";
    public static final String HEALTHKART_CHECKED_EXCEPTION = "Sorry,some problem occurred.Please contact the administrator.";
	public static final String ERROR = "Error";


	//Constants for shipment
	public static final String SHIPMENT_DETAILS = "Shipment Details: ";
	public static final String GROUND_SHIPPING = "Ground Shpping: ";
	public static final String AIR_SHIPPING = "Air Shipping: ";
	public static final String PINCODE_INVALID = "Pincode could not be found in system";
	public static final String SUGGESTED_COURIER_NOT_FOUND = "Suggested Courier is not found";
	public static final String COURIER_SERVICE_INFO_NOT_FOUND = "Courier service not available here";
	public static final String DROP_SHIPPED_ORDER = "No shipment for drop shipped orders";
	public static final String AWB_NOT_ASSIGNED = "Could not assign Awb: ";
	public static final String SUCCESS = "Success";
	


    //Common constants
    public static final String DELIVERED = "DELIVERED";
    public static final String AFL = "AFL";
    public static final String BLUEDART = "BLUEDART";
    public static final String CHHOTU = "CHHOTU";
    public static final String DTDC = "DTDC";
    public static final String DELHIVERY = "DELHIVERY";
	public static final String FEDEX = "FEDEX";
	public static final String QUANTIUM = "QUANTIUM";
	public static final String INDIAONTIME = "INDIA ONTIME";


    //Constants for DTDC
    public static final String DTDC_INPUT_CONSIGNMENT = "CONSIGNMENT";
    public static final String DTDC_INPUT_CNHEADER = "CNHEADER";
    public static final String DTDC_INPUT_CNTRACK = "CNTRACK";
    public static final String DTDC_ATTRIBUTE_NAME = "NAME";
    public static final String DTDC_ATTRIBUTE_VALUE = "VALUE";
    public static final String DTDC_INPUT_STR_STATUS = "strStatus";
    public static final String DTDC_INPUT_STR_STATUSTRANSON = "strStatusTransOn";
    public static final String DTDC_INPUT_DELIVERED = "DELIVERY";

    //Constants for AFL
    public static final String AFL_DELIVERY_DATE = "delivery_date";
    public static final String AFL_ORDER_GATEWAY_ID = "order_gateway_id";
    public static final String AFL_AWB = "awb";
    public static final String AFL_CURRENT_STATUS = "current_status";

    //Constants for DELHIVERY
    public static final String DELHIVERY_SHIPMENT_DATA = "ShipmentData";
    public static final String DELHIVERY_SHIPMENT = "Shipment";
    public static final String DELHIVERY_AWB = "AWB";
    public static final String DELHIVERY_STATUS = "Status";
    public static final String DELHIVERY_STATUS_DATETIME = "StatusDateTime";
    public static final String DELHIVERY_ORDER_TYPE = "OrderType";
    public static final String DELHIVERY_ERROR_MSG = "Incorrect Waybill number or No Information";


    //Constants for BLUEDART
    public static final String BLUEDART_STATUS_DATE = "StatusDate";
    public static final String BLUEDART_STATUS = "Status";
    public static final String BLUEDART_SHIPMENT_DELIVERED = "Shipment delivered";
	public static final String BLUEDART_AWB = "WaybillNo";
	public static final String BLUEDART_REF_NO = "RefNo";
	public static final String BLUEDART_ERROR_MSG = "Incorrect Waybill number or No Information";

	//Constants for QUANTIUM
	public static final String QUANTIUM_TRACKING_NO = "TrackingNo";
	public static final String QUANTIUM_REF_NO = "Reference";
	public static final String QUANTIUM_STATUS = "CurrentStatus";
	public static final String QUANTIUM_DELIVERED = "D";
	public static final String QUANTIUM_DELIVERY_DATE = "LastUpdatedDate";
	public static final String QUANTIUM_INVALID_NO = "Invalid Tracking NO";


	public static final String INDIAONTIME_DELIVERED = "DELIVERED";
	public static final String FEDEX_DELIVERED = "Delivered";

    //Constants for Healthkart Delivery
    public static final String HKDELIVERY_WORKSHEET_FOLDER = "hkDeliveryWorksheet";
    public static final String HKDELIVERY_WORKSHEET = "HKDelivery_worksheet";
    public static final String HKDELIVERY_IOEXCEPTION = "Worksheet download failed.(IOException occurred).";
    public static final String HKDELIVERY_NULLEXCEPTION = "Worksheet download failed.(NullPointerException occurred).";
    public static final String HKDELIVERY_EXCEPTION = "Worksheet download failed.";
    public static final String HEALTHKART_DELIVERY = "HealthkartDelivery";
    public static final String HKD_WORKSHEET_HEADING1 = "HEALTHKART DELIVERY";
    public static final String HKD_WORKSHEET_HEADING2 = "T-01,4TH FLOOR,";
    public static final String HKD_WORKSHEET_HEADING3 = "PARSVANATH ARCADIA";
    public static final String HKD_WORKSHEET_HEADING4 = "MG ROAD,SECTOR 14,";
    public static final String HKD_WORKSHEET_HEADING5 = "GURGAON-122001";
    public static final String HKD_WORKSHEET_HEADING6 = "PH.NO-0124-4551666";
    public static final String HKD_WORKSHEET_NAME = "NAME";
    public static final String HKD_WORKSHEET_MOBILE = "MOBILE NO:";
    public static final String HKD_WORKSHEET_DATE = "DATE:";
	public static final String HKD_HUB = "HUB:";
    public static final String HKD_WORKSHEET_TOTALPKTS = "TOTAL PKTs:";
    public static final String HKD_WORKSHEET_TOTAL_PREPAID_BOX = "TOTAL PRE-PAID BOX:";
    public static final String HKD_WORKSHEET_TOTAL_COD_BOX = "TOTAL COD BOX:";
    public static final String HKD_WORKSHEET_TOTAL_COD_AMT = "TOTAL COD AMT:";
    public static final String HKD_WORKSHEET_COD = "COD :";
    public static final String HKD_WORKSHEET_PREPAID = "Pre-Paid";

    public static final String HKD_WORKSHEET_COD_CASH = "TOTAL COD CASH RCVD:";
    public static final String HKD_WORKSHEET_PENDING_COD = "PENDING COD AMT:";
    public static final String HKD_WORKSHEET_HOLD_PKTS = "HOLD PKTs:";
    public static final String HKD_WORKSHEET_RTO_PKTS = "RTO PKTs:";
    public static final String HKD_WORKSHEET_SNO = "S.NO.";
    public static final String HKD_WORKSHEET_AWB_NO = "AWB NUMBER";
    public static final String HKD_WORKSHEET_GATEWAYID = "Gateway OrderId";    
    public static final String HKD_WORKSHEET_ADDRESS = "ADDRESS";
    public static final String HKD_WORKSHEET_AMT = "COD/PRE-PAID AMT";
    public static final String HK_WORKSHEET_INFO = "NAME,SIGN & MOB.NO.";
    public static final String HKD_WORKSHEET_REMARKS = "REMARKS";

	public static final String SOUTH_ZONE = "SOUTH" ;
	public static final String NORTH_ZONE = "NORTH" ;
	public static final String EAST_ZONE = "EAST" ;
	public static final String WEST_ZONE = "WEST" ;


}
