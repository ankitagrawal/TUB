<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.core.user.CustomerOrderHistoryAction" var="coha"/>
<s:layout-render name="/layouts/defaultBeta.jsp">
  <s:layout-component name="heading">Order History</s:layout-component>

    <s:layout-component name="centralContent">
        <%--breadcrumbs begins--%>
        <div class="hk-breadcrumb-cntnr mrgn-bt-10">
                <span>
                   <s:link beanclass="com.hk.web.action.HomeAction">Home</s:link>
                </span>
            <span>&raquo;</span>
            <span class="txt-blue fnt-bold">Orders</span>
        </div>
        <%--breadcrumbs ends--%>
    </s:layout-component>


    <s:layout-component name="lhsContent">
    <jsp:include page="myaccount-navBeta.jsp"/>
  </s:layout-component>

  <s:layout-component name="rhsContent">

    <div class="main-inn-right mrgn-l-40 my-acnt-ht">

      <div class="round-cont">
        <c:choose>
            <c:when test="${!empty coha.orderList}">
                <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${coha}"/>
                <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${coha}"/>

                <table class="order-tbl">
                    <tr class="order-specs-hdr btm-brdr">
                        <th class="fnt-bold">Order Id</th>
                        <th class="fnt-bold">Order Date</th>
                        <th class="fnt-bold">Invoices</th>
                        <th class="fnt-bold">Order Status</th>
                    </tr>
                    <tbody>
                    <c:forEach items="${coha.orderList}" var="order" varStatus="ctr">
                        <c:if test="${ctr.first}">
                            <tr class="order-tr top-brdr">
                        </c:if>
                        <c:if test="${ctr.last}">
                            <tr class="${ctr.index%2==0? 'order-tr btm-brdr':'order-tr btm-brdr bg-gray'}">
                        </c:if>
                        <c:if test="${!(ctr.first || ctr.last)}">
                            <tr class="${ctr.index%2==0? 'order-tr':'order-tr bg-gray'}">
                        </c:if>
                                <td>
                                    <c:if test="${!empty (order.gatewayOrderId)}">
                                        <s:link beanclass="com.hk.web.action.core.accounting.BOInvoiceAction" target="_blank">
                                            <s:param name="order" value="${order}"/>
                                            ${order.gatewayOrderId}
                                        </s:link>
                                    </c:if>
                                </td>
                                <td class="border-td">
                                    <fmt:formatDate value="${order.payment.paymentDate}" />
                                </td>
                                <td class="border-td">
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
                                            <s:link beanclass="com.hk.web.action.core.accounting.BOInvoiceAction" event="pre" target="_blank">
                                                <s:param name="order" value="${order.id}"/>
                                                R-${order.id}
                                            </s:link>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                                <td>
                                        ${order.orderStatus.name}
                                    <s:link beanclass="com.hk.web.action.core.order.OrderDetailsAction" target="_blank">
                                        <s:param name="order" value="${order}"/>
                                        (View Details)
                                    </s:link>
                                </td>
                            </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${coha}"/>
                <s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${coha}"/>
                <br/><br/>
            </c:when>
            <c:otherwise>
                <br/>
                <br/>
                You haven't ordered anything from healthkart yet.   <s:link beanclass="com.hk.web.action.HomeAction" event="pre" class="buttons" >
                Continue Shopping
            </s:link>
            </c:otherwise>
        </c:choose>
      </div>
    </div>
  </s:layout-component>
</s:layout-render>

<script type="text/javascript">
  window.onload = function() {
      $('#ohLink').addClass('selected');
  };
</script>
<%--<style type="text/css">--%>
  <%--table {--%>
    <%--width: 100%;--%>
    <%--margin-bottom: 10px;--%>
    <%--margin-top: 5px;--%>
    <%--border: 1px solid;--%>
    <%--border-collapse: separate;--%>
  <%--}--%>

  <%--table th {--%>
    <%--background: #f0f0f0;--%>
    <%--padding: 5px;--%>
    <%--text-align: left;--%>
  <%--}--%>

  <%--table td {--%>
    <%--padding: 5px;--%>
    <%--text-align: left;--%>
    <%--font-size: small;--%>
  <%--}--%>
<%--</style>--%>