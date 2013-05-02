package com.hk.web.action.report;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hk.dto.ProductOptionDto;
import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.RedirectResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;
import net.sourceforge.stripes.validation.Validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import com.akube.framework.stripes.action.BaseAction;
import com.akube.framework.util.DateUtils;
import com.hk.cache.CategoryCache;
import com.hk.constants.catalog.category.CategoryConstants;
import com.hk.constants.core.Keys;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.ProductOption;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.core.PaymentMode;
import com.hk.domain.core.Tax;
import com.hk.domain.courier.Courier;
import com.hk.domain.inventory.GrnLineItem;
import com.hk.domain.inventory.po.PurchaseOrder;
import com.hk.domain.inventory.rv.ReconciliationStatus;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderStatus;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.warehouse.Warehouse;
import com.hk.impl.dao.ReconciliationStatusDaoImpl;
import com.hk.manager.EmailManager;
import com.hk.manager.OrderManager;
import com.hk.pact.dao.catalog.product.ProductVariantDao;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.dao.payment.PaymentModeDao;
import com.hk.pact.service.OrderStatusService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.report.dto.catalog.CategoryPerformanceDto;
import com.hk.report.dto.inventory.ExpiryAlertReportDto;
import com.hk.report.dto.inventory.InventorySoldDto;
import com.hk.report.dto.inventory.RTOReportDto;
import com.hk.report.dto.inventory.RVReportDto;
import com.hk.report.dto.inventory.StockReportDto;
import com.hk.report.dto.order.CategoriesOrderReportDto;
import com.hk.report.dto.order.OrderLifecycleStateTransitionDto;
import com.hk.report.dto.order.ShipmentDto;
import com.hk.report.dto.payment.CODConfirmationDto;
import com.hk.report.dto.sales.CategorySalesDto;
import com.hk.report.dto.sales.DaySaleDto;
import com.hk.report.dto.sales.DaySaleShipDateWiseDto;
import com.hk.report.manager.ReportManager;
import com.hk.report.pact.service.catalog.product.ReportProductVariantService;
import com.hk.report.pact.service.order.ReportOrderService;
import com.hk.report.pact.service.shippingOrder.ReportShippingOrderService;
import com.hk.util.CustomDateTypeConvertor;
import com.hk.util.io.HkXlsWriter;
import com.hk.web.action.error.AdminPermissionAction;

@Secure(hasAnyPermissions = { PermissionConstants.REPORT_ADMIN }, authActionBean = AdminPermissionAction.class)
@Component
public class ReportAction extends BaseAction {

    private Date                                   startDate;
    private Date                                   endDate;
    private Date                                   activityDate;
    private PaymentMode                            paymentMode;
    private Courier                                codMode;
    private ReconciliationStatus                   reconciliationStatus;
    float                                          avgDeliveryTime                      = 0F;
    float                                          avgShipmentTime                      = 0F;
    private int                                    daysBtwMonthStartDateCurrentDate;
    private List<Tax>                              taxes                                = new ArrayList<Tax>();
    private List<OrderStatus>                      orderStatuses                        = new ArrayList<OrderStatus>();
    private String                                 inRegion;
    private String[]                               productIdArray;
    private String                                 productIdListCommaSeparated;
    // private InventorySoldDto inventorySold;
    private OrderStatus                            orderStatus;
    private Warehouse                              warehouse;
    private ShippingOrderStatus                    shippingOrderStatus;

    @Value("#{hkEnvProps['" + Keys.Env.adminDownloads + "']}")
    String                                         adminDownloads;
    File                                           xlsFile;
    @Autowired
    ReportShippingOrderService                     shippingOrderReportingService;
    @Autowired
    ReportOrderService                             reportOrderService;
    @Autowired
    private ReportProductVariantService            reportProductVariantService;
    @Autowired
    ReportManager                                  reportGenerator;
    @Autowired
    OrderDao                                       orderDao;
    @Autowired
    PaymentModeDao                                 paymentModeDao;
    @Autowired
    ReconciliationStatusDaoImpl                    reconciliationStatusDao;
    /*
     * @Autowired private CategoryService categoryService;
     */
    @Autowired
    OrderManager                                   orderManager;
    // @Autowired
    EmailManager                                   emailManager;
    @Autowired
    ProductVariantDao                              productVariantDao;
    @Autowired
    OrderService                                   orderService;
    @Autowired
    OrderStatusService                             orderStatusService;
    @Autowired
    ShippingOrderService                           shippingOrderService;

    private List<CategorySalesDto>                 categorySalesDtoList                 = new ArrayList<CategorySalesDto>();

    private List<DaySaleDto>                       daySaleList                          = new ArrayList<DaySaleDto>();
    private List<DaySaleShipDateWiseDto>           daySaleShipDateWiseDtoList           = new ArrayList<DaySaleShipDateWiseDto>();

    private List<ShipmentDto>                      shipmentDtoList                      = new ArrayList<ShipmentDto>();

    private List<OrderLifecycleStateTransitionDto> orderLifecycleStateTransitionDtoList = new ArrayList<OrderLifecycleStateTransitionDto>();

    private List<Order>                            orderList                            = new ArrayList<Order>();

    private List<CategoryPerformanceDto>           categoryPerformanceList              = new ArrayList<CategoryPerformanceDto>();

    private List<String>                           categoryList                         = new ArrayList<String>();

    private List<CODConfirmationDto>               CODUnConfirmedOrderList              = new ArrayList<CODConfirmationDto>();

    private List<CODConfirmationDto>               CODConfirmedOrderList                = new ArrayList<CODConfirmationDto>();

    private List<InventorySoldDto>                 inventorySoldList                    = new ArrayList<InventorySoldDto>();

    private Category                               topLevelCategory;

    private Courier                                courier;

    private Double                                 avgTxn                               = 0.0;
    private Double                                 avgTnxCOD                            = 0.0;
    private Double                                 avgTxnOfferOrder                     = 0.0;
    private Double                                 avgSku                               = 0.0;
    private Double                                 avgMrp                               = 0.0;
    private Double                                 avgHkp                               = 0.0;
    private Double                                 avgHkpPostDiscount                   = 0.0;

    private Double                                 avgSameDayShipping                   = 0.0;
    private Double                                 avgNextDayShipping                   = 0.0;
    private Double                                 avgShippingOnDayTwo                  = 0.0;

    private Double                                 totalAmount                          = 0.0;
    private Integer                                totalOrders                          = 0;
    private Integer                                totalDistinctOrders                  = 0;
    private Integer                                sumOfMrp                             = 0;
    private Integer                                sumOfHkPrice                         = 0;
    private Long                                   total                                = 0L;

    private Map<String, CategoriesOrderReportDto>  categoriesOrderReportDtosMap         = new HashMap<String, CategoriesOrderReportDto>();
    private Map<String, CategoriesOrderReportDto>  sixHourlyCategoriesOrderReportMap    = new HashMap<String, CategoriesOrderReportDto>();

    private Map<String, Long>                      targetMrpSalesMap;
    private Map<String, Long>                      targetOrderCountMap;
    private Map<String, Long>                      targetDailyMrpSalesMap               = new HashMap<String, Long>();

    @DefaultHandler
    @DontValidate
    public Resolution pre() {
        List<OrderStatus> orderStatusList = orderStatusService.getOrderStatuses(EnumOrderStatus.getStatusForReporting());
        daySaleList = reportOrderService.findSaleForTimeFrame(orderStatusList, DateUtils.getStartOfDay(new Date()), DateUtils.getEndOfDay(new Date()));
        for (DaySaleDto daySaleDto : daySaleList) {
            totalOrders += daySaleDto.getTxnCount().intValue();
            sumOfMrp += daySaleDto.getSumOfMrp().intValue();
            sumOfHkPrice += daySaleDto.getSumOfHkPrice().intValue();
        }
        return new ForwardResolution("/pages/admin/report.jsp");
    }

    @Secure(hasAnyPermissions = { PermissionConstants.SKU_SALES_REPORT }, authActionBean = AdminPermissionAction.class)
    public Resolution generateOrderReportExcel() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            xlsFile = new File(adminDownloads + "/reports/order-report" + sdf.format(new Date()) + ".xls");
            xlsFile = reportGenerator.generateOrderReportXsl(xlsFile.getPath(), startDate, endDate, orderStatus, topLevelCategory);
            addRedirectAlertMessage(new SimpleMessage("Order report successfully generated."));
        } catch (Exception e) {
            e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
            addRedirectAlertMessage(new SimpleMessage("Order report generation failed"));
        }
        return new HTTPResponseResolution();
    }

    @Secure(hasAnyPermissions = { PermissionConstants.SKU_SALES_REPORT }, authActionBean = AdminPermissionAction.class)
    public Resolution generateSKUSalesExcel() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            xlsFile = new File(adminDownloads + "/reports/inventory-sales-report-" + sdf.format(new Date()) + ".xls");
            xlsFile = reportGenerator.generateSKUSalesReportXsl(xlsFile.getPath(), startDate, endDate, orderStatus, topLevelCategory);
            addRedirectAlertMessage(new SimpleMessage("SKU Sales report successfully generated."));
        } catch (Exception e) {
            e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
            addRedirectAlertMessage(new SimpleMessage("SKU Sales report generation failed"));
        }
        return new HTTPResponseResolution();
    }

    @Secure(hasAnyPermissions = { PermissionConstants.ACCOUNTING_SALES_REPORT }, authActionBean = AdminPermissionAction.class)
    public Resolution generateAccountingSalesExcel() {
        /*
         * try { SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); xlsFile = new File(adminDownloads +
         * "/reports/accounting-sales-report-" + sdf.format(new Date()) + ".xls"); xlsFile =
         * reportGenerator.generateSalesReportXsl(xlsFile.getPath(), startDate, endDate); addRedirectAlertMessage(new
         * SimpleMessage("Sales report successfully generated.")); } catch (Exception e) { e.printStackTrace(); //To
         * change body of catch statement use File | Settings | File Templates. addRedirectAlertMessage(new
         * SimpleMessage("Sales report generation failed")); }
         */
        return new HTTPResponseResolution();
    }

    @Secure(hasAnyPermissions = { PermissionConstants.ACCOUNTING_SALES_REPORT }, authActionBean = AdminPermissionAction.class)
    public Resolution generateAccountingSalesExcelForBusy() {
        /*
         * try { SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd"); xlsFile = new File(adminDownloads +
         * "/reports/sales-data-busy-" + sdf.format(new Date()) + ".xls"); xlsFile =
         * reportGenerator.generateSalesReportXslForBusy(xlsFile.getPath(), startDate, endDate);
         * addRedirectAlertMessage(new SimpleMessage("Sales report successfully generated.")); } catch (Exception e) {
         * e.printStackTrace(); //To change body of catch statement use File | Settings | File Templates.
         * addRedirectAlertMessage(new SimpleMessage("Sales report generation failed")); }
         */
        return new HTTPResponseResolution();
    }

    @Secure(hasAnyPermissions = { PermissionConstants.SALES_REPORT }, authActionBean = AdminPermissionAction.class)
    public Resolution generateSalesByDateReport() {
        List<OrderStatus> orderStatusList = orderStatusService.getOrderStatuses(EnumOrderStatus.getStatusForReporting());
        daySaleList = reportOrderService.findSaleForTimeFrame(orderStatusList, startDate, endDate);
        for (DaySaleDto daySaleDto : daySaleList) {
            /*
             * Double orderLevelCouponAndRewardPoints =
             * orderDao.getNetDiscountViaOrderLevelCouponAndRewardPoints(daySaleDto.getOrderDate()); if
             * (orderLevelCouponAndRewardPoints != null) {
             * daySaleDto.setSumOfHkPricePostAllDiscounts(daySaleDto.getSumOfHkPricePostAllDiscounts() +
             * orderLevelCouponAndRewardPoints); }
             */
            /**
             * Commented COD/Offer/First-timer data as query were killing the report Need Optimisation here - may be
             * computed values need to kept somewhere. now SSD is there, then y fear --> but ssd ne bhi haar maan li
             * Long codOrderCount = 0L; codOrderCount = orderDao.getOrderCount(daySaleDto.getOrderDate(),
             * paymentModeDao.find(EnumPaymentMode.COD.getId())); daySaleDto.setCodTxnCount(codOrderCount); Long
             * offerInstanceOrderCount = 0L; offerInstanceOrderCount =
             * orderDao.getOrderCountByOfferInstance(daySaleDto.getOrderDate());
             * daySaleDto.setOfferTxnCount(offerInstanceOrderCount); Long getFirstTimeTxnUsers = 0L;
             * getFirstTimeTxnUsers = orderDao.getFirstTimeTxnUsers(daySaleDto.getOrderDate());
             * daySaleDto.setFirstTxnCount(getFirstTimeTxnUsers);
             */

            avgTxn += daySaleDto.getTxnCount();
            // avgTnxCOD += codOrderCount;
            // avgTxnOfferOrder += offerInstanceOrderCount;
            avgSku += daySaleDto.getSkuCount();
            avgMrp += daySaleDto.getSumOfMrp();
            avgHkp += daySaleDto.getSumOfHkPrice();
            avgHkpPostDiscount += daySaleDto.getSumOfHkPricePostAllDiscounts();
        }
        avgTxn = avgTxn / daySaleList.size();
        // avgTnxCOD = avgTnxCOD / daySaleList.size();
        // avgTxnOfferOrder = avgTxnOfferOrder / daySaleList.size();
        avgSku = avgSku / daySaleList.size();
        avgMrp = avgMrp / daySaleList.size();
        avgHkp = avgHkp / daySaleList.size();
        avgHkpPostDiscount = avgHkpPostDiscount / daySaleList.size();

        return new ForwardResolution("/pages/admin/salesByDateReport.jsp");
    }

    @Secure(hasAnyPermissions = { PermissionConstants.SALES_REPORT }, authActionBean = AdminPermissionAction.class)
    public Resolution generateDetailedSalesDailyReport() {
        // TODO: #warehouse refactor this

        /*
         * daySaleList = orderDao.findBasicCountSaleForTimeFrame(DateUtils.getStartOfPreviousDay(new Date()),
         * DateUtils.getEndOfDay(new Date())); for (DaySaleDto daySaleDto : daySaleList) { Long codOrderCount = 0L;
         * codOrderCount = orderDao.getOrderCount(daySaleDto.getOrderDate(),
         * paymentModeDao.find(EnumPaymentMode.COD.getId())); daySaleDto.setCodTxnCount(codOrderCount); Long
         * offerInstanceOrderCount = 0L; offerInstanceOrderCount =
         * orderDao.getOrderCountByOfferInstance(daySaleDto.getOrderDate());
         * daySaleDto.setOfferTxnCount(offerInstanceOrderCount); Long getFirstTimeTxnUsers = 0L; getFirstTimeTxnUsers =
         * orderDao.getFirstTimeTxnUsers(daySaleDto.getOrderDate()); daySaleDto.setFirstTxnCount(getFirstTimeTxnUsers); }
         */

        return new ForwardResolution("/pages/admin/detailedSalesDailyReport.jsp");
    }

    @Secure(hasAnyPermissions = { PermissionConstants.SALES_REPORT }, authActionBean = AdminPermissionAction.class)
    public Resolution generateSalesByDateReportForShippedProducts() {

        daySaleShipDateWiseDtoList = reportGenerator.generateSalesByDateReportForShippedProducts(startDate, endDate);

        return new ForwardResolution("/pages/admin/salesByDateReportForShippedItems.jsp");
    }

    @Secure(hasAnyPermissions = { PermissionConstants.SALES_REPORT }, authActionBean = AdminPermissionAction.class)
    public Resolution generateSalesByDateExcel() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            xlsFile = new File(adminDownloads + "/reports/sales-by-date-report-" + sdf.format(new Date()) + ".xls");
            xlsFile = reportGenerator.generateSalesByDateReportXls(xlsFile.getPath(), startDate, endDate);
            addRedirectAlertMessage(new SimpleMessage("Sales-by-date report successfully generated."));
        } catch (Exception e) {
            e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
            addRedirectAlertMessage(new SimpleMessage("Sales-by-date report generation failed"));
        }
        return new HTTPResponseResolution();
    }

    @Secure(hasAnyPermissions = { PermissionConstants.SALES_REPORT }, authActionBean = AdminPermissionAction.class)
    public Resolution generateSalesByDateForShippedProductsExcel() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            xlsFile = new File(adminDownloads + "/reports/sales-by-date-for-shipped-products-report-" + sdf.format(new Date()) + ".xls");
            xlsFile = reportGenerator.generateSalesByDateReportForShippedProductsXls(xlsFile.getPath(), startDate, endDate);
            addRedirectAlertMessage(new SimpleMessage("Sales-by-date report successfully generated."));
        } catch (Exception e) {
            e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
            addRedirectAlertMessage(new SimpleMessage("Sales-by-date report generation failed"));
        }
        return new HTTPResponseResolution();
    }

    @Secure(hasAnyPermissions = { PermissionConstants.MASTER_PERFORMANCE_REPORTS }, authActionBean = AdminPermissionAction.class)
    public Resolution generateMasterPerformanceReport() {
        // TODO: #warehouse refactor this
        orderLifecycleStateTransitionDtoList = shippingOrderReportingService.getOrderLifecycleStateTransitionDtoList(startDate, endDate);
        return new ForwardResolution("/pages/admin/masterPerformanceReport.jsp");
    }

    @Secure(hasAnyPermissions = { PermissionConstants.COD_PERFORMANCE_REPORTS }, authActionBean = AdminPermissionAction.class)
    public Resolution getUnescalatedOrders() {

        // TODO: #warehouse refactor this

        /*
         * List<Long> orderIds = orderDao.getActivityPerformedOrderIds(activityDate,
         * EnumOrderLifecycleActivity.OrderPlaced.getId(), -1, 17, 0, 17); Calendar calendar = Calendar.getInstance();
         * calendar.setTime(activityDate); if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
         * calendar.add(Calendar.HOUR, -24); List<Long> sundayOrderIds =
         * orderDao.getActivityPerformedOrderIds(calendar.getTime(), EnumOrderLifecycleActivity.OrderPlaced.getId(), -1,
         * 17, 0, 17); if (sundayOrderIds != null && sundayOrderIds.size() > 0) { orderIds.addAll(sundayOrderIds); } }
         * for (Long orderId : orderIds) { Order order = orderDao.find(orderId); if
         * (order.getOrderStatus().getId().equals(EnumOrderStatus.Pending.getId())) { if
         * (order.getPayment().getPaymentStatus().getId().equals(EnumPaymentStatus.SUCCESS.getId()) ||
         * order.getPayment().getPaymentStatus().getId().equals(EnumPaymentStatus.ON_DELIVERY.getId())) { List<LineItem>
         * productLineItems = order.getProductLineItems(); try { if (productLineItems.size() > 0 &&
         * productLineItems.get(0).getLineItemStatus().getId().equals(EnumLineItemStatus.ACTION_AWAITING.getId())) {
         * Category basketCategory =
         * productCatalogService.getTopLevelCategory(productLineItems.get(0).getProductVariant().getProduct());
         * order.setBasketCategory(basketCategory.getDisplayName()); orderList.add(order); } } catch (Exception e) {
         * //TODO } } } }
         */

        // this is a old comment not done while refactoring
        /*
         * Set<Order> tmpOrderList = orderDao.getOrderLyingIdleInActionQueue(orderIds, activityDate, 17); for (Order
         * order : tmpOrderList) { try { Category basketCategory =
         * productCatalogService.getTopLevelCategory(order.getProductLineItems().get(0).getProductVariantId().getProduct());
         * order.setBasketCategory(basketCategory.getDisplayName()); orderList.add(order); } catch (Exception e) {
         * //TODO } }
         */
        return new ForwardResolution("/pages/admin/unescalatedOrders.jsp");
    }

    @Secure(hasAnyPermissions = { PermissionConstants.COD_PERFORMANCE_REPORTS }, authActionBean = AdminPermissionAction.class)
    public Resolution generateCODConfirmationReport() {
        CODUnConfirmedOrderList = reportGenerator.generateUnConfirmedOrderList(startDate, endDate);
        CODConfirmedOrderList = reportGenerator.generateConfirmedOrderList(startDate, endDate);
        return new ForwardResolution("/pages/admin/codConfirmationPerformaceReport.jsp");
    }

    @Secure(hasAnyPermissions = { PermissionConstants.SALES_REPORT }, authActionBean = AdminPermissionAction.class)
    public Resolution generateSalesReport() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            xlsFile = new File(adminDownloads + "/reports/sales-report-" + sdf.format(new Date()) + ".xls");
            xlsFile = reportGenerator.generateSalesReportXsl(xlsFile.getPath(), startDate, endDate);
            addRedirectAlertMessage(new SimpleMessage("Sales report successfully generated."));
        } catch (Exception e) {
            e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
            addRedirectAlertMessage(new SimpleMessage("Sales report generation failed"));
        }
        return new HTTPResponseResolution();
    }

    @Secure(hasAnyPermissions = { PermissionConstants.CRM_REPORTS }, authActionBean = AdminPermissionAction.class)
    public Resolution generateCRMReport() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            xlsFile = new File(adminDownloads + "/reports/crm-report-" + sdf.format(new Date()) + ".xls");
            List<Category> categories = new ArrayList<Category>();
            if (topLevelCategory != null) {
                categories.add(topLevelCategory);
            } else if (categories.size() == 0) {

                // TODO: please change such queries does not make any sense do it this way
                /*
                 * categories.add(categoryService.getCategoryByName(CategoryConstants.HEALTH_DEVICES));
                 * categories.add(categoryService.getCategoryByName(CategoryConstants.DIABETES));
                 * categories.add(categoryService.getCategoryByName(CategoryConstants.BABY));
                 * categories.add(categoryService.getCategoryByName(CategoryConstants.NUTRITION));
                 * categories.add(categoryService.getCategoryByName(CategoryConstants.PERSONAL_CARE));
                 * categories.add(categoryService.getCategoryByName(CategoryConstants.EYE));
                 * categories.add(categoryService.getCategoryByName(CategoryConstants.BEAUTY));
                 */

                categories.add(CategoryCache.getInstance().getCategoryByName(CategoryConstants.HEALTH_DEVICES).getCategory());
                categories.add(CategoryCache.getInstance().getCategoryByName(CategoryConstants.DIABETES).getCategory());
                categories.add(CategoryCache.getInstance().getCategoryByName(CategoryConstants.BABY).getCategory());
                categories.add(CategoryCache.getInstance().getCategoryByName(CategoryConstants.NUTRITION).getCategory());
                categories.add(CategoryCache.getInstance().getCategoryByName(CategoryConstants.PERSONAL_CARE).getCategory());
                categories.add(CategoryCache.getInstance().getCategoryByName(CategoryConstants.EYE).getCategory());
                categories.add(CategoryCache.getInstance().getCategoryByName(CategoryConstants.BEAUTY).getCategory());
            }
            xlsFile = reportGenerator.generateCategorySalesReportXsl(xlsFile.getPath(), startDate, endDate, categories);
            addRedirectAlertMessage(new SimpleMessage("CRM report successfully generated."));
        } catch (Exception e) {
            e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
            addRedirectAlertMessage(new SimpleMessage("CRM report generation failed"));
        }
        return new HTTPResponseResolution();
    }

    @Secure(hasAnyPermissions = { PermissionConstants.CATEGORY_PERFORMANCE_REPORTS }, authActionBean = AdminPermissionAction.class)
    public Resolution generateCategoryPerformanceReportUI() {

        // TODO: # warehouse fix this

        /*
         * System.out.println("categoryList: " + categoryList); List<Category> applicableCategories = new ArrayList<Category>();
         * if (categoryList != null && categoryList.size() > 0) { for (String category : categoryList) { if (category !=
         * null && !category.equals("")) { Category cat = categoryDao.getCategoryByName(category); if (cat != null)
         * applicableCategories.add(cat); } } } if (applicableCategories.size() == 0) {
         * applicableCategories.addAll(categoryDao.getPrimaryCategories()); } Double orderLevelDiscounts = 0D; Set<Order>
         * distinctOrderBucket = new HashSet<Order>(); for (Category applicableCategory : applicableCategories) {
         * orderList = orderDao.findCategoryPerformanceParametersForTimeFrame(startDate, endDate, applicableCategory);
         * int distinctOrders = 0, mixedOrders = 0; for (Order order : orderList) { if (order.getProductLineItems() !=
         * null && order.getProductLineItems().size() > 0) { if (order.getProductLineItems().size() == 1 &&
         * !distinctOrderBucket.contains(order)) { distinctOrders++; totalDistinctOrders++;
         * distinctOrderBucket.add(order); } else if (order.getProductLineItems().size() > 1) { Category oldCategory =
         * null; boolean isMixOrder = false; for (LineItem lineItem : order.getProductLineItems()) { Category category =
         * productCatalogService.getTopLevelCategory(lineItem.getProductVariant().getProduct()); // on 25th oct data,
         * there is a particular product lineitem whose cat is null, have to check how, hence putting null check if
         * (category != null && oldCategory != null && !category.equals(oldCategory)) { mixedOrders++; isMixOrder =
         * true; break; } oldCategory = category; } if (!isMixOrder && !distinctOrderBucket.contains(order)) {
         * distinctOrders++; totalDistinctOrders++; distinctOrderBucket.add(order); } } } } CategoryPerformanceDto
         * categoryPerformanceDto = orderDao.findCategoryWiseSalesForTimeFrame(startDate, endDate, applicableCategory);
         * categoryPerformanceDto.setCategory(applicableCategory);
         * categoryPerformanceDto.setDistinctOrders(distinctOrders); categoryPerformanceDto.setMixedOrders(mixedOrders);
         * if (categoryPerformanceDto.getSumOfHkPricePostAllDiscounts() != null && orderLevelDiscounts != null) {
         * categoryPerformanceDto.setSumOfHkPricePostAllDiscounts(categoryPerformanceDto.getSumOfHkPrice() -
         * categoryPerformanceDto.getSumOfHkPricePostAllDiscounts() + orderLevelDiscounts); }
         * categoryPerformanceList.add(categoryPerformanceDto); } totalOrders =
         * orderDao.findOrdersForTimeFrame(startDate, endDate, applicableCategories).size();
         */
        return new ForwardResolution("/pages/admin/categoryPerformanceReport.jsp");
    }

    @Secure(hasAnyPermissions = { PermissionConstants.CATEGORY_PERFORMANCE_REPORTS }, authActionBean = AdminPermissionAction.class)
    public Resolution generateDailyCategoryPerformaceReportUI() {

        targetMrpSalesMap = CategoryConstants.targetMrpSalesMap;
        targetOrderCountMap = CategoryConstants.targetOrderCountMap;
        Date currentDate;
        Date yesterdayStartOfDayDate;
        Date sixHoursBeforeCurrentDate;
        Calendar currentDateCal = Calendar.getInstance();
        Calendar yesterdayEndOfDayCal = Calendar.getInstance();
        Calendar sixHoursBeforeCurrentDateCal = Calendar.getInstance();
        currentDate = currentDateCal.getTime();
        yesterdayStartOfDayDate = DateUtils.getStartOfPreviousDay(currentDate);
        yesterdayEndOfDayCal.setTime(yesterdayStartOfDayDate);
        sixHoursBeforeCurrentDate = DateUtils.getStartOfPreviousSixHours(currentDate);
        sixHoursBeforeCurrentDateCal.setTime(sixHoursBeforeCurrentDate);
        Integer numberOfDaysInMonth = yesterdayEndOfDayCal.getActualMaximum(Calendar.DAY_OF_MONTH);
        targetDailyMrpSalesMap.put(CategoryConstants.BABY, CategoryConstants.BABY_TARGET_SALES / numberOfDaysInMonth);
        targetDailyMrpSalesMap.put(CategoryConstants.BEAUTY, CategoryConstants.BEAUTY_TARGET_SALES / numberOfDaysInMonth);
        targetDailyMrpSalesMap.put(CategoryConstants.DIABETES, CategoryConstants.DIABETES_TARGET_SALES / numberOfDaysInMonth);
        targetDailyMrpSalesMap.put(CategoryConstants.EYE, CategoryConstants.EYE_TARGET_SALES / numberOfDaysInMonth);
        targetDailyMrpSalesMap.put(CategoryConstants.HEALTH_DEVICES, CategoryConstants.HEALTH_DEVICES_TARGET_SALES / numberOfDaysInMonth);
        targetDailyMrpSalesMap.put(CategoryConstants.NUTRITION, CategoryConstants.NUTRITION_TARGET_SALES / numberOfDaysInMonth);
        targetDailyMrpSalesMap.put(CategoryConstants.PERSONAL_CARE, CategoryConstants.PERSONAL_CARE_TARGET_SALES / numberOfDaysInMonth);
        targetDailyMrpSalesMap.put(CategoryConstants.SERVICES, CategoryConstants.SERVICES_TARGET_SALES / numberOfDaysInMonth);
        targetDailyMrpSalesMap.put(CategoryConstants.SPORTS, CategoryConstants.SPORTS_TARGET_SALES / numberOfDaysInMonth);

        categoriesOrderReportDtosMap = reportGenerator.generateDailyCategoryPerformaceReportUI();

        return new ForwardResolution("/pages/admin/categoryPerformanceDailyReport.jsp");
    }

    @Secure(hasAnyPermissions = { PermissionConstants.CATEGORY_PERFORMANCE_REPORTS }, authActionBean = AdminPermissionAction.class)
    public Resolution generateSixHourlyCategoryPerformaceReportUI() {

        sixHourlyCategoriesOrderReportMap = reportGenerator.generateSixHourlyCategoryPerformaceReportUI();
        return new ForwardResolution("/pages/admin/categoryPerformanceSixHourlyReport.jsp");
    }

    @Secure(hasAnyPermissions = { PermissionConstants.VIEW_RECONCILIATION_REPORTS }, authActionBean = AdminPermissionAction.class)
    public Resolution generateReconciliationReportUI() {
        // TODO: # warehouse fix this
        /*
         * orderList = orderDao.findReconciledOrdersForTimeFrame(startDate, endDate, orderStatus, paymentMode, codMode,
         * reconciliationStatus); Long ms = 86400000L; Long shipmentDiffinDays = 0L; float totalDeliveredOrders = 0;
         * Long deliveryDiffinDays = 0L; for (Order order : orderList) { if
         * (order.getProductLineItems().get(0).getDeliveryDate() != null) { totalDeliveredOrders++; deliveryDiffinDays +=
         * order.getProductLineItems().get(0).getDeliveryDate().getTime() -
         * order.getProductLineItems().get(0).getShipDate().getTime(); } totalAmount += order.getPayment().getAmount();
         * shipmentDiffinDays += order.getProductLineItems().get(0).getShipDate().getTime() -
         * order.getPayment().getPaymentDate().getTime(); } if (totalDeliveredOrders == 0F) { totalDeliveredOrders = 1; }
         * int orderSize = orderList.size(); if (orderSize == 0) { orderSize = 1; } avgDeliveryTime =
         * BaseUtils.roundTwoDecimals((deliveryDiffinDays / new Float(ms)) / new Float(totalDeliveredOrders));
         * avgShipmentTime = BaseUtils.roundTwoDecimals((shipmentDiffinDays / new Float(ms)) / new Float(orderSize));
         * System.out.println(avgShipmentTime + avgDeliveryTime + totalAmount);
         */
        return new ForwardResolution("/pages/admin/reconciliationReport.jsp");
    }

    @Secure(hasAnyPermissions = { PermissionConstants.UPDATE_RECONCILIATION_REPORTS }, authActionBean = AdminPermissionAction.class)
    public Resolution reconcile() {
        for (Order order : orderList) {
            orderDao.save(order);
        }
        addRedirectAlertMessage(new SimpleMessage("Orders have been Reconciled"));
        return new ForwardResolution("/pages/admin/reconciliationReport.jsp");
    }

    @Secure(hasAnyPermissions = { PermissionConstants.VIEW_RECONCILIATION_REPORTS }, authActionBean = AdminPermissionAction.class)
    public Resolution generateReconciliationReport() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            xlsFile = new File(adminDownloads + "/reports/Reconciliation-report-" + sdf.format(new Date()) + ".xls");
            xlsFile = reportGenerator.generateReconciliationReport(xlsFile.getPath(), startDate, endDate, orderStatus, paymentMode, codMode, reconciliationStatus);
            addRedirectAlertMessage(new SimpleMessage("Reconciliation report successfully generated."));
        } catch (Exception e) {
            e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
            addRedirectAlertMessage(new SimpleMessage("Reconciliation report generation failed"));
        }
        return new HTTPResponseResolution();
    }

    @Secure(hasAnyPermissions = { PermissionConstants.COD_PERFORMANCE_REPORTS }, authActionBean = AdminPermissionAction.class)
    public Resolution generateCODConfirmationReportXls() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            xlsFile = new File(adminDownloads + "/reports/COD-confirmation-report-" + sdf.format(new Date()) + ".xls");
            xlsFile = reportGenerator.generateCODConfirmationReportXls(xlsFile.getPath(), startDate, endDate);
            addRedirectAlertMessage(new SimpleMessage("Sales-by-date report successfully generated."));
        } catch (Exception e) {
            e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
            addRedirectAlertMessage(new SimpleMessage("Sales-by-date report generation failed"));
        }
        return new HTTPResponseResolution();
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

    @Secure(hasAnyPermissions = { PermissionConstants.COURIER_DELIVERY_REPORTS }, authActionBean = AdminPermissionAction.class)
    public Resolution generateCourierReport() {
        try {
            if (courier == null) {
                addRedirectAlertMessage(new SimpleMessage("Please select a courier first."));
                return new RedirectResolution(ReportAction.class);
            }
            if (startDate == null) {
                addRedirectAlertMessage(new SimpleMessage("Please select start date first."));
                return new RedirectResolution(ReportAction.class);
            }

            if (endDate == null) {
                addRedirectAlertMessage(new SimpleMessage("Please select end date first."));
                return new RedirectResolution(ReportAction.class);
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            xlsFile = new File(adminDownloads + "/reports/courier-report-" + courier.getName() + "-" + sdf.format(startDate) + "to" + sdf.format(endDate) + ".xls");
            xlsFile = reportGenerator.generateCourierReportXls(xlsFile.getPath(), startDate, endDate, courier);
            addRedirectAlertMessage(new SimpleMessage("Courier report successfully generated."));
        } catch (Exception e) {
            e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
            addRedirectAlertMessage(new SimpleMessage("Courier report generation failed"));
        }
        return new HTTPResponseResolution();
    }

    @Secure(hasAnyPermissions = { PermissionConstants.SALES_REPORT }, authActionBean = AdminPermissionAction.class)
    public Resolution generateSaleForProductsByTaxAndStatusInRegion() {

        // daySaleShipDateWiseDtoList = reportGenerator.generateSalesByDateReportForShippedProducts(startDate, endDate);

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            xlsFile = new File(adminDownloads + "/reports/sales-data-per-tax-category-" + sdf.format(new Date()) + ".xls");
            taxes.removeAll(Collections.singleton(null));
            orderStatuses.removeAll(Collections.singleton(null));
            xlsFile = reportGenerator.generateSaleForProductsByTaxAndStatusInRegion(taxes, orderStatuses, inRegion, xlsFile.getPath(), startDate, endDate);
            addRedirectAlertMessage(new SimpleMessage("Sales-by-date report successfully generated."));
        } catch (Exception e) {
            e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
            addRedirectAlertMessage(new SimpleMessage("Sales-by-date report generation failed"));
        }
        return new HTTPResponseResolution();

    }

    public Resolution generateNetMarginReportPerProductVariant() {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            xlsFile = new File(adminDownloads + "/reports/net-margin-per-product-variant" + sdf.format(new Date()) + ".xls");
            xlsFile = reportGenerator.generateNetMarginReportPerProductVariant(xlsFile.getPath(), startDate, endDate);
            addRedirectAlertMessage(new SimpleMessage("Net-margin-per-product-variant report successfully generated."));
        } catch (Exception e) {
            e.printStackTrace(); // To change body of catch statement use File | Settings | File Templates.
            addRedirectAlertMessage(new SimpleMessage("Net-margin-per-product-variant report generation failed"));
        }
        return new HTTPResponseResolution();

    }

    public Resolution generateInventorySoldByDate() {
        if (productIdListCommaSeparated != null) {
            productIdArray = productIdListCommaSeparated.split(",");
            for (String productId : productIdArray) {
                InventorySoldDto inventorySold = reportProductVariantService.findInventorySoldByDateAndProduct(startDate, endDate, productId);
                if (inventorySold.getProductId() == null) {
                    inventorySold.setProductId(productId);
                }
                total = total + inventorySold.getCountSold();
                inventorySoldList.add(inventorySold);
            }
            return new ForwardResolution("/pages/admin/inventorySoldReport.jsp");
        } else {
            inventorySoldList = reportProductVariantService.findInventorySoldByDate(startDate, endDate);
            for (InventorySoldDto inventorySold : inventorySoldList) {
                total = total + inventorySold.getCountSold();
            }
            return new ForwardResolution("/pages/admin/inventorySoldReport.jsp");
        }
    }

	//TODO: The report generated here is incorrect, as it should be generated from skuItem status instead of summing the quantity from PVI table.
    public Resolution generateStockReport() {
        StockReportDto stockReportDto = null;
        xlsFile = new File(adminDownloads + "/reports/StockReport.xls");
        HkXlsWriter xlsWriter = new HkXlsWriter();

        if (productIdListCommaSeparated != null) {
            int xlsRow = 1;
            xlsWriter.addHeader("PRODUCT VARIANT ID", "PRODUCT VARIANT ID");
            xlsWriter.addHeader("PRODUCT NAME", "PRODUCT NAME");
            xlsWriter.addHeader("PRODUCT OPTIONS", "PRODUCT OPTIONS");
            xlsWriter.addHeader("OPENING STOCK", "OPENING STOCK");
            xlsWriter.addHeader("STOCK LEFT", "STOCK LEFT");
            xlsWriter.addHeader("LINE ITEM CHECKOUT", "LINE ITEM CHECKOUT");
            xlsWriter.addHeader("RECONCILE CHECKOUT", "RECONCILE CHECKOUT");
            xlsWriter.addHeader("GRN CHECKIN", "GRN CHECKIN");
            xlsWriter.addHeader("RECONCILE CHECKIN", "RECONCILE CHECKIN");
            xlsWriter.addHeader("RTO CHECKIN", "RTO CHECKIN");
            xlsWriter.addHeader("MOVE BACK CHECKIN", "MOVE BACK CHECKIN");
            xlsWriter.addHeader("DAMAGED STOCK", "DAMAGED STOCK");
            xlsWriter.addHeader("INVENTORY CHECKOUT RESHIPPING", "INVENTORY CHECKOUT RESHIPPING");
            xlsWriter.addHeader("RV EXPIRED", "RV EXPIRED");
            xlsWriter.addHeader("RV LOST/PILFERAGE", "RV LOST/PILFERAGE");
            xlsWriter.addHeader("STOCK TRANSFER CHECKOUT", "STOCK TRANSFER CHECKOUT");
            xlsWriter.addHeader("STOCK TRANSFER CHECKIN", "STOCK TRANSFER CHECKIN");
            xlsWriter.addHeader("LOST DURING TRANSIT", "LOST DURING TRANSIT");
            // EnumInvTxnType.TRANSIT_LOST
            productIdArray = productIdListCommaSeparated.split(",");
            for (String productId : productIdArray) {
                stockReportDto = reportProductVariantService.getStockDetailsByProductVariant(productId, warehouse, startDate, endDate);
                if (stockReportDto != null) {
                    xlsWriter.addCell(xlsRow, stockReportDto.getProductVariant());
                    xlsWriter.addCell(xlsRow, stockReportDto.getProductName());
                    xlsWriter.addCell(xlsRow, stockReportDto.getProductOption());
                    xlsWriter.addCell(xlsRow, stockReportDto.getOpeningStock());
                    xlsWriter.addCell(xlsRow, stockReportDto.getStockLeft());
                    xlsWriter.addCell(xlsRow, stockReportDto.getLineItemCheckout());
                    xlsWriter.addCell(xlsRow, stockReportDto.getReconcileCheckout());
                    xlsWriter.addCell(xlsRow, stockReportDto.getGrnCheckin());
                    xlsWriter.addCell(xlsRow, stockReportDto.getReconcileCheckin());
                    xlsWriter.addCell(xlsRow, stockReportDto.getRtoCheckin());
                    xlsWriter.addCell(xlsRow, stockReportDto.getCancelCheckin());
                    xlsWriter.addCell(xlsRow, stockReportDto.getDamageCheckin());
                    xlsWriter.addCell(xlsRow, stockReportDto.getInventoryRepeatCheckout());
                    xlsWriter.addCell(xlsRow, stockReportDto.getRvExpired());
                    xlsWriter.addCell(xlsRow, stockReportDto.getRvLostPilferage());
                    xlsWriter.addCell(xlsRow, stockReportDto.getStockTransferCheckout());
                    xlsWriter.addCell(xlsRow, stockReportDto.getStockTransferCheckin());
                    xlsWriter.addCell(xlsRow, stockReportDto.getTransitLost());

                }
                xlsRow++;
            }
            xlsWriter.writeData(xlsFile, "Stock_Report");
            addRedirectAlertMessage(new SimpleMessage("Download complete"));

            return new HTTPResponseResolution();
        }
        addRedirectAlertMessage(new SimpleMessage("Product Variant not entered"));
        return new ForwardResolution("/pages/admin/inventorySoldReport.jsp");
    }

    public Resolution generateRTOReport() {
        List<RTOReportDto> rtoReportList = reportProductVariantService.getRTOProductsDetail(startDate, endDate, warehouse);
        xlsFile = new File(adminDownloads + "/reports/RTOReport.xls");
        HkXlsWriter xlsWriter = new HkXlsWriter();

        if (rtoReportList != null && rtoReportList.size() > 0) {
            int xlsRow = 1;
            xlsWriter.addHeader("RTO DATE", "RTO DATE");
            xlsWriter.addHeader("SHIPMENT ORDER NO.", "SHIPMENT ORDER NO.");
            xlsWriter.addHeader("PRODUCT VARIANT", "PRODUCT VARIANT");
            xlsWriter.addHeader("PRODUCT NAME", "PRODUCT NAME");
            xlsWriter.addHeader("PRODUCT OPTIONS", "PRODUCT OPTIONS");
            xlsWriter.addHeader("RTO CHECKIN QTY", "RTO CHECKIN QTY");
            xlsWriter.addHeader("RTO CHECKIN DAMAGE QTY", "RTO CHECKIN DAMAGE QTY");

            for (RTOReportDto rtoReportDto : rtoReportList) {
                xlsWriter.addCell(xlsRow, rtoReportDto.getRtoDate());
                xlsWriter.addCell(xlsRow, rtoReportDto.getShippingOrderNumber());
                xlsWriter.addCell(xlsRow, rtoReportDto.getProductVariantId());
                xlsWriter.addCell(xlsRow, rtoReportDto.getProductName());
                xlsWriter.addCell(xlsRow, rtoReportDto.getProductOptions());
                xlsWriter.addCell(xlsRow, rtoReportDto.getRtoCheckinQty());
                xlsWriter.addCell(xlsRow, rtoReportDto.getRtoDamageCheckinQty());
                xlsRow++;
            }
            xlsWriter.writeData(xlsFile, "RTO_Report");
            addRedirectAlertMessage(new SimpleMessage("Download complete"));

            return new HTTPResponseResolution();
        } else
            return new ForwardResolution("/pages/admin/report.jsp");
    }

    public Resolution generateExpiryAlertReport() {
        List<ExpiryAlertReportDto> expiryAlertReportDtoList = reportProductVariantService.getToBeExpiredProductDetails(startDate, endDate, warehouse);
        xlsFile = new File(adminDownloads + "/reports/ExpiryAlertReport.xls");
        HkXlsWriter xlsWriter = new HkXlsWriter();
        int xlsRow = 1;
        xlsWriter.addHeader("SKU BATCH", "SKU BATCH");
        xlsWriter.addHeader("PRODUCT VARIANT ID", "PRODUCT VARIANT ID");
        xlsWriter.addHeader("PRODUCT NAME", "PRODUCT NAME");
        xlsWriter.addHeader("PRODUCT OPTIONS", "PRODUCT OPTIONS");
        xlsWriter.addHeader("STOCK LEFT", "STOCK LEFT");

        for (ExpiryAlertReportDto expiryAlertReportDto : expiryAlertReportDtoList) {
            xlsWriter.addCell(xlsRow, expiryAlertReportDto.getBatchNumber());
            xlsWriter.addCell(xlsRow, expiryAlertReportDto.getProductVariantId());
            xlsWriter.addCell(xlsRow, expiryAlertReportDto.getProductName());
            xlsWriter.addCell(xlsRow, expiryAlertReportDto.getProductOption());
            xlsWriter.addCell(xlsRow, expiryAlertReportDto.getBatchQty());

            xlsRow++;
        }
        xlsWriter.writeData(xlsFile, "ExpiryAlert_Report");
        addRedirectAlertMessage(new SimpleMessage("Download complete"));

        return new HTTPResponseResolution();
    }

    public Resolution generateReconciliationVoucherReport() {
        if (productIdListCommaSeparated != null) {
            productIdArray = productIdListCommaSeparated.split(",");
            for (String productVariantId : productIdArray) {
                ProductVariant productVariant = productVariantDao.getVariantById(productVariantId);
                List<RVReportDto> rvReportDtoList = getReportProductVariantService().getReconciliationVoucherDetail(productVariantId, warehouse, startDate, endDate);

                xlsFile = new File(adminDownloads + "/reports/ReconciliationVoucherReport.xls");
                HkXlsWriter xlsWriter = new HkXlsWriter();
                int xlsRow = 1;
                xlsWriter.addHeader("PRODUCT VARIANT ID", "PRODUCT VARIANT ID");
                xlsWriter.addHeader("PRODUCT NAME", "PRODUCT NAME");
                xlsWriter.addHeader("PRODUCT OPTIONS", "PRODUCT OPTIONS");
                xlsWriter.addHeader("RECONCILIATION VOUCHER ID", "RECONCILIATION VOUCHER ID");
                xlsWriter.addHeader("INVENTORY TXN TYPE", "INVENTORY TXN TYPE");
                xlsWriter.addHeader("TXN DATE", "TXN DATE");
                xlsWriter.addHeader("QUANTITY", "QUANTITY");

                for (RVReportDto rvReportDto : rvReportDtoList) {
                    xlsWriter.addCell(xlsRow, productVariantId);
                    xlsWriter.addCell(xlsRow, productVariant.getProduct().getName());
                    xlsWriter.addCell(xlsRow, productVariant.getOptionsSlashSeparated());
                    xlsWriter.addCell(xlsRow, rvReportDto.getReconciliationVoucher().getId());
                    xlsWriter.addCell(xlsRow, rvReportDto.getProductVariantInventory().getInvTxnType().getName());
                    xlsWriter.addCell(xlsRow, rvReportDto.getProductVariantInventory().getTxnDate());
                    Long qty = rvReportDto.getQtyRV() < 0L ? rvReportDto.getQtyRV() * -1L : rvReportDto.getQtyRV();
                    xlsWriter.addCell(xlsRow, qty);

                    xlsRow++;
                }
                xlsWriter.writeData(xlsFile, "ReconciliationVoucher_Report");

            }
            addRedirectAlertMessage(new SimpleMessage("Download complete"));

            return new HTTPResponseResolution();
        }
        addRedirectAlertMessage(new SimpleMessage("Product Variant not entered"));
        return null;
    }

    private void prepareXlsForPurchaseOrder(List<GrnLineItem> grnLineItemList) {
        xlsFile = new File(adminDownloads + "/reports/POReportByVariant.xls");
        HkXlsWriter xlsWriter = new HkXlsWriter();
        int xlsRow = 1;
        xlsWriter.addHeader("PRODUCT VARIANT ID", "PRODUCT VARIANT ID");
        xlsWriter.addHeader("PRODUCT NAME", "PRODUCT NAME");
        xlsWriter.addHeader("PRODUCT OPTIONS", "PRODUCT OPTIONS");
        xlsWriter.addHeader("PO ID", "PO ID");
        xlsWriter.addHeader("PO CREATE DATE", "PO CREATE DATE");
        xlsWriter.addHeader("PO CREATED BY", "PO CREATED BY");
        xlsWriter.addHeader("PO APPROVER", "PO APPROVER");
        xlsWriter.addHeader("SUPPLIER", "SUPPLIER");
        xlsWriter.addHeader("SUPPLIER TIN", "SUPPLIER TIN");
        xlsWriter.addHeader("PO STATUS", "PO STATUS");
        xlsWriter.addHeader("GRN QTY", "GRN QTY");
        xlsWriter.addHeader("CHECKED IN QTY", "CHECKED IN QTY");
        xlsWriter.addHeader("GRN DATE", "GRN DATE");
        xlsWriter.addHeader("GRN STATUS", "GRN STATUS");
        xlsWriter.addHeader("MRP", "MRP");
        xlsWriter.addHeader("COST PRICE", "COST PRICE");
        xlsWriter.addHeader("MARGIN(CP vs MRP)", "MARGIN(CP vs MRP)");

        xlsWriter.writeData(xlsFile, "PurchaseOrder_Report");

        for (GrnLineItem grnLineItem : grnLineItemList) {
            PurchaseOrder purchaseOrder = grnLineItem.getGoodsReceivedNote().getPurchaseOrder();
            ProductVariant productVariant = grnLineItem.getSku().getProductVariant();

            xlsWriter.addCell(xlsRow, productVariant.getId());
            xlsWriter.addCell(xlsRow, productVariant.getProduct().getName());
            xlsWriter.addCell(xlsRow, productVariant.getOptionsCommaSeparated());
            xlsWriter.addCell(xlsRow, purchaseOrder.getId());
            xlsWriter.addCell(xlsRow, purchaseOrder.getCreateDate());
            xlsWriter.addCell(xlsRow, purchaseOrder.getCreatedBy().getName());
            if (purchaseOrder.getApprovedBy() != null) {
                xlsWriter.addCell(xlsRow, purchaseOrder.getApprovedBy().getName());
            } else {
                xlsWriter.addCell(xlsRow, purchaseOrder.getApprovedBy());
            }

            xlsWriter.addCell(xlsRow, purchaseOrder.getSupplier().getName());
            xlsWriter.addCell(xlsRow, purchaseOrder.getSupplier().getTinNumber());
            xlsWriter.addCell(xlsRow, purchaseOrder.getPurchaseOrderStatus().getName());
            xlsWriter.addCell(xlsRow, grnLineItem.getQty());
            xlsWriter.addCell(xlsRow, grnLineItem.getCheckedInQty());
            xlsWriter.addCell(xlsRow, grnLineItem.getGoodsReceivedNote().getGrnDate());
            xlsWriter.addCell(xlsRow, grnLineItem.getGoodsReceivedNote().getGrnStatus().getName());
            xlsWriter.addCell(xlsRow, productVariant.getMarkedPrice());
            xlsWriter.addCell(xlsRow, productVariant.getCostPrice());
            xlsWriter.addCell(xlsRow, (productVariant.getMarkedPrice() - productVariant.getCostPrice()) / productVariant.getCostPrice() * 100);

            xlsRow++;
        }
        xlsWriter.writeData(xlsFile, "PurchaseOrder_Report");

    }

    public Resolution generatePOReportByVariant() {
        if (productIdListCommaSeparated == null) {
            List<GrnLineItem> grnLineItemList = getReportProductVariantService().getGrnLineItemForPurchaseOrder(null, warehouse, startDate, endDate);
            prepareXlsForPurchaseOrder(grnLineItemList);
        } else {
            productIdArray = productIdListCommaSeparated.split(",");
            for (String productVariantId : productIdArray) {
                ProductVariant productVariant = productVariantDao.getVariantById(productVariantId);
                List<GrnLineItem> grnLineItemList = getReportProductVariantService().getGrnLineItemForPurchaseOrder(productVariant, warehouse, startDate, endDate);
                prepareXlsForPurchaseOrder(grnLineItemList);

            }
        }
        addRedirectAlertMessage(new SimpleMessage("Download complete"));

        return new HTTPResponseResolution();
    }

    public Resolution generateReportBySOStatus() {
        if (shippingOrderStatus == null) {
            addRedirectAlertMessage(new SimpleMessage("Download complete"));
            return new ForwardResolution("/pages/admin/report.jsp");
        }
        ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
        List<ShippingOrderStatus> shippingOrderStatusList = new ArrayList<ShippingOrderStatus>();
        shippingOrderStatusList.add(shippingOrderStatus);
        shippingOrderSearchCriteria.setShippingOrderStatusList(shippingOrderStatusList);
        List<ShippingOrder> shippingOrders = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria, true);
        prepareXlsForShippingOrder(shippingOrders);
        addRedirectAlertMessage(new SimpleMessage("Download complete"));
        return new HTTPResponseResolution();
    }

    private void prepareXlsForShippingOrder(List<ShippingOrder> shippingOrderList) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        xlsFile = new File(adminDownloads + "/reports/SOreportByStatus.xls");
        HkXlsWriter xlsWriter = new HkXlsWriter();
        int xlsRow = 1;
        xlsWriter.addHeader("SO GATEWAY ORDER ID", "SO GATEWAY ORDER ID");
        xlsWriter.addHeader("BASE ORDER NUMBER", "BASE ORDER NUMBER");
	      xlsWriter.addHeader("CATEGORY", "CATEGORY");
        xlsWriter.addHeader("PRODUCT NAME", "PRODUCT NAME");
        xlsWriter.addHeader("VARIANT ID", "VARIANT ID");
        xlsWriter.addHeader("BABY WEIGHT","BABY WEIGHT");
        xlsWriter.addHeader("COLOR","COLOR");
        xlsWriter.addHeader("FLAVOR","FLAVOR");
        xlsWriter.addHeader("FRAGRANCE","FRAGRANCE");
        xlsWriter.addHeader("NET WEIGHT","NET WEIGHT");
        xlsWriter.addHeader("OFFER","OFFER");
        xlsWriter.addHeader("QUANTITY","QUANTITY");
        xlsWriter.addHeader("SIZE","SIZE");
        xlsWriter.addHeader("TYPE","TYPE");
        xlsWriter.addHeader("WEIGHT","WEIGHT");
        xlsWriter.addHeader("QTY", "QTY");
        xlsWriter.addHeader("AMOUNT", "AMOUNT");
        xlsWriter.addHeader("ESCALATION DATE", "ESCALATION DATE");
        xlsWriter.writeData(xlsFile, "SO_StatusReport");

        for (ShippingOrder shippingOrder : shippingOrderList) {
            for (LineItem lineItem : shippingOrder.getLineItems()) {
                ProductVariant productVariant = lineItem.getSku().getProductVariant();
                Order order = shippingOrder.getBaseOrder();
                Map<String, String> productOptions = new HashMap<String, String>();
                for(ProductOption productOption : productVariant.getProductOptions()){
                  productOptions.put(productOption.getName(), productOption.getValue());
                }
                xlsWriter.addCell(xlsRow, shippingOrder.getGatewayOrderId());
                xlsWriter.addCell(xlsRow, order.getId());
	              xlsWriter.addCell(xlsRow, productVariant.getProduct().getPrimaryCategory().getName());
                xlsWriter.addCell(xlsRow, productVariant.getProduct().getName());
                xlsWriter.addCell(xlsRow, productVariant.getId());
                xlsWriter.addCell(xlsRow, productOptions.get("BABY WEIGHT"));
                xlsWriter.addCell(xlsRow, productOptions.get("COLOR"));
                xlsWriter.addCell(xlsRow, productOptions.get("FLAVOR"));
                xlsWriter.addCell(xlsRow, productOptions.get("FRAGRANCE"));
                xlsWriter.addCell(xlsRow, productOptions.get("NET WEIGHT"));
                xlsWriter.addCell(xlsRow, productOptions.get("OFFER"));
                xlsWriter.addCell(xlsRow, productOptions.get("QUANTITY"));
                xlsWriter.addCell(xlsRow, productOptions.get("SIZE"));
                xlsWriter.addCell(xlsRow, productOptions.get("TYPE"));
                xlsWriter.addCell(xlsRow, productOptions.get("WEIGHT"));
                xlsWriter.addCell(xlsRow, lineItem.getQty());
                xlsWriter.addCell(xlsRow, lineItem.getHkPrice());
                if (shippingOrder.getLastEscDate() != null) {
                    xlsWriter.addCell(xlsRow, sdf.format(shippingOrder.getLastEscDate()));
                }
                xlsRow++;
            }
        }
        xlsWriter.writeData(xlsFile, "ShippingOrder_Status_Report");
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

    public List<CategorySalesDto> getCategorySalesDtoList() {
        return categorySalesDtoList;
    }

    public void setCategorySalesDtoList(List<CategorySalesDto> categorySalesDtoList) {
        this.categorySalesDtoList = categorySalesDtoList;
    }

    public List<DaySaleDto> getDaySaleList() {
        return daySaleList;
    }

    public void setDaySaleList(List<DaySaleDto> daySaleList) {
        this.daySaleList = daySaleList;
    }

    public PaymentMode getPaymentMode() {
        return paymentMode;
    }

    public void setPaymentMode(PaymentMode paymentMode) {
        this.paymentMode = paymentMode;
    }

    public Courier getCodMode() {
        return codMode;
    }

    public void setCodMode(Courier codMode) {
        this.codMode = codMode;
    }

    public ReconciliationStatus getReconciliationStatus() {
        return reconciliationStatus;
    }

    public void setReconciliationStatus(ReconciliationStatus reconciliationStatus) {
        this.reconciliationStatus = reconciliationStatus;
    }

    public List<Order> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
    }

    public float getAvgDeliveryTime() {
        return avgDeliveryTime;
    }

    public void setAvgDeliveryTime(float avgDeliveryTime) {
        this.avgDeliveryTime = avgDeliveryTime;
    }

    public float getAvgShipmentTime() {
        return avgShipmentTime;
    }

    public void setAvgShipmentTime(float avgShipmentTime) {
        this.avgShipmentTime = avgShipmentTime;
    }

    public Double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public List<CategoryPerformanceDto> getCategoryPerformanceList() {
        return categoryPerformanceList;
    }

    public void setCategoryPerformanceList(List<CategoryPerformanceDto> categoryPerformanceList) {
        this.categoryPerformanceList = categoryPerformanceList;
    }

    public Integer getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Integer totalOrders) {
        this.totalOrders = totalOrders;
    }

    public Category getTopLevelCategory() {
        return topLevelCategory;
    }

    public void setTopLevelCategory(Category topLevelCategory) {
        this.topLevelCategory = topLevelCategory;
    }

    public Integer getTotalDistinctOrders() {
        return totalDistinctOrders;
    }

    public void setTotalDistinctOrders(Integer totalDistinctOrders) {
        this.totalDistinctOrders = totalDistinctOrders;
    }

    public List<String> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<String> categoryList) {
        this.categoryList = categoryList;
    }

    public Double getAvgTxn() {
        return avgTxn;
    }

    public void setAvgTxn(Double avgTxn) {
        this.avgTxn = avgTxn;
    }

    public Double getAvgSku() {
        return avgSku;
    }

    public void setAvgSku(Double avgSku) {
        this.avgSku = avgSku;
    }

    public Double getAvgMrp() {
        return avgMrp;
    }

    public void setAvgMrp(Double avgMrp) {
        this.avgMrp = avgMrp;
    }

    public Double getAvgHkp() {
        return avgHkp;
    }

    public void setAvgHkp(Double avgHkp) {
        this.avgHkp = avgHkp;
    }

    public Double getAvgHkpPostDiscount() {
        return avgHkpPostDiscount;
    }

    public void setAvgHkpPostDiscount(Double avgHkpPostDiscount) {
        this.avgHkpPostDiscount = avgHkpPostDiscount;
    }

    public Double getAvgSameDayShipping() {
        return avgSameDayShipping;
    }

    public void setAvgSameDayShipping(Double avgSameDayShipping) {
        this.avgSameDayShipping = avgSameDayShipping;
    }

    public Double getAvgNextDayShipping() {
        return avgNextDayShipping;
    }

    public void setAvgNextDayShipping(Double avgNextDayShipping) {
        this.avgNextDayShipping = avgNextDayShipping;
    }

    public Double getAvgShippingOnDayTwo() {
        return avgShippingOnDayTwo;
    }

    public void setAvgShippingOnDayTwo(Double avgShippingOnDayTwo) {
        this.avgShippingOnDayTwo = avgShippingOnDayTwo;
    }

    public Double getAvgTnxCOD() {
        return avgTnxCOD;
    }

    public void setAvgTnxCOD(Double avgTnxCOD) {
        this.avgTnxCOD = avgTnxCOD;
    }

    public List<CODConfirmationDto> getCODUnConfirmedOrderList() {
        return CODUnConfirmedOrderList;
    }

    public void setCODUnConfirmedOrderList(List<CODConfirmationDto> CODUnConfirmedOrderList) {
        this.CODUnConfirmedOrderList = CODUnConfirmedOrderList;
    }

    public List<CODConfirmationDto> getCODConfirmedOrderList() {
        return CODConfirmedOrderList;
    }

    public void setCODConfirmedOrderList(List<CODConfirmationDto> CODConfirmedOrderList) {
        this.CODConfirmedOrderList = CODConfirmedOrderList;
    }

    public List<OrderLifecycleStateTransitionDto> getOrderLifecycleStateTransitionDtoList() {
        return orderLifecycleStateTransitionDtoList;
    }

    public void setOrderLifecycleStateTransitionDtoList(List<OrderLifecycleStateTransitionDto> orderLifecycleStateTransitionDtoList) {
        this.orderLifecycleStateTransitionDtoList = orderLifecycleStateTransitionDtoList;
    }

    public List<ShipmentDto> getShipmentDtoList() {
        return shipmentDtoList;
    }

    public void setShipmentDtoList(List<ShipmentDto> shipmentDtoList) {
        this.shipmentDtoList = shipmentDtoList;
    }

    public Date getActivityDate() {
        return activityDate;
    }

    public void setActivityDate(Date activityDate) {
        this.activityDate = activityDate;
    }

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    public int getDaysBtwMonthStartDateCurrentDate() {
        return daysBtwMonthStartDateCurrentDate;
    }

    public void setDaysBtwMonthStartDateCurrentDate(int daysBtwMonthStartDateCurrentDate) {
        this.daysBtwMonthStartDateCurrentDate = daysBtwMonthStartDateCurrentDate;
        this.daysBtwMonthStartDateCurrentDate = daysBtwMonthStartDateCurrentDate;
    }

    public Map<String, CategoriesOrderReportDto> getCategoriesOrderReportDtosMap() {
        return categoriesOrderReportDtosMap;
    }

    public void setCategoriesOrderReportDtosMap(Map<String, CategoriesOrderReportDto> categoriesOrderReportDtosMap) {
        this.categoriesOrderReportDtosMap = categoriesOrderReportDtosMap;
    }

    public Map<String, Long> getTargetMrpSalesMap() {
        return targetMrpSalesMap;
    }

    public void setTargetMrpSalesMap(Map<String, Long> targetMrpSalesMap) {
        this.targetMrpSalesMap = targetMrpSalesMap;
    }

    public Map<String, Long> getTargetOrderCountMap() {
        return targetOrderCountMap;
    }

    public void setTargetOrderCountMap(Map<String, Long> targetOrderCountMap) {
        this.targetOrderCountMap = targetOrderCountMap;
    }

    public Integer getSumOfMrp() {
        return sumOfMrp;
    }

    public Integer getSumOfHkPrice() {
        return sumOfHkPrice;
    }

    public Map<String, Long> getTargetDailyMrpSalesMap() {
        return targetDailyMrpSalesMap;
    }

    public void setTargetDailyMrpSalesMap(Map<String, Long> targetDailyMrpSalesMap) {
        this.targetDailyMrpSalesMap = targetDailyMrpSalesMap;
    }

    public Double getAvgTxnOfferOrder() {
        return avgTxnOfferOrder;
    }

    public void setAvgTxnOfferOrder(Double avgTxnOfferOrder) {
        this.avgTxnOfferOrder = avgTxnOfferOrder;
    }

    public List<DaySaleShipDateWiseDto> getDaySaleShipDateWiseDtoList() {
        return daySaleShipDateWiseDtoList;
    }

    public void setDaySaleShipDateWiseDtoList(List<DaySaleShipDateWiseDto> daySaleShipDateWiseDtoList) {
        this.daySaleShipDateWiseDtoList = daySaleShipDateWiseDtoList;
    }

    public List<Tax> getTaxes() {
        return taxes;
    }

    public void setTaxes(List<Tax> taxes) {
        this.taxes = taxes;
    }

    public List<OrderStatus> getOrderStatuses() {
        return orderStatuses;
    }

    public void setOrderStatuses(List<OrderStatus> orderStatuses) {
        this.orderStatuses = orderStatuses;
    }

    public String getInRegion() {
        return inRegion;
    }

    public void setInRegion(String inRegion) {
        this.inRegion = inRegion;
    }

    public Map<String, CategoriesOrderReportDto> getSixHourlyCategoriesOrderReportMap() {
        return sixHourlyCategoriesOrderReportMap;
    }

    public void setSixHourlyCategoriesOrderReportMap(Map<String, CategoriesOrderReportDto> sixHourlyCategoriesOrderReportMap) {
        this.sixHourlyCategoriesOrderReportMap = sixHourlyCategoriesOrderReportMap;
    }

    public List<InventorySoldDto> getInventorySoldList() {
        return inventorySoldList;
    }

    public void setInventorySoldList(List<InventorySoldDto> inventorySoldList) {
        this.inventorySoldList = inventorySoldList;
    }

    public String[] getProductIdArray() {
        return productIdArray;
    }

    public void setProductIdArray(String[] productIdArray) {
        this.productIdArray = productIdArray;
    }

    public String getProductIdListCommaSeparated() {
        return productIdListCommaSeparated;
    }

    public void setProductIdListCommaSeparated(String productIdListCommaSeparated) {
        this.productIdListCommaSeparated = productIdListCommaSeparated;
    }

    public Long getTotal() {
        return total;
    }

    public void setTotal(Long total) {
        this.total = total;
    }

    public Warehouse getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Warehouse warehouse) {
        this.warehouse = warehouse;
    }

    public ReportProductVariantService getReportProductVariantService() {
        return reportProductVariantService;
    }

    public void setReportProductVariantService(ReportProductVariantService reportProductVariantService) {
        this.reportProductVariantService = reportProductVariantService;
    }

    public ShippingOrderStatus getShippingOrderStatus() {
        return shippingOrderStatus;
    }

    public void setShippingOrderStatus(ShippingOrderStatus shippingOrderStatus) {
        this.shippingOrderStatus = shippingOrderStatus;
    }
}
