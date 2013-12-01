package com.hk.report.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.hk.constants.shipment.EnumBoxSize;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.courier.Zone;
import com.hk.pact.service.core.WarehouseService;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.util.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.hk.admin.util.BarcodeGenerator;

import com.akube.framework.dao.Page;
import com.akube.framework.util.DateUtils;
import com.hk.cache.CategoryCache;
import com.hk.constants.catalog.category.CategoryConstants;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.report.ReportConstants;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.constants.courier.EnumCourier;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.core.fliter.ShippingOrderFilter;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.ProductOption;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.core.Tax;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.Shipment;
import com.hk.domain.courier.Awb;
import com.hk.domain.inventory.rv.ReconciliationStatus;
import com.hk.domain.marketing.NotifyMe;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.user.Address;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.catalog.category.CategoryDaoImpl;
import com.hk.manager.OrderManager;
import com.hk.pact.dao.OrderStatusDao;
import com.hk.pact.dao.TaxDao;
import com.hk.pact.dao.email.NotifyMeDao;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.dao.payment.PaymentModeDao;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.service.OrderStatusService;
import com.hk.pact.service.catalog.CategoryService;
import com.hk.pact.service.order.CartLineItemService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.report.dto.order.CategoriesOrderReportDto;
import com.hk.report.dto.payment.CODConfirmationDto;
import com.hk.report.dto.sales.DaySaleDto;
import com.hk.report.dto.sales.DaySaleShipDateWiseDto;
import com.hk.report.pact.service.order.ReportOrderService;
import com.hk.report.pact.service.shippingOrder.ReportShippingOrderService;
import com.hk.util.CartLineItemUtil;
import com.hk.util.io.HkXlsWriter;

/**
 * Created by IntelliJ IDEA. User: Ajeet Date: May 31, 2011 Time: 1:20:51 PM To change this template use File | Settings |
 * File Templates.
 */
@Component
public class ReportManager {

    private static Logger logger = LoggerFactory.getLogger(ReportManager.class);

    SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");

    @Autowired
    ShippingOrderService shippingOrderService;

    @Autowired
    ShippingOrderStatusService shippingOrderStatusService;

    @Autowired
    OrderStatusService orderStatusService;

    @Autowired
    CartLineItemService cartLineItemService;

    @Autowired
    ReportOrderService reportOrderService;

    @Autowired
    OrderService orderService;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ReportShippingOrderService reportShippingOrderService;

    OrderManager orderManagerProvider;

    PaymentModeDao paymentModeDaoProvider;

    LineItemDao lineItemDaoProvider;

    OrderStatusDao orderStatusDaoProvider;

    CategoryDaoImpl categoryDaoProvider;

    OrderDao orderDaoProvider;

    /*
     * LineItemStatusDao> lineItemStatusDaoProvider;
     */

    @Autowired
    NotifyMeDao notifyMeDao;
    @Autowired
    BarcodeGenerator barcodeGenerator;

    TaxDao taxDaoProvider;

    ReportShippingOrderService shippingOrderReportingServiceProvider;

    /**
     * The method returns an excel gfile for the sales report
     */

    public File generateSalesReportXsl(String xslFilePath, Date startDate, Date endDate) throws Exception {

        /*
         * File file = new File(xslFilePath); FileOutputStream out = new FileOutputStream(file); Workbook wb = new
         * HSSFWorkbook(); CellStyle style = wb.createCellStyle(); Font font = wb.createFont();
         * font.setFontHeightInPoints((short) 11); font.setColor(Font.COLOR_NORMAL);
         * font.setBoldweight(Font.BOLDWEIGHT_BOLD); style.setFont(font); Sheet sheet1 = wb.createSheet("Sales"); Row
         * row = sheet1.createRow(0); row.setHeightInPoints((short) 30); int totalColumnNo = 35; int columnmCtr = 0;
         * Cell cell; for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) { cell = row.createCell(columnNo);
         * cell.setCellStyle(style); } setCellValue(row, 0, INVOICE_ID); setCellValue(row, 1, ACCOUNTING_INVOICE_ID);
         * setCellValue(row, 2, ORDER_DATE); setCellValue(row, 3, PAYMENT_MODE); setCellValue(row, 4, ORDER_STATUS);
         * setCellValue(row, 5, NAME); setCellValue(row, 6, CITY); setCellValue(row, 7, STATE); setCellValue(row, 8,
         * CATEGORY); setCellValue(row, 9, ITEM_NAME); setCellValue(row, 10, BRAND); setCellValue(row, 11,
         * VARIANT_DETAILS); setCellValue(row, 12, VAT_PERCENTAGE); setCellValue(row, 13, SURCHARGE_PERCENTAGE);
         * setCellValue(row, 14, MRP); setCellValue(row, 15, OFFER_PRICE); setCellValue(row, 16, COST_PRICE);
         * setCellValue(row, 17, TAX_PAID_SUPPLIER); setCellValue(row, 18, TAX_RECOVERABLE_SUPPLIER); setCellValue(row,
         * 19, QTY); setCellValue(row, 20, MRP_SALES); setCellValue(row, 21, NET_PRE_DISCOUNT); setCellValue(row, 22,
         * NET_DISCOUNT_COUPON); setCellValue(row, 23, NET_DISCOUNT_REWARD_POINTS); setCellValue(row, 24, NET_DISCOUNT);
         * setCellValue(row, 25, NET_POST_DISCOUNT); setCellValue(row, 26, TAXABLE); setCellValue(row, 27, VAT_CST);
         * setCellValue(row, 28, SURCHARGE); setCellValue(row, 29, TAX_COLLECTED); setCellValue(row, 30, NET_MARGIN);
         * int rowCounter = 1; List<Order> orderList = orderDaoProvider.get().findOrdersForTimeFrame(startDate,
         * endDate); logger.debug("orderList: " + orderList.size()); for (Order order : orderList) { List<LineItem>
         * lineItems = order.getLineItems(); boolean sameOrder = false; for (LineItem lineItem : lineItems) { if
         * (!lineItem.getLineItemType().equals(lineItemTypeDao.find(EnumLineItemType.OrderLevelDiscount.getId())) &&
         * !lineItem.getLineItemType().equals(lineItemTypeDao.find(EnumLineItemType.RewardPoint.getId()))) {
         * rowCounter++; row = sheet1.createRow(rowCounter); for (int columnNo = 0; columnNo < totalColumnNo;
         * columnNo++) { cell = row.createCell(columnNo); } if (!sameOrder) { setCellValue(row, 0,
         * order.getGatewayOrderId()); if (order.getAccountingInvoiceNumber() != null) setCellValue(row, 1,
         * order.getAccountingInvoiceNumber().toString()); setCellValue(row, 2, order.getPayment().getPaymentDate());
         * PaymentMode paymentMode = order.getPayment().getPaymentMode(); String paymentModeString =
         * paymentMode.getName(); if (paymentMode != null &&
         * paymentMode.equals(paymentModeDao.find(EnumPaymentMode.COD.getId())) && lineItem.getCourier() != null) {
         * paymentModeString += "-" + lineItem.getCourier().getName(); } setCellValue(row, 3, paymentModeString);
         * setCellValue(row, 4, order.getOrderStatus().getName()); setCellValue(row, 5, order.getAddress().getName());
         * setCellValue(row, 6, order.getAddress().getCity()); setCellValue(row, 7, order.getAddress().getState());
         * sameOrder = true; } if
         * (lineItem.getLineItemType().equals(lineItemTypeDao.find(EnumLineItemType.Product.getId()))) { Category
         * category = productManagerProvider.get().getTopLevelCategory(lineItem.getProductVariant().getProduct()); if
         * (category != null) { setCellValue(row, 8, category.getDisplayName()); } setCellValue(row, 9,
         * lineItem.getProductVariant().getProduct().getName()); setCellValue(row, 10,
         * lineItem.getProductVariant().getProduct().getBrand()); String varinatDetails = ""; for (ProductOption o :
         * lineItem.getProductVariant().getProductOptions()) { varinatDetails += o.getName() + ":" + o.getValue() + " | ";
         * for (LineItemExtraOption exo : lineItem.getExtraOptions()) { varinatDetails +=exo.getName() + ":" +
         * exo.getValue() + "|"; } } setCellValue(row, 11, varinatDetails); Double vat_cst_percentage =
         * lineItem.getProductVariant().getTax().getValue(); setCellValue(row, 12, vat_cst_percentage); Double
         * surcharge_percentage = lineItem.getProductVariant().getTax().getValue() * 0.05; setCellValue(row, 13,
         * surcharge_percentage); setCellValue(row, 14, lineItem.getMarkedPrice()); setCellValue(row, 15,
         * lineItem.getHkPrice()); Double costPrice = null; if (lineItem.getCostPrice() != null) { costPrice =
         * lineItem.getCostPrice(); } else if (lineItem.getProductVariant().getCostPrice() != null) { costPrice =
         * lineItem.getProductVariant().getCostPrice(); } else { costPrice = lineItem.getHkPrice(); } setCellValue(row,
         * 16, costPrice); Double taxPaid = 0.0; Double taxRecoverable = 0.0; if
         * (lineItem.getProductVariant().getProduct().getSupplier() != null && costPrice != null) { if
         * (lineItem.getProductVariant().getProduct().getSupplier().getState().equalsIgnoreCase("haryana")) { Double
         * surcharge = 0.05; taxPaid = costPrice * lineItem.getProductVariant().getTax().getValue() * (1 + surcharge); }
         * else { Double surcharge = 0.0; // CST Surcharge Double cst = 0.02; //CST taxPaid = costPrice * cst; }
         * setCellValue(row, 17, taxPaid); if
         * (lineItem.getProductVariant().getProduct().getSupplier().getState().equalsIgnoreCase("haryana")) {
         * taxRecoverable = taxPaid; } setCellValue(row, 18, taxRecoverable); } setCellValue(row, 19,
         * lineItem.getQty()); Double mrpSales = lineItem.getMarkedPrice() * lineItem.getQty(); setCellValue(row, 20,
         * mrpSales); Double netPreDiscount = lineItem.getHkPrice() * lineItem.getQty(); setCellValue(row, 21,
         * netPreDiscount); Double couponDiscount = orderManagerProvider.get().getCouponDiscountOnLineItem(lineItem);
         * setCellValue(row, 22, couponDiscount); Double redeemPointDiscount =
         * orderManagerProvider.get().getProRatedRedeemPointsLineItem(lineItem); setCellValue(row, 23,
         * redeemPointDiscount); Double discount = orderManagerProvider.get().getNetDiscountOnLineItem(lineItem);
         * setCellValue(row, 24, discount); Double netPostDiscount = netPreDiscount - discount; setCellValue(row, 25,
         * netPostDiscount); Double taxable = netPostDiscount / (1 + vat_cst_percentage + surcharge_percentage);
         * setCellValue(row, 26, taxable); Double vat_cst = netPostDiscount * vat_cst_percentage / (1 +
         * vat_cst_percentage + surcharge_percentage); setCellValue(row, 27, vat_cst); Double surcharge =
         * netPostDiscount * surcharge_percentage / (1 + vat_cst_percentage + surcharge_percentage); setCellValue(row,
         * 28, surcharge); setCellValue(row, 29, vat_cst + surcharge); if (costPrice != null) { setCellValue(row, 30,
         * taxable - (costPrice + taxPaid - taxRecoverable) * lineItem.getQty()); } } else if
         * (lineItem.getLineItemType().equals(lineItemTypeDao.find(EnumLineItemType.Shipping.getId()))) {
         * setCellValue(row, 8, EnumLineItemType.Shipping.getName()); setCellValue(row, 20, lineItem.getHkPrice() -
         * lineItem.getDiscountOnHkPrice()); } else if
         * (lineItem.getLineItemType().equals(lineItemTypeDao.find(EnumLineItemType.CodCharges.getId()))) {
         * setCellValue(row, 8, EnumLineItemType.CodCharges.getName()); setCellValue(row, 20, lineItem.getHkPrice() -
         * lineItem.getDiscountOnHkPrice()); } } } } wb.write(out); out.close(); return file;
         */

        return null;
    }

    public File generateSalesReportXslForBusy(String xslFilePath, Date startDate, Date endDate) throws Exception {
        return null;
    }

    /**
     * The method returns an excel file for the category sales report - for cold calls and resale
     */

    public File generateCategorySalesReportXsl(String xslFilePath, Date startDate, Date endDate, List<Category> categories) throws Exception {

        HkXlsWriter xlsWriter = new HkXlsWriter();

        xlsWriter.addHeader(ReportConstants.USER_ID, ReportConstants.USER_ID);
        xlsWriter.addHeader(ReportConstants.NAME, ReportConstants.NAME);
        xlsWriter.addHeader(ReportConstants.EMAIL, ReportConstants.EMAIL);
        xlsWriter.addHeader(ReportConstants.PHONE, ReportConstants.PHONE);
        xlsWriter.addHeader(ReportConstants.CITY, ReportConstants.CITY);
        xlsWriter.addHeader(ReportConstants.STATE, ReportConstants.STATE);
        xlsWriter.addHeader(ReportConstants.BASE_ORDER_ID, ReportConstants.BASE_ORDER_ID);
        xlsWriter.addHeader(ReportConstants.ORDER_DATE, ReportConstants.ORDER_DATE);
        xlsWriter.addHeader(ReportConstants.CATEGORY, ReportConstants.CATEGORY);
        xlsWriter.addHeader(ReportConstants.ITEM_NAME, ReportConstants.ITEM_NAME);

        int rowCounter = 0;
        List<Order> orderList = getReportOrderService().findOrdersForTimeFrame(startDate, endDate, categories);
        System.out.println("orderList: " + orderList.size());
        for (Order order : orderList) {

            ShippingOrderFilter shippingOrderFilter = new ShippingOrderFilter(new HashSet<ShippingOrder>(order.getShippingOrders()));
            List<EnumShippingOrderStatus> applicableShippingOrderStatus = EnumShippingOrderStatus.getStatusForCRMReport();
            Set<ShippingOrder> shippingOrders = shippingOrderFilter.filterShippingOrdersByStatus(applicableShippingOrderStatus);
            for (ShippingOrder shippingOrder : shippingOrders) {

                Set<LineItem> lineItems = shippingOrder.getLineItems();
                for (LineItem lineItem : lineItems) {
                    rowCounter++;
                    xlsWriter.addCell(rowCounter, order.getUser().getId());
                    xlsWriter.addCell(rowCounter, order.getAddress().getName());
                    xlsWriter.addCell(rowCounter, order.getUser().getEmail());
                    xlsWriter.addCell(rowCounter, order.getAddress().getPhone());
                    xlsWriter.addCell(rowCounter, order.getAddress().getCity());
                    xlsWriter.addCell(rowCounter, order.getAddress().getState());
                    xlsWriter.addCell(rowCounter, order.getGatewayOrderId());
                    xlsWriter.addCell(rowCounter, order.getPayment().getPaymentDate().toString());
                    xlsWriter.addCell(rowCounter, getCategoryService().getTopLevelCategory(lineItem.getSku().getProductVariant().getProduct()));
                    xlsWriter.addCell(rowCounter, lineItem.getSku().getProductVariant().getProduct().getName());
                }
            }
        }
        return xlsWriter.writeData(xslFilePath);
    }

    /**
     * The method returns an excel gfile for the courier report
     */
    public File generateCourierReportXsl(String xslFilePath, EnumShippingOrderStatus shippingOrderStatus, List<Courier> courierList, Date startDate, Date endDate,
                                         Warehouse warehouse, Zone zone) throws Exception {

        List<ShippingOrder> shippingOrderList = null;
        File file = new File(xslFilePath);
        FileOutputStream out = new FileOutputStream(file);
        Workbook wb = new HSSFWorkbook();

        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setColor(Font.COLOR_NORMAL);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(font);
        Sheet sheet1 = wb.createSheet("Courier");
        Row row = sheet1.createRow(0);
        row.setHeightInPoints((short) 30);

        // for adding barcode picture to excel
        InputStream is = null;
        byte[] bytes = null;
        int pictureIdx = 0;
        CreationHelper helper = null;
        Drawing drawing = sheet1.createDrawingPatriarch();
        ClientAnchor anchor = null;
        Picture pict = null;

        int totalColumnNo = 25;

        Cell cell;
        for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
            cell = row.createCell(columnNo);
            cell.setCellStyle(style);
        }
        setCellValue(row, 0, ReportConstants.AWB);
        setCellValue(row, 1, ReportConstants.SHIPPERS_REFERENCE_NUMBER);
        setCellValue(row, 2, ReportConstants.CONSIGNEE_CONTACT_NAME);
        setCellValue(row, 3, ReportConstants.CONSIGNEE_NAME);
        setCellValue(row, 4, ReportConstants.CONSIGNEE_ADDRESS_LINE_1);
        setCellValue(row, 5, ReportConstants.CONSIGNEE_ADDRESS_LINE_2);
        setCellValue(row, 6, ReportConstants.CONSIGNEE_ADDRESS_LINE_3);
        setCellValue(row, 7, ReportConstants.CONSIGNEE_ADDRESS_LINE_4);
        setCellValue(row, 8, ReportConstants.CONSIGNEE_CITY);
        setCellValue(row, 9, ReportConstants.CONSIGNEE_PINCODE);
        setCellValue(row, 10, ReportConstants.CONSIGNEE_PHONE_NUMBER);
        setCellValue(row, 11, ReportConstants.CONSIGNEE_MOBILE);
        setCellValue(row, 12, ReportConstants.PRODUCT);
        setCellValue(row, 13, ReportConstants.WEIGHT);
        setCellValue(row, 14, ReportConstants.PIECE);
        setCellValue(row, 15, ReportConstants.DECLARED_VALUE);
        setCellValue(row, 16, ReportConstants.CONTENT_DESCRIPTION);
        setCellValue(row, 17, ReportConstants.COD_AMOUNT);
        setCellValue(row, 18, ReportConstants.COURIER);
        setCellValue(row, 19, ReportConstants.PAYMENT_MODE);
        setCellValue(row, 20, ReportConstants.SHIPMENT_DATE);
        setCellValue(row, 21, ReportConstants.BOX_SIZE);
        setCellValue(row, 22, ReportConstants.BOX_WEIGHT);
        if (zone != null) {
            setCellValue(row, 23, ReportConstants.ZONE);
        }
        if (courierList.contains(EnumCourier.Speedpost.asCourier())) {
            setCellValue(row, 24, ReportConstants.BARCODE);
        }

        int rowCounter = 1;
        if (startDate == null && endDate == null) {
            ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
            shippingOrderSearchCriteria.setShippingOrderStatusList(shippingOrderStatusService.getOrderStatuses(EnumShippingOrderStatus.getStatusForShipmentAwaiting()));
            shippingOrderSearchCriteria.setCourierList(courierList);

            Page shippingOrderPage = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria, 1, 1000);
            if (shippingOrderPage != null) {
                shippingOrderList = shippingOrderPage.getList();
            }
        } else {
            shippingOrderList = reportShippingOrderService.getShippingOrderListForCouriers(startDate, endDate, courierList, warehouse);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        if (shippingOrderList != null) {
            for (ShippingOrder order : shippingOrderList) {


                Set<LineItem> lineItems = order.getLineItems();
                LineItem firstProductLineItem = lineItems.iterator().next();
                rowCounter++;
                row = sheet1.createRow(rowCounter);
                for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
                    cell = row.createCell(columnNo);
                }
                if (zone != null) {
                    Zone shippingOrderZone = order.getShipment().getZone();
                    if (shippingOrderZone != null && shippingOrderZone.equals(zone)) {
                        setCellValue(row, 23, shippingOrderZone.getName().toUpperCase());
                    } else {
                        sheet1.removeRow(row);
                        rowCounter--;
                        continue;
                    }
                }
                Shipment shipment = order.getShipment();
                Awb awb = shipment.getAwb();
                Address address = order.getBaseOrder().getAddress();
                String trackingId = null;
                if (awb != null) {
                    trackingId = awb.getAwbNumber();
                }
                setCellValue(row, 0, trackingId);
                setCellValue(row, 1, order.getGatewayOrderId());
                setCellValue(row, 2, address.getName());
                setCellValue(row, 3, address.getName());
                setCellValue(row, 4, address.getLine1());

                if (address.getLine2() != null) {
                    setCellValue(row, 5, address.getLine2());
                }
                setCellValue(row, 6, "");
                setCellValue(row, 7, "");
                setCellValue(row, 8, address.getCity());
                setCellValue(row, 9, address.getPincode().getPincode());
                setCellValue(row, 10, address.getPhone());
                setCellValue(row, 11, address.getPhone());

                String contains = "General Goods";
                /*
                 * Category homeHealthDevices = getCategoryService().getCategoryByName(CategoryConstants.HEALTH_DEVICES);
                 * Category diabetes = getCategoryService().getCategoryByName(CategoryConstants.DIABETES); Category eye =
                 * getCategoryService().getCategoryByName(CategoryConstants.EYE);
                 */

                Category homeHealthDevices = CategoryCache.getInstance().getCategoryByName(CategoryConstants.HEALTH_DEVICES).getCategory();
                Category diabetes = CategoryCache.getInstance().getCategoryByName(CategoryConstants.DIABETES).getCategory();
                Category eye = CategoryCache.getInstance().getCategoryByName(CategoryConstants.EYE).getCategory();

                List<Category> categoryList = firstProductLineItem.getSku().getProductVariant().getProduct().getCategories();
                if (categoryList.contains(homeHealthDevices) || categoryList.contains(diabetes)) {
                    contains = "Monitor/Strips";
                } else if (categoryList.contains(eye)) {
                    contains = "Contact Lens";
                }
                setCellValue(row, 12, contains);
                setCellValue(row, 13, "");
                Long qty = 0L;
                Double declaredValue = order.getAmount();
                for (LineItem lineItem : lineItems) {
                    qty += lineItem.getQty();
                    // declaredValue += lineItem.getQty() * lineItem.getMarkedPrice();
                }
                setCellValue(row, 14, qty.toString());
                setCellValue(row, 15, declaredValue);
                setCellValue(row, 16, "");
                String paymentMode = "Pre-paid";
                if (order.getBaseOrder().getPayment().getPaymentMode().getId().equals(EnumPaymentMode.COD.getId()) && order.getAmount() > 10.0) {
                    setCellValue(row, 17, order.getAmount());
                    paymentMode = ReportConstants.COD;
                }
                setCellValue(row, 18, awb.getCourier().getName());
                setCellValue(row, 19, paymentMode);
                setCellValue(row, 20, shipment.getShipDate());
                if (shipment.getBoxSize() != null) {
                    setCellValue(row, 21, shipment.getBoxSize().getName());
                }
                if (shipment.getBoxWeight() != null) {
                    setCellValue(row, 22, shipment.getBoxWeight());
                }

                if (awb != null && awb.getCourier().equals((EnumCourier.Speedpost.asCourier()))) {
                    String barcodePath = barcodeGenerator.getBarcodePath(awb.getAwbNumber(), 1.0f, 150, false);
                    // add picture data to this workbook.
                    is = new FileInputStream(barcodePath);
                    bytes = IOUtils.toByteArray(is);
                    pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
                    is.close();
                    helper = wb.getCreationHelper();
                    sheet1.autoSizeColumn(24);
                    //sheet1.addMergedRegion()
                    //sheet1.groupRow(rowCounter, rowCounter++);
                    row.setHeightInPoints(60);

                    // Create the drawing patriarch. This is the top level container for all shapes.
                    // add a picture shape
                    anchor = helper.createClientAnchor();
                    anchor.setAnchorType(ClientAnchor.MOVE_AND_RESIZE);

                    // set top-left corner of the picture,
                    pict = drawing.createPicture(anchor, pictureIdx);
                    pict.resize(8.0);

                    //setCellValue(row, 24, awbNumber );
                    anchor.setDx1(10);
                    anchor.setDx2(1000);
                    anchor.setDy1(10);
                    anchor.setDy2(200);
                    anchor.setCol1(24);
                    anchor.setRow1(rowCounter);
                    anchor.setCol2(24);
                    anchor.setRow2(rowCounter);
                }
            }
        }
        wb.write(out);
        out.close();
        return file;
    }


    public File generateCourierReportXslForBlueDart(String xslFilePath, EnumShippingOrderStatus orderStatus, List<Courier> courierList, Date startDate, Date endDate,
                                                    Warehouse warehouse) throws Exception {
        List<ShippingOrder> shippingOrderList = null;
        File file = new File(xslFilePath);
        FileOutputStream out = new FileOutputStream(file);
        Workbook wb = new HSSFWorkbook();

        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setColor(Font.COLOR_NORMAL);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(font);
        Sheet sheet1 = wb.createSheet("Courier");
        Row row = sheet1.createRow(0);
        row.setHeightInPoints((short) 30);

        int totalColumnNo = 29;

        Cell cell;
        for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
            cell = row.createCell(columnNo);
            cell.setCellStyle(style);
        }
        setCellValue(row, 0, ReportConstants.AIR_WAY_BILL);
        setCellValue(row, 1, ReportConstants.TYPE);
        setCellValue(row, 2, ReportConstants.REFERENCE_NUMBER);
        setCellValue(row, 3, ReportConstants.SENDER);
        setCellValue(row, 4, ReportConstants.ATTENTION);
        setCellValue(row, 5, ReportConstants.ADDRESS_1);
        setCellValue(row, 6, ReportConstants.ADDRESS_2);
        setCellValue(row, 7, ReportConstants.ADDRESS_3);
        setCellValue(row, 8, ReportConstants.PINCODE);
        setCellValue(row, 9, ReportConstants.TEL_NUMBER);
        setCellValue(row, 10, ReportConstants.MOBILE_NUMBER);
        setCellValue(row, 11, ReportConstants.PRODUCT);
        setCellValue(row, 12, ReportConstants.CONTENT);
        setCellValue(row, 13, ReportConstants.WEIGHT);
        setCellValue(row, 14, ReportConstants.DECLARED_VALUE);
        setCellValue(row, 15, ReportConstants.COLLECTABLE_VALUES);
        setCellValue(row, 16, ReportConstants.VENDOR_CODE);
        setCellValue(row, 17, ReportConstants.SHIPPER_NAME);
        setCellValue(row, 18, ReportConstants.RETURN_ADDRESS1);
        setCellValue(row, 19, ReportConstants.RETURN_ADDRESS2);
        setCellValue(row, 20, ReportConstants.RETURN_ADDRESS3);
        setCellValue(row, 21, ReportConstants.RETURN_PIN);
        setCellValue(row, 22, ReportConstants.LENGTH);
        setCellValue(row, 23, ReportConstants.BREADTH);
        setCellValue(row, 24, ReportConstants.HEIGHT);
        setCellValue(row, 25, ReportConstants.PIECES);
        setCellValue(row, 26, ReportConstants.AREA_CUSTOMER_CODE);
        setCellValue(row, 27, ReportConstants.HAND_OVER_DATE);
        setCellValue(row, 28, ReportConstants.HAND_OVER_TIME);
        int rowCounter = 1;
        Page orderPage = null;

        if (startDate == null && endDate == null) {
            ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
            shippingOrderSearchCriteria.setShippingOrderStatusList(shippingOrderStatusService.getOrderStatuses(EnumShippingOrderStatus.getStatusForShipmentAwaiting()));
            shippingOrderSearchCriteria.setCourierList(courierList);
            Page shippingOrderPage = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria, 1, 1000);

            if (shippingOrderPage != null) {
                shippingOrderList = shippingOrderPage.getList();
            }
        } else {
            shippingOrderList = reportShippingOrderService.getShippingOrderListForCouriers(startDate, endDate, courierList, warehouse);
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (ShippingOrder order : shippingOrderList) {
            Set<LineItem> lineItems = order.getLineItems();
            rowCounter++;
            row = sheet1.createRow(rowCounter);
            for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
                cell = row.createCell(columnNo);
            }

            Address address = order.getBaseOrder().getAddress();
            Shipment shipment = order.getShipment();
            if (shipment != null && shipment.getAwb() != null) {
                setCellValue(row, 0, shipment.getAwb().getAwbNumber());
            }

            if (order.getBaseOrder().getPayment().getPaymentMode().getId().equals(EnumPaymentMode.COD.getId())) {
                setCellValue(row, 1, "COD");
                setCellValue(row, 15, order.getAmount());
                if (warehouse.getId().equals(WarehouseService.GGN_BRIGHT_WH_ID)) {
                    setCellValue(row, 26, ReportConstants.Ggn_Cod_Area_Customer_Code);
                    setCellValue(row,16,ReportConstants.Ggn_Vendor_Code);
                } else if (warehouse.getId().equals(WarehouseService.MUM_BRIGHT_WH_ID)) {
                    setCellValue(row, 26, ReportConstants.Mumbai_Cod_Area_Customer_Code);
                    setCellValue(row,16,ReportConstants.Mumbai_Vendor_Code);
                }else if (warehouse.getId().equals(WarehouseService.DEL_KAPASHERA_BRIGHT_WH_ID)) {
                    setCellValue(row, 26, ReportConstants.Delhi_Kapp_Cod_Area_Customer_Code);
                    setCellValue(row,16,ReportConstants.Delhi_Kapp_Vendor_Code);
                }

            } else {
                setCellValue(row, 15, 0.0);
                setCellValue(row, 1, "NonCOD");
                if (warehouse.getId().equals(WarehouseService.GGN_BRIGHT_WH_ID)) {
                    setCellValue(row, 26, ReportConstants.Ggn_Prepaid_Area_Customer_Code);
                    setCellValue(row,16,ReportConstants.Ggn_Vendor_Code);
                } else if (warehouse.getId().equals(WarehouseService.MUM_BRIGHT_WH_ID)) {
                    setCellValue(row, 26, ReportConstants.Mumbai_Prepaid_Area_Customer_Code);
                    setCellValue(row,16,ReportConstants.Mumbai_Vendor_Code);
                } else if(warehouse.getId().equals(WarehouseService.DEL_KAPASHERA_BRIGHT_WH_ID)){
                    setCellValue(row,26,ReportConstants.Delhi_Kapp_Prepaid_Area_Customer_Code);
                    setCellValue(row,16,ReportConstants.Delhi_Kapp_Vendor_Code);
                }

            }

            setCellValue(row, 2, order.getGatewayOrderId());
            setCellValue(row, 3, "AQUAMARINE HEALTHCARE");
            setCellValue(row, 4, address.getName());
            setCellValue(row, 5, address.getLine1());

            if (address.getLine2() != null) {
                setCellValue(row, 6, address.getLine2());
            }
            setCellValue(row, 7, address.getCity() + "/" + address.getState());
            setCellValue(row, 8, address.getPincode());
            setCellValue(row, 9, address.getPhone());
            setCellValue(row, 10, address.getPhone());

            String contains = "General Goods";
            /*
             * Category homeHealthDevices = getCategoryService().getCategoryByName(CategoryConstants.HEALTH_DEVICES);
             * Category diabetes = getCategoryService().getCategoryByName(CategoryConstants.DIABETES); Category eye =
             * getCategoryService().getCategoryByName(CategoryConstants.EYE);
             */

            Category homeHealthDevices = CategoryCache.getInstance().getCategoryByName(CategoryConstants.HEALTH_DEVICES).getCategory();
            Category diabetes = CategoryCache.getInstance().getCategoryByName(CategoryConstants.DIABETES).getCategory();
            Category eye = CategoryCache.getInstance().getCategoryByName(CategoryConstants.EYE).getCategory();

            LineItem firstProductLineItem = order.getLineItems().iterator().next();
            List<Category> categoryList = firstProductLineItem.getSku().getProductVariant().getProduct().getCategories();
            if (categoryList.contains(homeHealthDevices) || categoryList.contains(diabetes)) {
                contains = "Monitor/Strips";
            } else if (categoryList.contains(eye)) {
                contains = "Contact Lens";
            }
            setCellValue(row, 11, contains);
            setCellValue(row, 12, contains);
            if (shipment.getBoxWeight() != null) {
                setCellValue(row, 13, shipment.getBoxWeight());
            }
            Double declaredValue = order.getAmount();
            // for (LineItem lineItem : lineItems) {
            // declaredValue += lineItem.getQty() * lineItem.getMarkedPrice();
            // }
            setCellValue(row, 14, declaredValue);
            setCellValue(row, 17, "AQUAMARINE HEALTHCARE");

            setCellValue(row, 18, warehouse.getLine1());
            if (warehouse.getLine2() != null) {
                setCellValue(row, 19, warehouse.getLine2());
            }
            if (warehouse.getLine2() != null) {
                setCellValue(row, 20, warehouse.getCity() + "/" + warehouse.getState());
            }
            setCellValue(row, 21, warehouse.getPincode());
            Double boxWeight = shipment.getBoxWeight();
            Double length;
            Double breadth;
            Double height;


            EnumBoxSize enumBoxSize = EnumBoxSize.getBoxSize(shipment.getBoxSize());
            if (enumBoxSize == null) {
                length = 0D;
                breadth = 0D;
                height = 0D;
            } else {
                length = enumBoxSize.getLength();
                breadth = enumBoxSize.getBreadth();
                height = enumBoxSize.getHeight();
            }

            setCellValue(row, 22, length);
            setCellValue(row, 23, breadth);
            setCellValue(row, 24, height);
            setCellValue(row, 25, "1");
//            setCellValue(row,27,"NA");
//            setCellValue(row,28,"NA");
            if (shipment != null && shipment.getShipDate() != null) {
                setCellValue(row,27, shipment.getShipDate().getDate());
                setCellValue(row,28,1650);
            }
            else{
                setCellValue(row,27,"NA");
                setCellValue(row,28,"NA");
            }

        }
        wb.write(out);
        out.close();
        return file;

    }

    /**
     * The method returns an excel file for category sales report
     */

    public File generateSalesByDateReportXls(String xlsFilePath, Date startDate, Date endDate) throws Exception {

        /*
         * File file = new File(xlsFilePath); FileOutputStream out = new FileOutputStream(file); Workbook wb = new
         * HSSFWorkbook(); CellStyle style = wb.createCellStyle(); Font font = wb.createFont();
         * font.setFontHeightInPoints((short) 11); font.setColor(Font.COLOR_NORMAL);
         * font.setBoldweight(Font.BOLDWEIGHT_BOLD); style.setFont(font); Sheet sheet1 =
         * wb.createSheet("Sales-by-date"); Row row = sheet1.createRow(0); row.setHeightInPoints((short) 30); int
         * totalColumnNo = 7; Cell cell; for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) { cell =
         * row.createCell(columnNo); cell.setCellStyle(style); } setCellValue(row, 0, ORDER_DATE); setCellValue(row, 1,
         * DAY_OF_WEEK); setCellValue(row, 2, TXN_COUNT); setCellValue(row, 3, SKU_COUNT); setCellValue(row, 4,
         * SUM_MRP); setCellValue(row, 5, SUM_HK_PRICE); setCellValue(row, 6, SUM_HK_PRICE_POST_DISCOUNT);
         * SimpleDateFormat sdf_day_of_week = new SimpleDateFormat("E"); int rowCounter = 1; List<DaySaleDto>
         * daySaleList = orderDaoProvider.get().findSaleForTimeFrame(startDate, endDate);
         * System.out.println("daySaleList: " + daySaleList.size()); for (DaySaleDto daySaleDto : daySaleList) {
         * rowCounter++; row = sheet1.createRow(rowCounter); for (int columnNo = 0; columnNo < totalColumnNo;
         * columnNo++) { cell = row.createCell(columnNo); } setCellValue(row, 0, sdf.format(daySaleDto.getOrderDate()));
         * setCellValue(row, 1, sdf_day_of_week.format(daySaleDto.getOrderDate())); setCellValue(row, 2,
         * daySaleDto.getTxnCount()); setCellValue(row, 3, daySaleDto.getSkuCount()); setCellValue(row, 4,
         * daySaleDto.getSumOfMrp()); setCellValue(row, 5, daySaleDto.getSumOfHkPrice()); Double orderLevelCoupon =
         * orderDaoProvider.get().getNetDiscountViaOrderLevelCouponAndRewardPoints(daySaleDto.getOrderDate()); if
         * (orderLevelCoupon != null) { setCellValue(row, 6, (daySaleDto.getSumOfHkPrice() -
         * (daySaleDto.getSumOfHkPricePostAllDiscounts() + orderLevelCoupon))); } else { setCellValue(row, 6,
         * daySaleDto.getSumOfHkPrice()); } } wb.write(out); out.close(); return file;
         */

        return null;
    }

    public File generateSalesByDateReportForShippedProductsXls(String xlsFilePath, Date startDate, Date endDate) throws Exception {

        /*
         * //return null; File file = new File(xlsFilePath); FileOutputStream out = new FileOutputStream(file); Workbook
         * wb = new HSSFWorkbook(); CellStyle style = wb.createCellStyle(); Font font = wb.createFont();
         * font.setFontHeightInPoints((short) 11); font.setColor(Font.COLOR_NORMAL);
         * font.setBoldweight(Font.BOLDWEIGHT_BOLD); style.setFont(font); Sheet sheet1 =
         * wb.createSheet("Sales-by-date"); Row row = sheet1.createRow(0); row.setHeightInPoints((short) 30); int
         * totalColumnNo = 37; Cell cell; for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) { cell =
         * row.createCell(columnNo); cell.setCellStyle(style); } setCellValue(row, 0, SHIP_DATE); setCellValue(row, 1,
         * DAY_OF_WEEK); setCellValue(row, 3, TOTALS_FOR_DAY); setCellValue(row, 10, IN_SHIPPED_STATUS);
         * setCellValue(row, 17, IN_DELIVERED_STATUS); setCellValue(row, 24, IN_RETURNED_STATUS); setCellValue(row, 31,
         * IN_CANCELLED_STATUS); row = sheet1.createRow(1); row.setHeightInPoints((short) 30); for (int columnNo = 0;
         * columnNo < totalColumnNo; columnNo++) { cell = row.createCell(columnNo); cell.setCellStyle(style); }
         * setCellValue(row, 0, SHIP_DATE); setCellValue(row, 1, DAY_OF_WEEK); setCellValue(row, 3, SUM_MRP);
         * setCellValue(row, 4, SUM_HK_PRICE); setCellValue(row, 5, SUM_DISCOUNT); setCellValue(row, 6, NET_SALES);
         * setCellValue(row, 7, FORWARDING_CHARGES); setCellValue(row, 8, NET_RECEIVABLE); setCellValue(row, 10,
         * SUM_MRP); setCellValue(row, 11, SUM_HK_PRICE); setCellValue(row, 12, SUM_DISCOUNT); setCellValue(row, 13,
         * NET_SALES); setCellValue(row, 14, FORWARDING_CHARGES); setCellValue(row, 15, NET_RECEIVABLE);
         * setCellValue(row, 17, SUM_MRP); setCellValue(row, 18, SUM_HK_PRICE); setCellValue(row, 19, SUM_DISCOUNT);
         * setCellValue(row, 20, NET_SALES); setCellValue(row, 21, FORWARDING_CHARGES); setCellValue(row, 22,
         * NET_RECEIVABLE); setCellValue(row, 24, SUM_MRP); setCellValue(row, 25, SUM_HK_PRICE); setCellValue(row, 26,
         * SUM_DISCOUNT); setCellValue(row, 27, NET_SALES); setCellValue(row, 28, FORWARDING_CHARGES); setCellValue(row,
         * 29, NET_RECEIVABLE); setCellValue(row, 31, SUM_MRP); setCellValue(row, 32, SUM_HK_PRICE); setCellValue(row,
         * 33, SUM_DISCOUNT); setCellValue(row, 34, NET_SALES); setCellValue(row, 35, FORWARDING_CHARGES);
         * setCellValue(row, 36, NET_RECEIVABLE); SimpleDateFormat sdf_day_of_week = new SimpleDateFormat("E"); int
         * rowCounter = 2; List<OrderStatus> applicableOrderStatus = new ArrayList<OrderStatus>();
         * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Shipped.getId()));
         * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Delivered.getId()));
         * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Cancelled.getId()));
         * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.RETURNED.getId())); List<DaySaleShipDateWiseDto>
         * daySaleShipDateWiseDtoList = generateSalesByDateReportForShippedProducts(startDate, endDate);
         * System.out.println("daySaleList: " + daySaleShipDateWiseDtoList.size()); for (DaySaleShipDateWiseDto
         * daySaleShipDateWiseDto : daySaleShipDateWiseDtoList) { rowCounter++; row = sheet1.createRow(rowCounter); for
         * (int columnNo = 0; columnNo < totalColumnNo; columnNo++) { cell = row.createCell(columnNo); }
         * setCellValue(row, 0, sdf.format(daySaleShipDateWiseDto.getOrderShipDate())); setCellValue(row, 1,
         * sdf_day_of_week.format(daySaleShipDateWiseDto.getOrderShipDate())); setCellValue(row, 3,
         * Math.round(daySaleShipDateWiseDto.getTotalSumOfMrp())); setCellValue(row, 4,
         * Math.round(daySaleShipDateWiseDto.getTotalSumOfHkPrice())); setCellValue(row, 5,
         * Math.round(daySaleShipDateWiseDto.getTotalSumOfAllDiscounts())); setCellValue(row, 6,
         * Math.round(daySaleShipDateWiseDto.getTotalSumOfHkPricePostAllDiscounts())); setCellValue(row, 7,
         * Math.round(daySaleShipDateWiseDto.getTotalSumOfForwardingCharges())); setCellValue(row, 8,
         * Math.round(daySaleShipDateWiseDto.getTotalSumOfNetReceivable())); setCellValue(row, 10,
         * Math.round(daySaleShipDateWiseDto.getSumOfMrpOfShippedItems())); setCellValue(row, 11,
         * Math.round(daySaleShipDateWiseDto.getSumOfHkPriceOfShippedItems())); setCellValue(row, 12,
         * Math.round(daySaleShipDateWiseDto.getSumOfAllDiscountsOfShippedItems())); setCellValue(row, 13,
         * Math.round(daySaleShipDateWiseDto.getSumOfHkPricePostAllDiscountsOfShippedItems())); setCellValue(row, 14,
         * Math.round(daySaleShipDateWiseDto.getSumOfForwardingChargesOfShippedItems())); setCellValue(row, 15,
         * Math.round(daySaleShipDateWiseDto.getSumOfNetReceivableOfShippedItems())); setCellValue(row, 17,
         * Math.round(daySaleShipDateWiseDto.getSumOfMrpOfDeliveredItems())); setCellValue(row, 18,
         * Math.round(daySaleShipDateWiseDto.getSumOfHkPriceOfDeliveredItems())); setCellValue(row, 19,
         * Math.round(daySaleShipDateWiseDto.getSumOfAllDiscountsOfDeliveredItems())); setCellValue(row, 20,
         * Math.round(daySaleShipDateWiseDto.getSumOfHkPricePostAllDiscountsOfDeliveredItems())); setCellValue(row, 21,
         * Math.round(daySaleShipDateWiseDto.getSumOfForwardingChargesOfDeliveredItems())); setCellValue(row, 22,
         * Math.round(daySaleShipDateWiseDto.getSumOfNetReceivableOfDeliveredItems())); setCellValue(row, 24,
         * Math.round(daySaleShipDateWiseDto.getSumOfMrpOfReturnedItems())); setCellValue(row, 25,
         * Math.round(daySaleShipDateWiseDto.getSumOfHkPriceOfReturnedItems())); setCellValue(row, 26,
         * Math.round(daySaleShipDateWiseDto.getSumOfAllDiscountsOfReturnedItems())); setCellValue(row, 27,
         * Math.round(daySaleShipDateWiseDto.getSumOfHkPricePostAllDiscountsOfReturnedItems())); setCellValue(row, 28,
         * Math.round(daySaleShipDateWiseDto.getSumOfForwardingChargesOfReturnedItems())); setCellValue(row, 29,
         * Math.round(daySaleShipDateWiseDto.getSumOfNetReceivableOfReturnedItems())); setCellValue(row, 31,
         * Math.round(daySaleShipDateWiseDto.getSumOfMrpOfCancelledItems())); setCellValue(row, 32,
         * Math.round(daySaleShipDateWiseDto.getSumOfHkPriceOfCancelledItems())); setCellValue(row, 33,
         * Math.round(daySaleShipDateWiseDto.getSumOfAllDiscountsOfCancelledItems())); setCellValue(row, 34,
         * Math.round(daySaleShipDateWiseDto.getSumOfHkPricePostAllDiscountsOfCancelledItems())); setCellValue(row, 35,
         * Math.round(daySaleShipDateWiseDto.getSumOfForwardingChargesOfCancelledItems())); setCellValue(row, 36,
         * Math.round(daySaleShipDateWiseDto.getSumOfNetReceivableOfCancelledItems())); } wb.write(out); out.close();
         * return file;
         */

        return null;
    }

    public File generateCODConfirmationReportXls(String xslFilePath, Date startDate, Date endDate) throws Exception {

        /*
         * File file = new File(xslFilePath); FileOutputStream out = new FileOutputStream(file); Workbook wb = new
         * HSSFWorkbook(); CellStyle style = wb.createCellStyle(); CellStyle style_highlight = wb.createCellStyle();
         * style_highlight.setFillForegroundColor(IndexedColors.ORANGE.getIndex());
         * style_highlight.setFillPattern(CellStyle.SOLID_FOREGROUND); Font font = wb.createFont();
         * font.setFontHeightInPoints((short) 11); font.setColor(Font.COLOR_NORMAL);
         * font.setBoldweight(Font.BOLDWEIGHT_BOLD); style.setFont(font); Sheet sheet1 =
         * wb.createSheet("CODUnConfirmedOrderReport"); Row row1 = sheet1.createRow(0); row1.setHeightInPoints((short)
         * 30); int totalColumnNoInSheet1 = 7; Cell cell; for (int columnNo = 0; columnNo < totalColumnNoInSheet1;
         * columnNo++) { cell = row1.createCell(columnNo); cell.setCellStyle(style); } setCellValue(row1, 0,
         * ORDER_DATE); setCellValue(row1, 1, DAY_OF_WEEK); setCellValue(row1, 2, ORDER_ID); setCellValue(row1, 3,
         * EMAIL); setCellValue(row1, 4, NAME); setCellValue(row1, 5, TOTAL_TIME_ELAPSED); setCellValue(row1, 6,
         * ACTUAL_TIME_ELAPSED); //setCellValue(row1, 6, SUM_HK_PRICE_POST_DISCOUNT); SimpleDateFormat sdf_day_of_week =
         * new SimpleDateFormat("E"); SimpleDateFormat sdf_hour = new SimpleDateFormat("H"); SimpleDateFormat
         * sdf_complete_date_format = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy"); int rowCounter = 1; List<CODConfirmationDto>
         * unConfirmedCODList = generateUnConfirmedOrderList(startDate, endDate);
         * System.out.println("unConfirmedCODList: " + unConfirmedCODList.size()); for (CODConfirmationDto
         * codConfirmationDto : unConfirmedCODList) { rowCounter++; row1 = sheet1.createRow(rowCounter); for (int
         * columnNo = 0; columnNo < totalColumnNoInSheet1; columnNo++) { cell = row1.createCell(columnNo); if
         * (codConfirmationDto.isHighlightOrder()) {// computeActualTimeTakenToConfirm(codConfirmationDto,
         * permissibleHourCount, dayOfWeekExcluded, dayBeginHour, dayEndHour)){ cell.setCellStyle(style_highlight); } }
         * setCellValue(row1, 0, (codConfirmationDto.getOrderPlacedDate()).toString()); setCellValue(row1, 1,
         * sdf_day_of_week.format(codConfirmationDto.getOrderPlacedDate())); setCellValue(row1, 2,
         * codConfirmationDto.getOrder().getId()); setCellValue(row1, 3, codConfirmationDto.getUserEmail());
         * setCellValue(row1, 4, codConfirmationDto.getUserName()); setCellValue(row1, 5,
         * codConfirmationDto.getHoursTakenToConfirm() + " hrs " + codConfirmationDto.getMinutesTakenToConfirm() + "
         * min"); setCellValue(row1, 6, codConfirmationDto.getCalculatedHoursTakenToConfirm() + " hrs " +
         * codConfirmationDto.getCalculatedMinutesTakenToConfirm() + " min"); } Sheet sheet2 =
         * wb.createSheet("CODConfirmedOrderReport"); Row row2 = sheet2.createRow(0); row2.setHeightInPoints((short)
         * 30); int totalColumnNoInSheet2 = 9; for (int columnNo = 0; columnNo < totalColumnNoInSheet2; columnNo++) {
         * cell = row2.createCell(columnNo); cell.setCellStyle(style); } setCellValue(row2, 0, ORDER_PLACED_DATE);
         * setCellValue(row2, 1, DAY_OF_WEEK); setCellValue(row2, 2, ORDER_CONFIRMED_DATE); setCellValue(row2, 3,
         * DAY_OF_WEEK); setCellValue(row2, 4, ORDER_ID); setCellValue(row2, 5, EMAIL); setCellValue(row2, 6, NAME);
         * setCellValue(row2, 7, TOTAL_TIME_TAKEN_TO_CONFIRM); setCellValue(row2, 8, ACTUAL_TIME_TAKEN_TO_CONFIRM);
         * //setCellValue(row1, 6, SUM_HK_PRICE_POST_DISCOUNT); rowCounter = 1; List<CODConfirmationDto>
         * confirmedCODList = generateConfirmedOrderList(startDate, endDate); System.out.println("confirmedCODList: " +
         * confirmedCODList.size()); for (CODConfirmationDto codConfirmationDto : confirmedCODList) { rowCounter++; row2 =
         * sheet2.createRow(rowCounter); for (int columnNo = 0; columnNo < totalColumnNoInSheet2; columnNo++) { cell =
         * row2.createCell(columnNo); if (codConfirmationDto.isHighlightOrder()) { cell.setCellStyle(style_highlight); } }
         * setCellValue(row2, 0, codConfirmationDto.getOrderPlacedDate().toString()); setCellValue(row2, 1,
         * sdf_day_of_week.format(codConfirmationDto.getOrderPlacedDate())); setCellValue(row2, 2,
         * sdf.format(codConfirmationDto.getOrderConfirmationDate())); setCellValue(row2, 3,
         * sdf_day_of_week.format(codConfirmationDto.getOrderConfirmationDate())); setCellValue(row2, 4,
         * codConfirmationDto.getOrder().getId()); setCellValue(row2, 5, codConfirmationDto.getUserEmail());
         * setCellValue(row2, 6, codConfirmationDto.getUserName()); setCellValue(row2, 7,
         * codConfirmationDto.getHoursTakenToConfirm() + " hrs " + codConfirmationDto.getMinutesTakenToConfirm() + "
         * min"); setCellValue(row2, 8, codConfirmationDto.getCalculatedHoursTakenToConfirm() + " hrs " +
         * codConfirmationDto.getCalculatedMinutesTakenToConfirm() + " min"); } wb.write(out); out.close(); return file;
         */

        return null;
    }

    public File generateReconciliationReport(String xslFilePath, Date startDate, Date endDate, OrderStatus orderStatus, PaymentMode paymentMode, Courier codMode,
                                             ReconciliationStatus reconciliationStatus) throws Exception {
        /*
         * File file = new File(xslFilePath); FileOutputStream out = new FileOutputStream(file); Workbook wb = new
         * HSSFWorkbook(); CellStyle style = wb.createCellStyle(); Font font = wb.createFont();
         * font.setFontHeightInPoints((short) 11); font.setColor(Font.COLOR_NORMAL);
         * font.setBoldweight(Font.BOLDWEIGHT_BOLD); style.setFont(font); Sheet sheet1 =
         * wb.createSheet("Reconciliation-by-date"); Row row = sheet1.createRow(0); row.setHeightInPoints((short) 30);
         * int totalColumnNo = 20; Cell cell; for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) { cell =
         * row.createCell(columnNo); cell.setCellStyle(style); } setCellValue(row, 0, INVOICE_ID); setCellValue(row, 1,
         * ACCOUNTING_INVOICE_ID); setCellValue(row, 2, ORDER_DATE); setCellValue(row, 3, NAME); setCellValue(row, 4,
         * CITY); setCellValue(row, 5, PAYMENT_MODE); setCellValue(row, 6, TOTAL_AMOUNT); setCellValue(row, 7,
         * SHIPPING_CHARGE); setCellValue(row, 8, COD_CHARGES); setCellValue(row, 9, AMOUNT); setCellValue(row, 10,
         * COURIER); setCellValue(row, 11, AWB); setCellValue(row, 12, SHIPMENT_DATE); setCellValue(row, 13,
         * DELIVERY_DATE); setCellValue(row, 14, RECONCILED); setCellValue(row, 15, ORDER_STATUS); //change
         * setCellValue(row, 16, BOX_WEIGHT); setCellValue(row, 17, BOX_SIZE); int rowCounter = 1; List<Order>
         * orderList = orderDaoProvider.get().findReconciledOrdersForTimeFrame(startDate, endDate, orderStatus,
         * paymentMode, codMode, reconciliationStatus); for (Order order : orderList) { row =
         * sheet1.createRow(rowCounter); for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) { cell =
         * row.createCell(columnNo); } setCellValue(row, 0, order.getGatewayOrderId()); if
         * (order.getAccountingInvoiceNumber() != null) { setCellValue(row, 1,
         * order.getAccountingInvoiceNumber().toString()); } setCellValue(row, 2, order.getPayment().getPaymentDate());
         * setCellValue(row, 3, order.getAddress().getName()); setCellValue(row, 4, order.getAddress().getCity());
         * setCellValue(row, 5, order.getPayment().getPaymentMode().getName()); setCellValue(row, 6,
         * order.getPayment().getAmount()); Double amount = order.getPayment().getAmount(); List<LineItem> shippingLi =
         * order.getLineItems(EnumLineItemType.Shipping); if (shippingLi != null && !shippingLi.isEmpty()) { Double
         * shipping = 0.0; shipping = shippingLi.get(0).getHkPrice() - shippingLi.get(0).getDiscountOnHkPrice();
         * setCellValue(row, 7, shipping); amount -= shipping; } List<LineItem> codLi =
         * order.getLineItems(EnumLineItemType.CodCharges); if (codLi != null && !codLi.isEmpty()) { setCellValue(row,
         * 8, codLi.get(0).getHkPrice()); amount -= codLi.get(0).getHkPrice(); } setCellValue(row, 9, amount); LineItem
         * lineItem = order.getProductLineItems().get(0); if (lineItem.getCourier() != null) { setCellValue(row, 10,
         * lineItem.getCourier().getName()); String awb = ""; String oldTrackingId = null; for (LineItem li :
         * order.getProductLineItems()) { if (li.getTrackingId() != null) { if (oldTrackingId == null ||
         * !oldTrackingId.equals(li.getTrackingId())) { if (oldTrackingId != null &&
         * !oldTrackingId.equals(li.getTrackingId())) { awb += ","; } awb += li.getTrackingId(); oldTrackingId =
         * li.getTrackingId(); } } } setCellValue(row, 11, awb); } setCellValue(row, 12, lineItem.getShipDate());
         * setCellValue(row, 13, lineItem.getDeliveryDate()); setCellValue(row, 14,
         * order.getReconciliationStatus().getName()); setCellValue(row, 15, order.getOrderStatus().getName());
         * setCellValue(row, 16, lineItem.getBoxWeight()); setCellValue(row, 17, lineItem.getBoxSize().getName());
         * rowCounter++; } wb.write(out); out.close(); return file;
         */

        return null;
    }

    public List<CODConfirmationDto> generateUnConfirmedOrderList(Date startDate, Date endDate) {

        List<CODConfirmationDto> codUnConfirmationDtos = getReportOrderService().findCODUnConfirmedOrderReport(startDate, endDate);
        for (CODConfirmationDto codConfirmationDto : codUnConfirmationDtos) {
            computeActualTimeTakenToConfirm(codConfirmationDto, ReportConstants.permissibleHourCount, ReportConstants.dayBeginHour, ReportConstants.dayEndHour,
                    ReportConstants.COD_UNCONFIRMED);
        }
        return codUnConfirmationDtos;

    }

    public List<CODConfirmationDto> generateConfirmedOrderList(Date startDate, Date endDate) {

        /*
         * List<CODConfirmationDto> codConfirmationDtos = orderDaoProvider.get().findCODConfirmedOrderReport(startDate,
         * endDate); for (CODConfirmationDto codConfirmationDto : codConfirmationDtos) {
         * computeActualTimeTakenToConfirm(codConfirmationDto, permissibleHourCount, dayBeginHour, dayEndHour,
         * COD_CONFIRMED); } return codConfirmationDtos;
         */

        return null;
    }

    private void computeActualTimeTakenToConfirm(CODConfirmationDto codConfirmationDto, int permissibleHourCount, int dayBeginHour, int dayEndHour, String isConfirmed) {

        /*
         * Calendar now = Calendar.getInstance(); Calendar orderPlaceDate = Calendar.getInstance(); Calendar
         * orderConfirmedDate = Calendar.getInstance(); Calendar now_ConfirmDate; if
         * (codConfirmationDto.getOrderPlacedDate() != null) {
         * orderPlaceDate.setTime(codConfirmationDto.getOrderPlacedDate()); } if
         * (codConfirmationDto.getOrderConfirmationDate() != null) {
         * orderConfirmedDate.setTime(codConfirmationDto.getOrderConfirmationDate()); } // now_ConfirmDate leaves us
         * with a single date to take care of - it either now or confirmed date depending on order status. if
         * (isConfirmed.equals(COD_UNCONFIRMED)) { now_ConfirmDate = now; } else { now_ConfirmDate = orderConfirmedDate; }
         * int days_between_order_confirm = 0; int final_seconds_to_be_waived_off = 0; int seconds_to_be_added = 0; int
         * actual_sec_taken_to_confirm = 0; days_between_order_confirm = (int) ((now_ConfirmDate.getTimeInMillis() -
         * orderPlaceDate.getTimeInMillis()) / (1000 * 60 * 60 * 24)); orderPlaceDate.add(Calendar.DAY_OF_MONTH,
         * days_between_order_confirm); seconds_to_be_added = (dayEndHour - dayBeginHour) * days_between_order_confirm *
         * 3600; int order_placed_date = orderPlaceDate.get(Calendar.DATE); int order_placed_month =
         * orderPlaceDate.get(Calendar.MONTH); int order_placed_year = orderPlaceDate.get(Calendar.YEAR); int
         * order_placed_hour = orderPlaceDate.get(Calendar.HOUR_OF_DAY); int order_placed_minute =
         * orderPlaceDate.get(Calendar.MINUTE); int order_now_confirm_date = now_ConfirmDate.get(Calendar.DATE); int
         * order_now_confirm_month = now_ConfirmDate.get(Calendar.MONTH); int order_now_confirm_year =
         * now_ConfirmDate.get(Calendar.YEAR); int order_now_confirm_hour = now_ConfirmDate.get(Calendar.HOUR_OF_DAY);
         * int order_now_confirm_minute = now_ConfirmDate.get(Calendar.MINUTE); //for same day if (order_placed_date ==
         * order_now_confirm_date && order_placed_month == order_now_confirm_month && order_placed_year ==
         * order_now_confirm_year) { if (order_placed_hour >= dayBeginHour && order_placed_hour < dayEndHour) {
         * actual_sec_taken_to_confirm += (dayEndHour - order_placed_hour) * 3600 - order_placed_minute * 60; } else if
         * (order_placed_hour < dayBeginHour) { actual_sec_taken_to_confirm += (dayEndHour - dayBeginHour) * 3600; }
         * else if (order_placed_hour >= dayEndHour) { actual_sec_taken_to_confirm += 0; } if (order_now_confirm_hour >=
         * dayBeginHour && order_now_confirm_hour < dayEndHour) { actual_sec_taken_to_confirm -= (dayEndHour -
         * order_now_confirm_hour) * 3600 - order_now_confirm_minute * 60; } else if (order_now_confirm_hour <
         * dayBeginHour) { actual_sec_taken_to_confirm -= (dayEndHour - dayBeginHour) * 3600; } else if
         * (order_now_confirm_hour >= dayEndHour) { actual_sec_taken_to_confirm -= 0; } } //for different days but
         * within 24hrs else { if (order_placed_hour >= dayBeginHour && order_placed_hour < dayEndHour) {
         * actual_sec_taken_to_confirm += (dayEndHour - order_placed_hour) * 3600 - order_placed_minute * 60; } else if
         * (order_placed_hour < dayBeginHour) { actual_sec_taken_to_confirm += (dayEndHour - dayBeginHour) * 3600; }
         * else if (order_placed_hour >= dayEndHour) { actual_sec_taken_to_confirm += 0; } if (order_now_confirm_hour >=
         * dayBeginHour && order_now_confirm_hour < dayEndHour) { actual_sec_taken_to_confirm += (order_now_confirm_hour -
         * dayBeginHour) * 3600 + order_now_confirm_minute * 60; } else if (order_now_confirm_hour < dayBeginHour) {
         * actual_sec_taken_to_confirm += 0; } else if (order_now_confirm_hour >= dayEndHour) {
         * actual_sec_taken_to_confirm += (dayEndHour - dayBeginHour) * 3600; } } actual_sec_taken_to_confirm +=
         * seconds_to_be_added; codConfirmationDto.setCalculatedTimeTakenToConfirm((long) actual_sec_taken_to_confirm);
         */

    }

    private void setCellValue(Row row, int column, Double cellValue) {
        if (cellValue != null) {
            Cell cell = row.getCell(column);
            cell.setCellValue(cellValue);
        }
    }

    private void setCellValue(Row row, int column, Long cellValue) {
        if (cellValue != null) {
            Cell cell = row.getCell(column);
            cell.setCellValue(cellValue);
        }
    }

    private void setCellValue(Row row, int column, String cellValue) {
        if (cellValue == null)
            cellValue = "";
        Cell cell = row.getCell(column);
        cell.setCellValue(cellValue);
    }

    private void setCellValue(Row row, int column, Date cellValue) {
        if (cellValue != null) {
            Cell cell = row.getCell(column);
            cell.setCellValue(cellValue);
        }
    }

    private void setCellValue(Row row, int column, Object cellValue) {
        if (cellValue != null) {
            Cell cell = row.getCell(column);
            if (cellValue instanceof Double) {
                cell.setCellValue((Double) cellValue);
            } else if (cellValue instanceof Date) {
                cell.setCellValue((Date) cellValue);
            } else if (cellValue instanceof Long) {
                cell.setCellValue((Long) cellValue);
            } else
                cell.setCellValue(cellValue.toString());
        }
    }

    /**
     * The method returns an excel file for delivery dates for a given courier
     */

    public File generateCourierReportXls(String xlsFilePath, Date startDate, Date endDate, Courier courier) throws Exception {

        HkXlsWriter xlsWriter = new HkXlsWriter();
        Calendar ship_date_cal = Calendar.getInstance();
        Calendar delivery_date_cal = Calendar.getInstance();
        xlsWriter.addHeader("SHIPPING_ORDER_ID", "SHIPPING_ORDER_ID");
        xlsWriter.addHeader("TRACKING_ID", "TRACKING_ID");
        xlsWriter.addHeader("COURIER", "COURIER");
        xlsWriter.addHeader("WAREHOUSE", "WAREHOUSE");
        xlsWriter.addHeader("SHIPMENT_DATE", "SHIPMENT_DATE");
        xlsWriter.addHeader("DELIVERY_DATE", "DELIVERY_DATE");
        xlsWriter.addHeader("CITY", "CITY");
        xlsWriter.addHeader("PIN_CODE", "PIN_CODE");
        xlsWriter.addHeader("DAYS_TO_DELIVER", "DAYS_TO_DELIVER");
        List<ShippingOrder> shippingOrderList = getReportShippingOrderService().getDeliveredSOForCourierByDate(startDate, endDate, courier.getId());
        int rowCounter = 0;
        long days_to_deliver = 0;
        for (ShippingOrder shippingOrder : shippingOrderList) {
            Order order = shippingOrder.getBaseOrder();
            Shipment shipment = shippingOrder.getShipment();
            days_to_deliver = 0;
            rowCounter++;
            xlsWriter.addCell(rowCounter, shippingOrder.getId());
            if (shipment != null && shipment.getAwb() != null) {
                xlsWriter.addCell(rowCounter, shipment.getAwb().getAwbNumber());
            }
            xlsWriter.addCell(rowCounter, shipment.getAwb().getCourier().getName());
            xlsWriter.addCell(rowCounter, shippingOrder.getWarehouse().getName());
            xlsWriter.addCell(rowCounter, shipment.getShipDate().toString());
            xlsWriter.addCell(rowCounter, shipment.getDeliveryDate().toString());
            xlsWriter.addCell(rowCounter, order.getAddress().getCity());
            xlsWriter.addCell(rowCounter, order.getAddress().getPincode().getPincode());
            ship_date_cal.setTime(shipment.getShipDate());
            delivery_date_cal.setTime(shipment.getDeliveryDate());
            ship_date_cal.set(Calendar.HOUR_OF_DAY, 0);
            ship_date_cal.set(Calendar.MINUTE, 0);
            ship_date_cal.set(Calendar.SECOND, 0);
            ship_date_cal.set(Calendar.MILLISECOND, 0);
            delivery_date_cal.set(Calendar.HOUR_OF_DAY, 0);
            delivery_date_cal.set(Calendar.MINUTE, 0);
            delivery_date_cal.set(Calendar.SECOND, 0);
            delivery_date_cal.set(Calendar.MILLISECOND, 0);
            days_to_deliver = ((delivery_date_cal.getTimeInMillis() - ship_date_cal.getTimeInMillis()) / (1000 * 60 * 60 * 24));
            xlsWriter.addCell(rowCounter, days_to_deliver);
        }
        return xlsWriter.writeData(xlsFilePath);
    }

    public Map<String, CategoriesOrderReportDto> generateDailyCategoryPerformaceReportUI() {

        List<Category> applicableCategories = new ArrayList<Category>();

        if (applicableCategories.size() == 0) {
            applicableCategories = getCategoryService().getPrimaryCategories();
        }

        Date currentDate;
        Date yesterdayEndOfDayDate;
        Date yesterdayStartOfDayDate;
        Date currentStartDate;
        Date monthStartDate;
        Calendar currentDateCal = Calendar.getInstance();
        currentDate = currentDateCal.getTime();

        yesterdayStartOfDayDate = DateUtils.getStartOfPreviousDay(currentDate);
        yesterdayEndOfDayDate = DateUtils.getEndOfPreviousDay(currentDate);

        Calendar monthStartDateCal = Calendar.getInstance();
        Calendar yesterdayEndOfDayCal = Calendar.getInstance();
        yesterdayEndOfDayCal.setTime(yesterdayEndOfDayDate);

        currentStartDate = currentDateCal.getTime();
        currentDate = currentDateCal.getTime();
        monthStartDateCal.set(Calendar.DATE, 1);
        monthStartDateCal.set(Calendar.MONTH, yesterdayEndOfDayCal.get(Calendar.MONTH));
        monthStartDateCal.set(Calendar.YEAR, yesterdayEndOfDayCal.get(Calendar.YEAR));
        monthStartDateCal.set(Calendar.HOUR_OF_DAY, 0);
        monthStartDateCal.set(Calendar.MINUTE, 0);
        monthStartDateCal.set(Calendar.SECOND, 0);
        monthStartDateCal.set(Calendar.MILLISECOND, 0);

        monthStartDate = monthStartDateCal.getTime();

        final Integer numberOfDaysInMonth = yesterdayEndOfDayCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        final Map<String, Long> targetDailyMrpSalesMap = new HashMap<String, Long>() {
            {
                put(CategoryConstants.BABY, CategoryConstants.BABY_TARGET_SALES / numberOfDaysInMonth);
                put(CategoryConstants.BEAUTY, CategoryConstants.BEAUTY_TARGET_SALES / numberOfDaysInMonth);
                put(CategoryConstants.DIABETES, CategoryConstants.DIABETES_TARGET_SALES / numberOfDaysInMonth);
                put(CategoryConstants.EYE, CategoryConstants.EYE_TARGET_SALES / numberOfDaysInMonth);
                put(CategoryConstants.HEALTH_DEVICES, CategoryConstants.HEALTH_DEVICES_TARGET_SALES / numberOfDaysInMonth);
                put(CategoryConstants.NUTRITION, CategoryConstants.NUTRITION_TARGET_SALES / numberOfDaysInMonth);
                put(CategoryConstants.PERSONAL_CARE, CategoryConstants.PERSONAL_CARE_TARGET_SALES / numberOfDaysInMonth);
                put(CategoryConstants.SERVICES, CategoryConstants.SERVICES_TARGET_SALES / numberOfDaysInMonth);
                put(CategoryConstants.SPORTS, CategoryConstants.SPORTS_TARGET_SALES / numberOfDaysInMonth);
            }
        };

        CategoriesOrderReportDto dailyCategoriesOrderReportDto = getCategoriesOrderReportDto(yesterdayStartOfDayDate, yesterdayEndOfDayDate, applicableCategories);
        CategoriesOrderReportDto monthlyCategoriesOrderReportDto = getCategoriesOrderReportDto(monthStartDate, yesterdayEndOfDayDate, applicableCategories);
        Map<String, CategoriesOrderReportDto> categoriesOrderReportDtosMap = new HashMap<String, CategoriesOrderReportDto>();
        categoriesOrderReportDtosMap.put(CategoryConstants.YESTERDAY_CATEGORIERS_ORDER_REPORT_DTO, dailyCategoriesOrderReportDto);
        categoriesOrderReportDtosMap.put(CategoryConstants.MONTHLY_CATEGORIES_ORDER_REPORT_DTO, monthlyCategoriesOrderReportDto);

        return categoriesOrderReportDtosMap;

    }

    public Map<String, CategoriesOrderReportDto> generateSixHourlyCategoryPerformaceReportUI() {

        List<Category> applicableCategories = new ArrayList<Category>();

        if (applicableCategories.size() == 0) {
            applicableCategories = getCategoryService().getPrimaryCategories();
        }

        Calendar currentDateCal = Calendar.getInstance();
        Date currentDate = currentDateCal.getTime();

        Date startOfDay;
        Date startOfPreviousSixHours;
        Date endOfPreviousSixHours;

        startOfDay = DateUtils.getStartOfDay(currentDate);
        Calendar startSixHourCal = Calendar.getInstance();
        Calendar endSixHourCal = Calendar.getInstance();
        endSixHourCal.setTime(currentDate);

        endSixHourCal.set(Calendar.MINUTE, 0);
        endSixHourCal.set(Calendar.SECOND, 0);
        endSixHourCal.set(Calendar.MILLISECOND, 0);

        if (endSixHourCal.get(Calendar.HOUR_OF_DAY) == 0) {
            endSixHourCal.setTime(DateUtils.getEndOfPreviousDay(currentDate));
            startOfDay = DateUtils.getStartOfPreviousDay(currentDate);
        }
        startSixHourCal.set(Calendar.HOUR, (currentDateCal.get(Calendar.HOUR) - 6));
        startSixHourCal.set(Calendar.MINUTE, 0);
        startSixHourCal.set(Calendar.SECOND, 0);
        startSixHourCal.set(Calendar.MILLISECOND, 0);
        startOfPreviousSixHours = startSixHourCal.getTime();

        startOfPreviousSixHours = startSixHourCal.getTime();
        endOfPreviousSixHours = endSixHourCal.getTime();

        CategoriesOrderReportDto sixHourlyCategoriesReportDto = getCategoriesOrderReportDto(startOfPreviousSixHours, endOfPreviousSixHours, applicableCategories);
        CategoriesOrderReportDto dailyCategoriesOrderReportDto = getCategoriesOrderReportDto(startOfDay, endOfPreviousSixHours, applicableCategories);
        Map<String, CategoriesOrderReportDto> sixHourlycategoriesOrderReportDtosMap = new HashMap<String, CategoriesOrderReportDto>();
        sixHourlycategoriesOrderReportDtosMap.put(CategoryConstants.SIX_HOUR_CATEGORIES_ORDER_REPORT_DTO, sixHourlyCategoriesReportDto);
        sixHourlycategoriesOrderReportDtosMap.put(CategoryConstants.DAILY_CATEGORIES_ORDER_REPORT_DTO, dailyCategoriesOrderReportDto);

        return sixHourlycategoriesOrderReportDtosMap;
    }

    private CategoriesOrderReportDto getCategoriesOrderReportDto(Date startDate, Date endDate, List<Category> applicableCategories) {

        /*
         * CategoriesOrderReportDto categoriesOrderReportDto = new CategoriesOrderReportDto(); Double
         * orderLevelDiscounts = 0D; Set<Order> distinctOrderBucket = new HashSet<Order>(); Calendar startDateCal =
         * Calendar.getInstance(); startDateCal.setTime(startDate); List<Order> orderList = new ArrayList<Order>();
         * List<CategoryPerformanceDto> categoryPerformanceList = new ArrayList<CategoryPerformanceDto>(); Integer
         * numberOfDaysInMonth = startDateCal.getActualMaximum(Calendar.DAY_OF_MONTH); Integer totalOrders = 0; Integer
         * totalDistinctOrders = 0; Integer daysBtwMonthStartDateCurrentDate = 0; Long totalSumOfMrp = 0L; Long
         * totalSumOfProjectedMrp = 0L; Long totalSumOfTargetMrp = 0L; daysBtwMonthStartDateCurrentDate = (int)
         * ((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24)) + 1; //Generate category wise report for
         * today for (Category applicableCategory : applicableCategories) { orderList =
         * orderDaoProvider.get().findOrderListForTimeFrameForCategory(startDate, endDate, applicableCategory); int
         * distinctOrders = 0, mixedOrders = 0; for (Order order : orderList) { if (order.getProductLineItems() != null &&
         * order.getProductLineItems().size() > 0) { if (order.getProductLineItems().size() == 1 &&
         * !distinctOrderBucket.contains(order)) { distinctOrders++; totalDistinctOrders++;
         * distinctOrderBucket.add(order); } else if (order.getProductLineItems().size() > 1) { Category oldCategory =
         * null; boolean isMixOrder = false; for (LineItem lineItem : order.getProductLineItems()) { //Category category =
         * lineItem.getProductVariant().getProduct().getPrimaryCategory(); Category category =
         * productManagerProvider.get().getTopLevelCategory(lineItem.getProductVariant().getProduct()); // on 25th oct
         * data, there is a particular product lineitem whose cat is null, have to check how, hence putting null check
         * if (category != null && oldCategory != null && !category.equals(oldCategory)) { mixedOrders++; isMixOrder =
         * true; break; } oldCategory = category; } if (!isMixOrder && !distinctOrderBucket.contains(order)) {
         * distinctOrders++; totalDistinctOrders++; distinctOrderBucket.add(order); } } } } CategoryPerformanceDto
         * categoryPerformanceDto = orderDaoProvider.get().findCategoryWiseSalesForTimeFramePerCategory(startDate,
         * endDate, applicableCategory); categoryPerformanceDto.setCategory(applicableCategory);
         * categoryPerformanceDto.setDistinctOrders(distinctOrders); categoryPerformanceDto.setMixedOrders(mixedOrders);
         * categoryPerformanceDto.setTotalOrders(distinctOrders + mixedOrders); if
         * (categoryPerformanceDto.getSumOfHkPricePostAllDiscounts() != null && orderLevelDiscounts != null) {
         * categoryPerformanceDto.setSumOfHkPricePostAllDiscounts(categoryPerformanceDto.getSumOfHkPrice() -
         * categoryPerformanceDto.getSumOfHkPricePostAllDiscounts() + orderLevelDiscounts); } if
         * (daysBtwMonthStartDateCurrentDate != 0 && categoryPerformanceDto.getSumOfHkPrice() != null) {
         * categoryPerformanceDto.setAvgOfHkPrice(categoryPerformanceDto.getSumOfHkPrice() /
         * daysBtwMonthStartDateCurrentDate); } if (categoryPerformanceDto.getSumOfMrp() != null &&
         * daysBtwMonthStartDateCurrentDate != 0) {
         * categoryPerformanceDto.setAvgOfMrp(categoryPerformanceDto.getSumOfMrp() / daysBtwMonthStartDateCurrentDate); }
         * if (categoryPerformanceDto.getSumOfHkPricePostAllDiscounts() != null && daysBtwMonthStartDateCurrentDate !=
         * 0) {
         * categoryPerformanceDto.setAvgOfHkPricePostAllDiscounts(categoryPerformanceDto.getSumOfHkPricePostAllDiscounts() /
         * daysBtwMonthStartDateCurrentDate); } if (categoryPerformanceDto.getTotalOrders() != null &&
         * daysBtwMonthStartDateCurrentDate != 0) {
         * categoryPerformanceDto.setAvgOfTotalOrders(categoryPerformanceDto.getTotalOrders().doubleValue() /
         * daysBtwMonthStartDateCurrentDate); } if (numberOfDaysInMonth != null && categoryPerformanceDto.getAvgOfMrp() !=
         * null) { categoryPerformanceDto.setProjectedMrp(categoryPerformanceDto.getAvgOfMrp() * numberOfDaysInMonth); }
         * totalSumOfMrp += categoryPerformanceDto.getSumOfMrp().longValue(); totalSumOfProjectedMrp +=
         * categoryPerformanceDto.getProjectedMrp().longValue(); totalSumOfTargetMrp +=
         * CategoryConstants.targetMrpSalesMap.get(applicableCategory.getName());
         * categoryPerformanceList.add(categoryPerformanceDto); } totalOrders =
         * orderDaoProvider.get().findOrdersForTimeFrame(startDate, endDate, applicableCategories).size();
         * categoriesOrderReportDto.setCategoryPerformanceDtoList(categoryPerformanceList);
         * categoriesOrderReportDto.setTotalDistinctOrders(totalDistinctOrders);
         * categoriesOrderReportDto.setTotalOrders(totalOrders); categoriesOrderReportDto.setTotalMixedOrder(totalOrders -
         * totalDistinctOrders); categoriesOrderReportDto.setTotalSumOfMrp(totalSumOfMrp);
         * categoriesOrderReportDto.setTotalSumOfProjectedMrp(totalSumOfProjectedMrp);
         * categoriesOrderReportDto.setTotalSumOfTargetMrp(totalSumOfTargetMrp); return categoriesOrderReportDto;
         */

        return null;

    }

    public List<DaySaleShipDateWiseDto> generateSalesByDateReportForShippedProducts(Date startDate, Date endDate) {

        /*
         * List<DaySaleDto> daySaleListDatewiseOrderStatuswise = new ArrayList<DaySaleDto>(); List<OrderStatus>
         * applicableOrderStatus = new ArrayList<OrderStatus>();
         * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Shipped.getId()));
         * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Delivered.getId()));
         * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.Cancelled.getId()));
         * applicableOrderStatus.add(orderStatusDao.find(EnumOrderStatus.RETURNED.getId()));
         * daySaleListDatewiseOrderStatuswise = orderDaoProvider.get().findSaleForTimeFrameForShippedProducts(startDate,
         * endDate, applicableOrderStatus); for (DaySaleDto daySaleDto : daySaleListDatewiseOrderStatuswise) { Double
         * orderLevelCouponAndRewardPoint =
         * orderDaoProvider.get().getNetDiscountViaOrderLevelCouponAndRewardPointsPerStatus(daySaleDto.getOrderShipDate(),
         * daySaleDto.getOrderStatus()); if (orderLevelCouponAndRewardPoint != null) {
         * daySaleDto.setSumOfAllDiscounts(daySaleDto.getSumOfAllDiscounts() + orderLevelCouponAndRewardPoint); } Double
         * netForwardingCharges = orderDaoProvider.get().getNetForwardingChargesPerStatus(daySaleDto.getOrderShipDate(),
         * daySaleDto.getOrderStatus()); if (netForwardingCharges != null) {
         * daySaleDto.setSumOfForwardingCharges(netForwardingCharges); } } List<DaySaleShipDateWiseDto>
         * daySaleShipDateWiseDtoList = generateDaySaleShipDateWiseDto(daySaleListDatewiseOrderStatuswise); return
         * daySaleShipDateWiseDtoList;
         */

        return null;
    }

    private List<DaySaleShipDateWiseDto> generateDaySaleShipDateWiseDto(List<DaySaleDto> daySaleListDatewiseOrderStatuswise) {

        List<DaySaleShipDateWiseDto> daySaleShipDateWiseDtoList = new ArrayList<DaySaleShipDateWiseDto>();
        if (daySaleListDatewiseOrderStatuswise == null || daySaleListDatewiseOrderStatuswise.isEmpty()) {
            return null;
        }
        Date prevOrderShipDate = null;
        DaySaleShipDateWiseDto daySaleShipDateWiseDto = null;
        for (DaySaleDto daySaleDto : daySaleListDatewiseOrderStatuswise) {

            if (!daySaleDto.getOrderShipDate().equals(prevOrderShipDate)) {
                daySaleShipDateWiseDto = new DaySaleShipDateWiseDto();
                daySaleShipDateWiseDto.setOrderShipDate(daySaleDto.getOrderShipDate());
                daySaleShipDateWiseDtoList.add(daySaleShipDateWiseDto);
            }
            if (daySaleShipDateWiseDto != null) {
                if (daySaleDto.getOrderStatus().getId().equals(EnumOrderStatus.Shipped.getId())) {
                    daySaleShipDateWiseDto.setTxnCountOfShippedItems(daySaleDto.getTxnCount());
                    daySaleShipDateWiseDto.setSkuCountOfShippedItems(daySaleDto.getSkuCount());
                    daySaleShipDateWiseDto.setSumOfMrpOfShippedItems(daySaleDto.getSumOfMrp());
                    daySaleShipDateWiseDto.setSumOfHkPriceOfShippedItems(daySaleDto.getSumOfHkPrice());
                    daySaleShipDateWiseDto.setSumOfAllDiscountsOfShippedItems(daySaleDto.getSumOfAllDiscounts());
                    daySaleShipDateWiseDto.setSumOfHkPricePostAllDiscountsOfShippedItems(daySaleDto.getSumOfHkPrice() - daySaleDto.getSumOfAllDiscounts());
                    daySaleShipDateWiseDto.setSumOfForwardingChargesOfShippedItems(daySaleDto.getSumOfForwardingCharges());
                    daySaleShipDateWiseDto.setSumOfNetReceivableOfShippedItems(daySaleDto.getSumOfHkPrice() - daySaleDto.getSumOfAllDiscounts()
                            + daySaleDto.getSumOfForwardingCharges());
                }
                if (daySaleDto.getOrderStatus().getId().equals(EnumOrderStatus.Delivered.getId())) {
                    daySaleShipDateWiseDto.setTxnCountOfDeliveredItems(daySaleDto.getTxnCount());
                    daySaleShipDateWiseDto.setSkuCountOfDeliveredItems(daySaleDto.getSkuCount());
                    daySaleShipDateWiseDto.setSumOfMrpOfDeliveredItems(daySaleDto.getSumOfMrp());
                    daySaleShipDateWiseDto.setSumOfHkPriceOfDeliveredItems(daySaleDto.getSumOfHkPrice());
                    daySaleShipDateWiseDto.setSumOfAllDiscountsOfDeliveredItems(daySaleDto.getSumOfAllDiscounts());
                    daySaleShipDateWiseDto.setSumOfHkPricePostAllDiscountsOfDeliveredItems(daySaleDto.getSumOfHkPrice() - daySaleDto.getSumOfAllDiscounts());
                    daySaleShipDateWiseDto.setSumOfForwardingChargesOfDeliveredItems(daySaleDto.getSumOfForwardingCharges());
                    daySaleShipDateWiseDto.setSumOfNetReceivableOfDeliveredItems(daySaleDto.getSumOfHkPrice() - daySaleDto.getSumOfAllDiscounts()
                            + daySaleDto.getSumOfForwardingCharges());

                }
                if (daySaleDto.getOrderStatus().getId().equals(EnumOrderStatus.RTO.getId())) {
                    daySaleShipDateWiseDto.setTxnCountOfReturnedItems(daySaleDto.getTxnCount());
                    daySaleShipDateWiseDto.setSkuCountOfReturnedItems(daySaleDto.getSkuCount());
                    daySaleShipDateWiseDto.setSumOfMrpOfReturnedItems(daySaleDto.getSumOfMrp());
                    daySaleShipDateWiseDto.setSumOfHkPriceOfReturnedItems(daySaleDto.getSumOfHkPrice());
                    daySaleShipDateWiseDto.setSumOfAllDiscountsOfReturnedItems(daySaleDto.getSumOfAllDiscounts());
                    daySaleShipDateWiseDto.setSumOfHkPricePostAllDiscountsOfReturnedItems(daySaleDto.getSumOfHkPrice() - daySaleDto.getSumOfAllDiscounts());
                    daySaleShipDateWiseDto.setSumOfForwardingChargesOfReturnedItems(daySaleDto.getSumOfForwardingCharges());
                    daySaleShipDateWiseDto.setSumOfNetReceivableOfReturnedItems(daySaleDto.getSumOfHkPrice() - daySaleDto.getSumOfAllDiscounts()
                            + daySaleDto.getSumOfForwardingCharges());

                }
                if (daySaleDto.getOrderStatus().getId().equals(EnumOrderStatus.Cancelled.getId())) {
                    daySaleShipDateWiseDto.setTxnCountOfCancelledItems(daySaleDto.getTxnCount());
                    daySaleShipDateWiseDto.setSkuCountOfCancelledItems(daySaleDto.getSkuCount());
                    daySaleShipDateWiseDto.setSumOfMrpOfCancelledItems(daySaleDto.getSumOfMrp());
                    daySaleShipDateWiseDto.setSumOfHkPriceOfCancelledItems(daySaleDto.getSumOfHkPrice());
                    daySaleShipDateWiseDto.setSumOfAllDiscountsOfCancelledItems(daySaleDto.getSumOfAllDiscounts());
                    daySaleShipDateWiseDto.setSumOfHkPricePostAllDiscountsOfCancelledItems(daySaleDto.getSumOfHkPrice() - daySaleDto.getSumOfAllDiscounts());
                    daySaleShipDateWiseDto.setSumOfForwardingChargesOfCancelledItems(daySaleDto.getSumOfForwardingCharges());
                    daySaleShipDateWiseDto.setSumOfNetReceivableOfCancelledItems(daySaleDto.getSumOfHkPrice() - daySaleDto.getSumOfAllDiscounts()
                            + daySaleDto.getSumOfForwardingCharges());

                }

                daySaleShipDateWiseDto.setTotalTxnCount(daySaleShipDateWiseDto.getTotalTxnCount() + daySaleDto.getTxnCount());
                daySaleShipDateWiseDto.setTotalSkuCount(daySaleShipDateWiseDto.getTotalSkuCount() + daySaleDto.getSkuCount());
                daySaleShipDateWiseDto.setTotalSumOfMrp(daySaleShipDateWiseDto.getTotalSumOfMrp() + daySaleDto.getSumOfMrp());
                daySaleShipDateWiseDto.setTotalSumOfHkPrice(daySaleShipDateWiseDto.getTotalSumOfHkPrice() + daySaleDto.getSumOfHkPrice());
                daySaleShipDateWiseDto.setTotalSumOfAllDiscounts(daySaleShipDateWiseDto.getTotalSumOfAllDiscounts() + daySaleDto.getSumOfAllDiscounts());
                daySaleShipDateWiseDto.setTotalSumOfForwardingCharges(daySaleShipDateWiseDto.getTotalSumOfForwardingCharges() + daySaleDto.getSumOfForwardingCharges());
                daySaleShipDateWiseDto.setTotalSumOfHkPricePostAllDiscounts(daySaleShipDateWiseDto.getTotalSumOfHkPricePostAllDiscounts() + daySaleDto.getSumOfHkPrice()
                        - daySaleDto.getSumOfAllDiscounts());
                daySaleShipDateWiseDto.setTotalSumOfNetReceivable(daySaleShipDateWiseDto.getTotalSumOfNetReceivable() + daySaleDto.getSumOfHkPrice()
                        - daySaleDto.getSumOfAllDiscounts() + daySaleDto.getSumOfForwardingCharges());

            }
            prevOrderShipDate = daySaleDto.getOrderShipDate();
        }
        return daySaleShipDateWiseDtoList;

    }

    public File generateNotifyMeList(String xlsFilePath, Date startDate, Date endDate, Product product, ProductVariant productVariant, Category primaryCategory, Boolean productInStock, Boolean productDeleted) throws Exception {

        // return null;
        File file = new File(xlsFilePath);
        FileOutputStream out = new FileOutputStream(file);
        Workbook wb = new HSSFWorkbook();

        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setColor(Font.COLOR_NORMAL);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(font);
        Sheet sheet1 = wb.createSheet("Notify-me-list");
        Row row = sheet1.createRow(0);
        row.setHeightInPoints((short) 30);

        int totalColumnNo = 7;

        Cell cell;
        for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
            cell = row.createCell(columnNo);
            cell.setCellStyle(style);
        }
        setCellValue(row, 0, ReportConstants.VARIANT_ID);
        setCellValue(row, 1, ReportConstants.VARIANT_DETAILS);
        setCellValue(row, 2, ReportConstants.PRIMARY_CATEGORY);
        setCellValue(row, 3, ReportConstants.NOTIFICATION_REQUEST_DATE);
        setCellValue(row, 4, ReportConstants.NAME);
        setCellValue(row, 5, ReportConstants.EMAIL);
        setCellValue(row, 6, ReportConstants.PHONE);

        row = sheet1.createRow(1);
        row.setHeightInPoints((short) 30);

        int rowCounter = 1;

        List<NotifyMe> notifyMeList = getNotifyMeDao().searchNotifyMe(startDate, endDate, product, productVariant, primaryCategory, productInStock, productDeleted);

        // System.out.println("notifyMeList: " + notifyMeList.size());
        for (NotifyMe notifyMe : notifyMeList) {
            rowCounter++;
            row = sheet1.createRow(rowCounter);
            for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
                cell = row.createCell(columnNo);
            }

            String primaryCategoryName = "";
            if (notifyMe.getProductVariant().getProduct().getPrimaryCategory() != null) {
                primaryCategoryName = notifyMe.getProductVariant().getProduct().getPrimaryCategory().getDisplayName();
            }
            String productDetails = notifyMe.getProductVariant().getProduct().getName() + "\n";
            if (notifyMe.getProductVariant() != null && notifyMe.getProductVariant().getVariantName() != null) {
                productDetails += notifyMe.getProductVariant().getVariantName() + "\n";
            }
            if (notifyMe.getProductVariant().getProductOptions() != null) {
                for (ProductOption productOption : notifyMe.getProductVariant().getProductOptions()) {
                    productDetails += productOption.getName() + " " + productOption.getValue() + "\n";
                }
            }
            setCellValue(row, 0, notifyMe.getProductVariant().getId());
            setCellValue(row, 1, productDetails);
            setCellValue(row, 2, primaryCategoryName);
            setCellValue(row, 3, notifyMe.getCreatedDate().toString());
            setCellValue(row, 4, notifyMe.getName());
            setCellValue(row, 5, notifyMe.getEmail());
            setCellValue(row, 6, notifyMe.getPhone());
        }
        wb.write(out);
        out.close();
        return file;
    }

    /*
     * public List<GoogleBannedWordDto> generateDailyGoogleAdsBannedWords() { List<GoogleBannedWordDto>
     * googleBannedWordDtoList = new ArrayList<GoogleBannedWordDto>(); googleBannedWordDtoList =
     * googleBannedWordDaoProvider.get().getGoogleBannedWordDtoList(); return googleBannedWordDtoList; }
     */
    public File generateSaleForProductsByTaxAndStatusInRegion(List<Tax> taxList, List<OrderStatus> orderStatusList, String inRegion, String xlsFilePath, Date startDate,
                                                              Date endDate) {

        /*
         * List<DaySaleDto> result = orderDaoProvider.get().findSaleForProductsByTaxAndStatusInRegion(taxList,
         * orderStatusList, inRegion, "haryana", false, startDate, endDate); File file = new File(xlsFilePath); String
         * headers[] = new String[]{SHIP_DATE, SUM_MRP, SUM_HK_PRICE, SUM_DISCOUNT, NET_SALES, FORWARDING_CHARGES,
         * NET_RECEIVABLE}; try { FileOutputStream out = new FileOutputStream(file); Workbook wb = new HSSFWorkbook();
         * CellStyle style = wb.createCellStyle(); Font font = wb.createFont(); font.setFontHeightInPoints((short) 11);
         * font.setColor(Font.COLOR_NORMAL); font.setBoldweight(Font.BOLDWEIGHT_BOLD); style.setFont(font); Sheet sheet1 =
         * wb.createSheet("Deleiverd-All"); Row row = sheet1.createRow(0); row.setHeightInPoints((short) 30); Cell cell;
         * for (int columnNo = 0; columnNo < headers.length; columnNo++) { cell = row.createCell(columnNo);
         * cell.setCellStyle(style); setCellValue(row, columnNo, headers[columnNo]); } for (int i = 0, rowNo = 1; i <
         * result.size(); i++, rowNo++) { DaySaleDto currentDaySale = result.get(i); row = sheet1.createRow(rowNo); for
         * (int columnNo = 0; columnNo < headers.length; columnNo++) { cell = row.createCell(columnNo);
         * setCellValue(row, columnNo, currentDaySale.get(columnNo)); } } wb.write(out); out.close(); } catch
         * (IOException ioe) { ioe.printStackTrace(); } return file;
         */

        return null;
    }

    public File generateNetMarginReportPerProductVariant(String xlsFilePath, Date startDate, Date endDate) {
        /*
         * List<NetMarginPerProductVariantDto> result = orderDaoProvider.get().findNetMarginReportPerProductVariant(
         * startDate, endDate); File file = new File(xlsFilePath); String headers[] = new String[]{PRIMARY_CATEGORY,
         * PRODUCT_VARIANT, HK_PRICE, COST_PRICE, TAX_RATE, QUANTITY,
         * TAX_ADJ_PRICE,NET_MARGIN,TOTAL_SHIPPING,TOTAL_COLLECTION,PRODUCT_NAME,PRODUCT_OPTIONS,SHIPPING_COLLECTION,NET_CONT_MARGIN};
         * try { FileOutputStream out = new FileOutputStream(file); Workbook wb = new HSSFWorkbook(); CellStyle style =
         * wb.createCellStyle(); Font font = wb.createFont(); font.setFontHeightInPoints((short) 11);
         * font.setColor(Font.COLOR_NORMAL); font.setBoldweight(Font.BOLDWEIGHT_BOLD); style.setFont(font); Sheet sheet1 =
         * wb.createSheet("Deleiverd-All"); Row row = sheet1.createRow(0); row.setHeightInPoints((short) 30); Cell cell;
         * for (int columnNo = 0; columnNo < headers.length; columnNo++) { cell = row.createCell(columnNo);
         * cell.setCellStyle(style); setCellValue(row, columnNo, headers[columnNo]); } for (int i = 0, rowNo = 1; i <
         * result.size(); i++, rowNo++) { NetMarginPerProductVariantDto netMarginPerPV = result.get(i); row =
         * sheet1.createRow(rowNo); for (int columnNo = 0; columnNo < headers.length; columnNo++) { cell =
         * row.createCell(columnNo); setCellValue(row, columnNo, netMarginPerPV.get(columnNo)); } } wb.write(out);
         * out.close(); } catch (IOException ioe) { ioe.printStackTrace(); } return file;
         */

        return null;
    }

    /**
     * The method returns an excel file for category sales report
     */

    public File generateSKUSalesReportXsl(String xslFilePath, Date startDate, Date endDate, OrderStatus orderStatus, Category topLevelCategory) throws Exception {
        /*
         * HkXlsWriter xlsWriter = new HkXlsWriter(); xlsWriter.addHeader("CATEGORY", "CATEGORY");
         * xlsWriter.addHeader("BRAND", "BRAND"); xlsWriter.addHeader("ITEM", "ITEM_NAME");
         * xlsWriter.addHeader("VARIANT_DETAILS", "VARIANT_DETAILS"); xlsWriter.addHeader("MRP", "MRP");
         * xlsWriter.addHeader("OFFER_PRICE", "OFFER_PRICE"); xlsWriter.addHeader("COST_PRICE", "COST_PRICE");
         * xlsWriter.addHeader("TAX_PAID_SUPPLIER", "TAX_PAID_SUPPLIER");
         * xlsWriter.addHeader("TAX_RECOVERABLE_SUPPLIER", "TAX_RECOVERABLE_SUPPLIER"); xlsWriter.addHeader("QTY",
         * "QTY"); xlsWriter.addHeader("MRP_SALES", "MRP_SALES"); xlsWriter.addHeader("NET_SALES", "NET_SALES");
         * xlsWriter.addHeader("COST_SALES", "COST_SALES"); xlsWriter.addHeader("TAX_PAID_SALES", "TAX_PAID_SALES");
         * xlsWriter.addHeader("TAX_RECOVERABLE_SALES", "TAX_RECOVERABLE_SALES"); xlsWriter.addHeader("INVENTORY",
         * "INVENTORY"); List<OrderStatus> applicableOrderStatus = new ArrayList<OrderStatus>(); List<Category>
         * applicableCategories = new ArrayList<Category>(); if (orderStatus != null)
         * applicableOrderStatus.add(orderStatus); else applicableOrderStatus =
         * orderStatusService.getOrderStatuses(EnumOrderStatus.getStatusForReporting()); if (topLevelCategory != null)
         * applicableCategories.add(topLevelCategory); else applicableCategories = CategoryConstants.allCategoriesList;
         * OrderSearchCriteria orderSearchCriteria = new OrderSearchCriteria();
         * orderSearchCriteria.setOrderStatusList(applicableOrderStatus); orderSearchCriteria.setCategories(new HashSet<Category>(applicableCategories));
         * if (startDate != null) orderSearchCriteria.setPaymentStartDate(startDate); if (endDate != null)
         * orderSearchCriteria.setPaymentEndDate(endDate); Criteria searchCriteria =
         * orderSearchCriteria.getSearchCriteria(); List<Order> orders = searchCriteria.list(); List<ProductVariant>
         * productVariantList = new ArrayList<ProductVariant>(); for (Order order : orders) { CartLineItemFilter
         * cartLineItemFilter = new CartLineItemFilter(order.getCartLineItems()); Set<CartLineItem>
         * productCartLineItems = cartLineItemFilter.addCartLineItemType(EnumCartLineItemType.Product).filter(); for
         * (CartLineItem cartLineItem : productCartLineItems) {
         * productVariantList.add(cartLineItem.getProductVariant()); } } int row = 1; for (ProductVariant productVariant :
         * productVariantList) { xlsWriter.addCell(row,
         * productManagerProvider.get().getTopLevelCategory(productVariant.getProduct()).getDisplayName());
         * xlsWriter.addCell(row, productVariant.getProduct().getBrand()); xlsWriter.addCell(row,
         * productVariant.getProduct().getName()); xlsWriter.addCell(row, productVariant.getOptionsPipeSeparated());
         * xlsWriter.addCell(row, productVariant.getMarkedPrice()); xlsWriter.addCell(row,
         * productVariant.getHkPrice(null)); List<Sku> variantSkus =
         * skuService.getSKUsForProductVariant(productVariant); // xlsWriter.addCell(row,
         * productVariant.getCostPrice()); Double taxPaid = 0.0; Double taxRecoverable = 0.0; if
         * (productVariant.getProduct().getSupplier() != null && productVariant.getCostPrice() != null) { if
         * (productVariant.getProduct().getSupplier().getState().equalsIgnoreCase("haryana")) { Double surcharge = 0.05;
         * taxPaid = productVariant.getCostPrice() * productVariant.getTax().getValue() * (1 + surcharge); } else {
         * Double surcharge = 0.0; // CST Surcharge Double cst = 0.02; //CST taxPaid = productVariant.getCostPrice() *
         * cst; } setCellValue(row, 7, taxPaid); if
         * (productVariant.getProduct().getSupplier().getState().equalsIgnoreCase("haryana")) { taxRecoverable =
         * taxPaid; } setCellValue(row, 8, taxRecoverable); } Long qty =
         * orderDaoProvider.get().findTotalQtyOfOrderedProductVariantForTimeFrame(productVariant, startDate, endDate,
         * orderStatus); if (qty == null) { qty = 0L; } setCellValue(row, 9, qty); setCellValue(row, 10,
         * productVariant.getMarkedPrice() * qty); setCellValue(row, 11, productVariant.getHkPrice(null) * qty); if
         * (productVariant.getCostPrice() != null) { setCellValue(row, 12, productVariant.getCostPrice() * qty); } else {
         * setCellValue(row, 12, 0.0); } setCellValue(row, 13, taxPaid * qty); setCellValue(row, 14, taxRecoverable *
         * qty); setCellValue(row, 15, productVariant.getNetInventory()); } xlsWriter.writeData(xslFilePath);
         */
        /*
         * int totalColumnNo = 20; Cell cell; for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) { cell =
         * row.createCell(columnNo); cell.setCellStyle(style); } int rowCounter = 1; List<ProductVariant>
         * orderedProductVariantList = orderDaoProvider.get().findOrderedProductVariantsForTimeFrame(startDate, endDate,
         * orderStatus, topLevelCategory); System.out.println("orderedProductVariantList: " +
         * orderedProductVariantList.size()); for (ProductVariant productVariant : orderedProductVariantList) {
         * rowCounter++; row = sheet1.createRow(rowCounter); for (int columnNo = 0; columnNo < totalColumnNo;
         * columnNo++) { cell = row.createCell(columnNo); } setCellValue(row, 0,
         * productManagerProvider.get().getTopLevelCategory(productVariant.getProduct()).getDisplayName());
         * setCellValue(row, 1, productVariant.getProduct().getBrand()); setCellValue(row, 2,
         * productVariant.getProduct().getName()); String varinatDetails = ""; for (ProductOption o :
         * productVariant.getProductOptions()) { varinatDetails += o.getName() + ":" + o.getValue() + " | "; }
         * setCellValue(row, 3, varinatDetails); setCellValue(row, 4, productVariant.getMarkedPrice());
         * setCellValue(row, 5, productVariant.getHkPrice(null)); setCellValue(row, 6, productVariant.getCostPrice());
         * Double taxPaid = 0.0; Double taxRecoverable = 0.0; if (productVariant.getProduct().getSupplier() != null &&
         * productVariant.getCostPrice() != null) { if
         * (productVariant.getProduct().getSupplier().getState().equalsIgnoreCase("haryana")) { Double surcharge = 0.05;
         * taxPaid = productVariant.getCostPrice() * productVariant.getTax().getValue() * (1 + surcharge); } else {
         * Double surcharge = 0.0; // CST Surcharge Double cst = 0.02; //CST taxPaid = productVariant.getCostPrice() *
         * cst; } setCellValue(row, 7, taxPaid); if
         * (productVariant.getProduct().getSupplier().getState().equalsIgnoreCase("haryana")) { taxRecoverable =
         * taxPaid; } setCellValue(row, 8, taxRecoverable); } Long qty =
         * orderDaoProvider.get().findTotalQtyOfOrderedProductVariantForTimeFrame(productVariant, startDate, endDate,
         * orderStatus); if (qty == null) { qty = 0L; } setCellValue(row, 9, qty); setCellValue(row, 10,
         * productVariant.getMarkedPrice() * qty); setCellValue(row, 11, productVariant.getHkPrice(null) * qty); if
         * (productVariant.getCostPrice() != null) { setCellValue(row, 12, productVariant.getCostPrice() * qty); } else {
         * setCellValue(row, 12, 0.0); } setCellValue(row, 13, taxPaid * qty); setCellValue(row, 14, taxRecoverable *
         * qty); setCellValue(row, 15, productVariant.getNetInventory()); } wb.write(out); out.close(); return file;
         */

        return null;
    }

    public File generateOrderReportXsl(String xslFilePath, Date startDate, Date endDate, OrderStatus orderStatus, Category topLevelCategory) throws Exception {
        HkXlsWriter xlsWriter = new HkXlsWriter();

        xlsWriter.addHeader("BO_GATEWAY_ID", "BO_GATEWAY_ID");
        xlsWriter.addHeader("NAME", "NAME");
        xlsWriter.addHeader("ORDER_DATE", "ORDER_DATE");
        xlsWriter.addHeader("PAYMENT_STATUS", "PAYMENT_STATUS");
        xlsWriter.addHeader("CATEGORY", "CATEGORY");
        xlsWriter.addHeader("ITEM_NAME", "ITEM_NAME");
        xlsWriter.addHeader("BRAND", "BRAND");
        xlsWriter.addHeader("VARIANT_DETAILS", "VARIANT_DETAILS");
        xlsWriter.addHeader("QTY", "QTY");

        List<Order> orders = reportOrderService.findOrdersForTimeFrame(startDate, endDate, orderStatus, topLevelCategory);

        int rowCounter = 0;
        for (Order order : orders) {
            CartLineItemFilter cartLineItemFilter = new CartLineItemFilter(new HashSet<CartLineItem>(order.getCartLineItems()));
            // Set<CartLineItem> productCartLineItems =
            // cartLineItemFilter.addCartLineItemType(EnumCartLineItemType.Product).filter();
            cartLineItemFilter.addCartLineItemType(EnumCartLineItemType.Product);
            if (topLevelCategory != null) {
                cartLineItemFilter.setCategoryName(topLevelCategory.getName());
            }
            Set<CartLineItem> productCartLineItems = cartLineItemFilter.filter();
            // if (topLevelCategory != null)
            // productCartLineItems = cartLineItemFilter.setCategoryName(topLevelCategory.getName()).filter();
            ProductVariant productVariant;
            for (CartLineItem cartLineItem : productCartLineItems) {
                rowCounter++;
                xlsWriter.addCell(rowCounter, cartLineItem.getOrder().getGatewayOrderId());
                xlsWriter.addCell(rowCounter, cartLineItem.getOrder().getAddress().getName());
                xlsWriter.addCell(rowCounter, cartLineItem.getOrder().getPayment().getPaymentDate());
                xlsWriter.addCell(rowCounter, cartLineItem.getOrder().getPayment().getPaymentStatus().getName());

                productVariant = cartLineItem.getProductVariant();
                xlsWriter.addCell(rowCounter, getCategoryService().getTopLevelCategory(productVariant.getProduct()).getDisplayName());
                xlsWriter.addCell(rowCounter, productVariant.getProduct().getName());
                xlsWriter.addCell(rowCounter, productVariant.getProduct().getBrand());
                String variantDetails = productVariant.getOptionsPipeSeparated();
                String cartLineItemExtraOptions = CartLineItemUtil.getExtraOptionsAsString(cartLineItem, "|");
                if (!StringUtils.isBlank(cartLineItemExtraOptions)) {
                    variantDetails = variantDetails.concat(" |" + cartLineItemExtraOptions);
                }
                String cartLineItemConfigOptions = CartLineItemUtil.getConfigOptionsAsString(cartLineItem, "|");
                if (!StringUtils.isBlank(cartLineItemConfigOptions)) {
                    variantDetails = variantDetails.concat(cartLineItemConfigOptions);
                }
                xlsWriter.addCell(rowCounter, variantDetails);
                xlsWriter.addCell(rowCounter, cartLineItem.getQty());
            }
        }
        return xlsWriter.writeData(xslFilePath);
    }

    public ShippingOrderService getShippingOrderService() {
        return shippingOrderService;
    }

    public void setShippingOrderService(ShippingOrderService shippingOrderService) {
        this.shippingOrderService = shippingOrderService;
    }

    public ShippingOrderStatusService getShippingOrderStatusService() {
        return shippingOrderStatusService;
    }

    public void setShippingOrderStatusService(ShippingOrderStatusService shippingOrderStatusService) {
        this.shippingOrderStatusService = shippingOrderStatusService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public CategoryService getCategoryService() {
        return categoryService;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public ReportOrderService getReportOrderService() {
        return reportOrderService;
    }

    public void setReportOrderService(ReportOrderService reportOrderService) {
        this.reportOrderService = reportOrderService;
    }

    public ReportShippingOrderService getReportShippingOrderService() {
        return reportShippingOrderService;
    }

    public void setReportShippingOrderService(ReportShippingOrderService reportShippingOrderService) {
        this.reportShippingOrderService = reportShippingOrderService;
    }

    public NotifyMeDao getNotifyMeDao() {
        return notifyMeDao;
    }

    public void setNotifyMeDao(NotifyMeDao notifyMeDao) {
        this.notifyMeDao = notifyMeDao;
    }

}