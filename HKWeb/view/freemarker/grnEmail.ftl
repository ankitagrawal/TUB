GRN for Purchase Order # ${grn.purchaseOrder.id} closed
<html>
<style>
#poLineItemTable td{
text-align:center;
}

.colortd{
background: #ff0;
}

</style>
<head>
  <title>Welcome to HealthKart.com</title>
</head>
<body>
<#include "header.ftl">
<#assign skuGroupsSize = skuGroups?size>
<p style="margin-bottom:1.2em">Attention!!</p>

<p style="margin-bottom:1em">
  Goods are received for <a href="admin.healthkart.com/admin/inventory/EditPurchaseOrder.action?purchaseOrder=${grn.purchaseOrder.id?c}">Purchase Order # ${grn.purchaseOrder.id}.</a><br/>
  <#if grn.purchaseOrder.supplier.name??>
  <#if grn.purchaseOrder.supplier.creditDays??&&grn.purchaseOrder.supplier.creditDays==-1>
  Supplier = ${grn.purchaseOrder.supplier.name}; Credit Days = <label style="background:#ff0;">${grn.purchaseOrder.supplier.creditDays}</label><br/>
  <#else>
  Supplier = ${grn.purchaseOrder.supplier.name}; Credit Days = ${grn.purchaseOrder.supplier.creditDays}<br/>
  </#if>
  </#if>
  <#if grn.purchaseOrder.fillRate??>
  <#if grn.purchaseOrder.fillRate==100>
  PO Fill Rate = <label style="background:#ff0;">${grn.purchaseOrder.fillRate} % </label>;<br/>
  <#else>
  PO Fill Rate = <label style="background:#ff0;">${grn.purchaseOrder.fillRate} %</label>; <br/>
  </#if>
  <#else>
  PO Fill Rate =N/A; <br/>
  </#if>
  Approx. payable amount = ${grn.purchaseOrder.payable}; <br/>
  <#if grn.purchaseOrder.extraInventoryId??>
  Extra Inventory ID=  <a href="admin.healthkart.com/admin/rtv/ExtraInventory.action?purchaseOrderId=${grn.purchaseOrder.extraInventoryId?c}">${grn.purchaseOrder.extraInventoryId}</a>
  <#else>
  Extra Inventory ID = N/A; <br/>
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
		<th>Checkedin Batch Cost Price</th>
		<th>Site Cost Price</th>
		<th>Checkedin Batch MRP</th>
		<th>Site MRP</th>
		<th>HK Price</th>
		<th>Checking Weight(gm.)</th>
		<th>Weight(gm.)</th>
		<th>JIT</th>
		<th>Deleted</th>
		<th>Hidden</th>
		<th>Other Remarks</th>
		<th>First Time Purchased</th>
		<th>Payable Amount</th>
	</tr>
	
	<#list grn.purchaseOrder.poLineItems as poLineItem>
	<tr>
	<#if poLineItem.sku.productVariant.deleted || poLineItem.sku.productVariant.product.hidden 
		|| (poLineItem.sku.productVariant.weight??&&poLineItem.sku.productVariant.weight==0) || poLineItem.sku.productVariant.otherRemarks??>
 		<td style="background:#ff0;"><a href="admin.healthkart.com/product/${poLineItem.sku.productVariant.product.slug}/${poLineItem.sku.productVariant.product.id}" style="color:#802A2A";>${poLineItem.sku.productVariant.id}</a></td>
  		<#else>
  		<td><a href="admin.healthkart.com/product/${poLineItem.sku.productVariant.product.slug}/${poLineItem.sku.productVariant.product.id}">${poLineItem.sku.productVariant.id}</a></td>
  		</#if>
	<td>${poLineItem.sku.productVariant.product.name}<br/>${poLineItem.sku.productVariant.optionsCommaSeparated}</td>
	<td><#if poLineItem.fillRate??>
 		 ${poLineItem.fillRate}
  		<#else> 
  		</#if></td>
	<td>${poLineItem.qty}</td>
	<td><#if poLineItem.receivedQty??>
 		 ${poLineItem.receivedQty}
  		<#else>
  		 
  		</#if></td>
  		
  	<#assign checkedInQtytest = 0>
	<#list grn.grnLineItems as grnLineItem>
	<#if poLineItem.sku==grnLineItem.sku>
	<#assign checkedInQtytest = 1>
	<#if grnLineItem.checkedInQty??>
 		 <td>${grnLineItem.checkedInQty}</td>
  		<#else>
  		<td></td>
  		</#if>
	</#if>
	</#list>
	<#if checkedInQtytest ==0>
	<td></td>
	</#if>
	
	<#if (skuGroupsSize>0)>
	<td><#list skuGroups as skuGroup>
	<#if poLineItem.sku==skuGroup.sku>
		<#if skuGroup.costPrice!=poLineItem.sku.productVariant.costPrice>
			<label style="background:#ff0;">${skuGroup.costPrice}</label>, <br/>
		<#else>
			<label>${skuGroup.costPrice}</label>, <br/>
		</#if>
	</#if>
	</#list></td>
	<td>${poLineItem.sku.productVariant.costPrice}</td>
	<#else>
	<#assign cptest = 0>
	<#list grn.grnLineItems as grnLineItem>
	<#if poLineItem.sku==grnLineItem.sku>
	<#assign cptest = 1>
		<#if grnLineItem.costPrice!=poLineItem.sku.productVariant.costPrice>
			<td style="background:#ff0;">${grnLineItem.costPrice}</td>
			<td style="background:#ff0;">${poLineItem.sku.productVariant.costPrice}</td>
		<#else>
			<td>${grnLineItem.costPrice}</td>
			<td>${poLineItem.sku.productVariant.costPrice}</td>
		</#if>
	</#if>
	</#list>
	<#if cptest==0>
			<td></td>
			<td>${poLineItem.sku.productVariant.costPrice}</td>
	</#if>
	</#if>
	
	<#if (skuGroupsSize>0)>
	<td><#list skuGroups as skuGroup>
	<#if poLineItem.sku==skuGroup.sku>
		<#if skuGroup.mrp!=poLineItem.sku.productVariant.markedPrice>
			<label style="background:#ff0;">${skuGroup.mrp}</label>, <br/>
		<#else>
			<label>${skuGroup.mrp}</label>, <br/>
		</#if>
	</#if>
	</#list></td>
	<td>${poLineItem.sku.productVariant.markedPrice}</td>
	<#else>
	<#assign mptest =0>
	<#list grn.grnLineItems as grnLineItem>
	<#if poLineItem.sku==grnLineItem.sku>
	<#assign mptest =1>
		<#if grnLineItem.mrp!=poLineItem.sku.productVariant.markedPrice>
			<td style="background:#ff0;">${grnLineItem.mrp}</td>
			<td style="background:#ff0;">${poLineItem.sku.productVariant.markedPrice}</td>
		<#else>
			<td>${grnLineItem.mrp}</td>
			<td>${poLineItem.sku.productVariant.markedPrice}</td>
		</#if>
	</#if>
	</#list>
	<#if mptest==0>
			<td></td>
			<td>${poLineItem.sku.productVariant.markedPrice}</td>
	</#if>
	</#if>
	<td>${poLineItem.sku.productVariant.hkPrice}</td>
	
	<#assign hasWeight = 0>
	<#list grn.grnLineItems as grnLineItem>
	<#if poLineItem.sku==grnLineItem.sku>
	<#assign hasWeight = 1>
	<#if grnLineItem.weight?? && poLineItem.sku.productVariant.weight??>
 		 <#if grnLineItem.weight!=poLineItem.sku.productVariant.weight>
			<td style="background:#ff0;">${grnLineItem.weight}</td>
		<#else>
			<td>${grnLineItem.weight}</td>
		</#if>
		<#else>
			<#if grnLineItem.weight??>
			<td style="background:#ff0;">${grnLineItem.weight}</td>
		<#else>
			<td>N/A</td>
		</#if>
	</#if>
	</#if>
	</#list>
	<#if hasWeight ==0>
	<td style="background:#ff0;">N/A</td>
	</#if>
	
	<#if poLineItem.sku.productVariant.weight??>
	<#if poLineItem.sku.productVariant.weight==0>
 		 <td style="background:#ff0;">${poLineItem.sku.productVariant.weight}</td>
 		 <#else>
 		 <td>${poLineItem.sku.productVariant.weight}</td>
 		 </#if>
  		<#else>
  		 <td></td>
  		</#if>
  	<#if poLineItem.sku.productVariant.product.isJit()>
 		<td style="background:#ff0;">1</td>
  		<#else>
  		<td>0</td>
  		</#if>
  	<#if poLineItem.sku.productVariant.deleted>
 		<td style="background:#ff0;">1</td>
  		<#else>
  		<td>0</td>
  		</#if>
  	<#if poLineItem.sku.productVariant.product.hidden>
 		<td style="background:#ff0;">1</td>
  		<#else>
  		<td>0</td>
  		</#if>
  	<td><#if poLineItem.sku.productVariant.otherRemark??>
 		 ${poLineItem.sku.productVariant.otherRemark}
  		<#else>
  		
  		</#if></td>
  	<#if poLineItem.firstTimePurchased>
  	<td style="background:#ff0;">Yes</td>
  	<#else>
  	<td></td>
  	</#if>
  	
	<td>${poLineItem.payableAmount}</td>
	</tr>
	</#list>
	
	<tr>
	<td colspan="17"></td>
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