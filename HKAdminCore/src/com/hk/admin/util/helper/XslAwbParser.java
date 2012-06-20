package com.hk.admin.util.helper;


import com.hk.admin.pact.dao.courier.CourierDao;
import com.hk.admin.util.XslUtil;
import com.hk.constants.XslConstants;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.warehouse.Warehouse;
import com.hk.exception.ExcelBlankFieldException;
import com.hk.pact.dao.warehouse.WarehouseDao;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Row;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User:User
 * Date: Jun 14, 2012
 * Time: 3:12:57 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class XslAwbParser {

  private static Logger logger = LoggerFactory.getLogger(XslAwbParser.class);
   @Autowired
  private CourierDao courierDao;
  @Autowired
  private WarehouseDao warehouseDao;


  public Set<Awb> readAwbExcel(File file) throws Exception {
     logger.debug("parsing Awb info : " + file.getAbsolutePath());
     Set<Awb> awbSet = new HashSet<Awb>();
     InputStream awbInputStream = new FileInputStream(file);
     POIFSFileSystem awbInFileSys = new POIFSFileSystem(awbInputStream);

     HSSFWorkbook workbook = new HSSFWorkbook(awbInFileSys);

     // Assuming there is only one sheet, the first one only will be picked
     HSSFSheet awbSheet = workbook.getSheet("Sheet1");
     Iterator<Row> objRowIt = awbSheet.rowIterator();
     Map<Integer, String> headerMap;
     Map<Integer, String> rowMap;
     int rowCount = 1;
     headerMap = getRowMap(objRowIt);
     try {
       while (objRowIt.hasNext()) {
         rowCount++;
         rowMap = getRowMap(objRowIt);
         String courierId = getCellValue(XslConstants.COURIER_ID, rowMap, headerMap);
         String awbNumber = getCellValue(XslConstants.AWB_NUMBER, rowMap, headerMap);
         String cod = getCellValue(XslConstants.COD, rowMap, headerMap);
         String warehouse=getCellValue(XslConstants.WAREHOUSE, rowMap, headerMap);

         Awb awb = new Awb();
         if (StringUtils.isEmpty(courierId)) {

           if (StringUtils.isEmpty(awbNumber) && cod.isEmpty()) {
             if (awbSet.size() > 0) {
               return awbSet;
             }
             return null;

           } else {
             logger.error("courier id cannot be null/empty");
             throw new ExcelBlankFieldException("courier ID  cannot be empty" + "    ", rowCount);
           }

         }

         Courier courier = courierDao.getCourierById(XslUtil.getLong(courierId));
         if(courier == null ){
          logger.error("courierId is not valid  "+courierId ,rowCount);
           throw new ExcelBlankFieldException("courierId is not valid  " + "    ", rowCount); 
         }
         awb.setCourier(courier);
         if (StringUtils.isEmpty(awbNumber)) {
           logger.error("awbNumber cannot be null/empty");
           throw new ExcelBlankFieldException("awbNumber cannot be empty " + "    ", rowCount);
         }
         awb.setAwbNumber(awbNumber);
         awb.setAwbBarCode(awbNumber);
         awb.setUsed(false);
        if (StringUtils.isEmpty(warehouse)) {
           logger.error("warehouse cannot be call");
           throw new ExcelBlankFieldException("warehouse cannot be empty " + "    ", rowCount);
         }
         Warehouse warehoused=warehouseDao.get(Warehouse.class,XslUtil.getLong(warehouse));
           if(warehoused == null ){
          logger.error("warehouseId is not valid  "+warehouse ,rowCount);
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
         awbSet.add(awb);


       }


     } catch (ExcelBlankFieldException e) {
       throw new ExcelBlankFieldException(e.getMessage());

     }
     if (awbInputStream != null) {
       IOUtils.closeQuietly(awbInputStream);
     }

     if (awbSet.size() > 0) {
       return awbSet;
     }

     return null;

   }


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

     public static List<String> getIntersection(List<Awb> awbDatabase, List<Awb> awbSetFromExcel) {
    List<String> commonCourierIds = new ArrayList<String>();
       for(int i=0; i < awbSetFromExcel.size() ;i++) {
      if (awbDatabase.contains(awbSetFromExcel.get(i))) {
        commonCourierIds.add(awbSetFromExcel.get(i).getAwbNumber());
       }

    }
    return commonCourierIds;
  }

}
