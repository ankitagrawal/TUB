<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.report.ReportAction" var="reportBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Report Generator">

  <s:layout-component name="heading">Category Performance Report</s:layout-component>
  <s:layout-component name="content">
    <strong>Total Orders# ${reportBean.totalOrders}</strong> &nbsp;|&nbsp;
    <strong>Total Distinct Orders# ${reportBean.totalDistinctOrders}</strong> &nbsp;|&nbsp;
    <strong>Total Mix Orders# ${reportBean.totalOrders - reportBean.totalDistinctOrders}</strong>

    <table class="align_top" width="100%">
      <thead>
      <tr>
        <th>CATEGORY</th>
        <th>DISTINCT_ORDERS_COUNT</th>
        <th>MIXED_ORDERS_COUNT</th>
        <th>TOTAL_ORDERS_COUNT</th>
        <th>SKU_COUNT</th>
        <th>SUM_MRP</th>
        <th>SUM_HK_PRICE</th>
        <th>SUM_HK_PRICE_POST_DISCOUNT</th>
      </tr>
      </thead>

      <c:forEach items="${reportBean.categoryPerformanceList}" var="categoryPerformanceList">
        <tr>
          <td>
              ${categoryPerformanceList.category.displayName}
          </td>
          <td>
              ${categoryPerformanceList.distinctOrders}
          </td>
          <td>
              ${categoryPerformanceList.mixedOrders}
          </td>
          <td>
              ${categoryPerformanceList.distinctOrders + categoryPerformanceList.mixedOrders}
          </td>
          <td>
              ${categoryPerformanceList.skuCount}
          </td>
          <td>
              ${categoryPerformanceList.sumOfMrp}
          </td>
          <td>
              ${categoryPerformanceList.sumOfHkPrice}
          </td>
          <td>
              ${categoryPerformanceList.sumOfHkPricePostAllDiscounts}
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