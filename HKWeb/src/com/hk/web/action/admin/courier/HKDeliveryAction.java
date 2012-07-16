package com.hk.web.action.admin.courier;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.SimpleMessage;
import com.akube.framework.stripes.action.BaseAction;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.courier.Awb;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.UserService;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.courier.CourierConstants;
import com.hk.constants.courier.EnumAwbStatus;
import com.hk.web.action.error.AdminPermissionAction;
import com.hk.admin.util.BarcodeGenerator;
import com.hk.admin.pact.service.courier.AwbService;

import java.util.List;
import java.util.ArrayList;
import java.util.Date;
import java.io.*;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.util.IOUtils;
import org.stripesstuff.plugin.security.Secure;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Secure(hasAnyPermissions = {PermissionConstants.HK_DELIVERY_WORKSHEET_DOWNLOAD}, authActionBean = AdminPermissionAction.class)
@Component
public class HKDeliveryAction extends BaseAction {

    private File                  xlsFile;
    private SimpleDateFormat      sdf;
    private List<ShippingOrder>   shippingOrderList;
    private String                assignedTo;
    private int                   totalPackets;
    private int                   totalCODPackets = 0;
    private int                   totalPrepaidPackets = 0;
    private double                totalCODAmount = 0.0;
    private String                barcodePath;

    List<String>                  trackingIdList=new ArrayList<String>();
    @Autowired
    ShippingOrderService          shippingOrderService;
    @Autowired
    AwbService awbService;
    @Autowired
    UserService userService;

    @Autowired
    private BarcodeGenerator barcodeGenerator;



    @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
    String                        adminDownloads;


    @DefaultHandler
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/hkDeliveryWorksheet.jsp");
    }

    public Resolution downloadDeliveryWorkSheet() {
          shippingOrderList = new ArrayList<ShippingOrder>();
        List<Awb> awbList=new ArrayList<Awb>();
        for (String trackingNum : trackingIdList) {
        awbList=  awbService.getAvailableAwbForCourierByWarehouseCodStatus(null,trackingNum.trim(),userService.getWarehouseForLoggedInUser(),null, EnumAwbStatus.Used.getAsAwbStatus());
            for(Awb awb: awbList){
            List<ShippingOrder> shippinglist = shippingOrderService.getShippingOrderByAwb(awb);
                for( ShippingOrder shippingOrder:shippinglist )
                {
                if(shippingOrder.isCOD()){
                        ++totalCODPackets;
                    totalCODAmount=totalCODAmount+shippingOrder.getAmount();
                    totalCODAmount=Math.round(totalCODAmount);
                }  else{
                    ++totalPrepaidPackets;
                }
                shippingOrderList.add(shippingOrder);
            }

            }
        }
        totalPackets=shippingOrderList.size();

        try {
            sdf = new SimpleDateFormat("yyyyMMdd");
            xlsFile = new File(adminDownloads + "/" + CourierConstants.HKDELIVERY_WORKSHEET_FOLDER + "/" + CourierConstants.HKDELIVERY_WORKSHEET + "_" + sdf.format(new Date()) + ".xls");
            xlsFile = generateWorkSheetXls(xlsFile.getPath(), shippingOrderList);
        } catch (IOException ioe) {
            addRedirectAlertMessage(new SimpleMessage(CourierConstants.HKDELIVERY_IOEXCEPTION));
            return new ForwardResolution(HKDeliveryAction.class);
        } catch (NullPointerException npe) {
            addRedirectAlertMessage(new SimpleMessage(CourierConstants.HKDELIVERY_NULLEXCEPTION));
            return new ForwardResolution(HKDeliveryAction.class);
        }catch (Exception ex) {
            addRedirectAlertMessage(new SimpleMessage(CourierConstants.HKDELIVERY_EXCEPTION));
            return new ForwardResolution(HKDeliveryAction.class);
        }
        return new HTTPResponseResolution();
    }

    public File generateWorkSheetXls(String xslFilePath, List<ShippingOrder> shippingOrderList) throws NullPointerException, IOException, ParseException {
        File file = new File(xslFilePath);
        FileOutputStream out = new FileOutputStream(file);
        Workbook wb = new HSSFWorkbook();
        Sheet sheet1 = wb.createSheet(CourierConstants.HEALTHKART_DELIVERY + new Date());
        sdf=new SimpleDateFormat("dd-mm-yyyy");
        Row row = null;
        Cell cell = null;
        int rowCounter = 0;
        int totalColumnNoInSheet1 = 6;
        InputStream is=null;
        byte[] bytes=null;
        int pictureIdx=0;
        CreationHelper helper=null;
        Drawing drawing= sheet1.createDrawingPatriarch();

        ClientAnchor anchor=null;
        Picture pict=null;

        //Dynamic data to be displayed in sheet
        String awbNumber = null;
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


        //creating different styles for different elements of excel.
        CellStyle style = wb.createCellStyle();
        CellStyle style_header = wb.createCellStyle();
        CellStyle style_data = wb.createCellStyle();

        //setting borders for all cells in sheet.
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setBorderRight(CellStyle.BORDER_THIN);

        style_data.setBorderTop(CellStyle.BORDER_THIN);
        style_data.setBorderBottom(CellStyle.BORDER_THIN);
        style_data.setBorderLeft(CellStyle.BORDER_THIN);
        style_data.setBorderRight(CellStyle.BORDER_THIN);
        //enabling addition of new lines in cells(using "\n" )
        style_data.setWrapText(true);

        //creating different fonts for the excel data.

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


        //creating rows for header.
        
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
        Date currentDate=new Date();
        setCellValue(row, 0, CourierConstants.HKD_WORKSHEET_NAME);
        setCellValue(row, 1, assignedTo);
        setCellValue(row, 2, CourierConstants.HKD_WORKSHEET_MOBILE);
        setCellValue(row, 3, CourierConstants.HKD_WORKSHEET_DATE);
        setCellValue(row, 4,  currentDate+"");
        setCellValue(row, 5, "");
        addEmptyLine(row, sheet1, ++rowCounter, cell);
        row = sheet1.createRow(++rowCounter);
        for (int i = 0; i < 6; i++) {
            sheet1.autoSizeColumn(i);
            cell = row.createCell(i);
            cell.setCellStyle(style);
        }
        setCellValue(row, 0, CourierConstants.HKD_WORKSHEET_TOTALPKTS);
        setCellValue(row, 1, totalPackets+"");
        setCellValue(row, 2, CourierConstants.HKD_WORKSHEET_TOTAL_PREPAID_BOX + totalPrepaidPackets);
        setCellValue(row, 3, CourierConstants.HKD_WORKSHEET_TOTAL_COD_BOX + totalCODPackets);
        setCellValue(row, 4, CourierConstants.HKD_WORKSHEET_TOTAL_COD_AMT );
        setCellValue(row, 5, totalCODAmount +"");
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

        for (int index = 0; index < shippingOrderList.size(); index++) {
            rowCounter++;
            row = sheet1.createRow(rowCounter);
            for (int columnNo = 0; columnNo < totalColumnNoInSheet1; columnNo++) {
                sheet1.autoSizeColumn(columnNo);
                cell = row.createCell(columnNo);
                cell.setCellStyle(style_data);
            }

            //fetching contact name,contact-number for COD/Non COD
            paymentMode = shippingOrderList.get(index).getBaseOrder().getPayment().getPaymentMode().getName();
            if (paymentMode.equalsIgnoreCase("COD")) {
                name = shippingOrderList.get(index).getBaseOrder().getPayment().getContactName();
                phone = shippingOrderList.get(index).getBaseOrder().getPayment().getContactNumber();

            } else {
                name = shippingOrderList.get(index).getBaseOrder().getAddress().getName();
                phone = shippingOrderList.get(index).getBaseOrder().getAddress().getPhone();
            }

            name=name.toUpperCase();
            line1 = shippingOrderList.get(index).getBaseOrder().getAddress().getLine1();
            line2 = shippingOrderList.get(index).getBaseOrder().getAddress().getLine2();
            line2=(line2 == null)?"":line2;
            city = shippingOrderList.get(index).getBaseOrder().getAddress().getCity();
            pincode = shippingOrderList.get(index).getBaseOrder().getAddress().getPin();
            paymentAmt = shippingOrderList.get(index).getAmount();
            address = "Name:" + name + "\n" + "Address:" + line1 + "," + "\n" + line2 + "," + "\n" + city + "-" + pincode + "\n" + "Phone:" + phone;
            receivedDetails = "Name:" + "\n" + "Relation:" + "\n" + "Mobile No.:" + "\n" + "Received Date,Time:" + "\n" + "Sign";

           //adding barcode image to cell
            barcodePath = barcodeGenerator.getBarcodePath(shippingOrderList.get(index).getGatewayOrderId(),1.0f);

            //add picture data to this workbook.
            is = new FileInputStream(barcodePath);
            bytes = IOUtils.toByteArray(is);
            pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);
            is.close();
            helper = wb.getCreationHelper();

            // Create the drawing patriarch.  This is the top level container for all shapes.

            //add a picture shape
            anchor = helper.createClientAnchor();
            anchor.setAnchorType(ClientAnchor.MOVE_AND_RESIZE);
            //set top-left corner of the picture,
            pict = drawing.createPicture(anchor, pictureIdx);
            pict.resize(8.0);



            sNo = index + 1 + "";
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
                setCellValue(row, 3, CourierConstants.HKD_WORKSHEET_PREPAID );

            }
            setCellValue(row, 4, receivedDetails);
            setCellValue(row, 5, "");


        }
        wb.write(out);
        out.close();
        return file;
    }

    public class HTTPResponseResolution implements Resolution {
        public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
            OutputStream out = null;
            InputStream in = new BufferedInputStream(new FileInputStream(xlsFile));
            res.setContentLength((int) xlsFile.length());
            res.setHeader("Content-Disposition", "attachment; filename=\"" + xlsFile.getName() + "\";");
            out = res.getOutputStream();

            // Copy the contents of the file to the output stream
            byte[] buf = new byte[4096];
            int count = 0;
            while ((count = in.read(buf)) >= 0) {
                out.write(buf, 0, count);
            }
        }

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

    public String getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(String assignedTo) {
        this.assignedTo = assignedTo;
    }

    public List<String> getTrackingIdList() {
        return trackingIdList;
    }

    public void setTrackingIdList(List<String> trackingIdList) {
        this.trackingIdList = trackingIdList;
    }
}
