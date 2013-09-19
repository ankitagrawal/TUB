Your order ${order.baseOrder.gatewayOrderId} has been shipped.

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Just waiting eagerly to come home...</title>
</head>

<body style="margin:0; padding:0; font-family:Arial, Helvetica, sans-serif; background-color:#FFFFFF;">
<table width="530" border="0" align="center" cellpadding="0" cellspacing="0">
<#include "headerBeta.ftl">
    <tbody style="font-size:13px; line-height:1.75em;">
    <tr>
        <td colspan="2"><a href="http://www.healthkart.com"><img
                src="http://img.healthkart.com/email/order_shipped_email_new/main_banner.jpg"
                width="500"
                height="148" alt="Just waiting eagerly to come home..." border="0"/></a>
        </td>
    </tr>

    <tr>
        <td colspan="2">

            Following items of your order ${order.baseOrder.gatewayOrderId}, placed
            on ${order.baseOrder.payment.paymentDate} have been dispatched.
            This completes the shipping of your order in its totality. Here are
            the details:<br/>
            <br/>
        </td>
    </tr>

    <tr>
        <td style="padding-top: 35px;padding-bottom: 10px">Hi ${order.baseOrder.address.name}!<br/>

            <table style="font-size:12px;" cellpadding="5" cellspacing="0" border="1">
                <tr>
                    <td><strong>Item</strong></td>
                    <td><strong>Quantity</strong></td>
                    <td><strong>TrackingId</strong></td>
                    <td><strong>Courier</strong></td>
                    <td><strong>Est. Delivery Date</strong></td>
                </tr>
            <#list order.lineItems as lineItem>
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
                    <td>
                    ${order.shipment.awb.awbNumber}
                        <#if order.shipment.trackLink??>
                            <h5><a href="${order.shipment.trackLink}" target="_blank"> Track This </a></h5>
                        </#if>
                    </td>
                    <td>
                    ${order.shipment.awb.courier.name}
                    </td>
                    <td>
                    ${targetDeliverDate}
                    </td>
                </tr>
            </#list>
            </table>

        </td>
    </tr>

    <#if shippingOrderAlreadySentList??>
        <#list shippingOrderAlreadySentList as shippingOrder>
        <tr>
    <td colspan="2">
        <br>
        <#if shippingOrder.shipment.shipDate??>
            Following items were dispatched earlier on ${shippingOrder.shipment.shipDate?date} via a different
            shipment <br><br>
        <#else >
            Following items were dispatched earlier via a different shipment <br><br>
        </#if>

    </td>
</tr>

        <tr>

            <td colspan="2" valign="top">

                <table style="font-size:12px;" cellpadding="5" cellspacing="0" border="1">
                    <tr>
                        <td><strong>Item</strong></td>
                        <td><strong>Quantity</strong></td>
                    </tr>

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

                </table>

            </td>
        </tr>

        </#list>
    </#if>
    <tr>
        <td colspan="2" valign="top">
            <br/>
            <br/>

            <strong>Note: Our products are shipped in sealed bags & boxes and we request you not to accept
                any tampered packets.</strong>
            <br/>
            <br/>
            In case you have any queries, feel free to chat with our Customer Care or call them at
            0124-4616444. <br/>
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
