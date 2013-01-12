package com.hk.admin.util.helper;

import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.pact.service.courier.PincodeCourierService;
import com.hk.constants.XslConstants;
import com.hk.domain.core.City;
import com.hk.domain.core.Pincode;
import com.hk.domain.core.State;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.PincodeDefaultCourier;
import com.hk.domain.courier.Zone;
import com.hk.domain.warehouse.Warehouse;
import com.hk.exception.ExcelBlankFieldException;
import com.hk.exception.HealthkartCheckedException;
import com.hk.pact.service.core.CityService;
import com.hk.pact.service.core.PincodeService;
import com.hk.pact.service.core.StateService;
import com.hk.pact.service.core.WarehouseService;
import com.hk.util.io.ExcelSheetParser;
import com.hk.util.io.HKRow;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Pratham
 * Date: 1/3/13
 * Time: 2:32 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class XslPincodeParser {

    private Map<String, Integer> headerMap = new HashMap<String, Integer>();
    @Autowired
    PincodeService pincodeService;
    @Autowired
    PincodeCourierService pincodeCourierService;
    @Autowired
    CityService cityService;
    @Autowired
    CourierService courierService;
    @Autowired
    WarehouseService warehouseService;
    @Autowired
    StateService stateService;

    private static Logger logger         = LoggerFactory.getLogger(XslPincodeParser.class);


    public Set<Pincode> readPincodeList(File objInFile) throws Exception {

        Set<Pincode> pincodeSet = new HashSet<Pincode>();

        int rowCount = 1;
        ExcelSheetParser excel = new ExcelSheetParser(objInFile.getAbsolutePath(), "Pincode", true);
        Iterator<HKRow> rowiterator = excel.parse();

        try {
            while (rowiterator.hasNext()) {
                rowCount++;
                HKRow row = rowiterator.next();
                String pin = row.getColumnValue(XslConstants.PINCODE);
                String cityName = row.getColumnValue(XslConstants.CITY);
                String stateName = row.getColumnValue(XslConstants.STATE);
                String zoneName = row.getColumnValue(XslConstants.ZONE);
                String locality = row.getColumnValue(XslConstants.LOCALITY);
                String region = row.getColumnValue(XslConstants.REGION);

                if (StringUtils.isEmpty(pin) || StringUtils.isEmpty(cityName) || StringUtils.isEmpty(stateName) || StringUtils.isEmpty(zoneName)) {
                    throw new ExcelBlankFieldException("Blank field for at row number " + rowCount);
                }

                Pincode pincode = pincodeService.getByPincode(pin);
                if (pincode == null) {
                    pincode = new Pincode();
                }

                City city = cityService.getCityByName(cityName);
                State state = stateService.getStateByName(stateName);
                Zone zone = pincodeService.getZoneByName(zoneName);
                if (city == null || state == null || zone == null) {
                    throw new ExcelBlankFieldException("Invalid city/state/zone name at row " + rowCount);
                }
                pincode.setPincode(pin);
                pincode.setCity(city);
                pincode.setState(state);
                pincode.setZone(zone);
                pincode.setLocality(locality);
                pincode.setRegion(region);
                pincodeSet.add(pincode);
                rowCount++;
            }
        } catch (Exception e) {
            throw new Exception("Exception @ Row:" + rowCount + " " + e.getMessage());
        }
        return pincodeSet;
    }

    public File generatePincodeXsl(List<Pincode> pincodeList, String xslFilePath) throws Exception {

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
        Sheet sheet1 = wb.createSheet("PincodeInfo");
        Row row = sheet1.createRow(0);
        row.setHeightInPoints((short) 25);

        int totalColumnNo = 6;

        Cell cell;
        for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
            cell = row.createCell(columnNo);
            cell.setCellStyle(style);
        }
        setCellValue(row, 0, XslConstants.ID);
        setCellValue(row, 1, XslConstants.PINCODE);
        setCellValue(row, 2, XslConstants.CITY);
        setCellValue(row, 3, XslConstants.STATE);
        setCellValue(row, 4, XslConstants.LOCALITY);
        setCellValue(row, 5, XslConstants.ZONE);

        int initialRowNo = 1;
        for (Pincode pincode : pincodeList) {

            row = sheet1.createRow(initialRowNo);
            for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
                row.createCell(columnNo);
            }

            setCellValue(row, 0, pincode.getId());
            setCellValue(row, 1, pincode.getPincode());
            setCellValue(row, 2, pincode.getCity().getName());
            setCellValue(row, 3, pincode.getState().getName());
            setCellValue(row, 4, pincode.getLocality());
            if (pincode.getZone() != null) {
                setCellValue(row, 5, pincode.getZone().getName());
            }
            initialRowNo++;
        }

        wb.write(out);
        out.close();
        return file;
    }

    public File generatePincodeDefaultCourierXsl(List<PincodeDefaultCourier> pincodeDefaultCourierList, String xslFilePath) throws Exception {
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
        Sheet sheet1 = wb.createSheet(XslConstants.DEFAULT_COURIER_SHEET);
        Row row = sheet1.createRow(0);
        row.setHeightInPoints((short) 25);

        int totalColumnNo = 8;

        Cell cell;
        for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
            cell = row.createCell(columnNo);
            cell.setCellStyle(style);
        }
        setCellValue(row, 0, XslConstants.PINCODE);
        setCellValue(row, 1, XslConstants.WAREHOUSE);
        setCellValue(row, 2, XslConstants.COURIER_ID);
        setCellValue(row, 3, XslConstants.COD_AVAILABLE);
        setCellValue(row, 4, XslConstants.GROUND_SHIPPING_AVAILABLE);
        setCellValue(row, 5, XslConstants.ESTIMATED_SHIPPING_COST);

        int initialRowNo = 1;
        for (PincodeDefaultCourier pincodeDefaultCourier : pincodeDefaultCourierList) {

            row = sheet1.createRow(initialRowNo);
            for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
                row.createCell(columnNo);
            }

            setCellValue(row, 0, pincodeDefaultCourier.getPincode().getPincode());
            setCellValue(row, 1, pincodeDefaultCourier.getWarehouse().getId());
            if (pincodeDefaultCourier.getCourier() != null) {
                setCellValue(row, 2, pincodeDefaultCourier.getCourier().getId());
            } else {
                setCellValue(row, 2, -1L);
            }
            setCellValue(row, 3, pincodeDefaultCourier.isCod() ? "Y" : "N");
            setCellValue(row, 4, pincodeDefaultCourier.isGroundShipping() ? "Y" : "N");
            setCellValue(row, 5, pincodeDefaultCourier.getEstimatedShippingCost());

            initialRowNo++;
        }

        wb.write(out);
        out.close();
        return file;
    }

    public Set<PincodeDefaultCourier> readDefaultPincodeList(File objInFile) throws Exception {
        Set<PincodeDefaultCourier> pincodeDefaultCouriers = new HashSet<PincodeDefaultCourier>();
        int rowCount = 1;
        ExcelSheetParser excel = new ExcelSheetParser(objInFile.getAbsolutePath(), "PincodeDefaultCourier", true);
        Iterator<HKRow> rowiterator = excel.parse();

        try {
            while (rowiterator.hasNext()) {
                rowCount++;
                HKRow row = rowiterator.next();
                String pin = row.getColumnValue(XslConstants.PINCODE);
                String warehouseId = row.getColumnValue(XslConstants.WAREHOUSE);
                String courierId = row.getColumnValue(XslConstants.COURIER_ID);
                String groundShippingAvailable = row.getColumnValue(XslConstants.GROUND_SHIPPING_AVAILABLE);
                String estimatedShippingCost = row.getColumnValue(XslConstants.ESTIMATED_SHIPPING_COST);
                String codAvailable = row.getColumnValue(XslConstants.COD_AVAILABLE);

                if (StringUtils.isEmpty(pin) || StringUtils.isEmpty(warehouseId) || StringUtils.isEmpty(courierId) || StringUtils.isEmpty(groundShippingAvailable)) {
                    throw new ExcelBlankFieldException("Blank field for at row number " + rowCount);
                }

                Pincode pincode = pincodeService.getByPincode(pin);
                Warehouse warehouse = warehouseService.getWarehouseById(Long.valueOf(warehouseId));
                Courier courier = courierService.getCourierById(Long.valueOf(courierId));

                if (pincode == null || warehouse == null || courier == null) {
                    throw new ExcelBlankFieldException("Invalid pincode/warehouse/courier value at row " + rowCount);
                }

                boolean isCODAvailable = StringUtils.isNotBlank(codAvailable) && codAvailable.trim().toLowerCase().equals("y");
                boolean isGroundShippingAvailable = StringUtils.isNotBlank(groundShippingAvailable) && groundShippingAvailable.trim().toLowerCase().equals("y");

                if (!pincodeCourierService.isDefaultCourierApplicable(pincode, courier, isGroundShippingAvailable, isCODAvailable)) {
                    throw new HealthkartCheckedException("the default courier being updated is not yet serviceable for the mentioned conditions at row " + rowCount);
                }

                PincodeDefaultCourier pincodeDefaultCourier = pincodeService.createPincodeDefaultCourier(pincode, courier, warehouse, isGroundShippingAvailable,
                        isCODAvailable, Double.valueOf(estimatedShippingCost));
                pincodeDefaultCouriers.add(pincodeDefaultCourier);
                rowCount++;
            }
        } catch (Exception e) {
            throw new Exception("Exception @ Row:" + rowCount + " " + e.getMessage());
        }
        return pincodeDefaultCouriers;
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
