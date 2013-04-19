GRN for Purchase Order # ${grn.purchaseOrder.id} closed
<html>
<head>
  <title>Welcome to HealthKart.com</title>
</head>
<body>
<#include "header.ftl">

<p style="margin-bottom:1.2em">Attention!!</p>

<p style="margin-bottom:1em">
  Goods are received for Purchase Order # ${grn.purchaseOrder.id}.<br/>
  <#if grn.purchaseOrder.supplier.name??>
  Supplier = ${grn.purchaseOrder.supplier.name}; Credit Days = ${grn.purchaseOrder.supplier.creditDays}<br/>
  </#if>
  <#if grn.purchaseOrder.fillRate??>
  PO Fill Rate = ${grn.purchaseOrder.fillRate} %; <br/>
  <#else>
  PO Fill Rate =N/A; <br/>
  </#if>
  Approx. payable amount = ${grn.purchaseOrder.payable}; <br/>
  <#if grn.purchaseOrder.extraInventoryId??>
  Extra Inventory ID= ${grn.purchaseOrder.extraInventoryId}; <br/>
  <#else>
  Extra Inventory ID = N/A; <br/>
  </#if>
  <#if grn.purchaseOrder.estDelDate??>
  Est. Delivery Date = ${grn.purchaseOrder.estDelDate}; <br/>
  <#else>
  Est. Delivery Date = N/A <br/>
  </#if>
</p>

<table border="1">
	<tr>
		<th>Product Variant ID</th>
		<th>Product Detail</th>
		<th>Product Weight</th>
		<th>Product Fill Rate</th>
		<th>Asked Qty</th>
		<th>Checked In Qty </th>
		<th>Cost Price</th>
		<th>MRP</th>
		<th>MRP On HK</th>
		<th>Payable Amount</th>
	</tr>
	 <#list grn.grnLineItems as grnLineItem>
	<tr>
		<td>${grnLineItem.sku.productVariant.id}</td>
		<td>${grnLineItem.sku.productVariant.product.name}<br/>${grnLineItem.sku.productVariant.optionsCommaSeparated}</td>
		<td><#if grnLineItem.sku.productVariant.weight??>
 		 $${grnLineItem.sku.productVariant.weight}
  		<#else>
  		N/A
  		</#if></td>
		<td><#if grnLineItem.fillRate??>
 		 ${grnLineItem.fillRate}
  		<#else>
  		N/A
  		</#if></td>
		<td>${grnLineItem.qty}</td>
		<td>${grnLineItem.checkedInQty}</td>
		<td>${grnLineItem.costPrice}</td>
		<td>${grnLineItem.mrp}</td>
		<td>${grnLineItem.sku.productVariant.markedPrice}</td>
		<td><#if grnLineItem.payableAmount??>
 		 $${grnLineItem.payableAmount}
  		<#else>
  		N/A
  		</#if></td>
	</tr>
	</#list>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td></td>
	<td>Total Payable </td>
	<td><#if grn.payable??>
 		 $${grn.payable}
  		<#else>
  		N/A
  		</#if></td>
</table>



<p style="margin-bottom:1em"><strong>HealthKart Team</strong></p>
<#include "footer.ftl">
</body>
</html>