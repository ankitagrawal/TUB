Reset Password Instructions - HealthKart.com
<html>
<head>
  <title>Reset Password Instructions - HealthKart.com</title>
</head>
<body>
<#include "header.ftl">
<p>Hi ${user.name},</p>

<p style="margin-bottom:1em">Can't remember your password?</p>

<p>It happens to many of us :)</p>

<p>Your Username (Same as your Email Id): <a href="#">${user.email}</a></p>

<p>Please click the below link to set a new password: </p>

<p></p><a href="${link}" target="_blank">${link}</a></p>

<p>This will ask for new password which you can then use to login.</p>

<p>Note: If you are not able to click on the link, you can paste the above address into your browser.</p>

<p>Happy Shopping!</p>

<p><strong>HealthKart.com</strong></p>
<#include "footer.ftl">
</body>
</html>