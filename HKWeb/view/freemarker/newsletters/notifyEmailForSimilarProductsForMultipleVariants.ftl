<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Talking 'bout the one that got away!</title>
</head>

<body style="margin:0; padding:0; font-family:Verdana, Geneva, sans-serif; background-color:#FFFFFF;">
<table width="695" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td align="center" valign="top">
            <table width="683" border="0" cellspacing="0" cellpadding="0" align="center"
                   style="font-size:13px; font-family:Verdana, Geneva, sans-serif">
                <tr>
                    <td valign="top">&nbsp;</td>
                </tr>
                <tr>
                    <td valign="top" align="center"><a href="http://www.healthkart.com" target="_blank"><img
                            src="images/healthkart.jpg"
                            border="0"
                            alt="HealthKart.com"
                            title="HealthKart.com"/></a>
                    </td>
                </tr>
                <tr>
                    <td valign="top">&nbsp;</td>
                </tr>
                <tr>
                    <td valign="top" align="center"><img src="images/nav.jpg" alt="HealthKart.com" usemap="#Map"
                                                         title="HealthKart.com"
                                                         border="0"/></td>
                </tr>
                <tr>
                    <td valign="top" height="33"></td>
                </tr>
                <tr>
                    <td align="center" valign="top">
                        <table width="337" border="0" cellspacing="0" cellpadding="0" align="center"
                               style="font-size:13px; font-family:Verdana, Geneva, sans-serif">
                            <tr>
                                <td align="left" valign="top"><img src="images/one-that-got-away.png" border="0"
                                                                   alt="HealthKart"/></td>
                            </tr>
                            <tr>
                                <td height="25"></td>
                            </tr>
                            <tr>
                                <td align="left" valign="top"
                                    style="font-family:Verdana, Geneva, sans-serif; font-size:21px; line-height:25px; color:#646464;">
                                    Talking 'bout
                                    the one<br/>
                                    that got away!
                                </td>
                            </tr>
                            <tr>
                                <td valign="top"
                                    style="font-family:Verdana, Geneva, sans-serif; font-size:12px; line-height:18px; color:#646464; padding:10px 0px 10px 0px;">
                                    <em>Waits</em></td>
                            </tr>
                            <tr>
                                <td align="left" valign="top"
                                    style="font-family:Verdana, Geneva, sans-serif; font-size:12px; line-height:18px; color:#646464;">
                                    <p>We ran out
                                        of 'product x'. Terrible, we know!<br/>
                                        We let you down, so let us pick you up.</p>

                                    <p>How about we introduce you to something similar. Something that will make up for
                                        'product x'. If you like
                                        them, we suggest you pick them up.</p>

                                    <p>&nbsp;</p><br/>
                                </td>
                            </tr>



                        <#list productNotifyList as  notify>
                            <tr>
                                <td height="25"
                                    style="border-top:0px; font-family:Verdana, Geneva, sans-serif; font-size:12px; line-height:18px; color:#646464;">
                                    <p>You searched for Product  ${notify.productVariant.product.name} , may we
                                        suggest</p></td>
                            </tr>
                        <tr>

                            <#assign  map =  similarProductMap >
                            <#assign similarProductList = map[notify.productVariant.product.id]>

                            <#list  similarProductList as  similarProduct>
                                <td valign="top">
                                    <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                        <tr>
                                            <td align="center" valign="middle"
                                                style="font-family:Verdana, Geneva, sans-serif; font-size:12px; line-height:18px; color:#646464; font-weight:bold">
                                            ${similarProduct.name}
                                            </td>
                                            <td align="center" valign="middle"
                                                style="font-family:Verdana, Geneva, sans-serif; font-size:12px; line-height:18px; color:#646464; font-weight:bold">
                                                <#assign priceMap =  productPriceMap>
                                                <#assign highestDiscountVariant = priceMap[similarProduct.id]>
                                                Rs. {highestDiscountVariant.hkPrice}
                                            </td>
                                            <td align="center" valign="top"
                                                style="font-family:Verdana, Geneva, sans-serif; font-size:12px; line-height:18px; color:#646464; font-weight:bold">

                                                <table align="center" border="0" cellspacing="0"
                                                       cellpadding="0">
                                                    <tr>
                                                        <td valign="middle"
                                                            style="color:#000; font-family:Verdana, Geneva, sans-serif; font-weight:bold;">
                                                            <a href="www.healthkart.com/product/${similarProduct.slug}/${similarProduct.id}?utm_source=notifyme_similar&utm_medium=email&utm_campaign=${similarProduct.id}-${currentDate}"
                                                               target="_blank"
                                                               style="font-weight:bold; color:#646464; padding:2px 10px 4px 10px; text-decoration:none; font-size:12px; display:block; border:1px solid #646464;">Buy
                                                                Now</a></td>
                                                    </tr>
                                                </table>

                                            </td>
                                        </tr>

                                    </table>
                                </td>
                            </tr>

                                <tr>
                                    <td height="25"></td>
                                </tr>
                            </#list>
                            <tr>
                                <td height="25"></td>
                            </tr>

                            <tr>
                                <td height="25" style="border-top:1px dashed #646464;">&nbsp;</td>
                            </tr>
                        </#list>

                        </table>
                    </td>
                </tr>
                <tr>
                    <td height="33"></td>
                </tr>
                <tr>
                    <td colspan="3" align="center" valign="top"
                        style="font-family:Verdana, Geneva, sans-serif; font-size:12px; line-height:18px; color:#646464;">
                        <img
                                src="images/built.jpg" border="0" alt="HealthKart"/>&nbsp; <a style="color:#646464">info@healthkart.com</a>
                        | +91 124 4551616 &nbsp;&nbsp;&nbsp;<img
                            src="http://img.healthkart.com/email/notify_user_emailer_new/images/built.jpg" border="0"
                            alt="HealthKart"/>&nbsp;
                        <a
                                href="https://www.facebook.com/healthkart" style="color:#646464; text-decoration:none;">Like
                            us on
                            facebook</a> &nbsp;&nbsp;&nbsp;<img
                            src="http://img.healthkart.com/email/notify_user_emailer_new/images/built.jpg" border="0"
                            alt="HealthKart"/>&nbsp;
                        <a
                                href="https://www.twitter.com/healthkart" style="color:#646464; text-decoration:none;">Tweet
                            about
                            us</a></td>
                </tr>
                <tr>
                    <td height="12" valign="top"></td>
                </tr>
                <tr>
                    <td colspan="3" align="center" valign="top"
                        style="font-family:Verdana, Geneva, sans-serif; font-size:11px; line-height:18px; color:#646464;">
                        Parsvanath
                        Arcadia, 1 MG Road, Sector 14, Gurgaon, Haryana, INDIA. © 2011 HealthKart.com. All Rights
                        Reserved.
                    </td>
                </tr>
                <tr>
                    <td height="12" valign="top"></td>
                </tr>
                <tr>
                    <td valign="top" align="center"><img
                            src="http://img.healthkart.com/email/notify_user_emailer_new/images/all-heart.jpg"
                            border="0"
                            alt="ESTD 2011 ALL HEART"/></td>
                </tr>
                <tr>
                    <td valign="top" height="33"></td>
                </tr>
            </table>


        </td>
    </tr>
</table>

<map name="Map" id="Map">
    <area shape="rect" coords="2,3,85,28" href="http://www.healthkart.com/sports-nutrition" target="_blank"/>
    <area shape="rect" coords="86,2,169,28" href="http://www.healthkart.com/health-nutrition" target="_blank"/>
    <area shape="rect" coords="172,2,259,28" href="http://www.healthkart.com/sports" target="_blank"/>
    <area shape="rect" coords="262,2,311,28" href="http://www.healthkart.com/diabetes" target="_blank"/>
    <area shape="rect" coords="312,2,386,28" href="http://www.healthkart.com/health-devices" target="_blank"/>
    <area shape="rect" coords="388,2,466,29" href="http://www.healthkart.com/home-living" target="_blank"/>
    <area shape="rect" coords="469,2,504,29" href="http://www.healthkart.com/eye" target="_blank"/>
    <area shape="rect" coords="507,2,576,28" href="http://www.healthkart.com/personal-care" target="_blank"/>
    <area shape="rect" coords="578,2,618,28" href="http://www.healthkart.com/beauty" target="_blank"/>
    <area shape="rect" coords="620,2,673,28" href="http://www.healthkart.com/parenting" target="_blank"/>
</map>
</body>
</html>
