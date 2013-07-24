<%@ page import="com.akube.framework.util.FormatUtils"%>
<%@ page import="com.hk.constants.order.EnumOrderStatus"%>
<%@ page import="com.hk.constants.payment.EnumPaymentMode"%>
<%@ page import="com.hk.constants.payment.EnumPaymentStatus"%>
<%@ page import="com.hk.constants.shippingOrder.EnumShippingOrderStatus"%>
<%@ page import="com.hk.pact.dao.catalog.category.CategoryDao"%>
<%@ page import="com.hk.pact.service.OrderStatusService"%>
<%@ page import="com.hk.pact.service.payment.PaymentService"%>
<%@ page import="com.hk.pact.service.shippingOrder.ShippingOrderStatusService"%>
<%@ page import="com.hk.pact.service.shippingOrder.ShippingOrderLifecycleService"%>
<%@ page import="com.hk.service.ServiceLocatorFactory"%>
<%@ page import="com.hk.web.HealthkartResponse"%>
<%@ page import="com.hk.pact.service.store.StoreService"%>
<%@ page import="com.hk.constants.shippingOrder.EnumShippingOrderLifecycleActivity"%>
<%@ page import="com.hk.pact.dao.MasterDataDao"%>
<%@ page import="com.hk.constants.core.EnumUserCodCalling"%>
<%@ page import="com.hk.constants.core.RoleConstants"%>
<%@ page import="java.util.List"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/includes/_taglibInclude.jsp"%>

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Jit Shipping Order Queues">
	<s:layout-component name="htmlHead">
		<link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css" />
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
		<script src="http://jquery-ui.googlecode.com/svn/tags/latest/ui/jquery.effects.core.js"></script>
		<script src="http://jquery-ui.googlecode.com/svn/tags/latest/ui/jquery.effects.slide.js"></script>
		<jsp:include page="/includes/_js_labelifyDynDateMashup.jsp" />
		<style type="text/css">
</style>
		<script type="text/javascript"></script>


	</s:layout-component>


	<s:layout-component name="content">
		<div class="grouped grid_12">
			<s:form beanclass="com.hk.web.action.admin.queue.JitShippingOrderAction" method="get" autocomplete="false">
				<div id="filterDiv">
					<label>Start date</label>
					<s:text class="date_input startDate" style="width:150px" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
						name="startDate" />
					&nbsp; <label>End date</label>
					<s:text class="date_input endDate" style="width:150px" formatPattern="<%=FormatUtils.defaultDateFormatPattern%>"
						name="endDate" />
				</div>
				<div class="buttons">
					<s:submit name="pre" value="Search" />
				</div>
			</s:form>
		</div>
		
		<div id="shippingOrderTable">
		<table>
		<tr>
		<th>Supplier</th>
		<th>Shipping Order Id</th>
		<th>Line Item Id</th>
		<th>Product Variant</th>
		<th>Quantity</th>
		<th></th>
		<th></th>
		</tr>
		<tr></tr>
		</table>
		</div>
		
	</s:layout-component>
</s:layout-render>

