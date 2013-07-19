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
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>

  </s:layout-component>

  <s:layout-component name="heading">Report Master : Orders# ${reportActionBean.totalOrders}
    &nbsp;|&nbsp; MRP:<fmt:formatNumber value="${reportActionBean.sumOfMrp}" type="currency" currencySymbol=" "
    maxFractionDigits="0"/>
    &nbsp;|&nbsp; HKPrice:<fmt:formatNumber value="${reportActionBean.sumOfHkPrice}" type="currency" currencySymbol=" "
    maxFractionDigits="0"/>
  </s:layout-component>
  <s:layout-component name="content">
    <s:useActionBean beanclass="com.hk.web.action.admin.pos.POSAction" event="pre" var="posBean"/>

    <table class="cont" width="100%">
    <tr>
      <th>Cash</th>
      <th>Credit</th>
      <th>Total Sales</th>
      <th>Avg Per Invoice</th>
      <th>Items Total</th>
      <th>Discount</th>
      <th>Collection</th>
    </tr>
    <c:forEach items="${posBean.saleList}" var="sale" >
      <tr>
      <td>${sale.amount}</td>
      <td>${sale.paymentMode}</td>
<%--      <td>${sale.totalSale}</td>
      <td>${sale.avg}</td>
      <td>${sale.avg}</td>
      <td>${sale.avg}</td>
      <td>${sale.avg}</td>
      <td>${sale.avg}</td>--%>
      </tr>

    </c:forEach>
    <table>


  </s:layout-component>
</s:layout-render>
