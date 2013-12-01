<%@ include file="/includes/_taglibInclude.jsp"%>
<c:set var="httpPath" value="${pageContext.request.contextPath}" />
<s:useActionBean beanclass="com.hk.web.action.core.user.SSOPasswordResetAction" var="resetBean" />
<!DOCTYPE HTML>
<html>
<head>
    <meta http-equiv="Content-type" content="text/html;charset=UTF-8"/>
    <title>HealthKart - Reset Password</title>
    <link type="text/css" rel="stylesheet" href="${httpPath}/css/single_login.css" />
</head>
<body>
<div id="container">
    <div class="logo_block">
        <img src="${httpPath}/images/hkp-logo.jpg" alt="HealthKart Plus Logo" border="0">
        <img src="${httpPath}/images/hk_logo.png" alt="HealthKart Logo" border="0">


    </div>
    <div id="panel">
        <h1 class="login-header">Reset your password</h1>

        <div id="loginbox">
            <s:form beanclass="com.hk.web.action.core.user.SSOPasswordResetAction" method="post">
                <div>
                <s:errors/>
                </div>
                <s:hidden name="token" value="${resetBean.token}"/>
                <div>Enter the new password:</div>
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
  <div class="clear"></div>
  <%--<div class="footer">
    <br/>
    <span class="bottom">
                <a class="terms" href="${pageContext.request.contextPath}/pages/termsAndConditions.jsp">Terms of
                  Use</a>
        | &copy; 2013 HealthKart.com</span>
  </div>--%>
</div>
</body>
</html>
