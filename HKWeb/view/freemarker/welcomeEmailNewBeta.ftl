Hi ${user.name}, welcome to HealthKart.com
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Welcome to HealthKart</title>
</head>

<body style="margin:0; padding:0; font-family:Arial, Helvetica, sans-serif; background-color:#FFFFFF;">
<table width="530" border="0" align="center" cellpadding="0" cellspacing="0">
<#include "headerBeta.ftl">

    <tbody style="font-size:13px; line-height:1.75em;">

    <tr>
        <th colspan="2" style=" padding-top:15px;"><a href="http://www.healthkart.com"><img
                src="http://img.healthkart.com/email/welcome_emailer_new/images/main_banner.jpg"
                width="500"
                height="148" alt="Welcome to HealthKart!" border="0"/></a></th>
    </tr>
    <tr>
        <td style="padding-top:35px;" colspan="2">
            Hi ${user.name},<br/>
            Thank you for registering with HealthKart.com!<br/>
            Please click on the following link to verify your email and activate your account.
        </td>
    </tr>

    <tr>
        <td style="padding-top:15px;" align="left" colspan="2" valign="top"><a href="${activationLink}"><img
                src="http://img.healthkart.com/email/welcome_emailer_new/images/activate_btn.jpg"
                alt="Activate Account" title="Activate Account" width="139" height="26" border="0"/></td>
    </tr>

    <tr>
        <td style="padding-top:15px;" colspan="2" align="left" valign="top">If clicking on the link does not work, cut
            and paste the following URL
            in your
            browser:<br/>

            <a href="#">${activationLink}</a></td>
    </tr>
    <tr>
        <td align="left" valign="top">&nbsp;</td>
    </tr>
    <tr>
        <td align="left" height="30px" valign="top" style="font-size:16px; font-weight:bold">Login Details
        </td>
    </tr>
    <tr>
        <td colspan="2" align="left" height="30px" valign="top">To login to your HealthKart account, use the following
            details
        </td>
    </tr>
    <tr colspan="2">
        <td align="left" height="30px" valign="middle" bgcolor="#ececec"
            style="border-bottom:solid 1px #FFFFFF; padding-left:10px">Username: <span
                style="color:#FF0000; font-weight:bold; ">${user.email}</span></td>
    </tr>
    <tr>
        <td align="left" valign="top">&nbsp;</td>
    </tr>
    <tr>
        <td style="padding-bottom:10px" colspan="2" align="left" valign="top">You can login to see your order history,
            track previous orders and see
            the
            status of currently placed orders.<br/>
            <br/>
            Happy Shopping!<br/>
            <br/>
            HealthKart.com
        </td>
    </tr>


    </tbody>
<#include "footerBeta.ftl">
</table>
</body>
</html>
