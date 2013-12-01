<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.constants.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.report.ReportAction" var="reportBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Shipment Performance Report">

  <s:layout-component name="heading">Shipment Performance Report</s:layout-component>
  <s:layout-component name="content">
    <em class="add_message">Sunday's data is clubbed with Monday's. Order Placement Cut-off Time = 5PM </em>

    <table class="align_top" width="100%">
      <thead>
      <tr>
        <th>ORDER DATE</th>
        <th>DAY</th>
        <th>ORDER PLACED</th>
        <th>% SHIPPED SAME DAY</th>
        <th>% SHIPPED NEXT DAY</th>
        <th>% SHIPPED ON DAY TWO</th>
      </tr>
      </thead>

     <%-- <tr>
        <td colspan="2" style="font-weight:bold; font-size:14px;">Avg Values</td>
        <td style="font-weight:bold; font-size:14px;">
          <fmt:formatNumber value="${reportBean.avgTxn}" pattern="<%=FormatUtils.currencyFormatPattern%>"/></td>
        <td style="font-weight:bold; font-size:14px;">
          <fmt:formatNumber value="${reportBean.avgSameDayShipping}" pattern="<%=FormatUtils.currencyFormatPattern%>"/></td>
        <td style="font-weight:bold; font-size:14px;">
          <fmt:formatNumber value="${reportBean.avgNextDayShipping}" pattern="<%=FormatUtils.currencyFormatPattern%>"/></td>
        <td style="font-weight:bold; font-size:14px;">
          <fmt:formatNumber value="${reportBean.avgShippingOnDayTwo}" pattern="<%=FormatUtils.currencyFormatPattern%>"/></td>

      </tr>--%>

      <c:forEach items="${reportBean.shipmentDtoList}" var="shipmentDto">
        <tr>
          <td>
            <fmt:formatDate value="${shipmentDto.orderDate}"/>
          </td>
          <td>
            <fmt:formatDate value="${shipmentDto.orderDate}" pattern="E"/>
          </td>
          <td>
              ${shipmentDto.txnCount}
          </td>
          <td>
            <fmt:formatNumber value="${shipmentDto.shippedSameDay /shipmentDto.txnCount * 100}" pattern="<%=FormatUtils.currencyFormatPattern%>"/>
          </td>
          <td>
            <fmt:formatNumber value="${shipmentDto.shippedNextDay /shipmentDto.txnCount * 100}" pattern="<%=FormatUtils.currencyFormatPattern%>"/>
          </td>
          <td>
            <fmt:formatNumber value="${shipmentDto.shippedOnDayTwo /shipmentDto.txnCount * 100}" pattern="<%=FormatUtils.currencyFormatPattern%>"/>
          </td>

        </tr>
      </c:forEach>
    </table>
    
  </s:layout-component>
</s:layout-render>