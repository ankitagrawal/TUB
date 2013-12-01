package com.hk.admin.util.helper;

import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.pact.service.courier.PincodeCourierService;
import com.hk.admin.util.XslUtil;
import com.hk.constants.XslConstants;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.PincodeCourierMapping;
import com.hk.exception.ExcelBlankFieldException;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.service.core.PincodeService;
import com.hk.util.io.ExcelSheetParser;
import com.hk.util.io.HKRow;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Pratham
 * Date: 1/2/13
 * Time: 5:20 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class XslPincodeCourierMapping {

    private static Logger logger = LoggerFactory.getLogger(XslPincodeCourierMapping.class);

    @Autowired
    PincodeService pincodeService;
    @Autowired
    PincodeCourierService pincodeCourierService;

    @Autowired
    CourierService courierService;

    public static final String PINCODE = "PINCODE";
    public static final String ROUTING_CODE = "ROUTING_CODE";
    public static final String COD_AVAILABLE = "COD_AVAILABLE";
    public static final String COURIER_ID = "COURIER_ID";
    public static final String PREPAID_AIR = "PREPAID_AIR";
    public static final String PREPAID_GROUND = "PREPAID_GROUND";
    public static final String COD_AIR = "COD_AIR";
    public static final String COD_GROUND = "COD_GROUND";

    private Map<String, Integer> headerMap = new HashMap<String, Integer>();

    @Autowired
    BaseDao baseDao;

    public Set<PincodeCourierMapping> readPincodeCourierMappings(File objInFile) throws Exception {
        logger.debug("parsing pincode courier mapping : " + objInFile.getAbsolutePath());
        Set<PincodeCourierMapping> pincodeCourierMappings = new HashSet<PincodeCourierMapping>();
        int rowCount = 1;
        ExcelSheetParser excel = new ExcelSheetParser(objInFile.getAbsolutePath(), "PincodeCourierMapping", true);
        Iterator<HKRow> rowiterator = excel.parse();

        try {
            while (rowiterator.hasNext()) {
                rowCount++;
                PincodeCourierMapping pincodeCourierMapping;
                HKRow row = rowiterator.next();
                String pin = row.getColumnValue(XslConstants.PINCODE);
                String courierId = row.getColumnValue(XslConstants.COURIER_ID);
                String prepaidAir = row.getColumnValue(PREPAID_AIR);
                String prepaidGround = row.getColumnValue(PREPAID_GROUND);
                String codAir = row.getColumnValue(COD_AIR);
                String codGround = row.getColumnValue(COD_GROUND);
                String routingCode = row.getColumnValue(ROUTING_CODE);

                Pincode pincode;
                if (StringUtils.isBlank(pin)) {
                    throw new ExcelBlankFieldException("Pincode cannot be empty" + "    ", rowCount);
                } else {
                    pincode = pincodeService.getByPincode(pin);
                    if (pincode == null) {
                        throw new ExcelBlankFieldException("Invalid Pincode" + "    ", rowCount);
                    }
                }
                Long courierLongId = XslUtil.getLong(courierId.trim());
                Courier courier = courierService.getCourierById(courierLongId);
                if (courier == null) {
                    logger.error("courierId is not valid  " + courierId, rowCount);
                    throw new ExcelBlankFieldException("courierId is not valid  " + "    ", rowCount);
                }

                PincodeCourierMapping pincodeCourierMappingDB = pincodeCourierService.getApplicablePincodeCourierMapping(pincode, Arrays.asList(courier), null, null);

                boolean isPrepaidAir = StringUtils.isNotBlank(prepaidAir) && prepaidAir.trim().toLowerCase().equals("y");
                boolean isPrepaidGround = StringUtils.isNotBlank(prepaidGround) && prepaidGround.trim().toLowerCase().equals("y");
                boolean isCodAir = StringUtils.isNotBlank(codAir) && codAir.trim().toLowerCase().equals("y");
                boolean isCodGround = StringUtils.isNotBlank(codGround) && codGround.trim().toLowerCase().equals("y");

                    pincodeCourierMapping = new PincodeCourierMapping();
                    pincodeCourierMapping.setPincode(pincode);
                    pincodeCourierMapping.setCourier(courier);
                    pincodeCourierMapping.setCodAir(isCodAir);
                    pincodeCourierMapping.setCodGround(isCodGround);
                    pincodeCourierMapping.setPrepaidAir(isPrepaidAir);
                    pincodeCourierMapping.setPrepaidGround(isPrepaidGround);
                    pincodeCourierMapping.setRoutingCode(routingCode);
                if (pincodeCourierMappingDB == null) {
                    boolean isValidMapping = isCodGround || isPrepaidGround || isPrepaidAir || isCodAir;
                    if (isValidMapping) {
                        pincodeCourierMappings.add(pincodeCourierMapping);
                    }
                }
              else{
                 if(pincodeCourierService.changePincodeCourierMapping(pincodeCourierMappingDB, pincodeCourierMapping)){
                   pincodeCourierMapping.setId(pincodeCourierMappingDB.getId());
                   pincodeCourierMappings.add(pincodeCourierMapping);
                  }    
                }
            }
        } catch (Exception e) {
            logger.error("Exception @ Row:" + rowCount + e);
            throw new Exception("Exception @ Row:" + rowCount, e);
        }
        return pincodeCourierMappings;

    }


    public File generatePincodeCourierMappingXsl(List<PincodeCourierMapping> pincodeCourierMappings, String xslFilePath) throws Exception {

        File file = new File(xslFilePath);
        file.getParentFile().mkdirs();
        FileOutputStream out = new FileOutputStream(file);
        Workbook wb = new HSSFWorkbook();

        CellStyle style = wb.createCellStyle();
        Font font = wb.createFont();
        font.setFontHeightInPoints((short) 12);
        font.setColor(Font.COLOR_NORMAL);
        font.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(font);
        Sheet sheet1 = wb.createSheet("PincodeCourierMapping");
        Row row = sheet1.createRow(0);
        row.setHeightInPoints((short) 25);

        int totalColumnNo = 10;

        Cell cell;
        for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
            cell = row.createCell(columnNo);
            cell.setCellStyle(style);
        }
        setCellValue(row, 0, PINCODE);
        setCellValue(row, 1, COURIER_ID);
        setCellValue(row, 2, PREPAID_AIR);
        setCellValue(row, 3, PREPAID_GROUND);
        setCellValue(row, 4, COD_AIR);
        setCellValue(row, 5, COD_GROUND);
        setCellValue(row, 6, ROUTING_CODE);

        int initialRowNo = 1;

        for (PincodeCourierMapping pincodeCourierMapping : pincodeCourierMappings) {
            if(pincodeCourierMapping.isPrepaidAir() || pincodeCourierMapping.isPrepaidGround() || pincodeCourierMapping.isCodAir() || pincodeCourierMapping.isCodGround()){
            row = sheet1.createRow(initialRowNo);
            for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
                row.createCell(columnNo);
            }

            Pincode pincode = pincodeCourierMapping.getPincode();
            setCellValue(row, 0, pincode.getPincode());
            setCellValue(row, 1, pincodeCourierMapping.getCourier().getId());
            setCellValue(row, 2, pincodeCourierMapping.isPrepaidAir() ? "Y" : "N");
            setCellValue(row, 3, pincodeCourierMapping.isPrepaidGround() ? "Y" : "N");
            setCellValue(row, 4, pincodeCourierMapping.isCodAir() ? "Y" : "N");
            setCellValue(row, 5, pincodeCourierMapping.isCodGround() ? "Y" : "N");
            setCellValue(row, 6, pincodeCourierMapping.getRoutingCode());
            initialRowNo++;
            }
        }

        Sheet sheet2 = wb.createSheet("Courier IDs");
        row = sheet2.createRow(0);
        row.setHeightInPoints((short) 25);

        int totalColumnNo2 = 2;

        for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
            cell = row.createCell(columnNo);
            cell.setCellStyle(style);
        }
        setCellValue(row, 0, "Courier");
        setCellValue(row, 1, "ID");

        int initialRowNo2 = 1;
        List<Courier> courierList = baseDao.getAll(Courier.class);
        for (Courier courier : courierList) {
            row = sheet2.createRow(initialRowNo2);
            for (int columnNo = 0; columnNo < totalColumnNo2; columnNo++) {
                row.createCell(columnNo);
            }

            setCellValue(row, 0, courier.getName());
            setCellValue(row, 1, courier.getId());

            initialRowNo2++;
        }

        wb.write(out);
        out.close();
        return file;
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

    private void setHeaderCellValue(Row row, int column, String cellValue) {
        if (cellValue == null)
            cellValue = "";
        Cell cell = row.getCell(column);
        cell.setCellValue(cellValue);

        this.setRowMap(cellValue, column);
    }

    private void setRowMap(String columnHeader, int columnIndex) {
        headerMap.put(columnHeader, columnIndex);
    }


}
