${fromName} likes shopping at HealthKart and has gifted you an additional 5% off voucher to  try it out
<html>
<head>
  <title>${fromName} would like you to try HealthKart.com and has gifted you an additional 5% off to buy anything you like!!!</title>
</head>
<body>
<#include "header.ftl">
<h4>${fromName} would like you to try HealthKart.com and get an additional 5% off to buy anything you
  like!!!</h4>

<p>
  HealthKart is India's premier e-health store which provides authentic health & personal care products at the best
  prices in the market.
  ${fromName} thinks that you would like it, and has sent you a special invitation offer of an additional 5% off on your first
  purchase (minimum purchase of Rs. 500). This is HealthKart's best offer for new customers.
</p>
<#if customMessage??>
<p style="background-color:beige;">
  ${fromName} has included the following message for you :<br/>
  ${customMessage}
</p>
</#if>
<p>
  Here's how you can redeem this offer -
</p>

<p>
  1. Signup by clicking <a href="${signupLink}">this link</a>
  <br>OR<br/>
  2. Use the coupon <strong>${couponCode}</strong> on the shopping cart<br/><br/>
    <span style="font-size: .95em;">
        (When you make a purchase using this discount,
        ${fromName} will also get a credit of Rs. 100)
    </span>
</p>

<p>
 HealthKart products are 100% authentic, at the best prices in the market and are home delivered free of cost. Also, you may use the product counseling service, so feel free to call them or do a live chat session if you need any clarifications.
 Here are some of the awesome products you can buy at HealthKart -
  <ul style="list-style:decimal">
    <li>Nutrition Products (Body Building, Weight loss, Well being, Sexual performance)</li>
    <li>Beauty Products (Creams, Cleansers, make-up)</li>
    <li>Contact Lenses</li>
    <li>Health Devices (BP monitors, Thermometers, Nebulizers)</li>
    <li>Diabetes Supplies (Strips, Meters, Diabetic Food, foot care products)</li>
    <li>Feminine Care (Tampons, Sanitary pads, Ovulation kits)</li>
    <li>Baby & Mom Care (Diapers, Feeder bottles, Breast pumps)</li>
    <li>Elderly care (Adult Diapers, rehabilitation aids)</li>
  </ul>
  There is lots more - you would surely find some great products you need!
</p>

<p>
  Thanks for listening!<br/>
  (sent by HealthKart on behalf of ${fromName} - ${fromEmail})<br/><br/>
</p>

<p style="font-size:.9em; color: gray;">
  If you believe this is spam please report to us at info@healthkart.com<br/><br/>
  To stop receiving any such email in the future, <a href="${unsubscribeLink}">click here to unsubscribe</a><br/>
  If clicking on the link is not working, try copy pasting the link into your url bar - <br/>${unsubscribeLink}
</p>

<#include "footer.ftl">
</body>
</html>

