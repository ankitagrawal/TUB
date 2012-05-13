<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.constants.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.report.ReportAction" var="reportBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Reconciliation Report">

  <s:layout-component name="heading">Reconciliation Report</s:layout-component>
  <s:layout-component name="content">
    <table>
      <th>AVG_SHIPMENT_DAYS</th>
      <th>AVG_DELIVERY_DAYS</th>
      <th>TOTAL AMOUNT</th>
      <tr>
        <td>${reportBean.avgShipmentTime} Days</td>
        <td>${reportBean.avgDeliveryTime} Days</td>
        <td>${reportBean.totalAmount}</td>
      </tr>
    </table>
    <br>
    <s:form beanclass="com.hk.web.action.report.ReportAction" autocomplete="off">
      <table class="align_top" width="100%">
        <thead>
        <tr>
          <th>GATEWAY_ORDER_ID</th>
          <th>ORDER_DATE</th>
          <th>NAME</th>
          <th>CITY</th>
          <th>PAYMENT_MODE</th>
          <th>COURIER</th>
          <th>TRACKING_ID</th>
          <th>PAYMENT_STATUS</th>
          <th>AMOUNT</th>
          <th>SHIPMENT_DATE</th>
          <th>DELIVERY_DATE</th>
          <th>PROCESSED_STATUS</th>
          <th></th>
        </tr>
        </thead>

        <c:forEach items="${reportBean.orderList}" var="reconciledOrder" varStatus="ctr">
          <tr>
            <td>
              <s:hidden name="orderList[${ctr.index}]"/>
                ${reconciledOrder.gatewayOrderId}
            </td>
            <td>
              <fmt:formatDate value="${reconciledOrder.payment.paymentDate}"/>
            </td>
            <td>
                ${reconciledOrder.address.name}
            </td>
            <td>
                ${reconciledOrder.address.city}
            </td>
            <td>
                ${reconciledOrder.payment.paymentMode.name}
            </td>
            <td>
                ${reconciledOrder.productLineItems[0].courier.name}
            </td>
            <td>
                ${reconciledOrder.productLineItems[0].trackingId}
            </td>
            <td>
                ${reconciledOrder.payment.paymentStatus.name}
            </td>
            <td>
                ${reconciledOrder.payment.amount}
            </td>
            <td>
              <fmt:formatDate value="${reconciledOrder.productLineItems[0].shipDate}"/>
            </td>
            <td>
              <fmt:formatDate value="${reconciledOrder.productLineItems[0].deliveryDate}"/>
            </td>
            <td>
              <s:select name="orderList[${ctr.index}].reconciliationStatus">
                <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="reconciliationStatus" value="id" label="name"/>
              </s:select>
            </td>
          </tr>
        </c:forEach>
      </table>
      <div class="buttons"><s:submit name="generateReconciliationReport" value="Download"/></div>
      <div class="buttons"><s:submit name="reconcile" value="Save"/></div>
      <s:hidden name="startDate" value="${startDate}"/>
      <s:hidden name="endDate" value="${endDate}"/>
      <s:hidden name="paymentMode" value="${paymentMode}"/>
      <s:hidden name="codMode" value="${codMode}"/>
      <s:hidden name="reconciliationStatus" value="${reconciliationStatus}"/>
    </s:form>
  </s:layout-component>
</s:layout-render>
