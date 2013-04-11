GRN for Purchase Order # ${grn.purchaseOrder.id} is created
<html>
<head>
  <title>Welcome to HealthKart.com</title>
</head>
<body>
<#include "header.ftl">

<p style="margin-bottom:1.2em">Attention!!</p>

<p style="margin-bottom:1em">
  Goods are received for Purchase Order # ${grn.purchaseOrder.id}.<br/>
  Supplier = ${grn.purchaseOrder.supplier.name} <br/>
  <#if grn.purchaseOrder.supplier.name??>
   PO Fill Rate = ${grn.purchaseOrder.fillRate};
  </#if>
  Approx. payable amount = ${grn.purchaseOrder.payable} <br/>
  <#if grn.purchaseOrder.extraInventory??>
   Extra Inventory = ${grn.purchaseOrder.extraInventory.id};
  </#if>
  <#if grn.purchaseOrder.estDelDate??>
  Est. Delivery Date = ${grn.purchaseOrder.estDelDate} <br/>
  </#if>
</p>

<table border="1">
	<tr>
		<th>Product Variant ID</th>
		<th>Product Detail</th>
		<th>Product Fill Rate</th>
		<th>Asked Qty</th>
		<th>Checked In Qty </th>
		<th>Cost Price</th>
		<th>MRP</th>
	</tr>
	 <#list grn.grnLineItems as grnLineItem>
	<tr>
		<td>${grnLineItem.sku.productVariant.id}</td>
		<td>${grnLineItem.sku.productVariant.product.name}</td>
		<#list grnLineItem.goodsReceivedNote.purchaseOrder.poLineItems as poLineItem>
		<#if grnLineItem.sku.id==poLineItem.sku.id>
		<td>${poLineItem.fillRate}</td>
		</#if>
		</#list>
		<td>${grnLineItem.qty}</td>
		<td>${grnLineItem.checkedInQty}</td>
		<td>${grnLineItem.costPrice}</td>
		<td>${grnLineItem.mrp}</td>	
	</tr>
	</#list>
</table>


<p style="margin-bottom:1em"><strong>HealthKart Team</strong></p>
<#include "footer.ftl">
</body>
</html>