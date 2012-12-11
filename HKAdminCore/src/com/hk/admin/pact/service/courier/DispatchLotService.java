package com.hk.admin.pact.service.courier;

import com.akube.framework.dao.Page;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.DispatchLot;
import com.hk.domain.courier.DispatchLotStatus;
import com.hk.domain.courier.Zone;
import com.hk.exception.ExcelBlankFieldException;
import com.hk.pact.dao.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 12/4/12
 * Time: 4:33 PM
 * To change this template use File | Settings | File Templates.
 */
public interface DispatchLotService {

	public DispatchLot save(DispatchLot dispatchLot);

	public Page searchDispatchLot(DispatchLot dispatchLot, String docketNumber, Courier courier, Zone zone, String source,
		                              String destination, Date deliveryStartDate, Date deliveryEndDate, DispatchLotStatus dispatchLotStatus, int pageNo, int perPage);

	public void parseExcelAndSaveShipmentDetails(DispatchLot dispatchLot, String excelFilePath, String sheetName);

}
