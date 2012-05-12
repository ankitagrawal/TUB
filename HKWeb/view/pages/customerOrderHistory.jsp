<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="mhc.web.action.CustomerOrderHistoryAction" var="coha"/>
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

          <table class="cont footer_color">
            <tr>
              <th>Order Id</th>
              <th>Order Date</th>
              <th>Invoices</th>
              <th>Order Status</th>
              <th>View Order</th>

            </tr>
            <tbody>
            <c:forEach items="${coha.orderList}" var="order">
              <tr>
                <td>
                    ${order.gatewayOrderId}
                </td>
                <td>
                  <fmt:formatDate value="${order.payment.paymentDate}" pattern="dd/MM/yyyy"/>
                </td>
                <td>
                  <c:set var="shippingOrders" value="${order.shippingOrders}"/>
                  <c:choose>
                    <c:when test="${!empty shippingOrders}">
                      <c:forEach items="${shippingOrders}" var="shippingOrder">
                        <s:link beanclass="com.hk.web.action.core.accounting.SOInvoiceAction" event="pre" target="_blank">
                          <s:param name="shippingOrder" value="${shippingOrder.id}"/>
                          R-${shippingOrder.id}
                        </s:link>
                      </c:forEach>
                    </c:when>
                    <c:otherwise>
                      <s:link beanclass="mhc.web.action.BOInvoiceAction" event="pre" target="_blank">
                        <s:param name="order" value="${order.id}"/>
                        R-${order.id}
                      </s:link>
                    </c:otherwise>
                  </c:choose>
                </td>
                <td>
                    ${order.orderStatus.name}
                  <s:link beanclass="mhc.web.action.OrderDetailsAction" target="_blank">
                    <s:param name="order" value="${order}"/>
                    (View Details)
                  </s:link>
                </td>
                <td>
                  <s:link beanclass="mhc.web.action.BOInvoiceAction" target="_blank">
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
<style type="text/css">
  table {
    width: 100%;
    margin-bottom: 10px;
    margin-top: 5px;
    border: 1px solid;
    border-collapse: separate;
  }

  table th {
    background: #f0f0f0;
    padding: 5px;
    text-align: left;
  }

  table td {
    padding: 5px;
    text-align: left;
    font-size: small;
  }
</style>