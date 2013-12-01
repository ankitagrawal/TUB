<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.constants.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.report.ReportAction" var="reportBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Sales by Ship Date Report">

  <s:layout-component name="heading">Sale-by-ship-date Report</s:layout-component>
  <s:layout-component name="content">


    <table class="align_top" width="100%">
      <thead>
      <tr align="center">
        <th>SHIP DATE</th>
        <th>DAY</th>
        <th align="center" colspan=6> Totals For Day </th>
        <th align=center colspan=6>In Shipped Status</th>
        <th align=center colspan=6>In Delivered Status</th>
        <th align=center colspan=6>In Returned Status</th>
        <th align=center colspan=6>In Cancelled Status</th>
      </tr>

      </thead>
        <tr bgcolor="antiquewhite">
            <th></th>
            <th></th>

            <th>SUM MRP</th>
            <th>SUM HK PRICE</th>
            <th>SUM OF DISCOUNTS</th>
            <th>NET SALES</th>
            <th>FORWARDING CHARGES</th>
            <th>NET RECEIVABLE</th>

            <th>SUM MRP</th>
            <th>SUM HK PRICE</th>
            <th>SUM OF DISCOUNTS</th>
            <th>NET SALES</th>
            <th>FORWARDING CHARGES</th>
            <th>NET RECEIVABLE</th>

            <th>SUM MRP</th>
            <th>SUM HK PRICE</th>
            <th>SUM OF DISCOUNTS</th>
            <th>NET SALES</th>
            <th>FORWARDING CHARGES</th>
            <th>NET RECEIVABLE</th>

            <th>SUM MRP</th>
            <th>SUM HK PRICE</th>
            <th>SUM OF DISCOUNTS</th>
            <th>NET SALES</th>
            <th>FORWARDING CHARGES</th>
            <th>NET RECEIVABLE</th>

            <th>SUM MRP</th>
            <th>SUM HK PRICE</th>
            <th>SUM OF DISCOUNTS</th>
            <th>NET SALES</th>
            <th>FORWARDING CHARGES</th>
            <th>NET RECEIVABLE</th>

        </tr>



      <c:forEach items="${reportBean.daySaleShipDateWiseDtoList}" var="daySaleShipDateWiseDto">
        <tr>
          <td><fmt:formatDate value="${daySaleShipDateWiseDto.orderShipDate}"/></td>
          <td><fmt:formatDate value="${daySaleShipDateWiseDto.orderShipDate}" pattern="E"/></td>

          <td><fmt:formatNumber value="${daySaleShipDateWiseDto.totalSumOfMrp}" currencySymbol=" " maxFractionDigits="0" type="currency"/> </td>
          <td><fmt:formatNumber value="${daySaleShipDateWiseDto.totalSumOfHkPrice}" currencySymbol=" " maxFractionDigits="0" type="currency"/> </td>
          <td><fmt:formatNumber value="${daySaleShipDateWiseDto.totalSumOfAllDiscounts}" currencySymbol=" " maxFractionDigits="0" type="currency"/> </td>
          <td><fmt:formatNumber value="${daySaleShipDateWiseDto.totalSumOfHkPricePostAllDiscounts}" currencySymbol=" " maxFractionDigits="0" type="currency"/> </td>
          <td><fmt:formatNumber value="${daySaleShipDateWiseDto.totalSumOfForwardingCharges}" currencySymbol=" " maxFractionDigits="0" type="currency"/> </td>
          <td><fmt:formatNumber value="${daySaleShipDateWiseDto.totalSumOfNetReceivable}" currencySymbol=" " maxFractionDigits="0" type="currency"/> </td>

          <td><fmt:formatNumber value="${daySaleShipDateWiseDto.sumOfMrpOfShippedItems}" currencySymbol=" " maxFractionDigits="0" type="currency"/> </td>
          <td><fmt:formatNumber value="${daySaleShipDateWiseDto.sumOfHkPriceOfShippedItems}" currencySymbol=" " maxFractionDigits="0" type="currency"/> </td>
          <td><fmt:formatNumber value="${daySaleShipDateWiseDto.sumOfAllDiscountsOfShippedItems}" currencySymbol=" " maxFractionDigits="0" type="currency"/> </td>
          <td><fmt:formatNumber value="${daySaleShipDateWiseDto.sumOfHkPricePostAllDiscountsOfShippedItems}" currencySymbol=" " maxFractionDigits="0" type="currency"/> </td>
          <td><fmt:formatNumber value="${daySaleShipDateWiseDto.sumOfForwardingChargesOfShippedItems}" currencySymbol=" " maxFractionDigits="0" type="currency"/> </td>
          <td><fmt:formatNumber value="${daySaleShipDateWiseDto.sumOfNetReceivableOfShippedItems}" currencySymbol=" " maxFractionDigits="0" type="currency"/> </td>

          <td><fmt:formatNumber value="${daySaleShipDateWiseDto.sumOfMrpOfDeliveredItems}" currencySymbol=" " maxFractionDigits="0" type="currency"/> </td>
          <td><fmt:formatNumber value="${daySaleShipDateWiseDto.sumOfHkPriceOfDeliveredItems}" currencySymbol=" " maxFractionDigits="0" type="currency"/> </td>
          <td><fmt:formatNumber value="${daySaleShipDateWiseDto.sumOfAllDiscountsOfDeliveredItems}" currencySymbol=" " maxFractionDigits="0" type="currency"/> </td>
          <td><fmt:formatNumber value="${daySaleShipDateWiseDto.sumOfHkPricePostAllDiscountsOfDeliveredItems}" currencySymbol=" " maxFractionDigits="0" type="currency"/> </td>
          <td><fmt:formatNumber value="${daySaleShipDateWiseDto.sumOfForwardingChargesOfDeliveredItems}" currencySymbol=" " maxFractionDigits="0" type="currency"/> </td>
          <td><fmt:formatNumber value="${daySaleShipDateWiseDto.sumOfNetReceivableOfDeliveredItems}" currencySymbol=" " maxFractionDigits="0" type="currency"/> </td>

          <td><fmt:formatNumber value="${daySaleShipDateWiseDto.sumOfMrpOfReturnedItems}" currencySymbol=" " maxFractionDigits="0" type="currency"/> </td>
          <td><fmt:formatNumber value="${daySaleShipDateWiseDto.sumOfHkPriceOfReturnedItems}" currencySymbol=" " maxFractionDigits="0" type="currency"/> </td>
          <td><fmt:formatNumber value="${daySaleShipDateWiseDto.sumOfAllDiscountsOfReturnedItems}" currencySymbol=" " maxFractionDigits="0" type="currency"/> </td>
          <td><fmt:formatNumber value="${daySaleShipDateWiseDto.sumOfHkPricePostAllDiscountsOfReturnedItems}" currencySymbol=" " maxFractionDigits="0" type="currency"/> </td>
          <td><fmt:formatNumber value="${daySaleShipDateWiseDto.sumOfForwardingChargesOfReturnedItems}" currencySymbol=" " maxFractionDigits="0" type="currency"/> </td>
          <td><fmt:formatNumber value="${daySaleShipDateWiseDto.sumOfNetReceivableOfReturnedItems}" currencySymbol=" " maxFractionDigits="0" type="currency"/> </td>

          <td><fmt:formatNumber value="${daySaleShipDateWiseDto.sumOfMrpOfCancelledItems}" currencySymbol=" " maxFractionDigits="0" type="currency"/> </td>
          <td><fmt:formatNumber value="${daySaleShipDateWiseDto.sumOfHkPriceOfCancelledItems}" currencySymbol=" " maxFractionDigits="0" type="currency"/> </td>
          <td><fmt:formatNumber value="${daySaleShipDateWiseDto.sumOfAllDiscountsOfCancelledItems}" currencySymbol=" " maxFractionDigits="0" type="currency"/> </td>
          <td><fmt:formatNumber value="${daySaleShipDateWiseDto.sumOfHkPricePostAllDiscountsOfCancelledItems}" currencySymbol=" " maxFractionDigits="0" type="currency"/> </td>
          <td><fmt:formatNumber value="${daySaleShipDateWiseDto.sumOfForwardingChargesOfCancelledItems}" currencySymbol=" " maxFractionDigits="0" type="currency"/> </td>
          <td><fmt:formatNumber value="${daySaleShipDateWiseDto.sumOfNetReceivableOfCancelledItems}" currencySymbol=" " maxFractionDigits="0" type="currency"/> </td>

        </tr>
      </c:forEach>
    </table>
    <s:form beanclass="com.hk.web.action.report.ReportAction" autocomplete="off">
      <div class="buttons"><s:submit name="generateSalesByDateForShippedProductsExcel" value="Download"/></div>
      <s:hidden name="startDate" value="${startDate}"/>
      <s:hidden name="endDate" value="${endDate}"/>
    </s:form>
  </s:layout-component>
</s:layout-render>