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
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.constants.analytics.EnumReasonType" %>
<%@ page import="com.hk.domain.catalog.product.VariantConfigOptionParam" %>
<%@ page import="java.util.HashSet" %>
<%@ page import="java.util.Collection" %>
<%@ page import="com.hk.domain.queue.ActionTask" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:layout-definition>
<%
    Set<ShippingOrder> shippingOrders = new HashSet<ShippingOrder>();
    ShippingOrder shippingOrder = (ShippingOrder) pageContext.getAttribute("shippingOrder");
    if (shippingOrder != null) {
        shippingOrders.add(shippingOrder);
    } else {
        shippingOrders.addAll((Collection<? extends ShippingOrder>) pageContext.getAttribute("shippingOrders"));
    }
    pageContext.setAttribute("shippingOrders", shippingOrders);
    String isActionQueue = (String) pageContext.getAttribute("isActionQueue");
    String isProcessingQueue = (String) pageContext.getAttribute("isProcessingQueue");
    String isDropShipQueue = (String) pageContext.getAttribute("isDropShipQueue");
    String isServiceQueue = (String) pageContext.getAttribute("isServiceQueue");
    String isShipmentQueue = (String) pageContext.getAttribute("isShipmentQueue");
    if (isActionQueue != null) {
        pageContext.setAttribute("isActionQueue", Boolean.valueOf(isActionQueue));
    } else {
        pageContext.setAttribute("isActionQueue", false);
    }
    if (isShipmentQueue != null) {
        pageContext.setAttribute("isShipmentQueue", Boolean.valueOf(isShipmentQueue));
    } else {
        pageContext.setAttribute("isShipmentQueue", false);
    }
    if (isProcessingQueue != null) {
        pageContext.setAttribute("isProcessingQueue", Boolean.valueOf(isProcessingQueue));
    } else {
        pageContext.setAttribute("isProcessingQueue", false);
    }
    if (isDropShipQueue != null) {
        pageContext.setAttribute("isDropShipQueue", Boolean.valueOf(isDropShipQueue));
    } else {
        pageContext.setAttribute("isDropShipQueue", false);
    }
    if (isServiceQueue != null) {
        pageContext.setAttribute("isServiceQueue", Boolean.valueOf(isServiceQueue));
    } else {
        pageContext.setAttribute("isServiceQueue", false);
    }
    String showCourier = (String) pageContext.getAttribute("showCourier");
    if (showCourier != null) {
        pageContext.setAttribute("showCourier", Boolean.valueOf(showCourier));
    } else {
        pageContext.setAttribute("showCourier", false);
    }
    String hasAction = (String) pageContext.getAttribute("hasAction");
    if (hasAction != null) {
        pageContext.setAttribute("hasAction", Boolean.valueOf(hasAction));
    } else {
        pageContext.setAttribute("hasAction", true);
    }
    String isSearchShippingOrder = (String) pageContext.getAttribute("isSearchShippingOrder");
    if (isSearchShippingOrder != null) {
        pageContext.setAttribute("isSearchShippingOrder", Boolean.valueOf(isSearchShippingOrder));
    } else {
        pageContext.setAttribute("isSearchShippingOrder", false);
    }
%>
<c:set var="shippingOrderStatusActionAwaiting" value="<%=EnumShippingOrderStatus.SO_ActionAwaiting.getId()%>"/>
<c:set var="orderStatusHold" value="<%=EnumOrderStatus.OnHold.getId()%>"/>
<c:set var="shippingOrderStatusHold" value="<%=EnumShippingOrderStatus.SO_OnHold.getId()%>"/>
<c:set var="paymentStatusAuthPending" value="<%=EnumPaymentStatus.AUTHORIZATION_PENDING.getId()%>"/>
<c:set var="shippingOrderStatusShipped" value="<%=EnumShippingOrderStatus.SO_Shipped.getId()%>"/>
<c:set var="shippingOrderStatusCancelled" value="<%=EnumShippingOrderStatus.SO_Cancelled.getId()%>"/>
<c:set var="shippingOrderStatusDelivered" value="<%=EnumShippingOrderStatus.SO_Delivered.getId()%>"/>
<c:set var="shippingOrderStatusRTO" value="<%=EnumShippingOrderStatus.SO_RTO.getId()%>"/>
<c:set var="shippingOrderStatusRTOInitiated" value="<%=EnumShippingOrderStatus.RTO_Initiated.getId()%>"/>
<c:set var="shippingOrderStatusLost" value="<%=EnumShippingOrderStatus.SO_Lost.getId()%>"/>
<c:set var="lineItem_Service_Postpaid" value="<%=EnumProductVariantPaymentType.Postpaid.getId()%>"/>
<c:set var="shippingOrderStatusDropShippingAwaiting" value="<%=EnumShippingOrderStatus.SO_ReadyForDropShipping.getId()%>"/>
<c:set var="shippingOrderStatusReversePickup" value="<%=EnumShippingOrderStatus.SO_ReversePickup_Initiated.getId()%>"/>

<c:set var="TH" value="<%=VariantConfigOptionParam.THICKNESS.param()%>"/>
<c:set var="THBF" value="<%=VariantConfigOptionParam.BFTHICKNESS.param()%>"/>
<c:set var="CO" value="<%=VariantConfigOptionParam.COATING.param()%>"/>
<c:set var="COBF" value="<%=VariantConfigOptionParam.BFCOATING.param()%>"/>
<c:set var="BRANDCO" value="<%=VariantConfigOptionParam.BRANDCO.param()%>"/>
<c:set var="BRANDTH" value="<%=VariantConfigOptionParam.BRANDTH.param()%>"/>
<c:set var="BRANDTHBF" value="<%=VariantConfigOptionParam.BRANDTHBF.param()%>"/>



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
${shippingOrder.id}
<c:forEach items="${shippingOrders}" var="shippingOrder" varStatus="shippingOrderCtr">
<c:set var="baseOrder" value="${shippingOrder.baseOrder}"/>
<c:set var="payment" value="${shippingOrder.baseOrder.payment}"/>
<tr id="shippingOrder-${shippingOrder.id}"
    style="margin-bottom: 5px;border: 1px dotted;overflow: hidden;padding: 3px;">
<%--<div id="shippingOrder-${shippingOrder.id}" class="detailDiv">--%>
<td id="shippingOrderDetail-${shippingOrder.id}" style="width: 300px;">
<div class="floatleft">
    Store ID: <strong>${shippingOrder.baseOrder.store.prefix}</strong>, Score: ${shippingOrder.baseOrder.score},  <span
        style="margin-left:10px;">
                    Basket: <strong>${shippingOrder.basketCategory}</strong></span>
</div>
<div class="clear" style=""></div>
<div class="floatleft">
    Gateway Id: <strong>${shippingOrder.gatewayOrderId}</strong>
</div>
<c:if test="${isSearchShippingOrder == true}">
    <div class="floatleft">
        BO Id: <strong>${shippingOrder.baseOrder.gatewayOrderId}</strong>
    </div>
</c:if>
<div class="clear" style=""></div>
<div class="floatleft">
  IsB2B?: <strong>${shippingOrder.baseOrder.b2bOrder}</strong>
  <br/>
  Warehouse: <strong>${shippingOrder.warehouse.identifier}</strong>
  <br/>
  Service Type: <strong>${shippingOrder.shipment.shipmentServiceType.name}</strong>
</div>
<div class="clear" style=""></div>
<div class="floatleft">
    Amount: <strong>Rs.<fmt:formatNumber value="${shippingOrder.amount}"
                                         pattern="<%=FormatUtils.currencyFormatPattern%>"/></strong>
    <span
            style="margin-left:10px;">
    Placed On : <fmt:formatDate value="${shippingOrder.baseOrder.payment.paymentDate}" type="date"/></span>
</div>
<div class="clear"></div>
<c:if test="${isActionQueue == false}">
    <c:if test="${shippingOrder.lastEscDate != null}">
        <div class="floatleft">
            Escalted On: <fmt:formatDate value="${shippingOrder.lastEscDate}" type="both" timeStyle="short"/>
        </div>
        <div class="clear"></div>
        <c:if test="${shippingOrder.shippingOrderStatus.id < shippingOrderStatusShipped && shippingOrder.shippingOrderStatus.id != shippingOrderStatusCancelled}">
            <div class="floatleft">
                <strong>(${hk:periodFromNow(shippingOrder.lastEscDate)})</strong>
            </div>
            <div class="clear"></div>
        </c:if>
    </c:if>
</c:if>
<c:if test="${shippingOrder.shippingOrderStatus.id < shippingOrderStatusShipped && shippingOrder.shippingOrderStatus.id != shippingOrderStatusCancelled}">
    <div class="floatleft">
        Target Dispatch : <fmt:formatDate value="${shippingOrder.targetDispatchDate}" type="date"/>
    </div>
    <div class="clear"></div>
    <div class="floatleft">
        <strong>(${hk:periodFromNow(shippingOrder.targetDispatchDate)})</strong>
    </div>
</c:if>
<div class="clear"></div>
<c:if test="${isActionQueue == true || isSearchShippingOrder == true}">
    <c:if test="${! empty shippingOrder.shippingOrderLifecycles || shippingOrder.shippingOrderStatus.id eq shippingOrderStatusCancelled }">

        <label style="font-weight:bold;">Last Activity:</label><br>
        ${shippingOrder.shippingOrderLifecycles[fn:length(shippingOrder.shippingOrderLifecycles)-1].shippingOrderLifeCycleActivity.name} Reason:
        ${shippingOrder.shippingOrderLifecycles[fn:length(shippingOrder.shippingOrderLifecycles)-1].lifecycleReasons[0].reason.classification.primary}-
        ${shippingOrder.shippingOrderLifecycles[fn:length(shippingOrder.shippingOrderLifecycles)-1].lifecycleReasons[0].reason.classification.secondary}-
        ${shippingOrder.shippingOrderLifecycles[fn:length(shippingOrder.shippingOrderLifecycles)-1].comments}
        on
        <br>
        <fmt:formatDate
                value="${shippingOrder.shippingOrderLifecycles[fn:length(shippingOrder.shippingOrderLifecycles)-1].activityDate}"
                type="both"/> by
        "${shippingOrder.shippingOrderLifecycles[fn:length(shippingOrder.shippingOrderLifecycles)-1].user.name}"
    </c:if>
</c:if>
<div class="clear"></div>
    <div class="floatleft">
    <c:if test="${isActionQueue == false}">
        (<s:link beanclass="com.hk.web.action.admin.order.search.SearchOrderAction" event="searchOrders"
                 target="_blank">
        <s:param name="orderId" value="${shippingOrder.baseOrder.id}"/> Search BO
    </s:link>)
    </c:if>
    (<s:link beanclass="com.hk.web.action.admin.order.search.SearchShippingOrderAction" event="searchShippingOrder"
             target="_blank">
        <s:param name="shippingOrderGatewayId" value="${shippingOrder.gatewayOrderId}"/> Search SO
    </s:link>)
    (<s:link beanclass="com.hk.web.action.admin.shippingOrder.ShippingOrderLifecycleAction" event="pre" target="_blank">
        SO Lifecycle
        <s:param name="shippingOrder" value="${shippingOrder}"/>
    </s:link>)
    (<s:link beanclass="com.hk.web.action.core.accounting.SOInvoiceAction" class="invoiceLink" event="pre"
             target="_blank">
        <s:param name="shippingOrder" value="${shippingOrder}"/>
        Invoice
    </s:link>)
        &nbsp;&nbsp;&nbsp;(<s:link beanclass="com.hk.web.action.core.accounting.SOInvoiceAction" event="pre"
                                   target="_blank" class="personalCareInvoiceLink">
        <s:param name="shippingOrder" value="${shippingOrder}"/>
        <s:param name="printable" value="true"/>
        PC Invoice
    </s:link>)
          (<s:link beanclass="com.hk.web.action.core.accounting.AccountingInvoiceAction" event="pre"
                   target="_blank">
          <s:param name="shippingOrder" value="${shippingOrder}"/>
          Accounting Invoice
      </s:link>)
        <shiro:hasPermission name="<%=PermissionConstants.OPS_MANAGER_SRS_VIEW%>">
            <c:if test="${shippingOrderStatusDropShippingAwaiting == shippingOrder.orderStatus.id}">
                (<s:link beanclass="com.hk.web.action.admin.courier.ShipmentResolutionAction" event="createAutoShipment"
                         target="_blank">
                <s:param name="shippingOrder" value="${shippingOrder}"/>
                Create Auto Shipment
            </s:link>)
                (<s:link beanclass="com.hk.web.action.admin.courier.ShipmentResolutionAction" event="search"
                         target="_blank">
                <s:param name="gatewayOrderId" value="${shippingOrder.gatewayOrderId}"/>
                Create Manual Shipment
            </s:link>)
            </c:if>
        </shiro:hasPermission>

        <c:if test="${isActionQueue == true}">
            <shiro:hasPermission name="<%=PermissionConstants.EDIT_LINEITEM%>">
                &nbsp;&nbsp;(<s:link beanclass="com.hk.web.action.admin.shippingOrder.EditShippingOrderAction" class="editSO">
                <s:param name="shippingOrder" value="${shippingOrder}"/>
                Edit SO
            </s:link>)
            </shiro:hasPermission>
            <shiro:hasAnyRoles name="<%=RoleConstants.ROLE_GROUP_CATMAN_ADMIN%>">
                &nbsp;&nbsp;(<s:link beanclass="com.hk.web.action.admin.shippingOrder.ShippingOrderAction" event="pre">
                <s:param name="shippingOrder" value="${shippingOrder}"/>
                Flip Warehouse
            </s:link>)
                &nbsp;&nbsp;(<s:link beanclass="com.hk.web.action.admin.shippingOrder.ShippingOrderAction"
                                           event="autoEscalateShippingOrder" class="autoEscalate">
                <s:param name="shippingOrder" value="${shippingOrder}"/>
                <s:param name="firewall" value="<%=Boolean.FALSE%>"/>
                Auto Escalate SO
            </s:link>)
                &nbsp;&nbsp;(<s:link beanclass="com.hk.web.action.admin.shippingOrder.ShippingOrderAction"
                                     event="manualEscalateShippingOrder" class="manualEscalate">
                <s:param name="shippingOrder" value="${shippingOrder}"/>
                Manual Escalate SO
            </s:link>)
                &nbsp;&nbsp;(<s:link beanclass="com.hk.web.action.admin.shippingOrder.SplitShippingOrderAction"
                                     class="splitShippingOrder">
                <s:param name="shippingOrder" value="${shippingOrder}"/>
                Split SO
            </s:link>)               
                <c:if test="${isSearchShippingOrder == true}">
                    &nbsp;&nbsp;(<s:link beanclass="com.hk.web.action.admin.order.split.PseudoOrderSplitAction"
                                         class="pseudoSplitBaseOrder" event="splitOrderPractically">
                    <s:param name="gatewayOrderId" value="${shippingOrder.baseOrder.gatewayOrderId}"/>
                    BO Split Analytics
                </s:link>)
                </c:if>
            </shiro:hasAnyRoles>
            <shiro:hasPermission name="<%=PermissionConstants.OPS_MANAGER_SRS_VIEW%>">
                &nbsp;&nbsp;(<s:link beanclass="com.hk.web.action.admin.courier.ShipmentResolutionAction" event="search"
                                     class="resolveShipment" target="_blank">
                <s:param name="gatewayOrderId" value="${shippingOrder.gatewayOrderId}"/>
                Resolve Shipment
            </s:link>)
            </shiro:hasPermission>
        </c:if>

        <%--&nbsp;&nbsp;(<s:link beanclass="com.hk.web.action.admin.shippingOrder.ShippingOrderAction"--%>
                             <%--event="cancelShippingOrder"--%>
                             <%--class="cancelSO button_orange">--%>
        <%--<s:param name="shippingOrder" value="${shippingOrder}"/>--%>
        <%--Cancel SO--%>
    <%--</s:link>)--%>
    <%--</c:if>--%>

    <c:if test="${isActionQueue == false || isSearchShippingOrder == true }">
    <s:form beanclass="com.hk.web.action.admin.shippingOrder.ShippingOrderAction">
            <s:param name="shippingOrder" value="${shippingOrder}"/>

            <c:if test="${shippingOrder.shippingOrderStatus.id eq shippingOrderStatusActionAwaiting}">
            <br>
            Reason:
            <s:select name="shippingOrder.reason" id="soReason">
            <s:option value="">-------Select-------</s:option>
            <c:set var="soCancelReasonVar" value="<%=EnumReasonType.So_Cancelled.getName()%>"/>
            <c:forEach items="${hk:getReasonsByType(soCancelReasonVar)}" var="soCancelReason">
            <s:option value="${soCancelReason.id}"> ${soCancelReason.classification.primary} - ${soCancelReason.classification.secondary} </s:option>
            </c:forEach>
             </s:select>
             <br>
             Remark:
                <s:textarea name="cancellationRemark" id="cancellationId" style="height:100px"></s:textarea>
                <div class="buttons">
                   <s:submit name="cancelShippingOrder" value="Cancel SO" class="cancelSO"/>
                </div>
            </c:if>
        </s:form>                             
        <script type="text/javascript">
            $ ('.cancelSO').click(function(){
                if($('#soReason').val()==""){
                alert("Please select Reason");
                return false;
                }
                 var proceed = confirm('Are you sure you want to cancel shipping order?');
                 if (!proceed) return false;
                $(this).hide();
             });
        </script>
    </c:if>
    <c:if test="${isSearchShippingOrder}">
            <c:if test="${shippingOrder.orderStatus.id == shippingOrderStatusDelivered ||
                    shippingOrder.orderStatus.id == shippingOrderStatusReversePickup}">

                <shiro:hasAnyRoles name="<%=RoleConstants.CUSTOMER_SUPPORT%>">
                    <s:form beanclass="com.hk.web.action.admin.shippingOrder.ShippingOrderAction">
                        <s:hidden name="shippingOrder" value="${shippingOrder.id}"/>
                        <c:if test="${shippingOrder.orderStatus.id == shippingOrderStatusDelivered}">
                            <b>Customer Satisfaction:</b>
                            <s:select name="shippingOrder.orderStatus">
                                <s:option value="<%=EnumShippingOrderStatus.SO_Customer_Appeasement.getId()%>">
                                    <%=EnumShippingOrderStatus.SO_Customer_Appeasement.getName()%>
                                </s:option>
                            </s:select>
                            <br/><b>Customer Reason:</b>
                            <s:select name="customerSatisfyReason" id="customer-reason">
                                <s:option value="null">-Select Reason-</s:option>
                                <s:option value="Damaged Product">Damaged Product</s:option>
                                <s:option value="Expired Product">Expired Product</s:option>
                                <s:option value="Wrong Product">Wrong Product</s:option>
                                <s:option value="Not Interested">Not Interested</s:option>
                            </s:select>
                            <s:submit class="markOrderCustomerSatisfyButton" name="markOrderCustomerSatisfy" value="Save"
                                  style="padding:1px;"/>
                        </c:if>

                        <c:if test="${shippingOrder.orderStatus.id == shippingOrderStatusReversePickup}">
                            <b>Customer Return:</b>
                            <s:select name="shippingOrder.orderStatus">
                                <s:option value="<%=EnumShippingOrderStatus.SO_Customer_Return_Replaced.getId()%>">
                                    <%=EnumShippingOrderStatus.SO_Customer_Return_Replaced.getName()%>
                                </s:option>
                                <s:option value="<%=EnumShippingOrderStatus.SO_Customer_Return_Refunded.getId()%>">
                                    <%=EnumShippingOrderStatus.SO_Customer_Return_Refunded.getName()%>
                                </s:option>
                            </s:select>
                            <s:submit name="markOrderCustomerReturn" value="Save"
                                  style="padding:1px;"/>
                        </c:if>
                    </s:form>

                    <script type="text/javascript">
                        $('.markOrderCustomerSatisfyButton').click(function() {
                            if ($('#customer-reason').val() == "null") {
                                alert("Please select a reason for Customer Satisfaction !");
                                return false;
                            }

                            var proceed = confirm('Are you sure?');
                            if (!proceed) return false;
                        });

                    </script>
                </shiro:hasAnyRoles>
            </c:if>
            <shiro:hasAnyRoles name="<%=RoleConstants.ROLE_GROUP_LOGISTICS_ADMIN%>">
                <c:set var="shippingOrderStatusId" value="${shippingOrder.orderStatus.id}"/>
                <c:if
                        test="${shippingOrderStatusId == shippingOrderStatusShipped || shippingOrderStatusId == shippingOrderStatusLost}">
                    <br/>
                    <s:form beanclass="com.hk.web.action.admin.shippingOrder.ShippingOrderAction" class="markRTOForm">
                        <s:param name="shippingOrder" value="${shippingOrder.id}"/>
	                    <s:label name="Reason for RTO:" style="margin-left:7px;"/>
	                    <s:select name="rtoReason" id="rto-reason">
		                    <s:option value="null">-Select Reason-</s:option>
		                    <hk:master-data-collection service="<%=MasterDataDao.class%>"
		                                               serviceProperty="replacementOrderReasonForRto" value="id"
		                                               label="name"/>
	                    </s:select>
                        <div class="buttons">
                            <s:submit name="initiateRTO" value="Initiate RTO" class="initiateRTOButton"/>
                        </div>
                    </s:form>
                    <script type="text/javascript">
                        $('.initiateRTOButton').click(function() {
	                        if($('#rto-reason').val()=="null"){
		                        alert("Please select a reason for RTO !");
		                        return false;
	                        }
                            var proceed = confirm('Are you sure?');
                            if (!proceed) return false;
                        });

                        $('.markRTOForm').ajaxForm({dataType: 'json', success: _markOrderRTO});

                        function _markOrderRTO(res) {
                            if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                                alert("RTO Initiated.");
                            }
                        }
                    </script>
                </c:if>

                <c:if test="${shippingOrderStatusId == shippingOrderStatusRTOInitiated}">
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
        <c:choose>
            <c:when test="${baseOrder.priorityOrder}">
                <div id="userContactDetails" style="background:#F6CECE;"> </div>
            </c:when>
            <c:otherwise>
                <div id="userContactDetails" >  </div>
            </c:otherwise>
        </c:choose>

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
            <%--Processed Orders# ${hk:getProcessedOrdersCount(baseOrder.user)}--%>
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
            ${baseOrderAddress.state} - ( ${baseOrderAddress.pincode.pincode} ) - ${baseOrderAddress.phone}<br/>
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
				<c:if test="${isActionQueue == true}">
					<c:set var="skuNetInventory" value="${hk:netInventory(sku)}" />
				</c:if>

			<%--<tr>--%>
            <c:choose>
                <%--if order is in action awaiting state draw appropriate colour border for line item div--%>
                <c:when test="${shippingOrderStatusActionAwaiting == shippingOrder.orderStatus.id || shippingOrderStatusHold == shippingOrder.orderStatus.id}">
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
                    ${productVariant.id}
                <s:link beanclass="com.hk.web.action.core.catalog.product.ProductAction" event="pre" style="font-size:12px !important; font-weight:bold !important;" >
                    <s:param name="productId" value="${productVariant.product.id}"/>
                     ${productVariant.product.name}
                </s:link>
                <c:if test="${cartLineItem.comboInstance != null}">
                <span style="color:crimson;text-decoration:underline">
                <br/>(Part of Combo: ${cartLineItem.comboInstance.combo.name})
                </span>
                </c:if>
                <em>
                    <span>
                        <c:forEach items="${productVariant.productOptions}"
                                   var="productOption">
                            <c:if test="${hk:showOptionOnUI(productOption.name)}">
                                ${productOption.name}:${productOption.value};
                            </c:if>
                        </c:forEach>
                    </span>
                    <span>
                            ${cartLineItem.extraOptionsPipeSeparated}
                    </span>
                </em>

                <c:if test="${cartLineItem.cartLineItemConfig.cartLineItemConfigValues !=null}">

                    <c:forEach items="${cartLineItem.cartLineItemConfig.cartLineItemConfigValues}"
                               var="configValue" varStatus="configValueCtr">
                        <c:set var="additinalParam"
                               value="${configValue.variantConfigOption.additionalParam}"/>
                        <c:if
                                test="${( additinalParam == TH || additinalParam == THBF
								|| additinalParam == CO || additinalParam == COBF || additinalParam == BRANDCO || additinalParam == BRANDTH
								|| additinalParam == BRANDTHBF) }">
                            ${configValue.variantConfigOption.displayName}:${configValue.value}|

                        </c:if>
                    </c:forEach>
                    <table>
                        <tr>
                            <td style="width:10%;" ><b>Right</b></td>
                            <c:forEach items="${cartLineItem.cartLineItemConfig.cartLineItemConfigValues}"
                                       var="configValue" varStatus="configValueCtr">
                                <c:set var="additinalParam"
                                       value="${configValue.variantConfigOption.additionalParam}"/>

                                <c:set var="side" value="${configValue.variantConfigOption.name}"/>
                                <c:if
                                        test="${  fn:startsWith(side,'R' ) && !( additinalParam == TH || additinalParam == THBF
								|| additinalParam == CO || additinalParam == COBF || additinalParam == BRANDCO || additinalParam == BRANDTH
								|| additinalParam == BRANDTHBF) }">
                                    <td style="width:25%;">
                                        <b>${configValue.variantConfigOption.displayName}:${configValue.value}</b>
                                    </td>
                                </c:if>
                            </c:forEach>
                        </tr>
                        <tr>
                            <td style="width:10%;"><b>Left</b></td>
                            <c:forEach items="${cartLineItem.cartLineItemConfig.cartLineItemConfigValues}"
                                       var="configValue" varStatus="configValueCtr">
                                <c:set var="additinalParam"
                                       value="${configValue.variantConfigOption.additionalParam}"/>
                                <c:set var="side" value="${configValue.variantConfigOption.name}"/>
                                <c:if
                                        test="${fn:startsWith(side,'L' ) && !( additinalParam == TH || additinalParam == THBF
								|| additinalParam == CO || additinalParam == COBF || additinalParam == BRANDCO || additinalParam == BRANDTH
								|| additinalParam == BRANDTHBF)}">
                                    <td style="width:25%;">
                                        <b>${configValue.variantConfigOption.displayName}:${configValue.value}</b>
                                    </td>
                                </c:if>
                            </c:forEach>
                        </tr>
                    </table>
                </c:if>
                    <c:choose>
                        <c:when test="${productVariant.product.groundShipping}">
                             <span style="color: #ff0000;">(G)</span>
                        </c:when>
                        <c:otherwise>
                            <span style="color: #ff0000;">(A)</span>
                        </c:otherwise>
                    </c:choose>
                    <c:if test="${shippingOrder.dropShipping}">
                        <span style="color: #ff0000;">(D)</span>
                    </c:if>
                        <c:if test="${isActionQueue == true}">
                            Dispatch : ${productVariant.product.minDays}-${productVariant.product.maxDays} days
                        </c:if>
            </td>
            <td style="border:1px solid gray;border-left:none;">
                <%--<c:if test="${orderStatusActionAwaiting == shippingOrder.shippingOrderStatus.id}">--%>
                ${lineItem.qty}
                <c:if test="${isActionQueue == true}">
							<c:if test="${!productVariant.product.service}">
                    [${hk:bookedQty(sku)}]
                    (${skuNetInventory})
                </c:if>
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
            Courier: <strong>${shipment.awb.courier.name}</strong>
        </div>
        <div class="clear"></div>
        <div class="floatleft">
	        Tracking Id: <strong>${shipment.awb.awbNumber}</strong>
        </div>
        <div class="clear"></div>
        <div class="floatleft">
            Size: ${shipment.boxSize.name}, Weight: ${shipment.boxWeight}
        </div>
        <div class="clear"></div>
	    <div class="floatleft">
            Picker: ${shipment.picker}, Packer: ${shipment.packer}
        </div>
        <div class="clear"></div>
        <shiro:hasAnyRoles name="<%=RoleConstants.CUSTOMER_SUPPORT%>">
            <c:if test="${shippingOrder.orderStatus.id == shippingOrderStatusDelivered}">
                <div class="floatleft">
                    <s:link beanclass="com.hk.web.action.admin.courier.CreateReverseOrderAction"
                            target="_blank">
                        <s:param name="shippingOrder" value="${shippingOrder.id}"/>Reverse Pickup</s:link>
                </div>
                <div class="clear"></div>
            </c:if>
        </shiro:hasAnyRoles>
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
                <s:param name="isActionQueue" value="${isActionQueue}" />
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
                        <s:param name="isActionQueue" value="${isActionQueue}" />
                        <img src="<hk:vhostImage/>/images/admin/icon_hold.png" alt="Put Shipping Order on Hold"
                             title="Put Shipping Order on Hold"/>
                    </s:link>
                </c:when>
            </c:choose>
        </c:otherwise>
    </c:choose>
    <c:if test="${hasAction == true || isDropShipQueue == true}">
        <c:if test="${shippingOrder.baseOrder.payment.paymentStatus.id != paymentStatusAuthPending}">
            <c:if test="${isProcessingQueue == true || isShipmentQueue == true || isDropShipQueue == true}">
                <select name="shippingOrderReason_${shippingOrder.id}"
                          class="shippingOrderReason_${shippingOrder.id}">
                    <option value="">Choose Reason</option>
                    <c:set var="escalateBackReason" value="<%=EnumReasonType.Escalate_Back.getName()%>"/>
                    <c:forEach items="${hk:getReasonsByType(escalateBackReason)}" var="reason">
                        <option value="${reason.id}">${reason.classification.primary}- ${reason.classification.secondary}</option>
                    </c:forEach>
                </select>
            </c:if>
            <input type="checkbox" dataId="${shippingOrder.id}" class="shippingOrderDetailCheckbox"/>
        </c:if>
    </c:if>
</td>
<c:if test="${isServiceQueue== true}">
    <td>
        <c:if test="${shippingOrder.baseOrder.payment.paymentStatus.id == paymentStatusAuthPending}">
            <s:link beanclass="com.hk.web.action.admin.queue.ServiceQueueAction" event="moveToActionAwaiting"
                    dataId="${shippingOrder.id}" class="movetoactionqueue">
                (move back to action queue)
            </s:link>
        </c:if>
    </td>
</c:if>
</c:forEach>
</tr>
</table>
</s:layout-definition>
