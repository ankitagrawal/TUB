package com.hk.admin.util.helper;

import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.pact.service.courier.PincodeCourierService;
import com.hk.admin.pact.service.hkDelivery.HubService;
import com.hk.constants.XslConstants;
import com.hk.domain.core.City;
import com.hk.domain.core.Pincode;
import com.hk.domain.core.State;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.PincodeDefaultCourier;
import com.hk.domain.courier.Zone;
import com.hk.domain.hkDelivery.Hub;
import com.hk.domain.warehouse.Warehouse;
import com.hk.exception.ExcelBlankFieldException;
import com.hk.pact.service.core.CityService;
import com.hk.pact.service.core.PincodeService;
import com.hk.pact.service.core.StateService;
import com.hk.pact.service.core.WarehouseService;
import com.hk.util.NumberUtil;
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
    @Autowired
    HubService hubService;

    private static Logger logger = LoggerFactory.getLogger(XslPincodeParser.class);


    public Set<Pincode> readPincodeList(File objInFile) throws Exception {

        Set<Pincode> pincodeSet = new HashSet<Pincode>();

        int rowCount = 1;
        ExcelSheetParser excel = new ExcelSheetParser(objInFile.getAbsolutePath(), "PincodeInfo", true);
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
                String nearestHubName = row.getColumnValue(XslConstants.NEAREST_HUB);
                String lastMileCostString = row.getColumnValue(XslConstants.LAST_MILE_COST);
                Double lastMileCost = null;
                if (StringUtils.isEmpty(pin) || StringUtils.isEmpty(cityName) || StringUtils.isEmpty(stateName)
                    || StringUtils.isEmpty(zoneName)) {
                    throw new ExcelBlankFieldException("Blank field for at row number " + rowCount);
                }
                if (pin.length() != 6) {
                    throw new ExcelBlankFieldException("Pincode should be of 6 digits only" + rowCount);
                }
                Pincode pincode = pincodeService.getByPincode(pin);
                if (pincode == null) {
                    pincode = new Pincode();
                }

                City city = cityService.getCityByName(cityName);
                State state = stateService.getStateByName(stateName);
                Zone zone = pincodeService.getZoneByName(zoneName);
                Hub nearestHub = hubService.findHubByName(nearestHubName);
                if (city == null || state == null || zone == null) {
                    throw new ExcelBlankFieldException("Invalid city/state/zone/nearestHub name at row " + rowCount);
                }
                try {
                  if(lastMileCostString!= null && !StringUtils.isEmpty(lastMileCostString))
                    lastMileCost = Double.parseDouble(lastMileCostString);
                } catch (NumberFormatException nfe) {
                  throw new ExcelBlankFieldException("Last mile cost should be a number only" + rowCount);
                }
                pincode.setPincode(pin);
                pincode.setCity(city);
                pincode.setState(state);
                pincode.setZone(zone);
                pincode.setLocality(locality);
                pincode.setRegion(region);
                pincode.setNearestHub(nearestHub);
                pincode.setLastMileCost(lastMileCost);
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

        int totalColumnNo = 8;

        Cell cell;
        for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
            cell = row.createCell(columnNo);
            cell.setCellStyle(style);
        }
        setCellValue(row, 0, XslConstants.PINCODE);
        setCellValue(row, 1, XslConstants.CITY);
        setCellValue(row, 2, XslConstants.STATE);
        setCellValue(row, 3, XslConstants.LOCALITY);
        setCellValue(row, 4, XslConstants.ZONE);
        setCellValue(row, 5, XslConstants.NEAREST_HUB);
        setCellValue(row, 6, XslConstants.LAST_MILE_COST);

      int initialRowNo = 1;
        for (Pincode pincode : pincodeList) {

            row = sheet1.createRow(initialRowNo);
            for (int columnNo = 0; columnNo < totalColumnNo; columnNo++) {
                row.createCell(columnNo);
            }

            setCellValue(row, 0, pincode.getPincode());
            setCellValue(row, 1, pincode.getCity().getName());
            setCellValue(row, 2, pincode.getState().getName());
            setCellValue(row, 3, pincode.getLocality());
            if (pincode.getZone() != null) {
                setCellValue(row, 4, pincode.getZone().getName());
            }
            if (pincode.getNearestHub() != null) {
              setCellValue(row, 5, pincode.getNearestHub().getName());
            }
            setCellValue(row, 6, pincode.getLastMileCost());

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
                Double estimatedShippingCostDouble = estimatedShippingCost!=null ? Double.valueOf(estimatedShippingCost) : null;
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

                PincodeDefaultCourier pincodeDefaultCourier = pincodeCourierService.createPincodeDefaultCourier(pincode, courier, warehouse, isGroundShippingAvailable, isCODAvailable, estimatedShippingCostDouble);
                boolean isDefaultCourierApplicable = pincodeCourierService.isDefaultCourierApplicable(pincode, courier, isGroundShippingAvailable, isCODAvailable);
                PincodeDefaultCourier pincodeDefaultCourierDb = pincodeCourierService.getPincodeDefaultCourier(pincode, warehouse, isCODAvailable, isGroundShippingAvailable);
                if (pincodeDefaultCourierDb == null && isDefaultCourierApplicable) {
                    pincodeDefaultCouriers.add(pincodeDefaultCourier);
                }
                if (pincodeDefaultCourierDb != null && isDefaultCourierApplicable) {
                    pincodeDefaultCourierDb.setCourier(pincodeDefaultCourier.getCourier());
                    pincodeDefaultCouriers.add(pincodeDefaultCourierDb);
                }
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
