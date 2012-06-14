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
    public static final String MALFORMED_URL_EXCEPTION = "Malformed URL for Shipping Order Id :";
    public static final String IO_EXCEPTION = "IOException encountered for Shipping Order Id :";
    public static final String NULL_POINTER_EXCEPTION = "NullPointerExpection encountered for Shipping Order Id :";
    public static final String EXCEPTION = "Exception encountered for Shipping Order Id :";
    public static final String PARSE_EXCEPTION = "ParseException occurred for Shipping Order Id :";

    //Common constants
    public static final String DELIVERED="DELIVERED";
    public static final String AFL = "AFL";
    public static final String BLUEDART = "BLUEDART";
    public static final String CHHOTU = "CHHOTU";
    public static final String DTDC = "DTDC";
    public static final String DELHIVERY = "DELHIVERY";


    //Constants for DTDC
    public static final String DTDC_INPUT_CONSIGNMENT="CONSIGNMENT";
    public static final String DTDC_INPUT_CNHEADER="CNHEADER";
    public static final String DTDC_INPUT_CNTRACK="CNTRACK";
    public static final String DTDC_ATTRIBUTE_NAME="NAME";
    public static final String DTDC_ATTRIBUTE_VALUE="VALUE";
    public static final String DTDC_INPUT_STR_STATUS="strStatus";
    public static final String DTDC_INPUT_STR_STATUSTRANSON="strStatusTransOn";
    public static final String DTDC_INPUT_DELIVERED="DELIVERED";

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

    //Constants for BLUEDART
    public static final String BLUEDART_STATUS_DATE = "StatusDate";
    public static final String BLUEDART_STATUS = "Status";
    public static final String BLUEDART_SHIPMENT_DELIVERED = "Shipment delivered";
       
}
