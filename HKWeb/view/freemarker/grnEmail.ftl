GRN for Purchase Order # ${grn.purchaseOrder.id}
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
   Supplier = ${grn.purchaseOrder.supplier.name} <br/>
  </#if>
  PO Fill Rate = ${grn.purchaseOrder.fillRate};
  Approx. payable amount = ${grn.purchaseOrder.payable} <br/>
  <#if grn.purchaseOrder.extraInventory??>
   Extra Inventory = ${grn.purchaseOrder.extraInventory.id};
   <#else>
   Extra Inventory = N/A;
  </#if>
  <#if grn.purchaseOrder.estDelDate??>
  Est. Delivery Date = ${grn.purchaseOrder.estDelDate} <br/>
  <#else>
  Est. Delivery Date = N/A <br/>
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
		<td>${grnLineItem.fillRate}</td>
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