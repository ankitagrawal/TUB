Hi ${user.name}, welcome to HealthKart.com
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Welcome to HealthKart</title>
</head>

<body style="margin:0; padding:0; font-family:Arial, Helvetica, sans-serif; background-color:#FFFFFF;">
<table width="530" border="0" align="center" cellpadding="0" cellspacing="0">
    <thead width="500" align="left">
    <tr>
        <th width="210"><a href="http://www.healthkart.com"><img
                src="${hkLogo}" alt="HealthKart.com Logo" width="207"
                height="60" border="0"/></a></th>

        <th width="320" align="left"
            style="padding-left:20px;font-size:13px; font-weight:bold; color:#666666"><a
                href="http://www.healthkart.com" style="color:#666666; text-decoration:none">India&#39;s
            premier
            e-health store!</a></th>

    </tr>
    <tr>
        <th colspan="2" style=" padding-top:15px;"><a href="http://www.healthkart.com"><img
                src="http://img.healthkart.com/email/welcome_emailer_new/images/main_banner.jpg"
                width="500"
                height="148" alt="Welcome to HealthKart!" border="0"/></a></th>
    </tr>
    </thead>

    <tbody style="font-size:13px; line-height:1.75em;">

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
        <td colspan="2" align="left" valign="top">You can login to see your order history, track previous orders and see
            the
            status of currently placed orders.<br/>
            <br/>
            Happy Shopping!<br/>
            <br/>
            HealthKart.com
        </td>
    </tr>


    </tbody>

    <tfoot width="500" align="center">
    <tr>
        <td colspan="2" align="center" valign="top"
            style="padding-top:10px;border-top:1px solid #0000ff; font-family:Georgia ,'Times New Roman',Times,serif; font-size:11px; line-height:18px; color:#646464;">


            <a href="https://mail.google.com/mail/mailto:info@healthkart.com"
               style="color:#646464; text-decoration: none;">info@healthkart.com</a>
            | +91 124 4616444 |&nbsp;
            <a href="http://www.facebook.com/healthkart" style="color:#646464; text-decoration:none">Like us
                on facebook</a>
            |&nbsp;<a href="http://twitter.com/healthkart" style="color:#646464; text-decoration:none">Tweet
            about us</a>
        </td>

    </tr>
    <tr>
        <td colspan="2" align="center" valign="top"
            style="font-family:Georgia, 'Times New Roman', Times, serif; font-size:11px; line-height:18px; color:#646464;">
            <div style="margin-top: 10px;">
                Parsvanath Arcadia, 1 MG Road, Sector 14, Gurgaon, Haryana, INDIA. © 2013
    </tr>
    <tr>
        <td colspan="2" align="center"
            style="padding-bottom:10px; font-family:Georgia, 'Times New Roman', Times, serif; font-size:11px; line-height:18px; padding-top:10px;color:#646464;">
            <a style="color:#646464; text-decoration:none;">HealthKart.com</a>. All Rights Reserved.
        </td>

    </tr>
    <tr>
        <td colspan="2" valign="top" align="center" style="padding-top: 10px;"><img
                src="http://catalog.healthkart.com:8080/beta/assets/images/all-heart.jpg"
                border="0"
                alt="ESTD 2011 ALL HEART"></td>
    </tr>
    </tfoot>

</table>
</body>
</html>
