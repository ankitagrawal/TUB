Error creating shipment
<html>
<head>
  <title>Welcome to HealthKart.com</title>
</head>
<body>
<#include "header.ftl">

<p style="margin-bottom:1.2em">Attention!!</p>

<p style="margin-bottom:1em">
  ${message}
  Shipping Order Id = ${shippingOrder.gatewayOrderId} <br/>
</p>


<p style="margin-bottom:1em"><strong>HealthKart Team</strong></p>
<#include "footer.ftl">
</body>
</html>