<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="web.action.CustomerOrderHistoryAction" var="coha"/>
<s:layout-render name="/layouts/default.jsp">
  <s:layout-component name="heading">Order History</s:layout-component>
  <s:layout-component name="lhsContent">
    <jsp:include page="myaccount-nav.jsp"/>
  </s:layout-component>

  <s:layout-component name="rhsContent">
    <div class="main-inn-right">

      <div class="round-cont">
        <c:if test="${!empty coha.orderList}">
          <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${coha}"/>
          <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${coha}"/>

          <table class="zebra_vert" width="100%">
            <thead>
            <tr>
              <td>Order Id</td>
              <td>Order Date</td>
              <%--<td>Invoices</td>--%>
              <td>Order Status</td>
              <td>View Order</td>

            </tr>
            </thead>
            <tbody>
            <c:forEach items="${coha.orderList}" var="order">
              <tr>
                <td>
                    ${order.gatewayOrderId}
                </td>
                <td>
                  <fmt:formatDate value="${order.payment.paymentDate}" pattern="dd/MM/yyyy"/>
                </td>
                <%--<td>
                  <c:forEach items="${order.accountingInvoices}" var="accountingInvoice">
                    <s:link beanclass="web.action.AccountingInvoiceAction" event="pre" target="_blank">
                      <s:param name="accountingInvoice" value="${accountingInvoice.id}"/>
                      R-${accountingInvoice.retailInvoiceId} &lt;%&ndash;on
                      <fmt:formatDate value="${accountingInvoice.invoiceDate}" pattern="dd/MM/yyyy hh:mm"/>&ndash;%&gt;
                    </s:link><br/></c:forEach>
                </td>--%>
                <td>
                    ${order.orderStatus.name}
                  <s:link beanclass="web.action.OrderDetailsAction">
                    <s:param name="order" value="${order}"/>
                    (View Details)
                  </s:link>
                </td>
                <td>
                  <s:link beanclass="web.action.BOInvoiceAction" target="_blank">
                                  <s:param name="order" value="${order}"/>
                                  View Order
                  </s:link>
                </td>
              </tr>
            </c:forEach>
            </tbody>
          </table>
          <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${coha}"/>
          <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${coha}"/>
          <br/><br/>
        </c:if>
      </div>
    </div>
  </s:layout-component>
</s:layout-render>

<script type="text/javascript">
  window.onload = function() {
    document.getElementById("ohLink").style.fontWeight = "bold";
  };
</script>