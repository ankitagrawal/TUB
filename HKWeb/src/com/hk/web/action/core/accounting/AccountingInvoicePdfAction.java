package com.hk.web.action.core.accounting;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.SimpleError;
import net.sourceforge.stripes.validation.Validate;
import net.sourceforge.stripes.validation.ValidationMethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.util.AccountingInvoicePdfGenerator;
import com.hk.constants.core.Keys;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.order.ShippingOrder;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.util.CustomDateTypeConvertor;
import com.hk.web.action.admin.queue.ShipmentAwaitingQueueAction;

/**
 * Created by IntelliJ IDEA.
 * User: Rajni
 * Date: May 18, 2012
 * Time: 11:11:35 AM
 * To change this template use File | Settings | File Templates.
 */
public class AccountingInvoicePdfAction extends BaseAction {

  private static Logger                      logger                       = LoggerFactory.getLogger(AccountingInvoicePdfAction.class);
  // @Named(Keys.Env.adminDownloads)
  @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
  String                                       adminDownloads;

  @Autowired
  AccountingInvoicePdfGenerator              accountingInvoicePdfGenerator;

  @Autowired
  ShippingOrderStatusService                 shippingOrderStatusService;

  @Autowired
  ShippingOrderService                       shippingOrderService;

  private SimpleDateFormat                   sdf                           = new SimpleDateFormat("dd-MM-yyyy");
  private List<ShippingOrder>                shippingOrderList;
  private Date                               startDate;
  private Date                               endDate;


   @ValidationMethod(on = {"downloadAccountingInvoicePDF"})
   public void validateDateGap(){
        long dayConverter = 24 * 3600 * 1000;
        long dayDifference = (endDate.getTime() - startDate.getTime()) / dayConverter;
        if (dayDifference > 2l) {
            getContext().getValidationErrors().add("1", new SimpleError("Please enter the dates with a gap of MAX 2 days."));
        }
    }

  @DefaultHandler
  public Resolution pre() {
    return new ForwardResolution("/pages/admin/finance.jsp");
  }

  public Resolution downloadAccountingInvoicePDF() {
    logger.info("Inside pre method of AccountingInvoicePdfAction.");
    String pdfFilePath = null;
    ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
    pdfFilePath = adminDownloads + "/accountingInvoicePDFs/" + sdf.format(new Date()) + "/All_Accounting_Invoices" + ".pdf";
    final File pdfFile = new File(pdfFilePath);
    pdfFile.getParentFile().mkdirs();
    shippingOrderSearchCriteria.setShipmentStartDate(startDate).setShipmentEndDate(endDate);
    shippingOrderList = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria);
    if (shippingOrderList != null & shippingOrderList.size() > 0) {

      try {
        accountingInvoicePdfGenerator.generateAccountingInvoicePDF(shippingOrderList, pdfFilePath);
      } catch (Exception ex) {
        addRedirectAlertMessage(new SimpleMessage("Sorry ! Some problem occurred while generating Accouunting Invoice PDF."));
        return new RedirectResolution(ShipmentAwaitingQueueAction.class);
      }
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
          } catch (Exception ex) {
            logger.error("Exception occurred while generating accounting invoice pdf." + ex.getMessage());

          }
          finally {
            out.flush();
            out.close();
            in.close();
          }

        }
      };
    }
    addRedirectAlertMessage(new SimpleMessage("Sorry! There are no shipped orders."));
    return new RedirectResolution(AccountingInvoicePdfAction.class);
  }

  public List<ShippingOrder> getShippingOrderList() {
    return shippingOrderList;
  }

  public void setShippingOrderList(List<ShippingOrder> shippingOrderList) {
    this.shippingOrderList = shippingOrderList;
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
}
