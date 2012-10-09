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

import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.util.XslUtil;
import com.hk.constants.XslConstants;
import com.hk.domain.core.Pincode;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierServiceInfo;
import com.hk.exception.ExcelBlankFieldException;
import com.hk.pact.service.core.PincodeService;
import com.hk.util.io.ExcelSheetParser;
import com.hk.util.io.HKRow;

/**
 * Created by IntelliJ IDEA.
 * User: Ankit
 * Date: Sep 24, 2012
 * Time: 5:52:32 PM
 * To change this template use File | Settings | File Templates.
 */

@Component
public class XslCourierServiceInfoParser {
    private static Logger logger = LoggerFactory.getLogger(XslCourierServiceInfoParser.class);

    @Autowired
    private CourierService courierService;
    @Autowired
    private PincodeService pincodeService;

    public Set<CourierServiceInfo> readCourierServiceInfoList(File objInFile) throws Exception {
        logger.debug("parsing courier service info : " + objInFile.getAbsolutePath());
//          CourierServiceInfo courierServiceInfo = new CourierServiceInfo();
        Set<CourierServiceInfo> courierServiceInfoList = new HashSet<CourierServiceInfo>();
        int rowCount = 1;
        ExcelSheetParser excel = new ExcelSheetParser(objInFile.getAbsolutePath(), "CourierServiceInfo", true);
        Iterator<HKRow> rowiterator = excel.parse();

        try {
            while (rowiterator.hasNext()) {
                rowCount++;
                CourierServiceInfo courierServiceInfo = new CourierServiceInfo();
                HKRow row = rowiterator.next();
//                String id = row.getColumnValue(XslConstants.ID );
                String lPincode = row.getColumnValue(XslConstants.PINCODE);
                String courierId = row.getColumnValue(XslConstants.COURIER_ID);
                String codAvailable = row.getColumnValue(XslConstants.COD_AVAILABLE);
                String deleted = row.getColumnValue(XslConstants.IS_DELETED);
                String preferred = row.getColumnValue(XslConstants.IS_PREFERRED);
                String preferredCod = row.getColumnValue(XslConstants.IS_PREFERRED_COD);
                String routingCode = row.getColumnValue(XslConstants.ROUTING_CODE);
                String groundShippingAvailable = row.getColumnValue(XslConstants.GROUND_SHIPPING_AVAILABLE);
                String codAvailableOnGroundShipping = row.getColumnValue(XslConstants.COD_ON_GROUND_SHIPPING);

                String id = row.getColumnValue(XslConstants.ID);
                if (StringUtils.isNotBlank(id)) {
                    courierServiceInfo.setId(getLong(id));
                }
                if (StringUtils.isBlank(lPincode)) {
                    throw new ExcelBlankFieldException("Pincode cannot be empty" + "    ", rowCount);
                }
                Long courierLongId = XslUtil.getLong(courierId.trim());
                Courier courier = courierService.getCourierById(courierLongId);
                if (courier == null) {
                    logger.error("courierId is not valid  " + courierId, rowCount);
                    throw new ExcelBlankFieldException("courierId is not valid  " + "    ", rowCount);
                }
                try {
                    if (lPincode != null) {
                        Pincode pincode = getPincodeService().getByPincode(lPincode);
                        if (pincode != null) {
                            courierServiceInfo.setPincode(pincode);
                            boolean isCODAvailable = StringUtils.isNotBlank(codAvailable) && codAvailable.trim().toLowerCase().equals("y") ? true : false;
                            courierServiceInfo.setCodAvailable(isCODAvailable);
                            courierServiceInfo.setCourier(courier);
                            boolean isDeleted = StringUtils.isNotBlank(deleted) && deleted.trim().toLowerCase().equals("y") ? true : false;
                            courierServiceInfo.setDeleted(isDeleted);
                            boolean isPreferred = StringUtils.isNotBlank(preferred) && preferred.trim().toLowerCase().equals("y") ? true : false;
                            courierServiceInfo.setPreferred(isPreferred);
                            boolean isPreferredCod = StringUtils.isNotBlank(preferredCod) && preferredCod.trim().toLowerCase().equals("y") ? true : false;
                            courierServiceInfo.setPreferredCod(isPreferredCod);
                            courierServiceInfo.setRoutingCode(routingCode);
                            boolean isGroundShippingAvailable = StringUtils.isNotBlank(groundShippingAvailable) && groundShippingAvailable.trim().toLowerCase().equals("y") ? true : false;
                            courierServiceInfo.setGroundShippingAvailable(isGroundShippingAvailable);
                            boolean iscodAvailableOnGroundShipping = StringUtils.isNotBlank(codAvailableOnGroundShipping) && codAvailableOnGroundShipping.trim().toLowerCase().equals("y") ? true : false;
                            courierServiceInfo.setCodAvailableOnGroundShipping(iscodAvailableOnGroundShipping);
                            if (iscodAvailableOnGroundShipping) {
                                courierServiceInfo.setCodAvailable(true);
                            }
                            courierServiceInfoList.add(courierServiceInfo);
                        }
                    }
                } catch (Exception e) {
                    logger.error("issue with pin = " + lPincode);
                }
            }

        }
        catch (Exception e) {
            logger.error("Exception @ Row:" + rowCount + e.getMessage());
            throw new Exception("Exception @ Row:" + rowCount, e);
        }
        return courierServiceInfoList;

    }

    public CourierService getCourierService() {
        return courierService;
    }

    public void setCourierService(CourierService courierService) {
        this.courierService = courierService;
    }


    public PincodeService getPincodeService() {
        return pincodeService;
    }

    public void setPincodeService(PincodeService pincodeService) {
        this.pincodeService = pincodeService;
    }

    private Long getLong(String value) {
        Long valueInLong = null;
        try {
            valueInLong = Long.parseLong(value.replace(".0", ""));
        } catch (Exception e) {

        }
        return valueInLong;
    }

}
