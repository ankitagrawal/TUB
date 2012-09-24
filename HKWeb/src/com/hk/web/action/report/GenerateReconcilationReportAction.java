package com.hk.web.action.report;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.reconciliation.AdminReconciliationService;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierServiceInfo;
import com.hk.domain.order.OrderPaymentReconciliation;
import com.hk.domain.order.ShippingOrder;
import com.hk.impl.dao.warehouse.WarehouseDaoImpl;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.dao.payment.PaymentModeDao;
import com.hk.pact.dao.shippingOrder.ShippingOrderDao;
import com.hk.pact.service.UserService;
import com.hk.report.dto.order.reconcilation.ReconcilationReportDto;
import com.hk.report.pact.service.shippingOrder.ReportShippingOrderService;
import com.hk.util.XslGenerator;
import com.hk.util.io.HkXlsWriter;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Secure(hasAnyPermissions = {PermissionConstants.VIEW_RECONCILIATION_REPORTS}, authActionBean = AdminPermissionAction.class)
@Component
public class GenerateReconcilationReportAction extends BaseAction {
	private static Logger logger = Logger.getLogger(GenerateReconcilationReportAction.class);

	@Autowired
	UserService userService;
	@Autowired
	OrderDao orderDao;
	@Autowired
	ShippingOrderDao shippingOrderDao;
	@Autowired
	ReportShippingOrderService shippingOrderReportingService;
	@Autowired
	WarehouseDaoImpl warehouseDao;
	@Autowired
	AdminReconciliationService adminReconciliationService;
	@Autowired
	PaymentModeDao paymentModeDao;
	@Autowired
	XslGenerator xslGenerator;

	@Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
	String adminDownloadsPath;

	private Date startDate;
	private Date endDate;
	private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private String paymentProcess;
	private CourierServiceInfo courierServiceInfo;

	private Long warehouseId;
	private Courier courier;
	private Long shippingOrderStatusId;
	private String gatewayOrderType;
	private FileBean fileBean;

	@Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
	String adminUploadsPath;

	private Long    shippingOrderId;
	private Long    baseOrderId;
	private String  gatewayOrderId;
	private String  baseGatewayOrderId;
	private File    excelFile;

	@DefaultHandler
	public Resolution pre() {
		return new ForwardResolution("/pages/admin/generateReconcilationReport.jsp");
	}

	public Resolution generateReconilationReport() {
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
		List<ReconcilationReportDto> reconcilationReportDtoList = new ArrayList<ReconcilationReportDto>();
		reconcilationReportDtoList = shippingOrderReportingService.findReconcilationReportByDate(startDate, endDate, paymentProcess, courier, warehouseId, shippingOrderStatusId);
		if (reconcilationReportDtoList.isEmpty() == true) {
			addRedirectAlertMessage(new SimpleMessage("No order for given search criteria."));
			return new ForwardResolution("/pages/admin/generateReconcilationReport.jsp");
		}

		String excelFilePath = adminDownloadsPath + "/reports/ReconReport" + sdf.format(new Date()) + ".xls";
		final File
				excelFile = new File(excelFilePath);

		HkXlsWriter xlsWriter = new HkXlsWriter();
		xlsWriter.addHeader("SHIPPING ORDER ID", "SHIPPING ORDER ID");
		xlsWriter.addHeader("ORDER DATE", "ORDER DATE");
		xlsWriter.addHeader("NAME", "NAME");
		xlsWriter.addHeader("CITY", "CITY");
		xlsWriter.addHeader("PINCODE", "PINCODE");
		xlsWriter.addHeader("PAYMENT", "PAYMENT");
		xlsWriter.addHeader("TOTAL", "TOTAL");
		xlsWriter.addHeader("COURIER", "COURIER");
		xlsWriter.addHeader("AWB", "AWB");
		xlsWriter.addHeader("SHIPMENT DATE", "SHIPMENT DAT");
		xlsWriter.addHeader("DELIVERY DATE", "DELIVERY DATE");
		xlsWriter.addHeader("RECONCILED", "RECONCILED");
		xlsWriter.addHeader("ORDER STATUS", "ORDER STATU");
		xlsWriter.addHeader("BOX WEIGHT", "BOX WEIGHT");
		xlsWriter.addHeader("BOX SIZE", "BOX SIZE");
		xlsWriter.addHeader("WAREHOUSE", "WAREHOUSE");

		int row = 1;
		for (ReconcilationReportDto reconcilationReportDto : reconcilationReportDtoList) {
			xlsWriter.addCell(row, reconcilationReportDto.getInvoiceId());
			xlsWriter.addCell(row, reconcilationReportDto.getOrderDate() != null ? simpleDateFormat.format(reconcilationReportDto.getOrderDate()) : "");
			xlsWriter.addCell(row, reconcilationReportDto.getName());
			xlsWriter.addCell(row, reconcilationReportDto.getCity());
			xlsWriter.addCell(row, reconcilationReportDto.getPincode());
			xlsWriter.addCell(row, reconcilationReportDto.getPayment());
			xlsWriter.addCell(row, reconcilationReportDto.getTotal());
			xlsWriter.addCell(row, reconcilationReportDto.getCourier().getName());
			xlsWriter.addCell(row, reconcilationReportDto.getAwb());
			xlsWriter.addCell(row, reconcilationReportDto.getShipmentDate() != null ? simpleDateFormat.format(reconcilationReportDto.getShipmentDate()) : "");
			if (reconcilationReportDto.getDeliveryDate() == null) {
				xlsWriter.addCell(row, "");
			} else {
				xlsWriter.addCell(row, reconcilationReportDto.getDeliveryDate() != null ? simpleDateFormat.format(reconcilationReportDto.getDeliveryDate()) : "");
			}

			xlsWriter.addCell(row, reconcilationReportDto.getReconciled());
			xlsWriter.addCell(row, reconcilationReportDto.getOrderStatus());
			xlsWriter.addCell(row, reconcilationReportDto.getBoxWeight());
			xlsWriter.addCell(row, reconcilationReportDto.getBoxSize());
			xlsWriter.addCell(row, reconcilationReportDto.getWarehouse().getName());
			row++;
		}
		xlsWriter.writeData(excelFile, "Reconciliation_report");
		addRedirectAlertMessage(new SimpleMessage("Download complete"));

		return new Resolution() {

			public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
				OutputStream out = null;
				InputStream in = new BufferedInputStream(new FileInputStream(excelFile));
				res.setContentLength((int) excelFile.length());
				res.setHeader("Content-Disposition", "attachment; filename=\"" + excelFile.getName() + "\";");
				out = res.getOutputStream();

				// Copy the contents of the file to the output stream
				byte[] buf = new byte[8192];
				int count = 0;
				while ((count = in.read(buf)) >= 0) {
					out.write(buf, 0, count);
				}
			}
		};

		// return new ForwardResolution("/pages/admin/generateReconcilationReport.jsp");
	}

	public Resolution parse() throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String excelFilePath = adminUploadsPath + "/reconcile/" + sdf.format(new Date()) + ".xls";
		File excelFile = new File(excelFilePath);
		excelFile.getParentFile().mkdirs();
		fileBean.save(excelFile);
		PaymentMode paymentMode = null;

		try {
			if (paymentProcess.equalsIgnoreCase("cod")) {
				paymentMode = getPaymentModeDao().getPaymentModeById(EnumPaymentMode.COD.getId());
				getAdminReconciliationService().parseExcelForShippingOrder(excelFilePath, "Sheet1", paymentMode);
			} else {
				paymentMode = getPaymentModeDao().getPaymentModeById(EnumPaymentMode.TECHPROCESS.getId());
				getAdminReconciliationService().parseExcelForBaseOrder(excelFilePath, "Sheet1", paymentMode);
			}

			addRedirectAlertMessage(new SimpleMessage("Changes saved."));
		} catch (Exception e) {
			logger.error("Exception while reading excel sheet.", e);
			addRedirectAlertMessage(new SimpleMessage("Upload failed - " + e.getMessage()));
		}
		return new RedirectResolution(GenerateReconcilationReportAction.class);
	}

	public Resolution downloadPaymentDifference() throws Exception{
		List<OrderPaymentReconciliation> orderPaymentReconciliationList = new ArrayList<OrderPaymentReconciliation>();
		if(paymentProcess.equalsIgnoreCase("COD")) {
			orderPaymentReconciliationList = getAdminReconciliationService().findPaymentDifferenceInCODOrders(shippingOrderId, gatewayOrderId, startDate, endDate, courier);
		} else {
			orderPaymentReconciliationList = getAdminReconciliationService().findPaymentDifferenceInPrepaidOrders(baseOrderId, gatewayOrderId, startDate, endDate);
		}

		if (orderPaymentReconciliationList.isEmpty() == true) {
			addRedirectAlertMessage(new SimpleMessage("No order for given search criteria."));
			return new ForwardResolution("/pages/admin/generateReconcilationReport.jsp");
		}

		String excelFilePath = adminDownloadsPath + "/reports/ReconDiffReport" + sdf.format(new Date()) + ".xls";
		excelFile = new File(excelFilePath);
		if(paymentProcess.equalsIgnoreCase("COD")) {
			getXslGenerator().generateExcelForCOD(excelFile, orderPaymentReconciliationList);
		} else {
			getXslGenerator().generateExcelForPrepaid(excelFile, orderPaymentReconciliationList);
		}
		addRedirectAlertMessage(new SimpleMessage("Download complete"));
		return new HTTPResponseResolution();

	}

	private class HTTPResponseResolution implements Resolution {
	        public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
	            OutputStream out = null;
	            InputStream in = new BufferedInputStream(new FileInputStream(excelFile));
	            res.setContentLength((int) excelFile.length());
	            res.setHeader("Content-Disposition", "attachment; filename=\"" + excelFile.getName() + "\";");
	            out = res.getOutputStream();

	            // Copy the contents of the file to the output stream
	            byte[] buf = new byte[4096];
	            int count = 0;
	            while ((count = in.read(buf)) >= 0) {
	                out.write(buf, 0, count);
	            }
	        }

	    }

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getPaymentProcess() {
		return paymentProcess;
	}

	public void setPaymentProcess(String paymentProcess) {
		this.paymentProcess = paymentProcess;
	}

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public Courier getCourier() {
		return courier;
	}

	public void setCourier(Courier courier) {
		this.courier = courier;
	}

	public Long getShippingOrderStatusId() {
		return shippingOrderStatusId;
	}

	public void setShippingOrderStatusId(Long shippingOrderStatusId) {
		this.shippingOrderStatusId = shippingOrderStatusId;
	}

	public String getGatewayOrderType() {
		return gatewayOrderType;
	}

	public void setGatewayOrderType(String gatewayOrderType) {
		this.gatewayOrderType = gatewayOrderType;
	}

	public FileBean getFileBean() {
		return fileBean;
	}

	public void setFileBean(FileBean fileBean) {
		this.fileBean = fileBean;
	}

	public AdminReconciliationService getAdminReconciliationService() {
		return adminReconciliationService;
	}

	public void setAdminReconciliationService(AdminReconciliationService adminReconciliationService) {
		this.adminReconciliationService = adminReconciliationService;
	}

	public PaymentModeDao getPaymentModeDao() {
		return paymentModeDao;
	}

	public void setPaymentModeDao(PaymentModeDao paymentModeDao) {
		this.paymentModeDao = paymentModeDao;
	}

	public Long getShippingOrderId() {
		return shippingOrderId;
	}

	public void setShippingOrderId(Long shippingOrderId) {
		this.shippingOrderId = shippingOrderId;
	}

	public Long getBaseOrderId() {
		return baseOrderId;
	}

	public void setBaseOrderId(Long baseOrderId) {
		this.baseOrderId = baseOrderId;
	}

	public String getGatewayOrderId() {
		return gatewayOrderId;
	}

	public void setGatewayOrderId(String gatewayOrderId) {
		this.gatewayOrderId = gatewayOrderId;
	}

	public String getBaseGatewayOrderId() {
		return baseGatewayOrderId;
	}

	public void setBaseGatewayOrderId(String baseGatewayOrderId) {
		this.baseGatewayOrderId = baseGatewayOrderId;
	}

	public XslGenerator getXslGenerator() {
		return xslGenerator;
	}

	public void setXslGenerator(XslGenerator xslGenerator) {
		this.xslGenerator = xslGenerator;
	}
}