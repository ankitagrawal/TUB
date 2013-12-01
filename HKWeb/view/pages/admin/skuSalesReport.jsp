<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.constants.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.report.ReportAction" var="reportBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="SKU Sales Report">

  <s:layout-component name="heading">SKU Sales Report</s:layout-component>
  <s:layout-component name="content">

    <table class="align_top" width="100%">
      <thead>
      <tr>
        <th>CATEGORY</th>
        <th>BRAND</th>
        <th>ITEM_NAME</th>
        <th>VARIANT_DETAILS</th>
        <th>COST_PRICE</th>
        <th>MRP</th>
        <th>OFFER_PRICE</th>
        <th>QTY</th>
        <th>INVENTORY</th>
        <th>NET_SALES</th>
        <th></th>
      </tr>
      </thead>

      <c:forEach items="${reportBean.categorySalesDtoList}" var="categorySalesDtoList">
        <tr>
          <td>
              ${categorySalesDtoList.topLevelCategory.displayName}
          </td>
          <td>
              ${categorySalesDtoList.productVariant.product.brand}
          </td>
          <td>
              ${categorySalesDtoList.productVariant.product.name}
          </td>
          <td>
              ${categorySalesDtoList.variantDetails}
          </td>
          <td>
              ${categorySalesDtoList.productVariant.costPrice}
          </td>         
          <td>
              ${categorySalesDtoList.productVariant.markedPrice}
          </td>
          <td>
              ${categorySalesDtoList.productVariant.hkPrice}
          </td>
          <td>
              ${categorySalesDtoList.qty}
          </td>
          <td>
              ${categorySalesDtoList.productVariant.netInventory}
          </td>
          <td>
            <fmt:formatNumber value="${categorySalesDtoList.productVariant.hkPrice * categorySalesDtoList.qty}" pattern="<%=FormatUtils.currencyFormatPattern%>"/>
          </td>
        </tr>
      </c:forEach>
    </table>
    <s:form beanclass="com.hk.web.action.report.ReportAction" autocomplete="off">
      <div class="buttons"><s:submit name="generateSKUSalesReport" value="Download"/></div>
      <s:hidden name="startDate" value="${startDate}"/>
      <s:hidden name="endDate" value="${endDate}"/>
      <s:hidden name="topLevelCategory" value="${topLevelCategory}"/>
    </s:form>
  </s:layout-component>
</s:layout-render>
