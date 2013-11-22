Purchase Order # ${purchaseOrder.id} Approved
<html>
<head>
  <title>Welcome to HealthKart.com</title>
</head>
<body>
<#include "header.ftl">

<p style="margin-bottom:1.2em">Attention!!</p>

<p style="margin-bottom:1em">
  Purchase Order # ${purchaseOrder.id} is Approved.<br/>
  By Name :: <strong>${user.name}</strong>
  Email  ::   By Name> <strong>${user.email}</strong>
</p>

<p style="margin-bottom:1em"><strong>HealthKart Team</strong></p>
<#include "footer.ftl">
</body>
</html>