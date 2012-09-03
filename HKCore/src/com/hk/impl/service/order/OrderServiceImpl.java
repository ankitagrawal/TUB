package com.hk.impl.service.order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.akube.framework.dao.Page;
import com.hk.comparator.BasketCategory;
import com.hk.constants.core.Keys;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.core.search.OrderSearchCriteria;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.core.OrderLifecycleActivity;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.OrderCategory;
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
import com.hk.pact.service.order.OrderLoggingService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.order.OrderSplitterService;
import com.hk.pact.service.order.RewardPointService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;

import com.hk.pojo.DummyOrder;

@Service
public class OrderServiceImpl implements OrderService {

    private static Logger logger = LoggerFactory.getLogger(OrderService.class);

    @Autowired
    private ShippingOrderService shippingOrderService;
    @Autowired
    private BaseDao baseDao;
    @Autowired
    private UserService userService;
    @Autowired
    private OrderDao orderDao;
    @Autowired
    private EmailManager emailManager;
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private SkuService skuService;
    @Autowired
    private InventoryService inventoryService;
    @Autowired
    private AffilateService affilateService;
    @Autowired
    private ReferrerProgramManager referrerProgramManager;
    @Autowired
    private OrderStatusService orderStatusService;
    @Autowired
    private RewardPointService rewardPointService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private OrderLoggingService orderLoggingService;
    @Autowired
    private OrderSplitterService orderSplitterService;


    @Value("#{hkEnvProps['" + Keys.Env.codMinAmount + "']}")
    private Double codMinAmount;

    @Transactional
    public Order save(Order order) {
        return getOrderDao().save(order);
    }

    @Override
    public Order find(Long orderId) {
        return getOrderDao().get(Order.class, orderId);
    }

    public Order findByGatewayOrderId(String gatewayOrderId) {
        return orderDao.findByGatewayOrderId(gatewayOrderId);
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
            if (order.getContainsServices()) {
                String comments = "Order has services,abort system split and do a manual split";
                getOrderLoggingService().logOrderActivityByAdmin(order, EnumOrderLifecycleActivity.OrderCouldNotBeAutoSplit, comments);
                logger.debug("order with gatewayId:" + order.getGatewayOrderId() + " has services. abort system split and do a manual split");
            } else if (EnumOrderStatus.Placed.getId().equals(order.getOrderStatus().getId())) {
                shippingOrders = splitOrder(order);
            } else {
                logger.debug("order with gatewayId:" + order.getGatewayOrderId() + " is not in placed status. abort system split and do a manual split");
            }
        } catch (OrderSplitException e) {
            logger.error(e.getMessage());
        } catch (Exception e) {
            logger.error("Order could not be split due to some exception ", e);
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

        User loggedOnUser = getUserService().getLoggedInUser();
        if (loggedOnUser == null) {
            loggedOnUser = order.getUser();
        }

        OrderLifecycleActivity orderLifecycleActivity = getOrderLoggingService().getOrderLifecycleActivity(EnumOrderLifecycleActivity.EscalatedToProcessingQueue);
        getOrderLoggingService().logOrderActivity(order, loggedOnUser, orderLifecycleActivity, shippingOrderGatewayId + "escalated from action queue");

        return order;
    }

    /*
     * @Transactional public Order moveOrderBackToActionQueue(Order order, String shippingOrderGatewayId) {
     * order.setOrderStatus(orderStatusDao.find(EnumOrderStatus.ActionAwaiting.getId())); order =
     * getOrderDao().save(order); OrderLifecycleActivity orderLifecycleActivity =
     * getOrderLoggingService().getOrderLifecycleActivity(EnumOrderLifecycleActivity.EscalatedBackToAwaitingQueue);
     * getOrderLoggingService().logOrderActivity(order, userService.getLoggedInUser(), orderLifecycleActivity,
     * shippingOrderGatewayId + "escalated back to action queue"); return order; }
     */

    @Transactional
    public Order markOrderAsShipped(Order order) {
        boolean isUpdated = updateOrderStatusFromShippingOrders(order, EnumShippingOrderStatus.SO_Shipped, EnumOrderStatus.Shipped);
        if (isUpdated) {
            getOrderLoggingService().logOrderActivity(order, EnumOrderLifecycleActivity.OrderShipped);
        }
        return order;
    }

    @Transactional
    public Order markOrderAsDelivered(Order order) {
        boolean isUpdated = updateOrderStatusFromShippingOrders(order, EnumShippingOrderStatus.SO_Delivered, EnumOrderStatus.Delivered);
        if (isUpdated) {
            getOrderLoggingService().logOrderActivity(order, EnumOrderLifecycleActivity.OrderDelivered);
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
            getOrderLoggingService().logOrderActivity(order, EnumOrderLifecycleActivity.OrderReturned);
        } else {
            getOrderLoggingService().logOrderActivity(order, EnumOrderLifecycleActivity.OrderPartiallyReturned);
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

    @Transactional
    public Set<ShippingOrder> splitOrder(Order order) throws OrderSplitException {
//        List<Set<CartLineItem>> listOfCartLineItemSet = getMatchCartLineItemOrder(order);
        CartLineItemFilter cartLineItemFilter = new CartLineItemFilter(order.getCartLineItems());         
        Set<CartLineItem> productCartLineItems = cartLineItemFilter.addCartLineItemType(EnumCartLineItemType.Product).filter();
        Set<CartLineItem> groundShippedCartLineItemSet = cartLineItemFilter.addCartLineItemType(EnumCartLineItemType.Product).hasOnlyGroundShippedItems(true).filter();
        productCartLineItems.removeAll(groundShippedCartLineItemSet);

        List<Set<CartLineItem>> listOfCartLineItemSet = new ArrayList<Set<CartLineItem>>();
        if (groundShippedCartLineItemSet != null && groundShippedCartLineItemSet.size() > 0) {
            listOfCartLineItemSet.add(groundShippedCartLineItemSet);
        }
        if (productCartLineItems != null && productCartLineItems.size() > 0) {
            listOfCartLineItemSet.add(productCartLineItems);
        }
        
        Set<ShippingOrder> shippingOrders = new HashSet<ShippingOrder>();

        for (Set<CartLineItem> cartlineitems : listOfCartLineItemSet) {
            if (cartlineitems != null && cartlineitems.size() > 0) {

                List<DummyOrder> dummyOrders = orderSplitterService.listBestDummyOrdersPractically(order, cartlineitems);
                if (EnumOrderStatus.Placed.getId().equals(order.getOrderStatus().getId())) {
                    long startTime = (new Date()).getTime();

                    // Create Shipping orders and Save it in DB
                    for (DummyOrder dummyOrder : dummyOrders) {
                        if (dummyOrder.getCartLineItemList().size() > 0) {
                            Warehouse warehouse = dummyOrder.getWarehouse();
                            ShippingOrder shippingOrder = shippingOrderService.createSOWithBasicDetails(order, warehouse);
                            for (CartLineItem cartLineItem : dummyOrder.getCartLineItemList()) {
                                Sku sku = skuService.getSKU(cartLineItem.getProductVariant(), warehouse);
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
                    }

                    long endTime = (new Date()).getTime();
                    logger.debug("Total time to split order[" + order.getId() + "] = " + (endTime - startTime));
                } else {
                    logger.debug("order with gatewayId:" + order.getGatewayOrderId() + " is not in placed status. abort system split and do a manual split");
                }
            }
        }
        return shippingOrders;

    }

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

    public OrderLoggingService getOrderLoggingService() {
        return orderLoggingService;
    }

    public void setOrderLoggingService(OrderLoggingService orderLoggingService) {
        this.orderLoggingService = orderLoggingService;
    }

    public boolean isCODAllowed(Order order) {
        CartLineItemFilter cartLineItemFilter = new CartLineItemFilter(order.getCartLineItems());
        Set<CartLineItem> productCartLineItems = cartLineItemFilter.addCartLineItemType(EnumCartLineItemType.Product).filter();
        for (CartLineItem productCartLineItem : productCartLineItems) {

            ProductVariant productVariant = productCartLineItem.getProductVariant();
            if (productVariant != null && productVariant.getProduct() != null) {
                Product product = productVariant.getProduct();
                if (product.isCodAllowed() != null && !product.isCodAllowed()) {
                    return false;
                }
            }
        }
        return true;
    }
    
}