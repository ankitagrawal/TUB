package com.hk.web.action.report;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.akube.framework.util.BaseUtils;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.domain.core.Pincode;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderLifecycle;
import com.hk.domain.user.Address;
import com.hk.pact.service.core.PincodeService;
import com.hk.pact.service.shippingOrder.ShippingOrderLifecycleService;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.FileBean;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.pact.service.reconciliation.AdminReconciliationService;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.domain.courier.Courier;
import com.hk.domain.order.OrderPaymentReconciliation;
import com.hk.impl.dao.warehouse.WarehouseDaoImpl;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.dao.payment.PaymentModeDao;
import com.hk.pact.dao.shippingOrder.ShippingOrderDao;
import com.hk.pact.service.UserService;
import com.hk.report.dto.order.reconcilation.ReconcilationReportDto;
import com.hk.report.pact.service.shippingOrder.ReportShippingOrderService;
import com.hk.util.CustomDateTypeConvertor;
import com.hk.util.XslGenerator;
import com.hk.util.io.HkXlsWriter;
import com.hk.web.action.error.AdminPermissionAction;

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
    @Autowired
    PincodeService pincodeService;
    @Autowired
    ShippingOrderLifecycleService shippingOrderLifecycleService;

    @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
    String adminDownloadsPath;

    private Date startDate;
    private Date endDate;
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    private String paymentProcess;

    private Long warehouseId;
    private Courier courier;
    private Long shippingOrderStatusId;
    private String gatewayOrderType;
    private FileBean fileBean;

    @Value("#{hkEnvProps['" + Keys.Env.adminUploads + "']}")
    String adminUploadsPath;

    private Long shippingOrderId;
    private Long baseOrderId;
    private String gatewayOrderId;
    private String baseGatewayOrderId;
    private File excelFile;
    private final String COD = "cod";
    private final String PREPAID = "prepaid";

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
        xlsWriter.addHeader("ZONE", "ZONE");
        xlsWriter.addHeader("PAYMENT", "PAYMENT");
        xlsWriter.addHeader("TOTAL", "TOTAL");
        xlsWriter.addHeader("COURIER", "COURIER");
        xlsWriter.addHeader("AWB", "AWB");
        xlsWriter.addHeader("SHIPMENT DATE", "SHIPMENT DAT");
        xlsWriter.addHeader("DELIVERY DATE", "DELIVERY DATE");
        xlsWriter.addHeader("RECONCILED", "RECONCILED");
        xlsWriter.addHeader("ORDER STATUS", "ORDER STATU");
        xlsWriter.addHeader("RTO_COMMENTS", "RTO_COMMENTS");
        xlsWriter.addHeader("BOX WEIGHT", "BOX WEIGHT");
        xlsWriter.addHeader("BOX SIZE", "BOX SIZE");
        xlsWriter.addHeader("WAREHOUSE", "WAREHOUSE");
        xlsWriter.addHeader("DROP SHIPMENT", "DROP SHIPMENT");
        xlsWriter.addHeader("CATEGORY", "CATEGORY");
        xlsWriter.addHeader("ADDRESS", "ADDRESS");
        xlsWriter.addHeader("CUSTOMER PHONE", "CUSTOMER PHONE");
        xlsWriter.addHeader("ORDER CATEGORY", "ORDER CATEGORY");

        int row = 1;
        for (ReconcilationReportDto reconcilationReportDto : reconcilationReportDtoList) {
            String rtoInitiatedComments = "";
            ShippingOrder shippingOrder = reconcilationReportDto.getShippingOrder();
            Address address = reconcilationReportDto.getAddress();
            String custAddress = "";
            String custPhone = "";
            if (address != null) {
                custAddress = address.getLine1();
                if (address.getLine2() != null) {
                    custAddress = custAddress + BaseUtils.newline + address.getLine2();
                }
                custAddress = custAddress + BaseUtils.newline + address.getCity() + BaseUtils.newline + address.getState();
                custPhone = address.getPhone();
            }

            List<ShippingOrderLifecycle> shippingOrderLifecycleList =
                    getShippingOrderLifecycleService().getShippingOrderLifecycleBySOAndActivity(shippingOrder.getId(), EnumShippingOrderLifecycleActivity.RTO_Initiated.getId());
            if (shippingOrderLifecycleList != null && shippingOrderLifecycleList.size() > 0) {
                rtoInitiatedComments = shippingOrderLifecycleList.get(0).getComments();
            }

            xlsWriter.addCell(row, reconcilationReportDto.getInvoiceId());
            xlsWriter.addCell(row, reconcilationReportDto.getOrderDate() != null ? simpleDateFormat.format(reconcilationReportDto.getOrderDate()) : "");
            xlsWriter.addCell(row, reconcilationReportDto.getName());
            xlsWriter.addCell(row, reconcilationReportDto.getCity());
            xlsWriter.addCell(row, reconcilationReportDto.getPincode());
            Pincode pincode = getPincodeService().getByPincode(reconcilationReportDto.getPincode());
            if (pincode != null) {
                xlsWriter.addCell(row, pincode.getZone().getName());
            } else {
                xlsWriter.addCell(row, "");
            }
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

            xlsWriter.addCell(row, reconcilationReportDto.isReconciled() ? 'Y' : 'N');
            xlsWriter.addCell(row, reconcilationReportDto.getOrderStatus());
            xlsWriter.addCell(row, rtoInitiatedComments);
            xlsWriter.addCell(row, reconcilationReportDto.getBoxWeight());
            xlsWriter.addCell(row, reconcilationReportDto.getBoxSize());
            xlsWriter.addCell(row, reconcilationReportDto.getWarehouse().getIdentifier());
            xlsWriter.addCell(row, shippingOrder.isDropShipping() ? 'Y' : 'N');
            xlsWriter.addCell(row, shippingOrder.getBasketCategory());
            xlsWriter.addCell(row, custAddress);
            xlsWriter.addCell(row, custPhone);
            if (reconcilationReportDto.isB2B()) {
                String store = reconcilationReportDto.getOrderCategory();
                store = store + " B2B";
                xlsWriter.addCell(row, store);
            } else {
                xlsWriter.addCell(row, reconcilationReportDto.getOrderCategory());
            }
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
        //PaymentMode paymentMode = null;

        try {
            if (paymentProcess.equalsIgnoreCase("cod")) {
                getAdminReconciliationService().parseExcelForShippingOrder(excelFilePath, "Sheet1", COD);
            } else {
                getAdminReconciliationService().parseExcelForBaseOrder(excelFilePath, "Sheet1", PREPAID);
            }

            addRedirectAlertMessage(new SimpleMessage("Changes saved."));
        } catch (Exception e) {
            logger.error("Exception while reading excel sheet.", e);
            addRedirectAlertMessage(new SimpleMessage("Upload failed - " + e.getMessage()));
        }
        return new RedirectResolution(GenerateReconcilationReportAction.class);
    }

    public Resolution downloadPaymentDifference() throws Exception {
        List<OrderPaymentReconciliation> orderPaymentReconciliationList = new ArrayList<OrderPaymentReconciliation>();
        if (paymentProcess.equalsIgnoreCase("COD")) {
            orderPaymentReconciliationList = getAdminReconciliationService().findPaymentDifferenceInCODOrders(shippingOrderId, gatewayOrderId, startDate, endDate, courier, COD);
        } else {
            orderPaymentReconciliationList = getAdminReconciliationService().findPaymentDifferenceInPrepaidOrders(baseOrderId, baseGatewayOrderId, startDate, endDate, PREPAID);
        }

        if (orderPaymentReconciliationList.isEmpty() == true) {
            addRedirectAlertMessage(new SimpleMessage("No order for given search criteria."));
            return new ForwardResolution("/pages/admin/generateReconcilationReport.jsp");
        }

        String excelFilePath = adminDownloadsPath + "/reports/ReconDiffReport" + sdf.format(new Date()) + ".xls";
        excelFile = new File(excelFilePath);
        if (paymentProcess.equalsIgnoreCase("COD")) {
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

    @Validate(converter = CustomDateTypeConvertor.class)
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    @Validate(converter = CustomDateTypeConvertor.class)
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

    public PincodeService getPincodeService() {
        return pincodeService;
    }

    public void setPincodeService(PincodeService pincodeService) {
        this.pincodeService = pincodeService;
    }

    public ShippingOrderLifecycleService getShippingOrderLifecycleService() {

        return shippingOrderLifecycleService;
    }

    public void setShippingOrderLifecycleService(ShippingOrderLifecycleService shippingOrderLifecycleService) {
        this.shippingOrderLifecycleService = shippingOrderLifecycleService;
    }
}