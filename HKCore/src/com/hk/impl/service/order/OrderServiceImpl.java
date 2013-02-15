package com.hk.impl.service.order;

import com.akube.framework.dao.Page;
import com.hk.cache.CategoryCache;
import com.hk.comparator.BasketCategory;
import com.hk.constants.catalog.category.CategoryConstants;
import com.hk.constants.courier.CourierConstants;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.core.search.OrderSearchCriteria;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.Product;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.OrderLifecycleActivity;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.order.CartLineItem;
import com.hk.domain.order.Order;
import com.hk.domain.order.OrderCategory;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.sku.Sku;
import com.hk.domain.user.User;
import com.hk.domain.user.UserCodCall;
import com.hk.domain.warehouse.Warehouse;

import com.hk.exception.NoSkuException;
import com.hk.exception.OrderSplitException;
import com.hk.helper.LineItemHelper;
import com.hk.helper.OrderDateUtil;
import com.hk.helper.ShippingOrderHelper;
import com.hk.manager.EmailManager;
import com.hk.manager.ReferrerProgramManager;

import com.hk.pact.dao.BaseDao;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.dao.shippingOrder.LineItemDao;
import com.hk.pact.service.OrderStatusService;
import com.hk.pact.service.UserService;
import com.hk.pact.service.core.AffilateService;
import com.hk.pact.service.core.WarehouseService;
import com.hk.pact.service.inventory.InventoryService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.order.OrderLoggingService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.order.OrderSplitterService;
import com.hk.pact.service.order.RewardPointService;
import com.hk.pact.service.shippingOrder.ShipmentService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.pojo.DummyOrder;
import com.hk.util.HKDateUtil;
import com.hk.util.OrderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;

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
    /*
     * @Autowired private CategoryService categoryService;
     */
    @Autowired
    private OrderLoggingService orderLoggingService;
    @Autowired
    private OrderSplitterService orderSplitterService;
    @Autowired
    private ShippingOrderStatusService shippingOrderStatusService;
    @Autowired
    LineItemDao lineItemDao;

    @Autowired
    ShipmentService shipmentService;



    /*
     * @Value("#{hkEnvProps['" + Keys.Env.codMinAmount + "']}") private Double codMinAmount;
     */

    // @Value("#{hkEnvProps['codMaxAmount']}")
    // private Double codMaxAmount;
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

    /**
     * this will return the dispatch date for BO by adding min of dispatch days to refdate honouring the constraints of
     * warehouse like last time a order will be processed in WH each day (say till 4pm),
     *
     * @param order
     * @return
     */
    @Transactional
    @Override
    public void setTargetDispatchDelDatesOnBO(Order order) {
        Long[] dispatchDays = OrderUtil.getDispatchDaysForBO(order);
        Date refDateForBO = order.getPayment().getPaymentDate();
        Date refDateForSO = null;

        if (order.getTargetDispatchDate() == null) {
            Date targetDispatchDate = OrderDateUtil.getTargetDispatchDateForWH(refDateForBO, dispatchDays[0]);
            order.setTargetDispatchDate(targetDispatchDate);
            order.setTargetDispatchDateOnVerification(targetDispatchDate);
            refDateForSO = order.getPayment().getPaymentDate();
        }

        if (order.getTargetDelDate() == null && order.getTargetDispatchDate() != null) {
            Long diffInPromisedTimes = (dispatchDays[1] - dispatchDays[0]);
            int daysTakenForDelievery = Integer.valueOf(diffInPromisedTimes.toString());
            Date targetDelDate = HKDateUtil.addToDate(order.getTargetDispatchDate(), Calendar.DAY_OF_MONTH, daysTakenForDelievery);
            order.setTargetDelDate(targetDelDate);
        }

        if (EnumPaymentStatus.getEscalablePaymentStatusIds().contains(order.getPayment().getPaymentStatus().getId())) {
            Date targetDispatchDateOnVerification = OrderDateUtil.getTargetDispatchDateForWH(new Date(), dispatchDays[0]);
            order.setTargetDispatchDateOnVerification(targetDispatchDateOnVerification);
            refDateForSO = new Date();
        }

        /**
         * if target dispatch date was updated either on payment or on verification of payment, the change needs to
         * reflect to SO.
         */
        if (refDateForSO != null) {
            for (ShippingOrder shippingOrder : order.getShippingOrders()) {
                getShippingOrderService().setTargetDispatchDelDatesOnSO(refDateForSO, shippingOrder);
            }
        }

        getOrderDao().save(order);

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
            if (EnumOrderStatus.Placed.getId().equals(order.getOrderStatus().getId())) {
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
            if (!getShippingOrderService().shippingOrderHasReplacementOrder(shippingOrder)) {
                if (!soStatus.getId().equals(shippingOrder.getOrderStatus().getId())) {
                    shouldUpdate = false;
                    break;
                }
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
        splitBOCreateShipmentEscalateSOAndRelatedTasks(order);
    }

    @Transactional
    public Order escalateOrderFromActionQueue(Order order, String shippingOrderGatewayId) {
        User loggedOnUser = getUserService().getLoggedInUser();
        // User loggedOnUser = UserCache.getInstance().getLoggedInUser();
        if (loggedOnUser == null) {
            loggedOnUser = order.getUser();
        }

        OrderLifecycleActivity orderLifecycleActivity = getOrderLoggingService().getOrderLifecycleActivity(EnumOrderLifecycleActivity.EscalatedToProcessingQueue);
        getOrderLoggingService().logOrderActivity(order, loggedOnUser, orderLifecycleActivity, shippingOrderGatewayId + "escalated from action queue");

        return order;
    }

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
            affilateService.approvePendingAffiliateTxn(order);
            // Currently commented as we aren't doing COD for services as of yet, When we start, We may have to put a
            // check if payment mode was COD and email hasn't been sent yet
            // sendEmailToServiceProvidersForOrder(order);
        }
        return order;
    }

    @Transactional
    public Order markOrderAsRTO(Order order) {
        boolean isUpdated = updateOrderStatusFromShippingOrders(order, EnumShippingOrderStatus.SO_RTO, EnumOrderStatus.RTO);
        if (isUpdated) {
            getOrderLoggingService().logOrderActivity(order, EnumOrderLifecycleActivity.OrderReturned);
        } else {
            getOrderLoggingService().logOrderActivity(order, EnumOrderLifecycleActivity.OrderPartiallyReturned);
        }
        affilateService.cancelTxn(order);
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
        // List<Set<CartLineItem>> listOfCartLineItemSet = getMatchCartLineItemOrder(order);
        CartLineItemFilter cartLineItemFilter = new CartLineItemFilter(order.getCartLineItems());
        Set<CartLineItem> productCartLineItems = cartLineItemFilter.addCartLineItemType(EnumCartLineItemType.Product).filter();

        CartLineItemFilter groundShipLineItemFilter = new CartLineItemFilter(order.getCartLineItems());
        Set<CartLineItem> groundShippedCartLineItemSet = groundShipLineItemFilter.addCartLineItemType(EnumCartLineItemType.Product).hasOnlyGroundShippedItems(true).filter();

        CartLineItemFilter serviceCartLineItemFilter = new CartLineItemFilter(order.getCartLineItems());
        Set<CartLineItem> serviceCartLineItems = serviceCartLineItemFilter.addCartLineItemType(EnumCartLineItemType.Product).hasOnlyServiceLineItems(true).filter();

//   putting the logic of drop ship
        CartLineItemFilter dropShipLineItemFilter = new CartLineItemFilter(order.getCartLineItems());
        Set<CartLineItem> dropShippedCartLineItemSet = dropShipLineItemFilter.addCartLineItemType(EnumCartLineItemType.Product).hasOnlyDropShippedItems(true).filter();

//     Its contain only the groundshipped Line Item
        if (groundShippedCartLineItemSet != null && !groundShippedCartLineItemSet.isEmpty()) {
            groundShippedCartLineItemSet.removeAll(dropShippedCartLineItemSet);
        }
        productCartLineItems.removeAll(serviceCartLineItems);
        productCartLineItems.removeAll(groundShippedCartLineItemSet); // i.e product cart lineItems without services
        // and ground shipped product
        productCartLineItems.removeAll(dropShippedCartLineItemSet);

        Map<Long, Set<CartLineItem>> supplierDropShipMap = filterDropShippedItemOnSupplier(dropShippedCartLineItemSet);

        List<Set<CartLineItem>> listOfCartLineItemSet = new ArrayList<Set<CartLineItem>>();
        if (groundShippedCartLineItemSet != null && groundShippedCartLineItemSet.size() > 0) {
            listOfCartLineItemSet.add(groundShippedCartLineItemSet);
        }
        if (productCartLineItems != null && productCartLineItems.size() > 0) {
            listOfCartLineItemSet.add(productCartLineItems);
        }
        if (!supplierDropShipMap.isEmpty()) {
            Set<Long> keys = supplierDropShipMap.keySet();
            for (Long key : keys) {
                listOfCartLineItemSet.add(supplierDropShipMap.get(key));
            }
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
                            boolean isDropShipped = false;
                            ShippingOrder shippingOrder = shippingOrderService.createSOWithBasicDetails(order, warehouse);
                            for (CartLineItem cartLineItem : dummyOrder.getCartLineItemList()) {
                                isDropShipped = cartLineItem.getProductVariant().getProduct().isDropShipping();
                                Sku sku = skuService.getSKU(cartLineItem.getProductVariant(), warehouse);
                                LineItem shippingOrderLineItem = LineItemHelper.createLineItemWithBasicDetails(sku, shippingOrder, cartLineItem);
                                shippingOrder.getLineItems().add(shippingOrderLineItem);
                            }
                            shippingOrder.setDropShipping(isDropShipped);
                            shippingOrder.setBasketCategory(getBasketCategory(shippingOrder).getName());
                            ShippingOrderHelper.updateAccountingOnSOLineItems(shippingOrder, order);
                            shippingOrder.setAmount(ShippingOrderHelper.getAmountForSO(shippingOrder));
                            shippingOrder = shippingOrderService.save(shippingOrder);
                            /**
                             * this additional call to save is done so that we have shipping order id to generate
                             * shipping order gateway id
                             */
                            shippingOrder = shippingOrderService.setGatewayIdAndTargetDateOnShippingOrder(shippingOrder);
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

        if (serviceCartLineItems != null && serviceCartLineItems.size() > 0) {
            // orderSplitterService.createSOForService(serviceCartLineItems) ;
            for (CartLineItem serviceCartLineItem : serviceCartLineItems) {
                shippingOrders.add(createSOForService(serviceCartLineItem));
            }

        }

        return shippingOrders;

    }

    public ProductVariant getTopDealVariant(Order order) {
        Category personalCareCategory = CategoryCache.getInstance().getCategoryByName(CategoryConstants.PERSONAL_CARE).getCategory();

        // Category personalCareCategory = getCategoryService().getCategoryByName("personal-care");
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

    /*
     * public CategoryService getCategoryService() { return categoryService; } public void
     * setCategoryService(CategoryService categoryService) { this.categoryService = categoryService; }
     */

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

    public ShippingOrder createSOForService(CartLineItem serviceCartLineItem) {
        Order baseOrder = serviceCartLineItem.getOrder();
        Warehouse corporateOffice = getWarehouseService().getCorporateOffice();
        ShippingOrder shippingOrder = getShippingOrderService().createSOWithBasicDetails(baseOrder, corporateOffice);
        shippingOrder.setBaseOrder(baseOrder);

        ProductVariant productVariant = serviceCartLineItem.getProductVariant();
        Sku sku = getSkuService().getSKU(productVariant, corporateOffice);
        if (sku != null) {
            LineItem shippingOrderLineItem = LineItemHelper.createLineItemWithBasicDetails(sku, shippingOrder, serviceCartLineItem);
            shippingOrder.getLineItems().add(shippingOrderLineItem);
        } else {
            throw new NoSkuException(productVariant, corporateOffice);
        }

        shippingOrder.setBasketCategory(CategoryConstants.SERVICES);
        shippingOrder.setServiceOrder(true);
        shippingOrder.setOrderStatus(getShippingOrderStatusService().find(EnumShippingOrderStatus.SO_ReadyForProcess));
        ShippingOrderHelper.updateAccountingOnSOLineItems(shippingOrder, baseOrder);
        shippingOrder.setAmount(ShippingOrderHelper.getAmountForSO(shippingOrder));
        shippingOrder = getShippingOrderService().save(shippingOrder);
        /**
         * this additional call to save is done so that we have shipping order id to generate shipping order gateway id
         */
        shippingOrder = shippingOrderService.setGatewayIdAndTargetDateOnShippingOrder(shippingOrder);
        shippingOrder = getShippingOrderService().save(shippingOrder);

        return shippingOrder;

    }

    public boolean isShippingOrderExists(Order order) {
        Set<CartLineItem> productCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
        for (CartLineItem cartLineItem : productCartLineItems) {
            if (lineItemDao.getLineItem(cartLineItem) != null) {
                return true;
            }
        }
        return false;
    }

    public ShippingOrderStatusService getShippingOrderStatusService() {
        return shippingOrderStatusService;
    }

    public void setShippingOrderStatusService(ShippingOrderStatusService shippingOrderStatusService) {
        this.shippingOrderStatusService = shippingOrderStatusService;
    }


    public Map<Long, Set<CartLineItem>> filterDropShippedItemOnSupplier(Set<CartLineItem> dropShippedCartLineItemSet) {
        Map<Long, Set<CartLineItem>> supplierDropShipMap = new HashMap<Long, Set<CartLineItem>>();
        for (CartLineItem cartlineItem1 : dropShippedCartLineItemSet) {
            if (cartlineItem1 != null) {
                ProductVariant productVariant = cartlineItem1.getProductVariant();
                if (productVariant != null) {
                    Product product = productVariant.getProduct();
                    if (product != null && product.getSupplier() != null) {
                        Long supplierid = product.getSupplier().getId();
                        if (supplierDropShipMap.containsKey(supplierid)) {
                            supplierDropShipMap.get(supplierid).add(cartlineItem1);
                        } else {
                            Set<CartLineItem> itemSet = new HashSet<CartLineItem>();
                            itemSet.add(cartlineItem1);
                            supplierDropShipMap.put(supplierid, itemSet);
                        }
                    }
                }
            }
        }
        return supplierDropShipMap;
    }

    @Override
    @Transactional
    public boolean splitBOCreateShipmentEscalateSOAndRelatedTasks(Order order) {

        Set<CartLineItem> productCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
        boolean shippingOrderAlreadyExists = isShippingOrderExists(order);

        logger.debug("Trying to split order " + order.getId());

        Set<ShippingOrder> shippingOrders = order.getShippingOrders();
        User adminUser = getUserService().getAdminUser();

        if (shippingOrderAlreadyExists) {
            if (EnumOrderStatus.Placed.getId().equals(order.getOrderStatus().getId())) {
                order.setOrderStatus(EnumOrderStatus.InProcess.asOrderStatus());
                order = save(order);
            }
        } else {
            //DO Nothing for B2B Orders
            if (order.isB2bOrder() != null && order.isB2bOrder().equals(Boolean.TRUE)) {
                orderLoggingService.logOrderActivity(order, adminUser, orderLoggingService.getOrderLifecycleActivity(EnumOrderLifecycleActivity.OrderCouldNotBeAutoSplit), "Aboring Split for B2B Order");
            } else {
                shippingOrders = createShippingOrders(order);
            }
        }

	    //todo

        if (shippingOrders != null && shippingOrders.size() > 0) {
            if (!shippingOrderAlreadyExists) {
                // save order with InProcess status since shipping orders have been created
                order.setOrderStatus(getOrderStatusService().find(EnumOrderStatus.InProcess));
                order.setShippingOrders(shippingOrders);
                order = save(order);
                String comments = "No. of Shipping Orders created  " + shippingOrders.size();
                orderLoggingService.logOrderActivity(order, adminUser, orderLoggingService.getOrderLifecycleActivity(EnumOrderLifecycleActivity.OrderSplit), comments);
            }
            for (ShippingOrder shippingOrder : shippingOrders) {
                if (!shippingOrder.isDropShipping()) {
                    if (shippingOrder.getShipment() == null) {
                        shipmentService.createShipment(shippingOrder, true);
                    }
                } else {
                    shippingOrder.setDropShipping(true);
                    shippingOrder = shippingOrderService.save(shippingOrder);
                    shippingOrderService.logShippingOrderActivity(shippingOrder, adminUser, EnumShippingOrderLifecycleActivity.SO_ShipmentNotCreated.asShippingOrderLifecycleActivity(),
                            CourierConstants.DROP_SHIPPED_ORDER);
                }
            }
            // auto escalate shipping orders if possible  //seema
            if (EnumPaymentStatus.getEscalablePaymentStatusIds().contains(order.getPayment().getPaymentStatus().getId())) {
                for (ShippingOrder shippingOrder : shippingOrders) {
                    shippingOrderService.autoEscalateShippingOrder(shippingOrder);
                }
            }
            shippingOrderAlreadyExists = true;

        }
        // Check Inventory health of order lineitems
        for (CartLineItem cartLineItem : productCartLineItems) {
            inventoryService.checkInventoryHealth(cartLineItem.getProductVariant());
        }

        return shippingOrderAlreadyExists;
    }



	@Transactional
	public UserCodCall saveUserCodCall(UserCodCall userCodCall){
		return (UserCodCall)baseDao.save(userCodCall);
	}

	public UserCodCall createUserCodCall(Order order) {
		UserCodCall userCodCall = new UserCodCall();
		userCodCall.setBaseOrder(order);
		userCodCall.setRemark(" PENDING_WITH_THIRD_PARTY");
		userCodCall.setCallStatus(10);
		userCodCall.setCreateDate(new Date());
		return userCodCall;

	}

	public List<UserCodCall> getAllUserCodCallOfToday(){
	return 	orderDao.getAllUserCodCallOfToday();
	}

}