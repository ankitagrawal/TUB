<%@ page import="com.akube.framework.util.FormatUtils" %>
<%@ page import="com.hk.constants.order.EnumCartLineItemType" %>
<%@ page import="com.hk.constants.payment.EnumPaymentMode" %>
<%@ page import="com.hk.constants.payment.EnumPaymentStatus" %>
<%@ page import="com.hk.constants.order.EnumOrderStatus" %>
<%@ page import="com.hk.constants.shippingOrder.EnumShippingOrderStatus" %>
<%@ page import="com.hk.pact.dao.MasterDataDao" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<s:useActionBean beanclass="com.hk.web.action.admin.order.search.SearchShippingOrderAction" var="searchShippingOrderBean"/>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Shipping Order Search">

<s:layout-component name="htmlHead">
  <link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
  <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
    <script type="text/javascript">
        $('.orderStatusLink').click(function() {
            var proceed = confirm('Are you sure?');
            if (!proceed) return false;

            var clickedLink = $(this);
            var clickedP = clickedLink.parents('p');
            clickedP.find('.orderStatusName').html($('#ajaxLoader').html());

            $.getJSON($(this).attr('href'), function(res) {
                if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
                    window.location.reload();
                }  else {
                    alert("SO cannot be put on hold");
                    location.reload();
                }
            });
            return false;
        });

    </script>
</s:layout-component>


<s:layout-component name="modal">
  <div class="jqmWindow" id="orderDumpLogWindow"></div>
</s:layout-component>

<s:layout-component name="content">
    <c:set var="actionQueue" value="false" />
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
<c:set var="paymentStatusPending" value="<%=EnumPaymentStatus.AUTHORIZATION_PENDING.getId()%>"/>

<s:errors/>
<s:form beanclass="com.hk.web.action.admin.order.search.SearchShippingOrderAction" method="get" var="searchShippingOrderBean" autocomplete="false">
  <fieldset class="top_label">
    <ul>
      <div class="grouped">
        <li><label>SO Gateway Order ID</label> <s:text name="shippingOrderGatewayId"/></li>
        <li><label>SO Order ID</label> <s:text name="shippingOrderId"/></li>
        <li><label>Tracking ID</label> <s:text name="trackingId"/></li>
      </div>
    </ul>
    <div class="buttons"><s:submit name="searchShippingOrder" value="Search Orders"/></div>
  </fieldset>
</s:form>

<c:set var="paymentModeCod" value="<%=EnumPaymentMode.COD.getId()%>"/>


     <s:layout-render name="/pages/admin/queue/shippingOrderDetailGrid.jsp"
                               shippingOrders="${searchShippingOrderBean.shippingOrderList}" hasAction="false" showCourier="true" isSearchShippingOrder = "true"/>
</s:layout-component>

</s:layout-render>
