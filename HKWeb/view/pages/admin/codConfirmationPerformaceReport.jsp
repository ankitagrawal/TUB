<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.report.ReportAction" var="reportBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="COD Confirmation Report">

  <s:layout-component name="heading">COD Confirmation Report</s:layout-component>
  <s:layout-component name="content">


    <div> Unconfirmed CODs</div>
    <table class="align_top" width="100%">
      <thead>
      <tr>
        <th>ORDER_DATE</th>
        <th>ORDER_PLACED_DAY</th>
        <th>ORDER_ID</th>
        <th>USER_EMAIL</th>
        <th>USER_PHONE</th>
        <th>USER_NAME</th>
        <th>TIME_ELAPSED(TOTAL)</th>
        <th>TIME_ELAPSED(WORKING)</th>
      </tr>
      </thead>


      <c:forEach items="${reportBean.CODUnConfirmedOrderList}" var="CODUnConfirmedOrderList">
        <c:if test="${CODUnConfirmedOrderList.calculatedHoursTakenToConfirm > 3}">
          <tr style="background:#ff9900">
        </c:if>
        <c:if test="${CODUnConfirmedOrderList.calculatedHoursTakenToConfirm <= 3}">
          <tr>
        </c:if>

        <td>
          <fmt:formatDate value="${CODUnConfirmedOrderList.orderPlacedDate}" pattern="<%=FormatUtils.defaultDateFormatPattern%>"/></td>
        <td><fmt:formatDate value="${CODUnConfirmedOrderList.orderPlacedDate}" pattern="E"/></td>
        <td><s:link beanclass="com.hk.web.action.admin.order.search.SearchOrderAction" event="searchOrders" target="_blank">
          <s:param name="orderId" value="${CODUnConfirmedOrderList.order.id}"/>
          ${CODUnConfirmedOrderList.order.id}
        </s:link></td>
        <td>${CODUnConfirmedOrderList.userEmail}</td>
        <td>${CODUnConfirmedOrderList.order.payment.contactNumber}</td>
        <td>${CODUnConfirmedOrderList.userName}</td>
        <td>${CODUnConfirmedOrderList.hoursTakenToConfirm} hrs ${CODUnConfirmedOrderList.minutesTakenToConfirm} mins
        </td>
        <td>${CODUnConfirmedOrderList.calculatedHoursTakenToConfirm}
          hrs ${CODUnConfirmedOrderList.calculatedMinutesTakenToConfirm} mins
        </td>
        </tr>
      </c:forEach>

    </table>
    <div> Confirmed CODs</div>
    <table class="align_top" width="100%">
      <thead>
      <tr>
        <th>PLACED_DATE</th>
        <th>PLACED_DAY</th>
        <th>CONFIRMATION_DATE</th>
        <th>CONFIRMED_DAY</th>
        <th>ORDER_ID</th>
        <th>USER_EMAIL</th>
        <th>USER_NAME</th>
        <th>CONFIRMATION_TIME(TOTAL)</th>
        <th>CONFIRMATION_TIME(WORKING)</th>
        <th></th>
      </tr>
      </thead>


      <c:forEach items="${reportBean.CODConfirmedOrderList}" var="CODConfirmedOrderList">
        <c:if test="${CODConfirmedOrderList.calculatedHoursTakenToConfirm > 3}">
          <tr style="background:#ff9900">
        </c:if>
        <c:if test="${CODConfirmedOrderList.calculatedHoursTakenToConfirm <= 3}">
          <tr>
        </c:if>
        <td>
          <fmt:formatDate value="${CODConfirmedOrderList.orderPlacedDate}" pattern="<%=FormatUtils.defaultDateFormatPattern%>"/></td>
        <td><fmt:formatDate value="${CODConfirmedOrderList.orderPlacedDate}" pattern="E"/></td>
        <td>
          <fmt:formatDate value="${CODConfirmedOrderList.orderConfirmationDate}" pattern="<%=FormatUtils.defaultDateFormatPattern%>"/></td>
        <td><fmt:formatDate value="${CODConfirmedOrderList.orderConfirmationDate}" pattern="E"/></td>
        <td><s:link beanclass="com.hk.web.action.admin.order.search.SearchOrderAction" event="searchOrders" target="_blank">
          <s:param name="orderId" value="${CODConfirmedOrderList.order.id}"/>
          ${CODConfirmedOrderList.order.id}
        </s:link></td>
        <td>${CODConfirmedOrderList.userEmail}</td>
        <td>${CODConfirmedOrderList.userName}</td>
        <td>${CODConfirmedOrderList.hoursTakenToConfirm} hrs ${CODConfirmedOrderList.minutesTakenToConfirm} mins</td>
        <td>${CODConfirmedOrderList.calculatedHoursTakenToConfirm}
          hrs ${CODConfirmedOrderList.calculatedMinutesTakenToConfirm} mins
        </td>
        </tr>
      </c:forEach>
    </table>

    <s:form beanclass="com.hk.web.action.report.ReportAction" autocomplete="off">
      <div class="buttons"><s:submit name="generateCODConfirmationReportXls" value="Download"/></div>
      <s:hidden name="startDate" value="${reportBean.startDate}"/>
      <s:hidden name="endDate" value="${reportBean.endDate}"/>
    </s:form>
  </s:layout-component>
</s:layout-render>