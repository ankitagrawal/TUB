<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.constants.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.report.ReportAction" var="reportBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Sales by Date Report">

  <s:layout-component name="heading">Sale-by-date Report</s:layout-component>
  <s:layout-component name="content">


    <table class="align_top" width="100%">
      <thead>
      <tr>
        <th>ORDER_DATE</th>
        <th>DAY_OF_WEEK</th>
        <th>TXN_COUNT</th>
        <th>SKU_COUNT</th>
        <th>SUM_MRP</th>
        <th>SUM_HK_PRICE</th>
      </tr>
      </thead>

      <tr>
        <td colspan="2" style="font-weight:bold; font-size:14px;">Avg Values</td>
        <td style="font-weight:bold; font-size:14px;">
          <fmt:formatNumber value="${reportBean.avgTxn}" pattern="<%=FormatUtils.currencyFormatPattern%>"/></td>
        <td style="font-weight:bold; font-size:14px;">
          <fmt:formatNumber value="${reportBean.avgSku}" pattern="<%=FormatUtils.currencyFormatPattern%>"/></td>
        <td style="font-weight:bold; font-size:14px;">
          <fmt:formatNumber value="${reportBean.avgMrp}" pattern="<%=FormatUtils.currencyFormatPattern%>"/></td>
        <td style="font-weight:bold; font-size:14px;">
          <fmt:formatNumber value="${reportBean.avgHkp}" pattern="<%=FormatUtils.currencyFormatPattern%>"/></td>
      </tr>

      <c:forEach items="${reportBean.daySaleList}" var="daySaleList">
        <tr>
          <td><fmt:formatDate value="${daySaleList.orderDate}"/></td>
          <td><fmt:formatDate value="${daySaleList.orderDate}" pattern="E"/></td>
          <td>${daySaleList.txnCount}</td>
          <td>${daySaleList.skuCount}</td>
          <td>${daySaleList.sumOfMrp}</td>
          <td>${daySaleList.sumOfHkPrice}</td>
        </tr>
      </c:forEach>
    </table>
    <s:form beanclass="com.hk.web.action.report.ReportAction" autocomplete="off">
      <div class="buttons"><s:submit name="generateSalesByDateExcel" value="Download"/></div>
      <s:hidden name="startDate" value="${startDate}"/>
      <s:hidden name="endDate" value="${endDate}"/>
    </s:form>
  </s:layout-component>
</s:layout-render>