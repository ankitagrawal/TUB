<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.domain.core.Tax" %>
<%@ page import="com.hk.pact.dao.TaxDao" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<%@page import="com.hk.domain.warehouse.Warehouse"%>
<%@ page import="com.hk.pact.service.core.WarehouseService" %>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Add SKUs">
	<s:useActionBean beanclass="com.hk.web.action.admin.sku.SkuAction" var="skuAction"/>
	<s:layout-component name="htmlHead">
		<%
		    TaxDao taxDao = ServiceLocatorFactory.getService(TaxDao.class);
					List<Tax> taxList = taxDao.getLocalTaxList();
					pageContext.setAttribute("taxList", taxList);

					WarehouseService warehouseService = ServiceLocatorFactory.getService(WarehouseService.class);
					pageContext.setAttribute("whList", warehouseService.getAllActiveWarehouses());
		%>
		<link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
		<script type="text/javascript">
			$(document).ready(function() {
				$('.requiredFieldValidator').click(function() {
					var productVariant = $('.productVariant').val();
					var warehouse = $('.warehouse').val();
					var cutOffInventory = $('.cutOffInventory').val();
					var forecastedAmount = $('.forecastedAmount').val();
					var tax = $('.tax').val();
					if (productVariant == "" || warehouse == "" || cutOffInventory == "" || forecastedAmount == "" || costPrice == "" || tax == "") {
						alert("All fields are compulsory.");
						return false;
					}
					if (isNaN(cutOffInventory) || isNaN(forecastedAmount) || isNaN(costPrice)) {
						alert("Cut Off Inventory, Forecasted Amount, Amount Spend and cost price should be numbers.");
					}
				});
			});
		</script>
		<jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
	</s:layout-component>
	<s:layout-component name="heading">
		Add SKU
	</s:layout-component>
	<s:layout-component name="content">
		<div class="reportBox">
			<table>
				<s:form beanclass="com.hk.web.action.admin.sku.SkuAction">
					<s:hidden name="sku.id" value="${skuAction.sku.id}"/>
					<tr>
						<td>Product Variant:<span class='aster' title="this field is required">*</span></td>
						<td><s:text name="sku.productVariant" value="${skuAction.sku.productVariant.id}"
						            class="productVariant"/></td>
					</tr>
					<tr>
						<td>Warehouse:<span class='aster' title="this field is required">*</span></td>
						<td><s:select name="sku.warehouse" class="warehouse">
							<s:option value="">-Select-</s:option>
							<c:forEach items="${whList}" var="wh">
								<s:option value="${wh.id}">${wh.identifier}</s:option>
							</c:forEach>
						</s:select></td>
					</tr>
					<tr>
						<td>Cut Off Inventory:<span class='aster' title="this field is required">*</span></td>
						<td><s:text name="sku.cutOffInventory" value="${skuAction.sku.cutOffInventory}"
						            class="cutOffInventory"/></td>
					</tr>
					<tr>
						<td>Forecasted Quantity:<span class='aster' title="this field is required">*</span></td>
						<td><s:text name="sku.forecastedQuantity" value="${skuAction.sku.forecastedQuantity}"
						            class="forecastedAmount"/></td>
					</tr>
					<tr>
						<td>Tax<span class='aster' title="this field is required">*</span></td>
						<td><s:select name="sku.tax.id" class="tax">
							<s:option value="">-Select-</s:option>
							<c:forEach items="${taxList}" var="tax">
								<s:option value="${tax.id}">${tax.name}</s:option>
							</c:forEach>
						</s:select></td>
					</tr>
					<tr>
						<td><s:submit class="requiredFieldValidator" name="saveNewSku" value="Save"/></td>
					</tr>
				</s:form>
			</table>
		</div>
	</s:layout-component>
</s:layout-render>