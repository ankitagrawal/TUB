Unsuccessful Order!
<html>
<head>
    <title>Unsuccessful Order!</title>
</head>
<body style="margin:0; padding:0; font-family:Arial, Helvetica, sans-serif; background-color:#FFFFFF;">
<table width="530" border="0" align="center" cellpadding="0" cellspacing="0">
<#include "headerBeta.ftl">
    <tbody style="font-size:13px; line-height:1.75em;">

    <tr>
        <td style="margin-bottom:1em; padding-top: " colspan="2">Hi ${user.name},</td>
    </tr>

    <tr>
        <td style="margin-bottom:1em" colspan="2">
            We saw that you tried to place an order, order id being ${gatewayOrderId} on our website but did not
            complete
            the process...
            Is everything alright?
        </td>
    </tr>
    <tr>
        <td style="margin-bottom:1em" colspan="2">
            If this was an oversight, you may disregard this email. Otherwise, we would be happy to answer any questions
            that you may have about the ordering process.
        </td>
    </tr>
    <tr>
        <td style="margin-bottom:1em" colspan="2">
            Feel free to mail us info@healthkart.com or call us at 0124-4616444 (24x7)
        </td>
    </tr>
    <tr>
        <td style="margin-bottom:1em" colspan="2">
            Remember, your satisfaction is our first priority and it is our pleasure to serve you.
        </td>
    </tr>
    <tr>
        <td style="margin-bottom:1em" colspan="2">Happy Shopping!</td>
    </tr>
    <tr>
        <td style="margin-bottom:1em" colspan="2"><strong>HealthKart.com</strong></td>
    </tr>
    </tbody>
<#include "footerBeta.ftl">
</table>
</body>
</html>