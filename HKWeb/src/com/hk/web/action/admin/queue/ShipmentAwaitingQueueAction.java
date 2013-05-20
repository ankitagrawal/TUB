package com.hk.web.action.admin.queue;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hk.domain.courier.Zone;
import com.hk.loyaltypg.service.LoyaltyProgramService;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.pact.service.accounting.SeekInvoiceNumService;
import com.hk.admin.pact.service.courier.CourierService;
import com.hk.admin.pact.service.shippingOrder.AdminShippingOrderService;
import com.hk.admin.util.InvoicePDFGenerator;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.courier.EnumCourier;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.courier.Courier;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.warehouse.Warehouse;
import com.hk.helper.InvoiceNumHelper;
import com.hk.pact.service.UserService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.report.manager.ReportManager;
import com.hk.util.CustomDateTypeConvertor;
import com.hk.web.action.error.AdminPermissionAction;

@Component
public class ShipmentAwaitingQueueAction extends BasePaginatedAction {

  private static Logger              logger            = LoggerFactory.getLogger(ShipmentAwaitingQueueAction.class);

  Page                               shippingOrderPage;

  List<LineItem>                     lineItems         = new ArrayList<LineItem>();

  @Autowired
  private ShippingOrderService       shippingOrderService;
  @Autowired
  private AdminShippingOrderService  adminShippingOrderService;
  @Autowired
  private SeekInvoiceNumService      seekInvoiceNumService;
  @Autowired
  private ShippingOrderStatusService shippingOrderStatusService;
  @Autowired
  private CourierService             courierService;
  @Autowired
  InvoicePDFGenerator                invoicePDFgenerator;

  List<ShippingOrder>                shippingOrderList = new ArrayList<ShippingOrder>();

  private Long                       orderId;
  private String                     gatewayOrderId;
  private Courier                    courier;
  private SimpleDateFormat           sdf               = new SimpleDateFormat("yyyyMMdd");

  @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
  String                             adminDownloads;
  File                               xlsFile;
  @Autowired
  ReportManager                      reportGenerator;

  private Integer                    defaultPerPage    = 30;

  private Boolean                    courierDownloadFunctionality;

  private Date                       startDate;

  private Date                       endDate;
  @Autowired
  private UserService                userService;
	
  private Warehouse                  warehouse;

  private Zone zone;

  @DontValidate
  @DefaultHandler
  // @Secure(hasAnyPermissions = { PermissionConstants.VIEW_SHIPMENT_QUEUE }, authActionBean =
  // AdminPermissionAction.class)
  public Resolution pre() {
    return searchOrders();
  }

  public Resolution searchOrders() {
    List<Courier> courierList = new ArrayList<Courier>();
    if (courier == null) {
      courierList = courierService.getAllCouriers();
    } else {
      courierList.add(courier);
    }

    ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
    shippingOrderSearchCriteria.setShippingOrderStatusList(shippingOrderStatusService.getOrderStatuses(EnumShippingOrderStatus.getStatusForShipmentAwaiting()));
    shippingOrderSearchCriteria.setOrderId(orderId).setGatewayOrderId(gatewayOrderId);
    shippingOrderSearchCriteria.setOrderAsc(true);
    shippingOrderSearchCriteria.setCourierList(courierList);
	shippingOrderSearchCriteria.setShipmentZone(zone);
    shippingOrderPage = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria, getPageNo(), getPerPage());
    if (shippingOrderPage != null) {
      shippingOrderList = shippingOrderPage.getList();
    }

    return new ForwardResolution("/pages/admin/shipmentAwaitingQueue.jsp");
  }

  @Secure(hasAnyPermissions = { PermissionConstants.UPDATE_SHIPMENT_QUEUE }, authActionBean = AdminPermissionAction.class)
  public Resolution moveToActionAwaiting() {
    logger.info("shipment queue move to action awaiting");
    for (ShippingOrder shippingOrder : shippingOrderList) {
      adminShippingOrderService.moveShippingOrderBackToActionQueue(shippingOrder);
    }
    addRedirectAlertMessage(new SimpleMessage("Orders have been moved back to Action Awaiting"));
    return new RedirectResolution(ShipmentAwaitingQueueAction.class);
  }

  @Secure(hasAnyPermissions = { PermissionConstants.UPDATE_SHIPMENT_QUEUE }, authActionBean = AdminPermissionAction.class)
  public Resolution markShippingOrdersAsShipped() {
    logger.info("shipment queue mark as shipped");
    if (shippingOrderList != null && !shippingOrderList.isEmpty()) {
      for (ShippingOrder shippingOrder : shippingOrderList) {
        // shippingOrder.setAccountingInvoiceNumber();
        String invoiceType = InvoiceNumHelper.getInvoiceType(shippingOrder.isServiceOrder(), shippingOrder.getBaseOrder().isB2bOrder());
        shippingOrder.setAccountingInvoiceNumber(seekInvoiceNumService.getInvoiceNum(invoiceType, shippingOrder.getWarehouse()));
        adminShippingOrderService.markShippingOrderAsShipped(shippingOrder);
      }
      addRedirectAlertMessage(new SimpleMessage("Orders have been marked as shipped"));
    } else {
      addRedirectAlertMessage(new SimpleMessage("Please select at least one order to be marked as shipped"));
    }
    return new RedirectResolution(ShipmentAwaitingQueueAction.class);
  }

  public Resolution generatePDFs() {
    String pdfFilePath = null;
    if (courier != null) {
      pdfFilePath = adminDownloads + "/invoicePDFs/" + sdf.format(new Date()) + "/" + courier.getName() + ".pdf";
    } else {
      pdfFilePath = adminDownloads + "/invoicePDFs/" + sdf.format(new Date()) + "/All_Couriers.pdf";
    }
    final File pdfFile = new File(pdfFilePath);
    pdfFile.getParentFile().mkdirs();
    try {
      List<Courier> courierList = new ArrayList<Courier>();
      if (courier == null) {
        courierList = courierService.getAllCouriers();
      } else {
        courierList.add(courier);
      }
      ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
      shippingOrderSearchCriteria.setShippingOrderStatusList(shippingOrderStatusService.getOrderStatuses(EnumShippingOrderStatus.getStatusForShipmentAwaiting()));
      shippingOrderSearchCriteria.setCourierList(courierList);
      shippingOrderList = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria, true);

      if (shippingOrderList != null & shippingOrderList.size() > 0) {
        invoicePDFgenerator.generateMasterInvoicePDF(shippingOrderList, pdfFilePath);
        return new Resolution() {

          public void execute(HttpServletRequest req, HttpServletResponse res) throws Exception {
            OutputStream out = null;
            InputStream in = null;
            try {
              in = new BufferedInputStream(new FileInputStream(pdfFile));
              res.setContentLength((int) pdfFile.length());
              res.setHeader("Content-Disposition", "attachment; filename=\"" + pdfFile.getName() + "\";");
              out = res.getOutputStream();

              // Copy the contents of the file to the output stream
              byte[] buf = new byte[8192];
              int count = 0;
              while ((count = in.read(buf)) >= 0) {
                out.write(buf, 0, count);
              }
            } finally {
              out.flush();
              out.close();
              in.close();
            }
          }
        };
      }
    } catch (Exception ex) {
      logger.error("Exception occurred while generating pdf.", ex);
    }
    addRedirectAlertMessage(new SimpleMessage("Sorry! No shipping orders exist for courier:" + (courier != null ? courier.getName().toUpperCase() : "All")));
    return new RedirectResolution(ShipmentAwaitingQueueAction.class);
  }

  @DontValidate
  @Secure(hasAnyPermissions = { PermissionConstants.DOWNLOAD_COURIER_EXCEL }, authActionBean = AdminPermissionAction.class)
  public Resolution generateCourierReport() {
    if(getWarehouse() == null ){
      if (userService.getWarehouseForLoggedInUser() != null) {
        setWarehouse( getUserService().getWarehouseForLoggedInUser() );
      }
    }
    if (courierDownloadFunctionality) {
      try {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        xlsFile = new File(adminDownloads + "/reports/courier-report-" + sdf.format(new Date()) + ".xls");
        List<Courier> courierList = new ArrayList<Courier>();
        if (courier == null) {
          courierList = courierService.getAllCouriers();
        } else {
          courierList.add(courier);
        }
        if (courier != null) {
          if (courier.equals(courierService.getCourierById(EnumCourier.BlueDart.getId()))) {
            xlsFile = reportGenerator.generateCourierReportXslForBlueDart(xlsFile.getPath(), EnumShippingOrderStatus.SO_Packed, courierList, startDate, endDate, warehouse);
          } else {
            xlsFile = reportGenerator.generateCourierReportXsl(xlsFile.getPath(), EnumShippingOrderStatus.SO_Packed, courierList, startDate, endDate, warehouse, zone);
          }
        } else {
          xlsFile = reportGenerator.generateCourierReportXsl(xlsFile.getPath(), EnumShippingOrderStatus.SO_Packed, courierList, startDate, endDate, warehouse,zone);
        }
        addRedirectAlertMessage(new SimpleMessage("Courier report successfully generated."));
      } catch (Exception e) {
        logger.error("Error while generating report", e);
        addRedirectAlertMessage(new SimpleMessage("Courier report generation failed"));
      }

      return new HTTPResponseResolution();
    } else {
      return new ForwardResolution("/pages/admin/downloadCourierExcel.jsp");
    }

  }

  /**
   * Custom resolution for HTTP response. The resolution will write the output file in response
   */

  public class HTTPResponseResolution implements Resolution {
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
    return 30;
  }

  public int getPageCount() {
    return shippingOrderPage == null ? 0 : shippingOrderPage.getTotalPages();
  }

  public int getResultCount() {
    return shippingOrderPage == null ? 0 : shippingOrderPage.getTotalResults();
  }

  public Set<String> getParamSet() {
    HashSet<String> params = new HashSet<String>();
	params.add("gatewayOrderId");
	params.add("zone");
	params.add("orderId");
	params.add("courier");
	return params;
  }

  public List<ShippingOrder> getShippingOrderList() {
    return shippingOrderList;
  }

  public void setShippingOrderList(List<ShippingOrder> shippingOrderList) {
    this.shippingOrderList = shippingOrderList;
  }

  public List<LineItem> getLineItems() {
    return lineItems;
  }

  public void setLineItems(List<LineItem> lineItems) {
    this.lineItems = lineItems;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }

  public void setGatewayOrderId(String gatewayOrderId) {
    this.gatewayOrderId = gatewayOrderId;
  }

  public String getGatewayOrderId() {
    return gatewayOrderId;
  }

  public Courier getCourier() {
    return courier;
  }

  public void setCourier(Courier courier) {
    this.courier = courier;
  }

  public Integer getDefaultPerPage() {
    return defaultPerPage;
  }

  public void setDefaultPerPage(Integer defaultPerPage) {
    this.defaultPerPage = defaultPerPage;
  }

  public void setShippingOrderService(ShippingOrderService shippingOrderService) {
    this.shippingOrderService = shippingOrderService;
  }

  public void setShippingOrderStatusService(ShippingOrderStatusService shippingOrderStatusService) {
    this.shippingOrderStatusService = shippingOrderStatusService;
  }

  public void setCourierService(CourierService courierService) {
    this.courierService = courierService;
  }

  public void setSeekInvoiceNumService(SeekInvoiceNumService seekInvoiceNumService) {
    this.seekInvoiceNumService = seekInvoiceNumService;
  }

  public Boolean isCourierDownloadFunctionality() {
    return courierDownloadFunctionality;
  }

  public void setCourierDownloadFunctionality(Boolean courierDownloadFunctionality) {
    this.courierDownloadFunctionality = courierDownloadFunctionality;
  }

  public Date getStartDate() {
    return startDate;
  }

  public Long getOrderId() {
    return orderId;
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

  public UserService getUserService() {
    return userService;
  }

  public void setUserService(UserService userService) {
    this.userService = userService;
  }

  public Warehouse getWarehouse() {
    return warehouse;
  }

  public void setWarehouse(Warehouse warehouse) {
    this.warehouse = warehouse;
  }

  public Zone getZone() {
 	return zone;
  }

  public void setZone(Zone zone) {
	this.zone = zone;
  }
}
