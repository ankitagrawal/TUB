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
                                        src="http://img.healthkart.com/email/order_cancel_user_new/main_banner.jpg" width="500"
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
                        Your Subscription for ID ${subscription.id} has been canceled, as per your instructions. <br/>
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

                        If you have any questions, you can chat online with our Customer Care or call them at 0124-4551616.<br/>
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
    <tr>
        <td bgcolor="#b8b8b8" style="border-top: solid #606060 2px">
            <table width="500" border="0" align="center" cellpadding="0" cellspacing="0">
                <tr>
                    <td height="70" colspan="3" align="left" valign="middle"><a href="http://www.healthkart.com/"><img
                            src="http://img.healthkart.com/email/logos/hk_logo_bw.jpg" alt="HealthKart.com logo" width="178"
                            height="28" border="0"/></a></td>
                    <td width="312" align="right" valign="middle" style="color:#606060; font-weight:bold; font-size:13px;"><a
                            href="http://www.healthkart.com/" style="color:#606060; text-decoration:none;">www.healthkart.com</a>
                    </td>
                </tr>
                <tr>
                    <td width="106" height="60" style="border-top: solid #929292 1px; color:#606060; font-size:13px;">Spread the
                        word:
                    </td>
                    <td width="39" align="left" valign="middle" style="border-top: solid #929292 1px"><a
                            href="http://www.facebook.com/healthkart"><img
                            src="http://img.healthkart.com/email/logos/facebook.png" alt="facebook" width="32" height="32"
                            border="0"/></a></td>
                    <td width="43" align="left" valign="middle" style="border-top: solid #929292 1px"><a
                            href="http://twitter.com/healthkart"><img src="http://img.healthkart.com/email/logos/twitter.png"
                                                                      alt="twitter" width="32" height="32" border="0"/></a></td>
                    <td align="right" valign="middle" style="border-top: solid #929292 1px; color:#606060; font-size:13px;">e: <a
                            href="mailto:info@healthkart.com" style="color:#606060">info@healthkart.com</a> &nbsp;| &nbsp;t: +91
                        124-4551616
                    </td>
                </tr>
            </table>
        </td>
    </tr>
    <tr>
        <td align="center" valign="middle"
            style="border-top: solid #FFFFFF 2px; font-size:11px; text-align:center; color:#929292; padding:10px">

            Parsvanath Arcadia, 1 MG Road, Sector 14, Gurgaon, Haryana, INDIA<br/>
            &copy; 2011 HealthKart.com. All Rights Reserved.
        </td>
    </tr>
</table>
</body>
</html>
