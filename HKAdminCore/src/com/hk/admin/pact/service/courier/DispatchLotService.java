package com.hk.admin.pact.service.courier;

import com.akube.framework.dao.Page;
import com.hk.domain.courier.*;
import com.hk.exception.ExcelBlankFieldException;
import com.hk.pact.dao.BaseDao;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.File;
import java.util.Date;
import java.util.List;

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

	public boolean dispatchLotHasShipment(DispatchLot dispatchLot, Shipment shipment);

	public File generateDispatchLotExcel(File xlsFile, List<DispatchLotHasShipment> dispatchLotHasShipmentList);

	public List<Shipment> getShipmentsForDispatchLot(DispatchLot dispatchLot);

	public String markShipmentsAsReceived(DispatchLot dispatchLot, List<String> gateOrderIdList);

	public List<DispatchLot> getDispatchLotsForShipment(Shipment shipment);

	public DispatchLotHasShipment getDispatchLotHasShipment(DispatchLot dispatchLot, Shipment shipment);

	public List<DispatchLotHasShipment> getDispatchLotHasShipmentListByDispatchLot(DispatchLot dispatchLot, String shipmentStatus);

	public DispatchLot cancelDispatchLot(DispatchLot dispatchLot);

	public List<String> getSourceAndDestinationListForDispatchLot();

	public boolean markDispatchLotReceived(DispatchLot dispatchLot);

	public List<String> getShipmentStatusForDispatchLot();

}
