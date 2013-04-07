<%@ page import="com.hk.constants.order.EnumOrderStatus" %>
<%@ page import="com.hk.constants.shippingOrder.EnumShippingOrderStatus" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.domain.order.OrderLifecycle" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.Collections" %>
<%@ page import="com.hk.domain.order.ShippingOrderLifecycle" %>
<%@ page import="com.hk.domain.order.ShippingOrder" %>
<%@ page import="java.util.Set" %>
<%@ page import="java.util.ArrayList" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.crm.OrderDetailsAction" var="ojpa"/>
<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8"/>
  <title>Order Details</title>
  <script type="text/javascript" src="<hk:vhostJs/>/js/jquery-1.6.2.min.js"></script>
  <script type="text/javascript" src="<hk:vhostJs/>/js/jquery.hkCommonPlugins.js"></script>
  <link href="${pageContext.request.contextPath}/css/newcss/grid.css" rel="stylesheet" type="text/css"/>
  <link href="${pageContext.request.contextPath}/css/newcss/style.css" rel="stylesheet" type="text/css"/>
</head>
<body>
<c:set var="shippingOrderStatusActionAwaiting" value="<%=EnumShippingOrderStatus.SO_ActionAwaiting.getId()%>"/>
<c:set var="shippingOrderStatusCancelled" value="<%=EnumShippingOrderStatus.SO_Cancelled.getId()%>"/>
<c:set var="orderStatusCancelled" value="<%=EnumOrderStatus.Cancelled.getId()%>"/>
<c:set var="order" value="${ojpa.order}"/>
<c:set var="shippingOrders" value="${order.shippingOrders}"/>
<c:set var="payment" value="${order.payment}"/>
<c:set var="user" value="${order.user}"/>
<c:set var="address" value="${order.address}"/>


<div class="wrapper">
<div id="header">
  <div class="container_16 clearfix containerBox">
    <div class="grid_5">
      <h5>Order ID: ${order.gatewayOrderId}</h5>
      <h4>Amount: <fmt:formatNumber value="${ojpa.order.payment.amount}"
                                    pattern="<%=FormatUtils.currencyFormatPattern%>"/></h4>
    </div>
    <div class="grid_5">
      <h6>Order Status : ${order.orderStatus.name}</h6>
    </div>
    <div class="grid_6">
      <p>Placed on <span class="boldText"><fmt:formatDate value="${payment.paymentDate}" type="both"
                                                          timeStyle="short"/></span></p>

      <p>By <span class="boldText">${user.email} (${user.name})</span></p>

      <p class="marginBlock">
        <s:link beanclass="com.hk.web.action.admin.order.search.SearchOrderAction"
                event="searchOrders" class="button">
          <s:param name="gatewayOrderId" value="${order.gatewayOrderId}"/>
        View Order history
        </s:link>
        <s:link beanclass="com.hk.web.action.admin.user.SearchUserAction" event="search" class="button">
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
      <s:link beanclass="com.hk.web.action.admin.payment.CheckPaymentAction" class="button">
        <s:param name="order" value="${order.id}"/>
        Manage Payments
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

    <p>${address.pincode}
      <s:link beanclass="com.hk.web.action.admin.courier.PincodeCourierMappingAction" class="button" target="_blank">
        <s:param name="pincode" value="${address.pincode}"/>
        Pincode Info </s:link></p>

    <p>${address.city} </p>

    <p>${address.state}</p>

    <p>Ph: ${address.phone}</p>

    <p class="marginBlock">
      <s:link beanclass="com.hk.web.action.admin.address.ChangeOrderAddressAction" event="pre" class="button">
        Change Address
        <s:param name="order" value="${order}"/>
        <s:param name="address" value="${address}"/>
      </s:link>
    </p>
  </div>
  <div class="grid_3 gridBox">
    <h1 class="headers">Last Activity</h1>
    <c:set value="${order.orderLifecycles[fn:length(order.orderLifecycles) - 1].orderLifecycleActivity}"
           var="lastActivity"/>
    <c:set value="${order.orderLifecycles[fn:length(order.orderLifecycles) - 1].activityDate}" var="activityDate"/>
    <c:set value="${order.orderLifecycles[fn:length(order.orderLifecycles) - 1].user}" var="activityBy"/>
    <p>${lastActivity.name} on</p>

    <p><fmt:formatDate value="${activityDate}" type="both"/></p>

    <p>by ${activityBy.name} <br/>(${activityBy.login})</p>
  </div>
  <div class="grid_3 gridBox">
    <h1 class="headers">User Instructions</h1>
    <c:choose>
      <c:when test="${fn:length(order.userComments) > 0}">
        <c:if test="${order.commentType != null}">
          Type: ${order.commentType}
          <p class="dotBorder"></p>
        </c:if>
        <p>
            ${order.userComments}
        </p>
      </c:when>
      <c:otherwise>
        <p> No comments </p>
      </c:otherwise>
    </c:choose>
  </div>
  <div class="grid_3">
    <h1 class="headers">Cancellation</h1>
    <s:form beanclass="com.hk.web.action.admin.order.CancelOrderAction" class="cancelOrderForm">
      <s:param name="order" value="${order.id}"/>
      <c:choose>
        <c:when test="${order.orderStatus.id == orderStatusCancelled}">
          <span>Cancellation Type :</span>
          <br/>
          <span>${order.cancellationType.name}</span>
          <br/>
          <span>Cancellation Remark :</span>
          <br/>
          <span>${order.cancellationRemark}</span>
        </c:when>
        <c:otherwise>
          <p>Reason for cancellation</p>
          <s:select name="cancellationType" class="dropDown">
            <hk:master-data-collection service="<%=MasterDataDao.class%>"
                                       serviceProperty="cancellationTypeList" value="id"
                                       label="name"/>
          </s:select>
          <p>Remark</p>
          <s:textarea name="cancellationRemark"
                      class="textarea">
          </s:textarea>
          <p class="marginBlock">
            <s:submit name="pre" value="Cancel" class="button cancelOrderButton"/>
          </p>
        </c:otherwise>
      </c:choose>
    </s:form>
    <script type="text/javascript">
      $('.cancelOrderButton').click(function() {
        var proceed = confirm('Are you sure you want to cancel the order?');
        if (!proceed) return false;
      });

      $('.cancelOrderForm').ajaxForm({dataType: 'json', success: _cancelOrder});
      function _cancelOrder(res) {
        if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
          var status = res.data.orderStatus.name;
          if (status == "Cancelled") {
            //alert("Base order cancelled");
            location.reload();
          } else {
            alert("Base order cannot be cancelled");
            location.reload();
          }
        }
      }
    </script>
  </div>
</div>

<div class="container_16 clearfix containerBox2">
<c:forEach items="${shippingOrders}" var="shippingOrder">
  <table class="productable ">
    <thead>
    <tr>
      <td colspan="7">
        <h5>SO Id: ${shippingOrder.gatewayOrderId} &nbsp;&nbsp;&nbsp;&nbsp;
          SO Total: <fmt:formatNumber value="${shippingOrder.amount}" type="currency"
                                      currencySymbol="Rs. "/>&nbsp;&nbsp;&nbsp;
          Status: <span
              class="${shippingOrder.shippingOrderStatus.id == shippingOrderStatusCancelled ? 'cancelledText' : ''}">${shippingOrder.orderStatus.name}</span>&nbsp;&nbsp;&nbsp;&nbsp;
          <c:if test="${shippingOrder.shipment !=null}">
            Courier: ${shippingOrder.shipment.awb.courier.name} &nbsp;&nbsp;
            Tracking Id: <s:link beanclass="com.hk.web.action.core.order.TrackCourierAction"
                                 target="_blank">
            <s:param name="courierId" value="${shippingOrder.shipment.awb.courier.id}"/>
            <s:param name="shippingOrder" value="${shippingOrder.id}"/>
            <s:param name="trackingId" value="${shippingOrder.shipment.awb.awbNumber}"/>
            ${shippingOrder.shipment.awb.awbNumber}
          </s:link>
            &nbsp;&nbsp;&nbsp;&nbsp;
          </c:if>
          <br/>Promised Dispatch Date: <fmt:formatDate value="${shippingOrder.targetDispatchDate}"
                                                     pattern="MMM dd, yyyy "/>

        </h5>
      </td>

    <tr class="boldText">
      <td colspan="2" width="30%">
        Product
      </td>
      <td>
        Qty
      </td>
      <td>
        HK Price
      </td>
      <td>
        Fulfilment Type
      </td>
      <td>
        Shipping Mode
      </td>
      <td>
        Dispatch Days
      </td>
    </tr>

    </thead>

    <c:forEach items="${shippingOrder.lineItems}" var="lineitem" varStatus="liCtr">
    <c:set var="productVariant" value="${lineitem.cartLineItem.productVariant}"/>
    <c:set var="cartLineItem" value="${lineitem.cartLineItem}"/>
    <tbody class="${shippingOrder.shippingOrderStatus.id == shippingOrderStatusCancelled ? 'disabledstate' : ''}">
    <tr class="${liCtr.index % 2 == 0 ? 'innerCont1' : 'innerCont2'}">
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
          <c:when test="${shippingOrder.dropShipping}">
            <span style="margin-left:10px;color: #ff0000;">(Drop Ship)</span>
          </c:when>
          <c:when test="${productVariant.product.jit}">
            <span style="margin-left:10px;color: #ff0000;">(JIT)</span>
          </c:when>
          <c:otherwise>
            Normal
          </c:otherwise>
        </c:choose>
      </td>
      <td>
        <c:choose>
          <c:when test="${productVariant.product.groundShipping}">
            <span style="margin-left:10px;color: #ff0000;">(Ground)</span>
          </c:when>
          <c:otherwise>
            <span style="margin-left:10px;color: #ff0000;">(Air)</span>
          </c:otherwise>
        </c:choose>
      </td>
      <td>
          ${productVariant.product.minDays != null ? productVariant.product.minDays : 1}-${productVariant.product.maxDays != null ? productVariant.product.maxDays : 3}
        days
      </td>
      </c:forEach>

    </tbody>
  </table>


</c:forEach>

<table class="billsummary">

  <tbody>
  <tr>
    <td colspan="3">
      <h1>Bill Summary</h1>
    </td>

  </tr>
  <tr class="innerCont2">

    <td colspan="2">
      Item Total
    </td>
    <td>
      <fmt:formatNumber value="${ojpa.pricingDto.payableHkSubTotal}" type="currency" currencySymbol="Rs. "/>
    </td>

  </tr>
  <c:if test="${ojpa.pricingDto.codLineCount > 0}">
    <tr class="innerCont2">
      <td colspan="2">
        COD
      </td>
      <td>
        <fmt:formatNumber value="${ojpa.pricingDto.codSubTotal}" type="currency"
                          currencySymbol="Rs. "/>
      </td>
    </tr>
  </c:if>
  <tr class="innerCont2">

    <td colspan="2">
      Shipping
    </td>
    <td>
      <fmt:formatNumber value="${ojpa.pricingDto.shippingSubTotal - ojpa.pricingDto.shippingDiscount}"
                        type="currency" currencySymbol="Rs. "/>
    </td>

  </tr>
  <tr class="innerCont2">
    <td colspan="2">
      Coupon Discount
    </td>
    <td>
      <fmt:formatNumber
          value="${ojpa.pricingDto.totalDiscount - ojpa.pricingDto.shippingDiscount - ojpa.pricingDto.subscriptionDiscount}"
          type="currency" currencySymbol="Rs. "/>
    </td>
  </tr>
  <c:if test="${ojpa.pricingDto.subscriptionDiscount >0}">
    <tr class="innerCont2">

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
  <tr class="innerCont1">
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
      <td width="25%">
        Activity Date
      </td>
      <td width="15%">
        Activity
      </td>
      <td width="15%">
        Activity By User
      </td>
      <td>
        Comments
      </td>
    </tr>

    </thead>

    <tbody>
    <%
      List<OrderLifecycle> orderLifecycles = ojpa.getOrder().getOrderLifecycles();
      Collections.reverse(orderLifecycles);
      pageContext.setAttribute("orderLifecycles", orderLifecycles);
    %>
    <c:forEach items="${orderLifecycles}" var="orderLifeCycle" varStatus="olcCtr">
      <tr class="${olcCtr.index % 2 == 0 ? 'innerCont1' : 'innerCont2'}">
        <td>
          <fmt:formatDate value="${orderLifeCycle.activityDate}" type="both"/>
          (${hk:periodFromNow(orderLifeCycle.activityDate)})
        </td>
        <td>
            ${orderLifeCycle.orderLifecycleActivity.name}
        </td>
        <td>
            ${orderLifeCycle.user.name}
        </td>
        <td>
            ${orderLifeCycle.comments}
        </td>
      </tr>
    </c:forEach>
    </tbody>

  </table>
</div>


<div class="container_16 clearfix containerBox2">

  <%
    for (ShippingOrder shippingOrder : ojpa.getOrder().getShippingOrders()) {
      List<ShippingOrderLifecycle> shippingOrderLifecycleList = shippingOrder.getShippingOrderLifecycles();
      Collections.reverse(shippingOrderLifecycleList);
  %>

  <table class="productable">
    <thead>
    <tr>
      <td colspan="4">
        <h1>SO Lifecycle : Shipping Order#<%=shippingOrder.getGatewayOrderId()%>
        </h1>
      </td>
    </tr>

    <tr class="boldText">
      <td width="25%">
        Activity Date
      </td>
      <td width="15%">
        Activity
      </td>
      <td width="15%">
        Activity By User
      </td>
      <td>
        Comments
      </td>
    </tr>

    </thead>

    <tbody>
    <c:forEach items="<%=shippingOrderLifecycleList%>" var="orderLifeCycle" varStatus="olcCtr">
      <tr class="${olcCtr.index % 2 == 0 ? 'innerCont1' : 'innerCont2'}">
        <td>
          <fmt:formatDate value="${orderLifeCycle.activityDate}" type="both"/>
          (${hk:periodFromNow(orderLifeCycle.activityDate)})
        </td>
        <td>
            ${orderLifeCycle.shippingOrderLifeCycleActivity.name}
        </td>
        <td>
            ${orderLifeCycle.user.name}
        </td>
        <td>
            ${orderLifeCycle.comments}
        </td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
  <%}%>

</div>


</div>

</body>
</html>


