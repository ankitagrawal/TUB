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

<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
  <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
  <jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>

  <script type="text/javascript">
    $(document).ready(function() {
    	var context = '${pageContext.request.contextPath}';
    	$('#bookingIdButton').click(function(){
    		window.open(context+"/close.jsp", '_self');
    	});
    });
    </script>

	</s:layout-component>

	<s:layout-component name="content">
	
	<div align="center"><label><font size="6px">Booking Status Tables</font></label></div><br><br><br>
	<c:choose>
	<c:when test="${adminBookingBean.shippingOrderId!=null && fn:length(adminBookingBean.skuLiList)>0}">
	Shipping Order Id = ${adminBookingBean.shippingOrderId}
		<table class="t1" id="skuItemLineItemTable" border="1" >
				<tr>
					<th>SkuItemLineItem ID</th>
					<th>Product Variant Id</th>
					<th>SKU Item ID</th>
					<th>SKU Item Status</th>
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
						<td>${skuItemLineItem.skuItem.skuItemStatus.name }</td>
						<td>${skuItemLineItem.lineItem.id}</td>
						<td>${skuItemLineItem.unitNum}</td>
						<td>${skuItemLineItem.skuItemCLI.id}</td>
						<td>${skuItemLineItem.createDate}</td>
						<td>${skuItemLineItem.updateDate}</td>
					</tr>
				</c:forEach>
			</table>
	</c:when>
	<c:otherwise>
	<br>
	<div align="center"><label>Nothing Booked For This Shipping Order</label></div><br><br><br>
	</c:otherwise>
	</c:choose>
	
		<c:if test="${adminBookingBean.baseOrderId!=null}">
		<c:if test="${fn:length(adminBookingBean.skuCLIList)>0 }">
		<div align="center"><label><u><strong>SkuItemCLI entries for Base Order : ${adminBookingBean.baseOrderId }</strong></u></label></div>
			<table class="t1" id="skuCartItemLineItemTable" border="1">
				<tr>
					<th>SkuItemCLI ID</th>
					<th>Product Variant Id</th>
					<th>SKU Item ID</th>
					<th>SKU Item Status</th>
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
						<td>${skuCartItemLineItem.skuItem.skuItemStatus.name }</td>
						<td>${skuCartItemLineItem.cartLineItem.id}</td>
						<td>${skuCartItemLineItem.unitNum}</td>
						<td>${skuCartItemLineItem.createDate}</td>
						<td>${skuCartItemLineItem.updateDate}</td>
					</tr>
				</c:forEach>
			</table>
			</c:if>
			
			<br>
			<br>
			
			<c:if test="${adminBookingBean.soSiLiMap!=null && fn:length(adminBookingBean.soSiLiMap)>0}">
			<div align="center"><label><u><strong>SkuItemLineItem Entries For Shipping Orders:</strong></u></label></div><br><br>
			<c:forEach var="soSiLiMap" items="${adminBookingBean.soSiLiMap}" varStatus="ctr">
			<c:if test="${ fn:length(soSiLiMap.value)>0}">
			Shipping Orders: ${soSiLiMap.key.id}
			<table class="t1" id="skuItemLineItemTable" border="1">
				<tr>
					<th>SkuItemLineItem ID</th>
					<th>Product Variant Id</th>
					<th>SKU Item ID</th>
					<th>SKU Item Status</th>
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
						<td>${skuItemLineItem.skuItem.skuItemStatus.name }</td>
						<td>${skuItemLineItem.lineItem.id}</td>
						<td>${skuItemLineItem.unitNum}</td>
						<td>${skuItemLineItem.skuItemCLI.id}</td>
						<td>${skuItemLineItem.createDate}</td>
						<td>${skuItemLineItem.updateDate}</td>
					</tr>
				</c:forEach>
			</table>
			</c:if>
			</c:forEach>
		</c:if>
		</c:if>
		
		<s:form beanclass="com.hk.web.action.admin.booking.AdminBookingAction" id="closeForm">
		<s:submit id="bookingIdButton" class="button_green addToCartButton" name="closeWindow" value="Close"/>
		</s:form>
		
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
  #bookingIdButton{
  float: left;
	position: relative;
	left: 43%;
	margin-bottom: 2px;
	margin-top: 2px;
  }
</style>