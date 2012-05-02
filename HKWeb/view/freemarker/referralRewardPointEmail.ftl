Congratulations ${rewardPoint.user.firstName}. You have been credited ${rewardPoint.value} referral reward points.
<html>
<head>
    <title>Welcome to HealthKart.com</title>
</head>
<body>
<#include "header.ftl">

<p style="margin-bottom:1em">Hi ${rewardPoint.user.firstName},</p>

<p style="margin-bottom:1em">
  <i>${rewardPoint.referredUser.name}</i> has placed an order through your referral. Rs. ${rewardPoint.value} worth reward points have been credited to your account. These are valid till ${rewardPointTxn.expiryDate}. Enjoy :)
</p>
<p style="margin-bottom:1em">
  Many thanks from Team HealthKart for spreading the word.
</p>

<p style="margin-bottom:1em">Happy Shopping!</p>

<p style="margin-bottom:1em"><strong>HealthKart.com</strong></p>
<#include "footer.ftl">
</body>
</html>