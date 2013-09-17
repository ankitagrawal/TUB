Activate your HealthKart.com account
<html>
<head>
    <title>Account Activation Link</title>
</head>
<body style="margin:0; padding:0; font-family:Arial, Helvetica, sans-serif; background-color:#FFFFFF;">
<table width="530" border="0" align="center" cellpadding="0" cellspacing="0">
<#include "headerBeta.ftl">
    <tbody style="font-size:13px; line-height:1.75em;">
    <tr>
        <td style="margin-top: 1em">Hi ${user.name},</td>
    </tr>

    <tr>
        <td style="margin-top: 1em"> Please click on the following link to to verify your
            email and activate your account.
        </td>
    </tr>
    <tr>
        <td style="margin-bottom:1em"><a href="${activationLink}">Activate account &rarr;</a></td>
    </tr>
    <tr>
        <td style="margin-bottom:1em">If clicking on the link does not work, cut and paste the following URL in your
            browser:
        </td>
    </tr>
    <tr>
        <td style="margin-bottom:1em">${activationLink}</td>
    </tr>

    <tr>
        <td style="margin-bottom:1em">Happy Shopping!</td>
    </tr>
    <tr>
        <td style="margin-bottom:1em"><strong>HealthKart.com</strong></td>
    </tr>
    </tbody>
<#include "footerBeta.ftl">
</table>
</body>
</html>
