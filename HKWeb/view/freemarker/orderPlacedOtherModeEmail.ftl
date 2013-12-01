Order Id ${order.gatewayOrderId} received. ${order.payment.paymentMode.name} confirmation pending.
<html>
<head>
    <title>Order Id ${order.gatewayOrderId} received. ${order.payment.paymentMode.name} confirmation pending.</title>
</head>
<body>
<#include "header.ftl">
<p style="margin-bottom:1em">Hi ${order.user.name}!,</p>

<p style="margin-bottom:1em">
    Thanks for placing an order with us. We have received the order and are waiting for the payment via
    <strong>${order.payment.paymentMode.name}</strong>.
    Please note that <strong>${order.payment.paymentMode.name}</strong> orders will not be confirmed and shipped from
    our end until we receive the payment.
</p>

<p>
    If we do not receive the payment in our bank account within the next 3-4 business days, we will assume that you do
    not want to proceed with the order,
    and we will cancel the order from the system. In case of any legitimate delays, please place the order again.
</p>

<p>
    Our bank account details are as follows :<br/>
    Account name : Aquamarine HealthCare Pvt. Ltd.<br/>
    Branch : ICICI Bank, Dwarka Sector-5 Branch, New Delhi<br/>
    Current Account no. : 025005003746<br/>
    RTGS/NEFT/IFSC Code : ICIC0000250
</p>

<p style="margin-bottom:1em">Here are your order details for the order <strong> ${order.gatewayOrderId}</strong> placed
    on<strong> ${order.payment.createDate?string("MMM dd, yyyy hh:mm:ss aa")} </strong></p>

<div>
    <h3>Order Details</h3>
    <h5>No of shipping addresses - ${pricingDto.shippingLineCount}</h5>
    <table cellpadding="5" cellspacing="0" border="1" style="font-size:12px;">
        <tr>
            <td width="150"><strong>Item</strong></td>
            <td width="50"><strong>Quantity</strong></td>
            <td width="50"><strong>Unit price</strong></td>
            <td width="50"><strong>Total(Rs.)</strong></td>
        </tr>

        <#list pricingDto.productLineItems as productLineItem>
        <tr>
            <td>${productLineItem.productVariant.product.name}
                <#if productLineItem.productVariant.variantName??>
                     ${productLineItem.productVariant.variantName}
                </#if>
                <br/>
                <em style="font-size:0.9em; color:#666"><#list productLineItem.productVariant.productOptions as productOption>
                    ${productOption.name} ${productOption.value}
                    </#list></em>
            </td>
            <td>
                ${productLineItem.qty}
            </td>
            <td><span
                    style="text-decoration: line-through;">${productLineItem.markedPrice}</span> ${productLineItem.hkPrice}
            </td>
            <td> ${productLineItem.hkPrice * productLineItem.qty} </td>
        </tr>
        </#list>
    </table>
</div>

<div>
    <h3>Order Summary</h3>
    <table cellpadding="5" cellspacing="0" border="1" style="font-size:12px;">
        <tr>
            <td>Item Total</td>
            <td>
                ${pricingDto.productsHkSubTotal}
            </td>
        </tr>
        <tr>
            <td>Shipping</td>
            <td>
                ${pricingDto.shippingSubTotal - pricingDto.shippingDiscount}
            </td>
        </tr>
        <tr>
            <td>Discount</td>
            <td>
                ${pricingDto.totalDiscount - pricingDto.shippingDiscount}
            </td>
        </tr>
        <tr>
            <td><strong>Grand Total</strong></td>
            <td>
                <strong>${pricingDto.grandTotalPayable}</strong>
            </td>
        </tr>
    </table>
</div>

<div>
    <h3>Other Details</h3>
    <table cellpadding="5" cellspacing="0" border="1" style="font-size:12px;">
        <tr>
            <td>Payment mode</td>
            <td>
                ${order.payment.paymentMode.name}
            </td>
        </tr>
        <#if order.payment.contactName??>
        <tr>
            <td>Contact Name</td>
            <td>
                ${order.payment.contactName}
            </td>
        </tr>
        </#if>
        <#if order.payment.contactNumber??>
        <tr>
            <td>Contact Number</td>
            <td>
                ${order.payment.contactNumber}
            </td>
        </tr>
        </#if>
    </table>
</div>

<h3>Shipping Address & Customer details</h3>

<p style="margin-bottom:1em">
    ${order.address.name}<br/>
    ${order.address.line1}<br/>
    <#if order.address.line2??>
    ${order.address.line2}<br/>
    </#if>
    ${order.address.city} - ${order.address.pincode.pincode}<br/>
    ${order.address.state} (India)<br/>
    Ph: ${order.address.phone}<br/>
</p>

<p style="margin-bottom:1em">We will send you an email as soon as your order is shipped.</p>

<p style="margin-bottom:1em"><strong>HealthKart.com</strong></p>
<#include "footer.ftl">
</body>
</html>