package com.hk.admin.util.helper;

import com.hk.admin.pact.dao.courier.CityCourierTATDao;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.util.XslUtil;
import com.hk.constants.XslConstants;
import com.hk.domain.core.City;
import com.hk.domain.courier.CityCourierTAT;
import com.hk.domain.courier.Courier;
import com.hk.exception.ExcelBlankFieldException;
import com.hk.pact.service.core.CityService;
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
 * Date: Jun 21, 2012
 * Time: 10:04:02 AM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class XslCityCourierTATParser {
  private static Logger logger = LoggerFactory.getLogger(XslCityCourierTATParser.class);
  @Autowired
  CityService cityService;
  @Autowired
  CourierService courierService;
  @Autowired
    CityCourierTATDao cityCourierTATDao;


  public Set<CityCourierTAT> readCityCourierTATExcel(File file) throws Exception {
    logger.debug("parsing CityCourierTAT info : " + file.getAbsolutePath());
    Set<CityCourierTAT> citySet = new HashSet<CityCourierTAT>();
    InputStream awbInputStream = new FileInputStream(file);
    POIFSFileSystem awbInFileSys = new POIFSFileSystem(awbInputStream);

    HSSFWorkbook workbook = new HSSFWorkbook(awbInFileSys);

    // Assuming there is only one sheet, the first one only will be picked
    HSSFSheet citySheet = workbook.getSheet("Sheet1");
    Iterator<Row> objRowIt = citySheet.rowIterator();
    Map<Integer, String> headerMap;
    Map<Integer, String> rowMap;
    int rowCount = 1;
    headerMap = getRowMap(objRowIt);
    try {
      while (objRowIt.hasNext()) {
        rowCount++;
        rowMap = getRowMap(objRowIt);
        String cityName = getCellValue(XslConstants.CITY, rowMap, headerMap);
        String courierId = getCellValue(XslConstants.COURIER_ID, rowMap, headerMap);
        String cityTAT = getCellValue(XslConstants.CITY_TAT, rowMap, headerMap);

        CityCourierTAT cityObj = new CityCourierTAT();
        if (StringUtils.isEmpty(cityName)) {
          if (StringUtils.isEmpty(cityTAT)) {
            if (citySet.size() > 0) {
              return citySet;
            }
            return null;

          } else {
            logger.error("city cannot be null/empty");
            throw new ExcelBlankFieldException("city  cannot be empty" + "    ", rowCount);
          }

        }
        City city = cityService.getCityByName(cityName);
        if (city == null) {
          logger.error("Invalid City , Check  for spelling  " + city, rowCount);
          throw new ExcelBlankFieldException("Invalid City , Check  for spelling   " + "    ", rowCount);
        }     

        if (StringUtils.isEmpty(courierId)) {
          logger.error("courier id cannot be null/empty");
          throw new ExcelBlankFieldException("courier ID  cannot be empty" + "    ", rowCount);
        }
        Courier courier = courierService.getCourierById(XslUtil.getLong(courierId));
        if (courier == null) {
          logger.error("courierId is not valid  " + courierId, rowCount);
          throw new ExcelBlankFieldException("courierId is not valid  " + "    ", rowCount);
        }

        if (StringUtils.isEmpty(cityTAT)) {
          logger.error(" cityTAT cannot be null/empty");
          throw new ExcelBlankFieldException("cityTAT cannot be empty " + "    ", rowCount);
        }
        CityCourierTAT cityCourierTAT=  cityCourierTATDao.getCityTatByCity(city); 
        if(cityCourierTAT != null){
         cityCourierTAT.setCity(city);
         cityCourierTAT.setCourier(courier);
        cityCourierTAT.setTurnaroundTime(XslUtil.getLong(cityTAT));
        citySet.add(cityCourierTAT);

        }
        else{
        cityObj.setCity(city);
         cityObj.setCourier(courier);
        cityObj.setTurnaroundTime(XslUtil.getLong(cityTAT));
        citySet.add(cityObj);
        }
      }
    } catch (ExcelBlankFieldException e) {
      throw new ExcelBlankFieldException(e.getMessage());

    }
    if (awbInputStream != null) {
      IOUtils.closeQuietly(awbInputStream);
    }

    if (citySet.size() > 0) {
      return citySet;
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

}
