Your Subscription with Id ${subscription.id} is placed.
<html>
<head>
    <title>Your Subscription with Id ${subscription.id} is placed. </title>
</head>
<body>
<p style="margin-bottom:1em">Hi ${subscription.user.name}!,</p>

<p style="margin-bottom:1em">
    Thanks for subscribing with us. We will give you a call whenever your shipment is due and then process your subscription.
    In case you need a change in the address or the next shipment date, please call our customer care at 0124-4616444. or just drop us an email at info@healthkart.com.
</p>
<p style="margin-bottom:1em">Here are your subscription details for the subscription <strong> ${subscription.id}</strong> placed on<strong> ${subscription.baseOrder.payment.createDate?string("MMM dd, yyyy hh:mm:ss aa")} </strong></p>

<div>
    <h3>Subscription Details</h3>
    <table cellpadding="5" cellspacing="0" border="1" style="font-size:12px;">
        <tr>
            <td width="150"><strong>Item</strong></td>
            <td width="50"><strong>Quantity</strong></td>
            <td width="50"><strong>Unit price</strong></td>
            <td width="50"><strong>Total(Rs.)</strong></td>
        </tr>


        <tr>
            <td>${subscription.productVariant.product.name}<br/>
                <em style="font-size:0.9em; color:#666"><#list subscription.productVariant.productOptions as productOption>
            ${productOption.name} ${productOption.value}
          </#list></em>
            </td>
            <td>
          ${subscription.qty}
            </td>
            <td><span style="text-decoration: line-through;">${subscription.markedPriceAtSubscription}</span> ${subscription.subscriptionPrice} </td>
            <td> ${subscription.subscriptionPrice * subscription.qty} </td>
        </tr>
    </table>
</div>


<h3>Shipping Address & Customer details</h3>
<p style="margin-bottom:1em">
${subscription.address.name}<br/>
${subscription.address.line1}<br/>
<#if subscription.address.line2??>
${subscription.address.line2}<br/>
</#if>
${subscription.address.city} - ${subscription.address.pincode.pincode}<br/>
    Ph: ${subscription.address.phone}<br/>
</p>

<p style="margin-bottom:1em">We will inform you when ever your shipment is due and also drop you an email when even your subscription order is shipped.</p>

<p style="margin-bottom:1em"><strong>HealthKart.com</strong></p>
</body>
</html>