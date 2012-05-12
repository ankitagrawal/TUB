<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.catalog.product.EnumProductVariantPaymentType" %>
<%@ page import="com.hk.constants.core.PermissionConstants" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.constants.order.EnumOrderStatus" %>
<%@ page import="com.hk.constants.payment.EnumPaymentStatus" %>
<%@ page import="com.hk.constants.shippingOrder.EnumShippingOrderStatus" %>
<%@ page import="com.hk.domain.order.ShippingOrder" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="java.util.Set" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>

<%
  Set<ShippingOrder> shippingOrders = (Set) pageContext.getAttribute("shippingOrders");
  pageContext.setAttribute("shippingOrders", shippingOrders);
  Boolean isActionQueue = (Boolean) pageContext.getAttribute("isActionQueue");


  if (isActionQueue != null) {
    pageContext.setAttribute("isActionQueue", isActionQueue);
  } else {
    pageContext.setAttribute("isActionQueue", false);
  }
  Boolean showCourier = (Boolean) pageContext.getAttribute("showCourier");
  if (showCourier != null) {
    pageContext.setAttribute("showCourier", showCourier);
  } else {
    pageContext.setAttribute("showCourier", false);
  }
  Boolean hasAction = (Boolean) pageContext.getAttribute("hasAction");
  if (hasAction != null) {
    pageContext.setAttribute("hasAction", hasAction);
  } else {
    pageContext.setAttribute("hasAction", true);
  }
  Boolean isSearchShippingOrder = (Boolean) pageContext.getAttribute("isSearchShippingOrder");
  if (isSearchShippingOrder != null) {
    pageContext.setAttribute("isSearchShippingOrder", isSearchShippingOrder);
  } else {
    pageContext.setAttribute("isSearchShippingOrder", false);
  }
%>

<s:layout-definition>
<c:set var="shippingOrderStatusActionAwaiting" value="<%=EnumShippingOrderStatus.SO_ActionAwaiting.getId()%>"/>
<c:set var="orderStatusHold" value="<%=EnumOrderStatus.OnHold.getId()%>"/>
<c:set var="shippingOrderStatusHold" value="<%=EnumShippingOrderStatus.SO_OnHold.getId()%>"/>
<c:set var="paymentStatusAuthPending" value="<%=EnumPaymentStatus.AUTHORIZATION_PENDING.getId()%>"/>
<c:set var="shippingOrderStatusShipped" value="<%=EnumShippingOrderStatus.SO_Shipped.getId()%>"/>
<c:set var="shippingOrderStatusDelivered" value="<%=EnumShippingOrderStatus.SO_Delivered.getId()%>"/>
<c:set var="shippingOrderStatusRTO" value="<%=EnumShippingOrderStatus.SO_Returned.getId()%>"/>
<c:set var="lineItem_Service_Postpaid" value="<%=EnumProductVariantPaymentType.Postpaid.getId()%>"/>


<table width="100%" class="align_top" style="margin:1px;padding:0;">
<c:if test="${isActionQueue == false}">
  <thead>
  <tr>
    <th style="margin:2px;padding:3px;font-size:13px;font-weight:bold;text-align:center;">Shipping Order</th>
    <c:if test="${isActionQueue == false}">
      <th style="margin:2px;padding:3px;font-size:13px;font-weight:bold;text-align:center;">User & Adddress</th>
    </c:if>
    <th style="margin:2px;padding:3px;font-size:13px;font-weight:bold;text-align:center;">Items and Qty[Checkedout Qty]
    </th>
    <c:if test="${showCourier == true}">
      <th style="margin:2px;padding:3px;font-size:13px;font-weight:bold;text-align:center;">Courier Details</th>
    </c:if>
    <th style="margin:2px;padding:3px;font-size:13px;font-weight:bold;text-align:center;">Status</th>
    <th></th>
  </tr>
  </thead>
</c:if>
<c:forEach items="${shippingOrders}" var="shippingOrder" varStatus="shippingOrderCtr">
<c:set var="baseOrder" value="${shippingOrder.baseOrder}"/>
<c:set var="payment" value="${shippingOrder.baseOrder.payment}"/>
<tr id="shippingOrder-${shippingOrder.id}"
    style="margin-bottom: 5px;border: 1px dotted;overflow: hidden;padding: 3px;">
  <%--<div id="shippingOrder-${shippingOrder.id}" class="detailDiv">--%>
<td id="shippingOrderDetail-${shippingOrder.id}">
   <div class="floatleft">
    Store ID: <strong>${shippingOrder.baseOrder.store.prefix}</strong>
  </div>
  <div class="clear" style=""></div>
  <div class="floatleft">
    Gateway Id: <strong>${shippingOrder.gatewayOrderId}</strong>
                <span
                    style="margin-left:10px;"> Basket: <strong>${shippingOrder.basketCategory}</strong></span>
  </div>
  <div class="clear" style=""></div>
  <div class="floatleft">
    Amount: <strong>Rs.<fmt:formatNumber value="${shippingOrder.amount}"
                                         pattern="<%=FormatUtils.currencyFormatPattern%>"/></strong>
  </div>
  <div class="clear"></div>
  <c:if test="${isActionQueue == true || isSearchShippingOrder}">
    <div class="floatleft">
      Warehouse: <strong>${shippingOrder.warehouse.city}</strong>
    </div>
    <div class="clear"></div>
  </c:if>
  <c:if test="${isActionQueue == false}">
    <div class="floatleft">
      <span> Mode: <strong>${payment.paymentMode.name}</strong></span>
      <span style="margin-left:10px;"> Status: <strong>${payment.paymentStatus.name}</strong></span>
    </div>
    <div class="clear"></div>
    <div class="floatleft">
      Escalted On: <fmt:formatDate value="${(hk:getEscalationDate(shippingOrder))}" type="both" timeStyle="short"/>
    </div>
    <div class="clear"></div>
  </c:if>
  <div class="floatleft">
    (<s:link beanclass="com.hk.web.action.admin.order.search.SearchOrderAction" event="searchOrders" target="_blank">
    <s:param name="orderId" value="${shippingOrder.baseOrder.id}"/> Search BO
  </s:link>)
    (<s:link beanclass="com.hk.web.action.admin.order.search.SearchShippingOrderAction" event="searchShippingOrder"
             target="_blank">
    <s:param name="shippingOrderGatewayId" value="${shippingOrder.gatewayOrderId}"/> Search SO
  </s:link>)
    (<s:link beanclass="com.hk.web.action.admin.shippingOrder.ShippingOrderLifecycleAction" event="pre" target="_blank">
    SO Lifecycle
    <s:param name="shippingOrder" value="${shippingOrder}"/>
  </s:link>)
    (<s:link beanclass="com.hk.web.action.core.accounting.SOInvoiceAction" class="invoiceLink" event="pre" target="_blank">
    <s:param name="shippingOrder" value="${shippingOrder}"/>
    Invoice
  </s:link>)
    &nbsp;&nbsp;&nbsp;(<s:link beanclass="com.hk.web.action.core.accounting.SOInvoiceAction" event="pre"
                         target="_blank" class="personalCareInvoiceLink">
    <s:param name="shippingOrder" value="${shippingOrder}"/>
    <s:param name="printable" value="true"/>
    PC Invoice
  </s:link>)
    <shiro:hasAnyRoles name="<%=RoleConstants.ROLE_GROUP_ACCOUNTING_INVOICE%>">
    (<s:link beanclass="com.hk.web.action.core.accounting.AccountingInvoiceAction" event="pre" target="_blank">
    <s:param name="shippingOrder" value="${shippingOrder}"/>
    Accounting Invoice
  </s:link>)
    </shiro:hasAnyRoles>
    <c:if test="${isActionQueue == true}">
      <shiro:hasPermission name="<%=PermissionConstants.EDIT_LINEITEM%>">
        &nbsp;&nbsp;(<s:link beanclass="com.hk.web.action.admin.shippingOrder.EditShippingOrderAction" class="editSO">
        <s:param name="shippingOrder" value="${shippingOrder}"/>
        Edit SO
      </s:link>)
      </shiro:hasPermission>
      <shiro:hasAnyRoles name="<%=RoleConstants.ROLE_GROUP_CATMAN_ADMIN%>">
        &nbsp;&nbsp;(<s:link beanclass="com.hk.web.action.admin.shippingOrder.ShippingOrderAction" event="flipWarehouse"
                             class="flipWarehouse">
        <s:param name="shippingOrder" value="${shippingOrder}"/>
        Flip Warehouse
      </s:link>)
        &nbsp;&nbsp;(<s:link beanclass="com.hk.web.action.admin.shippingOrder.SplitShippingOrderAction"
                             class="splitShippingOrder">
        <s:param name="shippingOrder" value="${shippingOrder}"/>
        Split Shipping Order
      </s:link>)
      </shiro:hasAnyRoles>
       &nbsp;&nbsp;(<s:link beanclass="com.hk.web.action.admin.shippingOrder.ShippingOrderAction" event="cancelShippingOrder"
                             class="cancelSO">
        <s:param name="shippingOrder" value="${shippingOrder}"/>
        Cancel SO
      </s:link>)
    </c:if>
    <c:if test="${isSearchShippingOrder}">
      <shiro:hasAnyRoles name="<%=RoleConstants.ROLE_GROUP_CATMAN_ADMIN%>">
        &nbsp;&nbsp;(<s:link beanclass="com.hk.web.action.admin.shippingOrder.ShippingOrderAction" event="flipWarehouse"
                             class="flipWarehouse">
        <s:param name="shippingOrder" value="${shippingOrder}"/>
        Flip Warehouse
      </s:link>)
      </shiro:hasAnyRoles>
      <shiro:hasAnyRoles name="<%=RoleConstants.ROLE_GROUP_LOGISTICS_ADMIN%>">
        <c:set var="shippingOrderStatusId" value="${shippingOrder.orderStatus.id}"/>
        <c:if
            test="${shippingOrderStatusId == shippingOrderStatusShipped || shippingOrderStatusId == shippingOrderStatusDelivered}">
          <br/>
          <s:form beanclass="com.hk.web.action.admin.shippingOrder.ShippingOrderAction" class="markRTOForm">
            <s:param name="shippingOrder" value="${shippingOrder.id}"/>
            <div class="buttons">
              <s:submit name="markRTO" value="Mark RTO" class="markRTOButton"/>
            </div>
          </s:form>
          <script type="text/javascript">
            $('.markRTOButton').click(function() {
              var proceed = confirm('Are you sure?');
              if (!proceed) return false;
            });

            $('.markRTOForm').ajaxForm({dataType: 'json', success: _markOrderRTO});

            function _markOrderRTO(res) {
              if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                alert("Order marked as RTO");
              }
            }
          </script>
        </c:if>
      </shiro:hasAnyRoles>
    </c:if>
  </div>
</td>
<c:if test="${isActionQueue == false}">
  <td>
    <div id="userContactDetails">
      <div class="floatleft">
        Name : <span class="or">${baseOrder.user.name}</span>
      </div>
      <span style="margin-left:10px;">
        Email: (<s:link beanclass="com.hk.web.action.admin.user.SearchUserAction" event="search">
        <s:param name="userFilterDto.login" value="${baseOrder.user.login}"/>
        ${baseOrder.user.login}
      </s:link>)
      </span>

      <div class="clear"></div>
      <div class="floatleft" style="margin-top:3px;">
        Processed Orders# ${hk:getProcessedOrdersCount(baseOrder.user)}
      </div>
      <span style="margin-left:10px;">
        (<s:link beanclass="com.hk.web.action.admin.order.search.SearchOrderAction" event="searchOrders"
                 target="_blank"><s:param
          name="email" value="${baseOrder.user.login}"/>See previous orders</s:link>)
      </span>

      <div class="clear"></div>
      <div class="floatleft" style="margin-top:3px;">
        <c:set var="baseOrderAddress" value="${baseOrder.address}"/>
          ${baseOrderAddress.name}<br/>
          ${baseOrderAddress.city}<br/>
          ${baseOrderAddress.state} - ( ${baseOrderAddress.pin} ) - ${baseOrderAddress.phone}<br/>
      </div>
    </div>
  </td>
</c:if>
<td id="shippingOrderLineItems-${shippingOrder.id}">
  <table>
    <c:forEach items="${shippingOrder.lineItems}" var="lineItem" varStatus="lineItemCtr">
      <c:set var="cartLineItem" value="${lineItem.cartLineItem}"/>
      <c:set var="productVariant" value="${lineItem.sku.productVariant}"/>
      <c:set var="sku" value="${lineItem.sku}"/>
      <c:set var="skuNetInventory" value="${hk:netInventory(sku)}"/>

      <%--<tr>--%>
      <c:choose>
        <%--if order is in action awaiting state draw appropriate colour border for line item div--%>
        <c:when test="${shippingOrderStatusActionAwaiting == shippingOrder.orderStatus.id}">
          <c:choose>
            <c:when test="${hk:bookedQty(sku) <= skuNetInventory}">
              <tr style="border-left:5px solid green;">
            </c:when>
            <c:otherwise>
              <c:choose>
                <c:when test="${lineItem.qty <= skuNetInventory}">
                  <tr style="border-left:5px solid orange;">
                </c:when>
                <c:otherwise>
                  <tr style="border-left:5px solid red;">
                </c:otherwise>
              </c:choose>
            </c:otherwise>
          </c:choose>
        </c:when>
        <%--else draw a simple border div--%>
        <c:otherwise>
          <tr style="border-left:1px solid gray;">
        </c:otherwise>
      </c:choose>
      <td style="border-bottom:1px solid gray;border-top:1px solid gray;">
          ${productVariant.product.name}

        <c:if test="${cartLineItem.comboInstance != null}">
                <span style="color:crimson;text-decoration:underline">
                <br/>(Part of Combo: ${cartLineItem.comboInstance.combo.name})
                </span>
        </c:if>
        <c:if test="${productVariant.paymentType.id == lineItem_Service_Postpaid}">
                <span style="color:blue;text-decoration:underline">
                <br/>(Postpaid Service: ${productVariant.product.name})
                </span>
        </c:if>

          <%--<div class="floatleft">--%>
          <%--<br/>Dispatch Within: ${productVariant.product.minDays} - ${productVariant.product.maxDays} Days--%>
          <%--</div>--%>

        <c:if test="${not empty productVariant.productOptions}">
          <%--<br/>--%>
          <%-- <span style="word-wrap:break-word">--%>
          <c:forEach items="${productVariant.productOptions}" var="productOption"
                     varStatus="optionCtr">
            ${productOption.name} ${productOption.value}${!optionCtr.last?',':'|'}
            <c:if test="${not empty cartLineItem.cartLineItemExtraOptions}">
              <c:forEach items="${cartLineItem.cartLineItemExtraOptions}" var="extraOptions"
                         varStatus="extraOptionCtr">
                ${extraOptions.name} ${extraOptions.value}${!extraOptionCtr.last?',':''}
              </c:forEach>
            </c:if>
          </c:forEach>
          <%-- </span>--%>

        </c:if>
        <c:if test="${not empty cartLineItem.cartLineItemConfig.cartLineItemConfigValues}">

          <c:set var="TH" value="TH"/>
          <c:set var="THBF" value="THBF"/>
          <c:set var="CO" value="CO"/>
          <c:set var="COBF" value="COBF"/>
          <c:forEach items="${cartLineItem.cartLineItemConfig.cartLineItemConfigValues}" var="configValue"
                     varStatus="configCtr">
            <c:set var="variantConfigOption" value="${configValue.variantConfigOption}"/>
            <c:set var="additionalParam" value="${variantConfigOption.additionalParam}"/>
            ${variantConfigOption.displayName} : ${configValue.value}
            <c:if
                test="${(additionalParam ne TH) or (additionalParam ne THBF) or (additionalParam ne CO) or (additionalParam ne COBF) }">
              <c:if
                  test="${fn:startsWith(variantConfigOption.name, 'R')==true}">
                (R)
              </c:if>
              <c:if
                  test="${fn:startsWith(variantConfigOption.name, 'L')==true}">
                (L)
              </c:if>
            </c:if>
            ${!configCtr.last?',':''}

          </c:forEach>
        </c:if>
          <%--<c:if test="${not empty cartLineItem.cartLineItemConfig.cartLineItemConfigValues}">--%>
          <%--<br/>--%>
          <%--<span style="word-wrap:break-word">--%>
          <%--<c:forEach items="${cartLineItem.cartLineItemConfig.cartLineItemConfigValues}"--%>
          <%--var="configValue"--%>
          <%--varStatus="configCtr">--%>
          <%--<c:set var="variantConfigOption" value="${configValue.variantConfigOption}"/>--%>
          <%--${variantConfigOption.displayName} : ${configValue.value} ${!configCtr.last?',':''}--%>
          <%--</c:forEach>--%>
          <%--</span>--%>
          <%--</c:if>--%>
          <%--</span>--%>
        <c:if test="${isActionQueue == true}">
          <c:if test="${productVariant.product.jit}">
            ,<strong>Dispatch : ${productVariant.product.minDays}-${productVariant.product.maxDays} days </strong>
          </c:if>
        </c:if>
      </td>
      <td style="border:1px solid gray;border-left:none;">
          <%--<c:if test="${orderStatusActionAwaiting == shippingOrder.shippingOrderStatus.id}">--%>
          ${lineItem.qty}
        <c:if test="${!productVariant.product.service}">
          [${hk:bookedQty(sku)}]
          (${skuNetInventory})
        </c:if>
      </td>
      </tr>
      <%--</c:if>--%>
    </c:forEach>
  </table>
</td>
<c:set var="shipment" value="${shippingOrder.shipment}"/>
<c:if test="${shipment !=null}">
  <td>
    <div class="floatleft">
      Courier: <strong>${shipment.courier.name}</strong>
    </div>
    <div class="clear"></div>
    <div class="floatleft">
      Tracking Id: <strong>${shipment.trackingId}</strong>
    </div>
    <div class="clear"></div>
    <div class="floatleft">
      Size: ${shipment.boxSize}, Weight: ${shipment.boxWeight}
    </div>
    <div class="clear"></div>
  </td>
</c:if>
<td>
                  <span
                      class="orderStatusName"><strong>${shippingOrder.orderStatus.name}</strong></span>
  <c:choose>
    <c:when test="${shippingOrder.orderStatus.id == shippingOrderStatusHold}">
      <s:link beanclass="com.hk.web.action.admin.order.OrderOnHoldAction" event="unHoldShippingOrder"
              title="Unhold Shipping Order" class="orderStatusLink onHoldStatusLink">
        <s:param name="shippingOrder" value="${shippingOrder.id}"/>
        <img src="<hk:vhostImage/>/images/admin/icon_unhold.png" alt="Unhold Shipping Order"
             title="Unhold Shipping Order"/>
      </s:link>
    </c:when>
    <c:otherwise>
      <c:choose>
        <c:when test="${shippingOrder.orderStatus.id == shippingOrderStatusActionAwaiting}">
          <s:link beanclass="com.hk.web.action.admin.order.OrderOnHoldAction" event="holdShippingOrder"
                  title="Put Shipping Order on Hold"
                  class="orderStatusLink normalStatusLink">
            <s:param name="shippingOrder" value="${shippingOrder.id}"/>
            <img src="<hk:vhostImage/>/images/admin/icon_hold.png" alt="Put Shipping Order on Hold"
                 title="Put Shipping Order on Hold"/>
          </s:link>
        </c:when>
      </c:choose>
    </c:otherwise>
  </c:choose>
</td>
<c:if test="${hasAction == true}">
  <td>
    <c:if test="${shippingOrder.baseOrder.payment.paymentStatus.id != paymentStatusAuthPending}">
      <input type="checkbox" dataId="${shippingOrder.id}" class="shippingOrderDetailCheckbox"/>
    </c:if>
  </td>
</c:if>
</c:forEach>
</tr>
  <%--</c:forEach>--%>
</table>
</s:layout-definition>
