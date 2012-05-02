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
  Approx. payable amount = ${grn.purchaseOrder.payable} <br/>
  <#if grn.purchaseOrder.estDelDate??>
  Est. Delivery Date = ${grn.purchaseOrder.estDelDate} <br/>
  </#if>
</p>


<p style="margin-bottom:1em"><strong>HealthKart Team</strong></p>
<#include "footer.ftl">
</body>
</html>