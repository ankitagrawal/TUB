package com.hk.web.action.admin.courier;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.pact.service.courier.AwbService;
import com.hk.admin.pact.service.courier.DispatchLotService;
import com.hk.pact.service.shippingOrder.ShipmentService;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.courier.EnumDispatchLotStatus;
import com.hk.domain.courier.*;
import com.hk.domain.user.User;
import com.hk.exception.ExcelBlankFieldException;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.util.CustomDateTypeConvertor;
import com.hk.web.action.error.AdminPermissionAction;
import com.restfb.util.StringUtils;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: Rohit
 * Date: 12/3/12
 * Time: 7:16 PM
 * To change this template use File | Settings | File Templates.
 */
@Secure(hasAnyPermissions = {PermissionConstants.DISPATCH_LOT_OPERATIONS}, authActionBean = AdminPermissionAction.class)
@Component
public class DispatchLotAction extends BasePaginatedAction {

	private static Logger logger = Logger.getLogger(DispatchLotAction.class);

	@Autowired
	private DispatchLotService dispatchLotService;

	@Autowired
	ShipmentService shipmentService;

	@Autowired
	ShippingOrderService shippingOrderService;

	@Autowired
	private AwbService awbService;

	@Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
	String adminUploadsPath;

	File xlsFile;
	@Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
	String adminDownloads;

	private List<DispatchLot> dispatchLotList = new ArrayList<DispatchLot>();
	private Page dispatchLotPage;
	private DispatchLot dispatchLot;
	private Integer defaultPerPage = 20;
	private String docketNumber;
	private Courier courier;
	private Zone zone;
	private String source;
	private String destination;
	private Date dispatchStartDate;
	private Date dispatchEndDate;
	private DispatchLotStatus dispatchLotStatus;
	private FileBean fileBean;
	private List<String> gatewayOrderIdList;
	private List<DispatchLotHasShipment> dispatchLotShipments;
	private String shipmentStatusFilter;
	private FileBean uploadDocumentFileBean;
	private File documentFile;
	private String awbNumber;

	@DefaultHandler
	public Resolution pre() {
		return new ForwardResolution("/pages/admin/courier/dispatchLot.jsp");
	}

	public Resolution showDispatchLotList() {
		if(awbNumber != null){
		  List<Courier> courierList = new ArrayList<Courier>();
			Shipment  shipmentObj = shipmentService.findByAwb(awbService.findByCourierAwbNumber(courierList,awbNumber));
			if(shipmentObj != null){
				dispatchLotList = getDispatchLotService().getDispatchLotsForShipment(shipmentObj);
			}
		}
		else{
			dispatchLotPage = getDispatchLotService().searchDispatchLot(dispatchLot, docketNumber, courier, zone, source,
					destination, dispatchStartDate, dispatchEndDate, dispatchLotStatus, getPageNo(), getPerPage());
			dispatchLotList = dispatchLotPage.getList();
		}
		return new ForwardResolution("/pages/admin/courier/dispatchLotList.jsp");
	}

	public Resolution save() {
		if (dispatchLot == null) {
			addRedirectAlertMessage(new SimpleMessage("Please fill the required values"));
			return new ForwardResolution("/pages/admin/courier/dispatchLot.jsp");
		}
		if (!doValidations()) {
			return new ForwardResolution("/pages/admin/courier/dispatchLot.jsp");
		}

		//If dispatch Lot is being created for the first time
		if (dispatchLot.getId() == null) {
			dispatchLot.setDispatchLotStatus(EnumDispatchLotStatus.Generated.getDispatchLotStatus());

			User user = null;
			if (getPrincipal() != null) {
				user = getUserService().getUserById(getPrincipal().getId());
			}
			dispatchLot.setCreatedBy(user);
		}

		dispatchLot = getDispatchLotService().save(dispatchLot);
		addRedirectAlertMessage(new SimpleMessage("Changes saved"));
		return new ForwardResolution("/pages/admin/courier/dispatchLot.jsp");

	}

	private boolean doValidations() {
		boolean valid = true;
		if (StringUtils.isBlank(dispatchLot.getDocketNumber())) {
			addRedirectAlertMessage(new SimpleMessage("Please fill the Docket Number"));
			valid = false;
		}

		if (dispatchLot.getCourier() == null) {
			addRedirectAlertMessage(new SimpleMessage("Please Select a Courier"));
			valid = false;
		}

		if (dispatchLot.getZone() == null) {
			addRedirectAlertMessage(new SimpleMessage("Please Specify the Zone"));
			valid = false;
		}

		if (StringUtils.isBlank(dispatchLot.getSource())) {
			addRedirectAlertMessage(new SimpleMessage("Please Specify the Source"));
			valid = false;
		}

		if (StringUtils.isBlank(dispatchLot.getDestination())) {
			addRedirectAlertMessage(new SimpleMessage("Please Specify the Destination"));
			valid = false;
		}

		return valid;
	}

	public Resolution parse() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String excelFilePath = adminUploadsPath + "/dispatchLot/DispatchLot_" + sdf.format(new Date()) + ".xls";
		File excelFile = new File(excelFilePath);
		excelFile.getParentFile().mkdirs();
		try {
			if (fileBean != null) {
				fileBean.save(excelFile);
				getDispatchLotService().parseExcelAndSaveShipmentDetails(dispatchLot, excelFilePath, "Courier");
				addRedirectAlertMessage(new SimpleMessage("File uploaded successfully."));
			} else {
				addRedirectAlertMessage(new SimpleMessage("Please select a File to be uploaded"));
				return new ForwardResolution("/pages/admin/courier/dispatchLot.jsp");
			}

		} catch (IOException e) {
			logger.error("Exception while reading excel sheet.", e);
			addRedirectAlertMessage(new SimpleMessage("Upload failed - " + e.getMessage()));
		} catch (ExcelBlankFieldException e) {
			logger.error("Exception while reading excel sheet. : " + e.getMessage());
			addRedirectAlertMessage(new SimpleMessage("Upload failed - " + e.getMessage()));
		}
		return new ForwardResolution(DispatchLotAction.class, "showDispatchLotList").addParameter("dispatchLot", dispatchLot.getId());
	}

	public Resolution receiveLot(){
		if(dispatchLot == null){
			addRedirectAlertMessage(new SimpleMessage("Dispatch lot not found."));
			return new ForwardResolution(DispatchLotAction.class, "showDispatchLotList");
		}
		if(dispatchLot.getDispatchLotStatus().equals(EnumDispatchLotStatus.Cancelled.getDispatchLotStatus())) {
			addRedirectAlertMessage(new SimpleMessage("Dispatch lot is in CANCELLED state."));
			return new ForwardResolution(DispatchLotAction.class, "showDispatchLotList");
		}
		return new ForwardResolution("/pages/admin/courier/receiveDispatchLot.jsp").addParameter("dispatchLot", dispatchLot.getId());
	}

	public Resolution viewLot(){
		if(dispatchLot == null){
			addRedirectAlertMessage(new SimpleMessage("Dispatch lot not found."));
			return new ForwardResolution(DispatchLotAction.class, "showDispatchLotList");
		}

		dispatchLotShipments = getDispatchLotService().getDispatchLotHasShipmentListByDispatchLot(dispatchLot, shipmentStatusFilter);
		return new ForwardResolution("/pages/admin/courier/viewDispatchLotWithShipments.jsp").addParameter("dispatchLot", dispatchLot.getId());
	}

	public Resolution downloadLotDetailsToExcel() {
		if (dispatchLot == null) {
			addRedirectAlertMessage(new SimpleMessage("Dispatch lot not found."));
			return new ForwardResolution(DispatchLotAction.class, "showDispatchLotList");
		}

		dispatchLotShipments = getDispatchLotService().getDispatchLotHasShipmentListByDispatchLot(dispatchLot, shipmentStatusFilter);
		if(dispatchLotShipments != null) {
			xlsFile = new File(adminDownloads + "/reports/DispatchLot.xls");
			xlsFile = dispatchLotService.generateDispatchLotExcel(xlsFile, dispatchLotShipments);
			return new HTTPResponseResolutionExcel();
		}
		addRedirectAlertMessage(new SimpleMessage("No Shipment found for the Dispatch Lot"));
		return new ForwardResolution("/pages/admin/courier/viewDispatchLotWithShipments.jsp").addParameter("dispatchLot", dispatchLot.getId());
	}

	public Resolution markShipmentsReceived(){
		if(dispatchLot == null){
			addRedirectAlertMessage(new SimpleMessage("Dispatch lot not found."));
			return new ForwardResolution(DispatchLotAction.class, "showDispatchLotList");
		}

		if(gatewayOrderIdList == null) {
			addRedirectAlertMessage(new SimpleMessage("Please scan the Gateway Order Id"));
			return new ForwardResolution("/pages/admin/courier/receiveDispatchLot.jsp").addParameter("dispatchLot", dispatchLot.getId());
		}

		String invalidGatewayOrderIds = getDispatchLotService().markShipmentsAsReceived(dispatchLot, gatewayOrderIdList);
		if(invalidGatewayOrderIds != null){
			addRedirectAlertMessage(new SimpleMessage("Following Gateway Order Ids are invalid: "+invalidGatewayOrderIds));
		}
		return new ForwardResolution(DispatchLotAction.class, "showDispatchLotList").addParameter("dispatchLot", dispatchLot.getId());
	}

	public Resolution cancelDispatchLot() {
		if(dispatchLot != null) {
			if(dispatchLot.getDispatchLotStatus().equals(EnumDispatchLotStatus.Received.getDispatchLotStatus())) {
				addRedirectAlertMessage(new SimpleMessage("Cannot cancel the Received Dispatch Lot"));
				return new ForwardResolution(DispatchLotAction.class, "showDispatchLotList").addParameter("dispatchLot", dispatchLot.getId());
			}
			getDispatchLotService().cancelDispatchLot(dispatchLot);
			addRedirectAlertMessage(new SimpleMessage("Dispatch Lot cancelled"));
		} else {
			addRedirectAlertMessage(new SimpleMessage("Incorrect Dispatch Lot"));
		}
		return new ForwardResolution(DispatchLotAction.class, "showDispatchLotList");
	}

	public Resolution markDispatchLotReceived(){
		if (dispatchLot == null) {
			addRedirectAlertMessage(new SimpleMessage("Dispatch lot not found."));
			return new ForwardResolution(DispatchLotAction.class, "showDispatchLotList");
		}

		getDispatchLotService().markDispatchLotReceived(dispatchLot);
		addRedirectAlertMessage(new SimpleMessage("Dispatch Lot marked as Received"));
		return new ForwardResolution(DispatchLotAction.class, "showDispatchLotList");
	}

	public Resolution uploadDocument() {
		if(dispatchLot == null) {
			addRedirectAlertMessage(new SimpleMessage("Invalid Dispatch Lot"));
			return new ForwardResolution("/pages/admin/courier/dispatchLot.jsp");
		}

		if(uploadDocumentFileBean == null) {
			addRedirectAlertMessage(new SimpleMessage("Please select a document to be uploaded"));
			return new ForwardResolution("/pages/admin/courier/dispatchLot.jsp");
		}

		String documentFilePath = adminUploadsPath + "/dispatchLot/DispatchLot_" + dispatchLot.getId() + "_" + uploadDocumentFileBean.getFileName();
		documentFile = new File(documentFilePath);
		documentFile.getParentFile().mkdirs();
		try {
			documentFile.createNewFile();
			uploadDocumentFileBean.save(documentFile);
			dispatchLot.setDocumentFileName(uploadDocumentFileBean.getFileName());
			dispatchLotService.save(dispatchLot);
			addRedirectAlertMessage(new SimpleMessage("Document uploaded successfully"));
		} catch (IOException e) {
			logger.error("Document Upload failed: " + e);
			addRedirectAlertMessage(new SimpleMessage("Document Upload failed: " + e.getMessage()));
		}

		return new ForwardResolution("/pages/admin/courier/dispatchLot.jsp");

	}

	public Resolution downloadDocument() {
		if(dispatchLot != null) {
			String documentFilePath = adminUploadsPath + "/dispatchLot/DispatchLot_" + dispatchLot.getId() + "_" + dispatchLot.getDocumentFileName();
			documentFile = new File(documentFilePath);
		}
		return new HTTPResponseResolution();
	}

	public Resolution markLotAsInTransit() {
		if (dispatchLot == null) {
			addRedirectAlertMessage(new SimpleMessage("Invalid Dispatch Lot"));
			return new ForwardResolution("/pages/admin/courier/dispatchLot.jsp");
		}

		dispatchLot.setDispatchLotStatus(EnumDispatchLotStatus.InTransit.getDispatchLotStatus());
		dispatchLotService.save(dispatchLot);
		addRedirectAlertMessage(new SimpleMessage("Dispatch Lot marked as 'In Transit'"));
		return new ForwardResolution(DispatchLotAction.class, "showDispatchLotList");
	}

	private class HTTPResponseResolution implements Resolution {
		public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
			OutputStream out = null;
			InputStream in = new BufferedInputStream(new FileInputStream(documentFile));
			res.setContentLength((int) documentFile.length());
			res.setHeader("Content-Disposition", "attachment; filename=\"" + documentFile.getName() + "\";");
			out = res.getOutputStream();

			// Copy the contents of the file to the output stream
			byte[] buf = new byte[4096];
			int count = 0;
			while ((count = in.read(buf)) >= 0) {
				out.write(buf, 0, count);
			}
		}
	}

	private class HTTPResponseResolutionExcel implements Resolution {
		public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
			OutputStream out = null;
			InputStream in = new BufferedInputStream(new FileInputStream(xlsFile));
			res.setContentLength((int) xlsFile.length());
			res.setHeader("Content-Disposition", "attachment; filename=\"" + xlsFile.getName() + "\";");
			out = res.getOutputStream();

			// Copy the contents of the file to the output stream
			byte[] buf = new byte[4096];
			int count = 0;
			while ((count = in.read(buf)) >= 0) {
				out.write(buf, 0, count);
			}
		}
	}

	public int getPerPageDefault() {
		return defaultPerPage;
	}

	public int getPageCount() {
		return dispatchLotPage == null ? 0 : dispatchLotPage.getTotalPages();
	}

	public int getResultCount() {
		return dispatchLotPage == null ? 0 : dispatchLotPage.getTotalResults();
	}

	public Set<String> getParamSet() {
		HashSet<String> params = new HashSet<String>();
		params.add("dispatchLot");
		params.add("docketNumber");
		params.add("courier");
		params.add("zone");
		params.add("source");
		params.add("destination");
		params.add("dispatchStartDate");
		params.add("dispatchEndDate");
		params.add("dispatchLotStatus");
		return params;
	}

	public DispatchLotService getDispatchLotService() {
		return dispatchLotService;
	}

	public void setDispatchLotService(DispatchLotService dispatchLotService) {
		this.dispatchLotService = dispatchLotService;
	}

	public DispatchLot getDispatchLot() {
		return dispatchLot;
	}

	public void setDispatchLot(DispatchLot dispatchLot) {
		this.dispatchLot = dispatchLot;
	}

	public Page getDispatchLotPage() {
		return dispatchLotPage;
	}

	public void setDispatchLotPage(Page dispatchLotPage) {
		this.dispatchLotPage = dispatchLotPage;
	}

	public Integer getDefaultPerPage() {
		return defaultPerPage;
	}

	public void setDefaultPerPage(Integer defaultPerPage) {
		this.defaultPerPage = defaultPerPage;
	}

	public String getDocketNumber() {
		return docketNumber;
	}

	public void setDocketNumber(String docketNumber) {
		this.docketNumber = docketNumber;
	}

	public Courier getCourier() {
		return courier;
	}

	public void setCourier(Courier courier) {
		this.courier = courier;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getDestination() {
		return destination;
	}

	public void setDestination(String destination) {
		this.destination = destination;
	}

	public List<DispatchLot> getDispatchLotList() {
		return dispatchLotList;
	}

	public void setDispatchLotList(List<DispatchLot> dispatchLotList) {
		this.dispatchLotList = dispatchLotList;
	}

	public Date getDispatchStartDate() {
		return dispatchStartDate;
	}

	@Validate(converter = CustomDateTypeConvertor.class)
	public void setDispatchStartDate(Date dispatchStartDate) {
		this.dispatchStartDate = dispatchStartDate;
	}

	public Date getDispatchEndDate() {
		return dispatchEndDate;
	}

	@Validate(converter = CustomDateTypeConvertor.class)
	public void setDispatchEndDate(Date dispatchEndDate) {
		this.dispatchEndDate = dispatchEndDate;
	}

	public DispatchLotStatus getDispatchLotStatus() {
		return dispatchLotStatus;
	}

	public void setDispatchLotStatus(DispatchLotStatus dispatchLotStatus) {
		this.dispatchLotStatus = dispatchLotStatus;
	}

	public Zone getZone() {
		return zone;
	}

	public void setZone(Zone zone) {
		this.zone = zone;
	}

	public FileBean getFileBean() {
		return fileBean;
	}

	public void setFileBean(FileBean fileBean) {
		this.fileBean = fileBean;
	}

	public List<String> getGatewayOrderIdList() {
		return gatewayOrderIdList;
	}

	public void setGatewayOrderIdList(List<String> gatewayOrderIdList) {
		this.gatewayOrderIdList = gatewayOrderIdList;
	}

	public ShipmentService getShipmentService() {
		return shipmentService;
	}

	public ShippingOrderService getShippingOrderService() {
		return shippingOrderService;
	}

	public List<DispatchLotHasShipment> getDispatchLotShipments() {
		return dispatchLotShipments;
	}

	public void setDispatchLotShipments(List<DispatchLotHasShipment> dispatchLotShipments) {
		this.dispatchLotShipments = dispatchLotShipments;
	}

	public String getShipmentStatusFilter() {
		return shipmentStatusFilter;
	}

	public void setShipmentStatusFilter(String shipmentStatusFilter) {
		this.shipmentStatusFilter = shipmentStatusFilter;
	}

	public FileBean getUploadDocumentFileBean() {
		return uploadDocumentFileBean;
	}

	public void setUploadDocumentFileBean(FileBean uploadDocumentFileBean) {
		this.uploadDocumentFileBean = uploadDocumentFileBean;
	}

	public String getAwbNumber() {
		return awbNumber;
	}

	public void setAwbNumber(String awbNumber) {
		this.awbNumber = awbNumber;
	}
}
