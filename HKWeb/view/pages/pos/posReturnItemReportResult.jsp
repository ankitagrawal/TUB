<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.report.ReportAction" var="reportActionBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Report Master">

  <s:layout-component name="htmlHead">

    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>

  </s:layout-component>

  <s:layout-component name="heading">Return Items
  </s:layout-component>
  <s:layout-component name="content">
    <s:useActionBean beanclass="com.hk.web.action.admin.pos.POSReportAction" event="pre" var="posBean"/>

    <table class="cont" width="100%">
    <tr>
      <th>Return ID</th>
      <th>Order ID</th>
      <th>Status</th>
      <th>Amt</th>
      <th>Total Items Returned</th>
      <th>Items Returned</th>
    </tr>
    <c:forEach items="${posBean.returnItemList}" var="returnItem">
      <tr>
        <td>${returnItem.shippingOrder.id} </td>
        <td>${returnItem.shippingOrder.baseOrder.id} </td>
        <td>${returnItem.returnReason}</td>
        <td><fmt:formatNumber value="${returnItem.amount}" maxFractionDigits="2"/></td>
        <td>${fn:length(returnItem.reverseLineItems)}</td>
        <td><c:forEach items="${returnItem.reverseLineItems}" var="reverseItem">
          <table>
            <tr>
              <td>${reverseItem.referredLineItem.cartLineItem.productVariant.id}</td>
              <td>${reverseItem.referredLineItem.cartLineItem.productVariant.product.name}</td>
            </tr>
          </table>
        </c:forEach></td>
      </tr>

    </c:forEach>
    <table>

  </s:layout-component>
</s:layout-render>
