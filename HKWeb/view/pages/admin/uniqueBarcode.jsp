<%@ page import="com.akube.framework.util.FormatUtils"%>
<%@ page import="com.hk.constants.order.EnumCartLineItemType"%>
<%@ page import="com.hk.constants.payment.EnumPaymentMode"%>
<%@ page import="com.hk.constants.payment.EnumPaymentStatus"%>
<%@ page import="com.hk.constants.order.EnumOrderStatus"%>
<%@ page import="com.hk.constants.shippingOrder.EnumShippingOrderStatus"%>
<%@ page import="com.hk.pact.dao.MasterDataDao"%>
<%@ page import="com.hk.web.HealthkartResponse"%>
<%@ page import="com.hk.constants.core.RoleConstants"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@include file="/includes/_taglibInclude.jsp"%>

<s:useActionBean beanclass="com.hk.web.action.admin.inventory.UniqueBarcodeAction" var="uba" />
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Unique Barcode">
	<s:layout-component name="htmlHead">

		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
		<jsp:include page="/includes/_js_labelifyDynDateMashup.jsp" />

	</s:layout-component>
	<s:layout-component name="content">
	
	<div align="center"><label><font size="6px">Create Unique Barcodes</font></label></div><br><br><br>

		<div id="heading" style="margin: 0px auto; width: 1200px; margin-bottom: 30px;">
			<s:form beanclass="com.hk.web.action.admin.inventory.UniqueBarcodeAction" id="closeForm">
				<s:hidden name="productVariantId" value="${uba.productVariantId }" />
				<s:hidden name="userWarehouse" value="${uba.userWarehouse.id }" />
				<c:choose>
					<c:when test="${fn:length(uba.skuGroups)==0}">
						<strong>Enter Variant Here: </strong>
						<s:text name="productVariantId" />
						<s:submit id="submitVariantButton" class="button_green addToCartButton" name="searchBatchesForPV"
							value="Search Batches" />
					</c:when>
					<c:otherwise>
						<strong><u>For Product Variant: ${uba.productVariantId }</u></strong>
						</c:otherwise>
				</c:choose>

				<c:if test="${fn:length(uba.skuGroupQty)>0 }">
				<div align="center"><label><font size="3px">Non Uniquely Barcoded Groups</font></label></div><br>
					<table class="t1" id="skuGroupTable" border="1">
						<tr>
							<th>Sku Group ID</th>
							<th>Quantity</th>
							<th>Sku Id</th>
							<th>Barcode</th>
							<th>Batch Number</th>
							<th>Cost Price</th>
							<th>Mrp</th>
							<th>Create Date</th>
							<th>Mfg Date</th>
							<th>Expiry Date</th>
						</tr>

						<c:forEach var="skuGroupFromMap" items="${uba.skuGroupQty}" varStatus="item">
							<c:set var="skuGroup" value="${skuGroupFromMap.key}" />
							<tr>
								<td><input type="hidden" name="skuGroupIds[${item.count-1}]" value="${skuGroup.id}">${skuGroup.id }</td>
								<td>${skuGroupFromMap.value}</td>
								<td>${skuGroup.sku.id }</td>
								<td>${skuGroup.barcode}</td>
								<td>${skuGroup.batchNumber }</td>
								<td>${skuGroup.costPrice}</td>
								<td>${skuGroup.mrp}</td>
								<td>${skuGroup.createDate}</td>
								<td>${skuGroup.mfgDate}</td>
								<td>${skuGroup.expiryDate}</td>
								<td> <s:link beanclass ="com.hk.web.action.admin.inventory.UniqueBarcodeAction" event="downloadBarcode"> Download Barcode
                				<s:param name="skuGroupId" value="${skuGroup.id}"/>
                				<s:param name="productVariantId" value="${uba.productVariantId }" />
								<s:param name="userWarehouse" value="${uba.userWarehouse.id }" />
            				</s:link></td>
							</tr>
						</c:forEach>
					</table>
				</c:if>
				
				<c:if test="${fn:length(uba.skuGroupQty)>0 }">
				<div align="center">
				<s:submit id="submitVariantButton" class="button_green addToCartButton" name="makeUniqueBarcode"
					value="Make Unique Barcode" /></div></c:if>
				
				
				<c:if test="${fn:length(uba.uniquelyBarcodedskuGroupQty)>0 }">
				<div align="center"><label><font size="3px">Uniquely Barcoded Groups</font></label></div><br>
					<table class="t1" id="skuGroupTable" border="1">
						<tr>
							<th>Sku Group ID</th>
							<th>Quantity</th>
							<th>Sku Id</th>
							<th>Barcode</th>
							<th>Batch Number</th>
							<th>Cost Price</th>
							<th>Mrp</th>
							<th>Create Date</th>
							<th>Mfg Date</th>
							<th>Expiry Date</th>
						</tr>

						<c:forEach var="skuGroupFromMap" items="${uba.uniquelyBarcodedskuGroupQty}" varStatus="item">
							<c:set var="skuGroup" value="${skuGroupFromMap.key}" />
							<tr>
								<td><input type="hidden" name="skuGroupIds[${item.count-1}]" value="${skuGroup.id}">${skuGroup.id }</td>
								<td>${skuGroupFromMap.value}</td>
								<td>${skuGroup.sku.id }</td>
								<td>${skuGroup.barcode}</td>
								<td>${skuGroup.batchNumber }</td>
								<td>${skuGroup.costPrice}</td>
								<td>${skuGroup.mrp}</td>
								<td>${skuGroup.createDate}</td>
								<td>${skuGroup.mfgDate}</td>
								<td>${skuGroup.expiryDate}</td>
								<td> <s:link beanclass ="com.hk.web.action.admin.inventory.UniqueBarcodeAction" event="downloadBarcode"> Download Barcode
                				<s:param name="skuGroupId" value="${skuGroup.id}"/>
                				<s:param name="productVariantId" value="${uba.productVariantId }" />
								<s:param name="userWarehouse" value="${uba.userWarehouse.id }" />
            				</s:link></td>
							</tr>
						</c:forEach>
					</table>
				</c:if>
				
				
				<c:if test="${uba.skuGroups != null && !(fn:length(uba.skuGroupQty)>0) && !(fn:length(uba.uniquelyBarcodedskuGroupQty)>0)}">
				<div align="center"><strong>Nothing to display</strong></div><br>
				</c:if>
				
				<c:if test="${fn:length(uba.skuGroups)>0}">
				<div align="center">
				<s:submit id="backButton" class="button_green addToCartButton" name="back"
					value="Go Back" /></div></c:if> 
			</s:form>
		</div>

	</s:layout-component>

</s:layout-render>

<style>
.t1 {
	border-width: 0 0 1px 1px;
	border-style: solid;
	border-color: rgba(0, 0, 0, 0.1);
}

.t1 tr td {
	text-align: left;
	font-size: small;
	border-width: 1px 1px 0 0;
	border-style: solid;
	border-color: rgba(0, 0, 0, 0.1);
}

#bookingIdButton {
	float: left;
	position: relative;
	left: 43%;
	margin-bottom: 2px;
	margin-top: 2px;
}
</style>