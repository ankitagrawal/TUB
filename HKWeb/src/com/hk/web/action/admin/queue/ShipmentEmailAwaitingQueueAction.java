package com.hk.web.action.admin.queue;

public class ShipmentEmailAwaitingQueueAction {
  //extends BasePaginatedAction {

  /*Page orderPage;
  List<ShippingOrder> orderList = new ArrayList<ShippingOrder>();

  List<LineItem> lineItems = new ArrayList<LineItem>();

  
  OrderManager orderManager;
  
  LineItemDao lineItemDao;
  
  OrderDao orderDao;
  
  OrderStatusDao orderStatusDao;
  
  UserDao userDao;
  
  CourierDao courierDao;

  private Long orderId;
  private String gatewayOrderId;
  private Courier courier;

  
  @Named(Keys.Env.adminDownloads)
  String adminDownloads;
  File xlsFile;

  
  ReportManager reportGenerator;
  
  OrderLifecycleActivityDao orderLifecycleActivityDao;
  
  LineItemStatusDao lineItemStatusDao;
  
  ShippingOrderService shippingOrderService;

  @DontValidate
  @DefaultHandler
  @Secure(hasAnyPermissions = {PermissionConstants.VIEW_SHIPPED_EMAIL_AWAIT_Q}, authActionBean = AdminPermissionAction.class)
  public Resolution pre() {
   *//* orderPage = shippingOrderService.getShipmentEmailAwatingOrders(getPageNo(), getPerPage());
    orderList = orderPage.getList();*//*

    return new ForwardResolution("/pages/admin/shipmentEmailAwaitingQueue.jsp");
  }

  public Resolution searchOrders() {
   *//* List<Courier> courierList = new ArrayList<Courier>();
    if (courier == null) {
      courierList = courierDao.listAll();
    } else {
      courierList.add(courier);
    }

    ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
    shippingOrderSearchCriteria.setOrderId(orderId).setGatewayOrderId(gatewayOrderId);
    shippingOrderSearchCriteria.setCourierList(courierList);

    orderPage = shippingOrderService.searchShipmentAwaitingOrders(shippingOrderSearchCriteria, getPageNo(), getPerPage()) ;
    
    if (orderPage != null) {
      orderList.addAll(orderPage.getList());
    }*//*
    return new ForwardResolution("/pages/admin/shipmentEmailAwaitingQueue.jsp");

  }

  @DontValidate
  @Secure(hasAnyPermissions = {PermissionConstants.DOWNLOAD_COURIER_EXCEL}, authActionBean = AdminPermissionAction.class)
  public Resolution generateCourierReport() {
    *//*try {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
      xlsFile = new File(adminDownloads + "/reports/courier-report-" + sdf.format(new Date()) + ".xls");
      xlsFile = reportGenerator.generateCourierReportXsl(xlsFile.getPath(), EnumLineItemStatus.HANDED_OVER_TO_COURIER, courierDao.listAll());
      addRedirectAlertMessage(new SimpleMessage("Courier report successfully generated."));
    } catch (Exception e) {
      e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
      addRedirectAlertMessage(new SimpleMessage("Courier report generation failed"));
    }*//*

    //TODO: # warehouse fix this.
    
    return new HTTPResponseResolution();
  }

  *//**
   * Custom resolution for HTTP response. The resolution will write the output file in response
   *//*

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
    return orderPage == null ? 0 : orderPage.getTotalPages();
  }

  public int getResultCount() {
    return orderPage == null ? 0 : orderPage.getTotalResults();
  }

  public Set<String> getParamSet() {
    return null;
  }

  public List<ShippingOrder> getOrderList() {
    return orderList;
  }

  public void setOrderList(List<ShippingOrder> orderList) {
    this.orderList = orderList;
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

  public Courier getCourier() {
    return courier;
  }

  public void setCourier(Courier courier) {
    this.courier = courier;
  }*/
}