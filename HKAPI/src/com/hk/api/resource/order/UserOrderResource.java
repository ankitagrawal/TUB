package com.hk.api.resource.order;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Encoded;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;

import com.hk.admin.pact.service.order.AdminOrderService;
import com.hk.api.constants.HKAPIConstants;
import com.hk.api.models.user.APIUserDetail;
import com.hk.constants.core.EnumCancellationType;
import com.hk.constants.core.EnumUserCodCalling;
import com.hk.constants.order.EnumOrderStatus;
import com.hk.constants.payment.EnumPaymentStatus;
import com.hk.constants.report.ReportConstants;
import com.hk.domain.user.UserCodCall;
import com.hk.impl.service.queue.BucketService;
import com.hk.pact.service.order.OrderService;
import net.sourceforge.stripes.util.CryptoUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

import com.hk.constants.core.Keys;
import com.hk.domain.clm.KarmaProfile;
import com.hk.domain.order.Order;
import com.hk.domain.user.User;
import com.hk.domain.user.UserDetail;
import com.hk.pact.service.UserService;
import com.hk.pact.service.clm.KarmaProfileService;
import com.hk.pact.service.order.UserOrderService;
import com.hk.pact.service.user.UserDetailService;

/**
 * Created with IntelliJ IDEA. User: Marut Date: 10/11/12 Time: 1:33 PM To change this template use File | Settings |
 * File Templates.
 */

@Path("/user")
@Component
public class UserOrderResource {

    private static Logger logger = LoggerFactory.getLogger(UserOrderResource.class);

    @Value("#{hkEnvProps['" + Keys.Env.hkApiAccessKey + "']}")
    private String API_KEY;

    @Autowired
    UserService userService;

    @Autowired
    KarmaProfileService karmaProfileService;

    @Autowired
    private UserOrderService userOrderService;

    @Autowired
    private UserDetailService userDetailService;

    @Autowired
    AdminOrderService adminOrderService;

    @Autowired
    OrderService orderService;

    @Autowired
    BucketService bucketService;

    @POST
    @Path("/email/{email}/phone/{phone}")
    @Produces("application/json")
    public Response updateUser(@PathParam("email")
                               String email, @PathParam("phone")
    long phone) {
        email = email.toLowerCase().trim();
        User user = userService.findByLogin(email);
        // User user = UserCache.getInstance().getUserByLogin(email).getUser();
        Response response = null;
        try {
            if (user == null) {
                response = Response.status(Response.Status.NOT_FOUND).build();
            } else {
                UserDetail userDetail = new UserDetail();
                userDetail.setPhone(phone);
                userDetail.setUser(user);
                userDetailService.save(userDetail);
                response = Response.status(Response.Status.OK).build();
            }
        } catch (Exception ex) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            logger.error("Unable to save User Details ", ex);
        }
        return response;
    }

    @GET
    @Path("/priority/{priority}")
    @Produces("application/json")
    public Response getUserListByPriority(@PathParam("priority")
                                          long priority) {

        Response response = null;
        try {
            List<UserDetail> userDetailList = userDetailService.getByPriority((int) priority);
            final GenericEntity<List<UserDetail>> entity = new GenericEntity<List<UserDetail>>(userDetailList) {
            };
            response = Response.status(Response.Status.OK).entity(entity).build();
        } catch (Exception ex) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            logger.error("Unable to get User Details ", ex);
        }
        return response;
    }

    @GET
    @Path("/priority/phone/{phone}")
    @Produces("application/json")
    @Encoded
    public Response getUserDetails(@PathParam("phone")
                                   String phone, @QueryParam("key")
    String key) {

        long phoneNo = 0L;
        phoneNo = Long.parseLong(phone);

        Response response = null;
        String decryptKey = CryptoUtil.decrypt(key);
        if ((decryptKey == null) || !decryptKey.trim().equals(API_KEY)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        try {
            List<UserDetail> userDetails = userDetailService.findByPhone(phoneNo);
            ArrayList<APIUserDetail> userDetailList = new ArrayList<APIUserDetail>();
            for (UserDetail userDetail : userDetails) {
                APIUserDetail apiUserDetail = new APIUserDetail();
                User user = userDetail.getUser();
                apiUserDetail.setId(user.getId());
                apiUserDetail.setPhone(userDetail.getPhone());
                KarmaProfile karmaProfile = karmaProfileService.findByUser(user);
                if (karmaProfile != null) {
                    apiUserDetail.setPriority(karmaProfile.getKarmaPoints() >= UserDetailService.MIN_KARMA_POINTS ? 1 : 0);
                }
                userDetailList.add(apiUserDetail);
            }
            if (userDetails != null && (userDetails.size() > 0)) {
                final GenericEntity<List<APIUserDetail>> entity = new GenericEntity<List<APIUserDetail>>(userDetailList) {
                };
                response = Response.status(Response.Status.OK).entity(entity).build();
            } else {
                response = Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (NumberFormatException ex) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            logger.error("Wrong input passed from Drishti ");
        } catch (Exception ex) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            logger.error("Unable to get User Details ", ex);
        }
        return response;
    }

    @POST
    @Path("/order/source/{source}/order/{orderId}/action/{action}")
    @Produces("application/json")
    @Encoded
    public Response changeOrderStatus(@PathParam("orderId") Long orderId,
                                      @PathParam("source") String source,
                                      @PathParam("action") String action,
                                      @QueryParam("authToken") String authToken
    ) {
        String key = authToken;
        Response response = null;
        User loggedInUser = null;
        loggedInUser = userService.getAdminUser();
        Order order = orderService.find(orderId);
        UserCodCall userCodCall = null;

        String decryptKey = CryptoUtil.decrypt(key);
        if ((decryptKey == null) || !decryptKey.trim().equals(API_KEY)) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        try {
            userCodCall = order.getUserCodCall();
            userCodCall.setCallStatus(EnumUserCodCalling.PENDING_WITH_KNOWLARITY.getId());
            userCodCall.setRemark(source);

            if (!(order.isCOD())) {
                logger.debug("Order is not COD" + order.getId());
                return Response.status(Response.Status.BAD_REQUEST).build();
            }

            if (action.equalsIgnoreCase(HKAPIConstants.CANCELLED)) {
                if (order.getOrderStatus().getId().equals(EnumOrderStatus.Cancelled.getId())) {
                    logger.debug("Order Already Cancelled" + order.getId());
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
                adminOrderService.cancelOrder(order, EnumCancellationType.Customer_Not_Interested.asCancellationType(), source, loggedInUser);
                userCodCall.setRemark("Cancelled By " + source);
                userCodCall.setCallStatus(EnumUserCodCalling.valueOf(action).getId());
            } else if (action.equalsIgnoreCase(HKAPIConstants.CONFIRMED)) {
                List<Long> paymentStatusListForSuccessfulOrder = EnumPaymentStatus.getEscalablePaymentStatusIds();
                if (order.getPayment() != null && (paymentStatusListForSuccessfulOrder.contains(order.getPayment().getPaymentStatus().getId()))) {
                    logger.debug("Order Payment Already Confirmed" + order.getId());
                    return Response.status(Response.Status.BAD_REQUEST).build();
                }
                adminOrderService.confirmCodOrder(order, source, null);
                userCodCall.setRemark("Confirmed By " + source);
                userCodCall.setCallStatus(EnumUserCodCalling.valueOf(action).getId());
            } else if (action.equalsIgnoreCase(HKAPIConstants.NON_CONTACTABLE)) {
                userCodCall.setRemark(EnumUserCodCalling.PENDING_WITH_EFFORT_BPO.getName());
                userCodCall.setCallStatus(EnumUserCodCalling.PENDING_WITH_EFFORT_BPO.getId());
            } else if (action.equalsIgnoreCase(HKAPIConstants.HEALTHKART)) {
                userCodCall.setRemark(source);
                userCodCall.setCallStatus(EnumUserCodCalling.PENDING_WITH_HEALTHKART.getId());
            }
            userCodCall = orderService.saveUserCodCall(userCodCall);
            bucketService.confirmCOD(userCodCall.getBaseOrder());
            return Response.status(Response.Status.OK).build();
        } catch (DataIntegrityViolationException dataInt) {
            logger.error("Exception in  inserting  Duplicate UserCodCall in Updating COD status: " + dataInt.getMessage());
        } catch (Exception ex) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            logger.error("Unable to change order status ", ex);
            userCodCall.setRemark(action + " Request From Admin Failed..");
            try {
                orderService.saveUserCodCall(userCodCall);
            } catch (DataIntegrityViolationException dataInt) {
                logger.error("Exception in  inserting  Duplicate UserCodCall in Updating COD status in try catch block: " + dataInt.getMessage());
            } catch (Exception exp) {
                logger.error("Unable to save user_cod record..", exp);
            }
        }
        return response;
    }

    /**
     * internal class just for helping with JSON Marshalling
     */
    class APIOrderDetail {
        public Long id;
        public String status;
        public String gatewayOrderId;
        public String createDate;
    }

    @GET
    @Path("/orders/phone/{phone}")
    @Produces("application/json")
    public Response getUserOrders(@PathParam("phone")
                                  long phone) {

        Response response = null;
        try {
            if (userDetailService.findByPhone((int) phone) == null) {
                response = Response.status(Response.Status.NOT_FOUND).build();
            } else {
                List<Order> orders = userOrderService.getUserOrders(phone);
                final GenericEntity<List<APIOrderDetail>> entity = new GenericEntity<List<APIOrderDetail>>(getOrderDetailList(orders)) {
                };
                response = Response.status(Response.Status.OK).entity(entity).build();
            }

        } catch (Exception ex) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            logger.error("Unable to get User Orders ", ex);
        }
        return response;
    }

    @GET
    @Path("/orders/email/{email}")
    @Produces("application/json")
    public Response getUserOrders(@PathParam("email")
                                  String email) {

        Response response = null;
        try {
            List<Order> orders = userOrderService.getUserOrders(email);
            final GenericEntity<List<APIOrderDetail>> entity = new GenericEntity<List<APIOrderDetail>>(getOrderDetailList(orders)) {
            };
            response = Response.status(Response.Status.OK).entity(entity).build();
        } catch (Exception ex) {
            response = Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
            logger.error("Unable to get User Orders ", ex);
        }
        return response;
    }

    private List<APIOrderDetail> getOrderDetailList(List<Order> orders) {
        List<APIOrderDetail> orderDetails = new ArrayList<APIOrderDetail>();
        for (Order order : orders) {
            APIOrderDetail orderDetail = new APIOrderDetail();
            orderDetail.id = order.getId();
            orderDetail.status = order.getOrderStatus().getName();
            orderDetail.gatewayOrderId = order.getGatewayOrderId();
            orderDetail.createDate = order.getCreateDate().toString();
            orderDetails.add(orderDetail);
        }
        return orderDetails;
    }
}
