Hi ${user.name}, welcome to HealthKart.com
<html>
<head>
    <title>Welcome to HealthKart.com</title>
</head>
<body>
<#include "header.ftl">

<p style="margin-bottom:1em">Hi ${user.name},</p>

<p style="font-size:1.2em;">Thank you for registering with HealthKart.com.</p>

<p style="margin-bottom:1em">Please click on the following link to verify your
    email and activate your account.</p>

<p style="margin-bottom:1em"><a href="${activationLink}">Activate account &rarr;</a></p>
<p style="margin-bottom:1em">If clicking on the link does not work, cut and paste the following URL in your browser:</p>
<p style="margin-bottom:1em">${activationLink}</p>

<h2 style="text-transform:uppercase; font-weight:normal; font-size:1.3em; color:#666;">Login Details</h2>

<p style="margin-bottom:1em">To login to your HealthKart account, use the following details</p>

<p style="margin-bottom:1em"><strong>Username</strong> ${user.email}</p>

<p style="margin-bottom:1em"><strong>Password</strong> ${user.password}</p>

<p style="margin-bottom:1em">You can login to see your order history, track previous orders and see the status of currently placed orders</p>

<p style="margin-bottom:1em">Happy Shopping!</p>

<p style="margin-bottom:1em"><strong>HealthKart.com</strong></p>
<#include "footer.ftl">
</body>
</html>