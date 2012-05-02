package web.action.admin.order;

import com.akube.framework.stripes.action.BaseAction;


//@Secure(hasAnyRoles = {RoleConstants.MASTER_BUG_RESOLVER})
public class OrderKiMBAction extends BaseAction {

  
  /*LineItemDao lineItemDao;
  
  RetailLineItemDao retailLineItemDao;
  
  ProductVariantDao productVariantDao;
  
  PaymentDao paymentDao;
  
  OrderManager orderManager;
  
  OrderLifecycleActivityDao orderLifecycleActivityDao;
  
  UserDao userDao;
  
  CartLineItemTypeDao cartLineItemTypeDao;
  
  LineItemStatusDao lineItemStatusDao;
  
  ServiceTaxProvider serviceTaxProvider;
  
  OrderLifecycleDao orderLifecycleDao;
  
  ProductVariantInventoryDao productVariantInventoryDao;
  
  InvoiceLineItemDao invoiceLineItemDao;

  public static final Double defaultDiscount = 0.0;

  @Validate(required = true, on = "getMB")
  private Long orderId;

  private String gatewayOrderId;

  private String productVariantId;
  private Order order;
  private ProductVariant productVariant;
  private Payment payment;
  List<Payment> paymentList;
  private LineItem lineItem;
  List<LineItem> lineItems;
  PricingEngine pricingEngine;
  String formerOrderString;
  String latterOrderString;
  List<String> formerpaymentString;
  String latterpaymentString;
  List<String> formerLineItemsString = new ArrayList<String>();
  List<String> latterLineItemsString = new ArrayList<String>();

  @DefaultHandler
  @DontValidate
  public Resolution pre() {
    return new ForwardResolution("/pages/admin/orderKiMB.jsp");
  }

  public Resolution getMB() {

    //TODO: # warehouse fix this.

    User user = null;

    order = orderDao.find(orderId);
    if (getPrincipal() != null) {
      user = userDao.find(getPrincipal().getId());
    }
    formerOrderString = order.getOrderDetails();
    paymentList = paymentDao.listByOrderId(orderId);
    StringBuilder paymentBuilder = new StringBuilder();
    if (paymentList != null) {
      for (Payment payment1 : paymentList) {
        paymentBuilder.append(payment1.getPaymentDetails());
      }
    }
    lineItems = order.getLineItems();
    for (LineItem lineItem : lineItems) {
      formerLineItemsString.add(lineItem.getLineItemDetails());
    }
    StringBuffer sb = new StringBuffer();
    sb.append(formerOrderString);
    sb.append(paymentBuilder);
    for (String s : formerLineItemsString) {
      sb.append(s);
      sb.append("\n");
    }
    if (orderLifecycleDao.containsOrderLifeCycleActivity(order, orderLifecycleActivityDao.find(EnumOrderLifecycleActivity.OrderKiMBOpened.getId())) == null)
      orderManager.logOrderActivity(order, user, orderLifecycleActivityDao.find(EnumOrderLifecycleActivity.OrderKiMBOpened.getId()), sb.toString());
    return new ForwardResolution("/pages/admin/orderKiMB.jsp");
  }

  @Secure(hasAnyPermissions = {PermissionConstants.EDIT_LINEITEM}, authActionBean = AdminPermissionAction.class)
  public Resolution saveLineItems() {
     User user = null;
 order = orderDao.find(orderId);
 if (getPrincipal() != null) {
   user = userDao.find(getPrincipal().getId());
 }

 for (LineItem lineItem : lineItems) {
   if (lineItem.isSelected()) {
     latterLineItemsString.add(lineItem.getLineItemDetails());
     lineItemDao.save(lineItem);
   }
 }
 StringBuffer sb = new StringBuffer();
 for (String s : latterLineItemsString) {
   sb.append(s);
   sb.append("\n");
 }
 orderManager.logOrderActivity(order, user, orderLifecycleActivityDao.find(EnumOrderLifecycleActivity.LineItemUpdated.getId()), sb.toString());
 addRedirectAlertMessage(new SimpleMessage("line items saved"));

    //TODO: # warehouse fix this.

    return new ForwardResolution("/pages/admin/orderKiMB.jsp");
  }

  public Resolution getPVDetails() {
    Map dataMap = new HashMap();
    ProductVariant pv = productVariantDao.find(productVariantId);
    if (pv != null) {
      dataMap.put("variant", pv);
      dataMap.put("product", pv.getProduct().getName());
      dataMap.put("options", pv.getOptionsCommaSeparated());
      HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_OK, "Valid Product Variant", dataMap);
      noCache();
      return new JsonResolution(healthkartResponse);
    }
    HealthkartResponse healthkartResponse = new HealthkartResponse(HealthkartResponse.STATUS_ERROR, "Invalid Product VariantID", dataMap);
    noCache();
    return new JsonResolution(healthkartResponse);
  }

  @Secure(hasAnyPermissions = {PermissionConstants.DELETE_LINEITEM}, authActionBean = AdminPermissionAction.class)
  public Resolution deleteSelectedLineItems() {
    User user = null;
    order = orderDao.find(orderId);
    if (getPrincipal() != null) {
      user = userDao.find(getPrincipal().getId());
    }
    for (LineItem lineItem : lineItems) {
      if (lineItem.isSelected()) {
        List<InvoiceLineItem> invoiceLineItems = invoiceLineItemDao.getInvoiceLineItem(lineItem);
        for (InvoiceLineItem invoiceLineItem : invoiceLineItems) {
          invoiceLineItemDao.remove(invoiceLineItem.getId());
        }
        RetailLineItem retailLineItem = retailLineItemDao.getRetailLineItem(lineItem);
        if (retailLineItem != null) {
          retailLineItemDao.remove(retailLineItem.getId());
        }
        List<ProductVariantInventory> productVariantInventoryList = productVariantInventoryDao.getPVIByLineItem(lineItem);
        for (ProductVariantInventory productVariantInventory : productVariantInventoryList) {
          productVariantInventoryDao.remove(productVariantInventory.getId());
        }
        latterLineItemsString.add(lineItem.getLineItemDetails());
        lineItemDao.remove(lineItem.getId());
      }
    }
    StringBuffer sb = new StringBuffer();
    for (String s : latterLineItemsString) {
      sb.append(s);
      sb.append("\n");
    }
    orderManager.logOrderActivity(order, user, orderLifecycleActivityDao.find(EnumOrderLifecycleActivity.LineItemDeleted.getId()), sb.toString());
    addRedirectAlertMessage(new SimpleMessage("line items deleted"));

    //TODO: # warehouse fix this.

    return new RedirectResolution(OrderKiMBAction.class).addParameter("getMB").addParameter("orderId", orderId);
  }

  @Secure(hasAnyPermissions = {PermissionConstants.ADD_LINEITEM}, authActionBean = AdminPermissionAction.class)
  public Resolution addProductLineItem() {
    User user = null;
    order = orderDao.find(orderId);
    if (getPrincipal() != null) {
      user = userDao.find(getPrincipal().getId());
    }
    for (LineItem lineItem : lineItems) {
      if (lineItem.isSelected()) {
        //latterLineItemsString.add(lineItem.getLineItemDetails());  // PS, NULL Poointer is here - ASK
        latterLineItemsString.add(lineItem.toString());
        lineItem.setShippingOrder(order);
        lineItem.setTax(lineItem.getProductVariant().getTax());
        lineItem.setLineItemType(cartLineItemTypeDao.find(EnumLineItemType.Product.getId()));
        lineItem.setLineItemStatus(lineItemStatusDao.find(EnumLineItemStatus.ACTION_AWAITING.getId()));
        lineItemDao.save(lineItem);
      }
    }
    StringBuffer sb = new StringBuffer();
    for (String s : latterLineItemsString) {
      sb.append(s);
      sb.append("\n");
    }
    orderManager.logOrderActivity(order, user, orderLifecycleActivityDao.find(EnumOrderLifecycleActivity.ProductLineItemAdded.getId()), sb.toString());
    addRedirectAlertMessage(new SimpleMessage("line item added"));
    return new RedirectResolution(OrderKiMBAction.class).addParameter("getMB").addParameter("orderId", orderId);
    //TODO: # warehouse fix this.
    return null;
  }

  @Secure(hasAnyPermissions = {PermissionConstants.ADD_LINEITEM}, authActionBean = AdminPermissionAction.class)
  public Resolution addShippingLineItem() {
    User user = null;
    order = orderDao.find(orderId);
    if (getPrincipal() != null) {
      user = userDao.find(getPrincipal().getId());
    }
    Double shippingAmount = 30.0;
    String shippingLineItemString = "";

    if (order.getCartLineItems(EnumLineItemType.Shipping).size() == 0) {
      LineItem lineItem = new LineItem.Builder()
          .ofType(EnumLineItemType.Shipping)
          .tax(serviceTaxProvider.get())
          .hkPrice(shippingAmount)
          .discountOnHkPrice(defaultDiscount)
          .build();
      lineItem.setMarkedPrice(shippingAmount);
      lineItem.setLineItemStatus(lineItemStatusDao.find(EnumLineItemStatus.NA.getId()));
      lineItem.setShippingOrder(order);
      lineItemDao.save(lineItem);
      shippingLineItemString = lineItem.getLineItemDetails();
    }

    orderManager.logOrderActivity(order, user, orderLifecycleActivityDao.find(EnumOrderLifecycleActivity.ShippingLineItemAdded.getId()), shippingLineItemString);
    addRedirectAlertMessage(new SimpleMessage("shipping line items added"));
    return new RedirectResolution(OrderKiMBAction.class).addParameter("getMB").addParameter("orderId", orderId);
    //TODO: # warehouse fix this.
    return null;
  }

  @Secure(hasAnyPermissions = {PermissionConstants.UPDATE_ORDER}, authActionBean = AdminPermissionAction.class)
  public Resolution savePayments() {
     User user = null;
 order = orderDao.find(orderId);
 paymentList = paymentDao.listByOrderId(orderId);
 if (getPrincipal() != null) {
   user = userDao.find(getPrincipal().getId());
 }
 StringBuilder sb = new StringBuilder();
 for (Payment payment1 : paymentList) {
   if (payment1.isSelected()) {
     payment1 = paymentDao.save(payment1);
     order = orderDao.save(order);
     sb.append(order.getOrderDetails());
     sb.append("\n");
     sb.append(payment1.getPaymentDetails());
   }
 }
 orderManager.logOrderActivity(order, user, orderLifecycleActivityDao.find(EnumOrderLifecycleActivity.OrderEdited.getId()), sb.toString());
 addRedirectAlertMessage(new SimpleMessage("payments saved"));
 return new RedirectResolution(OrderKiMBAction.class).addParameter("getMB").addParameter("orderId", orderId);
    //TODO: # warehouse fix this.

    return null;
  }

  @Secure(hasAnyPermissions = {PermissionConstants.ADD_LINEITEM}, authActionBean = AdminPermissionAction.class)
  public Resolution createRewardPointLineItem() {


    //TODO: # warehouse fix this.
    User user = null;
    order = orderDao.find(orderId);
    if (getPrincipal() != null) {
      user = userDao.find(getPrincipal().getId());
    }
    String rewardPointLineItemString = "";

    if (order.getCartLineItems(EnumLineItemType.RewardPoint).size() == 0) {
      LineItem lineItem = new LineItem.Builder()
          .ofType(EnumLineItemType.RewardPoint)
          .tax(serviceTaxProvider.get())
          .hkPrice(0.0)
          .discountOnHkPrice(0.0)
          .build();
      lineItem.setMarkedPrice(0.0);
      lineItem.setLineItemStatus(lineItemStatusDao.find(EnumLineItemStatus.NA.getId()));
      lineItem.setShippingOrder(order);
      lineItemDao.save(lineItem);
      rewardPointLineItemString = lineItem.getLineItemDetails();
    }

    orderManager.logOrderActivity(order, user, orderLifecycleActivityDao.find(EnumOrderLifecycleActivity.RewardPointLineItemAdded.getId()), rewardPointLineItemString);
    addRedirectAlertMessage(new SimpleMessage("shipping line items added"));
    return new RedirectResolution(OrderKiMBAction.class).addParameter("getMB").addParameter("orderId", orderId);

    return null;
  }

  public Long getOrderId() {
    return orderId;
  }

  public void setOrderId(Long orderId) {
    this.orderId = orderId;
  }

  public String getGatewayOrderId() {
    return gatewayOrderId;
  }

  public void setGatewayOrderId(String gatewayOrderId) {
    this.gatewayOrderId = gatewayOrderId;
  }

  public Order getOrder() {
    return order;
  }

  public void setOrder(Order order) {
    this.order = order;
  }

  public List<LineItem> getLineItems() {
    return lineItems;
  }

  public void setLineItems(List<LineItem> lineItems) {
    this.lineItems = lineItems;
  }

  public ProductVariant getProductVariant() {
    return productVariant;
  }

  public void setProductVariant(ProductVariant productVariant) {
    this.productVariant = productVariant;
  }

  public String getProductVariantId() {
    return productVariantId;
  }

  public void setProductVariantId(String productVariantId) {
    this.productVariantId = productVariantId;
  }

  public Payment getPayment() {
    return payment;
  }

  public void setPayment(Payment payment) {
    this.payment = payment;
  }

  public LineItem getLineItem() {
    return lineItem;
  }

  public void setLineItem(LineItem lineItem) {
    this.lineItem = lineItem;
  }

  public List<Payment> getPaymentList() {
    return paymentList;
  }

  public void setPaymentList(List<Payment> paymentList) {
    this.paymentList = paymentList;
  }*/
}
