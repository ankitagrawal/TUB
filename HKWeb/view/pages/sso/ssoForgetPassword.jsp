
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
    <s:messages/>
    <s:errors/>
    <div id="panel">
        <h1 class="login-header">Welcome to the Healthkart Network</h1>
        <div id="forgetbox">
            <s:form id="signin_form" method="post" event="login" beanclass="com.hk.web.action.core.user.SSOForgotPasswordAction">

                <s:text type="text" placeholder="enter your login email" name="email" id="email" tabindex="10" size="50" maxlength="50" value=""/>
                <div class="login_block">
                    <s:submit tabindex="30" name="reset" id="reset"  class="submit" value="Send Password"/>
                </div>

            </s:form>
        </div>
    </div>
    <div class="footer"> <img src="${httpPath}/images/hk_bar_SSO.jpg" alt="bottom_stripe" border="0" height="10" width="705"> </div>
</div>
</body>
</html>
