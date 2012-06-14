package com.hk.admin.manager;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.hk.admin.pact.service.shippingOrder.AdminShippingOrderService;
import com.hk.admin.util.AFLResponseParser;
import com.hk.admin.util.ChhotuCourierDelivery;
import com.hk.constants.report.ReportConstants;
import com.hk.constants.courier.EnumCourier;
import com.hk.constants.courier.CourierConstants;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.courier.Shipment;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.user.User;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.service.shippingOrder.ShippingOrderService;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.xpath.XPath;

@SuppressWarnings("unchecked")
@Component
public class DeliveryStatusUpdateManager {

    private static       Logger                logger                        = LoggerFactory.getLogger(DeliveryStatusUpdateManager.class);

    List                                       sheetData                     = new ArrayList();
    ShippingOrder                              shippingOrder;
    int                                        ordersDelivered               = 0;
    String                                     courierName                   = "Delivered by ";
    String                                     prefixComments                = "Delivered Items :<br/>";
    public static final  int                   digitsInGatewayId             = 5;
    private static final String                authenticationIdForDelhivery  = "9aaa943a0c74e29b340074d859b2690e07c7fb25";
    List<Long>                                 courierIdList                 = new ArrayList<Long>();
    private static final String                loginIdForBlueDart            = "GGN37392";
    private static final String                licenceKeyForBlueDart         = "3c6867277b7a2c8cd78c8c4cb320f401";
    private static final String                trackingId                    ="";
    private              Date                  delivery_date                 = null;
    private              BufferedReader        in                            = null;
    private              String                inputLine                     = null;
    private              String                response                      = null;


    LineItemDao                 lineItemDaoProvider;

    @Autowired
    AdminShippingOrderService   adminShippingOrderService;

    @Autowired
    ShippingOrderService        shippingOrderService;

    public String updateDeliveryStatusDTDC(File excelFile) throws Exception {
        String messagePostUpdation = "";
        logger.debug("parsing Delivery Details DTDC Upload : " + excelFile.getAbsolutePath());
        POIFSFileSystem objInFileSys = new POIFSFileSystem(new FileInputStream(excelFile));

        HSSFWorkbook workbook = new HSSFWorkbook(objInFileSys);
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        HSSFDataFormat dataFormat = workbook.createDataFormat();
        cellStyle.setDataFormat(dataFormat.getFormat("text"));

        // Assuming there is only one sheet, the first one only will be picked
        HSSFSheet dtdcDeliverySheet = workbook.getSheetAt(0);
        Iterator<Row> objRowIt = dtdcDeliverySheet.rowIterator();
        Map<Integer, String> headerMap;
        Map<Integer, String> rowMap;
        int rowCount = 0;
        SimpleDateFormat sdf_date = new SimpleDateFormat("dd/MM/yyyy");
        try {
            headerMap = getRowMap(objRowIt);
            while (objRowIt.hasNext()) {
                rowCount++;
                rowMap = getRowMapStringFormat(objRowIt);
                String gatewayOrderIdInXls = getCellValue(ReportConstants.REF_NO, rowMap, headerMap);
                String awb = getCellValue(ReportConstants.CN_NO, rowMap, headerMap);
                String status = getCellValue(ReportConstants.STATUS, rowMap, headerMap);
                Date deliveryDateInXsl = null;
                try {
                    deliveryDateInXsl = sdf_date.parse(getCellValue(ReportConstants.DELIVERED_DATE, rowMap, headerMap));
                } catch (Exception e) {
                    messagePostUpdation += "Date is not in correct format @Row No." + (rowCount + 1) + "<br/>";
                    continue;
                }
                ShippingOrder shippingOrder = null;
                if (StringUtils.isBlank(awb)) {
                    messagePostUpdation += "CNNO cannot be blank @Row No." + (rowCount + 1) + "<br/>";
                    continue;
                }
                if (StringUtils.isBlank(gatewayOrderIdInXls) && StringUtils.isBlank(awb)) {
                    messagePostUpdation += "Ref. No. and CNNO cannot be null simultaneously @Row No. " + (rowCount + 1) + "<br/>";
                    continue;
                }
                if (StringUtils.isNotBlank(gatewayOrderIdInXls)) {
                    shippingOrder = getShippingOrderService().findByGatewayOrderId(gatewayOrderIdInXls);
                } else {
                    shippingOrder = getShippingOrderService().findByTrackingId(awb);
                }
                if (shippingOrder == null) {
                    messagePostUpdation += "Shipping order not found corresponding to the Ref No. @Row No." + (rowCount + 1) + "<br/>";
                    continue;
                }
                Shipment shipment = shippingOrder.getShipment();
                if (shipment == null) {
                    messagePostUpdation += "Shipment not found corresponding to Row No." + (rowCount + 1) + "<br/>";
                    continue;
                }
                if (status.equalsIgnoreCase("DELIVERED")) {
                    updateCourierDeliveryStatus(shippingOrder, shipment, awb, deliveryDateInXsl);
                }
            }
        } catch (Exception e) {
            logger.error("Exception @ Row:" + rowCount, e);
            throw new Exception("Exception @ Row:" + rowCount, e);
        }
        logger.debug("message post updation " + messagePostUpdation);
        return messagePostUpdation;
    }

    public Date updateDeliveryStatusAFL(String trackingId) throws IOException {

        Map<String, String> responseAFL;
        URL url = new URL("http://trackntrace.aflwiz.com/aflwiztrack?shpntnum=" + trackingId);
        in = new BufferedReader(new InputStreamReader(url.openStream()));
        while ((inputLine = in.readLine()) != null) {
            if (inputLine != null) {
                response += inputLine;
            }
        }
        responseAFL = AFLResponseParser.parseResponse(response, trackingId);
        if (responseAFL.get(CourierConstants.AFL_DELIVERY_DATE) != null && responseAFL.get(CourierConstants.AFL_ORDER_GATEWAY_ID) != null && responseAFL.get(CourierConstants.AFL_AWB) != null
                && responseAFL.get(CourierConstants.AFL_CURRENT_STATUS) != null) {

            delivery_date = sdf_date.parse(responseAFL.get("delivery_date"));
        }
        return delivery_date;
    }

    public Date updateDeliveryStatusChhotu(String trackingId) throws IOException {
        URL url = new URL("http://api.chhotu.in/shipmenttracking?tracking_number=" + trackingId);
        in = new BufferedReader(new InputStreamReader(url.openStream()));
        String inputLine;
        String response = "";
        String jsonFormattedResponse = "";

        while ((inputLine = in.readLine()) != null) {
            if (inputLine != null) {
                response += inputLine;
            }
        }
        in.close();
        jsonFormattedResponse = response.substring(14, (response.length() - 1));
        ChhotuCourierDelivery chhotuCourierDelivery = new Gson().fromJson(jsonFormattedResponse, ChhotuCourierDelivery.class);
        delivery_date = chhotuCourierDelivery.getFormattedDeliveryDate();

        /*if (delivery_date != null && chhotuCourierDelivery.getShipmentStatus().equalsIgnoreCase(CourierConstants.DELIVERED) && chhotuCourierDelivery.getTrackingId() != null) {
            ordersDelivered = updateCourierDeliveryStatus(shippingOrderInList, shipment, trackingId, delivery_date);
        } else {
            logger.error("Delivery date not available or status is not delieved for : " + shipment.getTrackingId());
        }*/
        return delivery_date;

    }

    public int updateCourierDeliveryStatus(ShippingOrder shippingOrder, Shipment shipment, String trackingId, Date deliveryDate) {

        int orderDeliveryCount = 0;
        if (shippingOrder.getOrderStatus().getId().equals(EnumShippingOrderStatus.SO_Shipped.getId())) {
            if (shipment.getShipDate().after(deliveryDate) || deliveryDate.after(new Date())) {
                Calendar deliveryDateAsShipDatePlusOne = Calendar.getInstance();
                deliveryDateAsShipDatePlusOne.setTime(shipment.getShipDate());
                deliveryDateAsShipDatePlusOne.add(Calendar.DAY_OF_MONTH, 1);
                deliveryDate = deliveryDateAsShipDatePlusOne.getTime();
            }
            if (shipment != null && shipment.getTrackingId() != null && shipment.getTrackingId().equals(trackingId)) {
                shipment.setDeliveryDate(deliveryDate);
                getAdminShippingOrderService().markShippingOrderAsDelivered(shippingOrder);
                orderDeliveryCount++;
            }
        }
        return orderDeliveryCount;
    }

    public String format_gateway_order_id(String order_gateway_id, int digitsInGatewayId) {
        if (order_gateway_id.length() > digitsInGatewayId - 1) {
            order_gateway_id = (order_gateway_id.substring(0, order_gateway_id.length() - digitsInGatewayId) + "-" + order_gateway_id.substring(order_gateway_id.length()
                    - digitsInGatewayId, order_gateway_id.length()));
        }
        return order_gateway_id;
    }

    public int updateDeliveryStatusDelhivery(String gatewayOrderId) {
                String ref_no = gatewayOrderId;

                    URL url = new URL("http://track.delhivery.com/api/packages/json/?token=" + authenticationIdForDelhivery + "&ref_nos=" + ref_no);
                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
                    String inputLine;
                    String jsonFormattedResponse = "";

                    while ((inputLine = in.readLine()) != null) {
                        jsonFormattedResponse += inputLine;
                    }
                    in.close();

                    JsonParser jsonParser = new JsonParser();
                    JsonObject shipmentObj = jsonParser.parse(jsonFormattedResponse).getAsJsonObject().getAsJsonArray(CourierConstants.DELHIVERY_SHIPMENT_DATA).get(0).getAsJsonObject().getAsJsonObject(CourierConstants.DELHIVERY_SHIPMENT);

                    String status = shipmentObj.getAsJsonObject(CourierConstants.DELHIVERY_STATUS).get(CourierConstants.DELHIVERY_STATUS).getAsString();
                    String awb = shipmentObj.get(CourierConstants.DELHIVERY_AWB).getAsString();
                    String deliveryDate = shipmentObj.getAsJsonObject(CourierConstants.DELHIVERY_STATUS).get(CourierConstants.DELHIVERY_STATUS_DATETIME).getAsString();

                    Date delivery_date = getFormattedDeliveryDate(deliveryDate);

                    if (delivery_date != null && status.equalsIgnoreCase(CourierConstants.DELIVERED)) {
                        ordersDelivered = updateCourierDeliveryStatus(shippingOrderInList, shipment, trackingId, delivery_date);
                    } else {
                        logger.error("Delivery date not avaialable or status is not delivered for : " + shipment.getTrackingId());
                    }

                } catch (MalformedURLException mue) {
                    logger.error(CourierConstants.MALFORMED_URL_EXCEPTION + shippingOrder.getId());
                    mue.printStackTrace();
                    continue;
                } catch (IOException ioe) {
                    logger.error(CourierConstants.IO_EXCEPTION + shippingOrderInList);
                    ioe.printStackTrace();
                    continue;
                } catch (NullPointerException npe) {
                    logger.error(CourierConstants.NULL_POINTER_EXCEPTION + shippingOrderInList);
                    npe.printStackTrace();
                    continue;
                } catch (Exception e) {
                    logger.error(CourierConstants.EXCEPTION + shippingOrderInList);
                    e.printStackTrace();
                    continue;
                }
            }
        }
        return ordersDelivered;

    }

    public int updateDeliveryStatusBlueDart(Date startDate, Date endDate, User loggedOnUser) {
        List<ShippingOrder> shippingOrderList = adminShippingOrderService.getShippingOrderListByCouriers(startDate, endDate, EnumCourier.getBlueDartCouriers());
        ordersDelivered = 0;
        SimpleDateFormat sdf_date = new SimpleDateFormat("dd MMMMM yyyy");
        if (shippingOrderList != null || shippingOrderList.size() != 0) {

            for (ShippingOrder shippingOrderInList : shippingOrderList) {

                Shipment shipment = shippingOrderInList.getShipment();
                String trackingId = shipment.getTrackingId();
                BufferedReader in = null;

                try {
                    URL url = new URL("http://www.bluedart.com/servlet/RoutingServlet?handler=tnt&action=custawbquery&loginid=" + loginIdForBlueDart + "&awb=awb&numbers=" + trackingId + "&format=xml&lickey=" + licenceKeyForBlueDart + "&verno=1.3&scan=1");
                    in = new BufferedReader(new InputStreamReader(url.openStream()));
                    String inputLine;
                    String response = "";

                    while ((inputLine = in.readLine()) != null) {
                        if (inputLine != null) {
                            response += inputLine;
                        }
                    }
                    Document doc = new SAXBuilder().build(new StringReader(response));
                    XPath xPath = XPath.newInstance("/*/Shipment");
                    Element ele = (Element) xPath.selectSingleNode(doc);
                    String status = ele.getChildText(CourierConstants.BLUEDART_STATUS);
                    String statusDate = ele.getChildText(CourierConstants.BLUEDART_STATUS_DATE);
                    if (status.equals(CourierConstants.BLUEDART_SHIPMENT_DELIVERED) && statusDate != null) {
                        Date delivery_date = sdf_date.parse(statusDate);
                        ordersDelivered = updateCourierDeliveryStatus(shippingOrderInList, shipment, trackingId, delivery_date);
                    }
                } catch (MalformedURLException mue) {
                    logger.error(CourierConstants.MALFORMED_URL_EXCEPTION + shippingOrder.getId());
                    mue.printStackTrace();
                    continue;
                } catch (IOException ioe) {
                    logger.error(CourierConstants.IO_EXCEPTION + shippingOrder.getId());
                    ioe.printStackTrace();
                    continue;
                } catch (ParseException pe) {
                    logger.error(CourierConstants.PARSE_EXCEPTION + shippingOrder.getId());
                    pe.printStackTrace();
                    continue;
                } catch (NullPointerException npe) {
                    logger.error(CourierConstants.NULL_POINTER_EXCEPTION + shippingOrder.getId());
                    npe.printStackTrace();
                    continue;
                } catch (Exception e) {
                    logger.error(CourierConstants.EXCEPTION + shippingOrder.getId());
                    e.printStackTrace();
                    continue;
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        logger.error(CourierConstants.IO_EXCEPTION + shippingOrder.getId());
                        e.printStackTrace();
                        continue;
                    }
                }
            }
        }
        return ordersDelivered;
    }

    public int updateDeliveryStatusDTDC(Date startDate, Date endDate) {
        courierIdList.add(EnumCourier.DTDC_COD.getId());
        courierIdList.add(EnumCourier.DTDC_Lite.getId());
        courierIdList.add(EnumCourier.DTDC_Plus.getId());
        courierIdList.add(EnumCourier.DTDC_Surface.getId());

        //Fetching shipping order list based on courier ids
        List<ShippingOrder> shippingOrderList = adminShippingOrderService.getShippingOrderListByCouriers(startDate, endDate, courierIdList);
        ordersDelivered = 0;

        if (shippingOrderList != null || shippingOrderList.size() != 0) {
            for (ShippingOrder shippingOrderInList : shippingOrderList) {

                Shipment shipment = shippingOrderInList.getShipment();
                String trackingId = shipment.getTrackingId();
                BufferedReader bufferedReader = null;

                try {
                    //http://cust.dtdc.co.in/DPTrack/XMLCnTrk.asp?strcnno=B65303941&TrkType=cnno&addtnlDtl=Y&strCustType=DP&strCustDtl=BL8068
                    URL url = new URL("http://cust.dtdc.co.in/DPTrack/XMLCnTrk.asp?strcnno=" + trackingId + "&TrkType=" + trackingId + "&addtnlDtl=Y&strCustType=DP&strCustDtl=DP");
                    bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
                    String inputLine;
                    String response = "";
                    //added for debugging
                    response = "<DTDCREPLY>" +
                            "<CONSIGNMENT>" +
                            "<CNHEADER>" +
                            "<CNTRACK>TRUE</CNTRACK>" +
                            "<FIELD NAME=\"strShipmentNo\" VALUE=\"A15082271\" />" +
                            "<FIELD NAME=\"strRefNo\" VALUE=\"N/A\" />" +
                            "<FIELD NAME=\"strMode\" VALUE=\"AIR\" />\n" +
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
                    String courierDeliveryStatus = null;
                    String deliveryDateString = null;
                    if (trackStatus.equalsIgnoreCase("TRUE")) {
                        Map<String, String> map = new HashMap<String, String>();
                        for (int i = 0; i < fields.size(); i++) {
                            Element elementObj = (Element) fields.get(i);
                            if (elementObj != cnTrack) {
                                if (elementObj.getAttribute(CourierConstants.DTDC_ATTRIBUTE_NAME).getValue().equals(CourierConstants.DTDC_INPUT_STR_STATUS) ||
                                        elementObj.getAttribute(CourierConstants.DTDC_ATTRIBUTE_NAME).getValue().equals(CourierConstants.DTDC_INPUT_STR_STATUSTRANSON)) {
                                    map.put(elementObj.getAttribute(CourierConstants.DTDC_ATTRIBUTE_NAME).getValue(), elementObj.getAttribute(CourierConstants.DTDC_ATTRIBUTE_VALUE).getValue());
                                }
                            }
                        }

                        for (Map.Entry entryObj : map.entrySet()) {
                            if (entryObj.getKey().equals(CourierConstants.DTDC_INPUT_STR_STATUS)) {
                                courierDeliveryStatus = entryObj.getValue().toString();
                            }
                            if (entryObj.getKey().equals(CourierConstants.DTDC_INPUT_STR_STATUSTRANSON)) {
                                deliveryDateString = entryObj.getValue().toString();
                            }
                        }
                        String subStringDeliveryDate = deliveryDateString.substring(4, 8) + "-" + deliveryDateString.substring(2, 4) + "-" + deliveryDateString.substring(0, 2);
                        if (courierDeliveryStatus != null && deliveryDateString != null) {
                            if (courierDeliveryStatus.equals(CourierConstants.DTDC_INPUT_DELIVERED)) {
                                Date delivery_date = getFormattedDeliveryDate(subStringDeliveryDate);
                                ordersDelivered = updateCourierDeliveryStatus(shippingOrderInList, shipment, trackingId, delivery_date);
                            }
                        }
                    }

                } catch (MalformedURLException mue) {
                    logger.error(CourierConstants.MALFORMED_URL_EXCEPTION + shippingOrderInList.getId());
                    mue.printStackTrace();
                    continue;
                } catch (IOException ioe) {
                    logger.error(CourierConstants.IO_EXCEPTION + shippingOrderInList.getId());
                    ioe.printStackTrace();
                    continue;
                } catch (NullPointerException npe) {
                    logger.error(CourierConstants.NULL_POINTER_EXCEPTION + shippingOrderInList.getId());
                    npe.printStackTrace();
                    continue;
                } catch (Exception e) {
                    logger.error(CourierConstants.EXCEPTION + shippingOrderInList.getId());
                    e.printStackTrace();
                    continue;
                } finally {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        logger.error(CourierConstants.IO_EXCEPTION + shippingOrderInList.getId());
                        e.printStackTrace();
                        continue;
                    }
                }
            }
        }
        return ordersDelivered;
    }


public int updateDeliveryStatusForCourier(Date startDate, Date endDate,String courierName) {

       //Fetching shipping order list based on courier ids
        List<ShippingOrder> shippingOrderList = adminShippingOrderService.getShippingOrderListByCouriers(startDate, endDate, getCourierList(courierName));

        if (shippingOrderList != null || shippingOrderList.size() != 0) {
            for (ShippingOrder shippingOrderInList : shippingOrderList) {

                Shipment shipment = shippingOrderInList.getShipment();
                String trackingId = shipment.getTrackingId();
                BufferedReader bufferedReader = null;

                try {
                    //http://cust.dtdc.co.in/DPTrack/XMLCnTrk.asp?strcnno=B65303941&TrkType=cnno&addtnlDtl=Y&strCustType=DP&strCustDtl=BL8068
                    URL url = new URL("http://cust.dtdc.co.in/DPTrack/XMLCnTrk.asp?strcnno=" + trackingId + "&TrkType=" + trackingId + "&addtnlDtl=Y&strCustType=DP&strCustDtl=DP");
                    bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
                    String inputLine;
                    String response = "";
                    //added for debugging
                    response = "<DTDCREPLY>" +
                            "<CONSIGNMENT>" +
                            "<CNHEADER>" +
                            "<CNTRACK>TRUE</CNTRACK>" +
                            "<FIELD NAME=\"strShipmentNo\" VALUE=\"A15082271\" />" +
                            "<FIELD NAME=\"strRefNo\" VALUE=\"N/A\" />" +
                            "<FIELD NAME=\"strMode\" VALUE=\"AIR\" />\n" +
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
                    String courierDeliveryStatus = null;
                    String deliveryDateString = null;
                    if (trackStatus.equalsIgnoreCase("TRUE")) {
                        Map<String, String> map = new HashMap<String, String>();
                        for (int i = 0; i < fields.size(); i++) {
                            Element elementObj = (Element) fields.get(i);
                            if (elementObj != cnTrack) {
                                if (elementObj.getAttribute(CourierConstants.DTDC_ATTRIBUTE_NAME).getValue().equals(CourierConstants.DTDC_INPUT_STR_STATUS) ||
                                        elementObj.getAttribute(CourierConstants.DTDC_ATTRIBUTE_NAME).getValue().equals(CourierConstants.DTDC_INPUT_STR_STATUSTRANSON)) {
                                    map.put(elementObj.getAttribute(CourierConstants.DTDC_ATTRIBUTE_NAME).getValue(), elementObj.getAttribute(CourierConstants.DTDC_ATTRIBUTE_VALUE).getValue());
                                }
                            }
                        }

                        for (Map.Entry entryObj : map.entrySet()) {
                            if (entryObj.getKey().equals(CourierConstants.DTDC_INPUT_STR_STATUS)) {
                                courierDeliveryStatus = entryObj.getValue().toString();
                            }
                            if (entryObj.getKey().equals(CourierConstants.DTDC_INPUT_STR_STATUSTRANSON)) {
                                deliveryDateString = entryObj.getValue().toString();
                            }
                        }
                        String subStringDeliveryDate = deliveryDateString.substring(4, 8) + "-" + deliveryDateString.substring(2, 4) + "-" + deliveryDateString.substring(0, 2);
                        if (courierDeliveryStatus != null && deliveryDateString != null) {
                            if (courierDeliveryStatus.equals(CourierConstants.DTDC_INPUT_DELIVERED)) {
                                Date delivery_date = getFormattedDeliveryDate(subStringDeliveryDate);
                                ordersDelivered = updateCourierDeliveryStatus(shippingOrderInList, shipment, trackingId, delivery_date);
                            }
                        }
                    }

                } catch (MalformedURLException mue) {
                    logger.error(CourierConstants.MALFORMED_URL_EXCEPTION + shippingOrderInList.getId());
                    mue.printStackTrace();
                    continue;
                } catch (IOException ioe) {
                    logger.error(CourierConstants.IO_EXCEPTION + shippingOrderInList.getId());
                    ioe.printStackTrace();
                    continue;
                } catch (NullPointerException npe) {
                    logger.error(CourierConstants.NULL_POINTER_EXCEPTION + shippingOrderInList.getId());
                    npe.printStackTrace();
                    continue;
                } catch (Exception e) {
                    logger.error(CourierConstants.EXCEPTION + shippingOrderInList.getId());
                    e.printStackTrace();
                    continue;
                } finally {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        logger.error(CourierConstants.IO_EXCEPTION + shippingOrderInList.getId());
                        e.printStackTrace();
                        continue;
                    }
                }
            }
        }
        return ordersDelivered;
    }


    public Date getFormattedDeliveryDate(String deliveryDate) {
        Date formattedDate = null;
        if (deliveryDate != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                formattedDate = sdf.parse(deliveryDate);
            } catch (Exception e) {
                logger.error("Exception occurred in parsing DeliveryDate format,Date was :" + deliveryDate);
            }
        }

        return formattedDate;
    }

    @SuppressWarnings("unchecked")
    private Map<Integer, String> getRowMap(Iterator<Row> objRowIt) {
        // Header are read and related columns are taken care of
        // accordignly.

        Map<Integer, String> headerMap = new HashMap<Integer, String>();

        HSSFRow headers = (HSSFRow) objRowIt.next();
        Iterator objCellIterator = headers.cellIterator();
        while (objCellIterator.hasNext()) {
            HSSFCell headerCell = (HSSFCell) objCellIterator.next();
            int headerColIndex = 0;
            headerColIndex = headerCell.getColumnIndex();
            Object cellValue = null;
            try {
                cellValue = headerCell.getStringCellValue();
                headerMap.put(headerColIndex, cellValue.toString());
            } catch (Exception e) {
                logger.debug("error trying to read column " + headerColIndex + " as String on Row " + headers.getRowNum() + " : Cell toString = " + headerCell.toString());
                logger.debug("Now trying to read as numeric");
                try {
                    cellValue = headerCell.getNumericCellValue();
                    headerMap.put(headerColIndex, cellValue.toString());
                } catch (Exception e1) {
                    logger.debug("error reading cell value as numeric - " + headerCell.toString());
                }
            }
        }

        return headerMap;
    }

    private List<Long>  getCourierList(String courierName){
        List<Long> courierIdList =new ArrayList<Long>();
        if(courierName.equalsIgnoreCase("DTDC")){
            courierIdList=EnumCourier.getDTDCCouriers();
        } else if(courierName.equalsIgnoreCase("BlueDart")){
            courierIdList=EnumCourier.getBlueDartCouriers();
        } else if(courierName.equalsIgnoreCase("AFL")){
            courierIdList.add(EnumCourier.AFLWiz.getId());
        } else if(courierName.equalsIgnoreCase("Delhivery")){
            courierIdList.add(EnumCourier.Delhivery.getId());
        }
        return courierIdList;
    }

    private Map<Integer, String> getRowMapStringFormat(Iterator<Row> objRowIt) {
        // Header are read and related columns are taken care of
        // accordignly.

        Map<Integer, String> headerMap = new HashMap<Integer, String>();

        HSSFRow headers = (HSSFRow) objRowIt.next();
        Iterator objCellIterator = headers.cellIterator();
        while (objCellIterator.hasNext()) {
            HSSFCell headerCell = (HSSFCell) objCellIterator.next();
            int headerColIndex = 0;
            headerColIndex = headerCell.getColumnIndex();
            Object cellValue = null;
            try {
                if (HSSFDateUtil.isCellDateFormatted(headerCell)) {
                    Date dateExcel = null;
                    dateExcel = headerCell.getDateCellValue();
                    Calendar cal = Calendar.getInstance();
                    cal.setTime(dateExcel);
                    int dd = cal.get(Calendar.DATE);
                    int mm = cal.get(Calendar.MONTH);
                    cal.set(Calendar.DATE, mm + 1);
                    cal.set(Calendar.MONTH, dd - 1);
                    dateExcel = cal.getTime();
                    String dateString = cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
                    headerMap.put(headerColIndex, dateString);
                    continue;
                }
            } catch (Exception e) {
                logger.debug("error trying to read column " + headerColIndex + " as Date on Row " + headers.getRowNum() + " : Cell toString = " + headerCell.toString());
                logger.debug("Now trying to read as String");
            }
            try {
                headerCell.setCellType(Cell.CELL_TYPE_STRING);
                cellValue = headerCell.getStringCellValue();
                headerMap.put(headerColIndex, cellValue.toString());
            } catch (Exception e) {
                logger.debug("error trying to read column " + headerColIndex + " as String on Row " + headers.getRowNum() + " : Cell toString = " + headerCell.toString());
                logger.debug("Now trying to read as numeric");
                try {
                    cellValue = headerCell.getNumericCellValue();
                    headerMap.put(headerColIndex, cellValue.toString());
                } catch (Exception e1) {
                    logger.debug("error reading cell value as numeric - " + headerCell.toString());
                }
            }
        }

        return headerMap;
    }

    private String getCellValue(String header, Map<Integer, String> rowMap, Map<Integer, String> headerMap) {
        Integer columnIndex = getColumnIndex(header, headerMap);
        if (columnIndex == null)
            return null;
        String cellVal = rowMap.get(columnIndex);
        return cellVal == null ? "" : cellVal.trim();
    }

    private Integer getColumnIndex(String header, Map<Integer, String> headerMap) {
        Integer columnIndex = null;
        for (Integer key : headerMap.keySet()) {
            if (headerMap.get(key).equals(header))
                columnIndex = key;
        }
        return columnIndex;
    }

    public AdminShippingOrderService getAdminShippingOrderService() {
        return adminShippingOrderService;
    }

    public void setAdminShippingOrderService(AdminShippingOrderService adminShippingOrderService) {
        this.adminShippingOrderService = adminShippingOrderService;
    }

    public ShippingOrderService getShippingOrderService() {
        return shippingOrderService;
    }

    public void setShippingOrderService(ShippingOrderService shippingOrderService) {
        this.shippingOrderService = shippingOrderService;
    }

}
