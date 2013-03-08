package com.hk.admin.manager;

import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.hk.admin.pact.dao.courier.CourierDao;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.shippingOrder.AdminShippingOrderService;
import com.hk.pact.service.shippingOrder.ShipmentService;
import com.hk.admin.util.ChhotuCourierDelivery;
import com.hk.admin.util.CourierStatusUpdateHelper;
import com.hk.admin.util.courier.thirdParty.IndiaOntimeCourierTrack;
import com.hk.admin.dto.courier.thirdParty.ThirdPartyTrackDetails;
import com.hk.constants.courier.CourierConstants;
import com.hk.constants.courier.EnumAwbStatus;
import com.hk.constants.courier.EnumCourier;
import com.hk.constants.report.ReportConstants;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.courier.Awb;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.Shipment;
import com.hk.domain.order.ShippingOrder;
import com.hk.exception.HealthkartCheckedException;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.service.shippingOrder.ShippingOrderService;

@SuppressWarnings("unchecked")
@Component
public class DeliveryStatusUpdateManager {

	private static Logger logger = LoggerFactory.getLogger(DeliveryStatusUpdateManager.class);

	ShippingOrder shippingOrder;
	int orderDeliveryCount = 0;
	int ordersDelivered = 0;
	String trackingId = "";
	String courierName = "Delivered by ";
	public static final int digitsInGatewayId = 5;
	List<ShippingOrder> shippingOrderList = new ArrayList<ShippingOrder>();
	List<String> unmodifiedTrackingIds = null;
	List<Long> courierIdList = null;
	int startIndex = 0;
	int endIndex = 0;
	//Map<String,Object>          jsonResponseMap               = new HashMap<String,Object>();

	LineItemDao lineItemDaoProvider;

	@Autowired
	AdminShippingOrderService adminShippingOrderService;

	@Autowired
	ShippingOrderService shippingOrderService;

	@Autowired
	CourierStatusUpdateHelper courierStatusUpdateHelper;
	@Autowired
	AwbService awbService;
	@Autowired
	ShipmentService shipmentService;

	@Autowired
	CourierDao courierDao;

	public String updateCourierDeliveryStatusByExcel(File excelFile) throws Exception {
		String messagePostUpdation = "";
		logger.debug("parsing Delivery Details DTDC Upload : " + excelFile.getAbsolutePath());
		POIFSFileSystem objInFileSys = new POIFSFileSystem(new FileInputStream(excelFile));

		HSSFWorkbook workbook = new HSSFWorkbook(objInFileSys);
		HSSFCellStyle cellStyle = workbook.createCellStyle();
		HSSFDataFormat dataFormat = workbook.createDataFormat();
		cellStyle.setDataFormat(dataFormat.getFormat("text"));

		// Assuming there is only one sheet, the first one only will be picked
		HSSFSheet dtdcDeliverySheet = workbook.getSheetAt(0);
		Iterator<Row> objRowIt = dtdcDeliverySheet.rowIterator();
		Map<Integer, String> headerMap;
		Map<Integer, String> rowMap;
		int rowCount = 0;
		SimpleDateFormat sdf_date = new SimpleDateFormat("dd/MM/yyyy");
		try {
			headerMap = getRowMap(objRowIt);
			while (objRowIt.hasNext()) {
				rowCount++;
				rowMap = getRowMapStringFormat(objRowIt);
				String gatewayOrderIdInXls = getCellValue(ReportConstants.REF_NO, rowMap, headerMap);
				String awb = getCellValue(ReportConstants.CN_NO, rowMap, headerMap);
				String status = getCellValue(ReportConstants.STATUS, rowMap, headerMap);
				String courierId = getCellValue(ReportConstants.COURIER_ID_STR, rowMap, headerMap);
				if (StringUtils.isBlank(courierId)) {
					messagePostUpdation += "courierId(courierId) at   @Row No." + (rowCount + 1) + " isnull<br/>";
					continue;
				}
				Long courier_Id = Long.parseLong(courierId.trim());
				Date deliveryDateInXsl = null;

				try {
					deliveryDateInXsl = sdf_date.parse(getCellValue(ReportConstants.DELIVERED_DATE, rowMap, headerMap));
				} catch (Exception e) {
					messagePostUpdation += "Date is not in correct format @Row No." + (rowCount + 1) + "<br/>";
					continue;
				}
				ShippingOrder shippingOrder = null;
				if (StringUtils.isBlank(awb)) {
					messagePostUpdation += "CNNO cannot be blank @Row No." + (rowCount + 1) + "<br/>";
					continue;
				}
				if (StringUtils.isBlank(gatewayOrderIdInXls) && StringUtils.isBlank(awb)) {
					messagePostUpdation += "Ref. No. and CNNO cannot be null simultaneously @Row No. " + (rowCount + 1) + "<br/>";
					continue;
				}
				if (StringUtils.isNotBlank(gatewayOrderIdInXls)) {
					shippingOrder = getShippingOrderService().findByGatewayOrderId(gatewayOrderIdInXls);
				} else {
					Courier courier = courierDao.get(Courier.class, courier_Id);
					Awb shipmentAwb = awbService.getAvailableAwbForCourierByWarehouseCodStatus(courier, awb.trim(), null, null, EnumAwbStatus.Used.getAsAwbStatus());
					if (shipmentAwb == null) {
						messagePostUpdation += "Awb number(Tracking_id) at   @Row No." + (rowCount + 1) + " does not exist in Healthkart system<br/>";
						continue;
					}
					shippingOrder = shipmentService.findByAwb(shipmentAwb).getShippingOrder();
				}
				if (shippingOrder == null) {
					messagePostUpdation += "Shipping order not found corresponding to the Ref No. @Row No." + (rowCount + 1) + "<br/>";
					continue;
				}
				Shipment shipment = shippingOrder.getShipment();
				if (shipment == null) {
					messagePostUpdation += "Shipment not found corresponding to Row No." + (rowCount + 1) + "<br/>";
					continue;
				}
				if (status.equalsIgnoreCase("DELIVERED")) {
					updateCourierDeliveryStatus(shippingOrder, shipment, awb, deliveryDateInXsl);
				}
			}
		} catch (Exception e) {
			logger.error("Exception @ Row:" + rowCount, e);
			throw new Exception("Exception @ Row:" + rowCount, e);
		}
		logger.debug("message post updation " + messagePostUpdation);
		return messagePostUpdation;
	}

	public int updateCourierStatus(Date startDate, Date endDate, String courierName) throws HealthkartCheckedException {
		ordersDelivered = 0;
		orderDeliveryCount = 0;
		unmodifiedTrackingIds = new ArrayList<String>();

		if (courierName.equalsIgnoreCase(CourierConstants.AFL)) {
			courierIdList = new ArrayList<Long>();
			courierIdList.add(EnumCourier.AFLWiz.getId());
			shippingOrderList = getAdminShippingOrderService().getShippingOrderListByCouriers(startDate, endDate, courierIdList);
			if (shippingOrderList != null && shippingOrderList.size() > 0) {
				for (ShippingOrder shippingOrderInList : shippingOrderList) {
					trackingId = shippingOrderInList.getShipment().getAwb().getAwbNumber();
					try {
						Date deliveryDate = courierStatusUpdateHelper.updateDeliveryStatusAFL(trackingId);
						if (deliveryDate != null) {
							ordersDelivered = updateCourierDeliveryStatus(shippingOrderInList, shippingOrderInList.getShipment(),
									shippingOrderInList.getShipment().getAwb().getAwbNumber(), deliveryDate);
						} else {
							logger.debug("Delivery date not available or status is not delivered for Tracking Id (AFL): " + shippingOrderInList.getShipment().getAwb().getAwbNumber());
							unmodifiedTrackingIds.add(trackingId);

						}
					} catch (Exception e) {
						logger.debug(CourierConstants.EXCEPTION + trackingId);
						continue;
					}

				}
			}
		} else if (courierName.equalsIgnoreCase(CourierConstants.CHHOTU)) {
			courierIdList = new ArrayList<Long>();
			courierIdList.add(EnumCourier.Chhotu.getId());
			shippingOrderList = getAdminShippingOrderService().getShippingOrderListByCouriers(startDate, endDate, courierIdList);
			if (shippingOrderList != null && shippingOrderList.size() > 0) {
				for (ShippingOrder shippingOrderInList : shippingOrderList) {
					trackingId = shippingOrderInList.getShipment().getAwb().getAwbNumber();
					try {
						ChhotuCourierDelivery chhotuCourierDelivery = courierStatusUpdateHelper.updateDeliveryStatusChhotu(trackingId);
						Date delivery_date = null;
						if (chhotuCourierDelivery != null) {
							delivery_date = chhotuCourierDelivery.getFormattedDeliveryDate();
						} else {
							unmodifiedTrackingIds.add(trackingId);
						}

						if (delivery_date != null && chhotuCourierDelivery.getShipmentStatus().equalsIgnoreCase(CourierConstants.DELIVERED) && chhotuCourierDelivery.getTrackingId() != null) {
							ordersDelivered = updateCourierDeliveryStatus(shippingOrderInList, shippingOrderInList.getShipment(), shippingOrderInList.getShipment().getAwb().getAwbNumber(), delivery_date);
						} else {
							logger.debug("Delivery date not available or status is not delivered for Tracking Id (Chhotu): " + shippingOrderInList.getShipment().getAwb().getAwbNumber());
						}
					} catch (Exception ex) {
						unmodifiedTrackingIds.add(trackingId);
						continue;
					}
				}
			}
		} else if (courierName.equalsIgnoreCase(CourierConstants.DELHIVERY)) {
			courierIdList = new ArrayList<Long>();
			courierIdList = EnumCourier.getDelhiveryCourierIds();
			shippingOrderList = getAdminShippingOrderService().getShippingOrderListByCouriers(startDate, endDate, courierIdList);
			List<ShippingOrder> shippingOrderSubList = new ArrayList<ShippingOrder>();
			JsonArray jsonShipmentDataArray = new JsonArray();
			int listSize = shippingOrderList.size();
			int batchSize = 70;
			startIndex = 0;
			endIndex = 0;

			if (shippingOrderList != null && listSize > 0) {
				//Checking if shippingOrderList size is > batchSize then we wud divide it into batches of 10 orders each.
				if (shippingOrderList.size() > batchSize) {
					for (int i = 0; i < listSize; i++) {
						//For the first time it wud be 0 and its value won't increment to 1.Bt later it would increment so need to subtract 1 to get correcr startIndex.
						if (i == 0) {
							startIndex = i;
						} else {
							startIndex = i - 1;
						}
						//checking if remaining elements are less than batchSize,then adding that count to the index to get correct endIndex.
						if (listSize - endIndex < batchSize) {
							endIndex = (startIndex + (listSize - endIndex)) - 1;
						} else {
							endIndex = startIndex + batchSize;
						}
						//Breaking the original list into batches of batchSize or the remaining size.
						try {
							shippingOrderSubList = shippingOrderList.subList(startIndex, endIndex);

							trackingId = getAppendedTrackingIdsString(shippingOrderSubList);

							if (trackingId != null) {
								//getting the JsonResponeArray for batch of trackingIds
								jsonShipmentDataArray = courierStatusUpdateHelper.bulkUpdateDeliveryStatusDelhivery(trackingId);
							}
						} catch (Exception ex) {
							logger.debug(CourierConstants.EXCEPTION + "(Delhivery)" + trackingId);
							unmodifiedTrackingIds.add(trackingId);
							continue;
						}
						if (jsonShipmentDataArray != null && jsonShipmentDataArray.size() > 0) {
							ordersDelivered = updateDelhiveryStatus(jsonShipmentDataArray);
						}
						i = endIndex;
					}
				} else {
					//Constructing trackingId using all shippingOrders
					trackingId = getAppendedTrackingIdsString(shippingOrderList);
					if (trackingId != null) {
						//getting the JsonResponeArray for batch of trackingIds
						jsonShipmentDataArray = courierStatusUpdateHelper.bulkUpdateDeliveryStatusDelhivery(trackingId);
					}
					if (jsonShipmentDataArray != null && jsonShipmentDataArray.size() > 0) {
						ordersDelivered = updateDelhiveryStatus(jsonShipmentDataArray);
					}
				}
			}


		} else if (courierName.equalsIgnoreCase(CourierConstants.BLUEDART)) {
			//SimpleDateFormat sdf_date = new SimpleDateFormat("dd MMMMM yyyy");
			courierIdList = new ArrayList<Long>();
			courierIdList = EnumCourier.getBlueDartCouriers();
			shippingOrderList = getAdminShippingOrderService().getShippingOrderListByCouriers(startDate, endDate, courierIdList);

			List<ShippingOrder> shippingOrderSubList;
			List<Element> elementList = new ArrayList();
			int listSize = shippingOrderList.size();
			int batchSize = 50;
			startIndex = 0;
			endIndex = 0;

			if (shippingOrderList != null && listSize > 0) {
				//Checking if shippingOrderList size is > batchSize then we wud divide it into batches of 10 orders each.
				if (shippingOrderList.size() > batchSize) {
					for (int i = 0; i < listSize; i++) {
						//For the first time it wud be 0 and its value won't increment to 1.Bt later it would increment so need to subtract 1 to get correcr startIndex.
						if (i == 0) {
							startIndex = i;
						} else {
							startIndex = i - 1;
						}
						//checking if remaining elements are less than batchSize,then adding that count to the index to get correct endIndex.
						if (listSize - endIndex < batchSize) {
							endIndex = (startIndex + (listSize - endIndex)) - 1;
						} else {
							endIndex = startIndex + batchSize;
						}
						//Breaking the original list into batches of batchSize or the remaining size.
						try {
							shippingOrderSubList = shippingOrderList.subList(startIndex, endIndex);

							trackingId = getAppendedTrackingIdsString(shippingOrderSubList);

							if (trackingId != null) {
								//getting the response for batch of trackingIds
								elementList = courierStatusUpdateHelper.bulkUpdateDeliveryStatusBlueDart(trackingId);

							}
						} catch (Exception ex) {
							logger.debug(CourierConstants.EXCEPTION + trackingId);
							unmodifiedTrackingIds.add(trackingId);
							continue;
						}
						if (elementList != null && elementList.size() > 0) {
							ordersDelivered = updateBlueDartStatus(elementList, shippingOrderSubList);
						}
						i = endIndex;
					}
				} else {
					//Constructing trackingId using all shippingOrders
					try {
						trackingId = getAppendedTrackingIdsString(shippingOrderList);
						if (trackingId != null) {
							//getting the response for batch of trackingIds
							elementList = courierStatusUpdateHelper.bulkUpdateDeliveryStatusBlueDart(trackingId);
						}
					}
					catch (Exception ex) {
						logger.debug(CourierConstants.EXCEPTION + trackingId);
						unmodifiedTrackingIds.add(trackingId);
					}
					if (elementList != null && elementList.size() > 0) {
						ordersDelivered = updateBlueDartStatus(elementList, shippingOrderList);
					}
				}
			}

		} else if (courierName.equalsIgnoreCase(CourierConstants.DTDC)) {
			courierIdList = new ArrayList<Long>();
			courierIdList = EnumCourier.getDTDCCouriers();
			shippingOrderList = getAdminShippingOrderService().getShippingOrderListByCouriers(startDate, endDate, courierIdList);
			Map<String, String> responseMap = new HashMap<String, String>();
			String courierDeliveryStatus = null;
			String deliveryDateString = null;
			if (shippingOrderList != null && shippingOrderList.size() > 0) {
				for (ShippingOrder shippingOrderInList : shippingOrderList) {
					trackingId = shippingOrderInList.getShipment().getAwb().getAwbNumber();
					try {
						responseMap = courierStatusUpdateHelper.updateDeliveryStatusDTDC(trackingId);
						if (responseMap != null) {
							for (Map.Entry entryObj : responseMap.entrySet()) {
								if (entryObj.getKey().equals(CourierConstants.DTDC_INPUT_STR_STATUS)) {
									courierDeliveryStatus = entryObj.getValue().toString();
								}
								if (entryObj.getKey().equals(CourierConstants.DTDC_INPUT_STR_STATUSTRANSON)) {
									deliveryDateString = entryObj.getValue().toString();
								}
							}

							if (courierDeliveryStatus != null && deliveryDateString != null) {
								if (courierDeliveryStatus.equals(CourierConstants.DTDC_INPUT_DELIVERED)) {
									String subStringDeliveryDate = null;
									if (deliveryDateString != null) {
										subStringDeliveryDate = deliveryDateString.substring(4, 8) + "-" + deliveryDateString.substring(2, 4) + "-" + deliveryDateString.substring(0, 2);
									}
									Date delivery_date = getFormattedDeliveryDate(subStringDeliveryDate);
									ordersDelivered = updateCourierDeliveryStatus(shippingOrderInList, shippingOrderInList.getShipment(), trackingId, delivery_date);
								}
							}

						} else {
							unmodifiedTrackingIds.add(trackingId);
						}
					} catch (Exception ex) {
						unmodifiedTrackingIds.add(trackingId);
						continue;
					}
				}

			}
		} else if (courierName.equalsIgnoreCase(CourierConstants.QUANTIUM)) {
			SimpleDateFormat sdf_date = new SimpleDateFormat("dd/MM/yyyy");
			courierIdList = new ArrayList<Long>();
			courierIdList.add(EnumCourier.Quantium.getId());
			shippingOrderList = getAdminShippingOrderService().getShippingOrderListByCouriers(startDate, endDate, courierIdList);
			Element responseElement;
			String courierDeliveryStatus = null;
			String deliveryDateString = null;

			if (shippingOrderList != null && shippingOrderList.size() > 0) {
				for (ShippingOrder shippingOrderInList : shippingOrderList) {
					trackingId = shippingOrderInList.getShipment().getAwb().getAwbNumber();
					logger.debug("response for tracking id :" + trackingId);
					try {
						responseElement = courierStatusUpdateHelper.updateDeliveryStatusQuantium(trackingId);
						if (responseElement != null) {
							String trckNo = responseElement.getChildText(CourierConstants.QUANTIUM_TRACKING_NO);
							String refId = responseElement.getChildText(CourierConstants.QUANTIUM_REF_NO);
							courierDeliveryStatus = responseElement.getChildText(CourierConstants.QUANTIUM_STATUS);
							deliveryDateString = responseElement.getChildText(CourierConstants.QUANTIUM_DELIVERY_DATE);
							if (courierDeliveryStatus != null && deliveryDateString != null) {
								logger.debug("status code :" + courierDeliveryStatus);
								if (courierDeliveryStatus.equalsIgnoreCase(CourierConstants.QUANTIUM_DELIVERED)) {
									if (refId != null && refId.equalsIgnoreCase(shippingOrderInList.getGatewayOrderId()) && trckNo.equalsIgnoreCase(trackingId)) {
										try {
											Date delivery_date = sdf_date.parse(deliveryDateString);
											ordersDelivered = updateCourierDeliveryStatus(shippingOrderInList, shippingOrderInList.getShipment(), trackingId, delivery_date);
										} catch (ParseException pe) {											
											logger.debug(CourierConstants.PARSE_EXCEPTION + trackingId);
											unmodifiedTrackingIds.add(trackingId);
										}
									}
								}
							}
						} else {
							unmodifiedTrackingIds.add(trackingId);
						}
					} catch (Exception e) {
						unmodifiedTrackingIds.add(trackingId);
					}
				}
			}
		} else if (courierName.equalsIgnoreCase(CourierConstants.INDIAONTIME)) {
			SimpleDateFormat sdf_date = new SimpleDateFormat("dd-MM-yyyy hh:mm");
			courierIdList = new ArrayList<Long>();
			courierIdList.add(EnumCourier.IndiaOnTime.getId());
			shippingOrderList = getAdminShippingOrderService().getShippingOrderListByCouriers(startDate, endDate, courierIdList);
			ThirdPartyTrackDetails courierTrack = null;
			String courierDeliveryStatus = null;
			String deliveryDateString = null;
			boolean statusReceived;

			if (shippingOrderList != null && shippingOrderList.size() > 0) {
				for (ShippingOrder shippingOrderInList : shippingOrderList) {
					trackingId = shippingOrderInList.getShipment().getAwb().getAwbNumber();
					statusReceived = false;
					try {
						courierTrack = courierStatusUpdateHelper.updateDeliveryStatusIndiaOntime(trackingId);
						if (courierTrack != null) {
							String trckNo = courierTrack.getTrackingNo();
							String refId = courierTrack.getReferenceNo();
							courierDeliveryStatus = courierTrack.getAwbStatus();
							deliveryDateString = courierTrack.getDeliveryDateString();
							if (courierDeliveryStatus != null && deliveryDateString != null) {
								if (courierDeliveryStatus.equalsIgnoreCase(CourierConstants.INDIAONTIME_DELIVERED)) {
									if (refId != null && refId.equalsIgnoreCase(shippingOrderInList.getGatewayOrderId()) && trckNo.equalsIgnoreCase(trackingId)) {
										try {
											Date delivery_date = sdf_date.parse(deliveryDateString);
											ordersDelivered = updateCourierDeliveryStatus(shippingOrderInList, shippingOrderInList.getShipment(), trackingId, delivery_date);

										} catch (ParseException pe) {
											logger.debug(CourierConstants.PARSE_EXCEPTION + trackingId);
											unmodifiedTrackingIds.add(trackingId);
										}
									}
								}
								statusReceived = true;
							}
						}
					} catch (Exception e) {
						logger.debug(CourierConstants.EXCEPTION + trackingId);
					}
					if(!statusReceived){
						unmodifiedTrackingIds.add(trackingId);
					}
				}
			}
		} else if (courierName.equalsIgnoreCase(CourierConstants.FEDEX)){
			SimpleDateFormat sdf_date = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
			courierIdList = new ArrayList<Long>();
			courierIdList = EnumCourier.getFedexCouriers();
			shippingOrderList = getAdminShippingOrderService().getShippingOrderListByCouriers(startDate, endDate, courierIdList);
			ThirdPartyTrackDetails courierTrack = null;
			boolean statusReceived;

			if (shippingOrderList != null && shippingOrderList.size() > 0) {
				for (ShippingOrder shippingOrderInList : shippingOrderList) {
					trackingId = shippingOrderInList.getShipment().getAwb().getAwbNumber();
					statusReceived = false;
					try {
						courierTrack = courierStatusUpdateHelper.updateDeliveryStatusFedex(trackingId);
						if (courierTrack != null) {
							String trckNo = courierTrack.getTrackingNo();
							String refId = courierTrack.getReferenceNo();
							String courierDeliveryStatus = courierTrack.getAwbStatus();
							String deliveryDateString = courierTrack.getDeliveryDateString();
							if (courierDeliveryStatus != null) {
								if (courierDeliveryStatus.equalsIgnoreCase(CourierConstants.FEDEX_DELIVERED)) {
									if (refId != null && refId.equalsIgnoreCase(shippingOrderInList.getGatewayOrderId()) && trckNo.equalsIgnoreCase(trackingId)) {
										try {
											Date delivery_date = sdf_date.parse(deliveryDateString);
											ordersDelivered = updateCourierDeliveryStatus(shippingOrderInList, shippingOrderInList.getShipment(), trackingId, delivery_date);
										} catch (ParseException pe) {
											logger.debug(CourierConstants.PARSE_EXCEPTION + trackingId);
											unmodifiedTrackingIds.add(trackingId);
										}
									}
								}
								statusReceived = true;
							}
						}
					} catch (Exception e) {
						logger.debug(CourierConstants.EXCEPTION + trackingId);
					}
					if(!statusReceived){
						unmodifiedTrackingIds.add(trackingId);
					}
				}
			}
		}
		return ordersDelivered;
	}

	public int updateCourierDeliveryStatus(ShippingOrder shippingOrder, Shipment shipment, String trackingId, Date deliveryDate) {
		Long shippingOrderStatusId = shippingOrder.getOrderStatus().getId();
		if (shipment != null && (shippingOrderStatusId.equals(EnumShippingOrderStatus.SO_Shipped.getId()) || shippingOrderStatusId.equals((EnumShippingOrderStatus.RTO_Initiated.getId()))) ) {
			if (shipment.getShipDate().after(deliveryDate) || deliveryDate.after(new Date())) {
				Calendar deliveryDateAsShipDatePlusOne = Calendar.getInstance();
				deliveryDateAsShipDatePlusOne.setTime(shipment.getShipDate());
				deliveryDateAsShipDatePlusOne.add(Calendar.DAY_OF_MONTH, 1);
				deliveryDate = deliveryDateAsShipDatePlusOne.getTime();
			}
			if (shipment.getAwb().getAwbNumber() != null && shipment.getAwb().getAwbNumber().equals(trackingId)) {
				shipment.setDeliveryDate(deliveryDate);
				getAdminShippingOrderService().markShippingOrderAsDelivered(shippingOrder);
				orderDeliveryCount++;
			}
		}
		return orderDeliveryCount;
	}

	private int updateDelhiveryStatus(JsonArray jsonShipmentDataArray) {
        String deliveryStatus = null;
        String deliveryDate = null;
        Date delivery_date = null;
        ShippingOrder shippingOrdr = null;
		List<Courier> couriers = new ArrayList<Courier>();
		for (Long courierId : EnumCourier.getDelhiveryCourierIds()){
				couriers.add(EnumCourier.getEnumCourierFromCourierId(courierId).asCourier());
		}


        //Iterating over the  jsonShipmentDataArray to extract the required values(AWB,Status,DeliveryDate) and putting
        //these into map<String,Object> ,AWB as key and  deliveryDate as value
        for (JsonElement jsonObj : jsonShipmentDataArray) {
            trackingId = jsonObj.getAsJsonObject().get(CourierConstants.DELHIVERY_SHIPMENT).getAsJsonObject().get(CourierConstants.DELHIVERY_AWB).getAsString();
            deliveryStatus = jsonObj.getAsJsonObject().get(CourierConstants.DELHIVERY_SHIPMENT).getAsJsonObject().get(CourierConstants.DELHIVERY_STATUS).getAsJsonObject().get(CourierConstants.DELHIVERY_STATUS).getAsString();
            deliveryDate = jsonObj.getAsJsonObject().get(CourierConstants.DELHIVERY_SHIPMENT).getAsJsonObject().get(CourierConstants.DELHIVERY_STATUS).getAsJsonObject().get(CourierConstants.DELHIVERY_STATUS_DATETIME).getAsString();
            if (trackingId != null && deliveryStatus != null && deliveryDate != null) {
                delivery_date = getFormattedDeliveryDate(deliveryDate);
                //if status is delivered then putting values in the map.
                if (deliveryStatus.equalsIgnoreCase(CourierConstants.DELIVERED)) {
                    Awb awb= awbService.findByCourierAwbNumber(couriers,trackingId);
                    Shipment shipment=shipmentService.findByAwb(awb);
                    shippingOrdr = shipment.getShippingOrder();
                    ordersDelivered=updateCourierDeliveryStatus(shippingOrdr, shippingOrdr.getShipment(), shippingOrdr.getShipment().getAwb().getAwbNumber(), delivery_date);
                }
            }
        }
        return ordersDelivered;
    }

	private int updateBlueDartStatus(List<Element> elementList, List<ShippingOrder> shippingOrderList) {
		//ShippingOrder shippingOrder;
		SimpleDateFormat sdf_date = new SimpleDateFormat("dd MMMMM yyyy");
		Iterator elementListIterator = elementList.listIterator();
		while (elementListIterator.hasNext()) {
			Element ele = (Element) elementListIterator.next();
			if (ele != null) {
				String status = ele.getChildText(CourierConstants.BLUEDART_STATUS);
				String statusDate = ele.getChildText(CourierConstants.BLUEDART_STATUS_DATE);
				String trackingId = ele.getAttributeValue(CourierConstants.BLUEDART_AWB);
				String refNo = ele.getAttributeValue(CourierConstants.BLUEDART_REF_NO);

				if (status.equals(CourierConstants.BLUEDART_SHIPMENT_DELIVERED) && statusDate != null) {
					try {
						for (ShippingOrder shippingOrder : shippingOrderList) {
							if (refNo != null && refNo.equalsIgnoreCase(shippingOrder.getGatewayOrderId())) {
								Date delivery_date = sdf_date.parse(statusDate);
								ordersDelivered = updateCourierDeliveryStatus(shippingOrder, shippingOrder.getShipment(), trackingId, delivery_date);
								break;
							}
						}
					}
					catch (ParseException pe) {
						logger.debug(CourierConstants.PARSE_EXCEPTION + trackingId);
						unmodifiedTrackingIds.add(trackingId);
					}
				}

			}
		}
		return ordersDelivered;
	}

	private String getAppendedTrackingIdsString(List<ShippingOrder> shippingOrderSubList) {
		String appendedTrackingId = "";

		//Iterating over the sub-list to get the required trackingId and fetch response
		for (ShippingOrder shipOrder : shippingOrderSubList) {
			appendedTrackingId = appendedTrackingId + "," + shipOrder.getShipment().getAwb().getAwbNumber();
		}

		return appendedTrackingId;
	}

	public List<String> getUnmodifiedTrackingIds() {
		return unmodifiedTrackingIds;
	}

	public String format_gateway_order_id(String order_gateway_id, int digitsInGatewayId) {
		if (order_gateway_id.length() > digitsInGatewayId - 1) {
			order_gateway_id = (order_gateway_id.substring(0, order_gateway_id.length() - digitsInGatewayId) + "-" + order_gateway_id.substring(order_gateway_id.length()
					- digitsInGatewayId, order_gateway_id.length()));
		}
		return order_gateway_id;
	}

	public Date getFormattedDeliveryDate(String deliveryDate) {
		Date formattedDate = null;
		if (deliveryDate != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			try {
				formattedDate = sdf.parse(deliveryDate);
			} catch (Exception e) {
				logger.error("Exception occurred in parsing DeliveryDate format,Date was :" + deliveryDate);
			}
		}

		return formattedDate;
	}

	@SuppressWarnings("unchecked")
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

	private Map<Integer, String> getRowMapStringFormat(Iterator<Row> objRowIt) {
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
				if (HSSFDateUtil.isCellDateFormatted(headerCell)) {
					Date dateExcel = null;
					dateExcel = headerCell.getDateCellValue();
					Calendar cal = Calendar.getInstance();
					cal.setTime(dateExcel);
					int dd = cal.get(Calendar.DATE);
					int mm = cal.get(Calendar.MONTH);
					cal.set(Calendar.DATE, mm + 1);
					cal.set(Calendar.MONTH, dd - 1);
					dateExcel = cal.getTime();
					String dateString = cal.get(Calendar.DAY_OF_MONTH) + "/" + (cal.get(Calendar.MONTH) + 1) + "/" + cal.get(Calendar.YEAR);
					headerMap.put(headerColIndex, dateString);
					continue;
				}
			} catch (Exception e) {
				logger.debug("error trying to read column " + headerColIndex + " as Date on Row " + headers.getRowNum() + " : Cell toString = " + headerCell.toString());
				logger.debug("Now trying to read as String");
			}
			try {
				headerCell.setCellType(Cell.CELL_TYPE_STRING);
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

	public AdminShippingOrderService getAdminShippingOrderService() {
		return adminShippingOrderService;
	}

	public void setAdminShippingOrderService(AdminShippingOrderService adminShippingOrderService) {
		this.adminShippingOrderService = adminShippingOrderService;
	}

	public ShippingOrderService getShippingOrderService() {
		return shippingOrderService;
	}

	public void setShippingOrderService(ShippingOrderService shippingOrderService) {
		this.shippingOrderService = shippingOrderService;
	}

}
