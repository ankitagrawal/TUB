Awbs are finished for courier : ${courier.name}
<html>
<head>
  <title>Welcome to HealthKart.com</title>
</head>
<body>
<#include "header.ftl">

<p style="margin-bottom:1.2em">Attention!!</p>

<p style="margin-bottom:1em">
  Please replenish awb's for courier: ${courier.name}.<br/>
  For warehouse = ${shippingOrder.warehouse.identifier} <br/>
  For orders delivered by COD = ${cod} <br/>
  Shipping Order Id = ${shippingOrder.gatewayOrderId} <br/>
</p>


<p style="margin-bottom:1em"><strong>HealthKart Team</strong></p>
<#include "footer.ftl">
</body>
</html>