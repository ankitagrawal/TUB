Your order ${order.gatewayOrderId} has been delivered. Please share some feedback!

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Your order has been delivered...</title>
</head>

<body style="margin:0; padding:0; font-family:Arial, Helvetica, sans-serif; background-color:#FFFFFF;">
<table width="530" border="0" align="center" cellpadding="0" cellspacing="0">
<#include "headerBeta.ftl">
    <tbody style="font-size:13px; line-height:1.75em;">

    <tr>
        <td style="padding-top: 35px;padding-bottom: 15px" colspan="2">Dear ${order.user.name}<br/> <br/>
            Thank you for shopping with HealthKart. We trust that you had a good shopping experience and are on your way
            to the zenith of health.<br/>
            Your Order No.: ${order.gatewayOrderId} has been successfully delivered.<br/><br/>

            We would like to know more about your experience shopping with us, so that we can improve our services. <br>
            While we like positive feedback, we think your criticism can help us correct our course & serve you
            better.<br>
            Feel free to be frank, we're listening.<br/>

            <table style="font-size:12px;" cellpadding="5" cellspacing="0" border="1" RULES=COLS>
                <tr>
                    <td>
                        <table cellpadding="5" cellspacing="5">
                            <tr>
                                <td colspan="10"><strong>How likely is it that you would
                                    recommend HealthKart to a friend or colleague? </strong>
                                </td>
                            </tr>
                        </table>
                        <table cellpadding="5" cellspacing="5" border="1" style="font-size:12px;" RULES=COLS FRAME=BOX>
                            <tr>
                                <td><strong>Not likely</strong></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td></td>
                                <td><strong>Very likely</strong></td>
                            </tr>
                            <tr>
                                <td>
                                    <a href="${feedbackPage}?recommendToFriends=0&baseOrderId=${order}">
                                        <input type="radio" name="recommendToFriends" value="0"/></a> 0
                                </td>
                                <td>
                                    <a href="${feedbackPage}?recommendToFriends=1&baseOrderId=${order}">
                                        <input type="radio" name="recommendToFriends" value="1"/></a> 1
                                </td>
                                <td>
                                    <a href="${feedbackPage}?recommendToFriends=2&baseOrderId=${order}">
                                        <input type="radio" name="recommendToFriends" value="2"/></a> 2
                                </td>
                                <td>
                                    <a href="${feedbackPage}?recommendToFriends=3&baseOrderId=${order}">
                                        <input type="radio" name="recommendToFriends" value="3"/></a> 3
                                </td>
                                <td>
                                    <a href="${feedbackPage}?recommendToFriends=4&baseOrderId=${order}">
                                        <input type="radio" name="recommendToFriends" value="4"/></a> 4
                                </td>
                                <td>
                                    <a href="${feedbackPage}?recommendToFriends=5&baseOrderId=${order}">
                                        <input type="radio" name="recommendToFriends" value="5"/></a> 5
                                </td>
                                <td>
                                    <a href="${feedbackPage}?recommendToFriends=6&baseOrderId=${order}">
                                        <input type="radio" name="recommendToFriends" value="6"/></a> 6
                                </td>
                                <td>
                                    <a href="${feedbackPage}?recommendToFriends=7&baseOrderId=${order}">
                                        <input type="radio" name="recommendToFriends" value="7"/></a> 7
                                </td>
                                <td>
                                    <a href="${feedbackPage}?recommendToFriends=8&baseOrderId=${order}">
                                        <input type="radio" name="recommendToFriends" value="8"/></a> 8
                                </td>
                                <td>
                                    <a href="${feedbackPage}?recommendToFriends=9&baseOrderId=${order}">
                                        <input type="radio" name="recommendToFriends" value="9"/></a> 9
                                </td>
                                <td>
                                    <a href="${feedbackPage}?recommendToFriends=10&baseOrderId=${order}">
                                        <input type="radio" name="recommendToFriends" value="10"/></a> 10
                                </td>
                            </tr>
                        </table>
                    </td>
                </tr>

            </table>

            <br/>

            <div class="line-separator" style="height:1px; background:#717171; border-bottom:1px solid #313030;"></div>

            <br/>Summary of your order: <br/>

            <table style="font-size:12px;" cellpadding="5" cellspacing="0" border="1">
                <tr>
                    <td><strong>Item</strong></td>
                    <td><strong>Quantity</strong></td>
                </tr>
            <#list order.shippingOrders as shippingOrder>
                <#list shippingOrder.lineItems as lineItem>
                    <tr>

                        <td>${lineItem.sku.productVariant.product.name}
                            <#if lineItem.sku.productVariant.variantName??>
                            ${lineItem.sku.productVariant.variantName}
                            </#if>
                            <br/>

                            <#list lineItem.sku.productVariant.productOptions as
                            productOption>
                                <em style="font-size:0.9em; color:#666">${productOption.name} ${productOption.value} </em>
                            </#list>
                        </td>
                        <td>
                        ${lineItem.qty}
                        </td>
                    </tr>
                </#list>
            </#list>
            </table>

            <br/>
            If you have any problems or concerns about your recent purchase, please get in touch with our customer
            service as soon as possible and we will do everything we can to help.
            <br/> <br/>
            Reach our Customer Care at 0124-4616444.
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
