Hi ${user.name}
<html>
<head>
    <title>You requested to be notified when the _productName_ was back in stock</title>
</head>
<body>
<#include "header.ftl">

<p style="margin-bottom:1em">Hi ${user.name},</p>

<p style="font-size:1.2em;">You visited us on _notificationCreateDate_ and requested to be notified when the _productName_ was back in stock.</p>

<p style="margin-bottom:1em">
  The variant of _productName_ that you were looking for has arrived back in stock recently. We thought we'd update you on the same. Here is the link to the
  product page - <a href="_productLink_?attach utm codes">_productLink_? attach utm codes</a>. You might want to check the latest prices
  and place your order if needed.
</p>

<p style="margin-bottom:1em">For any assistance, simply give us a call on 0124-4616444 or write to us on info@healthkart.com.</p>

<p style="margin-bottom:1em">Happy Shopping!</p>

<p style="margin-bottom:1em"><strong>HealthKart.com</strong></p>
<#include "footer.ftl">
</body>
</html>