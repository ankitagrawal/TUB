package com.hk.admin.util.helper;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.hk.admin.pact.service.courier.CityCourierTATService;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.util.XslUtil;
import com.hk.constants.XslConstants;
import com.hk.domain.core.City;
import com.hk.domain.courier.CityCourierTAT;
import com.hk.domain.courier.Courier;
import com.hk.exception.ExcelBlankFieldException;
import com.hk.pact.service.core.CityService;
import com.hk.util.io.ExcelSheetParser;
import com.hk.util.io.HKRow;

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
  CityCourierTATService cityCourierTATService;



  public Set<CityCourierTAT> readCityCourierTATExcel(File file) throws Exception {
    logger.debug("parsing CityCourierTAT info : " + file.getAbsolutePath());
    Set<CityCourierTAT> citySet = new HashSet<CityCourierTAT>();
    ExcelSheetParser excel = new ExcelSheetParser(file.getAbsolutePath(), "Sheet1", true);
    Iterator<HKRow> rowiterator = excel.parse();
     int rowCount = 1;

    try {
      while (rowiterator.hasNext()) {
        rowCount++;
      HKRow row=  rowiterator.next();
        String cityName = row.getColumnValue(XslConstants.CITY);
        String courierId = row.getColumnValue(XslConstants.COURIER_ID);
        String cityTAT = row.getColumnValue(XslConstants.CITY_TAT);

        CityCourierTAT cityObj = new CityCourierTAT();
        if (StringUtils.isEmpty(cityName)) {
          if (StringUtils.isEmpty(cityTAT) && StringUtils.isEmpty(cityTAT) ) {
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
        CityCourierTAT cityCourierTAT=  cityCourierTATService.getCityTatByCity(city); 
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
       if (citySet.size() > 0) {
      return citySet;
    }

    return null;

  }

}
