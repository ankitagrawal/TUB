package com.hk.admin.util;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.xpath.XPath;
import org.jdom.input.SAXBuilder;

import java.util.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.net.URL;
import java.net.MalformedURLException;

import com.hk.domain.order.ShippingOrder;
import com.hk.pact.dao.shippingOrder.ShippingOrderDao;
import com.hk.constants.courier.CourierConstants;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.google.gson.JsonObject;

/**
 * Created by IntelliJ IDEA.
 * User: Rajni
 * Date: Jun 14, 2012
 * Time: 3:56:17 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class CourierStatusUpdateHelper {

    private static        Logger           logger                        = LoggerFactory.getLogger(CourierStatusUpdateHelper.class);
    private               String           inputLine                     = null;
    private               String           response                      = null;
    private               BufferedReader   bufferedReader                = null;
    private               URL              url                           = null;
    private               SimpleDateFormat sdf_date                      = new SimpleDateFormat("yyyy-MM-dd");
    private static final  String           authenticationIdForDelhivery  = "9aaa943a0c74e29b340074d859b2690e07c7fb25";
    private static final  String           loginIdForBlueDart            = "GGN37392";
    private static final  String           licenceKeyForBlueDart         = "3c6867277b7a2c8cd78c8c4cb320f401";

    @Autowired
    private               ShippingOrderDao shippingOrderDao;


    public Date updateDeliveryStatusAFL(String trackingId) {
        Map<String, String> responseAFL;
        Date delivery_date = null;
        try {
            url = new URL("http://trackntrace.aflwiz.com/aflwiztrack?shpntnum=" + trackingId);
            bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));

            while ((inputLine = bufferedReader.readLine()) != null) {
                if (inputLine != null) {
                    response += inputLine;
                }
            }
            response="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                    "<SHIPMENTTRACK>" +
                    "<SEARCHON>" +
                    "2012-06-15 13:35:21</SEARCHON>" +
                    "<SHIPMENTREPORT>" +
                    "<AWB>AF0001</AWB>" +
                    "<SHIPMENTREFERENCENUMBER>1134776-94859</SHIPMENTREFERENCENUMBER>" +
                    "<TOTALWEIGHT>0.5</TOTALWEIGHT>" +
                    "<PICKUPDATE>2012-06-01</PICKUPDATE>" +
                    "<CONSIGNORNAME><![CDATA[AQUAMARINE HEALTHCARE PVT LTD]]></CONSIGNORNAME>" +
                    "<ORIGIN>BHIWANDI</ORIGIN>" +
                    "<CONSIGNEENAME><![CDATA[VINOD JALAGAM]]></CONSIGNEENAME>" +
                    "<DESTINATION>HYDERABAD</DESTINATION>" +
                    "<CURRENTSTATUS>DELIVERED</CURRENTSTATUS>" +
                    "<RETURNAWB>No Return AWB</RETURNAWB>" +
                    "<CHECKPOINTDETAILS>" +
                    "<CHECKPOINTS>" +
                    "<CHECKPOINT>PU</CHECKPOINT>" +
                    "<CHECKPOINTDESCRIPTION>Shipment Pick Up</CHECKPOINTDESCRIPTION>" +
                    "<LOCATIONNAME>BHIWANDI EXPRESS CENTRE</LOCATIONNAME>" +
                    "<CHECKDATE>2012-06-01</CHECKDATE>" +
                    "<CHECKTIME>20:34:25</CHECKTIME>" +
                    "</CHECKPOINTS>" +
                    "<CHECKPOINTS>" +
                    "<CHECKPOINT>DF</CHECKPOINT>" +
                    "<CHECKPOINTDESCRIPTION>Depart Facility</CHECKPOINTDESCRIPTION>" +
                    "<LOCATIONNAME>MUMBAI</LOCATIONNAME>" +
                    "<CHECKDATE>2012-06-02</CHECKDATE>" +
                    "<CHECKTIME>04:40:49</CHECKTIME>" +
                    "</CHECKPOINTS>" +
                    "<CHECKPOINTS>" +
                    "<CHECKPOINT>AF</CHECKPOINT>" +
                    "<CHECKPOINTDESCRIPTION>Arrived Facility</CHECKPOINTDESCRIPTION>" +
                    "<LOCATIONNAME>Hyderabad</LOCATIONNAME>" +
                    "<CHECKDATE>2012-06-02</CHECKDATE>" +
                    "<CHECKTIME>13:27:37</CHECKTIME>" +
                    "</CHECKPOINTS>" +
                    "<CHECKPOINTS>" +
                    "<CHECKPOINT>AR</CHECKPOINT>" +
                    "<CHECKPOINTDESCRIPTION>Arrival at Delivery Facility</CHECKPOINTDESCRIPTION>" +
                    "<LOCATIONNAME>ECIL EXPRESS CENTRE</LOCATIONNAME>" +
                    "<CHECKDATE>2012-06-02</CHECKDATE>" +
                    "<CHECKTIME>14:55:00</CHECKTIME>" +
                    "</CHECKPOINTS>" +
                    "<CHECKPOINTS>" +
                    "<CHECKPOINT>WC</CHECKPOINT>" +
                    "<CHECKPOINTDESCRIPTION>With Delivering Courier</CHECKPOINTDESCRIPTION>" +
                    "<LOCATIONNAME>ECIL EXPRESS CENTRE</LOCATIONNAME>" +
                    "<CHECKDATE>2012-06-04</CHECKDATE>" +
                    "<CHECKTIME>10:35:55</CHECKTIME>" +
                    "</CHECKPOINTS>" +
                    "<CHECKPOINTS>" +
                    "<CHECKPOINT>OK</CHECKPOINT>" +
                    "<CHECKPOINTDESCRIPTION>Delivery</CHECKPOINTDESCRIPTION>" +
                    "<LOCATIONNAME>ECIL EXPRESS CENTRE</LOCATIONNAME>" +
                    "<CHECKDATE>2012-06-04</CHECKDATE>" +
                    "<CHECKTIME>14:20:00</CHECKTIME>" +
                    "</CHECKPOINTS>" +
                    "</CHECKPOINTDETAILS>" +
                    "</SHIPMENTREPORT>" +
                    "</SHIPMENTTRACK>";
            responseAFL = AFLResponseParser.parseResponse(response, trackingId);
            if (responseAFL != null) {
                if (responseAFL.get(CourierConstants.AFL_DELIVERY_DATE) != null && responseAFL.get(CourierConstants.AFL_ORDER_GATEWAY_ID) != null && responseAFL.get(CourierConstants.AFL_AWB) != null
                        && responseAFL.get(CourierConstants.AFL_CURRENT_STATUS) != null) {

                    delivery_date = sdf_date.parse(responseAFL.get("delivery_date"));
                }
            }
        } catch (MalformedURLException mue) {
            logger.debug(CourierConstants.MALFORMED_URL_EXCEPTION + trackingId);
        } catch (IOException ioe) {
            logger.debug(CourierConstants.IO_EXCEPTION + trackingId);
        } catch (ParseException pe) {
            logger.debug(CourierConstants.PARSE_EXCEPTION + trackingId);
        } catch (NullPointerException npe) {
            logger.debug(CourierConstants.NULL_POINTER_EXCEPTION + trackingId);
        } catch (Exception e) {
            logger.debug(CourierConstants.EXCEPTION + trackingId);
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                logger.debug(CourierConstants.IO_EXCEPTION + trackingId);
            }
        }
        return delivery_date;
    }


    public ChhotuCourierDelivery updateDeliveryStatusChhotu(String trackingId) {
        ChhotuCourierDelivery chhotuCourierDelivery = null;
        String jsonFormattedResponse = "";
        try {

          /*  url = new URL("http://api.chhotu.in/shipmenttracking?tracking_number=" + trackingId);
            bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));*/

           /* while ((inputLine = bufferedReader.readLine()) != null) {
                if (inputLine != null) {
                    response += inputLine;
                }
            }*/
           // bufferedReader.close();
            response="jsonpcallback({\"delivery_type\":\"Cash On Delivery\",\"cod_amount\":\"410\",\"customer_name\":\"Veneet\",\"pin_code\":\"110049\",\"delivery_date\":\"2012-06-02\",\"tracking_number\":\"10000124755\",\"STATUS\":\"SUCCESS\",\"shipment_status\":\"Delivered\"})";
            jsonFormattedResponse = response.substring(14, (response.length() - 1));
            chhotuCourierDelivery = new Gson().fromJson(jsonFormattedResponse, ChhotuCourierDelivery.class);


        } /*catch (MalformedURLException mue) {
            logger.debug(CourierConstants.MALFORMED_URL_EXCEPTION + trackingId);

        } catch (IOException ioe) {
            logger.debug(CourierConstants.IO_EXCEPTION + trackingId);
        } */catch (NullPointerException npe) {
            logger.debug(CourierConstants.NULL_POINTER_EXCEPTION + trackingId);

        } catch (Exception e) {
            logger.debug(CourierConstants.EXCEPTION + trackingId);

        }
        return chhotuCourierDelivery;
    }


    public JsonObject updateDeliveryStatusDelhivery(String trackingId) {
        JsonObject      shipmentJsonObj          = null;
        String          inputLine;
        String          jsonFormattedResponse    = "";
        JsonParser      jsonParser               = new JsonParser();
        ShippingOrder   shippingOrder            = shippingOrderDao.findByTrackingId(trackingId);

        try {

            String gatewayOrderId = "";
            if (shippingOrder != null) {
                gatewayOrderId = shippingOrder.getGatewayOrderId();
            }

           /* url = new URL("http://track.delhivery.com/api/packages/json/?token=" + authenticationIdForDelhivery + "&ref_nos=" + gatewayOrderId);
            bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));

            while ((inputLine = bufferedReader.readLine()) != null) {
                jsonFormattedResponse += inputLine;
            }
            bufferedReader.close();
*/
            //for testing
            jsonFormattedResponse="{" +
                    "    \"ShipmentData\": [" +
                    "        {" +
                    "            \"Shipment\": {" +
                    "                \"Status\": {" +
                    "                    \"Status\": \"Delivered\"," +
                    "                    \"StatusLocation\": \"Gurgaon (Haryana)\"," +
                    "                    \"StatusDateTime\": \"2012-06-05T20:34:31.756000\"," +
                    "                    \"RecievedBy\": \"\"," +
                    "                    \"StatusType\": \"DL\"," +
                    "                    \"Instructions\": \"\"" +
                    "                }," +
                    "                \"Origin\": \"Gurgaon (Haryana)\"," +
                    "                \"PickUpDate\": \"2012-05-30T19:34:47\"," +
                    "                \"ToAttention\": \"Rishabh Sayal\"," +
                    "                \"CustomerName\": \"HealthKart\"," +
                    "                \"Weight\": 3200," +
                    "                \"OrderType\": \"COD\"," +
                    "                \"Destination\": \"Gurgaon\"," +
                    "                \"Consignee\": {" +
                    "                    \"City\": \"Gurgaon\"," +
                    "                    \"Name\": \"Rishabh Sayal\"," +
                    "                    \"Country\": \"India\"," +
                    "                    \"Address2\": []," +
                    "                    \"Address3\": \"\"," +
                    "                    \"PinCode\": 122001," +
                    "                    \"State\": \"\"," +
                    "                    \"Telephone2\": \"9990606006, 9990606006, 9990606006, 9990606006, 9990606006, 9990606006, 9990606006, 9990606006, 9990606006, 9990606006, 9990606006, 9990606006, 9990606006, 9990606006, 9990606006\"," +
                    "                    \"Telephone1\": [" +
                    "                        \"9990606006\"" +
                    "                    ]," +
                    "                    \"Address1\": [" +
                    "                        \"House no. 1163 Sector 10/A\"" +
                    "                    ]" +
                    "                }," +
                    "                \"ReferenceNo\": \"1134173-47619\"," +
                    "                \"InvoiceAmount\": 359900," +
                    "                \"CODAmount\": 359900," +
                    "                \"SenderName\": \"HealthKart\"," +
                    "                \"AWB\": \"10410113466\"," +
                    "                \"Scans\": [" +
                    "                    {" +
                    "                        \"ScanDetail\": {" +
                    "                            \"ScanDateTime\": \"2012-05-30T22:09:37.083000\"," +
                    "                            \"ScanType\": \"UD\"," +
                    "                            \"Scan\": \"Pending\"," +
                    "                            \"StatusDateTime\": \"2012-05-30T22:09:37.083000\"," +
                    "                            \"ScannedLocation\": \"Gurgaon (Haryana)\"," +
                    "                            \"Instructions\": \"Shipment received\"" +
                    "                        }" +
                    "                    }," +
                    "                    {" +
                    "                        \"ScanDetail\": {" +
                    "                            \"ScanDateTime\": \"2012-06-05T14:45:49\"," +
                    "                            \"ScanType\": \"UD\"," +
                    "                            \"Scan\": \"Dispatched\"," +
                    "                            \"StatusDateTime\": \"2012-06-05T14:45:49\"," +
                    "                            \"ScannedLocation\": \"Gurgaon (Haryana)\"," +
                    "                            \"Instructions\": \"Dispatched for Delivery\"" +
                    "                        }" +
                    "                    }," +
                    "                    {" +
                    "                        \"ScanDetail\": {" +
                    "                            \"ScanDateTime\": \"2012-06-05T20:34:31.756000\"," +
                    "                            \"ScanType\": \"DL\"," +
                    "                            \"Scan\": \"Delivered\"," +
                    "                            \"StatusDateTime\": \"2012-06-05T20:34:31.756000\"," +
                    "                            \"ScannedLocation\": \"Gurgaon (Haryana)\"," +
                    "                            \"Instructions\": \"\"" +
                    "                        }" +
                    "                    }" +
                    "                ]" +
                    "            }" +
                    "        }" +
                    "    ]" +
                    "}";

            shipmentJsonObj = jsonParser.parse(jsonFormattedResponse).getAsJsonObject().getAsJsonArray(CourierConstants.DELHIVERY_SHIPMENT_DATA).get(0).getAsJsonObject().getAsJsonObject(CourierConstants.DELHIVERY_SHIPMENT);
        } /*catch (MalformedURLException mue) {
            logger.debug(CourierConstants.MALFORMED_URL_EXCEPTION + trackingId);

        } catch (IOException ioe) {
            logger.debug(CourierConstants.IO_EXCEPTION + trackingId);

        }*/ catch (NullPointerException npe) {
            logger.debug(CourierConstants.NULL_POINTER_EXCEPTION + trackingId);

        } catch (Exception e) {
            logger.debug(CourierConstants.EXCEPTION + trackingId);
            e.printStackTrace();

        }
        return shipmentJsonObj;
    }


    public Element updateDeliveryStatusBlueDart(String trackingId) {
        Element    xmlElement   = null;
        String     inputLine;
        String     response     = "";

        try {
            url = new URL("http://www.bluedart.com/servlet/RoutingServlet?handler=tnt&action=custawbquery&loginid=" + loginIdForBlueDart + "&awb=awb&numbers=" + trackingId + "&format=xml&lickey=" + licenceKeyForBlueDart + "&verno=1.3&scan=1");
            bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));

            while ((inputLine = bufferedReader.readLine()) != null) {
                if (inputLine != null) {
                    response += inputLine;
                }
            }

            response="<ShipmentData>" +
                    "<Shipment WaybillNo=\"44330038841\" RefNo=\"1131275-77758\">" +
                    "<Service>Domestic Priority</Service>" +
                    "<PickUpDate>01 June 2012</PickUpDate>" +
                    "<Origin>NAVIMUMBAI</Origin>" +
                    "<Destination>SRINAGAR</Destination>" +
                    "<ProductType>Non Documents</ProductType>" +
                    "<CustomerName>AQUAMARINE HEALTHCARE -PREPAID</CustomerName>" +
                    "<SenderName>AQUAMARINE HEALTHCAR</SenderName>" +
                    "<ToAttention>RUQSANA FAROOQ</ToAttention>" +
                    "<Consignee>RUQSANA FAROOQ</Consignee>" +
                    "<ConsigneeAddress1>SHAKTI COLONY ,YAKHRAJPURA</ConsigneeAddress1>" +
                    "<ConsigneeAddress2>NEAR FIRDOUS GENERAL STORE</ConsigneeAddress2>" +
                    "<ConsigneeAddress3>SRINAGAR/Jammu and Kash</ConsigneeAddress3>" +
                    "<ConsigneePincode>190008</ConsigneePincode>" +
                    "<ConsigneeTelNo>9086873133</ConsigneeTelNo>" +
                    "<Weight>1.5</Weight>" +
                    "<Status>Shipment delivered</Status>" +
                    "<StatusType>DL</StatusType>" +
                    "<StatusDate>07 June 2012</StatusDate>" +
                    "<StatusTime>13:05</StatusTime>" +
                    "<ReceivedBy>SIGN</ReceivedBy>" +
                    "<Instructions>General Goods</Instructions>" +
                    "<Scans>" +
                    "<ScanDetail>" +
                    "<Scan>Shipment delivered</Scan>" +
                    "<ScanType>DL</ScanType>" +
                    "<ScanDate>07-Jun-2012</ScanDate>" +
                    "<ScanTime>13:05</ScanTime>" +
                    "<ScannedLocation>SRINAGAR</ScannedLocation>" +
                    "</ScanDetail>" +
                    "<ScanDetail>" +
                    "<Scan>Shipment Outscan</Scan>" +
                    "<ScanType>UD</ScanType>" +
                    "<ScanDate>07-Jun-2012</ScanDate>" +
                    "<ScanTime>11:38</ScanTime>" +
                    "<ScannedLocation>SRINAGAR</ScannedLocation>" +
                    "</ScanDetail>" +
                    "<ScanDetail>" +
                    "<Scan>Shipment Inscan</Scan>" +
                    "<ScanType>UD</ScanType>" +
                    "<ScanDate>05-Jun-2012</ScanDate>" +
                    "<ScanTime>17:09</ScanTime>" +
                    "<ScannedLocation>SRINAGAR</ScannedLocation>" +
                    "</ScanDetail>" +
                    "<ScanDetail>" +
                    "<Scan>SHIPMENT OUTSCANNED TO NETWORK</Scan>" +
                    "<ScanType>UD</ScanType>" +
                    "<ScanDate>03-Jun-2012</ScanDate>" +
                    "<ScanTime>07:00</ScanTime>" +
                    "<ScannedLocation>DELHI HUB</ScannedLocation>" +
                    "</ScanDetail>" +
                    "<ScanDetail>" +
                    "<Scan>PLASTIC BAG - AUTO TALLY</Scan>" +
                    "<ScanType>UD</ScanType>" +
                    "<ScanDate>02-Jun-2012</ScanDate>" +
                    "<ScanTime>06:52</ScanTime>" +
                    "<ScannedLocation>DELHI HUB</ScannedLocation>" +
                    "</ScanDetail>" +
                    "<ScanDetail>" +
                    "<Scan>SHIPMENT OUTSCANNED TO NETWORK</Scan>" +
                    "<ScanType>UD</ScanType>" +
                    "<ScanDate>02-Jun-2012</ScanDate>" +
                    "<ScanTime>05:12</ScanTime>" +
                    "<ScannedLocation>MUMBAI HUB</ScannedLocation>" +
                    "</ScanDetail>" +
                    "<ScanDetail>" +
                    "<Scan>Canvas Bag Consolidated Scan</Scan>" +
                    "<ScanType>UD</ScanType>" +
                    "<ScanDate>02-Jun-2012</ScanDate>" +
                    "<ScanTime>02:38</ScanTime>" +
                    "<ScannedLocation>MUMBAI HUB</ScannedLocation>" +
                    "</ScanDetail>" +
                    "<ScanDetail>" +
                    "<Scan>SHIPMENT OUTSCANNED TO NETWORK</Scan>" +
                    "<ScanType>UD</ScanType>" +
                    "<ScanDate>02-Jun-2012</ScanDate>" +
                    "<ScanTime>00:00</ScanTime>" +
                    "<ScannedLocation>OVALI WAREHOUSE</ScannedLocation>" +
                    "</ScanDetail>" +
                    "<ScanDetail>" +
                    "<Scan>Shipment Inscan</Scan>" +
                    "<ScanType>UD</ScanType>" +
                    "<ScanDate>01-Jun-2012</ScanDate>" +
                    "<ScanTime>21:23</ScanTime>" +
                    "<ScannedLocation>OVALI WAREHOUSE</ScannedLocation>" +
                    "</ScanDetail>" +
                    "</Scans>" +
                    "</Shipment>" +
                    "</ShipmentData>";
            Document doc = new SAXBuilder().build(new StringReader(response));
            XPath xPath = XPath.newInstance("/*/Shipment");
            xmlElement = (Element) xPath.selectSingleNode(doc);

        } catch (MalformedURLException mue) {
            logger.debug(CourierConstants.MALFORMED_URL_EXCEPTION + trackingId);

        } catch (IOException ioe) {
            logger.debug(CourierConstants.IO_EXCEPTION + trackingId);

        } catch (NullPointerException npe) {
            logger.debug(CourierConstants.NULL_POINTER_EXCEPTION + trackingId);

        } catch (Exception e) {
            logger.debug(CourierConstants.EXCEPTION + trackingId);

        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                logger.debug(CourierConstants.IO_EXCEPTION + trackingId);

            }
        }
        return xmlElement;
    }


    public Map updateDeliveryStatusDTDC(String trackingId) {

        String                 inputLine;
        String                 response      = "";
        Map<String, String>    responseMap   = null;

        try {

            url = new URL("http://cust.dtdc.co.in/DPTrack/XMLCnTrk.asp?strcnno=" + trackingId + "&TrkType=" + trackingId + "&addtnlDtl=Y&strCustType=DP&strCustDtl=DP");
            bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            //added for debugging
            response = "<DTDCREPLY>" +
                    "<CONSIGNMENT>" +
                    "<CNHEADER>" +
                    "<CNTRACK>TRUE</CNTRACK>" +
                    "<FIELD NAME=\"strShipmentNo\" VALUE=\"A15082271\" />" +
                    "<FIELD NAME=\"strRefNo\" VALUE=\"N/A\" />" +
                    "<FIELD NAME=\"strMode\" VALUE=\"AIR\" />" +
                    "<FIELD NAME=\"strOrigin\" VALUE=\"LEAK-PROOF ENGINEERING PVT.LTD, AHMEDABAD\" />" +
                    "<FIELD NAME=\"strOriginRemarks\" VALUE=\"Received from\" />" +
                    "<FIELD NAME=\"strBookedOn\" VALUE=\"08072009\" />" +
                    "<FIELD NAME=\"strPieces\" VALUE=\"1\" />" +
                    "<FIELD NAME=\"strWeightUnit\" VALUE=\"Kg\" />" +
                    "<FIELD NAME=\"strWeight\" VALUE=\"0.020\" />" +
                    "<FIELD NAME=\"strDestination\" VALUE=\"PUNE\" />" +
                    "<FIELD NAME=\"strStatus\" VALUE=\"DELIVERED\" />" +
                    "<FIELD NAME=\"strStatusTransOn\" VALUE=\"12062012\" />" +
                    "<FIELD NAME=\"strStatusTransTime\" VALUE=\"1210\" />" +
                    "<FIELD NAME=\"strRemarks\" VALUE=\"CO SEAL\" />" +
                    "<FIELD NAME=\"strNoOfAttempts\" VALUE=\"\" />" +
                    "</CNHEADER>" +
                    "<CNBODY>" +
                    "<CNACTIONTRACK>TRUE</CNACTIONTRACK>" +
                    "<CNACTION>" +
                    "<FIELD NAME=\"strAction\" VALUE=\"DISPATCHED\" />" +
                    "<FIELD NAME=\"strManifestNo\" VALUE=\"A1534068\" />" +
                    "<FIELD NAME=\"strOrigin\" VALUE=\"AHMEDABAD APEX, AHMEDABAD\" />" +
                    "<FIELD NAME=\"strDestination\" VALUE=\"PUNE APEX, PUNE\" />" +
                    "<FIELD NAME=\"strActionDate\" VALUE=\"08072009\" />" +
                    "<FIELD NAME=\"strRemarks\" VALUE=\"\" />" +
                    "</CNACTION>" +
                    "<CNACTION>" +
                    "<FIELD NAME=\"strAction\" VALUE=\"RECEIVED\" />" +
                    "<FIELD NAME=\"strManifestNo\" VALUE=\"A1534068\" />" +
                    "<FIELD NAME=\"strOrigin\" VALUE=\"AHMEDABAD, AHMEDABAD\" />" +
                    "<FIELD NAME=\"strDestination\" VALUE=\"PUNE APEX, PUNE\" />" +
                    "<FIELD NAME=\"strActionDate\" VALUE=\"09072009\" />" +
                    "<FIELD NAME=\"strRemarks\" VALUE=\"\" />" +
                    "</CNACTION>" +
                    "<CNACTION>" +
                    "<FIELD NAME=\"strAction\" VALUE=\"DISPATCHED\" />" +
                    "<FIELD NAME=\"strManifestNo\" VALUE=\"90033365\" />" +
                    "<FIELD NAME=\"strOrigin\" VALUE=\"PUNE, PUNE\" />" +
                    "<FIELD NAME=\"strDestination\" VALUE=\"PUNE SHIVAJI NAGAR BRANCH, PUNE\" />" +
                    "<FIELD NAME=\"strActionDate\" VALUE=\"09072009\" />" +
                    "<FIELD NAME=\"strRemarks\" VALUE=\"\" />" +
                    "</CNACTION>" +
                    "<CNACTION>" +
                    "<FIELD NAME=\"strAction\" VALUE=\"RECEIVED\" />" +
                    "<FIELD NAME=\"strManifestNo\" VALUE=\"90033365\" />" +
                    "<FIELD NAME=\"strOrigin\" VALUE=\"PUNE, PUNE\" />" +
                    "<FIELD NAME=\"strDestination\" VALUE=\"PUNE SHIVAJI NAGAR BRANCH, PUNE\" />" +
                    "<FIELD NAME=\"strActionDate\" VALUE=\"09072009\" />" +
                    "<FIELD NAME=\"strRemarks\" VALUE=\"\" />" +
                    "</CNACTION>" +
                    "<CNACTION>" +
                    "<FIELD NAME=\"strAction\" VALUE=\"OUT FOR DELIVERY\" />" +
                    "<FIELD NAME=\"strManifestNo\" VALUE=\"91108846\" />" +
                    "<FIELD NAME=\"strOrigin\" VALUE=\"PUNE SHIVAJI NAGAR BRANCH, PUNE\" />" +
                    "<FIELD NAME=\"strDestination\" VALUE=\"KOTHRUD (MAIN ROAD), PUNE\" />" +
                    "<FIELD NAME=\"strActionDate\" VALUE=\"09072009\" />" +
                    "<FIELD NAME=\"strRemarks\" VALUE=\"\" />" +
                    "</CNACTION>" +
                    "</CNBODY>" +
                    "</CONSIGNMENT>" +
                    "</DTDCREPLY>";
            /*while ((inputLine = bufferedReader.readLine()) != null) {
              if (inputLine != null) {
                response += inputLine;
              }
            }*/
            Document doc = new SAXBuilder().build(new StringReader(response));

            //XPath xPath = XPath.newInstance("/*/Shipment");
            //Element ele = (Element) xPath.selectSingleNode(doc);
            Element element = doc.getRootElement();
            Element consig = element.getChild(CourierConstants.DTDC_INPUT_CONSIGNMENT);
            Element header = consig.getChild(CourierConstants.DTDC_INPUT_CNHEADER);
            Element cnTrack = header.getChild(CourierConstants.DTDC_INPUT_CNTRACK);
            String trackStatus = header.getChildText(CourierConstants.DTDC_INPUT_CNTRACK);
            List fields = header.getChildren();
            if (trackStatus.equalsIgnoreCase("TRUE")) {
                responseMap = new HashMap<String, String>();
                for (int i = 0; i < fields.size(); i++) {
                    Element elementObj = (Element) fields.get(i);
                    if (elementObj != cnTrack) {
                        if (elementObj.getAttribute(CourierConstants.DTDC_ATTRIBUTE_NAME).getValue().equals(CourierConstants.DTDC_INPUT_STR_STATUS) ||
                                elementObj.getAttribute(CourierConstants.DTDC_ATTRIBUTE_NAME).getValue().equals(CourierConstants.DTDC_INPUT_STR_STATUSTRANSON)) {
                            responseMap.put(elementObj.getAttribute(CourierConstants.DTDC_ATTRIBUTE_NAME).getValue(), elementObj.getAttribute(CourierConstants.DTDC_ATTRIBUTE_VALUE).getValue());
                        }
                    }
                }
            }

        } catch (MalformedURLException mue) {
            logger.debug(CourierConstants.MALFORMED_URL_EXCEPTION + trackingId);

        } catch (IOException ioe) {
            logger.debug(CourierConstants.IO_EXCEPTION + trackingId);

        } catch (NullPointerException npe) {
            logger.debug(CourierConstants.NULL_POINTER_EXCEPTION + trackingId);

        } catch (Exception e) {
            logger.debug(CourierConstants.EXCEPTION + trackingId);

        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                logger.debug(CourierConstants.IO_EXCEPTION + trackingId);

            }
        }
        return responseMap;
    }


}
