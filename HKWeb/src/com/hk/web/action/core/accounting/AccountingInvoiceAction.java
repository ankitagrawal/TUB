package com.hk.web.action.core.accounting;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.dto.accounting.InvoiceDto;
import com.hk.admin.dto.accounting.ReverseOrderInvoiceDto;
import com.hk.constants.core.EnumTax;
import com.hk.constants.core.TaxConstants;
import com.hk.domain.order.ReplacementOrder;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.reverseOrder.ReverseOrder;
import com.hk.domain.user.B2bUserDetails;
import com.hk.helper.InvoiceNumHelper;
import com.hk.manager.OrderManager;
import com.hk.pact.dao.TaxDao;
import com.hk.pact.dao.user.B2bUserDetailsDao;
import com.hk.pact.service.order.B2BOrderService;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class AccountingInvoiceAction extends BaseAction {

  /*
  * private PricingDto pricingDto; private PricingDto partialPricingDto;
  */
  private String defaultCourier;

  @Autowired
  OrderManager orderManager;
  @Autowired
  B2bUserDetailsDao b2bUserDetailsDao;

  private boolean cFormAvailable;

  /*
  * @Validate(required = true, encrypted = true) private AccountingInvoice accountingInvoice;
  */

  @Validate(required = true, encrypted = true)
  private ShippingOrder shippingOrder;
  private InvoiceDto invoiceDto;
  private ReverseOrderInvoiceDto reverseOrderInvoiceDto;

  // private Order order;
  private B2bUserDetails b2bUserDetails;
  @Autowired
  B2BOrderService b2bOrderService;

  @Autowired
  TaxDao taxDao;

  private List<EnumTax> enumTaxes = taxDao.getEnumTaxByType(TaxConstants.VAT_TYPE);

  private ReverseOrder reverseOrder;

  @DefaultHandler
  public Resolution pre() {
    if (shippingOrder != null) {
      String invoiceType = InvoiceNumHelper.getInvoiceType(shippingOrder.isServiceOrder(), shippingOrder.getBaseOrder().getB2bOrder());
      if (invoiceType.equals(InvoiceNumHelper.PREFIX_FOR_B2B_ORDER)) {
        b2bUserDetails = b2bUserDetailsDao.getB2bUserDetails(shippingOrder.getBaseOrder().getUser());
      }
      ReplacementOrder replacementOrder = getBaseDao().get(ReplacementOrder.class, shippingOrder.getId());

      if (replacementOrder != null) {
        if (shippingOrder.getBaseOrder().isB2bOrder() != null && shippingOrder.getBaseOrder().isB2bOrder()) {
          cFormAvailable = getB2bOrderService().checkCForm(shippingOrder.getBaseOrder());
          invoiceDto = new InvoiceDto(shippingOrder, b2bUserDetails, cFormAvailable);
        } else
          invoiceDto = new InvoiceDto(replacementOrder, b2bUserDetails, false);
      } else {
        if (shippingOrder.getBaseOrder().isB2bOrder() != null && shippingOrder.getBaseOrder().isB2bOrder()) {
          cFormAvailable = getB2bOrderService().checkCForm(shippingOrder.getBaseOrder());
          invoiceDto = new InvoiceDto(shippingOrder, b2bUserDetails, cFormAvailable);
        } else
          invoiceDto = new InvoiceDto(shippingOrder, b2bUserDetails, false);
      }
      return new ForwardResolution("/pages/accountingInvoice.jsp");
    } else {
      addRedirectAlertMessage(new SimpleMessage("Given shipping order doesnot exist"));
      return new ForwardResolution("pages/admin/adminHome.jsp");
    }
  }

  public Resolution posPrintInvoice() {
    if (shippingOrder != null) {
      String invoiceType = InvoiceNumHelper.getInvoiceType(shippingOrder.isServiceOrder(), shippingOrder.getBaseOrder().getB2bOrder());
      if (invoiceType.equals(InvoiceNumHelper.PREFIX_FOR_B2B_ORDER)) {
        b2bUserDetails = b2bUserDetailsDao.getB2bUserDetails(shippingOrder.getBaseOrder().getUser());
      }
      ReplacementOrder replacementOrder = getBaseDao().get(ReplacementOrder.class, shippingOrder.getId());

      if (replacementOrder != null) {
        invoiceDto = new InvoiceDto(replacementOrder, b2bUserDetails);
      } else {
        invoiceDto = new InvoiceDto(shippingOrder, b2bUserDetails);
      }
      return new ForwardResolution("/pages/pos/posAccountingInvoice.jsp");
    } else {
      addRedirectAlertMessage(new SimpleMessage("Given shipping order doesnot exist"));
      return new ForwardResolution("pages/admin/adminHome.jsp");
    }
  }

  public Resolution reverseOrderInvoice() {
    if (reverseOrder != null) {

      String invoiceType = InvoiceNumHelper.getInvoiceType(shippingOrder.isServiceOrder(), shippingOrder.getBaseOrder().getB2bOrder());
      if (invoiceType.equals(InvoiceNumHelper.PREFIX_FOR_B2B_ORDER)) {
        b2bUserDetails = b2bUserDetailsDao.getB2bUserDetails(shippingOrder.getBaseOrder().getUser());
      }

      reverseOrderInvoiceDto = new ReverseOrderInvoiceDto(reverseOrder, b2bUserDetails);

      return new ForwardResolution("/pages/reverseOrderInvoice.jsp");
    } else {
      addRedirectAlertMessage(new SimpleMessage("Given shipping order doesnot exist"));
      return new ForwardResolution("pages/admin/adminHome.jsp");
    }
  }

	

  public String getDefaultCourier() {
    return defaultCourier;
  }

  public void setDefaultCourier(String defaultCourier) {
    this.defaultCourier = defaultCourier;
  }

  public ShippingOrder getShippingOrder() {
    return shippingOrder;
  }

  public void setShippingOrder(ShippingOrder shippingOrder) {
    this.shippingOrder = shippingOrder;
  }

  public InvoiceDto getInvoiceDto() {
    return invoiceDto;
  }

  public void setInvoiceDto(InvoiceDto invoiceDto) {
    this.invoiceDto = invoiceDto;
  }

  public B2bUserDetails getB2bUserDetails() {
    return b2bUserDetails;
  }

  public void setB2bUserDetails(B2bUserDetails b2bUserDetails) {
    this.b2bUserDetails = b2bUserDetails;
  }

  public List<EnumTax> getEnumTaxes() {
    return enumTaxes;
  }

  public boolean isCFormAvailable() {
    return cFormAvailable;
  }

  public boolean getCFormAvailable() {
    return cFormAvailable;
  }

  public void setCFormAvailable(boolean cFormAvailable) {
    this.cFormAvailable = cFormAvailable;
  }

  public B2BOrderService getB2bOrderService() {
    return b2bOrderService;
  }

  public void setB2bOrderService(B2BOrderService b2bOrderService) {
    this.b2bOrderService = b2bOrderService;
  }

  public ReverseOrderInvoiceDto getReverseOrderInvoiceDto() {
    return reverseOrderInvoiceDto;
  }

  public void setReverseOrderInvoiceDto(ReverseOrderInvoiceDto reverseOrderInvoiceDto) {
    this.reverseOrderInvoiceDto = reverseOrderInvoiceDto;
  }

  public ReverseOrder getReverseOrder() {
    return reverseOrder;
  }

  public void setReverseOrder(ReverseOrder reverseOrder) {
    this.reverseOrder = reverseOrder;
  }
}