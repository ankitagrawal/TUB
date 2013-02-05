

<%@ include file="/includes/_taglibInclude.jsp"%>
<c:set var="httpPath" value="${pageContext.request.contextPath}" />
<s:useActionBean beanclass="com.hk.web.action.core.auth.SSOLoginAction" var="sla" />
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-type" content="text/html;charset=UTF-8"/>
    <title>Healthkart Login</title>
    <link type="text/css" rel="stylesheet" href="${httpPath}/css/single_login.css" />
</head>
<body>
<div id="container">
    <div id="tabbox">
        <img src="${httpPath}/images/hk_bar_SSO.jpg" alt="top_stripe" border="0" height="10" width="705">
    </div>
    <div class="logo_block">
        <a title="go to healthkart home" href="/"><img src="${httpPath}/images/hk_plus_logo.png" alt="Healthkart Logo" border="0" height="40" width="140"></a>
        <a title="go to healthkart home" href="/"><img src="${httpPath}/images/hk_logo.png" alt="Healthkart Plus Logo" border="0" height="25" width="140"></a>


    </div>
    <s:errors/>
    <div id="panel">
        <h1 class="login-header">Reset your password</h1>

        <div id="loginbox">
            <s:form beanclass="com.hk.web.action.core.user.SSOPasswordResetAction" method="post">
                <s:errors/>

                <s:errors/>
                <s:hidden name="token" value="${resetBean.token}"/>
                <div >Enter the new password:</div>
                <s:password name="password" />
                <div class="label">Confirm Password: </div>
                <div><s:password name="passwordConfirm"/></div>
                <br/>
                <div>
                    <s:submit name="reset" value="Change Password" class="submit"/>
                </div>
            </s:form>
        </div>
    </div>
    <div class="footer"> <img src="${httpPath}/images/hk_bar_SSO.jpg" alt="bottom_stripe" border="0" height="10" width="705"> </div>
</div>
</body>
</html>
