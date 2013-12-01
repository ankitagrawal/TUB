<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.constants.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.report.ReportAction" var="reportBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Report Generator">

  <s:layout-component name="heading">Category Performance Report</s:layout-component>
  <s:layout-component name="content">
    <strong>Six Hour's Count</strong><br><br>
    <strong>Six Hour's Orders# ${reportBean.sixHourlyCategoriesOrderReportMap["SixHourlyCategoriesReportDto"].totalOrders}</strong> &nbsp;|&nbsp;
    <strong>Six Hour's Sale
        <fmt:formatNumber value="${reportBean.sixHourlyCategoriesOrderReportMap.SixHourlyCategoriesReportDto.totalSumOfMrp}" type="currency" currencySymbol=" " maxFractionDigits="0"/></strong> &nbsp;|&nbsp;
    <strong>Six Hour's Distinct Orders# ${reportBean.sixHourlyCategoriesOrderReportMap["SixHourlyCategoriesReportDto"].totalDistinctOrders}</strong> &nbsp;|&nbsp;
    <strong>Six Hour's Mix Orders# ${reportBean.sixHourlyCategoriesOrderReportMap["SixHourlyCategoriesReportDto"].totalMixedOrder}</strong>

    <br><br><br>
    <strong>Today's Count</strong><br><br>
    <strong>Total Orders# ${reportBean.sixHourlyCategoriesOrderReportMap.DailyCategoriesReportDto.totalOrders}</strong> &nbsp;|&nbsp;
    <strong>Total Sale
        <fmt:formatNumber value="${reportBean.sixHourlyCategoriesOrderReportMap.DailyCategoriesReportDto.totalSumOfMrp}" type="currency" currencySymbol=" " maxFractionDigits="0" /></strong> &nbsp;|&nbsp;
    <strong>Total Distinct Orders# ${reportBean.sixHourlyCategoriesOrderReportMap.DailyCategoriesReportDto.totalDistinctOrders}</strong> &nbsp;|&nbsp;
    <strong>Total Mix Orders# ${reportBean.sixHourlyCategoriesOrderReportMap.DailyCategoriesReportDto.totalMixedOrder}</strong>

    <style type="text/css" media="screen">
        table {
          border-collapse: collapse;
          border: 1px solid black;float:left;
        }
        th, td {
          border: 1px solid #777777;
          padding-left:  4px;
          padding-right: 4px;
        }
        th {
          background-color: #000000;
          border-bottom: 3px solid #777777;
        }
        /* Display Tag */
        /* START:odd_even */
        tr:nth-child(odd) {
          background-color:#E5F5E5;
        }

        tr:nth-child(even) {
          background-color:#FFFFFF;
        }

         #table_container{width:100%;margin:0 auto;}
    </style>
    <div id="table_container">


    <table  width="100%">
      <thead>
      <tr>
        <th>CATEGORY</th>
        <%--<th>MONTHLY_TOTAL_ORDERS_COUNT</th>--%>

        <th>DAILY COUNT </th>
        <th>DAILY  SALES</th>
        <th>SIX HOUR'S COUNT</th>
        <th>SIX HOUR'S SALES</th>

      </tr>
      </thead>

      <c:forEach items="${reportBean.sixHourlyCategoriesOrderReportMap.DailyCategoriesReportDto.categoryPerformanceDtoList}" var="categoryPerformanceDto" varStatus="ctr">
        <tr>
          <td>
              ${categoryPerformanceDto.category.displayName}
          </td>
          <td>
              <fmt:formatNumber value="${categoryPerformanceDto.totalOrders}" type="number" maxFractionDigits="0"/>
          </td>
          <td>
              <fmt:formatNumber value="${categoryPerformanceDto.sumOfMrp}" type="number" maxFractionDigits="0"/>
          </td>
          <td>
              ${reportBean.sixHourlyCategoriesOrderReportMap.SixHourlyCategoriesReportDto.categoryPerformanceDtoList[ctr.index].totalOrders}
          </td>
          <td>
              <fmt:formatNumber value="${reportBean.sixHourlyCategoriesOrderReportMap.SixHourlyCategoriesReportDto.categoryPerformanceDtoList[ctr.index].sumOfMrp}" type="currency" currencySymbol=" " maxFractionDigits="0"/>
          </td>

        </tr>
      </c:forEach>
    </table>


    <s:form beanclass="com.hk.web.action.report.ReportAction" autocomplete="off">
      <%--<div class="buttons"><s:submit name="generateSalesByDateReport" value="Download"/></div>--%>
      <%--<s:hidden name="startDate" value="${startDate}"/>--%>
      <%--<s:hidden name="endDate" value="${endDate}"/>--%>
    </s:form>
  </s:layout-component>
</s:layout-render>