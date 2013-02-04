<%@ page import="com.hk.pact.dao.warehouse.WarehouseDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.constants.sku.EnumSkuItemTransferMode" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<c:set var="StockTransferOut" value="<%=EnumSkuItemTransferMode.STOCK_TRANSFER_OUT.getId()%>"/>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.StockTransferAction" var="sta"/>
<s:useActionBean beanclass="com.hk.web.action.admin.warehouse.SelectWHAction" var="whAction" event="getUserWarehouse"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Create/Edit Stock Transfer">
<jsp:useBean id="now" class="java.util.Date" scope="request"/>
<s:layout-component name="htmlHead">
 <c:set var="fromwarehouse" value="${whAction.setWarehouse}"/>
	<%
		WarehouseDao warehouseDao = ServiceLocatorFactory.getService(WarehouseDao.class);
		pageContext.setAttribute("whList", warehouseDao.getAllWarehouses());
		String messageColor = request.getParameter("messageColor");
		pageContext.setAttribute("messageColor", messageColor);
	%>


	<link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
	<script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
	<jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
	<script type="text/javascript">
		$(document).ready(function() {
			$('.alert').hide();

			$('#productVariantBarcode').focus();
			$('#productVariantBarcode').keydown(function(){
				$('.alertST').hide();
			});

			if($('#messageColorParam').val() == "green") {
				$('.alertST').find('li').css('font-size', '30px').css('color', 'green');
			} else {
				$('.alertST').find('li').css('font-size', '30px').css('color', 'red');
			}


            $("#createST").click(function() {
	            if ($('.toWarehouse').val() == " ") {
		            alert("Select  To Warehouse");
		            return false;
	            }
	            var fromWarehouse = $('.toWarehouse').val();
	            if (fromWarehouse == ${fromwarehouse}) {
		            return confirm("To Warehouse value  and  From Warehouse are  same. Click Ok To Proceed");
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
	<input type="hidden" id="messageColorParam" value="${messageColor}">
	<c:if test="${sta.stockTransfer.id != null}">
		<h2>Edit Stock Transfer # ${sta.stockTransfer.id}</h2>
	</c:if>
	<c:if test="${sta.stockTransfer.id == null}">
		<h2>Create New Stock Transfer</h2>
	</c:if>
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
						${whAction.setWarehouse.name}
				</td>
				<td>
					To Warehouse :
				</td>
				<td><c:if test="${sta.stockTransfer.id == null}">
					<s:select name="toWarehouse" value="${sta.stockTransfer.toWarehouse}" class="toWarehouse">
						<s:option> </s:option>
						<c:forEach items="${whList}" var="wh">
							<s:option value="${wh}">${wh.name}</s:option>
						</c:forEach>
					</s:select>
				</c:if>
					<c:if test="${sta.stockTransfer.id != null}">
						<s:hidden name="toWarehouse" value="${sta.stockTransfer.toWarehouse}"/>
						${sta.stockTransfer.toWarehouse.name}
					</c:if>
				</td>
			</tr>
			<tr>
				<td><s:submit name="createOrUpdateStockTransfer" value="Create/Update Stock Transfer" id="createST"/> </td>
			</tr>
		</table>
	</s:form>
	<div class="alertST messages"><s:messages key="generalMessages"/></div>

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
				<th>Barcode</th>
				<th>VariantID</th>
				<th>Details</th>
				<th>Checkedout Qty</th>
				<th>Cost Price<br/>(Without TAX)</th>
				<th>MRP</th>
				<th>Batch Number</th>
				<th>Mfg. Date<br/>(yyyy-MM-dd)</th>
				<th>Exp. Date<br/>(yyyy-MM-dd)</th>
                <th>Reduce Qty By 1</th>
                <th> Item Details</th>


			</tr>
			</thead>
			<tbody id="stTable">
			<c:forEach var="stockTransferLineItem" items="${sta.stockTransfer.stockTransferLineItems}" varStatus="ctr">
				<c:set var="productVariant" value="${stockTransferLineItem.sku.productVariant}"/>
				<c:set var="checkedOutSkuGroup" value="${stockTransferLineItem.checkedOutSkuGroup}"/>
				<tr count="${ctr.index}" class="${ctr.last ? 'lastRow lineItemRow':'lineItemRow'}">
					<td>${stockTransferLineItem.checkedOutSkuGroup.barcode}</td>
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
                <td>    
					<c:if test="${stockTransferLineItem.checkedoutQty > 0}">
				<s:link beanclass="com.hk.web.action.admin.inventory.StockTransferAction" event="revertStockTransferOut">
						Reduce Qty By 1
					<s:param name="stliToBeReduced" value="${stockTransferLineItem}"/>
					<s:param name="stockTransfer" value="${sta.stockTransfer}" /></s:link>
					</c:if>    </td>
                     <td><s:link beanclass="com.hk.web.action.admin.sku.ViewSkuItemAction" event="pre">
						View Item Details
					 <s:param name="stockTransferLineItem" value="${stockTransferLineItem}"/>
                     <s:param name="entityId" value="${StockTransferOut}"/>
					</s:link> </td>
				</tr>

			</c:forEach>
			</tbody>
		</table>
	</c:if>

</s:layout-component>

</s:layout-render>