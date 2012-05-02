<%@ page import="com.hk.constants.order.EnumOrderStatus" %>
<%@ page import="com.hk.constants.payment.EnumPaymentStatus" %>
<%@ page import="com.hk.constants.payment.EnumPaymentMode" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.CheckPaymentAction" var="checkPaymentBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Welcome">

  <s:layout-component name="heading">${checkPaymentBean.currentBreadcrumb.name}</s:layout-component>

  <c:set var="orderStatusInCart" value="<%=EnumOrderStatus.InCart.getId()%>"/>
  <c:set var="orderStatusCancelled" value="<%=EnumOrderStatus.Cancelled.getId()%>"/>
  <c:set var="orderStatusPlaced" value="<%=EnumOrderStatus.Placed.getId()%>"/>
  <c:set var="orderStatusSplit" value="<%=EnumOrderStatus.InProcess.getId()%>"/>
  <c:set var="paymentStatusAuthPending" value="<%=EnumPaymentStatus.AUTHORIZATION_PENDING.getId()%>"/>
  <c:set var="paymentStatusRequested" value="<%=EnumPaymentStatus.REQUEST.getId()%>"/>
  <c:set var="paymentStatusSuccess" value="<%=EnumPaymentStatus.SUCCESS.getId()%>"/>
  <c:set var="paymentModeCod" value="<%=EnumPaymentMode.COD.getId()%>"/>

  <s:layout-component name="content">

    <h2>Order Id ${checkPaymentBean.order.id}, Order Status: ${checkPaymentBean.order.orderStatus.name}</h2>

    <h3>
      <c:if test="${!(checkPaymentBean.order.orderStatus.id == orderStatusCancelled || checkPaymentBean.order.orderStatus.id == orderStatusInCart)}">
        <s:link beanclass="com.hk.web.action.admin.order.CancelOrderAction" event="pre" onclick="return confirm('Are you sure?')">
          Cancel Order
          <s:param name="order" value="${checkPaymentBean.order.id}"/>
        </s:link>
      </c:if>
    </h3>
    <table class="cont order_summary">
      <thead>
      <tr>
        <th>Product</th>
        <th>Chosen Options</th>
        <th>Quantity</th>
        <th>Subtotal</th>
      </tr>
      </thead>
      <c:forEach items="${checkPaymentBean.pricingDto.productLineItems}" var="lineItem" varStatus="lineItemCtr">
        <tbody>
        <tr>
          <td>${lineItem.productVariant.product.name}</td>
          <td>
            <c:forEach items="${lineItem.productVariant.productOptions}" var="productOption">${productOption.name}
              <br/>${productOption.value}
            </c:forEach>
          </td>
          <td>
            <fmt:formatNumber value="${lineItem.qty}"/>
            <c:if test="${checkPaymentBean.pricingDto.shippingLineCount > 1}">
              x ${checkPaymentBean.pricingDto.shippingLineCount} (addresses)
            </c:if>
          </td>
          <td><fmt:formatNumber value="${lineItem.hkPrice * lineItem.qty}" type="currency" currencySymbol="Rs. "/></td>
        </tr>
        </tbody>
      </c:forEach>
      <tfoot>
      <tr>
        <td colspan="3"><strong>Item total</strong></td>
        <td>
          <fmt:formatNumber value="${checkPaymentBean.pricingDto.productsHkSubTotal}" type="currency" currencySymbol="Rs. "/></td>
      </tr>
      <tr>
        <td colspan="3"><strong>Shipping</strong></td>
        <td>
          <fmt:formatNumber value="${checkPaymentBean.pricingDto.shippingSubTotal - checkPaymentBean.pricingDto.shippingDiscount}" type="currency" currencySymbol="Rs. "/></td>
      </tr>
      <tr>
        <td colspan="3"><strong>Discount</strong></td>
        <td>
          <fmt:formatNumber value="${checkPaymentBean.pricingDto.totalDiscount - checkPaymentBean.pricingDto.shippingDiscount}" type="currency" currencySymbol="Rs. "/></td>
      </tr>
      <tr>
        <td colspan="3"><strong>Grand Total</strong></td>
        <td>
          <fmt:formatNumber value="${checkPaymentBean.pricingDto.grandTotalPayable}" type="currency" currencySymbol="Rs. "/></td>
      </tr>
      </tfoot>
    </table>
    <h3>Address(es)</h3>

    <p>
      <c:choose>
        <c:when test="${checkPaymentBean.order.address == null}">
          No address associated with this order.
          <s:link beanclass="com.hk.web.action.admin.SelectAddressAction" event="pre">
            Please select atleast 1 address to continue.
            <s:param name="order" value="${checkPaymentBean.order.id}"/>
          </s:link>
        </c:when>
        <c:otherwise>
          <s:link beanclass="com.hk.web.action.admin.ChangeOrderAddressAction" event="pre">
            Change Address
            <s:param name="order" value="${checkPaymentBean.order.id}"/>
          </s:link>
        </c:otherwise>
      </c:choose>
    </p>

    <p>
    <ul>
      <c:set value="${checkPaymentBean.order.address}" var="address"/>
        <li>
            ${address.name}<br/>
            ${address.line1}<br/>
          <c:if test="${address.line2 != null}">${address.line2}</c:if>
            ${address.city} - ${address.pin}<br/>
            ${address.state}<br/>
          Ph. ${address.phone}
        </li>
        <hr/>
    </ul>
    </p>

    <s:form beanclass="com.hk.web.action.admin.CheckPaymentAction">

      <div>
        <table class="cont">
          <thead>
          <tr>
            <th colspan="10">
              Payment List
              (
              <s:link beanclass="com.hk.web.action.admin.NewPaymentAction">
                <s:param name="order" value="${checkPaymentBean.order}"/>
                <s:param name="amount" value="${checkPaymentBean.pricingDto.grandTotalPayable}"/>
                <s:param name="paymentMode" value="<%=EnumPaymentMode.TECHPROCESS_TEST.getId()%>"/>
                <s:param name="paymentStatus" value="<%=EnumPaymentStatus.SUCCESS.getId()%>"/>
                Create New Payment
              </s:link>
              )
              <br/>
            </th>
          </tr>
          <tr>
            <th></th>
            <th>gateway Id</th>
            <th>Date/time</th>
            <th>Amount</th>
            <th>Status</th>
            <th>Mode</th>
            <th>tickets</th>
          </tr>
          </thead>
          <c:forEach items="${checkPaymentBean.paymentList}" var="payment" varStatus="ctr">
            <tr>
              ${checkPaymentBean.pricingDto.grandTotalPayable} = ${payment.amount} | ${checkPaymentBean.order.orderStatus.id} eq ${orderStatusInCart}

              <c:set var="radioDisabled" value="${checkPaymentBean.pricingDto.grandTotalPayable eq payment.amount ? false : true}"/>

              <td><s:radio value="${payment.id}" name="payment" disabled="${radioDisabled}" /></td>
              <td>
                ${payment.gatewayOrderId}
                (<s:link beanclass="com.hk.web.action.admin.EditPaymentAction">
                  <s:param name="paymentId" value="${payment.id}"/>
                  Edit Payment
                </s:link>)
              </td>
              <td><fmt:formatDate value="${payment.createDate}" type="both"/></td>
              <td><fmt:formatNumber value="${payment.amount}" currencySymbol="Rs. " type="currency"/></td>
              <td>${payment.paymentStatus.name}</td>
              <td>${payment.paymentMode.name}</td>
              <td>
                <c:if test="${payment.paymentMode.id != paymentModeCod}">
                  <span class="xsml">
                    <s:link beanclass="com.hk.web.action.admin.ticket.CreateTicketAction" event="createPaymentTypeTicket">
                      Create Ticket to track with TechProcess
                      <s:param name="order" value="${payment.order.id}"/>
                      <s:param name="message" value="Track With TechProcess"/>
                      <s:param name="gatewayOrderId" value="${payment.gatewayOrderId}"/>
                      <s:param name="paymentDate" value="${hk:formatDate(payment.paymentDate)}"/>
                    </s:link> <br/>
                    <s:link beanclass="com.hk.web.action.admin.ticket.CreateTicketAction" event="createPaymentTypeTicket">
                      create Refund Ticket
                      <s:param name="order" value="${payment.order.id}"/>
                      <s:param name="message" value="Payment Refund"/>
                      <s:param name="gatewayOrderId" value="${payment.gatewayOrderId}"/>
                      <s:param name="paymentDate" value="${hk:formatDate(payment.paymentDate)}"/>
                    </s:link>
                  </span>
                </c:if>
              </td>
              <td>
                <%--<c:if test="${payment.paymentStatus.id == paymentStatusRequested && fn:length(checkPaymentBean.order.addresses) > 0}">--%>
                  <%--<s:link beanclass="com.hk.web.action.admin.BackedUpOrderSummaryAction" event="pre">--%>
                    <%--View BackedUp Order Summary--%>
                    <%--<s:param name="payment" value="${payment.id}"/>--%>
                  <%--</s:link>--%>
                <%--</c:if>--%>
              </td>
            </tr>
          </c:forEach>
        </table>
      </div>

      <div>
        <s:hidden name="order"/>

        <c:if test="${checkPaymentBean.order.orderStatus.id eq orderStatusInCart && checkPaymentBean.order.address != null}">
          <s:submit name="acceptAsAuthPending" value="Accept payment as Auth Pending" onclick="return confirm('Are you sure?')"/>
          <s:submit name="acceptAsSuccessful" value="Accept payment as Successful" onclick="return confirm('Are you sure?')"/>
        </c:if>
        <c:if test="${(checkPaymentBean.order.orderStatus.id eq orderStatusPlaced || checkPaymentBean.order.orderStatus.id eq orderStatusSplit ) && checkPaymentBean.payment.paymentStatus.id != paymentStatusSuccess}">
          <s:submit name="updateToSuccess" value="Update as successful" onclick="return confirm('Are you sure?')"/>
        </c:if>
        <c:if test="${checkPaymentBean.order.address != null}">
          <s:submit name="associateToPayment" value="Associate to payment" onclick="return confirm('Are you sure?')"/>
        </c:if>

      </div>
    </s:form>
  </s:layout-component>

</s:layout-render>
