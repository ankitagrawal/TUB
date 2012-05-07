<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.order.EnumCartLineItemType" %>
<%@ page import="com.hk.constants.payment.EnumPaymentMode" %>
<%@ page import="com.hk.constants.payment.EnumPaymentStatus" %>
<%@ page import="com.hk.constants.order.EnumOrderStatus" %>
<%@ page import="com.hk.constants.shippingOrder.EnumShippingOrderStatus" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.order.search.SearchOrderAction" var="orderAdmin" event="pre"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Base Order Search">

<s:layout-component name="htmlHead">
  <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
  <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>

  <script type="text/javascript">
    $(document).ready(function() {

    <%--
    Confirm cod order
    --%>
      $('.confirmCodLink').click(function() {
        var proceed = confirm('Are you sure?');
        if (!proceed) return false;

        var clickedLink = $(this);
        $.getJSON(clickedLink.attr('href'), function(res) {
          if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
            clickedLink.parents('h2').find('.paymentStatusName').html(res.data.paymentStatus.name);
            clickedLink.parents('h2').find('.codOrderText').hide();
            clickedLink.hide();
          }
        });

        return false;
      });


    <%--
    Cancel order
    --%>
      $('.cancelOrderLink').click(function() {
        var proceed = confirm('Are you sure?');
        if (!proceed) return false;

        var clickedLink = $(this);
        $.getJSON(clickedLink.attr('href'), function(res) {
          if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
            clickedLink.parents('h2').find('.orderStatusName').html(res.data.orderStatus.name);
            clickedLink.hide();
            clickedLink.parents('h2').find('.onHoldStatusLink').hide();
            clickedLink.parents('h2').find('.normalStatusLink').hide();
          }
        });
        return false;
      });


    <%--
    Set default courier
    --%>
      $('.setAsDefaultCourierLink').click(function() {

        var clickedLink = $(this);
        $.getJSON($(this).attr('href'), {'courier':$(this).parents('.addressRow').find('.courierId').val()}, function(res) {
          if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
            clickedLink.parents('.addressRow').find('.addressDefaultCourier').html(res.data.courier.name);
          }
        });

        return false;
      });


    <%--
    Delete Order Dump
    --%>
      $('.deleteOrderDumpLink').click(function() {
        var proceed = confirm('Are you sure?');
        if (!proceed) return false;

        var clickedLink = $(this);
        var clickedH2 = clickedLink.parents('h2');
        clickedH2.find('.orderDumpStatus').html($('#ajaxLoader').html());
        $.getJSON(clickedLink.attr('href'), function(res) {
          if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
            clickedLink.parents('.orderRow').find('.lineItemCheckBox').attr('disabled', 'disabled');
            clickedH2.find('.orderDumpStatus').html("Not Generated");
            clickedH2.find('.deleteOrderDumpLink').hide();
          }
        });
        return false;
      });


    <%--
    Order status hold/unhold toggle functionality
    --%>
      $('.orderStatusLink').click(function() {
        var proceed = confirm('Are you sure?');
        if (!proceed) return false;

        var clickedLink = $(this);
        var clickedP = clickedLink.parents('p');
        clickedP.find('.orderStatusName').html($('#ajaxLoader').html());

        $.getJSON($(this).attr('href'), function(res) {
          if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
          <%--  clickedP.find('.orderStatusName').html(res.data.orderStatus.name);
            if (res.data.orderStatus.id == '<%=EnumOrderStatus.OnHold.getId()%>') {
              clickedP.find('.onHoldStatusLink').show();
              clickedP.find('.normalStatusLink').hide();
              clickedLink.parents('.orderRow').addClass('highlight_hold');
              // disable the line item checkboxes
              clickedLink.parents('.orderRow').find('.lineItemCheckBox').attr('disabled', 'disabled');
            } else {
              clickedP.find('.onHoldStatusLink').hide();
              clickedP.find('.normalStatusLink').show();
              clickedLink.parents('.orderRow').removeClass('highlight_hold');
              // enable the line item checkboxes
              clickedLink.parents('.orderRow').find('.lineItemCheckBox').removeAttr('disabled');
            }  --%>
            window.location.reload();
          }
        });

        return false;
      });

    <%-- order dump log modal --%>
      $('#orderDumpLogWindow').jqm({trigger: '.orderDumpLog', ajax: '@href'});
    });

  </script>
</s:layout-component>


<s:layout-component name="heading">${orderAdmin.currentBreadcrumb.name}</s:layout-component>

<s:layout-component name="modal">
  <div class="jqmWindow" id="orderDumpLogWindow"></div>
</s:layout-component>

<s:layout-component name="content">

<span id="ajaxLoader" style="display:none;"><img src="<hk:vhostImage/>/common/images/ajax-loader.gif"/></span>

<c:set var="lineItemType_Product" value="<%=EnumCartLineItemType.Product.getId()%>"/>
<c:set var="linItemStatusShipped" value="<%=EnumOrderStatus.Shipped.getId()%>"/>
<c:set var="shippingOrderStatusShipped" value="<%=EnumShippingOrderStatus.SO_Shipped.getId()%>"/>
<c:set var="linItemStatusDelivrd" value="<%=EnumOrderStatus.Delivered.getId()%>"/>
<c:set var="shippingOrderStatusDelivrd" value="<%=EnumShippingOrderStatus.SO_Delivered.getId()%>"/>
<c:set var="linItemStatusRTO" value="<%=EnumOrderStatus.RTO.getId()%>"/>
<c:set var="orderStatusCart" value="<%=EnumOrderStatus.InCart.getId()%>"/>
<c:set var="orderStatusCancelled" value="<%=EnumOrderStatus.Cancelled.getId()%>"/>
<c:set var="orderStatusPending" value="<%=EnumOrderStatus.InProcess.getId()%>"/>
<c:set var="orderStatusHold" value="<%=EnumOrderStatus.OnHold.getId()%>"/>
<c:set var="paymentStatusPending" value="<%=EnumPaymentStatus.AUTHORIZATION_PENDING.getId()%>"/>

<s:errors/>
<s:form beanclass="com.hk.web.action.admin.order.search.SearchOrderAction" method="get" autocomplete="false">
  <fieldset class="top_label">
    <ul>
      <div class="grouped">
        <li><label>BO Order ID</label> <s:text name="orderId" style="width: 100px;"/></li>
        <li><label>BO Gateway Order ID</label> <s:text name="gatewayOrderId"/></li>
        <li><label>Email ID</label> <s:text name="email"/></li>
        <li><label>Login ID</label> <s:text name="login"/></li>
        <li><label>Name</label> <s:text name="name"/></li>
        <li><label>Phone</label> <s:text name="phone"/></li>
        <li><label>Status</label><s:select name="orderStatus">
          <option value="">Any order status</option>
          <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="orderStatusList" value="id"
                                     label="name"/>
        </s:select></li>
        <li><label>Payment Mode</label><s:select name="paymentMode">
          <option value="">Any Payment Mode</option>
          <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="paymentModes" value="id"
                                     label="name"/>
        </s:select></li>
          <%--<li><label>Tracking ID</label> <s:text name="trackingId" style="width: 120px;"/></li>--%>
        <li>
          <label>Start
            date</label><s:text class="date_input" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                name="startDate"/>
        </li>
        <li>
          <label>End
            date</label><s:text class="date_input" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                name="endDate"/>
        </li>
      </div>
    </ul>
    <div class="buttons"><s:submit name="searchOrders" value="Search Orders"/></div>
      <%--<div class="buttons"><s:submit name="orderKiMB" value="Order Ki MB"/></div>--%>
  </fieldset>
</s:form>

<c:set var="paymentModeCod" value="<%=EnumPaymentMode.COD.getId()%>"/>

<s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${orderAdmin}"/>
<s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${orderAdmin}"/>

<c:forEach items="${orderAdmin.orderList}" var="order" varStatus="ctr">
<table class="cont" width="100%">
<thead>
<tr>
  <th width="10">Sr.</th>
  <th>Order Status</th>
  <th>Order Info</th>
  <th>Addresses</th>
  <th>Invoice Detail</th>
  <c:if test="${not empty order.shippingOrders}">
    <th>Shipping Order Details</th>
  </c:if>
</tr>
</thead>
<tr class="${order.payment.paymentMode.id == paymentModeCod ? 'highlight_cod' : ''}">
<td>${ctr.count}</td>
<td>
  <span class="xsml gry">Order Status :</span>

  <span class="orderStatusName or">${order.orderStatus.name}</span>
  <c:if test="${order.orderStatus.id == orderStatusPending || order.orderStatus.id == orderStatusHold}">
    <s:link beanclass="com.hk.web.action.admin.order.OrderOnHoldAction" event="unHoldOrder" title="Unhold Order"
            class="orderStatusLink onHoldStatusLink"
            style="${order.orderStatus.id == orderStatusHold ? '': 'display:none;'}">
      <s:param name="order" value="${order.id}"/>
      <img src="<hk:vhostImage/>/images/admin/icon_unhold.png" alt="Unhold Order" title="Unhold Order"/>
    </s:link>
    <s:link beanclass="com.hk.web.action.admin.order.OrderOnHoldAction" event="holdOrder" title="Put Order on Hold"
            class="orderStatusLink normalStatusLink"
            style="${order.orderStatus.id == orderStatusHold ? 'display:none;': ''}">
      <s:param name="order" value="${order.id}"/>
      <img src="<hk:vhostImage/>/images/admin/icon_hold.png" alt="Put Order on Hold" title="Put Order on Hold"/>
    </s:link>
  </c:if>
  <c:if test="${order.orderStatus.id == orderStatusCancelled}">
    <br>
    <span>Cancellation Type :</span>
    <span>${order.cancellationType.name}</span>
    <br>
    <span>Cancellation Remark :</span>
    <span>${order.cancellationRemark}</span>
  </c:if>
  <br/>
  <span class="xsml gry">Payment :</span>
  <span
      class="paymentStatusName">${order.payment.paymentStatus != null ? order.payment.paymentStatus.name : 'N/A'}</span><br/>
  <s:link beanclass="com.hk.web.action.admin.CheckPaymentAction">
    <s:param name="order" value="${order.id}"/>
    Manage Payments
  </s:link>
  <c:if
      test="${order.payment.paymentStatus.id == paymentStatusPending && order.payment.paymentMode.id == paymentModeCod}">
    <span class="codOrderText">&middot;</span>
    <s:link beanclass="com.hk.web.action.admin.VerifyCodAction" class="confirmCodLink">
      <s:param name="order" value="${order.id}"/>
      Confirm COD
    </s:link>
  </c:if>
  <hr/>
  <c:if test="${! empty order.orderLifecycles}">
    <s:link beanclass="com.hk.web.action.admin.order.OrderLifecycleAction" event="pre" target="_blank">
      <label style="font-weight:bold;">Last Activity:</label><br>
      ${order.orderLifecycles[fn:length(order.orderLifecycles)-1].orderLifecycleActivity.name} on <br>
      <fmt:formatDate value="${order.orderLifecycles[fn:length(order.orderLifecycles)-1].activityDate}" type="both"/> by
      "${order.orderLifecycles[fn:length(order.orderLifecycles)-1].user.name}"
      <s:param name="order" value="${order}"/>
    </s:link>
  </c:if>
  <br/>
  <c:if test="${!(order.orderStatus.id == orderStatusCancelled || order.orderStatus.id == orderStatusCart)}">
    <br/>
    <s:form beanclass="com.hk.web.action.admin.order.CancelOrderAction" class="cancelOrderForm">
      <s:param name="order" value="${order.id}"/>
      Reason:
      <s:select name="cancellationType" class="cancellationTypeId">
        <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="cancellationTypeList" value="id"
                                   label="name"/>
      </s:select>
      <br/>
      Remark:
      <s:textarea name="cancellationRemark" style="height:100px"/>
      <div class="buttons">
        <s:submit name="pre" value="Cancel" class="cancelOrderButton"/>
      </div>
    </s:form>
    <script type="text/javascript">
      $('.cancelOrderButton').click(function() {
        var proceed = confirm('Are you sure?');
        if (!proceed) return false;
      });
      $('.cancelOrderForm').ajaxForm({dataType: 'json', success: _cancelOrder});

      function _cancelOrder(res) {
        if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
          alert("Order cancelled");
        }
      }
    </script>

  </c:if>

</td>
<td>
  <span class="upc lgry sml">ID
  <strong><span class="or"> ${order.id}</span></strong>
  <c:if test="${order.orderStatus.id != orderStatusCart}">
    (<s:link beanclass="com.hk.web.action.core.accounting.BOInvoiceAction" event="pre" target="_blank">
    Invoice <s:param name="order" value="${order.id}"/>
  </s:link>) <br/>
  </c:if>
    <%--<br/>(<s:link beanclass="com.hk.web.action.AccountingInvoiceAction" event="oldAccountingInvoice" target="_blank">--%>
    <%--Prime Accounting Invoice <s:param name="order" value="${order.id}"/></s:link>) <br>--%>
    <%--<br/><b>Retail Invoice:</b> <br/>--%>
    <%--<c:forEach items="${order.retailInvoices}" var="accountingInvoice">--%>
    <%--<s:link beanclass="com.hk.web.action.AccountingInvoiceAction" event="retailInvoice" target="_blank">--%>
    <%--<s:param name="accountingInvoice" value="${accountingInvoice.id}"/>--%>
    <%--R-${accountingInvoice.retailInvoiceId} on <fmt:formatDate value="${accountingInvoice.invoiceDate}" type="both"/>--%>
    <%--</s:link><br/></c:forEach>--%>
    <%--<br/><b>B2B Invoice:</b> <br/>--%>
    <%--<c:forEach items="${order.b2BInvoices}" var="accountingInvoice">--%>
    <%--<s:link beanclass="com.hk.web.action.AccountingInvoiceAction" event="b2bInvoice" target="_blank">--%>
    <%--<s:param name="accountingInvoice" value="${accountingInvoice.id}"/>--%>
    <%--T-${accountingInvoice.retailInvoiceId} on--%>
    <%--<fmt:formatDate value="${accountingInvoice.invoiceDate}" type="both"/>--%>
    <%--</s:link><br/></c:forEach><br/><br/><b>Service Invoice:</b> <br/>--%>
    <%--<c:forEach items="${order.seviceInvoices}" var="accountingInvoice">--%>
    <%--<s:link beanclass="com.hk.web.action.AccountingInvoiceAction" event="serviceInvoice" target="_blank">--%>
    <%--<s:param name="accountingInvoice" value="${accountingInvoice.id}"/>--%>
    <%--S-${accountingInvoice.serviceInvoiceId} on--%>
    <%--<fmt:formatDate value="${accountingInvoice.invoiceDate}" type="both"/>--%>
    <%--</s:link><br/></c:forEach>--%>
    <%--</c:if>--%>
  <span class="upc lgry sml">GID</span> <strong><span class="or"> ${order.gatewayOrderId}</span></strong><br/>

  <c:choose>
    <c:when test="${order.user.email == order.user.login}">
      <%-- Usual case , seems like a registered user --%>
      <span class="upc lgry sml">Email</span>
      <s:link beanclass="com.hk.web.action.admin.user.SearchUserAction" event="search">
        <s:param name="userFilterDto.login" value="${order.user.login}"/>
        ${order.user.login}
      </s:link>
    </c:when>
    <c:otherwise>
      <%-- Probably a guest user account --%>
      <span class="upc lgry sml">Login</span>
      <s:link beanclass="com.hk.web.action.admin.user.SearchUserAction" event="search">
      <s:param name="userFilterDto.login" value="${order.user.login}"/>
      ${order.user.login}
      </s:link><br/>
      <span class="upc lgry sml">Email:</span>
      ${order.user.email}
    </c:otherwise>
  </c:choose>

        <span class="sml">
        (<s:link beanclass="com.hk.web.action.admin.SearchTicketAction" event="search" target="_blank">
          View Tickets
          <s:param name="ticketFilterDto.associatedLogin" value="${order.user.email}"/>
        </s:link>) <br/>
        (<s:link beanclass="com.hk.web.action.admin.order.search.SearchOrderAction" event="searchOrders">
          View Orders
          <s:param name="login" value="${order.user.login}"/>
        </s:link>)
        </span>
  <br/>
  <hr/>
  <c:if test="${order.payment.paymentMode.id == paymentModeCod}">
    <span class="upc lgry sml"><strong>Cod Contact</strong></span><br/>
    <span class="upc lgry sml or">${order.payment.contactName}</span><br/>
    <span class="upc lgry sml or">${order.payment.contactNumber}</span><br/>
  </c:if>
  <strong><fmt:formatNumber currencySymbol="Rs. " value="${order.payment.amount}" type="currency"/></strong><br/>

  <i><fmt:formatDate value="${order.payment.createDate}" type="both"/></i> <br/>
      <span class="sml">
        <s:link beanclass="com.hk.web.action.admin.ticket.CreateTicketAction" event="createOrderRelatedTicket">
          Create Ticket
          <s:param name="order" value="${order.id}"/>
        </s:link> <br/>
        <s:link beanclass="com.hk.web.action.admin.SearchTicketAction" event="search" target="_blank">
          View Tickets
          <s:param name="ticketFilterDto.associatedOrderId" value="${order.id}"/>
        </s:link>
      </span>
</td>
<td class="has_table">

  <table>

    <tr class="addressRow">
      <td width="150">
          ${order.address.name}<br/>
          ${order.address.line1}<br/>
          ${order.address.line2}<br/>
          ${order.address.city} -
        <a href="http://www.dtdc.in/subpages/services/location_search.asp?pin=${order.address.pin}" target="_blank">
            ${order.address.pin}
        </a><br/>
          ${order.address.state}<br/>
        Ph: ${order.address.phone}<br/>
        Default Courier:
        <c:choose>
          <c:when test="${order.address.courier != null}">
            <span class="addressDefaultCourier">${order.address.courier.name}</span>
          </c:when>
          <c:otherwise>
            <span class="addressDefaultCourier">Not Set</span>
          </c:otherwise>
        </c:choose>
        <br/>
        <s:form beanclass="com.hk.web.action.HomeAction" autocomplete="false">
          <s:select name="" value="${order.address.courier != null ? order.address.courier.id : '0'}" class="courierId">
            <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="courierList" value="id"
                                       label="name"/>
          </s:select>
        </s:form>
        <s:link beanclass="com.hk.web.action.admin.SetDefaultCourierAction" event="pre" class="setAsDefaultCourierLink">
          Set as default
          <s:param name="address" value="${order.address.id}"/>
        </s:link>
      </td>
    </tr>

    <c:if test="${order.orderStatus.id == orderStatusPending || order.orderStatus.id == orderStatusHold}">
      <tr>
        <td><s:link beanclass="com.hk.web.action.admin.ChangeOrderAddressAction" event="pre">
          <img src="<hk:vhostImage/>/images/admin/icon_edit_add.png" alt="Change Address"
               title="Change Address"/> Change Address
          <s:param name="order" value="${order.id}"/>
        </s:link></td>
      </tr>
    </c:if>
  </table>
</td>
<td class="has_table">

  <table>
    <c:forEach items="${order.cartLineItems}" var="lineItem">
      <c:if test="${lineItem.lineItemType.id == lineItemType_Product}">
        <tr>
          <td width="300">
            <c:if test="${lineItem.comboInstance != null}">
            <span style="color:crimson;text-decoration:underline">
              (Part of Combo: ${lineItem.comboInstance.combo.name})
            </span><br/>
            </c:if>${lineItem.productVariant.product.name}<br/>
              <span class="sml gry em">
                <c:forEach items="${lineItem.productVariant.productOptions}" var="productOption">
                  ${productOption.name} ${productOption.value}
                </c:forEach>
              </span>
          </td>
          <td>
              ${lineItem.qty}
          </td>
        </tr>
      </c:if>
    </c:forEach>
  </table>

  <s:link beanclass="com.hk.web.action.admin.order.OrderLifecycleAction" event="pre" target="_blank">
    Order Lifecycle
    <s:param name="order" value="${order}"/>
  </s:link>

  <s:link beanclass="com.hk.web.action.admin.order.OrderCommentAction" event="pre" target="_blank">
    <c:if test="${!empty order.comments}">
      <text style="color:red; font-weight:bold">Comments</text>
    </c:if>
    <c:if test="${empty order.comments}">Comments</c:if>
    <s:param name="order" value="${order}"/>
  </s:link>
</td>
<td class="has_table">
  <c:if test="${not empty order.shippingOrders}">
    <table>
      <c:forEach items="${order.shippingOrders}" var="shippingOrder">
        <tr>
          <td>
            GatewayId:
            <s:link beanclass="com.hk.web.action.admin.order.search.SearchShippingOrderAction"
                    event="searchShippingOrder"
                    target="_blank">
              <s:param name="shippingOrderGatewayId" value="${shippingOrder.gatewayOrderId}"/>
              ${shippingOrder.gatewayOrderId} </s:link><br/>
            Status: ${shippingOrder.orderStatus.name} <br/>
            <c:if test="${shippingOrder.shipment !=null}">
              Track Link: <s:link beanclass="com.hk.web.action.TrackCourierAction" target="_blank">
              <s:param name="courierId" value="${shippingOrder.shipment.courier.id}"/>
              <s:param name="trackingId" value="${shippingOrder.shipment.trackingId}"/>
              ${shippingOrder.shipment.trackingId}
            </s:link>
            </c:if>
          </td>
        </tr>
      </c:forEach>
    </table>
  </c:if>
</td>
</tr>

</c:forEach>
</table>
<s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${orderAdmin}"/>
<s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${orderAdmin}"/>

</s:layout-component>

</s:layout-render>
