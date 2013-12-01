<%@ page import="com.hk.constants.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.report.ReportAction" var="reportBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Report Generator">

  <s:layout-component name="heading">Category Performance Report</s:layout-component>
  <s:layout-component name="content">
    <strong>Yesterday's Count</strong><br><br>
    <strong>Yesterday Orders# ${reportBean.categoriesOrderReportDtosMap["YesterdayCategoriesOrderReportDto"].totalOrders}</strong> &nbsp;|&nbsp;
    <strong>Yesterday Sale
        <fmt:formatNumber value="${reportBean.categoriesOrderReportDtosMap.YesterdayCategoriesOrderReportDto.totalSumOfMrp}" type="currency" currencySymbol=" " maxFractionDigits="0"/></strong> &nbsp;|&nbsp;
    <strong>Yesterday Distinct Orders# ${reportBean.categoriesOrderReportDtosMap["YesterdayCategoriesOrderReportDto"].totalDistinctOrders}</strong> &nbsp;|&nbsp;
    <strong>Yesterday Mix Orders# ${reportBean.categoriesOrderReportDtosMap["YesterdayCategoriesOrderReportDto"].totalMixedOrder}</strong>

    <br><br><br>
    <strong>Monthly Count</strong><br><br>
    <strong>Total Orders# ${reportBean.categoriesOrderReportDtosMap.MonthlyCategoriesOrderReportDto.totalOrders}</strong> &nbsp;|&nbsp;
    <strong>Total Sale
        <fmt:formatNumber value="${reportBean.categoriesOrderReportDtosMap.MonthlyCategoriesOrderReportDto.totalSumOfMrp}" type="currency" currencySymbol=" " maxFractionDigits="0" /></strong> &nbsp;|&nbsp;
    <strong>Total Distinct Orders# ${reportBean.categoriesOrderReportDtosMap.MonthlyCategoriesOrderReportDto.totalDistinctOrders}</strong> &nbsp;|&nbsp;
    <strong>Total Mix Orders# ${reportBean.categoriesOrderReportDtosMap.MonthlyCategoriesOrderReportDto.totalMixedOrder}</strong>

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

        <th>MTD AVG #</th>
        <th>TARGET #</th>
        <th>MTD AVG SALES</th>
        <th>YEST #</th>
        <th>YEST SALES</th>
        <th>MTD SALES</th>
        <th>PROJECTED SALES</th>
        <th>TARGET SALES</th>

      </tr>
      </thead>

      <c:forEach items="${reportBean.categoriesOrderReportDtosMap.MonthlyCategoriesOrderReportDto.categoryPerformanceDtoList}" var="categoryPerformanceDto" varStatus="ctr">
        <tr>
          <td>
              ${categoryPerformanceDto.category.displayName}
          </td>
          <%--<td>
              ${categoryPerformanceDto.totalOrders}
          </td>--%>
          <td>
              <fmt:formatNumber value="${categoryPerformanceDto.avgOfTotalOrders}" type="number" maxFractionDigits="0"/> yest # ${reportBean.categoriesOrderReportDtosMap.YesterdayCategoriesOrderReportDto.categoryPerformanceDtoList[ctr.index].totalOrders}
          </td>
          <td>
              <fmt:formatNumber value="${reportBean.targetOrderCountMap[categoryPerformanceDto.category.name]}" type="number" maxFractionDigits="0"/>
          </td>
          <td>
              <fmt:formatNumber value="${categoryPerformanceDto.avgOfMrp}" type="currency" currencySymbol=" " maxFractionDigits="0"/>
          </td>
          <td>
              ${reportBean.categoriesOrderReportDtosMap.YesterdayCategoriesOrderReportDto.categoryPerformanceDtoList[ctr.index].totalOrders}
          </td>
          <td>
              <fmt:formatNumber value="${reportBean.categoriesOrderReportDtosMap.YesterdayCategoriesOrderReportDto.categoryPerformanceDtoList[ctr.index].sumOfMrp}" type="currency" currencySymbol=" " maxFractionDigits="0"/>
          </td>
          <td>
              <fmt:formatNumber value="${categoryPerformanceDto.sumOfMrp}" type="currency" currencySymbol=" " maxFractionDigits="0"/>
          </td>
          <td>
              <fmt:formatNumber value="${categoryPerformanceDto.projectedMrp}" type="currency" currencySymbol=" " maxFractionDigits="0"/>
          </td>
          <td>
              <fmt:formatNumber value="${reportBean.targetMrpSalesMap[categoryPerformanceDto.category.name]}" type="currency" currencySymbol=" " maxFractionDigits="0"/>
          </td>

        </tr>
      </c:forEach>
    </table>

    <%--<table  border = "10" width="20%">
      <thead>
      <tr>
        <th>YEST #</th>
        <th>YEST SALES</th>
      </tr>
      </thead>

      <c:forEach items="${reportBean.categoriesOrderReportDtosMap.YesterdayCategoriesOrderReportDto.categoryPerformanceDtoList}" var="categoryPerformanceDto">
        <tr>

          <td>
              ${categoryPerformanceDto.totalOrders}
          </td>
          <td>
              <fmt:formatNumber value="${categoryPerformanceDto.sumOfMrp}" type="currency" currencySymbol=" " maxFractionDigits="0"/>
          </td>

        </tr>
      </c:forEach>
    </table>--%>

    <%--<table  width="30%">
      <thead>
      <tr>
        <th>MTD SALES</th>
        <th>PROJECTED SALES</th>
        <th>TARGET SALES</th>
      </tr>
      </thead>

      <c:forEach items="${reportBean.categoriesOrderReportDtosMap.MonthlyCategoriesOrderReportDto.categoryPerformanceDtoList}" var="categoryPerformanceDto">
        <tr>
            <td>
                <fmt:formatNumber value="${categoryPerformanceDto.sumOfMrp}" type="currency" currencySymbol=" " maxFractionDigits="0"/>
            </td>
            <td>
                <fmt:formatNumber value="${categoryPerformanceDto.projectedMrp}" type="currency" currencySymbol=" " maxFractionDigits="0"/>
            </td>
            <td>
                <fmt:formatNumber value="${reportBean.targetMrpSalesMap[categoryPerformanceDto.category.name]}" type="currency" currencySymbol=" " maxFractionDigits="0"/>
            </td>
        </tr>
      </c:forEach>
    </table>--%>



    </div>

    <s:form beanclass="com.hk.web.action.report.ReportAction" autocomplete="off">
      <%--<div class="buttons"><s:submit name="generateSalesByDateReport" value="Download"/></div>--%>
      <%--<s:hidden name="startDate" value="${startDate}"/>--%>
      <%--<s:hidden name="endDate" value="${endDate}"/>--%>
    </s:form>
  </s:layout-component>
</s:layout-render>