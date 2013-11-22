Congratulations ${rewardPoint.user.firstName}. You have been credited ${rewardPoint.value} cash back reward points.
<html>
<head>
    <title>Welcome to HealthKart.com</title>
</head>
<body style="margin:0; padding:0; font-family:Arial, Helvetica, sans-serif; background-color:#FFFFFF;">
<table width="530" border="0" align="center" cellpadding="0" cellspacing="0">

<#include "headerBeta.ftl">
    <tbody style="font-size:13px; line-height:1.75em;">
    <tr>
        <td style="padding-top:10px;margin-bottom:1em" colspan="2">Hi ${rewardPoint.user.firstName},</td>
    </tr>

    <tr>
        <td style="padding-top:10px;margin-bottom:1em" colspan="2">
            Rs. ${rewardPoint.value} worth reward points have been credited to your account as cash back points. These
            are
            valid till ${rewardPointTxn.expiryDate}. Enjoy :)
        </td>
    </tr>
    <tr>
        <td style="padding-top:10px;margin-bottom:1em" colspan="2">
            Many thanks from Team HealthKart for shopping with us.
        </td>
    </tr>
    <tr>
        <td style="padding-top:10px;margin-bottom:1em" colspan="2">Happy Shopping!</td>
    </tr>
    <tr>
        <td style="padding-top:10px;margin-bottom:1em" colspan="2"><strong>HealthKart.com</strong></td>
    </tr>
    </tbody>
<#include "footerBeta.ftl">
</table>
</body>
</html>