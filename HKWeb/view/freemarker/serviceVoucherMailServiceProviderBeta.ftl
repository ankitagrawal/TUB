Order Confirmation for Order ID ${order.gatewayOrderId} placed at HealthKart
<html>
<head>
    <title>Order Confirmation for Order ID ${order.gatewayOrderId} placed at HealthKart</title>
</head>
<body>
<#include "header.ftl">
<p style="margin-bottom:1em">Hi ${lineItem.productVariant.product.manufacturer.name}!,</p>

<p style="margin-bottom:1em">Here are the order details for the order <strong> ${order.gatewayOrderId}</strong> placed on<strong> ${order.payment.createDate?string("MMM dd, yyyy hh:mm:ss aa")} at HealthKart.com</strong></p>

<div>
  <h3>Order Details</h3>
  <table cellpadding="5" cellspacing="0" border="1" style="font-size:12px;">
    <tr>
      <td width="150"><strong>Item</strong></td>
      <td width="50"><strong>Quantity</strong></td>
    </tr>

        <tr>
        <td>${lineItem.productVariant.product.name}<br/>
          <#if lineItem.productVariant.variantName??>
          ${lineItem.productVariant.variantName}: <br/>
          </#if>
          <em style="font-size:0.9em; color:#666"><#list lineItem.productVariant.productOptions as productOption>
            ${productOption.name} ${productOption.value}
              </#list></em>
        </td>
        <td>
          ${lineItem.qty}
        </td>
      </tr>
  </table>
</div>

<div>
  <h3>Other Details</h3>
  <table cellpadding="5" cellspacing="0" border="1" style="font-size:12px;">
    <#if order.payment.contactName??>
      <tr>
        <td>Contact Name</td>
        <td>
          ${payment.contactName}
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
      ${order.user.email}<br/>
      ${order.address.line1}<br/>
      <#if order.address.line2??>
        ${order.address.line2}<br/>
      </#if>
      ${order.address.city} - ${order.address.pincode.pincode}<br/>
      ${order.address.state} (India)<br/>
    Ph: ${order.address.phone}<br/>
  </p>


<p style="margin-bottom:1em"><strong>HealthKart.com</strong></p>
<#include "footer.ftl">
</body>
</html>