<%@ page import="com.hk.constants.order.EnumCartLineItemType" %>
<%@ page import="com.hk.constants.order.EnumOrderStatus" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<c:set var="lineItemTypeId_Product" value="<%=EnumCartLineItemType.Product.getId()%>"/>
<c:set var="orderStatusActionAwaiting" value="<%=EnumOrderStatus.Placed.getId()%>"/>

<s:useActionBean beanclass="com.hk.web.action.report.ReportAction" var="reportBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Unescalated Escalable Order List">

  <s:layout-component name="htmlHead">
    <script type="text/javascript">
      $(document).ready(function() {
        $('#liveSearchBox').keyup(function() {
          var searchString = $(this).val().toLowerCase();
          $('.orderRow').each(function() {
            if ($(this).find('.basketCategory').text().toLowerCase().indexOf(searchString) == -1) {
              $(this).hide();
            } else {
              $(this).show();
            }
          });
        });
      });
    </script>
  </s:layout-component>

  <s:layout-component name="heading">Unescalated Escalable Order List</s:layout-component>
  <s:layout-component name="content">

    <h2>Quick Search <i>(by Basket Category)</i>: <input type="text" id="liveSearchBox"/></h2><br/>

    <h2>Unescalated Orders - ${fn:length(reportBean.orderList)}</h2>

    <table class="zebra_vert" width="100%">
      <thead>
      <tr>
        <th>Order Date</th>
        <th>Day</th>
        <th>Basket Category</th>
        <th>Order Details</th>
        <th>Items</th>
      </tr>
      </thead>
      <tbody>
      <c:forEach items="${reportBean.orderList}" var="order">
        <tr class="orderRow">
          <td><fmt:formatDate value="${order.payment.paymentDate}" pattern="yyyy-MM-dd HH:mm"/><br>
            <c:if test="${! empty order.orderLifecycles}">
              <s:link beanclass="com.hk.web.action.admin.order.OrderLifecycleAction" event="pre" target="_blank">
                <label style="font-weight:bold;">Last Activity:</label><br>
                ${order.orderLifecycles[fn:length(order.orderLifecycles)-1].orderLifecycleActivity.name} on <br>
                <fmt:formatDate value="${order.orderLifecycles[fn:length(order.orderLifecycles)-1].activityDate}" type="both"/> by
                "${order.orderLifecycles[fn:length(order.orderLifecycles)-1].user.name}"
                <s:param name="order" value="${order}"/>
              </s:link>
            </c:if>
          </td>
          <td><fmt:formatDate value="${order.payment.paymentDate}" pattern="E"/></td>
          <td class="basketCategory">${order.basketCategory}</td>
          <td>
            Order#<s:link beanclass="com.hk.web.action.admin.order.search.SearchOrderAction" event="searchOrders" target="_blank">
            <s:param name="orderId" value="${order.id}"/>
            ${order.id}
          </s:link><br/>
            Payment Mode: ${order.payment.paymentMode.name}<br/>
              <c:if test="${order.payment.gateway != null}">
                  <span style="margin-left:30px;">Gateway: ${order.payment.gateway.name}</span>
              </c:if>
            Name: ${order.user.name}<br/>
            Email: ${order.user.email}<br/>
            <%--<s:link beanclass="com.hk.web.action.admin.order.OrderLifecycleAction" event="pre" target="_blank">
              Order Lifecycle
              <s:param name="order" value="${order}"/>
            </s:link>

            <s:link beanclass="com.hk.web.action.admin.order.OrderLifecycleAction" event="pre" target="_blank">
              <c:if test="${!empty hk:orderComments(order)}">
                <text style="color:#f88; font-weight:bold">Comments!</text>
              </c:if>
              <c:if test="${empty hk:orderComments(order)}">Add a comment</c:if>
              <s:param name="order" value="${order}"/>
            </s:link>--%>
          </td>
          <td style="padding: 0" valign="top">
            <table class="" width="100%">
              <c:forEach items="${order.lineItems}" var="lineItem" varStatus="lineItemCtr">
                <c:if test="${lineItemTypeId_Product == lineItem.lineItemType.id}">
                  <c:if test="${orderStatusActionAwaiting == lineItem.lineItemStatus.id}">
                    <c:choose>
                      <c:when test="${hk:bookedQty(lineItem.productVariant, null) <= lineItem.productVariant.netInventory}">
                        <tr style="border-left:5px solid green;">
                      </c:when>
                      <c:otherwise>
                        <c:choose>
                          <c:when test="${lineItem.qty <= lineItem.productVariant.netInventory}">
                            <tr style="border-left:5px solid orange;">
                          </c:when>
                          <c:otherwise>
                            <tr style="border-left:5px solid red;">
                          </c:otherwise>
                        </c:choose>
                      </c:otherwise>
                    </c:choose>

                    <td>
                        ${lineItem.productVariant.product.name}
                      <c:if test="${not empty lineItem.productVariant.productOptions}">
                        <br/><span class="gry">
                            <c:forEach items="${lineItem.productVariant.productOptions}" var="productOption" varStatus="optionCtr">
                              ${productOption.name} ${productOption.value}${!optionCtr.last?',':''}
                            </c:forEach>
                          </span>
                      </c:if>
                    </td>
                    <td>${lineItem.qty} <b>[${hk:bookedQty(lineItem.productVariant, null)}]</b>
                      <b>(${lineItem.productVariant.netInventory})</b>
                    </td>
                    </tr>
                  </c:if>

                  <c:if test="${orderStatusActionAwaiting != lineItem.lineItemStatus.id}">
                    <tr>
                      <td>
                          ${lineItem.productVariant.product.name}
                        <c:if test="${not empty lineItem.productVariant.productOptions}">
                          <br/><span class="gry">
                            <c:forEach items="${lineItem.productVariant.productOptions}" var="productOption" varStatus="optionCtr">
                              ${productOption.name} ${productOption.value}${!optionCtr.last?',':''}
                            </c:forEach>
                          </span>
                        </c:if>
                      </td>
                      <td>${lineItem.qty}
                      </td>
                    </tr>
                  </c:if>
                </c:if>
              </c:forEach>
            </table>

          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>

  </s:layout-component>
</s:layout-render>