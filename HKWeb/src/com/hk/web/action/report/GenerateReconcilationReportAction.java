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

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;


import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.impl.dao.courier.ShipmentDao;
import com.hk.constants.core.PermissionConstants;
import com.hk.dao.order.OrderDao;
import com.hk.dao.shippingOrder.ShippingOrderDao;
import com.hk.dao.warehouse.WarehouseDao;
import com.hk.domain.courier.Courier;
import com.hk.domain.courier.CourierServiceInfo;
import com.hk.report.dto.order.reconcilation.ReconcilationReportDto;
import com.hk.report.pact.service.shippingOrder.ReportShippingOrderService;
import com.hk.service.UserService;
import com.hk.util.io.HkXlsWriter;
import com.hk.web.action.error.AdminPermissionAction;

@Secure (hasAnyPermissions = {PermissionConstants.VIEW_RECONCILIATION_REPORTS}, authActionBean = AdminPermissionAction.class)
@Component
public class GenerateReconcilationReportAction extends BaseAction {
 
 UserService userService;

 
 OrderDao orderDao;

 
 ShipmentDao shipmentDao;

 
 ShippingOrderDao shippingOrderDao;

 
  ReportShippingOrderService shippingOrderReportingService;

 
  WarehouseDao warehouseDao;

  
  //@Named (Keys.Env.adminDownloads)
  String adminDownloadsPath;
   
  private Date startDate;
  private Date endDate;
  private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
  private String paymentProcess;
  private CourierServiceInfo courierServiceInfo;
  
  private Long warehouseId;
  private Courier courier;

  @DefaultHandler
  public Resolution pre() {
    return new ForwardResolution("/pages/admin/generateReconciilationReport.jsp");
  }

  public Resolution generateReconilationReport() {
    List<ReconcilationReportDto> reconcilationReportDtoList = new ArrayList<ReconcilationReportDto>();
    reconcilationReportDtoList = shippingOrderReportingService.findReconcilationReportByDate(startDate, endDate, paymentProcess, courier, warehouseId);
    if(reconcilationReportDtoList.isEmpty() ==  true){
      addRedirectAlertMessage(new SimpleMessage("No order for given search criteria."));
      return new ForwardResolution("/pages/admin/generateReconciilationReport.jsp");
    }

    String excelFilePath = adminDownloadsPath + "/reports/ReconReport" + sdf.format(new Date()) + ".xls";
    final File excelFile = new File(excelFilePath);

    HkXlsWriter xlsWriter = new HkXlsWriter();
    xlsWriter.addHeader("SHIPPING ORDER ID", "SHIPPING ORDER ID");
    xlsWriter.addHeader("ORDER DATE", "ORDER DATE");
    xlsWriter.addHeader("NAME", "NAME");
    xlsWriter.addHeader("CITY", "CITY");
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
      xlsWriter.addCell(row, reconcilationReportDto.getOrderDate());
      xlsWriter.addCell(row, reconcilationReportDto.getName());
      xlsWriter.addCell(row, reconcilationReportDto.getCity());
      xlsWriter.addCell(row, reconcilationReportDto.getPayment());
      xlsWriter.addCell(row, reconcilationReportDto.getTotal());
      xlsWriter.addCell(row, reconcilationReportDto.getCourier().getName());
      xlsWriter.addCell(row, reconcilationReportDto.getAwb());
      xlsWriter.addCell(row, reconcilationReportDto.getShipmentDate());
      if(reconcilationReportDto.getDeliveryDate() == null){
        xlsWriter.addCell(row, "");
      }
      else{
        xlsWriter.addCell(row, reconcilationReportDto.getDeliveryDate());
      }

      xlsWriter.addCell(row, reconcilationReportDto.getReconciled());
      xlsWriter.addCell(row, reconcilationReportDto.getOrderStatus());
      xlsWriter.addCell(row, reconcilationReportDto.getBoxWeight());
      xlsWriter.addCell(row, reconcilationReportDto.getBoxSize());
      xlsWriter.addCell(row, reconcilationReportDto.getWarehouse().getName());
      row++;
    }
    xlsWriter.writeData(excelFile, "Reconciliation_report");
    addRedirectAlertMessage(new SimpleMessage("Downlaod complete"));

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


//    return new ForwardResolution("/pages/admin/generateReconciilationReport.jsp");
  }

  public Date getStartDate(){
    return startDate;
  }

  public void setStartDate(Date startDate){
    this.startDate = startDate;
  }


  public Date getEndDate(){
    return endDate;
  }

  public void setEndDate(Date endDate){
    this.endDate = endDate;
  }

  public String getPaymentProcess(){
    return paymentProcess;
  }

  public void setPaymentProcess(String paymentProcess){
    this.paymentProcess = paymentProcess;
  }

  public Long getWarehouseId(){
    return warehouseId;
  }

  public void setWarehouseId(Long warehouseId){
    this.warehouseId = warehouseId;
  }

  public Courier getCourier() {
    return courier;
  }

  public void setCourier(Courier courier) {
    this.courier = courier;
  }
}