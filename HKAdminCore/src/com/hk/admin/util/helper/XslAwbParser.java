package com.hk.admin.util.helper;


import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.util.XslUtil;
import com.hk.constants.XslConstants;
import com.hk.constants.courier.EnumAwbStatus;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.AwbStatus;
import com.hk.domain.warehouse.Warehouse;
import com.hk.exception.ExcelBlankFieldException;
import com.hk.pact.service.core.WarehouseService;
import com.hk.util.io.ExcelSheetParser;
import com.hk.util.io.HKRow;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
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
  private CourierService courierService;
  @Autowired
  private WarehouseService warehouseService;


  public Set<Awb> readAwbExcel(File file) throws Exception {
    logger.debug("parsing Awb info : " + file.getAbsolutePath());
    Set<Awb> awbSet = new HashSet<Awb>();
    int rowCount = 1;
    ExcelSheetParser excel = new ExcelSheetParser(file.getAbsolutePath(), "Sheet1", true);
    Iterator<HKRow> rowiterator = excel.parse();
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

          if (StringUtils.isEmpty(awbNumber) && cod.isEmpty() && warehouse.isEmpty() ) {
            if (awbSet.size() > 0) {
              return awbSet;
            }
            return null;

          } else {
            logger.error("courier id cannot be null/empty");
            throw new ExcelBlankFieldException("courier ID  cannot be empty" + "    ", rowCount);
          }

        }

        Courier courier = courierService.getCourierById(XslUtil.getLong(courierId));
        if (courier == null) {
          logger.error("courierId is not valid  " + courierId, rowCount);
          throw new ExcelBlankFieldException("courierId is not valid  " + "    ", rowCount);
        }
        awb.setCourier(courier);
        if (StringUtils.isEmpty(awbNumber)) {
          logger.error("awbNumber cannot be null/empty");
          throw new ExcelBlankFieldException("awbNumber cannot be empty " + "    ", rowCount);
        }
        awb.setAwbNumber(awbNumber);
        awb.setAwbBarCode(awbNumber);
             AwbStatus awbStatus= new AwbStatus();
          awbStatus.setStatus(EnumAwbStatus.Unused.getStatus());
          awbStatus.setId(0L);
         awb.setAwbStatus(awbStatus);
        awb.setUsed(false);
        if (StringUtils.isEmpty(warehouse)) {
          logger.error("warehouse cannot be call");
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
        awbSet.add(awb);


      }


    } catch (ExcelBlankFieldException e) {
      throw new ExcelBlankFieldException(e.getMessage());

    }

    if (awbSet.size() > 0) {
      return awbSet;
    }

    return null;

  }

  public static List<String> getIntersection(List<Awb> awbDatabase, List<Awb> awbSetFromExcel) {
    List<String> commonCourierIds = new ArrayList<String>();
    for (int i = 0; i < awbSetFromExcel.size(); i++) {
      if (awbDatabase.contains(awbSetFromExcel.get(i))) {
        commonCourierIds.add(awbSetFromExcel.get(i).getAwbNumber());
      }

    }
    return commonCourierIds;
  }

}
