<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.assistly.OrderInfoAction" var="orderInfoBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp">
  <s:layout-component name="heading">Order Info</s:layout-component>
  <s:layout-component name="content">
    <h2>No order found by order id associated with this case.</h2>
    <p>
      Order id : <strong>${orderInfoBean.orderId}</strong>
    </p>
  </s:layout-component>
</s:layout-render>
