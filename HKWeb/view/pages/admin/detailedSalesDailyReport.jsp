<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.constants.*" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.report.ReportAction" var="reportBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Sales by Date Report">

  <s:layout-component name="heading">Detailed Sales Report</s:layout-component>
  <s:layout-component name="content">

    <table class="align_top" width="100%">
      <thead>
      <tr>
        <th>ORDER_DATE</th>
        <th>TXN_COUNT</th>
        <th>(%) First Txn</th>
        <th>(%) COD</th>
        <th>(%) Offer</th>
      </tr>
      </thead>

      <c:forEach items="${reportBean.daySaleList}" var="daySaleList">
        <tr>
          <td><fmt:formatDate value="${daySaleList.orderDate}"/></td>
          <td>${daySaleList.txnCount}</td>
          <td>
            <fmt:formatNumber value="${daySaleList.firstTxnCount/daySaleList.txnCount * 100}" pattern="#.#"/></td>
          <td>
            <fmt:formatNumber value="${daySaleList.codTxnCount/daySaleList.txnCount * 100}" pattern="#.#"/></td>
          <td>
            <fmt:formatNumber value="${daySaleList.offerTxnCount/daySaleList.txnCount * 100}" pattern="#.#"/></td>
        </tr>
      </c:forEach>
    </table>
  </s:layout-component>
</s:layout-render>