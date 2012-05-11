package com.hk.admin.impl.service.order;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hk.admin.pact.service.order.AdminOrderService;
import com.hk.admin.pact.service.shippingOrder.AdminShippingOrderService;
import com.hk.constants.order.EnumOrderLifecycleActivity;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.shippingOrder.EnumShippingOrderStatus;
import com.hk.core.fliter.ShippingOrderFilter;
import com.hk.domain.core.CancellationType;
import com.hk.domain.core.OrderLifecycleActivity;
import com.hk.domain.offer.rewardPoint.RewardPoint;
import com.hk.domain.order.Order;
import com.hk.domain.order.ShippingOrder;
import com.hk.domain.user.User;
import com.hk.manager.EmailManager;
import com.hk.manager.ReferrerProgramManager;
import com.hk.pact.dao.order.OrderDao;
import com.hk.pact.service.OrderStatusService;
import com.hk.pact.service.UserService;
import com.hk.pact.service.core.AffilateService;
import com.hk.pact.service.order.OrderService;
import com.hk.pact.service.order.RewardPointService;

@Service
public class AdminOrderServiceImpl implements AdminOrderService {

    
    private static Logger logger = LoggerFactory.getLogger(AdminOrderService.class);
    
    
    @Autowired
    private UserService               userService;
    @Autowired
    private OrderStatusService        orderStatusService;
    @Autowired
    private RewardPointService        rewardPointService;
    @Autowired
    private OrderService              orderService;
    @Autowired
    private AdminShippingOrderService adminShippingOrderService;
    @Autowired
    private AffilateService           affilateService;
    @Autowired
    private ReferrerProgramManager    referrerProgramManager;
    @Autowired
    private EmailManager              emailManager;

    @Autowired
    private OrderDao                  orderDao;

    @Transactional
    public Order putOrderOnHold(Order order) {

        ShippingOrderFilter shippingOrderFilter = new ShippingOrderFilter(order.getShippingOrders());
        Set<ShippingOrder> shippingOrdersToPutOnHold = shippingOrderFilter.filterShippingOrdersByStatus(EnumShippingOrderStatus.getStatusForPuttingOrderOnHold());

        for (ShippingOrder shippingOrder : shippingOrdersToPutOnHold) {
            getAdminShippingOrderService().putShippingOrderOnHold(shippingOrder);
        }
        order.setOrderStatus(getOrderService().getOrderStatus(EnumOrderStatus.OnHold));
        order = getOrderDao().save(order);

        /**
         * Order lifecycle activity logging - Order Put OnHold
         */
        logOrderActivity(order, EnumOrderLifecycleActivity.OrderPutOnHold);

        return order;
    }

    @Transactional
    public void cancelOrder(Order order, CancellationType cancellationType, String cancellationRemark, User loggedOnUser) {
        boolean shouldCancel = true;

        for (ShippingOrder shippingOrder : order.getShippingOrders()) {
          if (!EnumShippingOrderStatus.SO_ActionAwaiting.getId().equals(shippingOrder.getOrderStatus().getId())) {
            shouldCancel = false;
            break;
          }
        }

        if (shouldCancel) {
          order.setOrderStatus((getOrderStatusService().find(EnumOrderStatus.Cancelled)));
          order.setCancellationType(cancellationType);
          order.setCancellationRemark(cancellationRemark);
          order = getOrderService().save(order);

          Set<ShippingOrder> shippingOrders = order.getShippingOrders();
          if (shippingOrders != null) {
            for (ShippingOrder shippingOrder : order.getShippingOrders()) {
              getAdminShippingOrderService().cancelShippingOrder(shippingOrder);
            }
          }

          affilateService.cancelTxn(order);

          if (order.getRewardPointsUsed() != null && order.getRewardPointsUsed() > 0) {
            referrerProgramManager.refundRedeemedPoints(order);
          }

          List<RewardPoint> rewardPointList = getRewardPointService().findByReferredOrder(order);
          if (rewardPointList != null && rewardPointList.size() > 0) {
            for (RewardPoint rewardPoint : rewardPointList) {
              referrerProgramManager.cancelReferredOrderRewardPoint(rewardPoint);
            }
          }

          emailManager.sendOrderCancelEmailToUser(order);
          emailManager.sendOrderCancelEmailToAdmin(order);

          this.logOrderActivity(order, loggedOnUser, getOrderService().getOrderLifecycleActivity(EnumOrderLifecycleActivity.OrderCancelled), null);
        } else {
          String comment = "All SOs of BO#" + order.getGatewayOrderId() + " are not in Action Awaiting Status - Aborting Cancellation.";
          logger.info(comment);
          this.logOrderActivityByAdmin(order, EnumOrderLifecycleActivity.LoggedComment, comment);
        }
    }

    @Transactional
    public Order unHoldOrder(Order order) {

        ShippingOrderFilter shippingOrderFilter = new ShippingOrderFilter(order.getShippingOrders());
        Set<ShippingOrder> shippingOrdersToPutOnHold = shippingOrderFilter.filterShippingOrdersByStatus(Arrays.asList(EnumShippingOrderStatus.SO_OnHold));

        for (ShippingOrder shippingOrder : shippingOrdersToPutOnHold) {
            getAdminShippingOrderService().unHoldShippingOrder(shippingOrder);
        }

        Set<ShippingOrder> shippingOrders = order.getShippingOrders();
        if (shippingOrders != null && !shippingOrders.isEmpty()) {
            order.setOrderStatus(getOrderService().getOrderStatus(EnumOrderStatus.InProcess));
        } else {
            order.setOrderStatus(getOrderService().getOrderStatus(EnumOrderStatus.Placed));
        }

        order = getOrderDao().save(order);

        /**
         * Order lifecycle activity logging - Order Put OnHold
         */
        logOrderActivity(order, EnumOrderLifecycleActivity.OrderRemovedOnHold);

        return order;
    }

    public void logOrderActivity(Order order, EnumOrderLifecycleActivity enumOrderLifecycleActivity) {
        User user = userService.getLoggedInUser();
        OrderLifecycleActivity orderLifecycleActivity = getOrderService().getOrderLifecycleActivity(enumOrderLifecycleActivity);
        logOrderActivity(order, user, orderLifecycleActivity, null);
    }

    public void logOrderActivityByAdmin(Order order, EnumOrderLifecycleActivity enumOrderLifecycleActivity, String comments) {
        User user = userService.getAdminUser();
        OrderLifecycleActivity orderLifecycleActivity = getOrderService().getOrderLifecycleActivity(enumOrderLifecycleActivity);
        logOrderActivity(order, user, orderLifecycleActivity, comments);
    }

    @Override
    public void logOrderActivity(Order order, User user, OrderLifecycleActivity orderLifecycleActivity, String comments) {
        getOrderDao().logOrderActivity(order, user, orderLifecycleActivity, comments);
    }

    /**
     * if all shipping orders for a order are in shipped/delievered or escalted to packing queue status update base
     * order's status to statusToUpdate
     */
    @Transactional
    private boolean updateOrderStatusFromShippingOrders(Order order, EnumShippingOrderStatus soStatus, EnumOrderStatus boStatusOnSuccess) {

        boolean shouldUpdate = true;

        for (ShippingOrder shippingOrder : order.getShippingOrders()) {
            if (!soStatus.getId().equals(shippingOrder.getOrderStatus().getId())) {
                shouldUpdate = false;
                break;
            }
        }

        if (shouldUpdate) {
            order.setOrderStatus(getOrderStatusService().find(boStatusOnSuccess));
            order = getOrderDao().save(order);
        }
        /*
         * else { order.setOrderStatus(orderStatusDao.find(boStatusOnFailure.getId())); order =
         * orderDaoProvider.get().save(order); }
         */

        return shouldUpdate;
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
            rewardPointService.approvePendingRewardPointsForOrder(order);
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

    @Override
    public Order moveOrderBackToActionQueue(Order order, String shippingOrderGatewayId) {
        /*
         * order.setOrderStatus(orderStatusDao.find(EnumOrderStatus.ActionAwaiting.getId())); order =
         * orderDaoProvider.get().save(order);
         */

        OrderLifecycleActivity orderLifecycleActivity = getOrderService().getOrderLifecycleActivity(EnumOrderLifecycleActivity.EscalatedBackToAwaitingQueue);
        logOrderActivity(order, userService.getLoggedInUser(), orderLifecycleActivity, shippingOrderGatewayId + "escalated back to  action queue");

        return order;
    }

    public UserService getUserService() {
        return userService;
    }

    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    public OrderDao getOrderDao() {
        return orderDao;
    }

    public void setOrderDao(OrderDao orderDao) {
        this.orderDao = orderDao;
    }

    public OrderStatusService getOrderStatusService() {
        return orderStatusService;
    }

    public void setOrderStatusService(OrderStatusService orderStatusService) {
        this.orderStatusService = orderStatusService;
    }

    public OrderService getOrderService() {
        return orderService;
    }

    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    public RewardPointService getRewardPointService() {
        return rewardPointService;
    }

    public void setRewardPointService(RewardPointService rewardPointService) {
        this.rewardPointService = rewardPointService;
    }

    public AdminShippingOrderService getAdminShippingOrderService() {
        return adminShippingOrderService;
    }

    public void setAdminShippingOrderService(AdminShippingOrderService adminShippingOrderService) {
        this.adminShippingOrderService = adminShippingOrderService;
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

    public EmailManager getEmailManager() {
        return emailManager;
    }

    public void setEmailManager(EmailManager emailManager) {
        this.emailManager = emailManager;
    }

}
