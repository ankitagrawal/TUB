Order Id ${order.gatewayOrderId} received. Cash on Delivery confirmation pending.
<html>
<head>
    <title>Order Id ${order.gatewayOrderId} received. Cash on Delivery confirmation pending.</title>
</head>
<body style="margin:0; padding:0; font-family:Arial, Helvetica, sans-serif; background-color:#FFFFFF;">
<#--<#include "header.ftl">-->
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
<#include "headerBeta.ftl">
    <tbody>
    <tr>
        <td colspan="2" style="margin-bottom:1em; padding-top: 15px; padding-bottom: 15px">Hi ${order.user.name}!,</td>
    </tr>
    <tr>
        <td colspan="2" style="margin-bottom:1em">
            Thanks for placing an order with us. We have received the order and will give you a call shortly to confirm
            it.
            Please note that COD <br/>orders will not be confirmed and shipped from our end until we manually confirm the
            order
            on
            the phone number provided.
            Thanks<br/> for your patience.
        </td>
    </tr>
    <tr>
        <td colspan="2" style="padding-top:10px;margin-bottom:1em; padding-bottom: 10px">Here are your order details for the order
            <strong> ${order.gatewayOrderId}</strong>
            placed
            on<strong> ${order.payment.createDate?string("MMM dd, yyyy hh:mm:ss aa")} </strong></td>
    </tr>

    <tr>
        <td colspan="2" style="margin-bottom: 1em">
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
            <td style="margin-bottom: 1em">
            ${productLineItem.productVariant.name}
                    </#list>
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
        </td>
    </tr>


    <tr>
        <td colspan="2" style="padding-top:15px;margin-bottom: 1em">
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
        </td>
    </tr>

    <tr>
        <td colspan="2" style="padding-top:15px; margin-bottom: 1em">
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
        </td>
    </tr>
    <tr>
        <td colspan="2" style="padding-top:15px;margin-bottom: 1em">
            <h3>Shipping Address & Customer details</h3></td>
    </tr>
    <tr>
        <td colspan="2" style="margin-bottom:1em">
        ${order.address.name}<br/>
        ${order.address.line1}<br/>
        <#if order.address.line2??>
        ${order.address.line2}<br/>
        </#if>
        ${order.address.city} - ${order.address.pincode.pincode}<br/>
        ${order.address.state} (India)<br/>
            Ph: ${order.address.phone}<br/>
        </td>
    </tr>
    <tr>
        <td colspan="2" style="margin-bottom:1em">We will send you an email as soon as your order is shipped.</td>
    </tr>

    <#--<#include "orderNote.ftl">-->
    <tr>
        <td style="padding-bottom:10px;padding-top: 10px; margin-bottom:1em"><strong>HealthKart.com</strong></td>
    </tr>
    </tbody>
<#include "footerBeta.ftl">
</table>

</body>
</html>