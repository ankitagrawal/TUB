GRN for Purchase Order # ${grn.purchaseOrder.id} closed
<html>
<style>
#grnLineItemTable td{
text-align:center;
}
#poLineItemTable td{
text-align:center;
}
</style>
<head>
  <title>Welcome to HealthKart.com</title>
</head>
<body>
<#include "header.ftl">

<p style="margin-bottom:1.2em">Attention!!</p>

<p style="margin-bottom:1em">
  Goods are received for <a href="admin.healthkart.com/admin/inventory/EditPurchaseOrder.action?purchaseOrder=${grn.purchaseOrder.id}">Purchase Order # ${grn.purchaseOrder.id}.</a><br/>
  <#if grn.purchaseOrder.supplier.name??>
  Supplier = ${grn.purchaseOrder.supplier.name}; Credit Days = ${grn.purchaseOrder.supplier.creditDays}<br/>
  </#if>
  <#if grn.purchaseOrder.fillRate??>
  <#if grn.purchaseOrder.fillRate==100>
  PO Fill Rate = ${grn.purchaseOrder.fillRate} %; <br/>
  <#else>
  PO Fill Rate = <font color="#802A2A">${grn.purchaseOrder.fillRate} %</font>; <br/>
  </#if>
  <#else>
  PO Fill Rate =N/A; <br/>
  </#if>
  Approx. payable amount = ${grn.purchaseOrder.payable}; <br/>
  <#if grn.purchaseOrder.extraInventoryId??>
  Extra Inventory ID= <font color="#802A2A">${grn.purchaseOrder.extraInventoryId};</font> <br/>
  <#else>
  Extra Inventory ID = N/A; <br/>
  </#if>
  <#if grn.purchaseOrder.estDelDate??>
  Est. Delivery Date = ${grn.purchaseOrder.estDelDate}; <br/>
  <#else>
  Est. Delivery Date = N/A <br/>
  </#if>
</p>


<p>***PO Status***</p>


<table id="poLineItemTable" border="1">
	<tr>
		<th>Product Variant ID</th>
		<th>Product Detail</th>
		<th>Product Fill Rate(%)</th>
		<th>Asked Qty</th>
		<th>Total Received Qty</th>
		<th>CheckedIn With this GRN</th>
		<th>Cost Price</th>
		<th>Site Cost Price</th>
		<th>MRP</th>
		<th>Site MRP</th>
		<th>HK Price</th>
		<th>Weight</th>
		<th>JIT</th>
		<th>Deleted</th>
		<th>Hidden</th>
		<th>Other Remarks</th>
		<th>First Time Purchase</th>
		<th>Payable Amount</th>
	</tr>
	
	<#list grn.purchaseOrder.poLineItems as poLineItem>
	<tr>
	<td><#if poLineItem.sku.productVariant.deleted || poLineItem.sku.productVariant.product.hidden || poLineItem.costPrice!=poLineItem.sku.productVariant.costPrice || poLineItem.mrp!=poLineItem.sku.productVariant.markedPrice
		|| (poLineItem.sku.productVariant.weight??&&poLineItem.sku.productVariant.weight==0) || poLineItem.sku.productVariant.otherRemarks??>
 		<a href="admin.healthkart.com/product/${poLineItem.sku.productVariant.product.slug}/${poLineItem.sku.productVariant.product.id}" style="color:#802A2A";>${poLineItem.sku.productVariant.id}</a>
  		<#else>
  		<a href="admin.healthkart.com/product/${poLineItem.sku.productVariant.product.slug}/${poLineItem.sku.productVariant.product.id}">${poLineItem.sku.productVariant.id}</a>
  		</#if>
  		</td>
	<td>${poLineItem.sku.productVariant.product.name}<br/>${poLineItem.sku.productVariant.optionsCommaSeparated}</td>
	<td><#if poLineItem.fillRate??>
 		 ${poLineItem.fillRate}
  		<#else>
  		N/A
  		</#if></td>
	<td>${poLineItem.qty}</td>
	<td><#if poLineItem.receivedQty??>
 		 ${poLineItem.receivedQty}
  		<#else>
  		N/A
  		</#if></td>
  		
  	<#assign checkedInQtytest = 0>
	<#list grn.grnLineItems as grnLineItem>
	<#if poLineItem.sku==grnLineItem.sku>
	<#assign checkedInQtytest = 1>
	<#if grnLineItem.checkedInQty??>
 		 <td>${grnLineItem.checkedInQty}</td>
  		<#else>
  		<td>N/A</td>
  		</#if>
	</#if>
	</#list>
	<#if checkedInQtytest ==0>
	<td> </td>
	</#if>
	
	<#assign cptest = 0>
	<#list grn.grnLineItems as grnLineItem>
	<#if poLineItem.sku==grnLineItem.sku>
	<#assign cptest = 1>
		<#if grnLineItem.costPrice!=poLineItem.sku.productVariant.costPrice>
			<td><font color="#802A2A">${grnLineItem.costPrice}</font></td>
			<td><font color="#802A2A">${poLineItem.sku.productVariant.costPrice}</font></td>
		<#else>
			<td>${grnLineItem.costPrice}</td>
			<td>${poLineItem.sku.productVariant.costPrice}</td>
		</#if>
	</#if>
	</#list>
	<#if cptest==0>
			<td>N/A</td>
			<td>${poLineItem.sku.productVariant.costPrice}</td>
	</#if>
	
	<#assign mptest =0>
	<#list grn.grnLineItems as grnLineItem>
	<#if poLineItem.sku==grnLineItem.sku>
	<#assign mptest =1>
		<#if grnLineItem.mrp!=poLineItem.sku.productVariant.markedPrice>
			<td><font color="#802A2A">${grnLineItem.mrp}</font></td>
			<td><font color="#802A2A">${poLineItem.sku.productVariant.markedPrice}</font></td>
		<#else>
			<td>${grnLineItem.mrp}</td>
			<td>${poLineItem.sku.productVariant.markedPrice}</td>
		</#if>
	</#if>
	</#list>
	<#if mptest==0>
			<td>N/A</td>
			<td>${poLineItem.sku.productVariant.markedPrice}</td>
	</#if>
	
	<td>${poLineItem.sku.productVariant.hkPrice}</td>
	<td><#if poLineItem.sku.productVariant.weight??>
	<#if poLineItem.sku.productVariant.weight==0>
 		 <font color="#0000FF">${poLineItem.sku.productVariant.weight}</font>
 		 <#else>
 		 ${poLineItem.sku.productVariant.weight}
 		 </#if>
  		<#else>
  		N/A
  		</#if></td>
  	<td><#if poLineItem.sku.productVariant.product.isJit()>
 		 True
  		<#else>
  		False
  		</#if></td>
  	<td><#if poLineItem.sku.productVariant.product.deleted>
 		 True
  		<#else>
  		False
  		</#if></td>
  	<td><#if poLineItem.sku.productVariant.product.hidden>
 		 True
  		<#else>
  		False
  		</#if></td>
  	<td><#if poLineItem.sku.productVariant.otherRemark??>
 		 ${poLineItem.sku.productVariant.otherRemark}
  		<#else>
  		N/A
  		</#if></td>
  	<td></td>
	<td>${poLineItem.payableAmount}</td>
	</tr>
	</#list>
	
	<tr>
	<td colspan="16"></td>
	<td colspan="2">Total Payable: <#if grn.purchaseOrder.payable??>
 		 ${grn.purchaseOrder.payable}
  		<#else>
  		N/A
  		</#if></td>
  	</tr>
	</table>

<p style="margin-bottom:1em"><strong>HealthKart Team</strong></p>
<#include "footer.ftl">
</body>
</html>