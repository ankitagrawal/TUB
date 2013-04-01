<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.*" %>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.shippingOrder.EnumShippingOrderStatus" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.inventory.SearchOrderAndReCheckinRTOInventoryAction" var="orderAdmin"/>
<c:set var="shippingOrderStatusRTO" value="<%=EnumShippingOrderStatus.SO_RTO.getId()%>"/>
<%
  int lineItemGlobalCtr = 0;
%>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Order Search">

  <s:layout-component name="htmlHead">
    <script type="text/javascript">
      $(document).ready(function() {
        $('.rto').live("change", function() {
          var shippingRow = $(this).parents('.shippingRow');
          var recheckin = shippingRow.find('.re-checkin').val();
          var qty = shippingRow.find('.qty').html();
          if (Math.round(recheckin) > Math.round(qty)) {
            alert("Invalid qty - plz check.");
            $(this).val(0);
          }
        });
      });
    </script>
  </s:layout-component>


  <s:layout-component name="heading">Search Order and Checkin RTO Units</s:layout-component>

  <s:layout-component name="content">
    <s:form beanclass="com.hk.web.action.admin.inventory.SearchOrderAndReCheckinRTOInventoryAction" method="get" autocomplete="false">
      <fieldset class="top_label">
        <ul>
          <div class="grouped">
            <li><label>Order ID</label> <s:text name="orderId" style="width: 100px;"/></li>
            <li><label>Gateway Order ID</label> <s:text name="gatewayOrderId"/></li>
          </div>
        </ul>
        <div class="buttons"><s:submit name="searchOrder" value="Search Order"/></div>
      </fieldset>
    </s:form>

    <c:if test="${orderAdmin.shippingOrder != null}">
      <s:form beanclass="com.hk.web.action.admin.inventory.SearchOrderAndReCheckinRTOInventoryAction" autocomplete="off">
        <s:hidden name="orderId" value="${orderAdmin.shippingOrder.id}"/>
        <c:set var="order" value="${orderAdmin.shippingOrder.baseOrder}"/>
        <table class="align_top" width="100%">
          <thead>
          <tr>
            <th>Order and payment</th>
            <th>User</th>
            <th>Address</th>
            <th>Items and Qty | Re-checkin (RTO) | (Delivery Date)</th>
              <%--<th></th>--%>
          </tr>
          </thead>
        <c:if test="${orderAdmin.shippingOrder.orderStatus.id ==  shippingOrderStatusRTO }">

          <tr>
            <td>
              <strong>${order.gatewayOrderId}</strong>
              (<s:link beanclass="com.hk.web.action.core.accounting.SOInvoiceAction" target="_blank">
              <s:param name="shippingOrder" value="${orderAdmin.shippingOrder}"/>
              Invoice
            </s:link>)<br>(<s:link beanclass="com.hk.web.action.core.accounting.SOInvoiceAction" target="_blank">
              <s:param name="shippingOrder" value="${orderAdmin.shippingOrder}"/>
              <s:param name="printable" value="true"/>
              Personal Care
            </s:link>)<br/><br/>
              <strong>Rs.
                <fmt:formatNumber value="${order.payment.amount}" pattern="<%=FormatUtils.currencyFormatPattern%>"/></strong>
              <ul>
                <li class="fieldLabel">Date</li>
                <li><fmt:formatDate value="${order.payment.paymentDate}" type="both"/></li>
                <li class="fieldLabel">Mode</li>
                <li>${order.payment.paymentMode.name}</li>
                <li class="fieldLabel">Status</li>
                <li>${order.payment.paymentStatus.name}</li>
              </ul>
            </td>
            <td>
              <ul>
                <li class="fieldLabel">Name</li>
                <li>${order.user.name}</li>
                <li class="fieldLabel">Email</li>
                <li>${order.user.email}</li>
                <li class="fieldLabel">Signup date</li>
                <li><fmt:formatDate value="${order.user.createDate}"/></li>
              </ul>
            </td>
            <td class="addressContainer">
                ${order.address.name}<br/>
                ${order.address.line1}<br/>
              <c:if test="${hk:isNotBlank(order.address.line2)}">${order.address.line2}<br/></c:if>
                ${order.address.city} - ${order.address.pincode.pincode}<br/>
                ${order.address.state}<br/>
              Ph. ${order.address.phone}<br/>
            </td>
            <td style="padding: 0" valign="top">
              <table width="100%">
                <c:forEach items="${orderAdmin.shippingOrder.lineItems}" var="lineItem" varStatus="lineItemCtr">
                  <c:set var="productVariant" value="${lineItem.sku.productVariant}"/>
                    <c:if test="${!productVariant.product.service}">
                      <c:set value="<%=lineItemGlobalCtr%>" var="lineItemGlobalCtr"/>
                      <s:hidden name="lineItems[${lineItemGlobalCtr}]" value="${lineItem.id}"/>
                      <tr class="shippingRow">
                        <td width="250">
                          <s:hidden name="lineItems[${lineItemGlobalCtr}]" value="${lineItem.id}"/>
                            ${productVariant.product.name}
                          <c:if test="${not empty productVariant.productOptions}">
                            <br/><span class="gry">
                            <c:forEach items="${productVariant.productOptions}" var="productOption" varStatus="optionCtr">
                              ${productOption.name} ${productOption.value}${!optionCtr.last?',':''}
                            </c:forEach>
                          </span>
                          </c:if>
                        </td>
                        <td class="qty">${lineItem.qty}</td>
                        <td title="Re-checkin">
                          <s:text name="lineItemRecheckinQtyMap[${lineItem}]" size="1" style="width:25px;" class="rto re-checkin" value="0"/>
                          </td>
                        <td>
                          OK-${hk:getReCheckedinUnitsCount(lineItem)}<br/>
                          DAMAGE-${hk:getDamageUnitsCount(lineItem)}
                        </td>
                        <td>

                            <s:link beanclass="com.hk.web.action.admin.inventory.SearchOrderAndReCheckinRTOInventoryAction"
                                    event="downloadBarcode"> Download
                                <%--<s:hidden name="lineItem" value="${lineItem.id}"/>--%>
                                <s:param name="lineItem" value="${lineItem.id}"/>
                                <s:param name="shippingOrder" value="${orderAdmin.shippingOrder}"/>
                            </s:link>
                            <%--${sh.lineItemStatus.name}--%>
                        </td>
                        <td>
                            <%--${lineItem.courier.name}--%>
                        </td>
                        <td>
                            <%--${lineItem.trackingId}--%>
                        </td>
                        <td>
                          <%--<fmt:formatDate value="${lineItem.deliveryDate}" type="both"/>--%>
                        </td>
                      </tr>
                      <%
                        lineItemGlobalCtr++;
                      %>
                    </c:if>
                </c:forEach>
              </table>

              <s:link beanclass="com.hk.web.action.admin.order.OrderLifecycleAction" event="pre" target="_blank">
                Order Lifecycle
                <s:param name="order" value="${order}"/>
              </s:link>

              <s:link beanclass="com.hk.web.action.admin.order.OrderLifecycleAction" event="pre" target="_blank">
                <c:if test="${!empty hk:orderComments(order)}">
                  <text style="color:#f88; font-weight:bold">Comments!</text>
                </c:if>
                <c:if test="${empty hk:orderComments(order)}">Add a comment</c:if>
                <s:param name="order" value="${order}"/>
              </s:link>
            </td>
          </tr>   
          </c:if>
        </table>

        <div class="buttons"><s:submit name="checkinRTOUnits" value="Checkin RTO Units"/></div>
      </s:form>

    </c:if>
  </s:layout-component>

</s:layout-render>
