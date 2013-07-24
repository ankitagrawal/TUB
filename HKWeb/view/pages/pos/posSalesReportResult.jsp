<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.report.ReportAction" var="reportActionBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Report Master">

  <s:layout-component name="htmlHead">

    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>

  </s:layout-component>

  <s:layout-component name="heading">Report Sales Result
  </s:layout-component>
  <s:layout-component name="content">
    <s:useActionBean beanclass="com.hk.web.action.admin.pos.POSReportAction" event="pre" var="posBean"/>
    <table class="cont" width="100%">
    <tr>
      <th>Cash Collection</th>
      <th>Cash Refund</th>
      <th>Credit Card Collection</th>
      <th>Items Sold</th>
      <th>Items Returned</th>
      <th>Total Collection</th>
      <th>Avg Collection</th>
      <th>Avg Items per Invoice</th>
    </tr>
    <tr>
      <td>${posBean.posSummaryDto.cashAmountCollected}</td>
      <td>${posBean.posSummaryDto.cashAmountRefunded}</td>
      <td>${posBean.posSummaryDto.creditCardAmountCollected}</td>
      <td>${posBean.posSummaryDto.itemsSold}</td>
      <td>${posBean.posSummaryDto.itemsReturned}</td>
      <td>${posBean.posSummaryDto.totalCollection}</td>
      <td>${posBean.posSummaryDto.avgAmtPerInvoice}</td>
      <td>${posBean.posSummaryDto.apc}</td>
    </tr>
    <table>

    <table class="cont" width="100%">
    <tr>
      <th>Order No</th>
      <th>Status</th>
      <th>Amt</th>
      <th>Payment Mode</th>
      <th>Items Total</th>
      <th>Discount</th>
      <th>Loyalty Points Redeemed</th>
    </tr>
    <c:forEach items="${posBean.posSaleItems}" var="sale">
      <tr>
        <td>${sale.order.id}</td>
        <c:forEach items="${sale.order.shippingOrders}" var="saleOrder">
          <td>${saleOrder.shippingOrderStatus.name} </td>
        </c:forEach>
        <td>${sale.order.amount}</td>
        <c:forEach items="${sale.order.payments}" var="payment">
          <td>${payment.paymentMode.name} </td>
        </c:forEach>
        <td>${fn:length(sale.order.cartLineItems)}</td>
        <td>${sale.discount}</td>
        <td>${sale.order.rewardPointsUsed}</td>
      </tr>

    </c:forEach>
    <table>

  </s:layout-component>
</s:layout-render>
