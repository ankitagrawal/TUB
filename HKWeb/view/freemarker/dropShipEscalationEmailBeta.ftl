Shipping Order -: ${shippingOrder.id?c} has been escalated to DropShip Queue
<html>
<head>
    <title>Welcome to HealthKart.com</title>
</head>
<body style="margin:0; padding:0; font-family:Arial, Helvetica, sans-serif; background-color:#FFFFFF;">
<table width="530" border="0" align="center" cellpadding="0" cellspacing="0">
<#include "headerBeta.ftl">

    <tbody style="font-size:13px; line-height:1.75em;">
    <tr>
        <td colspan="2" style="padding-top:15px;margin-bottom:1.2em">Attention!!</td>
    </tr>
    <tr>
        <td colspan="2" style="margin-bottom:1em">
            Please note that Shipping Order : ${shippingOrder.id?c} has been escalated to DropShip Queue.

        </td>
    </tr>


    <tr>
        <td colspan="2" style="padding-top:10px;padding-bottom:10px;margin-bottom:1em"><strong>HealthKart Team</strong>
        </td>
    </tr>
    </tbody>
<#include "footerBeta.ftl">
</table>
</body>
</html>