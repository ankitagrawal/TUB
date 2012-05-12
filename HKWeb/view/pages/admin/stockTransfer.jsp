<%@ page import="com.hk.pact.dao.warehouse.WarehouseDao" %>
<%@ page import="com.hk.service.ServiceLocatorFactory" %>
<%@ page import="com.hk.web.HealthkartResponse" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.StockTransferAction" var="sta"/>
<s:useActionBean beanclass="com.hk.web.action.admin.warehouse.SelectWHAction" var="whAction" event="getUserWarehouse"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Create/Edit Stock Transfer">
	<jsp:useBean id="now" class="java.util.Date" scope="request"/>
	<s:layout-component name="htmlHead">

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
				$('.addRowButton').click(function() {

					var lastIndex = $('.lastRow').attr('count');
					if (!lastIndex) {
						lastIndex = -1;
					}
					$('.lastRow').removeClass('lastRow');

					var nextIndex = eval(lastIndex + "+1");


					var newRowHtml =
							'<tr count="' + nextIndex + '" class="lastRow lineItemRow">' +
							'  <td>' +
							'    <input type="hidden" name="stockTransferLineItems[' + nextIndex + '].id" />' +
							'    <input type="text" class="variant" name="stockTransferLineItems[' + nextIndex + '].productVariant"/>' +
							'  </td>' +
							'  <td class="pvDetails"></td>' +
							'  <td>' +
							'    <input type="text" name="stockTransferLineItems[' + nextIndex + '].checkedoutQty" class="checkedoutQty" />' +
							'  </td>' +
							'  <td>' +
							'    <input class="costPrice" type="text" name="stockTransferLineItems[' + nextIndex + '].costPrice" />' +
							'  </td>' +
							'  <td>' +
							'    <input class="mrp" type="text" name="stockTransferLineItems[' + nextIndex + '].mrp" />' +
							'  </td>' +
							'  <td>' +
							'    <input type="text" name="stockTransferLineItems[' + nextIndex + '].batchNumber" />' +
							'  </td>' +
							'  <td>' +
							'    <input class="date_input" formatPattern="yyyy-MM-dd" type="text" name="stockTransferLineItems[' + nextIndex + '].mfgDate" />' +
							'  </td>' +
							'  <td>' +
							'    <input class="date_input" formatPattern="yyyy-MM-dd" type="text" name="stockTransferLineItems[' + nextIndex + '].expiryDate" />' +
							'  </td>' +
							'</tr>';

					$('#stTable').append(newRowHtml);
					$('.requiredFieldValidator').click(function() {

						var checkedoutQty = $('.checkedoutQty').val();
						if (checkedoutQty == "" || checkedoutQty < 0 || isNaN(checkedoutQty)) {
							alert("Enter checkedout Qty in correct format.");
							return false;
						}
						var costPrice = $('.costPrice').val();
						var mrp = $('.mrp').val();
						if (costPrice == "" || costPrice < 0 || mrp == "" || mrp < 0 || isNaN(costPrice) || isNaN(mrp)) {
							alert("Enter valid cost price and mrp.");
							return false;
						}
					});

					return false;
				});

				$('.variant').live("change", function() {
					var variantRow = $(this).parents('.lineItemRow');
					var productVariantId = variantRow.find('.variant').val();
					var productVariantDetails = variantRow.find('.pvDetails');
					$.getJSON(
							$('#pvInfoLink').attr('href'), {productVariantId: productVariantId, warehouse: ${whAction.setWarehouse.id}},
							function(res) {
								if (res.code == '<%=HealthkartResponse.STATUS_OK%>') {
									variantRow.find('.mrp').val(res.data.variant.markedPrice);
									variantRow.find('.costPrice').val(res.data.variant.costPrice);
									productVariantDetails.html(
											res.data.product + '<br/>' +
											res.data.options
											);
								} else {
									$('.variantDetails').html('<h2>' + res.message + '</h2>');
								}
							}
							);
				});

			});
		</script>
	</s:layout-component>
	<s:layout-component name="content">
		<div style="display: none;">
			<s:link beanclass="com.hk.web.action.admin.inventory.EditPurchaseOrderAction" id="pvInfoLink"
			        event="getPVDetails"></s:link>
		</div>
		<h2>Create/Edit Stock Transfer</h2>
		<s:form beanclass="com.hk.web.action.admin.inventory.StockTransferAction">
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
					<td>From Warehouse : </td>
					<td>
						<s:hidden name="fromWarehouse" value="${whAction.setWarehouse}"/>
						${whAction.setWarehouse.city}
					</td>
					<td>
						To Warehouse :
					</td>
					<td><s:select name="toWarehouse" class="toWarehouse">
						<c:forEach items="${whList}" var="wh">
							<c:if test="${whAction.setWarehouse.id != wh.id}">
							<s:option value="${wh}">${wh.city}</s:option>
							</c:if>
						</c:forEach>
					</s:select></td>
				</tr>
			</table>

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
					<s:hidden name="stockTransferLineItems[${ctr.index}]" value="${stockTransferLineItem.id}"/>
					<tr count="${ctr.index}" class="${ctr.last ? 'lastRow lineItemRow':'lineItemRow'}">
						<td>
								${productVariant.id}
						</td>
						<td>${productVariant.product.name}<br/>${productVariant.productOptionsWithoutColor}
						</td>
						<td> ${stockTransferLineItem.checkedoutQty}
						</td>
						<td>${stockTransferLineItem.costPrice}
						</td>
						<td> ${stockTransferLineItem.mrp}
						</td>
						<td>${stockTransferLineItem.batchNumber}</td>
						<td>
							<fmt:formatDate value="${stockTransferLineItem.mfgDate}" type="both"/></td>
						<td>
							<fmt:formatDate value="${stockTransferLineItem.expiryDate}" type="both"/></td>
					</tr>

				</c:forEach>
				</tbody>
			</table>
			<div class="variantDetails info"></div>
			<br/>
			<a href="stockTransfer.jsp#" class="addRowButton" style="font-size:1.2em">Add new row</a>

			<s:submit name="save" value="Save" class="requiredFieldValidator"/>
		</s:form>

	</s:layout-component>

</s:layout-render>