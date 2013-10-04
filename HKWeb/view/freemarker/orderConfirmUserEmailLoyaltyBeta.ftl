Order Confirmation for Order ID ${order.gatewayOrderId}
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Order Confirmation</title>
</head>

<body style="margin:0; padding:0; font-family:Arial, Helvetica, sans-serif; background-color:#FFFFFF;">
<table width="530" border="0" align="center" cellpadding="0" cellspacing="0">
<#include "headerBeta.ftl">
    <tbody style="font-size:13px; line-height:1.75em;">


    <tr>
        <td style="padding-top: 35px; padding-bottom: 10px" colspan="2">Hi ${order.user.name}!<br/>
            <br/>
            Thanks for ordering! <br/>
            <br/>

            Your order, dated ${order.payment.paymentDate}, for INR 0, with
            order confirmation number ${order.gatewayOrderId} is being processed. <br/>
        <#if isServiceOrder == true>
            Please carry a printout of this confirmation email to avail the services at any of the centers
            mentioned on the website <br/>
            <#if  (categoryCountInOrder > 1)>
                The following items will be shipped shortly:<br/>
            </#if>
        <#else>
            The following items will be shipped shortly:<br/>
        </#if>

            <br/>
            <table cellpadding="5" cellspacing="0" border="1" style="font-size:12px;">
                <tr>
                    <td width="150"><strong>Item</strong></td>
                    <td width="50"><strong>Quantity</strong></td>
                    <td width="50"><strong>Unit price</strong></td>
                    <td width="50"><strong>Total(Rs.)</strong></td>
                </tr>

            <#list pricingDto.productLineItems as productLineItem>
                <tr>
                    <td>${productLineItem.productVariant.variantName}

                    </td>
                    <td>
                    ${productLineItem.qty}
                    </td>
                    <td>
	                                <span
                                            style="text-decoration: line-through;">${productLineItem.markedPrice}</span>
                        0
                    </td>
                    <td> 0</td>
                </tr>
            </#list>
            </table>

            <br/>

            If you have any questions, you can chat online with our Customer Care or call them at
            0124-4616444 . We will send another email once your order ships.<br/>
            <br/>

        <#--<#include "orderNote.ftl">-->

            Healthy Shopping!<br/>

            HealthKart.com <br/>


        </td>
    </tr>
    </tbody>
<#include "footerBeta.ftl">


</table>
</body>
</html>
