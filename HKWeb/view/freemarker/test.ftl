Congratulations ${user.name}. As a prized customer of HealthKart, you have won a Coupon for 5% off!
<html>
<head>
<title>Welcome to HealthKart.com</title>
</head>
<body>
<#include "header.ftl">

<p style="margin-bottom:1em">Hi ${user.name},</p>

<p style="margin-bottom:1em">
  <#if product??>
Also, since you're a cherished customer, we've decided to give you a couple of free products like:
- ${product.name}
- ${product_image_url_medium}

Please use this product. It's very useful. Here's a short description: ${product.overview}.
 </#if>
Also, you last logged in on ${user.lastLoginDate}. Now, can you please login soon and get buy something?

</p>
<p style="margin-bottom:1em">
Many thanks from Team HealthKart for shopping with us.
</p>

<p style="margin-bottom:1em">Happy Shopping!</p>

<p style="margin-bottom:1em"><strong>HealthKart.com</strong></p>
<#include "footer.ftl">
</body>
</html>
