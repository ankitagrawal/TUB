package web.action.admin.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import net.sourceforge.stripes.action.DefaultHandler;
import net.sourceforge.stripes.action.DontValidate;
import net.sourceforge.stripes.action.ForwardResolution;
import net.sourceforge.stripes.action.Resolution;
import net.sourceforge.stripes.action.SimpleMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.stripesstuff.plugin.security.Secure;

import web.action.error.AdminPermissionAction;

import com.akube.framework.dao.Page;
import com.akube.framework.stripes.action.BasePaginatedAction;
import com.hk.admin.pact.service.shippingOrder.AdminShippingOrderService;
import com.hk.constants.core.PermissionConstants;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.dao.catalog.category.CategoryDao;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.order.ShippingOrderStatus;
import com.hk.search.ShippingOrderSearchCriteria;
import com.hk.service.shippingOrder.ShippingOrderService;
import com.hk.service.shippingOrder.ShippingOrderStatusService;

@Secure(hasAnyPermissions = { PermissionConstants.VIEW_SHIPMENT_QUEUE }, authActionBean = AdminPermissionAction.class)
@Component
public class ChooseOrdersForPrintPickAction extends BasePaginatedAction {

    @SuppressWarnings("unused")
    private static Logger              logger             = LoggerFactory.getLogger(ChooseOrdersForPrintPickAction.class);

    @Autowired
    private ShippingOrderService       shippingOrderService;

    @Autowired
    private AdminShippingOrderService  adminShippingOrderService;

    @Autowired
    private ShippingOrderStatusService shippingOrderStatusService;

    CategoryDao                        categoryDao;

    Page                shippingOrdersPage;
    List<ShippingOrder>                shippingOrdersList = new ArrayList<ShippingOrder>();
    Category                           category;
    String                             gatewayOrderId;
    String                             baseGatewayOrderId;
    ShippingOrderStatus                shippingOrderStatus;

    private static final int           PRINTING           = 1;
    private static final int           PICKING            = 2;

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
    @Secure(hasAnyPermissions = { PermissionConstants.VIEW_PACKING_QUEUE }, authActionBean = AdminPermissionAction.class)
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
        shippingOrderSearchCriteria.setShippingOrderStatusList(getShippingOrderStatusService().getOrderStatuses(shippingOrderStatuses));

        if (baseGatewayOrderId != null) {
            shippingOrderSearchCriteria.setBaseGatewayOrderId(baseGatewayOrderId);
        } else if (gatewayOrderId != null) {
            shippingOrderSearchCriteria.setGatewayOrderId(gatewayOrderId);
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
        shippingOrderSearchCriteria.setOrderAsc(true);
        shippingOrdersPage = shippingOrderService.searchShippingOrders(shippingOrderSearchCriteria, 1, 10);
        shippingOrdersList = shippingOrdersPage.getList();
        return shippingOrdersList;
    }

    public Resolution batchPrintOrders() {

        List<ShippingOrder> existingOrdersInPrintingQueue = getShippingOrdersForPrintingInCategory();

        int counter = 0;
        for (ShippingOrder shippingOrder : existingOrdersInPrintingQueue) {
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

}
