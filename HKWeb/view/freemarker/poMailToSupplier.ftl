Purchase Order Approval Mail
<html>
<head>
  <title>Welcome to HealthKart.com</title>
</head>
<body>
<#include "header.ftl">

		<div style="padding: 2px;">
		<p style="margin-top: 2px;margin-bottom: 2px;margin-left: 2px;">${purchaseOrder.warehouse.name}</p>
		<p style="margin-top: 2px;margin-bottom: 2px;margin-left: 2px;">${purchaseOrder.warehouse.line1}</p>
		<p style="margin-top: 2px;margin-bottom: 2px;margin-left: 2px;">${purchaseOrder.warehouse.line2}</p>
		<p style="margin-top: 2px;margin-bottom: 2px;margin-left: 2px;">${purchaseOrder.warehouse.city}-${purchaseOrder.warehouse.pincode}</p>
		<p style="margin-top: 2px;margin-bottom: 2px;margin-left: 2px;">${purchaseOrder.warehouse.state}</p>
		<p style="margin-top: 2px;margin-bottom: 2px;margin-left: 2px;">Tin No - ${purchaseOrder.warehouse.tin}</p>
		</div>

<div style="text-align: center;">
<h4>
PURCHASE ORDER
</h4>
</div>

  <table cellspacing="0" id="mainTable">
  <tr>
  <td style="font-weight: bold;">Supplier</td>
  <td>${purchaseOrder.supplier.name}</td>
  <td style="font-weight: bold;">PO Place Date</td>
  
  <#if purchaseOrder.poPlaceDate??>
  <td>${purchaseOrder.poPlaceDate }</td>
  <#else>
  <td>N/A</td>
  </#if>
  </tr>
  
  <tr>
  <td style="font-weight: bold;">Address</td>
  <td>
  <#if purchaseOrder.supplier.line1??>
  <p style="margin-top: 2px;margin-bottom: 2px;margin-left: 2px;">${purchaseOrder.supplier.line1}</p>
  </#if>
  <#if purchaseOrder.supplier.line2??>
  <p style="margin-top: 2px;margin-bottom: 2px;margin-left: 2px;">${purchaseOrder.supplier.line2}</p>
  </#if>
  <#if purchaseOrder.supplier.city??>
  <p style="margin-top: 2px;margin-bottom: 2px;margin-left: 2px;">${purchaseOrder.supplier.city}</p>
  </#if>
  <#if purchaseOrder.supplier.state??>
  <p style="margin-top: 2px;margin-bottom: 2px;margin-left: 2px;">${purchaseOrder.supplier.state}</p>
  </#if>
  </td>
  <td style="font-weight: bold;">PO#</td>
  <td>${purchaseOrder.id }</td>
  </tr>
  
  <tr>
  	<td style="font-weight: bold;">Contact Name</td>
  	<#if purchaseOrder.supplier.contactPerson??>
  	<td>${purchaseOrder.supplier.contactPerson}</td>
  	<#else>
  	<td>N/A</td>
  	</#if>
  	<td style="font-weight: bold;">Contact Number</td>
  	<#if purchaseOrder.supplier.contactNumber??>
  	<td>${purchaseOrder.supplier.contactNumber}</td>
  	<#else>
  	<td>N/A</td>
   </#if>
  </tr>
  
  </table>

<br/>
  <table cellspacing="0" id="mainTable" >
  <tr>
  <td style="font-weight: bold;">Variant Id</td>
  <td style="font-weight: bold;">UPC</td>
  <td style="font-weight: bold;">Details</td>
  <td style="font-weight: bold;">Quantity</td>
  <td style="font-weight: bold;">Cost Price<br/>(Without Tax)</td>
  <td style="font-weight: bold;">MRP</td>
  <td style="font-weight: bold;">Tax(%)</td>
  <td style="font-weight: bold;">Taxable</td>
  <td style="font-weight: bold;">Tax</td>
  <td style="font-weight: bold;">Surcharge</td>
  <td style="font-weight: bold;">Payable</td>
  </tr>
  
  <#list purchaseOrder.poLineItems as poLineItem>
  <tr>
	<#if poLineItem.sku??>
		<td>${poLineItem.sku.productVariant.id}</td>
		<#if poLineItem.sku.productVariant.upc??>
		<#else>
		<td>N/A</td>
		</#if>
		<td>${poLineItem.sku.productVariant.product.name}<br/>${poLineItem.sku.productVariant.optionsCommaSeparated}</td>
	<#else>
		<td>N/A</td>
		<td>N/A</td>
		<td>${poLineItem.productName}</td>
	</#if>
	
	<td>${poLineItem.qty}</td>
	<td>${poLineItem.costPrice}</td>
	<td>${poLineItem.mrp}</td>
	<td>${poLineItem.sku.tax.value*100}</td>
	<td>${poLineItem.taxableAmount}</td>
	<td>${poLineItem.taxAmount}</td>
	<td>${poLineItem.surchargeAmount}</td>
	<td>${poLineItem.payableAmount}</td>
	</tr>
	</#list>
	<tr>
	<td colspan="6">Total</td>
	<td>${purchaseOrder.taxableAmount}</td>
	<td>${purchaseOrder.taxAmount}</td>
	<td>${purchaseOrder.surchargeAmount}</td>
	<td>${purchaseOrder.finalPayableAmount}</td>
	</tr>
  </table>

<p style="margin-bottom:1em"><strong>HealthKart Team</strong></p>
<#include "footer.ftl">
</body>
</html>