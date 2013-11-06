You requested to be notified when ${product.name} was back in stock
<html>
<head>
    <title>Greetings from HealthKart.com</title>
</head>
<body>

<p style="margin-bottom:1em">Hi ${notifiedUser.name},</p>

<p style="font-size:1.2em;">You visited us on ${notifiedUser.createdDate} and requested to be notified when the ${product.name} was back in stock.</p>

<p style="margin-bottom:1em">
  Some or all variants of ${product.name} have arrived back in stock recently. We thought we'd update you on the same. Here is the link to the
  product page - <a href="www.healthkart.com/product/${product.slug}/${product.id}?utm_source=notifyme&utm_medium=email&utm_campaign=${emailCampaign.name}">www.healthkart.com/product/${product.slug}/${product.id}</a>. You might want to check the latest prices
  and place your order if needed.
</p>

<p style="margin-bottom:1em">For any assistance, simply give us a call on 0124-4616444 or write to us on info@healthkart.com.</p>

<p style="margin-bottom:1em">Happy Shopping!</p>

<p style="margin-bottom:1em"><strong>HealthKart.com</strong></p>

</body>
</html>