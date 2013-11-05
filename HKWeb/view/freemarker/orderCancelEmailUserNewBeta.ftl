Order Cancellation for Order ID ${order.gatewayOrderId}
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Your order has been cancelled.</title>
</head>

<body style="margin:0; padding:0; font-family:Arial, Helvetica, sans-serif; background-color:#FFFFFF;">
<table width="530" border="0" align="center" cellpadding="0" cellspacing="0">
<#include "headerBeta.ftl">
    <tbody style="font-size:13px; line-height:1.75em;">

    <tr>
        <td style="padding-top: 15px" colspan="2"><a href="http://www.healthkart.com"><img
                src="http://img.healthkart.com/email/order_cancel_user_new/main_banner.jpg" width="500"
                height="148" alt="Your order has been cancelled." border="0"/></a></td>
    </tr>


    <tr>
        <td style="padding-top: 35px; padding-bottom: 10px" colspan="2">Hi ${order.user.name}! <br/>
            <br/>
            Your order, dated ${order.payment.paymentDate}, for INR ${pricingDto.grandTotalPayable}, with order
            confirmation number ${order.gatewayOrderId} has been canceled. <br/>
            <br/>

            You had ordered for:<br/>
            <table style="font-size:12px;" cellpadding="5" cellspacing="0" border="1">
                <tr>
                    <td><strong>Item</strong></td>
                    <td><strong>Quantity</strong></td>
                    <td><strong>Unit Price</strong></td>
                    <td><strong>Total (Rs.)</strong></td>
                </tr>

            <#list pricingDto.productLineItems as productLineItem>
                <tr>
                    <td><#if productLineItem.productVariant.variantNameFromHKEdge??>
                    ${productLineItem.productVariant.variantNameFromHKEdge}
                    <#else>
                    ${productLineItem.productVariant.product.name}
                    </#if>
                      <br/>
                      <em style="font-size:0.9em; color:#666"><#list productLineItem.productVariant.productOptions as productOption>
                    ${productOption.name} ${productOption.value}
                    </#list></em>

                    </td>
                    <td>
                    ${productLineItem.qty/pricingDto.shippingLineCount}
                    </td>
                    <td><span
                            style="text-decoration: line-through;">${productLineItem.markedPrice}</span> ${productLineItem.hkPrice}
                    </td>
                    <td> ${productLineItem.hkPrice * productLineItem.qty} </td>
                </tr>
            </#list>
            </table>
            <br/>
            <br/>

            If you have any questions, you can call our Customer Care at 0124-4616444.<br/>
            <br/>


            Healthy Shopping!<br/>

            HealthKart.com <br/>

        </td>
    </tr>
    </tbody>
<#include "footerBeta.ftl">
</table>
</body>
</html>
