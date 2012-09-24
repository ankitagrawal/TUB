<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.report.ReconciliationDiffReportAction" var="diffReportBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Reconcilation Diff Report">
	<s:layout-component name="htmlHead">
	<link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
	</s:layout-component>

  <s:layout-component name="content">


	  <fieldset class="top_label">
	    <ul>
	      <div class="grouped grid_12">
	        <s:form beanclass="com.hk.web.action.report.ReconciliationDiffReportAction" method="get" >
		        <label>Payment Mode </label>
		        <s:select name="paymentProcess" class="uploadPaymentMode" style="width: 100">
		        								<s:option value="all">-Select-</s:option>
		        								<s:option value="cod">COD</s:option>
		        								<s:option value="techprocess">Prepaid</s:option>
		        							</s:select>
	          <label>SO Gateway ID </label><s:text name="gatewayOrderId" id="gatewayOrderId"/>
	          <label>SO Order ID </label> <s:text name="shippingOrderId"/>
	          <label>BO Gateway ID </label><s:text name="baseGatewayOrderId" id="baseGatewayOrderId"/>
	          <label>BO Order ID </label> <s:text name="baseOrderId"/>
	          <label>Start Date </label><s:text class="date_input startDate" style="width:150px"
	                                 formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="startDate"/>
	          <label>End Date </label><s:text class="date_input endDate" style="width:150px"
	                                 formatPattern="<%=FormatUtils.defaultDateFormatPattern%>" name="endDate"/>
	          <s:submit name="searchOrders" value="Search"/>
	        </s:form>
	        <script language=javascript type=text/javascript>
	          $('#gatewayOrderId').focus();
	        </script>
	      </div>
	    </ul>
	  </fieldset>



<%--


    <table class="align_top" width="100%">
      <thead>
      <tr>
        <th>ORDER NO</th>
        <th>DAY_OF_WEEK</th>
        <th>TXN_COUNT</th>
       &lt;%&ndash; <th>(%) First Txn</th>
        <th>(%) COD</th>
        <th>(%) Offer</th>&ndash;%&gt;
        <th>SKU_COUNT</th>
        <th>SUM_MRP</th>
        <th>SUM_HK_PRICE</th>
        <th>SUM_HK_PRICE_POST_DISCOUNT</th>
      </tr>
      </thead>

      <tr>
        <td colspan="2" style="font-weight:bold; font-size:14px;">Avg Values</td>
        <td style="font-weight:bold; font-size:14px;">
          <fmt:formatNumber value="${reportBean.avgTxn}" pattern="<%=FormatUtils.currencyFormatPattern%>"/></td>
        &lt;%&ndash;<td></td>
        <td style="font-weight:bold; font-size:14px;">
          <fmt:formatNumber value="${reportBean.avgTnxCOD/reportBean.avgTxn * 100}" pattern="#.#"/></td>
        <td style="font-weight:bold; font-size:14px;">
            <fmt:formatNumber value="${reportBean.avgTxnOfferOrder/reportBean.avgTxn * 100}" pattern="#.#"/></td>&ndash;%&gt;
        <td style="font-weight:bold; font-size:14px;">
          <fmt:formatNumber value="${reportBean.avgSku}" pattern="<%=FormatUtils.currencyFormatPattern%>"/></td>
        <td style="font-weight:bold; font-size:14px;">
          <fmt:formatNumber value="${reportBean.avgMrp}" pattern="<%=FormatUtils.currencyFormatPattern%>"/></td>
        <td style="font-weight:bold; font-size:14px;">
          <fmt:formatNumber value="${reportBean.avgHkp}" pattern="<%=FormatUtils.currencyFormatPattern%>"/></td>
        <td style="font-weight:bold; font-size:14px;">&lt;%&ndash;<fmt:formatNumber value="${reportBean.avgHkpPostDiscount}" pattern="<%=FormatUtils.currencyFormatPattern%>"/>&ndash;%&gt;</td>
      </tr>

      <c:forEach items="${reportBean.daySaleList}" var="daySaleList">
        <tr>
          <td><fmt:formatDate value="${daySaleList.orderDate}"/></td>
          <td><fmt:formatDate value="${daySaleList.orderDate}" pattern="E"/></td>
          <td>${daySaleList.txnCount}</td>
          &lt;%&ndash;<td>
            <fmt:formatNumber value="${daySaleList.firstTxnCount/daySaleList.txnCount * 100}" pattern="#.#"/></td>
          <td>
            <fmt:formatNumber value="${daySaleList.codTxnCount/daySaleList.txnCount * 100}" pattern="#.#"/></td>
          <td>
            <fmt:formatNumber value="${daySaleList.offerTxnCount/daySaleList.txnCount * 100}" pattern="#.#"/></td>&ndash;%&gt;
          <td>${daySaleList.skuCount}</td>
          <td>${daySaleList.sumOfMrp}</td>
          <td>${daySaleList.sumOfHkPrice}</td>
          <td>
            <fmt:formatNumber value="${daySaleList.sumOfHkPrice - daySaleList.sumOfHkPricePostAllDiscounts}" pattern="<%=FormatUtils.currencyFormatPattern%>"/></td>
        </tr>
      </c:forEach>
    </table>
    <s:form beanclass="com.hk.web.action.report.ReportAction" autocomplete="off">
      <div class="buttons"><s:submit name="generateSalesByDateExcel" value="Download"/></div>
      <s:hidden name="startDate" value="${startDate}"/>
      <s:hidden name="endDate" value="${endDate}"/>
    </s:form>--%>
  </s:layout-component>
</s:layout-render>