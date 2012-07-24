<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>
<s:useActionBean beanclass="com.hk.web.action.admin.inventory.InventoryCheckinAction" var="ica"/>
<s:layout-render name="/layouts/defaultAdmin.jsp" pageTitle="Stock Transfer Inventory Checkin">
	<jsp:useBean id="now" class="java.util.Date" scope="request"/>
	<s:layout-component name="htmlHead">
		<link href="${pageContext.request.contextPath}/css/calendar-blue.css" rel="stylesheet" type="text/css"/>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dynDateTime.pack.js"></script>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar-en.js"></script>
		<jsp:include page="/includes/_js_labelifyDynDateMashup.jsp"/>
	</s:layout-component>
	<s:layout-component name="heading">Inventory Checkin Against Stock Transfer</s:layout-component>
	<s:layout-component name="content">
		<div style="display:inline;float:left;">
			<h2>Item Checkin against Stock Transfer#${ica.stockTransfer.id}</h2>
			<s:form beanclass="com.hk.web.action.admin.inventory.InventoryCheckinAction">
				<s:hidden name="stockTransfer" value="${ica.stockTransfer.id}"/>
				<table border="1">
					<tr>
						<td>UPC(Barcode) or VariantID:</td>
						<td><s:text name="upc" class="variant"/></td>
					</tr>
					<tr>
						<td>Checkin Date:</td>
						<td>
							<s:text class="date_input" formatPattern="yyyy-MM-dd" name="stockTransfer.checkinDate"
							        value="${ica.stockTransfer.checkinDate != null ? ica.stockTransfer.checkinDate : now}"/>
						</td>
					</tr>
					<tr>
						<td>Checkin Qty:</td>
						<td><s:text name="qty" value="0"/></td>
					</tr>
					<tr>
						<td>Cost Price:</td>
						<td><s:text name="costPrice" value="0.0"/></td>
					</tr>
					<tr>
						<td>MRP:</td>
						<td><s:text name="mrp" value="0.0"/></td>
					</tr>
					<tr>
						<td>Batch Number:</td>
						<td><s:text name="batch" class="batchNumber"/></td>
					</tr>
					<tr>
						<td>Mfg. Date:</td>
						<td><s:text class="date_input" formatPattern="yyyy-MM-dd" name="mfgDate"/></td>
					</tr>
					<tr>
						<td>Expiry Date:</td>
						<td><s:text class="date_input" formatPattern="yyyy-MM-dd" name="expiryDate"/></td>
					</tr>
				</table>
				<script language=javascript type=text/javascript>
					$('#courierTrackingId').focus();

					function stopRKey(evt) {
						var evt = (evt) ? evt : ((event) ? event : null);
						var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);
						if ((evt.keyCode == 13) && (node.type == "text")) {
							return false;
						}
					}
					document.onkeypress = stopRKey;
					$(document).ready(function() {
						$('.requiredFieldValidator').click(function() {

							var batchNumber = $('.batchNumber').val();
							if (batchNumber == "") {
								alert("Batch Number is must.");
								return false;
							}
						});

					 	$(".pvId").click(function() {
							$('.variant').val(this.innerHTML);
						})
					});

				</script>
				<br/>
				<s:submit name="saveInventoryAgainstStockTransfer" value="Save" class="requiredFieldValidator"/>
			</s:form>
      <span style="display:inline;float:right;"><h2><s:link
		      beanclass="com.hk.web.action.admin.inventory.StockTransferAction">&lang;&lang;&lang;
	      Back to Stock Transfer List</s:link></h2></span>
		</div>
		<div style="display:inline;" align="center">

			<table style="font-size: .8em;">
				<tr>
					<th width="">S.No.</th>
					<th width="">Item</th>
					<th width="">VariantId</th>
					<th width="">UPC</th>
					<th width="">Batch</th>
					<th width="">Checked-out Qty</th>
					<th width="">Checked-in Qty</th>
				</tr>
				<c:forEach items="${ica.stockTransfer.stockTransferLineItems}" var="stockTransferLineItem" varStatus="ctr">
					<c:set value="${stockTransferLineItem.sku.productVariant}" var="productVariant"/>
					<tr>
						<td>${ctr.index+1}</td>
						<td>
								${productVariant.product.name}<br/>
							<em><c:forEach items="${productVariant.productOptions}" var="productOption">
								${productOption.name} ${productOption.value}
							</c:forEach></em>
						</td>
						<td><a href="#" class="pvId">${productVariant.id}</a></td>
						<%--<td>${productVariant.upc}</td>--%>
						<td>${stockTransferLineItem.batchNumber}</td>

						<td>${stockTransferLineItem.checkedoutQty}</td>

						<td style="color:green; font-weight:bold">${stockTransferLineItem.checkedinQty}</td>
					</tr>
				</c:forEach>
			</table>
		</div>
	</s:layout-component>
</s:layout-render>