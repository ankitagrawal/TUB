<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/default.jsp" pageTitle="Payment is pending approval">

  <s:layout-component name="menu"> </s:layout-component>
  <s:layout-component name="heading">
    <div style="margin-top: 50px;">
      <h2 class="blue strikeline">
        Payment Pending Approval
      </h2>
    </div>
  </s:layout-component>

  <s:layout-component name="left_col">
    <s:useActionBean beanclass="com.hk.web.action.core.payment.PaymentPendingApprovalAction" var="actionBean"/>

    <c:choose>
      <c:when test="${actionBean.payment != null}">
        <h5>Your payment is pending approval.</h5>

        <p>
          Your order ID is <strong class="orange">${actionBean.payment.order.gatewayOrderId}</strong>.
        </p>

        <p>Your order will be processed as soon as your payment is confirmed.</p>

        <h2>Shipping & Delivery</h2>

        <p>Your order will be shipped as per the days mentioned against each product. Additional time will be taken by
          the courier company.</p>

        <h2>Customer Support</h2>

        <p><s:link beanclass="com.hk.web.action.pages.ContactAction">Write to us</s:link> with your Order ID if you have
          any questions.</p>
      </c:when>
      <c:otherwise>
        Invalid request!
      </c:otherwise>
    </c:choose>

  </s:layout-component>

</s:layout-render>
