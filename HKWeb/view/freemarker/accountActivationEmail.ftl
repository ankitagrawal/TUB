Activate your HealthKart.com account
<html>
<head>
    <title>Account Activation Link</title>
</head>
<body>
<#include "header.ftl">

<p style="margin-bottom:1em">Hi ${user.name},</p>

<p style="margin-bottom:1em">Please click on the following link to to verify your
    email and activate your account.</p>
<p style="margin-bottom:1em"><a href="${activationLink}">Activate account &rarr;</a></p>
<p style="margin-bottom:1em">If clicking on the link does not work, cut and paste the following URL in your browser:</p>
<p style="margin-bottom:1em">${activationLink}</p>


<p style="margin-bottom:1em">Happy Shopping!</p>

<p style="margin-bottom:1em"><strong>HealthKart.com</strong></p>
<#include "footer.ftl">
</body>
</html>
