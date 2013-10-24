Attention!! Mail For Purchase Order # ${purchaseOrder.id} Could Not Be Sent.
<html>
<head>
  <title>Welcome to HealthKart.com</title>
</head>
<body>
<#include "header.ftl">

<p style="margin-bottom:1.2em">Attention!!</p>
<p style="margin-bottom:1em">
  Mail For Purchase Order # ${purchaseOrder.id} Could Not Be Sent.<br/>
  Please send it manually.<br/>
  Supplier = ${purchaseOrder.supplier.name} <br/>
</p>

<p style="margin-bottom:1em"><strong>HealthKart Team</strong></p>
<#include "footer.ftl">
</body>
</html>