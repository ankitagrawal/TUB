<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>You are our Ramesh Sippy!</title>
</head>

<body style="margin:0; padding:0; font-family: Arial, Helvetica, sans-serif; background-color:#FFFFFF;">
<table width="700" border="0" align="center" cellpadding="0" cellspacing="0">
<tr>
    <td align="center" valign="top"
        style="border-left:1px solid #b8b8b8;  border-right:1px solid #b8b8b8; border-top:1px solid #b8b8b8; border-bottom:none;">
        <table width="600" align="center" border="0" cellspacing="0" cellpadding="0">
            <tr>
                <td valign="top">
                    <table width="300" border="0" cellspacing="0" cellpadding="0" align="center"
                           style="font-size:13px; line-height:1.75em;">
                        <tr>
                            <td valign="top">&nbsp;</td>
                        </tr>
                        <tr>
                            <td align="center" valign="top"
                                style="font-family:Georgia, 'Times New Roman', Times, serif; font-size:27px; line-height:30px; font-weight:bold; color:#000000; padding-bottom:10px;">
                                Hey ${notifiedUser.name}
                            </td>
                        </tr>
                        <tr>
                            <td align="center" valign="top"
                                style="font-family:Georgia, 'Times New Roman', Times, serif; font-size:50px; line-height:48px; font-weight:bold; color:#000000;">
                                You asked for Jai, may we suggest a Veeru?
                            </td>
                        </tr>
                        <tr>
                            <td valign="top">&nbsp;</td>
                        </tr>
                        <tr>
                            <td valign="top">
                                <table width="300" align="center" bgcolor="#b7b7b7" border="0" cellspacing="0"
                                       cellpadding="0">
                                    <tr>
                                        <td height="1"></td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td align="center" valign="top" height="10"></td>
                        </tr>
                        <tr>
                            <td align="center" valign="top"
                                style="font-family:Georgia, 'Times New Roman', Times, serif; font-weight:bold; font-size:21px; line-height:24px; color:#000000;">
                                Even though Jai dies, the story still continues.
                            </td>
                        </tr>
                        <tr>
                            <td align="center" valign="top" height="10"></td>
                        </tr>
                        <tr>
                            <td valign="top">
                                <table width="300" align="center" bgcolor="#b7b7b7" border="0" cellspacing="0"
                                       cellpadding="0">
                                    <tr>
                                        <td height="1"></td>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                        <tr>
                            <td align="center">&nbsp;</td>
                        </tr>
                        <tr>
                            <td align="center" valign="top"
                                style="font-family:Verdana, Geneva, sans-serif; font-size:13px; line-height:20px; color:#000000;">
                                We noticed that you asked us to notify<br/>
                                you for Product ${product.name}. <br/>
                                <br/>
                                We are sorry to inform you that we don't have that product as of now. How about you
                                take a look at similar products!
                                <br/>
                                So, go through rest of the cast and complete your set, with the same qualities!
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td valign="top" height="25">&nbsp;</td>
            </tr>
            <tr>
                <td valign="top">
                    <table width="550" align="center" bgcolor="#b7b7b7" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                            <td height="1"></td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td height="25">&nbsp;</td>
            </tr>

            <tr>
                <td valign="top">
                    <table width="600" border="0" cellspacing="0" cellpadding="0" align="center"
                           style="font-size:13px; line-height:1.75em;">
                        <tr>
                            <td valign="top" width="200" align="center">
                                <table width="100%" border="0" cellspacing="0" cellpadding="0">
                                    <tr>
                                    <#list similarProductList as simProduct>
                                        <td align="center" valign="top" style="padding-bottom:7px;"><a
                                                href="www.healthkart.com/product/${simProduct.slug}/${simProduct.id}?utm_source=notifyme_similar&utm_medium=email&utm_campaign=${simProduct.id}-${currentDate}"
                                                target="_blank"><img
                                                src="http://img.healthkart.com/email/notify_user_emailer_new/images/Classic-Mosquito-Net.jpg"
                                                alt="${simProduct.name}" border="0"/></a></td>
                                    </#list>
                                    </tr>

                                    <tr>
                                    <#list similarProductList as simProduct>
                                        <td align="center" valign="top"
                                            style="color:#000000; font-family:Verdana, Geneva, sans-serif; font-size:15px; line-height:18px; font-weight:bold;">
                                            <a href="www.healthkart.com/product/${simProduct.slug}/${simProduct.id}?utm_source=notifyme_similar&utm_medium=email&utm_campaign=${simProduct.id}-${currentDate}"
                                               target="_blank"
                                               style="color:#000; text-decoration:none; outline:none;">${simProduct.name}</a></td>
                                    </#list>
                                    </tr>
                                    <tr>
                                    <#list similarProductList as simProduct>
                                        <td align="center" valign="top"
                                            style="font-size:15px; color:#000; font-weight:bold; padding-bottom:2px;">
                                            <span style="color:#888888; text-decoration:line-through; font-size:13px; font-weight:normal; padding-right:10px;">Rs 1,500</span>Rs
                                            1,350
                                        </td>
                                    </#list>
                                    </tr>
                                    <tr>
                                    <#list similarProductList as simProduct>
                                        <td valign="top" align="center">
                                            <table align="center" border="0" cellspacing="0" cellpadding="0">
                                                <tr>
                                                    <td valign="middle"
                                                        style="color:#000; font-family:Verdana, Geneva, sans-serif; font-weight:bold;">
                                                        <a href="www.healthkart.com/product/${simProduct.slug}/${simProduct.id}?utm_source=notifyme_similar&utm_medium=email&utm_campaign=${simProduct.id}-${currentDate}" target="_blank"
                                                           style="font-weight:bold; color:#000; padding:2px 10px 4px 10px; text-decoration:none; font-size:15px; display:block; border:1px solid #000;">Buy
                                                            Now</a></td>
                                                </tr>
                                            </table>
                                        </td>
                                    </#list>
                                    </tr>
                                </table>
                            </td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td valign="top" height="25">&nbsp;</td>
            </tr>
            <tr>
                <td valign="top">
                    <table width="550" align="center" bgcolor="#b7b7b7" border="0" cellspacing="0" cellpadding="0">
                        <tr>
                            <td height="1"></td>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td valign="top" align="center">&nbsp;</td>
            </tr>
            <tr>
                <td valign="top" align="center"
                    style="font-family:Verdana, Geneva, sans-serif; font-size:13px; color:#000000;">Stay healthy!
                </td>
            </tr>
            <tr>
                <td valign="top">&nbsp;</td>
            </tr>
            <tr>
                <td valign="top" align="center"><a href="http://www.healthkart.com" target="_blank"><img
                        src="images/healthkart.png" border="0" alt="healthkart.com"/></a></td>
            </tr>
            <tr>
                <td valign="top">&nbsp;</td>
            </tr>
        </table>
    </td>
</tr>
<tr>
    <td bgcolor="#b8b8b8" style="border-top:1px solid #acacac;">
        <table width="500" border="0" align="center" cellpadding="0" cellspacing="0">
            <tr>
                <td width="106" height="60" style="color:#606060; font-size:13px;">Spread the word:</td>
                <td width="39" align="left" valign="middle"><a href="http://www.facebook.com/healthkart"
                                                               target="_blank"><img src="images/facebook.png"
                                                                                    alt="Facebook" title="Facebook"
                                                                                    border="0"/></a></td>
                <td width="43" align="left" valign="middle"><a href="http://twitter.com/healthkart"><img
                        src="images/twitter.png" alt="Twitter" title="Twitter" border="0"/></a></td>
                <td width="312" align="right" valign="middle" style="color:#606060; font-size:13px;">e: <a
                        href="mailto:info@healthkart.com" title="info@healthkart.com" style="color:#606060">info@healthkart.com</a>
                    &nbsp;| &nbsp;t: +91 124 4616444
                </td>
            </tr>
        </table>
    </td>
</tr>
<tr>
    <td align="center" valign="middle"
        style="border-top: solid #FFFFFF 2px; font-size:11px; text-align:center; color:#929292; padding:10px; font-family:Tahoma, Geneva, sans-serif;">

        Parsvanath Arcadia, 1 MG Road, Sector 14, Gurgaon, Haryana, INDIA<br/>
        © 2013 <a style="color:#929292; text-decoration:none;">HealthKart.com.</a> All Rights Reserved.
    </td>
</tr>
</table>
</body>
</html>
