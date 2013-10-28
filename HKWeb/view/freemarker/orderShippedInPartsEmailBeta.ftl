Your order ${order.gatewayOrderId} has been fully shipped.
<html>
<head>
    <title>Your order ${order.gatewayOrderId} has been fully shipped.</title>
</head>
<body>
<#include "header.ftl">

<p style="margin-bottom:1em">Some items in your order ${order.gatewayOrderId} have been shipped and should reach you
    shortly.</p>

<h4>Shipping details for order ${order.gatewayOrderId} placed on ${order.payment.paymentDate}</h4>


<div>
    <h5 style="padding:0; margin:0; margin-bottom:5px;">Shipping Address</h5>

    <p style="padding:0; margin:0; margin-bottom:5px;">${order.address.name}<br/>
        ${order.address.line1},
        <#if order.address.line2??>
        ${order.address.line2},
        </#if>
        ${order.address.city} - ${order.address.pincode.pincode},
        ${order.address.state},
        Ph: ${order.address.phone}</p>
    <h5 style="padding:0; margin:0; margin-bottom:5px;">Shipment Details</h5>
    <table style="font-size:12px;" cellpadding="5" cellspacing="0" border="1">
        <#list order.productLineItems as lineItem>
        <tr>
            <td>${lineItem.productVariant.product.name}<br/>
                <#list lineItem.productVariant.productOptions as
                                 productOption>
                <em style="font-size:0.9em; color:#666">${productOption.name} ${productOption.value} </em>
                </#list>
            </td>
            <td>
                ${lineItem.qty}
            </td>
            <td>
                ${lineItem.lineItemStatus.name}
            </td>
            <td>
                <#if lineItem.trackingId??>
                Shipped on ${lineItem.formattedShipDate} by ${lineItem.courier.name} -
                ${lineItem.trackingId}
                <#if lineItem.trackLink??>
                <h5><a href="${lineItem.trackLink}" target="_blank"> Track This </a></h5>
                </#if>
                </#if>
            </td>
        </tr>
        </#list>
    </table>
</div>
<p style="margin-bottom:1em">You can <strong><a href="${invoiceLink}" target="_blank"> view the invoice for your order
    here.</a></strong></p>

<strong>Note: Our products are shipped in sealed bags & boxes and we request you not to accept
    any tampered packets.</strong>
<br/>
<br/>

<p style="margin-bottom:1em">Happy Shopping!</p>

<p style="margin-bottom:1em"><strong>HealthKart.com</strong></p>
<#include "footer.ftl">
</body>
</html>



