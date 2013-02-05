Subscription Cancellation for Subscription ID ${subscription.id}
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Your Subscription has been cancelled.</title>
</head>

<body style="margin:0; padding:0; font-family:Arial, Helvetica, sans-serif; background-color:#FFFFFF;">
<table width="530" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td align="center" valign="top">
            <table width="500" border="0" cellspacing="0" cellpadding="0" align="center"
                   style="font-size:13px; line-height:1.75em;">


                <tr>
                    <td height="20"></td>
                </tr>


                <tr>
                    <td>
                        <table width="500" border="0" cellpadding="0" cellspacing="0">
                            <tr>
                                <td width="222"><a href="http://www.healthkart.com"><img
                                        src="http://img.healthkart.com/email/logos/logo.png" alt="HealthKart.com Logo" width="207"
                                        height="30" border="0"/></a></td>
                                <td width="15" style="border-left: solid 1px #999999"></td>
                                <td width="245" align="left" style="font-size:13px; font-weight:bold; color:#666666"><a
                                        href="http://www.healthkart.com" style="color:#666666; text-decoration:none">India&#39;s premier
                                    e-health store!</a></td>
                                <td width="18">&nbsp;</td>
                            </tr>
                            <tr>
                                <td colspan="5" height="10"></td>
                            </tr>
                            <tr>
                                <td colspan="5"><a href="http://www.healthkart.com"><img
                                        src="http://img.healthkart.com/email/subscription/subscription_cancel_email.jpg" width="500"
                                        height="148" alt="Your subscription has been cancelled." border="0"/></a></td>
                            </tr>


                        </table>
                    </td>
                </tr>


                <tr>
                    <td align="left" height="15"></td>
                </tr>
                <tr>
                    <td width="579" valign="top">Hi ${subscription.user.name}! <br/>
                        <br/>
                        Your Subscription for ID ${subscription.id} has been cancelled. <br/>
                        <br/>

                        You had subscribed for:<br/>
                        <table style="font-size:12px;" cellpadding="5" cellspacing="0" border="1">
                            <tr>
                                <td><strong>Item</strong></td>
                                <td><strong>Quantity</strong></td>
                                <td><strong>Unit Price</strong></td>
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
                                <td>
                                ${subscription.subscriptionPrice}
                                </td>
                            </tr>
                        </table>
                        <br/>
                        <br/>

                        If you have any questions, you can chat online with our Customer Care or call them at 0124-4616444.<br/>
                        <br/>


                        Healthy Shopping!<br/>

                        HealthKart.com <br/>

                        (India&#39;s Premier eHealth Store)
                    </td>
                </tr>
                <tr>
                    <td height="15"></td>
                </tr>
            </table>
        </td>
    </tr>
</table>

<br/>
<br/>
<br/>


<h3>Shipping Address & Customer details</h3>
<p style="margin-bottom:1em">
${subscription.address.name}<br/>
${subscription.address.line1}<br/>
<#if subscription.address.line2??>
${subscription.address.line2}<br/>
</#if>
${subscription.address.city} - ${subscription.address.pincode.pincode}<br/>
${subscription.address.state} (India)<br/>
    Ph: ${subscription.address.phone}<br/>
</p>

<p style="margin-bottom:1em"><strong>HealthKart.com</strong></p>
</body>
</html>
