Order Cancellation for Order ID ${order.gatewayOrderId}
<html>
<head>
    <title>Order Cancellation for Order ID ${order.gatewayOrderId}</title>
</head>
<body style="margin:0; padding:0; font-family:Arial, Helvetica, sans-serif; background-color:#FFFFFF;">
<table width="530" border="0" align="center" cellpadding="0" cellspacing="0">
<#include "headerBeta.ftl">

    <tbody style="font-size:13px; line-height:1.75em;">
    <tr>
        <td style="padding-top:10px;margin-bottom:1em" colspan="2">Hi ${order.user.name}!,</td>
    </tr>
    <tr>
        <td style="padding-top:10px;margin-bottom:1em" colspan="2">We have cancelled your order
            id ${order.gatewayOrderId}
            .
        </td>
    </tr>
    <tr>
        <td style="margin-bottom:1em" colspan="2">In case of any confusion, please feel free to contact us about this on
            our
            customer care
            email - info@healthkart.com
            or our customer care phone on 0124-4616444
        </td>
    </tr>
    <tr>
        <td style="padding-top: 15px" colspan="2">
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
                    <td><#if lineItem.productVariant.variantName??>
                    ${lineItem.productVariant.variantName}
                    <#else>
                    ${lineItem.productVariant.product.name}
                    </#if>
                      <br/>
                      <em style="font-size:0.9em; color:#666"><#list lineItem.productVariant.productOptions as productOption>
                    ${productOption.name} ${productOption.value}
                    </#list></em>

                    </td>
                    <td>
                    ${productLineItem.qty/pricingDto.shippingLineCount}
                    </td>
                    <td><span style="text-decoration: line-through;">${productLineItem.markedPrice}</span> 0</td>
                    <td> 0</td>
                </tr>
            </#list>
            </table>
        <#--<#if pricingDto.totalDiscount &gt; 0 >-->
        <#--<h3>Discounts </h3>-->
        <#--</#if>-->
        <#--<table cellpadding="5" cellspacing="0" border="1" style="font-size:12px;">-->
        <#--<#list pricingDto.productLineItems as productLineItem>-->
        <#--<#if productLineItem.discountOnHkPrice &gt; 0>-->
        <#--<tr>-->
        <#--<td width="150">${productLineItem.productVariant.product.name}<br/>-->
        <#--<em style="font-size:0.9em; color:#666"><#list productLineItem.productVariant.productOptions as productOption>-->
        <#--${productOption.name} ${productOption.value}-->
        <#--</#list></em>-->
        <#--</td>-->
        <#--<td width="50"></td>-->
        <#--<td width="50"></td>-->
        <#--<td width="50">${productLineItem.discountOnHkPrice}</td>-->
        <#--</tr>-->
        <#--</#if>-->
        <#--</#list>-->
        <#--<#if pricingDto.orderLevelDiscount &gt; 0>-->
        <#--<tr>-->
        <#--<td width="150">Order Discount</td>-->
        <#--<td width="50"></td>-->
        <#--<td width="50"></td>-->
        <#--<td width="50">${-pricingDto.orderLevelDiscount}</td>-->
        <#--</tr>-->
        <#--</#if>-->
        <#--<#if pricingDto.shippingDiscount &gt; 0>-->
        <#--<tr>-->
        <#--<td width="150">Shipping Discount</td>-->
        <#--<td width="50"></td>-->
        <#--<td width="50"></td>-->
        <#--<td width="50">${pricingDto.shippingDiscount}</td>-->
        <#--</tr>-->
        <#--</#if>-->
        <#--</table>-->
        </td>
    </tr>
    <tr>
        <td style="padding-top: 15px;margin-bottom: 1em" colspan="2">
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
                    <#if pricingDto.shippingLineCount &gt; 1>
                        (${pricingDto.shippingLineCount} addresses)
                    </#if>
                    </td>
                </tr>
                <tr>
                    <td>Discount</td>
                    <td>
                    ${pricingDto.totalDiscount - pricingDto.shippingDiscount}
                    <#if pricingDto.shippingLineCount &gt; 1>
                        (${pricingDto.shippingLineCount} addresses)
                    </#if>
                    </td>
                </tr>
                <tr>
                    <td><strong>Grand Total</strong></td>
                    <td>
                        <strong>0</strong>
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td style="padding-top: 15px;margin-bottom: 1em" colspan="2">
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
        <td style="padding-top: 15px ;margin-bottom: 1em" colspan="2">
            <h3>Shipping Address & Customer details</h3>
        </td>
    </tr>
    <tr>
        <td style="padding-top:15px;margin-bottom:1em" colspan="2">
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
        <td style="margin-bottom:1em ;padding-top: 15px;padding-bottom: 10px;" colspan="2"><strong>HealthKart.com</strong></td>
    </tr>
    </tbody>
<#include "footerBeta.ftl">
</table>
</body>
</html>