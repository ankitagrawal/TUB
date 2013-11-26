<%--
  Created by IntelliJ IDEA.
  User: Pradeep
  Date: 7/12/12
  Time: 3:34 PM
--%>
<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.order.EnumCartLineItemType" %>
<%@ page import="com.hk.constants.order.EnumOrderStatus" %>
<%@ page import="com.hk.constants.payment.EnumPaymentMode" %>
<%@ page import="com.hk.constants.payment.EnumPaymentStatus" %>
<%@ page import="com.hk.constants.shippingOrder.EnumShippingOrderStatus" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page import="com.hk.constants.core.RoleConstants" %>
<%@ page import="com.hk.constants.subscription.EnumSubscriptionStatus" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.subscription.SearchSubscriptionAction" var="subscriptionAdmin" event="pre"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Subscription Search">

<s:layout-component name="htmlHead">
    <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>

    <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>

    <script type="text/javascript">

        $(document).ready(function() {

            $('.changeNextShipmentDateLink').click(function(){
                $(this).siblings('.changeNextShipmentDateDiv').show();
                return false;
            } );

            $('.cancelSubscriptionLink').click(function(){
                $(this).siblings('.cancelSubscriptionDiv').show();
                return false;
            } );


            $('.cancelSubscriptionButton').click(function(){
                var proceed = confirm('Are you sure?');
                if (!proceed) return false;
            });
            $('.changeShipmentDateButton').click(function(){
                var proceed = confirm('Are you sure?');
                if (!proceed) return false;
            });


            $('.confirmSubscriptionOrderButton').click(function(){
                var proceed = confirm('Are you sure?');
                if (!proceed) return false;
            });
        });
        function areYouSure(){
            var proceed = confirm('Are you sure?');
            if (!proceed) return false;
        }

    </script>
</s:layout-component>


<s:layout-component name="heading">${subscriptionAdmin.currentBreadcrumb.name}</s:layout-component>

<s:layout-component name="content">

<span id="ajaxLoader" style="display:none;"><img src="<hk:vhostImage/>/images/ajax-loader.gif"/></span>

<c:set var="lineItemType_Product" value="<%=EnumCartLineItemType.Product.getId()%>"/>
<c:set var="linItemStatusShipped" value="<%=EnumOrderStatus.Shipped.getId()%>"/>
<c:set var="shippingOrderStatusShipped" value="<%=EnumShippingOrderStatus.SO_Shipped.getId()%>"/>
<c:set var="linItemStatusDelivrd" value="<%=EnumOrderStatus.Delivered.getId()%>"/>
<c:set var="shippingOrderStatusDelivrd" value="<%=EnumShippingOrderStatus.SO_Delivered.getId()%>"/>
<c:set var="linItemStatusRTO" value="<%=EnumOrderStatus.RTO.getId()%>"/>
<c:set var="orderStatusCart" value="<%=EnumOrderStatus.InCart.getId()%>"/>
<c:set var="orderStatusCancelled" value="<%=EnumOrderStatus.Cancelled.getId()%>"/>
<c:set var="orderStatusPending" value="<%=EnumOrderStatus.InProcess.getId()%>"/>
<c:set var="orderStatusHold" value="<%=EnumOrderStatus.OnHold.getId()%>"/>
<c:set var="orderStatusPlaced" value="<%=EnumOrderStatus.Placed.getId()%>"/>
<c:set var="paymentStatusPending" value="<%=EnumPaymentStatus.AUTHORIZATION_PENDING.getId()%>"/>
<c:set var="constomerConfirmationAwaited" value="<%=EnumSubscriptionStatus.CustomerConfirmationAwaited.getId()%>"/>
<c:set var="subscriptionStatusCancelled" value="<%=EnumSubscriptionStatus.Cancelled.getId()%>"/>
<c:set var="subscriptionStatusCart" value="<%=EnumSubscriptionStatus.InCart.getId()%>"/>
<c:set var="subscriptionStatusAbandoned" value="<%=EnumSubscriptionStatus.Abandoned.getId()%>"/>
<c:set var="subscriptionStatusInProcess" value="<%=EnumSubscriptionStatus.InProcess.getId()%>"/>
<c:set var="subscriptionStatusExpired" value="<%=EnumSubscriptionStatus.Expired.getId()%>"/>
<s:errors/>
<s:form beanclass="com.hk.web.action.admin.subscription.SubscriptionAdminAction" method="get">
    <s:submit name="escalateSubscriptions" value="escalate Subscriptions"></s:submit>
</s:form>
<s:form beanclass="com.hk.web.action.admin.subscription.SearchSubscriptionAction" method="get" autocomplete="false">
    <fieldset class="top_label">
        <ul>
            <div class="grouped">
                <li><label>Subscription ID</label> <s:text name="subscriptionId" style="width: 100px;"/></li>
                <li><label>Product Variant ID</label><s:text name="productVariant" style="width: 100px;"/></li>
                <li><label>BO Order ID</label> <s:text name="orderId" style="width: 100px;"/></li>
                <li><label>Email ID</label> <s:text name="email"/></li>
                <li><label>Login ID</label> <s:text name="login"/></li>
                <li><label>Name</label> <s:text name="name"/></li>
                <li><label>Phone</label> <s:text name="phone"/></li>
                <li><label>Status</label><s:select name="subscriptionStatus">
                    <option value="">Any subscription status</option>
                    <hk:master-data-collection service="<%=MasterDataDao.class%>" serviceProperty="subscriptionStatusList" value="id"
                                               label="name"/>
                </s:select></li>
                    <%--<li><label>Tracking ID</label> <s:text name="trackingId" style="width: 120px;"/></li>--%>
                    <%-- <li>
                        <label>Start
                            date</label><s:text class="date_input" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                                name="startDate"/>
                    </li>
                    <li>
                        <label>End
                            date</label><s:text class="date_input" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
                                                name="endDate"/>
                    </li>--%>
            </div>
        </ul>
        <div class="buttons"><s:submit name="searchSubscriptions" value="Search Subscriptions"/></div>
    </fieldset>
</s:form>

<s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${subscriptionAdmin}"/>
<s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${subscriptionAdmin}"/>

<c:forEach items="${subscriptionAdmin.subscriptionList}" var="subscription" varStatus="ctr">
<table class="cont" width="100%">
<thead>
<tr>
    <th width="10">Sr.</th>
    <th>Subscription Status</th>
    <th>Subscription Details</th>
    <th>Addresses</th>
    <th>Shipping Details</th>
</tr>
</thead>
<tr>
<td>${ctr.count}</td>
<td>
    <span class="xsml gry">Subscription Status :</span>

    <span class="orderStatusName or">${subscription.subscriptionStatus.name}</span>

    <br/>

    <c:if test="${subscription.subscriptionStatus.id == constomerConfirmationAwaited }">
        <c:choose>
            <c:when test="${subscription.productVariant.outOfStock}">
                    <strong style="color: red"> out of stock!!</strong>
            </c:when>
            <c:otherwise>
                <span class="codOrderText">&middot;</span>

                <a href="#" class="confirmSubscriptionOrderLink">(confirm subscription Order)</a>
                <div class="confirmSubscriptionDiv" style="display: none;">
                    <s:form beanclass="com.hk.web.action.admin.subscription.SubscriptionAdminAction" class="confirmSubscriptionOrderForm" >
                        <s:param name="subscription" value="${subscription.id}"/>
                        <s:submit name="confirmedByCustomer" class="confirmSubscriptionOrderButton" value="confirm"/>
                    </s:form>
                </div>
                <script type="text/javascript">

                    $('.confirmSubscriptionOrderLink').click(function(){
                        $(this).siblings('.confirmSubscriptionDiv').show();
                        return false;
                    } );

                    $('.confirmSubscriptionOrderForm').ajaxForm({dataType: 'json', success: _confirmSubscriptionOrder});

                    function _confirmSubscriptionOrder(res) {
                        if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                            var status = res.data.subscriptionStatus.name;
                            if (status == "Confirmed By Customer") {
                                alert("Subscription order Confirmed");
                                $('.confirmSubscriptionDiv').hide();
                            } else {
                                alert("Subscription cannot be confirmed");
                            }
                        }
                    }
                </script>
            </c:otherwise>
        </c:choose>
        <c:if test="${!subscription.productVariant.outOfStock}">

        </c:if>
    </c:if>
    <hr/>
	<c:if test="${! empty subscription.subscriptionLifecycles}">
		<c:set var="subLifeCycleCnt" value="${fn:length(subscription.subscriptionLifecycles)}"/>
		<c:if test="${subLifeCycleCnt > 0}">
			<s:link beanclass="com.hk.web.action.admin.subscription.SubscriptionLifecycleAction" event="pre"
			        target="_blank">
				<label style="font-weight:bold;">Last Activity:</label><br>
				<c:set var="lastActivity" value="${subscription.subscriptionLifecycles[subLifeCycleCnt-1]}"/>
				${lastActivity.subscriptionLifecycleActivity.name} on <br>
				<fmt:formatDate value="${lastActivity.date}" type="both"/> by
				"${lastActivity.user.name}"
				<s:param name="subscription" value="${subscription}"/>
			</s:link>
		</c:if>
	</c:if>
    <br/> <br/>
    <c:if test="${!(subscription.subscriptionStatus.id == subscriptionStatusCancelled || subscription.subscriptionStatus.id == subscriptionStatusCart || subscription.subscriptionStatus.id == subscriptionStatusAbandoned || subscription.subscriptionStatus.id == subscriptionStatusInProcess || subscription.subscriptionStatus.id== subscriptionStatusExpired)}">
        <a href="#" class="cancelSubscriptionLink">(cancel subscription)</a>
        <div class="cancelSubscriptionDiv" style="display: none;">
            <s:form beanclass="com.hk.web.action.admin.subscription.SubscriptionAdminAction" class="cancelSubscriptionForm" >
                <s:param name="subscription" value="${subscription.id}"/>
                <br/>
                Remark:
                <s:textarea name="cancellationRemark" style="height:70px"/> <br/>

                <s:submit name="cancelSubscription" value="Cancel" class="cancelSubscriptionButton"/>

            </s:form>
        </div>
        <script type="text/javascript">
            $('.cancelSubscriptionForm').ajaxForm({dataType: 'json', success: _cancelSubscription});

            function _cancelSubscription(res) {
                if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                    var status = res.data.subscriptionStatus.name;
                    if (status == "Cancelled") {
                        alert("Subscription cancelled");
                        $('.cancelSubscriptionDiv').hide();
                    } else {
                        alert("Subscription cannot be cancelled");
                    }
                }
            }
        </script>

    </c:if>

    <c:if test="${subscription.subscriptionStatus.id == subscriptionStatusCancelled}">
        <s:form beanclass="com.hk.web.action.admin.subscription.SubscriptionAdminAction" class="rewardPointsForm" >
            <s:param name="subscription" value="${subscription.id}"/>

            <s:submit name="rewardPointsOnSubscriptionCancellation" value="get reward points"/>

        </s:form>
        <script type="text/javascript">
            $('.rewardPointsForm').ajaxForm({dataType: 'json', success: _rewardPoints});

            function _rewardPoints(res) {
                if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                    var rewardPoints = res.data.rewardPoints;
                    alert("reward points to be given: "+rewardPoints);
                }else{
                    alert("error");
                }
            }
        </script>
    </c:if>

</td>

<td >

  <span class="upc lgry sml">ID
  <strong><span class="or"> ${subscription.id}</span></strong>     <br/>

  <span class="upc lgry sml">BO ID</span> <strong><span class="or"> ${subscription.baseOrder.id}</span></strong>

     <c:if test="${subscription.baseOrder.orderStatus.id != orderStatusCart}">
      (<s:link beanclass="com.hk.web.action.core.accounting.BOInvoiceAction" event="pre" target="_blank">
     BO Invoice <s:param name="order" value="${subscription.baseOrder.id}"/>
  </s:link>)
  </c:if> <br/>

  <c:choose>
      <c:when test="${subscription.user.email == subscription.user.login}">
          <%-- Usual case , seems like a registered user --%>
          <span class="upc lgry sml">Email</span>
          <s:link beanclass="com.hk.web.action.admin.user.SearchUserAction" event="search">
              <s:param name="userFilterDto.login" value="${subscription.user.login}"/>
              ${subscription.user.login}
          </s:link>
      </c:when>
      <c:otherwise>
          <%-- Probably a guest user account --%>
          <span class="upc lgry sml">Login</span>
          <s:link beanclass="com.hk.web.action.admin.user.SearchUserAction" event="search">
              <s:param name="userFilterDto.login" value="${subscription.user.login}"/>
              ${subscription.user.login}
      </s:link><br/>
          <span class="upc lgry sml">Email:</span>
          ${subscription.user.email}
      </c:otherwise>
  </c:choose>

        <span class="sml">
        (<s:link beanclass="com.hk.web.action.admin.order.search.SearchOrderAction" event="searchOrders" target="_blank">
            View Orders
            <s:param name="login" value="${subscription.user.login}"/>
        </s:link>)
        </span>
  <br/>
           <table>
               <tr>
                   <td width="300">
                       <s:link beanclass="com.hk.web.action.core.catalog.product.ProductAction" class="prod_link" id="productLink" target="_blank">
                           <s:param name="productId" value="${subscription.productVariant.product.id}"/>
                           <s:param name="productSlug" value="${subscription.productVariant.product.slug}"/>
                           ${subscription.productVariant.product.name}
                       </s:link>
                       <br/>
              <span class="sml gry em">
                <c:forEach items="${subscription.productVariant.productOptions}" var="productOption">
                    ${productOption.name} ${productOption.value}
                </c:forEach>
              </span>
                   </td>
               </tr>
           </table>
      subscription price: <strong><span class="or">Rs. ${subscription.subscriptionPrice}</span></strong><br/>
      start Date:  <strong><span class="or"> <fmt:formatDate value="${subscription.startDate}" /></span></strong>  <br/>
      frequency: <strong><span class="or"> ${subscription.frequencyDays}</span></strong> days <br/>
      subscription Period: <strong><span class="or"> ${subscription.subscriptionPeriodDays}</span></strong> days <br/>
      total qty: <strong><span class="or"> ${subscription.qty}</span></strong> <br/>
</td>
<td class="has_table">

    <table>

        <tr class="addressRow">
            <td width="150">
                    ${subscription.address.name}<br/>
                    ${subscription.address.line1}<br/>
                    ${subscription.address.line2}<br/>
                    ${subscription.address.city} -
                <a href="http://www.dtdc.in/subpages/services/location_search.asp?pin=${subscription.address.pincode.pincode}" target="_blank">
                        ${subscription.address.pincode.pincode}
                </a><br/>
                    ${subscription.address.state}<br/>
                Ph: ${subscription.address.phone}<br/>

            </td>
        </tr>


        <tr> <td>
            <c:if test="${!(subscription.subscriptionStatus.id==subscriptionStatusAbandoned || subscription.subscriptionStatus.id==subscriptionStatusExpired || subscription.subscriptionStatus.id==subscriptionStatusCancelled || subscription.subscriptionStatus.id==subscriptionStatusCart)}">
                <s:link beanclass="com.hk.web.action.admin.subscription.SubscriptionAdminAction" event="changeAddress" target="_blank">
                    <img src="<hk:vhostImage/>/images/admin/icon_edit_add.png" alt="Change Address"
                         title="Change Address"/> Change Address
                    <s:param name="subscription" value="${subscription.id}"/>
                </s:link>
            </c:if></td>
        </tr>

    </table>
</td>
<td class="has_table">
    qty per delivery: <strong><span class="or"> ${subscription.qtyPerDelivery}</span></strong><br/>
    qty delivered: <strong><span class="or"> ${subscription.qtyDelivered}</span></strong> <br/>

    <s:link beanclass="com.hk.web.action.admin.subscription.SubscriptionOrdersAction" event="pre" target="_blank">
        (view subscription orders)
        <s:param name="subscription" value="${subscription}"/>
    </s:link>
    <br/>
    <c:if test="${!(subscription.subscriptionStatus.id == subscriptionStatusCancelled || subscription.subscriptionStatus.id == subscriptionStatusCart || subscription.subscriptionStatus.id == subscriptionStatusAbandoned || subscription.subscriptionStatus.id == subscriptionStatusExpired)}">
        <c:choose>
            <c:when test="${subscription.subscriptionStatus.id == subscriptionStatusInProcess}">
                order in process for <strong><span class="or"><fmt:formatDate value="${subscription.nextShipmentDate}"/> </span></strong>
            </c:when>
            <c:otherwise>
                next due date: <strong><span class="or"><fmt:formatDate value="${subscription.nextShipmentDate}"/> </span></strong>
                <a class="changeNextShipmentDateLink" href="#">
                    (change)
                </a>
            </c:otherwise>
        </c:choose>

        <div class="changeNextShipmentDateDiv" style="display: none;">
            <s:form beanclass="com.hk.web.action.admin.subscription.SubscriptionAdminAction" class="shipmentDateForm">
                <s:param name="subscription" value="${subscription.id}"/>`
                <s:text class="date_input nextShipmentDate" style="width:100px" name="nextShipmentDate"/>
                <s:submit name="changeNextShipmentDate" value="change" class="changeShipmentDateButton" />
            </s:form>
            <script type="text/javascript">

                $('.shipmentDateForm').ajaxForm({dataType: 'json', success: _changeShipmentDate});
                function _changeShipmentDate(res){
                    if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                        var status = res.message;
                        if (status == "success") {
                            alert("next shipment date changed");
                            $('.changeNextShipmentDateDiv').hide();
                        } else {
                            alert("next shipment date  cannot be changed");
                        }
                    }
                }
            </script>
        </div>
    </c:if>
    <br/>

    <s:link beanclass="com.hk.web.action.admin.subscription.SubscriptionLifecycleAction" event="pre" target="_blank">
        (subscription Lifecycle)
        <s:param name="subscription" value="${subscription}"/>
    </s:link>
</td>
</tr>

</c:forEach>
</table>
<s:layout-render name="/layouts/embed/paginationResultCount.jsp" paginatedBean="${subscriptionAdmin}"/>
<s:layout-render name="/layouts/embed/pagination.jsp" paginatedBean="${subscriptionAdmin}"/>

</s:layout-component>

</s:layout-render>
