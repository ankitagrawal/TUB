Talking 'bout the one that got away!
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Talking 'bout the one that got away!</title>
</head>

<body style="margin:0; padding:0; font-family:Verdana, Geneva, sans-serif; background-color:#FFFFFF;">
<table width="100%" border="0" align="center" cellpadding="0" cellspacing="0">
<#include "../headerBeta.ftl">
    <tbody style="font-size:13px; line-height:1.75em;">

    <tr>
        <td align="left" valign="top"><img
                src="http://img.healthkart.com/email/notify_user_emailer_new/images/one-that-got-away.png"
                border="0" alt="HealthKart"/></td>
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
                of <strong> ${product.name}.</strong> Terrible, we know!<br/>
                We let you down, so let us pick you up.</p>

            <p>How about we introduce you to something similar. Something that will make up for
                <strong>${product.name}.</strong> If you like
                them, we suggest you pick them up.</p>

            <p>&nbsp;</p><br/>
        </td>
    </tr>

    <tr>
        <td height="25"
            style="border-top:0px; font-family:Verdana, Geneva, sans-serif; font-size:12px; line-height:18px; color:#646464;">
            <p>For Product <strong>${product.name}</strong> , may we suggest</p></td>
    </tr>


    <tr>
        <td valign="top">
            <table width="100%" border="0" cellspacing="0" cellpadding="0">
            <#list  similarProductList as  similarProduct>
                <tr>
                    <td align="left" valign="middle"
                        style="font-family:Verdana, Geneva, sans-serif; font-size:12px; line-height:18px; color:#646464; font-weight:bold">
                    ${similarProduct.name}
                    </td>

                    <td align="center" valign="middle"
                        style="font-family:Verdana, Geneva, sans-serif; font-size:12px; line-height:18px; color:#646464; font-weight:bold">
                        <#assign map =  productPriceMap >
                        <#assign highestDiscountVariant = map[similarProduct.id]>
                        Rs.${highestDiscountVariant.hkPrice}
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
                <tr>
                    <td height="25"></td>
                </tr>
            </#list>
            </table>
        </td>
    </tr>
    </tbody>
<#include "../footerBeta.ftl">
</table>
</body>
</html>
