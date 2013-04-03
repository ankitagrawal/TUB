<%@ taglib prefix="shirp" uri="http://shiro.apache.org/tags" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Customer Support Admin Home">

    <s:layout-component name="heading">Category</s:layout-component>
    <s:layout-component name="content">
        <style type="text/css">
            .float {
                float:left;
                position:relative;
                height:200px;
            }
        </style>

        <div class="left roundBox">

            <h2>Basic Functionalities</h2>

            <h3><s:link
                    beanclass="com.hk.web.action.admin.order.search.SearchOrderAction">Search Base Orders</s:link></h3>

            <h3><s:link
                    beanclass="com.hk.web.action.admin.order.search.SearchShippingOrderAction">Search Shipping Orders</s:link></h3>

            <h3><s:link
                    beanclass="com.hk.web.action.admin.subscription.SearchSubscriptionAction">Search Subscriptions</s:link></h3>

            <h3><s:link beanclass="com.hk.web.action.admin.user.SearchUserAction">Search Users</s:link></h3>

            <h3>
                <s:link beanclass="com.hk.web.action.admin.payment.CheckPaymentAction" event="seekPayment">
                    Seek Payment
                </s:link></h3>

            <h3><s:link
                    beanclass="com.hk.web.action.admin.queue.ActionAwaitingQueueAction">Action Awaiting Queue</s:link></h3>

            <h3><s:link beanclass="com.hk.web.action.admin.marketing.NotifyMeListAction"> Notify Me List </s:link></h3>
        </div>

        <div class="cl"></div>

        <div class="float roundBox">
            <h2>Site Admin</h2>

            <h3><s:link
                    beanclass="com.hk.web.action.admin.user.PendingRewardPointQueueAction">Pending Reward Points</s:link></h3>

        </div>

        <div class="cl"></div>

        <div class="left roundBox">
            <h2>Marketing</h2>

            <h3><s:link beanclass="com.hk.web.action.admin.offer.SelectOfferAction">Edit Offer</s:link></h3>

            <h3><s:link beanclass="com.hk.web.action.admin.offer.EditCouponAction">Edit Coupons</s:link></h3>

        </div>

        <div class="cl"></div>

        <div class="left roundBox">
            <h2>Pincode / Couriers</h2>

            <h3>
                <s:link beanclass="com.hk.web.action.admin.courier.MasterPincodeAction">
                    Pincode Crud Operations
                </s:link>
            </h3>

            <h3>
                <s:link beanclass="com.hk.web.action.admin.courier.PincodeCourierMappingAction">
                    Pincode Courier Mapping Screen
                </s:link>
            </h3>

            <h3>
                <s:link beanclass="com.hk.web.action.admin.courier.ChangeDefaultCourierAction">
                    Pincode Default Courier
                </s:link>
            </h3>
        </div>

        <div class="cl"></div>

        <div class="left roundBox">
            <h2>Services</h2>

            <h3><s:link beanclass="com.hk.web.action.admin.queue.ServiceQueueAction">Service Queue</s:link></h3>

            <h3>
                <s:link beanclass="com.hk.web.action.admin.catalog.ManufacturerAction">View/Edit Merchant Details</s:link></h3>

        </div>

    </s:layout-component>
</s:layout-render>
