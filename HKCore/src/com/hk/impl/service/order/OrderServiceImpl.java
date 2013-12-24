package com.hk.impl.service.order;

import com.akube.framework.dao.Page;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hk.cache.CategoryCache;
import com.hk.comparator.BasketCategory;
import com.hk.constants.analytics.EnumReason;
import com.hk.constants.catalog.category.CategoryConstants;
import com.hk.constants.core.EnumUserCodCalling;
import com.hk.constants.core.Keys;
import com.hk.constants.order.EnumCartLineItemType;
import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.order.EnumUnitProcessedStatus;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.fliter.CartLineItemFilter;
import com.hk.core.fliter.OrderSplitterFilter;
import com.hk.core.search.OrderSearchCriteria;
import com.hk.domain.api.HKAPIBookingInfo;
import com.hk.domain.api.HKAPIForeignBookingResponseInfo;
import com.hk.domain.catalog.category.Category;
import com.hk.domain.catalog.product.ProductVariant;
import com.hk.domain.core.OrderLifecycleActivity;
import com.hk.domain.core.OrderStatus;
import com.hk.domain.order.*;
import com.hk.domain.shippingOrder.LineItem;
import com.hk.domain.shippingOrder.ShippingOrderCategory;
import com.hk.domain.sku.ForeignSkuItemCLI;
import com.hk.domain.sku.Sku;
import com.hk.domain.store.Store;
import com.hk.domain.user.User;
import com.hk.domain.user.UserCodCall;
import com.hk.domain.warehouse.Warehouse;
import com.hk.exception.NoSkuException;
import com.hk.exception.OrderSplitException;
import com.hk.helper.LineItemHelper;
import com.hk.helper.OrderDateUtil;
import com.hk.helper.ShippingOrderHelper;
import com.hk.impl.service.queue.BucketService;
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
import com.hk.pact.service.inventory.SkuItemLineItemService;
import com.hk.pact.service.inventory.SkuService;
import com.hk.pact.service.order.OrderLoggingService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.order.OrderSplitterService;
import com.hk.pact.service.order.RewardPointService;
import com.hk.pact.service.shippingOrder.ShipmentService;
import com.hk.pact.service.shippingOrder.ShippingOrderService;
import com.hk.pact.service.shippingOrder.ShippingOrderStatusService;
import com.hk.pact.service.splitter.OrderSplitter;
import com.hk.pact.service.splitter.ShippingOrderProcessor;
import com.hk.pact.service.subscription.SubscriptionService;
import com.hk.pojo.DummyOrder;
import com.hk.util.HKDateUtil;
import com.hk.util.OrderUtil;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.lang.reflect.Type;
import java.security.InvalidParameterException;
import java.util.*;

@Service
public class OrderServiceImpl implements OrderService {

    private static Logger logger = LoggerFactory.getLogger(OrderService.class);
    
    @Value("#{hkEnvProps['" + Keys.Env.brightlifecareRestUrl + "']}")
    private String brightlifecareRestUrl;
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
    private OrderLoggingService orderLoggingService;
    @Autowired
    private OrderSplitterService orderSplitterService;
    @Autowired
    private ShippingOrderStatusService shippingOrderStatusService;
    @Autowired
    LineItemDao lineItemDao;
    @Autowired
    ShipmentService shipmentService;
    @Autowired
    BucketService bucketService;
    @Autowired
    SubscriptionService subscriptionService;
    @Autowired
    SkuItemLineItemService skuItemLineItemService;

    @Autowired OrderSplitter orderSplitter;

    @Autowired ShippingOrderProcessor shippingOrderProcessor;
    
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

      public Long getCountOfOrdersByStatus(User user, EnumOrderStatus enumOrderStatus) {
        return getOrderDao().getCountOfOrdersWithStatus( user, enumOrderStatus);
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

    /**
     * this will return the dispatch date for BO by adding min of dispatch days to refdate honouring the constraints of
     * warehouse like last time a order will be processed in WH each day (say till 4pm),
     *
     * @param order
     * @return
     */
    //this has been replaced by setTargetDispatchDate
    @Transactional
    @Deprecated
    private void setTargetDispatchDelDatesOnBO(Order order) {
        Long[] dispatchDays = OrderUtil.getDispatchDaysForBO(order);
        Date refDateForBO = order.getPayment().getPaymentDate();
        Date refDateForSO = null;

        if (order.getTargetDispatchDate() == null) {
            Date targetDispatchDate = OrderDateUtil.getTargetDispatchDateForWH(refDateForBO, dispatchDays[0]);
            order.setTargetDispatchDate(targetDispatchDate);
//            order.setTargetDispatchDateOnVerification(targetDispatchDate);
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
//            order.setTargetDispatchDateOnVerification(targetDispatchDateOnVerification);
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

    private void setTargetDatesOnBO(Order order) {
        Date maxSOTargetDispatchDate = new Date();
        for (ShippingOrder shippingOrder : order.getShippingOrders()) {
             if(maxSOTargetDispatchDate.getTime() <= shippingOrder.getTargetDispatchDate().getTime()){
                 maxSOTargetDispatchDate = shippingOrder.getTargetDispatchDate();
             }
        }
        order.setTargetDispatchDate(maxSOTargetDispatchDate);
//        Date orderTargetDeliveryDate = HKDateUtil.addToDate(maxSOTargetDispatchDate, Calendar.DAY_OF_MONTH, 3);
        order.setTargetDelDate(maxSOTargetDispatchDate);
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

    public Set<ShippingOrderCategory> getCategoriesForShippingOrder(ShippingOrder shippingOrder) {

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

        Set<ShippingOrderCategory> shippingOrderCategories = new HashSet<ShippingOrderCategory>();
        boolean primaryCategory = true;

        for (BasketCategory basketCategory : basketCategories) {
            ShippingOrderCategory shippingOrderCategory = new ShippingOrderCategory();
            shippingOrderCategory.setShippingOrder(shippingOrder);
            shippingOrderCategory.setCategory(basketCategory.getCategory());
            if (primaryCategory) {
                shippingOrderCategory.setPrimary(true);
                primaryCategory = false;
            }
            shippingOrderCategory = (ShippingOrderCategory) getBaseDao().save(shippingOrderCategory);
            shippingOrderCategories.add(shippingOrderCategory);
        }

        return shippingOrderCategories;
    }


    public Category getBasketCategory(Set<ShippingOrderCategory> shippingOrderCategories) {
        for (ShippingOrderCategory shippingOrderCategory : shippingOrderCategories) {
            if (shippingOrderCategory.isPrimary()) {
                return shippingOrderCategory.getCategory();
            }
        }
        return shippingOrderCategories.iterator().next().getCategory();
    }

    private Set<ShippingOrder> createShippingOrders(Order order) {
        Set<ShippingOrder> shippingOrders = new HashSet<ShippingOrder>();
        try {
            shippingOrders = orderSplitter.split(order.getId());
        } catch (NoSkuException e) {
          logger.error("Sku could not be found" + e.getMessage());
          orderLoggingService.logOrderActivity(order, getUserService().getAdminUser(), orderLoggingService.getOrderLifecycleActivity(EnumOrderLifecycleActivity.OrderCouldNotBeAutoSplit), e.getMessage());
        } catch (OrderSplitException e) {
          logger.error(e.getMessage());
          orderLoggingService.logOrderActivity(order, getUserService().getAdminUser(), orderLoggingService.getOrderLifecycleActivity(EnumOrderLifecycleActivity.OrderCouldNotBeAutoSplit), e.getMessage());
        } catch (Exception e) {
          logger.error("Order could not be split due to some exception ", e);
          orderLoggingService.logOrderActivity(order, getUserService().getAdminUser(), orderLoggingService.getOrderLifecycleActivity(EnumOrderLifecycleActivity.OrderCouldNotBeAutoSplit), e.getMessage());
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
       boolean orderMarkedDeliverd=false;

        for (ShippingOrder shippingOrder : order.getShippingOrders()) {
            if (!getShippingOrderService().shippingOrderHasReplacementOrder(shippingOrder)) {
                if (!soStatus.getId().equals(shippingOrder.getOrderStatus().getId())) {
                    shouldUpdate = false;
                  if(shippingOrder.getOrderStatus().getId().equals(EnumShippingOrderStatus.SO_Delivered.getId())) {
                       orderMarkedDeliverd=true;
                  }
                 else {
                    break;
                  }
                }
            }
        }

        if (shouldUpdate) {
            order.setOrderStatus(getOrderStatus(boStatusOnSuccess));
            order = getOrderDao().save(order);
        }
        if(!shouldUpdate && orderMarkedDeliverd){
           order.setOrderStatus(EnumOrderStatus.Delivered.asOrderStatus());
          order = getOrderDao().save(order);
        }
        /*
         * else { order.setOrderStatus(orderStatusDao.find(boStatusOnFailure.getId())); order =
         * getOrderDao().save(order); }
         */

        return shouldUpdate;
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
        Map<String, List<CartLineItem>> bucketCartLineItems = OrderSplitterFilter.classifyOrder(order);
        Set<ShippingOrder> shippingOrders = new HashSet<ShippingOrder>();
        for (Map.Entry<String, List<CartLineItem>> bucketCartLineItemMap : bucketCartLineItems.entrySet()) {
            logger.debug("bucketedCartLineItemMapEntry Key " + bucketCartLineItemMap.getKey() + " Size " + bucketCartLineItemMap.getValue().size());
            Set<CartLineItem> cartLineItems = new HashSet<CartLineItem>(bucketCartLineItemMap.getValue());
            if (order.isB2bOrder()) {
            } else {
                if (!cartLineItems.isEmpty() && !bucketCartLineItemMap.getKey().equals("Service")) {
                    List<DummyOrder> dummyOrders = orderSplitterService.listBestDummyOrdersPractically(order, cartLineItems);
                    if (EnumOrderStatus.Placed.getId().equals(order.getOrderStatus().getId())) {
                        long startTime = (new Date()).getTime();
                        // Create Shipping orders and Save it in DB
                        for (DummyOrder dummyOrder : dummyOrders) {
                            if (dummyOrder.getCartLineItemList().size() > 0) {
                                Warehouse warehouse = dummyOrder.getWarehouse();
                                Map<String, List<CartLineItem>> bucketedCartLineItemMap = OrderSplitterFilter.bucketCartLineItems(dummyOrder.getCartLineItemList());
                                logger.debug("bucketedCartLineItemMap Size " + bucketedCartLineItemMap.size());
                                for (Map.Entry<String, List<CartLineItem>> bucketedCartLineItemMapEntry : bucketedCartLineItemMap.entrySet()) {
                                    logger.debug("bucketedCartLineItemMapEntry Key " + bucketedCartLineItemMapEntry.getKey() + " Size " + bucketedCartLineItemMapEntry.getValue().size());
                                    ShippingOrder shippingOrder = shippingOrderService.createSOWithBasicDetails(order, warehouse);
                                    boolean isDropShipped = false;
                                    boolean containsJitProducts = false;
                                    for (CartLineItem cartLineItem : bucketedCartLineItemMapEntry.getValue()) {
                                        isDropShipped = cartLineItem.getProductVariant().getProduct().isDropShipping();
                                        containsJitProducts = cartLineItem.getProductVariant().getProduct().isJit();
                                        Sku sku = skuService.getSKU(cartLineItem.getProductVariant(), warehouse);
                                        LineItem shippingOrderLineItem = LineItemHelper.createLineItemWithBasicDetails(sku, shippingOrder, cartLineItem);
                                        shippingOrder.getLineItems().add(shippingOrderLineItem);
                                    }
                                    shippingOrder.setDropShipping(isDropShipped);
                                    shippingOrder.setContainsJitProducts(containsJitProducts);
                                    ShippingOrderHelper.updateAccountingOnSOLineItems(shippingOrder, order);
                                    shippingOrder.setAmount(ShippingOrderHelper.getAmountForSO(shippingOrder));
                                    shippingOrder = shippingOrderService.save(shippingOrder);
                                    shippingOrder = shippingOrderService.setGatewayIdAndTargetDateOnShippingOrder(shippingOrder);
                                    shippingOrder = shippingOrderService.save(shippingOrder);
                                    Set<ShippingOrderCategory> categories = getCategoriesForShippingOrder(shippingOrder);
                                    shippingOrder.setShippingOrderCategories(categories);
                                    shippingOrder.setBasketCategory(getBasketCategory(categories).getName());
                                    shippingOrder = shippingOrderService.save(shippingOrder);
                                    shippingOrders.add(shippingOrder);
                                }
                            }
                        }
                        long endTime = (new Date()).getTime();
                        logger.debug("Total time to split order[" + order.getId() + "] = " + (endTime - startTime));
                    } else {
                        logger.debug("order with gatewayId:" + order.getGatewayOrderId() + " is not in placed status. abort system split and do a manual split");
                    }
                } else {
                    for (CartLineItem serviceCartLineItem : cartLineItems) {
                        shippingOrders.add(createSOForService(serviceCartLineItem));
                    }
                }
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
        shippingOrder.setShippingOrderCategories(getCategoriesForShippingOrder(shippingOrder));
        /**
         * this additional call to save is done so that we have shipping order id to generate shipping order gateway id
         */
        shippingOrder = shippingOrderService.setGatewayIdAndTargetDateOnShippingOrder(shippingOrder);
        shippingOrder = getShippingOrderService().save(shippingOrder);

        return shippingOrder;

    }

    public ShippingOrderStatusService getShippingOrderStatusService() {
        return shippingOrderStatusService;
    }

    public void setShippingOrderStatusService(ShippingOrderStatusService shippingOrderStatusService) {
        this.shippingOrderStatusService = shippingOrderStatusService;
    }


    @Override
    @Transactional
    public boolean splitBOCreateShipmentEscalateSOAndRelatedTasks(Order order) {
    	order = this.find(order.getId());
    	logger.info("SPLIT START ORDER-ID: " + order.getId() + " ORDER STATUS: " + order.getOrderStatus().getName());
        Set<CartLineItem> productCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
        boolean shippingOrderAlreadyExists = false;
        Set<ShippingOrder> shippingOrders = order.getShippingOrders();
        if(!shippingOrders.isEmpty()) {
            shippingOrderAlreadyExists = true;
        }

        logger.debug("Trying to split order " + order.getId());

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
                if (EnumOrderStatus.Placed.getId().equals(order.getOrderStatus().getId())) {
                    shippingOrders = createShippingOrders(order);
                }
            }
        }
        logger.debug("Number of shipping order created is --" + shippingOrders.size());
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
                    getShippingOrderService().logShippingOrderActivityByAdmin(shippingOrder, EnumShippingOrderLifecycleActivity.SO_ShipmentNotCreated,
                            EnumReason.DROP_SHIPPED_ORDER.asReason());
                }
//                if (!(shippingOrder instanceof ReplacementOrder)){
//                    shippingOrderService.validateShippingOrder(shippingOrder);
//                }
            }


          logger.debug("post split will create sku item line item");
          for (ShippingOrder shippingOrder : order.getShippingOrders()){
            //Check to ignore old orders whose status is greater equal than 180
            if(shippingOrder.getShippingOrderStatus().getId() >= EnumShippingOrderStatus.SO_Shipped.getId()){
              continue;
            }
            for (LineItem lineItem : shippingOrder.getLineItems()){
              lineItemDao.refresh(lineItem);
              CartLineItem cartLineItem = lineItem.getCartLineItem();
              // manual split
              if(bookedOnBright(cartLineItem) && (lineItem.getSkuItemLineItems() == null  || lineItem.getSkuItemLineItems().size() < 1)){
                logger.debug("Update booking on Bright after splitting");
                Long   warehousIdForAqua = lineItem.getSku().getWarehouse().getId();
                Warehouse warehouse = warehouseService.getWarehouseById(warehousIdForAqua);

                // writing code for booking type


                List<HKAPIForeignBookingResponseInfo>  infos =   updateBookedInventoryOnBright(lineItem,warehouse.getFulfilmentCenterCode());
                List<ForeignSkuItemCLI> ForeignSkuItemCLIs =skuItemLineItemService.updateSkuItemForABJit(infos);
                skuItemLineItemService.populateSILIForABJit(ForeignSkuItemCLIs, lineItem) ;

              }
              else
              if(lineItem.getSkuItemLineItems() == null || lineItem.getSkuItemLineItems().size() == 0){
                Boolean skuItemLineItemStatus = skuItemLineItemService.createNewSkuItemLineItem(lineItem);
                if(!skuItemLineItemStatus){
                  shippingOrderService.logShippingOrderActivity(shippingOrder, EnumShippingOrderLifecycleActivity.SO_LoggedComment, null, "No Entry in sku_item_cart_line_item");
                }
              }
            }
          }
            // auto escalate shipping orders if possible
             getBaseDao().refresh(order);
            if (EnumPaymentStatus.getEscalablePaymentStatusIds().contains(order.getPayment().getPaymentStatus().getId())) {
              logger.debug("Shipping Orders size --" + shippingOrders.size());
                for (ShippingOrder shippingOrder : order.getShippingOrders()) {
                	shippingOrderProcessor.autoEscalateShippingOrder(shippingOrder, true);
                }
            }

            for (ShippingOrder shippingOrder : shippingOrders) {
                Date confirmationDate = order.getConfirmationDate() != null ? order.getConfirmationDate() : order.getPayment().getPaymentDate();

                //auto allocate buckets, based on business use case
                if(EnumShippingOrderStatus.getStatusIdsForActionQueue().contains(shippingOrder.getOrderStatus().getId())){
                    bucketService.autoCreateUpdateActionItem(shippingOrder);
                } else {
                    bucketService.popFromActionQueue(shippingOrder);
                }

                getShippingOrderService().setTargetDispatchDelDatesOnSO(confirmationDate, shippingOrder);
            }

            subscriptionService.placeSubscriptions(order);
            setTargetDatesOnBO(order);
            shippingOrderAlreadyExists = true;
        }


        // Check Inventory health of order lineItems
        for (CartLineItem cartLineItem : productCartLineItems) {
            inventoryService.checkInventoryHealth(cartLineItem.getProductVariant());
        }
        
        logger.info("SPLIT END ORDER-ID: " + order.getId());

        //create sku_item_line_items for each item of each shipping order of base order


        return shippingOrderAlreadyExists;
    }

	public  boolean bookedOnBright(CartLineItem cartLineItem) {
		try {

			String url = brightlifecareRestUrl + "product/variant/getBookingForCartLineItemId/" + cartLineItem.getId();
			ClientRequest request = new ClientRequest(url);
			ClientResponse response = request.get();
			int status = response.getStatus();
			if (status == 200) {
				String data = (String) response.getEntity(String.class);
				Boolean bookedAtBright = new Gson().fromJson(data, Boolean.class);
				return bookedAtBright;
			}
		} catch (Exception e) {
			logger.error("Exception while checking booking status on Bright", e.getMessage());
		}
		return false;
	}

  public List<HKAPIForeignBookingResponseInfo>  updateBookedInventoryOnBright(LineItem lineItem, String  fulfilmentCenterCode) {
    logger.debug(" Going to book inventory at Bright for lineItem --" + lineItem.getId());
    User loggedOnUser = userService.getLoggedInUser();
    List<HKAPIForeignBookingResponseInfo> infos = null;
    List<HKAPIForeignBookingResponseInfo> infos1 = new ArrayList<HKAPIForeignBookingResponseInfo>();
    // getOrderBooking Type
    Long bookingTypeId = getShippingOrderService().getShippingOrderBookingType(lineItem.getShippingOrder());
    try {
      List<ForeignSkuItemCLI> foreignSkuItemCLIs = lineItem.getCartLineItem().getForeignSkuItemCLIs();
      for (ForeignSkuItemCLI foreignSkuItemCLI : foreignSkuItemCLIs) {
        HKAPIBookingInfo hkapiBookingInfo = new HKAPIBookingInfo();
        hkapiBookingInfo.setMrp(lineItem.getSku().getProductVariant().getMarkedPrice());
        hkapiBookingInfo.setPvId(lineItem.getSku().getProductVariant().getId());
       // hkapiBookingInfo.setWhId(warehouseId);
        hkapiBookingInfo.setSoId(lineItem.getShippingOrder().getId());
        hkapiBookingInfo.setBoId(lineItem.getCartLineItem().getId());
        hkapiBookingInfo.setCliId(lineItem.getCartLineItem().getId());
        hkapiBookingInfo.setFsiCLIId(foreignSkuItemCLI.getId());
        hkapiBookingInfo.setFcCode(fulfilmentCenterCode);
        hkapiBookingInfo.setBkTyId(bookingTypeId);

        foreignSkuItemCLI.setShippingOrderBookingTypeId(bookingTypeId);
        getBaseDao().save(foreignSkuItemCLI);

        Gson gson = new Gson();
        String json = gson.toJson(Arrays.asList(hkapiBookingInfo));

        String url = brightlifecareRestUrl + "product/variant/" + "bookInventoryOnBright/";
        ClientRequest request = new ClientRequest(url);
        request.body("application/json", json);
        ClientResponse response = request.post();
        int status = response.getStatus();
        if (status == 200) {
          logger.debug(" got Success response during bright inv  booking for lineitem  " + lineItem.getId());
          String data = (String) response.getEntity(String.class);
          Type listType = new TypeToken<List<HKAPIForeignBookingResponseInfo>>() {
          }.getType();
          infos = new Gson().fromJson(data, listType);
          infos1.add(infos.get(0));
          orderLoggingService.logOrderActivity(lineItem.getCartLineItem().getOrder(), loggedOnUser,
							EnumOrderLifecycleActivity.ABCommunicationTempBookingSuccess.asOrderLifecycleActivity(),
							"Inventory successfully temp booked on Bright for variant:- " + lineItem.getCartLineItem().getProductVariant());
          shippingOrderService.logShippingOrderActivity(lineItem.getShippingOrder(), EnumShippingOrderLifecycleActivity.ABCommunicationBookingSuccess, null,
  						"Inventory successfully booked on Bright for variant:- "
  								+ lineItem.getSku().getProductVariant());
        }
        else{
        	orderLoggingService.logOrderActivity(lineItem.getCartLineItem().getOrder(), loggedOnUser, EnumOrderLifecycleActivity.ABCommunicationTempBookingFailure.asOrderLifecycleActivity(), "Failed to Temp Book Inventory on Bright"
							+ lineItem.getCartLineItem().getProductVariant());
        	shippingOrderService.logShippingOrderActivity(lineItem.getShippingOrder(), EnumShippingOrderLifecycleActivity.ABCommunicationBookingFailure, null,
  						"Failed to Book Inventory on Bright fpr variant:-"+ lineItem.getSku().getProductVariant());
        }
      }
    } catch (Exception e) {
    	shippingOrderService.logShippingOrderActivity(lineItem.getShippingOrder(), EnumShippingOrderLifecycleActivity.ABCommunicationBookingSuccess, null,
					"Exception while booking/updating Bright Inventory"
							+ lineItem.getSku().getProductVariant());
      logger.error("Exception while booking/updating Bright Inventory against BO# " + lineItem.getCartLineItem().getOrder().getId(), e);
      
    }
    logger.debug(" Infos got from bright for booking inv for lineItem" + lineItem.getId() + " infos --" + infos1);
    return infos1;

  }

	@Transactional
	public UserCodCall saveUserCodCall(UserCodCall userCodCall){
		return (UserCodCall)baseDao.save(userCodCall);
	}

	public UserCodCall createUserCodCall(Order order , EnumUserCodCalling enumUserCodCalling) {
		UserCodCall userCodCall = new UserCodCall();
		userCodCall.setBaseOrder(order);
		userCodCall.setRemark(enumUserCodCalling.getName());
		userCodCall.setCallStatus(enumUserCodCalling.getId());
		userCodCall.setCreateDate(new Date());
		return userCodCall;

	}

	public List<UserCodCall> getAllUserCodCallForToday(){
	return 	orderDao.getAllUserCodCallOfToday();
	}
	
	@Override
	public Order findCart(User user, Store store) {
		DetachedCriteria criteria = DetachedCriteria.forClass(Order.class);
		criteria.add(Restrictions.eq("user.id", user.getId()));
		criteria.add(Restrictions.eq("orderStatus.id", EnumOrderStatus.InCart.getId()));
		criteria.add(Restrictions.eq("store.id", store.getId()));

		@SuppressWarnings("unchecked")
		List<Order> orders = this.baseDao.findByCriteria(criteria);
		if(orders != null && orders.size() > 0) {
			return orders.iterator().next();
		}
		return null;
	}

    @Override
    public boolean isBOCancelable(Long orderId) {
        boolean isBOCancelable;
        int count=0;
        Order order = this.find(orderId);
        Set<ShippingOrder> shippingOrders = order.getShippingOrders();
        for (ShippingOrder shippingOrder : shippingOrders) {
            if (EnumShippingOrderStatus.SO_ActionAwaiting.getId().equals(shippingOrder.getShippingOrderStatus().getId())) {
                count++;
            }
        }
        if (count != shippingOrders.size()) {
            isBOCancelable = false;
        } else {
            isBOCancelable = true;
        }
        return isBOCancelable;
    }

  public int getOrderCountByUser(Long userId){
     if(userId == null){
       throw new InvalidParameterException("INVALID USER ID");
     }
    return getOrderDao().getOrderCountByUser(userId);
  }
  
  @Override
	public void updatePaymentStatusForBrightBooking(Order order) {
		List<ForeignSkuItemCLI> foreignSkuItemCLIList = new ArrayList<ForeignSkuItemCLI>();
		Set<CartLineItem> productCartLineItems = new CartLineItemFilter(order.getCartLineItems()).addCartLineItemType(EnumCartLineItemType.Product).filter();
		for (CartLineItem cartLineItem : productCartLineItems) {
			List<ForeignSkuItemCLI> foreignSkuItemCLIs = cartLineItem.getForeignSkuItemCLIs();
			if (foreignSkuItemCLIs != null && foreignSkuItemCLIs.size() > 0) {
				for (ForeignSkuItemCLI foreignSkuItemCLI : foreignSkuItemCLIs) {
					if (foreignSkuItemCLI.getProcessedStatus().equals(EnumUnitProcessedStatus.AUTHORIZATION_PENDING.getName())) {
						foreignSkuItemCLIList.add(foreignSkuItemCLI);
					}
				}
			}
		}
		if (foreignSkuItemCLIList != null && foreignSkuItemCLIList.size() > 0) {
			boolean updated = updatePaymentStatusForBookingOnBright(foreignSkuItemCLIList);
			if (updated) {
				for (ForeignSkuItemCLI foreignSkuItemCLI : foreignSkuItemCLIList) {
					foreignSkuItemCLI.setProcessedStatus(EnumUnitProcessedStatus.UNPROCESSED.getId());
					getBaseDao().save(foreignSkuItemCLI);
				}
				logger.debug("(Bright Booking) Payment status updated from Auth Pending, cart line item of order -" + order.getId());
			} else {
				logger.debug("(Bright Booking) Could not update payment status, cart line item Id -" + order.getId());
			}
		}
	}
  
	@SuppressWarnings("unchecked")
	private boolean updatePaymentStatusForBookingOnBright(List<ForeignSkuItemCLI> foreignSkuItemCLIs) {
		if (foreignSkuItemCLIs != null && foreignSkuItemCLIs.size() > 0) {
			try {
				List<Long> fsicliIds = new ArrayList<Long>();
				for (ForeignSkuItemCLI foreignSkuItemCLI : foreignSkuItemCLIs) {
					fsicliIds.add(foreignSkuItemCLI.getId());
				}
				String url = brightlifecareRestUrl + "product/variant/updatePaymentStatusForBookingOnBright/";
				Gson gson = new Gson();
				String json = gson.toJson(Arrays.asList(foreignSkuItemCLIs));
				ClientRequest request = new ClientRequest(url);
				request.body("application/json", json);
				ClientResponse response = request.post();
				int status = response.getStatus();
				if (status == 200) {
					String data = (String) response.getEntity(String.class);
					Boolean bookedAtBright = new Gson().fromJson(data, Boolean.class);
					return bookedAtBright;
				}
			} catch (Exception e) {
				logger.error("Exception while checking booking status on Bright", e.getMessage());
			}
		}
		return false;
	}
}