package com.hk.pact.service.accounting;

import java.util.List;

import org.springframework.stereotype.Component;

import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;

@Component
public class InvoiceService {

  
 /*AccountingInvoiceDao> accountingInvoiceDaoProvider;
  
 InvoiceLineItemDao> invoiceLineItemDaoProvider;*/


  public void generateServiceInvoiceForLineItems(Order order, List<CartLineItem> lineItems) {
    //TODO: # warehouse fix this
    /*AccountingInvoice accountingInvoice = new AccountingInvoice();
    accountingInvoice.setShippingOrder(order);
    Long serviceInvoiceId = accountingInvoiceDaoProvider.get().getLastServiceInvoiceId();
    if (serviceInvoiceId != null) {
      serviceInvoiceId += 1L;
    } else {
      serviceInvoiceId = 1L;
    }
    accountingInvoice.setServiceInvoiceId(serviceInvoiceId);
    accountingInvoice.setInvoiceDate(new Date());
    accountingInvoice = accountingInvoiceDaoProvider.get().save(accountingInvoice);

    for (CartLineItem lineItem : lineItems) {
      InvoiceLineItem invoiceLineItem = new InvoiceLineItem();
      invoiceLineItem.setLineItem(lineItem);
      invoiceLineItem.setAccountingInvoice(accountingInvoice);
      invoiceLineItemDaoProvider.get().save(invoiceLineItem);
    }*/
  }

  public void generateRetailInvoice(Order order) {
                  //TODO: # warehouse fix this
    /*List<CartLineItem> productLineItems = new ArrayList<CartLineItem>();
    List<CartLineItem> servicesLineItems = new ArrayList<CartLineItem>();
    for (CartLineItem lineItem : order.getLineItemsOfTypeAndStatus(EnumLineItemType.Product, EnumLineItemStatus.HANDED_OVER_TO_COURIER)) {
      if (lineItem.getProductVariant().getProduct().isService()) {
        servicesLineItems.add(lineItem);
      } else {
        List<InvoiceLineItem> ilis = invoiceLineItemDaoProvider.get().getInvoiceLineItem(lineItem);
        if (ilis == null || ilis.isEmpty()) {
          productLineItems.add(lineItem);
        }
      }
    }
    if (productLineItems != null && !productLineItems.isEmpty()) {
      if (order.getUser().getRoles().contains(getRoleService().getRoleByName(EnumRole.B2B_USER.getRoleName()))) {
        generateB2BInvoiceForLineItems(order, productLineItems);
      } else {
        generateRetailInvoiceForLineItems(order, productLineItems);
      }
    }*/
  }

  @SuppressWarnings("unused")
private void generateRetailInvoiceForLineItems(Order order, List<CartLineItem> lineItems) {
    //TODO: # warehouse fix this
   /* AccountingInvoice accountingInvoice = new AccountingInvoice();
    accountingInvoice.setShippingOrder(order);
    Long retailInvoiceId = accountingInvoiceDaoProvider.get().getLastRetailInvoiceId();
    if (retailInvoiceId != null) {
      retailInvoiceId += 1L;
    } else {
      retailInvoiceId = 1L;
    }
    accountingInvoice.setRetailInvoiceId(retailInvoiceId);
    accountingInvoice.setInvoiceDate(new Date());
    accountingInvoice = accountingInvoiceDaoProvider.get().save(accountingInvoice);

    for (CartLineItem lineItem : lineItems) {
      InvoiceLineItem invoiceLineItem = new InvoiceLineItem();
      invoiceLineItem.setLineItem(lineItem);
      invoiceLineItem.setAccountingInvoice(accountingInvoice);
      invoiceLineItemDaoProvider.get().save(invoiceLineItem);
    }
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
    File directory = new File(adminDownloadsPath + "/invoices/" + sdf.format(new Date()) + "/");
    directory.mkdirs();
    String inputFileName = directory.getAbsolutePath() + "/R-" + accountingInvoice.getId().toString() + ".html";
    String outputFileName = directory.getAbsolutePath() + "/R-" + accountingInvoice.getId().toString() + ".pdf";
    logger.debug("Going to generate pdf - " + outputFileName);
    String webUrl = linkManager.getRetailInvoiceLink(accountingInvoice);
    logger.debug("Going to generate pdf for webUrl - " + webUrl);
    generateAndSavePdf.generatePdf(webUrl, inputFileName, outputFileName);*/

  }

  @SuppressWarnings("unused")
private void generateB2BInvoiceForLineItems(Order order, List<CartLineItem> lineItems) {
    //TODO: # warehouse fix this
   /* AccountingInvoice accountingInvoice = new AccountingInvoice();
    accountingInvoice.setShippingOrder(order);
    Long b2bInvoiceId = accountingInvoiceDaoProvider.get().getLastB2BInvoiceId();
    if (b2bInvoiceId != null) {
      b2bInvoiceId += 1L;
    } else {
      b2bInvoiceId = 1L;
    }
    accountingInvoice.setB2bInvoiceId(b2bInvoiceId);
    accountingInvoice.setInvoiceDate(new Date());
    accountingInvoice = accountingInvoiceDaoProvider.get().save(accountingInvoice);

    for (CartLineItem lineItem : lineItems) {
      InvoiceLineItem invoiceLineItem = new InvoiceLineItem();
      invoiceLineItem.setLineItem(lineItem);
      invoiceLineItem.setAccountingInvoice(accountingInvoice);
      invoiceLineItemDaoProvider.get().save(invoiceLineItem);
    }*/
  }


  public boolean invoiceAlreadyGenerated(ShippingOrder shippingOrder) {
    //TODO: # warehouse fix this
    /* List<AccountingInvoice> accountingInvoices = shippingOrder.getAccountingInvoices();
     if (accountingInvoices != null && !accountingInvoices.isEmpty()) {
       AccountingInvoice latestInvoice = accountingInvoices.get(0);
       List<LineItem> lineItemsInInvoice = new ArrayList<LineItem>();
       for (InvoiceLineItem invoiceLineItem : latestInvoice.getInvoiceLineItems()) {
         lineItemsInInvoice.add(invoiceLineItem.getLineItem());
       }
       List<CartLineItem> inProcessLineItems = shippingOrder.getLineItemsOfTypeAndStatus(EnumLineItemType.Product, EnumLineItemStatus.CHECKEDOUT);
       if (lineItemsInInvoice.containsAll(inProcessLineItems)) {
         return true;
       }
     }            */
     return false;
   }
  
}
