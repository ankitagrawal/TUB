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

    private static Logger       logger                        = LoggerFactory.getLogger(DeliveryStatusUpdateManager.class);

    List                        sheetData                     = new ArrayList();
    ShippingOrder               shippingOrder;
    String                      addedItems;
    int                         itemsDeliveredCount;
    int                         allItemsDeliveredCount;
    int                         ordersDelivered               = 0;
    int                         orderDeliveryCount            = 0;
    String                      courierName                   = "Delivered by ";
    String                      prefixComments                = "Delivered Items :<br/>";
    public static final int     digitsInGatewayId             = 5;
    // private static int acceptableDeliveryPeriod = 50;

    private static final String authenticationIdForDelhivery  = "9aaa943a0c74e29b340074d859b2690e07c7fb25";
    List<Long>                  courierIdList                 = new ArrayList<Long>();
    private static final String loginIdForBlueDart            = "GGN37392";
    private static final String licenceKeyForBlueDart         = "3c6867277b7a2c8cd78c8c4cb320f401";


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

    public String updateDeliveryStatusDTDC(ShippingOrder shippingOrder, String awb, String deliveryDate, String messagePostUpdation) throws Exception {

        /*
         * logger.info("parsing delivery status excel : " + objInFile.getAbsolutePath()); POIFSFileSystem objInFileSys =
         * new POIFSFileSystem(new FileInputStream(objInFile)); HSSFWorkbook workbook = new HSSFWorkbook(objInFileSys); //
         * Assuming there is only one sheet, the first one only will be picked HSSFSheet reportSheet =
         * workbook.getSheetAt(0); Iterator rows = reportSheet.rowIterator(); while (rows.hasNext()) { HSSFRow row =
         * (HSSFRow) rows.next(); Iterator cells = row.cellIterator(); List data = new ArrayList(); while
         * (cells.hasNext()) { HSSFCell cell = (HSSFCell) cells.next(); data.add(cell); } sheetData.add(data); } return
         * updateOrderStatusDTDC(sheetData, loggedOnUser);
         */

        return null;
    }

    private String updateOrderStatusDTDC(List sheetData, User loggedOnUser) throws Exception {
        // TODO: #warehouse fix this

        /*
         * Date deliveryDateInXls = null; String remarks = null; Calendar cal = Calendar.getInstance(); Date dateToday =
         * new Date(); Date shipDate = null; long daysTakenInDelivery = 0; String errorMessage = ""; String
         * exceptionMessage = ""; String messageFormat=""; String tracking_Id=""; int formatExceptionFlag = 0;
         * SimpleDateFormat sdf_date = new SimpleDateFormat("dd/MM/yyyy"); String orderStatusInXls = null; int
         * ordersDelivered = 0; order = null; Long orderId; String courier_trackingId = null; String order_gateway_id =
         * ""; for (int i = 0; i < sheetData.size(); i++) { order = null; remarks = null; courier_trackingId = null;
         * allItemsDeliveredCount = 0; itemsDeliveredCount = 0; addedItems = ""; orderId = null; orderStatusInXls =
         * null; deliveryDateInXls = null; List list = (List) sheetData.get(i); for (int j = 0; j < list.size(); j++) {
         * HSSFCell cell = (HSSFCell) list.get(j); // excluding the top row //extract the tracking id/courier number //
         * courier id or tracking id can either be a numeric or a string value if (i != 0 && j == 1 && cell != null) {
         * if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) { courier_trackingId =
         * cell.getRichStringCellValue().getString(); } else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) {
         * courier_trackingId = Long.toString((long) cell.getNumericCellValue()); } else { break; } } // extract order
         * by gateway_order_id // ideally gateway_order_id should be a string containing a - in between // in COD cases
         * we get an excel which does not have a - in place hence we have to insert it manually if (i != 0 && j == 2 &&
         * cell != null) { if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING && null !=
         * cell.getRichStringCellValue().getString()) { order_gateway_id = cell.getRichStringCellValue().getString(); }
         * else if (cell.getCellType() == HSSFCell.CELL_TYPE_NUMERIC) { order_gateway_id = Long.toString((long)
         * cell.getNumericCellValue()); //insert a - in between in case the reference number does not have one
         * order_gateway_id = format_gateway_order_id(order_gateway_id, digitsInGatewayId); } order =
         * orderDaoProvider.get().findByGatewayOrderId(order_gateway_id); if (order == null ||
         * order.getOrderStatus().equals(orderDao.find(EnumOrderStatus.Delivered.getId()))) { break; } } // status //
         * check which items are shipped and update their status as delivered if (i != 0 && j == 9 && null !=
         * cell.getRichStringCellValue().getString() &&
         * cell.getRichStringCellValue().getString().trim().equalsIgnoreCase("Delivered")) { orderStatusInXls =
         * "Delivered"; } // delivery deliveryDateInXls if (i != 0 && j == 10 && orderStatusInXls != null &&
         * orderStatusInXls.equals("Delivered") && cell != null) { if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) {
         * deliveryDateInXls = sdf_date.parse(cell.getRichStringCellValue().getString()); } else if (cell.getCellType() ==
         * HSSFCell.CELL_TYPE_NUMERIC) { // microsoft excel here starts reading date as it fits MM/dd/yyyy criteria and
         * gives out a numeric value // convert a date from MM/dd/yyyy to dd/MM/yyyy deliveryDateInXls =
         * cell.getDateCellValue(); cal.setTime(deliveryDateInXls); int dd = cal.get(Calendar.DATE); int mm =
         * cal.get(Calendar.MONTH); cal.set(Calendar.DATE, mm + 1); cal.set(Calendar.MONTH, dd - 1); deliveryDateInXls =
         * cal.getTime(); } else { deliveryDateInXls = new Date(); } cal.setTime(deliveryDateInXls); //Truncate time
         * cal.set(Calendar.HOUR_OF_DAY, 0); cal.set(Calendar.MINUTE, 0); cal.set(Calendar.SECOND, 0);
         * cal.set(Calendar.MILLISECOND, 0); deliveryDateInXls = cal.getTime(); } // check which items are shipped and
         * update their status as delivered if (i != 0 && j == 11 && order != null && orderStatusInXls != null &&
         * deliveryDateInXls != null) { if (cell.getCellType() == HSSFCell.CELL_TYPE_STRING) { remarks = "Remarks :
         * <br/> " + cell.getRichStringCellValue().getString(); //cell.getDateCellValue() } for (LineItem lineItem :
         * order.getProductLineItems()) { if (lineItem != null && Boolean.TRUE.equals(lineItem.isShippedEmailSent()) &&
         * lineItem.getTrackingId() != null && lineItem.getTrackingId().equals(courier_trackingId) &&
         * lineItem.getLineItemStatus().equals(lineItemStatusDao.find(EnumLineItemStatus.SHIPPED.getId()))) { shipDate =
         * lineItem.getShipDate(); //Calculating no.of days taken in delivery daysTakenInDelivery =
         * (deliveryDateInXls.getTime() - shipDate.getTime()) / (60 * 60 * 1000 * 24); if (shipDate!=null &&
         * (deliveryDateInXls.after(dateToday) || daysTakenInDelivery > acceptableDeliveryPeriod )) { errorMessage =
         * errorMessage.concat((i + 1) + ";" + courier_trackingId + ";"); formatExceptionFlag = 1; break; } //When ship
         * date is more than delivery date -> this happens when ship date in our system sometimes is delayed but courier
         * pics the shipment if( deliveryDateInXls.before(shipDate)){ Calendar
         * deliveryDateAsShipDatePlusOne=Calendar.getInstance(); deliveryDateAsShipDatePlusOne.setTime(shipDate);
         * deliveryDateAsShipDatePlusOne.add(Calendar.DAY_OF_MONTH,1);
         * deliveryDateAsShipDatePlusOne.set(Calendar.HOUR_OF_DAY,0);
         * deliveryDateAsShipDatePlusOne.set(Calendar.MINUTE,0); deliveryDateAsShipDatePlusOne.set(Calendar.SECOND,0);
         * deliveryDateAsShipDatePlusOne.set(Calendar.MILLISECOND,0);
         * deliveryDateInXls=deliveryDateAsShipDatePlusOne.getTime(); }
         * lineItem.setLineItemStatus(lineItemStatusDao.find(EnumLineItemStatus.DELIVERED.getId()));
         * lineItem.setDeliveryDate(deliveryDateInXls); lineItemDaoProvider.get().save(lineItem); addedItems +=
         * lineItem.getProductVariant().getProduct().getName() + "<br/>"; itemsDeliveredCount++; } if (lineItem != null &&
         * lineItem.getLineItemStatus().equals(lineItemStatusDao.find(EnumLineItemStatus.DELIVERED.getId()))) {
         * allItemsDeliveredCount++; } } addedItems+=" by DTDC"; //add activity only when atleast a single product is
         * delivered if (itemsDeliveredCount > 0) { orderManagerProvider.get().logOrderActivity(order, loggedOnUser,
         * orderLifecycleActivityDaoProvider.get().find(EnumOrderLifecycleActivity.OrderDelivered.getId()),
         * prefixComments + addedItems + remarks, deliveryDateInXls); orderManagerProvider.get().logOrderActivity(order,
         * loggedOnUser,
         * orderLifecycleActivityDaoProvider.get().find(EnumOrderLifecycleActivity.OrderDeliveredUpdated.getId()),
         * "DeliveryStatusUpdated"); ordersDelivered++; } //mark order as delivered only when all the items are
         * delivered if (order.getProductLineItems().size() == allItemsDeliveredCount &&
         * order.getOrderStatus().equals(orderStatusDao.find(EnumOrderStatus.Shipped.getId()))) {
         * order.setOrderStatus(orderStatusDao.find(EnumOrderStatus.Delivered.getId()));
         * orderDaoProvider.get().save(order); } } } // for j loop close } // for i loop close if (formatExceptionFlag ==
         * 1) { messageFormat="<br/><br/>The delivery date in the following order is not in the correct
         * format(dd/MM/yyyy).Please verify <br/><br/>" ; StringTokenizer st = new StringTokenizer(errorMessage, ";");
         * while (st.hasMoreTokens()) { String row_number = st.nextToken(); String order_id = st.nextToken();
         * exceptionMessage = exceptionMessage + ("Row No.=" + row_number + " Tracking_Id=" + order_id + "<br/><br/>"); } }
         * else { messageFormat=""; } return ("<br/>No.of rows updated=" + ordersDelivered + messageFormat +
         * exceptionMessage);
         */

        return null;
    }

    public int updateDeliveryStatusAFL(Date startDate, Date endDate, User loggedOnUser) {
        courierIdList.add( EnumCourier.AFLWiz.getId());
        List<Long> shippingOrderList = getAdminShippingOrderService().getShippingOrderListByCouriers(startDate, endDate,courierIdList);
        courierName = " AFLwiz";

        ordersDelivered = 0;
        orderDeliveryCount = 0;

        SimpleDateFormat sdf_date = new SimpleDateFormat("yyyy-MM-dd");

        Map<String, String> responseAFL;
        if (shippingOrderList != null || shippingOrderList.size() != 0) {

            for (Long shippingOrderId : shippingOrderList) {

                ShippingOrder shippingOrderInList = getShippingOrderService().find(shippingOrderId);
                Shipment shipment = shippingOrderInList.getShipment();
                String trackingId = shipment.getTrackingId();
                addedItems = "";
                itemsDeliveredCount = 0;
                allItemsDeliveredCount = 0;
                BufferedReader in = null;

                try {
                    URL url = new URL("http://trackntrace.aflwiz.com/aflwiztrack?shpntnum=" + trackingId);
                    in = new BufferedReader(new InputStreamReader(url.openStream()));
                    String inputLine;
                    String response = "";

                    while ((inputLine = in.readLine()) != null) {
                        if (inputLine != null) {
                            response += inputLine;
                        }
                    }
                    responseAFL = AFLResponseParser.parseResponse(response, trackingId);
                    if (responseAFL.get(CourierConstants.AFL_DELIVERY_DATE) != null && responseAFL.get(CourierConstants.AFL_ORDER_GATEWAY_ID) != null && responseAFL.get(CourierConstants.AFL_AWB) != null
                            && responseAFL.get(CourierConstants.AFL_CURRENT_STATUS) != null) {

                        Date delivery_date = sdf_date.parse(responseAFL.get("delivery_date"));
                        ordersDelivered = updateCourierDeliveryStatus(shippingOrderInList, shipment, trackingId, delivery_date);
                    }
                } catch (MalformedURLException mue) {
                    logger.error(CourierConstants.MALFORMED_URL_EXCEPTION + shippingOrderId);
                    mue.printStackTrace();
                    continue;
                } catch (IOException ioe) {
                    logger.error(CourierConstants.IO_EXCEPTION + shippingOrderId);
                    ioe.printStackTrace();
                    continue;
                } catch (ParseException pe) {
                    logger.error(CourierConstants.PARSE_EXCEPTION + shippingOrderId);
                    pe.printStackTrace();
                    continue;
                } catch (NullPointerException npe) {
                    logger.error(CourierConstants.NULL_POINTER_EXCEPTION + shippingOrderId);
                    npe.printStackTrace();
                    continue;
                } catch (Exception e) {
                    logger.error(CourierConstants.EXCEPTION + shippingOrderId);
                    e.printStackTrace();
                    continue;
                } finally {
                    try {
                        in.close();
                    } catch (IOException e) {
                        logger.error(CourierConstants.IO_EXCEPTION + shippingOrderId);
                        e.printStackTrace();
                        continue;
                    }
                }
            }
        }
        return ordersDelivered;

    }

    public int updateDeliveryStatusChhotu(Date startDate, Date endDate, User loggedOnUser) {
        courierIdList.add( EnumCourier.Chhotu.getId());
        List<Long> shippingOrderList = getAdminShippingOrderService().getShippingOrderListByCouriers(startDate, endDate,courierIdList);
        courierName += " Chhotu";

        ordersDelivered = 0;
        orderDeliveryCount = 0;

        if (shippingOrderList != null || shippingOrderList.size() != 0) {
            for (Long shippingOrderId : shippingOrderList) {

                ShippingOrder shippingOrderInList = getShippingOrderService().find(shippingOrderId);
                Shipment shipment = shippingOrderInList.getShipment();
                String trackingId = shipment.getTrackingId();
                addedItems = "";
                itemsDeliveredCount = 0;
                allItemsDeliveredCount = 0;

                try {

                    URL url = new URL("http://api.chhotu.in/shipmenttracking?tracking_number=" + trackingId);
                    BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
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
                    Date delivery_date = chhotuCourierDelivery.getFormattedDeliveryDate();

                    if (delivery_date != null && chhotuCourierDelivery.getShipmentStatus().equalsIgnoreCase(CourierConstants.DELIVERED) && chhotuCourierDelivery.getTrackingId() != null) {
                        ordersDelivered = updateCourierDeliveryStatus(shippingOrderInList, shipment, trackingId, delivery_date);
                    } else {
                        logger.error("Delivery date not available or status is not delieved for : " + shipment.getTrackingId());
                    }

                } catch (MalformedURLException mue) {
                    logger.error(CourierConstants.MALFORMED_URL_EXCEPTION+ shippingOrderId);
                    mue.printStackTrace();
                    continue;
                } catch (IOException ioe) {
                    logger.error(CourierConstants.IO_EXCEPTION+ shippingOrderId);
                    ioe.printStackTrace();
                    continue;
                } catch (NullPointerException npe) {
                    logger.error(CourierConstants.NULL_POINTER_EXCEPTION + shippingOrderId);
                    npe.printStackTrace();
                    continue;
                } catch (Exception e) {
                    logger.error(CourierConstants.EXCEPTION + shippingOrderId);
                    e.printStackTrace();
                    continue;
                }
            }
        }
        return ordersDelivered;

    }

    public int updateCourierDeliveryStatus(ShippingOrder shippingOrder, Shipment shipment, String trackingId, Date deliveryDate) {

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

        /*
         * for (LineItem lineItem : order.getProductLineItems()) { if (lineItem != null &&
         * Boolean.TRUE.equals(lineItem.isShippedEmailSent()) && lineItem.getTrackingId() != null &&
         * lineItem.getLineItemStatus().equals(lineItemStatusDao.find(EnumLineItemStatus.SHIPPED.getId())) &&
         * lineItem.getTrackingId().equals(trackingId)) {
         * lineItem.setLineItemStatus(lineItemStatusDao.find(EnumLineItemStatus.DELIVERED.getId()));
         * lineItem.setDeliveryDate(delivery_date); lineItemDaoProvider.get().save(lineItem); addedItems +=
         * lineItem.getProductVariant().getProduct().getName() + "<br/>"; itemsDeliveredCount++; } //counting delivered
         * items separately as there may be a case of partially escalated orders if (lineItem != null &&
         * lineItem.getLineItemStatus().equals(lineItemStatusDao.find(EnumLineItemStatus.DELIVERED.getId()))) {
         * allItemsDeliveredCount++; } } //add activity only when atleast a single product is delivered
         * addedItems+=courierName; if (itemsDeliveredCount > 0) { orderService.logOrderActivity(order, loggedOnUser,
         * orderLifecycleActivityDaoProvider.get().find(EnumOrderLifecycleActivity.OrderDelivered.getId()),
         * prefixComments + addedItems, delivery_date); orderService.logOrderActivity(order, loggedOnUser,
         * orderLifecycleActivityDaoProvider.get().find(EnumOrderLifecycleActivity.OrderDeliveredUpdated.getId()),
         * "DeliveryStatusUpdated"); orderDeliveryCount++; } //mark order as delivered only when all the items are
         * delivered if (order.getProductLineItems().size() == allItemsDeliveredCount &&
         * order.getOrderStatus().equals(orderStatusDao.find(EnumOrderStatus.Shipped.getId()))) {
         * order.setOrderStatus(orderStatusDao.find(EnumOrderStatus.Delivered.getId()));
         * orderDaoProvider.get().save(order); }
         */

        return orderDeliveryCount;
    }

    public String format_gateway_order_id(String order_gateway_id, int digitsInGatewayId) {
        if (order_gateway_id.length() > digitsInGatewayId - 1) {
            order_gateway_id = (order_gateway_id.substring(0, order_gateway_id.length() - digitsInGatewayId) + "-" + order_gateway_id.substring(order_gateway_id.length()
                    - digitsInGatewayId, order_gateway_id.length()));
        }
        return order_gateway_id;
    }

    public int updateDeliveryStatusDelhivery(Date startDate, Date endDate, User loggedOnUser) {
        courierIdList.add( EnumCourier.Delhivery.getId());
        List<Long> shippingOrderList = adminShippingOrderService.getShippingOrderListByCouriers(startDate, endDate,courierIdList);
        // courierName += " Delhivery";

        ordersDelivered = 0;
        orderDeliveryCount = 0;

        if (shippingOrderList != null || shippingOrderList.size() != 0) {
            for (Long shippingOrderId : shippingOrderList) {

                ShippingOrder shippingOrderInList = getShippingOrderService().find(shippingOrderId);
                Shipment shipment = shippingOrderInList.getShipment();
                String trackingId = shipment.getTrackingId();
                String ref_no = shippingOrderInList.getGatewayOrderId();
                addedItems = "";
                itemsDeliveredCount = 0;
                allItemsDeliveredCount = 0;
                try {

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
                    logger.error(CourierConstants.MALFORMED_URL_EXCEPTION+ shippingOrderId);
                    mue.printStackTrace();
                    continue;
                } catch (IOException ioe) {
                    logger.error(CourierConstants.IO_EXCEPTION + shippingOrderId);
                    ioe.printStackTrace();
                    continue;
                } catch (NullPointerException npe) {
                    logger.error(CourierConstants.NULL_POINTER_EXCEPTION + shippingOrderId);
                    npe.printStackTrace();
                    continue;
                } catch (Exception e) {
                    logger.error(CourierConstants.EXCEPTION + shippingOrderId);
                    e.printStackTrace();
                    continue;
                }
            }
        }
        return ordersDelivered;

    }

  public int updateDeliveryStatusBlueDart(Date startDate, Date endDate, User loggedOnUser) {
    courierIdList.add( EnumCourier.BlueDart.getId());
    courierIdList.add( EnumCourier.BlueDart_COD.getId());
    List<Long> shippingOrderList = adminShippingOrderService.getShippingOrderListByCouriers(startDate, endDate,courierIdList);
    courierName = " BlueDart";
    ordersDelivered = 0;
    orderDeliveryCount = 0;
    SimpleDateFormat sdf_date = new SimpleDateFormat("dd MMMMM yyyy");
    if (shippingOrderList != null || shippingOrderList.size() != 0) {

      for (Long shippingOrderId : shippingOrderList) {

        ShippingOrder shippingOrderInList = getShippingOrderService().find(shippingOrderId);
        Shipment shipment = shippingOrderInList.getShipment();
        String trackingId = shipment.getTrackingId();
        addedItems = "";
        itemsDeliveredCount = 0;
        allItemsDeliveredCount = 0;
        BufferedReader in = null;

        try {
          URL url = new URL("http://www.bluedart.com/servlet/RoutingServlet?handler=tnt&action=custawbquery&loginid="+loginIdForBlueDart+"&awb=awb&numbers=" + trackingId + "&format=xml&lickey="+licenceKeyForBlueDart+"&verno=1.3&scan=1");
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
          logger.error(CourierConstants.MALFORMED_URL_EXCEPTION+ shippingOrderId);
          mue.printStackTrace();
          continue;
        } catch (IOException ioe) {
          logger.error(CourierConstants.IO_EXCEPTION + shippingOrderId);
          ioe.printStackTrace();
          continue;
        } catch (ParseException pe) {
          logger.error(CourierConstants.PARSE_EXCEPTION + shippingOrderId);
          pe.printStackTrace();
          continue;
        } catch (NullPointerException npe) {
          logger.error(CourierConstants.NULL_POINTER_EXCEPTION + shippingOrderId);
          npe.printStackTrace();
          continue;
        } catch (Exception e) {
          logger.error(CourierConstants.EXCEPTION + shippingOrderId);
          e.printStackTrace();
          continue;
        } finally {
          try {
            in.close();
          } catch (IOException e) {
            logger.error(CourierConstants.IO_EXCEPTION + shippingOrderId);
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
        List<Long> shippingOrderList = adminShippingOrderService.getShippingOrderListByCouriers(startDate, endDate, courierIdList);
        ordersDelivered = 0;
        orderDeliveryCount = 0;
        if (shippingOrderList != null || shippingOrderList.size() != 0) {
            for (Long shippingOrderId : shippingOrderList) {

                ShippingOrder shippingOrderInList = getShippingOrderService().find(shippingOrderId);
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
            response="<DTDCREPLY>" +
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
                    if(trackStatus.equalsIgnoreCase("TRUE")){
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
                    logger.error(CourierConstants.MALFORMED_URL_EXCEPTION + shippingOrderId);
                    mue.printStackTrace();
                    continue;
                } catch (IOException ioe) {
                    logger.error(CourierConstants.IO_EXCEPTION + shippingOrderId);
                    ioe.printStackTrace();
                    continue;
                } catch (NullPointerException npe) {
                    logger.error(CourierConstants.NULL_POINTER_EXCEPTION + shippingOrderId);
                    npe.printStackTrace();
                    continue;
                } catch (Exception e) {
                    logger.error(CourierConstants.EXCEPTION + shippingOrderId);
                    e.printStackTrace();
                    continue;
                } finally {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        logger.error(CourierConstants.IO_EXCEPTION + shippingOrderId);
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
