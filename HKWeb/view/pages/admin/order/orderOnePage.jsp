<%@ page import="com.hk.constants.order.EnumOrderStatus" %>
<%@ page import="com.hk.constants.shippingOrder.EnumShippingOrderStatus" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.crm.OrderJanamPatniAction" var="ojpa"/>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title>Order Ki Janampatni</title>
    <link href="${pageContext.request.contextPath}/css/newcss/grid.css" rel="stylesheet" type="text/css"/>
    <link href="${pageContext.request.contextPath}/css/newcss/style.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<c:set var="shippingOrderStatusActionAwaiting" value="<%=EnumShippingOrderStatus.SO_ActionAwaiting.getId()%>"/>
<c:set var="orderStatusCancelled" value="<%=EnumOrderStatus.Cancelled.getId()%>"/>
<c:set var="order" value="${ojpa.order}"/>
<c:set var="shippingOrders" value="${ojpa.order.shippingOrders}"/>
<c:set var="payment" value="${ojpa.order.payment}"/>
<c:set var="user" value="${ojpa.order.user}"/>
<c:set var="address" value="${ojpa.order.address}"/>


<div class="wrapper">
<div id="header">
    <div class="container_16 clearfix">
        <div class="grid_5">
            <h5>${order.gatewayOrderId}</h5>
            <h4>${payment.amount}</h4>
        </div>
        <div class="grid_4">
            <h6>Order Status : ${order.orderStatus.name}</h6>
        </div>
        <div class="grid_5">
            <p>Placed on <span class="boldText"><fmt:formatDate value="${payment.paymentDate}" type="both"
                                                                timeStyle="short"/></span></p>

            <p>By <span class="boldText">${user.email} ${user.name}</span></p>

            <p class="marginBlock">
                <s:link beanclass="com.hk.web.action.admin.order.search.SearchOrderAction"
                        event="searchOrders">
                    <s:param name="gatewayOrderId" value="${order.gatewayOrderId}"/>
                View Order history
                </s:link>
                <s:link beanclass="com.hk.web.action.admin.user.SearchUserAction" event="search">
                    <s:param name="userFilterDto.login" value="${order.user.login}"/>
                View user info
                </s:link>

            <p>
        </div>
    </div>


</div>
<div class="container_16 clearfix containerBox">
    <div class="grid_3 gridBox">

        <h1 class="headers">Payment</h1>

        <p>Status : <span class="successBoldText"> ${payment.paymentStatus.name}</span></p>

        <p>Mode : <span class="boldText">${payment.paymentMode.name}</span></p>
        <c:if test="${payment.gateway != null && payment.issuer != null}">
            <p>Gateway : <span class="boldText">${payment.gateway.name}</span></p>

            <p>Issuer : <span class="boldText">${payment.issuer.name}</span></p>
        </c:if>
        <p class="marginBlock">
            <s:link beanclass="com.hk.web.action.admin.payment.CheckPaymentAction">
                <s:param name="order" value="${order.id}"/>
                Manage Payments/Update Successful
            </s:link>
        </p>
    </div>
    <div class="grid_3 gridBox">
        <h1 class="headers">Shipping Address</h1>

        <p>${address.name}</p>

        <p>${address.line1}
            <c:if test="${not empty address.line2}">
                , ${address.line2}
            </c:if>
        </p>

        <p><s:link beanclass="com.hk.web.action.admin.courier.PincodeCourierMappingAction">
            <s:param name="pincode" value="${address.pincode}"/>
            ${address.pincode.pincode} </s:link></p>

        <p>${address.city} </p>

        <p>${address.state}</p>

        <p>Ph: ${address.phone}</p>

        <p class="marginBlock">
            <s:link beanclass="com.hk.web.action.admin.address.ChangeOrderAddressAction" event="pre">
                Change Address
                <s:param name="order" value="${order}"/>
                <s:param name="address" value="${address}"/>
            </s:link>
        </p>
    </div>
    <div class="grid_3 gridBox">
        <h1 class="headers">Last Activity</h1>

        <p>Order Delivered on</p>

        <p>Feb 27, 2013 8:08:01 PM</p>

        <p>by "Kamal Kumar"</p>

        <p>by "Kamal Kumar"</p>
    </div>
    <div class="grid_3 gridBox">
        <h1 class="headers">User Instructions</h1>

        <p class="dotBorder"></p>

        <p>
            ${order.userComments}
        </p>
    </div>
    <div class="grid_3">

        <h1 class="headers">Cancellation</h1>
        <c:choose>
            <c:when test="${order.orderStatus.id == orderStatusCancelled}">
                <span>Cancellation Type :</span>
                <span>${order.cancellationType.name}</span>
                <br>
                <span>Cancellation Remark :</span>
                <span>${order.cancellationRemark}</span>
            </c:when>
            <c:otherwise>
                <p>Reason for cancellation</p>
                <s:select name="cancellationType" class="cancellationTypeId">
                    <hk:master-data-collection service="<%=MasterDataDao.class%>"
                                               serviceProperty="cancellationTypeList" value="id"
                                               label="name"/>
                </s:select>
                <p>Remark</p>
                <s:textarea name="cancellationRemark" rows="4" cols="30" class="textarea">
                    Probably there was a reason behind cancellation. Please specify here.
                </s:textarea>
                <p class="marginBlock">
                    <s:submit name="pre" value="Cancel" class="cancelOrderButton"/>
                </p>
            </c:otherwise>
        </c:choose>
    </div>
</div>


<div class="container_16 clearfix containerBox2">

<table class="productable ">

    <c:if test="${not empty shippingOrders}">
    <c:forEach items="${shippingOrders}" var="shippingOrder">
    <thead>
    <tr>
        <td colspan="7">
            <h5>SO Id: ${shippingOrder.gatewayOrderId} &nbsp;&nbsp;&nbsp;&nbsp;
                SO Total: <fmt:formatNumber value="${shippingOrder.amount}" type="currency"
                                            currencySymbol="Rs. "/>&nbsp;&nbsp;&nbsp;
                Status: <span class="cancelledText">"${shippingOrder.orderStatus.name}</span>&nbsp;&nbsp;&nbsp;&nbsp;
                <c:if test="${shippingOrder.shipment !=null}">
                    Track Link: <s:link beanclass="com.hk.web.action.core.order.TrackCourierAction"
                                        target="_blank">
                    <s:param name="courierId" value="${shippingOrder.shipment.awb.courier.id}"/>
                    <s:param name="shippingOrder" value="${shippingOrder.id}"/>
                    <s:param name="trackingId" value="${shippingOrder.shipment.awb.awbNumber}"/>
                    Courier: ${shippingOrder.shipment.awb.courier.name} &nbsp;&nbsp;&nbsp;&nbsp;
                    Tracking Id: ${shippingOrder.shipment.awb.awbNumber} &nbsp;&nbsp;&nbsp;&nbsp;
                </s:link>
                    Promised date of dispatch: <fmt:formatDate value="${shippingOrder.targetDispatchDate}"
                                                               type="both"
                                                               timeStyle="short"/> &nbsp;&nbsp;&nbsp;
                </c:if>
            </h5>
        </td>

    <tr class="boldText">
        <td colspan="2">
            Product
        </td>
        <td>
            Qty
        </td>
        <td>
            HK Price
        </td>
        <td>
            Type
        </td>
        <td>
            Days
        </td>
    </tr>

    </thead>

    <c:forEach items="${shippingOrder.lineItems}" var="lineitem">
    <c:set var="productVariant" value="${lineitem.cartLineItem.productVariant}"/>
    <c:set var="cartLineItem" value="${lineitem.cartLineItem}"/>
    <tbody class="disabledstate"> //todo for cancelled
    <tr class="innerCont1"> //todo alternate innercont2
        <td colspan="2">
            <a href="#">${productVariant.product.name}</a>
            <c:if test="${not empty productVariant.productOptions}">
                <c:forEach items="${productVariant.productOptions}" var="productOption"
                           varStatus="optionCtr">
                    ${productOption.name} ${productOption.value}${!optionCtr.last?',':'|'}
                    <c:if test="${not empty cartLineItem.cartLineItemExtraOptions}">
                        <c:forEach items="${cartLineItem.cartLineItemExtraOptions}" var="extraOptions"
                                   varStatus="extraOptionCtr">
                            ${extraOptions.name} ${extraOptions.value}${!extraOptionCtr.last?',':''}
                        </c:forEach>
                    </c:if>
                </c:forEach>
            </c:if>
            <c:if test="${not empty cartLineItem.cartLineItemConfig.cartLineItemConfigValues}">
                <c:set var="TH" value="TH"/>
                <c:set var="THBF" value="THBF"/>
                <c:set var="CO" value="CO"/>
                <c:set var="COBF" value="COBF"/>
                <c:forEach items="${cartLineItem.cartLineItemConfig.cartLineItemConfigValues}"
                           var="configValue"
                           varStatus="configCtr">
                    <c:set var="variantConfigOption" value="${configValue.variantConfigOption}"/>
                    <c:set var="additionalParam" value="${variantConfigOption.additionalParam}"/>
                    ${variantConfigOption.displayName} : ${configValue.value}
                    <c:if
                            test="${(additionalParam ne TH) or (additionalParam ne THBF) or (additionalParam ne CO) or (additionalParam ne COBF) }">
                        <c:if
                                test="${fn:startsWith(variantConfigOption.name, 'R')==true}">
                            (R)
                        </c:if>
                        <c:if
                                test="${fn:startsWith(variantConfigOption.name, 'L')==true}">
                            (L)
                        </c:if>
                    </c:if>
                    ${!configCtr.last?',':''}

                </c:forEach>
            </c:if>
        </td>
        <td>
                ${lineitem.qty}
        </td>
        <td>
                ${lineitem.hkPrice}
        </td>
        <td>
            <c:choose>
                <c:when test="${productVariant.product.groundShipping}">
                    <span style="margin-left:10px;color: #ff0000;">(G)</span>
                </c:when>
                <c:otherwise>
                    <span style="margin-left:10px;color: #ff0000;">(A)</span>
                </c:otherwise>
            </c:choose>
            <c:if test="${shippingOrder.dropShipping}">
                <span style="margin-left:10px;color: #ff0000;">(D)</span>
            </c:if>
        </td>
        <strong>Dispatch : ${productVariant.product.minDays}-${productVariant.product.maxDays}
            days </strong>
        </c:forEach>

    </tbody>
</table>

</c:forEach>
</c:if>

<table class="billsummary">

    <tbody>
    <tr>
        <td colspan="3">
            <h1>Bill Summary</h1>
        </td>

    </tr>
    <tr class="innerCont1">

        <td colspan="2">
            Item Total
        </td>
        <td>
            <fmt:formatNumber value="${ojpa.pricingDto.payableHkSubTotal}" type="currency" currencySymbol="Rs. "/>
        </td>

    </tr>
    <c:if test="${ojpa.pricingDto.codLineCount > 0}">
        <tr class="innercont2">
            <td colspan="2">
                COD
            </td>
            <td>
                <fmt:formatNumber value="${ojpa.pricingDto.codSubTotal}" type="currency"
                                  currencySymbol="Rs. "/>
            </td>
        </tr>
    </c:if>
    <tr class="innerCont1">

        <td colspan="2">
            Shipping
        </td>
        <td>
            <fmt:formatNumber value="${ojpa.pricingDto.shippingSubTotal - ojpa.pricingDto.shippingDiscount}"
                              type="currency" currencySymbol="Rs. "/>
        </td>

    </tr>
    <tr class="innercont2">
        <td colspan="2">
            Coupon Discount
        </td>
        <td>
        <td>
            <fmt:formatNumber
                    value="${ojpa.pricingDto.totalDiscount - ojpa.pricingDto.shippingDiscount - ojpa.pricingDto.subscriptionDiscount}"
                    type="currency" currencySymbol="Rs. "/>
        </td>
    </tr>
    <c:if test="${ojpa.pricingDto.subscriptionDiscount >0}">
        <tr class="innerCont1">

            <td colspan="2">
                Subscription Discount
            </td>
            <td>
                <fmt:formatNumber value="${ojpa.pricingDto.subscriptionDiscount}"
                                  type="currency" currencySymbol="Rs. "/>
            </td>

        </tr>
    </c:if>

    <tr class="innerCont2">

        <td colspan="2">
            Reward points
        </td>
        <td>
            <fmt:formatNumber value="${ojpa.pricingDto.redeemedRewardPoints}" type="currency"
                              currencySymbol="Rs. "/></td>

    </tr>
    <tr class="innercont1">
        <td colspan="2">
            Grand total
        </td>
        <td>
            <strong><fmt:formatNumber value="${ojpa.pricingDto.grandTotalPayable}" type="currency"
                                      currencySymbol="Rs. " maxFractionDigits="0"/> </strong>
        </td>

    </tr>
    </tbody>
</table>


</div>
<div class="container_16 clearfix">
    <table class="productable">

        <thead>

        <tr>
            <td colspan="4">
                <h1>Order Lifecycle : Order#${order.gatewayOrderId}</h1>
            </td>
        </tr>

        <tr class="boldText">
            <td>
                Activity Date
            </td>
            <td>
                Activity
            </td>
            <td>
                Activity By User
            </td>
            <td colspan="3">
                Comments
            </td>
        </tr>

        </thead>

        <tbody>
        <tr class="innerCont1"> //todo innercont2 alternate
            <c:forEach items="${order.orderLifecycles}" var="orderLifeCycle">
                <td>
                    <fmt:formatDate value="${orderLifeCycle.activityDate}" type="both"/> (0mins)
                </td>
                <td>
                        ${orderLifeCycle.orderLifecycleActivity.name}
                </td>
                <td>
                        ${orderLifeCycle.user.name}
                </td>
                <td colspan="3">
                        ${orderLifeCycle.comments}
                </td>

            </c:forEach>

        </tr>
        </tbody>

    </table>
</div>


<div class="container_16 clearfix containerBox2">

    <c:if test="${not empty shippingOrders}">
    <c:forEach items="${shippingOrders}" var="shippingOrder">
    <table class="productable">

        <thead>
        <tr>
            <td>
                <h1>SO Lifecycle : Order#${shippingOrder.gatewayOrderId}</h1>
            </td>
        </tr>

        <tr class="boldText">
            <td colspan="1">
                Activity Date
            </td>
            <td>
                Activity
            </td>
            <td>
                Activity By User
            </td>
            <td colspan="2">
                Comments
            </td>
        </tr>

        </thead>

        <tbody>
        <c:forEach items="${shippingOrder.shippingOrderLifecycles}" var="orderLifeCycle">

        <tr class="innerCont1">
            <td><fmt:formatDate value="${orderLifeCycle.activityDate}" type="both"/></td>
            <td>${orderLifeCycle.shippingOrderLifeCycleActivity.name}</td>
            <td>${orderLifeCycle.user.name}</td>
            <td>${orderLifeCycle.comments}</td>

        </tr>
        </tbody>


        </c:forEach>
        </c:forEach>
        </c:if>

</div>


</div>

</body>
</html>


