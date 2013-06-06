<html>
<style>
p {
    margin-top: 2px;
    margin-bottom: 2px;
    margin-left: 2px;
  }
 table {
    width: 100%;
    font-size: .8em;
    border-width: 0 0 1px 1px;
    border-style: solid;
    border-collapse: separate;
    border-color: black;
  }

  table tr td {
    text-align: left;
    font-size: small;
    border-width: 1px 1px 0 0;
    border-style: solid;
    border-color: black;
  }

  table tr th {
    border-width: 1px 1px 0 0;
    border-style: solid;
    border-color: black;
  }
  
  table th {
  text-align: center;
  }

  .clear {
    clear: both;
    display: block;
    overflow: hidden;
    visibility: hidden;
    width: 0;
    height: 0
  }

  .tdHead{
	font-weight: bold;
	}
	
  .column {
    padding: 2px;
  }
	
</style>
<head>
  <title>Welcome to HealthKart.com</title>
</head>
<body>
<#include "header.ftl">

<div class="grid_12">
	<div class="grid_4 alpha omega">
		<div class="column">
		<p>${purchaseOrder.warehouse.name}</p>
		<p>${purchaseOrder.warehouse.line1}</p>
		<p>${purchaseOrder.warehouse.line2}</p>
		<p>${purchaseOrder.warehouse.city}-${purchaseOrder.warehouse.pincode}</p>
		<p>${purchaseOrder.warehouse.state}</p>
		<p>Tin No - ${purchaseOrder.warehouse.tin}</p>
		</div>
	</div>
</div>

<div style="text-align: center;">
<h4>
PURCHASE ORDER
</h4>
</div>

<div class="grid_12">
  <table cellspacing="0" id="mainTable">
  <tr>
  <td class="tdHead">Supplier</td>
  <td>${purchaseOrder.supplier.name}</td>
  <td class="tdHead">PO Place Date</td>
  <td>${purchaseOrder.poPlaceDate }</td>
  </tr>
  
  <tr>
  <td class="tdHead">Address</td>
  <td>
  <#if purchaseOrder.supplier.line1??>
  <p>${purchaseOrder.supplier.line1}</p>
  </#if>
  <#if purchaseOrder.supplier.line2??>
  <p>${purchaseOrder.supplier.line2}</p>
  </#if>
  <#if purchaseOrder.supplier.city??>
  <p>${purchaseOrder.supplier.city}</p>
  </#if>
  <#if purchaseOrder.supplier.state??>
  <p>${purchaseOrder.supplier.state}</p>
  </#if>
  </td>
  <td class="tdHead">PO#</td>
  <td>${purchaseOrder.id }</td>
  </tr>
  
  <tr>
  	<td class="tdHead">Contact Name</td>
  	<#if purchaseOrder.supplier.contactPerson??>
  	<td>${purchaseOrder.supplier.contactPerson}</td>
  	<#else>
  	<td>N/A</td>
  	</#if>
  	<td class="tdHead">Contact Number</td>
  	<#if purchaseOrder.supplier.contactNumber??>
  	<td>${purchaseOrder.supplier.contactNumber}</td>
  	<#else>
  	<td>N/A</td>
   </#if>
  </tr>
  
  </table>
  </div>


<div class="clear"></div>
<div style="margin-top: 20px;"></div>
<br/>
  <div class="grid_12">
  <table cellspacing="0" id="mainTable">
  <tr>
  <td class="tdHead">Variant Id</td>
  <td class="tdHead">UPC</td>
  <td class="tdHead">Details</td>
  <td class="tdHead">Quantity</td>
  <td class="tdHead">Cost Price<br/>(Without Tax)</td>
  <td class="tdHead">MRP</td>
  <td class="tdHead">Tax(%)</td>
  <td class="tdHead">Taxable</td>
  <td class="tdHead">Tax</td>
  <td class="tdHead">Surcharge</td>
  <td class="tdHead">Payable</td>
  </tr>
  
  <#list purchaseOrder.poLineItems as poLineItem>
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
  </table></div>

<p style="margin-bottom:1em"><strong>HealthKart Team</strong></p>
<#include "footer.ftl">
</body>
</html>