package com.hk.impl.service.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.dao.Page;
import com.hk.comparator.BasketCategory;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumPaymentMode;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.core.search.OrderSearchCriteria;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.OrderLifecycleActivity;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.OrderCategory;
import com.hk.domain.order.OrderLifecycle;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.user.User;
import com.hk.domain.warehouse.Warehouse;
import com.hk.exception.OrderSplitException;
import com.hk.helper.LineItemHelper;
import com.hk.helper.ShippingOrderHelper;
import com.hk.manager.EmailManager;
import com.hk.manager.ReferrerProgramManager;
import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.service.OrderStatusService;
import com.hk.pact.service.UserService;
import com.hk.pact.service.catalog.CategoryService;
import com.hk.pact.service.core.AffilateService;
import com.hk.pact.service.core.WarehouseService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.order.RewardPointService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;

@Service
public class OrderServiceImpl implements OrderService {

    private static Logger          logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private ShippingOrderService   shippingOrderService;

    @Autowired
    private BaseDao                baseDao;

    @Autowired
    private UserService            userService;
    @Autowired
    private OrderDao               orderDao;
    @Autowired
    private EmailManager           emailManager;
    @Autowired
    private WarehouseService       warehouseService;
    @Autowired
    private SkuService             skuService;
    @Autowired
    private InventoryService       inventoryService;
    @Autowired
    private AffilateService        affilateService;
    @Autowired
    private ReferrerProgramManager referrerProgramManager;
    @Autowired
    private OrderStatusService     orderStatusService;
    @Autowired
    private RewardPointService     rewardPointService;
    @Autowired
    private CategoryService        categoryService;
    
    //@Named (Keys.Env.codMinAmount)
    @Value("#{hkEnvProps['codMinAmount']}")
    private Double codMinAmount;

    @Transactional
    public Order save(Order order) {
        return getOrderDao().save(order);
    }

    @Override
    public Order find(Long orderId) {
        return getOrderDao().get(Order.class, orderId);
    }

    public Order findByUserAndOrderStatus(User user, EnumOrderStatus orderStatus) {
        return getOrderDao().findByUserAndOrderStatus(user, orderStatus);
    }

    @Override
    public Page searchOrders(OrderSearchCriteria orderSearchCriteria, int pageNo, int perPage) {
        return getOrderDao().searchOrders(orderSearchCriteria, pageNo, perPage);
    }

    @Override
    public List<Order> searchOrders(OrderSearchCriteria orderSearchCriteria) {
        return getOrderDao().searchOrders(orderSearchCriteria);
    }

    public OrderLifecycleActivity getOrderLifecycleActivity(EnumOrderLifecycleActivity enumOrderLifecycleActivity) {
        return getOrderDao().get(OrderLifecycleActivity.class, enumOrderLifecycleActivity.getId());
    }

    public OrderStatus getOrderStatus(EnumOrderStatus enumOrderStatus) {
        return getOrderDao().get(OrderStatus.class, enumOrderStatus.getId());
    }

    public Long getCountOfOrdersWithStatus() {
        return getOrderDao().getCountOfOrdersWithStatus(EnumOrderStatus.Placed);
    }

    public Set<OrderCategory> getCategoriesForBaseOrder(Order order) {

        // Map<BasketCategory, Category> basketCategoryMap = new HashMap<BasketCategory, Category>();

        List<BasketCategory> basketCategories = new ArrayList<BasketCategory>();

        Set<CartLineItem> cartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
        for (CartLineItem lineItem : cartLineItems) {
            Category lineItemPrimaryCategory = lineItem.getProductVariant().getProduct().getPrimaryCategory();
            BasketCategory lineItemBasketCategory = new BasketCategory(lineItemPrimaryCategory);

            if (basketCategories.contains(lineItemBasketCategory)) {
                BasketCategory basketCategoryToUpdate = basketCategories.get(basketCategories.indexOf(lineItemBasketCategory));
                basketCategoryToUpdate.addQty(lineItem.getQty()).addAmount(lineItem.getHkPrice());
            } else {
                lineItemBasketCategory.addQty(lineItem.getQty()).addAmount(lineItem.getHkPrice());
                basketCategories.add(lineItemBasketCategory);
            }

        }

        Collections.sort(basketCategories);

        Set<OrderCategory> orderCategories = new HashSet<OrderCategory>();
        boolean primaryCategory = true;

        for (BasketCategory basketCategory : basketCategories) {
            OrderCategory orderCategory = new OrderCategory();
            orderCategory.setOrder(order);
            orderCategory.setCategory(basketCategory.getCategory());
            if (primaryCategory) {
                orderCategory.setPrimary(true);
                primaryCategory = false;
            }
            orderCategory = (OrderCategory) getBaseDao().save(orderCategory);
            orderCategories.add(orderCategory);
        }

        return orderCategories;

    }

    public Category getBasketCategory(ShippingOrder shippingOrder) {
        List<BasketCategory> basketCategories = new ArrayList<BasketCategory>();

        for (LineItem lineItem : shippingOrder.getLineItems()) {
            Category lineItemPrimaryCategory = lineItem.getSku().getProductVariant().getProduct().getPrimaryCategory();
            BasketCategory lineItemBasketCategory = new BasketCategory(lineItemPrimaryCategory);

            if (basketCategories.contains(lineItemBasketCategory)) {
                BasketCategory basketCategoryToUpdate = basketCategories.get(basketCategories.indexOf(lineItemBasketCategory));
                basketCategoryToUpdate.addQty(lineItem.getQty()).addAmount(lineItem.getHkPrice());
            } else {
                lineItemBasketCategory.addQty(lineItem.getQty()).addAmount(lineItem.getHkPrice());
                basketCategories.add(lineItemBasketCategory);
            }

        }

        Collections.sort(basketCategories);

        LineItem firstLineItem = shippingOrder.getLineItems().iterator().next();
        Category basketCategory = firstLineItem.getSku().getProductVariant().getProduct().getPrimaryCategory();

        if (!basketCategories.isEmpty()) {
            basketCategory = basketCategories.get(0).getCategory();
        }

        return basketCategory;
    }

    public Set<ShippingOrder> createShippingOrders(Order order) {
        Set<ShippingOrder> shippingOrders = new HashSet<ShippingOrder>();
        try {
            // TODO looks redundant, since services don't have inventory and are JIT it will never split by the logic
            // defined below, then Y a special check??
            if (order.getContainsServices()) {
                String comments = "Order has services,abort system split and do a manual split";
                logOrderActivityByAdmin(order, EnumOrderLifecycleActivity.OrderCouldNotBeAutoSplit, comments);
                logger.debug("order with gatewayId:" + order.getGatewayOrderId() + " has services. abort system split and do a manual split");
            } else if (EnumOrderStatus.Placed.getId().equals(order.getOrderStatus().getId())) {
                shippingOrders = splitOrder(order);
            } else {
                logger.debug("order with gatewayId:" + order.getGatewayOrderId() + " is not in placed status. abort system split and do a manual split");
            }
        } catch (OrderSplitException e) {
            logger.error(e.getMessage());
        }

        return shippingOrders;
    }

    /**
     * if all shipping orders for a order are in shipped/delievered or escalted to packing queue status update base
     * order's status to statusToUpdate
     */
    @Transactional
    public boolean updateOrderStatusFromShippingOrders(Order order, EnumShippingOrderStatus soStatus, EnumOrderStatus boStatusOnSuccess) {

        boolean shouldUpdate = true;

        for (ShippingOrder shippingOrder : order.getShippingOrders()) {
            if (!soStatus.getId().equals(shippingOrder.getOrderStatus().getId())) {
                shouldUpdate = false;
                break;
            }
        }

        if (shouldUpdate) {
            order.setOrderStatus(getOrderStatus(boStatusOnSuccess));
            order = getOrderDao().save(order);
        }
        /*
         * else { order.setOrderStatus(orderStatusDao.find(boStatusOnFailure.getId())); order =
         * getOrderDao().save(order); }
         */

        return shouldUpdate;
    }

    public void processOrderForAutoEsclationAfterPaymentConfirmed(Order order) {
        // Auto escalation of order if unbooked inventory is positive
        if (order.getShippingOrders() != null && !order.getShippingOrders().isEmpty()) {
            for (ShippingOrder shippingOrder : order.getShippingOrders()) {
                shippingOrderService.autoEscalateShippingOrder(shippingOrder);
            }
        } else if (order.getShippingOrders() == null && order.getShippingOrders().isEmpty()) {
            splitOrder(order);
        }
    }

    @Transactional
    public Order escalateOrderFromActionQueue(Order order, String shippingOrderGatewayId) {
        // order = updateOrderStatusFromShippingOrders(order, EnumShippingOrderStatus.SO_Ready_For_Process,
        // EnumOrderStatus.ESCALTED, EnumOrderStatus.PARTIAL_ESCALTION);
        OrderLifecycleActivity orderLifecycleActivity = getOrderLifecycleActivity(EnumOrderLifecycleActivity.EscalatedToProcessingQueue);
        logOrderActivity(order, userService.getLoggedInUser(), orderLifecycleActivity, shippingOrderGatewayId + "escalated from action queue");

        return order;
    }

    public Order moveOrderBackToActionQueue(Order order, String shippingOrderGatewayId) {
        /*
         * order.setOrderStatus(orderStatusDao.find(EnumOrderStatus.ActionAwaiting.getId())); order =
         * getOrderDao().save(order);
         */

        OrderLifecycleActivity orderLifecycleActivity = getOrderLifecycleActivity(EnumOrderLifecycleActivity.EscalatedBackToAwaitingQueue);
        logOrderActivity(order, userService.getLoggedInUser(), orderLifecycleActivity, shippingOrderGatewayId + "escalated back to  action queue");

        return order;
    }

    @Transactional
    public Order markOrderAsShipped(Order order) {
        boolean isUpdated = updateOrderStatusFromShippingOrders(order, EnumShippingOrderStatus.SO_Shipped, EnumOrderStatus.Shipped);
        if (isUpdated) {
            logOrderActivity(order, EnumOrderLifecycleActivity.OrderShipped);
        }
        return order;
    }

    @Transactional
    public Order markOrderAsDelivered(Order order) {
        boolean isUpdated = updateOrderStatusFromShippingOrders(order, EnumShippingOrderStatus.SO_Delivered, EnumOrderStatus.Delivered);
        if (isUpdated) {
            logOrderActivity(order, EnumOrderLifecycleActivity.OrderDelivered);
            approvePendingRewardPointsForOrder(order);
            // Currently commented as we aren't doing COD for services as of yet, When we start, We may have to put a
            // check if payment mode was COD and email hasn't been sent yet
            // sendEmailToServiceProvidersForOrder(order);
        }
        return order;
    }

    @Transactional
    public Order markOrderAsRTO(Order order) {
        boolean isUpdated = updateOrderStatusFromShippingOrders(order, EnumShippingOrderStatus.SO_Returned, EnumOrderStatus.RTO);
        if (isUpdated) {
            logOrderActivity(order, EnumOrderLifecycleActivity.OrderReturned);
        } else {
            logOrderActivity(order, EnumOrderLifecycleActivity.OrderPartiallyReturned);
        }
        return order;
    }

    public Order getLatestOrderForUser(User user) {
        return getOrderDao().getLatestOrderForUser(user);
    }

   
    public Page listOrdersForUser(User user, int page, int perPage) {
        List<OrderStatus> orderStatusList = orderStatusService.getOrderStatuses(EnumOrderStatus.getStatusForCustomers());
        return getOrderDao().listOrdersForUser(orderStatusList, user, page, perPage);
    }

    /**
     * @param order
     * @return set of shipping orders which are split/derived from a base order
     * @throws OrderSplitException
     */

    public Set<ShippingOrder> splitOrder(Order order) throws OrderSplitException {
        Set<ShippingOrder> shippingOrders = new HashSet<ShippingOrder>();
        if (EnumOrderStatus.Placed.getId().equals(order.getOrderStatus().getId())) {
            long startTime = (new Date()).getTime();

            Set<CartLineItem> productCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
            Map<CartLineItem, Set<Warehouse>> cartLineItemWarehouseListMap = new HashMap<CartLineItem, Set<Warehouse>>();

            for (CartLineItem lineItem : productCartLineItems) {
                List<Sku> skuList = new ArrayList<Sku>();
                skuList = skuService.getSKUsForProductVariant(lineItem.getProductVariant());

                Set<Warehouse> applicableWarehousesForLineItem = null;
                if (!skuList.isEmpty()) {
                    applicableWarehousesForLineItem = new HashSet<Warehouse>();
                    for (Sku sku : skuList) {
                        if (inventoryService.getAvailableUnbookedInventory(sku) > 0) {
                            applicableWarehousesForLineItem.add(sku.getWarehouse());
                        }
                    }
                    /*
                     * Can be uncommented if daddy doesn't agree if (applicableWarehousesForLineItem.isEmpty()) { String
                     * comments = "Did not get the required qty in any of the warehouse as none had the right amount of
                     * net inventory to serve the order, one of the sku being " +
                     * lineItem.getProductVariant().getProduct().getName(); logOrderActivityByAdmin(order,
                     * EnumOrderLifecycleActivity.OrderCouldNotBeAutoSplit, comments); throw new
                     * OrderSplitException("Didn't get inventory for sku. Aborting splitting of order.", order); }
                     */
                } else {
                    String comments = "No Sku has been created for " + lineItem.getProductVariant().getProduct().getName();
                    logOrderActivityByAdmin(order, EnumOrderLifecycleActivity.OrderCouldNotBeAutoSplit, comments);
                    throw new OrderSplitException("Didn't get sku for few variants. Aborting splitting of order.", order);
                }
                cartLineItemWarehouseListMap.put(lineItem, applicableWarehousesForLineItem);
            }

            // phase 1 complete

            Map<Warehouse, Set<CartLineItem>> warehouseLineItemSetMap = new HashMap<Warehouse, Set<CartLineItem>>();

            for (CartLineItem cartLineItem : cartLineItemWarehouseListMap.keySet()) {
                Warehouse warehouse = warehouseService.getWarehouseToBeAssignedByDefinedLogicForSplitting(cartLineItemWarehouseListMap.get(cartLineItem));

                if (warehouseLineItemSetMap.containsKey(warehouse)) {
                    Set<CartLineItem> cartLineItems = warehouseLineItemSetMap.get(warehouse);
                    if (cartLineItems != null) {
                        cartLineItems.add(cartLineItem);
                    }
                    warehouseLineItemSetMap.put(warehouse, cartLineItems);
                } else {
                    Set<CartLineItem> cartLineItems = new HashSet<CartLineItem>();
                    cartLineItems.add(cartLineItem);
                    warehouseLineItemSetMap.put(warehouse, cartLineItems);
                }
            }

            // phase 2 complete

            // COD Amount Check
            if (order.getPayment().getPaymentMode().getId() == EnumPaymentMode.COD.getId()) {
                for (Map.Entry<Warehouse, Set<CartLineItem>> warehouseSetEntry : warehouseLineItemSetMap.entrySet()) {
                    ShippingOrder shippingOrder = shippingOrderService.createSOWithBasicDetails(order, warehouseSetEntry.getKey());
                    for (CartLineItem cartLineItem : warehouseSetEntry.getValue()) {
                        Sku sku = skuService.getSKU(cartLineItem.getProductVariant(), warehouseSetEntry.getKey());
                        LineItem shippingOrderLineItem = LineItemHelper.createLineItemWithBasicDetails(sku, shippingOrder, cartLineItem);
                        shippingOrder.getLineItems().add(shippingOrderLineItem);
                    }
                    ShippingOrderHelper.updateAccountingOnSOLineItems(shippingOrder, order);
                    if (ShippingOrderHelper.getAmountForSO(shippingOrder) <= codMinAmount) {
                        String comments = "One of the SO amount was computed below " + codMinAmount;
                        logOrderActivityByAdmin(order, EnumOrderLifecycleActivity.OrderCouldNotBeAutoSplit, comments);
                        throw new OrderSplitException(comments + ". Aborting splitting of order.", order);
                    }
                }
            }

            // Create Shipping orders and Save it in DB

            for (Map.Entry<Warehouse, Set<CartLineItem>> warehouseSetEntry : warehouseLineItemSetMap.entrySet()) {

                ShippingOrder shippingOrder = shippingOrderService.createSOWithBasicDetails(order, warehouseSetEntry.getKey());
                for (CartLineItem cartLineItem : warehouseSetEntry.getValue()) {
                    Sku sku = skuService.getSKU(cartLineItem.getProductVariant(), warehouseSetEntry.getKey());
                    LineItem shippingOrderLineItem = LineItemHelper.createLineItemWithBasicDetails(sku, shippingOrder, cartLineItem);
                    shippingOrder.getLineItems().add(shippingOrderLineItem);
                }
                shippingOrder.setBasketCategory(getBasketCategory(shippingOrder).getName());
                ShippingOrderHelper.updateAccountingOnSOLineItems(shippingOrder, order);
                shippingOrder.setAmount(ShippingOrderHelper.getAmountForSO(shippingOrder));
                shippingOrder = shippingOrderService.save(shippingOrder);
                /**
                 * this additional call to save is done so that we have shipping order id to generate shipping order
                 * gateway id
                 */
                shippingOrder = ShippingOrderHelper.setGatewayIdOnShippingOrder(shippingOrder);
                shippingOrder = shippingOrderService.save(shippingOrder);
                shippingOrders.add(shippingOrder);
            }

            long endTime = (new Date()).getTime();
            logger.debug("Total time to split order[" + order.getId() + "] = " + (endTime - startTime));
        } else {
            logger.debug("order with gatewayId:" + order.getGatewayOrderId() + " is not in placed status. abort system split and do a manual split");
        }

        return shippingOrders;
    }

    /*
     * Warehouse prevWH = null; Map<Warehouse, Set<CartLineItem>> warehouseLineItemSetMap = new HashMap<Warehouse,
     * Set<CartLineItem>>(); for (CartLineItem lineItem : productCartLineItems) { List<Sku> skuList =
     * skuService.getSKUsForProductVariant(lineItem.getProductVariant()); if (skuList == null || skuList.isEmpty()) {
     * String comments = "No Sku has been created for " + lineItem.getProductVariant().getProduct().getName();
     * logOrderActivity(order,userService.getAdminUser(),orderLifecycleActivityDaoProvider.get().find(EnumOrderLifecycleActivity.OrderCouldNotBeAutoSplit.getId()),comments);
     * throw new OrderSplitException("Didn't get sku for few variants. Aborting splitting of order.", order); } List<Warehouse>
     * warehouses = inventoryService.getWarehousesForSkuAndQty(skuList, lineItem.getQty()); //List<Warehouse>
     * warehouses = Arrays.asList(warehouseService.getDefaultWarehouse()); if (warehouses == null ||
     * warehouses.isEmpty()) { String comments = "Did not get the required qty in any of the warehouse as none had the
     * right amount of net inventory to serve the order, one of the sku being " +
     * lineItem.getProductVariant().getProduct().getName();
     * logOrderActivity(order,userService.getAdminUser(),orderLifecycleActivityDaoProvider.get().find(EnumOrderLifecycleActivity.OrderCouldNotBeAutoSplit.getId()),comments);
     * throw new OrderSplitException("Didn't get inventory for sku. Aborting splitting of order.", order); } for
     * (Warehouse warehouse : warehouses) { if (prevWH == null) { // First lineItem Set<CartLineItem> whLi = new
     * HashSet<CartLineItem>(); whLi.add(lineItem); warehouseLineItemSetMap.put(warehouse, whLi); prevWH = warehouse; }
     * else if (warehouse.equals(prevWH)) { Set<CartLineItem> prevWHLi = warehouseLineItemSetMap.get(prevWH);
     * prevWHLi.add(lineItem); warehouseLineItemSetMap.put(warehouse, prevWHLi); } else { Set<CartLineItem> whLi = new
     * HashSet<CartLineItem>(); whLi.add(lineItem); warehouseLineItemSetMap.put(warehouse, whLi); } break; } } //Sanity
     * check of CartLI->Li; few CartLIs get lost while splitting Long liCounter = 0L; for (Map.Entry<Warehouse, Set<CartLineItem>>
     * warehouseSetEntry : warehouseLineItemSetMap.entrySet()) { Set<CartLineItem> whCartLineItems =
     * warehouseSetEntry.getValue(); liCounter += whCartLineItems.size(); } if (liCounter !=
     * productCartLineItems.size()) { String comments = "LineItems count doesn't match with CartLineItems. Aborting
     * splitting of order";
     * logOrderActivity(order,userService.getAdminUser(),orderLifecycleActivityDaoProvider.get().find(EnumOrderLifecycleActivity.OrderCouldNotBeAutoSplit.getId()),comments);
     * throw new OrderSplitException("LineItems count doesn't match with CartLineItems. Aborting splitting of order.",
     * order); }
     */

    public ProductVariant getTopDealVariant(Order order) {
        Category personalCareCategory = getCategoryService().getCategoryByName("personal-care");
        ProductVariant topOrderedVariant = null;
        Set<CartLineItem> productCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();

        for (CartLineItem cartLineItem : productCartLineItems) {
            ProductVariant productVariant = cartLineItem.getProductVariant();
            // Excluding personal care
            if (!productVariant.getProduct().getCategories().contains(personalCareCategory)) {

                if (topOrderedVariant == null) {
                    topOrderedVariant = productVariant;
                } else if (productVariant.getDiscountPercent() > topOrderedVariant.getDiscountPercent()) {
                    topOrderedVariant = productVariant;
                }
            }
        }
        return topOrderedVariant;
    }

    public void approvePendingRewardPointsForOrder(Order order) {
        rewardPointService.approvePendingRewardPointsForOrder(order);
    }

    public void sendEmailToServiceProvidersForOrder(Order order) {
        CartLineItemFilter cartLineItemFilter = new CartLineItemFilter(order.getCartLineItems());
        Set<CartLineItem> serviceCartLineItems = cartLineItemFilter.addCartLineItemType(EnumCartLineItemType.Product).hasOnlyServiceLineItems(true).filter();
        for (CartLineItem lineItem : serviceCartLineItems) {
            // emailManager.sendServiceVoucherMailToCustomer(lineItem);
            emailManager.sendServiceVoucherMailToServiceProvider(order, lineItem);
        }
    }

    public void logOrderActivity(Order order, EnumOrderLifecycleActivity enumOrderLifecycleActivity) {
        User user = userService.getLoggedInUser();
        OrderLifecycleActivity orderLifecycleActivity = getOrderLifecycleActivity(enumOrderLifecycleActivity);
        logOrderActivity(order, user, orderLifecycleActivity, null);
    }

    public void logOrderActivityByAdmin(Order order, EnumOrderLifecycleActivity enumOrderLifecycleActivity, String comments) {
        User user = userService.getAdminUser();
        OrderLifecycleActivity orderLifecycleActivity = getOrderLifecycleActivity(enumOrderLifecycleActivity);
        logOrderActivity(order, user, orderLifecycleActivity, comments);
    }

    public void logOrderActivity(Order order, User user, OrderLifecycleActivity orderLifecycleActivity, String comments) {
        OrderLifecycle orderLifecycle = new OrderLifecycle();
        orderLifecycle.setOrder(order);
        orderLifecycle.setOrderLifecycleActivity(orderLifecycleActivity);
        orderLifecycle.setUser(user);
        if (StringUtils.isNotBlank(comments)) {
            orderLifecycle.setComments(comments);
        }
        orderLifecycle.setActivityDate(new Date());
        getOrderDao().save(orderLifecycle);
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }

    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public ShippingOrderService getShippingOrderService() {
        return shippingOrderService;
    }

    public void setShippingOrderService(ShippingOrderService shippingOrderService) {
        this.shippingOrderService = shippingOrderService;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public EmailManager getEmailManager() {
        return emailManager;
    }

    public void setEmailManager(EmailManager emailManager) {
        this.emailManager = emailManager;
    }

    public WarehouseService getWarehouseService() {
        return warehouseService;
    }

    public void setWarehouseService(WarehouseService warehouseService) {
        this.warehouseService = warehouseService;
    }

    public SkuService getSkuService() {
        return skuService;
    }

    public void setSkuService(SkuService skuService) {
        this.skuService = skuService;
    }

    public InventoryService getInventoryService() {
        return inventoryService;
    }

    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public AffilateService getAffilateService() {
        return affilateService;
    }

    public void setAffilateService(AffilateService affilateService) {
        this.affilateService = affilateService;
    }

    public ReferrerProgramManager getReferrerProgramManager() {
        return referrerProgramManager;
    }

    public void setReferrerProgramManager(ReferrerProgramManager referrerProgramManager) {
        this.referrerProgramManager = referrerProgramManager;
    }

    public OrderStatusService getOrderStatusService() {
        return orderStatusService;
    }

    public void setOrderStatusService(OrderStatusService orderStatusService) {
        this.orderStatusService = orderStatusService;
    }

    public RewardPointService getRewardPointService() {
        return rewardPointService;
    }

    public void setRewardPointService(RewardPointService rewardPointService) {
        this.rewardPointService = rewardPointService;
    }

    public CategoryService getCategoryService() {
        return categoryService;
    }

    public void setCategoryService(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    public BaseDao getBaseDao() {
        return baseDao;
    }

    public void setBaseDao(BaseDao baseDao) {
        this.baseDao = baseDao;
    }

}