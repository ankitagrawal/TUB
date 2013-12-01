package com.hk.admin.util.helper;


import java.io.File;
import java.util.*;

import com.hk.util.io.HkXlsWriter;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.util.XslUtil;
import com.hk.constants.XslConstants;
import com.hk.constants.courier.EnumAwbStatus;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.warehouse.Warehouse;
import com.hk.exception.DuplicateAwbexception;
import com.hk.exception.ExcelBlankFieldException;
import com.hk.pact.service.core.WarehouseService;
import com.hk.util.io.ExcelSheetParser;
import com.hk.util.io.HKRow;
import com.hk.util.io.HkXlsWriter;

/**
 * Created by IntelliJ IDEA.
 * User:Seema
 * Date: Jun 14, 2012
 * Time: 3:12:57 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class XslAwbParser {

    private static Logger logger = LoggerFactory.getLogger(XslAwbParser.class);
    @Autowired
    private CourierService courierService;
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    AwbService awbService;
    private Map<Courier,List<String>> courierWithAllAwbsInExcel = null;

    public List<Awb> readAwbExcel(File file) throws Exception {

        logger.debug("parsing Awb info : " + file.getAbsolutePath());
        List<Awb> awbList = new ArrayList<Awb>();
        int rowCount = 1;
        ExcelSheetParser excel = new ExcelSheetParser(file.getAbsolutePath(), "Sheet1", true);
        Iterator<HKRow> rowiterator = excel.parse();
        // Map has < each courier in excel , List of Awb of particular courier>
        courierWithAllAwbsInExcel=new HashMap<Courier,List<String>>();
        try {
            while (rowiterator.hasNext()) {              
                rowCount++;
                HKRow row = rowiterator.next();
                String courierId = row.getColumnValue(XslConstants.COURIER_ID);
                String awbNumber = row.getColumnValue(XslConstants.AWB_NUMBER);
                String cod = row.getColumnValue(XslConstants.COD);
                String warehouse = row.getColumnValue(XslConstants.WAREHOUSE);
                Awb awb = new Awb();
                if (StringUtils.isEmpty(courierId)) {

                    if ((awbNumber == null || StringUtils.isEmpty(awbNumber)) && (cod == null || cod.isEmpty()) && (warehouse == null || warehouse.isEmpty()) ) {
                        if (awbList.size() > 0) {
                            return awbList;
                        }
                        return null;

                    } else {
                        logger.error("courier id cannot be null/empty");
                        throw new ExcelBlankFieldException("courier ID  cannot be empty" + "    ", rowCount);
                    }

                }
                Long courierLongId = XslUtil.getLong(courierId.trim());
                Courier courier = courierService.getCourierById(courierLongId);
                if (courier == null) {
                    logger.error("courierId is not valid  " + courierId, rowCount);
                    throw new ExcelBlankFieldException("courierId is not valid  " + "    ", rowCount);
                }
                awb.setCourier(courier);
                if (StringUtils.isEmpty(awbNumber)) {
                    logger.error("awbNumber cannot be null/empty");
                    throw new ExcelBlankFieldException("awbNumber cannot be empty " + "    ", rowCount);                         
                }
                if (courierWithAllAwbsInExcel.containsKey(courier)) {
                    awbNumber = awbNumber.trim();
                    List<String> awbsOfCourier = courierWithAllAwbsInExcel.get(courier);
                    if (awbsOfCourier.contains(awbNumber)) {
                        throw new DuplicateAwbexception("DUPLICATE VALUES IN EXCEL :  ", courier, awbNumber);
                    } else {
                        awbsOfCourier.add(awbNumber);
                    }
                } else {
                    List<String> newAwbList = new ArrayList<String>();
                    newAwbList.add(awbNumber);
                    courierWithAllAwbsInExcel.put(courier, newAwbList);

                }
                awb.setAwbNumber(awbNumber);
                awb.setAwbBarCode(awbNumber);
                awb.setAwbStatus(EnumAwbStatus.Unused.getAsAwbStatus());                
                if (StringUtils.isEmpty(warehouse)) {
                    logger.error("warehouse cannot be empty");
                    throw new ExcelBlankFieldException("warehouse cannot be empty " + "    ", rowCount);
                }
                Warehouse warehoused = warehouseService.getWarehouseById(XslUtil.getLong(warehouse));
                if (warehoused == null) {
                    logger.error("warehouseId is not valid  " + warehouse, rowCount);
                    throw new ExcelBlankFieldException("warehouseID is not valid  " + "    ", rowCount);
                }
                awb.setWarehouse(warehoused);
                if ((StringUtils.isEmpty(cod))) {
                    throw new ExcelBlankFieldException("COD cannot be empty " + "    ", rowCount);
                }
                if (XslUtil.getLong(cod).equals(1l)) {
                    awb.setCod(true);
                } else if (XslUtil.getLong(cod).equals(0l)) {
                    awb.setCod(false);
                }
                awbList.add(awb);
            }


        } catch (ExcelBlankFieldException e) {
            throw new ExcelBlankFieldException(e.getMessage());

        }

        if (awbList.size() > 0) {
            return awbList;
        }

        return null;

    }

    public Map<Courier, List<String>> getCourierWithAllAwbsInExcel() {
        return courierWithAllAwbsInExcel;
    }

	  // AWB excel Generator
	public File generateAwbExcel(List<Awb> awbList, File xlsFile) {
		HkXlsWriter xlsWriter = new HkXlsWriter();
		int xlsRow = 1;
		xlsWriter.addHeader(XslConstants.COURIER_ID, XslConstants.COURIER_ID);
		xlsWriter.addHeader(XslConstants.AWB_NUMBER, XslConstants.AWB_NUMBER);
		xlsWriter.addHeader(XslConstants.COD, XslConstants.COD);
		xlsWriter.addHeader(XslConstants.WAREHOUSE, XslConstants.WAREHOUSE);
		xlsWriter.addHeader(XslConstants.STATUS, XslConstants.STATUS);

		for (Awb awb : awbList) {
			xlsWriter.addCell(xlsRow, awb.getCourier().getId());
			xlsWriter.addCell(xlsRow, awb.getAwbNumber());
			String cod = "0";
			if (awb.getCod()) {
				cod = "1";
			}
			xlsWriter.addCell(xlsRow, cod);
			xlsWriter.addCell(xlsRow, awb.getWarehouse().getId());
			xlsWriter.addCell(xlsRow,awb.getAwbStatus().getStatus());
			xlsRow++;
		}
		xlsWriter.writeData(xlsFile, "Sheet1");
		return xlsFile;
	}
}
