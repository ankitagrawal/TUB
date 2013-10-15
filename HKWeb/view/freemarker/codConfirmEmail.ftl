Cash-on-Delivery Order Id ${order.gatewayOrderId} confirmed.
<html>
<head>
    <title>Cash-on-Delivery Order Id ${order.gatewayOrderId} confirmed.</title>
</head>
<body>
<#--<#include "header.ftl">-->
<p style="margin-bottom:1em">Hi ${order.user.name},</p>

<p style="margin-bottom:1em">
    Thanks for confirming your order. We have received the order and will process the order soon.
    Thanks for your patience.
</p>

<p style="margin-bottom:1em">Here are your order details <strong> ${order.gatewayOrderId}</strong> placed
    on<strong> ${order.payment.createDate?string("MMM dd, yyyy hh:mm:ss aa")} </strong></p>

<div>
    <h3>Order Details</h3>
<#--<h5>No of shipping addresses - ${pricingDto.shippingLineCount}</h5>-->
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
            <#--${productLineItem.qty/pricingDto.shippingLineCount}-->
                ${productLineItem.qty}
            </td>
            <td>
              <#if productLineItem.markedPrice &gt; productLineItem.hkPrice>
                  <span
                          style="text-decoration: line-through;">${productLineItem.markedPrice}</span>
              </#if>
            ${productLineItem.hkPrice}
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
            <td>Discount</td>
            <td>
            ${pricingDto.totalDiscount - pricingDto.shippingDiscount}
            </td>
        </tr>
        <tr>
            <td>Redeemed Reward Points</td>
            <td>
            ${pricingDto.rewardPointTotal}
            </td>
        </tr>
        <tr>
            <td>Shipping Charges</td>
            <td>
            ${pricingDto.shippingSubTotal - pricingDto.shippingDiscount}
            </td>
        </tr>
    <#if order.payment.paymentMode.id = 40>
        <tr>
            <td>COD Charges</td>
            <td>
            ${pricingDto.codSubTotal - pricingDto.codDiscount}
            </td>
        </tr>
    </#if>
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

<#include "orderNote.ftl">

<p style="margin-bottom:1em"><strong>HealthKart.com</strong></p>
</body>
</html>