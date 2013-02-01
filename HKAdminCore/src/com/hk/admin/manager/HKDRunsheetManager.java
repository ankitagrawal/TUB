package com.hk.admin.manager;

import static com.akube.framework.util.BaseUtils.newline;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Set;

import com.hk.domain.hkDelivery.Hub;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.admin.pact.service.hkDelivery.ConsignmentService;
import com.hk.admin.util.BarcodeGenerator;
import com.hk.constants.courier.CourierConstants;
import com.hk.domain.hkDelivery.Consignment;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.payment.Payment;
import com.hk.domain.store.Store;
import com.hk.domain.user.Address;
import com.hk.pact.service.store.StoreService;

@SuppressWarnings("unchecked")
@Component
public class HKDRunsheetManager {

    /* private SimpleDateFormat sdf; */

    private String             barcodePath;
    @Autowired
    private BarcodeGenerator   barcodeGenerator;
    @Autowired
    private ConsignmentService consignmentService;

    public File generateWorkSheetXls(String xslFilePath, Set<Consignment> consignments, String assignedTo, Double totalCODAmount, int totalPackets, int totalCODPackets, Hub hub)
            throws NullPointerException, IOException, ParseException {
        File file = new File(xslFilePath);
        FileOutputStream out = new FileOutputStream(file);
        Workbook wb = new HSSFWorkbook();
        Sheet sheet1 = wb.createSheet(CourierConstants.HEALTHKART_DELIVERY + new Date());
        /* SimpleDateFormat sdf = new SimpleDateFormat("dd-mm-yyyy"); */
        Row row = null;
        Cell cell = null;
        int rowCounter = 0;
        int serialCounter = 0;
        int totalColumnNoInSheet1 = 6;
        InputStream is = null;
        byte[] bytes = null;
        int pictureIdx = 0;
        CreationHelper helper = null;
        Drawing drawing = sheet1.createDrawingPatriarch();

        ClientAnchor anchor = null;
        Picture pict = null;

        // Dynamic data to be displayed in sheet
        /* String awbNumber = null; */
        String name = null;
        String line1 = null;
        String line2 = null;
        String city = null;
        String pincode = null;
        String phone = null;
        String paymentMode = null;
        Double paymentAmt = 0.0;
        String address = null;
        String receivedDetails = null;
        String sNo = null;
        int totalPrepaidPackets = totalPackets - totalCODPackets;
        ShippingOrder shippingOrder = null;
        Address addressObj = null;
        Payment payment = null;
        Store store = null;

        // creating different styles for different elements of excel.
        CellStyle style = wb.createCellStyle();
        CellStyle style_header = wb.createCellStyle();
        CellStyle style_data = wb.createCellStyle();

        // setting borders for all cells in sheet.
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);

        style_data.setBorderTop(CellStyle.BORDER_THIN);
        style_data.setBorderBottom(CellStyle.BORDER_THIN);
        style_data.setBorderLeft(CellStyle.BORDER_THIN);
        style_data.setBorderRight(CellStyle.BORDER_THIN);
        // enabling addition of new lines in cells(using "\n" )
        style_data.setWrapText(true);

        // creating different fonts for the excel data.

        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 11);
        font.setColor(Font.COLOR_NORMAL);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(font);

        Font fontForHeader = wb.createFont();
        fontForHeader.setBoldweight(Font.BOLDWEIGHT_BOLD);
        fontForHeader.setFontHeightInPoints((short) 16);
        style_header.setFont(fontForHeader);
        style_header.setAlignment(HSSFCellStyle.ALIGN_CENTER);

        // creating rows for header.

        row = sheet1.createRow(++rowCounter);
        cell = row.createCell(2);
        cell.setCellStyle(style_header);
        setCellValue(row, 2, CourierConstants.HKD_WORKSHEET_HEADING1);

        row = sheet1.createRow(++rowCounter);
        cell = row.createCell(0);
        cell.setCellStyle(style_header);
        setCellValue(row, 0, CourierConstants.HKD_WORKSHEET_HEADING2);
        cell = row.createCell(1);
        cell.setCellStyle(style_header);
        setCellValue(row, 1, CourierConstants.HKD_WORKSHEET_HEADING3);
        cell = row.createCell(2);
        cell.setCellStyle(style_header);
        setCellValue(row, 2, CourierConstants.HKD_WORKSHEET_HEADING4);
        cell = row.createCell(3);
        cell.setCellStyle(style_header);
        setCellValue(row, 3, CourierConstants.HKD_WORKSHEET_HEADING5);
        cell = row.createCell(4);
        cell.setCellStyle(style_header);
        setCellValue(row, 4, CourierConstants.HKD_WORKSHEET_HEADING6);

        addEmptyLine(row, sheet1, ++rowCounter, cell);

        row = sheet1.createRow(++rowCounter);
        // style.setFont(font);
        for (int i = 0; i < 6; i++) {
            sheet1.autoSizeColumn(i);
            cell = row.createCell(i);
            cell.setCellStyle(style);
        }
        Date currentDate = new Date();
        setCellValue(row, 0, CourierConstants.HKD_WORKSHEET_NAME);
        setCellValue(row, 1, assignedTo);
        setCellValue(row, 2, CourierConstants.HKD_WORKSHEET_MOBILE);
        setCellValue(row, 3, CourierConstants.HKD_WORKSHEET_DATE);
        setCellValue(row, 4, currentDate + "");
        setCellValue(row, 5, CourierConstants.HKD_HUB+ " "+ hub.getName() );
        addEmptyLine(row, sheet1, ++rowCounter, cell);
        row = sheet1.createRow(++rowCounter);
        for (int i = 0; i < 6; i++) {
            sheet1.autoSizeColumn(i);
            cell = row.createCell(i);
            cell.setCellStyle(style);
        }
        setCellValue(row, 0, CourierConstants.HKD_WORKSHEET_TOTALPKTS);
        setCellValue(row, 1, totalPackets + "");
        setCellValue(row, 2, CourierConstants.HKD_WORKSHEET_TOTAL_PREPAID_BOX + totalPrepaidPackets);
        setCellValue(row, 3, CourierConstants.HKD_WORKSHEET_TOTAL_COD_BOX + totalCODPackets);
        setCellValue(row, 4, CourierConstants.HKD_WORKSHEET_TOTAL_COD_AMT);
        setCellValue(row, 5, totalCODAmount + "");
        addEmptyLine(row, sheet1, ++rowCounter, cell);

        row = sheet1.createRow(++rowCounter);
        for (int i = 0; i < 6; i++) {
            sheet1.autoSizeColumn(i);
            cell = row.createCell(i);
            cell.setCellStyle(style);
        }
        setCellValue(row, 0, CourierConstants.HKD_WORKSHEET_COD_CASH);
        setCellValue(row, 1, "");
        setCellValue(row, 2, CourierConstants.HKD_WORKSHEET_PENDING_COD);
        setCellValue(row, 3, CourierConstants.HKD_WORKSHEET_HOLD_PKTS);
        setCellValue(row, 4, "");
        setCellValue(row, 5, CourierConstants.HKD_WORKSHEET_RTO_PKTS);
        addEmptyLine(row, sheet1, ++rowCounter, cell);

        row = sheet1.createRow(++rowCounter);
        for (int i = 0; i < 6; i++) {
            sheet1.autoSizeColumn(i);
            cell = row.createCell(i);
            cell.setCellStyle(style);
        }
        setCellValue(row, 0, CourierConstants.HKD_WORKSHEET_SNO);
        setCellValue(row, 1, CourierConstants.HKD_WORKSHEET_GATEWAYID);
        setCellValue(row, 2, CourierConstants.HKD_WORKSHEET_ADDRESS);
        setCellValue(row, 3, CourierConstants.HKD_WORKSHEET_AMT);
        setCellValue(row, 4, CourierConstants.HK_WORKSHEET_INFO);
        setCellValue(row, 5, CourierConstants.HKD_WORKSHEET_REMARKS);
        addEmptyLine(row, sheet1, ++rowCounter, cell);

        /* for (int index = 0; index < consignmentList.size(); index++) { */
        for (Consignment consignment : consignments) {
            ++serialCounter;
            rowCounter++;
            row = sheet1.createRow(rowCounter);
            for (int columnNo = 0; columnNo < totalColumnNoInSheet1; columnNo++) {
                sheet1.autoSizeColumn(columnNo);
                cell = row.createCell(columnNo);
                cell.setCellStyle(style_data);
            }
            shippingOrder = consignmentService.getShippingOrderFromConsignment(consignment);
            addressObj = shippingOrder.getBaseOrder().getAddress();
            payment = shippingOrder.getBaseOrder().getPayment();
            store = shippingOrder.getBaseOrder().getStore();

            // fetching contact name,contact-number for COD/Non COD
            paymentMode = consignment.getPaymentMode();
            if (paymentMode.equalsIgnoreCase("COD")) {
                if (store.getId().equals(StoreService.MIH_STORE_ID)) {
                    name = addressObj.getName();
                    phone = addressObj.getPhone();
                } else {
                    name = payment.getContactName();
                    phone = payment.getContactNumber();
	                if(name == null){
		                name = addressObj.getName();
	                }
                }

            } else {
                name = addressObj.getName();
                phone = addressObj.getPhone();
            }

            name = name.toUpperCase();
            line1 = addressObj.getLine1();
            line2 = addressObj.getLine2();
            line2 = (line2 == null) ? "" : line2;
            city = addressObj.getCity();
            pincode = addressObj.getPincode().getPincode();
            paymentAmt = consignment.getAmount();
            address = "Name:" + name + newline + "Address:" + line1 + "," + newline + line2 + "," + newline + city + "-" + pincode + newline + "Phone:" + phone;
            receivedDetails = "Name:" + newline + "Relation:" + newline + "Mobile No.:" + newline + "Received Date,Time:" + newline + "Sign";

            // adding barcode image to cell
            barcodePath = barcodeGenerator.getBarcodePath(consignment.getCnnNumber(), 1.0f, 150, false);

            // add picture data to this workbook.
            is = new FileInputStream(barcodePath);
            bytes = IOUtils.toByteArray(is);
            pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
            is.close();
            helper = wb.getCreationHelper();

            // Create the drawing patriarch. This is the top level container for all shapes.

            // add a picture shape
            anchor = helper.createClientAnchor();
            anchor.setAnchorType(ClientAnchor.MOVE_AND_RESIZE);
            // set top-left corner of the picture,
            pict = drawing.createPicture(anchor, pictureIdx);
            pict.resize(8.0);

            sNo = serialCounter + "";
            setCellValue(row, 0, sNo);
            // setCellValue(row, 1, awbNumber);
            anchor.setDx1(10);
            anchor.setDx2(700);
            anchor.setDy1(10);
            anchor.setDy2(200);
            anchor.setCol1(1);
            anchor.setRow1(rowCounter);
            anchor.setCol2(1);
            anchor.setRow2(rowCounter);
            setCellValue(row, 2, address);
            if (paymentMode.equals("COD")) {
                setCellValue(row, 3, CourierConstants.HKD_WORKSHEET_COD + Math.round(paymentAmt));
            } else {
                setCellValue(row, 3, CourierConstants.HKD_WORKSHEET_PREPAID);

            }
            setCellValue(row, 4, receivedDetails);
            setCellValue(row, 5, "");

        }
        wb.write(out);
        out.close();
        return file;
    }

    public String getAwbWithoutConsignmntString(List<String> trackingIdsWithoutConsignment) {
        StringBuffer strBuffr = new StringBuffer();
        for (String unmodifiedTrackingId : trackingIdsWithoutConsignment) {
            strBuffr.append(unmodifiedTrackingId);
            strBuffr.append(",");
        }
        return strBuffr.toString();
    }

    public static void setCellValue(Row row, int column, Long cellValue) {
        if (cellValue != null) {
            Cell cell = row.getCell(column);
            cell.setCellValue(cellValue);
        }
    }

    public static void setCellValue(Row row, int column, String cellValue) {
        if (cellValue == null)
            cellValue = "";
        Cell cell = row.getCell(column);
        if (cell == null) {
            cell = row.createCell(column);
        }
        cell.setCellValue(cellValue);
    }

    public static void setCellValue(Row row, int column, Date cellValue) {
        if (cellValue != null) {
            Cell cell = row.getCell(column);
            cell.setCellValue(cellValue);
        }
    }

    public static void addEmptyLine(Row row1, Sheet sheet1, int rowCounter, Cell cell) {
        row1 = sheet1.createRow(rowCounter);
        cell = row1.createCell(0);
        setCellValue(row1, 0, " ");

    }

}
