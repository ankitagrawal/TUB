<%@ taglib prefix="s" uri="http://stripes.sourceforge.net/stripes-dynattr.tld" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.constants.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.report.ReportAction" var="reportBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Master Performance Report">

  <s:layout-component name="heading">Master Performance Report (<label style="color:#996600">1. Customer Care</label>
    <label style="color:#003399">2. Category</label> <label style="color:#006600">3.
      Logistics</label>)</s:layout-component>
  <s:layout-component name="content">
    <em class="add_message">Sunday's data is clubbed with Monday's. Cut-off Time = 4PM (Order); 4:15PM (COD Confirmation); 4:30PM (Escalation)</em>

      <table class="header" width="100%" >
         <tr>
           <th></th>   <th></th>
          <th  align=" center" colspan="5" > BASE ORDER </th>
          <th  align="center" colspan="8" > SHIPPING ORDER </th>
          </tr>
      </table>

      <table class="zebra_vert" width="100%">
      <thead>

      <tr>
        <th>Order Date</th>
        <th>Day</th>
        <th>Placed Orders</th>
        <th>TPSL Orders</th>
        <th style="background:#996600">COD Orders</th>
        <th style="background:#996600">Confirmed COD Orders</th>
        <th style="background:#996600">%Confirmation</th>
        <th style="background:#003399">Escalable Orders</th>
        <th style="background:#003399">Esc to Packing</th>
        <th style="background:#003399">Back to Action</th>
        <th style="background:#003399">%Esc (Escalable)</th>
        <th style="background:#006600">Packed Orders</th>
        <th style="background:#006600">%Packed</th>
        <th style="background:#006600">Shipped Orders</th>
        <th style="background:#006600">%Shipped (Ordered)</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach items="${reportBean.orderLifecycleStateTransitionDtoList}" var="olcstDto">
        <tr class="">
          <td><fmt:formatDate value="${olcstDto.activityDate}"/></td>
          <td><fmt:formatDate value="${olcstDto.activityDate}" pattern="E"/></td>
          <td>${fn:length(olcstDto.placedOrders)}</td>
          <td>${fn:length(olcstDto.tpslOrders)}</td>
          <td>${fn:length(olcstDto.codOrders)}</td>
          <td>${fn:length(olcstDto.confirmedCODOrders)}</td>
          <td>
            <fmt:formatNumber value="${(fn:length(olcstDto.confirmedCODOrders))/(fn:length(olcstDto.codOrders)) * 100}" pattern="#.#"/>%
          </td>
          <td>${fn:length(olcstDto.escalableOrders)}</td>
          <td>${fn:length(olcstDto.ordersEscalatedToPackingQeueue)}</td>
          <td>${fn:length(olcstDto.ordersPushedBackToActionQueue)}</td>
          <td>
            <s:link beanclass="com.hk.web.action.report.ReportAction" event="getUnescalatedOrders" target="_blank">
              <s:param name="activityDate" value="${olcstDto.activityDate}"/>
              <fmt:formatNumber value="${(fn:length(olcstDto.ordersEscalatedToPackingQeueue) - fn:length(olcstDto.ordersPushedBackToActionQueue))/(fn:length(olcstDto.escalableOrders)) * 100}" pattern="#.#"/>%
            </s:link></td>
          <td>${fn:length(olcstDto.ordersEscalatedToShipmentQueue)}</td>
          <td>
            <fmt:formatNumber value="${(fn:length(olcstDto.ordersEscalatedToShipmentQueue)) /(fn:length(olcstDto.ordersEscalatedToPackingQeueue) - fn:length(olcstDto.ordersPushedBackToActionQueue)) * 100}" pattern="#.#"/>%
          </td>
          <td>${fn:length(olcstDto.shippedOrders)}</td>
          <td>
            <fmt:formatNumber value="${fn:length(olcstDto.shippedOrders) /fn:length(olcstDto.placedOrders) * 100}" pattern="#.#"/>%
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>

  </s:layout-component>
</s:layout-render>