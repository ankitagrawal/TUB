Reset Password Instructions - HealthKart.com
<html>
<head>
    <title>Reset Password Instructions - HealthKart.com</title>
</head>
<body style="margin:0; padding:0; font-family:Arial, Helvetica, sans-serif; background-color:#FFFFFF;">
<table width="530" border="0" align="center" cellpadding="0" cellspacing="0">
    <tr>
        <td align="center" valign="top">
            <table width="500" border="0" cellspacing="0" cellpadding="0" align="center"
                   style="font-size:13px; padding-bottom: 10px; line-height:1.75em;">


                <tr>
                    <td height="20"></td>
                </tr>


                <tr>
                    <td>
                    <#include "headerBeta.ftl">
                    </td>
                </tr>

                <tr>
                    <td>
                        Hi <b>${user.name}</b>,
                    </td>
                </tr>
                <tr>

                    <td style="padding-bottom:1em; padding-top: 10px">Can't remember your password?</td>
                </tr>
                <tr>
                    <td style="padding-top: 10px">
                        It happens to many of us :)
                    </td>
                </tr>
                <tr>

                    <td style="padding-top: 10px">
                        Your Username (Same as your Email Id): <a href="#">${user.email}</a></td>
                </tr>
                <tr>
                    <td style="padding-top: 10px">
                        Please click the below link to set a new password:
                    </td>
                </tr>
                <tr>
                    <td style="padding-top: 10px"><a href="${link}" target="_blank">${link}</a></td>
                </tr>
                <tr>
                    <td style="padding-top: 10px">
                        This will ask for new password which you can then use to login.
                    </td>
                </tr>
                <tr>
                    <td style="padding-top: 10px">
                        Note: If you are not able to click on the link, you can paste the above address into your
                        browser.
                    </td>
                </tr>
                <tr>
                    <td style="padding-top: 10px">Happy Shopping!</td>
                </tr>

                <tr>
                    <td style="padding-top: 10px">
                        <strong>HealthKart.com</strong></td>
                </tr>
            </table>
        </td>
    <tr>
        <td style="border-top: solid #0000ff 1px;padding-top: 10px">
        <#include "footerBeta.ftl">
    </tr>
</table>

</body>
</html>