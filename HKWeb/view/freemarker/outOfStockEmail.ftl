[ATTENTION] Inventory of Variant [${productVariant.id}] is Out of Stock
<html>
<head>
  <title>[ATTENTION] Inventory of Variant [${productVariant.id}] is Out  of Stock</title>
</head>
<body>
<#include "header.ftl">
<p>Dear ${category} Manager,</p>
<h2>Attention!!!</h2>

<p>
  Inventory of Variant ${productVariant.id} is is Out  of Stock now. Please get it replenished ASAP.
</p>

<div>
  <h3>Variant Details</h3>
  <table cellpadding="5" cellspacing="0" border="1" style="font-size:12px;">
    <tr>
      <td>Product</td>
      <td>${productVariant.product.name}</td>
    </tr>
    <tr>
      <td>VariantID</td>
      <td>${productVariant.id}</td>
    </tr>
    <tr>
      <td>Options</td>
      <td>
        <table cellpadding="2" cellspacing="0" border="1" style="font-size:10px;">
          <#if productVariant.productOptions?has_content>
          	<#list productVariant.productOptions as productOption>
         		<tr>
            		<td>${productOption.name}</td>
            		<td>${productOption.value}</td>
          		</tr>
          	</#list>
          </#if>
        </table>
      </td>
    </tr>
    <#if productVariant.upc??>
    <tr>
      <td>UPC</td>
      <td>${productVariant.upc}</td>
    </tr>
    </#if>
    <#if productVariant.product.supplier??>
    <tr>
      <td>Preferred Supplier</td>
      <td>${productVariant.product.supplier.name}</td>
    </tr>
    </#if>
  </table>
</div>

<p>
  - HealthKart Admin
</p>

<#include "footer.ftl">
</body>
</html>