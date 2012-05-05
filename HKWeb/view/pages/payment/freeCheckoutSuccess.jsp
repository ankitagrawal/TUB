<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:layout-render name="/layouts/default.jsp" pageTitle="Payment Successful">

  <s:layout-component name="menu"> </s:layout-component>
  <s:layout-component name="heading">Order Confirmed</s:layout-component>

  <s:layout-component name="lhsContent">
    <s:useActionBean beanclass="com.hk.web.action.core.payment.FreeCheckoutSuccessAction" var="actionBean"/>

    <c:choose>
      <c:when test="${actionBean.payment != null}">
        <p>Your order has been confirmed. Your order ID is <strong>${actionBean.payment.order.gatewayOrderId}</strong>.</p>
        <h2>Shipping & Delivery</h2>
        <p>Your order will be shipped within 3-4 days. Additional time will be taken by the courier company.</p>
        <h2>Customer Support</h2>
        <p>Write to info@healthkart.com with your Order ID if you have any questions.</p>
      </c:when>
      <c:otherwise>
        Invalid request!
      </c:otherwise>
    </c:choose>

  </s:layout-component>

</s:layout-render>
