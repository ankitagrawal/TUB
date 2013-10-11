You requested to be notified when ${product.name} was back in stock
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Personal alerts that matter to us. We mean it!</title>
</head>

<body style="margin:0; padding:0; font-family:Arial, Helvetica, sans-serif; background-color:#FFFFFF;">
<table width="530" border="0" align="center" cellpadding="0" cellspacing="0">
<#include "/freemarker/headerBeta.ftl">
    <tbody style="font-size:13px; line-height:1.75em;">
    <tr>
        <td colspan="2" style="padding-top: 15px"><a href="http://www.healthkart.com"><img
                src="http://img.healthkart.com/email/notify_user_emailer_new/images/main_banner.jpg"
                width="500" height="148" alt="Personal alerts that matter to us. We mean it!"
                border="0"/></a></td>
    </tr>


    <tr>
        <td colspan="2" style="padding-top: 10px">Hi ${notifiedUser.name}!<br/>
            <br/>
            On your last visit, you clicked notify me for the following product(s):<br/>
            <strong> ${product.name} </strong>
            <br/>
            <br/>


            The products are back in stock. Yay!
        </td>
    </tr>

    <tr>
        <td height="15" style="padding-bottom: 15px;"><a
                href="www.healthkart.com/product/${product.slug}/${product.id}?utm_source=notifyme&utm_medium=email&utm_campaign=${product.id}-${currentDate}"><img
                src="http://img.healthkart.com/email/notify_user_emailer_new/images/shop_now.jpg"
                alt="Click here to shop now" width="157" height="26" border="0"/></a></td>
    </tr>

    <tr>
        <td style="padding-bottom: 15px;">Healthy Shopping!<br/>

            HealthKart.com <br/>

            (India&#39;s Premier eHealth Store)
        </td>
    </tr>
    </tbody>
<#include "/freemarker/footerBeta.ftl" >
</table>
</body>
</html>
