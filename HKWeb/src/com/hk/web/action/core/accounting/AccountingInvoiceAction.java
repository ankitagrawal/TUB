package com.hk.web.action.core.accounting;

import java.util.Arrays;
import java.util.List;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.akube.framework.stripes.action.BaseAction;
import com.hk.admin.dto.accounting.InvoiceDto;
import com.hk.constants.core.EnumTax;
import com.hk.domain.order.ReplacementOrder;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.user.B2bUserDetails;
import com.hk.helper.InvoiceNumHelper;
import com.hk.manager.OrderManager;
import com.hk.pact.dao.user.B2bUserDetailsDao;

@Component
public class AccountingInvoiceAction extends BaseAction {

    /*
     * private PricingDto pricingDto; private PricingDto partialPricingDto;
     */
    private String         defaultCourier;

    @Autowired
    OrderManager           orderManager;
    @Autowired
    B2bUserDetailsDao      b2bUserDetailsDao;

    /*
     * @Validate(required = true, encrypted = true) private AccountingInvoice accountingInvoice;
     */

    @Validate(required = true, encrypted = true)
    private ShippingOrder  shippingOrder;
    private InvoiceDto     invoiceDto;
    // private Order order;
    private B2bUserDetails b2bUserDetails;
    
    private List<EnumTax> enumTaxes = Arrays.asList(EnumTax.values());

    @DefaultHandler
    public Resolution pre() {
        if (shippingOrder != null) {
            String invoiceType = InvoiceNumHelper.getInvoiceType(shippingOrder.isServiceOrder(), shippingOrder.getBaseOrder().getB2bOrder());
            if (invoiceType.equals(InvoiceNumHelper.PREFIX_FOR_B2B_ORDER)) {
                b2bUserDetails = b2bUserDetailsDao.getB2bUserDetails(shippingOrder.getBaseOrder().getUser());
            }
            ReplacementOrder replacementOrder = getBaseDao().get(ReplacementOrder.class, shippingOrder.getId());

            if(replacementOrder != null){
              invoiceDto = new InvoiceDto(replacementOrder, b2bUserDetails);
            }
            else{
              invoiceDto = new InvoiceDto(shippingOrder, b2bUserDetails);
            }
            return new ForwardResolution("/pages/accountingInvoice.jsp");
        } else {
            addRedirectAlertMessage(new SimpleMessage("Given shipping order doesnot exist"));
            return new ForwardResolution("pages/admin/adminHome.jsp");
        }
    }

    @DontValidate
    public Resolution oldAccountingInvoice() {
        // TODO: # warehouse fix this

        /* pricingDto = new PricingDto(order.getLineItems(), order.getAddress()); */
        return new ForwardResolution("/pages/oldAccountingInvoice.jsp");
    }

    public Resolution retailInvoice() {
        // TODO: # warehouse fix this
        /*
         * order = accountingInvoice.getShippingOrder(); // Partial invoice/Re-ship logic will come here for line items
         * List<LineItem> lineItems = new ArrayList<LineItem>(); for (InvoiceLineItem invoiceLineItem :
         * accountingInvoice.getInvoiceLineItems()) { lineItems.add(invoiceLineItem.getLineItem()); } if
         * (lineItems.isEmpty()) { lineItems = order.getLineItems(); } if (lineItems.get(0).getCourier() != null) {
         * defaultCourier = lineItems.get(0).getCourier().getName(); } partialPricingDto = new PricingDto(lineItems,
         * order.getAddress()); pricingDto = new PricingDto(order.getLineItems(), order.getAddress());
         */
        return new ForwardResolution("/pages/retailInvoice.jsp");
    }

    public Resolution b2bInvoice() {
        // TODO: # warehouse fix this

        /*
         * order = accountingInvoice.getShippingOrder(); b2bUserDetails
         * =b2bUserDetailsDao.getB2bUserDetails(order.getUser()); // Partial invoice/Re-ship logic will come here for
         * line items List<LineItem> lineItems = new ArrayList<LineItem>(); for (InvoiceLineItem invoiceLineItem :
         * accountingInvoice.getInvoiceLineItems()) { lineItems.add(invoiceLineItem.getLineItem()); } if
         * (lineItems.isEmpty()) { lineItems = order.getLineItems(); } if (lineItems.get(0).getCourier() != null) {
         * defaultCourier = lineItems.get(0).getCourier().getName(); } partialPricingDto = new PricingDto(lineItems,
         * order.getAddress()); pricingDto = new PricingDto(order.getLineItems(), order.getAddress());
         */
        return new ForwardResolution("/pages/b2bInvoice.jsp");
    }

    public Resolution serviceInvoice() {
        // TODO: # warehouse fix this
        /*
         * order = accountingInvoice.getShippingOrder(); // Partial invoice/Re-ship logic will come here for line items
         * List<LineItem> lineItems = new ArrayList<LineItem>(); for (InvoiceLineItem invoiceLineItem :
         * accountingInvoice.getInvoiceLineItems()) { lineItems.add(invoiceLineItem.getLineItem()); } if
         * (lineItems.isEmpty()) { lineItems = order.getLineItems(); } partialPricingDto = new PricingDto(lineItems,
         * order.getAddress()); pricingDto = new PricingDto(order.getLineItems(), order.getAddress());
         */
        return new ForwardResolution("/pages/serviceInvoice.jsp");
    }

    /*
     * public PricingDto getPricingDto() { return pricingDto; } public PricingDto getPartialPricingDto() { return
     * partialPricingDto; }
     */

    /*
     * public AccountingInvoice getAccountingInvoice() { return accountingInvoice; } public void
     * setAccountingInvoice(AccountingInvoice accountingInvoice) { this.accountingInvoice = accountingInvoice; }
     */

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
}