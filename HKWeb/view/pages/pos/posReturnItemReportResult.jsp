<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.pact.service.core.WarehouseService" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.constants.shippingOrder.EnumShippingOrderStatus" %>
<%@ page import="com.hk.pact.dao.BaseDao" %>
<%@ page import="com.hk.domain.order.ShippingOrderStatus" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.report.ReportAction" var="reportActionBean"/>
<%
  WarehouseService warehouseService = ServiceLocatorFactory.getService(WarehouseService.class);
  BaseDao baseDao = ServiceLocatorFactory.getService(BaseDao.class);
  pageContext.setAttribute("whList", warehouseService.getAllActiveWarehouses());
  pageContext.setAttribute("soStatusList", baseDao.getAll(ShippingOrderStatus.class));
%>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Report Master">

  <s:layout-component name="htmlHead">

    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>

  </s:layout-component>

  <s:layout-component name="heading">Report Master
    </s:layout-component>
  <s:layout-component name="content">
    <s:useActionBean beanclass="com.hk.web.action.admin.pos.POSAction" event="pre" var="posBean"/>

    <table class="cont" width="100%">
    <tr>
      <th>Order No</th>
      <th>Status</th>
      <th>Amt</th>
      <th>Payment Mode</th>
      <th>Items Returned</th>
    </tr>
    <c:forEach items="${posBean.returnItemList}" var="returnItem">
      <tr>
        <td>${returnItem.id}</td>
        <td>${returnItem.orderStatus.name}</td>
        <td>${returnItem.amount}</td>
        <c:forEach items="${returnItem.payments}" var="payment" ><td>${returnItem.paymentMode.name} </td> </c:forEach>
        <td>${fn:length(returnItem.cartLineItems)}</td>
      </tr>

    </c:forEach>
    <table>

  </s:layout-component>
</s:layout-render>
