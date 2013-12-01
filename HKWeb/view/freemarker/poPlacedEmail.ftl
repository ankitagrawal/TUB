Order for Purchase Order # ${purchaseOrder.id} is placed
<html>
<head>
  <title>Welcome to HealthKart.com</title>
</head>
<body>
<#include "header.ftl">

<p style="margin-bottom:1.2em">Attention!!</p>

<p style="margin-bottom:1em">
  Order for Purchase Order # ${purchaseOrder.id} is placed.<br/>
  Supplier = ${purchaseOrder.supplier.name} <br/>
  Approx. payable amount = ${purchaseOrder.payable} <br/>
  <#if purchaseOrder.estPaymentdate??>
  Est. Payment Date = ${purchaseOrder.estPaymentdate} <br/>
  </#if>
</p>


<p style="margin-bottom:1em"><strong>HealthKart Team</strong></p>
<#include "footer.ftl">
</body>
</html>