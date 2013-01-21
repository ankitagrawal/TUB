<%@ page import="java.text.DateFormat" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="/includes/_taglibInclude.jsp" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
	<title>Stock Transfer</title>
	<style type="text/css">
		table {
			border-collapse: collapse;
			width: 100%;
			font-size:.8em;
		}

		table tr td {
			padding: 1px;
			border: 1px solid #CCC;
		}

		table tr th {
			padding: 1px;
			border: 1px solid #CCC;
			text-align: left;
		}

		h2 {
			margin: 0;
			padding: 0;
		}

		h1 {
			margin: 0;
			padding: 0;
		}

		table.header tr td {
			border: none;
			vertical-align: top;
		}

		.clear {
			clear: both;
			display: block;
			overflow: hidden;
			visibility: hidden;
			width: 0;
			height: 0
		}
	</style>
</head>
<body>

<s:useActionBean beanclass="com.hk.web.action.admin.inventory.StockTransferAction" var="easySolBean"/>
<c:set var="totalAmount" value="${0}" />
<table class="header">
	<tr>
		<td>
			Bright Lifecare Pvt. Ltd.<br/>
			${easySolBean.stockTransfer.fromWarehouse.line1}<br/>
			${easySolBean.stockTransfer.fromWarehouse.line2}<br/>
			${easySolBean.stockTransfer.fromWarehouse.city} &nbsp;
			-${easySolBean.stockTransfer.fromWarehouse.pincode} <br/>
			${easySolBean.stockTransfer.fromWarehouse.state}<br/>
			TIN: ${easySolBean.stockTransfer.fromWarehouse.tin}
		</td>
		<td align="right">
			<img src="${pageContext.request.contextPath}/images/logo.png" alt=""/>
		</td>
	</tr>
</table>

<h3 align="center">STOCK TRANSFER</h3>
<br/>
<table style="font-size:.9em">
	<tr>
		<td><b>Stock Transfer id:</b></td>
		<td>${easySolBean.stockTransfer.id}</td>
		<td><b>Stock Transfer Date</b></td>
		<td><fmt:formatDate value="${easySolBean.stockTransfer.createDate}" pattern="yyyy-MM-dd"/></td>
	</tr>
	<tr>
		<td><b>From Warehouse:</b></td>
		<td>${easySolBean.stockTransfer.fromWarehouse.name}</td>
		<td><b>To Warehouse</b></td>
		<td>${easySolBean.stockTransfer.toWarehouse.name}</td>
	</tr>
	<tr valign="top">
		<td><b>Checked out date: </b></td>
		<td><fmt:formatDate value="${easySolBean.stockTransfer.checkoutDate}" pattern="yyyy-MM-dd"/>
		</td>
		<td><b>Check in date:</b></td>
		<td><fmt:formatDate value="${easySolBean.stockTransfer.checkinDate}" pattern="yyyy-MM-dd"/></td>
	</tr>
	<tr>
		<td><b>Created By</b></td>
		<td>${easySolBean.stockTransfer.createdBy.name}</td>
		<td><b>Received By</b></td>
		<td>${easySolBean.stockTransfer.receivedBy.name}</td>
	</tr>
</table>

<br/>

<table border="1">
	<thead>
	<tr>
		<th>S.No.</th>
		<th>Product Name</th>
		<th>Product Variant Id</th>
		<th>Manufacturer</th>
		<th>Batch Number</th>
		<th>Expiry (dd/mm/yyyy)</th>
		<th>Cost Price</th>
		<th>MRP</th>
		<th>HK Selling Price</th>
		<th>VAT %</th>
		<th>Checked Out Quantity</th>
		<th>Value</th>

	</tr>
	</thead>
	<tbody id="poTable">
	<c:forEach items="${easySolBean.stockTransfer.stockTransferLineItems}" var="stockTransferLineItem" varStatus="ctr">
		<tr>
			<td>${ctr.index+1}.</td>
			<td>
					${stockTransferLineItem.sku.productVariant.product.name}
				<em>
					    <br>
						${stockTransferLineItem.sku.productVariant.optionsPipeSeparated}
					    <br>
						${stockTransferLineItem.sku.productVariant.extraOptionsPipeSeparated}

				</em>
			</td>
			<td>${stockTransferLineItem.sku.productVariant}</td>
			<td>${stockTransferLineItem.sku.productVariant.product.manufacturer.name}</td>
			<td>
					${stockTransferLineItem.checkedOutSkuGroup.batchNumber}
			</td>
			<td><fmt:formatDate value="${stockTransferLineItem.expiryDate}" pattern="dd/MM/yyyy" /> </td>
			<td>${stockTransferLineItem.checkedOutSkuGroup.costPrice}
			</td>
			<td>${stockTransferLineItem.checkedOutSkuGroup.mrp}</td>
			<td>${stockTransferLineItem.sku.productVariant.hkPrice}</td>
			<td>${stockTransferLineItem.sku.tax.value * 100}</td>
			<td>${stockTransferLineItem.checkedoutQty}
			</td>
			<td>${stockTransferLineItem.checkedoutQty * stockTransferLineItem.checkedOutSkuGroup.costPrice}
				<c:set var="totalAmount" value="${totalAmount + (stockTransferLineItem.checkedoutQty * stockTransferLineItem.checkedOutSkuGroup.costPrice)}" />
			</td>
		</tr>
	</c:forEach>
	</tbody>
	<tr>
		<td ></td>
		<td ></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td></td>
		<td><strong>Total</strong></td>
		<td></td>
		<td><strong>${totalAmount}</strong></td>
	</tr>
</table>
<br>
</body>
</html>
