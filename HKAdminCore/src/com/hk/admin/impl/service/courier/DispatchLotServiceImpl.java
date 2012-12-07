package com.hk.admin.impl.service.courier;

import com.akube.framework.dao.Page;
import com.hk.admin.pact.dao.courier.DispatchLotDao;
import com.hk.admin.pact.dao.shippingOrder.AdminShippingOrderDao;
import com.hk.admin.pact.service.courier.DispatchLotService;
import com.hk.constants.XslConstants;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.DispatchLot;
import com.hk.exception.ExcelBlankFieldException;
import com.hk.exception.HealthkartCheckedException;
import com.hk.util.io.ExcelSheetParser;
import com.hk.util.io.HKRow;
import com.restfb.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 12/4/12
 * Time: 4:34 PM
 * To change this template use File | Settings | File Templates.
 */

@Service
public class DispatchLotServiceImpl implements DispatchLotService {

	private static Logger logger = LoggerFactory.getLogger(DispatchLotServiceImpl.class);

	@Autowired
	private DispatchLotDao dispatchLotDao;
	@Autowired
	private AdminShippingOrderDao adminShippingOrderDao;

	/*public DispatchLot saveDispatchLot(DispatchLot dispatchLot, String docketNumber, Courier courier, String zone, String source,
		                                   String destination, Long noOfShipmentsSent, Long noOfShipmentsReceived, Long noOfMotherBags,
		                                   Double totalWeight, Date deliveryDate) {
		if(dispatchLot != null) {
			dispatchLot.setDocketNumber(docketNumber);
			dispatchLot.setCourier(courier);
			dispatchLot.setZone(zone);
			dispatchLot.setSource(source);
			dispatchLot.setDestination(destination);
			dispatchLot.setNoOfShipmentsSent(noOfShipmentsSent);
			dispatchLot.setNoOfShipmentsReceived(noOfShipmentsReceived);
			dispatchLot.setNoOfMotherBags(noOfMotherBags);
			dispatchLot.setTotalWeight(totalWeight);
			dispatchLot.setDeliveryDate(deliveryDate);
			baseDao.save(dispatchLot);
		}
		return dispatchLot;
	}
	*/
	public Page searchDispatchLot(DispatchLot dispatchLot, String docketNumber, Courier courier, String zone, String source,
	                              String destination, Date deliveryStartDate, Date deliveryEndDate, int pageNo, int perPage) {

		return getDispatchLotDao().searchDispatchLot(dispatchLot, docketNumber, courier, zone, source, destination,
				deliveryStartDate, deliveryEndDate, pageNo, perPage);
	}

	public void parseExcelAndSaveShipmentDetails(String excelFilePath, String sheetName) throws Exception {
		ExcelSheetParser parser = new ExcelSheetParser(excelFilePath, sheetName);
		Iterator<HKRow> rowIterator = parser.parse();
		int rowCount = 1;
		List<String> soGatewayOrderIdInExcel = new ArrayList<String>();
		try {
			while (rowIterator.hasNext()) {
				HKRow row = rowIterator.next();
				String soGatewayOrderId = row.getColumnValue(XslConstants.GATEWAY_ORDER_ID);
				if (StringUtils.isBlank(soGatewayOrderId)) {
					throw new ExcelBlankFieldException("SO Gateway Order Id Cannot be blank", rowCount);
				}

				soGatewayOrderIdInExcel.add(soGatewayOrderId);
				rowCount++;
			}
			List<String> soGatewayOrderIdInDBList = getAdminShippingOrderDao().getGatewayOrderList(soGatewayOrderIdInExcel);
			if (soGatewayOrderIdInDBList.size() < soGatewayOrderIdInExcel.size()) {
				soGatewayOrderIdInExcel.removeAll(soGatewayOrderIdInDBList);
				String invalidOrders = "";
				for (String soGatewayOrderId : soGatewayOrderIdInExcel) {
					invalidOrders += soGatewayOrderId + " ";
				}
				throw new ExcelBlankFieldException("Following gatewayOrderIds are Invalid : " + invalidOrders);
			}

		} catch (ExcelBlankFieldException e) {
			logger.error("Exception @ Row:" + (rowCount + 1) + e.getMessage());
			//throw new Exception("Exception @ Row:" + (rowCount + 1) + ": " + e.getMessage(), e);
		}

	}

	public DispatchLot save(DispatchLot dispatchLot) {
		return (DispatchLot) getDispatchLotDao().save(dispatchLot);
	}

	public DispatchLotDao getDispatchLotDao() {
		return dispatchLotDao;
	}

	public void setDispatchLotDao(DispatchLotDao dispatchLotDao) {
		this.dispatchLotDao = dispatchLotDao;
	}

	public AdminShippingOrderDao getAdminShippingOrderDao() {
		return adminShippingOrderDao;
	}

	public void setAdminShippingOrderDao(AdminShippingOrderDao adminShippingOrderDao) {
		this.adminShippingOrderDao = adminShippingOrderDao;
	}
}
