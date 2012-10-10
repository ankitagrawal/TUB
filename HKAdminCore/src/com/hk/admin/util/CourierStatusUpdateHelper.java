package com.hk.admin.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hk.constants.hkDelivery.EnumConsignmentLifecycleStatus;
import com.hk.constants.hkDelivery.EnumConsignmentStatus;
import com.hk.constants.hkDelivery.HKDeliveryConstants;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hk.constants.courier.CourierConstants;
import com.hk.exception.HealthkartCheckedException;

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
    private               BufferedReader   bufferedReader                = null;
    private               URL              url                           = null;
    private               SimpleDateFormat sdf_date                      = new SimpleDateFormat("yyyy-MM-dd");
    private static final  String           authenticationIdForDelhivery  = "9aaa943a0c74e29b340074d859b2690e07c7fb25";
    private static final  String           loginIdForBlueDart            = "GGN37392";
    private static final  String           licenceKeyForBlueDart         = "3c6867277b7a2c8cd78c8c4cb320f401";
    private               String           courierName                   = null;

   
    public Date updateDeliveryStatusAFL(String trackingId) throws HealthkartCheckedException{
        String inputLine = "";
        String response = "";
        courierName="(AFL)";


        Map<String, String> responseAFL;
        Date delivery_date = null;
         //added for debugging
        //trackingId = "861042704819986";
        try {
            url = new URL("http://trackntrace.aflwiz.com/aflwiztrack?shpntnum=" + trackingId);
            bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));

            while ((inputLine = bufferedReader.readLine()) != null) {
                if (inputLine != null) {
                    response += inputLine;
                }
            }

            responseAFL = AFLResponseParser.parseResponse(response, trackingId);
            if (responseAFL != null && responseAFL.size()!=0) {
                if (responseAFL.get(CourierConstants.AFL_DELIVERY_DATE) != null && responseAFL.get(CourierConstants.AFL_ORDER_GATEWAY_ID) != null && responseAFL.get(CourierConstants.AFL_AWB) != null
                        && responseAFL.get(CourierConstants.AFL_CURRENT_STATUS) != null) {

                    delivery_date = sdf_date.parse(responseAFL.get("delivery_date"));
                }
            }
        } catch (MalformedURLException mue) {
            logger.debug(CourierConstants.MALFORMED_URL_EXCEPTION +courierName+ trackingId);
            throw new HealthkartCheckedException(CourierConstants.MALFORMED_URL_EXCEPTION + trackingId);
        } catch (IOException ioe) {
            logger.debug(CourierConstants.IO_EXCEPTION +courierName+ trackingId);
            throw new HealthkartCheckedException(CourierConstants.IO_EXCEPTION + trackingId);
        } catch (ParseException pe) {
            logger.debug(CourierConstants.PARSE_EXCEPTION +courierName+ trackingId);
            throw new HealthkartCheckedException(CourierConstants.PARSE_EXCEPTION + trackingId);
        } catch (NullPointerException npe) {
            logger.debug(CourierConstants.NULL_POINTER_EXCEPTION +courierName+ trackingId);
            throw new HealthkartCheckedException(CourierConstants.NULL_POINTER_EXCEPTION + trackingId);
        } catch (Exception e) {
            logger.debug(CourierConstants.EXCEPTION +courierName+ trackingId);
            throw new HealthkartCheckedException(CourierConstants.EXCEPTION + trackingId);
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                logger.debug(CourierConstants.IO_EXCEPTION +courierName+ trackingId);
                throw new HealthkartCheckedException(CourierConstants.IO_EXCEPTION + trackingId);
            }
        }
        return delivery_date;
    }


    public ChhotuCourierDelivery updateDeliveryStatusChhotu(String trackingId) throws HealthkartCheckedException{
        ChhotuCourierDelivery chhotuCourierDelivery = null;
        String inputLine = "";
        String response = "";
        String jsonFormattedResponse = "";
        courierName="(Chhotu)";

        //added for debugging
       // trackingId = "10000124755";
        try {

          url = new URL("http://api.chhotu.in/shipmenttracking?tracking_number=" + trackingId);
            bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));

            while ((inputLine = bufferedReader.readLine()) != null) {
                if (inputLine != null) {
                    response += inputLine;
                }
            }
            jsonFormattedResponse = response.substring(14, (response.length() - 1));
            chhotuCourierDelivery = new Gson().fromJson(jsonFormattedResponse, ChhotuCourierDelivery.class);


        } catch (MalformedURLException mue) {
            logger.debug(CourierConstants.MALFORMED_URL_EXCEPTION +courierName+ trackingId);
            throw new HealthkartCheckedException(CourierConstants.MALFORMED_URL_EXCEPTION + trackingId);
        } catch (IOException ioe) {
            logger.debug(CourierConstants.IO_EXCEPTION +courierName+ trackingId);
            throw new HealthkartCheckedException(CourierConstants.IO_EXCEPTION + trackingId);
        } catch (NullPointerException npe) {
            logger.debug(CourierConstants.NULL_POINTER_EXCEPTION +courierName+ trackingId);
            throw new HealthkartCheckedException(CourierConstants.NULL_POINTER_EXCEPTION + trackingId);
        } catch (Exception e) {
            logger.debug(CourierConstants.EXCEPTION +courierName+ trackingId);
            throw new HealthkartCheckedException(CourierConstants.EXCEPTION + trackingId);
        } finally {
            try{
            bufferedReader.close();
            }catch (IOException ioe){
                logger.debug(CourierConstants.IO_EXCEPTION +courierName+ trackingId);
                throw new HealthkartCheckedException(CourierConstants.IO_EXCEPTION + trackingId);
            }
        }
        return chhotuCourierDelivery;
    }


    public JsonObject updateDeliveryStatusDelhivery(String trackingId) throws HealthkartCheckedException{
        JsonObject      shipmentJsonObj          = null;
        String          inputLine;
        String          jsonFormattedResponse    = "";
        JsonParser      jsonParser               = new JsonParser();
        courierName="(Delhivery)";


        try {
             //added for debugging
           //trackingId ="10410239481";
           url = new URL("http://track.delhivery.com/api/packages/json/?token=" + authenticationIdForDelhivery + "&waybill=" + trackingId);
            bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));

            while ((inputLine = bufferedReader.readLine()) != null) {
                jsonFormattedResponse += inputLine;
            }

            shipmentJsonObj = jsonParser.parse(jsonFormattedResponse).getAsJsonObject().getAsJsonArray(CourierConstants.DELHIVERY_SHIPMENT_DATA).get(0).getAsJsonObject().getAsJsonObject(CourierConstants.DELHIVERY_SHIPMENT);
        } catch (MalformedURLException mue) {
            logger.debug(CourierConstants.MALFORMED_URL_EXCEPTION +courierName+ trackingId);
            throw new HealthkartCheckedException(CourierConstants.MALFORMED_URL_EXCEPTION + trackingId);

        } catch (IOException ioe) {
            logger.debug(CourierConstants.IO_EXCEPTION +courierName+ trackingId);
            throw new HealthkartCheckedException(CourierConstants.IO_EXCEPTION + trackingId);

        } catch (NullPointerException npe) {
            logger.debug(CourierConstants.NULL_POINTER_EXCEPTION +courierName+ trackingId);
            throw new HealthkartCheckedException(CourierConstants.NULL_POINTER_EXCEPTION + trackingId);

        } catch (Exception e) {
            logger.debug(CourierConstants.EXCEPTION +courierName+ trackingId);
            throw new HealthkartCheckedException(CourierConstants.EXCEPTION + trackingId);
        }
        finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                logger.debug(CourierConstants.IO_EXCEPTION +courierName+ trackingId);
                throw new HealthkartCheckedException(CourierConstants.IO_EXCEPTION + trackingId);
            }
        }
        return shipmentJsonObj;
    }


    public JsonArray bulkUpdateDeliveryStatusDelhivery(String trackingId) throws HealthkartCheckedException {
        JsonObject shipmentJsonObj = null;
        String inputLine;
        String jsonFormattedResponse = "";
        JsonParser jsonParser = new JsonParser();
        JsonArray shipmentList     = new JsonArray();
        courierName = "(Delhivery)";


        try {
            //added for debugging
            //trackingId ="10410239481";
            url = new URL("http://track.delhivery.com/api/packages/json/?token=" + authenticationIdForDelhivery + "&waybill=" + trackingId);
            bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));

            while ((inputLine = bufferedReader.readLine()) != null) {
                jsonFormattedResponse += inputLine;
            }

            //shipmentJsonObj = jsonParser.parse(jsonFormattedResponse).getAsJsonObject().getAsJsonArray(CourierConstants.DELHIVERY_SHIPMENT_DATA).get(0).getAsJsonObject().getAsJsonObject(CourierConstants.DELHIVERY_SHIPMENT);
            shipmentList =jsonParser.parse(jsonFormattedResponse).getAsJsonObject().getAsJsonArray(CourierConstants.DELHIVERY_SHIPMENT_DATA);
        } catch (MalformedURLException mue) {
            logger.debug(CourierConstants.MALFORMED_URL_EXCEPTION + courierName + trackingId);
            throw new HealthkartCheckedException(CourierConstants.MALFORMED_URL_EXCEPTION + trackingId);

        } catch (IOException ioe) {
            logger.debug(CourierConstants.IO_EXCEPTION + courierName + trackingId);
            throw new HealthkartCheckedException(CourierConstants.IO_EXCEPTION + trackingId);

        } catch (NullPointerException npe) {
            logger.debug(CourierConstants.NULL_POINTER_EXCEPTION + courierName + trackingId);
            throw new HealthkartCheckedException(CourierConstants.NULL_POINTER_EXCEPTION + trackingId);

        } catch (Exception e) {
            logger.debug(CourierConstants.EXCEPTION + courierName + trackingId);
            throw new HealthkartCheckedException(CourierConstants.EXCEPTION + trackingId);
        }
        finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                logger.debug(CourierConstants.IO_EXCEPTION + courierName + trackingId);
                throw new HealthkartCheckedException(CourierConstants.IO_EXCEPTION + trackingId);
            }
        }
        return shipmentList;
    }


    public Element updateDeliveryStatusBlueDart(String trackingId) throws HealthkartCheckedException{
        Element    xmlElement   = null;
        String inputLine = "";
        String response = "";
        courierName="(Bluedart)";

        //added for debugging
        //trackingId              = "43872110553";

        try {
            url = new URL("http://www.bluedart.com/servlet/RoutingServlet?handler=tnt&action=custawbquery&loginid=" + loginIdForBlueDart + "&awb=awb&numbers=" + trackingId + "&format=xml&lickey=" + licenceKeyForBlueDart + "&verno=1.3&scan=1");
            bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));

            while ((inputLine = bufferedReader.readLine()) != null) {
                if (inputLine != null) {
                    response += inputLine;
                }
            }

            Document doc = new SAXBuilder().build(new StringReader(response));
            XPath xPath = XPath.newInstance("/*/Shipment");
            xmlElement = (Element) xPath.selectSingleNode(doc);

        } catch (MalformedURLException mue) {
            logger.debug(CourierConstants.MALFORMED_URL_EXCEPTION +courierName+ trackingId);
            throw new HealthkartCheckedException(CourierConstants.MALFORMED_URL_EXCEPTION + trackingId);

        } catch (IOException ioe) {
            logger.debug(CourierConstants.IO_EXCEPTION +courierName+ trackingId);
            throw new HealthkartCheckedException(CourierConstants.IO_EXCEPTION + trackingId);

        } catch (NullPointerException npe) {
            logger.debug(CourierConstants.NULL_POINTER_EXCEPTION +courierName+ trackingId);
            throw new HealthkartCheckedException(CourierConstants.NULL_POINTER_EXCEPTION + trackingId);

        } catch (Exception e) {
            logger.debug(CourierConstants.EXCEPTION +courierName + trackingId);
            throw new HealthkartCheckedException(CourierConstants.EXCEPTION + trackingId);

        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                logger.debug(CourierConstants.IO_EXCEPTION +courierName+ trackingId);
                throw new HealthkartCheckedException(CourierConstants.IO_EXCEPTION + trackingId);

            }
        }
        return xmlElement;
    }


    @SuppressWarnings("unchecked")
    public Map updateDeliveryStatusDTDC(String trackingId) throws HealthkartCheckedException{

        String                 inputLine="";
        String                 response      = "";
        Map<String, String>    responseMap   = null;
        courierName="(DTDC)";

        //added for debugging
        //trackingId="x00473680";

        try {

            url = new URL("http://cust.dtdc.co.in/DPTrack/XMLCnTrk.asp?strcnno=" + trackingId + "&TrkType=cnno&addtnlDtl=Y&strCustType=DP&strCustDtl=LL070");
            bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            while ((inputLine = bufferedReader.readLine()) != null) {
                if (inputLine != null) {
                    response += inputLine;
                }
            }
            Document doc = new SAXBuilder().build(new StringReader(response));

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
            logger.debug(CourierConstants.MALFORMED_URL_EXCEPTION +courierName+ trackingId);
            throw new HealthkartCheckedException(CourierConstants.MALFORMED_URL_EXCEPTION+trackingId);

        } catch (IOException ioe) {
            logger.debug(CourierConstants.IO_EXCEPTION +courierName+ trackingId);
            throw new HealthkartCheckedException(CourierConstants.IO_EXCEPTION + trackingId);

        } catch (NullPointerException npe) {
            logger.debug(CourierConstants.NULL_POINTER_EXCEPTION +courierName+ trackingId);
            throw new HealthkartCheckedException(CourierConstants.NULL_POINTER_EXCEPTION + trackingId);

        } catch (Exception e) {
            logger.debug(CourierConstants.EXCEPTION +courierName+ trackingId);
            throw new HealthkartCheckedException(CourierConstants.EXCEPTION + trackingId);

        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                logger.debug(CourierConstants.IO_EXCEPTION +courierName+ trackingId);
                throw new HealthkartCheckedException(CourierConstants.IO_EXCEPTION + trackingId);

            }
        }
        return responseMap;
    }

	@SuppressWarnings("unchecked")
	public String getHkDeliveryStatusForUser(String status){
		Map responseMap = new HashMap<String, String>();
		responseMap.put(EnumConsignmentLifecycleStatus.ReceivedAtHub.getStatus(), HKDeliveryConstants.USER_STATUS_RECEIVED);
		responseMap.put(EnumConsignmentLifecycleStatus.Dispatched.getStatus(),HKDeliveryConstants.USER_STATUS_DISPATCHED);
		responseMap.put(EnumConsignmentLifecycleStatus.OnHoldByCustomer.getStatus(), HKDeliveryConstants.USER_STATUS_CUSTOMERHOLD);
		responseMap.put(EnumConsignmentLifecycleStatus.Hold.getStatus(), HKDeliveryConstants.USER_STATUS_HOLD);
		responseMap.put(EnumConsignmentLifecycleStatus.Delivered.getStatus(), HKDeliveryConstants.USER_STATUS_DELIVERED);
		responseMap.put(EnumConsignmentLifecycleStatus.Damaged.getStatus(), HKDeliveryConstants.USER_STATUS_DAMAGED);
		responseMap.put(EnumConsignmentLifecycleStatus.ConsignmentLost.getStatus(), HKDeliveryConstants.USER_STATUS_LOST);
		responseMap.put(EnumConsignmentLifecycleStatus.ReturnedToHub.getStatus(), HKDeliveryConstants.USER_STATUS_RTH);
		responseMap.put(EnumConsignmentLifecycleStatus.ReturnedToSource.getStatus(), HKDeliveryConstants.USER_STATUS_RTO);
		responseMap.put(HKDeliveryConstants.HEALTHKART_HUB, HKDeliveryConstants.USER_SOURCE);
		responseMap.put(HKDeliveryConstants.DELIVERY_HUB, HKDeliveryConstants.USER_DESTINATION);
		return (String)responseMap.get(status);
	}


}
