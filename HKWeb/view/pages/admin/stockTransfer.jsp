<%@ page import="com.hk.pact.dao.warehouse.WarehouseDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.StockTransferAction" var="sta"/>
<s:useActionBean beanclass="com.hk.web.action.admin.warehouse.SelectWHAction" var="whAction" event="getUserWarehouse"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Create/Edit Stock Transfer">
<jsp:useBean id="now" class="java.util.Date" scope="request"/>
<s:layout-component name="htmlHead">
 <c:set var="fromwarehouse" value="${whAction.setWarehouse}"/>
	<%
		WarehouseDao warehouseDao = ServiceLocatorFactory.getService(WarehouseDao.class);
		pageContext.setAttribute("whList", warehouseDao.getAllWarehouses());
	%>


	<link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
	<jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
	<script type="text/javascript">
		$(document).ready(function() {

			var validateForm = function() {
				var isValidated = true;
                if($('.toWarehouse').val() == " "){
                 alert("Select  To Warehouse");
                     isValidated = false;
                }
				return isValidated;
			};

            $("#createST").click(function() {
                var isValidated = validateForm();
                if (!isValidated) {
                    return isValidated;
                }
                if (isValidated) {
                    var fromWarehouse = $('.toWarehouse').val();
                    if (fromWarehouse == ${fromwarehouse}) {
                        return confirm("To Warehouse value  and  From Warehouse are  same. Click Ok To Proceed");
                    }
                    else {
                        return isValidated;
                    }
                }
            });

			$('#productVariantBarcode').change(function() {
				var formName = $('#stForm2');
				var formURL = formName.attr('action');
				formName.attr('action', formURL+"?stockTransfer=" + ${sta.stockTransfer.id} + "&save=");
				formName.submit();
			});

		});
	</script>

</s:layout-component>
<s:layout-component name="content">
	<h2>Create/Edit Stock Transfer</h2>
	<s:form beanclass="com.hk.web.action.admin.inventory.StockTransferAction" id="stForm">
		<s:hidden name="stockTransfer" value="${sta.stockTransfer.id}"/>
		<table>
			<tr>
				<td>Create Date</td>
				<td>
					<s:text class="date_input" formatPattern="yyyy-MM-dd" name="createDate"
					        value="${sta.stockTransfer.createDate != null ? sta.stockTransfer.createDate : now}"/>
				</td>
				<td>Checkout Date</td>
				<td>
					<s:text class="date_input" formatPattern="yyyy-MM-dd" name="checkOutDate"
					        value="${sta.stockTransfer.checkoutDate != null ? sta.stockTransfer.checkoutDate : now}"/>
				</td>
			</tr>
			<tr>
				<td>Remarks<br/><span class="sml gry">(eg. XXX)</span></td>
				<td><s:textarea name="stockTransfer.remarks" style="height:50px;"
				                value="${sta.stockTransfer.remarks}"/></td>
			</tr>
			<tr>
				<td>From Warehouse :</td>
				<td>
					<s:hidden name="fromWarehouse" value="${whAction.setWarehouse}"/>
						${whAction.setWarehouse.city}
				</td>
				<td>
					To Warehouse :
				</td>
				<td><s:select name="toWarehouse" value="${sta.stockTransfer.toWarehouse}" class="toWarehouse">
					<s:option> </s:option>
					<c:forEach items="${whList}" var="wh">
						<s:option value="${wh}">${wh.city}</s:option>
					</c:forEach>
				</s:select></td>
			</tr>
			<tr>
				<td><s:submit name="createOrUpdateStockTransfer" value="Create/Update Stock Transfer" id="createST"/> </td>
			</tr>
		</table>
	</s:form>

	<c:if test="${sta.stockTransfer.id != null}">
		<s:form beanclass="com.hk.web.action.admin.inventory.StockTransferAction" id="stForm2">
		<fieldset class="right_label">
			<legend>Scan Barcode:</legend>
			<ul>
				<li>
					<s:label name="barcode">Product Variant Barcode</s:label>
					<s:text name="productVariantBarcode" id="productVariantBarcode"/>
				</li>
				<li></li>
			</ul>
		</fieldset>
		</s:form>

		<table border="1">
			<thead>
			<tr>
				<th>VariantID</th>
				<th>Details</th>
				<th>Checkedout Qty</th>
				<th>Cost Price<br/>(Without TAX)</th>
				<th>MRP</th>
				<th>Batch Number</th>
				<th>Mfg. Date<br/>(yyyy-MM-dd)</th>
				<th>Exp. Date<br/>(yyyy-MM-dd)</th>
			</tr>
			</thead>
			<tbody id="stTable">
			<c:forEach var="stockTransferLineItem" items="${sta.stockTransfer.stockTransferLineItems}" varStatus="ctr">
				<c:set var="productVariant" value="${stockTransferLineItem.sku.productVariant}"/>
				<c:set var="checkedOutSkuGroup" value="${stockTransferLineItem.checkedOutSkuGroup}"/>
				<tr count="${ctr.index}" class="${ctr.last ? 'lastRow lineItemRow':'lineItemRow'}">
					<td>
							${productVariant.id}
					</td>
					<td>${productVariant.product.name}<br/>${productVariant.productOptionsWithoutColor}
					</td>
					<td> ${stockTransferLineItem.checkedoutQty}
					</td>
					<td>${checkedOutSkuGroup.costPrice}
					</td>
					<td> ${checkedOutSkuGroup.mrp}
					</td>
					<td>${checkedOutSkuGroup.batchNumber}</td>
					<td>
						<fmt:formatDate value="${checkedOutSkuGroup.mfgDate}" type="both"/></td>
					<td>
						<fmt:formatDate value="${checkedOutSkuGroup.expiryDate}" type="both"/></td>
				</tr>

			</c:forEach>
			</tbody>
		</table>
	</c:if>

</s:layout-component>

</s:layout-render>