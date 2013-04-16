package com.hk.web.action.admin.queue;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.dto.inventory.CycleCountDto;
import com.hk.admin.pact.service.inventory.CycleCountService;
import com.hk.admin.pact.service.shippingOrder.AdminShippingOrderService;
import com.hk.admin.util.CycleCountDtoUtil;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.search.ShippingOrderSearchCriteria;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.courier.Courier;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderStatus;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.warehouse.Warehouse;
import com.hk.pact.dao.catalog.category.CategoryDao;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.util.CustomDateTypeConvertor;
import com.hk.web.action.error.AdminPermissionAction;
import net.sourceforge.stripes.action.*;
import net.sourceforge.stripes.validation.Validate;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Secure(hasAnyPermissions = {PermissionConstants.VIEW_SHIPMENT_QUEUE}, authActionBean = AdminPermissionAction.class)
@Component
public class ChooseOrdersForPrintPickAction extends BasePaginatedAction {

    @SuppressWarnings("unused")
    private static Logger logger = LoggerFactory.getLogger(ChooseOrdersForPrintPickAction.class);

    @Autowired
    private ShippingOrderService shippingOrderService;

    @Autowired
    private AdminShippingOrderService adminShippingOrderService;

    @Autowired
    private ShippingOrderStatusService shippingOrderStatusService;

    @Autowired
    CycleCountService cycleCountService;


    @Autowired
    CategoryDao categoryDao;

    Page shippingOrdersPage;
    List<ShippingOrder> shippingOrdersList = new ArrayList<ShippingOrder>();
    Category category;
    String gatewayOrderId;
    String baseGatewayOrderId;
    ShippingOrderStatus shippingOrderStatus;
    private Courier courier;

    private static final int PRINTING = 1;
    private static final int PICKING = 2;
    private Date startDate;
    private Date endDate;
    private Date paymentStartDate;
    private Date paymentEndDate;

    private String brand;

    private String getActionMessage(int action) {
        switch (action) {
            case PRINTING:
                return "printing";
            case PICKING:
                return "picking";
        }
        return "";
    }

    private String getRedirectMessage(int action) {
        StringBuilder messageString = new StringBuilder();
        if (shippingOrdersList != null && shippingOrdersList.size() > 0) {
            messageString.append(Integer.toString(shippingOrdersList.size()));
            messageString.append(" Orders selected for " + getActionMessage(action));
        } else {
            messageString.append("No orders found for " + getActionMessage(action));
        }

        return messageString.toString();
    }

    @DontValidate
    @DefaultHandler
    @Secure(hasAnyPermissions = {PermissionConstants.VIEW_PACKING_QUEUE}, authActionBean = AdminPermissionAction.class)
    public Resolution pre() {
        return new ForwardResolution("/pages/admin/chooseOrdersForPrintingPicking.jsp");
    }

    public Resolution searchOrdersForPrinting() {
        List<ShippingOrder> existingOrdersInPickingQueue = getShippingOrdersForPicking();

        if (existingOrdersInPickingQueue != null && !existingOrdersInPickingQueue.isEmpty()) {
            shippingOrderStatus = getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_MarkedForPrinting);
            shippingOrdersList = existingOrdersInPickingQueue;
            addRedirectAlertMessage(new SimpleMessage(getRedirectMessage(PICKING)));
        } else {
            shippingOrderStatus = getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_ReadyForProcess);
            shippingOrdersList = getShippingOrdersForPrintingInCategory();
            addRedirectAlertMessage(new SimpleMessage(getRedirectMessage(PRINTING)));
        }

        return new ForwardResolution("/pages/admin/chooseOrdersForPrintingPicking.jsp");
    }

    private ShippingOrderSearchCriteria getShippingOrderSearchCriteria(List<EnumShippingOrderStatus> shippingOrderStatuses) {
        ShippingOrderSearchCriteria shippingOrderSearchCriteria = new ShippingOrderSearchCriteria();
        shippingOrderSearchCriteria.setSearchForPrinting(true);
        shippingOrderSearchCriteria.setServiceOrder(false);
        shippingOrderSearchCriteria.setShippingOrderStatusList(getShippingOrderStatusService().getOrderStatuses(shippingOrderStatuses));

        if (baseGatewayOrderId != null) {
            shippingOrderSearchCriteria.setBaseGatewayOrderId(baseGatewayOrderId);
        } else if (gatewayOrderId != null) {
            shippingOrderSearchCriteria.setGatewayOrderId(gatewayOrderId);
        } else if (startDate != null && endDate != null) {
            shippingOrderSearchCriteria.setLastEscStartDate(startDate);
            shippingOrderSearchCriteria.setLastEscEndDate(endDate);
        } else if (paymentStartDate != null && paymentEndDate != null) {
            shippingOrderSearchCriteria.setPaymentStartDate(paymentStartDate);
            shippingOrderSearchCriteria.setPaymentEndDate(paymentEndDate);
        } else if (courier != null) {
            List<Courier> couriers = new ArrayList<Courier>();
            couriers.add(courier);
            shippingOrderSearchCriteria.setCourierList(couriers);
        } else {
            shippingOrderSearchCriteria.setBasketCategory(category.getName()).setBaseGatewayOrderId(baseGatewayOrderId).setGatewayOrderId(gatewayOrderId);
        }

        return shippingOrderSearchCriteria;
    }

    /*
     * private void updateShippingOrdersForPrinting() { ShippingOrderSearchCriteria shippingOrderSearchCriteria =
     * getShippingOrderSearchCriteria(EnumShippingOrderStatus.getStatusForPrinting()); shippingOrdersPage =
     * shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria, 1, 10); //hard coding to get it working
     * shippingOrdersList = shippingOrdersPage.getList(); }
     */

    private List<ShippingOrder> getShippingOrdersForPicking() {
        ShippingOrderSearchCriteria shippingOrderSearchCriteria = getShippingOrderSearchCriteria(EnumShippingOrderStatus.getStatusForPicking());

        return getShippingOrderService().searchShippingOrders(shippingOrderSearchCriteria);
    }

    private List<ShippingOrder> getShippingOrdersForPrintingInCategory() {
        ShippingOrderSearchCriteria shippingOrderSearchCriteria = getShippingOrderSearchCriteria(EnumShippingOrderStatus.getStatusForPrinting());

        shippingOrdersPage = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria, 1, 60);
        // shippingOrdersList = shippingOrdersPage.getList();
        shippingOrdersList = filterShippingOrdersByBrand(shippingOrdersPage);
        return shippingOrdersList;
    }

    public Resolution batchPrintOrders() {

        List<ShippingOrder> ordersDisplayerForPrinting = getShippingOrdersList();

        int counter = 0;

        for (ShippingOrder shippingOrder : ordersDisplayerForPrinting) {
            if (EnumShippingOrderStatus.SO_ReadyForProcess.getId().equals(shippingOrder.getOrderStatus().getId())) {
                getAdminShippingOrderService().markShippingOrderAsPrinted(shippingOrder);
                counter++;
            }
            // if (counter == 10) break;
        }

        // addRedirectAlertMessage(new SimpleMessage("10 Orders for selected category sent for printing"));
        return searchOrdersForPrinting();
    }

    public Resolution sendOrdersBackToProcessingQueue() {

        for (ShippingOrder shippingOrder : shippingOrdersList) {
            if (EnumShippingOrderStatus.SO_ReadyForProcess.getId().equals(shippingOrder.getOrderStatus().getId())
                    || EnumShippingOrderStatus.SO_MarkedForPrinting.getId().equals(shippingOrder.getOrderStatus().getId())) {
                getAdminShippingOrderService().moveShippingOrderBackToPackingQueue(shippingOrder);
            }
        }
        /*
         * addRedirectAlertMessage(new SimpleMessage("Selected orders are sent back to processing queue")); return new
         * ForwardResolution("/pages/admin/chooseOrdersForPrintingPicking.jsp");
         */

        return searchOrdersForPrinting();

    }

    public Resolution clearPickingQueue() {
        List<ShippingOrder> existingOrdersInPickingQueue = getShippingOrdersForPicking();
        for (ShippingOrder shippingOrder : existingOrdersInPickingQueue) {
            if (EnumShippingOrderStatus.SO_MarkedForPrinting.getId().equals(shippingOrder.getOrderStatus().getId())) {
                getAdminShippingOrderService().moveShippingOrderToPickingQueue(shippingOrder);
            }
        }

        /*
         * addRedirectAlertMessage(new SimpleMessage("Selected orders are cleared and marked for picking")); return new
         * RedirectResolution(ChooseOrdersForPrintPickAction.class);
         */

        return searchOrdersForPrinting();
    }

    private List<ShippingOrder> filterShippingOrdersByBrand(Page shippingOrdersPage) {
        List<ShippingOrder> shippingOrdersTempList = shippingOrdersPage.getList();
        List<String> brandsToExcludeList = new ArrayList<String>();
        List<String> productsToExcludeList = new ArrayList<String>();
        List<String> variantsToExcludeList = new ArrayList<String>();
        if (getPrincipalUser() != null) {
            Warehouse warehouse = getPrincipalUser().getSelectedWarehouse();
            List<CycleCountDto> cycleCountDtoList = cycleCountService.inProgressCycleCounts(warehouse);
            brandsToExcludeList = CycleCountDtoUtil.getCycleCountInProgressForBrand(cycleCountDtoList);
            productsToExcludeList = CycleCountDtoUtil.getCycleCountInProgressForProduct(cycleCountDtoList);
            variantsToExcludeList = CycleCountDtoUtil.getCycleCountInProgressForVariant(cycleCountDtoList);
        }
        for (ShippingOrder shippingOrder : shippingOrdersTempList) {
            if (shippingOrdersList.size() == 10) {
                break;
            }
            boolean shouldAdd = true;
            for (LineItem lineItem : shippingOrder.getLineItems()) {
                String brandName = lineItem.getSku().getProductVariant().getProduct().getBrand();
                String productId = lineItem.getSku().getProductVariant().getProduct().getId();
                String variantId = lineItem.getSku().getProductVariant().getId();


                if (StringUtils.isNotBlank(brandName) && brandsToExcludeList.contains(brandName)) {
                    shouldAdd = false;
                    break;
                }

                if (StringUtils.isNotBlank(productId) && productsToExcludeList.contains(productId)) {
                    shouldAdd = false;
                    break;
                }

                if (StringUtils.isNotBlank(variantId) && variantsToExcludeList.contains(variantId)) {
                    shouldAdd = false;
                    break;
                }
            }
            if (shouldAdd) {
                shippingOrdersList.add(shippingOrder);
            }
        }
        return shippingOrdersList;
    }

    public int getPerPageDefault() {
        return 10;
    }

    public int getPageCount() {
        return shippingOrdersPage == null ? 0 : shippingOrdersPage.getTotalPages();
    }

    public int getResultCount() {
        return shippingOrdersPage == null ? 0 : shippingOrdersPage.getTotalResults();
    }

    public Set<String> getParamSet() {
        return null;
    }

    /*
     * public Set<ShippingOrder> getPrintingPickingQueueOrders() { return printingPickingQueueOrders; } public void
     * setPrintingPickingQueueOrders(Set<ShippingOrder> printingPickingQueueOrders) { this.printingPickingQueueOrders =
     * printingPickingQueueOrders; }
     */

    public Page getShippingOrdersPage() {
        return shippingOrdersPage;
    }

    public void setShippingOrdersPage(Page shippingOrdersPage) {
        this.shippingOrdersPage = shippingOrdersPage;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
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

    public List<ShippingOrder> getShippingOrdersList() {
        return shippingOrdersList;
    }

    public void setShippingOrdersList(List<ShippingOrder> shippingOrdersList) {
        this.shippingOrdersList = shippingOrdersList;
    }

    public ShippingOrderStatus getShippingOrderStatus() {
        return shippingOrderStatus;
    }

    public void setShippingOrderStatus(ShippingOrderStatus shippingOrderStatus) {
        this.shippingOrderStatus = shippingOrderStatus;
    }

    public void setShippingOrderService(ShippingOrderService shippingOrderService) {
        this.shippingOrderService = shippingOrderService;
    }

    public void setShippingOrderStatusService(ShippingOrderStatusService shippingOrderStatusService) {
        this.shippingOrderStatusService = shippingOrderStatusService;
    }

    public AdminShippingOrderService getAdminShippingOrderService() {
        return adminShippingOrderService;
    }

    public void setAdminShippingOrderService(AdminShippingOrderService adminShippingOrderService) {
        this.adminShippingOrderService = adminShippingOrderService;
    }

    public ShippingOrderService getShippingOrderService() {
        return shippingOrderService;
    }

    public ShippingOrderStatusService getShippingOrderStatusService() {
        return shippingOrderStatusService;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
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

    public Courier getCourier() {
        return courier;
    }

    public void setCourier(Courier courier) {
        this.courier = courier;
    }

    public Date getPaymentStartDate() {
        return paymentStartDate;
    }

    @Validate(converter = CustomDateTypeConvertor.class)
    public void setPaymentStartDate(Date paymentStartDate) {
        this.paymentStartDate = paymentStartDate;
    }

    public Date getPaymentEndDate() {
        return paymentEndDate;
    }

    @Validate(converter = CustomDateTypeConvertor.class)
    public void setPaymentEndDate(Date paymentEndDate) {
        this.paymentEndDate = paymentEndDate;
    }
}
