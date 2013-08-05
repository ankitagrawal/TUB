<%@ page import="com.akube.framework.util.FormatUtils"%>
<%@ page import="com.hk.constants.order.EnumCartLineItemType"%>
<%@ page import="com.hk.constants.payment.EnumPaymentMode"%>
<%@ page import="com.hk.constants.payment.EnumPaymentStatus"%>
<%@ page import="com.hk.constants.order.EnumOrderStatus"%>
<%@ page import="com.hk.constants.shippingOrder.EnumShippingOrderStatus"%>
<%@ page import="com.hk.pact.dao.MasterDataDao"%>
<%@ page import="com.hk.web.HealthkartResponse"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/includes/_taglibInclude.jsp"%>

<s:useActionBean beanclass="com.hk.web.action.admin.booking.AdminBookingAction" var="adminBookingBean" />

<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Order Booking Status">

	<s:layout-component name="htmlHead">

	</s:layout-component>

	<s:layout-component name="content">
		<c:if test="${adminBookingBean.shippingOrderId!=null }">
		Shipping Order Id = ${adminBookingBean.shippingOrderId }
		<table class="t1" id="skuItemLineItemTable" border="1">
				<tr>
					<th>SkuItemLineItem ID</th>
					<th>Product Variant Id</th>
					<th>SKU Item ID</th>
					<th>Line Item ID</th>
					<th>Unit Number Booked</th>
					<th>SkuItemCartLineItem Id</th>
					<th>Create Date</th>
					<th>Update Date</th>
				</tr>
				<c:forEach var="skuItemLineItem" items="${adminBookingBean.skuLiList}" varStatus="ctr">
					<tr>
						<td>${skuItemLineItem.id }</td>
						<td>${skuItemLineItem.productVariant.id }</td>
						<td>${skuItemLineItem.skuItem.id }</td>
						<td>${skuItemLineItem.lineItem.id}</td>
						<td>${skuItemLineItem.unitNum}</td>
						<td>${skuItemLineItem.skuItemCLI.id}</td>
						<td>${skuItemLineItem.createDate}</td>
						<td>${skuItemLineItem.updateDate}</td>
					</tr>
				</c:forEach>
			</table>
		</c:if>

		<c:if test="${adminBookingBean.baseOrderId!=null }">
		SkuItemCLI entries for Base Order : ${adminBookingBean.baseOrderId }
			<table class="t1" id="skuCartItemLineItemTable" border="1">
				<tr>
					<th>SkuItemCLI ID</th>
					<th>Product Variant Id</th>
					<th>SKU Item ID</th>
					<th>Cart Line Item ID</th>
					<th>Unit Number Booked</th>
					<th>Create Date</th>
					<th>Update Date</th>
				</tr>
				<c:forEach var="skuCartItemLineItem" items="${adminBookingBean.skuCLIList}" varStatus="ctr">
					<tr>
						<td>${skuCartItemLineItem.id }</td>
						<td>${skuCartItemLineItem.productVariant.id }</td>
						<td>${skuCartItemLineItem.skuItem.id }</td>
						<td>${skuCartItemLineItem.cartLineItem.id}</td>
						<td>${skuCartItemLineItem.unitNum}</td>
						<td>${skuCartItemLineItem.createDate}</td>
						<td>${skuCartItemLineItem.updateDate}</td>
					</tr>
				</c:forEach>
			</table>
			
			<br>
			<br>
			
			<strong>SkuItemLineItem Entries For Shipping Orders:</strong><br><br>
			<c:forEach var="soSiLiMap" items="${adminBookingBean.soSiLiMap}" varStatus="ctr">
			Shipping Orders: ${soSiLiMap.key.id}
			<table class="t1" id="skuItemLineItemTable" border="1">
				<tr>
					<th>SkuItemLineItem ID</th>
					<th>Product Variant Id</th>
					<th>SKU Item ID</th>
					<th>Line Item ID</th>
					<th>Unit Number Booked</th>
					<th>SkuItemCartLineItem Id</th>
					<th>Create Date</th>
					<th>Update Date</th>
				</tr>
				<c:forEach var="skuItemLineItem" items="${soSiLiMap.value}" varStatus="ctr">
					<tr>
						<td>${skuItemLineItem.id }</td>
						<td>${skuItemLineItem.productVariant.id }</td>
						<td>${skuItemLineItem.skuItem.id }</td>
						<td>${skuItemLineItem.lineItem.id}</td>
						<td>${skuItemLineItem.unitNum}</td>
						<td>${skuItemLineItem.skuItemCLI.id}</td>
						<td>${skuItemLineItem.createDate}</td>
						<td>${skuItemLineItem.updateDate}</td>
					</tr>
				</c:forEach>
			</table>
			</c:forEach>
		</c:if>
	</s:layout-component>
</s:layout-render>
<style>
 .t1 {
    border-width: 0 0 1px 1px;
    border-style: solid;
    border-color: rgba(0, 0, 0, 0.1);
  }

  .t1 tr td{
    text-align: left;
    font-size: small;
    border-width: 1px 1px 0 0;
    border-style: solid;
    border-color: rgba(0, 0, 0, 0.1);
  }
</style>