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

import com.google.gson.JsonObject;
import com.hk.admin.pact.service.shippingOrder.AdminShippingOrderService;
import com.hk.admin.util.ChhotuCourierDelivery;
import com.hk.admin.util.CourierStatusUpdateHelper;
import com.hk.constants.report.ReportConstants;
import com.hk.constants.courier.EnumCourier;
import com.hk.constants.courier.CourierConstants;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.courier.Shipment;
import com.hk.domain.order.ShippingOrder;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import org.jdom.Element;

@SuppressWarnings("unchecked")
@Component
public class DeliveryStatusUpdateManager {

    private static Logger       logger                        = LoggerFactory.getLogger(DeliveryStatusUpdateManager.class);

    List                        sheetData                     = new ArrayList();
    ShippingOrder               shippingOrder;
    String                      addedItems;
    int                         itemsDeliveredCount;
    int                         allItemsDeliveredCount;
    int                         orderDeliveryCount            = 0;
    int                         ordersDelivered               = 0;
    String                      trackingId                    = "";
    String                      courierName                   = "Delivered by ";
    String                      prefixComments                = "Delivered Items :<br/>";
    public static final int     digitsInGatewayId             = 5;
    List<ShippingOrder>         shippingOrderList             = new ArrayList<ShippingOrder>();
    List<Long>                  courierIdList                 = null;

    LineItemDao                 lineItemDaoProvider;

    @Autowired
    AdminShippingOrderService   adminShippingOrderService;

    @Autowired
    ShippingOrderService        shippingOrderService;

    @Autowired
    CourierStatusUpdateHelper   courierStatusUpdateHelper;

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

    public int updateCourierStatus(Date startDate, Date endDate, String courierName) {
        ordersDelivered = 0;
        orderDeliveryCount = 0;

        if (courierName.equalsIgnoreCase(CourierConstants.AFL)) {
            courierIdList=new ArrayList<Long>();
            courierIdList.add(EnumCourier.AFLWiz.getId());
            shippingOrderList = getAdminShippingOrderService().getShippingOrderListByCouriers(startDate, endDate, courierIdList);
            if (shippingOrderList != null && shippingOrderList.size() > 0) {
                for (ShippingOrder shippingOrderInList : shippingOrderList) {
                    trackingId = shippingOrderInList.getShipment().getTrackingId();
                    Date deliveryDate = courierStatusUpdateHelper.updateDeliveryStatusAFL(trackingId);
                    if (deliveryDate != null) {
                        ordersDelivered = updateCourierDeliveryStatus(shippingOrderInList, shippingOrderInList.getShipment(),
                                shippingOrderInList.getShipment().getTrackingId(), deliveryDate);
                    } else {
                        logger.debug("Delivery date not available or status is not delivered for : " + shippingOrderInList.getShipment().getTrackingId());

                    }

                }
            } }else if (courierName.equalsIgnoreCase(CourierConstants.CHHOTU)) {
                courierIdList=new ArrayList<Long>();
                courierIdList.add(EnumCourier.Chhotu.getId());
                shippingOrderList = getAdminShippingOrderService().getShippingOrderListByCouriers(startDate, endDate, courierIdList);
                if (shippingOrderList != null && shippingOrderList.size() > 0) {
                    for (ShippingOrder shippingOrderInList : shippingOrderList) {
                        trackingId = shippingOrderInList.getShipment().getTrackingId();
                        ChhotuCourierDelivery chhotuCourierDelivery = courierStatusUpdateHelper.updateDeliveryStatusChhotu(trackingId);
                        Date delivery_date=null;
                        if (chhotuCourierDelivery != null) {
                            delivery_date = chhotuCourierDelivery.getFormattedDeliveryDate();
                        }

                        if (delivery_date != null && chhotuCourierDelivery.getShipmentStatus().equalsIgnoreCase(CourierConstants.DELIVERED) && chhotuCourierDelivery.getTrackingId() != null) {
                            ordersDelivered = updateCourierDeliveryStatus(shippingOrderInList, shippingOrderInList.getShipment(), shippingOrderInList.getShipment().getTrackingId(), delivery_date);
                        } else {
                            logger.debug("Delivery date not available or status is not delivered for : " + shippingOrderInList.getShipment().getTrackingId());
                        }
                    }
                }
            } else if (courierName.equalsIgnoreCase(CourierConstants.DELHIVERY)) {
                courierIdList=new ArrayList<Long>();
                courierIdList=EnumCourier.getDelhiveryCourierIds();
                shippingOrderList = getAdminShippingOrderService().getShippingOrderListByCouriers(startDate, endDate, courierIdList);
                JsonObject shipmentJsonObj = null;
                if (shippingOrderList != null && shippingOrderList.size() > 0) {
                    for (ShippingOrder shippingOrderInList : shippingOrderList) {

                        trackingId = shippingOrderInList.getShipment().getTrackingId();
                        shipmentJsonObj = courierStatusUpdateHelper.updateDeliveryStatusDelhivery(trackingId);
                        String status = shipmentJsonObj.getAsJsonObject(CourierConstants.DELHIVERY_STATUS).get(CourierConstants.DELHIVERY_STATUS).getAsString();
                        String awb = shipmentJsonObj.get(CourierConstants.DELHIVERY_AWB).getAsString();
                        String deliveryDate = shipmentJsonObj.getAsJsonObject(CourierConstants.DELHIVERY_STATUS).get(CourierConstants.DELHIVERY_STATUS_DATETIME).getAsString();

                        Date delivery_date = getFormattedDeliveryDate(deliveryDate);

                        if (delivery_date != null && status.equalsIgnoreCase(CourierConstants.DELIVERED)) {
                            ordersDelivered = updateCourierDeliveryStatus(shippingOrderInList, shippingOrderInList.getShipment(), trackingId, delivery_date);
                        } else {
                            logger.error("Delivery date not avaialable or status is not delivered for : " + trackingId);
                        }
                    }
                }


            } else if (courierName.equalsIgnoreCase(CourierConstants.BLUEDART)) {
                SimpleDateFormat sdf_date = new SimpleDateFormat("dd MMMMM yyyy");
                courierIdList=new ArrayList<Long>();
                courierIdList = EnumCourier.getBlueDartCouriers();
                shippingOrderList = getAdminShippingOrderService().getShippingOrderListByCouriers(startDate, endDate, courierIdList);
                if (shippingOrderList != null && shippingOrderList.size() > 0) {
                    for (ShippingOrder shippingOrderInList : shippingOrderList) {
                        trackingId = shippingOrderInList.getShipment().getTrackingId();
                        Element ele = courierStatusUpdateHelper.updateDeliveryStatusBlueDart(trackingId);
                        String status = ele.getChildText(CourierConstants.BLUEDART_STATUS);
                        String statusDate = ele.getChildText(CourierConstants.BLUEDART_STATUS_DATE);
                        try {
                            if (status.equals(CourierConstants.BLUEDART_SHIPMENT_DELIVERED) && statusDate != null) {
                                Date delivery_date = sdf_date.parse(statusDate);
                                ordersDelivered = updateCourierDeliveryStatus(shippingOrderInList, shippingOrderInList.getShipment(), trackingId, delivery_date);
                            }
                        } catch (ParseException pe) {
                            logger.debug(CourierConstants.PARSE_EXCEPTION);
                        }
                    }
                }

            } else if (courierName.equalsIgnoreCase(CourierConstants.DTDC)) {
                courierIdList=new ArrayList<Long>();
                courierIdList = EnumCourier.getDTDCCouriers();
                shippingOrderList = getAdminShippingOrderService().getShippingOrderListByCouriers(startDate, endDate, courierIdList);
                Map<String, String> responseMap = new HashMap<String, String>();
                String courierDeliveryStatus = null;
                String deliveryDateString = null;
                if (shippingOrderList != null && shippingOrderList.size() > 0) {
                    for (ShippingOrder shippingOrderInList : shippingOrderList) {
                        trackingId = shippingOrderInList.getShipment().getTrackingId();
                        responseMap = courierStatusUpdateHelper.updateDeliveryStatusDTDC(trackingId);
                        for (Map.Entry entryObj : responseMap.entrySet()) {
                            if (entryObj.getKey().equals(CourierConstants.DTDC_INPUT_STR_STATUS)) {
                                courierDeliveryStatus = entryObj.getValue().toString();
                            }
                            if (entryObj.getKey().equals(CourierConstants.DTDC_INPUT_STR_STATUSTRANSON)) {
                                deliveryDateString = entryObj.getValue().toString();
                            }
                        }
                        String subStringDeliveryDate = null;
                        if (deliveryDateString != null) {
                            subStringDeliveryDate = deliveryDateString.substring(4, 8) + "-" + deliveryDateString.substring(2, 4) + "-" + deliveryDateString.substring(0, 2);
                        }
                        if (courierDeliveryStatus != null && deliveryDateString != null) {
                            if (courierDeliveryStatus.equals(CourierConstants.DTDC_INPUT_DELIVERED)) {
                                Date delivery_date = getFormattedDeliveryDate(subStringDeliveryDate);
                                ordersDelivered = updateCourierDeliveryStatus(shippingOrderInList, shippingOrderInList.getShipment(), trackingId, delivery_date);
                            }
                        }
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
        return orderDeliveryCount;
    }

    public String format_gateway_order_id(String order_gateway_id, int digitsInGatewayId) {
        if (order_gateway_id.length() > digitsInGatewayId - 1) {
            order_gateway_id = (order_gateway_id.substring(0, order_gateway_id.length() - digitsInGatewayId) + "-" + order_gateway_id.substring(order_gateway_id.length()
                    - digitsInGatewayId, order_gateway_id.length()));
        }
        return order_gateway_id;
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
